package net.zdsoft.newgkelective.data.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.custom.XyConstant;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.utils.*;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jgrapht.alg.color.SmallestDegreeLastColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.SectionDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassHourExRemoteService;
import net.zdsoft.basedata.remote.service.ClassHourRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StudentSelectSubjectRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassExRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkArrayDao;
import net.zdsoft.newgkelective.data.dao.NewGkCourseHeapDao;
import net.zdsoft.newgkelective.data.dao.NewGkSubjectTeacherDao;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkCourseHeap;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.optaplanner.solver.CGForLectureSolver;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkCourseHeapService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkElectiveArrayComputeService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.springframework.web.client.RestTemplate;

@Service("newGkArrayService")
public class NewGkArrayServiceImpl extends BaseServiceImpl<NewGkArray, String> implements NewGkArrayService {
	Logger log = Logger.getLogger(NewGkArrayServiceImpl.class);
	private static RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;
	@Autowired
    private NewGkDivideService newGkDivideService;
	@Autowired
    private NewGkChoiceService newGkChoiceService;
	@Autowired
    private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkArrayDao newGkArrayDao;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private TeachClassExRemoteService teachClassExRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private GradeTeachingRemoteService gradeTeachingRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	private NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	private NewGkCourseHeapDao newGkCourseHeapDao;
	@Autowired
	private NewGkSubjectTeacherDao newGkSubjectTeacherDao;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private NewGkElectiveArrayComputeService arrayComputeService;
	@Autowired
	private NewGkCourseHeapService newGkCourseHeapService;
	@Autowired
	private NewGkClassCombineRelationService combineRelationService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassHourRemoteService classHourRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SchoolRemoteService schoolService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private ClassHourExRemoteService classHourExRemoteService;

	@Override
	protected BaseJpaRepositoryDao<NewGkArray, String> getJpaDao() {
		return newGkArrayDao;
	}

	@Override
	protected Class<NewGkArray> getEntityClass() {
		return NewGkArray.class;
	}

	@Override
	public void saveArray(NewGkArray addDto) {
		
		NewGkArray old = findOne(addDto.getId());
		if(old == null || !old.getDivideId().equals(addDto.getDivideId())) { // 已经复制过班级学生数据 且没有修改分班方案
			save(addDto);
			List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(addDto.getUnitId(), 
					addDto.getDivideId(), null, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, true);
			List<NewGkDivideClass> delClassList = newGkDivideClassService.findByDivideIdAndSourceType(addDto.getId(), NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
			Set<String> delClassIds = EntityUtils.getSet(delClassList, NewGkDivideClass::getId);
			
			List<NewGkClassStudent> addStuList = new ArrayList<NewGkClassStudent>();
			List<NewGkDivideClass> newClassList = new ArrayList<NewGkDivideClass>();
			
			arrayComputeService.copyDivideClassToArray(allClassList, addDto, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, addStuList, newClassList);
			newGkDivideClassService.saveAllList(addDto.getUnitId(), addDto.getDivideId(), delClassIds.toArray(new String[] {}), newClassList, addStuList, false);
			
//			allClassList = newGkDivideClassService.findByDivideIdAndClassType(addDto.getUnitId(), 
//					addDto.getId(), null, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
//			List<NewGkDivideClass> findByDivideIdAndSourceType = newGkDivideClassService.findByDivideIdAndSourceType(addDto.getId(), NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
			
			// 同时将走班课程 存入课程表
			newGkTimetableService.updatePreTimetable(addDto.getId(),newClassList);
		}else {
			save(addDto);
		}
	}

	@Override
	public List<NewGkArray> findByDivideId(String divideId) {

		return newGkArrayDao.findByDivideId(divideId);
	}

	@Override
	public List<NewGkArray> findByGradeId(String unitId, String gradeId,String stat,String arrangeType) {
		
		Specification<NewGkArray> s = new Specification<NewGkArray>() {
			@Override
			public Predicate toPredicate(Root<NewGkArray> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				 if(StringUtils.isNotBlank(gradeId)) {
					 ps.add(cb.equal(root.get("gradeId").as(String.class), gradeId));
				 }
				 if(StringUtils.isNotBlank(stat)){
                	 ps.add(root.get("stat").as(String.class).in(stat));
				 }
				 ps.add(cb.equal(root.get("arrangeType").as(String.class), arrangeType));
				 
				 ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				 //排序
				 List<Order> orderList=new ArrayList<>();
				 orderList.add(cb.desc(root.get("times").as(Integer.class)));
				 orderList.add(cb.desc(root.get("creationTime").as(Date.class)));
				 cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				 
				 return cq.getRestriction();
			}
        };
        
        return newGkArrayDao.findAll(s);
		
//		if(StringUtils.isNotBlank(stat)){
//			return newGkArrayDao.findByGradeId(unitId, gradeId,stat);
//		}
//		return newGkArrayDao.findByGradeId(unitId, gradeId);
	}
	
	@Override
	public List<NewGkArray> findByGradeIdWithMaster(String unitId, String gradeId,String stat,String arrangeType){
		return this.findByGradeId(unitId, gradeId, stat, arrangeType);
	}
	
	@Override
	public int findMaxByGradeId(String unitId, String gradeId,String arrangeType) {
		Integer maxTimes;
		if(StringUtils.isNotBlank(gradeId)) {
			maxTimes = newGkArrayDao.findMaxByGradeIdArrangeType(unitId, gradeId,arrangeType);
		}else {
			maxTimes = newGkArrayDao.findMaxByArrangeType(unitId, arrangeType);
		}
		
		if (maxTimes == null) {
			return 0;
		}
		return maxTimes;
	}

	@Override
	public void updateStatById(String stat, String id) {
		newGkArrayDao.updateStatById(stat, id);
	}

	@Override
	public void deleteById(String unitId, String id) {
		//newgkelective_timetable(周课表)
		//newgkelective_timetable_other(周课表坐标点)
		//newgkelective_timetable_teach(周课表的老师)
		newGkTimetableService.deleteByArrayId(id);
		
		//newgkelective_subject_teacher(预授课程老师)
		newGkSubjectTeacherDao.deleteByArrayId(id);
		
		//newgkelective_course_heap(算法分摊)
		newGkCourseHeapDao.deleteByArrayId(id);
		
		//newgkelective_divide_class(分班后班级)
		//newgkelective_class_student(班级内学生)
		newGkDivideClassService.deleteByDivideId(unitId, id);
		
		//(newgkelective_choice_relation(选课内关联)
		
		//newgkelective_array
		newGkArrayDao.deleteById(new Date(),id);
		
	}

	@Override
	public NewGkArray findById(String arrayId) {
		return newGkArrayDao.findbyId(arrayId);
	}
	/**
	 * 获取选课结果写入BASE_STUDENT_SELSUB
	 * @param divideId
	 * @param grade
	 * @param acadyear
	 * @param semester
	 */
	private void makeStudentSelectSubject(String divideId,Grade grade,String acadyear,Integer semester,Date now) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
        NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
        List<NewGkChoResult> choiceList = newGkChoResultService.findByChoiceIdAndKindType(grade.getSchoolId(), NewGkElectiveConstant.KIND_TYPE_01, choice.getId());
        List<StudentSelectSubject> resultList = new ArrayList<>();
        for (NewGkChoResult one : choiceList) {
            StudentSelectSubject studentSelectSubject = new StudentSelectSubject();
            studentSelectSubject.setId(UuidUtils.generateUuid());
            studentSelectSubject.setGradeId(grade.getId());
            studentSelectSubject.setSchoolId(grade.getSchoolId());
            studentSelectSubject.setStudentId(one.getStudentId());
            studentSelectSubject.setSubjectId(one.getSubjectId());
            studentSelectSubject.setAcadyear(acadyear);
            studentSelectSubject.setSemester(semester);
            studentSelectSubject.setCreationTime(now);
            studentSelectSubject.setModifyTime(now);
            studentSelectSubject.setIsDeleted(0);
            resultList.add(studentSelectSubject);
        }
        studentSelectSubjectRemoteService.updateStudentSelectsByGradeId(grade.getSchoolId(), acadyear, semester, grade.getId(), resultList);
	}
	/**
	 * 封装保存班级数据已经修改学生数据
	 */
	private void makeClassAndStudent(Grade grade,List<NewGkDivideClass> xzbClaList,
			Map<String, Clazz> oldClaMap,Map<String,String> classPlaceMap,Map<String,String> OldorXId,Map<String,String> stuIdNewXzbCidMap,Date now) {
		List<Clazz> upClaList = new ArrayList<Clazz>();
		List<Clazz> addClaList = new ArrayList<Clazz>();
		
		List<Student> studentList = Student.dt(studentRemoteService.findPartStudByGradeId(grade.getSchoolId(), grade.getId(), null, null));
		Map<String, Student> studentMap = new HashMap<String, Student>();
		if (CollectionUtils.isNotEmpty(studentList)) {
			studentMap = EntityUtils.getMap(studentList, Student::getId);
		}
		//未安排的学生(理论上没有)
		Set<String> notStudentIds = studentMap.keySet();
		//更新信息的学生
		List<Student> updateStudentList = new ArrayList<Student>();
		
		Student student = null;
		//班级名称序号
		int i = 0;
		//班级code前缀  入学年份+2位学段+2位序号
		int enrollYear = NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));
		String sectionStr = StringUtils.leftPad(grade.getSection() + "", 2, "0");
		Set<String> classCodes = new HashSet<>();
		for (NewGkDivideClass divideClass : xzbClaList) {   
			// 行政班班级 和 学生 ；增加 或者更新 
			Clazz clazz = null;
			String oldclaid =divideClass.getOldClassId();
			if(StringUtils.isNotBlank(oldclaid)){
				clazz = oldClaMap.get(oldclaid);
			}
			if(clazz == null && oldClaMap.get(divideClass.getId())!=null){//重复执行班级
				clazz = oldClaMap.get(divideClass.getId());
			}
			if(clazz==null){
				i++;
				String newId = oldclaid;
				if(newId == null)
					newId = divideClass.getId();
				clazz=new Clazz();
				clazz.setId(newId);
				clazz.setClassName(divideClass.getClassName());
				String classCode = enrollYear + StringUtils.leftPad(i + "", 2, "0");
				clazz.setClassCode(classCode);
				clazz.setSchoolId(grade.getSchoolId());
				clazz.setAcadyear(grade.getOpenAcadyear());
				clazz.setIsGraduate(NewGkElectiveConstant.IF_INT_0);
				clazz.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				clazz.setSchoolingLength(grade.getSchoolingLength());
				clazz.setGradeId(grade.getId());
				clazz.setSection(grade.getSection());
				clazz.setArtScienceType(0);
				clazz.setBuildDate(now);
				clazz.setCreationTime(now);
				clazz.setModifyTime(now);
				clazz.setRemark("智能分班排课重组班级");
				addClaList.add(clazz);
			}else{
				if(clazz.getClassCode().length()==8){
					clazz.setClassCode(clazz.getClassCode().substring(0,4)+clazz.getClassCode().substring(6,8));
				}
				clazz.setIsGraduate(NewGkElectiveConstant.IF_INT_0);
				clazz.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				clazz.setModifyTime(now);
				if(!clazz.getGradeId().equals(grade.getId())) {
					//错误班级数据
					log.info("班级id:"+clazz.getId()+",数据有误，根据分出班级id能找到对应行政班班级id,但年级id:"+clazz.getGradeId()+"与排课年级id"+grade.getId()+"不匹配");
				}
				clazz.setGradeId(grade.getId());
				upClaList.add(clazz);
				oldClaMap.remove(clazz.getId());
			}
			classCodes.add(clazz.getClassCode());
			OldorXId.put(divideClass.getId(), clazz.getId());
			clazz.setTeachPlaceId(classPlaceMap.get(divideClass.getId()));
			if(CollectionUtils.isNotEmpty(divideClass.getStudentList())) {
				//修改学生班级信息
				for (String studentId : divideClass.getStudentList()) {
					student = studentMap.get(studentId);
					if(student!=null){
						student.setClassId(clazz.getId());
						student.setModifyTime(new Date());
						updateStudentList.add(student);
						notStudentIds.remove(studentId);
						stuIdNewXzbCidMap.put(studentId, clazz.getId());
					}else {
						log.info(divideClass.getClassName()+"下学生id="+studentId+"学生数据不正常，可能对应的班级不是正常班级");
					}
				}
			}
		}
		i = dealClassCode(upClaList, i, enrollYear, classCodes);
		//未选科的学生班 重组一个班
		if (notStudentIds.size() > 0) {
			Clazz clazz = new Clazz();
//			//判断是否重复班级名称			
			clazz.setId(UuidUtils.generateUuid());
			clazz.setRemark("智能分班排课重组班级");
			//clazz.setClassName(StringUtils.leftPad(i + "", 2, "0")+"班");
			clazz.setClassName("未选科学生班");
			String classCode = enrollYear + StringUtils.leftPad("0", 2, "0");
			clazz.setClassCode(classCode);
			clazz.setSchoolId(grade.getSchoolId());
			clazz.setAcadyear(grade.getOpenAcadyear());
			clazz.setIsGraduate(NewGkElectiveConstant.IF_INT_0);
			clazz.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
			clazz.setSchoolingLength(grade.getSchoolingLength());
			clazz.setGradeId(grade.getId());
			clazz.setSection(grade.getSection());
			clazz.setArtScienceType(0);
			clazz.setBuildDate(new Date());
			clazz.setCreationTime(new Date());
			clazz.setModifyTime(new Date());
			addClaList.add(clazz);
			
			//修改学生班级信息
			for (String studentId : notStudentIds) {
				student = studentMap.get(studentId);
				student.setClassId(clazz.getId());
				student.setModifyTime(new Date());
				updateStudentList.add(student);
			}
		}
		if(MapUtils.isNotEmpty(oldClaMap)) {
			for (String key : oldClaMap.keySet()) {
				Clazz oldClazz = oldClaMap.get(key);
				//方便业务数据查找
				oldClazz.setIsGraduate(Clazz.GRADUATED_JS);
				oldClazz.setRemark("智能分班排课毕业原来行政班级");
				upClaList.add(oldClazz);
			}
		}
		
		// 保存 班级 更新或添加；更新学生班级信息
		addClaList.addAll(upClaList);
		if (CollectionUtils.isNotEmpty(addClaList)) {
			classRemoteService.saveAllEntitys(SUtils.s(addClaList));
		}
		if (CollectionUtils.isNotEmpty(updateStudentList)) {
			studentRemoteService.updateClaIds(updateStudentList);
		}
	}

	/**
	 * 处理classCode 为00的班级
	 * @param upClaList
	 * @param i
	 * @param enrollYear
	 * @param classCodes
	 */
	private int dealClassCode(List<Clazz> upClaList, int i, int enrollYear, Set<String> classCodes) {
		for (Clazz clazz : upClaList) {
			if (clazz.getClassCode().endsWith("00")) {
				String newCode = null;
				for (i++; i <= 99; i++) {
					newCode = enrollYear + StringUtils.leftPad(i + "", 2, "0");
					if (!classCodes.contains(newCode)) {
						clazz.setClassCode(newCode);
						classCodes.add(newCode);
						break;
					}
				}
			}
		}
		return i;
	}


	@Override
	public ResultDto pushThirdParty(Grade grade, String pushHost) {
		final ResultDto resultDto = new ResultDto().setSuccess(true).setMsg("推送成功");
		try {
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(grade.getSchoolId(), grade.getId()), new TR<List<Clazz>>() {});
            List<Student> studentList = Student.dt(studentRemoteService.findPartStudByGradeId(grade.getSchoolId(), grade.getId(), null, null));

            Map<String, Clazz> clazzMap = clazzList.stream().filter(e -> Clazz.NOT_GRADUATED.equals(e.getIsGraduate()) && e.getIsDeleted() == NewGkElectiveConstant.IF_INT_0)
                    .collect(Collectors.toMap(e -> e.getId(), e -> e));
            //TODO 年级名称 构造方法，待完善 grade
            String gradeName;
            Integer section = grade.getSection();
            String gradeLevelStr = grade.getGradeCode().substring(grade.getGradeCode().length() - 1);
            int gradeLevel = Integer.parseInt(gradeLevelStr);
            String[] numStrArr = new String[]{"零","一","二","三","四","五","六","七","八","九","十"};
            if(Objects.equals(section, BaseConstants.SECTION_PRIMARY)){
                // 小学
                gradeName = numStrArr[gradeLevel] + "年级";
            }else if(Objects.equals(section,BaseConstants.SECTION_JUNIOR)){
                //  初中
                gradeName = numStrArr[gradeLevel+6] + "年级";
            }else if(Objects.equals(section,BaseConstants.SECTION_HIGH_SCHOOL)){
                // 高中
                gradeName = "高" + numStrArr[gradeLevel];
            }else{
                //其他学段的年级 不进行通知
				return resultDto.setSuccess(false).setMsg("推送失败 原因：其他学段的年级 不进行通知");
            }


            JSONArray stuArray = new JSONArray();
            JSONObject jo;
            for (Student stu : studentList){
                String classId = stu.getClassId();
                if(clazzMap.containsKey(classId)){
                    Clazz clazz = clazzMap.get(classId);
                    String classCode = clazz.getClassCode();
					String classNumber = classCode.substring(classCode.length() - 2);
                    if(classNumber.startsWith("0")){
                    	classNumber = classCode.substring(classCode.length() - 1);
					}

                    jo = new JSONObject();
                    jo.put("userId",stu.getId());
                    jo.put("classNumber",classNumber);
                    jo.put("grade",gradeName);
                    stuArray.add(jo);
                }
            }
			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("students",stuArray);
//            bodyMap.put("students",stuArray.toJSONString());

//			Map<String, String> headMap = new HashMap<>();
//			headMap.put("Content-Type","application/json");
//			headMap.put("aId", XyConstant.XY_APP_ID_VAL);

            String baseUrl = pushHost + "/portal/userManager/modifyStudent";

			// body
			String bodyParam = jsonObject1.toJSONString();
			// headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.set("aId",XyConstant.XY_APP_ID_VAL);
			headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
			HttpEntity<String> requestEntity = new HttpEntity<>(bodyParam, headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, String.class);
			String respInf = responseBody.getBody();

//			String respInf = HttpClientUtils.exeUrlSync(baseUrl, httpClientParam);
			// 写入日志
			JSONObject jsonObject = JSONObject.parseObject(respInf);
			if(jsonObject.containsKey("code")
					&& (Objects.equals(jsonObject.get("code"),200)
					|| Objects.equals(jsonObject.get("code"),"200"))){
				// 发送成功
			}else if(jsonObject.containsKey("msg")){
				resultDto.setSuccess(false).setMsg("推送失败 原因："+jsonObject.get("msg"));
			}else{
				// 失败 写日志
				resultDto.setSuccess(false).setMsg("推送失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 写入日志
			resultDto.setSuccess(false).setMsg("推送失败  原因:"+e.getMessage());
		}
		return resultDto;
	}

	private GradeTeaching initGradeTeaching(String unitId,String acadyear,String semester,String gradeId) {
		GradeTeaching gradeTeaching  =new GradeTeaching();
		gradeTeaching.setId(UuidUtils.generateUuid());
		gradeTeaching.setAcadyear(acadyear);
		gradeTeaching.setSemester(semester);
		gradeTeaching.setUnitId(unitId);
		gradeTeaching.setGradeId(gradeId);
		gradeTeaching.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
		gradeTeaching.setCreationTime(new Date());
		gradeTeaching.setModifyTime(new Date());
		return gradeTeaching;
	}
	
	private ClassTeaching initClassTeaching(String unitId,String acadyear,String semester) {
		ClassTeaching ctent = new ClassTeaching();
		ctent.setUnitId(unitId);
		ctent.setAcadyear(acadyear);
		ctent.setSemester(semester);
		ctent.setId(UuidUtils.generateUuid());
		ctent.setPunchCard(NewGkElectiveConstant.IF_INT_1);
		ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
		ctent.setModifyTime(new Date());
		ctent.setCreationTime(new Date());
//		ctent.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
		ctent.setEventSource(NewGkElectiveConstant.IF_INT_0);
		return ctent;
	}
	
	private Course makeVirtualCourse(String schoolId,Date now) {
		Course course = new Course();
		course.setId(UuidUtils.generateUuid());
		course.setCreationTime(now);
		course.setModifyTime(now);
		course.setType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
		course.setSubjectType(BaseConstants.UNIT_CLASS_SCH);
		course.setCourseTypeId(BaseConstants.VIRTUAL_COURSE_TYPE);
		course.setUnitId(schoolId);
		course.setIsDeleted(0);
		course.setIsUsing(1);
		course.setFullMark(5);
		course.setInitCredit(3);
		course.setInitPassMark(3);
		course.setEventSource(0);
		return course;
	}
	
	private List<Course> saveVirtualCourse(Grade grade,Map<String, List<NewGkDivideClass>> batchClassMap,Date now) {
		List<Course> virtualCourses2 = SUtils.dt(courseRemoteService.getVirtualCourses(grade.getSchoolId(), null), Course.class);
		List<Course> virtualCourses = virtualCourses2.stream()
				.filter(e->e.getSection()!= null && e.getSection().contains(grade.getSection()+""))
				.collect(Collectors.toList());
		if(batchClassMap.size() <= virtualCourses.size()) {
			return virtualCourses;
		}
		Set<String> subNames = EntityUtils.getSet(virtualCourses, Course::getSubjectName);
		Set<String> subCodes = EntityUtils.getSet(virtualCourses, Course::getSubjectCode);
		List<SectionDto> sectionDtos = getAllSectionByUnit(grade.getSchoolId());
		String[] sectionArray = EntityUtils.getSet(sectionDtos, "sectionValue").toArray(new String[0]);
		Arrays.sort(sectionArray);
		String section = String.join(",", sectionArray);
		Integer maxOrderId = virtualCourses2.stream().filter(e->e.getOrderId()!=null).map(e->e.getOrderId()).max(Integer::compareTo).orElse(0);
		String subName = "";
		String subCode = "";
		// 创建 N 个虚拟课程
		List<Course> addCourses = new ArrayList<>();
		int n=1;
		for(int i=1;i<=(batchClassMap.size() - virtualCourses.size());i++) {
			Course course = makeVirtualCourse(grade.getSchoolId(), now);
			do {
				subName = "走班课程"+n;
				subCode = "XNKC"+n;
				n++;
			}while(subNames.contains(subName) || subCodes.contains(subCode));
			course.setSubjectName(subName);
			course.setSubjectCode(subCode);
			course.setSection(section);
			course.setOrderId(++maxOrderId);
			addCourses.add(course);
		}
		courseRemoteService.saveAll(addCourses.toArray(new Course[0]));
		virtualCourses.addAll(addCourses);
		return virtualCourses;
	}
	
	private void initCourseScheduleList(ClassTeaching classTeaching,DateInfo startDateInfo,DateInfo endDateInfo,List<NewGkTimetableOther> timetableOtherList,
			List<CourseSchedule> insertCourseScheduleList){
		int startWeek = startDateInfo.getWeek();
		int startDay = startDateInfo.getWeekday() - 1;
		int endWeek = endDateInfo.getWeek();
		int weekday = endDateInfo.getWeekday()-1;
		for (NewGkTimetableOther o : timetableOtherList) {
			CourseSchedule c = new CourseSchedule();
			c.setAcadyear(startDateInfo.getAcadyear());
			c.setSemester(startDateInfo.getSemester());
			c.setSchoolId(startDateInfo.getSchoolId());
			c.setClassId(classTeaching.getClassId());
			c.setSubjectId(classTeaching.getSubjectId());
			c.setPlaceId(o.getPlaceId());
			c.setTeacherId(classTeaching.getTeacherId());
			c.setWeekType(o.getFirstsdWeek()==null?NewGkElectiveConstant.FIRSTSD_WEEK_3:o.getFirstsdWeek());
			c.setPunchCard(NewGkElectiveConstant.IF_INT_1);
			if (StringUtils.isNotBlank(classTeaching.getId())) {
				c.setClassType(CourseSchedule.CLASS_TYPE_NORMAL);
				c.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				c.setCourseId(classTeaching.getId());
			} else {
				c.setClassType(CourseSchedule.CLASS_TYPE_TEACH);
				c.setSubjectType(String.valueOf(CourseSchedule.SUBJECT_TYPE_1));
			}
			CourseSchedule c1 = null;
			//包括开始时间与结束时间 
			for (int index = startWeek; index <= endWeek; index++) {
				
				if (index == startWeek && o.getDayOfWeek() < startDay) {//开始日期
					continue;
				}
				if (index == endWeek && o.getDayOfWeek() > weekday) {//结束时间
					continue;
				}
				if((NewGkElectiveConstant.FIRSTSD_WEEK_1==c.getWeekType() && index%2==0) || (NewGkElectiveConstant.FIRSTSD_WEEK_2==c.getWeekType() && index%2==1)){
					continue;
				}
				c1 = new CourseSchedule();
				EntityUtils.copyProperties(c, c1);
				c1.setId(UuidUtils.generateUuid());
				c1.setWeekOfWorktime(index);
				c1.setPeriod(o.getPeriod());
				c1.setPeriodInterval(o.getPeriodInterval());
				c1.setDayOfWeek(o.getDayOfWeek());
				insertCourseScheduleList.add(c1);
			}
		}
		
	}
	
	private void initCourseScheduleList2(ClassTeaching classTeaching,DateInfo startDateInfo,DateInfo endDateInfo,Set<String> times,
			List<CourseSchedule> insertCourseScheduleList){
		int startWeek = startDateInfo.getWeek();
		int startDay = startDateInfo.getWeekday() - 1;
		int endWeek = endDateInfo.getWeek();
		int weekday = endDateInfo.getWeekday()-1;
		for (String o : times) {
			String[] timearr=o.split("_");
			CourseSchedule c = new CourseSchedule();
			c.setAcadyear(startDateInfo.getAcadyear());
			c.setSemester(startDateInfo.getSemester());
			c.setSchoolId(startDateInfo.getSchoolId());
			c.setClassId(classTeaching.getClassId());
			c.setSubjectId(classTeaching.getSubjectId());
			c.setWeekType(NewGkElectiveConstant.FIRSTSD_WEEK_3);
			c.setPunchCard(NewGkElectiveConstant.IF_INT_1);
			c.setClassType(CourseSchedule.CLASS_TYPE_NORMAL);
			c.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
			c.setCourseId(classTeaching.getId());
			CourseSchedule c1 = null;
			//包括开始时间与结束时间 
			Integer day = Integer.parseInt(timearr[0]);
			String periodInterval = timearr[1];
			Integer period = Integer.parseInt(timearr[2]);
			for (int index = startWeek; index <= endWeek; index++) {
				
				if (index == startWeek && day < startDay) {//开始日期
					continue;
				}
				if (index == endWeek && day > weekday) {//结束时间
					continue;
				}
				if((NewGkElectiveConstant.FIRSTSD_WEEK_1==c.getWeekType() && index%2==0) || (NewGkElectiveConstant.FIRSTSD_WEEK_2==c.getWeekType() && index%2==1)){
					continue;
				}
				c1 = new CourseSchedule();
				EntityUtils.copyProperties(c, c1);
				c1.setId(UuidUtils.generateUuid());
				c1.setWeekOfWorktime(index);
				c1.setPeriod(period);
				c1.setPeriodInterval(periodInterval);
				c1.setDayOfWeek(day);
				insertCourseScheduleList.add(c1);
			}
		}
		
	}
	
	@Override
	public void saveToSchedule(NewGkArray array, DateInfo startDateInfo, DateInfo endDateInfo) {
		Date now = new Date();
		String gradeId=array.getGradeId();
		String acadyear=startDateInfo.getAcadyear();
		Integer semester = startDateInfo.getSemester();
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(array.getGradeId()), new TR<Grade>() {});
		String schoolId=grade.getSchoolId();
		//1:7选3写入选课结果
		boolean isXzbArray = NewGkElectiveConstant.ARRANGE_XZB.equals(array.getArrangeType());
		if(!isXzbArray) {
			makeStudentSelectSubject(array.getDivideId(), grade, acadyear,semester,now);
		}else {
			saveToScheduleXzb(array, startDateInfo, endDateInfo);
			return;
		}
        //开设班级数据
		List<NewGkDivideClass> allDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(grade.getSchoolId(), array.getId(), 
				new String[] { NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4 },true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		Map<String, NewGkDivideClass> allClassMap = EntityUtils.getMap(allDivideClassList, e->e.getId());
		
		List<NewGkDivideClass> xzbClaList = allDivideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.collect(Collectors.toList());

		List<NewGkDivideClass> olnyjxbClazzList = allDivideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&& StringUtils.isNotBlank(e.getBatch()))
				.collect(Collectors.toList());
		
		

		// 排课结果---课表数据
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(array.getUnitId(), array.getId());
		newGkTimetableService.makeTeacher(timetableList);
		Map<String, List<NewGkTimetable>> timeTableByClassIdMap = timetableList.stream().collect(Collectors.groupingBy(NewGkTimetable::getClassId));
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]));
		Map<String, List<NewGkTimetableOther>> timetableOtherMap = timetableOtherList.stream().collect(Collectors.groupingBy(NewGkTimetableOther::getTimetableId));
		Set<String> allSubIds=new HashSet<>();
		//班级对应的场地
		Map<String,String> classPlaceMap = new HashMap<>();
		for (NewGkTimetable ttb : timetableList) {
			List<NewGkTimetableOther> list = timetableOtherMap.get(ttb.getId());
			if(CollectionUtils.isNotEmpty(list)) {
				String placeId = list.stream().map(e->e.getPlaceId()).filter(e->StringUtils.isNotBlank(e)).findFirst().orElse("");
				classPlaceMap.put(ttb.getClassId(), placeId);
			}
			allSubIds.add(ttb.getSubjectId());
		}
		
		//原行政班
		List<Clazz> oldClazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(array.getUnitId(), array.getGradeId()), new TR<List<Clazz>>() {});
		//老班级id，原行政班(包括已经删除等不正常状态的班级)
		Map<String, Clazz> oldClaMap=EntityUtils.getMap(oldClazzList, Clazz::getId);
		Set<String> oldClassIds = EntityUtils.getSet(xzbClaList, e->e.getOldClassId());
		List<Clazz> oldClazsByOldIds = classRemoteService.findListObjectByIds(oldClassIds.toArray(new String[0]));
		oldClazsByOldIds.forEach(e->oldClaMap.put(e.getId(), e));
		//不重组  老的行政班id 拷贝到新的行政班对应关系
		//包括所有  key:divideClassId,value:xzbId
		Map<String,String> OldorXId =new HashMap<String, String>();
		//最后学生id对应的新行政班id
		Map<String,String> stuIdNewXzbCidMap = new HashMap<>();
		makeClassAndStudent(grade, xzbClaList, oldClaMap, classPlaceMap, OldorXId, stuIdNewXzbCidMap, now);
		
		//虚拟课程
		// 7选 3 排课
		Map<String, List<NewGkDivideClass>> batchClassMap = EntityUtils.getListMap(olnyjxbClazzList, e->e.getSubjectType()+"-"+e.getBatch(), e->e);
		//保存虚拟课程
		List<Course> virtualCourses = saveVirtualCourse(grade, batchClassMap, now);
		// 将批次 和 虚拟课程 一一对应
		//key:bath value:courseId
		Map<String,String> bathCourseIdMap=new HashMap<>();
		int n=0;
		for (String batchstr : batchClassMap.keySet()) {
			String id = virtualCourses.get(n++).getId();
			bathCourseIdMap.put(batchstr, id);
		}
		
		
		//老的教学班包括 allSubIds 或者关联虚拟课程的
		List<TeachClass> oldAllTeachClassList = SUtils.dt(teachClassRemoteService.findBySearch(array.getUnitId(),startDateInfo.getAcadyear(),
				startDateInfo.getSemester()+"",TeachClass.CLASS_TYPE_REQUIRED,array.getGradeId(),null), TeachClass.class);
		
		List<TeachClass> oldTeachClassList = oldAllTeachClassList.stream().filter(e->StringUtils.isNotBlank(e.getRelaCourseId()) || allSubIds.contains(e.getCourseId())).collect(Collectors.toList());
		oldAllTeachClassList.removeAll(oldTeachClassList);
		
		//key:subjectId
		Map<String, GradeTeaching> oldGraTeaMap = SUtils.dt(gradeTeachingRemoteService.findBySearchMap(startDateInfo.getSchoolId(), 
				startDateInfo.getAcadyear(), String.valueOf(startDateInfo.getSemester()),array.getGradeId()), new TR<Map<String, GradeTeaching>>(){});
		List<ClassTeaching> ctList = SUtils.dt(classTeachingRemoteService.findListByGradeId(startDateInfo.getAcadyear(),
				String.valueOf(startDateInfo.getSemester()), startDateInfo.getSchoolId(), grade.getId()),ClassTeaching.class);
		Map<String, List<ClassTeaching>> oldCtMap = ctList.stream().collect(Collectors.groupingBy(t -> t.getSubjectId()+t.getClassId()));
		
		List<TeachClass> addTeachClassList=new ArrayList<>();
		List<TeachClass> updateTeachClassList=new ArrayList<>();
		List<TeachClassStu> teachClassStuList=new ArrayList<>();
		List<TeachClassEx> teachExList=new ArrayList<>();
		
		List<GradeTeaching> upGtList=new ArrayList<>();
		List<GradeTeaching> addGtList=new ArrayList<>();
		
		List<ClassTeaching> upctList=new ArrayList<>();
		List<ClassTeaching> addctList=new ArrayList<>();
		
		//新课表
		List<CourseSchedule> insertCourseScheduleList = new ArrayList<CourseSchedule>();
		
		//只针对oldTeachClassList进行处理
		Map<String,TeachClass> oldTeachClaMap=EntityUtils.getMap(oldTeachClassList, TeachClass::getId);
		//key:classId key1:subjectId
		Map<String,Map<String,ClassTeaching>> xzbClassTeachingMap=new HashMap<>();
		ClassTeaching ct=null;
		//key:subjectId
		Map<String,GradeTeaching> gtBySubject=new HashMap<>();
		Map<String,Set<String>> xnBathTimeMap=new HashMap<>();
		Map<String,Set<String>> xnClassMap=new HashMap<>();
		GradeTeaching gt=null;
		
		//存放虚拟课程初始化对应base_class_hour的id
		Map<String,String> xnCourseHourIdMap=new HashMap<>();
		
		
		for(NewGkDivideClass cc:allDivideClassList) {
			if(cc.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)) {
				List<NewGkTimetable> list1 = timeTableByClassIdMap.get(cc.getId());
				if(CollectionUtils.isEmpty(list1)) {
					continue;
				}
				Map<String, ClassTeaching> map2 = xzbClassTeachingMap.get(OldorXId.get(cc.getId()));
				if(map2==null) {
					map2=new HashMap<>();
					xzbClassTeachingMap.put(OldorXId.get(cc.getId()), map2);
				}
				for(NewGkTimetable nn:list1) {
					if(!map2.containsKey(nn.getSubjectId())) {
						List<ClassTeaching> ctList1=oldCtMap.get(nn.getSubjectId()+OldorXId.get(cc.getId()));
						if(CollectionUtils.isNotEmpty(ctList1)) {
							ct=ctList1.get(0);
							ct.setModifyTime(new Date());
							upctList.add(ct);
						}else {
							ct=initClassTeaching(schoolId, acadyear, semester+"");
							addctList.add(ct);
						}
						map2.put(ct.getSubjectId(), ct);
					}else {
						ct=map2.get(nn.getSubjectId());
					}
					
					ct.setClassId(OldorXId.get(cc.getId()));
					ct.setSubjectId(nn.getSubjectId());
					ct.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
					ct.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
					List<NewGkTimetableOther> listO = timetableOtherMap.get(nn.getId());
					
					// 目前只有行政班有 单双周
					if(CollectionUtils.isEmpty(listO)) {
						continue;
					}
					Optional<Integer> optional = listO.stream().filter(e->Objects.equals(NewGkElectiveConstant.WEEK_TYPE_EVEN, e.getFirstsdWeek())
							|| Objects.equals(NewGkElectiveConstant.WEEK_TYPE_ODD, e.getFirstsdWeek())).map(e->e.getFirstsdWeek()).findFirst();
					if(optional.isPresent()) {
						//是单双周
						ct.setWeekType(optional.get());
					}
					if(CollectionUtils.isNotEmpty(nn.getTeacherIdList())) {
						ct.setTeacherId(nn.getTeacherIdList().get(0));
					}
					
					ct.setCourseHour(Float.valueOf(listO.size()));
					
					if(!gtBySubject.containsKey(nn.getSubjectId())) {
						gt=oldGraTeaMap.get(nn.getSubjectId());
						if(gt==null) {
							gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
							gt.setModifyTime(now);
							addGtList.add(gt);
						}else {
							upGtList.add(gt);
						}
						gt.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
						gt.setSubjectId(nn.getSubjectId());
						gtBySubject.put(nn.getSubjectId(), gt);
					}
					//直接行政班课表
					initCourseScheduleList(ct, startDateInfo, endDateInfo, listO, insertCourseScheduleList);
				}
			}else  if(cc.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2)) {
				//开设教学班
				TeachClass teachClass = oldTeachClaMap.get(cc.getId());
				if(teachClass==null){
					teachClass = new TeachClass();
					teachClass.setId(cc.getId());
					teachClass.setUnitId(startDateInfo.getSchoolId());
					//所有都是必修
					teachClass.setClassType(TeachClass.CLASS_TYPE_REQUIRED);
					teachClass.setCreationTime(new Date());
					addTeachClassList.add(teachClass);
				}else {
					updateTeachClassList.add(teachClass);
					oldTeachClaMap.remove(teachClass.getId());
				}
				teachClass.setAcadyear(startDateInfo.getAcadyear());
				teachClass.setSemester(startDateInfo.getSemester()+"");
				teachClass.setName(cc.getClassName());
				teachClass.setGradeId(array.getGradeId());
				teachClass.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				teachClass.setPunchCard(NewGkElectiveConstant.IF_INT_1);
				teachClass.setCourseId(cc.getSubjectIds());
				teachClass.setIsDeleted(NewGkElectiveConstant.IF_INT_0);;
				teachClass.setIsUsing(NewGkElectiveConstant.IF_INT_1+"");
				teachClass.setIsUsingMerge(NewGkElectiveConstant.IF_0);
				teachClass.setIsMerge(NewGkElectiveConstant.IF_0);
				teachClass.setModifyTime(new Date());
				//新增教学班学生信息
				for (String studentId : cc.getStudentList()) {
					TeachClassStu teachClassStu = new TeachClassStu();
					teachClassStu.setStudentId(studentId);
					teachClassStu.setClassId(teachClass.getId());
					teachClassStu.setId(UuidUtils.generateUuid());
					teachClassStu.setCreationTime(new Date());
					teachClassStu.setModifyTime(new Date());
					teachClassStu.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
					teachClassStuList.add(teachClassStu);
				}
				if(StringUtils.isNotBlank(cc.getBatch())) {
					String placeId = classPlaceMap.get(cc.getId());
					teachClass.setPlaceId(placeId);
					//纯教学班
					String xnCourseId=bathCourseIdMap.get(cc.getSubjectType()+"-"+cc.getBatch());
					
					//换算
					if(!xnCourseHourIdMap.containsKey(xnCourseId)) {
						xnCourseHourIdMap.put(xnCourseId, UuidUtils.generateUuid());
					}
					teachClass.setRelaCourseId(xnCourseHourIdMap.get(xnCourseId));
					
					
					List<NewGkTimetable> list1 = timeTableByClassIdMap.get(cc.getId());
					if(CollectionUtils.isEmpty(list1)) {
						teachClass.setTeacherId(BaseConstants.ZERO_GUID);
						//直接开设年级科目,否则教学班不好开设
						if(!gtBySubject.containsKey(cc.getSubjectIds())) {
							gt=oldGraTeaMap.get(cc.getSubjectIds());
							if(gt==null) {
								gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
								gt.setModifyTime(now);
								addGtList.add(gt);
							}else {
								upGtList.add(gt);
							}
							gt.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
							gt.setSubjectId(cc.getSubjectIds());
							gtBySubject.put(cc.getSubjectIds(), gt);
						}
						continue;
					}
					String tid ="" ;
					if(CollectionUtils.isNotEmpty(list1.get(0).getTeacherIdList())) {
						tid=list1.get(0).getTeacherIdList().get(0);
					}
					if(StringUtils.isNotBlank(tid)){
						teachClass.setTeacherId(tid);
					}else {
						teachClass.setTeacherId(BaseConstants.ZERO_GUID);
					}
					List<NewGkTimetableOther> allListo=new ArrayList<NewGkTimetableOther>();
					//时间点
					Set<String> times=new HashSet<>();
					for(NewGkTimetable nn:list1) {
						List<NewGkTimetableOther> listO = timetableOtherMap.get(nn.getId());
						if(CollectionUtils.isEmpty(listO)) {
							continue;
						}
						if(!gtBySubject.containsKey(nn.getSubjectId())) {
							gt=oldGraTeaMap.get(nn.getSubjectId());
							if(gt==null) {
								gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
								gt.setModifyTime(now);
								addGtList.add(gt);
							}else {
								upGtList.add(gt);
							}
							gt.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
							gt.setSubjectId(nn.getSubjectId());
							gtBySubject.put(cc.getSubjectIds(), gt);
						}
						for(NewGkTimetableOther o:listO) {
							times.add(o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod()+"_"+o.getFirstsdWeek());
						}
						allListo.addAll(listO);
					}
					//需要开设虚拟课程
					if(!gtBySubject.containsKey(xnCourseId)) {
						gt=oldGraTeaMap.get(xnCourseId);
						if(gt==null) {
							gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
							addGtList.add(gt);
						}else {
							gt.setModifyTime(now);
							upGtList.add(gt);
						}
						gt.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
						gt.setSubjectId(xnCourseId);
						gtBySubject.put(xnCourseId, gt);
					}
					//行政班开设虚拟课程
					Set<String> xzbIds=new HashSet<>();
					for(String s:cc.getStudentList()) {
						if(StringUtils.isNotBlank(stuIdNewXzbCidMap.get(s))) {
							xzbIds.add(stuIdNewXzbCidMap.get(s));
						}
					}
					for(String s:xzbIds) {
						Map<String, ClassTeaching> map2 = xzbClassTeachingMap.get(s);
						if(map2==null) {
							map2=new HashMap<>();
							xzbClassTeachingMap.put(s, map2);
						}
						if(!map2.containsKey(xnCourseId)) {
							List<ClassTeaching> ctList1=oldCtMap.get(xnCourseId+s);
							if(CollectionUtils.isNotEmpty(ctList1)) {
								ct=ctList1.get(0);
								ct.setModifyTime(new Date());
								upctList.add(ct);
							}else {
								ct=initClassTeaching(schoolId, acadyear, semester+"");
								addctList.add(ct);
							}
							ct.setClassId(s);
							ct.setSubjectId(xnCourseId);
							ct.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
							ct.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
							map2.put(xnCourseId, ct);
						}
					}
					//虚拟课程时间
					Set<String> timeList = xnBathTimeMap.get(xnCourseId);
					if(CollectionUtils.isEmpty(timeList)) {
						timeList=new HashSet<>();
						xnBathTimeMap.put(xnCourseId,timeList);
					}
					//虚拟对应行政班
					Set<String> classIdList=xnClassMap.get(xnCourseId);
					if(CollectionUtils.isEmpty(classIdList)) {
						classIdList=new HashSet<>();
						xnClassMap.put(xnCourseId,classIdList);
					}
					for(String time:times) {
						TeachClassEx ex=new TeachClassEx();
						String[] arr = time.split("_");
						ex.setDayOfWeek(Integer.parseInt(arr[0]));
						ex.setPeriod(Integer.parseInt(arr[2]));
						ex.setPeriodInterval(arr[1]);
						ex.setId(UuidUtils.generateUuid());
						ex.setTeachClassId(teachClass.getId());
						ex.setPlaceId(classPlaceMap.get(cc.getId()));
						teachExList.add(ex);
					}
					timeList.addAll(times);
					classIdList.addAll(xzbIds);
					//行政班虚拟课表(后面维护)
					//教学班课表
					ClassTeaching tt=new ClassTeaching();
					tt.setClassId(teachClass.getId());
					tt.setSubjectId(teachClass.getCourseId());
					
					tt.setTeacherId(teachClass.getTeacherId());
					
					initCourseScheduleList(tt, startDateInfo, endDateInfo, allListo, insertCourseScheduleList);
					
				}else {
					NewGkDivideClass releclazz = allClassMap.get(cc.getRelateId());
					if(releclazz!=null) {
						String placeId = classPlaceMap.get(releclazz.getId());
						teachClass.setPlaceId(placeId);
						List<NewGkTimetable> list1 = timeTableByClassIdMap.get(releclazz.getId());
						if(CollectionUtils.isEmpty(list1)) {
							if(!gtBySubject.containsKey(cc.getSubjectIds())) {
								gt=oldGraTeaMap.get(cc.getSubjectIds());
								if(gt==null) {
									gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
									addGtList.add(gt);
								}else {
									gt.setModifyTime(now);
									upGtList.add(gt);
								}
								gt.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
								gt.setSubjectId(cc.getSubjectIds());
								gtBySubject.put(cc.getSubjectIds(), gt);
							}
							
							continue;
						}
						
						List<NewGkTimetable> newList1 = list1.stream().filter(e->e.getSubjectId().equals(cc.getSubjectIds())).collect(Collectors.toList());
						String tid ="" ;
						if(CollectionUtils.isNotEmpty(newList1.get(0).getTeacherIdList())) {
							tid=newList1.get(0).getTeacherIdList().get(0);
						}
						if(StringUtils.isNotBlank(tid)){
							teachClass.setTeacherId(tid);
						}else {
							teachClass.setTeacherId(BaseConstants.ZERO_GUID);
						}
						//时间点
						Set<String> times=new HashSet<>();
						List<NewGkTimetableOther> allListo=new ArrayList<>();
						for(NewGkTimetable nn:newList1) {
							List<NewGkTimetableOther> listO = timetableOtherMap.get(nn.getId());
							if(CollectionUtils.isEmpty(listO)) {
								continue;
							}
							if(!gtBySubject.containsKey(nn.getSubjectId())) {
								gt=oldGraTeaMap.get(nn.getSubjectId());
								if(gt==null) {
									gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
									addGtList.add(gt);
								}else {
									gt.setModifyTime(now);
									upGtList.add(gt);
								}
								gt.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
								gt.setSubjectId(nn.getSubjectId());
								gtBySubject.put(nn.getSubjectId(), gt);
							}
							for(NewGkTimetableOther o:listO) {
								times.add(o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod()+"_"+o.getFirstsdWeek());
							}
							allListo.addAll(listO);
						}
						
						for(String time:times) {
							TeachClassEx ex=new TeachClassEx();
							String[] arr = time.split("_");
							ex.setDayOfWeek(Integer.parseInt(arr[0]));
							ex.setPeriod(Integer.parseInt(arr[2]));
							ex.setPeriodInterval(arr[1]);
							ex.setId(UuidUtils.generateUuid());
							ex.setTeachClassId(teachClass.getId());
							ex.setPlaceId(classPlaceMap.get(releclazz.getId()));
							teachExList.add(ex);
						}
						//教学班课表
						ClassTeaching tt=new ClassTeaching();
						tt.setClassId(teachClass.getId());
						tt.setSubjectId(teachClass.getCourseId());
						tt.setTeacherId(teachClass.getTeacherId());
						initCourseScheduleList(tt, startDateInfo, endDateInfo, allListo, insertCourseScheduleList);
						
						
					}else {
						//目前不会出现
						String placeId = classPlaceMap.get(cc.getId());
						teachClass.setPlaceId(placeId);
						//纯教学班
						List<NewGkTimetable> list1 = timeTableByClassIdMap.get(cc.getId());
						if(CollectionUtils.isEmpty(list1)) {
							teachClass.setTeacherId(BaseConstants.ZERO_GUID);
							//直接开设年级科目,否则教学班不好开设
							if(!gtBySubject.containsKey(cc.getSubjectIds())) {
								gt=oldGraTeaMap.get(cc.getSubjectIds());
								if(gt==null) {
									gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
									gt.setModifyTime(now);
									addGtList.add(gt);
								}else {
									upGtList.add(gt);
								}
								gt.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
								gt.setSubjectId(cc.getSubjectIds());
								gtBySubject.put(cc.getSubjectIds(), gt);
							}
							continue;
						}
						String tid ="" ;
						if(CollectionUtils.isNotEmpty(list1.get(0).getTeacherIdList())) {
							tid=list1.get(0).getTeacherIdList().get(0);
						}
						if(StringUtils.isNotBlank(tid)){
							teachClass.setTeacherId(tid);
						}else {
							teachClass.setTeacherId(BaseConstants.ZERO_GUID);
						}
						List<NewGkTimetableOther> allListo=new ArrayList<NewGkTimetableOther>();
						//时间点
						Set<String> times=new HashSet<>();
						for(NewGkTimetable nn:list1) {
							List<NewGkTimetableOther> listO = timetableOtherMap.get(nn.getId());
							if(CollectionUtils.isEmpty(listO)) {
								continue;
							}
							if(!gtBySubject.containsKey(nn.getSubjectId())) {
								gt=oldGraTeaMap.get(nn.getSubjectId());
								if(gt==null) {
									gt=initGradeTeaching(schoolId, acadyear, semester+"", gradeId);
									gt.setModifyTime(now);
									addGtList.add(gt);
								}else {
									upGtList.add(gt);
								}
								gt.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
								gt.setSubjectId(nn.getSubjectId());
								gtBySubject.put(cc.getSubjectIds(), gt);
							}
							for(NewGkTimetableOther o:listO) {
								times.add(o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod()+"_"+o.getFirstsdWeek());
							}
							allListo.addAll(listO);
						}
						for(String time:times) {
							TeachClassEx ex=new TeachClassEx();
							String[] arr = time.split("_");
							ex.setDayOfWeek(Integer.parseInt(arr[0]));
							ex.setPeriod(Integer.parseInt(arr[2]));
							ex.setPeriodInterval(arr[1]);
							ex.setId(UuidUtils.generateUuid());
							ex.setTeachClassId(teachClass.getId());
							ex.setPlaceId(classPlaceMap.get(cc.getId()));
							teachExList.add(ex);
						}
						//教学班课表
						ClassTeaching tt=new ClassTeaching();
						tt.setClassId(teachClass.getId());
						tt.setSubjectId(teachClass.getCourseId());
						tt.setTeacherId(teachClass.getTeacherId());
						initCourseScheduleList(tt, startDateInfo, endDateInfo, allListo, insertCourseScheduleList);
					}
				}
			
			}
		}
		List<ClassHour> saveClassHours = new ArrayList<>();
		List<ClassHourEx> saveClassHourExs = new ArrayList<>();
		
		ClassHour ch;
		ClassHourEx chEx;
		//虚拟课程时间 虚拟课程时间进课表
		for (String key : xnBathTimeMap.keySet()) {
			String subIds=key;
			Set<String> times=xnBathTimeMap.get(key);
			Set<String> xzbIds=xnClassMap.get(key);
			if(CollectionUtils.isEmpty(xzbIds)) {
				continue;
			}
			for(String xzbid:xzbIds) {
				ct=xzbClassTeachingMap.get(xzbid).get(subIds);
				initCourseScheduleList2(ct, startDateInfo, endDateInfo, times, insertCourseScheduleList);
			}
			ch = new ClassHour();
			ch.setId(xnCourseHourIdMap.get(key));
			ch.setAcadyear(startDateInfo.getAcadyear());
			ch.setSemester(String.valueOf(startDateInfo.getSemester()));
			ch.setGradeId(array.getGradeId());
			ch.setCreationTime(now);
			ch.setModifyTime(now);
			ch.setIsDeleted(0);
			ch.setSubjectId(subIds);
			ch.setClassIds(StringUtils.join(xzbIds, ","));
			ch.setUnitId(array.getUnitId());
			saveClassHours.add(ch);
			
			
			
			
			for (String time : times) {
				String[] timearr = time.split("_");
				chEx=new ClassHourEx();
				chEx.setClassHourId(ch.getId());
				chEx.setId(UuidUtils.generateUuid());
				chEx.setDayOfWeek(Integer.parseInt(timearr[0]));
				chEx.setPeriodInterval(timearr[1]);
				chEx.setPeriod(Integer.parseInt(timearr[2]));
				chEx.setCreationTime(now);
				chEx.setModifyTime(now);
				chEx.setIsDeleted(0);
				saveClassHourExs.add(chEx);
			}
		}
		
		
		//保存教学班
		if(MapUtils.isNotEmpty(oldTeachClaMap)) {
			Set<String> oldjxClaIds = oldTeachClaMap.keySet();
			//改为完成教学
			teachClassRemoteService.notUsing(oldjxClaIds.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(updateTeachClassList)) {
			Set<String> updateIds = EntityUtils.getSet(updateTeachClassList, e->e.getId());
			teachClassExRemoteService.deleteByTeachClass(updateIds.toArray(new String[0]));
			
			teachClassRemoteService.deleteStusByClaIds(updateIds.toArray(new String[0]));
		}
		addTeachClassList.addAll(updateTeachClassList);
		if (CollectionUtils.isNotEmpty(addTeachClassList)) {
			teachClassRemoteService.saveAll(SUtils.s(addTeachClassList));
		}
		if (CollectionUtils.isNotEmpty(teachClassStuList)) {
			teachClassStuRemoteService.saveAll(SUtils.s(teachClassStuList));
		}
		if (CollectionUtils.isNotEmpty(teachExList)) {
			teachClassExRemoteService.saveAll(SUtils.s(teachExList));
		}
		
		
		//保存教学计划
		if(MapUtils.isNotEmpty(oldGraTeaMap)) {
			for(String key: oldGraTeaMap.keySet()){
				if(gtBySubject.containsKey(key)) {
					continue;
				}
				GradeTeaching gtent= oldGraTeaMap.get(key);
				gtent.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
				gtent.setModifyTime(new Date());
				upGtList.add(gtent);
			}
		}
		Set<String> updateIds = EntityUtils.getSet(upctList, e->e.getId());
		if(MapUtils.isNotEmpty(oldCtMap)){
			for(String key: oldCtMap.keySet()){
				List<ClassTeaching> ctentlist = oldCtMap.get(key);
				for(ClassTeaching ctt:ctentlist){
					if(updateIds.contains(ctt.getId())) {
						continue;
					}
					if(Objects.equals(NewGkElectiveConstant.IF_INT_1, ctt.getIsTeaCls())) {
						ctt.setCourseHour(0f);
					}else {
						ctt.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
					}
					ctt.setModifyTime(new Date());
					upctList.add(ctt);
				}
			}
		}
	
		addGtList.addAll(upGtList);
		if (CollectionUtils.isNotEmpty(addGtList)) {
			gradeTeachingRemoteService.saveAll(SUtils.s(addGtList));
		}
		addctList.addAll(upctList);
		if(CollectionUtils.isNotEmpty(addctList)){
			addctList.stream().filter(e->e!=null&&e.getCourseHour()==null).forEach(e->e.setCourseHour(0f));
			classTeachingRemoteService.saveAll(addctList.toArray(new ClassTeaching[0]));
		}
		
		//保存课表
		//删除指定时间内旧课表
		CourseScheduleDto dto = new CourseScheduleDto();
		dto.setSchoolId(array.getUnitId());
		dto.setAcadyear(startDateInfo.getAcadyear());
		dto.setSemester(startDateInfo.getSemester());
		dto.setWeekOfWorktime1(startDateInfo.getWeek());
		dto.setDayOfWeek1((startDateInfo.getWeekday()==null?1:startDateInfo.getWeekday())-1);// 星期重0开始
		Set<String> delclaids=new HashSet<>();//删除的班级id
		if(CollectionUtils.isNotEmpty(insertCourseScheduleList)) {
			delclaids.addAll(EntityUtils.getSet(insertCourseScheduleList,CourseSchedule::getClassId));
		}
		if(CollectionUtils.isNotEmpty(oldClazzList)) {
			delclaids.addAll(EntityUtils.getSet(oldClazzList,Clazz::getId));
		}
		if(CollectionUtils.isNotEmpty(oldTeachClassList)) {
			delclaids.addAll(EntityUtils.getSet(oldTeachClassList,TeachClass::getId));
		}
		if(oldClaMap!=null && oldClaMap.size()>0) {
			delclaids.addAll(oldClaMap.keySet());
		}
		if(CollectionUtils.isNotEmpty(delclaids)) {
			dto.setClassIds(delclaids.toArray(new String[] {}));
			courseScheduleRemoteService.deleteByClassId(dto);
		}
		
		if (CollectionUtils.isNotEmpty(insertCourseScheduleList)) {
			courseScheduleRemoteService.saveAll(SUtils.s(insertCourseScheduleList));
		}		
		
		// 删除原来的虚拟课程 时间点 ，保存新的
		List<ClassHour> oldClassHours = SUtils.dt(classHourRemoteService.findListByUnitId(startDateInfo.getAcadyear(), String.valueOf(startDateInfo.getSemester()),
				array.getUnitId(), array.getGradeId()), ClassHour.class);
		//同时删除时间
		if(CollectionUtils.isNotEmpty(oldClassHours)) {
			oldClassHours.forEach(e->{
				e.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
				e.setModifyTime(new Date());
			});
			Set<String> hourIds = EntityUtils.getSet(oldClassHours, e->e.getId());
			List<ClassHourEx> oldClassHourExs=SUtils.dt(classHourExRemoteService.findListByHourIdIn(hourIds.toArray(new String[0])),ClassHourEx.class);
			if(CollectionUtils.isNotEmpty(oldClassHourExs)) {
				oldClassHourExs.forEach(e->{
					e.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
					e.setModifyTime(new Date());
				});
				saveClassHourExs.addAll(oldClassHourExs);
			}
			saveClassHours.addAll(oldClassHours);
		}
		
		classHourRemoteService.saveAll(saveClassHours.toArray(new ClassHour[0]));
		classHourExRemoteService.saveAll(saveClassHourExs.toArray(new ClassHourEx[0]));
		
		
		
		// 将方案 设为默认 方案，取消其他方案的 默认标识
		String[] names = {"gradeId", "isDefault","arrangeType"};
		Object[] params = {array.getGradeId(), 1,array.getArrangeType()};
		List<NewGkArray> list = newGkArrayService.findListBy(names, params);
		if (CollectionUtils.isNotEmpty(list)){
			for (NewGkArray one : list){
				if(one.getId().equals(array.getId()))
					continue;
				
				one.setIsDefault(0);
				one.setModifyTime(new Date());
			}
		}
		array.setIsDefault(1);
		array.setModifyTime(new Date());
		list.add(array);
		newGkArrayService.saveAll(list.toArray(new NewGkArray[0]));
	}
	
	
	public void saveToScheduleXzb(NewGkArray array, DateInfo startDateInfo, DateInfo endDateInfo) {
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(array.getGradeId()), new TR<Grade>() {});
        /**
         * 获取选课结果写入BASE_STUDENT_SELSUB
         * 2018-09-30
         */
		String acadyear = startDateInfo.getAcadyear();
		Integer semester = startDateInfo.getSemester();
		boolean isXzbArray = NewGkElectiveConstant.ARRANGE_XZB.equals(array.getArrangeType());
		if(!isXzbArray) {
			NewGkDivide divide = newGkDivideService.findById(array.getDivideId());
	        NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
	        List<NewGkChoResult> choiceList = newGkChoResultService.findByChoiceIdAndKindType(grade.getSchoolId(), NewGkElectiveConstant.KIND_TYPE_01, choice.getId());
	        List<StudentSelectSubject> resultList = new ArrayList<>();
	        for (NewGkChoResult one : choiceList) {
	            StudentSelectSubject studentSelectSubject = new StudentSelectSubject();
	            studentSelectSubject.setId(UuidUtils.generateUuid());
	            studentSelectSubject.setGradeId(grade.getId());
	            studentSelectSubject.setSchoolId(grade.getSchoolId());
	            studentSelectSubject.setStudentId(one.getStudentId());
	            studentSelectSubject.setSubjectId(one.getSubjectId());
	            studentSelectSubject.setAcadyear(acadyear);
	            studentSelectSubject.setSemester(semester);
	            studentSelectSubject.setCreationTime(new Date());
	            studentSelectSubject.setModifyTime(new Date());
	            studentSelectSubject.setIsDeleted(0);
	            resultList.add(studentSelectSubject);
	        }
	        studentSelectSubjectRemoteService.updateStudentSelectsByGradeId(grade.getSchoolId(), acadyear, semester, grade.getId(), resultList);
	        
		}
        
		//新行政班
		List<NewGkDivideClass> allDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(grade.getSchoolId(), array.getId(), 
				new String[] { NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2 },true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		List<NewGkDivideClass> xzbClaList = allDivideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.collect(Collectors.toList());
		List<NewGkDivideClass> jxdivideClassList = allDivideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.collect(Collectors.toList());
		//原行政班
		List<Clazz> oldClazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(array.getUnitId(), array.getGradeId()), new TR<List<Clazz>>() {});
		Set<String> oldClassIds = EntityUtils.getSet(xzbClaList, e->e.getOldClassId());
		List<Clazz> oldClazsByOldIds = classRemoteService.findListObjectByIds(oldClassIds.toArray(new String[0]));
		Map<String, Clazz> oldClaMap=EntityUtils.getMap(oldClazzList, Clazz::getId);
		oldClazsByOldIds.forEach(e->oldClaMap.put(e.getId(), e));
		/**
		 * 1.比较是否具有足够的 虚拟课程
		 * 行政班排课 只要班级存在 肯定有虚拟课程，如果 没有 对应的班级 则创建班级 然后创建虚拟课程
		 * 7选3 根据批次点 确定虚拟课程数量 
		 */
		List<Course> virtualCourses2 = SUtils.dt(courseRemoteService.getVirtualCourses(grade.getSchoolId(), null), Course.class);
		List<Course> virtualCourses = virtualCourses2.stream()
				.filter(e->e.getSection()!= null && e.getSection().contains(String.valueOf(grade.getSection())))
				.collect(Collectors.toList());
		Set<String> subNames = EntityUtils.getSet(virtualCourses, Course::getSubjectName);
		Set<String> subCodes = EntityUtils.getSet(virtualCourses, Course::getSubjectCode);
		Map<String, List<NewGkDivideClass>> batchClassMap = EntityUtils.getListMap(jxdivideClassList, e->e.getSubjectType()+"-"+e.getBatch(), e->e);
		Date now = new Date();
		
		List<String> needVirCourseIds;
		if(isXzbArray){
			needVirCourseIds = jxdivideClassList.stream().map(e -> e.getBatch().split("-")[0]).distinct().collect(Collectors.toList());
		}else{
			needVirCourseIds = new ArrayList<>(batchClassMap.keySet());
		}

//		Set<String> newVirCourseIds = new HashSet<>();
		if(needVirCourseIds.size() > virtualCourses.size()) {
			List<SectionDto> sectionDtos = getAllSectionByUnit(grade.getSchoolId());
			String[] sectionArray = EntityUtils.getSet(sectionDtos, "sectionValue").toArray(new String[0]);
			Arrays.sort(sectionArray);
			String section = String.join(",", sectionArray);
			Integer maxOrderId = virtualCourses2.stream().filter(e->e.getOrderId()!=null).map(e->e.getOrderId()).max(Integer::compareTo).orElse(0);
			String subName = "";
			String subCode = "";
			// 创建 N 个虚拟课程
			List<Course> addCourses = new ArrayList<>();
			int n=1;
			for(int i=1;i<=(needVirCourseIds.size() - virtualCourses.size());i++) {
				Course course = new Course();
				course.setId(UuidUtils.generateUuid());
				course.setCreationTime(now);
				course.setModifyTime(now);
				do {
					subName = "走班课程"+n;
					subCode = "XNKC"+n;
					n++;
				}while(subNames.contains(subName) || subCodes.contains(subCode));
				course.setSubjectName(subName);
				course.setSubjectCode(subCode);
				course.setSection(section);
				course.setType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
				course.setSubjectType(BaseConstants.UNIT_CLASS_SCH);
				course.setCourseTypeId(BaseConstants.VIRTUAL_COURSE_TYPE);
				course.setUnitId(grade.getSchoolId());
				course.setIsDeleted(0);
				course.setIsUsing(1);
				course.setFullMark(5);
				course.setInitCredit(3);
				course.setInitPassMark(3);
				course.setEventSource(0);
				course.setOrderId(++maxOrderId);
				
				addCourses.add(course);
//				newVirCourseIds.add(course.getId());
			}
			courseRemoteService.saveAll(addCourses.toArray(new Course[0]));
			virtualCourses.addAll(addCourses);
		}
		// 将批次 和 虚拟课程 一一对应
		Map<String,Course> virtualCouIdMap = EntityUtils.getMap(virtualCourses, Course::getId);
		Map<String,String> transVirMap = new HashMap<>();
		if(isXzbArray) {
			Iterator<String> iter = virtualCouIdMap.keySet().stream().filter(e->!needVirCourseIds.contains(e)).iterator();
			Map<String,String> transMap = new HashMap<>();
			for (String batchstr : batchClassMap.keySet()) {
				String relaCouId = batchstr.split("-")[1];
				if(virtualCouIdMap.containsKey(relaCouId)) {
					continue;
				}
				String next = transMap.computeIfAbsent(relaCouId, e -> iter.next());
				transVirMap.put(relaCouId,next);
				batchClassMap.get(batchstr).forEach(e->{
					String[] split = e.getBatch().split("-");
					String cids = split[1];
					e.setBatch(next+"-"+cids);
				});
			}
		}else {
			// 7选 3 排课
			int n=0;
			for (String batchstr : batchClassMap.keySet()) {
				String id = virtualCourses.get(n++).getId();
				batchClassMap.get(batchstr).forEach(e->e.setBatch(id));
			}
		}
		batchClassMap = EntityUtils.getListMap(jxdivideClassList, e->e.getSubjectType()+"-"+e.getBatch(), e->e);
		
		List<Clazz> upClaList = new ArrayList<Clazz>();
		List<Clazz> addClaList = new ArrayList<Clazz>();
		//不重组  老的行政班id 拷贝到新的行政班对应关系
		Map<String,String> OldorXId =new HashMap<String, String>();
		List<Student> studentList = Student.dt(studentRemoteService.findPartStudByGradeId(array.getUnitId(), array.getGradeId(), null, null));
		Map<String, Student> studentMap = new HashMap<String, Student>();
		if (CollectionUtils.isNotEmpty(studentList)) {
			studentMap = EntityUtils.getMap(studentList, Student::getId);
		}
		//未安排的学生(理论上没有)
		Set<String> notStudentIds = studentMap.keySet();
		//更新信息的学生
		List<Student> updateStudentList = new ArrayList<Student>();
		
		Student student = null;
		//班级名称序号
		int i = 0;
		//班级code前缀  入学年份+2位学段+2位序号
		int enrollYear = NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));
		String sectionStr = StringUtils.leftPad(grade.getSection() + "", 2, "0");
		Map<String,String> stuIdNewXzbCidMap = new HashMap<>();
		Map<String,String> dCidMap = new HashMap<>();
		Map<String,String> oldIdNewMap = new HashMap<>();
		Set<String> classCodes = new HashSet<>();
		for (NewGkDivideClass divideClass : xzbClaList) {   // 行政班班级 和 学生 ；增加 或者更新
//			if (CollectionUtils.isEmpty(divideClass.getStudentList())) {
//				continue;
//			}
			Clazz clazz = null;
			String oldclaid =divideClass.getOldClassId();
			if(StringUtils.isNotBlank(oldclaid)){//历史班级
				clazz = oldClaMap.get(oldclaid);
				if(clazz!=null)
					OldorXId.put(divideClass.getId(), clazz.getId());
			}
			if(clazz == null && oldClaMap.get(divideClass.getId())!=null){//重复执行班级
				clazz = oldClaMap.get(divideClass.getId());
			}
			if(clazz==null){
				i++;
				String newId = oldclaid;
				if(newId == null)
					newId = divideClass.getId();
				clazz=new Clazz();
				clazz.setId(newId);
				clazz.setClassName(divideClass.getClassName());
				String classCode = enrollYear + StringUtils.leftPad(i + "", 2, "0");
				clazz.setClassCode(classCode);
				clazz.setSchoolId(grade.getSchoolId());
				clazz.setAcadyear(grade.getOpenAcadyear());
				clazz.setIsGraduate(NewGkElectiveConstant.IF_INT_0);
				clazz.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				clazz.setSchoolingLength(grade.getSchoolingLength());
				clazz.setGradeId(grade.getId());
				clazz.setSection(grade.getSection());
				clazz.setArtScienceType(0);
				clazz.setBuildDate(now);
				clazz.setCreationTime(now);
				clazz.setModifyTime(now);
				clazz.setRemark("智能分班排课重组班级");
				addClaList.add(clazz);
			}else{
				if(clazz.getClassCode().length()==8){
					clazz.setClassCode(clazz.getClassCode().substring(0,4)+clazz.getClassCode().substring(6,8));
				}
				clazz.setIsGraduate(NewGkElectiveConstant.IF_INT_0);
				clazz.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				clazz.setModifyTime(now);
				upClaList.add(clazz);
				oldClaMap.remove(clazz.getId());
			}
			classCodes.add(clazz.getClassCode());
			dCidMap.put(divideClass.getOldDivideClassId(),clazz.getId());
			oldIdNewMap.put(divideClass.getOldClassId(),clazz.getId());
			if(CollectionUtils.isNotEmpty(divideClass.getStudentList())) {
				//修改学生班级信息
				for (String studentId : divideClass.getStudentList()) {
					student = studentMap.get(studentId);
					if(student!=null){
						student.setClassId(clazz.getId());
						student.setModifyTime(new Date());
						updateStudentList.add(student);
						notStudentIds.remove(studentId);
						stuIdNewXzbCidMap.put(studentId, clazz.getId());
					}
				}
			}
		}
		i = dealClassCode(upClaList, i, enrollYear, classCodes);
		// 将新添加的学生 放置到原来的班级
		Iterator<String> iter = notStudentIds.iterator();
		while (iter.hasNext()) {
			String stuId = iter.next();
			Student stu = studentMap.get(stuId);
			if(stu != null && oldIdNewMap.containsKey(stu.getClassId())){
				String newCid = oldIdNewMap.get(stu.getClassId());
				stu.setClassId(newCid);
				stu.setModifyTime(new Date());
				iter.remove();
				stuIdNewXzbCidMap.put(stuId, newCid);
			}
		}

		//未选科的学生班 重组一个班
		if (notStudentIds.size() > 0) {
			Clazz clazz = new Clazz();
//			//判断是否重复班级名称
//			Set<String> classNames = EntityUtils.getSet(upClaList, Clazz::getClassName);
//			if(CollectionUtils.isNotEmpty(addClaList)){
//				classNames.addAll(EntityUtils.getSet(addClaList, Clazz::getClassName));
//				while (classNames.contains(StringUtils.leftPad(++i + "", 2, "0")+"班"));
//			}			
			clazz.setId(UuidUtils.generateUuid());
			clazz.setRemark("智能分班排课重组班级");
			//clazz.setClassName(StringUtils.leftPad(i + "", 2, "0")+"班");
			clazz.setClassName("未选科学生班");
			String classCode = enrollYear + StringUtils.leftPad("0", 2, "0");
			clazz.setClassCode(classCode);
			clazz.setSchoolId(grade.getSchoolId());
			clazz.setAcadyear(grade.getOpenAcadyear());
			clazz.setIsGraduate(NewGkElectiveConstant.IF_INT_0);
			clazz.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
			clazz.setSchoolingLength(grade.getSchoolingLength());
			clazz.setGradeId(grade.getId());
			clazz.setSection(grade.getSection());
			clazz.setArtScienceType(0);
			clazz.setBuildDate(new Date());
			clazz.setCreationTime(new Date());
			clazz.setModifyTime(new Date());
			addClaList.add(clazz);
			
			//修改学生班级信息
			for (String studentId : notStudentIds) {
				student = studentMap.get(studentId);
				student.setClassId(clazz.getId());
				student.setModifyTime(new Date());
				updateStudentList.add(student);
			}
		}
		if(MapUtils.isNotEmpty(oldClaMap)) {
			for (String key : oldClaMap.keySet()) {
				Clazz oldClazz = oldClaMap.get(key);
				//方便业务数据查找
				oldClazz.setIsGraduate(Clazz.GRADUATED_JS);
				oldClazz.setRemark("智能分班排课毕业原来行政班级");
				upClaList.add(oldClazz);
			}
		}
		
		// 保存 班级 更新或添加；更新学生班级信息
		addClaList.addAll(upClaList);
		if (CollectionUtils.isNotEmpty(addClaList)) {
			classRemoteService.saveAllEntitys(SUtils.s(addClaList));
		}
		if (CollectionUtils.isNotEmpty(updateStudentList)) {
			studentRemoteService.updateClaIds(updateStudentList);
		}
		//封装班级完毕
		
		// 创建 classHour
		List<ClassHour> oldClassHours = SUtils.dt(classHourRemoteService.findListByUnitId(acadyear, String.valueOf(semester),
				array.getUnitId(), grade.getId()), ClassHour.class);
		Map<String, ClassHour> clsHourMap = EntityUtils.getMap(oldClassHours, e -> e.getSubjectId() + "-" + (e.getClassIds()==null?"":e.getClassIds()));
		oldClassHours.forEach(e->e.setIsDeleted(NewGkElectiveConstant.IF_INT_1));
		Map<String,String> transClsIdsMap = new HashMap<>();
		List<ClassHour> savedClsHourList = new ArrayList<>();
		if(isXzbArray){
			for (String batch : batchClassMap.keySet()) {
				String[] split = batch.split("-");
				String clsIds = split[2];
				List<String> classIdList = Stream.of(clsIds.split(",")).filter(e -> dCidMap.containsKey(e)).map(e -> dCidMap.get(e)).collect(Collectors.toList());
				String newCids = classIdList.stream().collect(Collectors.joining(","));
				if(BaseConstants.ZERO_GUID.equals(clsIds)){
					newCids = "";
				}
				transClsIdsMap.put(clsIds,newCids);
				String finalNewCids = newCids;
				batchClassMap.get(batch).forEach(e->e.setBatch(split[1]+"-"+ finalNewCids));
				ClassHour classHour = clsHourMap.get(split[1] + "-" + newCids);
				if(classHour == null){
					classHour = new ClassHour();
					classHour.setId(UuidUtils.generateUuid());
					classHour.setAcadyear(acadyear);
					classHour.setSemester(String.valueOf(semester));
					classHour.setUnitId(array.getUnitId());
					classHour.setGradeId(grade.getId());
					classHour.setSubjectId(split[1]);
					classHour.setClassIds(newCids);
					classHour.setCreationTime(now);
					classHour.setModifyTime(now);
					classHour.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
					clsHourMap.put(split[1] + "-" + newCids,classHour);
					savedClsHourList.add(classHour);
				}else{
					classHour.setModifyTime(now);
					classHour.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				}
				classHour.setClassIdList(classIdList);
			}
			savedClsHourList.addAll(oldClassHours);
		}

		// 批次课表
		List<NewGkLessonTime> batchTimes = newGkLessonTimeService.findByObjectTypeAndItem(NewGkElectiveConstant.LIMIT_SUBJECT_7,
				new String[]{array.getLessonArrangeId()}, true);
		Map<String,Set<String>> batchPeriodMap = new HashMap<>();
		Map<String,Set<String>> xzbBatchTimeMap = new HashMap<>();
		List<ClassHourEx> classHourExList = new ArrayList<>();
		ClassHourEx classHourEx;
		for (NewGkLessonTime lt : batchTimes) {
			String[] split = lt.getLevelType().split("-");
			String virId = split[0];
			if(transVirMap.containsKey(virId)){
				virId = transVirMap.get(split[0]);
			}
			String newCids = transClsIdsMap.get(split[1]);
			if(newCids == null){
				throw new RuntimeException("找不到虚拟课程的班级组");
			}
			String levelType = virId+"-"+newCids;
			ClassHour classHour = clsHourMap.get(levelType);
			Set<String> times = lt.getTimesList().stream().map(e -> e.getDayOfWeek() + "-" + e.getPeriodInterval() + "-" + e.getPeriod()).collect(Collectors.toSet());
			final String virId2 = virId;
			classHour.getClassIdList().forEach(cid->{
				xzbBatchTimeMap.computeIfAbsent(cid+"-"+virId2,e->new HashSet<>()).addAll(times);
			});
//			batchPeriodMap.put(code,times);

			for (NewGkLessonTimeEx ex : lt.getTimesList()) {
				classHourEx = new ClassHourEx();
				classHourEx.setId(UuidUtils.generateUuid());
				classHourEx.setClassHourId(classHour.getId());
				classHourEx.setCreationTime(now);
				classHourEx.setModifyTime(now);
				classHourEx.setDayOfWeek(ex.getDayOfWeek());
				classHourEx.setPeriodInterval(ex.getPeriodInterval());
				classHourEx.setPeriod(ex.getPeriod());
				classHourEx.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				classHourExList.add(classHourEx);
			}
		}
		// 保存ClassHour
		List<ClassHourEx> chexList = SUtils.dt(classHourExRemoteService.findListByHourIdIn(EntityUtils.getSet(savedClsHourList,
				e -> e.getId()).toArray(new String[0])), ClassHourEx.class);
		chexList.forEach(e->e.setIsDeleted(NewGkElectiveConstant.IF_INT_1));
		classHourExList.addAll(chexList);
		if(CollectionUtils.isNotEmpty(classHourExList)){
			classHourExRemoteService.saveAll(classHourExList.toArray(new ClassHourEx[0]));
		}
		if(CollectionUtils.isNotEmpty(savedClsHourList)){
			classHourRemoteService.saveAll(savedClsHourList.toArray(new ClassHour[0]));
		}


		// 行政班学期开课  处理年级班级教学计划   是否考勤默认都是; 教学计划
		List<ClassTeaching> ctList = SUtils.dt(classTeachingRemoteService.findListByGradeId(acadyear,
				String.valueOf(semester), startDateInfo.getSchoolId(), grade.getId()),ClassTeaching.class);
		Map<String, List<ClassTeaching>> oldCtMap = ctList.stream().collect(Collectors.groupingBy(t -> t.getSubjectId()+t.getClassId()));
		//ctList.stream().collect(Collectors.toMap(a-> a.getClassId()+a.getSubjectId(), a -> a));
		// 获取虚拟课程
		Set<String> virtuCids = new HashSet<>();
		if(isXzbArray) {
//			List<Course> virtualCourses = SUtils.dt(courseRemoteService.getVirtualCourses(grade.getSchoolId(), String.valueOf(grade.getSection())), Course.class);
			virtuCids = EntityUtils.getSet(virtualCourses, Course::getId);
		}
		
		List<ClassTeaching> upctList=new ArrayList<>();
		List<ClassTeaching> addctList=new ArrayList<>();
		Map<String, GradeTeaching> oldGraTeaMap = SUtils.dt(gradeTeachingRemoteService.findBySearchMap(startDateInfo.getSchoolId(), 
				acadyear, String.valueOf(semester),array.getGradeId()), new TR<Map<String, GradeTeaching>>(){});
		List<GradeTeaching> upGtList=new ArrayList<>();
		List<GradeTeaching> addGtList=new ArrayList<>();
		//课程表数据
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(array.getUnitId(), array.getId());
		List<NewGkTimetableOther> timetableOtherList = newGkTimetableOtherService.findListByIn("timetableId", EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]));
		List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findListByIn("timetableId", EntityUtils.getList(timetableList, NewGkTimetable::getId).toArray(new String[0]));
		
		//选课课表-老师映射
		Map<String, String> timetableTeacherMap = EntityUtils.getMap(timetableTeacherList, NewGkTimetableTeacher::getTimetableId, NewGkTimetableTeacher::getTeacherId);
		Map<String, List<NewGkTimetableOther>> timetableOtherMap = timetableOtherList.stream().collect(Collectors.groupingBy(NewGkTimetableOther::getTimetableId));
		// 获取 班级单双周 上课信息 key: classId-subjectId
		Map<String,Integer> weekTypeMap = new HashMap<>();
		for (NewGkTimetable tt : timetableList) {
			List<NewGkTimetableOther> list = timetableOtherMap.get(tt.getId());
			// 目前只有行政班有 单双周
			if(CollectionUtils.isEmpty(list) || !NewGkElectiveConstant.CLASS_TYPE_1.equals(tt.getClassType())) 
				continue;
			Optional<Integer> optional = list.stream().filter(e->Objects.equals(NewGkElectiveConstant.WEEK_TYPE_EVEN, e.getFirstsdWeek())
					|| Objects.equals(NewGkElectiveConstant.WEEK_TYPE_ODD, e.getFirstsdWeek())).map(e->e.getFirstsdWeek()).findFirst();
			if(optional.isPresent()) {
				String newClassId = tt.getClassId();
				if(OldorXId.containsKey(tt.getClassId())) {
					newClassId = OldorXId.get(tt.getClassId());
				}
				weekTypeMap.put(newClassId+"-"+tt.getSubjectId(), optional.get());
			}
		}
		
		// k:classId  V:placeId
		Map<String,String> classPlaceMap = new HashMap<>();
		for (NewGkTimetable tt : timetableList) {
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(tt.getClassType())) {
				tt.getClassId();
				List<NewGkTimetableOther> list = timetableOtherMap.get(tt.getId());
				if(list != null) {
					classPlaceMap.put(tt.getClassId(), list.get(0).getPlaceId());
				}
			}
		}
		
		Map<String,Map<String,String[]>> opeClaSubMap=new HashMap<>();//班级开设课程<subid,<classId+subtype,{teachid,count}>>
		Set<String> opentSubIds=new HashSet<>();//行政班subids
		Map<String,Set<String>> subTypeMap=new HashMap<>();//教学班id K:科目id  V:科目类型
		for(NewGkTimetable ent :timetableList){
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(ent.getClassType())){
				opentSubIds.add(ent.getSubjectId());
				Map<String,String[]> openMap = opeClaSubMap.get(ent.getSubjectId());
				if(openMap==null){
					openMap =new HashMap<>();
					int size=0;
					if(timetableOtherMap.containsKey(ent.getId())) {
						size=timetableOtherMap.get(ent.getId()).size();
					}
					openMap.put(ent.getClassId()+"-"+ent.getSubjectType(), new String[]{timetableTeacherMap.get(ent.getId()),size+""});
					opeClaSubMap.put(ent.getSubjectId(),openMap);
				}else{
					String[] teachIdcount = openMap.get(ent.getClassId()+"-"+ent.getSubjectType());
					if(teachIdcount==null){
						int size=0;
						if(timetableOtherMap.containsKey(ent.getId())) {
							size=timetableOtherMap.get(ent.getId()).size();
						}
						openMap.put(ent.getClassId()+"-"+ent.getSubjectType(), new String[]{timetableTeacherMap.get(ent.getId()),size+""});
					}
				}
			}else{
				Set<String> xksub= subTypeMap.get(ent.getSubjectId());
				if(xksub==null){
					xksub =new HashSet<>();
					subTypeMap.put(ent.getSubjectId(), xksub);
				}
				xksub.add(ent.getSubjectType());
			}
		}
		//教学班科目
		for(String subid: subTypeMap.keySet()){
			GradeTeaching gradeTeaching= oldGraTeaMap.get(subid);
			if(gradeTeaching==null){//存在年级计划不做调整 不存在则添加
				gradeTeaching  =new GradeTeaching();
				gradeTeaching.setId(UuidUtils.generateUuid());
				gradeTeaching.setAcadyear(acadyear);
				gradeTeaching.setSemester(semester +"");
				gradeTeaching.setUnitId(startDateInfo.getSchoolId());
				gradeTeaching.setGradeId(array.getGradeId());
				gradeTeaching.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				gradeTeaching.setSubjectId(subid);
				gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				gradeTeaching.setCreationTime(new Date());
				gradeTeaching.setModifyTime(new Date());
				addGtList.add(gradeTeaching);
			}else {
				gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				gradeTeaching.setModifyTime(new Date());
				gradeTeaching.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				upGtList.add(gradeTeaching);
				//为了后续处理不开的科目
				oldGraTeaMap.remove(subid);
			}
			
//			if(isXzbArray)
//				gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
			
			//<classId+subtype,{teachid,count}>
			Map<String, String[]> map= opeClaSubMap.get(subid);
			if(map != null) {  // 寻找与教学班 相同科目的行政班
				for(String key :map.keySet()){
					//选学考科目是行政班上课
					String[] classIdsubtype =key.split("-");
					//新高考这边班级id转换行政班id
					String newClassId=classIdsubtype[0];
					if(OldorXId.containsKey(classIdsubtype[0])) {
						newClassId=OldorXId.get(classIdsubtype[0]);
					}
					//这个行政班id对应的老教学计划
					List<ClassTeaching> ctentlist = oldCtMap.get(subid+newClassId);
//					List<ClassTeaching> ctentlist = oldCtMap.get(subid+classIdsubtype[0]);
					if(CollectionUtils.isNotEmpty(ctentlist)){
						int coun=0;
						for(ClassTeaching ctent:ctentlist){
							if(coun==0){
								ctent.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
								ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
								ctent.setModifyTime(new Date());
								String[] teachidAndcount =map.get(classIdsubtype[0]+"-"+classIdsubtype[1]);
								if(teachidAndcount!=null){
									ctent.setTeacherId(teachidAndcount[0]);
									ctent.setCourseHour(Float.valueOf(teachidAndcount[1]));
								}
								ctent.setPunchCard(NewGkElectiveConstant.IF_INT_1);
								Integer weekType = weekTypeMap.get(ctent.getClassId()+"-"+ctent.getSubjectId());
								if(weekType!=null)
									ctent.setWeekType(weekType);
								upctList.add(ctent);
								// TODO 加？
								coun++;
							}else{
								ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
								ctent.setModifyTime(new Date());
								upctList.add(ctent);
							}
						}
						oldCtMap.remove(subid+newClassId);
					}else{
						ClassTeaching ctent = new ClassTeaching();
						ctent.setUnitId(startDateInfo.getSchoolId());
						ctent.setAcadyear(acadyear);
						ctent.setSemester(semester + "");
						ctent.setClassId(newClassId);
//						ctent.setClassId(classIdsubtype[0]);
						
						ctent.setSubjectId(subid);
						ctent.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
						ctent.setEventSource(NewGkElectiveConstant.IF_INT_0);
						ctent.setId(UuidUtils.generateUuid());
						ctent.setCreationTime(new Date());
						Integer weekType = weekTypeMap.get(ctent.getClassId()+"-"+ctent.getSubjectId());
						if(weekType!=null)
							ctent.setWeekType(weekType);
						String[] teachidAndcount =map.get(classIdsubtype[0]+"-"+classIdsubtype[1]);
						if(teachidAndcount!=null){
							ctent.setTeacherId(teachidAndcount[0]);
							ctent.setCourseHour(Float.valueOf(teachidAndcount[1]));
						}
						ctent.setPunchCard(NewGkElectiveConstant.IF_INT_1);
						ctent.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
						ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
						ctent.setModifyTime(new Date());
						addctList.add(ctent);
					}
				}
			}
			
			opeClaSubMap.remove(subid);
			opentSubIds.remove(subid);
		}
		//行政班
		for (String subid : opentSubIds) {
			GradeTeaching gradeTeaching= oldGraTeaMap.get(subid);
			if(gradeTeaching==null){//存在年级计划不做调整 不存在添加
				gradeTeaching  =new GradeTeaching();
				gradeTeaching.setId(UuidUtils.generateUuid());
				gradeTeaching.setAcadyear(acadyear);
				gradeTeaching.setSemester(semester +"");
				gradeTeaching.setUnitId(startDateInfo.getSchoolId());
				gradeTeaching.setGradeId(array.getGradeId());
				gradeTeaching.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				gradeTeaching.setSubjectId(subid);
				gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				gradeTeaching.setCreationTime(now);
				gradeTeaching.setModifyTime(now);
				addGtList.add(gradeTeaching);
			}else {
				if(!subTypeMap.containsKey(subid)) {
					gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				}
				gradeTeaching.setModifyTime(now);
				gradeTeaching.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				upGtList.add(gradeTeaching);
				//为了后续处理不开的科目
				oldGraTeaMap.remove(subid);
			}
		}
		//TODO 虚拟课程 的 年级开设计划
		List<String> allVirtuSubjectIds = jxdivideClassList.stream().map(e->e.getBatch().split("-")[0]).distinct().collect(Collectors.toList());
		for (String subId : allVirtuSubjectIds) {
			GradeTeaching gradeTeaching= oldGraTeaMap.get(subId);
			if(gradeTeaching==null){//存在年级计划不做调整 不存在添加
				gradeTeaching  =new GradeTeaching();
				gradeTeaching.setId(UuidUtils.generateUuid());
				gradeTeaching.setAcadyear(acadyear);
				gradeTeaching.setSemester(semester +"");
				gradeTeaching.setUnitId(startDateInfo.getSchoolId());
				gradeTeaching.setGradeId(array.getGradeId());
				gradeTeaching.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				gradeTeaching.setSubjectId(subId);
				gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
				gradeTeaching.setCreationTime(now);
				gradeTeaching.setModifyTime(now);
				gradeTeaching.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
				addGtList.add(gradeTeaching);
			}else {
				gradeTeaching.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
				gradeTeaching.setModifyTime(now);
				gradeTeaching.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				gradeTeaching.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
				upGtList.add(gradeTeaching);
				//为了后续处理不开的科目
				oldGraTeaMap.remove(subId);
			}
		}
		
		if(MapUtils.isNotEmpty(oldGraTeaMap)){//删除原来的年级计划
			for(String key: oldGraTeaMap.keySet()){
//				if(virtuCids.contains(key))
//					continue;
				GradeTeaching gtent= oldGraTeaMap.get(key);
				gtent.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
				gtent.setModifyTime(new Date());
				upGtList.add(gtent);
			}
		}
		
		
		// 行政班上课的科目 
		if(MapUtils.isNotEmpty(opeClaSubMap)){
			for(String subid :opeClaSubMap.keySet()){
				//<classId+subtype,{teachid,count}>
				Map<String, String[]> map= opeClaSubMap.get(subid);
				for(String key :map.keySet()){
					String[] classIdsubtype =key.split("-");
					//新高考这边班级id转换行政班id
					String newClassId=classIdsubtype[0];
					if(OldorXId.containsKey(classIdsubtype[0])) {
						newClassId=OldorXId.get(classIdsubtype[0]);
					}
					//这个行政班id对应的老教学计划
					List<ClassTeaching> ctentlist = oldCtMap.get(subid+newClassId);
					
					if(CollectionUtils.isNotEmpty(ctentlist)){
						int coun=0;//同一个班级同一个科目可能有多条记录 数据错误 遗留一条
						for(ClassTeaching ctent:ctentlist){
							// TODO ?
							if(coun==0){
								ctent.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);//BaseConstants.SUBJECT_TYPE_BX
								ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
								ctent.setModifyTime(new Date());
								String[] teachidAndcount =map.get(classIdsubtype[0]+"-"+classIdsubtype[1]);
								if(teachidAndcount!=null){
									ctent.setTeacherId(teachidAndcount[0]);
									ctent.setCourseHour(Float.valueOf(teachidAndcount[1]));
								}
								ctent.setPunchCard(NewGkElectiveConstant.IF_INT_1);
								Integer weekType = weekTypeMap.get(ctent.getClassId()+"-"+ctent.getSubjectId());
								if(weekType!=null)
									ctent.setWeekType(weekType);
								upctList.add(ctent);
								coun++;
							}else{
								ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
								ctent.setModifyTime(new Date());
								upctList.add(ctent);
							}
						}
						
						oldCtMap.remove(subid+newClassId);
					}else{
						ClassTeaching ctent = new ClassTeaching();
						ctent.setUnitId(startDateInfo.getSchoolId());
						ctent.setAcadyear(acadyear);
						ctent.setSemester(semester + "");
						
						ctent.setClassId(newClassId);
						
						ctent.setSubjectId(subid);
						ctent.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
						ctent.setEventSource(NewGkElectiveConstant.IF_INT_0);
						ctent.setId(UuidUtils.generateUuid());
						ctent.setCreationTime(new Date());
						Integer weekType = weekTypeMap.get(ctent.getClassId()+"-"+ctent.getSubjectId());
						if(weekType!=null)
							ctent.setWeekType(weekType);
						String[] teachidAndcount =map.get(classIdsubtype[0]+"-"+classIdsubtype[1]);
						if(teachidAndcount!=null){
							ctent.setTeacherId(teachidAndcount[0]);
							ctent.setCourseHour(Float.valueOf(teachidAndcount[1]));
						}
						ctent.setPunchCard(NewGkElectiveConstant.IF_INT_1);
						ctent.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);//BaseConstants.SUBJECT_TYPE_BX
						ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
						ctent.setModifyTime(new Date());
						addctList.add(ctent);
					}
				}
			}
			//TODO 虚拟课程 班级教学计划
			Map<String,Set<String>> stuBatchMap = new HashMap<>();
			for (NewGkDivideClass jx : jxdivideClassList) {
				List<String> studentList2 = jx.getStudentList();
				if(CollectionUtils.isNotEmpty(studentList2)) {
					for (String stuId : studentList2) {
						stuBatchMap.computeIfAbsent(stuId,e->new HashSet<>()).add(jx.getBatch());
						}
					}
				}
			Map<String,Set<String>> xzbBatchMap = new HashMap<>();
			Map<String,Set<String>> xzbVirMap = new HashMap<>();
			for (String batchstr : batchClassMap.keySet()) {
				String[] split = batchstr.split("-");
				String[] clsIds = split[2].split(",");
				for (String clsId : clsIds) {
					xzbBatchMap.computeIfAbsent(clsId,e->new HashSet<>()).add(batchstr);
					xzbVirMap.computeIfAbsent(clsId,e->new HashSet<>()).add(split[1]);
				}
			}
			
			for (NewGkDivideClass xzb : xzbClaList) {
				Set<String> xuniCourIds = xzb.getStudentList().stream().filter(e->stuBatchMap.containsKey(e)).flatMap(e->stuBatchMap.get(e).stream())
						.map(e->e.split("-")[0]).collect(Collectors.toSet());
				Set<String> others = xzbVirMap.get(xzb.getOldDivideClassId());
				if(CollectionUtils.isNotEmpty(others)){
					xuniCourIds.addAll(others);
				}
				for (String xuniSubId : xuniCourIds) {
					List<ClassTeaching> ctentlist = oldCtMap.get(xuniSubId+ (OldorXId.get(xzb.getId())==null?xzb.getId():OldorXId.get(xzb.getId())));
					if(CollectionUtils.isNotEmpty(ctentlist)) {
						ClassTeaching ctent = ctentlist.get(0);
						ctent.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);//BaseConstants.SUBJECT_TYPE_VIRTUAL
						ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
						ctent.setModifyTime(now);
						ctent.setPunchCard(NewGkElectiveConstant.IF_INT_1);
						ctent.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
						upctList.add(ctent);
						oldCtMap.remove(xuniSubId+ (OldorXId.get(xzb.getId())==null?xzb.getId():OldorXId.get(xzb.getId())));
					}else {
						ClassTeaching ctent = new ClassTeaching();
						ctent.setUnitId(startDateInfo.getSchoolId());
						ctent.setAcadyear(acadyear);
						ctent.setSemester(semester + "");
						if(OldorXId.containsKey(xzb.getId())) {
							ctent.setClassId(OldorXId.get(xzb.getId()));
						}else {
							ctent.setClassId(xzb.getId());
						}
						
						ctent.setSubjectId(xuniSubId);
						ctent.setIsTeaCls(NewGkElectiveConstant.IF_INT_0);
						ctent.setEventSource(NewGkElectiveConstant.IF_INT_0);
						ctent.setId(UuidUtils.generateUuid());
						ctent.setCreationTime(now);
						ctent.setPunchCard(NewGkElectiveConstant.IF_INT_1);
						ctent.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);//BaseConstants.SUBJECT_TYPE_VIRTUAL
						ctent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
						ctent.setModifyTime(now);
						addctList.add(ctent);
					}
				}
			}
			
			
			// 删除老的 班级 教学计划
			if(MapUtils.isNotEmpty(oldCtMap)){
				for(String key: oldCtMap.keySet()){
					List<ClassTeaching> ctentlist = oldCtMap.get(key);
					for(ClassTeaching ct:ctentlist){
						if(Objects.equals(NewGkElectiveConstant.IF_INT_1, ct.getIsTeaCls())) {
							ct.setCourseHour(0f);
						}else {
							ct.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
						}
						ct.setModifyTime(new Date());
						upctList.add(ct);
					}
					
				}
			}
		}
		
		addGtList.addAll(upGtList);
		if (CollectionUtils.isNotEmpty(addGtList)) {
			gradeTeachingRemoteService.saveAll(SUtils.s(addGtList));
		}
		addctList.addAll(upctList);
		Map<String,String> claIdSubIdMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(addctList)){
			claIdSubIdMap = EntityUtils.getMap(addctList,a->a.getClassId()+a.getSubjectId() ,a -> a.getId());
			addctList.stream().filter(e->e!=null&&e.getCourseHour()==null).forEach(e->e.setCourseHour(0f));
			classTeachingRemoteService.saveAll(addctList.toArray(new ClassTeaching[0]));
		}
		//end 分装年级班级教学计划结束
		
		//TODO 处理教学班 ; 将此次的排课教学班 转换为 原教学班
//		List<NewGkDivideClass> jxdivideClassList = allDivideClassList.stream()
//				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
//				.collect(Collectors.toList());
		// 旧教学班
		List<TeachClass> oldAllTeachClassList = SUtils.dt(teachClassRemoteService.findBySearch(array.getUnitId(), acadyear,
				semester +"",TeachClass.CLASS_TYPE_REQUIRED,array.getGradeId(),null), TeachClass.class);
		List<TeachClass> oldTeachClassList = oldAllTeachClassList.stream().filter(e->StringUtils.isNotBlank(e.getRelaCourseId())).collect(Collectors.toList());
		oldAllTeachClassList.removeAll(oldTeachClassList);
		
		// 检测学生是否还一致
//		if(isXzbArray) {
//			// 行政班排课 判断学生是否一致
//			Set<String> virtuClassIds = EntityUtils.getSet(oldTeachClassList, TeachClass::getId);
//			List<TeachClassStu> teachClassStus = SUtils.dt(teachClassStuRemoteService.findByClassIds(virtuClassIds.toArray(new String[0])),
//					TeachClassStu.class);
//			Map<String, String> teachNameMap = EntityUtils.getMap(oldTeachClassList, TeachClass::getId,TeachClass::getName);
//			Map<String, List<String>> teachClassStuMap = EntityUtils.getListMap(teachClassStus, TeachClassStu::getClassId, TeachClassStu::getStudentId);
//			for (NewGkDivideClass dc : jxdivideClassList) {
//				Set<String> studentList2 = new HashSet<>(dc.getStudentList());
//				List<String> oldStuIds = teachClassStuMap.get(dc.getOldClassId());
//				if(CollectionUtils.isEmpty(oldStuIds)) {
//					continue;
//				}
//				
//				// 排课系统的数据 一定要大于 等于原来的数据，原来的班级可以删人 但是不能加人
//				if(!studentList2.containsAll(oldStuIds)) {
//					throw new RuntimeException(teachNameMap.get(dc.getOldClassId())+"学生已经发生变化");
//				}
//			}
//		}
		
		Map<String,TeachClass> oldTeachClaMap=EntityUtils.getMap(oldTeachClassList, TeachClass::getId);
		List<TeachClass> addTcClaList=new ArrayList<>();
		List<TeachClass> upTcClaList=new ArrayList<>();
		List<TeachClassStu> newTeachClassStuList = new ArrayList<>();
		
		Map<String, NewGkTimetable> timetableMap = EntityUtils.getMap(timetableList, NewGkTimetable::getId);
		// 获取教学班 课程  TODO 直接使用timetableList过滤即可 不需要重新获取数据库
		List<NewGkTimetable> teachTimetableList = timetableList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.collect(Collectors.toList());
		
		Map<String, String> classTimetableIdMap = EntityUtils.getMap(teachTimetableList, NewGkTimetable::getClassId,NewGkTimetable::getId);
		// 过滤掉没有课的教学班
		Set<String> hadJxbCids = EntityUtils.getSet(teachTimetableList, NewGkTimetable::getClassId);
		// 过滤出 有课的 教学班
		jxdivideClassList = jxdivideClassList.stream().filter(e->hadJxbCids.contains(e.getId())).collect(Collectors.toList());
		
		TeachClass teachClass = null;
		TeachClassStu teachClassStu = null;
		for (NewGkDivideClass divideClass : jxdivideClassList) {
			teachClass = oldTeachClaMap.get(divideClass.getId());
			if(teachClass==null){
				teachClass = new TeachClass();
				teachClass.setId(divideClass.getId());
				teachClass.setUnitId(startDateInfo.getSchoolId());
				//所有都是必修
				teachClass.setClassType(TeachClass.CLASS_TYPE_REQUIRED);
				teachClass.setCreationTime(new Date());
				addTcClaList.add(teachClass);
			}else {
				upTcClaList.add(teachClass);
				oldTeachClaMap.remove(teachClass.getId());
			}
			ClassHour classHour = clsHourMap.get(divideClass.getBatch());
			if(classHour != null){
				teachClass.setRelaCourseId(classHour.getId());
			}else{
				log.error("虚拟课程 classHour 找不到。");
				throw new RuntimeException(divideClass.getClassName()+"找不到对应的 虚拟课程。");
			}
//			if(isXzbArray
//					&&StringUtils.isNotBlank(divideClass.getOldClassId())) {
//				//TODO 与原教学班的 关联
//				OldorXId.put(divideClass.getId(), teachClass.getId());
//			}
			OldorXId.put(divideClass.getId(), teachClass.getId());
			//teachClass.setCredit(Integer.parseInt(vals[0]));//这里默认学分就是周课时
			teachClass.setAcadyear(acadyear);
			teachClass.setSemester(semester +"");
			teachClass.setAcadyear(acadyear);
			teachClass.setSemester(semester +"");
			teachClass.setName(divideClass.getClassName());
			teachClass.setGradeId(array.getGradeId());
			teachClass.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
			String tid = timetableTeacherMap.get(classTimetableIdMap.get(divideClass.getId()));
			if(StringUtils.isNotBlank(tid)){
				teachClass.setTeacherId(tid);
			}else {
				teachClass.setTeacherId(BaseConstants.ZERO_GUID);
			}
			String placeId = classPlaceMap.get(divideClass.getId());
			teachClass.setPlaceId(placeId);
			teachClass.setPunchCard(NewGkElectiveConstant.IF_INT_1);
			teachClass.setCourseId(divideClass.getSubjectIds());
			teachClass.setIsDeleted(NewGkElectiveConstant.IF_INT_0);;
			teachClass.setIsUsing(NewGkElectiveConstant.IF_INT_1+"");
			teachClass.setIsUsingMerge(NewGkElectiveConstant.IF_0);
			teachClass.setIsMerge(NewGkElectiveConstant.IF_0);
			teachClass.setModifyTime(new Date());
			//新增教学班学生信息
			for (String studentId : divideClass.getStudentList()) {
				teachClassStu = new TeachClassStu();
				teachClassStu.setStudentId(studentId);
				teachClassStu.setClassId(teachClass.getId());
				teachClassStu.setId(UuidUtils.generateUuid());
				teachClassStu.setCreationTime(new Date());
				teachClassStu.setModifyTime(new Date());
				teachClassStu.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
				newTeachClassStuList.add(teachClassStu);
			}
		}
		//TODO 删除原来的教学班
		if(MapUtils.isNotEmpty(oldTeachClaMap)){
			for(String key: oldTeachClaMap.keySet()){
				TeachClass gtent= oldTeachClaMap.get(key);
				gtent.setIsDeleted(NewGkElectiveConstant.IF_INT_1);
				gtent.setModifyTime(new Date());
				upTcClaList.add(gtent);
			}
		}
		
		//教学班的学生每次都是重新添加所有需要先删除
		Set<String> oldjxClaIds = EntityUtils.getSet(upTcClaList, TeachClass::getId);
		oldjxClaIds.addAll(EntityUtils.getSet(addTcClaList, TeachClass::getId));
		if(!oldjxClaIds.isEmpty()){//删除 7选3 中 教学班同时删除学生
			teachClassRemoteService.deleteByIds(oldjxClaIds.toArray(new String[0]));
			teachClassExRemoteService.deleteByTeachClass(oldjxClaIds.toArray(new String[0]));
		}
		
		// oldAllTeachClassList
		Set<String> oldNomalClaIdList = EntityUtils.getSet(oldAllTeachClassList, TeachClass::getId);
		// 查找 有课 的非 虚拟课程教学班
		List<TeachClass> hasExClazzs = SUtils.dt(teachClassRemoteService.findClassHasEx(array.getUnitId(), acadyear, ""+ semester, array.getGradeId(),
				new String[] {TeachClass.CLASS_TYPE_REQUIRED}), TeachClass.class);
		hasExClazzs = hasExClazzs.stream().filter(e->oldNomalClaIdList.contains(e.getId())).peek(e->e.setIsUsing("0")).collect(Collectors.toList());
		teachClassExRemoteService.deleteByTeachClass(oldNomalClaIdList.toArray(new String[0]));
		
		// 添加教学班 以及 学生; 行政班排课时 没有教学班级的增加
		addTcClaList.addAll(upTcClaList);
		addTcClaList.addAll(hasExClazzs);
		if (CollectionUtils.isNotEmpty(addTcClaList)) {
			teachClassRemoteService.saveAll(SUtils.s(addTcClaList));
		}
		if (CollectionUtils.isNotEmpty(newTeachClassStuList)) {
			teachClassStuRemoteService.saveAll(SUtils.s(newTeachClassStuList));
		}
		
		
		//删除指定时间内旧课表
		CourseScheduleDto dto = new CourseScheduleDto();
		dto.setSchoolId(array.getUnitId());
		dto.setAcadyear(acadyear);
		dto.setSemester(semester);
		dto.setWeekOfWorktime1(startDateInfo.getWeek());
		dto.setDayOfWeek1((startDateInfo.getWeekday()==null?1:startDateInfo.getWeekday())-1);// 星期重0开始

		//新课表
		List<CourseSchedule> insertCourseScheduleList = new ArrayList<CourseSchedule>();
		//教学班扩展表
		List<TeachClassEx> insertTeachClassExList = new ArrayList<TeachClassEx>();
		//为了补充班级教学计划中的老师
		Map<String, String> subclaidTeaidMap=new HashMap<String, String>();
		// key: classId-virtuCourseId  v:day-periodInterval-period
		Map<String,Set<String>> classVirtuTimeMap = new HashMap<>();
		Map<String, NewGkDivideClass> allDivideClassMap = EntityUtils.getMap(allDivideClassList, e->e.getId());
		
		int startWeek = startDateInfo.getWeek();
		int startDay = startDateInfo.getWeekday() - 1;
		int endWeek = endDateInfo.getWeek();
		int weekday = endDateInfo.getWeekday()-1;
		for (NewGkTimetableOther o : timetableOtherList) {
			CourseSchedule c = new CourseSchedule();
			c.setAcadyear(acadyear);
			c.setSemester(semester);
			c.setSchoolId(startDateInfo.getSchoolId());
			
			NewGkTimetable timetable = timetableMap.get(o.getTimetableId());
			String oidString=OldorXId.get(timetable.getClassId());
			if(StringUtils.isNotBlank(oidString)){
				c.setClassId(oidString); //不重组   拷贝到新的行政班 装换 老的行政班id
			}else{
				c.setClassId(timetable.getClassId());
			}
			c.setSubjectId(timetable.getSubjectId());
			c.setPlaceId(o.getPlaceId());
			c.setTeacherId(timetableTeacherMap.get(o.getTimetableId()));
			c.setWeekType(o.getFirstsdWeek()==null?NewGkElectiveConstant.FIRSTSD_WEEK_3:o.getFirstsdWeek());
			c.setPunchCard(NewGkElectiveConstant.IF_INT_1);
			if (NewGkElectiveConstant.CLASS_TYPE_1.equals(timetable.getClassType())) {
				c.setClassType(CourseSchedule.CLASS_TYPE_NORMAL);
				c.setSubjectType(BaseConstants.SUBJECT_TYPE_BX);
				subclaidTeaidMap.put(c.getSubjectId()+c.getClassId(), c.getTeacherId());
			} else {
				c.setClassType(CourseSchedule.CLASS_TYPE_TEACH);
				c.setSubjectType(String.valueOf(CourseSchedule.SUBJECT_TYPE_1));
				TeachClassEx  teachClassEx = new TeachClassEx();
				teachClassEx.setId(o.getId());
				teachClassEx.setTeachClassId(c.getClassId());
				teachClassEx.setDayOfWeek(o.getDayOfWeek());
				teachClassEx.setPeriodInterval(o.getPeriodInterval());
				teachClassEx.setPeriod(o.getPeriod());
				teachClassEx.setPlaceId(o.getPlaceId());
				insertTeachClassExList.add(teachClassEx);
				
				NewGkDivideClass divideClass = allDivideClassMap.get(timetable.getClassId());
				if(divideClass != null) {
					divideClass.getStudentList().stream()
					.filter(e->stuIdNewXzbCidMap.containsKey(e))
					.map(e->stuIdNewXzbCidMap.get(e))
					.distinct()
					.forEach(cid->{
						String key = cid+"-"+divideClass.getBatch();
						if(!classVirtuTimeMap.containsKey(key)) {
							classVirtuTimeMap.put(key, new HashSet<>());
						}
						classVirtuTimeMap.get(key).add(o.getDayOfWeek()+"-"+o.getPeriodInterval()+"-"+o.getPeriod());
					});
				}
			}
			
			c.setCourseId(claIdSubIdMap.get(c.getClassId()+c.getSubjectId())==null?"":claIdSubIdMap.get(c.getClassId()+c.getSubjectId()));
			CourseSchedule c1 = null;
			//包括开始时间与结束时间 
			for (int index = startWeek; index <= endWeek; index++) {
				
				if (index == startWeek && o.getDayOfWeek() < startDay) {//开始日期
					continue;
				}
				if (index == endWeek && o.getDayOfWeek() > weekday) {//结束时间
					continue;
				}
				if((NewGkElectiveConstant.FIRSTSD_WEEK_1==c.getWeekType() && index%2==0) || (NewGkElectiveConstant.FIRSTSD_WEEK_2==c.getWeekType() && index%2==1)){
					continue;
				}
				c1 = new CourseSchedule();
				EntityUtils.copyProperties(c, c1);
				c1.setId(UuidUtils.generateUuid());
				c1.setWeekOfWorktime(index);
				c1.setPeriod(o.getPeriod());
				c1.setPeriodInterval(o.getPeriodInterval());
				c1.setDayOfWeek(o.getDayOfWeek());
				insertCourseScheduleList.add(c1);
			}
		}
		
		for (String key : classVirtuTimeMap.keySet()) {
			String[] split = key.split("-");
			String newCid = split[0];
			String virtuCourseId = split[1];
			Set<String> times = classVirtuTimeMap.get(key);
			Set<String> btTimes = xzbBatchTimeMap.get(newCid+"-"+virtuCourseId);
			if(CollectionUtils.isNotEmpty(btTimes)){
				times.addAll(btTimes);
			}
			for (String ts : times) {
				CourseSchedule c = new CourseSchedule();
				c.setAcadyear(acadyear);
				c.setSemester(semester);
				c.setSchoolId(startDateInfo.getSchoolId());
				
				c.setClassId(newCid);
				c.setSubjectId(virtuCourseId);
				c.setWeekType(NewGkElectiveConstant.FIRSTSD_WEEK_3);
				c.setPunchCard(NewGkElectiveConstant.IF_INT_1);
				c.setClassType(CourseSchedule.CLASS_TYPE_NORMAL);
				c.setSubjectType(BaseConstants.SUBJECT_TYPE_VIRTUAL);
				c.setCourseId(claIdSubIdMap.get(c.getClassId()+c.getSubjectId())==null?"":claIdSubIdMap.get(c.getClassId()+c.getSubjectId()));
				
				CourseSchedule c1 = null;
				//包括开始时间与结束时间 
				
				String[] timearr = ts.split("-");
				Integer day = Integer.parseInt(timearr[0]);
				Integer period = Integer.parseInt(timearr[2]);
				String periodInterval = timearr[1];
				for (int index = startWeek; index <= endWeek; index++) {
					
					if (index == startWeek && day < startDay) {//开始日期
						continue;
					}
					if (index == endWeek && day > weekday) {//结束时间
						continue;
					}
					if((NewGkElectiveConstant.FIRSTSD_WEEK_1==c.getWeekType() && index%2==0) || (NewGkElectiveConstant.FIRSTSD_WEEK_2==c.getWeekType() && index%2==1)){
						continue;
					}
					c1 = new CourseSchedule();
					EntityUtils.copyProperties(c, c1);
					c1.setId(UuidUtils.generateUuid());
					c1.setWeekOfWorktime(index);
					c1.setPeriod(period);
					c1.setPeriodInterval(periodInterval);
					c1.setDayOfWeek(day);
					insertCourseScheduleList.add(c1);
				}
			}
			
		}
		
		
		// 教学班课程时间;TODO 暂时不保存 
		if (CollectionUtils.isNotEmpty(insertTeachClassExList)) {
//			teachClassExRemoteService.saveAll(SUtils.s(insertTeachClassExList.toArray(new TeachClassEx[0])));
		}
		Set<String> delclaids=new HashSet<>();
		if(CollectionUtils.isNotEmpty(insertCourseScheduleList)) {
			// courseScheduleId 有用吗？
			delclaids.addAll(EntityUtils.getSet(insertCourseScheduleList,CourseSchedule::getId));
		}
		if(CollectionUtils.isNotEmpty(oldClazzList)) {
			delclaids.addAll(EntityUtils.getSet(oldClazzList,Clazz::getId));
		}
		
		if(CollectionUtils.isNotEmpty(oldTeachClassList)) {
			delclaids.addAll(EntityUtils.getSet(oldTeachClassList,TeachClass::getId));
		}
		if(CollectionUtils.isNotEmpty(oldAllTeachClassList)) {
			delclaids.addAll(EntityUtils.getSet(oldAllTeachClassList,TeachClass::getId));
		}
		
		if(CollectionUtils.isNotEmpty(delclaids)) {
			dto.setClassIds(delclaids.toArray(new String[] {}));
			courseScheduleRemoteService.deleteByClassId(dto);
		}
		
		if (CollectionUtils.isNotEmpty(insertCourseScheduleList)) {
			//老的行政班id和教学班id 删除时间点范围所有班级课程表数据
		
			courseScheduleRemoteService.saveAll(SUtils.s(insertCourseScheduleList));
		}
		
		// 写 虚拟课程时间
//		Map<String,Set<String>> xuniCourseTimeMap = new HashMap<>();
//		Map<String, List<NewGkTimetable>> classTableMap = EntityUtils.getListMap(timetableList, e->e.getClassId(),e->e);
//		for (NewGkDivideClass dc : jxdivideClassList) {
//			List<NewGkTimetable> list = classTableMap.get(dc.getId());
//
//			if(!xuniCourseTimeMap.containsKey(dc.getBatch())) {
//				xuniCourseTimeMap.put(dc.getBatch(), new HashSet<>());
//			}
//
//			list.stream().flatMap(e->timetableOtherMap.get(e.getId()).stream())
//				.map(e->e.getDayOfWeek()+"-"+e.getPeriodInterval()+"-"+e.getPeriod())
//				.distinct().forEach(e->xuniCourseTimeMap.get(dc.getBatch()).add(e));
//		}
		
		// 将方案 设为默认 方案，取消其他方案的 默认标识
		String[] names = {"gradeId", "isDefault","arrangeType"};
		Object[] params = {array.getGradeId(), 1,array.getArrangeType()};
		List<NewGkArray> list = newGkArrayService.findListBy(names, params);
		if (CollectionUtils.isNotEmpty(list)){
			for (NewGkArray one : list){
				if(one.getId().equals(array.getId()))
					continue;
				
				one.setIsDefault(0);
				one.setModifyTime(new Date());
			}
		}
		array.setIsDefault(1);
		array.setModifyTime(new Date());
		list.add(array);
		newGkArrayService.saveAll(list.toArray(new NewGkArray[0]));
	}
	
	private List<SectionDto> getAllSectionByUnit(String unitId) {
		List<SectionDto> sectionDtos = new ArrayList<>();
		String sectionMcode = ColumnInfoUtils.getColumnInfo(Course.class, "section").getMcodeId();
		Map<String, McodeDetail> sectionMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(sectionMcode),new TypeReference<Map<String,McodeDetail>>(){});
		School school = schoolService.findOneObjectById(unitId);
		if(school==null || school.getSections()==null){
			for (Entry<String, McodeDetail> entry : sectionMap.entrySet()) {
				SectionDto sectionDto = new SectionDto(entry.getValue().getMcodeContent(), entry.getKey());
				sectionDtos.add(sectionDto);
			}
		}else{
			String[] sectionArray = school.getSections().split(",");
			for (String temp : sectionArray) {
				//解决报错问题 杭外有一个9的参数
				if(!sectionMap.containsKey(temp)){
					continue;
				}
				SectionDto sectionDto = new SectionDto(sectionMap.get(temp).getMcodeContent(), temp);
				sectionDtos.add(sectionDto);
			}
		}
		return sectionDtos;
	}
	
	@Override
	public boolean checkIsArrayIng(String arrayId){
		if(CGForLectureSolver.isSolverIdRunning(arrayId)){
			return true;
		}else{
			final String key = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId;
			final String key1 = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId+"_mess";	
			
			if(RedisUtils.get(key)!=null && !"error".equals(RedisUtils.get(key))){
				RedisUtils.del(new String[]{key,key1});
			}
			
			return false;
		}	
	}

	@Override
	public String autoArraySameClass(String unitId, String arrayId) {
		List<NewGkTimetable> list = newGkTimetableService.findByArrayId(unitId, arrayId);
		if(CollectionUtils.isEmpty(list)){
			//没有课程数据
			return "没有需要设置的教师的课程";
		}
		//组装辅助数据
		newGkTimetableService.makeTime(unitId, arrayId, list);
		
		Map<String,String> ttoCidMap = new HashMap<>();
		// k:subjectId v(k:periodCode v:ttOthers);
		Map<String,Map<String,List<NewGkTimetableOther>>> subjMap = new HashMap<>();
		Map<String, List<NewGkTimetableOther>> timeClassMap = null;
		Map<String,String> clsSubMap = new HashMap<>();
		for (NewGkTimetable ttb : list) {
			clsSubMap.put(ttb.getClassId()+"_"+ttb.getSubjectId(), ttb.getId());
			List<NewGkTimetableOther> timeList = ttb.getTimeList();
			if(CollectionUtils.isEmpty(timeList)) {
				continue;
			}
			timeClassMap = subjMap.get(ttb.getSubjectId());
			if(timeClassMap == null) {
				timeClassMap = new HashMap<>();
				subjMap.put(ttb.getSubjectId(), timeClassMap);
			}
			for (NewGkTimetableOther tto : timeList) {
				String timeKey = tto.getDayOfWeek()+"_"+tto.getPeriodInterval()+"_"+tto.getPeriod();
				List<NewGkTimetableOther> classIds = timeClassMap.get(timeKey);
				if(classIds == null) {
					classIds = new ArrayList<>();
					timeClassMap.put(timeKey, classIds);
				}
				classIds.add(tto);
				ttoCidMap.put(tto.getId(), ttb.getClassId());
			}
		}
		
		Date now = new Date();
		List<NewGkCourseHeap> insertList = new ArrayList<>();
		for (String subId : subjMap.keySet()) {
			timeClassMap = subjMap.get(subId);
			SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
			Set<String> classIdSet = new HashSet<>();
			for (String periodCode : timeClassMap.keySet()) {
				List<NewGkTimetableOther> ttos = timeClassMap.get(periodCode);
				for (int i=0;i<ttos.size();i++) {
					NewGkTimetableOther tto1 = ttos.get(i);
					String classId = ttoCidMap.get(tto1.getId());
					if(StringUtils.isBlank(classId)) {
						continue;
					}
					if(!classIdSet.contains(classId)) {
						graph.addVertex(classId);
						classIdSet.add(classId);
					}
					for (int j=i+1;j<ttos.size();j++) {
						NewGkTimetableOther tto2 = ttos.get(j);
						String classId2 = ttoCidMap.get(tto2.getId());
						if(StringUtils.isBlank(classId2)) {
							continue;
						}
						if(!classIdSet.contains(classId2)) {
							graph.addVertex(classId2);
							classIdSet.add(classId2);
						}
						if(tto1.getFirstsdWeek() == null 
								|| tto2.getFirstsdWeek() == null
								|| (tto1.getFirstsdWeek() + tto2.getFirstsdWeek() != NewGkElectiveConstant.FIRSTSD_WEEK_3)) {
							if(classId.equals(classId2)){
								throw new RuntimeException("班级冲突没有解决。请先解决再进行下一步");
							}
							graph.addEdge(classId, classId2);
						}
					}
				}
				
			}
			SmallestDegreeLastColoring<String, DefaultEdge> sdc = new SmallestDegreeLastColoring<String, DefaultEdge>(graph);
			Coloring<String> coloring = sdc.getColoring();
			Map<String, Integer> colors = coloring.getColors();
			for (String classId : colors.keySet()) {
				Integer heapNum = colors.get(classId);
				NewGkCourseHeap heap = new NewGkCourseHeap();
				heap.setArrayId(arrayId);
				heap.setHeapNum(heapNum);
				heap.setTimetableId(clsSubMap.get(classId+"_"+subId));
				heap.setSubjectId(subId);
				heap.setCreationTime(now);
				heap.setModifyTime(now);
				insertList.add(heap);
			}
		}
		
		if(CollectionUtils.isNotEmpty(insertList)){
    		try{
    			newGkCourseHeapService.saveOrDel(arrayId,insertList);
    		}catch(Exception e){
    			e.printStackTrace();
    			return "教师分堆保存失败成功";
    		}
    	}else{
    		return "没有需要设置的教师的课程";
    	}
    	return null;
	}
	
	@Override
	public String checkAllTeacherConflict(String arrayId) {
		NewGkArray newGkArray = newGkArrayService.findById(arrayId);
		if(newGkArray==null){
			return ("该方案不存在");
		}

		List<Set<String>> combineClasss = combineRelationService.getCombineRelation(newGkArray.getUnitId(),newGkArray.getLessonArrangeId());
		Map<String,Map<String,Set<String>>> subIdCombineClassMap = new HashMap<>();
		for (Set<String> set : combineClasss) {
			String[] split = set.iterator().next().split("-");
			String subjectId = split[1];
			
			Map<String, Set<String>> map2 = subIdCombineClassMap.get(subjectId);
			if(map2 == null) {
				map2 = new HashMap<>();
				subIdCombineClassMap.put(subjectId, map2);
			}
			Set<String> collect = set.stream().map(e->e.split("-")[0]).collect(Collectors.toSet());
			for (String csid : collect) {
				map2.put(csid, collect);
			}
		}
		
		List<NewGkTimetable> timeList = newGkTimetableService.findByArrayId(newGkArray.getUnitId(), arrayId);
		Map<String, NewGkTimetable> ttCidMap = EntityUtils.getMap(timeList, NewGkTimetable::getId,e->e);
		
		List<NewGkDivideClass> xzbClassList = newGkDivideClassService.findByDivideIdAndClassType(newGkArray.getUnitId(), newGkArray.getId(),
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_4}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		Map<String, String> toOldCidMap = EntityUtils.getMap(
				xzbClassList.stream().filter(e -> e.getOldDivideClassId() != null).collect(Collectors.toList()),
				NewGkDivideClass::getId, NewGkDivideClass::getOldDivideClassId);
		
    	if(CollectionUtils.isNotEmpty(timeList)){
    		newGkTimetableService.makeTime(newGkArray.getUnitId(), arrayId, timeList);
    		Set<String> tabletimeIds = EntityUtils.getSet(timeList, NewGkTimetable::getId);
    		List<NewGkTimetableTeacher> teacherlist = newGkTimetableTeacherService.findByTimetableIds(tabletimeIds.toArray(new String[]{}));
    		Map<String,Set<String>> teacherIdsByTable=new HashMap<String,Set<String>>();
    		if(CollectionUtils.isNotEmpty(teacherlist)){
    			for(NewGkTimetableTeacher n:teacherlist){
    				if(!teacherIdsByTable.containsKey(n.getTimetableId())){
    					teacherIdsByTable.put(n.getTimetableId(), new HashSet<String>());
    					if(StringUtils.isNotEmpty(n.getTeacherId()))
    						teacherIdsByTable.get(n.getTimetableId()).add(n.getTeacherId());
    				}
    			}
    		}
    		
    		Set<String> allSubIds = EntityUtils.getSet(timeList, NewGkTimetable::getSubjectId);
    		Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(allSubIds.toArray(new String[0])), new TypeReference<Map<String,String>>() {});
    		// k:tid k2:timeCode val2:
    		Map<String,Map<String,List<NewGkTimetableOther>>> teacherTimeMap=new HashMap<>();
    		List<NewGkTimetableOther> itemtimeList=null;
    		Map<String,List<NewGkTimetableOther>> keySet=null;
    		Set<String> tidSet=null;
    		for(NewGkTimetable t:timeList){
    			if(CollectionUtils.isEmpty(t.getTimeList())){
    				continue;
    			}
    			if(!teacherIdsByTable.containsKey(t.getId())){
    				continue;
    			}
    			itemtimeList = t.getTimeList();
    			tidSet=teacherIdsByTable.get(t.getId());
    			for(String s:tidSet){
    				if(!teacherTimeMap.containsKey(s)){
    					keySet=new HashMap<>();
    					teacherTimeMap.put(s, keySet);
    					for (NewGkTimetableOther o : itemtimeList) {
		    				String key=o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod();
		    				if(o.getFirstsdWeek() == null) {
		    					return ("存在时间点 "+key+" 没有单双周信息");
		    				}
		    				keySet.put(key, new ArrayList<>(Arrays.asList(o)));
						}
    				}else{
    					keySet = teacherTimeMap.get(s);
    					for (NewGkTimetableOther o : itemtimeList) {
		    				String key=o.getDayOfWeek()+"_"+o.getPeriodInterval()+"_"+o.getPeriod();
		    				List<NewGkTimetableOther> list = keySet.get(key);
		    				if(list == null) {
		    					list = new ArrayList<>();
		    					keySet.put(key, list);
		    				}
		    				if(o.getFirstsdWeek() == null) {
		    					return ("存在时间点 "+key+" 没有单双周信息");
		    				}
		    				list.add(o);
		    				Integer adds = list.stream().map(e->e.getFirstsdWeek()).reduce((x,y)->x+y)
									.orElse(0);
		    				
		    				//beforeIds 与 keySet 有交叉
		    				if(list.size() > 1 && 
		    						adds != (NewGkElectiveConstant.FIRSTSD_WEEK_1+NewGkElectiveConstant.FIRSTSD_WEEK_2)){
		    					List<NewGkTimetable> collect = list.stream().map(e->ttCidMap.get(e.getTimetableId())).collect(Collectors.toList());
		    					String subjectId = collect.get(0).getSubjectId();
		    					Map<String, Set<String>> combineMap = subIdCombineClassMap.get(subjectId);
		    					// 如果有两个科目 肯定冲突
		    					long count = collect.stream().map(e->e.getSubjectId()).distinct().count();
		    					if(count < 2 && combineMap != null) {
		    						List<String> classIds = collect.stream()
		    								.map(e->e.getClassId())
		    								.filter(e->toOldCidMap.containsKey(e))
											.map(e->toOldCidMap.get(e))
		    								.collect(Collectors.toList());
//		    						String classId = classIds.get(0);
		    						if(classIds.size() == list.size() && combineMap.containsKey(classIds.get(0)) && combineMap.get(classIds.get(0)).containsAll(classIds)) {
		    							continue;
		    						}
		    					}
		    					
		    					String[] setArr = key.split("_");
		    					String timeStr=NewGkElectiveConstant.dayOfWeekMap.get(setArr[0]);
		    					timeStr=timeStr+BaseConstants.PERIOD_INTERVAL_Map.get(setArr[1]);
		    					timeStr=timeStr+"第"+setArr[2]+"节";
		    					String mess="";
		    					String subName = subNameMap.get(t.getSubjectId());
		    					if(StringUtils.isBlank(subName)) {
		    						subName = "未知科目 ";
		    					}
		    					mess += subName;
		    					Teacher tttt = SUtils.dc(teacherRemoteService.findOneById(s),Teacher.class);
		    					if(tttt!=null){
		    						mess +=",教师"+tttt.getTeacherName();
		    					}else{
		    						mess +=",存在教师";
		    					}
		    					mess+="在"+timeStr+"存在冲突";
		    					return (mess);
		    				}
						}
    				}
    			}
    				
    		}
    	}
    	
    	return null;
	}

	@Override
	public List<NewGkArray> findByDivideIds(String unitId, String[] divideIds) {
		if(ArrayUtils.isEmpty(divideIds)){
			return new ArrayList<>();
		}
		return newGkArrayDao.findByUnitIdAndDivideIdIn(unitId, divideIds);
	}
	
	@Override
	public List<NewGkArray> findByDivideIdsWithMaster(String unitId, String[] divideIds){
		return findByDivideIds(unitId, divideIds);
	}

    @Override
    public void deleteByGradeIds(String... gradeId) {
        newGkArrayDao.deleteByGradeIds(new Date(), gradeId);
    }

}
