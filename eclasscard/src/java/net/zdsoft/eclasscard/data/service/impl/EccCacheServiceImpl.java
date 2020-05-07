package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolCalendarRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccCacheConstants;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.courseDto;
import net.zdsoft.eclasscard.data.dto.cache.ClassAttCacheDto;
import net.zdsoft.eclasscard.data.dto.cache.StudentCacheDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccAttenceGateGradeService;
import net.zdsoft.eclasscard.data.service.EccCacheService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccCacheService")
public class EccCacheServiceImpl implements EccCacheService{
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private SchoolCalendarRemoteService schoolCalendarRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private EccAttenceGateGradeService eccAttenceGateGradeService;
	
	
	@Override
	public List<StudentCacheDto> getSchoolStuListCache(String schoolId) {
		 return RedisUtils.getObject(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD + schoolId + ".student.list", 10*60, new TypeReference<List<StudentCacheDto> >() {
	        }, new RedisInterface<List<StudentCacheDto> >() {
	            @Override
	            public List<StudentCacheDto>  queryData() {
	            	List<Student> students = SUtils.dt(studentRemoteService.findBySchoolId(schoolId),new TR<List<Student>>() {});
	            	
	            	return StudentCacheDto.toStudentCacheDto(students);
	            }

	        });
	}
	
	@Override
	public List<courseDto> getCardToDayCourseSchedule(EccInfo eccInfo,Semester semester,String scheduleId) {
		return RedisUtils.getObject(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD + eccInfo.getId() + ".today.schedule.list", 5*60*60, new TypeReference<List<courseDto> >() {
	        }, new RedisInterface<List<courseDto> >() {
	            @Override
	            public List<courseDto>  queryData() {
	            	List<courseDto> courseDtos = Lists.newArrayList();
	            	String[] section = new String[]{};
	        		Calendar calendar = Calendar.getInstance();
	        		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-2;
	            	if(dayOfWeek==-1){
	            		dayOfWeek = 6;
	            	}
	            	List<CourseSchedule> todayCourses = Lists.newArrayList(); 
	        		if (EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())) {
	        			List<CourseSchedule> tcsClass = Lists.newArrayList();
//	        					SUtils.dt(
//	        					courseScheduleRemoteService.findByClassId(
//	        							eccInfo.getUnitId(), eccInfo.getClassId()),
//	        							CourseSchedule.class);
	        			Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
	        			if(clazz!=null && StringUtils.isNotBlank(clazz.getTeachPlaceId())){
	        				// 场地课表
	        				Map<String, Integer> cur2Max = SUtils.dt(schoolCalendarRemoteService.findCurrentWeekAndMaxWeek(semester.getAcadyear(), semester.getSemester()+"", eccInfo.getUnitId()),new TR<Map<String, Integer>>() {});
	        				
	        				Integer week = cur2Max.get("current");
	        				if (week != null){
	        					List<CourseSchedule> tcs = SUtils.dt(
	        							courseScheduleRemoteService.findByPlaceId(eccInfo.getUnitId(), semester.getAcadyear(), semester.getSemester(), clazz.getTeachPlaceId(), week, false),
	        							CourseSchedule.class);
	        					tcsClass.addAll(tcs);
	        				}
	        			}
	        			Set<String> tcsIds = Sets.newHashSet();
	        			for(CourseSchedule schedule:tcsClass){
	        				if(EccConstants.CLASS_TYPE_NORMAL==schedule.getClassType()&&String.valueOf(EccConstants.SUBJECT_TYPE_3).equals(schedule.getSubjectType())){
	        					continue;
	        				}
	        				if(dayOfWeek == schedule.getDayOfWeek() && !tcsIds.contains(schedule.getId())){
	        					tcsIds.add(schedule.getId());
	        					todayCourses.add(schedule);
	        				}
	        			}
	        			section = new String[]{clazz.getSection()+""};
	        		} else {
	        			// 场地课表
	        			Map<String, Integer> cur2Max = SUtils.dt(schoolCalendarRemoteService.findCurrentWeekAndMaxWeek(semester.getAcadyear(), semester.getSemester()+"", eccInfo.getUnitId()),new TR<Map<String, Integer>>() {});
	        			
	        			Integer week = cur2Max.get("current");
	        			if (week != null){
	        				List<CourseSchedule> tcs = SUtils.dt(
	        						courseScheduleRemoteService.findByPlaceId(eccInfo.getUnitId(), semester.getAcadyear(), semester.getSemester(), eccInfo.getPlaceId(), week, false),
	        						CourseSchedule.class);
	        				for(CourseSchedule schedule:tcs){
	        					if(EccConstants.CLASS_TYPE_NORMAL==schedule.getClassType()&&String.valueOf(EccConstants.SUBJECT_TYPE_3).equals(schedule.getSubjectType())){
	        						continue;
	        					}
	        					if(dayOfWeek == schedule.getDayOfWeek()){
	        						todayCourses.add(schedule);
	        					}
	        				}
	        			}
	        			
	        		}
	        		if(CollectionUtils.isNotEmpty(todayCourses)){
	        			if(section==null || section.length==0) {
	        				Set<String> classIds=EntityUtils.getSet(todayCourses, e->e.getClassId());
	        				List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), Clazz.class);
	        				Set<String> gradeIds=new HashSet<>();
	        				if(CollectionUtils.isNotEmpty(clazzList)) {
	        					gradeIds.addAll(EntityUtils.getSet(clazzList, e->e.getGradeId()));
	        				}
	        				List<TeachClass> teachClassList = SUtils.dt(teacherRemoteService.findListByIds(classIds.toArray(new String[0])), TeachClass.class);
	        				if(CollectionUtils.isNotEmpty(teachClassList)) {
	        					for(TeachClass t:teachClassList) {
	        						if(StringUtils.isNotBlank(t.getGradeId())) {
	        							gradeIds.addAll(Arrays.asList(t.getGradeId().split(",")));
	        						}
	        					}
	        				}
	        				
	        				List<Grade> gradeList=new ArrayList<Grade>();
							if(CollectionUtils.isNotEmpty(gradeIds)) {
	        					gradeList=SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), Grade.class);
	        				}
	        				if(CollectionUtils.isNotEmpty(gradeList)) {
	        					Set<String> set1 = EntityUtils.getSet(gradeList, e->(e.getSection()+""));
	        					section=set1.toArray(new String[0]);
	        				}else {
	        					String sections = schoolRemoteService.findSectionsById(eccInfo.getUnitId());
			                	section = sections.split(",");//非行政班课程对应的学段暂不确定，上课时间也不能确定
	        				}
	        				
	        			}
	        			
	        			List<StusysSectionTimeSet> stusysSectionTimeSetList = SUtils.dt(stusysSectionTimeSetRemoteService.findByAcadyearAndSemesterAndUnitId(semester.getAcadyear(), semester.getSemester(),eccInfo.getUnitId(),section,true),StusysSectionTimeSet.class);
		        	  	Map<Integer,String> secTimeMap = Maps.newHashMap();
//		        	  	Map<String,String> secTimeStrMap = Maps.newHashMap();
		        	  	//对于场地课表 高一第一节课 与高二第一节课开始时间与结束时间不一致 取一个范围大一点的
		        	  	for(StusysSectionTimeSet set:stusysSectionTimeSetList){
		        	  		//优化页面显示时间点--沿用之前的节次
//		        	  		if(StringUtils.isNotBlank(set.getPeriodInterval()) && set.getPeriod()!=null) {
//		        	  			secTimeStrMap.put(set.getPeriodInterval()+"_"+set.getPeriod(),  set.getBeginTime()+"~"+set.getEndTime());
//		        	  		}
		        	  		secTimeMap.put(set.getSectionNumber(), set.getBeginTime()+"~"+set.getEndTime());
		        	  	}
		        	  	fillCourseScheduleNames(todayCourses);
		        	  	for(CourseSchedule schedule:todayCourses){
		        	  		int periodAll = 0;
		        	  		if(EccConstants.PERIOD_MORN.equals(schedule.getPeriodInterval())){
		        	  			periodAll = schedule.getPeriod();
		        	  		}else if(EccConstants.PERIOD_AM.equals(schedule.getPeriodInterval())){
		        	  			if(schedule.getPeriod()>semester.getAmPeriods()){
		        	  				continue;
		        	  			}
		        	  			periodAll = semester.getMornPeriods()+schedule.getPeriod();
		        	  		}else if(EccConstants.PERIOD_PM.equals(schedule.getPeriodInterval())){
		        	  			if(schedule.getPeriod()>semester.getPmPeriods()){
		        	  				continue;
		        	  			}
		        	  			periodAll = semester.getMornPeriods()+semester.getAmPeriods()+schedule.getPeriod();
		        	  		}else if(EccConstants.PERIOD_NIGHT.equals(schedule.getPeriodInterval())){
		        	  			periodAll = semester.getMornPeriods()+semester.getAmPeriods()+semester.getPmPeriods()+schedule.getPeriod();
		        	  		}
		        	  		courseDto dto = new courseDto();
		        	  		dto.setAttend(false);
		        	  		dto.setName(schedule.getSubjectName());
		        	  		dto.setPeriod(schedule.getPeriod());
		        	  		dto.setPeriodInterval(schedule.getPeriodInterval());
		        	  		//修改节次显示
		        	  		dto.setPeriodName(BaseConstants.PERIOD_INTERVAL_Map2.get(schedule.getPeriodInterval())+EccUtils.getSectionName(schedule.getPeriod()));
		        	  		
		        	  		dto.setTeacherName(schedule.getTeacherName());
		        	  		
		        	  		dto.setTeacherId(schedule.getTeacherId());
		        	  		dto.setScheduleId(schedule.getId());
		        	  		//放到外围
//		        	  		if(StringUtils.isNotBlank(scheduleId)&&scheduleId.equals(schedule.getId())){
//		        	  			if(StringUtils.isNotBlank(schedule.getTeacherId())){
//		        	  				User user = SUtils.dt(userRemoteService.findByOwnerId(schedule.getTeacherId()),new TR<User>() {});
//		        	  				if (user != null) {
//		        	  					dto.setTeacherUserName(user.getUsername());
//		        	  				}
//		        	  			}
//		        	  			dto.setAttend(true);
//		        	  		}
		        	  		
//		        	  		if(StringUtils.isNotBlank(secTimeStrMap.get(schedule.getPeriodInterval()+"_"+schedule.getPeriod()))){
//		        	  			dto.setTime(secTimeStrMap.get(schedule.getPeriodInterval()+"_"+schedule.getPeriod()));
//		        	  			courseDtos.add(dto);
//		        	  		}else 
		        	  		if(StringUtils.isNotBlank(secTimeMap.get(periodAll))){
		        	  			dto.setTime(secTimeMap.get(periodAll));
		        	  			courseDtos.add(dto);
		        	  		}
		        		}
	        		}
	        	  	if(CollectionUtils.isNotEmpty(courseDtos)) {
	        	  		Collections.sort(courseDtos, new Comparator<courseDto>() {
		        			@Override
		        			public int compare(courseDto o1,courseDto o2) {
		        				if(o1.getPeriodInterval().equals(o2.getPeriodInterval())) {
		        					if(o1.getPeriod() == o2.getPeriod()) {
			        					return 0;
			        				}else if(o1.getPeriod() > o2.getPeriod()) {
			        					return 1;
			        				}else{
			        					return -1;
			        				}
		        				}else {
		        					return o1.getPeriodInterval().compareTo(o2.getPeriodInterval());
		        				}
		        				
		        			}
		        		});
	        	  	}
	        	  	
	            	return courseDtos;
	            }
	        });
	}
	
	private void fillCourseScheduleNames(List<CourseSchedule> todayCourses){
		Set<String> subjectIds = EntityUtils.getSet(todayCourses, tc -> tc.getSubjectId());
		Set<String> teacherIds = EntityUtils.getSet(todayCourses, tc -> tc.getTeacherId());
		Map<String, String> courseNameMap = new HashMap<String, String>();
		Map<String, String> teacherNameMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(subjectIds)) {
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
            if (CollectionUtils.isNotEmpty(courseList)) {
                courseNameMap =EntityUtils.getMap(courseList, cl -> cl.getId(), cl -> cl.getSubjectName());
            }
        }
        if (CollectionUtils.isNotEmpty(teacherIds)) {
        	List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])),Teacher.class);
        	teacherNameMap = EntityUtils.getMap(teachers, tec -> tec.getId(), tec -> tec.getTeacherName());
        }
        for (CourseSchedule courseSchedule : todayCourses) {
        	if (courseNameMap != null && courseNameMap.get(courseSchedule.getSubjectId()) != null) {
        		String subname = courseNameMap.get(courseSchedule.getSubjectId());
        		if(StringUtils.isNotBlank(subname)){
        			courseSchedule.setSubjectName(subname);
        		}else {
        			courseSchedule.setSubjectName(courseSchedule.getSubjectName());
        		}
        	}
        	if(teacherNameMap!=null&&teacherNameMap.containsKey(courseSchedule.getTeacherId())){
        		courseSchedule.setTeacherName(teacherNameMap.get(courseSchedule.getTeacherId()));
        	}
        }
	}

	@Override
	public ClassAttCacheDto getStuClassAttCacheDto(String studentId) {
		return RedisUtils.getObject(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD +EccCacheConstants.CLASS_ATT + studentId,ClassAttCacheDto.class);
	}

	@Override
	public void saveStuClassAttCacheDto(String studentId, ClassAttCacheDto dto) {
		RedisUtils.setObject(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD +EccCacheConstants.CLASS_ATT + studentId,dto,60*60);
	}

	@Override
	public void deleteStuClassAttCacheDto(String... studentIds) {
		for(String key:studentIds){
			key = EccCacheConstants.CACHE_SERVICE_CACHE_HEAD +EccCacheConstants.CLASS_ATT +key;
		}
		RedisUtils.del(studentIds);
	}

	@Override
	public String getInOutPeroidIdCache(String schoolId, String gradeCode) {
		Calendar calendar = Calendar.getInstance();
		String time = DateUtils.date2String(calendar.getTime(), "HH:mm");
		//启动时或凌晨定时器放入缓存的数据
		List<EccAttenceGateGrade> grades = RedisUtils.getObject(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD +EccCacheConstants.CLASS_ATT + schoolId,new TypeReference<List<EccAttenceGateGrade>>(){});
		
		if(CollectionUtils.isEmpty(grades)){//redis flush 只能取数据及时数据
			grades = eccAttenceGateGradeService.findByInOutAndCode(schoolId, gradeCode);
		}
		for(EccAttenceGateGrade grade:grades){
			if(org.apache.commons.lang3.StringUtils.equals(gradeCode, grade.getGrade()) 
					&& EccUtils.addTimeStr(grade.getBeginTime()).compareTo(time)<=0 
					&& EccUtils.addTimeStr(grade.getEndTime()).compareTo(time)>0){
				return grade.getPeriodId();
			}
		}
		return "";
	}
	

	@Override
	public List<EccAttenceGateGrade> getInOutCacheByPeroidId(String periodId,String schoolId) {
		List<EccAttenceGateGrade> grades = RedisUtils.getObject(EccCacheConstants.CACHE_SERVICE_CACHE_HEAD +EccCacheConstants.CLASS_ATT + schoolId,new TypeReference<List<EccAttenceGateGrade>>(){});
		if(CollectionUtils.isEmpty(grades)){//redis flush 只能取数据及时数据
			return grades = eccAttenceGateGradeService.findByClassifyAndPeriodId(EccConstants.ECC_CLASSIFY_2, periodId);
		}
		return grades.stream().filter(g ->StringUtils.equals(periodId, g.getPeriodId())).collect(Collectors.toList());
	}

}
