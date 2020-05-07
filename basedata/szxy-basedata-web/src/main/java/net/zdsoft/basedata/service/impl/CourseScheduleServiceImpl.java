package net.zdsoft.basedata.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.CourseScheduleDao;
import net.zdsoft.basedata.dao.CourseScheduleJdbcDao;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.ClassTeachingEx;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.TeachPlan;
import net.zdsoft.basedata.entity.TeachPlanEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.AdjustedService;
import net.zdsoft.basedata.service.ClassHourExService;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingExService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeachPlanExService;
import net.zdsoft.basedata.service.TeachPlanService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiNewElectiveService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("CourseScheduleService")
public class CourseScheduleServiceImpl extends BaseServiceImpl<CourseSchedule, String> implements CourseScheduleService {

	@Autowired
	private CourseScheduleDao courseScheduleDao;
	@Autowired
	private CourseScheduleJdbcDao courseScheduleJdbcDao;
	@Autowired
	private ClassService classService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TeachClassStuService teachClassStuService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeachPlaceService teachPlaceService;
	@Autowired
	private StudentService studentService;
	@Autowired(required=false)
	private OpenApiNewElectiveService openApiNewElectiveService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private TeachPlanService teachPlanService;
	@Autowired
	private TeachPlanExService teachPlanExService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private ClassTeachingExService classTeachingExService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private SchoolCalendarService schoolCalendarService;
	@Autowired
	private AdjustedService adjustedService;
	@Autowired
	private ClassHourService classHourService;
	@Autowired
	private ClassHourExService classHourExService;
	
	@Override
	public List<CourseSchedule> findCourseScheduleListByClassId(String searchAcadyear, Integer searchSemester, String classId, Integer week) {
		if(week==null){
			return courseScheduleDao.findCourseScheduleListByClassId(searchAcadyear, searchSemester, classId);
		}
		return courseScheduleDao.findCourseScheduleListByClassId(searchAcadyear, searchSemester, classId, week);
	}
	@Override
	public List<CourseSchedule> findCourseScheduleListBySubjectId(
			String schoolId, String acadyear, Integer semester, String subjectId) {
		return courseScheduleDao.findCourseScheduleListBySubjectId(schoolId,acadyear, semester, subjectId);
	}
	@Override
	public List<CourseSchedule> findCourseScheduleListByTeacherId(String searchAcadyear, Integer searchSemester, String teacherId, Integer week) {
		//取正在使用的教学班
		List<CourseSchedule> timetableCourseScheduleList = courseScheduleJdbcDao.findCourseScheduleListByTeacherId(searchAcadyear,searchSemester, teacherId, week);
		if(timetableCourseScheduleList==null){
			timetableCourseScheduleList=new ArrayList<CourseSchedule>();
        }
		List<CourseSchedule> timetableCourseScheduleFuList = courseScheduleJdbcDao.findCourseScheduleListByFuTeacherId(searchAcadyear,searchSemester, teacherId, week);
        if(CollectionUtils.isNotEmpty(timetableCourseScheduleFuList)){
        	timetableCourseScheduleList.addAll(timetableCourseScheduleFuList);
        }
		return timetableCourseScheduleList;
	}

	@Override
	protected BaseJpaRepositoryDao<CourseSchedule, String> getJpaDao() {
		return courseScheduleDao;
	}

	@Override
	protected Class<CourseSchedule> getEntityClass() {
		return CourseSchedule.class;
	}

	@Override
	public List<CourseSchedule> getByCourseScheduleDto(CourseScheduleDto dto) {
		return courseScheduleJdbcDao.getByCourseScheduleDto(dto);
	}

	@Override
	public void deleteByIds(final String[] ids) {
		if(ids.length>0){
			if (ids.length<= 1000) {
				courseScheduleDao.deleteByIds(ids);
			} else {
				int cyc = ids.length / 1000 + (ids.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > ids.length)
						max = ids.length;
					courseScheduleDao.deleteByIds(ArrayUtils.subarray(ids, i * 1000, max));
				}
			}
		}
	}

	@Override
	public List<CourseSchedule> saveAllEntitys(CourseSchedule... cs) {
		 return courseScheduleDao.saveAll(checkSave(cs));
	}

	@Override
	public List<CourseSchedule> findCourseScheduleListByClassIdes(
			String searchAcadyear, Integer searchSemester, String[] classId,
			Integer week) {
		List<CourseSchedule> courseScheduleList = new ArrayList<CourseSchedule>();
    	if(classId!=null && classId.length>0){
			Specification<CourseSchedule> s = new Specification<CourseSchedule>() {
				@Override
				public Predicate toPredicate(Root<CourseSchedule> root,
						CriteriaQuery<?> cq, CriteriaBuilder cb) {
					 List<Predicate> ps = new ArrayList<Predicate>();
					 queryIn("classId", classId, root, ps, cb);
					 ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
					 ps.add(cb.equal(root.get("acadyear").as(String.class), searchAcadyear));
					 ps.add(cb.equal(root.get("semester").as(Integer.class), searchSemester));
					 if(week!=null){
						 ps.add(cb.equal(root.get("weekOfWorktime").as(Integer.class), week));
					 }
					 cq.where(ps.toArray(new Predicate[0]));
					 return cq.getRestriction();
				}
	        };
	        courseScheduleList = courseScheduleDao.findAll(s);
    	}
        return courseScheduleList;
//		if(ArrayUtils.isNotEmpty(classId)){
//			if(week == null){
//				return courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, searchSemester, classId);
//			}else{
//			return courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, searchSemester, week,classId);
//			}
//		}else{
//			return new ArrayList<CourseSchedule>();
//		}
		
	}

	@Override
	public List<CourseSchedule> findCourseScheduleListByPerId(String searchAcadyear,
			Integer searchSemester, Integer week, String perId,
			String type) {
		List<CourseSchedule> timetableCourseScheduleList=new ArrayList<CourseSchedule>();
		Map<String, String> classId2Name = new HashMap<String, String>();
		Set<String> gradeIds = new HashSet<String>();
		List<TeachClass> teachClasses=new ArrayList<TeachClass>();
		if("1".equals(type)){//教师
	        // 早上下晚时间段的 课程数
	        // 查出教师所有行政班任课信息
	        timetableCourseScheduleList = courseScheduleJdbcDao.findCourseScheduleListByTeacherId(searchAcadyear,searchSemester, perId, week);
	        //查出当做辅助教师的
	        List<CourseSchedule> timetableCourseScheduleFuList = courseScheduleJdbcDao.findCourseScheduleListByFuTeacherId(searchAcadyear,searchSemester, perId, week);
	        if(CollectionUtils.isNotEmpty(timetableCourseScheduleFuList)){
	        	timetableCourseScheduleList.addAll(timetableCourseScheduleFuList);
	        }
	        Set<String> classIds = EntityUtils.getSet(timetableCourseScheduleList, CourseSchedule::getClassId);
	        List<Clazz> classes = classService.findListByIds(classIds.toArray(new String[0]));
	        if(CollectionUtils.isNotEmpty(classes)){
		        for (Clazz c : classes) {
		            classId2Name.put(c.getId(), c.getClassNameDynamic());
		        }
	        }
	     // 获取学年学期下学生的所有的教学班List
	        teachClasses = teachClassService.findListByIdIn(classIds.toArray(new String[0]));
	        if(CollectionUtils.isNotEmpty(teachClasses)){
		        for (TeachClass c : teachClasses) {
		        	if(StringUtils.isNotBlank(c.getGradeId())){
		        		gradeIds.add(c.getGradeId());
		        	}
//		            gradeIds.add(c.getGradeId());
//		            classId2GradeId.put(c.getId(), c.getGradeId());
		            classId2Name.put(c.getId(), c.getName());
		        }
	        }
	        
		}else if("2".equals(type)){//学生
			Set<String> claIds = new HashSet<String>();
			Student student = studentService.findOne(perId);
			if(student!=null){
				claIds.add(student.getClassId());
				Clazz cla =classService.findOne(student.getClassId());
				Grade grade = gradeService.findOne(cla.getGradeId());
	            if (grade != null) {
	            	cla.setClassNameDynamic(grade.getGradeName() + cla.getClassName());
	            }
	            classId2Name.put(cla.getId(), cla.getClassNameDynamic());
			}
			// 获取学年学期下学生的所有的教学班List
	        teachClasses =teachClassStuService.findByStudentId2(perId, searchAcadyear, searchSemester+"");
	        if(CollectionUtils.isNotEmpty(teachClasses)){
	        	for(TeachClass t:teachClasses){
	        		if(StringUtils.isNotBlank(t.getGradeId())){
		        		gradeIds.add(t.getGradeId());
		        	}
	        		claIds.add(t.getId());
	        	}
	        }
	      //6.0选课学生进入课程表数据
	    	List<String> jxclaids=null;
	    	if(openApiNewElectiveService!=null && student!=null){
	        	jxclaids = openApiNewElectiveService.getClassByUidSemesterStuId(student.getSchoolId(), searchAcadyear, searchSemester+"", student.getId());
	        }
	    	if(CollectionUtils.isNotEmpty(jxclaids)){
	    		claIds.addAll(jxclaids);
	    	}
	    	
        	//查出学生课程
        	if(CollectionUtils.isNotEmpty(claIds)){
        		timetableCourseScheduleList = courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), Integer.valueOf(week), claIds.toArray(new String[0]));
        	}
		}
		else if("3".equals(type)){//班级
			Set<String> claIds = new HashSet<>();
			Clazz clazz = classService.findOne(perId);
			if(clazz!=null){
				claIds.add(clazz.getId());
				Grade grade = gradeService.findOne(clazz.getGradeId());
	            if (grade != null) {
	            	clazz.setClassNameDynamic(grade.getGradeName() + clazz.getClassName());
	            }
	            classId2Name.put(clazz.getId(), clazz.getClassNameDynamic());
			}
        	//查出班级课程
        	if(CollectionUtils.isNotEmpty(claIds)){
        		timetableCourseScheduleList = courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), week, claIds.toArray(new String[0]));
        	}
		}
		else if ("4".equals(type)) { //学校
			Set<String> claIds = new HashSet<>();
			List<Clazz> clazzs = classService.findBySchoolId(perId);
			if(CollectionUtils.isNotEmpty(clazzs)){
				Set<String> gradeSet = EntityUtils.getSet(clazzs, Clazz::getGradeId);
				List<Grade> grades = gradeService.findListByIdIn(gradeSet.toArray(new String[gradeSet.size()]));
				Map<String, String> gradeNaMap = EntityUtils.getMap(grades, Grade::getId, Grade::getGradeName);
				clazzs.forEach(c->{
					claIds.add(c.getId());
					if(gradeNaMap != null && StringUtils.isNotBlank(gradeNaMap.get(c.getGradeId()))){
						c.setClassNameDynamic(gradeNaMap.get(c.getGradeId()) + c.getClassName());
					}
					classId2Name.put(c.getId(), c.getClassNameDynamic());
				});
			}
        	//查出班级课程
        	if(CollectionUtils.isNotEmpty(claIds)){
        		timetableCourseScheduleList = courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), week, claIds.toArray(new String[0]));
        	}
		}
		Map<String, String> gradeMap = new HashMap<String, String>();
		if(gradeIds.size()>0){
			List<Grade> grades = gradeService.findListByIdIn(gradeIds.toArray(new String[0]));
			gradeMap=EntityUtils.getMap(grades, Grade::getId, Grade::getGradeName);
		}
		if(CollectionUtils.isNotEmpty(teachClasses)){
        	for(TeachClass t:teachClasses){
        		if (t.getName() == null) {
        			t.setName("");
				}
        		if(StringUtils.isNotBlank(t.getGradeId()) && gradeMap.containsKey(t.getGradeId()) && !t.getName().contains(gradeMap.get(t.getGradeId()))){
        			classId2Name.put(t.getId(), gradeMap.get(t.getGradeId())+t.getName());
	        	}else{
	        		classId2Name.put(t.getId(), t.getName());
	        	}
        		
        	}
		}
		
		// timetableCourseScheduleList中className,subjectName,placeName内容填充
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleList)) {
            Set<String> subjectIds = EntityUtils.getSet(timetableCourseScheduleList, "subjectId");
            Set<String> placeIds = EntityUtils.getSet(timetableCourseScheduleList, "placeId");
            Set<String> placeIds1 = new HashSet<String>();
            for (String pid : placeIds) {
				if(pid != null) {
					placeIds1.add(perId);
				}
			}
            Map<String, Course> id2CourseName = new HashMap<String, Course>();
            Map<String, String> id2PlaceName = new HashMap<String, String>();
            
            if (CollectionUtils.isNotEmpty(subjectIds)) {
                List<Course> courseList = courseService.findListByIdIn(subjectIds.toArray(new String[0]));
                if (CollectionUtils.isNotEmpty(courseList)) {
                    id2CourseName =EntityUtils.getMap(courseList, Course::getId);
                }
            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
                id2PlaceName = teachPlaceService.findTeachPlaceMap(placeIds.toArray(new String[0]));
            }
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
                	Course cs = id2CourseName.get(courseSchedule.getSubjectId());
	           		 if(cs != null){
	           			 courseSchedule.setSubjectName(cs.getSubjectName());
	           			if (StringUtils.isNotEmpty(cs.getBgColor())) {
							String[] bcs = cs.getBgColor().split(",");
							if (StringUtils.isNotEmpty(bcs[0])) {
								courseSchedule.setBgColor(bcs[0]);
							}
							if (bcs.length > 1 && StringUtils.isNotEmpty(bcs[1])) {
								courseSchedule.setBorderColor(bcs[1]);
							}
						}
	           		 }else {
	           			 courseSchedule.setSubjectName(courseSchedule.getSubjectName());
	   				}
                }
                if (id2PlaceName != null && id2PlaceName.get(courseSchedule.getPlaceId()) != null) {
                    courseSchedule.setPlaceName(id2PlaceName.get(courseSchedule.getPlaceId()));
                }
                // 重新填充 班级名称
                courseSchedule.setClassName(classId2Name.get(courseSchedule.getClassId()));
            }
        }
		return timetableCourseScheduleList;
	}
	

	@Override
	public List<CourseSchedule> findCourseScheduleListByPerId(String searchAcadyear,
			Integer searchSemester, String startDate, String endDate, String perId,
			String type) {
		List<CourseSchedule> timetableCourseScheduleList=new ArrayList<CourseSchedule>();
		Map<String, String> classId2Name = new HashMap<String, String>();
		Set<String> gradeIds = new HashSet<String>();
		List<TeachClass> teachClasses=new ArrayList<TeachClass>();
		if("1".equals(type)){//教师
	        // 早上下晚时间段的 课程数
	        // 查出教师所有行政班任课信息
	        timetableCourseScheduleList = courseScheduleJdbcDao.findCourseScheduleListByTeacherId(searchAcadyear,searchSemester, perId, startDate, endDate);
	        //查出当做辅助教师的
	        List<CourseSchedule> timetableCourseScheduleFuList = courseScheduleJdbcDao.findCourseScheduleListByFuTeacherId(searchAcadyear,searchSemester, perId, startDate, endDate);
	        if(CollectionUtils.isNotEmpty(timetableCourseScheduleFuList)){
	        	timetableCourseScheduleList.addAll(timetableCourseScheduleFuList);
	        }
	        Set<String> classIds = EntityUtils.getSet(timetableCourseScheduleList, "classId");
	        List<Clazz> classes = classService.findListByIds(classIds.toArray(new String[0]));
	        if(CollectionUtils.isNotEmpty(classes)){
		        for (Clazz c : classes) {
		            classId2Name.put(c.getId(), c.getClassNameDynamic());
		        }
	        }
	     // 获取学年学期下学生的所有的教学班List
	        teachClasses = teachClassService.findListByIdIn(classIds.toArray(new String[0]));
	        if(CollectionUtils.isNotEmpty(teachClasses)){
		        for (TeachClass c : teachClasses) {
		        	if(StringUtils.isNotBlank(c.getGradeId())){
		        		gradeIds.add(c.getGradeId());
		        	}
//		            gradeIds.add(c.getGradeId());
//		            classId2GradeId.put(c.getId(), c.getGradeId());
		            classId2Name.put(c.getId(), c.getName());
		        }
	        }
	        
		}else if("2".equals(type)){//学生
			Set<String> claIds = new HashSet<String>();
			Student student = studentService.findOne(perId);
			if(student!=null){
				claIds.add(student.getClassId());
				Clazz cla =classService.findOne(student.getClassId());
				Grade grade = gradeService.findOne(cla.getGradeId());
	            if (grade != null) {
	            	cla.setClassNameDynamic(grade.getGradeName() + cla.getClassName());
	            }
	            classId2Name.put(cla.getId(), cla.getClassNameDynamic());
			}
			// 获取学年学期下学生的所有的教学班List
	        teachClasses =teachClassStuService.findByStudentId2(perId, searchAcadyear, searchSemester+"");
	        if(CollectionUtils.isNotEmpty(teachClasses)){
	        	for(TeachClass t:teachClasses){
	        		if(StringUtils.isNotBlank(t.getGradeId())){
		        		gradeIds.add(t.getGradeId());
		        	}
	        		claIds.add(t.getId());
	        	}
	        }
	      //6.0选课学生进入课程表数据
	    	List<String> jxclaids=null;
	    	if(openApiNewElectiveService!=null && student!=null){
	        	jxclaids = openApiNewElectiveService.getClassByUidSemesterStuId(student.getSchoolId(), searchAcadyear, searchSemester+"", student.getId());
	        }
	    	if(CollectionUtils.isNotEmpty(jxclaids)){
	    		claIds.addAll(jxclaids);
	    	}
	    	
        	//查出学生课程
        	if(CollectionUtils.isNotEmpty(claIds)){
        		timetableCourseScheduleList = courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), startDate, endDate, claIds.toArray(new String[0]));
        	}
		}
		else if("3".equals(type)){//班级
			Set<String> claIds = new HashSet<>();
			Clazz clazz = classService.findOne(perId);
			if(clazz!=null){
				claIds.add(clazz.getId());
				Grade grade = gradeService.findOne(clazz.getGradeId());
	            if (grade != null) {
	            	clazz.setClassNameDynamic(grade.getGradeName() + clazz.getClassName());
	            }
	            classId2Name.put(clazz.getId(), clazz.getClassNameDynamic());
			}
        	//查出班级课程
        	if(CollectionUtils.isNotEmpty(claIds)){
        		timetableCourseScheduleList = courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), startDate, endDate, claIds.toArray(new String[0]));
        	}
		}
		else if ("4".equals(type)) { //学校
			Set<String> claIds = new HashSet<>();
			List<Clazz> clazzs = classService.findBySchoolId(perId);
			if(CollectionUtils.isNotEmpty(clazzs)){
				Set<String> gradeSet = EntityUtils.getSet(clazzs, Clazz::getGradeId);
				List<Grade> grades = gradeService.findListByIdIn(gradeSet.toArray(new String[gradeSet.size()]));
				Map<String, String> gradeNaMap = EntityUtils.getMap(grades, Grade::getId, Grade::getGradeName);
				clazzs.forEach(c->{
					claIds.add(c.getId());
					if(gradeNaMap != null && StringUtils.isNotBlank(gradeNaMap.get(c.getGradeId()))){
						c.setClassNameDynamic(gradeNaMap.get(c.getGradeId()) + c.getClassName());
					}
					classId2Name.put(c.getId(), c.getClassNameDynamic());
				});
			}
        	//查出班级课程
        	if(CollectionUtils.isNotEmpty(claIds)){
        		timetableCourseScheduleList = courseScheduleDao.findCourseScheduleListByClassIdes(searchAcadyear, Integer.valueOf(searchSemester), startDate, endDate, claIds.toArray(new String[0]));
        	}
		}
		Map<String, String> gradeMap = new HashMap<String, String>();
		if(gradeIds.size()>0){
			List<Grade> grades = gradeService.findListByIdIn(gradeIds.toArray(new String[0]));
			gradeMap=EntityUtils.getMap(grades, "id", "gradeName");
		}
		if(CollectionUtils.isNotEmpty(teachClasses)){
        	for(TeachClass t:teachClasses){
        		if(StringUtils.isNotBlank(t.getGradeId()) && gradeMap.containsKey(t.getGradeId())){
        			classId2Name.put(t.getId(), gradeMap.get(t.getGradeId())+t.getName());
	        	}else{
	        		classId2Name.put(t.getId(), t.getName());
	        	}
        		
        	}
		}
		
		// timetableCourseScheduleList中className,subjectName,placeName内容填充
        if (CollectionUtils.isNotEmpty(timetableCourseScheduleList)) {
            Set<String> subjectIds = EntityUtils.getSet(timetableCourseScheduleList, "subjectId");
            Set<String> placeIds = EntityUtils.getSet(timetableCourseScheduleList, "placeId");
            Set<String> placeIds1 = new HashSet<String>();
            for (String pid : placeIds) {
				if(pid != null) {
					placeIds1.add(perId);
				}
			}
            Map<String, String> id2CourseName = new HashMap<String, String>();
            Map<String, String> id2PlaceName = new HashMap<String, String>();
            
            if (CollectionUtils.isNotEmpty(subjectIds)) {
                List<Course> courseList = courseService.findListByIdIn(subjectIds.toArray(new String[0]));
                if (CollectionUtils.isNotEmpty(courseList)) {
                    id2CourseName =EntityUtils.getMap(courseList,"id","subjectName");
                }
            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
                id2PlaceName = teachPlaceService.findTeachPlaceMap(placeIds.toArray(new String[0]));
            }
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
                    String subname = id2CourseName.get(courseSchedule.getSubjectId());
	           		 if(StringUtils.isNotBlank(subname)){
	           			 courseSchedule.setSubjectName(subname);
	           		 }else {
	           			 courseSchedule.setSubjectName(courseSchedule.getSubjectName());
	   				}
                }
                if (id2PlaceName != null && id2PlaceName.get(courseSchedule.getPlaceId()) != null) {
                    courseSchedule.setPlaceName(id2PlaceName.get(courseSchedule.getPlaceId()));
                }
                // 重新填充 班级名称
                courseSchedule.setClassName(classId2Name.get(courseSchedule.getClassId()));
            }
        }
		return timetableCourseScheduleList;
	}
	
	@Override
	public Map<String, Set<String>> findCourseScheduleMap(String schoolId,
			String acadyear, Integer semester) {
		Map<String,Set<String>> reMap  = new HashMap<String, Set<String>>();
		Set<String> claIds = new HashSet<String>();
		List<Clazz> clazzs = classService.findByIdCurAcadyear(schoolId, acadyear);
		if(CollectionUtils.isNotEmpty(clazzs)){
        	for(Clazz c:clazzs){
        		claIds.add(c.getId());
        	}
        }else{
        	return reMap;
        }
		Map<String,Set<String>> stuClsMap = new HashMap<String, Set<String>>();
		List<Student> stulist = studentService.findByClassIds(claIds.toArray(new String[0]));
		for (Student student : stulist) {
			if(!stuClsMap.containsKey(student.getId())){
				stuClsMap.put(student.getId(), new HashSet<String>());
			}
			stuClsMap.get(student.getId()).add(student.getClassId());
		}
		// 获取学年学期下学生的所有的教学班List
		List<TeachClass> teachClasses =teachClassService.findByAcadyearAndSemesterAndUnitId(acadyear, semester+"",schoolId);
        if(CollectionUtils.isNotEmpty(teachClasses)){
        	for(TeachClass t:teachClasses){
        		claIds.add(t.getId());
        	}
        }
        List<TeachClassStu> teaStuList = teachClassStuService.findByClassIds(claIds.toArray(new String[0]));
        for (TeachClassStu teachClassStu : teaStuList) {
        	if(!stuClsMap.containsKey(teachClassStu.getStudentId())){
				stuClsMap.put(teachClassStu.getStudentId(), new HashSet<String>());
			}
			stuClsMap.get(teachClassStu.getStudentId()).add(teachClassStu.getClassId());
		}
      //查出学生课程
		if(CollectionUtils.isNotEmpty(claIds)){
			List<CourseSchedule>  timetableCourseScheduleList = 
					this.findCourseScheduleListByClassIdes(acadyear, Integer.valueOf(semester), claIds.toArray(new String[0]),null);
			for(CourseSchedule sle : timetableCourseScheduleList){
				for (Map.Entry<String, Set<String>> entry : stuClsMap.entrySet()) {  
					if(entry.getValue().contains(sle.getClassId())){
						if(!reMap.containsKey(entry.getKey())){
							reMap.put(entry.getKey(), new HashSet<String>());
						}
						reMap.get(entry.getKey()).add(sle.getSubjectId()+","+sle.getTeacherId());
					}
				}  
			}
		}
		return reMap;
	}
	
	@Override
	public Map<String,Set<String>> findCourseScheduleMapByObjId(
			String acadyear, Integer semester, String type, String ownerId) {
		Map<String,Set<String>> reMap  = new HashMap<String, Set<String>>();
		 if("2".equals(type)){//学生
				Set<String> claIds = new HashSet<String>();
				Student student = studentService.findOne(ownerId);
				if(student!=null){
					claIds.add(student.getClassId());
				}
				// 获取学年学期下学生的所有的教学班List
				List<TeachClass> teachClasses =teachClassStuService.findByStudentId2(ownerId, acadyear, semester+"");
		        if(CollectionUtils.isNotEmpty(teachClasses)){
		        	for(TeachClass t:teachClasses){
		        		claIds.add(t.getId());
		        	}
		        }
		      //6.0选课学生进入课程表数据
		    	List<String> jxclaids=null;
		    	if(openApiNewElectiveService!=null && student!=null){
		        	jxclaids = openApiNewElectiveService.getClassByUidSemesterStuId(student.getSchoolId(), acadyear, semester+"", student.getId());
		        }
		    	if(CollectionUtils.isNotEmpty(jxclaids)){
		    		claIds.addAll(jxclaids);
		    	}
		        
		       //查出学生课程
	        	if(CollectionUtils.isNotEmpty(claIds)){
	        		List<CourseSchedule>  timetableCourseScheduleList = 
	        				courseScheduleDao.findCourseScheduleListByClassIdes(acadyear, Integer.valueOf(semester), claIds.toArray(new String[0]));
	        		Set<String> teaIds;
	        		for(CourseSchedule sle : timetableCourseScheduleList){
	        			if(!reMap.containsKey(sle.getSubjectId())){
	        				teaIds = new HashSet<String>();
	        				reMap.put(sle.getSubjectId(), teaIds);
	        			}
	        			reMap.get(sle.getSubjectId()).add(sle.getTeacherId());
	        		}
	        	}
		 }else if("1".equals(type)){//教师
			 List<CourseSchedule>  timetableCourseScheduleList = courseScheduleJdbcDao.findCourseScheduleListByTeacherId(acadyear,semester, ownerId, null);
			 Set<String> subjectIds;
     		for(CourseSchedule sle : timetableCourseScheduleList){
     			if(!reMap.containsKey(sle.getClassId() + "," + sle.getClassType())){
     				subjectIds = new HashSet<String>();
     				reMap.put(sle.getClassId() + "," + sle.getClassType(), subjectIds);
     			}
     			reMap.get(sle.getClassId() + "," + sle.getClassType()).add(sle.getSubjectId());
     		}
		 }
		return reMap;
	}
	@Override
	public List<CourseSchedule> findCourseScheduleList(String searchAcadyear, Integer searchSemester, String schoolId,
			Integer weekOfWorktime, Integer dayOfWeek, String periodInterval, Integer punchCard,Integer period) {
		 List<CourseSchedule> courseSchedules = courseScheduleDao.findCourseScheduleList(searchAcadyear, searchSemester, schoolId, weekOfWorktime, dayOfWeek, periodInterval, punchCard,period);
		 Set<String> subjectIds = EntityUtils.getSet(courseSchedules, "subjectId");
		 Map<String, String> courseNameMap = new HashMap<String, String>();
         if (CollectionUtils.isNotEmpty(subjectIds)) {
             List<Course> courseList = courseService.findListByIdIn(subjectIds.toArray(new String[0]));
             if (CollectionUtils.isNotEmpty(courseList)) {
                 courseNameMap =EntityUtils.getMap(courseList,"id","subjectName");
             }
         }
         for (CourseSchedule courseSchedule : courseSchedules) {
        	 if (courseNameMap != null && courseNameMap.get(courseSchedule.getSubjectId()) != null) {
        		 String subname = courseNameMap.get(courseSchedule.getSubjectId());
        		 if(StringUtils.isNotBlank(subname)){
        			 courseSchedule.setSubjectName(subname);
        		 }else {
        			 courseSchedule.setSubjectName(courseSchedule.getSubjectName());
				}
        	 }
         }
		return courseSchedules;
	}
	@Override
	public List<CourseSchedule> findByTimes(CourseScheduleDto dto,
			String[] timeStr) {
		if(ArrayUtils.isEmpty(timeStr)){
			return new ArrayList<CourseSchedule>();
		}
		return courseScheduleJdbcDao.findByTimes(dto,timeStr);
	}
	@Override
	public void makeTeacherSet(List<CourseSchedule> list) {
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			//辅助教师
			Map<String, Set<String>> map = courseScheduleJdbcDao.findTeacherIds(ids.toArray(new String[]{}));
			if(map.size()>0){
				for(CourseSchedule c:list){
					c.setTeacherIds(new HashSet<String>());
					if(StringUtils.isNotEmpty(c.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(c.getTeacherId()))){
						c.getTeacherIds().add(c.getTeacherId());
					}
					if(map.containsKey(c.getId())){
						c.getTeacherIds().addAll(map.get(c.getId()));
					}
				}
			}else{
				for(CourseSchedule c:list){
					c.setTeacherIds(new HashSet<String>());
					if(StringUtils.isNotEmpty(c.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(c.getTeacherId()))){
						c.getTeacherIds().add(c.getTeacherId());
					}
				}
			}
		}
	}
	@Override
	public void deleteByClassId(CourseScheduleDto deleteDto) {
		/**
		 * DOTO  辅助教师那张表没有删除
		 */
		courseScheduleJdbcDao.deleteByClassId(deleteDto);
	}
	@Override
	public List<CourseSchedule> findCourseScheduleListByPlaceId(String schoolId,
			String searchAcadyear, Integer searchSemester, String placeId,
			Integer week,boolean ismakeTeacher) {
		List<CourseSchedule> list = courseScheduleDao.findCourseScheduleListByPlaceId(schoolId,searchAcadyear, searchSemester, placeId,week);
		if(ismakeTeacher){
			makeTeacherSet(list);
		}
		return list;
	}
	
	@Override
	public List<CourseSchedule> findCourseScheduleListByPlaceId(String schoolId,
			String searchAcadyear, Integer searchSemester, String placeId,
			String startDate, String endDate,boolean ismakeTeacher) {
		List<CourseSchedule> list = courseScheduleDao.findCourseScheduleListByPlaceId(schoolId,searchAcadyear, searchSemester, placeId,startDate, endDate);
		if(ismakeTeacher){
			makeTeacherSet(list);
		}
		return list;
	}
	@Override
	public List<CourseSchedule> findBySchoolIdAndGradeIdAndSubjectId(String schoolId,String acadyear,
			Integer semester,String gradeId, String subjectId) {
		return courseScheduleJdbcDao.findBySchoolIdAndGradeIdAndSubjectId(schoolId,acadyear,semester, gradeId,subjectId);
	}
	@Override
	public List<CourseSchedule> findCourseScheduleListByTeacherIdIn(String searchAcadyear, Integer searchSemester, Integer week, String[] teacherIds) {
		List<CourseSchedule> timetableCourseScheduleList = courseScheduleJdbcDao.findCourseScheduleListByTeacherIdIn(searchAcadyear,searchSemester, week,teacherIds);
		if(timetableCourseScheduleList==null){
			timetableCourseScheduleList=new ArrayList<CourseSchedule>();
        }
		return timetableCourseScheduleList;
	}
	@Override
	public String deleteByClassIdAndSubjectId(String schoolId, String acadyear, String semester, String[] classIds, String[] subjectIds) {
		Semester se=semesterService.findByAcadyearAndSemester(acadyear,Integer.parseInt(semester),schoolId);
		Date nowDate = new Date();
		if(DateUtils.compareForDay(nowDate, se.getSemesterEnd())<0){//学期未结束
			if(ArrayUtils.isEmpty(classIds) || ArrayUtils.isEmpty(subjectIds)){
				return null;
			}
			List<CourseSchedule> csList = courseScheduleDao.findCourseScheduleListByClassIdsAndSubjectIds(schoolId,acadyear, Integer.valueOf(semester), classIds,subjectIds);
			if(CollectionUtils.isNotEmpty(csList)){
				if(DateUtils.compareForDay(nowDate, se.getSemesterBegin())>0){//学期已开始
					DateInfo nowDateInfo = dateInfoService.getDate(schoolId, acadyear, Integer.valueOf(semester), nowDate);
					if(nowDateInfo==null){
						return "保存失败,未维护当前选择的学年学期内的节假日信息！";
					}
					Iterator<CourseSchedule> iterator = csList.iterator();
					while(iterator.hasNext()){
						CourseSchedule cs = iterator.next();
						if((cs.getWeekOfWorktime()<nowDateInfo.getWeek()) || (cs.getWeekOfWorktime()==nowDateInfo.getWeek() && cs.getDayOfWeek()+1<=nowDateInfo.getWeekday())){
							iterator.remove();
						}
					}
				}
				if(CollectionUtils.isNotEmpty(csList)){
//				this.deleteAll(csList.toArray(new CourseSchedule[0]));
					Set<String> csIds = EntityUtils.getSet(csList, CourseSchedule::getId);
					deleteByIds(csIds.toArray(new String[0]));
				}
			}
		}
		return null;
	}
	@Override
	public List<CourseSchedule> findCourseScheduleListByClassIdsAndSubjectIds(String schoolId, String acadyear, Integer semester, String[] classIds, String[] subjectIds) {
		return courseScheduleDao.findCourseScheduleListByClassIdsAndSubjectIds(schoolId,acadyear, semester, classIds,subjectIds);
	}
	@Override
	public Map<String, List<CourseSchedule>> findCourseScheduleMapByTeacherId(String acadyear, String semester, String[] teacherIds, Integer week) {
		List<CourseSchedule> scheduleList = courseScheduleJdbcDao.findCourseScheduleListByTeacherIdIn(acadyear, Integer.parseInt(semester), week, teacherIds);
		Map<String,List<CourseSchedule>> scheduleMap = courseScheduleJdbcDao.findCourseScheduleMapByFuTeacherId(acadyear, semester, teacherIds, week);
		if(scheduleMap==null){
			scheduleMap = new HashMap<String, List<CourseSchedule>>();
		}
		if(CollectionUtils.isNotEmpty(scheduleList)){
			for (CourseSchedule cs : scheduleList) {
				if(!scheduleMap.containsKey(cs.getTeacherId())){
					scheduleMap.put(cs.getTeacherId(), new ArrayList<CourseSchedule>());
				}
				scheduleMap.get(cs.getTeacherId()).add(cs);
			}
		}
		return scheduleMap;
	}
	@Override
	public List<CourseSchedule> findByClassIdAndSubjectIdAndTeacherId(String schoolId, String acadyear, Integer semester, String classId, String subjectId, String teacherId) {
		return courseScheduleDao.findByClassAndSubjectTeacher(schoolId,acadyear,semester,classId,subjectId,teacherId);
	}
	@Override
	public void saveClassScheduleImport(CourseScheduleDto dto, List<ClassTeaching> ctlist, List<ClassTeachingEx> ctexlist,List<TeachPlan> tplist,
			List<TeachPlanEx> tpexlist, List<CourseSchedule> cslist, List<GradeTeaching> gtList, List<ClassHour> chList,  List<ClassHourEx> cheList, List<TeachClass> tcList) {
		
		if(CollectionUtils.isNotEmpty(cslist)){
			this.deleteByClassId(dto);
			this.saveAllEntitys(cslist.toArray(new CourseSchedule[0]));
		}
		
		if(CollectionUtils.isNotEmpty(ctlist)){
			classTeachingService.saveAll(ctlist.toArray(new ClassTeaching[0]));
		}
		
		if(CollectionUtils.isNotEmpty(gtList)){
			gradeTeachingService.saveAll(gtList.toArray(new GradeTeaching[0]));
		}
		
		if(CollectionUtils.isNotEmpty(tpexlist)){
			teachPlanExService.saveAll(tpexlist.toArray(new TeachPlanEx[0]));
		}
		if(CollectionUtils.isNotEmpty(tplist)){
			teachPlanService.deleteByAcadyearAndSemesterAndClassIds(dto.getSchoolId(),dto.getAcadyear(),dto.getSemester(),dto.getClassIds());
			teachPlanService.saveAll(tplist.toArray(new TeachPlan[0]));
		}
		if(CollectionUtils.isNotEmpty(chList)){
			classHourService.saveAll(chList.toArray(new ClassHour[0]));
		}
		if(CollectionUtils.isNotEmpty(tcList)){
			teachClassService.saveAll(tcList.toArray(new TeachClass[0]));
		}
		if(CollectionUtils.isNotEmpty(cheList)){
			classHourExService.saveAll(cheList.toArray(new ClassHourEx[0]));
		}
	}

    @Override
    public List<CourseSchedule> findListByTeacherIdsIn(String schoolId, String acadyear, Integer semester, String[] teacherIds) {
        return courseScheduleDao.findListByTeacherIdsIn(schoolId, acadyear, semester, teacherIds);
    }
	@Override
	public void makeScheduleInfo(List<CourseSchedule> timetableCourseScheduleList, String type) {

        if (CollectionUtils.isNotEmpty(timetableCourseScheduleList)) {
        	Set<String> xzbIds = timetableCourseScheduleList.stream().filter(e -> CourseSchedule.CLASS_TYPE_NORMAL == e.getClassType())
    				.map(e -> e.getClassId()).collect(Collectors.toSet());
    		Set<String> jxbIds = timetableCourseScheduleList.stream().filter(e -> CourseSchedule.CLASS_TYPE_NORMAL != e.getClassType())
    				.map(e -> e.getClassId()).collect(Collectors.toSet());
    		List<Clazz> xzbs = classService.findListByIds(xzbIds.toArray(new String[0]));
    		List<TeachClass> jxbs = teachClassService.findListByIds(jxbIds.toArray(new String[0]));
    		Set<String> gradeIds = EntityUtils.getSet(xzbs, Clazz::getGradeId);
    		Set<String> gradeIds2 = EntityUtils.getSet(jxbs, TeachClass::getGradeId);
    		gradeIds.addAll(gradeIds2);
    		List<Grade> gradeList = gradeService.findListByIds(gradeIds.toArray(new String[0]));
    		Map<String, String> gradeNameMap = EntityUtils.getMap(gradeList, Grade::getId,Grade::getGradeName);
    		Map<String, String> classGradeIdMap = new HashMap<>();
    		Map<String, String> classId2Name = new HashMap<>();
    		for (Clazz c : xzbs) {
				String className = c.getClassName();
				if(gradeNameMap.containsKey(c.getGradeId())) {
					className = gradeNameMap.get(c.getGradeId()) + className;
				}
				classId2Name.put(c.getId(), className);
				classGradeIdMap.put(c.getId(), c.getGradeId());
			}
    		for (TeachClass th : jxbs) {
    			String className = th.getName();
    			if(gradeNameMap.containsKey(th.getGradeId())) {
    				className = gradeNameMap.get(th.getGradeId()) + className;
    			}
    			classId2Name.put(th.getId(), className);
    			classGradeIdMap.put(th.getId(), th.getGradeId());
    		}
    		
            Set<String> subjectIds = new HashSet<String>();
            Map<String, Course> id2CourseName = new HashMap<String, Course>();
            Set<String> placeIds = new HashSet<String>();
            Map<String, String> id2PlaceName = new HashMap<String, String>();
            Set<String> teacherIds = new HashSet<String>();
            Map<String, String> id2TeacherName = new HashMap<String, String>();
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (courseSchedule.getSubjectId() != null && StringUtils.isBlank(courseSchedule.getSubjectName())) {
                    subjectIds.add(courseSchedule.getSubjectId());
                }
                if (courseSchedule.getPlaceId() != null) {
                    placeIds.add(courseSchedule.getPlaceId());
                }
                if (courseSchedule.getTeacherId() != null) {
                    teacherIds.add(courseSchedule.getTeacherId());
                }
            }
            if (CollectionUtils.isNotEmpty(subjectIds)) {
            	 List<Course> courseList = courseService.findByIdIn(subjectIds.toArray(new String[0]));
                for (Course c : courseList) {
                    id2CourseName.put(c.getId(), c);
                }
            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
            	id2PlaceName = teachPlaceService.findTeachPlaceMap(placeIds.toArray(new String[0]));
//                id2PlaceName = SUtils.dt(teachPlaceService.findTeachPlaceMap(placeIds.toArray(new String[0])),new TR<Map<String, String>>() {});
            }
            if (CollectionUtils.isNotEmpty(teacherIds)) {
            	List<Teacher> teacherList = teacherService.findListByIdIn(teacherIds.toArray(new String[0]));
//                List<Teacher> teacherList = SUtils.dt(teacherService.findListByIds(teacherIds.toArray(new String[0])), new TR<List<Teacher>>() {});
                for (Teacher t : teacherList) {
                    id2TeacherName.put(t.getId(), t.getTeacherName());
                }
            }
            for (CourseSchedule courseSchedule : timetableCourseScheduleList) {
                if (id2CourseName != null && id2CourseName.get(courseSchedule.getSubjectId()) != null) {
                	Course cs = id2CourseName.get(courseSchedule.getSubjectId());
	           		 if(cs != null){
	           			 courseSchedule.setSubjectName(cs.getSubjectName());
	           			if(StringUtils.isNotEmpty(cs.getBgColor())) {
	           				 String[] bcs = cs.getBgColor().split(",");
	           				if (StringUtils.isNotEmpty(bcs[0])) {
								courseSchedule.setBgColor(bcs[0]);
							}
							if (bcs.length > 1 && StringUtils.isNotEmpty(bcs[1])) {
								courseSchedule.setBorderColor(bcs[1]);
							}
	           			 }
	           		 }else {
	           			 courseSchedule.setSubjectName(courseSchedule.getSubjectName());
	   				}
                }else{
                	courseSchedule.setSubjectName("");
                }
                if (id2PlaceName != null && id2PlaceName.get(courseSchedule.getPlaceId()) != null) {
                    courseSchedule.setPlaceName(id2PlaceName.get(courseSchedule.getPlaceId()));
                }else{
                	courseSchedule.setPlaceName("");
                }
                if (id2TeacherName != null && id2TeacherName.get(courseSchedule.getTeacherId()) != null) {
                    courseSchedule.setTeacherName(id2TeacherName.get(courseSchedule.getTeacherId()));
                }else{
                	courseSchedule.setTeacherName("");
                }
                if (("2").equals(type)) {
                    courseSchedule.setSubAndTeacherName(courseSchedule.getSubjectName());
                    if (StringUtils.isNotBlank(courseSchedule.getTeacherName())) {
                        courseSchedule.setSubAndTeacherName(courseSchedule.getSubAndTeacherName() + "("+ courseSchedule.getTeacherName() + ")");
                    }
                    
                }
                courseSchedule.setGradeId(classGradeIdMap.get(courseSchedule.getClassId()));
                // 在行政班上的课的班级名称填充
                if (courseSchedule.getClassName() == null) {
                	String claname =classId2Name.get(courseSchedule.getClassId());
                	if(StringUtils.isNotEmpty(claname)){
                		courseSchedule.setClassName(classId2Name.get(courseSchedule.getClassId()));
                	}else{
                		courseSchedule.setClassName("");
                	}
//                	else {//教学班
//                		courseSchedule.setClassName(courseSchedule.getSubjectName());
//					}
                }
            }
        }
	}
	
	@Override
	public List<CourseSchedule> findListByTeacherIdsInWeekGte(String schoolId, String acadyear, Integer semester,
			String[] teacherIds, Integer week) {
		if(week == null)
			week = 0;
		return courseScheduleDao.findListByTeacherIdsInWeekGte(schoolId, acadyear, semester, teacherIds,week);
	}
	@Override
	public void deleteByClassIds(String... classIds) {
		courseScheduleDao.deleteByClassIdIn(classIds);
	}
	@Override
	public void deleteByTeacherIds(String... teacherIds) {
		courseScheduleDao.deleteByTeacherIdIn(teacherIds);
	}
	@Override
	public void deleteTeachClassScheduleByGradeIds(String... gradeIds) {
		courseScheduleDao.deleteByGradeIds(gradeIds);		
	}
	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		courseScheduleDao.deleteBySubjectIdIn(subjectIds);
	}
	@Override
	public void saveTeacherScheduleImport(List<ClassTeaching> updateClassTeachingList, List<CourseSchedule> updateCourseScheduleList, List<TeachPlan> updateTeachPlanList,
			List<TeachPlanEx> newTeachPlanExList, List<ClassTeachingEx> newClassTeachingExList, List<TeachPlanEx> deleteTeachPlanExList, List<ClassTeachingEx> deleteClassTeachingExList) {
		if(CollectionUtils.isNotEmpty(updateClassTeachingList)){
			classTeachingService.saveAll(updateClassTeachingList.toArray(new ClassTeaching[0]));
		}
		if(CollectionUtils.isNotEmpty(updateCourseScheduleList)){
			this.saveAllEntitys(updateCourseScheduleList.toArray(new CourseSchedule[0]));
		}
		if(CollectionUtils.isNotEmpty(updateTeachPlanList)){
			teachPlanService.saveAll(updateTeachPlanList.toArray(new TeachPlan[0]));
		}
		if(CollectionUtils.isNotEmpty(newTeachPlanExList)){
			teachPlanExService.saveAll(newTeachPlanExList.toArray(new TeachPlanEx[0]));
		}
		if(CollectionUtils.isNotEmpty(newClassTeachingExList)){
			classTeachingExService.saveAll(newClassTeachingExList.toArray(new ClassTeachingEx[0]));
		}
		if(CollectionUtils.isNotEmpty(deleteTeachPlanExList)){
			teachPlanExService.deleteAll(deleteTeachPlanExList.toArray(new TeachPlanEx[0]));
		}
		if(CollectionUtils.isNotEmpty(deleteClassTeachingExList)){
			classTeachingExService.deleteAll(deleteClassTeachingExList.toArray(new ClassTeachingEx[0]));
		}
	}
	@Override
	public String saveCopy(String gradeId, String isCopySchedule, String srcAcadyear, String srcSemester, String srcWeek,
			String destAcadyear, String destSemester, String destWeek, String operatorId) {
		Grade grade = gradeService.findOne(gradeId);
		List<Clazz> classList = classService.findByGradeId(gradeId);
		String[] classIds = EntityUtils.getList(classList, Clazz::getId).toArray(new String[0]);
		
		//判断年级课程
		List<GradeTeaching> gradeTeachingList = gradeTeachingService.findGradeTeachingList(srcAcadyear, srcSemester, new String[]{gradeId}, grade.getSchoolId(), Constant.IS_DELETED_FALSE, BaseConstants.SUBJECT_TYPE_BX, null);
		if(CollectionUtils.isEmpty(gradeTeachingList)){
			return srcAcadyear+"学年第"+srcSemester+"学期没有开设必修课程";
		}
		List<Course> courseList = courseService.findListByIds(EntityUtils.getList(gradeTeachingList, GradeTeaching::getSubjectId).toArray(new String[0]));
		Map<String, String> courseTypeMap = courseList.stream().filter(e->StringUtils.isNotBlank(e.getCourseTypeId())).collect(Collectors.toMap(Course::getId, Course::getCourseTypeId));
		if(MapUtils.isNotEmpty(courseTypeMap)){
			gradeTeachingList = gradeTeachingList.stream().filter(e->!BaseConstants.VIRTUAL_COURSE_TYPE.equals(courseTypeMap.get(e.getSubjectId()))).collect(Collectors.toList());
		}
		if(CollectionUtils.isEmpty(gradeTeachingList)){
			return srcAcadyear+"学年第"+srcSemester+"学期没有开设非走班课程";
		}
		
		//判断课程表
		Map<String, Object> dtoMap = new HashMap<String, Object>();
		List<CourseSchedule> courseScheduleList = null;
		if(Constant.IS_TRUE_Str.equals(isCopySchedule)){
			dtoMap = makeDto(grade.getSchoolId(), destAcadyear, destSemester, destWeek);
			if(dtoMap.containsKey("error")){
				return (String)dtoMap.get("error");
			}
			courseScheduleList = findCourseScheduleListByClassIdes(srcAcadyear, Integer.parseInt(srcSemester), classIds, Integer.parseInt(srcWeek));
			if(CollectionUtils.isEmpty(courseScheduleList)){
				return srcAcadyear+"学年第"+srcSemester+"学期第"+srcWeek+"周不存在课程表";
			}
			courseScheduleList = courseScheduleList.stream().filter(e->!BaseConstants.VIRTUAL_COURSE_TYPE.equals(courseTypeMap.get(e.getSubjectId()))).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(courseScheduleList)){
				return srcAcadyear+"学年第"+srcSemester+"学期第"+srcWeek+"周不存在非走班课程表";
			}
		}
		
		//复制年级课程开设数据
		List<GradeTeaching> gtList = new ArrayList<GradeTeaching>();
		if(CollectionUtils.isNotEmpty(gradeTeachingList)){
			GradeTeaching gt = null;
			for (GradeTeaching gradeTeaching : gradeTeachingList) {
				gt = new GradeTeaching();
				EntityUtils.copyProperties(gradeTeaching, gt);
				gt.setId(UuidUtils.generateUuid());
				gt.setAcadyear(destAcadyear);
				gt.setSemester(destSemester);
				gt.setCreationTime(new Date());
				gt.setModifyTime(new Date());
				gtList.add(gt);
			}
		}
		
		//复制班级课程开设数据
		List<ClassTeaching> ctList = new ArrayList<ClassTeaching>();
		List<ClassTeachingEx> ctExList = new ArrayList<ClassTeachingEx>();
		List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(srcAcadyear, srcSemester, classIds);
		if(CollectionUtils.isNotEmpty(classTeachingList)){
			classTeachingList = classTeachingList.stream()
					.filter(e->BaseConstants.SUBJECT_TYPE_BX.equals(e.getSubjectType())&&!BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getSubjectId()))
					.collect(Collectors.toList());
			ClassTeaching ct = null;
			for (ClassTeaching classTeaching : classTeachingList) {
				ct = new ClassTeaching();
				EntityUtils.copyProperties(classTeaching, ct);
				ct.setId(UuidUtils.generateUuid());
				ct.setAcadyear(destAcadyear);
				ct.setSemester(destSemester);
				ct.setCreationTime(new Date());
				ct.setModifyTime(new Date());
				ct.setOperatorId(operatorId);
				ctList.add(ct);
			}
			List<ClassTeachingEx> classTeachingExList = classTeachingExService.findByClassTeachingIdIn(EntityUtils.getList(classTeachingList, ClassTeaching::getId).toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(classTeachingExList)){
				ClassTeachingEx ctEx = null;
				for (ClassTeachingEx classTeachingEx : classTeachingExList) {
					ctEx = new ClassTeachingEx();
					EntityUtils.copyProperties(classTeachingEx, ctEx);
					ctEx.setId(UuidUtils.generateUuid());
					ctExList.add(ctEx);
				}
			}
		}
		
		//删除旧数据
		List<GradeTeaching> oldGradeTeachingList = gradeTeachingService.findGradeTeachingList(destAcadyear, destSemester, new String[]{gradeId}, grade.getSchoolId(), Constant.IS_DELETED_FALSE, null, null);
		if(CollectionUtils.isNotEmpty(oldGradeTeachingList)){
			oldGradeTeachingList.forEach(e->e.setIsDeleted(Constant.IS_DELETED_TRUE));
			gtList.addAll(oldGradeTeachingList);
		}
		List<ClassTeaching> oldClassTeachingList = classTeachingService.findBySearchForList(destAcadyear, destSemester, classIds);
		if(CollectionUtils.isNotEmpty(oldClassTeachingList)){
			oldClassTeachingList.forEach(e->{
				e.setIsDeleted(Constant.IS_DELETED_TRUE);
				e.setModifyTime(new Date());
				e.setOperatorId(operatorId);
			});
			ctList.addAll(oldClassTeachingList);
		}
		
		if(!Constant.IS_TRUE_Str.equals(isCopySchedule)){
			saveClassScheduleImport(null, ctList, ctExList, null, null, null, gtList,null,null,null);
			//暂时不删除旧课表
//			if(dtoMap.containsKey("searchDto")){
//				CourseScheduleDto searchDto = (CourseScheduleDto) dtoMap.get("searchDto");
//				searchDto.setClassIds(classIds);
//				deleteByClassId(searchDto);
//			}
			return null;
		}
		
		//复制课程表
		CourseScheduleDto searchDto = (CourseScheduleDto) dtoMap.get("searchDto");
	  	int startWeek = searchDto.getWeekOfWorktime1();
	  	int startDay = searchDto.getDayOfWeek1();
	  	int endWeek = searchDto.getWeekOfWorktime2();
	  	int endDay = searchDto.getDayOfWeek2();
	  	List<CourseSchedule> csList = new ArrayList<CourseSchedule>();
	  	CourseSchedule cs = null;
		for (CourseSchedule courseSchedule : courseScheduleList) {
			int dayOfWeek = courseSchedule.getDayOfWeek();//星期从0开始
			for (int index = startWeek; index <= endWeek; index++) {
				if (index == startWeek && dayOfWeek < startDay) {//开始日期
					continue;
				}
				if (index == endWeek && dayOfWeek > endDay) {//结束时间
					continue;
				}
				cs = new CourseSchedule();
				EntityUtils.copyProperties(courseSchedule, cs);
				cs.setId(UuidUtils.generateUuid());
				cs.setAcadyear(destAcadyear);
				cs.setSemester(Integer.parseInt(destSemester));
				cs.setWeekOfWorktime(index);
				cs.setCreationTime(new Date());
				cs.setModifyTime(new Date());
				csList.add(cs);
			}
		}
		
		//复制中间表
		List<TeachPlan> tpList = new ArrayList<TeachPlan>();
		List<TeachPlanEx> tpExList = new ArrayList<TeachPlanEx>();
		List<TeachPlan> teachPlanList = teachPlanService.findTeachPlanListByClassIds(srcAcadyear, Integer.parseInt(srcSemester), classIds);
		if(CollectionUtils.isNotEmpty(teachPlanList)){
			teachPlanList = teachPlanList.stream().filter(e->!BaseConstants.VIRTUAL_COURSE_TYPE.equals(e.getSubjectId())).collect(Collectors.toList());
			TeachPlan tp = null;
			Map<String, String> inMap = new HashMap<String, String>();
			for (TeachPlan teachPlan : teachPlanList) {
				tp = new TeachPlan();
				EntityUtils.copyProperties(teachPlan, tp);
				tp.setId(UuidUtils.generateUuid());
				tp.setAcadyear(destAcadyear);
				tp.setSemester(Integer.parseInt(destSemester));
				inMap.put(teachPlan.getId(), tp.getId());
				tpList.add(tp);
			}
			List<TeachPlanEx> teachPlanExList = teachPlanExService.findByPrimaryTableIdIn(EntityUtils.getList(teachPlanList, TeachPlan::getId).toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(teachPlanExList)){
				TeachPlanEx tpEx = null;
				for (TeachPlanEx teachPlanEx : teachPlanExList) {
					tpEx = new TeachPlanEx();
					EntityUtils.copyProperties(teachPlanEx, tpEx);
					tpEx.setId(UuidUtils.generateUuid());
					tpEx.setPrimaryTableId(inMap.get(teachPlanEx.getPrimaryTableId()));
					tpEx.setAcadyear(destAcadyear);
					tpEx.setSemester(Integer.parseInt(destSemester));
					tpExList.add(tpEx);
				}
			}
		}
		
		searchDto.setClassIds(classIds);
		saveClassScheduleImport(searchDto, ctList, ctExList, tpList, tpExList, csList, gtList, null, null, null);
		return null;
	}
	
	/**
	 * 获取课程表查询字段 周次时间段 星期段   如果节假日有报错 返回key:error  成功 返回：searchDto
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param destWeek
	 * 结合当前时间
	 * @return
	 */
	private Map<String,Object> makeDto(String unitId,String acadyear,String semester,String destWeek){
		Map<String,Object> objMap=new HashMap<String,Object>();
		CourseScheduleDto searchDto =null;
		Semester chooseSem= semesterService.findByAcadyearAndSemester(acadyear,Integer.parseInt(semester),unitId);
		Date nowDate = new Date();
		if(DateUtils.compareForDay(nowDate, chooseSem.getSemesterEnd())>0){
			//学年学期已经结束  不用删除  无需进出课表
			objMap.put("error", "学年学期已经结束！");
			return objMap;
		}else{
			DateInfo endDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSem.getSemesterEnd());
			if(endDateInfo == null){
				objMap.put("error", "未维护节假日信息或者上课开始时间不在当前选择的学年学期内！");
				return objMap;
			}
			int weekOfWorktime1=1;
			int weekOfWorktime2=endDateInfo.getWeek();
			
			int dayOfWeek1=0;//开始星期
			int dayOfWeek2=endDateInfo.getWeekday()-1;//结束星期
			if(DateUtils.compareForDay(nowDate, chooseSem.getSemesterBegin())<0 || Constant.IS_TRUE_Str.equals(destWeek)){
				//学期未开始或者选择从第一周开始
				weekOfWorktime1=1;
				DateInfo startDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), chooseSem.getSemesterBegin());
				if(startDateInfo == null){
					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
					return objMap;
				}
				dayOfWeek1=startDateInfo.getWeekday()-1;
			}else{
//				DateInfo nowDateInfo = dateInfoService.getDate(unitId, acadyear, Integer.valueOf(semester), nowDate);
//				if(nowDateInfo == null){
//					objMap.put("error", "保存失败,未维护当前选择的学年学期内的节假日信息！");
//					return objMap;
//				}
//				weekOfWorktime1=nowDateInfo.getWeek();
//				dayOfWeek1=nowDateInfo.getWeekday();
				weekOfWorktime1=Integer.parseInt(destWeek);
				dayOfWeek1=0;
				
			}
			//删除原来未上课的数据
			searchDto=new CourseScheduleDto();
			searchDto.setAcadyear(acadyear);
			searchDto.setSemester(Integer.parseInt(semester));
			searchDto.setDayOfWeek1(dayOfWeek1);
			searchDto.setDayOfWeek2(dayOfWeek2);
			searchDto.setWeekOfWorktime1(weekOfWorktime1);
			searchDto.setWeekOfWorktime2(weekOfWorktime2);
			searchDto.setSchoolId(unitId);
		}
		objMap.put("searchDto", searchDto);
		return objMap;
	}
    @Override
	public List<CourseSchedule> findByModifyTime(final String[] schoolId,final String acadyear,
			final	Integer semester, final Date modifyTime,final Pagination pagination) {
		return findAll(new Specification<CourseSchedule>() {
			@Override
            public Predicate toPredicate(Root<CourseSchedule> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
            	 List<Predicate> ps = new ArrayList<Predicate>();
            	 if(schoolId != null && schoolId.length>0){
             		In<String> in = cb.in(root.get("schoolId").as(String.class));
  					for (int i = 0; i < schoolId.length; i++) {
  						in.value(schoolId[i]);
  					}
  					ps.add(in);
             	 }
            	 if(StringUtils.isNotBlank(acadyear)){
            		 ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
            	 }
            	 if(semester != null){
            		 ps.add(cb.equal(root.get("semester").as(Integer.class), semester));
            	 }
            	 if (modifyTime != null) {
                     ps.add(cb.greaterThanOrEqualTo(root.get("modifyTime").as(Date.class), modifyTime));
                 }
            	 List<Order> orderList = new ArrayList<>();
                 orderList.add(cb.asc(root.get("modifyTime").as(Date.class)));
            	 cq.where(ps.toArray(new Predicate[0]));
            	return cq.getRestriction();
            }
		}, pagination);
	}
	@Override
	public String saveCoverAll(String searchAcadyear, Integer searchSemester, String schoolId, String classId, int weekOfWorktime) {
		Integer minWeek = courseScheduleDao.findMinWeek(searchAcadyear, searchSemester, classId);
		if(Objects.equals(weekOfWorktime, minWeek)) {
			return "这一周课表可能不完整， 请使用后面的周次";
		}
		
		// 只检测 老师的冲突
		//1. 取出班级 本周的所有课程
		List<CourseSchedule> classSchedules = this.findCourseScheduleListByClassId(searchAcadyear, searchSemester, classId, weekOfWorktime);
//		classSchedules = classSchedules.stream().filter(e->StringUtils.isNotBlank(e.getTeacherId())).collect(Collectors.toList());
		
		List<String> dfTimes = classSchedules.stream().filter(e->e.getWeekType()!=CourseSchedule.WEEK_TYPE_NORMAL)
				.map(e->e.getDayOfWeek()+"-"+e.getPeriodInterval()+"-"+e.getPeriod())
				.distinct()
				.collect(Collectors.toList());
		List<CourseSchedule> classSchedulesNormal = classSchedules.stream().filter(e->e.getWeekType()==CourseSchedule.WEEK_TYPE_NORMAL)
				.collect(Collectors.toList());
//		boolean hasCouple = classSchedules.stream().anyMatch(e->e.getWeekType()!=CourseSchedule.WEEK_TYPE_NORMAL);
//		if(hasCouple) {
//			return "存在单双周的课程不能进行此操作。";
//		}
		
		// 2. 本班级所有老师 这一周 以后的课程
		Map<String, Integer> cur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(searchAcadyear, searchSemester+"", schoolId);
        Integer weekNow = cur2Max.get("current");
        if(weekNow==null){
        	weekNow=1;
        }
        Integer weekMax = cur2Max.get("max");
        if(weekMax==null || weekMax<weekNow){
        	return "无法获取本学期周次信息";
        }
        Set<Integer> weeks = Stream.iterate(weekOfWorktime, e->e+1).limit(weekMax-weekOfWorktime+1)
        	.filter(e->!Objects.equals(e, weekOfWorktime))
        	.collect(Collectors.toSet());
        //本班级 老师所有的课程，包括在其他班级的；本周可能会添加课程，出现其他周次没有的教师，所以weeks2要包含本周
        Set<Integer> weeks2 = Stream.iterate(weekOfWorktime, e->e+1).limit(weekMax-weekOfWorktime+1)
        		.collect(Collectors.toSet());
		List<CourseSchedule> scheduleList =  courseScheduleDao.findTeacherScheduleByCondition(searchAcadyear, searchSemester,
				schoolId,weeks2.toArray(new Integer[0]), classId);
		
		//3. 将本班的课 覆盖到 其他周
		List<CourseSchedule> partSchedules = scheduleList.stream().filter(e->!e.getClassId().equals(classId)).collect(Collectors.toList());
		List<CourseSchedule> classAllWeekSchedules = new ArrayList<>();
//		Set<Integer> weeks = EntityUtils.getSet(scheduleList, CourseSchedule::getWeekOfWorktime);
		List<Integer> reservedWeekList = new ArrayList<>();
		for (Integer week : weeks) {
			List<CourseSchedule> list;
			if(week%2 == weekOfWorktime%2) {
				list = EntityUtils.copyProperties(classSchedules, CourseSchedule.class, CourseSchedule.class);
			}else {
				list = EntityUtils.copyProperties(classSchedulesNormal, CourseSchedule.class, CourseSchedule.class);
				reservedWeekList.add(week);
			}
			
			list.forEach(e->{
				e.setId(UuidUtils.generateUuid());
				e.setWeekOfWorktime(week);
			});
			partSchedules.addAll(list);
			classAllWeekSchedules.addAll(list);
		}
		scheduleList = partSchedules;
		
		// 获取 班级在 和当前单双周不同的 周次时 指定时间的课程
		List<CourseSchedule> reservedList = this.findClassScheduleByPeriod(searchAcadyear, searchSemester, classId, reservedWeekList,dfTimes);
		
		//2. 逐周 检测 老师冲突
		Map<Integer, List<String>> conflictMap = new HashMap<>();
		Map<Integer, List<CourseSchedule>> collect = scheduleList.stream().collect(Collectors.groupingBy(e->e.getWeekOfWorktime()));
		Set<String> tids = new HashSet<>();
		for (Integer week : collect.keySet()) {
			if(weekOfWorktime == week)
				continue;
			
			List<CourseSchedule> schedules = collect.get(week);
			
			Map<String, List<CourseSchedule>> tschedules = schedules.stream()
					.collect(Collectors.groupingBy(e->e.getTeacherId()+"-"+e.getDayOfWeek()
						+"-"+e.getPeriodInterval()+"-"+e.getPeriod()));
			
			for (String str : tschedules.keySet()) {
				List<CourseSchedule> list = tschedules.get(str);
				if(list.size()<2)
					continue;
				Integer weekCode = list.stream().map(e->e.getWeekType()).reduce((x,y)->x+y).get();
				if(weekCode != 3) {
					// 教师存在冲突
					
					String tid = str.split("-", 2)[0];
					List<String> list2 = conflictMap.get(week);
					if(list2 == null) {
						list2 = new ArrayList<>();
						conflictMap.put(week, list2);
					}
					list2.add(tid);
					tids.add(tid);
				}
			}
		}
		
		List<Teacher> teachers = teacherService.findListBy(Teacher.class, null, null, "id", tids.toArray(new String[0]), new String[]{"id","teacherName"});
		Map<String, String> tnameMap = EntityUtils.getMap(teachers, Teacher::getId,Teacher::getTeacherName);
		
		StringBuilder sb =new StringBuilder();
		for (Integer week : conflictMap.keySet()) {
			conflictMap.get(week).forEach(e->{
				sb.append("第").append(week).append("周 老师：").append(tnameMap.get(e)).append("存在冲突。<br>");
			});
		}
		String msg = sb.toString();
		if(StringUtils.isNotBlank(msg)) {
			// 存在冲突
			return msg;
		}
		// 没有冲突 保存结果
		//先删除 所有 课程 然后再
		courseScheduleDao.deleteByWeekAndClass(searchAcadyear, searchSemester,
				schoolId,weeks.toArray(new Integer[0]), classId);
		classAllWeekSchedules.addAll(reservedList);
		this.saveAllEntitys(classAllWeekSchedules.toArray(new CourseSchedule[0]));
		
		// 删除调节 关联表
		adjustedService.deleteByWeeks(searchAcadyear, searchSemester,
				schoolId,weeks.toArray(new Integer[0]), classId);
		
		return null;
	}

	@Override
	public List<CourseSchedule> findClassScheduleByPeriod(String searchAcadyear, Integer searchSemester,
			String classId, List<Integer> weekList, List<String> dfTimes) {
		List<CourseSchedule> list = new ArrayList<>(); 
		if(CollectionUtils.isEmpty(weekList) || CollectionUtils.isEmpty(dfTimes)) {
			return list;
		}
		
		list = courseScheduleDao.findClassScheduleByPeriod(searchAcadyear,searchSemester,classId,weekList.toArray(new Integer[0])
				,dfTimes.toArray(new String[0]));
		return list;
	}
	@Override
	public List<CourseSchedule> findListByIdIn(String[] ids) {
		if(ids == null || ids.length==0)
			return new ArrayList<>();
		return courseScheduleDao.findByIds(ids);
	}
	@Override
	public List<CourseSchedule> findListByIds(String[] ids) {
		return findListByIdIn(ids);
	}
	@Override
	public CourseSchedule findOne(String id) {
		if(StringUtils.isBlank(id))
			return null;
		List<CourseSchedule> list = findListByIdIn(new String[] {id});
		if(list.size()<1)
			return null;
		
		return list.get(0);
	}
	@Override
	public void updateClassTeaching(String classTeachingId, List<String> delTeacherIds, List<String> timetableIds,
			List<ClassTeaching> classTeachingList, List<ClassTeachingEx> classTeachingExList,List<CourseSchedule> courseScheduleList, List<TeachPlanEx> teachPlanExList) {
		
		if(CollectionUtils.isNotEmpty(delTeacherIds)){
			if(StringUtils.isNotBlank(classTeachingId)){
				classTeachingExService.deleteByClassTeachingIdAndTeacherIdIn(classTeachingId, delTeacherIds.toArray(new String[0]));
			}
			if(CollectionUtils.isNotEmpty(timetableIds)){
				teachPlanExService.deleteByTeacherIdInAndPrimaryTableIdIn(delTeacherIds.toArray(new String[0]), timetableIds.toArray(new String[0]));
			}
		}
		
		if(CollectionUtils.isNotEmpty(classTeachingList)){
			classTeachingService.saveAll(classTeachingList.toArray(new ClassTeaching[0]));
		}
		if(CollectionUtils.isNotEmpty(classTeachingExList)){
			classTeachingExService.saveAll(classTeachingExList.toArray(new ClassTeachingEx[0]));
		}
		if(CollectionUtils.isNotEmpty(courseScheduleList)){
			this.saveAllEntitys(courseScheduleList.toArray(new CourseSchedule[0]));
		}
		if(CollectionUtils.isNotEmpty(teachPlanExList)){
			teachPlanExService.saveAll(new TeachPlanEx[0]);
		}
	}
	@Override
	public List<CourseSchedule> findCourseScheduleListByClassIdes(String searchAcadyear, Integer searchSemester,
			String[] classId, Integer week, String showXN) {
		
		return courseScheduleJdbcDao.findCourseScheduleListByClassIdes(searchAcadyear,searchSemester,classId,week,showXN);
	}
	
	@Override
	public void saveScheduleModify(List<CourseSchedule> insertList, List<String> delIds) {
		if(CollectionUtils.isNotEmpty(insertList)) {
			this.saveAllEntitys(insertList.toArray(new CourseSchedule[0]));
		}
		if(CollectionUtils.isNotEmpty(delIds)) {
			this.deleteByIds(delIds.toArray(new String[0]));
		}
		
	}
	
	@Override
	public Map<String,Integer> getClassRealHour(String unitId, String[] gradeIds, String acadyear, String semester,
												int weekIndex){
		Map<String,Integer> map = new HashMap<>();
		
		List<Object[]> result = courseScheduleDao.getClassRealHour(unitId, gradeIds, acadyear, semester, weekIndex);
		for (Object[] objects : result) {
			map.put((String)objects[0], ((BigDecimal)objects[1]).intValue());
		}
		
		return map;
	}
	@Override
	public void deleteByCourseIdIn(String[] courseIds) {
		courseScheduleDao.deleteByCourseIdIn(courseIds);
	}
	
	
	@Override
	public void saveRevertSchedule(String unitId, String acadyear, String semester, List<String> classIdList,
			List<CourseSchedule> savedList) {
		//先删除 原来的课表;如果原来的classId不存在了 可能删不干净
		if(CollectionUtils.isNotEmpty(classIdList)) {
			if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
				return;
			}
			
			CourseScheduleDto deleteDto = new CourseScheduleDto();
			deleteDto.setSchoolId(unitId);
			deleteDto.setAcadyear(acadyear);
			deleteDto.setSemester(Integer.parseInt(semester));
			deleteDto.setClassIds(classIdList.toArray(new String[0]));
			this.deleteByClassId(deleteDto);
		}
		
		// 保存课表
		this.saveAllEntitys(savedList.toArray(new CourseSchedule[0]));
	}
	@Override
	public Map<String, Set<String>> findCourseScheduleMapByObjId(String acadyear, Integer semester, String type,
			Integer week, String ownerId) {
		Map<String,Set<String>> reMap  = new HashMap<String, Set<String>>();
		 if("2".equals(type)){//学生
				Set<String> claIds = new HashSet<String>();
				Student student = studentService.findOne(ownerId);
				if(student!=null){
					claIds.add(student.getClassId());
				}
				// 获取学年学期下学生的所有的教学班List
				List<TeachClass> teachClasses =teachClassStuService.findByStudentId2(ownerId, acadyear, semester+"");
		        if(CollectionUtils.isNotEmpty(teachClasses)){
		        	for(TeachClass t:teachClasses){
		        		claIds.add(t.getId());
		        	}
		        }
		      //6.0选课学生进入课程表数据
		    	List<String> jxclaids=null;
		    	if(openApiNewElectiveService!=null && student!=null){
		        	jxclaids = openApiNewElectiveService.getClassByUidSemesterStuId(student.getSchoolId(), acadyear, semester+"", student.getId());
		        }
		    	if(CollectionUtils.isNotEmpty(jxclaids)){
		    		claIds.addAll(jxclaids);
		    	}
		    	
		       //查出学生课程
	        	if(CollectionUtils.isNotEmpty(claIds)){
	        		List<CourseSchedule>  timetableCourseScheduleList = courseScheduleDao.findCourseScheduleListByClassIdes(acadyear, Integer.valueOf(semester), week,claIds.toArray(new String[0]));
	        		Set<String> teaIds;
	        		for(CourseSchedule sle : timetableCourseScheduleList){
	        			if(!reMap.containsKey(sle.getSubjectId())){
	        				teaIds = new HashSet<String>();
	        				reMap.put(sle.getSubjectId(), teaIds);
	        			}
	        			reMap.get(sle.getSubjectId()).add(sle.getTeacherId());
	        		}
	        	}
		 }else if("1".equals(type)){//教师
			 List<CourseSchedule>  timetableCourseScheduleList = courseScheduleJdbcDao.findCourseScheduleListByTeacherId(acadyear,semester, ownerId, week);
			 Set<String> subjectIds;
    		for(CourseSchedule sle : timetableCourseScheduleList){
    			if(!reMap.containsKey(sle.getClassId() + "," + sle.getClassType())){
    				subjectIds = new HashSet<String>();
    				reMap.put(sle.getClassId() + "," + sle.getClassType(), subjectIds);
    			}
    			reMap.get(sle.getClassId() + "," + sle.getClassType()).add(sle.getSubjectId());
    		}
		 }
		return reMap;
	}
	
	@Override
	public List<CourseSchedule> findByWeekTimes(CourseScheduleDto dto, String[] timeStr) {
		if(ArrayUtils.isEmpty(timeStr)){
			return new ArrayList<CourseSchedule>();
		}
		return courseScheduleJdbcDao.findByWeekTimes(dto,timeStr);
	}
	
	@Override
	public List<String> findAllPlaceIds(String schoolId, String searchAcadyear, Integer searchSemester,
			Integer weekIndex, String gradeId) {
		return courseScheduleJdbcDao.findAllPlaceIds(schoolId, searchAcadyear, searchSemester, weekIndex, gradeId);
	}

	@Override
	public void updatePunchCard(String schoolId, String acadyear, Integer semester,
								String classId, Map<String, Integer> subStatus){
		if(MapUtils.isEmpty(subStatus)){
			return;
		}
		List<CourseSchedule> courseScheduleList = this.findCourseScheduleListByClassIdsAndSubjectIds(schoolId, acadyear, semester,
				new String[]{classId}, subStatus.keySet().toArray(new String[0]));
		Date now = new Date();
		courseScheduleList.stream().filter(e->subStatus.containsKey(e.getSubjectId()))
		.forEach(e->{
			e.setPunchCard(subStatus.get(e.getSubjectId()));
			e.setModifyTime(now);
		});
		this.saveAllEntitys(courseScheduleList.toArray(new CourseSchedule[0]));
	}
}
