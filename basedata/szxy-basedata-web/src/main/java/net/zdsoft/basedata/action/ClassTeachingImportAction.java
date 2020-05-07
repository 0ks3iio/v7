package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
//import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/basedata/courseopen/gradeImport")
public class ClassTeachingImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(ClassTeachingImportAction.class);
	@Autowired
	private GradeService gradeService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	@RequestMapping("/index/page")
	public String importIndex(ModelMap map,String acadyear,String semester){
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "年级课程开设导入");
		// 导入URL 
		map.put("businessUrl", "/basedata/courseopen/gradeImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/courseopen/gradeImport/template");
        map.put("exportErrorExcelUrl", "/basedata/courseopen/gradeImport/exportErrorExcel");
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/basedata/courseopen/gradeImport/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("acadyear", acadyear);
	    obj.put("semester", semester);
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/basedata/classTeaching/gradeCourseOpenImport.ftl";
	}
	@Override
	public String getObjectName() {
		return "classTeachingImport";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>2、改变选项后请重新上传模板</p>";
		return desc;
		
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> rowTitle=new ArrayList<String>();
		rowTitle.add("*年级");
		rowTitle.add("*课程名称");
		return rowTitle;
	}

	/**
	 * 导入数据
	 */
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		//获取上传数据 第一行行标是0
		List<String[]> rowDatas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
		rowDatas.remove(0);
		//错误数据序列号
        int sequence = 0;
        int totalSize =rowDatas.size();
		List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(rowDatas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
        }
		if(StringUtils.isBlank(unitId) || StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="参数丢失，无法保存";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
		List<Grade> gradeList = gradeService.findByUnitId(unitId);
		Map<String, Grade> gradeMap = EntityUtils.getMap(gradeList, Grade::getId);
		if(CollectionUtils.isEmpty(gradeList)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位下不存在正常状态的年级";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
		
		Set<String> sameGradeNames=new HashSet<String>();//防止一个学校一个年级名称对应多个年级
		Map<String,String> gradeIdByGradeName=new HashMap<String,String>();//如果名称重复 这边取第一个访问的值
		for(Grade g:gradeList){
			if(gradeIdByGradeName.containsKey(g.getGradeName())){
				//重复年级
				sameGradeNames.add(g.getGradeName());
				continue;
			}
			gradeIdByGradeName.put(g.getGradeName(), g.getId());
		}
		
//		List<String> unitIds = new ArrayList<String>();
//		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(unitId),Unit.class);
//		String topUnitId = unit.getId();
//		unitIds.add(unitId);
//		unitIds.add(topUnitId);
		List<Course> courseList = SUtils.dt(courseRemoteService.getListByCondition(unitId, null, null, null, null, Constant.IS_TRUE, null),new TR<List<Course>>() {});
//		List<Course> courseList = SUtils.dt(courseRemoteService.findBy(unitIds.toArray(new String[unitIds.size()]), null, null, null, null, Constant.IS_TRUE, null),new TR<List<Course>>() {});
		if(CollectionUtils.isEmpty(courseList)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位课程库中没有课程";
            errorDataList.add(errorData);
            return result(rowDatas.size(),0,0,errorDataList,"");
		}
//		Map<String, Course> subjectBySubjectName = EntityUtils.getMap(courseList,Course::getSubjectName);
		
		Map<String, List<Course>> subjectsBySubjectName=EntityUtils.getListMap(courseList, Course::getSubjectName, e->e);
		
		List<GradeTeaching> gradeTeachingList2 = gradeTeachingService.findGradeTeachingList(acadyear, semester,null, unitId, Constant.IS_DELETED_FALSE, null, null);
		Map<String,List<String>> gradeTeachingMap = new HashMap<String, List<String>>();
		for (GradeTeaching gradeTeaching : gradeTeachingList2) {
			if(!gradeTeachingMap.containsKey(gradeTeaching.getGradeId())){
				gradeTeachingMap.put(gradeTeaching.getGradeId(), new ArrayList<String>());
			}
			gradeTeachingMap.get(gradeTeaching.getGradeId()).add(gradeTeaching.getSubjectId());
		}
		
		int successCount=0;
		int i=0;
		//同一张表格同一年级不能重复导入开设课程
		Set<String> sameNames=new HashSet<String>();
		List<GradeTeaching> gradeTeachingList = new ArrayList<GradeTeaching>();
		
		Map<String, Course> subjectIdToCourse=new HashMap<String, Course>();
		
		for(String[] arr:rowDatas){
			i++;
			
			String gradeName=arr[0]==null?null:arr[0].trim();
			String subjectName=arr[1]==null?null:arr[1].trim();
			//判断不为空的参数
        	if(StringUtils.isBlank(gradeName)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="年级名称不能为空";
                errorDataList.add(errorData);
                continue;
        	}
        	if(StringUtils.isBlank(subjectName)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="课程名称不能为空";
                errorDataList.add(errorData);
                continue;
        	}
        	
        	
        	if(sameGradeNames.contains(gradeName)){
    			String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=gradeName;
                errorData[3]="年级中"+gradeName+"未找到唯一年级";
                errorDataList.add(errorData);
                continue;
    		}
    		if(!gradeIdByGradeName.containsKey(gradeName)){
    			String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=gradeName;
                errorData[3]="年级中"+gradeName+"未找到对应年级";
                errorDataList.add(errorData);
                continue;
    		}
    		String gradeId = gradeIdByGradeName.get(gradeName);
    		Grade grade = gradeMap.get(gradeId);
    		if(grade==null) {
    			String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=gradeName;
                errorData[3]="年级中"+gradeName+"未找到对应年级";
                errorDataList.add(errorData);
                continue;
    		}
    		if(sameNames.contains(gradeName+subjectName)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=subjectName;
                errorData[3]="在之前的数据中"+gradeName+"年级已经存在"+subjectName+"科目";
                errorDataList.add(errorData);
                continue;
        	}
    		List<Course> cList = subjectsBySubjectName.get(subjectName);
    		if(CollectionUtils.isEmpty(cList)) {
    			String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=gradeName;
                errorData[3]="课程名称中"+subjectName+"未找到对应科目";
                errorDataList.add(errorData);
                continue;
    			
    		}
    		List<Course> chooseList=new ArrayList<>();
    		for(Course c:cList) {
    			if(StringUtils.isBlank(c.getSection()) || (c.getSection().indexOf(grade.getSection().toString())>-1)) {
    				chooseList.add(c);
    			}
    		}
    		if(CollectionUtils.isEmpty(chooseList)) {
    			String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=subjectName;
                errorData[3]=gradeName+"年级所属学段中未开设该科目";
                errorDataList.add(errorData);
                continue;
    		}
    		if(chooseList.size()>1) {
    			String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=subjectName;
                errorData[3]=gradeName+"年级所属学段中找到对应的科目,不唯一";
                errorDataList.add(errorData);
                continue;
    		}
    		Course course = chooseList.get(0);
    		if(gradeTeachingMap.containsKey(gradeId)&&gradeTeachingMap.get(gradeId).contains(course.getId())){
    			String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=subjectName;
                errorData[3]=gradeName+"年级中已开设该科目";
                errorDataList.add(errorData);
                continue;
    		}
//    		if(StringUtils.isNotBlank(course.getSection())){
//    			if(course.getSection().indexOf(String.valueOf(gradeMap.get(gradeId).getSection()))<0){
//        			String[] errorData=new String[4];
//                    sequence++;
//                    errorData[0]=String.valueOf(sequence);
//                    errorData[1]="第"+i+"行";
//                    errorData[2]=subjectName;
//                    errorData[3]=gradeName+"年级所属学段中未开设该科目";
//                    errorDataList.add(errorData);
//                    continue;
//        		}
//    		}
    		
    		GradeTeaching gradeTeaching = new GradeTeaching();
			gradeTeaching.setId(UuidUtils.generateUuid());
			gradeTeaching.setAcadyear(acadyear);
			gradeTeaching.setSemester(semester);
			gradeTeaching.setGradeId(gradeId);
			gradeTeaching.setCreationTime(new Date());
			gradeTeaching.setIsDeleted(Constant.IS_FALSE);
			gradeTeaching.setModifyTime(new Date());
			gradeTeaching.setSubjectId(course.getId());
			gradeTeaching.setSubjectType(course.getType());
			gradeTeaching.setUnitId(unitId);
			if(BaseConstants.SUBJECT_TYPE_XX.equals(course.getType())) {
				gradeTeaching.setIsTeaCls(Constant.IS_TRUE);
			}else{
				gradeTeaching.setIsTeaCls(Constant.IS_FALSE);
			}
			gradeTeachingList.add(gradeTeaching);
			
    		sameNames.add(gradeName+subjectName);
    		subjectIdToCourse.put(course.getId(), course);
    		successCount++;
		}//for结尾

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
                String[] datasDetail = rowDatas.get(Integer.parseInt(errorDataList.get(j)[0]) - 1);
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

		if(CollectionUtils.isNotEmpty(gradeTeachingList)){
			try {
				gradeTeachingService.saveAndInit(unitId, acadyear, semester, getLoginInfo().getUserId(), gradeTeachingList);
			} catch (Exception e) {
				e.printStackTrace();
				return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
			}
		}
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
		return result;
	}
	
	
	/**
	 * 下载课程导入模板
	 * @param response
	 */
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		
		List<String> titleList = getRowTitleList();//表头
		Map<String,List<Map<String,String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
		sheetName2Map.put(getObjectName(),new ArrayList<Map<String, String>>());
		Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
	    titleMap.put(getObjectName(),titleList);
        
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile("年级课程开设导入", titleMap, sheetName2Map, response);
		
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
		logger.info("模板校验中......");
		validRowStartNo = "0";
		try{
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
	
}
