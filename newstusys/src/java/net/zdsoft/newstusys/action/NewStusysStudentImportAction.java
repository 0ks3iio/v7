package net.zdsoft.newstusys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.service.NewStusysStudentImportService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * 
 * @author weixh
 * @since 2018年2月28日 上午10:07:58
 */
@Controller
@RequestMapping("/newstusys/sch/student/studentImport")
public class NewStusysStudentImportAction extends DataImportAction {
	private Logger logger = Logger.getLogger(NewStusysStudentImportAction.class);

	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private NewStusysStudentImportService newStusysStudentImportService;
	
	private int stuUpdate = 0;
	
	@RequestMapping("/index")
	public String importIndex(HttpServletRequest req, ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
	    int update = NumberUtils.toInt(req.getParameter("update"));
	    stuUpdate = update;
		// 业务名称
		if(update == 0) {
			map.put("businessName", "学生信息");
	    } else {
	    	map.put("businessName", "学生信息更新");
	    }
		// 导入URL 
		map.put("businessUrl", "/newstusys/sch/student/studentImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/newstusys/sch/student/studentImport/template?update="+update);
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/newstusys/sch/student/studentImport/validate?update="+update);
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("update", update);
	    map.put("importParams",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/newstusys/studentImport/studentImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "student_import";
	}

	@Override
	public String getDescription() {
		StringBuilder desc = new StringBuilder("<h4 class='box-graybg-title'>说明</h4>");
		int col = 1;
		desc.append("<p>"+(col++)+"、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>");
		desc.append("<p>"+(col++)+"、改变选项后请重新上传模板，同时不要随意修改模板中内容，否则容易导致上传文件与模板的不匹配</p>");
		if(stuUpdate == 0) {
			desc.append("<p>"+(col++)+"、导入学生信息，增加系统里没有的学生信息，也可以更新系统里已有的学生信息；以列【证件编号、姓名】作为更新学生的依据</p>");
		} else {
			desc.append("<p style='color:red;'>"+(col++)+"、更新导入时仅更新系统里已有的学生信息（列【证件编号、姓名】作为更新学生的依据），不增加学生；<br>仅姓名和证件编号是必填项，如导入文件中有班级列，则班级也需必填，没有则为选填，其他都为选填项；不需要更新的列请删除，否则会将学生中对应的数据置空</p>");
		}
		desc.append("<p>"+(col++)+"、请仔细查看模板中的提示，请严格根据提示填写数据</p>");
		return desc.toString();
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> titles = new ArrayList<String>();
		if(stuUpdate == 0) {
			titles.add("*班级");
		} else {
			titles.add("班级");
		}
		titles.add("*姓名");
		titles.add("曾用名");
		titles.add("性别");
		titles.add("出生日期");
		titles.add("民族");
		titles.add("政治面貌");
		titles.add("国籍");
		titles.add("港澳台侨外");
		titles.add("证件类型");
		titles.add("*证件编号");// 10
		titles.add("学号");// 11
		titles.add("班内编号");// 12
		titles.add("学生类别");
		titles.add("原毕业学校");
		titles.add("入学年月");
		titles.add("一卡通卡号");
		titles.add("户籍省县");
		titles.add("户籍镇/街");
		titles.add("籍贯");
		titles.add("家庭住址");
		titles.add("家庭邮编");
		titles.add("家庭电话");
		titles.add("监护人");
		titles.add("监护人与学生关系");
		titles.add("监护人联系电话");
		titles.add("父亲姓名");
		titles.add("父亲政治面貌");
		titles.add("父亲文化程度");
		titles.add("父亲单位");
		titles.add("父亲职务");
		titles.add("父亲手机号");
		titles.add("父亲证件类型");
		titles.add("父亲证件号");
		titles.add("父亲国籍");
		titles.add("父亲出生日期");
		titles.add("母亲姓名");
		titles.add("母亲政治面貌");//37
		titles.add("母亲文化程度");
		titles.add("母亲单位");
		titles.add("母亲职务");
		titles.add("母亲手机号");	
		titles.add("母亲证件类型");//42
		titles.add("母亲证件号");
		titles.add("母亲国籍");
		titles.add("母亲出生日期");
		titles.add("特长爱好");
		titles.add("获奖情况");
		titles.add("简历");
		return titles;
	}
	
	/**
	 * 下载对照文件
	 * @return
	 */
	@RequestMapping("/remarkDoc")
	public void downloadRemarkDoc(HttpServletRequest request, HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		List<Region> regions = Region.dt(regionRemoteService.findByType(Region.TYPE_1));
		// 写入文件
		String[] titles = {"序号", "地区", "区划码"};
		HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 10);// 字体大小
        headfont.setBold(true);// 加粗
        style.setFont(headfont);
		HSSFSheet sheet = workbook.createSheet("行政区划");
		sheet.setColumnWidth(1, 30 * 256);// 内容列宽度
		//标题行
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints((1.5f)*sheet.getDefaultRowHeightInPoints());
        for(int j=0;j<titles.length;j++) {
        	HSSFCell cell = titleRow.createCell(j);
        	cell.setCellValue(new HSSFRichTextString(titles[j]));
        	cell.setCellStyle(style);
        }
        // 内容行
        int rowInt = 1;
        for(Region re : regions) {
        	if("000000".equals(re.getFullCode())) {
        		continue;
        	}
        	HSSFRow conRow = sheet.createRow(rowInt);
        	HSSFCell cell = conRow.createCell(0);
        	cell.setCellValue(new HSSFRichTextString(rowInt+""));
        	HSSFCell cell1 = conRow.createCell(1);
        	cell1.setCellValue(new HSSFRichTextString(re.getFullName()));
        	HSSFCell cell2 = conRow.createCell(2);
        	cell2.setCellValue(new HSSFRichTextString(re.getFullCode()));
        	rowInt++;
        }
		String fileName = "学生信息行政区划导入对照文件";
		ExportUtils.outputData(workbook, fileName, response);
	}

	/**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo, HttpServletRequest req){
		logger.info("模板校验中......");
		int update = NumberUtils.toInt(req.getParameter("update"));
		if(update == 1) {
			return "";
		}
		validRowStartNo = "1";
		try{
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	/**
	 * 导入数据
	 */
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("学生数据处理中......");
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		int update = performance.getIntValue("update");
		String msg;
		if (update == 1) {
			msg = newStusysStudentImportService.saveUpdateDatas(unitId, 2, filePath);
		} else {
			//获取上传数据 第一行行标是0
			List<String[]> rowDatas = ExcelUtils.readExcelByRow(filePath, 2, getRowTitleList().size());
			msg = newStusysStudentImportService.saveImportStuDatas(unitId, rowDatas);
		}
		logger.info("导入结束......");
		return msg;
	}
	
	/**
	 * 下载导入模板
	 */
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
		stuUpdate = NumberUtils.toInt(request.getParameter("update"));
		List<String> titleList = getRowTitleList();//表头
		Map<String,List<Map<String,String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
		sheetName2Map.put(getObjectName(),new ArrayList<Map<String, String>>());
		Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
	    titleMap.put(getObjectName(),titleList);
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    int size = titleList.size();
	    
	    //sheet 合并单元格
	    CellRangeAddress car = new CellRangeAddress(0,0,0,size-1);
        sheet.addMergedRegion(car);
        //列宽
        for(int i=0;i<size;i++){
        	 sheet.setColumnWidth(i, 20 * 256);
        }
	    
	    // HSSFCell----单元格样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        //高度：行数倍的默认高度
        titleRow.setHeightInPoints(7*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        //注意事项
        String remark = getRemark();
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(style);
	    
        HSSFRow rowTitle = sheet.createRow(1);
        for(int j=0;j<size;j++){
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
		
        setDataValidation(titleList.toArray(new String[0]), 2, size, workbook, sheet);
        
		String fileName;
		if (stuUpdate == 0) {
			fileName = "学生信息导入";
		} else {
			fileName = "学生信息更新导入";
		}
		ExportUtils.outputData(workbook, fileName, response);
	}
	
	private String[] letters = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	/**
	 * 
	 * @param wb
	 * @param textList 微代码内容
	 * @param firstRow 开始行
	 * @param firstCol 开始列
	 * @param endRow 结束行
	 * @param endCol 结束列
	 * @param mcodeNum 第几列
	 * @return
	 */
	private HSSFDataValidation createDataValidation(HSSFWorkbook wb, String[] textList, int firstRow, int firstCol,
            int endRow, int endCol, int mcodeNum) {
        HSSFSheet hidden = wb.getSheet("hidden");
    	if (hidden==null){
        	hidden = wb.createSheet("hidden");
        }
    	for (int i = 0; i < textList.length; i++) {
			String name = textList[i];
			HSSFRow row = hidden.getRow(i);
			if (row == null){
				row = hidden.createRow(i);
			}
			HSSFCell cell = row.createCell(mcodeNum);
			cell.setCellValue(name);
		}
    	
    	// 加载下拉列表内容
//    	char col = (char)('A' + mcodeNum);
    	//2014-12-17 modify by like 
    	//功能完善--当微代码的数量超过26个时  理论上支持（26+1）*26个
    	String col = "";
    	if(mcodeNum < 26){
    		col = String.valueOf((char)('A' + mcodeNum));
    	}else{
    		int m = mcodeNum%26;
    		int n = mcodeNum/26;
    		col = letters[n-1]+String.valueOf((char)('A' + m));
    	}
    	DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden!$" + col + "$1:$" + col + "$" + textList.length);
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);

        // 数据有效性对象
        HSSFDataValidation data_validation = new HSSFDataValidation(cellRangeAddressList, constraint);
        return data_validation;
    }
	
	/**
     * 设置数据有效性
     * @param cols
     */
    private void setDataValidation(String[] cols, int beginRow, int endRow, 
    		HSSFWorkbook workbook, HSSFSheet sheet) {
    	Map<String, String[]> defineMcodeMap = getColMcodeMap();
    	
    	if (MapUtils.isEmpty(defineMcodeMap))
            return;

        if (endRow - beginRow < 500) {
            endRow = beginRow + 500;
        }
        
        //微代码数量
        int mcodeNum = 0;
        
        for (int j = 0; j < cols.length; j++) {
        	String[] texts=null;
        	if(StringUtils.isNotBlank(cols[j]))
        		texts = defineMcodeMap.get(cols[j]);
            if (null != texts && texts.length > 0) {
                // 指定第几列默认num行,都设置为含有下拉列表的格式，加入数据有效性到当前sheet对象
                try {
                	mcodeNum ++;
                    HSSFDataValidation datavalidation = createDataValidation(workbook, texts, beginRow, j,
                            endRow - 1, j, mcodeNum-1);
                    sheet.addValidationData(datavalidation);
                } catch (Exception e) {
                	e.printStackTrace();
                }

            }
        }
        
       //隐藏微代码EXCEL
       if (mcodeNum > 0){
        	workbook.setSheetHidden(1, true);
        }
    }
	
    public Map<String, String[]> getColMcodeMap(){
		Map<String, String[]> dmcodeMap = new HashMap<String, String[]>();
		String[] mcodes = {"DM-XB","DM-MZ", "DM-ZZMM", "DM-COUNTRY", "DM-GATQ", 
				"DM-SFZJLX", "DM-XSLB", "DM-GX", "DM-WHCD"};
		List<McodeDetail> mds = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodes), new TR<List<McodeDetail>>() {} );
		// <code, [内容,值]>
		Map<String, List<String>> mcodeMap = new HashMap<String, List<String>>();
		for(McodeDetail md : mds) {
			List<String> ml = mcodeMap.get(md.getMcodeId());
			if(ml == null) {
				ml = new ArrayList<String>();
			}
			ml.add(md.getMcodeContent());
			mcodeMap.put(md.getMcodeId(), ml);
		}
		String[] cols = {"性别", "民族", "政治面貌","国籍", "港澳台侨外", "证件类型", "学生类别", 
				"监护人与学生关系", "父亲政治面貌", "父亲文化程度", "父亲证件类型", "父亲国籍", "母亲政治面貌", "母亲文化程度", "母亲证件类型" , "母亲国籍"};
		String[] colm = {"DM-XB","DM-MZ", "DM-ZZMM", "DM-COUNTRY", "DM-GATQ", 
				"DM-SFZJLX", "DM-XSLB", "DM-GX", "DM-ZZMM", "DM-WHCD","DM-SFZJLX", "DM-COUNTRY", "DM-ZZMM", "DM-WHCD", "DM-SFZJLX", "DM-COUNTRY"};
		for(int i=0;i<cols.length;i++) {
			String col = cols[i];
			List<String> ml = mcodeMap.get(colm[i]);
			if(CollectionUtils.isNotEmpty(ml)) {
				dmcodeMap.put(col, ml.toArray(new String[0]));
			}
		}
		return dmcodeMap;
	}
    
	/**
	 * 导入文件中的填写注意
	 * @return
	 */
	private String getRemark(){
		int col = 1;
		StringBuilder remark = new StringBuilder("填写注意：\n");
		if(stuUpdate == 0) {
			remark.append((col++)+".带 * 为必填\n");
		} else {
			remark.append((col++)+".带 * 为必填，更新导入时仅更新系统里已有的学生信息（列【证件编号、姓名】作为更新学生的依据），不增加学生\n")
				.append((col++)+".仅姓名和证件编号是必填项，如导入文件中有班级列，则班级也需必填，没有则为选填，其他都为选填项；不需要更新的列请删除，否则会将学生中对应的数据置空\n");
		}
		remark.append((col++)+".出生日期（类型支持三种格式(年月日、年-月-日、年/月/日)，其中【年月日】这种格式必须满足8位数，即月份和日期不足两位的，前面要补0，如20060102）；入学年月的以此类推\n")
				.append((col++)+".户籍省县、籍贯填写时，要按照行政区划对照文件填写区划码；对照文件可在导入页面下载\n")
				.append((col++)+".证件编号（即相当于身份证号）\n")
				.append((col++)+".家长信息（注意：以家长姓名为主，如果姓名没有维护，则家长信息无效，不存入系统；监护人维护时，监护人与学生关系也要维护，监护人与学生关系这里如果填写的是父亲或者母亲，则以下面父亲母亲的信息为准并且可以不用填写监护人和监护人联系电话）\n")
				.append((col++)+".简历（XX年XX月至XX年XX月，XX学校；可添加多条，多条中间以\"；\"分隔）");
		return remark.toString();
	}
	
}
