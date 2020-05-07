package net.zdsoft.evaluation.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dao.TeachEvaluateProjectDao;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemOptionService;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.evaluation.data.service.TeachEvaluateProjectService;
import net.zdsoft.evaluation.data.service.TeachEvaluateRelationService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultStatService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;


@Service("teachEvaluateProjectService")
public class TeachEvaluateProjectServiceImpl extends BaseServiceImpl<TeachEvaluateProject, String> implements TeachEvaluateProjectService {
	@Autowired
	private TeachEvaluateProjectDao teachEvaluateProjectDao;
	@Autowired
	private TeachEvaluateResultService teachEvaluateResultService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	/*@Autowired(required=false)
    private OpenApiNewElectiveService openApiNewElectiveService;*/
	@Autowired
	private TeachEvaluateResultStatService teachEvaluateResultStatService;
	@Autowired
	private TeachEvaluateItemService teachEvaluateItemService;
	@Autowired
	private TeachEvaluateItemOptionService teachEvaluateItemOptionService;
	@Autowired
	private TeachEvaluateRelationService teachEvaluateRelationService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	
	@Override
	public List<TeachEvaluateProject> findByUnitId(String unitId, String acadyear, String semester) {
		List<TeachEvaluateProject> projects =  teachEvaluateProjectDao.findByUnitIdAndAcadyearAndSemester(unitId, acadyear, semester);
		/*if(CollectionUtils.isNotEmpty(projects)){
			statSubmitNum(unitId,acadyear,semester,projects);
		}*/
		return projects;
	}
	
	@Override
	public List<TeachEvaluateProject> findByUnitIdAndType(String unitId,
			String acadyear, String semester, String evaluateType) {
		List<TeachEvaluateProject> projects = new ArrayList<>();
		if(StringUtils.isBlank(evaluateType)){
			projects =  teachEvaluateProjectDao.findByUnitIdAndAcadyearAndSemester(unitId, acadyear, semester);
		}else{
			projects =  teachEvaluateProjectDao.findByUnitIdAndAcadyearAndSemesterAndEvaluateType(unitId, acadyear, semester,evaluateType);
		}
		if(CollectionUtils.isNotEmpty(projects)){
			Set<String> projectIds = EntityUtils.getSet(projects, TeachEvaluateProject::getId);
//			List<TeachEvaluateResultStat> statList = teachEvaluateResultStatService.findByProjectIds(projectIds.toArray(new String[0]));
			Map<String,Integer> resultNumMap = teachEvaluateResultService.getCountMapByProjectIds(projectIds.toArray(new String[0]));
			for (TeachEvaluateProject p : projects) {
				if(resultNumMap.containsKey(p.getId()) && resultNumMap.get(p.getId()) > 0){
					p.setHasResult("1");
					p.setHasStat("1");
				}else{
					p.setHasResult("0");
					p.setHasStat("0");
				}
//				for (TeachEvaluateResultStat stat : statList) {
//					if(StringUtils.equals(stat.getProjectId(), p.getId())){
//						break;
//					}
//				}
			}
		}
		return projects;
	}
	
	@Override
	public TeachEvaluateProject getSubNum(String projectId) {
		TeachEvaluateProject p = findOne(projectId);
		if(p != null){
			List<TeachEvaluateProject> plist = new ArrayList<TeachEvaluateProject>();
			plist.add(p);
			statSubmitNum(p.getUnitId(),p.getAcadyear(),p.getSemester(),plist);
			return plist.get(0);
		}
		return null;
	}
	
	@Override
	public List<TeachEvaluateProject> findExist(String unitId, String acadyear,
			String semester, String evaluateType, Date beginTime, Date endTime) {
		return teachEvaluateProjectDao.findExist(unitId,acadyear,semester,evaluateType,beginTime,endTime);
	}
	
	@Override
	public void saveProject(TeachEvaluateProject project) {
		if(StringUtils.isBlank(project.getId())){
			checkSave(project);
			save(project);
		}else {
			teachEvaluateProjectDao.updateDate(project.getBeginTime(),project.getEndTime(),project.getId());
			return;
		}
		
		List<TeachEvaluateItem> items = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),BaseConstants.ZERO_GUID);
		List<TeachEvaluateItem> projectItem = new ArrayList<>();
		List<TeachEvaluateItemOption> projectOption = new ArrayList<>();
		TeachEvaluateItem item = null;
		TeachEvaluateItemOption option = null;
		for (TeachEvaluateItem e : items) {
			item = new TeachEvaluateItem();
			item.setProjectId(project.getId());
			TeachEvaluateItem.copyItem(item, e);
			item.setId(UuidUtils.generateUuid());
			projectItem.add(item);
			for (TeachEvaluateItemOption o : e.getOptionList()) {
				option = new TeachEvaluateItemOption();
				TeachEvaluateItemOption.copyOption(option, o);
				option.setId(UuidUtils.generateUuid());
				option.setItemId(item.getId());
				projectOption.add(option);
			}
		}
		if(CollectionUtils.isNotEmpty(projectItem)){
			teachEvaluateItemService.saveAll(projectItem.toArray(new TeachEvaluateItem[0]));
		}
		if(CollectionUtils.isNotEmpty(projectOption)){
			teachEvaluateItemOptionService.saveAll(projectOption.toArray(new TeachEvaluateItemOption[0]));
		}
		String gradeIds=project.getGradeIds();
		if(StringUtils.isNotBlank(gradeIds)){
			List<TeachEvaluateRelation> relationList=new ArrayList<TeachEvaluateRelation>();
			TeachEvaluateRelation relation=null;
			for(String gradeId:gradeIds.split(",")){
				relation=new TeachEvaluateRelation();
				relation.setId(UuidUtils.generateUuid());
				relation.setProjectId(project.getId());
				relation.setValueId(gradeId);
				relationList.add(relation);
			}
			teachEvaluateRelationService.saveAll(relationList.toArray(new TeachEvaluateRelation[0]));
		}
	}
	
	@Override
	public void deleteById(String projectId) {
		delete(projectId);
		teachEvaluateResultService.deleteByProjectId(projectId);
		teachEvaluateResultStatService.deleteByProjectId(projectId);
		// 删除项目对应的指标数据
		teachEvaluateItemService.deleteByProjectId(projectId);
	}
	
	@Override
	public void updatePorject(TeachEvaluateProject project) {
		teachEvaluateProjectDao.update(project, new String[]{"projectName"});
	}
	
	@Override
	public List<Student> findByStuSubList(String projectId, Set<String> classIds,
			String selectType, String selectObj, Pagination page) {
		TeachEvaluateProject project = findOne(projectId);
//		List<TeachEvaluateResult> resultlist = teachEvaluateResultService.findBySubmitAndInProjectId(new String[]{project.getId()});
//		Set<String> stuIds = EntityUtils.getSet(resultlist, TeachEvaluateResult::getOperatorId);
		Set<String> stuIds = teachEvaluateResultService.getStuIdByProjectId(projectId, null, null, null);
		/*if(classIds == null){
			classIds = EntityUtils.getSet(resultlist, "classId");
		}*/
		Student searchStudent = new Student();
		if (("1").equals(selectType)) {
        	// 学号模糊查询
        	if(StringUtils.isNotBlank(selectObj)){
        		//防止注入
        		String linStr = selectObj.replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentCode(linStr+ "%");
        	}
        }
        else {
        	// 姓名
        	if(StringUtils.isNotBlank(selectObj)){
        		//防止注入
        		String linStr = selectObj.replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentName(linStr+ "%");
        	}
        }
		
		List<Student> stulist = Student.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(project.getUnitId(),
				stuIds.toArray(new String[0]), classIds==null?null:classIds.toArray(new String[0]), Json.toJSONString(searchStudent), SUtils.s(page)), page);
		if(CollectionUtils.isNotEmpty(stulist)){
			Set<String> clsIds = EntityUtils.getSet(stulist, "classId");
			List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>(){});
			Map<String,Clazz> clsMap = EntityUtils.getMap(clsList, "id");
			for(Student stu : stulist){
				stu.setClassName(clsMap.containsKey(stu.getClassId())?clsMap.get(stu.getClassId()).getClassNameDynamic():"无班级!");
			}
		}
		return stulist;
	}
	
	@Override
	public List<Student> findByStuNoSubList(String projectId,
			Set<String> clsIds, String selectType, String selectObj,
			Pagination page) {
		TeachEvaluateProject project = findOne(projectId);
//		List<TeachEvaluateResult> resultlist = teachEvaluateResultService.findBySubmitAndInProjectId(new String[]{project.getId()});
//		Set<String> subStuIds = EntityUtils.getSet(resultlist, "operatorId");
		Set<String> subStuIds = teachEvaluateResultService.getStuIdByProjectId(projectId, null, null, null);
		Student searchStudent = new Student();
		if (("1").equals(selectType)) {
        	// 学号模糊查询
        	if(StringUtils.isNotBlank(selectObj)){
        		//防止注入
        		String linStr = selectObj.replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentCode(linStr+ "%");
        	}
        }
        else {
        	// 姓名
        	if(StringUtils.isNotBlank(selectObj)){
        		//防止注入
        		String linStr = selectObj.replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentName(linStr+ "%");
        	}
        }
		List<Clazz> clazzlist = SUtils.dt(classRemoteService.findByIdCurAcadyear(project.getUnitId(), project.getAcadyear()), new TR<List<Clazz>>(){});
		Set<String> classIds = EntityUtils.getSet(clazzlist, "id");
		if(clsIds == null){
			clsIds = classIds;
		}
		Map<String,List<Student>> stuClsMap = new HashMap<String, List<Student>>();
		List<Student> stulist = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>(){});
		for (Student stu : stulist) {
			if(!stuClsMap.containsKey(stu.getClassId())){
				stuClsMap.put(stu.getClassId(), new ArrayList<Student>());
			}
			stuClsMap.get(stu.getClassId()).add(stu);
		}
		Set<String> noSubStuIds = new HashSet<String>();
		Set<String> stuIds = new HashSet<String>();
		//关系表
		List<TeachEvaluateRelation> relationList=teachEvaluateRelationService.findByProjectIds(new String[]{projectId});
		if(CollectionUtils.isNotEmpty(relationList)){
			if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType())
							|| StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE, project.getEvaluateType())){
				// 教学 选修课评教
				Set<String> reClassIds=new HashSet<String>();//行政班ids
				Set<String> reTeaClassIds=new HashSet<String>();//教学班ids
				for(TeachEvaluateRelation relation:relationList){
					if(relation.getClassType().equals("1")){
						reClassIds.add(relation.getValueId());
					}else{
						reTeaClassIds.add(relation.getValueId());
					}
				}
				if(CollectionUtils.isNotEmpty(reClassIds)){
					stuIds.addAll(EntityUtils.getSet(SUtils.dt(studentRemoteService.findByClassIds(reClassIds.toArray(new String[0])),new TR<List<Student>>(){}), "id"));
				}
				if(CollectionUtils.isNotEmpty(reTeaClassIds)){
					reTeaClassIds.addAll(EntityUtils.getSet(SUtils.dt(teachClassRemoteService.findByParentIds(reTeaClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){}),"id"));
					stuIds.addAll(EntityUtils.getSet(SUtils.dt(teachClassStuRemoteService.findByClassIds(reTeaClassIds.toArray(new String[0])),new TR<List<TeachClassStu>>(){}), "studentId"));
				}
			}else if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACHER, project.getEvaluateType())
						||StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TOTOR, project.getEvaluateType())){
				//班主任 导师 评教
				Set<String> gradeIds=new HashSet<String>();//年级ids
				for(TeachEvaluateRelation relation:relationList){
					gradeIds.add(relation.getValueId());
				}
				List<Clazz> clazzList=SUtils.dt(classRemoteService.findByIdCurAcadyear(project.getUnitId(), project.getAcadyear()),new TR<List<Clazz>>(){});
				Set<String> reClassIds=new HashSet<String>();//行政班ids
				if(CollectionUtils.isNotEmpty(clazzList)){
					for(Clazz clazz:clazzList){
						if(gradeIds.contains(clazz.getGradeId())){
							reClassIds.add(clazz.getId());
						}
					}
				}
				if(CollectionUtils.isNotEmpty(reClassIds)){
					stuIds.addAll(EntityUtils.getSet(SUtils.dt(studentRemoteService.findByClassIds(reClassIds.toArray(new String[0])),new TR<List<Student>>(){}), "id"));
				}
			}
			for(String stuId : stuIds){
				if(CollectionUtils.isNotEmpty(subStuIds) && subStuIds.contains(stuId)){
				}else{
					noSubStuIds.add(stuId);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(noSubStuIds)){
			List<Student> stulist1=null;
			if(page==null){
				stulist1 = Student.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(project.getUnitId(),noSubStuIds.toArray(new String[0]), clsIds.toArray(new String[0]), Json.toJSONString(searchStudent),null));
			}else{
				stulist1 = Student.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(project.getUnitId(),
						noSubStuIds.toArray(new String[0]), clsIds.toArray(new String[0]), Json.toJSONString(searchStudent), SUtils.s(page)), page);
			}
			if(CollectionUtils.isNotEmpty(stulist1)){
				Set<String> clsIds2 = EntityUtils.getSet(stulist1, "classId");
				List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIds2.toArray(new String[0])), new TR<List<Clazz>>(){});
				Map<String,Clazz> clsMap = EntityUtils.getMap(clsList, "id");
				for(Student stu : stulist1){
					stu.setClassName(clsMap.containsKey(stu.getClassId())?clsMap.get(stu.getClassId()).getClassNameDynamic():"无班级!");
				}
			}
			return stulist1;
		}else{
			return new ArrayList<Student>();
		}
		
	}
	public void setSubmitStuIdsMap(String unitId,String acadyear,String semester,Map<String,Set<String>> teaClaIdOfStuIdsMap,
			Map<String,Set<String>> claIdOfStuIdsMap,Map<String,Set<String>> gradeIdOfStuIdsMap){
		//获取学年学期下的教学班
		List<TeachClass> teachClassList=SUtils.dt(teachClassRemoteService.findTeachClassList(unitId, acadyear, semester, null, null,true),new TR<List<TeachClass>>(){});
		if(CollectionUtils.isNotEmpty(teachClassList)){
			Set<String> teachClassIds=EntityUtils.getSet(teachClassList, "id");
			teachClassIds.addAll(EntityUtils.getSet(SUtils.dt(teachClassRemoteService.findByParentIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){}), "id"));
			List<TeachClass> allteachClaList=SUtils.dt(teachClassRemoteService.findListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
			//大班id  对应的所有小班ids map
			if(CollectionUtils.isNotEmpty(allteachClaList)){
				List<TeachClassStu> teaStuList=SUtils.dt(teachClassStuRemoteService.findByClassIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClassStu>>(){});
				//教学班id 对应的学生数
				Map<String,Set<String>> map1=new HashMap<String,Set<String>>(); 
				if(CollectionUtils.isNotEmpty(teaStuList)){
					for(TeachClassStu teaStu:teaStuList){
						if(!map1.containsKey(teaStu.getClassId())){
							map1.put(teaStu.getClassId(), new HashSet<String>());
						}
						map1.get(teaStu.getClassId()).add(teaStu.getStudentId());
					}
				}
				//获取对应的
				for(TeachClass teachClass:allteachClaList){
					String id=teachClass.getParentId();
					if(StringUtils.isEmpty(id)){
						id=teachClass.getId();
					}
					if(!teaClaIdOfStuIdsMap.containsKey(id)){
						teaClaIdOfStuIdsMap.put(id, new HashSet<String>());
					}
					if(map1.get(teachClass.getId())!=null){
						teaClaIdOfStuIdsMap.get(id).addAll(map1.get(teachClass.getId()));
					}
				}
			}
		}
		
		//获取行政班
		List<Clazz> clazzList=SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear),new TR<List<Clazz>>(){});
		Set<String> classIds=EntityUtils.getSet(clazzList, "id");
		Map<String,String> claIdOfGraIdMap=EntityUtils.getMap(clazzList, "id", "gradeId");
		List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
		if(CollectionUtils.isNotEmpty(studentList)){
			for(Student stu:studentList){
				if(!claIdOfStuIdsMap.containsKey(stu.getClassId())){
					claIdOfStuIdsMap.put(stu.getClassId(), new HashSet<String>());
				}
				claIdOfStuIdsMap.get(stu.getClassId()).add(stu.getId());
			}
		}
		//获取年级对应的map
		for(Entry<String, String> entry:claIdOfGraIdMap.entrySet()){
			Set<String> studentIds=claIdOfStuIdsMap.get(entry.getKey());
			if(!gradeIdOfStuIdsMap.containsKey(entry.getValue())){
				gradeIdOfStuIdsMap.put(entry.getValue(), new HashSet<String>());
			}
			if(studentIds!=null){
				gradeIdOfStuIdsMap.get(entry.getValue()).addAll(studentIds);
			}
		}
	}
	private List<TeachEvaluateProject> statSubmitNum(String unitId,String acadyear,String semester,List<TeachEvaluateProject> projects){
		Set<String> projectIds = EntityUtils.getSet(projects, TeachEvaluateProject::getId);
		// 统计已提交未提交人数
		//<projectId,Set<StudentId>>
		Map<String,Set<String>> resultMap = new HashMap<>();
//		List<TeachEvaluateResult> results = teachEvaluateResultService.findBySubmitAndInProjectId(projectIds.toArray(new String[0]));
//		for (TeachEvaluateResult result : results) {
//			if(!resultMap.containsKey(result.getProjectId())){
//				resultMap.put(result.getProjectId(), new HashSet<String>());
//			}
//			resultMap.get(result.getProjectId()).add(result.getOperatorId());
//		}
		for (String id : projectIds) {
			Set<String> stuIds = teachEvaluateResultService.getStuIdByProjectId(id,null,null,null);
			if(CollectionUtils.isNotEmpty(stuIds)) {
				resultMap.put(id, stuIds);
			}
		}
		
		//获取项目projectId 对应的关系数据
		List<TeachEvaluateRelation> relationList=teachEvaluateRelationService.findByProjectIds(projectIds.toArray(new String[0]));
		Map<String,List<TeachEvaluateRelation>> relationMap=new HashMap<String,List<TeachEvaluateRelation>>();
		if(CollectionUtils.isNotEmpty(relationList)){
			for(TeachEvaluateRelation relation:relationList){
				if(!relationMap.containsKey(relation.getProjectId())){
					relationMap.put(relation.getProjectId(), new ArrayList<TeachEvaluateRelation>());
				}
				relationMap.get(relation.getProjectId()).add(relation);
			}
		}
		//教学班 班级id 对应的所有学生ids
		Map<String,Set<String>> teaClaIdOfStuIdsMap=RedisUtils.getObject(unitId+acadyear+semester+"teaClaIdOfStuIdsMap",new TR<Map<String,Set<String>>>(){});
		//行政班 班级id 对应的所有学生ids
		Map<String,Set<String>> claIdOfStuIdsMap=RedisUtils.getObject(unitId+acadyear+semester+"claIdOfStuIdsMap",new TR<Map<String,Set<String>>>(){});
		//年级id 对应的所有学生ids
		Map<String,Set<String>> gradeIdOfStuIdsMap=RedisUtils.getObject(unitId+acadyear+semester+"gradeIdOfStuIdsMap",new TR<Map<String,Set<String>>>(){});
		if(teaClaIdOfStuIdsMap==null || claIdOfStuIdsMap==null || gradeIdOfStuIdsMap==null){
			teaClaIdOfStuIdsMap=new HashMap<String,Set<String>>();
			claIdOfStuIdsMap=new HashMap<String,Set<String>>();
			gradeIdOfStuIdsMap=new HashMap<String,Set<String>>();
			setSubmitStuIdsMap(unitId, acadyear, semester, teaClaIdOfStuIdsMap, claIdOfStuIdsMap, gradeIdOfStuIdsMap);
			RedisUtils.setObject(unitId+acadyear+semester+"teaClaIdOfStuIdsMap", teaClaIdOfStuIdsMap,RedisUtils.TIME_ONE_MINUTE);
			RedisUtils.setObject(unitId+acadyear+semester+"claIdOfStuIdsMap", claIdOfStuIdsMap,RedisUtils.TIME_ONE_MINUTE);
			RedisUtils.setObject(unitId+acadyear+semester+"gradeIdOfStuIdsMap", gradeIdOfStuIdsMap,RedisUtils.TIME_ONE_MINUTE);
		}
		
		for (TeachEvaluateProject project : projects) {
			int noSubmit = 0;
			int submit = 0;
			Set<String> subStuIds = resultMap.get(project.getId());
			if(CollectionUtils.isNotEmpty(subStuIds)){
				submit = subStuIds.size();
			}else{
				submit = 0;
			}
			List<TeachEvaluateRelation> inList=relationMap.get(project.getId());
			Set<String> stuIds = new HashSet<String>();
			if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType())
					|| StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE, project.getEvaluateType())){
				// 教学 选修课 评教
				if(CollectionUtils.isNotEmpty(inList)){
					for(TeachEvaluateRelation in:inList){
						if(in.getClassType().equals("1")){//行政班
							if(claIdOfStuIdsMap.get(in.getValueId())!=null){
								stuIds.addAll(claIdOfStuIdsMap.get(in.getValueId()));
							}
						}else{
							if(teaClaIdOfStuIdsMap.get(in.getValueId())!=null){
								stuIds.addAll(teaClaIdOfStuIdsMap.get(in.getValueId()));
							}
						}
					}
				}
			}else if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACHER, project.getEvaluateType())
					|| StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TOTOR, project.getEvaluateType())){
				//班主任导师评教
				if(CollectionUtils.isNotEmpty(inList)){
					for(TeachEvaluateRelation in:inList){
						if(gradeIdOfStuIdsMap.containsKey(in.getValueId()) && gradeIdOfStuIdsMap.get(in.getValueId())!=null){
							stuIds.addAll(gradeIdOfStuIdsMap.get(in.getValueId()));
						}
					}
				}
			}
			for(String stuId : stuIds){
				if(CollectionUtils.isNotEmpty(subStuIds) && subStuIds.contains(stuId)){
//					submit++;
				}else{
					noSubmit++;
				}
			}
			project.setNoSubmitNum(noSubmit);
			project.setSubmitNum(submit);
		}
		return projects;
	}
	@Override
	public List<TeachEvaluateProject> findByCon(String unitId, String acadyear, String semester) {
		List<TeachEvaluateProject> projects =  teachEvaluateProjectDao.findByUnitIdAndAcadyearAndSemester(unitId, acadyear, semester);
		return projects;
	}
	@Override
	protected BaseJpaRepositoryDao<TeachEvaluateProject, String> getJpaDao() {
		return teachEvaluateProjectDao;
	}

	@Override
	protected Class<TeachEvaluateProject> getEntityClass() {
		return TeachEvaluateProject.class;
	}

}
