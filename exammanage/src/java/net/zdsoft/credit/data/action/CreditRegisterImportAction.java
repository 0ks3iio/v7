package net.zdsoft.credit.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.credit.data.constant.CreditConstants;
import net.zdsoft.credit.data.entity.CreditDailyInfo;
import net.zdsoft.credit.data.entity.CreditDailySet;
import net.zdsoft.credit.data.entity.CreditExamSet;
import net.zdsoft.credit.data.entity.CreditModuleInfo;
import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.credit.data.service.CreditDailyInfoService;
import net.zdsoft.credit.data.service.CreditExamSetService;
import net.zdsoft.credit.data.service.CreditModuleInfoService;
import net.zdsoft.credit.data.service.CreditSetService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Controller
@RequestMapping("/exammanage/credit/import/register")
public class CreditRegisterImportAction extends DataImportAction{
	
	private Logger logger = Logger.getLogger(CreditRegisterImportAction.class);
	
	@Autowired
	private CreditSetService creditSetService;
	@Autowired
	private CustomRoleRemoteService customRoleRemoteService;
	@Autowired
	private GradeTeachingRemoteService gradeTeachingRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private CreditExamSetService creditExamSetService;
	@Autowired
	private CreditModuleInfoService creditModuleInfoService;
	@Autowired
	private CreditDailyInfoService creditDailyInfoService;
	
	@ControllerInfo("进入查询头")
	@RequestMapping("/head")
	public String index(String acadyear,String semester,String gradeId,String type,String subjectId,String clsTypeId,ModelMap map) {
		String unitId = this.getLoginInfo().getUnitId();
		String userId = this.getLoginInfo().getUserId();
		String teacherId = this.getLoginInfo().getOwnerId();
		map.put("acadyear",acadyear);
		map.put("semester",semester);
		map.put("type",type);
		map.put("gradeId",gradeId);
		map.put("isAdmin",isAdmin(unitId, userId));
		if(isAdmin(unitId, userId)) {
			List<GradeTeaching> gradeTeachings = SUtils.dt(gradeTeachingRemoteService.findBySearchList(unitId, acadyear, semester, gradeId, 1),new TR<List<GradeTeaching>>() {});
			Set<String> subIds = EntityUtils.getSet(gradeTeachings, GradeTeaching::getSubjectId);
			List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),new TR<List<Course>>(){});
			if(CollectionUtils.isEmpty(courseList)) {
				return errorFtl(map,"未维护年级课程信息");
			}
			map.put("courseList", courseList);
			if(StringUtils.isBlank(subjectId)) {
				subjectId = courseList.get(0).getId();
			}
			map.put("subjectId",subjectId);
			//教学班数据
			List<TeachClass> teaClslist = SUtils.dt(teachClassRemoteService.findTeachClassListByGradeId(unitId, acadyear, semester, subjectId, gradeId), new TR<List<TeachClass>>() {});
			//行政班数据
			List<ClassTeaching> clsTealist = SUtils.dt(classTeachingRemoteService.findListByGradeIdAndSubId(acadyear, semester, unitId, gradeId,subjectId), new TR<List<ClassTeaching>>() {});
			List<Clazz> clslist = new ArrayList<>();
			Set<String> clsIds = new HashSet<>();
			for (ClassTeaching classTeaching : clsTealist) {
				if(classTeaching.getIsTeaCls() != 1) {
					clsIds.add(classTeaching.getClassId());
				}
			}
			if(clsIds.size()>0) {
				clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR	<List<Clazz>>() {});
			}
			if(StringUtils.isBlank(clsTypeId)) {
				if(CollectionUtils.isNotEmpty(clslist)) {
					clsTypeId = clslist.get(0).getId() + "_" + CreditConstants.CLASS_TYPE1;
				}else if(CollectionUtils.isNotEmpty(clsTealist)) {
					clsTypeId = clsTealist.get(0).getId() + "_" +  CreditConstants.CLASS_TYPE2;
				}
			}
			map.put("clsTypeId",clsTypeId);
			map.put("clslist",clslist);
			map.put("teaClslist",teaClslist);
		}else {
			// 找到自己相关的班级
			Set<String> clsIds = new HashSet<>();
			//任课老师
			//行政班任课信息
			String str = classTeachingRemoteService.findClassTeachingList(unitId, acadyear, semester, teacherId);
			List<ClassTeaching> list1 = SUtils.dt(str, new TR<List<ClassTeaching>>() {});
			for (ClassTeaching classTeaching : list1) {
				if(classTeaching.getIsTeaCls() == 1) {
					continue;
				}
				clsIds.add(classTeaching.getClassId());
			}
			List<Clazz> clsAlllist = SUtils.dt(classRemoteService.findBySchoolIdAcadyear(unitId, acadyear),new TR<List<Clazz>>() {});
			List<Clazz> clslist = new ArrayList<>();
			Set<String> teaClsIds = new HashSet<>();
			for (Clazz clazz : clsAlllist) {
				//班主任权限或者行政班任课
				if(StringUtils.equals(clazz.getTeacherId(), teacherId)
						|| clsIds.contains(clazz.getId())) {
					clslist.add(clazz);
					if(StringUtils.equals(clazz.getTeacherId(), teacherId)) {
						teaClsIds.add(clazz.getId());
					}
				}
			}
			//教学班任课信息
			List<TeachClass> teaClslist = SUtils.dt(teachClassRemoteService.findListByTeacherId(teacherId, acadyear, semester), new TR<List<TeachClass>>() {});
			if(StringUtils.isBlank(clsTypeId)) {
				if(CollectionUtils.isNotEmpty(clslist)) {
					clsTypeId = clslist.get(0).getId() + "_" + CreditConstants.CLASS_TYPE1;
				}else if(CollectionUtils.isNotEmpty(teaClslist)) {
					clsTypeId = teaClslist.get(0).getId() + "_" +  CreditConstants.CLASS_TYPE2;
				}
			}
			map.put("teaClslist",teaClslist);
			map.put("clslist",clslist);
			map.put("clsTypeId",clsTypeId);
			Set<String> subIds = new HashSet<>();
			if(StringUtils.isNotBlank(clsTypeId)) {
				String clsId = clsTypeId.split("_")[0];
				String clsType = clsTypeId.split("_")[1];
				if(StringUtils.equals(clsType, CreditConstants.CLASS_TYPE1)) {
					List<ClassTeaching> classTeachingsList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, clsId), new TR<List<ClassTeaching>>() {});
					if(teaClsIds.contains(clsId)) {
						subIds = EntityUtils.getSet(classTeachingsList, ClassTeaching::getSubjectId);
					}else {
						for (ClassTeaching e : classTeachingsList) {
							if(StringUtils.equals(e.getTeacherId(), teacherId)) {
								subIds.add(e.getSubjectId());
							}
						}
					}
				}else {
					TeachClass teaCls = SUtils.dc(teachClassRemoteService.findOneById(clsId), TeachClass.class);
					subIds.add(teaCls.getCourseId());
				}
			}
			if(CollectionUtils.isNotEmpty(subIds)) {
				List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),new TR<List<Course>>(){});
				if(StringUtils.isBlank(subjectId)) {
					map.put("subjectId",courseList.get(0).getId());
				}else {
					if(subIds.contains(subjectId)) {
						map.put("subjectId",subjectId);
					}else {
						map.put("subjectId",courseList.get(0).getId());
					}
				}
				map.put("courseList", courseList);
			}else {
				map.put("courseList", new ArrayList<Course>());
				map.put("subjectId","");
			}
		}
		return "/exammanage/credit/register/registerImportIndex.ftl";
	}
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(String acadyear,String semester,String subjectId,String clsTypeId,String type,ModelMap map) {//
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "学分录入");
		// 导入URL 
		map.put("businessUrl", "/exammanage/credit/import/register/1");
		// 导入模板
		map.put("templateDownloadUrl", "/exammanage/credit/import/register/template?acadyear="+acadyear+"&semester="+semester+"&subjectId="+subjectId+"&clsTypeId="+clsTypeId+
				"&type="+type);
		// 导入对象
		map.put("objectName", "");
		// 导入说明
		map.put("description", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
//		map.put("validateUrl", "/exammanage/credit/import/register/validate/1?type="+type);
		map.put("validateUrl", "/exammanage/credit/import/register/validate/1?acadyear="+acadyear+"&semester="+semester+"&subjectId="+subjectId+"&clsTypeId="+clsTypeId+
				"&type="+type);
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
		obj.put("acadyear", acadyear);
	    obj.put("unitId", unitId);
	    obj.put("semester", semester);
	    obj.put("subjectId", subjectId);
	    obj.put("clsTypeId", clsTypeId);
	    obj.put("type", type);
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/exammanage/credit/register/registerImport.ftl";
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String semester = request.getParameter("semester");
		String acadyear = request.getParameter("acadyear");
		String subjectId = request.getParameter("subjectId");
		String clsTypeId = request.getParameter("clsTypeId");
		String type = request.getParameter("type");
		String unitId = this.getLoginInfo().getUnitId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		List<Student> stulist = new ArrayList<>();
		Set<String> stuIds = new HashSet<>();
		String gradeId = "";
		if(StringUtils.equals(classType, CreditConstants.CLASS_TYPE2)) {
			//教学班
			List<TeachClassStu> clsStuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[] {classId}), new TR<List<TeachClassStu>>() {});
			stuIds = EntityUtils.getSet(clsStuList, TeachClassStu::getStudentId);
			stulist = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
			TeachClass teachClass =SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
			gradeId = teachClass.getGradeId();
		}else {
			//教学班
			stulist = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {});
			stuIds =EntityUtils.getSet(stulist, Student::getId);
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			gradeId = cls.getGradeId();
		}
		List<String> titleList = new ArrayList<>();//表头
		titleList.add("*学号");
		titleList.add("*姓名");
		ArrayList<Map<String, String>> recordList = new ArrayList<>();
		Map<String,String> inMap = null;
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<>();
		if(StringUtils.equals(type, "1")) {
			if(stulist != null && stulist.size() > 0) {
				Clazz cls = SUtils.dc(classRemoteService.findOneById(stulist.get(0).getClassId()), Clazz.class);
				gradeId = cls.getGradeId();
			}
			Map<String, CreditDailyInfo> infoMap = creditDailyInfoService.findMapByStuIds(acadyear,semester,subjectId,set,stuIds);
			List<CreditDailySet> setlist = set.getDailySetList();
			for (CreditDailySet setF : setlist) {
				List<CreditDailySet> setS = setF.getSubSetList();
				for (CreditDailySet setSub : setS) {
					titleList.add(setF.getName()+"("+setSub.getName()+") "+setSub.getScore());
				}
			}
			for (Student student : stulist) {
	        	inMap = new HashMap<>();
	        	inMap.put("*学号", student.getStudentCode());
	        	inMap.put("*姓名", student.getStudentName());
	        	for (CreditDailySet setF : setlist) {
					List<CreditDailySet> setS = setF.getSubSetList();
					for (CreditDailySet setSub : setS) {
						float score = infoMap.get(setF.getId()+","+setSub.getId()+","+student.getId()).getScore();
						inMap.put(setF.getName()+"("+setSub.getName()+") "+setSub.getScore(), score+"");
					}
				}
	        	recordList.add(inMap);
	        }
			
		}else {
			List<CreditExamSet> usualSetList = creditExamSetService.findByUsualSet(set.getId(), acadyear, semester, subjectId, classId, classType);
			List<CreditExamSet> moudleSets = creditExamSetService.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(set.getId(), acadyear, semester, gradeId, CreditConstants.CREDIT_EXAM_TYPE_2);
			CreditExamSet moudleSet = null;
			if(CollectionUtils.isNotEmpty(moudleSets)) {
				moudleSet = moudleSets.get(0);
			}
			Map<String, CreditModuleInfo> infoMap = creditModuleInfoService.findMapByStuIds(acadyear,semester,subjectId,usualSetList,moudleSet,set, stuIds);
			for (CreditExamSet usualSet : usualSetList) {
				titleList.add(usualSet.getName());
			}
			if(moudleSet != null) {
				titleList.add("模块成绩");
			}
			for (Student student : stulist) {
	        	inMap = new HashMap<String, String>();
	        	inMap.put("*学号", student.getStudentCode());
	        	inMap.put("*姓名", student.getStudentName());
	        	for (CreditExamSet usualSet : usualSetList) {
					float score = infoMap.get("1,"+usualSet.getId()+",1,"+student.getId()).getScore();
					inMap.put(usualSet.getName(), score+"");
				}
	        	if(moudleSet != null) {
	        		float score = infoMap.get("2,"+moudleSet.getId()+",1,"+student.getId()).getScore();
	        		inMap.put("模块成绩",score+"");
				}
	        	recordList.add(inMap);
	        }
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), titleList);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile("学分录入", titleMap, sheetName2RecordListMap, response);
	}
	
	@RequestMapping("/{flag}")
	@ResponseBody
	public String dataImport(String filePath, String params,@PathVariable("flag")String flag) {
		logger.info("业务数据处理中......");
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		String clsTypeId = (String) performance.get("clsTypeId");
		String subjectId = (String) performance.get("subjectId");
		String type = (String) performance.get("type");
		
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		List<Student> stulist = new ArrayList<>();
		Set<String> stuIds = new HashSet<>();
		String userId = this.getLoginInfo().getUserId();
		String gradeId = "";
		if(StringUtils.equals(classType, CreditConstants.CLASS_TYPE2)) {
			//教学班
			List<TeachClassStu> clsStuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[] {classId}), new TR<List<TeachClassStu>>() {});
			stuIds = EntityUtils.getSet(clsStuList, TeachClassStu::getStudentId);
			stulist = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
			TeachClass teachClass =SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
			gradeId = teachClass.getGradeId();
		}else {
			//行政班
			stulist = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {});
			stuIds =EntityUtils.getSet(stulist, Student::getId);
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			gradeId = cls.getGradeId();
		}
		Map<String, Student> stuMap = EntityUtils.getMap(stulist, Student::getStudentCode);
		List<String[]> datas = new ArrayList<>();
		String unitId = this.getLoginInfo().getUnitId();
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		int successCount = 0;
		int errorCount = 0;
		List<String[]> errorDataList = new ArrayList<>();
		if(StringUtils.equals(type, "1")) {
			//TODO
			List<String> titleList = new ArrayList<>();//表头
			titleList.add("*学号");
			titleList.add("*姓名");
			List<CreditDailySet> setlist = set.getDailySetList();
			for (CreditDailySet setF : setlist) {
				List<CreditDailySet> setS = setF.getSubSetList();
				for (CreditDailySet setSub : setS) {
					titleList.add(setF.getName()+"("+setSub.getName()+") " + setSub.getScore());
				}
			}
			//每一行数据   表头列名：0
			datas = ExcelUtils.readExcelByRow(filePath,1,titleList.size());
			creditDailyInfoService.importInfo(titleList,datas,set,gradeId,userId,subjectId,clsTypeId, stuMap, successCount, errorCount, errorDataList);
		}else {
			//获取第一行的表头数据
			List<String> colls = ExcelUtils.readExcelOneRow(filePath, 0);
			//每一行数据   表头列名：0
			datas = ExcelUtils.readExcelByRow(filePath,1,colls.size());
			//TODO
			creditModuleInfoService.importInfo(colls,datas,set,gradeId,userId,subjectId,clsTypeId, stuMap, successCount, errorCount, errorDataList);
		}
		successCount = datas.size() - errorDataList.size();
		String result = result(datas.size(),successCount,errorDataList.size(),errorDataList);
        return result;
	}
	
	@RequestMapping("/validate/{flag}")
	@ResponseBody
	public String validate(String filePath,String type, String validRowStartNo,@PathVariable("flag") String flag,HttpServletRequest request) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "0";
		}
		try{
			String semester = request.getParameter("semester");
			String acadyear = request.getParameter("acadyear");
			String subjectId = request.getParameter("subjectId");
			String clsTypeId = request.getParameter("clsTypeId");
			String unitId = this.getLoginInfo().getUnitId();
//			List<String> colls = ExcelUtils.readExcelOneRow(filePath, 0);
			List<String> colls = getTitleList(type, unitId, acadyear, semester, subjectId, clsTypeId);
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,Integer.valueOf(validRowStartNo), colls.size());
			return templateValidate(datas, colls);
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	public List<String> getTitleList(String type,String unitId,String acadyear,String semester,String subjectId,String clsTypeId){
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		String gradeId = "";
		if(StringUtils.equals(classType, CreditConstants.CLASS_TYPE2)) {
			//教学班
			TeachClass teachClass =SUtils.dc(teachClassRemoteService.findOneById(classId), TeachClass.class);
			gradeId = teachClass.getGradeId();
		}else {
			//教学班
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			gradeId = cls.getGradeId();
		}
		List<String> titleList = new ArrayList<>();//表头
		titleList.add("*学号");
		titleList.add("*姓名");
		CreditSet set = creditSetService.findAndInit(unitId, acadyear, semester);
		if(StringUtils.equals(type, "1")) {
			List<CreditDailySet> setlist = set.getDailySetList();
			for (CreditDailySet setF : setlist) {
				List<CreditDailySet> setS = setF.getSubSetList();
				for (CreditDailySet setSub : setS) {
					titleList.add(setF.getName()+"("+setSub.getName()+") "+setSub.getScore());
				}
			}
			
		}else {
			List<CreditExamSet> usualSetList = creditExamSetService.findByUsualSet(set.getId(), acadyear, semester, subjectId, classId, classType);
			List<CreditExamSet> moudleSets = creditExamSetService.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(set.getId(), acadyear, semester, gradeId, CreditConstants.CREDIT_EXAM_TYPE_2);
			CreditExamSet moudleSet = null;
			if(CollectionUtils.isNotEmpty(moudleSets)) {
				moudleSet = moudleSets.get(0);
			}
			for (CreditExamSet usualSet : usualSetList) {
				titleList.add(usualSet.getName());
			}
			if(moudleSet != null) {
				titleList.add("模块成绩");
			}
		}
		return titleList;
	}
	
	public static void main(String[] args) {
		String filePath = "c:\\upload\\20190826\\402880926ccc7a82016ccc7f7e650011\\学分录入.xls";
		List<String> datas = ExcelUtils.readExcelOneRow(filePath, 0);
		System.out.println(datas.size());
		for (String string : datas) {
			System.out.println(string);
		}
		//readExcelByRow(filePath,0, 5);
	}
	
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
	 }

	@Override
	public String dataImport(String filePath, String params) {
		return null;
	}
	
	
	@Override
	public String getObjectName() {
		return "registerImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、改变选项后请重新下载并上传模板</p>";
	}
	
	@Override
	public List<String> getRowTitleList() {
		return null;
	}
  /**
    * 判断是否为教务管理员
    */
   private boolean isAdmin(String unitId,String userId) {
	   return customRoleRemoteService.checkUserRole(unitId, CreditConstants.SUBSYSTEM_86, CreditConstants.EDUCATION_CODE, userId);
//		   return true;
   }
}
