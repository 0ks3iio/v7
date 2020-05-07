package net.zdsoft.gkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.entity.GkLimitSubject;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkLimitSubjectService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/gkelective/selectStuImport")
public class GkElectiveChosenSubjectImportAction extends DataImportAction{

	private Logger logger = Logger.getLogger(GkElectiveChosenSubjectImportAction.class);

    @Autowired
    private GkRelationshipService gkRelationshipService;
    @Autowired
    private CourseRemoteService courseRemoteService;
	@Autowired
    private GkSubjectArrangeService gkSubjectArrangeService;
    @Autowired
    private ClassRemoteService classService;
    @Autowired
    private GkResultService gkResultService;
    @Autowired
    private StudentRemoteService studentService;
    @Autowired
    private GkLimitSubjectService gkLimitSubjectService;
    
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String importMain(String arrangeId,String type,ModelMap map) {
	    // 业务名称
	    map.put("businessName", "已选学生导入");
	 	// 导入URL 
	 	map.put("businessUrl", "/gkelective/selectStuImport/import");
	 	// 导入模板
	 	map.put("templateDownloadUrl", "/gkelective/selectStuImport/template");
	 	// 导入对象
	 	map.put("objectName", "");
	 	// 导入说明
	 	StringBuffer description=new StringBuffer();
	 	description.append(getMyDescription(arrangeId));
	 	
	 	// 如果导入文件中前面有说明性文字的  这里需要传一个有效数据开始行（列名那一行）
	 	//如果列名在第一行的就不需要传
	 	map.put("validRowStartNo",0);
	 	//模板校验
	 	map.put("validateUrl", "/gkelective/selectStuImport/validate");
	 	 		
	 	map.put("description", description);
	 	map.put("businessKey", UuidUtils.generateUuid());
	 	 		
	 	JSONObject obj=new JSONObject();
	 	obj.put("arrangeId", arrangeId);
	 	
	 	map.put("monthPermance",JSON.toJSONString(obj));
	 	map.put("arrangeId", arrangeId);
	 	map.put("type", type);
		return "/gkelectiveys/chosenSubject/chosenSubjectImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "chosenSubjectImport";
	}

	@Override
	public String getDescription() {
		return null;
	}
	
	public String getMyDescription(String arrangeId) {
		Map<String,String> coursesMap = new LinkedHashMap<String,String>();
        List<Course> coursesList = getCoursesList(arrangeId);
        for (Course course : coursesList) {
        	coursesMap.put(course.getId(), course.getSubjectName());
        }
		List<GkLimitSubject> GkLimitSubjectList = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
        String limitSubjectName = "";
        for (GkLimitSubject gkLimitSubject : GkLimitSubjectList) {
        	String[] subjectIds = gkLimitSubject.getSubjectVal().split(",");
        	String subName = "";
        	for (String str : subjectIds) {
        		subName += coursesMap.get(str) + "、";
        	}
        	subName = "(" + subName.substring(0, subName.length()-1) + ")";
        	limitSubjectName += subName + ",";
        }
        if (StringUtils.isNotBlank(limitSubjectName)) {
        	limitSubjectName = limitSubjectName.substring(0, limitSubjectName.length()-1);
        } else {
        	limitSubjectName = "无";
        }
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、请正确填写学号、姓名</p>"
				+ "<p>2、请填写三门科目，且不可重复</p>"
				+ "<p>3、限制不能选："+limitSubjectName+"</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		//下载文件表头
		List<String> tis = new ArrayList<String>();
		tis.add("学号");
		tis.add("姓名");
		tis.add("选1");
		tis.add("选2");
		tis.add("选3");
		return tis;
	}

	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		//每一行数据   表头列名：0
		List<String[]> datas = ExcelUtils.readExcelByRow(filePath,1,getRowTitleList().size());
		 //错误数据序列号
        int sequence =1;
        int totalSize =datas.size();
        
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(datas)){
        	sequence++;
        	String[] errorData=new String[]{String.valueOf(sequence),"学号","","没有导入数据"};
        	errorDataList.add(errorData);
        	return result(datas.size()-1,0,0,errorDataList);
        }
        
        JSONObject performance = JSON.parseObject(params,JSONObject.class);
        final String arrangeId = (String) performance.get("arrangeId");
        List<GkLimitSubject> GkLimitSubjectList = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
        final GkSubjectArrange gkArrange =gkSubjectArrangeService.findArrangeById(arrangeId);
        if(gkArrange==null){
        	String[] errorData=new String[]{String.valueOf(sequence),"学号","","该选课课程不存在"};
        	sequence++;
        	errorDataList.add(errorData);
        	return result(datas.size()-1,0,0,errorDataList);
        }
        Map<String,String> coursesMap = new LinkedHashMap<String,String>();
        List<Course> coursesList = getCoursesList(arrangeId);
        for (Course course : coursesList) {
        	coursesMap.put(course.getSubjectName(), course.getId());
        }
        // 查出年级下的所有行政班级--5分钟缓存减少性能消耗
        List<Clazz> clazzList = getClazzList(gkArrange);
        Map<String,GkResult> gkResultMap = new LinkedHashMap<String,GkResult>();
        List<GkResult> gkResultList = getGkResultList(arrangeId,clazzList);
        for (GkResult gkResult : gkResultList) {
        	gkResultMap.put(gkResult.getStucode(), gkResult);
        }
        List<GkResult> saveList = new ArrayList<GkResult>();
        Set<String> stuIds = new HashSet<String>();
        int successCount=0;
        for (String[] arr:datas) {
        	String[] errorData=new String[4];
            sequence++;
            
            if(StringUtils.isBlank(arr[0])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(arr[1])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="姓名";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            
            if(StringUtils.isBlank(arr[2])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="选1";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(arr[3])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="选2";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(arr[4])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="选3";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            GkResult gkResult = gkResultMap.get(arr[0]);
            
            if (gkResult == null) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]=arr[0];
                errorData[3]="该学号不存在";
                errorDataList.add(errorData);
                continue;
            } else {
            	if (!arr[1].equals(gkResult.getStuName())) {
            		errorData[0]=String.valueOf(sequence);
                    errorData[1]="姓名";
                    errorData[2]=arr[1];
                    errorData[3]="该学号对应的学生姓名错误";
                    errorDataList.add(errorData);
                    continue;
            	}
            	
            }
            
            String sel1 = coursesMap.get(arr[2]);
            String sel2 = coursesMap.get(arr[3]);
            String sel3 = coursesMap.get(arr[4]);
            Set<String> subSet = new HashSet<String>();
            subSet.add(sel1);
            subSet.add(sel2);
            subSet.add(sel3);
            
            if(StringUtils.isBlank(sel1)){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="选1";
                errorData[2]="";
                errorData[3]="该科目不存在或未开放";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(sel2)){
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="选2";
            	errorData[2]="";
            	errorData[3]="该科目不存在或未开放";
            	errorDataList.add(errorData);
            	continue;
            }
            
            if(StringUtils.isBlank(sel3)){
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="选3";
            	errorData[2]="";
            	errorData[3]="该科目不存在或未开放";
            	errorDataList.add(errorData);
            	continue;
            }
            
            if (subSet.size() != 3) {
            	errorData[0]=String.valueOf(sequence);
            	if (sel1.equals(sel2)) {
            		errorData[1]="选2";
            	} else if (sel1.equals(sel3)) {
            		errorData[1]="选3";
            	} else if (sel2.equals(sel3)) {
            		errorData[1]="选3";
            	}
            	errorData[2]="";
            	errorData[3]="选课不能重复";
            	errorDataList.add(errorData);
            	continue;
            }
            
            boolean selectSet = false;
            for (GkLimitSubject gkLimitSubject : GkLimitSubjectList) {
            	List<String> subjectIdsList = Arrays.asList(gkLimitSubject.getSubjectVal().split(","));
            	if (subjectIdsList.containsAll(subSet)) {
            		selectSet = true;
            		break;
            	}
            }
            
            if (selectSet) {
            	errorData[0]=String.valueOf(sequence);
            	errorData[1]="选1";
            	errorData[2]="";
            	errorData[3]="限选组合";
            	errorDataList.add(errorData);
            	continue;
            }
            
            for (GkResult gkResult2 : saveList) {
            	if (gkResult2.getStudentId().equals(gkResult.getStudentId())) {
            		errorData[0]=String.valueOf(sequence);
                	errorData[1]="学号";
                	errorData[2]="";
                	errorData[3]="学生不能重复选课";
                	errorDataList.add(errorData);
                	continue;
            	}
            }
            
            successCount++;
            stuIds.add(gkResult.getStudentId());
            GkResult gk = null;
            for (String str : subSet) {
            	gk = new GkResult();
            	gk.setSubjectArrangeId(arrangeId);
            	gk.setStudentId(gkResult.getStudentId());
            	gk.setSubjectId(str);
            	saveList.add(gk);
            }
        }
        
        try{
			if (CollectionUtils.isNotEmpty(saveList)) {
				gkResultService.deleteByArrangeIdAndStudentIds(arrangeId,stuIds.toArray(new String[0]));
				gkResultService.saveAll(saveList, null, null);
			}
        }catch (Exception e){
            e.printStackTrace();

        }
        
        int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList);
		return result;
	}

	public List<GkResult> getGkResultList(String arrangeId,List<Clazz> clazzList) {
		Set<String> classIds = new HashSet<String>();
        Map<String, String> id2name = new HashMap<String, String>();
        for (Clazz c : clazzList) {
            classIds.add(c.getId());
            id2name.put(c.getId(), c.getClassNameDynamic());
        }
		List<GkResult> gkResultList = new ArrayList<GkResult>();
		List<GkResult> gkResultList1 = new ArrayList<GkResult>();
        ChosenSubjectSearchDto searchDto = new ChosenSubjectSearchDto();
        searchDto.setSearchSelectType("2");
        gkResultList = gkResultService.findUnChosenGkResult(arrangeId, searchDto, null, true, classIds.toArray(new String[0]));
        gkResultList1 = gkResultService.findGkResult(arrangeId, searchDto, null, null);
        gkResultList.addAll(gkResultList1);
        Set<String> stuIds = EntityUtils.getSet(gkResultList, "studentId");
        Map<String, Student> studentMap = new HashMap<String, Student>();
        if(CollectionUtils.isNotEmpty(stuIds)){
        	List<Student> studentList = SUtils.dt(studentService.findListByIds(stuIds.toArray(new String[0])),new TR<List<Student>>() {});
            studentMap = EntityUtils.getMap(studentList, "id");
        }
        Student student = null;
        for (GkResult gkr : gkResultList) {
        	student = studentMap.get(gkr.getStudentId());
        	if(student!=null){
        		gkr.setClassName(id2name.get(student.getClassId()));
        	}
        }
        return gkResultList;
	}
	
	public List<Clazz> getClazzList(final GkSubjectArrange gkArrange) {
		List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gkArrange.getGradeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
			@Override
			public List<Clazz> queryData() {
				return SUtils.dt(classService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
			}
	    });
		return clazzList;
	}
	
	public List<Course> getCoursesList(final String arrangeId) {
		// 所有供选择的学科
        List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
		return coursesList;
	} 
	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> titleList = getRowTitleList();//表头
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),new ArrayList<Map<String, String>>());
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), titleList);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile("已选学生导入", titleMap, sheetName2RecordListMap, response);
	}
	
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "0";
		}try{
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
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
