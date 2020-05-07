package net.zdsoft.tutor.data.action.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.tutor.data.dto.TutorShowStuDto;
import net.zdsoft.tutor.data.entity.TutorRecord;
import net.zdsoft.tutor.data.entity.TutorResult;
import net.zdsoft.tutor.data.service.TutorRecordService;
import net.zdsoft.tutor.data.service.TutorResultService;

/**
 * @author yangsj  2017年9月15日下午1:56:03
 * 导师学生管理
 */
@Controller
@RequestMapping("/tutor/manage")
public class TutorManageAction extends BaseAction {
	@Autowired
	private TutorResultService tutorResultService;
	@Autowired
	private TutorRecordService tutorRecordService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired 
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	
	@RequestMapping("/showIndex")
	@ControllerInfo("页面的第一次跳转")
	public String showIndex(){
		
		return "/tutor/teacher/manage/manageShowIndex.ftl";
	}
	
	@RequestMapping("/showStudentList")
    @ControllerInfo("查看学生列表")
	public String showStudentList(ModelMap map){
		
		List<TutorShowStuDto>  listSSDto = new ArrayList<TutorShowStuDto>();
    	try {
    		//得出当前导师下的所有学生
			List<Student> students = getTutorAllStudent();
			for(Student student:students){
				if(student.getIsDeleted()==1){
					student.setStudentName(student.getStudentName()+"（已删除）");
				}else if(student.getIsLeaveSchool()==1){
					student.setStudentName(student.getStudentName()+"（已毕业）");
				}
			}
			List<String> studentIds1  = EntityUtils.getList(students, Student::getId);
			Map<String, Student> sMap = EntityUtils.getMap(students, Student::getId);
			if(CollectionUtils.isNotEmpty(studentIds1)) {
				//得到所有的学生记录
			    List<TutorRecord> listTRe = tutorRecordService.findByStudentIds(studentIds1.toArray(new String[studentIds1.size()]));
			    Map<String, List<TutorRecord>> trListMap = EntityUtils.getListMap(listTRe, TutorRecord::getStudentId,Function.identity());
				
				Map<String, String> scMap = EntityUtils.getMap(students, Student::getId, Student::getClassId);
				//得到当前的班级的id
				List<String> clazzIds = EntityUtils.getList(students, Student::getClassId);
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(clazzIds.toArray(new String[clazzIds.size()])), Clazz.class);
				Map<String, Clazz> cMap = EntityUtils.getMap(clazzs, Clazz::getId);
				//得到所有的gradeids
//				List<String> gradeIds = EntityUtils.getList(clazzs, Clazz::getGradeId);
//				removeNull(gradeIds);
				Set<String> gradeIds = EntityUtils.getSet(clazzs, Clazz::getGradeId);
				Map<String, String> gnMap = new HashMap<String, String>();
				if (CollectionUtils.isNotEmpty(gradeIds) && gradeIds.size() > 0) {
					List<Grade> grades = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()])), Grade.class);
					gnMap = EntityUtils.getMap(grades, Grade::getId, Grade::getGradeName);
				}
				//得到所有的班主任老师姓名
//			    List<String> teacherIds = EntityUtils.getList(clazzs, Clazz::getTeacherId);
//			    removeNull(teacherIds);
			    Set<String> teacherIds = EntityUtils.getSet(clazzs, Clazz::getTeacherId);
				Map<String, String> tnMap = new HashMap<String, String>();
				if (CollectionUtils.isNotEmpty(teacherIds) && teacherIds.size() > 0) {
					 List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])), Teacher.class);
					 tnMap = EntityUtils.getMap(teachers, Teacher::getId, Teacher::getTeacherName);
				}
			    for (String studentId : studentIds1) {
			    	TutorShowStuDto tssd =new TutorShowStuDto();
			    	String classId = scMap.get(studentId);
			    	TutorRecord tutorRecord = trListMap.isEmpty() || CollectionUtils.isEmpty(trListMap.get(studentId)) ? new TutorRecord() : trListMap.get(studentId).get(0);
			    	tssd.setTutorRecord(tutorRecord);
			    	tssd.setStudent(sMap.get(studentId));
			    	tssd.setClassMaster(tnMap.get(cMap.get(classId).getTeacherId()));
			    	tssd.setGcName(gnMap.get(cMap.get(classId).getGradeId())+cMap.get(classId).getClassName());
			    	listSSDto.add(tssd);
				}	
			}
		map.put("ShowStuList", listSSDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "/tutor/teacher/manage/manageStudentList.ftl";
	}

	@RequestMapping("/showStudentIndex")
	@ControllerInfo("展示导师列表")
	public String showStudentIndex(ModelMap map){
		
		return "/tutor/teacher/manage/manageStudentIndex.ftl";
	}
	
	
	@RequestMapping("/showRecordIndex")
	@ControllerInfo("展示学生的导师记录")
	public String showRecordIndex(String studentId,ModelMap map){
		//当前的学年学期
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        //得出当前导师下的所有学生
        List<Student> studentList = getTutorAllStudent();
        map.put("semester", semester); 
		map.put("acadyearList", acadyearList);
        map.put("studentId", studentId);
        map.put("studentList", studentList);
		return "/tutor/teacher/manage/showRecordIndex.ftl";
	}
	
	@RequestMapping("/showRecordList")
    @ControllerInfo("查看学生的导师记录")
	public String showRecordList(ModelMap map,String acadyear,String semester,String studentId){
    	//当前的学年学期
    	Semester semester1 = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
    	if(StringUtils.isBlank(semester) && StringUtils.isBlank(semester)) {
    		acadyear = semester1.getAcadyear();
    		semester = String.valueOf(semester1.getSemester());
    	}
		List<TutorRecord> listTR = tutorRecordService.findByAllSIdAndSemester(studentId,acadyear,semester);
		for (TutorRecord tutorRecord : listTR) {
			if(!tutorRecord.getUnitId().equals(getLoginInfo().getUnitId())) {
				listTR.remove(tutorRecord);
			}
		}
		if(CollectionUtils.isNotEmpty(listTR)) {
			String teacherId = listTR.get(0).getTeacherId();
			Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
			//得到老师的姓名
			for (TutorRecord tutorRecord : listTR) {
				tutorRecord.setTeacherName(teacher.getTeacherName());
			}
			Map<String, List<TutorRecord>> trMap = EntityUtils.getListMap(listTR, TutorRecord::getRecordType, Function.identity());
			
			map.put("trMap", trMap);
			map.put("tManage", true);
		}else {
			map.put("messageEmpty", "暂无记录");
		}
    	return "/tutor/tutorrecord/showRecordList.ftl";
	}
	

	/**
	 * @return
	 */
	private List<Student> getTutorAllStudent() {
		User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true),User.class);
		//得出当前导师下的所有学生
		String teacherId = user.getOwnerId();
		List<TutorResult> listTR = tutorResultService.findByTeacherId(teacherId);
		List<String> studentIds = EntityUtils.getList(listTR, TutorResult::getStudentId);
		List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[studentIds.size()])), Student.class);
		return students;
	}
	
}
