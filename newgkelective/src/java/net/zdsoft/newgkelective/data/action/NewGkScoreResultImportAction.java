package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;
@Controller
@RequestMapping("/newgkelective")
public class NewGkScoreResultImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(NewGkScoreResultImportAction.class);
	@Autowired
	private NewGkScoreResultService newGkScoreResultService;
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	
	@RequestMapping("/newGkScoreResult/import/main")
	public String execute(String chioceId,String gradeId, String showReturn, ModelMap map) {
		// 业务名称
		map.put("businessName", "成绩");
		// 导入URL 
		map.put("businessUrl", "/newgkelective/newGkScoreResult/import");
		
		map.put("templateDownloadUrl", "/newgkelective/newGkScoreResult/template");
        map.put("exportErrorExcelUrl", "/newgkelective/newGkScoreResult/exportErrorExcel");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());			
		map.put("businessKey", UuidUtils.generateUuid());
		
		List<NewGkReferScore> newGkReferScoreList =  newGkReferScoreService.findListByGradeId(getLoginInfo().getUnitId(), gradeId, false, false);
		if(CollectionUtils.isNotEmpty(newGkReferScoreList)){
			map.put("showpage", "1");//返回参考分
		}else{
			map.put("showpage", "0");//返回进来页面
		}
		
		map.put("gradeId", gradeId);
		map.put("chioceId", chioceId);
		//模板校验
	 	map.put("validateUrl", "/newgkelective/newGkScoreResult/validate");
	 	map.put("validRowStartNo", "2");
	 	map.put("showReturn",showReturn);
		return "/newgkelective/newGkScoreResult/newgkelectiveImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "newgkelectiveImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+"<p>1、请不要修改模板中的列名</p>"
				+ "<p>2、导入文件中请确认数据是否正确</p>"
				+ "<p>3、各科成绩请输入数字型成绩，要求格式(最多3位整数，2位小数)</p>"
				+ "<p>4、如果成绩可以为空，代表不录入该科成绩</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		List<Course> coursesList =SUtils.dt(courseRemoteService.findByCodes73YSY(getLoginInfo().getUnitId()),new TR<List<Course>>() {});
		//下载文件表头
		tis.add("学号");
		tis.add("姓名");
		for (Course course : coursesList) {
			tis.add(course.getSubjectName());
		}
		return tis;
	}

	@Override
	@RequestMapping("/newGkScoreResult/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject jsStr = JSONObject.parseObject(params); 
		String gradeId = jsStr.getString("gradeId");
		//表头datas.get(0)
		List<String[]> allDatas = ExcelUtils.readExcelByRow(filePath, 0, getRowTitleList().size());
		String[] headTitle;
        if (allDatas.get(1)[1] == "" || allDatas.get(1)[1] == null) {
            headTitle = allDatas.get(2);
            allDatas.remove(0);
        } else {
            headTitle = allDatas.get(1);
        }
        List<String> headList = Arrays.asList(Arrays.copyOf(headTitle, headTitle.length));
		Collections.sort(headList);
		List<String> titleList = getRowTitleList();
		Collections.sort(titleList);
		boolean flag = false;
		if(!headList.equals(titleList)){
			flag = true;
		}
		List<String[]> datas=new ArrayList<String[]>();
		datas.addAll(allDatas);
		String jsonMsg = newGkScoreResultService.doImport(getLoginInfo().getUnitId(), gradeId, headTitle, datas, filePath, flag);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/newGkScoreResult/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		List<String> titleList = getRowTitleList();
		
		//标题行固定
		sheet.createFreezePane(0, 3);
		
		CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size()-1);
        sheet.addMergedRegion(car);
		
		// 注意事项样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);//水平居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style2.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style2.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow remarkRow = sheet.createRow(0);
        //高度：4倍默认高度
        remarkRow.setHeightInPoints(4*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(style);
        //注意事项
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(style);
        
        CellRangeAddress cra = new CellRangeAddress(1,1,0,titleList.size()-1);
        sheet.addMergedRegion(cra);
        HSSFRow titleRow = sheet.createRow(1);
        titleRow.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(new HSSFRichTextString("请在此填写参考成绩名称,不可为空"));
        titleCell.setCellStyle(style2);
        
        HSSFRow rowTitle = sheet.createRow(2);
        for(int i=0;i<titleList.size();i++){
        	sheet.setColumnWidth(i, 10 * 256);//列宽
        	HSSFCell cell = rowTitle.createCell(i);
        	cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }
		
		String fileName = "成绩导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}

	@RequestMapping("/newGkScoreResult/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {

		String remark = "填写注意：\n" 
						+ "1.请在首行填写参考成绩名称，如下所示;"; 
		return remark;
	}
	
	@RequestMapping("/newGkScoreResult/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "2";
		}
		try {
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
            if (datas.get(0)[1] == "" || datas.get(0)[1] == null) {
                datas.remove(0);
            }
            return templateValidate(datas, getRowTitleList());
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
}
