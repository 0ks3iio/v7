package net.zdsoft.evaluation.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseTypeRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dao.TeachEvaluateRelationDao;
import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;
import net.zdsoft.evaluation.data.service.TeachEvaluateRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teachEvaluateRelationService")
public class TeachEvaluateRelationServiceImpl extends BaseServiceImpl<TeachEvaluateRelation, String> implements TeachEvaluateRelationService{

	@Autowired
	private TeachEvaluateRelationDao teachEvaluateRelationDao;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private CourseTypeRemoteService courseTypeRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private ClassTeachingRemoteService  classTeachingRemoteService;
	
	@Override
	public List<TeachEvaluateRelation> getRelationList(String unitId,String projectId,String acadyear,
			String semester,String evaluateType,String gradeId){
		//最终结果
		List<TeachEvaluateRelation> relationList=new ArrayList<TeachEvaluateRelation>();
		//已选的参评班级
		List<TeachEvaluateRelation> haveRList=teachEvaluateRelationDao.findByProjectId(projectId);
		//key-valueId+subjectId    value-id(为获取已选参评的id)
		Map<String,String> relationMap=new HashMap<String,String>();
		Set<String> valueIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(haveRList)){
			for(TeachEvaluateRelation relation:haveRList){
				valueIds.add(relation.getValueId()+"_"+relation.getSubjectId());
				relationMap.put(relation.getValueId()+"_"+relation.getSubjectId(),relation.getId());
			}
		}
		//年级名称map
		Map<String,String> gradeNameMap=EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId,acadyear), new TR<List<Grade>>(){}),"id","gradeName");
		//TODO
		//获取行政班（非教学班形式）
		List<Clazz> clazzList=null;
		if(StringUtils.isBlank(gradeId)){
			clazzList=SUtils.dt(classRemoteService.findBySchoolIdCurAcadyear(unitId,acadyear),new TR<List<Clazz>>(){});
		}else{
			clazzList=SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId),new TR<List<Clazz>>(){});
		}
		Set<String> classIds=EntityUtils.getSet(clazzList,Clazz::getId);
		Map<String,Clazz> classMap=EntityUtils.getMap(clazzList, "id");
		List<ClassTeaching> classTeachingList=new ArrayList<ClassTeaching>();
		if(evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_TEACH) && CollectionUtils.isNotEmpty(classIds)){//只有教学调查 才有行政班的情况
			classTeachingList=SUtils.dt(classTeachingRemoteService.findBySearch(unitId, acadyear, semester,classIds.toArray(new String[0]), 0, 0),new TR<List<ClassTeaching>>(){});
		}
		//获取教学班
		String[] gradeIds=StringUtils.isNotBlank(gradeId)?new String[]{gradeId}:null;
		List<TeachClass> teachClassList=SUtils.dt(teachClassRemoteService.findTeachClassList(unitId, acadyear, semester, null,gradeIds,true),new TR<List<TeachClass>>(){});
		Set<String> courseIds=EntityUtils.getSet(classTeachingList,"subjectId");
		courseIds.addAll(EntityUtils.getSet(teachClassList,"courseId"));
		//key-courseId
		Map<String,String> courseIdOfTypeMap=new HashMap<String, String>();
		Map<String,String> courseNameMap=new HashMap<String, String>();
		List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
		//所属学科
		Map<String,String> courseTypeMap=EntityUtils.getMap(SUtils.dt(courseTypeRemoteService.findListByIds(EntityUtils.getSet(courseList,"courseTypeId")
				.toArray(new String[0])),new TR<List<CourseType>>(){}),"id","name");
		for(Course course:courseList){
			courseIdOfTypeMap.put(course.getId(), courseTypeMap.get(course.getCourseTypeId()));
			courseNameMap.put(course.getId(), course.getSubjectName());
		}
		TeachEvaluateRelation relation=null;
		if(CollectionUtils.isNotEmpty(classTeachingList)){
			//组装数据
			for(ClassTeaching classTeaching:classTeachingList){
				relation=new TeachEvaluateRelation();
				relation.setValueId(classTeaching.getClassId()+"_"+classTeaching.getSubjectId()+"_"+"1");//1 行政班
				Clazz clazz=classMap.get(classTeaching.getClassId());
				if(clazz!=null){
					relation.setTeachClassName(gradeNameMap.get(clazz.getGradeId())+clazz.getClassName());
					relation.setGradeName(gradeNameMap.get(clazz.getGradeId()));
				}
				relation.setCourseName(courseNameMap.get(classTeaching.getSubjectId()));
				relation.setCourseTypeName(courseIdOfTypeMap.get(classTeaching.getSubjectId()));
				if(valueIds.contains(classTeaching.getClassId()+"_"+classTeaching.getSubjectId())){
					relation.setId(relationMap.get(classTeaching.getClassId()+"_"+classTeaching.getSubjectId()));
					relation.setHaveSelected(true);
				}else{
					relation.setHaveSelected(false);//默认false
				}
				relationList.add(relation);
			}
		}
		
		if(CollectionUtils.isNotEmpty(teachClassList)){
			//组装数据
			for(TeachClass teachClass:teachClassList){
				//教学调查 去除选修课
				if(evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_TEACH) && teachClass.getClassType().equals("2") ){
					continue;
				}
				//选修课调查 去除非选修课
				if(evaluateType.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE) && !teachClass.getClassType().equals("2") ){
					continue;
				}
				relation=new TeachEvaluateRelation();
				relation.setValueId(teachClass.getId()+"_"+teachClass.getCourseId()+"_"+"2");//2 教学班
				relation.setTeachClassName(teachClass.getName());
				relation.setCourseName(courseNameMap.get(teachClass.getCourseId()));
				relation.setCourseTypeName(courseIdOfTypeMap.get(teachClass.getCourseId()));
				if(StringUtils.isNotBlank(teachClass.getGradeId())){
					String[] gradeIdarr=teachClass.getGradeId().split(",");
					String gradeName="";
					for(String gradeIda:gradeIdarr){
						gradeName+=gradeNameMap.get(gradeIda)+" ";
					}
					relation.setGradeName(gradeName);
				}
				if(valueIds.contains(teachClass.getId()+"_"+teachClass.getCourseId())){
					relation.setId(relationMap.get(teachClass.getId()+"_"+teachClass.getCourseId()));
					relation.setHaveSelected(true);
				}else{
					relation.setHaveSelected(false);//默认false
				}
				relationList.add(relation);
			}
			Collections.sort(relationList, new Comparator<TeachEvaluateRelation>() {
				@Override
				public int compare(TeachEvaluateRelation o1,
						TeachEvaluateRelation o2) {
					if(StringUtils.isNotBlank(o1.getGradeName()) && StringUtils.isNotBlank(o2.getGradeName())){
						if(o1.getGradeName().compareTo(o2.getGradeName())==0){
							return o1.getTeachClassName().compareTo(o2.getTeachClassName());
						}else{
							return o1.getGradeName().compareTo(o2.getGradeName());
						}
					}
					return 0;
				}
			});
		}
		return relationList;
	}
	@Override
	public void saveRelations(String valueIds,String noCheckIds,String projectId){
		if(StringUtils.isNotBlank(valueIds)){
			String[] valueIdarr=valueIds.split(",");
			List<TeachEvaluateRelation> insertList=new ArrayList<TeachEvaluateRelation>();
			TeachEvaluateRelation relation=null;
			for(String valueId:valueIdarr){
				String[] sth=valueId.split("_");
				relation=new TeachEvaluateRelation();
				if(sth.length<4 || StringUtils.isBlank(sth[3])){
					relation.setId(UuidUtils.generateUuid());
				}else{
					relation.setId(sth[3]);
				}
				relation.setProjectId(projectId);
				relation.setValueId(sth[0]);
				relation.setSubjectId(sth[1]);
				relation.setClassType(sth[2]);
				insertList.add(relation);
			}
			saveAll(insertList.toArray(new TeachEvaluateRelation[0]));
		}
		if(StringUtils.isNotBlank(noCheckIds)){
			Set<String> deleteIds=new HashSet<String>();
			for(String noCheckId:noCheckIds.split(",")){
				String[] sth=noCheckId.split("_");
				if(sth.length>=4){
					deleteIds.add(sth[3]);
				}
			}
			if(CollectionUtils.isNotEmpty(deleteIds)){
				teachEvaluateRelationDao.deleteByIdIn(deleteIds.toArray(new String[0]));
			}
		}
	}
	@Override
	public List<TeachEvaluateRelation> findByProjectIds(String[] projectIds){
		return teachEvaluateRelationDao.findByProjectIdIn(projectIds);
	}
	@Override
	public Map<String,Set<String>> findMapByProjectIds(String[] projectIds){
		List<TeachEvaluateRelation> relationList=teachEvaluateRelationDao.findByProjectIdIn(projectIds);
		Map<String,Set<String>> map=new HashMap<String,Set<String>>();
		if(CollectionUtils.isNotEmpty(relationList)){
			for(TeachEvaluateRelation relation:relationList){
				if(!map.containsKey(relation.getProjectId())){
					map.put(relation.getProjectId(), new HashSet<String>());
				}
				if(StringUtils.isNotBlank(relation.getSubjectId())){
					map.get(relation.getProjectId()).add(relation.getValueId()+"_"+relation.getSubjectId());
				}else{
					map.get(relation.getProjectId()).add(relation.getValueId());
				}
			}
		}
		return map;
	}
	@Override
	protected BaseJpaRepositoryDao<TeachEvaluateRelation, String> getJpaDao() {
		return teachEvaluateRelationDao;
	}

	@Override
	protected Class<TeachEvaluateRelation> getEntityClass() {
		return TeachEvaluateRelation.class;
	}

}
