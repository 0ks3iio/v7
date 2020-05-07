package net.zdsoft.basedata.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
@RequestMapping("/basedata/import/courseType")
@Controller
public class CourseTypeImportAction extends DataImportAction{
	@Autowired
	private CourseTypeService courseTypeService;
	
	@RequestMapping("/index")
	public String importIndex(String type, ModelMap map){
		// 业务名称
		map.put("businessName", "学科");
		// 导入URL 
		map.put("businessUrl", "/basedata/import/courseType/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/import/courseType/template?type="+type);
        map.put("exportErrorExcelUrl", "/basedata/import/courseType/exportErrorExcel");
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/basedata/import/courseType/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("type", type);
	    
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		map.put("type", type);
		return "/basedata/course/courseImportIndex.ftl";
	}
	@Override
	public String getObjectName() {
		return "courseTypeImport";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、改变选项后请重新上传模板</p>";
		return desc;
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> titleList = new ArrayList<>();
		titleList.add("*学科名称");
		titleList.add("*学科编号");
		return titleList;
	}

	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		System.out.println("开始导入数据");
		
		//获取上传数据 第一行行标是0
		List<String[]> courseTypeDatas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
		courseTypeDatas.remove(0);

		//错误数据序列号
        int sequence = 0;
        int totalSize =courseTypeDatas.size();
        
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(courseTypeDatas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize,0,0,errorDataList,"");
        }
		
        int successCount=0;
		List<CourseType> courseTypeList = new ArrayList<>();
		int i=0;
		for (String[] array : courseTypeDatas) {
			i++;
			String name = array[0];
 			String code = array[1];
			boolean bug = false;
			
			//必填验证
			String[] checkBlank = new String[]{name,code};
        	for (String str : checkBlank) {
				if(StringUtils.isBlank(str)){
					bug = true;
					break;
				}
			}
        	if(bug){
        		 String[] errorData=new String[4];
                 sequence++;
                 // errorData[0]=String.valueOf(sequence);
                 errorData[0]=i+"";
                 errorData[1]="第"+i+"行";
                 errorData[2]="";
                 errorData[3]="加*为必填数据";
                 errorDataList.add(errorData);
        		continue;
        	}
        	
        	//长度验证
        	for (String string : checkBlank) {
				if(string.getBytes().length > 100){
					bug = true;
					break;
				}
			}
        	if(bug){
        		 String[] errorData=new String[4];
                 sequence++;
                 // errorData[0]=String.valueOf(sequence);
                 errorData[0]=i+"";
                 errorData[1]="第"+i+"行";
                 errorData[2]="";
                 errorData[3]="名称、编号等不得超过50个字";
                 errorDataList.add(errorData);
        		continue;
        	}
        	//验证学科编号的 格式
        	if(!code.matches("^[0-9a-zA-Z]+$")){
        		String[] errorData=new String[4];
        		sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
        		errorData[1]="第"+i+"行";
        		errorData[2]="";
        		errorData[3]="学科编号只支持数字、字母";
        		errorDataList.add(errorData);
        		continue;
        	}
        	
        	//重复验证
        	CourseType checkMulti = new CourseType();
        	checkMulti.setCode(code);
        	checkMulti.setName(name);
        	String mess = courseTypeService.checkUnique(checkMulti);
        	//检查本次导入的数据之间有没有重复的
        	if(EntityUtils.getSet(courseTypeList, CourseType::getName).contains(name) ||
        			EntityUtils.getSet(courseTypeList, CourseType::getCode).contains(code)){
        		bug = true;
        		if(StringUtils.isEmpty(mess)){
        			mess = "科目名称或学科编号重复";
        		}
        	}
        	
        	if(StringUtils.isNotEmpty(mess) || bug){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]= "第"+i+"行";
                errorData[2]="";
                errorData[3]= mess;
                errorDataList.add(errorData);
        		continue;
        	}
        	
        	//验证通过 保存
        	checkMulti.setId(UuidUtils.generateUuid());
        	checkMulti.setIsDeleted(Constant.IS_DELETED_FALSE);
        	checkMulti.setType(BaseConstants.SUBJECT_TYPE_BX);
        	
        	courseTypeList.add(checkMulti);
        	successCount++;
		}

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();

            List<String> titleList = getRowTitleList();
            titleList.add("错误数据");
            titleList.add("错误原因");

            // HSSFCell----单元格样式
            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.LEFT);//水平
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            style.setWrapText(true);//自动换行

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            errorStyle.setAlignment(HorizontalAlignment.LEFT);
            errorStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            errorStyle.setWrapText(true);//自动换行
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int j=0;j<titleList.size();j++){
                HSSFCell cell = rowTitle.createCell(j);
                cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
                cell.setCellStyle(style);
            }

            for(int j=0;j<errorDataList.size();j++){
                HSSFRow row = sheet.createRow(j+1);
                String[] datasDetail = courseTypeDatas.get(Integer.parseInt(errorDataList.get(j)[0]) - 1);
                for (int k=0;k<titleList.size();k++) {
                    HSSFCell cell = row.createCell(k);
                    if (k<titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[k]));
                        cell.setCellStyle(style);
                    } else if (k==titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[2]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }

		if (CollectionUtils.isNotEmpty(courseTypeList)) {
	        	courseTypeService.saveAll(courseTypeList.toArray(new CourseType[0]));
		}
			
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
        return result;
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
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
        style.setAlignment(HorizontalAlignment.LEFT);//水平
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        //高度：3倍默认高度
        titleRow.setHeightInPoints(3*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        //注意事项
        titleCell.setCellValue(new HSSFRichTextString(getRemark()));
        titleCell.setCellStyle(style);
	    
        HSSFRow rowTitle = sheet.createRow(1);
        for(int j=0;j<size;j++){
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
		
		String fileName = "学科批量导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}


    @RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    /**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo){
		validRowStartNo = "1";
		if (StringUtils.isBlank(validRowStartNo)) {
		}try{
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
	 }
	
	/**
	 * 获取excel文件的备注
	 * @return
	 */
	private String getRemark(){
		String remark = "1.带 * 为必填\n"
				+ "2.学科编号只能是数字、字母。";
		return remark;
	}
}
