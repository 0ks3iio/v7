package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.eis.remote.entity.StudocMannerRecordDto;
import net.zdsoft.eis.remote.service.ExamRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.dto.StuDevelopMannerRecordListDto;
import net.zdsoft.studevelop.data.entity.StuDevelopMannerRecord;
import net.zdsoft.studevelop.data.service.StuDevelopMannerRecordService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop")
public class StuDevelopMannerRecordAction extends BaseAction{

	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private StuDevelopMannerRecordService stuDevelopMannerRecordService;
	@Autowired
	private StudentRemoteService studenteRemoteService;
	
	private List<ClassTeaching> classTeachingList;
	//private List<Student> studentList;	
	
	private ExamRemoteService examRemoteService;
	
	@RequestMapping("/mannerRecord/index/page")
    @ControllerInfo(value = "考试成绩录入搜索区")
	public String showIndex(ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        //List<Clazz> classList = SUtils.dt(classService.findByAllSchoolId(getLoginInfo().getUnitId()), new TR<List<Clazz>>(){});
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),semester.getAcadyear()), new TR<List<Clazz>>() {});
        /*if(CollectionUtils.isNotEmpty(classList)){
        	getSubjectList(semester.getAcadyear(), String.valueOf(semester.getSemester()), new String[]{classList.get(0).getId()});
        	getStudentList(classList.get(0).getId());
        }*/
        /*if(CollectionUtils.isEmpty(classTeachingList)){
        	//TODO
        }*/
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("classList", classList);
        map.put("classTeachingList", classTeachingList);
        //map.put("studentList", studentList);
		return "/studevelop/record/mannerRecordAdmin.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/mannerRecord/clsList")
	public List<Clazz> clsList(String acadyear){
		return SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
	}
	
	@ResponseBody
	@RequestMapping("/mannerRecord/subjectList")
	public List<ClassTeaching> subjectList(String queryAcadyear, String querySemester, String classId){
		return getSubjectList(queryAcadyear, querySemester, new String[]{classId});
	}
	
	/*public void getStudentList(String classId){
		studentList = SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
	}*/
	
	@ResponseBody
	@RequestMapping("/mannerRecord/studentList")
	public List<Student> studentList(String classId){
		return SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
	}
	
	@RequestMapping("/mannerRecord/tablist")
    @ControllerInfo(value = "考试成绩录入列表")
	public String showList(String queryAcadyear, String querySemester, String classId, String subjectId, ModelMap map){
		map.put("modelType", "1");//按班级查询
		classTeachingList = getSubjectList(queryAcadyear, querySemester, new String[]{classId});
		if(CollectionUtils.isEmpty(classTeachingList) || StringUtils.isBlank(subjectId)){
			return "/studevelop/record/mannerRecordList.ftl";
        }
		List<Student> stuList = SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
		Set<String> stuIdSet = new HashSet<String>();
		for(Student stu : stuList){
			stuIdSet.add(stu.getId());
		}		
		if(CollectionUtils.isEmpty(stuIdSet)){
			return "/studevelop/record/mannerRecordList.ftl";
		}
		List<StuDevelopMannerRecord> mannerRecordListTemp = stuDevelopMannerRecordService.findListByCls(queryAcadyear, querySemester, subjectId, stuIdSet.toArray(new String[0]));
		Map<String,StuDevelopMannerRecord> mannRecMap = new HashMap<String,StuDevelopMannerRecord>();
		for(StuDevelopMannerRecord item : mannerRecordListTemp){
			mannRecMap.put(item.getStudentId(), item);
		}
		List<StuDevelopMannerRecord> mannerRecordList = new ArrayList<StuDevelopMannerRecord>();
		for(Student stu : stuList){
			StuDevelopMannerRecord mannRec = new StuDevelopMannerRecord();	
			mannRec.setStudentId(stu.getId());
			mannRec.setStudentName(stu.getStudentName());	
			mannRec.setAcadyear(queryAcadyear);
			mannRec.setSemester(querySemester);
			mannRec.setSubjectId(subjectId);
			StuDevelopMannerRecord mannRecTemp = mannRecMap.get(stu.getId());
			if(null!=mannRecTemp){
				mannRec.setId(mannRecTemp.getId());
				mannRec.setManner(mannRecTemp.getManner());
				mannRec.setDiscovery(mannRecTemp.getDiscovery());
				mannRec.setCommunication(mannRecTemp.getCommunication());
			}
			mannerRecordList.add(mannRec);
		}
		examRemoteService = getExamRemoteService();
		if (examRemoteService != null) {
			//List<StudocMannerRecordDto> studocMannerRecordDtoList = examRemoteService.getStudocMannerRecordForClass("4028809248AC68DB0148AC7755DD00E4", "4028803D53794D4301537965DA9D0002", queryAcadyear, querySemester);
			List<StudocMannerRecordDto> studocMannerRecordDtoList = examRemoteService.getStudocMannerRecordForClass(classId, subjectId, queryAcadyear, querySemester);
			for(StuDevelopMannerRecord item1 : mannerRecordList){
				for(StudocMannerRecordDto item2 : studocMannerRecordDtoList){
					if(item1.getStudentId().equals(item2.getStudentId())){
						item1.setQzScore(item2.getQzScore());
						item1.setQmScore(item2.getQmScore());
						item1.setZpScore(item2.getZpScore());
					}
				}
			}
		}
		map.put("mannerRecordList", mannerRecordList);
		return "/studevelop/record/mannerRecordList.ftl";
	}
	
	@RequestMapping("/mannerRecord/stuRecblist")
    @ControllerInfo(value = "学生考试成绩录入列表")
	public String showStuRecList(String queryAcadyear, String querySemester, String classId, String studentId, String subjectId, ModelMap map){
		classTeachingList = getSubjectList(queryAcadyear, querySemester, new String[]{classId});
		List<StuDevelopMannerRecord> mannerRecordListTemp = stuDevelopMannerRecordService.findListByStu(queryAcadyear, querySemester, studentId, subjectId);
		Map<String,StuDevelopMannerRecord> mannRecMap = new HashMap<String,StuDevelopMannerRecord>();
		for(StuDevelopMannerRecord item : mannerRecordListTemp){
			mannRecMap.put(item.getSubjectId(), item);
		}
		List<StuDevelopMannerRecord> mannerRecordList = new ArrayList<StuDevelopMannerRecord>();
		List<ClassTeaching> classTeachingTemp = new ArrayList<ClassTeaching>();
		if(StringUtils.isNotBlank(subjectId)){
			for(ClassTeaching item : classTeachingList){
				if(subjectId.equals(item.getSubjectId())){
					classTeachingTemp.add(item);
				}
			}
			classTeachingList = classTeachingTemp;
		}		
		for(ClassTeaching item : classTeachingList){
			StuDevelopMannerRecord mannRec = new StuDevelopMannerRecord();	
			mannRec.setStudentId(studentId);	
			mannRec.setAcadyear(queryAcadyear);
			mannRec.setSemester(querySemester);
			mannRec.setSubjectId(item.getSubjectId());
			mannRec.setSubjectName(item.getSubjectName());
			StuDevelopMannerRecord mannRecTemp = mannRecMap.get(item.getSubjectId());
			if(null!=mannRecTemp){
				mannRec.setId(mannRecTemp.getId());
				mannRec.setManner(mannRecTemp.getManner());
				mannRec.setDiscovery(mannRecTemp.getDiscovery());
				mannRec.setCommunication(mannRecTemp.getCommunication());
			}
			mannerRecordList.add(mannRec);
		}
		examRemoteService = getExamRemoteService();
		if (examRemoteService != null) {
			//List<StudocMannerRecordDto> studocMannerRecordDtoList = examRemoteService.getStudocMannerRecord("402880FD49EEE7510149EEF602790008", queryAcadyear, querySemester);
			List<StudocMannerRecordDto> studocMannerRecordDtoList = examRemoteService.getStudocMannerRecord(studentId, queryAcadyear, querySemester);
			for(StuDevelopMannerRecord item1 : mannerRecordList){
				for(StudocMannerRecordDto item2 : studocMannerRecordDtoList){
					if(item1.getSubjectId().equals(item2.getSubjectId())){
						item1.setQzScore(item2.getQzScore());
						item1.setQmScore(item2.getQmScore());
						item1.setZpScore(item2.getZpScore());
					}
				}
			}
		}
		map.put("mannerRecordList", mannerRecordList);
		map.put("modelType", "2");//按学生查询
		return "/studevelop/record/mannerRecordList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/mannerRecord/save")
    @ControllerInfo(value = "保存数据")
	public String save(StuDevelopMannerRecordListDto mannerRecordListDto){
		try{
			stuDevelopMannerRecordService.bacthSave(mannerRecordListDto.getMannerRecordList());
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	public List<ClassTeaching> getSubjectList(String acadyear, String semester, String[] classIds){
		classTeachingList =  SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, classIds), new TR<List<ClassTeaching>>(){});
	    Set<String> subjectIdSet = new HashSet<String>();
		for(ClassTeaching clsTeach : classTeachingList){
			subjectIdSet.add(clsTeach.getSubjectId());
	    }
		if(CollectionUtils.isNotEmpty(classTeachingList)){
			List<Course> courseList =  SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdSet.toArray(new String[0])), new TR<List<Course>>(){});
			Map<String, String> courseMap = new HashMap<String, String>();		
			for(Course course : courseList){
				courseMap.put(course.getId(), course.getSubjectName());
			}
			for(ClassTeaching clsTeach : classTeachingList){
				clsTeach.setSubjectName(courseMap.get(clsTeach.getSubjectId()));
		    }
		}
		return classTeachingList;
	}

	public SemesterRemoteService getSemesterService() {
		return semesterRemoteService;
	}

	public void setSemesterService(SemesterRemoteService semesterService) {
		this.semesterRemoteService = semesterService;
	}
	
	public ExamRemoteService getExamRemoteService() {
		if(examRemoteService == null){
			examRemoteService = Evn.getBean("examRemoteService");
			log.error("教务成绩duddo服务没有开启，没有提供者");
		}
		return examRemoteService;
	}

}
