package net.zdsoft.evaluation.data.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dao.TeachEvaluateResultDao;
import net.zdsoft.evaluation.data.dto.OptionDto;
import net.zdsoft.evaluation.data.dto.ResultStatDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemOptionService;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.evaluation.data.service.TeachEvaluateProjectService;
import net.zdsoft.evaluation.data.service.TeachEvaluateRelationService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.tutor.remote.service.TutorRemoteService;


@Service("teachEvaluateResultService")
public class TeachEvaluateResultServiceImpl extends BaseServiceImpl<TeachEvaluateResult, String> implements TeachEvaluateResultService {
	@Autowired
	private TeachEvaluateResultDao teachEvaluateResultDao;
	@Autowired
	private TeachEvaluateProjectService teachEvaluateProjectService;
	@Autowired
	private TeachEvaluateItemService teachEvaluateItemService;
	@Autowired
	private TeachEvaluateItemOptionService teachEvaluateItemOptionService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private TutorRemoteService tutorRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private TeachEvaluateRelationService teachEvaluateRelationService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;

	public Map<String, List<Map<String,String>>> getResultTxtDto(String projectId, String evaluateType) {
    	Map<String,List<Map<String,String>>> map = new HashMap<>();
    	if(StringUtils.isBlank(projectId)) {
    		return map;
    	}
    	List<TeachEvaluateResult> resultTxtList = teachEvaluateResultDao.findTxtByTeaId(projectId);
    	if(CollectionUtils.isEmpty(resultTxtList)) {
    		return map;
    	}
    	//key:teaId + opterId
    	Map<String,List<TeachEvaluateResult>> reMap = new HashMap<>();
    	for (TeachEvaluateResult re : resultTxtList) {
    		String key = re.getOperatorId()+","+re.getTeacherId();
    		if(StringUtils.equals(evaluateType, EvaluationConstants.EVALUATION_TYPE_TEACHER)) {
				key = key + "_" + re.getClassId();
			}
    		if(!reMap.containsKey(key)) {
    			reMap.put(key, new ArrayList<TeachEvaluateResult>());
    		}
    		reMap.get(key).add(re);
    	}
    	
    	for (String e : reMap.keySet()) {
			List<TeachEvaluateResult> list = reMap.get(e);
			String key = e.split(",")[1];
			if(!map.containsKey(key)) {
    			map.put(key, new ArrayList<Map<String,String>>());
    		}
			Map<String,String> map1 = new HashMap<>();
			for (TeachEvaluateResult reTxt : list) {
				map1.put(reTxt.getItemId(), reTxt.getResult());
			}
			map.get(key).add(map1);
		}
    	return map;
    }
    @Override
    public List<ResultStatDto> getResultTxtDto(String projectId,
    		String subjectId, String classId,String teaId) {
    	// TODO 取学生数据的时候有点问题 后面记得看
    	List<ResultStatDto> dtolist = new ArrayList<>();
    	if(StringUtils.isBlank(teaId)){
    		return dtolist;
    	}
    	List<TeachEvaluateResult> resultTxtList = new ArrayList<>(); 
    	if(StringUtils.isNotBlank(subjectId) && StringUtils.isNotBlank(classId)){
    		resultTxtList = teachEvaluateResultDao.findTxtBySubIdAndClsId(projectId,teaId,subjectId,classId);
    	}else if(StringUtils.isNotBlank(subjectId)){
    		resultTxtList = teachEvaluateResultDao.findTxtBySubId(projectId,teaId,subjectId);
    	}else if(StringUtils.isNotBlank(classId)){
    		resultTxtList = teachEvaluateResultDao.findTxtByClsId(projectId,teaId,classId);
    	}else{
    		resultTxtList = teachEvaluateResultDao.findTxtByTeaId(projectId,teaId);
    	}
    	if(CollectionUtils.isNotEmpty(resultTxtList)){
//    		Set<String> stuIds = EntityUtils.getSet(resultTxtList, "operatorId");
//    		List<Student> stuList = SUtils.dt(studentRemoteService.findByIds(stuIds.toArray(new String[0])), new TR<List<Student>>()); 
//    		Map<String,Student> stuMap = EntityUtils.getMap(stuList, "id");
    		Map<String,ResultStatDto> dtoMap = new HashMap<>();
    		ResultStatDto dto;
    		for (TeachEvaluateResult reTxt : resultTxtList) {
				if(!dtoMap.containsKey(reTxt.getOperatorId()+","+reTxt.getTeacherId()+","+reTxt.getClassId())){
					dto = new ResultStatDto();
					dto.setStuId(reTxt.getOperatorId());
					Student stu=SUtils.dc(studentRemoteService.findOneById(reTxt.getOperatorId()),Student.class);
//					dto.setStuName(stuMap.containsKey(reTxt.getOperatorId())?stuMap.get(reTxt.getOperatorId()).getStudentName():"");
					dto.setStuName(stu.getStudentName());
					dto.setStuCode(stu.getStudentCode());
					dtoMap.put(reTxt.getOperatorId()+","+reTxt.getTeacherId()+","+reTxt.getClassId(), dto);
				}
				dtoMap.get(reTxt.getOperatorId()+","+reTxt.getTeacherId()+","+reTxt.getClassId()).getItemTxtMap().put(reTxt.getItemId(), reTxt.getResult());
			}
    		for (ResultStatDto txtDto : dtoMap.values()) {
				dtolist.add(txtDto);
			}
    		Collections.sort(dtolist, new Comparator<ResultStatDto>(){
				@Override
				public int compare(ResultStatDto o1, ResultStatDto o2) {
					return o1.getStuCode().compareTo(o2.getStuCode());
				}
    		});
    		
    	}
    	return dtolist;
    }
    
	@Override
	public List<TeachEvaluateResult> findBySubmitAndInProjectId(String[] projectIds) {
		List<TeachEvaluateResult> results =  teachEvaluateResultDao.findBySubmitAndInProjectId(projectIds);
		List<TeachEvaluateResult> relist = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(results)){
			Set<String> stuIds = EntityUtils.getSet(results, TeachEvaluateResult::getOperatorId);
			Map<String,Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])),new TR<List<Student>>(){}),Student::getId);
			for(TeachEvaluateResult result : results){
				if(stuMap.containsKey(result.getOperatorId()) && stuMap.get(result.getOperatorId()).getIsDeleted() == 0){
					relist.add(result);
				}
			}
		}
		return relist;
	}
	
	@Override
	public void deleteByProjectId(String projectId) {
		teachEvaluateResultDao.deleteByProjectId(projectId);
	}
	@Override
	public Set<String> getResultClsSubTeaIds(String projectId) {
		return teachEvaluateResultDao.getResultClsSubTeaIds(projectId);
	}
	@Override
	public Set<String> getResultGradeClsId(String projectId) {
		return teachEvaluateResultDao.getResultGradeClsId(projectId);
	}
	@Override
	public List<TeachEvaluateResult> findByProjectIdAndClsIdAndSubId(String projectId,
			String clsId, String subId) {
		return teachEvaluateResultDao.findByProjectIdAndClsIdAndSubId(projectId,clsId,subId);
	}
	@Override
	public Set<String> getResultGradeTeaId(String projectId) {
		return teachEvaluateResultDao.getResultGradeTeaId(projectId);
	}
	@Override
	public Set<String> getResultSubIds(String projectId) {
		return teachEvaluateResultDao.getResultSubIds(projectId);
	}
	@Override
	public Map<String, Integer> getCountMapByProjectIds(String[] projectIds) {
		return teachEvaluateResultDao.getCountMapByProjectIds(projectIds);
	}
	
	@Override
	public Set<String> getStuIdByProjectId(String projectId,
			String classId, String subId, String teacherId) {
		return teachEvaluateResultDao.getStuIdByProjectId(projectId, classId, subId, teacherId);
	}
	@Override
	public List<TeachEvaluateResult> findByProjectIdAndTeaId(String projectId,
			String teaId) {
		return teachEvaluateResultDao.findByProjectIdAndTeaId(projectId,teaId);
	}
	@Override
	public List<TeachEvaluateResult> findByProjectIdAndGradeId(
			String projectId, String gradeId) {
		return teachEvaluateResultDao.findByProjectIdAndGradeId(projectId,gradeId);
	}
//	@Override
//	public List<TeachEvaluateResult> findByProjectIdAndSubId(String projectId,
//			String subId) {
//		return teachEvaluateResultDao.findByProjectIdAndSubId(projectId,subId);
//	}
	@Override
	public List<TeachEvaluateResult> findByProjectIdAndSubIdAndTeacherId(
			String projectId, String subId, String teaId) {
		return teachEvaluateResultDao.findByProjectIdAndSubIdAndTeacherId(projectId,subId,teaId);
	}
	
	@Override
	public List<TeachEvaluateResult> findByProjectIdAndClsId(String projectId,
			String clsId) {
		return teachEvaluateResultDao.findByProjectIdAndClsId(projectId,clsId);
	}
	@Override
	public Set<String> getResultStudents(String projectId, String classId,
			String subId, String teacherId) {
		return teachEvaluateResultDao.getStuIdByProjectId(projectId, classId, subId, teacherId);
	}
	
	
	@Override
	public Map<String,List<TeachEvaluateItem>> findItemByUnitId(Map<String,String> resultOfTeaIdMap,String unitId, String studentId,
										String projectId,String evaluateType,String subjectId,String status, String teacherId, String clsId){
		
		List<TeachEvaluateItem>  itemList=teachEvaluateItemService.findByUidAndEvaType(unitId, evaluateType,projectId);
		List<TeachEvaluateResult> resultList=null;
		if("1".equals(status)){//编辑页面
			resultList=teachEvaluateResultDao.findByCon(unitId, studentId, projectId, evaluateType);
		}else{
			resultList=teachEvaluateResultDao.findByCon(unitId, studentId, projectId, evaluateType,EvaluationConstants.EVALUATION_STATE_SUBMIT);
		}
		if(StringUtils.isNotBlank(teacherId)) {
			for(int i = 0;i<resultList.size();i++) {
				TeachEvaluateResult re = resultList.get(i);
				if(!StringUtils.equals(teacherId, re.getTeacherId())) {
					resultList.remove(re);
					i--;
				}
			}
		}
		if(StringUtils.isNotBlank(clsId)) {
			for(int i = 0;i<resultList.size();i++) {
				TeachEvaluateResult re = resultList.get(i);
				if(!StringUtils.equals(clsId, re.getClassId())) {
					resultList.remove(re);
					i--;
				}
			}
		}
		Map<String,List<TeachEvaluateResult>> resultMap=new HashMap<>();
		boolean haveResult=CollectionUtils.isNotEmpty(resultList);
		if(haveResult){
			for(TeachEvaluateResult result:resultList){
				//若选项id不存在，则代表 为解答题  没有选项。只有结果  通过指标id去查找对应的结果
				String id=result.getResultId();
				if(StringUtils.isBlank(id)){
					id=result.getItemId();
				}
				List<TeachEvaluateResult> inList=resultMap.get(id);
				if(CollectionUtils.isEmpty(inList)){
					inList=new ArrayList<>();
				}
				inList.add(result);
				resultMap.put(id,inList);
				if(StringUtils.isNotBlank(result.getSubjectId())){
					resultOfTeaIdMap.put(result.getSubjectId(), result.getTeacherId());
				}else{
					resultOfTeaIdMap.put(result.getOperatorId(), result.getTeacherId());
				}
			}
		}
		//为调查问卷添加已保存的答案 或查看数据
		Map<String,List<TeachEvaluateItem>> itemMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(itemList)){
			for(TeachEvaluateItem item:itemList){
				List<TeachEvaluateItemOption> optionList=item.getOptionList();
				if(CollectionUtils.isEmpty(optionList)){
					List<TeachEvaluateResult> evaResultList=resultMap.get(item.getId());
					if(CollectionUtils.isNotEmpty(evaResultList)){
						if(StringUtils.isNotBlank(subjectId)){//不空 代表是教学调查或选修课调查 有多门课的情况、 需要判断是否包含选的课
							for(TeachEvaluateResult result:evaResultList){
								if(result.getSubjectId().equals(subjectId)){
									item.setResult(result.getResult());
									break;
								}
							}
						}else{
							item.setResult(evaResultList.get(0).getResult());
						}
					}
				}else{
					for(TeachEvaluateItemOption option:optionList){
						List<TeachEvaluateResult> evaResultList=resultMap.get(option.getId());
						if(CollectionUtils.isNotEmpty(evaResultList)){
							if(StringUtils.isNotBlank(subjectId)){//不空 代表是教学调查或选修课调查 有多门课的情况、 需要判断是否包含选的课
								for(TeachEvaluateResult result:evaResultList){
									if(subjectId.equals(result.getSubjectId())){
										option.setHaveSelected(true);
										break;
									}
								}
							}else{
								option.setHaveSelected(true);
							}
						}
					}
				}
				/*//status!="1" &&
				if(haveResult && !statusFlag){//有结果（说明不是未开始） statusFlag是false说明没有此结果不存在 是后期增加的指标
					continue;
				}*/
				List<TeachEvaluateItem> inList=itemMap.get(item.getItemType());
				if(CollectionUtils.isEmpty(inList)){
					inList=new ArrayList<>();
				}
				inList.add(item);
				itemMap.put(item.getItemType(),inList);
			}
		}
		return itemMap;
	}
	@Override
	public void setCourseMap(Set<String> subIdTeaIdClassIdSet,String unitId,String studentId,String acadyear, String semester,String projectId){
		RedisUtils.del(new String[] {studentId+"_"+projectId,studentId+acadyear+semester+"_COURSE_TEA_CLS_ID"});
		Set<String> lastIds=SUtils.dt(RedisUtils.get(studentId+"_"+projectId),new TR<Set<String>>(){});
//		Set<String> courseIdOfTeaIds=SUtils.dt(RedisUtils.get(studentId+acadyear+semester+"_COURSE_TEA_ID"),new TR<Set<String>>(){});
		Set<String> setss = SUtils.dt(RedisUtils.get(studentId+acadyear+semester+"_COURSE_TEA_CLS_ID"),new TR<Set<String>>(){});
		if(CollectionUtils.isEmpty(lastIds) || CollectionUtils.isEmpty(setss) ){
			lastIds=new HashSet<>();
			setss=new HashSet<>();
			Student stu=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			//不走班的行政班（该科目不走班）
			List<ClassTeaching> classTeachingList=new ArrayList<>();
			if(stu!=null){
				Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
				if(clazz!=null){
					classTeachingList=SUtils.dt(classTeachingRemoteService.findBySearch(unitId, acadyear, semester,new String[]{clazz.getId()}, 0, 0),new TR<List<ClassTeaching>>(){});
				}
			}
			//获取relation数据  key-projectId
			Map<String, Set<String>> relationMap=teachEvaluateRelationService.findMapByProjectIds(new String[]{projectId});
			Set<String> realationVals=relationMap.get(projectId);
			Set<String> valueIds=new HashSet<>();
			//该学生对应的教学班ids（包括选修课的）
			Set<String> teachClassIds=EntityUtils.getSet(SUtils.dt(teachClassStuRemoteService.findTeachClassByStudentId2(studentId, acadyear, semester),new TR<List<TeachClass>>(){}), TeachClass::getId);
			List<TeachClass> allteachClassList=SUtils.dt(teachClassRemoteService.findTeachClassList(unitId, acadyear, semester, null, null,true),new TR<List<TeachClass>>(){});
			List<TeachClass> teachClassList=new ArrayList<>();
			if(CollectionUtils.isNotEmpty(allteachClassList)){
				for(TeachClass teachClass:allteachClassList){
					if(teachClassIds.contains(teachClass.getId())){
						teachClassList.add(teachClass);
					}
				}
			}
			for(ClassTeaching classTeaching:classTeachingList){
				valueIds.add(classTeaching.getClassId()+"_"+classTeaching.getSubjectId());
//				courseIdOfTeaIds.add(classTeaching.getSubjectId()+"_"+classTeaching.getTeacherId());
				setss.add(classTeaching.getSubjectId()+"_"+classTeaching.getTeacherId()+"_"+classTeaching.getClassId());
			}
			for(TeachClass teachClass:teachClassList){
				valueIds.add(teachClass.getId()+"_"+teachClass.getCourseId());
//				courseIdOfTeaIds.add(teachClass.getCourseId()+"_"+teachClass.getTeacherId());
				setss.add(teachClass.getCourseId()+"_"+teachClass.getTeacherId()+"_"+teachClass.getId());
			}
			if(realationVals == null) {
				realationVals = new HashSet<>();
			}
			Collection<String> c=CollectionUtils.intersection(realationVals, valueIds);
			lastIds=new HashSet<>();
			if(CollectionUtils.isNotEmpty(c)){
				for(String lastId:c){
					lastIds.add(lastId);
				}
			}
			//放缓存10分钟
			RedisUtils.set(studentId+acadyear+semester+"_COURSE_TEA_CLS_ID", SUtils.s(setss),RedisUtils.TIME_TEN_MINUTES);
			RedisUtils.set(studentId+"_"+projectId, SUtils.s(lastIds),RedisUtils.TIME_TEN_MINUTES);
		}
		if(CollectionUtils.isNotEmpty(lastIds) && subIdTeaIdClassIdSet!=null){
			for(String lastId:lastIds){
				String[] sth=lastId.split("_");
				String subId = sth[1];
				String clsId = sth[0];
				for(String str:setss){
					String[] strArr=str.split("_");
					if(StringUtils.equals(subId, strArr[0]) && StringUtils.equals(clsId, strArr[2])) {
						subIdTeaIdClassIdSet.add(strArr[0]+","+strArr[1]+","+strArr[2]);
						break;
					}
				}
			}
		}
	}
	@Override
	public List<TeachEvaluateProject> findResultByUnitId(String unitId,String studentId, String acadyear, String semester){
		List<TeachEvaluateProject> projects =  teachEvaluateProjectService.findByCon(unitId, acadyear, semester);
		List<TeachEvaluateProject> lastList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(projects)){
			//获取导师
			Teacher tutorTeacher=getTutorTeacher(studentId, unitId);
			//获取班主任
			Teacher classTeacher=null;
			//该学生的年级
			String gradeId="";
			Student stu=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			//不走班的行政班（该科目不走班）
			List<ClassTeaching> classTeachingList=new ArrayList<>();
			if(stu!=null){
				Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
				if(clazz!=null){
					classTeacher=SUtils.dc(teacherRemoteService.findOneById(clazz.getTeacherId()),Teacher.class);
					gradeId=clazz.getGradeId();
					classTeachingList=SUtils.dt(classTeachingRemoteService.findBySearch(unitId, acadyear, semester,new String[]{clazz.getId()}, 0, 0),new TR<List<ClassTeaching>>(){});
				}
			}
			Set<String> projectIds=new HashSet<>();
			for(TeachEvaluateProject project:projects){
				projectIds.add(project.getId());
			}
			//获取relation数据  key-projectId
			Map<String, Set<String>> relationMap=teachEvaluateRelationService.findMapByProjectIds(projectIds.toArray(new String[0]));
			//该学生对应的教学班ids（包括选修课的）
			Set<String> teachClassIds=EntityUtils.getSet(SUtils.dt(teachClassStuRemoteService.findTeachClassByStudentId2(studentId, acadyear, semester),new TR<List<TeachClass>>(){}), TeachClass :: getId);
			//教学班与行政班的结合（包含subjectid）
			Set<String> valueIds=new HashSet<>();
			//所有的科目id
			Set<String> courseIds=new HashSet<>();
			//科目与教师id
			Set<String> courseIdOfTeaIds=new HashSet<>();
			for(ClassTeaching classTeaching:classTeachingList){
				valueIds.add(classTeaching.getClassId()+"_"+classTeaching.getSubjectId());
				courseIds.add(classTeaching.getSubjectId());
				courseIdOfTeaIds.add(classTeaching.getSubjectId()+"_"+classTeaching.getTeacherId());
			}
			List<TeachClass> teachClassList=SUtils.dt(teachClassRemoteService.findListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
			//获取教学班对应courseId
			Map<String,String> teachClassMap=new HashMap<>();
			for(TeachClass teachClass:teachClassList){
				valueIds.add(teachClass.getId()+"_"+teachClass.getCourseId());
				teachClassMap.put(teachClass.getId(), teachClass.getCourseId());
				courseIds.add(teachClass.getCourseId());
				courseIdOfTeaIds.add(teachClass.getCourseId()+"_"+teachClass.getTeacherId());
			}
			Map<String,String> courseNameMap=EntityUtils.getMap(SUtils.dt(courseRemoteService
						.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){}),Course::getId,Course::getSubjectName);
			//将此科目老师id的结果存入缓存  与下缓存对应
			//放缓存10分钟
			RedisUtils.set(studentId+acadyear+semester+"_COURSE_TEA_ID", SUtils.s(courseIdOfTeaIds),RedisUtils.TIME_TEN_MINUTES);
			//获取已有的结果
			List<TeachEvaluateResult>  resultList=teachEvaluateResultDao.findByCon(unitId, studentId,EvaluationConstants.EVALUATION_STATE_SUBMIT, projectIds.toArray(new String[0]));
			Map<String,List<TeachEvaluateResult>> resultMap=new HashMap<>();
			if(CollectionUtils.isNotEmpty(resultList)){
				for(TeachEvaluateResult result:resultList){
					List<TeachEvaluateResult> inList=resultMap.get(result.getProjectId());
					if(CollectionUtils.isEmpty(inList)){
						inList=new ArrayList<>();
					}
					inList.add(result);
					resultMap.put(result.getProjectId(),inList);
				}
			}
			
			for(TeachEvaluateProject project:projects){
				String thisId=project.getEvaluateType();
				Set<String> relationValueIds=relationMap.get(project.getId());//（包含subjectid）
				boolean teachFlag=thisId.equals(EvaluationConstants.EVALUATION_TYPE_TEACH);
				boolean eleFlag=thisId.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE);
				String courseNames="";//必修课 7选3 选修课名称
				if(relationValueIds==null) relationValueIds=new HashSet<>();
				//筛选relation过滤条件
				if(teachFlag || eleFlag){
					int i=0;
					Collection<String> c=CollectionUtils.intersection(relationValueIds, valueIds);
					Set<String> lastIds=new HashSet<>();
					if(CollectionUtils.isNotEmpty(c)){
						for(String lastId:c){
							lastIds.add(lastId);
						}
					}
					//放缓存5分钟
					RedisUtils.set(studentId+"_"+project.getId(), SUtils.s(lastIds),RedisUtils.TIME_TEN_MINUTES);
					if(lastIds.size()==0){
						continue;
					}
					Set<String> myCourseIds=new HashSet<>();
					for(String lastId:lastIds){
						if(lastId.split("_").length>1){
							myCourseIds.add(lastId.split("_")[1]);
						}
					}
					for(String myCourseId:myCourseIds){
						courseNames+=courseNameMap.get(myCourseId);
						if(i!=myCourseIds.size()-1){
							courseNames+="、";
						}
						i++;
					}
				}else{
					if(!relationValueIds.contains(gradeId)){
						continue;
					}
				}
				boolean flag=false;
				if(DateUtils.compareIgnoreSecond(new Date(),project.getBeginTime())<=0){//当前时间小于开始时间时 则未开始
					project.setStatus(EvaluationConstants.EVALUATION_STATE_NOT_START);
					flag=true;
				}else if(DateUtils.compareIgnoreSecond(new Date(),project.getEndTime())>0){//当前时间大于结束时间时 则已结束
					project.setStatus(EvaluationConstants.EVALUATION_STATE_END);
					flag=true;
				}
				if(!flag){
					if(CollectionUtils.isNotEmpty(resultMap.get(project.getId()))){
						project.setStatus(EvaluationConstants.EVALUATION_STATE_END);//若有提交的数据 则结束
					}else{
						project.setStatus(EvaluationConstants.EVALUATION_STATE_START);//进行中
					}
				}
				if(teachFlag){//教学调查
					project.setContainsType(courseNames);
				}else if(thisId.equals(EvaluationConstants.EVALUATION_TYPE_TEACHER)){//班主任
					project.setContainsType(classTeacher==null?"":classTeacher.getTeacherName());
				}else if(thisId.equals(EvaluationConstants.EVALUATION_TYPE_TOTOR)){//导师
					project.setContainsType(tutorTeacher==null?"":tutorTeacher.getTeacherName());
				}else if(eleFlag){//选修课
					project.setContainsType(courseNames);
				}
				lastList.add(project);
			}
		}
		return lastList;
	}
	@Override
	public Teacher getClassTeacher(String studentId){
		Student stu=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		if(stu!=null){
			Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
			if(clazz!=null){
				Teacher classTeacher=SUtils.dc(teacherRemoteService.findOneById(clazz.getTeacherId()),Teacher.class);
				return classTeacher;
			}
		}
		return null;
	}
	@Override
	public Teacher getTutorTeacher(String studentId,String unitId){
		Map<String,Set<String>> tutorMap=SUtils.dt(tutorRemoteService.getTutorTeaStuMapByUnitId(unitId),new TR<Map<String,Set<String>>>(){});
		Set<Entry<String,Set<String>>> entries= tutorMap.entrySet();
		String tutorTeacherId="";//导师id
		for(Entry<String,Set<String>> entry:entries){
			if(entry.getValue().contains(studentId)){
				tutorTeacherId=entry.getKey();
				break;
			}
		}
		Teacher tutorTeacher=SUtils.dc(teacherRemoteService.findOneById(tutorTeacherId),Teacher.class);
		return tutorTeacher;
	}
	
	@Override
	public String saveResult(String unitId,String studentId,OptionDto dto){
		List<TeachEvaluateResult> resultList=dto.getResultList();
		boolean flag=false;
		String evaluateType=dto.getEvaluateType();
		String subjectId=dto.getSubjectId();
		String state=dto.getState();//1保存   2提交
		boolean evaluateFlag=evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_TEACH) || evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE);
		if(evaluateFlag && StringUtils.isBlank(subjectId)){
			return "请求出错，请重新进入该模块";
		}
		if(state.equals(EvaluationConstants.EVALUATION_STATE_SUBMIT)&& evaluateFlag){//教学选修课调查
			flag=true;
		}
		if(CollectionUtils.isNotEmpty(resultList)){
			//先删除已有数据
			if(StringUtils.isNotBlank(subjectId)){
				if(StringUtils.isNotBlank(dto.getTeachOrclassId())) {
					teachEvaluateResultDao.deleteByConTea(unitId, studentId, dto.getProjectId(), evaluateType, subjectId,dto.getTeacherId(),dto.getTeachOrclassId());
				}else {
					teachEvaluateResultDao.deleteByConTea(unitId, studentId, dto.getProjectId(), evaluateType, subjectId,dto.getTeacherId());
				}
			}else{
				teachEvaluateResultDao.deleteByCon(unitId, studentId, dto.getProjectId(), evaluateType);
			}
			
			Student stu=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
			Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu!=null?stu.getClassId():""),Clazz.class);
			TeachEvaluateProject project=teachEvaluateProjectService.findOne(dto.getProjectId());
			Map<String,TeachEvaluateItemOption> optionMap=EntityUtils.getMap(teachEvaluateItemOptionService.findByUnitId(unitId),"id");
			String classId=dto.getTeachOrclassId();
			if(StringUtils.isBlank(classId)) classId=clazz.getId();
			
			List<TeachEvaluateResult> insertList=new ArrayList<>();
			for(TeachEvaluateResult result:resultList){
				String resultId=result.getResultId();
				if(StringUtils.isBlank(resultId)&&StringUtils.isBlank(result.getResult())){
					continue;
				}
				if(StringUtils.isNotBlank(resultId)){
					String[] strs=resultId.split(",");
					for(String str:strs){
						TeachEvaluateResult insert=new TeachEvaluateResult();
						insert.setUnitId(unitId);
						insert.setProjectId(dto.getProjectId());
						insert.setProjectName(project==null?"":project.getProjectName());
						insert.setEvaluateType(evaluateType);
						insert.setItemId(result.getItemId());
						insert.setItemName(result.getItemName());
						insert.setItemType(result.getItemType());
						insert.setClassId(classId);
						insert.setGradeId(clazz.getGradeId());
						insert.setOperatorId(studentId);
						insert.setCreationTime(new Date());
						insert.setSubjectId(subjectId);
						insert.setTeacherId(dto.getTeacherId());
						if(flag){
							insert.setState(EvaluationConstants.EVALUATION_STATE_SAVE);//先保存
						}else{
							insert.setState(state);//可直接提交
						}
						TeachEvaluateItemOption option=optionMap.get(str);
						if(option!=null){
							insert.setResult(option.getOptionName());
							insert.setScore(option.getScore());
						}
						insert.setResultId(str);
						insertList.add(insert);
					}
				}else{
					TeachEvaluateResult insert=new TeachEvaluateResult();
					insert.setUnitId(unitId);
					insert.setProjectId(dto.getProjectId());
					insert.setProjectName(project==null?"":project.getProjectName());
					insert.setEvaluateType(evaluateType);
					insert.setItemId(result.getItemId());
					insert.setItemName(result.getItemName());
					insert.setItemType(result.getItemType());
					insert.setClassId(classId);
					insert.setGradeId(clazz.getGradeId());
					insert.setOperatorId(studentId);
					insert.setResult(result.getResult());
					insert.setCreationTime(new Date());
					insert.setSubjectId(subjectId);
					insert.setTeacherId(dto.getTeacherId());
					if(flag){
						insert.setState(EvaluationConstants.EVALUATION_STATE_SAVE);//先保存
					}else{
						insert.setState(state);//可直接提交
					}
					insertList.add(insert);
				}
			}
			checkSave(insertList.toArray(new TeachEvaluateResult[0]));
			saveAll(insertList.toArray(new TeachEvaluateResult[0]));
		}
		if(flag){
			List<TeachEvaluateResult> allList=teachEvaluateResultDao.findByCon(unitId, studentId, dto.getProjectId(), evaluateType);
			Set<String> subjectIds=new HashSet<>();
			Set<String> resultIds=new HashSet<>();
			Set<String> subTeaClsIds = new HashSet<>();
			if(CollectionUtils.isNotEmpty(allList)){
				for(TeachEvaluateResult result:allList){
					if(StringUtils.isNotBlank(result.getSubjectId())){
						subjectIds.add(result.getSubjectId());
						resultIds.add(result.getId());
						subTeaClsIds.add(result.getSubjectId()+","+result.getTeacherId()+","+result.getClassId());
					}
				}
			}
			if(evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_TEACH) || evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE)){//教学调查 必修课
				//获取subjectId
				Set<String> set = new HashSet<>();
				setCourseMap(set, unitId, studentId, dto.getAcadyear(), dto.getSemester(),dto.getProjectId());
				if(CollectionUtils.isNotEmpty(subTeaClsIds) &&subTeaClsIds.containsAll(set)){
					teachEvaluateResultDao.updateByCon(EvaluationConstants.EVALUATION_STATE_SUBMIT, resultIds.toArray(new String[0]));
				}else{
					return "还有课程未完成调查";
				}
			}
		}
		return "success";
	}
//	@Override
//	public List<TeachEvaluateResult> findResultByItemId(String unitId, String itemId){
//		return teachEvaluateResultDao.findByCon(unitId, itemId);
//	}
	@Override
	protected BaseJpaRepositoryDao<TeachEvaluateResult, String> getJpaDao() {
		return teachEvaluateResultDao;
	}
	
	@Override
	protected Class<TeachEvaluateResult> getEntityClass() {
		return TeachEvaluateResult.class;
	}

	@Override
	public Map<String, Float> findTeaRankBy(String projectId, String evaluateType) {
		return teachEvaluateResultDao.findTeaRankBy(projectId, evaluateType);
	}

}
