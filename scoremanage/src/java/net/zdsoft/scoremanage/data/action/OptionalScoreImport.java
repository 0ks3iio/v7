package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

@Controller
@RequestMapping("/scoremanage/optionalScore/import")
public class OptionalScoreImport extends DataImportAction{
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@RequestMapping("/index")
	public String index(ScoreInfo scoreInfo,ModelMap map){
		LoginInfo loginInfo = getLoginInfo();
		// 业务名称
		map.put("businessName", "选修课成绩录入");
		// 导入URL 
		map.put("businessUrl", "/scoremanage/optionalScore/import/importData");
		// 导入模板
		map.put("templateDownloadUrl", "/scoremanage/optionalScore/import/template?teachClassId="+scoreInfo.getTeachClassId());
		// 导入对象
		map.put("objectName", "");
		// 导入说明
		map.put("description", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/scoremanage/optionalScore/import/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
		obj.put("subjectId", scoreInfo.getSubjectId());
	    obj.put("unitId", scoreInfo.getUnitId());
	    obj.put("teachClassId", scoreInfo.getTeachClassId());
		obj.put("ownerId", loginInfo.getUserId());
		
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
	    
		return "/scoremanage/optionalScoreInfo/optionalScoreImport.ftl";
	}
	
	/**
	 * 模板校验
	 * @param filePath
	 * @param validRowStartNo
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo){
		validRowStartNo = "1";
		if (StringUtils.isBlank(validRowStartNo)) {
		}try{
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	@Override
	@RequestMapping("/importData")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		System.out.println("导入");
		//取出公共参数
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String subjectId = (String) performance.get("subjectId");
		String unitId = (String) performance.get("unitId");
		String teachClassId = (String) performance.get("teachClassId");
		String ownerId = (String) performance.get("ownerId");
		
		//每一行数据   表头列名：0
		List<String[]> datas = ExcelUtils.readExcelByRow(filePath,2,getRowTitleList().size());
		
		 //错误数据序列号
        int sequence =0;
        int totalSize =datas.size();
        
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(StringUtils.isBlank(teachClassId)){
        	String[] errorData=new String[4];
        	sequence++;
        	errorData[0]=String.valueOf(sequence);
        	errorData[1]="";
        	errorData[2]="";
        	errorData[3]="此教学班不存在";
        	errorDataList.add(errorData);
        	return result(datas.size(),0,0,errorDataList);
        }
        if(CollectionUtils.isEmpty(datas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(datas.size(),0,0,errorDataList);
        }
        //获取教学班信息
        TeachClass teachClass = SUtils.dc(teachClassService.findOneById(teachClassId),TeachClass.class);
        String acadyear = teachClass.getAcadyear();
		String semester = teachClass.getSemester();
        //获取此教学班所有学生
		String[] classIds = null;
		Map<String, TeachClass> teachClassMap = new HashMap<String, TeachClass>();
		List<ScoreInfo> scoreInfList = null;
		if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
			List<TeachClass> childTeachClassList = SUtils.dt(teachClassService.findByParentIds(new String[]{teachClassId}),TeachClass.class);
			for (TeachClass childTeachClass : childTeachClassList) {
				if(childTeachClass.getPassMark()==null)childTeachClass.setPassMark(60);
				if(childTeachClass.getFullMark()==null)teachClass.setPassMark(100);
			}
			teachClassMap = EntityUtils.getMap(childTeachClassList, "id");
			Set<Object> classIdSet = EntityUtils.getSet(childTeachClassList, "id");
			classIds = classIdSet.toArray(new String[0]);
			scoreInfList = scoreInfoService.findOptionalCourseScore(unitId,classIds);
		}else{
			classIds = new String[]{teachClassId};
			if(teachClass.getPassMark()==null)teachClass.setPassMark(60);
			if(teachClass.getFullMark()==null)teachClass.setPassMark(100);
			teachClassMap.put(teachClassId, teachClass);
			scoreInfList = scoreInfoService.findOptionalCourseScore(unitId, subjectId, teachClassId);
		}
        List<TeachClassStu> teachStuList = SUtils.dt(teachClassStuService.findByClassIds(classIds),TeachClassStu.class);
        List<Student> stuList = SUtils.dt(studentService.findListByIds(EntityUtils.getSet(teachStuList, "studentId").toArray(new String[0])),Student.class);
        Map<String, Student> stuMap = EntityUtils.getMap(stuList, "studentCode");
        Map<String, String> teachStuClassMap = EntityUtils.getMap(teachStuList, "studentId","classId");
        //获取此选修课课程信息
//        Course course = SUtils.dc(courseService.findById(subjectId), Course.class);
//        Integer fullMark = course.getFullMark();
//        Integer passMark = course.getInitPassMark();
//        Integer credit = course.getInitCredit();
        //从数据库中获取数据
        Map<String, ScoreInfo> scoreInfMap = EntityUtils.getMap(scoreInfList, "studentId");
        
        Set<String> stuCodes = new HashSet<>();
        List<ScoreInfo> forSaveList = new ArrayList<ScoreInfo>();
        int successCount=0;
        int i = 2;
        for (String[] arr : datas) {
        	i++;
			String stuCode = arr[0];
			String stuName = arr[1];
			String score = arr[2];
			String scoreStatus = arr[3];
			
			//验证必填
			if(StringUtils.isBlank(stuCode)||StringUtils.isBlank(stuName)||
					StringUtils.isBlank(score)||StringUtils.isBlank(scoreStatus)){
				String[] errorData=new String[4];
	            sequence++;
	            errorData[0]=String.valueOf(sequence);
	            errorData[1]="第"+i+"行";
	            errorData[2]="";
	            errorData[3]="带  * 标识的为必填项";
	            errorDataList.add(errorData);
	            continue;
			}
			//验证是否存在此学生
			if(stuMap.get(stuCode) == null){
				String[] errorData=new String[4];
	            sequence++;
	            errorData[0]=String.valueOf(sequence);
	            errorData[1]="第"+i+"行";
	            errorData[2]="";
	            errorData[3]="此学生不存在或不在此班级";
	            errorDataList.add(errorData);
	            continue;
			}
			//学号姓名是否匹配
			if(!stuName.equals(stuMap.get(stuCode).getStudentName())){
				String[] errorData=new String[4];
				sequence++;
				errorData[0]=String.valueOf(sequence);
				errorData[1]="第"+i+"行";
				errorData[2]="";
				errorData[3]="学号姓名不匹配";
				errorDataList.add(errorData);
				continue;
				
			}
			//验证分数 格式 及 范围
			Integer fullMark = null;
			if(teachClassMap.get(teachStuClassMap.get(stuMap.get(stuCode).getId()))!=null){
				fullMark = teachClassMap.get(teachStuClassMap.get(stuMap.get(stuCode).getId())).getFullMark();
			}
			if(fullMark==null)fullMark=100;
			if(score.matches("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$") && Float.parseFloat(score) > fullMark){
				String[] errorData=new String[4];
				sequence++;
				errorData[0]=String.valueOf(sequence);
				errorData[1]="第"+i+"行";
				errorData[2]="考试成绩";
				errorData[3]="考试成绩为不超过" + fullMark+"的数，且最多保留两位小数";
				errorDataList.add(errorData);
				continue;
			}
			//验证状态是否存在
			if(getAllScoreStatus().get(scoreStatus) == null){
				String[] errorData=new String[4];
				sequence++;
				errorData[0]=String.valueOf(sequence);
				errorData[1]="第"+i+"行";
				errorData[2]="状态";
				errorData[3]="输入的状态代码不存在";
				errorDataList.add(errorData);
				continue;
			}
			//验证是否重复导入
			if(stuCodes.contains(stuCode)){
				String[] errorData=new String[4];
				sequence++;
				errorData[0]=String.valueOf(sequence);
				errorData[1]="第"+i+"行";
				errorData[2]="学号";
				errorData[3]="学号："+stuCode+"重复出现，请勿重复导入成绩";
				errorDataList.add(errorData);
				continue;
			}
			//验证通过开始保存
			
			ScoreInfo forSave = null;
			if((forSave = scoreInfMap.get(stuMap.get(stuCode).getId())) == null){
				forSave = new ScoreInfo();
				forSave.setId(UuidUtils.generateUuid());
				forSave.setAcadyear(acadyear);
				forSave.setSemester(semester);
				forSave.setUnitId(unitId);
				forSave.setExamId(BaseConstants.ZERO_GUID);
				forSave.setStudentId(stuMap.get(stuCode).getId());
				if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
					forSave.setTeachClassId(teachStuClassMap.get(forSave.getStudentId()));
					forSave.setSubjectId(teachClassMap.get(forSave.getTeachClassId())==null?"":teachClassMap.get(forSave.getTeachClassId()).getCourseId());
				}else{
					forSave.setTeachClassId(teachClassId);
					forSave.setSubjectId(subjectId);
				}
				
				forSave.setOperatorId(getLoginInfo().getUserId());
				forSave.setInputType(ScoreDataConstants.ACHI_SCORE);
				forSave.setClassId(stuMap.get(forSave.getStudentId()).getClassId());
			}else {
				forSave.setScore(score);
				forSave.setScoreStatus(scoreStatus);
				forSave.setOperatorId(getLoginInfo().getUserId());
			}
			
			//赋予学分
			Integer passMark = teachClassMap.get(forSave.getTeachClassId()).getPassMark();
			Integer credit = teachClassMap.get(forSave.getTeachClassId()).getCredit();
			if(Float.parseFloat(forSave.getScore()) >= passMark){
				forSave.setToScore(credit+"");
			}else{
				forSave.setToScore("0");
			}
			
			forSaveList.add(forSave);
			stuCodes.add(stuCode);
			successCount++;
		}
        
        if (CollectionUtils.isNotEmpty(forSaveList)) {
        	scoreInfoService.saveAllEntitys(forSaveList.toArray(new ScoreInfo[0]));
		}
		
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList);
        return result;
	}
	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String teachClassId = request.getParameter("teachClassId");
		TeachClass teachClass = SUtils.dc(teachClassService.findOneById(teachClassId), TeachClass.class);
		String subjectName = SUtils.dc(courseService.findOneById(teachClass.getCourseId()),Course.class).getSubjectName();
		String semester = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", teachClass.getSemester()), McodeDetail.class).getMcodeContent();
		
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
        style.setAlignment(HorizontalAlignment.CENTER);//水平
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        //高度：3倍默认高度
        titleRow.setHeightInPoints(4*sheet.getDefaultRowHeightInPoints());
  
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
        
        String[] classIds = null;
        if(Constant.IS_TRUE_Str.equals(teachClass.getIsMerge())){
        	List<TeachClass> childTeachClassList = SUtils.dt(teachClassService.findByParentIds(new String[]{teachClassId}),TeachClass.class);
        	classIds = EntityUtils.getSet(childTeachClassList, "id").toArray(new String[0]);
        }else{
        	classIds = new String[]{teachClassId};
        }
        List<TeachClassStu> teachStudentList = SUtils.dt(teachClassStuService.findByClassIds(classIds),TeachClassStu.class);
        List<Student> studentList = SUtils.dt(studentService.findListByIds(EntityUtils.getSet(teachStudentList, "studentId").toArray(new String[0])),Student.class);
        
        // 排序
        Set<String> xzbClasIds = EntityUtils.getSet(studentList, Student::getClassId);
        List<Clazz> clazzList = classRemoteService.findListObjectByIds(xzbClasIds.toArray(new String[0]));
        Map<String, String> classNameMap = EntityUtils.getMap(clazzList, Clazz::getId,Clazz::getClassName);
        
        Comparator<Student> c = (s1,s2)->{
        	String cn = classNameMap.get(s1.getClassId());
        	String cn2 = classNameMap.get(s2.getClassId());
        	if(!StringUtils.equals(cn, cn2)) {
				String cns1 = cn.replaceAll("[0-9]", "");
				String cns2 = cn2.replaceAll("[0-9]", "");
				if(!StringUtils.equals(cns1, cns2)) {
					return cn.compareTo(cn2);
				}
				int c1 = NumberUtils.toInt(cn.replaceAll("[^0-9]", ""));
				int c2 = NumberUtils.toInt(cn2.replaceAll("[^0-9]", ""));
				return c1-c2;
			}
        	if(!StringUtils.equals(s1.getClassId(), s2.getClassId())) {
				return s1.getClassId().compareTo(s2.getClassId());
			}
			if(StringUtils.isNotBlank(s1.getClassInnerCode()) && StringUtils.isNotBlank(s2.getClassInnerCode())){
				return NumberUtils.toInt(s1.getClassInnerCode()) - NumberUtils.toInt(s2.getClassInnerCode());
			} else if(StringUtils.isNotBlank(s1.getClassInnerCode())) {
				return 1;
			} else if(StringUtils.isNotBlank(s2.getClassInnerCode())) {
				return 0;
			}
			
			if(StringUtils.isNotBlank(s1.getStudentCode()) && StringUtils.isNotBlank(s2.getStudentCode())) {
				return s1.getStudentCode().compareTo(s2.getStudentCode());
			}
			
			return 0;
        };
        
		Collections.sort(studentList, c );
        
        int i = 2;
        HSSFRow rowContent = null;
		for (Student student : studentList) {
			List<String> tis = new ArrayList<String>();
			tis.add(student.getStudentCode());
			tis.add(student.getStudentName());
			tis.add("");
			tis.add("0");
			rowContent = sheet.createRow(i++);
			for(int j=0;j<size;j++){
	        	HSSFCell cell = rowContent.createCell(j);
	            cell.setCellValue(new HSSFRichTextString(tis.get(j)));
	        }
		}
        
		String fileName = teachClass.getAcadyear()+"学年" + semester + "_"+subjectName+"_"+teachClass.getName()+"成绩导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}
	private String getRemark() {
		Map<String, String> map = getAllScoreStatus();
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(entry.getValue()).append(" ");
		}
		String remark = "填写注意：\n"
				+ "1.带 * 为必填\n"
				+ "2.状态请填写以下数字：\n"
				+ "   "+sb.toString();
		return remark;
	}

	private Map<String, String> getAllScoreStatus() {
		List<McodeDetail> mcodeList = SUtils.dt(mcodeRemoteService.findAllByMcodeIds("DM-CJLX"), McodeDetail.class);
		Map<String, String> map = EntityUtils.getMap(mcodeList, "thisId","mcodeContent");
		
		return map;
	}

	@Override
	public String getObjectName() {
		return "OptionalScoreImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、改变选项后请重新上传模板</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("*学号");
		tis.add("*姓名");
		tis.add("*考试成绩");
		tis.add("*状态");
		return tis;
	}
	
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
	 }
	
}
