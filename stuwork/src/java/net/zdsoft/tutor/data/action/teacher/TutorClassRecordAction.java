package net.zdsoft.tutor.data.action.teacher;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.tutor.data.entity.TutorRecord;
import net.zdsoft.tutor.data.service.TutorRecordService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;

/**
 * @author yangsj  2017年11月22日上午11:13:58
 */
@Controller
@RequestMapping("/tutor/class/record")
public class TutorClassRecordAction extends BaseAction {
   
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TutorRecordService tutorRecordService;
	
	
	@RequestMapping("/index")
	public String allrecordTeaHead(ModelMap map){
		//当前的学年学期
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        map.put("semester", semester); 
		map.put("acadyearList", acadyearList);
		return "/tutor/teacher/class/tutorClassIndex.ftl";
	}
	
	@RequestMapping("/student/record/list")
	public String allrecordStuList(String studentId,String acadyear,String semester,ModelMap map){
		Map<String, List<TutorRecord>> trMap = Maps.newHashMap();
		if(StringUtils.isNotBlank(studentId)){
			List<TutorRecord> listTR = tutorRecordService.findByAllSIdAndSemester(studentId,acadyear,semester);
			if(CollectionUtils.isNotEmpty(listTR)){
				String teacherId = listTR.get(0).getTeacherId();
				Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
				//得到老师的姓名
				for (TutorRecord tutorRecord : listTR) {
					tutorRecord.setTeacherName(teacher.getTeacherName());
				}
				trMap = EntityUtils.getListMap(listTR, TutorRecord::getRecordType, Function.identity());
			}
		}
		map.put("trMap", trMap);
		return "/tutor/tutorrecord/showRecordList.ftl";
	}
}
