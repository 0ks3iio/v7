package net.zdsoft.basedata.remote.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("courseScheduleRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class CourseScheduleRemoteServiceImpl extends BaseRemoteServiceImpl<CourseSchedule,String> implements
		CourseScheduleRemoteService {

	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private SchoolCalendarService schoolCalendarService;
	@Autowired
	private TeacherService teacherService;
	
	@Override
	public String findByTeacherId(String schoolId, String searchAcadyear, Integer searchSemester, String teacherId, Integer week) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByTeacherId(searchAcadyear, searchSemester, teacherId, week));
	}

	@Override
	public String findByClassId(String schoolId, String searchAcadyear, Integer searchSemester, String classId, Integer week) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByClassId(searchAcadyear, searchSemester, classId, week));
	}
	
	@Override
	public String findByClassIdes(String schoolId, String searchAcadyear, Integer searchSemester, String[] classId, Integer week) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByClassIdes(searchAcadyear, searchSemester, classId, week));
	}

	@Override
	protected BaseService<CourseSchedule, String> getBaseService() {
		return courseScheduleService;
	}

	@Override
	public String findByTeacherId(String schoolId, String teacherId) {
		Semester semester = semesterService.getCurrentSemester(0);
		if (semester == null)
			return SUtils.s(new ArrayList<CourseSchedule>());

		 Map<String, Integer> cur2Max = schoolCalendarService.findCurrentWeekAndMaxWeek(semester.getAcadyear(), semester.getSemester()+"", schoolId);
		 
		 Integer week = cur2Max.get("current");
		if (week == null)
			return SUtils.s(new ArrayList<CourseSchedule>());

		return SUtils.s(courseScheduleService.findCourseScheduleListByTeacherId(semester.getAcadyear(), semester.getSemester(), teacherId,
				week ));
	}
	
	@Override
	public String findBySubjectId(String schoolId, String subjectId) {
		// TODO Auto-generated method stub
		Semester semester = semesterService.getCurrentSemester(0);
		if (semester == null)
			return SUtils.s(new ArrayList<CourseSchedule>());
		return SUtils.s(courseScheduleService.findCourseScheduleListBySubjectId(schoolId,semester.getAcadyear(), semester.getSemester(), subjectId));
	}
	@Override
	public String findByClassId(String schoolId, String classId) {
		Semester semester = semesterService.getCurrentSemester(0);
		if (semester == null)
			return SUtils.s(new ArrayList<CourseSchedule>());
		
		Map<String, Integer> cur2Max = schoolCalendarService.findCurrentWeekAndMaxWeek(semester.getAcadyear(), semester.getSemester()+"", schoolId);
		
		Integer week = cur2Max.get("current");
		if (week == null)
			return SUtils.s(new ArrayList<CourseSchedule>());
		
		return SUtils.s(courseScheduleService.findCourseScheduleListByClassId(semester.getAcadyear(), semester.getSemester(), classId,
				week ));
	}

	@Override
	public String getByCourseScheduleDto(CourseScheduleDto dto) {
		return SUtils.s(courseScheduleService.getByCourseScheduleDto(dto));
	}

	@Override
	public void deleteCourseByIds(String[] ids) {
		courseScheduleService.deleteByIds(ids);
		
	}

	@Override
	public void saveAll(String courseSchedule) {
		CourseSchedule[] cs = SUtils.dt(courseSchedule, new TR<CourseSchedule[]>() {
        });
        if (ArrayUtils.isNotEmpty(cs)) {
        	courseScheduleService.saveAllEntitys(cs);
        }
	}

	@Override
	public String findCourseScheduleListByPerId(String acadyear, Integer semester, Integer week, String ownerId, String s) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByPerId(acadyear,semester,week,ownerId,s));
	}
	
	@Override
	public String findCourseScheduleListByPerId(String acadyear, Integer semester, String startDate, String endDate, String ownerId, String s) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByPerId(acadyear,semester,startDate, endDate,ownerId,s));
	}
	
	@Override
	public String findCourseScheduleMapByObjId(String acadyear,
			Integer semester, String type, String ownerId) {
		return SUtils.s(courseScheduleService.findCourseScheduleMapByObjId(acadyear,semester,type,ownerId));
	}
	
	@Override
	public String findCourseScheduleMap(String schoolId, String acadyear,
			Integer semester) {
		return SUtils.s(courseScheduleService.findCourseScheduleMap(schoolId,acadyear,semester));
	}
	
	@Override
	public String findCourseScheduleList(String searchAcadyear, Integer searchSemester, String schoolId,
			Integer weekOfWorktime, Integer dayOfWeek, String periodInterval, Integer punchCard,Integer period) {
		return SUtils.s(courseScheduleService.findCourseScheduleList(searchAcadyear, searchSemester, schoolId, weekOfWorktime, dayOfWeek, periodInterval, punchCard,period));
	}

	@Override
	public void deleteByClassId(CourseScheduleDto deleteDto) {
		courseScheduleService.deleteByClassId(deleteDto);
	}

	@Override
	public String findByPlaceId(String schoolId, String searchAcadyear,
			Integer searchSemester, String placeId, Integer week,boolean ismakeTeacher) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByPlaceId(schoolId,searchAcadyear, searchSemester, placeId, week,ismakeTeacher));
	}
	
	@Override
	public String findByPlaceId(String schoolId, String searchAcadyear,
			Integer searchSemester, String placeId, String startDate, String endDate,boolean ismakeTeacher) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByPlaceId(schoolId,searchAcadyear, searchSemester, placeId, startDate, endDate,ismakeTeacher));
	}

	@Override
	public String findTeachersByGradeIdAndSubjectId(String schoolId,String acadyear,
			Integer semester,String gradeId, String subjectId) {
		List<CourseSchedule> courseScheduleList = courseScheduleService.findBySchoolIdAndGradeIdAndSubjectId(schoolId,acadyear,semester,gradeId,subjectId);
		if(CollectionUtils.isNotEmpty(courseScheduleList)){
			return SUtils.s(teacherService.findListByIds(EntityUtils.getSet(courseScheduleList, CourseSchedule::getTeacherId).toArray(new String[0])));
		}
		return null;
	}

	@Override
	public String findCourseScheduleListByTeacherIdIn(String searchAcadyear, Integer searchSemester, Integer week, String[] teacherIds) {
		return SUtils.s(courseScheduleService.findCourseScheduleListByTeacherIdIn(searchAcadyear, searchSemester, week, teacherIds));
	}

	@Override
	public String findCourseScheduleMapByTeacherId(String acadyear, String semester, String[] teacherIds, Integer week) {
		return SUtils.s(courseScheduleService.findCourseScheduleMapByTeacherId(acadyear,semester,teacherIds,week));
	}

	@Override
	public String findByTeacherIdsInWeekGte(String schoolId, String acadyear, Integer semester,
			String[] teacherIds, Integer week) {
		return SUtils.s(courseScheduleService.findListByTeacherIdsInWeekGte(schoolId,acadyear,semester,teacherIds,week));
	}

	@Override
	public String findByModifyTime(String[] schoolId,String acadyear, Integer semester,
			Date modifyTime, Pagination page) {
		return SUtils.s(courseScheduleService.findByModifyTime(schoolId, acadyear, semester, modifyTime, page));
	}

	@Override
	public String findCourseScheduleMapByObjId(String acadyear, Integer semester, String type, Integer week,String ownerId) {
		return SUtils.s(courseScheduleService.findCourseScheduleMapByObjId(acadyear,semester,type,week,ownerId));
	}
}
