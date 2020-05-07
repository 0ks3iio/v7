package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.dto.StudentDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.SaveTestNumDto;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ExamNum;
import net.zdsoft.scoremanage.data.entity.Filtration;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ExamNumService;
import net.zdsoft.scoremanage.data.service.FiltrationService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

@Controller
@RequestMapping("/scoremanage")
public class ExamNumAction extends BaseAction{
	
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private ExamNumService examNumService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuService;
	@Autowired
	private FiltrationService filtrationService;
	@Autowired
	private SemesterRemoteService	semesterService;
	//tab页等判断参数
	public static String P_1="1";
	public static String P_2="2";
	
	//-------------------------考号设置开始-------------
	@RequestMapping("/testNumber/index/page")
    @ControllerInfo(value = "考号设置")
    public String showNumIndex(ModelMap map, HttpSession httpSession,HttpServletRequest request) {
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
        	return errorFtl(map, "学年学期不存在");
		}
        LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		//遗留学年学期都没有
		if(semester==null){
			return errorFtl(map, "学年学期不存在");
		}
		//页面参数
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
		String classType=ScoreDataConstants.CLASS_TYPE1;//班级类型 默认行政班
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("unitId",unitId);
		map.put("classType", classType);
		return "/scoremanage/testNumber/testNumberIndex.ftl";
	}
    @RequestMapping("/testNumber/list/page")
    @ControllerInfo("显示列表")
    public String showExamNumList( ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
		Pagination page = createPagination();
        String examId= request.getParameter("examId");
        String gradeCode= request.getParameter("gradeCode");
        String classType= request.getParameter("classType");
        String classIdSearch= request.getParameter("classIdSearch");
        String unitId=loginInfo.getUnitId();
        List<Student> studentList=new ArrayList<Student>();
        Map<String, String> classMap = new LinkedHashMap<String, String>();
		Set<String> classIds = toMakeClassIds(examId, unitId,gradeCode, classType, classIdSearch);
		classMap=toMakeClassMap(classType, classIds.toArray(new String[]{}));
		if(classIds!=null && classIds.size()>0){
			 if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
				//行政班
				studentList = Student.dt(studentService.findByClassIds(classIds.toArray(new String[]{}),SUtils.s(page)), page);
			}else{
				studentList = Student.dt(studentService.findByTeachClassIds(classIds.toArray(new String[]{}),SUtils.s(page)), page);
				//组装classId
				if(CollectionUtils.isNotEmpty(studentList)){
					Map<String,List<String>> tmap = SUtils.dt(teachClassStuService.findMapWithStuIdByClassIds(classIds.toArray(new String[]{})), new TR<Map<String,List<String>>>(){});
					for(Student s:studentList){
						if(tmap.containsKey(s.getId()) && CollectionUtils.isNotEmpty(tmap.get(s.getId()))){
							s.setClassId(tmap.get(s.getId()).get(0));//随机默认取第一个教学班
						}
					}
				}
			}
		}
        Map<String,String> examNumMap=examNumService.findByExamId(examId,unitId);
        List<StudentDto> stuDtoList=new ArrayList<StudentDto>();
        StudentDto dto=new StudentDto();
        if(CollectionUtils.isNotEmpty(studentList)){
        	for(Student stu:studentList){
        		dto=new StudentDto();
        		dto.setStudent(stu);
        		if(examNumMap.containsKey(stu.getId())){
        			dto.setExamNum(examNumMap.get(stu.getId()));
        		}
        		if(classMap.containsKey(stu.getClassId())){
        			dto.setClassName(classMap.get(stu.getClassId()));
        		}
        		stuDtoList.add(dto);
        	}
        }
        sortStudentDto(stuDtoList);
        map.put("examId", examId);
        map.put("stuDtoList", stuDtoList);
        sendPagination(request, map, page);
        return "/scoremanage/testNumber/testNumberList.ftl";
    }
    
    private void sortStudentDto(List<StudentDto> dtos){
		if(CollectionUtils.isEmpty(dtos)){
			return;
		}
		//升序
		Collections.sort(dtos, new Comparator<StudentDto>() {
			 
            @Override
            public int compare(StudentDto o1, StudentDto o2) {
            	if(StringUtils.isNotBlank(o1.getStudent().getUnitiveCode())&&StringUtils.isNotBlank(o2.getStudent().getUnitiveCode())){
            		return o1.getStudent().getUnitiveCode().compareTo(o2.getStudent().getUnitiveCode());
            	}
            	return 0;
            }
		});
    }
		
    @ResponseBody
    @RequestMapping("/testNumber/updateTestNum")
    @ControllerInfo("批量保存")
	public String updateTestNum(String examId,SaveTestNumDto dto){
		try{
 			if(dto==null || CollectionUtils.isEmpty(dto.getStudentDtoList())){
				return error("没有需要保存的数据！");
			}
 			//查询这次考试下
 			String unitId=getLoginInfo().getUnitId();
 			List<ExamNum> list = examNumService.findByExamIdList(examId, unitId);
 			//考号：学生ids(防止历史数据已经重复啦)
 			Map<String,Set<String>> numSet=new HashMap<String, Set<String>>();
 			if(CollectionUtils.isNotEmpty(list)){
 				for(ExamNum e:list){
 					if(!numSet.containsKey(e)){
 						numSet.put(e.getExamNumber(), new HashSet<String>());
 					}
 					numSet.get(e.getExamNumber()).add(e.getStudentId());
 				}
 			}
 			List<StudentDto> allList = dto.getStudentDtoList();
 			List<StudentDto> insertList=new ArrayList<StudentDto>();
 			if(CollectionUtils.isEmpty(allList)){
 				return error("没有数据要保存！");
 			}
 			boolean flag=false;//是否有重复
 			for(StudentDto d:allList){
 				if(StringUtils.isBlank(d.getExamNum())){
 					continue;
 				}
 				if(numSet.containsKey(d.getExamNum())){
 					Set<String> s = numSet.get(d.getExamNum());
 					if(s==null || s.size()<=0){
 						insertList.add(d);
 	 					numSet.get(d.getExamNum()).add(d.getId());
 					}else{
 						if(s.size()==1 && s.contains(d.getId())){
 							insertList.add(d);
 						}else{
 							//重复
 							if(!flag){
 								flag=true;
 							}
 						}
 					}
 				}else{
 					insertList.add(d);
 					numSet.put(d.getExamNum(), new HashSet<String>());
 					numSet.get(d.getExamNum()).add(d.getId());
 				}
 			}
 			if(CollectionUtils.isEmpty(insertList)){
 				if(flag){
 					error("没有数据要保存！考号都重复。");
 				}
 				return error("没有数据要保存！");
 			}
			examNumService.save(examId,insertList);
			if(flag){
				return success("保存成功,但部分考号重复不能保存。");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("保存成功！");
	}
    
    @ResponseBody
	@RequestMapping("/testNumber/clearTestNum")
	@ControllerInfo("清除")
	public String clearTestNum(String unitId,String examId){
		ExamInfo exam = examInfoService.findOne(examId);
		if(exam==null || exam.getIsDeleted()==Constant.IS_DELETED_TRUE){
			return error("该考试不存在或已删除！");
		}
		List<SubjectInfo> infoList = subjectInfoService.findByExamIdIn(null, examId);
		if(CollectionUtils.isNotEmpty(infoList)){
			try{
				examNumService.deleteBySchoolIdExamId(unitId,examId);
			}catch (Exception e) {
				e.printStackTrace();
				return returnError("操作失败！", e.getMessage());
			}
		}else{
			return error("该考试下还没有设置考试科目！");
		}
		return success("操作成功！");
	}
	
	@ResponseBody
	@RequestMapping("/testNumber/copyTestNum")
	@ControllerInfo("复制学号到考号")
	public String copyTestNum(String unitId,String examId){
		ExamInfo exam = examInfoService.findOne(examId);
		if(exam==null || exam.getIsDeleted()==Constant.IS_DELETED_TRUE){
			return error("该考试不存在或已删除！");
		}
		List<SubjectInfo> infoList = subjectInfoService.findByExamIdIn(null, examId);
		if(CollectionUtils.isNotEmpty(infoList)){
			Set<String> courseInfoIds = new HashSet<String> ();
			for(SubjectInfo info:infoList){
				courseInfoIds.add(info.getId());
			}
			List<ClassInfo> clInfoList=classInfoService.findList(unitId,courseInfoIds.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(clInfoList)){
				Set<String> classIds = new HashSet<String> ();
				Set<String> teachClIds = new HashSet<String> ();
				for(ClassInfo info:clInfoList){
					if(ScoreDataConstants.CLASS_TYPE1.equals(info.getClassType())){
						classIds.add(info.getClassId());
					}else if(ScoreDataConstants.CLASS_TYPE2.equals(info.getClassType())){
						teachClIds.add(info.getClassId());
					}
				}
				List<Student> studentList=new ArrayList<Student>();
				//取得行政学生
				if(classIds!=null && classIds.size()>0){
					List<Student> stuList = SUtils.dt(studentService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>(){});
					studentList.addAll(stuList);
				}
				//取得教学班学生
				if(teachClIds!=null && teachClIds.size()>0){
					List<Student> stuList = SUtils.dt(studentService.findByTeachClassIds(teachClIds.toArray(new String[]{})), new TR<List<Student>>(){});
					studentList.addAll(stuList);
				}
				Set<String> stuIds=new HashSet<String>();
				Set<String> sameNum=new HashSet<String>();
				List<ExamNum> insertList=new ArrayList<ExamNum>();
				ExamNum examNum=new ExamNum();
				boolean flag=false;//是否有重复
 				if(CollectionUtils.isNotEmpty(studentList)){
 					for(Student stu:studentList){
 						if(!stuIds.contains(stu.getId())){
 							if(StringUtils.isBlank(stu.getStudentCode())){
 								continue;
 							}
 							if(sameNum.contains(stu.getStudentCode())){
 								//本身学号重复
 								if(!flag){
 									flag=true;
 								}
 								continue;
 							}
 							sameNum.add(stu.getStudentCode());
 							stuIds.add(stu.getId());
 							examNum=new ExamNum();
 							examNum.setExamNumber(stu.getStudentCode());
 							examNum.setStudentId(stu.getId());
 							examNum.setExamId(examId);
 							examNum.setSchoolId(stu.getSchoolId());
 							examNum.setId(UuidUtils.generateUuid());
 							insertList.add(examNum);
 						}
 					}
 					try{
 						if(CollectionUtils.isEmpty(insertList)){
 							return error("该考试下还没有学生！");
 						}
 						examNumService.insertList(insertList,unitId,examId);
 						if(flag){
							return success("操作成功！但该部分学生学号重复，无法保存。");
						}
 					}catch (Exception e) {
 						e.printStackTrace();
 						return returnError("操作失败！", e.getMessage());
 					}
				}
				
			}else{
				return error("该考试下还没有设置班级！");
			}
		}else{
			return error("该考试下还没有设置考试科目！");
		}
		return success("操作成功！");
	}
	
    @ResponseBody
	@RequestMapping("/testNumber/deleteTestNum")
	@ControllerInfo("单个删除")
	public String deleteTestNum(String stuId,String examId){
		try{
			examNumService.deleteByexamIdStudent(examId, new String[]{stuId});
		}catch (Exception e) {
			e.printStackTrace();
			return returnError("操作失败！", e.getMessage());
		}
		return success("");
	}
	
	
	//-------------------------考号设置结束-------------
	
	//-------------------------统计过滤设置:不排考设置,不统分设置开始-------------
	@RequestMapping("/filter/index/page")
    @ControllerInfo(value = "tab页")
    public String showTabIndex(ModelMap map, HttpSession httpSession,HttpServletRequest request) {
		String tabType= request.getParameter("tabType");
		if(StringUtils.isBlank(tabType)){
			tabType=P_1;//1:不排课 2:不统分
		}
		return "/scoremanage/filter/index.ftl";
	}
	
	@RequestMapping("/filter/indexHead/page")
    @ControllerInfo(value = "过滤设置")
    public String showFilterHead(ModelMap map, HttpSession httpSession,HttpServletRequest request) {
        List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
        	return errorFtl(map, "学年学期不存在");
		}
        LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		//遗留学年学期都没有
		if(semester==null){
			return errorFtl(map, "学年学期不存在");
		}
		//页面参数
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
		String classType=ScoreDataConstants.CLASS_TYPE1;//班级类型 默认行政班
        String tabType= request.getParameter("tabType");
        if(StringUtils.isBlank(tabType)){
			tabType=P_1;
		}
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		map.put("unitId",unitId);
		map.put("classType", classType);
		map.put("tabType", tabType);
		return "/scoremanage/filter/filterHead.ftl";	
    }
    @RequestMapping("/filter/list/page")
    @ControllerInfo("显示列表")
    public String showFilterlist( ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
        String examId= request.getParameter("examId");
        String classType= request.getParameter("classType");
        String classIdSearch= request.getParameter("classIdSearch");
        String gradeCode= request.getParameter("gradeCode");
        String arrType= request.getParameter("arrType");
        String tabType= request.getParameter("tabType");
        String unitId=loginInfo.getUnitId();
        List<Student> studentList=new ArrayList<Student>();
        Map<String, String> classMap = new LinkedHashMap<String, String>();
		Set<String> classIds = toMakeClassIds(examId, unitId,gradeCode, classType, classIdSearch);
		classMap=toMakeClassMap(classType, classIds.toArray(new String[]{}));
		if(classIds!=null && classIds.size()>0){
			 if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
				//行政班
				studentList=SUtils.dt(studentService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>(){});
			}else{
				studentList=SUtils.dt(studentService.findByTeachClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>(){});
				//组装classId
				if(CollectionUtils.isNotEmpty(studentList)){
					Map<String,List<String>> tmap=SUtils.dt(teachClassStuService.findMapWithStuIdByClassIds(classIds.toArray(new String[]{})), new TR<Map<String,List<String>>>(){});
					for(Student s:studentList){
						if(tmap.containsKey(s.getId()) && CollectionUtils.isNotEmpty(tmap.get(s.getId()))){
							s.setClassId(ArrayUtil.print(tmap.get(s.getId()).toArray(new String[]{})));//随机默认取第一个教学班
						}
					}
				}
			}
		}
		Map<String,String> filterMap1=new HashMap<String, String>();
		Map<String,String> filterMap2=new HashMap<String, String>();
		filterMap1=filtrationService.findByExamIdAndSchoolIdAndType(examId,unitId,ScoreDataConstants.FILTER_TYPE1);     
		if(P_1.equals(tabType)){
			//不排课
		}else if(P_2.equals(tabType)){
			//不统分
			filterMap2=filtrationService.findByExamIdAndSchoolIdAndType(examId,unitId,ScoreDataConstants.FILTER_TYPE2);     
		}
        List<StudentDto> stuDtoList=new ArrayList<StudentDto>();
        StudentDto dto=new StudentDto();
        if(CollectionUtils.isNotEmpty(studentList)){
        	for(Student stu:studentList){
        		dto=new StudentDto();
        		dto.setStudent(stu);
        		if(P_1.equals(tabType)){
        			if((P_1.equals(arrType)&&filterMap1.containsKey(stu.getId())) || (P_2.equals(arrType)&&!filterMap1.containsKey(stu.getId()))){
            			continue;
            		}
        		}else if(P_2.equals(tabType)){
        			//不统分
        			if(filterMap1.containsKey(stu.getId())){
            			continue;
            		}
        			if((P_1.equals(arrType)&&filterMap2.containsKey(stu.getId())) || (P_2.equals(arrType)&&!filterMap2.containsKey(stu.getId()))){
            			continue;
            		}
        		}
        		if(StringUtils.isNotBlank(stu.getClassId())){
        			String[] s=null;
        			if(stu.getClassId().indexOf(",")>0){
        				s=stu.getClassId().split(",");
        			}else{
        				s=new String[]{stu.getClassId()};
        			}
        			String classNames="";
        			for(String ss:s){
        				if(classMap.containsKey(ss)){
                			//教学班 可以显示多个
        					classNames=classNames+","+classMap.get(ss);
                		}
            		}
        			if(StringUtils.isNotBlank(classNames)){
        				classNames=classNames.substring(1);
        			}
        			dto.setClassName(classNames);
        		}
//        		if(classMap.containsKey(stu.getClassId())){
//        			dto.setClassName(classMap.get(stu.getClassId()));
//        		}
        		stuDtoList.add(dto);
        	}
        }
        map.put("examId", examId);
        map.put("arrType", arrType);
        map.put("tabType", tabType);
        map.put("stuDtoList", stuDtoList);
        return "/scoremanage/filter/filterList.ftl";
    }
    @ResponseBody
	@RequestMapping("/filter/filterSet")
	@ControllerInfo("批量设置")
	public String filterSet(String[] ids,String examId,String arrType,String tabType,ModelMap map, HttpSession session) {
	 try {
			LoginInfo loginInfo = getLoginInfo(session);
			String unitId = loginInfo.getUnitId();
			Map<String,String> filterMap=new HashMap<String, String>();
			String type="";
			if(P_1.equals(tabType)){
				//不排课
				filterMap=filtrationService.findByExamIdAndSchoolIdAndType(examId,unitId,ScoreDataConstants.FILTER_TYPE1);     
				type=ScoreDataConstants.FILTER_TYPE1;
			}else if(P_2.equals(tabType)){
				//不统分
				filterMap=filtrationService.findByExamIdAndSchoolIdAndType(examId,unitId,ScoreDataConstants.FILTER_TYPE2);     
				type=ScoreDataConstants.FILTER_TYPE2;
			}
		    List<Filtration> fList=new ArrayList<Filtration>();
			Set<String> del=new HashSet<String>();
			if(ids!=null && ids.length>0){
				if(P_1.equals(arrType)){
					//设置不排考或者不排课
					for(int i=0;i<ids.length;i++){
						String id = ids[i];
						if(!filterMap.containsKey(id)){
							Filtration f=new Filtration();
							f.setExamId(examId);
							f.setId(UuidUtils.generateUuid());
							f.setSchoolId(unitId);
							f.setStudentId(id);
							f.setType(type);
							fList.add(f);
						}
					}
				}else if(P_2.equals(arrType)){
					//撤销
					for(int i=0;i<ids.length;i++){
						String id = ids[i];
						if(filterMap.containsKey(id)){
							del.add(id);
						}
					}
				}
			}
			if(CollectionUtils.isNotEmpty(fList)){
				filtrationService.insertList(fList);
			}
			if(CollectionUtils.isNotEmpty(del)){
				filtrationService.deleteByExamIdAndStudentIdIn(examId,type,del.toArray(new String[]{}));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("操作失败！", e.getMessage());
			}
		return success("操作成功");
	}
	
	//-------------------------统计过滤设置:不排考设置,不统分设置结束-------------
  
    private Map<String,String> toMakeClassMap(String classType,String[] classIds){
		Map<String,String> classMap=new LinkedHashMap<String, String>();
		if(classIds==null || classIds.length<=0){
			return classMap;
		}
		if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
			//行政班
			List<Clazz> classList = SUtils.dt(classService.findListByIds(classIds), new TR<List<Clazz>>(){});
			if(CollectionUtils.isNotEmpty(classList)){
				for(Clazz z:classList){
					classMap.put(z.getId(), z.getClassNameDynamic());
				}
			}
		}else{
			List<TeachClass> tcalssList = SUtils.dt(teachClassService.findListByIds(classIds), new TR<List<TeachClass>>(){});
			if(CollectionUtils.isNotEmpty(tcalssList)){
				for(TeachClass t:tcalssList){
					//排除删除班级
					if(t.getIsDeleted()!=Constant.IS_TRUE){
						classMap.put(t.getId(), t.getName());
					}
					
				}
			}
		}
		return classMap;
	}
    
    private Set<String> toMakeClassIds(String examId,String unitId,String gradeCode,String classType,String classId){
		Set<String> classIds=new HashSet<String>();
		List<SubjectInfo> subjectInfoList=new ArrayList<SubjectInfo>();
        
		if(StringUtils.isNotBlank(gradeCode)){
			subjectInfoList=subjectInfoService.findByUnitIdExamId(null, examId, gradeCode);
		}else{
			subjectInfoList=subjectInfoService.findByExamIdIn(null, examId);
		}
		if(CollectionUtils.isNotEmpty(subjectInfoList)){
        	Set<String> courseInfoIds=new HashSet<>();
        	for(SubjectInfo info:subjectInfoList){
        		courseInfoIds.add(info.getId());
        	}
        	List<ClassInfo> classInfoList=classInfoService.findList(unitId,classType,classId,courseInfoIds.toArray(new String[]{}));
        	if(CollectionUtils.isNotEmpty(classInfoList)){
        		for(ClassInfo info:classInfoList){
        			classIds.add(info.getClassId());
            	}
        	}
        }
        return classIds;
	}
}	
	
	
