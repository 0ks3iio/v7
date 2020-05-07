package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassHourRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkDivideDao;
import net.zdsoft.newgkelective.data.dto.DivideResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkDivideClassDto;
import net.zdsoft.newgkelective.data.dto.NewGkDivideDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkDivideEx;
import net.zdsoft.newgkelective.data.entity.NewGkDivideStusub;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeService;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassBatchService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideExService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkDivideStusubService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkRelateSubtimeService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkplaceArrangeService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.TypeReference;
@Service("newGkDivideService")
public class NewGkDivideServiceImpl extends BaseServiceImpl<NewGkDivide, String> implements NewGkDivideService{

	@Autowired
	private NewGkDivideDao newGkDivideDao;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGKStudentRangeService newGKStudentRangeService;
	@Autowired
	private NewGKStudentRangeExService newGKStudentRangeExService;
	@Autowired
	private NewGkDivideExService newGkDivideExService;
	@Autowired
	private NewGkClassBatchService newGkClassBatchService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private NewGkplaceArrangeService newGkplaceArrangeService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private NewGkPlaceItemService newGkPlaceItemService;
	@Autowired
	private NewGkPlaceItemService placeItemService;
	@Autowired
	private NewGkRelateSubtimeService relateSubtimeService;
	@Autowired
	private NewGkClassCombineRelationService newGkClassCombineRelationService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private ClassTeachingRemoteService classTeachingService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuService;
	@Autowired
	private NewGkClassSubjectTimeService classSubjectTimeService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private NewGkDivideStusubService newGkDivideStusubService;
	@Autowired
	private ClassHourRemoteService classHourRemoteService;

	@Override
	protected BaseJpaRepositoryDao<NewGkDivide, String> getJpaDao() {
		return newGkDivideDao;
	}

	@Override
	protected Class<NewGkDivide> getEntityClass() {
		return NewGkDivide.class;
	}

	@Override
	public List<NewGkDivide> findByGradeId(String unitId,String gradeId,String stat) {
		List<NewGkDivide> list=null;
		if(StringUtils.isNotBlank(stat)){
			list = newGkDivideDao.findByGradeIdAndStat(unitId,gradeId,stat);
		}else{
			list = newGkDivideDao.findByGradeId(unitId,gradeId);
		}
		if(CollectionUtils.isNotEmpty(list)){
			return list;
		}else{
			return new ArrayList<NewGkDivide>();
		}
	}
	
	@Override
	public List<NewGkDivide> findByGradeIdWithMaster(String unitId, String gradeId, String stat) {
		return findByGradeId(unitId,gradeId,stat);
	}
	
	
	

	@Override
	public void deleteDivide(String unitId, String divideId) {
		//班级学生
		//newGkClassStudentService.deleteByDivideId(divideId);
		newGkDivideClassService.deleteByDivideId(unitId, divideId);
		//newgkelective_stu_range(分班学生范围)
		newGKStudentRangeService.deleteByDivideId(unitId, divideId);
		newGKStudentRangeExService.saveAndDelete(null, divideId);
		//newgkelective_open_subject(分班开设科目)
		newGkOpenSubjectService.deleteByDivideId(divideId);
		
		//班级批次详情表
		newGkClassBatchService.deleteByDivideId(unitId, divideId, null);
		
		// 行政班排课 需要删除  同步来的 场地教师 信息
		NewGkDivide divide = this.findOne(divideId);
		if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
			newGkPlaceItemService.deleteByArrayItemId(divideId);
			newGkTeacherPlanService.deleteByArrayItemId(divideId);
		}
		
		//newgkelective_array_item(排课基础数据) 分班方案中删除
		List<NewGkArrayItem> arrItems= newGkArrayItemService.findListBy("divideId", divideId);
		for(NewGkArrayItem ent : arrItems){
			//newgkelective_lesson_time(上课时间安排)
			//newgkelective_lesson_time_ex(上课时间安排扩展表)
			newGkLessonTimeService.deleteByArrayItemId(unitId, ent.getId());
			
			//newgkelective_subject_time(科目周课时)
			newGkSubjectTimeService.deleteByArrayItemId(unitId, ent.getId());
			
			//newgkelective_teacher_plan(教师安排)
			//newgkelective_teacher_plan_ex(具体教师安排)
			newGkTeacherPlanService.deleteByArrayItemId(ent.getId());
			
			//newgkelective_place_arrange(场地安排)
			newGkplaceArrangeService.deleteByItemId(ent.getId());
			//newgkelective_place_item(场地具体安排)
			newGkPlaceItemService.deleteByArrayItemId(ent.getId());
			
			//newgkelective_choice_relation 07教师特征值  08:科目设置
			
			relateSubtimeService.deletedByItemId(ent.getId());
			
			placeItemService.deleteByArrayItemId(ent.getId());
			
			//newgkelective_clas_comb_rela(同时排课)
			classSubjectTimeService.deleteByArrayItemId(ent.getId());
			newGkClassCombineRelationService.deleteByArrayItemId(ent.getId());
			
			newGkArrayItemService.deleteById(unitId, ent.getId());
		}
		//删除辅助表
		newGkDivideStusubService.deleteByChoiceIdAndDivideId(divide.getChoiceId(),divide.getId());
		newGkDivideDao.updateDelete(new Date(),divideId);
	}
	
	private void deleteDivideTrue(String unitId,String divideId){
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndSourceType(divideId,NewGkElectiveConstant.CLASS_SOURCE_TYPE1,false);
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> set = EntityUtils.getSet(list, NewGkDivideClass::getId);
			newGkDivideClassService.deleteByClassIdIn(unitId,divideId, set.toArray(new String[]{}));
		}
	}

	@Override
	public void saveDivide(NewGkDivide newGkDivide,
			List<NewGkOpenSubject> openSubjectList, boolean isAdd) throws Exception {
		boolean isSysStuChoose=false;
		boolean isSix=false;
		String[] wl=null;//默认物理历史
		if(NewGkElectiveConstant.DIVIDE_TYPE_08.equals(newGkDivide.getOpenType())) {
			//放入辅助表
			isSysStuChoose=true;
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(newGkDivide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_10.equals(newGkDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(newGkDivide.getOpenType()) 
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(newGkDivide.getOpenType()) ) {
			isSysStuChoose=true;
			isSix=true;
		}
		if(isSysStuChoose) {
			String error=newGkDivideStusubService.saveChoiceResult(newGkDivide,isSix,wl);
			if(StringUtils.isNotBlank(error)) {
				throw new Exception(error);
			}
		}
		
		save(newGkDivide);
		
		if(!isAdd){
			newGkOpenSubjectService.deleteByDivideId(newGkDivide.getId());
			//删除 对应的分班结果
			deleteDivideTrue(newGkDivide.getUnitId()
					,newGkDivide.getId());
		}
		if(CollectionUtils.isNotEmpty(openSubjectList)){
			newGkOpenSubjectService.saveAllEntity(openSubjectList);
		}
		if(true) {
			//不管选择什么模式 都重组
			if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(newGkDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(newGkDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(newGkDivide.getOpenType())){
				/**
				 * doto 同步行政班
				 */
				saveFromClazz(newGkDivide);
			}
		}
		
		
	}
	@Override
	public String saveXzbDivide(List<Clazz> classList, Grade grade, String acadyear, String semester, boolean useJxb) {
		
		String unitId = grade.getSchoolId();
		String gradeId = grade.getId();
		
		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getList(classList, Clazz::getId).toArray(new String[0])),Student.class);
		Map<String, List<Student>> studentMap = studentList.stream().collect(Collectors.groupingBy(Student::getClassId));
//		Map<String, List<Clazz>> classMap = classList.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
//		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getList(classList, Clazz::getId).toArray(new String[0])),Student.class);
//		
		NewGkDivide divide = null;
		Date now = new Date();
		List<NewGkDivide> insertDivideList = new ArrayList<NewGkDivide>();
		NewGkDivideClass divideClass = null;
		List<NewGkDivideClass> insertClassList = new ArrayList<NewGkDivideClass>();
		NewGkClassStudent classStudent = null;
		List<NewGkClassStudent> insertStudentList = new ArrayList<NewGkClassStudent>();
//		for (String gradeId : gradeIdList) {
			divide = new NewGkDivide();
			divide.setId(UuidUtils.generateUuid());
			divide.setUnitId(unitId);
			divide.setGradeId(gradeId);
			divide.setChoiceId(BaseConstants.ZERO_GUID);
			
			divide.setOpenType(NewGkElectiveConstant.DIVIDE_TYPE_07);
			int maxTimes=this.findMaxByGradeId(unitId, divide.getGradeId(),true);
			
			divide.setDivideName(grade.getGradeName()+"行政班排课对象"+(maxTimes+1));
			divide.setTimes(maxTimes+1);
			
			divide.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
			divide.setCreationTime(now);
			divide.setModifyTime(now);
			divide.setStat(NewGkElectiveConstant.IF_1);
			divide.setIsDefault(NewGkElectiveConstant.IF_INT_0);
			insertDivideList.add(divide);
//			List<Clazz> inClassList = classMap.get(gradeId);
//			if(CollectionUtils.isNotEmpty(classList)){
				Map<String,String> stuClaMap = new HashMap<>();
				Map<String,String> claPlaceMap = new HashMap<>();
				Map<String,String> claNameMap = new HashMap<>();
				for (Clazz clazz : classList) {
					divideClass = new NewGkDivideClass();
					divideClass.setId(UuidUtils.generateUuid());
					divideClass.setDivideId(divide.getId());
					divideClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_1);
					divideClass.setCreationTime(now);
					divideClass.setModifyTime(now);
					divideClass.setIsHand(NewGkElectiveConstant.IF_0);
					divideClass.setOldClassId(clazz.getId());
					divideClass.setClassName(clazz.getClassName());
					//divideClass.setClassName(clazz.getClassNameDynamic());
					divideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
					insertClassList.add(divideClass);
					if(StringUtils.isNotBlank(clazz.getTeachPlaceId()))
						claPlaceMap.put(clazz.getId(), clazz.getTeachPlaceId());
					claNameMap.put(clazz.getId(), clazz.getClassName());
					List<Student> inStudnetList = studentMap.get(clazz.getId());
					if(CollectionUtils.isNotEmpty(inStudnetList)){
						List<String> stuIds = new ArrayList<>();
						for (Student student : inStudnetList) {
							classStudent = new NewGkClassStudent();
							classStudent.setId(UuidUtils.generateUuid());
							classStudent.setCreationTime(now);
							classStudent.setModifyTime(now);
							classStudent.setDivideId(divide.getId());
							classStudent.setUnitId(unitId);
							classStudent.setClassId(divideClass.getId());
							classStudent.setStudentId(student.getId());
							insertStudentList.add(classStudent);
							stuIds.add(student.getId());
							stuClaMap.put(student.getId(), clazz.getId());
						}
						divideClass.setStudentList(stuIds);
					}
				}
//			}
//		}
		
		// 如果存在 虚拟课程 以及其下的教学班 将教学班数据也迁移进来
		//TODO 新建分班方案 添加 学年学期设置
		// 找到所有 虚拟课程
		Map<String,String> classIdRelaMap = new HashMap<>();
		Map<String,String> virtuSubNameMap = new HashMap<>();
		Map<String,String> tcVirtuSubIdMap = new HashMap<>();
		List<TeachClass> teachClassList = new ArrayList<>();
		if(useJxb){
			teachClassList = insertTeachClass(gradeId, acadyear, semester, unitId, grade, classList, divide, now, insertClassList,
					insertStudentList, classIdRelaMap,virtuSubNameMap,tcVirtuSubIdMap);
		}
		List<String> openVirtualSubList = teachClassList.stream().map(e->e.getCourseId()).distinct().collect(Collectors.toList());
		// 构建 openSubject数据
		if(CollectionUtils.isNotEmpty(openVirtualSubList)) {
			List<NewGkOpenSubject> osList = new ArrayList<>();
			NewGkOpenSubject os = null;
			for (String vs : openVirtualSubList) {
				os = new NewGkOpenSubject();
				os.setId(UuidUtils.generateUuid());
				os.setDivideId(divide.getId());
				os.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
				os.setCreationTime(now);
				os.setModifyTime(now);
				os.setSubjectId(vs);
				os.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
				
				osList.add(os);
			}
			
			
			// 从教学计划处 同步 教师 场地信息
			Map<String, List<String>> teacherClassMap = new HashMap<>();
			Map<String, String> classPlaceMap = new HashMap<>();
			List<NewGkPlaceItem> placeItemList = new ArrayList<>();
			NewGkPlaceItem pi = null;
			// relaCourseId+"-"+placeId
			Set<String> placeChecks = new HashSet<>();
			// relaCourseId+"-"+teacherId
			Set<String> teacherChecks = new HashSet<>();
			Map<String,Set<String>> stuSubMap = new HashMap<>();
			for (TeachClass tc : teachClassList) {
				String divClaId = classIdRelaMap.get(tc.getId());
				if(StringUtils.isBlank(divClaId))
					continue;
				
				if(CollectionUtils.isNotEmpty(tc.getStuIdList())) {
					for (String stuId : tc.getStuIdList()) {
						stuSubMap.computeIfAbsent(stuId,e->new HashSet<>()).add(tc.getCourseId());
					}
				}
				
				if(StringUtils.isNotBlank(tc.getPlaceId())) {
					String placeCheckstr = tc.getRelaCourseId()+"-"+tc.getPlaceId();
					if(placeChecks.contains(placeCheckstr)) {
						//TODO 场地 冲突； 除了同一批次的场地不能重复外，还要求 可以在此批次时间上 上课的行政班 场地 不能与 批次里面的相同
						return tc.getName()+" 的场地 和 同虚拟课程中的其他班级重复";
					}else {
						//没有冲突
						placeChecks.add(placeCheckstr);
					}
					
					classPlaceMap.put(divClaId, tc.getPlaceId());
					pi = new NewGkPlaceItem();
					pi.setId(UuidUtils.generateUuid());
					pi.setArrayItemId(divide.getId());
					pi.setObjectId(divClaId);
					pi.setPlaceId(tc.getPlaceId());
					pi.setType(NewGkElectiveConstant.SCOURCE_PLACE_TYEP_2);
					placeItemList.add(pi);
				}
				if(StringUtils.isNotBlank(tc.getTeacherId()) && !BaseConstants.ZERO_GUID.equals(tc.getTeacherId())) {
					String teacherCheckstr = tc.getRelaCourseId()+"-"+tc.getTeacherId();
					if(teacherChecks.contains(teacherCheckstr)) {
						// 冲突
						return tc.getName()+" 的教师 和 同虚拟课程中的其他教师重复";
					}else {
						//没有冲突
						teacherChecks.add(teacherCheckstr);
					}

					teacherClassMap.computeIfAbsent(tc.getTeacherId() + "-" + tc.getCourseId(), k -> new ArrayList<>())
							.add(divClaId);
				}
			}
			
			//  场地 冲突； 除了同一批次的场地不能重复外，还要求 可以在此批次时间上 上课的行政班 场地 不能与 批次里面的相同
			Map<String, List<TeachClass>> relaCourseClassMap = EntityUtils.getListMap(teachClassList, TeachClass::getRelaCourseId,e->e);
			for (String relaCourseId : relaCourseClassMap.keySet()) {
				List<TeachClass> tCs = relaCourseClassMap.get(relaCourseId);
				Set<String> placeIds = tCs.stream().filter(e->StringUtils.isNotBlank(e.getPlaceId())).map(e->e.getPlaceId()).collect(Collectors.toSet());
				if(CollectionUtils.isEmpty(placeIds))
					continue;
				// 在此批次走班的学生所在的行政班
				Set<String> bxzbClaIds = tCs.stream().filter(e->CollectionUtils.isNotEmpty(e.getStuIdList()))
					.flatMap(e->e.getStuIdList().stream()).filter(e->stuClaMap.containsKey(e))
					.map(e->stuClaMap.get(e)).collect(Collectors.toSet());
				
				Optional<String> optional = claPlaceMap.keySet().stream()
						.filter(e->!bxzbClaIds.contains(e))  // 找出 与 此 批次的 学生没有交集的 行政班；这些行政班的场地不能 和 教学班 相同
						.filter(e->placeIds.contains(claPlaceMap.get(e)))
						.findFirst();
				if(optional.isPresent()) {
					String xzbId = optional.get();
					return "虚拟课程 "+virtuSubNameMap.get(tcVirtuSubIdMap.get(relaCourseId)) +" 下的班级不能使用 和 "+claNameMap.get(xzbId)+" 一样的场地";
				}
			}
			
			// 教师信息
			List<NewGkTeacherPlan> tpList = new ArrayList<>();
			List<NewGkTeacherPlanEx> tpxList = new ArrayList<>();
			NewGkTeacherPlan tp;
			NewGkTeacherPlanEx tpx;
			Map<String, NewGkTeacherPlan> tpMap = new HashMap<>();
			for (String key : teacherClassMap.keySet()) {
				List<String> claIds = teacherClassMap.get(key);
				String[] split = key.split("-");
				String teacherId = split[0];
				String subjectId = split[1];
				
				tp = tpMap.get(subjectId);
				if(tp == null) {
					tp = new NewGkTeacherPlan();
					tp.setId(UuidUtils.generateUuid());
					tp.setArrayItemId(divide.getId());
					tp.setSubjectId(subjectId);
					tp.setCreationTime(now);
					tp.setModifyTime(now);
					tpMap.put(subjectId, tp);
					tpList.add(tp);
				}
				
				tpx = new NewGkTeacherPlanEx();
				tpx.setId(UuidUtils.generateUuid());
				tpx.setTeacherPlanId(tp.getId());
				tpx.setClassIds(String.join(",", claIds.toArray(new String[0])));
				tpx.setCreationTime(now);
				tpx.setModifyTime(now);
				tpx.setTeacherId(teacherId);
				tpxList.add(tpx);
			}
			
			// 将 行政班所对应的 额外 科目写进 subjectIds
			for (NewGkDivideClass dc : insertClassList) {
				if(NewGkElectiveConstant.CLASS_TYPE_1.equals(dc.getClassType())
						&& CollectionUtils.isNotEmpty(dc.getStudentList())) {
					Set<String> subIds = dc.getStudentList().stream()
						.filter(e->stuSubMap.containsKey(e))
						.flatMap(e->stuSubMap.get(e).stream())
						.collect(Collectors.toSet());
					String exzbSubIds = openVirtualSubList.stream().filter(e->!subIds.contains(e)).collect(Collectors.joining(","));
					if(StringUtils.isNotBlank(exzbSubIds)) {
						dc.setSubjectIds(exzbSubIds);
					}
				}
			}
			
			
//			int i = 1/0;
			// 保存场地 老师 信息
			if(CollectionUtils.isNotEmpty(placeItemList)) {
				placeItemService.saveAll(placeItemList.toArray(new NewGkPlaceItem[0]));
			}
			newGkTeacherPlanService.saveList(tpList, tpxList);
			if(CollectionUtils.isNotEmpty(osList))
				newGkOpenSubjectService.saveAllEntity(osList);
		}
//		int i = 1/0;
		// 保存结果 
		newGkDivideClassService.saveAllList(insertDivideList, insertClassList, insertStudentList);
		
		return null;
	}
	
	private List<TeachClass> insertTeachClass(String gradeId, String acadyear, String semester, String unitId, Grade grade,
			List<Clazz> classList, NewGkDivide divide, Date now, List<NewGkDivideClass> insertClassList,
			List<NewGkClassStudent> insertStudentList, Map<String,String> classIdRelaMap,
			Map<String,String> virtuSubNameMap,Map<String,String> tcVirtuSubIdMap) {
		List<TeachClass> teachClassList = new ArrayList<>(); 
		if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
			return teachClassList;
		}
		
		NewGkDivideClass divideClass;
		NewGkClassStudent classStudent;
		Integer section = grade.getSection();
		List<Course> virtualCourses = SUtils.dt(courseRemoteService.getVirtualCourses(unitId, String.valueOf(section)), Course.class);
		if(CollectionUtils.isEmpty(virtualCourses)) // 没有虚拟课程 返回
			return teachClassList;
		
		Set<String> virtualSubjectIds = EntityUtils.getSet(virtualCourses, Course::getId);
//		Set<String> classIdList = EntityUtils.getSet(classList, Clazz::getId);
		// 虚拟课程的班级 
		/*List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingService.findByClassIdsSubjectIds(unitId, acadyear, semester,
				classIdList.toArray(new String[0]), virtualSubjectIds.toArray(new String[0]),false), ClassTeaching.class);
		// 没有班级开设虚拟课程
		if(CollectionUtils.isEmpty(classTeachingList)) 
			return teachClassList;
		
		virtualSubjectIds.clear();
		virtualSubjectIds.addAll(EntityUtils.getSet(classTeachingList, ClassTeaching::getSubjectId));*/
		
		TeachClassSearchDto dto = new TeachClassSearchDto();
		dto.setUnitId(unitId);
		dto.setAcadyearSearch(acadyear);
		dto.setSemesterSearch(semester);
		dto.setGradeIds(gradeId);
		dto.setClassType(BaseConstants.SUBJECT_TYPE_BX);
		List<TeachClass> allTeachClazzs = SUtils.dt(teachClassService.findBySearch(unitId, acadyear, semester, 
				BaseConstants.SUBJECT_TYPE_BX, gradeId, null), TeachClass.class);

		List<ClassHour> classHourList = SUtils.dt(classHourRemoteService.findListByUnitId(acadyear, semester, unitId, gradeId),ClassHour.class);
		Map<String, ClassHour> clsHourIdMap = EntityUtils.getMap(classHourList, e -> e.getId(), e -> e);
		//TODO 教学班 学生为空？
		teachClassList = allTeachClazzs.stream()
				.filter(e->NewGkElectiveConstant.IF_1.equals(e.getIsUsing()) &&clsHourIdMap.containsKey(e.getRelaCourseId()))
				.collect(Collectors.toList());
		 // 没有教学班 绑定虚拟课程
		if(CollectionUtils.isEmpty(teachClassList)) {
			return teachClassList;
		}

		Map<String, String> oldIdNewMap = EntityUtils.getMap(insertClassList, e -> e.getOldClassId(), e -> e.getId());
		Set<String> virtualClasIds = EntityUtils.getSet(teachClassList, TeachClass::getId);
		List<TeachClassStu> teaStus = SUtils.dt(teachClassStuService.findByClassIds(virtualClasIds.toArray(new String[0])), TeachClassStu.class);
		Map<String, List<String>> teachStuMap = EntityUtils.getListMap(teaStus, TeachClassStu::getClassId, TeachClassStu::getStudentId);
		Map<String, String> stuXzbIdMap = EntityUtils.getMap(insertStudentList, NewGkClassStudent::getStudentId, NewGkClassStudent::getClassId);
		long virtualCount = teachClassList.stream().map(TeachClass::getRelaCourseId).distinct().count();
		divide.setBatchCountTypea((int)virtualCount);
		Map<String,Set<String>> batchStuMap = new HashMap<>();
		virtualCourses.forEach(e->virtuSubNameMap.put(e.getId(), e.getSubjectName()));
		// 转换教学班 为 divideClass 教学班
		for (TeachClass teachClass : teachClassList) {
			divideClass = new NewGkDivideClass();
			divideClass.setId(UuidUtils.generateUuid());
			divideClass.setDivideId(divide.getId());
			divideClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
			divideClass.setCreationTime(now);
			divideClass.setModifyTime(now);
			divideClass.setIsHand(NewGkElectiveConstant.IF_0);
			divideClass.setOldClassId(teachClass.getId());
			divideClass.setClassName(teachClass.getName());
			divideClass.setSubjectIds(teachClass.getCourseId());
			divideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
			divideClass.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			//TODO
			ClassHour classHour = clsHourIdMap.get(teachClass.getRelaCourseId());
			if(StringUtils.isNotBlank(classHour.getClassIds())){
				String newClsIds = Stream.of(classHour.getClassIds().split(",")).filter(e -> oldIdNewMap.containsKey(e)).map(e -> oldIdNewMap.get(e))
						.collect(Collectors.joining(","));
				divideClass.setBatch(classHour.getSubjectId()+"-"+newClsIds);
			}else if(teachStuMap.containsKey(teachClass.getId())){
				String classIds = teachStuMap.get(teachClass.getId()).stream()
						.filter(e ->stuXzbIdMap.containsKey(e)).map(e ->stuXzbIdMap.get(e))
						.distinct().collect(Collectors.joining(","));
				if(StringUtils.isNotBlank(classIds)){
					// 不限班级的 情况
				    divideClass.setBatch(classHour.getSubjectId()+"-"+BaseConstants.ZERO_GUID);
//					divideClass.setBatch(classHour.getSubjectId()+"-"+classIds);
				}else{
					continue;
				}
			}
			insertClassList.add(divideClass);
			classIdRelaMap.put(teachClass.getId(), divideClass.getId());
			tcVirtuSubIdMap.put(teachClass.getId(),classHour.getSubjectId());
			if(teachStuMap.containsKey(teachClass.getId())&& teachStuMap.get(teachClass.getId()).size()>0) {
				// 判断学生重复
				List<String> stuIds = teachStuMap.get(teachClass.getId());
				divideClass.setStudentList(stuIds);
				teachClass.setStuIdList(stuIds);
				Set<String> batchStus = batchStuMap.computeIfAbsent(divideClass.getBatch(), k -> new HashSet<>());
				boolean allMatch = stuIds.stream().anyMatch(e->batchStus.contains(e));
				if(allMatch) {
					throw new RuntimeException("虚拟课程 "+virtuSubNameMap.get(classHour.getSubjectId())+" 对应教学班中存在相同的学生");
				}
				batchStus.addAll(stuIds);
				
				for (String stuId : stuIds) {
					classStudent = new NewGkClassStudent();
					classStudent.setId(UuidUtils.generateUuid());
					classStudent.setCreationTime(now);
					classStudent.setModifyTime(now);
					classStudent.setDivideId(divide.getId());
					classStudent.setUnitId(unitId);
					classStudent.setClassId(divideClass.getId());
					classStudent.setStudentId(stuId);
					insertStudentList.add(classStudent);
				}
			}
		}
		
		
		return teachClassList;
	}
	
	/**
	 * 同步行政班  wyy.将分班结果 初始化为  行政班数据
	 * @param newGkDivide
	 */
	private void saveFromClazz(NewGkDivide newGkDivide){
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(newGkDivide.getUnitId(), newGkDivide.getGradeId()), new TR<List<Clazz>>(){});
		
		if(CollectionUtils.isNotEmpty(clazzList)){
			List<NewGkDivideClass> insertClass=new ArrayList<NewGkDivideClass>();
			List<NewGkClassStudent> insertStudentList=new ArrayList<NewGkClassStudent>();
			Set<String> ids = EntityUtils.getSet(clazzList,Clazz::getId);
			//所有 行政班的 所有学生
			List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(ids.toArray(new String[]{})), new TR<List<Student>>(){});
			if(CollectionUtils.isEmpty(studentList)){
				return;
			}
			Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newGkDivide.getUnitId(),newGkDivide.getChoiceId(),new String[]{NewGkElectiveConstant.CHOICE_TYPE_04});
			List<NewGkChoRelation> ngcrlist = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
			Set<String> notstuids =new HashSet<String>();
			if(CollectionUtils.isNotEmpty(ngcrlist)){
				notstuids = EntityUtils.getSet(ngcrlist, NewGkChoRelation::getObjectValue);
			}
			
			//有排课的学生id
//			Set<String> stuIds = newGkChoResultService.findSetByChoiceId(newGkDivide.getChoiceId());
			Map<String,List<String>> studentMap=new HashMap<String,List<String>>();  //K:班级id  V:学生id List 
			for(Student s:studentList){
				if(notstuids.contains(s.getId())){
					continue;
				}
//				if(stuIds.contains(s.getId())){
					if(!studentMap.containsKey(s.getClassId())){
						studentMap.put(s.getClassId(), new ArrayList<String>());
					}
					studentMap.get(s.getClassId()).add(s.getId());
//				}
//				studentMap.get(s.getClassId()).add(s.getId());
			}
			NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
			int i=1;
			NewGkClassStudent studentDto=null;
			for(Clazz clazz:clazzList){
				newGkDivideClass = new NewGkDivideClass();
				newGkDivideClass.setBestType(NewGkElectiveConstant.BEST_TYPE_2);
				//暂时自定义
//				newGkDivideClass.setClassName(i+"班");
				newGkDivideClass.setClassName(clazz.getClassName());
				newGkDivideClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_1);
				newGkDivideClass.setCreationTime(new Date());
				newGkDivideClass.setDivideId(newGkDivide.getId());
				newGkDivideClass.setId(UuidUtils.generateUuid());
				newGkDivideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
				newGkDivideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
				newGkDivideClass.setModifyTime(new Date());
				//防止多次不重组
				newGkDivideClass.setOldClassId(clazz.getId());
				newGkDivideClass.setOrderId(i++);
//				i++;
				if(studentMap.containsKey(clazz.getId())){
					for( String stu:studentMap.get(clazz.getId())){
						studentDto=initClassStudent(newGkDivide.getUnitId(), newGkDivide.getId(), newGkDivideClass.getId(), stu);
						insertStudentList.add(studentDto);
					}
				}
				insertClass.add(newGkDivideClass);
				
			}
			if(CollectionUtils.isNotEmpty(insertClass)){
				newGkDivideClassService.saveAllList(null, null, null, insertClass, insertStudentList, false);
			}
		}
	}

	@Override
	public int findMaxByGradeId(String unitId, String gradeId,boolean isXzb) {
		Integer maxTimes =null;
		if(isXzb) {
			maxTimes = newGkDivideDao.findMaxByGradeIdXzb(unitId,gradeId);
		}else {
			maxTimes = newGkDivideDao.findMaxByGradeId(unitId,gradeId);
		}
		if(maxTimes==null){
			return 0;
		}
		return maxTimes;
	}

	@Override
	public void updateStat(String divideId, String stat) {
		newGkDivideDao.updateStat(divideId, stat);
	}

	@Override
	public List<NewGkDivide> findByChoiceId(String choiceId) {
		return newGkDivideDao.findByChoiceId(choiceId);
	}
	
	/**
	 * 根据分班Id查询学考班 选考班的科目
	 */
	@Override
	public List<Course> findCourseByDivideId(String divide_id) {
		List<NewGkOpenSubject> rowOpenSubjectList = newGkOpenSubjectService.findByDivideId(divide_id);
		
//		Iterator<NewGkOpenSubject> iterator = rowOpenSubjectList.iterator();
//		while(iterator.hasNext()){
//			if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(iterator.next().getSubjectType())){
//				iterator.remove();
//			}
//		}
		String[] subjectIds = EntityUtils.getSet(rowOpenSubjectList, NewGkOpenSubject::getSubjectId).toArray(new String[0]);
		String subjectJsons = courseRemoteService.findListByIds(subjectIds);
		List<Course> subjects = SUtils.dt(subjectJsons, Course.class);
		return subjects;
	}

	@Override
	@Transactional
	public NewGkDivide findById(String divideId) {
		return newGkDivideDao.findById(divideId).orElse(null);
	}
	

	//展现 行政班数量  三科组合数量 两科组合数量   threeSubCls 
	//各科目AB数量 teaCls
	@Override
	public List<NewGkDivideDto> makeDivideItem(List<NewGkDivide> divideList){
		List<NewGkDivideDto> dtos = new ArrayList<NewGkDivideDto>();
		if (CollectionUtils.isEmpty(divideList)) {
			return dtos;
		}
		Map<String,Integer> xzbMap=new HashMap<String,Integer>();
		Map<String,Integer> zhbThreeMap=new HashMap<String,Integer>();//3+0
		Map<String,Integer> zhbTwoMap=new HashMap<String,Integer>();//2+x
		Map<String,Integer> zhbOneMap=new HashMap<String,Integer>();//1+x
		Map<String,Integer> zhbMixMap=new HashMap<String,Integer>();//混合
		
		//单科(7选3)
		Map<String,Map<String,Integer>> subjectClassNumMap=new HashMap<String,Map<String,Integer>>();
		//文理
		Map<String,Map<String,Integer>> subjectClassNumMap2=new HashMap<String,Map<String,Integer>>();

		
		Set<String> subjectIds=new HashSet<String>();
		Map<String,Set<String>> subjectIdsByDivideMap=new HashMap<String,Set<String>>();
		Set<String> ids = EntityUtils.getSet(divideList, NewGkDivide::getId);
		
		Map<String, Integer> countByDivideMap = newGkDivideClassService.findCountByDivideIdAndClassType(ids.toArray(new String[]{}), new String[]{NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1});
		
		Map<String, Integer> countBySubjectMap = newGkDivideClassService.findCountByDivideIdAndSubjectType(ids.toArray(new String[]{}));
		
		if(countByDivideMap.size()>0){
			for(Entry<String, Integer> item:countByDivideMap.entrySet()){
				Integer value = item.getValue();
				if(value==null || value==0){
					continue;
				}
				String key=item.getKey();
				String[] arr = key.split("_");
				if(NewGkElectiveConstant.CLASS_TYPE_1.equals(arr[1])){
					//行政班
					addMapNum(arr[0], xzbMap,value);
				}else if(arr.length>2){
					//旧数据 3科组合 sujectType="" //这个直接排除 遇到问题 数据库处理
					//组合
					if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(arr[2])){
						//3科
						addMapNum(arr[0], zhbThreeMap,value);
					}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(arr[2])){
						//2科
						addMapNum(arr[0], zhbTwoMap,value);
					}else if(NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(arr[2])){
						// 定1走2
						addMapNum(arr[0], zhbOneMap,value);
					}else {
						//混合数量
						addMapNum(arr[0], zhbMixMap,value);
					}
				}
			}
		}
		
		if(countBySubjectMap.size()>0){
			for(Entry<String, Integer> item:countBySubjectMap.entrySet()){
				Integer value = item.getValue();
				if(value==null || value==0){
					continue;
				}
				String key=item.getKey();
				String[] arr = key.split("_");
				String subjectKey=arr[1]+"_"+arr[2];
				addMapNum2(arr[0], subjectKey, subjectClassNumMap,value);
				subjectIds.add(arr[1]);
				if(!subjectIdsByDivideMap.containsKey(arr[0])){
					subjectIdsByDivideMap.put(arr[0], new HashSet<String>());
				}
				subjectIdsByDivideMap.get(arr[0]).add(arr[1]);
				if(!key.endsWith("_")){
					if(StringUtils.isNotBlank(arr[3])){
						//文理abc
						String subjectKey2=arr[1]+"_"+arr[3];
						addMapNum2(arr[0], subjectKey2, subjectClassNumMap2,value);
					}
				}
				
			}
		}
		
//		
//		List<NewGkDivideClass> list = newGkDivideClassService.findListByIn("divideId", ids.toArray(new String[]{}));
//		if(CollectionUtils.isNotEmpty(list)){
//			for(NewGkDivideClass divideClazz:list){
//				if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClazz.getClassType())){
//					//行政班
//					addMapNum(divideClazz.getDivideId(), xzbMap,1);
//				}else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(divideClazz.getClassType())){
//					if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(divideClazz.getSubjectType())){
//						//3科
//						addMapNum(divideClazz.getDivideId(), zhbThreeMap,1);
//					}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(divideClazz.getSubjectType())){
//						//2科
//						addMapNum(divideClazz.getDivideId(), zhbTwoMap,1);
//					}else{
//						//混合
//					}
//				}else{
//					//单科
//					String subjectKey=divideClazz.getSubjectIds()+"_"+divideClazz.getSubjectType();
//					addMapNum2(divideClazz.getDivideId(), subjectKey, subjectClassNumMap,1);
//					subjectIds.add(divideClazz.getSubjectIds());
//					if(!subjectIdsByDivideMap.containsKey(divideClazz.getDivideId())){
//						subjectIdsByDivideMap.put(divideClazz.getDivideId(), new HashSet<String>());
//					}
//					subjectIdsByDivideMap.get(divideClazz.getDivideId()).add(divideClazz.getSubjectIds());
//					if(StringUtils.isNotBlank(divideClazz.getBestType())){
//						//文理abc
//						String subjectKey2=divideClazz.getSubjectIds()+"_"+divideClazz.getBestType();
//						addMapNum2(divideClazz.getDivideId(), subjectKey2, subjectClassNumMap2,1);
//					}
//				}
//			}
//		}
		List<Course> courseList=new ArrayList<Course>();
		if(CollectionUtils.isNotEmpty(subjectIds)){
			courseList=SUtils.dt(courseRemoteService.orderCourse(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{}))),new TR<List<Course>>(){});
			
			//按subjectCode排序
//			if(CollectionUtils.isNotEmpty(courseList)){
//				Collections.sort(courseList,new Comparator<Course>() {
//
//					@Override
//					public int compare(Course o1, Course o2) {
//						return o1.getSubjectCode().compareTo(o2.getSubjectCode());
//					}
//				});
//			}
		}
		//数量为0 不展现
		for (NewGkDivide ent : divideList) {
			NewGkDivideDto dto = new NewGkDivideDto();
			dto.setEnt(ent);
			
			if(xzbMap.containsKey(ent.getId())){
				dto.setXzbAllclassNum(xzbMap.get(ent.getId()));
			}else{
				dto.setXzbAllclassNum(0);
			}
			if(zhbThreeMap.containsKey(ent.getId())){
				dto.setThreeAllclassNum(zhbThreeMap.get(ent.getId()));
			}else{
				dto.setThreeAllclassNum(0);
			}
			if(zhbTwoMap.containsKey(ent.getId())){
				dto.setTwoAllclassNum(zhbTwoMap.get(ent.getId()));
			}else{
				dto.setTwoAllclassNum(0);
			}
			if(zhbOneMap.containsKey(ent.getId())){
				dto.setOneAllclassNum(zhbOneMap.get(ent.getId()));
			}else{
				dto.setOneAllclassNum(0);
			}
			if(zhbMixMap.containsKey(ent.getId())){
				dto.setMixAllclassNum(zhbMixMap.get(ent.getId()));
			}else{
				dto.setMixAllclassNum(0);
			}
			List<String[]> showCourseList = new ArrayList<String[]>();
			//暂时如果未开设的单科教学班的科目 则不展示 这样目的 否则就要取一遍开设课程数据
			Set<String> showSubjectIds = subjectIdsByDivideMap.get(ent.getId());
			if(CollectionUtils.isNotEmpty(showSubjectIds)){
				//为了排序一直
				for(Course c:courseList){
					if(showSubjectIds.contains(c.getId())){
						showCourseList.add(new String[]{c.getId(),c.getSubjectName()});
					}
				}
			}
			dto.setShowCourseList(showCourseList);
			if(NewGkElectiveConstant.DIVIDE_TYPE_03.equals(ent.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_04.equals(ent.getOpenType())){
				Map<String, Integer> subjectNumMap = subjectClassNumMap2.get(ent.getId());
				if(subjectNumMap==null){
					subjectNumMap=new HashMap<String, Integer>();
				}
				dto.setAbAllclassNum(subjectNumMap);
				dto.setShowFen(new String[]{"A","B","C"});
			}else{
				Map<String, Integer> subjectNumMap = subjectClassNumMap.get(ent.getId());
				if(subjectNumMap==null){
					subjectNumMap=new HashMap<String, Integer>();
				}
				dto.setAbAllclassNum(subjectNumMap);
				dto.setShowFen(new String[]{"A","B"});
			}
			
			makeDivideing(dto);
			
			dtos.add(dto);
		}
		
		return dtos;
	}
	private  void makeDivideing(NewGkDivideDto dto){
		NewGkDivide ent = dto.getEnt();
		String key = NewGkElectiveConstant.DIVIDE_CLASS+"_"+ent.getId();
//		String keyA = NewGkElectiveConstant.DIVIDE_CLASS+"_"+ent.getId()+"_A";
//		String keyB = NewGkElectiveConstant.DIVIDE_CLASS+"_"+ent.getId()+"_B";
		String key1 = NewGkElectiveConstant.DIVIDE_CLASS+"_"+ent.getId()+"_mess";
		String keyType = NewGkElectiveConstant.DIVIDE_CLASS+"_"+ent.getId()+"_type";
//		if(RedisUtils.get(key)!=null && ("error".equals(RedisUtils.get(keyA)) || "error".equals(RedisUtils.get(keyB)))){
//			 RedisUtils.set(key, "error");
//		 }
//		 if(RedisUtils.get(key)!=null && ("success".equals(RedisUtils.get(keyA)) && "success".equals(RedisUtils.get(keyB)))){
//			 RedisUtils.set(key, "success");
//		 }
		//2+x
		String key2 = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + ent.getId();
		String keyMess2 = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ ent.getId() + "_mess";
		Boolean isTwoNow=false;
		String twoMess="";
		if (RedisUtils.get(key2) != null && "start".equals(RedisUtils.get(key2))) {
			// 智能分班
			isTwoNow=true;
		} else {
			if (RedisUtils.get(key2) != null && ("success".equals(RedisUtils.get(key2))
					|| "error".equals(RedisUtils.get(key2)))) {
				if("error".equals(RedisUtils.get(key2))) {
					twoMess=RedisUtils.get(keyMess2);
				}
				RedisUtils.del(new String[] { key2, keyMess2 });
			}
			isTwoNow=false;
		}
		if(isTwoNow || (RedisUtils.get(key)!=null && "start".equals(RedisUtils.get(key)))){
			 dto.setHaveDivideIng(true);
		}else{
			 dto.setHaveDivideIng(false);
			 if(StringUtils.isNotBlank(twoMess)) {
				 twoMess="智能2+x分班失败："+twoMess;
			 }
			 if(RedisUtils.get(key)!=null && ("error".equals(RedisUtils.get(key)) ||  "success".equals(RedisUtils.get(key)))){
				if("error".equals(RedisUtils.get(key))){
					//失败消息
					if(StringUtils.isNotBlank(RedisUtils.get(key1))) {
						if(StringUtils.isBlank(twoMess)) {
							twoMess="教学班分班失败："+RedisUtils.get(key1);
						}else {
							twoMess=twoMess+";教学班分班失败："+RedisUtils.get(key1);
						}
					}
					
//					RedisUtils.del(new String[]{key,key1,keyA,keyB});
					RedisUtils.del(new String[]{key,key1,keyType});
				}else if("success".equals(RedisUtils.get(key))){
//					RedisUtils.del(new String[]{key,key1,keyA,keyB});
					RedisUtils.del(new String[]{key,key1,keyType});
				}
			}
			
			dto.setErrorMess(twoMess);
		 }
	}
	
	private void addMapNum(String key,Map<String,Integer> map,int addNum){
		if(map.containsKey(key)){
			map.put(key, map.get(key)+addNum);
		}else{
			map.put(key, addNum);
		}
	}
	private void addMapNum2(String key,String subjectKey,Map<String,Map<String,Integer>> map,int addNum){
		if(map.containsKey(key)){
			Map<String, Integer> map1 = map.get(key);
			addMapNum(subjectKey,map1,addNum);
		}else{
			map.put(key, new HashMap<String,Integer>());
			map.get(key).put(subjectKey, addNum);
		}
	}

	private void copy(NewGkDivide divide,NewGkDivide copyDivide){
		copyDivide.setId(UuidUtils.generateUuid());
		copyDivide.setBatchCountTypea(divide.getBatchCountTypea());
		copyDivide.setBatchCountTypeb(divide.getBatchCountTypeb());
		copyDivide.setChoiceId(divide.getChoiceId());
		copyDivide.setCreationTime(new Date());
		copyDivide.setGalleryful(divide.getGalleryful());
		copyDivide.setGradeId(divide.getGradeId());
		copyDivide.setMaxGalleryful(divide.getMaxGalleryful());
		copyDivide.setIsDefault(NewGkElectiveConstant.IF_INT_0);
		copyDivide.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
		copyDivide.setModifyTime(new Date());
		copyDivide.setOpenType(divide.getOpenType());
		copyDivide.setReferScoreId(divide.getReferScoreId());
		copyDivide.setStat(NewGkElectiveConstant.IF_0);
		copyDivide.setUnitId(divide.getUnitId());
		copyDivide.setFollowType(divide.getFollowType());
//		copyDivide.setDivideName(divideName);
//		copyDivide.setTimes(times);
	}
	
	private void copyOpenSubject(List<NewGkOpenSubject> openSubjectList,List<NewGkOpenSubject> newOpenSubjectList,String divideId){
		NewGkOpenSubject newOpen;
		if(CollectionUtils.isEmpty(openSubjectList)){
			return;
		}
		for(NewGkOpenSubject open:openSubjectList){
			newOpen=new NewGkOpenSubject();
			newOpen.setCreationTime(new Date());
			newOpen.setDivideId(divideId);
			newOpen.setGroupType(open.getGroupType());
			newOpen.setId(UuidUtils.generateUuid());
			newOpen.setModifyTime(new Date());
			newOpen.setSubjectId(open.getSubjectId());
			newOpen.setSubjectType(open.getSubjectType());
			newOpenSubjectList.add(newOpen);
		}
	}
	
	private void copyDivideClass(List<NewGkDivideClass> oldDivideClass,
			List<NewGkDivideClass> newDivideClass,
			List<NewGkClassStudent> newClassStudentList, NewGkDivide divide,
			Map<String,String> oldIdToNewId,boolean isJxbCopy) {
		if(CollectionUtils.isEmpty(oldDivideClass)){
			return;
		}
		NewGkDivideClass newGkDivideClass;
		NewGkClassStudent newGkClassStudent;
		for(NewGkDivideClass cc:oldDivideClass){
			newGkDivideClass=new NewGkDivideClass();
			newGkDivideClass.setId(UuidUtils.generateUuid());
			newGkDivideClass.setDivideId(divide.getId());
			newGkDivideClass.setClassName(cc.getClassName());
			newGkDivideClass.setBestType(cc.getBestType());
			newGkDivideClass.setClassType(cc.getClassType());
			if(NewGkElectiveConstant.CLASS_TYPE_3.equals(cc.getClassType())) {
				//先做复制行政班功能
				newGkDivideClass.setSubjectIdsB(cc.getSubjectIdsB());
				newGkDivideClass.setParentId(oldIdToNewId.get(cc.getParentId()));
				String newIds="";
				String[] ids = cc.getRelateId().split(",");
				for(String s:ids) {
					newIds=newIds+","+oldIdToNewId.get(s);
				}
				newGkDivideClass.setRelateId(newIds.substring(1));
			}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(cc.getClassType())) {
				newGkDivideClass.setSubjectIdsB(cc.getSubjectIdsB());
			}else if(NewGkElectiveConstant.CLASS_TYPE_1.equals(cc.getClassType())) {
				if(StringUtils.isNotBlank(cc.getRelateId())) {
					newGkDivideClass.setRelateId(oldIdToNewId.get(cc.getRelateId()));
				}
			}
			
			//newGkDivideClass.setRelateId(relateId);
			newGkDivideClass.setSubjectIds(cc.getSubjectIds());
			newGkDivideClass.setSubjectIdsB(cc.getSubjectIdsB());
			newGkDivideClass.setBatch(cc.getBatch());
			
			newGkDivideClass.setSubjectType(cc.getSubjectType());
			newGkDivideClass.setCreationTime(new Date());
			newGkDivideClass.setModifyTime(new Date());
			newGkDivideClass.setIsHand(cc.getIsHand());
			newGkDivideClass.setOldClassId(cc.getOldClassId());
			newGkDivideClass.setSourceType(cc.getSourceType());
//			if(isJxbCopy) {
//				newGkDivideClass.setBatch(cc.getBatch());
//			}else {
//				newGkDivideClass.setBatch("");//这个是分班算法后的结果
//			}
			newGkDivideClass.setBatch(cc.getBatch());
			newGkDivideClass.setOrderId(cc.getOrderId());
			newDivideClass.add(newGkDivideClass);
			oldIdToNewId.put(cc.getId(), newGkDivideClass.getId());
			for(String ss:cc.getStudentList()){
				newGkClassStudent=initClassStudent(divide.getUnitId(), divide.getId(), newGkDivideClass.getId(), ss);
				newClassStudentList.add(newGkClassStudent);
			}
		}
		
	}
	
	private void copyGKStudentRangeEx(List<NewGKStudentRangeEx> exList, List<NewGKStudentRangeEx> newRangeExList,
			String divideId, String unitId) {
		if(CollectionUtils.isEmpty(exList)){
			return;
		}
		NewGKStudentRangeEx newGKStudentRangeEx;
		for(NewGKStudentRangeEx rr:exList){
			newGKStudentRangeEx=new NewGKStudentRangeEx();
			newGKStudentRangeEx.setId(UuidUtils.generateUuid());
			newGKStudentRangeEx.setDivideId(divideId);
			newGKStudentRangeEx.setModifyTime(new Date());
			newGKStudentRangeEx.setRange(rr.getRange());
			newGKStudentRangeEx.setSubjectType(rr.getSubjectType());
			newGKStudentRangeEx.setSubjectId(rr.getSubjectId());
			newGKStudentRangeEx.setClassNum(rr.getClassNum());
			newGKStudentRangeEx.setMaximum(rr.getMaximum());
			newGKStudentRangeEx.setLeastNum(rr.getLeastNum());
			newRangeExList.add(newGKStudentRangeEx);
		}
	}
	
	private void copyGKStudentRange(List<NewGKStudentRange> allStuRangeList,
			List<NewGKStudentRange> newStudentRenge, String divideId,String unitId) {
		if(CollectionUtils.isEmpty(allStuRangeList)){
			return;
		}
		NewGKStudentRange newGKStudentRange;
		for(NewGKStudentRange rr:allStuRangeList){
			newGKStudentRange=new NewGKStudentRange();
			newGKStudentRange.setDivideId(divideId);
			newGKStudentRange.setId(UuidUtils.generateUuid());
			newGKStudentRange.setModifyTime(new Date());
			newGKStudentRange.setRange(rr.getRange());
			newGKStudentRange.setStudentId(rr.getStudentId());
			newGKStudentRange.setSubjectType(rr.getSubjectType());
			newGKStudentRange.setSubjectId(rr.getSubjectId());
			newGKStudentRange.setUnitId(unitId);
			newStudentRenge.add(newGKStudentRange);
		}
		 
	}
	@Override
	public void saveCopyDivide(NewGkDivide divide) {
		//分班参数
		NewGkDivide copyDivide=new NewGkDivide();
		copy(divide, copyDivide);
		
		//开设科目
		List<NewGkOpenSubject> newOpenSubjectList=new ArrayList<NewGkOpenSubject>();
		
		List<NewGkOpenSubject> openSubjectList=newGkOpenSubjectService.findByDivideId(divide.getId());
		
		copyOpenSubject(openSubjectList,newOpenSubjectList,copyDivide.getId());
		//班级复制
		//组合班
		List<NewGkDivideClass> newDivideClass=new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> newClassStudentList=new ArrayList<NewGkClassStudent>();
		List<NewGkClassBatch> newClassBath=new ArrayList<>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())) {
				//纯复制
				List<NewGkDivideClass> oldDivideClass = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				Map<String,String> oldIdToNewId=new HashMap<>();
				copyDivideClass(oldDivideClass,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,true);
			}else if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())) {
				List<NewGkDivideClass> oldDivideClass = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				Map<String,String> oldIdToNewId=new HashMap<>();
				List<NewGkDivideClass> xzbClassList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
				copyDivideClass(xzbClassList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
				List<NewGkDivideClass> wlClassList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
				copyDivideClass(wlClassList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
				
				Map<String, NewGkDivideClass> newClassMap = EntityUtils.getMap(newDivideClass, e->e.getId());
				List<Course> courseList=newGkChoRelationService.findChooseSubject(copyDivide.getChoiceId(), copyDivide.getUnitId());
				Map<String, Course> courseMap = EntityUtils.getMap(courseList,e->e.getId());
				//修改名称
				for(NewGkDivideClass cc:wlClassList) {
					String newClazzId = oldIdToNewId.get(cc.getId());
					NewGkDivideClass newClass3 = newClassMap.get(newClazzId);
					Course course1 = courseMap.get(cc.getSubjectIds());
					String subName="物理";
					if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(course1.getSubjectCode())) {
						subName="历史";
					}
					if(newClass3.getRelateId().indexOf(",")>-1) {
						newClass3.setClassName(newClassMap.get(newClass3.getParentId()).getClassName()+"-"+subName+"选");
					}else {
						newClass3.setClassName(subName+"-"+"选"+newClassMap.get(newClass3.getParentId()).getClassName());
					}
				}
				//教学班复制--复制走批次的班级
				List<NewGkDivideClass> jxbClassList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2) && StringUtils.isNotBlank(e.getBatch())).collect(Collectors.toList());
				copyDivideClass(jxbClassList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,true);
				
			}else if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())) {
				List<NewGkDivideClass> oldDivideClass = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_4}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				List<NewGkDivideClass> zhbClassList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_0)).collect(Collectors.toList());
				List<NewGkDivideClass> classList4 = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_4)).collect(Collectors.toList());
				List<NewGkDivideClass> xzbList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
				Map<String,String> oldIdToNewId=new HashMap<>();
				copyDivideClass(zhbClassList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,true);
				//复制xzb
				copyDivideClass(xzbList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
				copyDivideClass(classList4,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
			}else {
				List<NewGkDivideClass> oldDivideClass = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_4,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				Map<String,String> oldIdToNewId=new HashMap<>();
				List<NewGkDivideClass> xzbClassList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
				copyDivideClass(xzbClassList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
				
				List<NewGkDivideClass> wlClassList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
				copyDivideClass(wlClassList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
				
				List<NewGkDivideClass> classList4 = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_4)).collect(Collectors.toList());
				copyDivideClass(classList4,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
			}
			
			
			
		}else {
			List<NewGkDivideClass> oldDivideClass=new ArrayList<NewGkDivideClass>();
			//不重组 行政班数据复制
			Map<String,String> oldIdToNewId=new HashMap<>();
			if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())){
				oldDivideClass = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_1}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				//复制行政班
				copyDivideClass(oldDivideClass,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
			}else{
				//重组 组合班复制
			    oldDivideClass = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				List<NewGkDivideClass> zhbClassList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_0)).collect(Collectors.toList());
				List<NewGkDivideClass> xzbList = oldDivideClass.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
				copyDivideClass(zhbClassList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,true);
				//复制xzb
				copyDivideClass(xzbList,newDivideClass,newClassStudentList,copyDivide,oldIdToNewId,false);
			}
			
			
			
			if((NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())) && oldIdToNewId.size()>0) {
				//手动安排
				//时间点复制
				List<NewGkClassBatch> classbathList = newGkClassBatchService.findListBy("divideId", divide.getId());
				if(CollectionUtils.isNotEmpty(classbathList)) {
					copyNewGkClassBatch(classbathList,copyDivide.getId(),oldIdToNewId,newClassBath);
				}
				//关联的组合班的教学班以及其他A
//				List<NewGkDivideClass> jxbDivideClass=newGkDivideClassService.findListByRelateId(divide.getId(),NewGkElectiveConstant.CLASS_SOURCE_TYPE1,NewGkElectiveConstant.CLASS_TYPE_2,oldIdToNewId.keySet().toArray(new String[] {}));
				List<NewGkDivideClass> jxbDivideClass=newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				if(CollectionUtils.isNotEmpty(jxbDivideClass)) {
//					newGkDivideClassService.toMakeStudentList(jxbDivideClass);
					copyJxbDivideClass(copyDivide,jxbDivideClass,oldIdToNewId,newDivideClass,newClassStudentList);
				}
			}
		}
		
		//取得数据库最大值
		int maxTimes=findMaxByGradeId(divide.getUnitId(), divide.getGradeId(),false);
		//获取学年学期信息
		String semesterJson = semesterRemoteService.getCurrentSemester(2, divide.getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);	
		String divideName = semester.getAcadyear()+"学年"+grade.getGradeName()+
				"第"+semester.getSemester()+"学期分班方案"+(maxTimes+1);
		copyDivide.setTimes(maxTimes+1);
		copyDivide.setDivideName(divideName);
		
		
		List<NewGKStudentRange> newStudentRange=new ArrayList<NewGKStudentRange>();
		List<NewGKStudentRangeEx> newRangeExList=new ArrayList<>();
		List<NewGkDivideEx> newExList=new ArrayList<NewGkDivideEx>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType()) ||
				NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())){
			//分层数据复制
			List<NewGKStudentRange> allStuRangeList = newGKStudentRangeService.findByDivideId(divide.getUnitId(), divide.getId());
			copyGKStudentRange(allStuRangeList,newStudentRange,copyDivide.getId(),divide.getUnitId());
			List<NewGKStudentRangeEx> exList = newGKStudentRangeExService.findByDivideId(divide.getId());
			copyGKStudentRangeEx(exList,newRangeExList,copyDivide.getId(),divide.getUnitId());
			
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_03.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_04.equals(divide.getOpenType())){
			//学生分层参数复制
			List<NewGkDivideEx> exList = newGkDivideExService.findByDivideId(divide.getId());
			copyNewGkDivideEx(exList,newExList,copyDivide.getId());
		}
		
	
		
		
		
		List<NewGkDivideStusub> newStuSubList = null;
		if(NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType()) ||
				NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			List<NewGkDivideStusub> stuSubList = newGkDivideStusubService.findByDivideIdWithMaster(divide.getId(), null,null);
			if(CollectionUtils.isNotEmpty(stuSubList)) {
				newStuSubList = EntityUtils.copyProperties(stuSubList, NewGkDivideStusub.class, NewGkDivideStusub.class);
				Date now = new Date();
				newStuSubList.forEach(e->{
					e.setId(UuidUtils.generateUuid());
					e.setDivideId(copyDivide.getId());
					e.setChoiceId(divide.getChoiceId());
					e.setUnitId(divide.getUnitId());
					e.setCreationTime(now);
					e.setModifyTime(now);
				});
			}
		}
		
		//保存
		save(copyDivide);
		if(CollectionUtils.isNotEmpty(newClassBath)) {
			newGkClassBatchService.saveAll(newClassBath.toArray(new NewGkClassBatch[] {}));
		}
		newGkDivideClassService.saveAllList(null, null, null, newDivideClass, newClassStudentList, false);
		if(CollectionUtils.isNotEmpty(newOpenSubjectList)){
			newGkOpenSubjectService.saveAllEntity(newOpenSubjectList);
		}
		if(CollectionUtils.isNotEmpty(newStudentRange)){
			newGKStudentRangeService.saveAll(newStudentRange.toArray(new NewGKStudentRange[0]));
		}
		if(CollectionUtils.isNotEmpty(newExList)){
			newGkDivideExService.saveAll(newExList.toArray(new NewGkDivideEx[0]));
		}
		if(CollectionUtils.isNotEmpty(newStuSubList)){
			newGkDivideStusubService.saveAll(newStuSubList.toArray(new NewGkDivideStusub[0]));
		}
		if(CollectionUtils.isNotEmpty(newRangeExList)) {
			newGKStudentRangeExService.saveAll(newRangeExList.toArray(new NewGKStudentRangeEx[0]));
		}
	}


	private void copyJxbDivideClass(NewGkDivide divide,List<NewGkDivideClass> jxbDivideClass, Map<String, String> oldIdToNewId,
			List<NewGkDivideClass> newDivideClass, List<NewGkClassStudent> newClassStudentList) {
		NewGkDivideClass newGkDivideClass;
		NewGkClassStudent newGkClassStudent;
		for(NewGkDivideClass clazz:jxbDivideClass) {
//			if(!NewGkElectiveConstant.SUBJECT_TYPE_A.equals(clazz.getSubjectType())) {
//				continue;
//			}
			newGkDivideClass=new NewGkDivideClass();
			newGkDivideClass.setId(UuidUtils.generateUuid());
			newGkDivideClass.setDivideId(divide.getId());
			newGkDivideClass.setClassName(clazz.getClassName());
			newGkDivideClass.setBestType(clazz.getBestType());
			newGkDivideClass.setClassType(clazz.getClassType());
			if(oldIdToNewId.containsKey(clazz.getRelateId())) {
				newGkDivideClass.setRelateId(oldIdToNewId.get(clazz.getRelateId()));
			}
			newGkDivideClass.setSubjectIds(clazz.getSubjectIds());
			newGkDivideClass.setSubjectType(clazz.getSubjectType());
			newGkDivideClass.setCreationTime(new Date());
			newGkDivideClass.setModifyTime(new Date());
			newGkDivideClass.setIsHand(clazz.getIsHand());
			newGkDivideClass.setOldClassId(clazz.getOldClassId());
			newGkDivideClass.setSourceType(clazz.getSourceType());
			newGkDivideClass.setBatch(clazz.getBatch());//这个是分班算法后的结果
			newGkDivideClass.setOrderId(clazz.getOrderId());
			newDivideClass.add(newGkDivideClass);
			for(String ss:clazz.getStudentList()){
				newGkClassStudent=initClassStudent(divide.getUnitId(), divide.getId(), newGkDivideClass.getId(), ss);
				newClassStudentList.add(newGkClassStudent);
			}
		}
		
	}

	private void copyNewGkClassBatch(List<NewGkClassBatch> classbathList, String newDivideId, Map<String, String> oldIdToNewId,
			List<NewGkClassBatch> newClassBath) {
		NewGkClassBatch newBath;
		for(NewGkClassBatch batch:classbathList) {
			if(!oldIdToNewId.containsKey(batch.getDivideClassId())) {
				continue;
			}
			newBath=new NewGkClassBatch();
			newBath.setId(UuidUtils.generateUuid());
			newBath.setBatch(batch.getBatch());
			newBath.setDivideClassId(oldIdToNewId.get(batch.getDivideClassId()));
			newBath.setDivideId(newDivideId);
			newBath.setSubjectId(batch.getSubjectId());
			newBath.setSubjectIds(batch.getSubjectIds());
			newBath.setSubjectType(batch.getSubjectType());
			newBath.setUnitId(batch.getUnitId());
			newClassBath.add(newBath);
		}
	}

	private void copyNewGkDivideEx(List<NewGkDivideEx> exList,
			List<NewGkDivideEx> newExList, String divideId) {
		if(CollectionUtils.isEmpty(exList)){
			return;
		}
		NewGkDivideEx newGkDivideEx;
		for(NewGkDivideEx ee:exList){
			newGkDivideEx=new NewGkDivideEx();
			newGkDivideEx.setId(UuidUtils.generateUuid());
			newGkDivideEx.setDivideId(divideId);
			newGkDivideEx.setHierarchyScore(ee.getHierarchyScore());
			newGkDivideEx.setHierarchyType(ee.getHierarchyType());
			newGkDivideEx.setClassSumNum(ee.getClassSumNum());
			newGkDivideEx.setGroupType(ee.getGroupType());
			newGkDivideEx.setCreationTime(new Date());
			newGkDivideEx.setModifyTime(new Date());
			newGkDivideEx.setSubjectType(ee.getSubjectType());
			newExList.add(newGkDivideEx);
		}
	}

	

	@Override
	public void saveCombineDivide(NewGkDivide divide1,NewGkDivide divide2,NewGkChoice choice){
		//分班参数
		NewGkDivide copyDivide=new NewGkDivide();
		copy(divide1, copyDivide);
		copyDivide.setChoiceId(choice.getId());
		copyDivide.setStat(NewGkElectiveConstant.IF_1);
		if(divide1.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_06) 
				|| divide2.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_06) ) {
			copyDivide.setOpenType(NewGkElectiveConstant.DIVIDE_TYPE_06);
		}else {
			copyDivide.setOpenType(NewGkElectiveConstant.DIVIDE_TYPE_02);
		}
		//批次数取最大值
		if(divide2.getBatchCountTypea()>copyDivide.getBatchCountTypea()) {
			copyDivide.setBatchCountTypea(divide2.getBatchCountTypea());
		}
		if(divide2.getBatchCountTypeb()>copyDivide.getBatchCountTypeb()) {
			copyDivide.setBatchCountTypeb(divide2.getBatchCountTypeb());
		}
		Map<String,Integer> bathSize=new HashMap<String,Integer>();
		
		int bathA1 = divide1.getBatchCountTypea();
		int bathA2 = divide2.getBatchCountTypea();
		bathSize.put(divide1.getId()+"A", bathA1);
		bathSize.put(divide2.getId()+"A", bathA2);
		
		int maxA=bathA1>bathA2?bathA1:bathA2;
		
		//开设科目--暂时默认divide1与divide2一样
		List<NewGkOpenSubject> newOpenSubjectList=new ArrayList<NewGkOpenSubject>();
		
		List<NewGkOpenSubject> openSubjectList=newGkOpenSubjectService.findByDivideId(divide1.getId());
		Set<String> subIds = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[] {})), new TR<List<Course>>() {});
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		
		copyOpenSubject(openSubjectList,newOpenSubjectList,copyDivide.getId());
		
		//key:'xzb' 或者 subjectIds 或者 subject_A
		Map<String,Integer> classNumMap=new HashMap<String,Integer>();
		List<NewGkDivideClass> insertDivideClass=new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> insertClassStudentList=new ArrayList<NewGkClassStudent>();
		
		//只复制教学班，行政班，以及组合班数据（组合班空余的批次数？--有问题）
		List<NewGkDivideClass> divideList1 = newGkDivideClassService.findByDivideIdAndClassType(divide1.getUnitId(), divide1.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> divideList2 = newGkDivideClassService.findByDivideIdAndClassType(divide2.getUnitId(), divide2.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		
		List<NewGkDivideClass> divideListAll=new ArrayList<NewGkDivideClass>();
		divideListAll.addAll(divideList1);
		divideListAll.addAll(divideList2);
		//old:就组合数据与行政班关联数据，key:xzbId,value:zhbId
		//old:key:教学班id value:zhbId
		Map<String,String> relatekeyTokey=new HashMap<>();
		//oldId--newId
		Map<String,String> newIdToOldclassId=new HashMap<>();
		Map<String,NewGkDivideClass> newXzbAndJxb=new HashMap<>();
		
		NewGkDivideClass newGkDivideClass;
		NewGkClassStudent newGkClassStudent;
		for(NewGkDivideClass item:divideListAll) {
			newGkDivideClass=new NewGkDivideClass();
			newGkDivideClass.setId(UuidUtils.generateUuid());
			newGkDivideClass.setDivideId(copyDivide.getId());
			newGkDivideClass.setBestType(item.getBestType());
			newGkDivideClass.setClassType(item.getClassType());
			newGkDivideClass.setSubjectIds(item.getSubjectIds());
			newGkDivideClass.setSubjectType(item.getSubjectType());
			newGkDivideClass.setCreationTime(new Date());
			newGkDivideClass.setModifyTime(new Date());
			newGkDivideClass.setIsHand(item.getIsHand());
			newGkDivideClass.setOldClassId(item.getOldClassId());
			newGkDivideClass.setSourceType(item.getSourceType());
			
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getClassType())) {
				//教学班
				newGkDivideClass.setBatch(item.getBatch());
				String subName=courseMap.get(item.getSubjectIds())==null?"":courseMap.get(item.getSubjectIds()).getSubjectName();
				String key2=item.getSubjectIds()+"_"+item.getSubjectType();
				if(!classNumMap.containsKey(key2)) {
					classNumMap.put(key2, 1);
				}
				int ii=classNumMap.get(key2);
				if(StringUtils.isNotBlank(item.getBestType())) {
					subName=subName+item.getSubjectType()+"("+item.getBestType()+")"+ii+"班";
				}else {
					subName=subName+item.getSubjectType()+ii+"班";
				}

				newGkDivideClass.setClassName(subName);
				newGkDivideClass.setOrderId(ii);
				classNumMap.put(key2, ii+1);
				
				
				if(StringUtils.isNotBlank(item.getRelateId())) {
					relatekeyTokey.put(item.getId(), item.getRelateId());
					newIdToOldclassId.put(item.getId(), newGkDivideClass.getId());
					newXzbAndJxb.put(newGkDivideClass.getId(), newGkDivideClass);
				}
				
			}else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(item.getClassType())) {
				newGkDivideClass.setSubjectIdsB(item.getSubjectIdsB());
				//组合班
				int a=bathSize.get(item.getDivideId()+"A");
				String ss=item.getBatch()==null?"":item.getBatch();
				if(maxA>a) {
					for(int iii=a+1;iii<=maxA;iii++) {
						ss=ss+","+iii;
					}
					if(ss.startsWith(",")) {
						ss=ss.substring(1);
					}
				}
				newGkDivideClass.setBatch(ss);
				String subName="";

				String key2=item.getSubjectIds();
				if(!classNumMap.containsKey(key2)) {
					classNumMap.put(key2, 1);
				}
				int ii=classNumMap.get(key2);
				
				if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(item.getSubjectType())) {
					subName="混合";
					subName=subName+ii+"班";
				}else {
					subName=nameSet(courseMap, item.getSubjectIds());
					subName=subName+ii+"班";
				}
				

				newGkDivideClass.setClassName(subName);
				newGkDivideClass.setOrderId(ii);
				classNumMap.put(key2, ii+1);
				
				newIdToOldclassId.put(item.getId(), newGkDivideClass.getId());
			}else {
				newGkDivideClass.setBatch("");
				
				String key2="xzb";
				if(!classNumMap.containsKey(key2)) {
					classNumMap.put(key2, 1);
				}
				int ii=classNumMap.get(key2);
				
				newGkDivideClass.setClassName(ii+"班");
				newGkDivideClass.setOrderId(ii);
				classNumMap.put(key2, ii+1);
				
				newXzbAndJxb.put(newGkDivideClass.getId(), newGkDivideClass);
				newIdToOldclassId.put(item.getId(), newGkDivideClass.getId());
				if(StringUtils.isNotBlank(item.getRelateId())) {
					relatekeyTokey.put(item.getId(), item.getRelateId());
				}
			}
			
			//newGkDivideClass.setRelateId(relateId);
			
			insertDivideClass.add(newGkDivideClass);
		
			for(String ss:item.getStudentList()){
				
				newGkClassStudent=initClassStudent(copyDivide.getUnitId(), copyDivide.getId(), newGkDivideClass.getId(), ss);
				
				insertClassStudentList.add(newGkClassStudent);
			}
		}
		
		if(relatekeyTokey.size()>0) {
			for (Entry<String, String> kk:relatekeyTokey.entrySet()) {
				String oldId=kk.getKey();
				String oldRelateId=kk.getValue();
				String newId=newIdToOldclassId.get(oldId);
				String newRelateId=newIdToOldclassId.get(oldRelateId);
				if(newXzbAndJxb.containsKey(newId) && StringUtils.isNotBlank(newRelateId)) {
					newXzbAndJxb.get(newId).setRelateId(newRelateId);
				}
			}
		}
		
		//取得数据库最大值
		int maxTimes=findMaxByGradeId(copyDivide.getUnitId(), copyDivide.getGradeId(),false);
		//获取学年学期信息
		String semesterJson = semesterRemoteService.getCurrentSemester(2, copyDivide.getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(copyDivide.getGradeId()), Grade.class);	
		String divideName = semester.getAcadyear()+"学年"+grade.getGradeName()+
				"第"+semester.getSemester()+"学期合并分班方案"+(maxTimes+1);
		copyDivide.setTimes(maxTimes+1);
		copyDivide.setDivideName(divideName);
		
		//保存
		save(copyDivide);
		
		newGkDivideClassService.saveAllList(null, null, null, insertDivideClass, insertClassStudentList, false);
		if(CollectionUtils.isNotEmpty(newOpenSubjectList)){
			newGkOpenSubjectService.saveAllEntity(newOpenSubjectList);
		}
		
	}
	
	private String nameSet(Map<String, Course> courseMap, String ids) {
		String[] s = ids.split(",");
		Arrays.sort(s);
		String returnS = "";
		for (String s1 : s) {
			returnS = returnS
					+ StringUtils.trimToEmpty(courseMap.get(s1) == null ? ""
							: courseMap.get(s1).getShortName());// 简称
		}
		return returnS;
	}
	
	
	

	public void saveCombineDivideList(List<NewGkDivide> divideList,NewGkChoice choice){
		//分班参数
		NewGkDivide divide1 = divideList.get(0);
		NewGkDivide copyDivide=new NewGkDivide();
		
		copy(divide1, copyDivide);
		copyDivide.setChoiceId(choice.getId());
		copyDivide.setStat(NewGkElectiveConstant.IF_1);
		String openType=null;
		int bathAMax=0;
		int bathBMax=0;
		Map<String,Integer> bathSizeMap=new HashMap<>();
		//批次数取最大值
		for(NewGkDivide item:divideList) {
			if(item.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_06)) {
				if(StringUtils.isBlank(openType)) {
					openType=NewGkElectiveConstant.DIVIDE_TYPE_06;
				}
			}
			if(bathAMax==0) {
				bathAMax=item.getBatchCountTypea();
			}else {
				if(item.getBatchCountTypea()>bathAMax) {
					bathAMax=item.getBatchCountTypea();
				}
			}
			if(bathBMax==0) {
				bathBMax=item.getBatchCountTypeb();
			}else {
				if(item.getBatchCountTypeb()>bathBMax) {
					bathBMax=item.getBatchCountTypeb();
				}
			}
			bathSizeMap.put(item.getId()+"A", item.getBatchCountTypea());
			bathSizeMap.put(item.getId()+"B", item.getBatchCountTypeb());
			
		}
		if(StringUtils.isBlank(openType)) {
			openType=NewGkElectiveConstant.DIVIDE_TYPE_02;
		}
		copyDivide.setOpenType(openType);
		copyDivide.setBatchCountTypea(bathAMax);
		copyDivide.setBatchCountTypeb(bathBMax);
		
		
		//开设科目--暂时默认divideList中都一样
		List<NewGkOpenSubject> newOpenSubjectList=new ArrayList<NewGkOpenSubject>();
		
		List<NewGkOpenSubject> openSubjectList=newGkOpenSubjectService.findByDivideId(divide1.getId());
		Set<String> subIds = EntityUtils.getSet(openSubjectList,NewGkOpenSubject::getSubjectId);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[] {})), new TR<List<Course>>() {});
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		
		copyOpenSubject(openSubjectList,newOpenSubjectList,copyDivide.getId());
		
		//key:'xzb' 或者 subjectIds 或者 subject_A
		Map<String,Integer> classNumMap=new HashMap<String,Integer>();
		List<NewGkDivideClass> insertDivideClass=new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> insertClassStudentList=new ArrayList<NewGkClassStudent>();
		List<NewGkDivideClass> divideListAll=new ArrayList<NewGkDivideClass>();
		for(NewGkDivide cc: divideList) {
			//只复制教学班，行政班，以及组合班数据（组合班空余的批次数）
			List<NewGkDivideClass> divideList1 = newGkDivideClassService.findByDivideIdAndClassType(cc.getUnitId(), cc.getId(), new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			divideListAll.addAll(divideList1);
		}
		

		//old:就组合数据与行政班关联数据，key:xzbId,value:zhbId
		//old:key:教学班id value:zhbId
		Map<String,String> relatekeyTokey=new HashMap<>();
		//oldId--newId
		Map<String,String> newIdToOldclassId=new HashMap<>();
		Map<String,NewGkDivideClass> newXzbAndJxb=new HashMap<>();
		
		NewGkDivideClass newGkDivideClass;
		NewGkClassStudent newGkClassStudent;
		for(NewGkDivideClass item:divideListAll) {
			newGkDivideClass=new NewGkDivideClass();
			newGkDivideClass.setId(UuidUtils.generateUuid());
			newGkDivideClass.setDivideId(copyDivide.getId());
			newGkDivideClass.setBestType(item.getBestType());
			newGkDivideClass.setClassType(item.getClassType());
			newGkDivideClass.setSubjectIds(item.getSubjectIds());
			newGkDivideClass.setSubjectType(item.getSubjectType());
			newGkDivideClass.setCreationTime(new Date());
			newGkDivideClass.setModifyTime(new Date());
			newGkDivideClass.setIsHand(item.getIsHand());
			newGkDivideClass.setOldClassId(item.getOldClassId());
			newGkDivideClass.setSourceType(item.getSourceType());
			
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getClassType())) {
				//教学班
				newGkDivideClass.setBatch(item.getBatch());
				String subName=courseMap.get(item.getSubjectIds())==null?"":courseMap.get(item.getSubjectIds()).getSubjectName();
				String key2=item.getSubjectIds()+"_"+item.getSubjectType();
				if(!classNumMap.containsKey(key2)) {
					classNumMap.put(key2, 1);
				}
				int ii=classNumMap.get(key2);
				if(StringUtils.isNotBlank(item.getBestType())) {
					subName=subName+item.getSubjectType()+"("+item.getBestType()+")"+ii+"班";
				}else {
					subName=subName+item.getSubjectType()+ii+"班";
				}

				newGkDivideClass.setClassName(subName);
				newGkDivideClass.setOrderId(ii);
				classNumMap.put(key2, ii+1);
				
				
				if(StringUtils.isNotBlank(item.getRelateId())) {
					relatekeyTokey.put(item.getId(), item.getRelateId());
					newIdToOldclassId.put(item.getId(), newGkDivideClass.getId());
					newXzbAndJxb.put(newGkDivideClass.getId(), newGkDivideClass);
				}
				
			}else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(item.getClassType())) {
				//组合班
				int a=bathSizeMap.get(item.getDivideId()+"A");
				String ss=item.getBatch()==null?"":item.getBatch();
				if(bathAMax>a) {
					for(int iii=a+1;iii<=bathAMax;iii++) {
						ss=ss+","+iii;
					}
					if(ss.startsWith(",")) {
						ss=ss.substring(1);
					}
				}
				newGkDivideClass.setBatch(ss);
				String subName="";

				String key2=item.getSubjectIds();
				if(!classNumMap.containsKey(key2)) {
					classNumMap.put(key2, 1);
				}
				int ii=classNumMap.get(key2);
				
				if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(item.getSubjectIds())) {
					subName="混合";
					subName=subName+ii+"班";
				}else {
					subName=nameSet(courseMap, item.getSubjectIds());
					subName=subName+ii+"班";
				}
				

				newGkDivideClass.setClassName(subName);
				newGkDivideClass.setOrderId(ii);
				classNumMap.put(key2, ii+1);
				
				newIdToOldclassId.put(item.getId(), newGkDivideClass.getId());
			}else {
				newGkDivideClass.setBatch("");
				
				String key2="xzb";
				if(!classNumMap.containsKey(key2)) {
					classNumMap.put(key2, 1);
				}
				int ii=classNumMap.get(key2);
				
				newGkDivideClass.setClassName(ii+"班");
				newGkDivideClass.setOrderId(ii);
				classNumMap.put(key2, ii+1);
				
				newXzbAndJxb.put(newGkDivideClass.getId(), newGkDivideClass);
				newIdToOldclassId.put(item.getId(), newGkDivideClass.getId());
				if(StringUtils.isNotBlank(item.getRelateId())) {
					relatekeyTokey.put(item.getId(), item.getRelateId());
				}
			}
			
			//newGkDivideClass.setRelateId(relateId);
			
			insertDivideClass.add(newGkDivideClass);
		
			for(String ss:item.getStudentList()){
				newGkClassStudent=initClassStudent(copyDivide.getUnitId(), copyDivide.getId(), newGkDivideClass.getId(), ss);
				insertClassStudentList.add(newGkClassStudent);
			}
		}
		
		if(relatekeyTokey.size()>0) {
			for (Entry<String, String> kk:relatekeyTokey.entrySet()) {
				String oldId=kk.getKey();
				String oldRelateId=kk.getValue();
				String newId=newIdToOldclassId.get(oldId);
				String newRelateId=newIdToOldclassId.get(oldRelateId);
				if(newXzbAndJxb.containsKey(newId) && StringUtils.isNotBlank(newRelateId)) {
					newXzbAndJxb.get(newId).setRelateId(newRelateId);
				}
			}
		}
		
		//取得数据库最大值
		int maxTimes=findMaxByGradeId(copyDivide.getUnitId(), copyDivide.getGradeId(),false);
		//获取学年学期信息
		String semesterJson = semesterRemoteService.getCurrentSemester(2, copyDivide.getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(copyDivide.getGradeId()), Grade.class);	
		String divideName = semester.getAcadyear()+"学年"+grade.getGradeName()+
				"第"+semester.getSemester()+"学期合并分班方案"+(maxTimes+1);
		copyDivide.setTimes(maxTimes+1);
		copyDivide.setDivideName(divideName);
		
		//保存
		save(copyDivide);
		
		newGkDivideClassService.saveAllList(null, null, null, insertDivideClass, insertClassStudentList, false);
		if(CollectionUtils.isNotEmpty(newOpenSubjectList)){
			newGkOpenSubjectService.saveAllEntity(newOpenSubjectList);
		}
		
	}
	
	public NewGkClassStudent initClassStudent(String unitId,String divideId,String classId,String studentId) {
		NewGkClassStudent item=new NewGkClassStudent();
		item.setId(UuidUtils.generateUuid());
		item.setUnitId(unitId);
		item.setUnitId(unitId);
		item.setDivideId(divideId);
		item.setClassId(classId);
		item.setStudentId(studentId);
		item.setModifyTime(new Date());
		item.setCreationTime(new Date());
		return item;
	}

	@Override
	public List<NewGkDivide> findListByOpenTypeAndGradeIdIn(String unitId, String openType, String[] gradeIds) {
		if(gradeIds==null || gradeIds.length==0) {
			return newGkDivideDao.findByOpenType(unitId, openType);
		}
		return newGkDivideDao.findByOpenTypeAndGradeIdIn(unitId, openType, gradeIds);
	}
	@Override
	public List<NewGkDivide> findListByOpenTypeAndGradeIdInWithMaster(String unitId, String openType, String[] gradeIds) {
		return findListByOpenTypeAndGradeIdIn(unitId, openType, gradeIds);
	}

    @Override
    public void deleteByGradeIds(String... gradeIds) {
        newGkDivideDao.deleteByGradeIds(new Date(), gradeIds);
    }

    @Override
    public void showDivideXzbList(NewGkDivide divide, String fromSolve, String arrayId, ModelMap map) {

        List<NewGkDivideClass> classList;
	    if (StringUtils.isNotBlank(arrayId)) {
            classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(divide.getUnitId(),
                    arrayId,
                    new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
        } else {
            classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(divide.getUnitId(),
                    divide.getId(),
                    new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        }
//			List<String> classIds = EntityUtils.getList(classList, "id");
//		Set<String> studentIds = classList.stream().map(e->e.getStudentList()).flatMap(e->e.stream()).collect(Collectors.toSet());
		//不选用根据id去查学生信息
		List<Student> studentList =SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(),divide.getGradeId(),null, null), Student.class);
		//List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[] {})), Student.class);
		Map<String, Student> studentMap = EntityUtils.getMap(studentList,Student::getId);
		
		List<NewGkDivideClass> xzbClassList = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.sorted((x,y)->{
					if(x.getOrderId()==null){
						return 1;
					}else if(y.getOrderId()==null){
						return -1;
					}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
						return x.getOrderId().compareTo(y.getOrderId());
					}
					return x.getClassName().compareTo(y.getClassName());
				})
				.collect(Collectors.toList());
		List<NewGkDivideClass> zhbClassList = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()))
				.collect(Collectors.toList());
//			List<NewGkDivideClass> jxbClassList = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
//					.collect(Collectors.toList());
		
		
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbClassList, NewGkDivideClass::getId);
		
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		String manCodet = "";
		String woManCodet ="";
		for (String thisId : codeMap.keySet()) {
			if("男".equals(codeMap.get(thisId).getMcodeContent())) {
				manCodet = thisId;
			}
			if("女".equals(codeMap.get(thisId).getMcodeContent())) {
				woManCodet = thisId;
			}
		}
		final String manCode = manCodet;
		final String woManCode = woManCodet;
		
		NewGkDivideClass zhbClass;
		List<Student> studentsT;
		int manCount;
		int woManCount;
		List<NewGkDivideClassDto> dtoList = new ArrayList<>();
		NewGkDivideClassDto dto;
		for (NewGkDivideClass xzbClass : xzbClassList) {
			zhbClass = null;
			if(xzbClass.getRelateId() != null && zhbMap.get(xzbClass.getRelateId()) != null) {
				zhbClass = zhbMap.get(xzbClass.getRelateId());
			}
			
			String aClassNames = "/";
			String bClassNames = "/";
			if(zhbClass != null) {
				xzbClass.setRelateName(zhbClass.getClassName());
			}
			studentsT = xzbClass.getStudentList().stream().map(e->studentMap.get(e)).collect(Collectors.toList());
			
			manCount = (int)studentsT.stream().filter(e->e!=null && (""+e.getSex()).equals(manCode)).count();
			woManCount = (int)studentsT.stream().filter(e->e!=null && (""+e.getSex()).equals(woManCode)).count();
			
			xzbClass.setStudentCount(xzbClass.getStudentList().size());
			xzbClass.setBoyCount(manCount);
			xzbClass.setGirlCount(woManCount);
			
			dto = new NewGkDivideClassDto();
			dto.setEvn(xzbClass);
			dto.setClassNames(new String[] {aClassNames,bClassNames});
			dtoList.add(dto);
		}

		map.put("dtoList", dtoList);
		map.put("divideId", divide.getId());
    }

    @Override
    public void divideXzbVirtualCount(NewGkDivide divide, String fromSolve, String arrayId, ModelMap map, String type){
		List<NewGkDivideClass> divideClassList;
		if (StringUtils.isNotBlank(arrayId)) {
			divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), arrayId,
					new String[] {}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		} else {
			divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(),
					new String[]{}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		}
		Map<String, String> clsNameMap = EntityUtils.getMap(divideClassList, e -> e.getId(), e -> e.getClassName());
		List<NewGkDivideClass> jxbClassList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
		List<NewGkDivideClass> xzbList = divideClassList.stream()
				.sorted((x,y)->{
					if(x.getOrderId()==null){
						return 1;
					}else if(y.getOrderId()==null){
						return -1;
					}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
						return x.getOrderId().compareTo(y.getOrderId());
					}
					return x.getClassName().compareTo(y.getClassName());
				})
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).collect(Collectors.toList());

		Map<String,List<NewGkDivideClass>> subXzbMap = new HashMap<>();
		for (NewGkDivideClass xzb : xzbList) {
			if(StringUtils.isNotBlank(xzb.getSubjectIds())) {
				//TODO 行政班排课 虚拟课程 在行政班上课的虚拟课程科目
				String[] subIds = xzb.getSubjectIds().split(",");
				Stream.of(subIds).forEach(sub->subXzbMap.computeIfAbsent(sub,e->new ArrayList<>()).add(xzb));
			}
		}

		Set<String> virIds = new HashSet<>();
		for (NewGkDivideClass jxb : jxbClassList) {
			String[] split = jxb.getBatch().split("-");
			String virId = split[0];
			virIds.add(virId);
		}

		// 统计某一科目所有的班级 包括 教学班和行政班
		Map<String, List<NewGkDivideClass>> subjectClassMap = jxbClassList.stream().collect(Collectors.groupingBy(e->e.getSubjectIds()));

		Set<String> subjectIdArr = new HashSet<>(subjectClassMap.keySet());
		subjectIdArr.addAll(virIds);
		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIdArr.toArray(new String[0]));
		Map<String, String> coureNameMap = EntityUtils.getMap(courseList,e->e.getId(),e->e.getSubjectName());
		Map<String, Integer> coureOrderIdMap = EntityUtils.getMap(courseList,e->e.getId(),e->e.getOrderId());


		List<String> virList = virIds.stream().peek(e -> {
			if (!coureNameMap.containsKey(e)) {
				coureNameMap.put(e, "未知科目");
			}
		}).sorted(Comparator.comparing(coureOrderIdMap::get)).collect(Collectors.toList());
		map.put("virList",virList);
		map.put("coureNameMap",coureNameMap);

		List<DivideResultDto> dtoList = new ArrayList<>();
		DivideResultDto dto;
//		String courseName = null;
		List<NewGkDivideClass> newList;
		for (String subjectIds2 : subjectClassMap.keySet()) {
			List<NewGkDivideClass> alist = subjectClassMap.get(subjectIds2);

			Integer aStuNum = (int) alist.stream().flatMap(e->e.getStudentList().stream()).distinct().count();

			dto = new DivideResultDto();
			dto.setCourseName(coureNameMap.get(subjectIds2));
			dto.setTotalNum(alist.size());

			dto.setaClassNum(alist.size());

			dto.setaStuNum(aStuNum);

			// 加上行政班的 班级
			newList = new ArrayList<>(alist);
			dto.setNormalClassList(newList);
			dto.setBatchClassListMap(new HashMap<>());
			Map<String, List<NewGkDivideClass>> bathListMap = dto.getBatchClassListMap();
			Collections.sort(newList, (x,y)->compareClassName(x.getClassName(), y.getClassName()));
			for (NewGkDivideClass dc : newList) {
				String[] split = dc.getBatch().split("-");
				String virId = split[0];
				bathListMap.computeIfAbsent(virId,e->new ArrayList<>()).add(dc);
			}
			if(subXzbMap.containsKey(subjectIds2)){
				dto.setAsXzbClassList(subXzbMap.get(subjectIds2));
			}
			dtoList.add(dto);
		}
		map.put("dtoList", dtoList);
		map.put("coureNameMap", coureNameMap);
		map.put("openType", divide.getOpenType());
	}

    @Override
    public void divideResultCount(NewGkDivide divide, String fromSolve, String arrayId, ModelMap map, String type) {

        List<NewGkDivideClass> divideClassList;
	    if ("1".equals(fromSolve)) {
            divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), arrayId,
                    new String[] {}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
        } else {
            divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(),
                    new String[]{}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        }
		List<NewGkDivideClass> jxbClassList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
		if(type != null) {
			jxbClassList = jxbClassList.stream().filter(e->type.equals(e.getSubjectType())).collect(Collectors.toList());
		}
		
		List<NewGkOpenSubject> subjectByDivideList = newGkOpenSubjectService.findByDivideId(divide.getId());
		Set<String> aSubjectId=new HashSet<String>();
		Set<String> bSubjectId=new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();
		for(NewGkOpenSubject open:subjectByDivideList){
			subjectIds.add(open.getSubjectId());
			if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(open.getSubjectType())){
//				xzbSubjectId.add(open.getSubjectId());
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(open.getSubjectType())){
				aSubjectId.add(open.getSubjectId());			
			}else{
				bSubjectId.add(open.getSubjectId());
			}
		}
		
		// 统计某一科目所有的班级 包括 教学班和行政班
		Map<String, List<NewGkDivideClass>> subjectClassMap = jxbClassList.stream().collect(Collectors.groupingBy(e->e.getSubjectIds()));
		Map<String,Map<String, List<NewGkDivideClass>>> subjectIdTypeClassMap = new HashMap<>();
		for (String subjectId : subjectClassMap.keySet()) {
			List<NewGkDivideClass> list = subjectClassMap.get(subjectId);
			List<NewGkDivideClass> aList = list.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_A.equals(e.getSubjectType())).collect(Collectors.toList());
			List<NewGkDivideClass> bList = list.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_B.equals(e.getSubjectType())).collect(Collectors.toList());
			Map<String, List<NewGkDivideClass>> map2 = new HashMap<>();
			map2.put(NewGkElectiveConstant.SUBJECT_TYPE_A, aList);
			map2.put(NewGkElectiveConstant.SUBJECT_TYPE_B, bList);
			
			subjectIdTypeClassMap.put(subjectId, map2);
		}
		
		
		Map<String, NewGkDivideClass> divideClassMap = EntityUtils.getMap(divideClassList, NewGkDivideClass::getId);
		Map<String, List<NewGkDivideClass>> relatedJxbClassMap = jxbClassList.stream()
				.filter(e->StringUtils.isNotBlank(e.getRelateId()))
				.collect(Collectors.groupingBy(e->e.getRelateId()));
		NewGkDivideClass zhbClass;
//		String[] subjectIdsT;
		Set<String> setT;
		Set<String> relatedSubjectIds = null;
		for (NewGkDivideClass clazz : divideClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType()) && StringUtils.isNotBlank(clazz.getRelateId())
					&& divideClassMap.containsKey(clazz.getRelateId())) {
				// 组合班
				zhbClass = divideClassMap.get(clazz.getRelateId());
				String[] subjectIdsT = zhbClass.getSubjectIds().split(",");
				String subjectIdsB = zhbClass.getSubjectIdsB();
				if(subjectIdsT.length > 0) {
					List<String> subjectIdsTList = new ArrayList<String>();
					if(!BaseConstants.ZERO_GUID.equals(subjectIdsT[0])){
						subjectIdsTList = Arrays.asList(subjectIdsT);
					}
					setT = Arrays.stream(subjectIdsT).filter(e->aSubjectId.contains(e))
							.collect(Collectors.toSet());
					
					relatedSubjectIds = null;
					/*
					if(subjectIdsT.length > 2) {
						setT.addAll(bSubjectId.stream()
								.filter(e->!Arrays.asList(subjectIdsT).contains(e))
								.collect(Collectors.toSet()));
						
					}
					*/
					if(StringUtils.isNotBlank(subjectIdsB)){
						setT.addAll(bSubjectId.stream()
								.filter(e->subjectIdsB.contains(e))
								.collect(Collectors.toSet()));
					}
					List<NewGkDivideClass> relatedJxbs = relatedJxbClassMap.get(zhbClass.getId());
					if(CollectionUtils.isNotEmpty(relatedJxbs)) {
						relatedSubjectIds = EntityUtils.getSet(relatedJxbs, NewGkDivideClass::getSubjectIds);
					}
					for (String subjectId : setT) {
						if(CollectionUtils.isNotEmpty(relatedSubjectIds) && relatedSubjectIds.contains(subjectId)) {
							continue;
						}
						if(!subjectIdTypeClassMap.containsKey(subjectId)) {
							subjectIdTypeClassMap.put(subjectId, new HashMap<>());
							subjectIdTypeClassMap.get(subjectId).put(NewGkElectiveConstant.SUBJECT_TYPE_A, new ArrayList<>());
							subjectIdTypeClassMap.get(subjectId).put(NewGkElectiveConstant.SUBJECT_TYPE_B, new ArrayList<>());
						}
						if(subjectIdsTList.contains(subjectId) && (type == null || NewGkElectiveConstant.SUBJECT_TYPE_A.equals(type))) {
							subjectIdTypeClassMap.get(subjectId).get(NewGkElectiveConstant.SUBJECT_TYPE_A).add(clazz);
						}else if(!subjectIdsTList.contains(subjectId) && (type == null || NewGkElectiveConstant.SUBJECT_TYPE_B.equals(type))){
							subjectIdTypeClassMap.get(subjectId).get(NewGkElectiveConstant.SUBJECT_TYPE_B).add(clazz);
						}
					}
				}
			}
		}
		
		Set<String> subjectIdArr = subjectIdTypeClassMap.keySet();
		Map<String, String> coureNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIdArr.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		
		List<DivideResultDto> dtoList = new ArrayList<>();
		DivideResultDto dto;
//		String courseName = null;
		List<NewGkDivideClass> newList;
		for (String subjectIds2 : subjectIdTypeClassMap.keySet()) {
			List<NewGkDivideClass> alist = subjectIdTypeClassMap.get(subjectIds2).get(NewGkElectiveConstant.SUBJECT_TYPE_A);
			List<NewGkDivideClass> blist = subjectIdTypeClassMap.get(subjectIds2).get(NewGkElectiveConstant.SUBJECT_TYPE_B);
			
			Integer aStuNum = (int) alist.stream().flatMap(e->e.getStudentList().stream()).distinct().count();
			Integer stuNum = (int) blist.stream().flatMap(e->e.getStudentList().stream()).distinct().count();
			
			dto = new DivideResultDto();
			dto.setCourseName(coureNameMap.get(subjectIds2));
			if(type != null) {
				dto.setCourseName(coureNameMap.get(subjectIds2) + type);
			}
			dto.setTotalNum(alist.size() + blist.size());
			
			dto.setaClassNum(alist.size());
			dto.setbClassNum(blist.size());
			
			dto.setaStuNum(aStuNum);
			dto.setbStuNum(stuNum);

			// 加上行政班的 班级

			if(!NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())) {
				newList = new ArrayList<>(alist);
				newList.addAll(blist);
				dto.setNormalClassList(newList);
				dto.setBatchClassListMap(new HashMap<>());
				Map<String, List<NewGkDivideClass>> bathListMap = dto.getBatchClassListMap();
				Collections.sort(newList, (x,y)->compareClassName(x.getClassName(), y.getClassName()));
				for (NewGkDivideClass dc : newList) {
					if(NewGkElectiveConstant.CLASS_TYPE_2.equals(dc.getClassType())) {
						if(!bathListMap.containsKey(dc.getBatch())) {
							bathListMap.put(dc.getBatch(), new ArrayList<>());
						}
						bathListMap.get(dc.getBatch()).add(dc);
//					}
//					if("1".equals(dc.getBatch())){
//						dto.getBatchClassList1().add(dc);
//					}else if("2".equals(dc.getBatch())){
//						dto.getBatchClassList2().add(dc);
//					}else if("3".equals(dc.getBatch())){
//						dto.getBatchClassList3().add(dc);
					}else if(NewGkElectiveConstant.CLASS_TYPE_1.equals(dc.getClassType())){
						dto.getAsXzbClassList().add(dc);
					}
				}
			}
			dtoList.add(dto);

		}
		map.put("dtoList", dtoList);
		map.put("openType", divide.getOpenType());
    }
    
    private int compareClassName(String className, String className2) {
		if((className.contains("A") || className.contains("B")) 
				&& (!className2.contains("A") && !className2.contains("B"))) {
			return -1;
		}
		if((className2.contains("A") || className2.contains("B")) 
				&& (!className.contains("A") && !className.contains("B"))) {
			return 1;
		}
//		Pattern p = Pattern.compile("[^\\d]+(\\d+)班");
		Pattern p = Pattern.compile("(\\d+)班");
		try {
			Matcher matcher = p.matcher(className);
			matcher.find();
			Integer num1 = Integer.parseInt(matcher.group(1));
			
			matcher = p.matcher(className2);
			matcher.find();
			Integer num2 = Integer.parseInt(matcher.group(1));
			
			return num1.compareTo(num2);
		} catch (Exception e) {
			e.printStackTrace();
			return className.compareTo(className2);
		}
		
	}

	@Override
	public void deleteOpenXzbNext(String unitId,String divideId) {
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, true);
		if(CollectionUtils.isNotEmpty(list)) {
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			//删除班级下学生以及班级
			newGkDivideClassService.deleteByClassIdIn2(unitId, divideId, ids.toArray(new String[] {}));
		}
		newGkClassBatchService.deleteByDivideId(unitId, divideId, null);
		updateStat(divideId,NewGkElectiveConstant.IF_0);
	}

	@Override
	public void divideJxbResultCount(NewGkDivide divide, String fromSolve, String arrayId, String courseId, ModelMap map) {
		List<NewGkDivideClass> divideClassList;
	    if ("1".equals(fromSolve)) {
            divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), arrayId,
                    new String[] {}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
        } else {
            divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(),
                    new String[]{}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        }
		List<NewGkDivideClass> jxbClassList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.filter(e->StringUtils.isNotBlank(e.getBatch())).collect(Collectors.toList());
		
		List<NewGkDivideClass> xzbClassList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).collect(Collectors.toList());
		
		List<NewGkOpenSubject> subjectByDivideList = newGkOpenSubjectService.findByDivideId(divide.getId());
		Set<String> aSubjectId=new HashSet<String>();
		Set<String> bSubjectId=new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();
		for(NewGkOpenSubject open:subjectByDivideList){
			subjectIds.add(open.getSubjectId());
			if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(open.getSubjectType())){
//				xzbSubjectId.add(open.getSubjectId());
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(open.getSubjectType())){
				aSubjectId.add(open.getSubjectId());			
			}else{
				bSubjectId.add(open.getSubjectId());
			}
		}
		
		// 统计某一科目所有的班级 包括 教学班和行政班
		Map<String, List<NewGkDivideClass>> subjectClassMap = jxbClassList.stream().collect(Collectors.groupingBy(e->e.getSubjectIds()));
		Map<String,Map<String, List<NewGkDivideClass>>> subjectIdTypeClassMap = new HashMap<>();
		for (String subjectId : subjectClassMap.keySet()) {
			List<NewGkDivideClass> list = subjectClassMap.get(subjectId);
			List<NewGkDivideClass> aList = list.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_A.equals(e.getSubjectType())).collect(Collectors.toList());
			List<NewGkDivideClass> bList = list.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_B.equals(e.getSubjectType())).collect(Collectors.toList());
			Map<String, List<NewGkDivideClass>> map2 = new HashMap<>();
			map2.put(NewGkElectiveConstant.SUBJECT_TYPE_A, aList);
			map2.put(NewGkElectiveConstant.SUBJECT_TYPE_B, bList);
			
			subjectIdTypeClassMap.put(subjectId, map2);
		}
		
		
		Map<String, NewGkDivideClass> divideClassMap = EntityUtils.getMap(divideClassList, NewGkDivideClass::getId);
		Map<String, List<NewGkDivideClass>> relatedJxbClassMap = jxbClassList.stream()
				.filter(e->StringUtils.isNotBlank(e.getRelateId()))
				.collect(Collectors.groupingBy(e->e.getRelateId()));
		NewGkDivideClass zhbClass;
//		String[] subjectIdsT;
		Set<String> setT;
		Set<String> relatedSubjectIds = null;
		for (NewGkDivideClass clazz : divideClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType()) && StringUtils.isNotBlank(clazz.getRelateId())
					&& divideClassMap.containsKey(clazz.getRelateId())) {
				// 组合班
				zhbClass = divideClassMap.get(clazz.getRelateId());
				String[] subjectIdsT = zhbClass.getSubjectIds().split(",");
				String subjectIdsB = zhbClass.getSubjectIdsB();
				if(subjectIdsT.length > 0) {
					List<String> subjectIdsTList = new ArrayList<String>();
					if(!BaseConstants.ZERO_GUID.equals(subjectIdsT[0])){
						subjectIdsTList = Arrays.asList(subjectIdsT);
					}
					setT = Arrays.stream(subjectIdsT).filter(e->aSubjectId.contains(e))
							.collect(Collectors.toSet());
					
					relatedSubjectIds = null;
					/*
					if(subjectIdsT.length > 2) {
						setT.addAll(bSubjectId.stream()
								.filter(e->!Arrays.asList(subjectIdsT).contains(e))
								.collect(Collectors.toSet()));
						
					}
					*/
					if(StringUtils.isNotBlank(subjectIdsB)){
						setT.addAll(bSubjectId.stream()
								.filter(e->subjectIdsB.contains(e))
								.collect(Collectors.toSet()));
					}
					List<NewGkDivideClass> relatedJxbs = relatedJxbClassMap.get(zhbClass.getId());
					if(CollectionUtils.isNotEmpty(relatedJxbs)) {
						relatedSubjectIds = EntityUtils.getSet(relatedJxbs, NewGkDivideClass::getSubjectIds);
					}
					for (String subjectId : setT) {
						if(CollectionUtils.isNotEmpty(relatedSubjectIds) && relatedSubjectIds.contains(subjectId)) {
							continue;
						}
						if(!subjectIdTypeClassMap.containsKey(subjectId)) {
							subjectIdTypeClassMap.put(subjectId, new HashMap<>());
							subjectIdTypeClassMap.get(subjectId).put(NewGkElectiveConstant.SUBJECT_TYPE_A, new ArrayList<>());
							subjectIdTypeClassMap.get(subjectId).put(NewGkElectiveConstant.SUBJECT_TYPE_B, new ArrayList<>());
						}
						if(subjectIdsTList.contains(subjectId)) {
							subjectIdTypeClassMap.get(subjectId).get(NewGkElectiveConstant.SUBJECT_TYPE_A).add(clazz);
						}else if(!subjectIdsTList.contains(subjectId)){
							subjectIdTypeClassMap.get(subjectId).get(NewGkElectiveConstant.SUBJECT_TYPE_B).add(clazz);
						}
					}
				}
			}
		}
		
		if(StringUtils.isNotBlank(courseId)){
			Map<String, List<NewGkDivideClass>> map2 = subjectIdTypeClassMap.get(courseId);
			if(map2 != null) {
				List<NewGkDivideClass> alist = map2.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
				List<NewGkDivideClass> blist = map2.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
				List<DivideResultDto> aDtoList = makeDtoList(xzbClassList, alist);
				List<DivideResultDto> bDtoList = makeDtoList(xzbClassList, blist);
				map.put("aDtoList", aDtoList);
				map.put("bDtoList", bDtoList);
			}
		}else{
			Map<String, Map<String, List<DivideResultDto>>> exportMap = new HashMap<String, Map<String,List<DivideResultDto>>>();
			for (Entry<String, Map<String, List<NewGkDivideClass>>> entry : subjectIdTypeClassMap.entrySet()) {
				exportMap.put(entry.getKey(), new HashMap<String, List<DivideResultDto>>());
				List<NewGkDivideClass> alist = entry.getValue().get(NewGkElectiveConstant.SUBJECT_TYPE_A);
				List<NewGkDivideClass> blist = entry.getValue().get(NewGkElectiveConstant.SUBJECT_TYPE_B);
				List<DivideResultDto> aDtoList = makeDtoList(xzbClassList, alist);
				List<DivideResultDto> bDtoList = makeDtoList(xzbClassList, blist);
				exportMap.get(entry.getKey()).put("aDtoList", aDtoList);
				exportMap.get(entry.getKey()).put("bDtoList", bDtoList);
				map.put("exportMap", exportMap);
			}
		}
	}

	private List<DivideResultDto> makeDtoList(List<NewGkDivideClass> xzbClassList, List<NewGkDivideClass> list) {
		DivideResultDto dto;
		NewGkDivideClass dc1;
		List<DivideResultDto> dtoList = new ArrayList<>();
		for (NewGkDivideClass dc : list) {
			dto = new DivideResultDto();
			dto.setClassName(dc.getClassName());
			dto.setTotalNum(dc.getStudentList().size());
			dto.setClassType(dc.getClassType());
			dto.setStudentList(dc.getStudentList());
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(dc.getClassType())){
				dc1 = new NewGkDivideClass();
				dc1.setClassNum(dc.getStudentList().size());
				dc1.setClassName(dc.getClassName());
				dto.getNormalClassList().add(dc1);
			}else{
				for (NewGkDivideClass dcc : xzbClassList) {
					dc1 = new NewGkDivideClass();
					if(CollectionUtils.intersection(dcc.getStudentList(), dc.getStudentList()).size()>0){
						dc1.setClassName(dcc.getClassName());
						dc1.setClassNum(CollectionUtils.intersection(dcc.getStudentList(), dc.getStudentList()).size());
						dto.getNormalClassList().add(dc1);
					}
				}
			}
			dtoList.add(dto);
		}
		Collections.sort(dtoList, new Comparator<DivideResultDto>() {
			
			@Override
			public int compare(DivideResultDto o1, DivideResultDto o2) {
				if(o1.getClassType().equals(o2.getClassType())){
					return o1.getClassName().compareTo(o2.getClassName());
				}
				return o1.getClassType().compareTo(o2.getClassType());
			}
		});
		return dtoList;
	}

	@Override
	public void saveFinishDivide(NewGkDivide divide, String[] delClassIds, List<NewGkDivideClass> savedClassList,
								 List<NewGkClassBatch> updateBatchList) {
		if(divide == null)
			return;
		
		String divideId = divide.getId();
		String unitId = divide.getUnitId();
		if(delClassIds!=null && delClassIds.length>0){
			newGkDivideClassService.deleteByClassIdIn(unitId,divideId, delClassIds);
		}

		if(CollectionUtils.isNotEmpty(savedClassList)){
			newGkDivideClassService.saveAll(savedClassList.toArray(new NewGkDivideClass[0]));
		}

		if(CollectionUtils.isNotEmpty(updateBatchList)){
			newGkClassBatchService.saveAll(updateBatchList.toArray(new NewGkClassBatch[0]));
		}

		divide.setStat(NewGkElectiveConstant.IF_1);
		this.save(divide);
		
	}

	@Override
	public void deleteOpenLeftZhbNext(String unitId, String divideId) {
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_4}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if(CollectionUtils.isNotEmpty(list)) {
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			//删除班级下学生以及班级
			newGkDivideClassService.deleteByClassIdIn2(unitId, divideId, ids.toArray(new String[] {}));
		}
		updateStat(divideId,NewGkElectiveConstant.IF_0);
	}

	@Override
	public void deleteOpenJxbNext(String unitId, String divideId, String subjectType) {
		List<NewGkDivideClass> list = null;
		if(StringUtils.isBlank(subjectType)) {
			newGKStudentRangeExService.deleteByDivideIdAndSubjectType(divideId,null);
			list=newGkDivideClassService.findByDivideIdAndClassTypeSubjectTypeWithMaster(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);
		}else if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			//删除学考参数设置
			newGKStudentRangeExService.deleteByDivideIdAndSubjectType(divideId,NewGkElectiveConstant.SUBJECT_TYPE_B);
			list = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, true);
		}else{
			list=newGkDivideClassService.findByDivideIdAndClassTypeSubjectTypeWithMaster(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, subjectType);
		}
		if(CollectionUtils.isNotEmpty(list)) {
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			//删除班级下学生以及班级
			newGkDivideClassService.deleteByClassIdIn2(unitId, divideId, ids.toArray(new String[] {}));
		}
		updateStat(divideId,NewGkElectiveConstant.IF_0);
		
	}

	@Override
	public void deleteOpenJxb(String unitId, String divideId, String subjectType) {
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassTypeSubjectTypeWithMaster(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, subjectType);
		if(CollectionUtils.isNotEmpty(list)) {
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			//删除班级下学生以及班级
			newGkDivideClassService.deleteByClassIdIn2(unitId, divideId, ids.toArray(new String[] {}));
		}
	}
}
