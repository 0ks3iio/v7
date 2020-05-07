package net.zdsoft.tutor.data.action.family;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.tutor.data.entity.TutorRecord;
import net.zdsoft.tutor.data.service.TutorRecordService;

/**
 * @author yangsj  2017年9月14日下午3:01:48
 */
@Controller
@RequestMapping("/tutor/family")
public class TutorFamilyAction extends BaseAction {
	
	@Autowired
	private TutorRecordService tutorRecordService;
	@Autowired 
	private UserRemoteService userRemoteService;
	@Autowired 
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	
	
    @RequestMapping("/showRecordList")
    @ControllerInfo("查看孩子的导师记录")
	public String showRecordList(ModelMap map,String acadyear,String semester){
    	User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true),User.class);
		Family family = SUtils.dc(familyRemoteService.findOneById(user.getOwnerId()),Family.class);
		Map<String, List<TutorRecord>> trMap = Maps.newHashMap();
		List<TutorRecord> listTR = tutorRecordService.findByFamIdAndSemester(family.getStudentId(),acadyear,semester);
		if(CollectionUtils.isNotEmpty(listTR)) {
			String teacherId = listTR.get(0).getTeacherId();
			Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
			//得到老师的姓名
			for (TutorRecord tutorRecord : listTR) {
				tutorRecord.setTeacherName(teacher.getTeacherName());
			}
			trMap = EntityUtils.getListMap(listTR, TutorRecord::getRecordType, Function.identity());
		}
		map.put("trMap", trMap);
    	return "/tutor/tutorrecord/showRecordList.ftl";
	}
	
    @RequestMapping("/showRecordIndex")
    @ControllerInfo("查看孩子的导师记录")
	public String showRecordIndex(ModelMap map){
    	User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true),User.class);
    	//当前的学年学期
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
		Family family = SUtils.dc(familyRemoteService.findOneById(user.getOwnerId()),Family.class);
		Student student = Student.dc(studentRemoteService.findOneById(family.getStudentId()));
    	List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		map.put("acadyearList", acadyearList);
    	map.put("semester", semester);
    	map.put("studentName", student.getStudentName());
    	return "/tutor/family/showRecordIndex.ftl";
	}
}
