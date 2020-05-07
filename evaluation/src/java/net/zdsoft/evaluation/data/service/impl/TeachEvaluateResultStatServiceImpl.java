package net.zdsoft.evaluation.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dao.TeachEvaluateResultStatDao;
import net.zdsoft.evaluation.data.dto.EvaluateTableDto;
import net.zdsoft.evaluation.data.dto.ResultStatDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResultStat;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.evaluation.data.service.TeachEvaluateProjectService;
import net.zdsoft.evaluation.data.service.TeachEvaluateRelationService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultStatService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("teachEvaluateResultStatService")
public class TeachEvaluateResultStatServiceImpl extends BaseServiceImpl<TeachEvaluateResultStat, String> implements TeachEvaluateResultStatService {
	@Autowired
	private TeachEvaluateResultStatDao teachEvaluateResultStatDao;
	@Autowired
	private TeachEvaluateResultService teachEvaluateResultService;
	@Autowired
	private TeachEvaluateProjectService teachEvaluateProjectSerivce;
	@Autowired
	private TeachEvaluateItemService teachEvaluateItemService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeachEvaluateRelationService teachEvaluateRelationService;
	
	@Override
	public List<ResultStatDto> getResultSubStat(String projectId,
			String statDimension, String teacherId, String subId) {
		List<ResultStatDto> dtolist = new ArrayList<ResultStatDto>();
		List<TeachEvaluateResultStat> statlist = new ArrayList<>();
		if(StringUtils.isNotBlank(teacherId)&&StringUtils.isNotBlank(subId)) {
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatSubjectIdAndStatTeacherId(projectId,statDimension,subId,teacherId);
		}else if(StringUtils.isNotBlank(teacherId)) {
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatTeacherId(projectId,statDimension,teacherId);
		}else if(StringUtils.isNotBlank(subId)) {
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatSubjectId(projectId,statDimension,subId);
		}else {
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,statDimension);
		}
		if(CollectionUtils.isNotEmpty(statlist)){
			Map<String,ResultStatDto> dtoMap = new HashMap<String, ResultStatDto>();
			Map<String,Float> countScoreMap = new HashMap<String, Float>();
			for(TeachEvaluateResultStat stat : statlist){
				if(!dtoMap.containsKey(stat.getStatTeacherId()+","+stat.getStatSubjectId())){
					ResultStatDto dto = new ResultStatDto();
					dto.setSubId(stat.getStatSubjectId());
					dto.setSubName(stat.getStatSubjectName());
					dto.setTeaId(stat.getStatTeacherId());
					dto.setTeaName(stat.getStatTeacherName());
					dtoMap.put(stat.getStatTeacherId()+","+stat.getStatSubjectId(), dto);
					countScoreMap.put(stat.getStatTeacherId()+","+stat.getStatSubjectId(), 0f);
				}
				ResultStatDto dto1 = dtoMap.get(stat.getStatTeacherId()+","+stat.getStatSubjectId());
				if(StringUtils.isBlank(stat.getOptionId())){
					//是指标成绩（百分比或者成绩）
					if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, stat.getItemType())){
						dto1.getItemScoreMap().put(stat.getItemId(), stat.getScore());
						countScoreMap.put(stat.getStatTeacherId()+","+stat.getStatSubjectId(), countScoreMap.get(stat.getStatTeacherId()+","+stat.getStatSubjectId())+stat.getScore());
					}
				}else{
					if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, stat.getItemType())){
						dto1.getItemScoreMap().put(stat.getOptionId(), stat.getScore());
						countScoreMap.put(stat.getStatTeacherId()+","+stat.getStatSubjectId(), countScoreMap.get(stat.getStatTeacherId()+","+stat.getStatSubjectId())+stat.getScore());
					}else{
						dto1.getItemScoreMap().put(stat.getOptionId(), stat.getBfb());
					}
				}
			}
			for (ResultStatDto dto : dtoMap.values()) {
				dto.setCountScore(countScoreMap.get(dto.getTeaId()+","+dto.getSubId()));
				dtolist.add(dto);
			}
		}
		if(CollectionUtils.isNotEmpty(dtolist)){
			Collections.sort(dtolist, new Comparator<ResultStatDto>() {
				@Override
				public int compare(ResultStatDto o1, ResultStatDto o2) {
					return (o1.getTeaName()+","+o1.getSubName()).compareTo(o2.getTeaName()+","+o2.getSubName());
				}
			});
		}
		return dtolist;
	}
	@Override
	public List<ResultStatDto> getResultStatElectiveDto(String projectId,
			String statDimension) {
		List<ResultStatDto> dtolist = new ArrayList<ResultStatDto>();
		List<TeachEvaluateResultStat> statlist  =teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,statDimension);
		if(CollectionUtils.isNotEmpty(statlist)){
			Map<String,ResultStatDto> dtoMap = new HashMap<String, ResultStatDto>();
			Map<String,Float> countScoreMap = new HashMap<String, Float>();
			for(TeachEvaluateResultStat stat : statlist){
				if(!dtoMap.containsKey(stat.getStatSubjectId()+","+stat.getStatTeacherId())){
					ResultStatDto dto = new ResultStatDto();
					dto.setSubId(stat.getStatSubjectId());
					dto.setSubName(stat.getStatSubjectName());
					dto.setTeaId(stat.getStatTeacherId());
					dto.setTeaName(stat.getStatTeacherName());
					dtoMap.put(stat.getStatSubjectId()+","+stat.getStatTeacherId(), dto);
					countScoreMap.put(stat.getStatSubjectId()+","+stat.getStatTeacherId(), 0f);
				}
				ResultStatDto dto1 = dtoMap.get(stat.getStatSubjectId()+","+stat.getStatTeacherId());
				if(StringUtils.isBlank(stat.getOptionId())){
					//是指标成绩（百分比或者成绩）
					if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, stat.getItemType())){
						dto1.getItemScoreMap().put(stat.getItemId(), stat.getScore());
						countScoreMap.put(stat.getStatSubjectId()+","+stat.getStatTeacherId(), countScoreMap.get(stat.getStatSubjectId()+","+stat.getStatTeacherId())+stat.getScore());
					}
				}else{
					if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, stat.getItemType())){
						dto1.getItemScoreMap().put(stat.getOptionId(), stat.getScore());
						countScoreMap.put(stat.getStatSubjectId()+","+stat.getStatTeacherId(), countScoreMap.get(stat.getStatSubjectId()+","+stat.getStatTeacherId())+stat.getScore());
					}else{
						dto1.getItemScoreMap().put(stat.getOptionId(), stat.getBfb());
					}
				}
			}
			for (ResultStatDto dto : dtoMap.values()) {
				dto.setCountScore(countScoreMap.get(dto.getSubId()+","+dto.getTeaId()));
				dtolist.add(dto);
			}
		}
		if(CollectionUtils.isNotEmpty(dtolist)){
			Collections.sort(dtolist, new Comparator<ResultStatDto>() {
				@Override
				public int compare(ResultStatDto o1, ResultStatDto o2) {
					return (o1.getSubName()+","+o1.getTeaName()).compareTo(o2.getSubName()+","+o2.getTeaName());
				}
			});
		}
		return dtolist;
	}
	
	@Override
	public Map<String, Float> getResultStatAllDto(String projectId,String evaluateType) {
		Map<String,Float> itemScoreMap = new HashMap<String, Float>();
		List<TeachEvaluateResultStat> statlist = new ArrayList<>();
		if(StringUtils.equals(evaluateType, EvaluationConstants.EVALUATION_TYPE_TEACHER)) {
			//班主任
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_CLASS);
		}else {
			//导师
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_ONE);
		}
		if(CollectionUtils.isEmpty(statlist)){
			return itemScoreMap;
		}
		for (TeachEvaluateResultStat stat : statlist) {
			if(StringUtils.equals(evaluateType, EvaluationConstants.EVALUATION_TYPE_TEACHER)) {
				if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
					itemScoreMap.put(stat.getOptionId()+"_"+stat.getStatTeacherId()+"_"+stat.getStatClassId(), stat.getBfb());
				}else{
					if(StringUtils.isBlank(stat.getOptionId())){
						itemScoreMap.put(stat.getItemId()+"_"+stat.getStatTeacherId()+"_"+stat.getStatClassId(), stat.getScore());
					}else{
						itemScoreMap.put(stat.getOptionId()+"_"+stat.getStatTeacherId()+"_"+stat.getStatClassId(), stat.getBfb());
					}
				}
			}else {
				if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
					itemScoreMap.put(stat.getOptionId()+"_"+stat.getStatTeacherId(), stat.getBfb());
				}else{
					if(StringUtils.isBlank(stat.getOptionId())){
						itemScoreMap.put(stat.getItemId()+"_"+stat.getStatTeacherId(), stat.getScore());
					}else{
						itemScoreMap.put(stat.getOptionId()+"_"+stat.getStatTeacherId(), stat.getBfb());
					}
				}
			}
			
		}
	
		//TODO
		return itemScoreMap;
	}
	@Override
	public Map<String, Float> getResultStatTeaDto(String projectId, String teaId) {
		Map<String,Float> itemScoreMap = new HashMap<>();
		if(StringUtils.isBlank(teaId)){
			return itemScoreMap;
		}
		List<TeachEvaluateResultStat> statTealist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatTeacherId(projectId,EvaluationConstants.STATE__DIMENSION_ONE,teaId);
		if(CollectionUtils.isEmpty(statTealist)){
			return itemScoreMap;
		}else{
			for (TeachEvaluateResultStat stat : statTealist) {
				if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
					itemScoreMap.put(stat.getOptionId(), stat.getBfb());
				}else{
					if(StringUtils.isBlank(stat.getOptionId())){
						itemScoreMap.put(stat.getItemId(), stat.getScore());
					}else{
						itemScoreMap.put(stat.getOptionId(), stat.getBfb());
					}
				}
			}
		}
		return itemScoreMap;
	}
	@Override
	public Map<String, Float> getResultStatTeaClsDto(String projectId, String clsId) {
		Map<String,Float> itemScoreMap = new HashMap<String, Float>();
		if(StringUtils.isBlank(clsId)){
			return itemScoreMap;
		}
		List<TeachEvaluateResultStat> statClslist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatClassId(projectId,EvaluationConstants.STATE__DIMENSION_CLASS,clsId);
		if(CollectionUtils.isEmpty(statClslist)){
			return itemScoreMap;
		}else{
			for (TeachEvaluateResultStat stat : statClslist) {
				if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
					itemScoreMap.put(stat.getOptionId(), stat.getBfb());
				}else{
					if(StringUtils.isBlank(stat.getOptionId())){
						itemScoreMap.put(stat.getItemId(), stat.getScore());
					}else{
						itemScoreMap.put(stat.getOptionId(), stat.getBfb());
					}
				}
			}
		}
		return itemScoreMap;
	}
	
	@Override
	public List<ResultStatDto> findTeaRankBy(String projectId, String evaluateType) {
		List<ResultStatDto> dtolist = new ArrayList<ResultStatDto>();
		Map<String,Float> statMap = teachEvaluateResultService.findTeaRankBy(projectId,evaluateType);
		Set<String> teaClsIdSet = statMap.keySet();
		Set<String> teaIds = new HashSet<>();
		Set<String> clsIds = new HashSet<>();
		for (String teaCls : teaClsIdSet) {
			String[] teaClsId = teaCls.split(",");
			teaIds.add(teaClsId[0]);
			clsIds.add(teaClsId[1]);
		}
		Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])),new TR<List<Teacher>>(){}), "id");
		List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>(){});
		Map<String,Clazz> clsMap = EntityUtils.getMap(clsList, "id");
		ResultStatDto dto;
		for(String teaCls : teaClsIdSet) {
			dto = new ResultStatDto();
			String[] teaClsId = teaCls.split(",");
			dto.setTeaId(teaClsId[0]);
			dto.setTeaName(teaMap.containsKey(teaClsId[0])?teaMap.get(teaClsId[0]).getTeacherName():"");
			dto.setCountScore(statMap.get(teaCls));
			if(StringUtils.equals(evaluateType, EvaluationConstants.EVALUATION_TYPE_TEACHER)) {
				dto.setClsId(teaClsId[1]);
				dto.setClsName(clsMap.containsKey(teaClsId[1])?clsMap.get(teaClsId[1]).getClassNameDynamic():"");
			}
			dtolist.add(dto);
		}
		Collections.sort(dtolist, new Comparator<ResultStatDto>() {
			@Override
			public int compare(ResultStatDto o1, ResultStatDto o2) {
				// TODO Auto-generated method stub
				if(o1.getCountScore()>o2.getCountScore()) {
					return -1;
				}else if(o1.getCountScore()<o2.getCountScore()) {
					return 1;
				}else {
					return 0;
				}
			}
		});
		int rank = 0;
		float rankScore = 0f;
		for(int i = 0;i<dtolist.size();i++) {
			ResultStatDto dto1  =dtolist.get(i);
			if(i == 0) {
				rankScore = dto1.getCountScore();
				rank = 1;
			}
			if(dto1.getCountScore() <rankScore) {
				rankScore = dto1.getCountScore();
				dto1.setRank(i+1);
				rank = i+1;
			}else {
				dto1.setRank(rank);
			}
		}
//		for(ResultStatDto dto1 : dtolist) {
//			if(dto1.getCountScore() <rankScore) {
//				dto1.setRank(rank+1);
//			}else {
//				dto1.setRank(rank);
//			}
//		}
		return dtolist;
	}
	
	@Override
	public Map<String,Float> getResultStatTeaSchDto(String projectId) {
		Map<String,Float> itemScoreMap = new HashMap<String, Float>();
		List<TeachEvaluateResultStat> statSchlist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_SCH);
		//school:全校 countScore 总分
		itemScoreMap.put("school,countScore", 0f);
		for(TeachEvaluateResultStat stat : statSchlist){
			if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				itemScoreMap.put("school,"+stat.getOptionId(), stat.getBfb());
			}else{
				if(StringUtils.isBlank(stat.getOptionId())){
					if(!itemScoreMap.containsKey("school,"+stat.getItemId())){
						itemScoreMap.put("school,"+stat.getItemId(), 0f);
					}
					itemScoreMap.put("school,countScore", itemScoreMap.get("school,countScore")+stat.getScore());
					itemScoreMap.put("school,"+stat.getItemId(), itemScoreMap.get("school,"+stat.getItemId())+stat.getScore());
				}else{
					if(!itemScoreMap.containsKey("school,"+stat.getOptionId())){
						itemScoreMap.put("school,"+stat.getOptionId(), 0f);
					}
					itemScoreMap.put("school,"+stat.getOptionId(), itemScoreMap.get("school,"+stat.getOptionId())+stat.getBfb());
				}
			}
		}
		List<TeachEvaluateResultStat> statGradelist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_GRADE);
		for(TeachEvaluateResultStat stat : statGradelist){
			if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				itemScoreMap.put(stat.getStatGradeId()+","+stat.getOptionId(), stat.getBfb());
			}else{
				if(StringUtils.isBlank(stat.getOptionId())){
					if(!itemScoreMap.containsKey(stat.getStatGradeId()+","+stat.getItemId())){
						itemScoreMap.put(stat.getStatGradeId()+","+stat.getItemId(), 0f);
					}
					itemScoreMap.put(stat.getStatGradeId()+","+stat.getItemId(), itemScoreMap.get(stat.getStatGradeId()+","+stat.getItemId())+stat.getScore());
					
					
					if(!itemScoreMap.containsKey(stat.getStatGradeId()+",countScore")){
						itemScoreMap.put(stat.getStatGradeId()+",countScore", 0f);
					}
					itemScoreMap.put(stat.getStatGradeId()+",countScore", itemScoreMap.get(stat.getStatGradeId()+",countScore")+stat.getScore());
				}else{
					if(!itemScoreMap.containsKey(stat.getStatGradeId()+","+stat.getOptionId())){
						itemScoreMap.put(stat.getStatGradeId()+","+stat.getOptionId(), 0f);
					}
					itemScoreMap.put(stat.getStatGradeId()+","+stat.getOptionId(), itemScoreMap.get(stat.getStatGradeId()+","+stat.getOptionId())+stat.getBfb());
				}
			}
		}
		return itemScoreMap;
	}
	
	@Override
	public List<ResultStatDto> getResultStatDto(String projectId,
			String statDimension, String subjectId, String classId,String teacherId) {
		List<ResultStatDto> dtolist = new ArrayList<>();
		List<TeachEvaluateResultStat> statlist  = new ArrayList<>();
		if(StringUtils.isNotBlank(subjectId) && StringUtils.isNotBlank(classId)){
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatSubjectIdAndStatClassId(projectId,statDimension,subjectId,classId);
		}else if (StringUtils.isNotBlank(subjectId)){
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatSubjectId(projectId,statDimension,subjectId);
		}else if(StringUtils.isNotBlank(classId)){
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimensionAndStatClassId(projectId,statDimension,classId);
		}else{
			statlist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,statDimension);
		}
		if(StringUtils.isNotBlank(teacherId)) {
			for (int i = 0;i<statlist.size();i++) {
				TeachEvaluateResultStat e = statlist.get(i);
				if(!StringUtils.equals(teacherId, e.getStatTeacherId())) {
					statlist.remove(e);
					i--;
				}
			}
		}
		if(CollectionUtils.isNotEmpty(statlist)){
			Map<String,ResultStatDto> dtoMap = new HashMap<>();
			Map<String,Float> countScoreMap = new HashMap<>();
			for(TeachEvaluateResultStat stat : statlist){
				if(!dtoMap.containsKey(stat.getStatClassId()+","+stat.getStatSubjectId())){
					ResultStatDto dto = new ResultStatDto();
					dto.setClsId(stat.getStatClassId());
					dto.setClsName(stat.getStatClassName());
					dto.setSubId(stat.getStatSubjectId());
					dto.setSubName(stat.getStatSubjectName());
					dto.setTeaId(stat.getStatTeacherId());
					dto.setTeaName(stat.getStatTeacherName());
					dtoMap.put(stat.getStatClassId()+","+stat.getStatSubjectId(), dto);
					countScoreMap.put(stat.getStatClassId()+","+stat.getStatSubjectId(), 0f);
				}
				ResultStatDto dto1 = dtoMap.get(stat.getStatClassId()+","+stat.getStatSubjectId());
				if(StringUtils.isBlank(stat.getOptionId())){
					//是指标成绩（百分比或者成绩）
					if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, stat.getItemType())){
						dto1.getItemScoreMap().put(stat.getItemId(), stat.getScore());
						countScoreMap.put(stat.getStatClassId()+","+stat.getStatSubjectId(), countScoreMap.get(stat.getStatClassId()+","+stat.getStatSubjectId())+stat.getScore());
					}
				}else{
					if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, stat.getItemType())){
						dto1.getItemScoreMap().put(stat.getOptionId(), stat.getScore());
						countScoreMap.put(stat.getStatClassId()+","+stat.getStatSubjectId(), countScoreMap.get(stat.getStatClassId()+","+stat.getStatSubjectId())+stat.getScore());
					}else{
						dto1.getItemScoreMap().put(stat.getOptionId(), stat.getBfb());
					}
				}
			}
			for (ResultStatDto dto : dtoMap.values()) {
				dto.setCountScore(countScoreMap.get(dto.getClsId()+","+dto.getSubId()));
				dtolist.add(dto);
			}
		}
		if(CollectionUtils.isNotEmpty(dtolist)){
			Collections.sort(dtolist, new Comparator<ResultStatDto>() {
				@Override
				public int compare(ResultStatDto o1, ResultStatDto o2) {
					return (o1.getClsName()+","+o1.getSubName()).compareTo(o2.getClsName()+","+o2.getSubName());
				}
			});
		}
		return dtolist;
	}
	
	@Override
	public void statResult(String projectId) {
		TeachEvaluateProject project = teachEvaluateProjectSerivce.findOne(projectId);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		if(CollectionUtils.isEmpty(itemList)){
			return;
		}
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		// 先删除原有的统计结果
		teachEvaluateResultStatDao.deleteByProjectId(projectId);
		//开始统计
		if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType())){
			//教学调查统计
			statTeach(project,dto);
		}else if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACHER, project.getEvaluateType())){
			//班主任调查统计
			statTeacher(project,dto);
		}else if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TOTOR, project.getEvaluateType())){
			//导师统计
			statTotor(project,dto);
		}else if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE, project.getEvaluateType())){
			//选修课统计
			statElective(project,dto);
		}
	}
	//导师统计
	private void statTotor(TeachEvaluateProject project,EvaluateTableDto dto){
		/**
		 * 分3个维度统计，个人、年级、全校,
		 * 只需要统计评教项目和满意度即可，文本内容只作展示，不需要统计
		 * 评教项目统计折分和百分比都要计算，满意度统计百分比
		 */
		//得到各个年级对应的导师set
		Set<String> gradeTeaIds = teachEvaluateResultService.getResultGradeTeaId(project.getId());
		Set<String> teaIds = new HashSet<>();
		Set<String> gradeIds = new HashSet<>();
		for(String gradeTea : gradeTeaIds){
			String gradeId = gradeTea.split(",")[0];
			String teaId = gradeTea.split(",")[1];
			gradeIds.add(gradeId);
			teaIds.add(teaId);
		}
		Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])),new TR<List<Teacher>>(){}), Teacher::getId);
		List<TeachEvaluateItem> items =  new ArrayList<>();
		items.addAll(dto.getEvaluaList());
		items.addAll(dto.getFillList());
		List<TeachEvaluateResultStat> statlist = new ArrayList<>();
		/**个人维度统计**/
		for(String teaId : teaIds){
			List<TeachEvaluateResult> relist1 = teachEvaluateResultService.findByProjectIdAndTeaId(project.getId(), teaId);
			List<TeachEvaluateResult> relist = new ArrayList<>();
			for(TeachEvaluateResult result : relist1) {
				if(StringUtils.equals(result.getState(), "2")) {
					relist.add(result);
				}
			}
			int countNum = EntityUtils.getSet(relist, TeachEvaluateResult::getOperatorId).size();
			for (TeachEvaluateItem item : items) {
				TeachEvaluateResultStat stat;
				if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, item.getItemType())){
					//如果指标是评教项目,则需要计算改项折分，如果是满意率的话只需要计算各个选项的百分比即可
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_ONE);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					float countScore = 0f;
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getItemId(), item.getId())){
							countScore = countScore + re.getScore();
						}
					}
					stat.setScore(countScore/countNum);
					stat.setStatTeacherId(teaId);
					stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
					statlist.add(stat);
				}
				for(TeachEvaluateItemOption option : item.getOptionList()){
					//项目指标和满意度，都需要统计各个选择项的百分比
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_ONE);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					Set<String> fillStu = new HashSet<>();
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getResultId(), option.getId())){
							fillStu.add(re.getOperatorId());
						}
					}
					int fillStuNum  =fillStu.size()*100;
					stat.setBfb((float)fillStuNum/countNum);
					stat.setOptionId(option.getId());
					stat.setOptionName(option.getOptionName());
					stat.setOptionNo(option.getOptionNo());
					stat.setStatTeacherId(teaId);
					stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
					statlist.add(stat);
				}
			}
		}
		/**年级维度统计**/
		int schSubNum = 0;
		Map<String,Float> schCountNum = new HashMap<>();
		for (String gradeId : gradeIds) {
			//此处不用个人维度中的数据是因为需要考虑一个教师有可能在多个年级有学生调查数据
			List<TeachEvaluateResult> relist = teachEvaluateResultService.findByProjectIdAndGradeId(project.getId(), gradeId);
			int countNum = EntityUtils.getSet(relist, TeachEvaluateResult::getOperatorId).size();
			schSubNum = schSubNum + countNum;
			for (TeachEvaluateItem item : items) {
				TeachEvaluateResultStat stat;
				if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, item.getItemType())){
					//如果指标是评教项目,则需要计算改项折分，如果是满意率的话只需要计算各个选项的百分比即可
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_GRADE);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					float countScore = 0f;
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getItemId(), item.getId())){
							countScore = countScore + re.getScore();
						}
					}
					stat.setScore(countScore/countNum);
					if(!schCountNum.containsKey(item.getId())){
						schCountNum.put(item.getId(), 0f);
					}
					schCountNum.put(item.getId(), schCountNum.get(item.getId())+countScore);
					stat.setStatGradeId(gradeId);
					statlist.add(stat);
				}
				for(TeachEvaluateItemOption option : item.getOptionList()){
					//项目指标和满意度，都需要统计各个选择项的百分比
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_GRADE);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					Set<String> fillStu = new HashSet<>();
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getResultId(), option.getId())){
							fillStu.add(re.getOperatorId());
						}
					}
					int fillStuNum  =fillStu.size()*100;
					stat.setBfb((float)fillStuNum/countNum);
					if(!schCountNum.containsKey(option.getId())){
						schCountNum.put(option.getId(), 0f);
					}
					schCountNum.put(option.getId(), schCountNum.get(option.getId())+fillStuNum);
					stat.setOptionId(option.getId());
					stat.setOptionName(option.getOptionName());
					stat.setOptionNo(option.getOptionNo());
					stat.setStatGradeId(gradeId);
					statlist.add(stat);
				}
			}
		}
		/**全校维度统计**/
		for(TeachEvaluateItem item : items){
			TeachEvaluateResultStat stat;
			if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, item.getItemType())){
				stat = new TeachEvaluateResultStat();
				stat.setDimension(EvaluationConstants.STATE__DIMENSION_SCH);
				stat.setEvaluateType(project.getEvaluateType());
				stat.setUnitId(project.getUnitId());
				stat.setProjectId(project.getId());
				stat.setProjectName(project.getProjectName());
				itemToStat(item,stat);
				float countScore = schCountNum.containsKey(item.getId())?schCountNum.get(item.getId()):0f;
				stat.setScore(countScore/schSubNum);
				statlist.add(stat);
			}
			for(TeachEvaluateItemOption option : item.getOptionList()){
				//项目指标和满意度，都需要统计各个选择项的百分比
				stat = new TeachEvaluateResultStat();
				stat.setDimension(EvaluationConstants.STATE__DIMENSION_SCH);
				stat.setEvaluateType(project.getEvaluateType());
				stat.setUnitId(project.getUnitId());
				stat.setProjectId(project.getId());
				stat.setProjectName(project.getProjectName());
				itemToStat(item,stat);
				float countScore = schCountNum.containsKey(option.getId())?schCountNum.get(option.getId()):0f;
				stat.setBfb((float)countScore/schSubNum);
				stat.setOptionId(option.getId());
				stat.setOptionName(option.getOptionName());
				stat.setOptionNo(option.getOptionNo());
				statlist.add(stat);
			}
		}
		if(CollectionUtils.isNotEmpty(statlist)){
			this.checkSave(statlist.toArray(new TeachEvaluateResultStat[0]));
			saveAll(statlist.toArray(new TeachEvaluateResultStat[0]));
		}
	}
	//班主任调查统计
	private void statTeacher(TeachEvaluateProject project,EvaluateTableDto dto){
		/**分3个维度统计，班级、年级、全校,
		 * 只需要统计评教项目和满意度即可，文本内容只作展示，不需要统计
		 * 评教项目统计折分和百分比都要计算，满意度统计百分比**/
		//key:clsId value:gradeId
		Map<String,String> clsGradeIdMap = new HashMap<>();
		//年级对应的学生数
		Map<String,Integer> gradeNumMap = new HashMap<>();
		Set<String> gradeClsIds = teachEvaluateResultService.getResultGradeClsId(project.getId());
		//班级维度使用
		Set<String> clsIds = new HashSet<>();
		//年级维度使用
		Set<String> gradeIds = new HashSet<>();
		for(String gradeClsId : gradeClsIds){
			String gradeId = gradeClsId.split(",")[0];
			String clsId = gradeClsId.split(",")[1];
			gradeIds.add(gradeId);
			clsIds.add(clsId);
			clsGradeIdMap.put(clsId, gradeId);
		}
		List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>(){});
		Set<String> teaIds = EntityUtils.getSet(clsList, "teacherId");
		Map<String,String> clsTeaMap = EntityUtils.getMap(clsList, "id","teacherId");
		Map<String,Clazz> clsMap = EntityUtils.getMap(clsList, "id");
		Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])),new TR<List<Teacher>>(){}), "id");
		List<TeachEvaluateItem> items =  new ArrayList<TeachEvaluateItem>();
		items.addAll(dto.getEvaluaList());
		items.addAll(dto.getFillList());
		/**按照班级维度统计**/
		List<TeachEvaluateResultStat> clsStatlist = new ArrayList<TeachEvaluateResultStat>();
		//这个Map用了存放一个年级的学生折分总分（总人数） key:gradeId+itemId or gradeId + optionId
		Map<String,Float> gradeScoreMap = new HashMap<String,Float>(); 
		Map<String,Float> schScoreMap = new HashMap<String,Float>();
		for(String clsId:clsIds){
			List<TeachEvaluateResult> relist1 = teachEvaluateResultService.findByProjectIdAndClsId(project.getId(), clsId);
			List<TeachEvaluateResult> relist = new ArrayList<>();
			for(TeachEvaluateResult result : relist1) {
				if(StringUtils.equals(result.getState(), "2")) {
					relist.add(result);
				}
			}
			int countNum = EntityUtils.getSet(relist, "operatorId").size();
//			clsNumMap.put(clsId, countNum);
			String gradeId = clsGradeIdMap.get(clsId);
			if(!gradeNumMap.containsKey(gradeId)){
				gradeNumMap.put(gradeId, 0);
			}
			gradeNumMap.put(gradeId, gradeNumMap.get(gradeId)+countNum);
			String teaId = clsTeaMap.get(clsId);
			for(TeachEvaluateItem item : items){
				TeachEvaluateResultStat stat;
				if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, item.getItemType())){
					//如果指标是评教项目,则需要计算改项折分，如果是满意率的话只需要计算各个选项的百分比即可
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_CLASS);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					float countScore = 0f;
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getItemId(), item.getId())){
							countScore = countScore + re.getScore();
						}
					}
					if(!gradeScoreMap.containsKey(gradeId+","+item.getId())){
						gradeScoreMap.put(gradeId+","+item.getId(), 0f);
					}
					gradeScoreMap.put(gradeId+","+item.getId(), gradeScoreMap.get(gradeId+","+item.getId())+countScore);
					if(!schScoreMap.containsKey(item.getId())){
						schScoreMap.put(item.getId(), 0f);
					}
					schScoreMap.put(item.getId(), schScoreMap.get(item.getId())+countScore);
					stat.setScore((float)countScore/countNum);
					stat.setStatClassId(clsId);
					stat.setStatClassName(clsMap.containsKey(clsId)?clsMap.get(clsId).getClassNameDynamic():"无班级");
					stat.setStatGradeId(clsMap.containsKey(clsId)?clsMap.get(clsId).getGradeId():"");
					stat.setStatTeacherId(teaId);
					stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
					clsStatlist.add(stat);
				}
				for(TeachEvaluateItemOption option : item.getOptionList()){
					//项目指标和满意度，都需要统计各个选择项的百分比
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_CLASS);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					Set<String> fillStu = new HashSet<>();
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getResultId(), option.getId())){
							fillStu.add(re.getOperatorId());
						}
					}
					int fillStuNum  =fillStu.size()*100;
					if(!gradeScoreMap.containsKey(gradeId+","+option.getId())){
						gradeScoreMap.put(gradeId+","+option.getId(), 0f);
					}
					gradeScoreMap.put(gradeId+","+option.getId(), gradeScoreMap.get(gradeId+","+option.getId())+fillStuNum);
					
					if(!schScoreMap.containsKey(option.getId())){
						schScoreMap.put(option.getId(), 0f);
					}
					schScoreMap.put(option.getId(), schScoreMap.get(option.getId())+fillStuNum);
					stat.setBfb((float)fillStuNum/countNum);
					stat.setOptionId(option.getId());
					stat.setOptionName(option.getOptionName());
					stat.setOptionNo(option.getOptionNo());
					stat.setStatClassId(clsId);
					stat.setStatClassName(clsMap.containsKey(clsId)?clsMap.get(clsId).getClassNameDynamic():"无班级");
					stat.setStatGradeId(clsMap.containsKey(clsId)?clsMap.get(clsId).getGradeId():"");
					stat.setStatTeacherId(teaId);
					stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
					clsStatlist.add(stat);
				}
			}
		}
		//保存班级统计结果
		if(CollectionUtils.isNotEmpty(clsStatlist)){
			this.checkSave(clsStatlist.toArray(new TeachEvaluateResultStat[0]));
			saveAll(clsStatlist.toArray(new TeachEvaluateResultStat[0]));
		}
		/** 按照年级维度统计**/
		List<TeachEvaluateResultStat> gradeStatlist = new ArrayList<TeachEvaluateResultStat>();
		int schSubNum = 0;
		for(String gradeId : gradeIds){
			//年级提交学生数
			int gradeSubNum = gradeNumMap.get(gradeId);
			schSubNum = schSubNum + gradeSubNum;
			for(TeachEvaluateItem item : items){
				TeachEvaluateResultStat stat;
				if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, item.getItemType())){
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_GRADE);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					float countScore = gradeScoreMap.containsKey(gradeId+","+item.getId())?gradeScoreMap.get(gradeId+","+item.getId()):0f;
					stat.setScore(countScore/gradeSubNum);
					stat.setStatGradeId(gradeId);
					gradeStatlist.add(stat);
				}
				for(TeachEvaluateItemOption option : item.getOptionList()){
					//项目指标和满意度，都需要统计各个选择项的百分比
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_GRADE);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(item,stat);
					float countScore = gradeScoreMap.containsKey(gradeId+","+option.getId())?gradeScoreMap.get(gradeId+","+option.getId()):0f;
					stat.setBfb((float)countScore/gradeSubNum);
					stat.setOptionId(option.getId());
					stat.setOptionName(option.getOptionName());
					stat.setOptionNo(option.getOptionNo());
					stat.setStatGradeId(gradeId);
					gradeStatlist.add(stat);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(gradeStatlist)){
			this.checkSave(gradeStatlist.toArray(new TeachEvaluateResultStat[0]));
			saveAll(gradeStatlist.toArray(new TeachEvaluateResultStat[0]));
		}
		/**全校维度统计**/
		List<TeachEvaluateResultStat> statlist = new ArrayList<TeachEvaluateResultStat>();
		if(schSubNum == 0){
			return;
		}
		for(TeachEvaluateItem item : items){
			TeachEvaluateResultStat stat;
			if(StringUtils.equals(EvaluationConstants.ITEM_TYPE_EVALUA, item.getItemType())){
				stat = new TeachEvaluateResultStat();
				stat.setDimension(EvaluationConstants.STATE__DIMENSION_SCH);
				stat.setEvaluateType(project.getEvaluateType());
				stat.setUnitId(project.getUnitId());
				stat.setProjectId(project.getId());
				stat.setProjectName(project.getProjectName());
				itemToStat(item,stat);
				float countScore = schScoreMap.containsKey(item.getId())?schScoreMap.get(item.getId()):0f;
				stat.setScore(countScore/schSubNum);
				statlist.add(stat);
			}
			for(TeachEvaluateItemOption option : item.getOptionList()){
				//项目指标和满意度，都需要统计各个选择项的百分比
				stat = new TeachEvaluateResultStat();
				stat.setDimension(EvaluationConstants.STATE__DIMENSION_SCH);
				stat.setEvaluateType(project.getEvaluateType());
				stat.setUnitId(project.getUnitId());
				stat.setProjectId(project.getId());
				stat.setProjectName(project.getProjectName());
				itemToStat(item,stat);
				float countScore = schScoreMap.containsKey(option.getId())?schScoreMap.get(option.getId()):0f;
				stat.setBfb((float)countScore/schSubNum);
				stat.setOptionId(option.getId());
				stat.setOptionName(option.getOptionName());
				stat.setOptionNo(option.getOptionNo());
				statlist.add(stat);
			}
		}
		if(CollectionUtils.isNotEmpty(statlist)){
			this.checkSave(statlist.toArray(new TeachEvaluateResultStat[0]));
			saveAll(statlist.toArray(new TeachEvaluateResultStat[0]));
		}
	}
	//选修课统计
	private void statElective(TeachEvaluateProject project,EvaluateTableDto dto){
		/**选修课只按照教学班维度统计（我们的系统中选修课的教学班班级还是以选修课id为教学班id，不进入教学班表，在课表中体现）
		 * 所以这边的教学班名称也是选修课名称 
		 **/
		List<TeachEvaluateResultStat> statlist = new ArrayList<TeachEvaluateResultStat>();
		Set<String> subTeaIds = teachEvaluateResultService.getResultSubIds(project.getId());
//		Map<String,String> subTeaMap = new HashMap<String,String>();
		Set<String> subIds = new HashSet<>();
		Set<String> teaIds = new HashSet<>();
		for (String subTeaId : subTeaIds) {
			String subId = subTeaId.split(",")[0];
			String teaId = subTeaId.split(",")[1];
			subIds.add(subId);
			teaIds.add(teaId);
//			subTeaMap.put(subId, teaId);//这里默认一门选修课只会有一个老师教
		}
		Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])),new TR<List<Teacher>>(){}), Teacher::getId);
		//TODO
		List<TeachEvaluateRelation> relations = new ArrayList<>(); 
		relations = teachEvaluateRelationService.findByProjectIds(new String[] {project.getId()});
		Set<String> clsIds = new HashSet<>();
		Set<String> courseIds = new HashSet<>();
		for (TeachEvaluateRelation e : relations) {
			clsIds.add(e.getValueId());
			courseIds.add(e.getSubjectId());
		}
		List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
		Map<String,Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		
		List<TeachEvaluateItem> evaItemList = dto.getEvaluaList();
		List<TeachEvaluateItem> fillItemList = dto.getFillList();
		TeachEvaluateResultStat stat;
//		for(String subId : subIds){
		for (String subTeaId : subTeaIds) {
			String subId = subTeaId.split(",")[0];
			String teaId = subTeaId.split(",")[1];
			List<TeachEvaluateResult> relist1 = teachEvaluateResultService.findByProjectIdAndSubIdAndTeacherId(project.getId(),subId,teaId);
			List<TeachEvaluateResult> relist = new ArrayList<>();
			for(TeachEvaluateResult result : relist1) {
				if(StringUtils.equals(result.getState(), "2")) {
					relist.add(result);
				}
			}
			int countNum = EntityUtils.getSet(relist, TeachEvaluateResult::getOperatorId).size();
//			String teaId = subTeaMap.get(subId);
			for (TeachEvaluateItem eva : evaItemList) {
				//指标项目只需要计算出该指标的折分即可
				stat = new TeachEvaluateResultStat();
				stat.setDimension(EvaluationConstants.STATE__DIMENSION_CLASS);
				stat.setEvaluateType(project.getEvaluateType());
				stat.setUnitId(project.getUnitId());
				stat.setProjectId(project.getId());
				stat.setProjectName(project.getProjectName());
				itemToStat(eva,stat);
				float countScore = 0f;
				for(TeachEvaluateResult re :relist){
					if(StringUtils.equals(re.getItemId(), eva.getId())){
						countScore = countScore + re.getScore();
					}
				}
				stat.setScore(countScore/countNum);
				stat.setStatSubjectId(subId);
				stat.setStatSubjectName(courseMap.containsKey(subId)?courseMap.get(subId).getSubjectName():"无课程");
				stat.setStatTeacherId(teaId);
				stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
				statlist.add(stat);
			}
			for(TeachEvaluateItem fill:fillItemList){
				//满意度指标需要对每一个选项进行统计
				for(TeachEvaluateItemOption fillOption : fill.getOptionList()){
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_CLASS);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(fill,stat);
					Set<String> fillStu = new HashSet<>();
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getResultId(), fillOption.getId())){
							fillStu.add(re.getOperatorId());
						}
					}
					int fillStuNum  =fillStu.size()*100;
					stat.setBfb((float)fillStuNum/countNum);
					stat.setOptionId(fillOption.getId());
					stat.setOptionName(fillOption.getOptionName());
					stat.setOptionNo(fillOption.getOptionNo());
					stat.setStatSubjectId(subId);
					stat.setStatSubjectName(courseMap.containsKey(subId)?courseMap.get(subId).getSubjectName():"无课程");
					stat.setStatTeacherId(teaId);
					stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
					statlist.add(stat);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(statlist)){
			this.checkSave(statlist.toArray(new TeachEvaluateResultStat[0]));
			saveAll(statlist.toArray(new TeachEvaluateResultStat[0]));
		}
	}
	//教学调查统计
	private void statTeach(TeachEvaluateProject project,EvaluateTableDto dto){
		List<TeachEvaluateResultStat> statlist = new ArrayList<TeachEvaluateResultStat>();
		Set<String> teaIds = new HashSet<>();
		Set<String> clsIds = new HashSet<>();
		Set<String> courseIds = new HashSet<>();
		Set<String> clsSubTeaIds = teachEvaluateResultService.getResultClsSubTeaIds(project.getId());
		//clsId+","+subId 以班级为维度统计是需要用到
		Set<String> clsSubIds = new HashSet<>();
		Map<String,String> clsSubTeaMap = new HashMap<>();
		//subId+","+teaId 以学科为维度统计是需要用到 
		Set<String> subTeaIds = new HashSet<>();
		for (String clsSubTea : clsSubTeaIds) {
			String clsId = clsSubTea.split(",")[0];
			String subId = clsSubTea.split(",")[1];
			String teaId = "";
			if(clsSubTea.split(",").length == 3){
				teaId = clsSubTea.split(",")[2];
			}
			teaIds.add(teaId);
			clsIds.add(clsId);
			courseIds.add(subId);
			clsSubIds.add(clsId+","+subId);
			clsSubTeaMap.put(clsId+","+subId, teaId);
			subTeaIds.add(subId+","+teaId);
		}
		Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])),new TR<List<Teacher>>(){}), Teacher::getId);
		Map<String,Course> courseMap = EntityUtils.getMap(SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){}), Course::getId);
		//TODO 班级可能为教学班
		List<Clazz> clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>(){});
		List<TeachClass> teaClses = SUtils.dt(teachClassRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<TeachClass>>(){});
		if(CollectionUtils.isNotEmpty(teaClses)) {
			Clazz clazz = null;
			for (TeachClass e : teaClses) {
				clazz = new Clazz();
				clazz.setId(e.getId());
				clazz.setClassNameDynamic(e.getName());
				clslist.add(clazz);
			}
		}
		Map<String,Clazz> clsMap = EntityUtils.getMap(clslist, Clazz::getId);
		/**分2个维度统计，教学班和学科,
		 * 只需要统计评教项目和满意度即可，文本内容只作展示，不需要统计
		 * 评教项目统计折分，满意度统计百分比**/
		//教学班维度统计(班级+学科)
		List<TeachEvaluateItem> evaItemList = dto.getEvaluaList();
		List<TeachEvaluateItem> fillItemList = dto.getFillList();
		TeachEvaluateResultStat stat;
		for(String clsSubId : clsSubIds){
			String clsId = clsSubId.split(",")[0];
			String subId = clsSubId.split(",")[1];
			//数据量太大，放循环内分批次取
			List<TeachEvaluateResult> relist1 = teachEvaluateResultService.findByProjectIdAndClsIdAndSubId(project.getId(),clsId,subId);
			List<TeachEvaluateResult> relist = new ArrayList<>();
			for(TeachEvaluateResult result : relist1) {
				if(StringUtils.equals(result.getState(), "2")) {
					relist.add(result);
				}
			}
			int countNum = EntityUtils.getSet(relist, TeachEvaluateResult::getOperatorId).size();
			String teaId = clsSubTeaMap.get(clsSubId);
			for (TeachEvaluateItem eva : evaItemList) {
				//指标项目只需要计算出该指标的折分即可
				stat = new TeachEvaluateResultStat();
				stat.setDimension(EvaluationConstants.STATE__DIMENSION_CLASS);
				stat.setEvaluateType(project.getEvaluateType());
				stat.setUnitId(project.getUnitId());
				stat.setProjectId(project.getId());
				stat.setProjectName(project.getProjectName());
				itemToStat(eva,stat);
				float countScore = 0f;
				for(TeachEvaluateResult re :relist){
					if(StringUtils.equals(re.getItemId(), eva.getId())){
						countScore = countScore + re.getScore();
					}
				}
				stat.setScore(countScore/countNum);
				stat.setStatClassId(clsId);
				stat.setStatClassName(clsMap.containsKey(clsId)?clsMap.get(clsId).getClassNameDynamic():"无班级");
				stat.setStatGradeId(clsMap.containsKey(clsId)?clsMap.get(clsId).getGradeId():"");
				stat.setStatSubjectId(subId);
				stat.setStatSubjectName(courseMap.containsKey(subId)?courseMap.get(subId).getSubjectName():"无课程");
				stat.setStatTeacherId(teaId);
				stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
				statlist.add(stat);
			}
			for (TeachEvaluateItem fill : fillItemList) {
				//满意度指标需要对每一个选项进行统计
				for(TeachEvaluateItemOption fillOption : fill.getOptionList()){
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_CLASS);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(fill,stat);
					Set<String> fillStu = new HashSet<>();
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getResultId(), fillOption.getId())){
							fillStu.add(re.getOperatorId());
						}
					}
					int fillStuNum  =fillStu.size()*100;
					stat.setBfb((float)fillStuNum/countNum);
					stat.setOptionId(fillOption.getId());
					stat.setOptionName(fillOption.getOptionName());
					stat.setOptionNo(fillOption.getOptionNo());
					stat.setStatClassId(clsId);
					stat.setStatClassName(clsMap.containsKey(clsId)?clsMap.get(clsId).getClassNameDynamic():"无班级");
					stat.setStatGradeId(clsMap.containsKey(clsId)?clsMap.get(clsId).getGradeId():"");
					stat.setStatSubjectId(subId);
					stat.setStatSubjectName(courseMap.containsKey(subId)?courseMap.get(subId).getSubjectName():"无课程");
					stat.setStatTeacherId(teaId);
					stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
					statlist.add(stat);
				}
			}
		}
		//学科维度统计(学科)
		for(String subTeaId : subTeaIds){
			String subId = subTeaId.split(",")[0];
			String teaId = "";
			if(subTeaId.split(",").length>1)
				teaId = subTeaId.split(",")[1];
			//数据量太大，放循环内分批次取
			List<TeachEvaluateResult> relist1 = teachEvaluateResultService.findByProjectIdAndSubIdAndTeacherId(project.getId(),subId,teaId);
			List<TeachEvaluateResult> relist = new ArrayList<>();
			for(TeachEvaluateResult result : relist1) {
				if(StringUtils.equals(result.getState(), "2")) {
					relist.add(result);
				}
			}
			int countNum = teachEvaluateResultService.getResultStudents(project.getId(), null, subId,teaId).size();
			for (TeachEvaluateItem eva : evaItemList) {
				//指标项目只需要计算出该指标的折分即可
				stat = new TeachEvaluateResultStat();
				stat.setDimension(EvaluationConstants.STATE__DIMENSION_SUBJECT);
				stat.setEvaluateType(project.getEvaluateType());
				stat.setUnitId(project.getUnitId());
				stat.setProjectId(project.getId());
				stat.setProjectName(project.getProjectName());
				itemToStat(eva,stat);
				float countScore = 0f;
				for(TeachEvaluateResult re :relist){
					if(StringUtils.equals(re.getItemId(), eva.getId())){
						countScore = countScore + re.getScore();
					}
				}
				stat.setScore(countScore/countNum);
				stat.setStatSubjectId(subId);
				stat.setStatSubjectName(courseMap.containsKey(subId)?courseMap.get(subId).getSubjectName():"无课程");
				stat.setStatTeacherId(teaId);
				stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
				statlist.add(stat);
			}
			for (TeachEvaluateItem fill : fillItemList) {
				//满意度指标需要对每一个选项进行统计
				for(TeachEvaluateItemOption fillOption : fill.getOptionList()){
					stat = new TeachEvaluateResultStat();
					stat.setDimension(EvaluationConstants.STATE__DIMENSION_SUBJECT);
					stat.setEvaluateType(project.getEvaluateType());
					stat.setUnitId(project.getUnitId());
					stat.setProjectId(project.getId());
					stat.setProjectName(project.getProjectName());
					itemToStat(fill,stat);
					Set<String> fillStu = new HashSet<>();
					for(TeachEvaluateResult re :relist){
						if(StringUtils.equals(re.getResultId(), fillOption.getId())){
							fillStu.add(re.getOperatorId());
						}
					}
					int fillStuNum  =fillStu.size()*100;
					stat.setBfb((float)fillStuNum/countNum);
					stat.setOptionId(fillOption.getId());
					stat.setOptionName(fillOption.getOptionName());
					stat.setOptionNo(fillOption.getOptionNo());
					stat.setStatSubjectId(subId);
					stat.setStatSubjectName(courseMap.containsKey(subId)?courseMap.get(subId).getSubjectName():"无课程");
					stat.setStatTeacherId(teaId);
					stat.setStatTeacherName(teaMap.containsKey(teaId)?teaMap.get(teaId).getTeacherName():"无教师");
					statlist.add(stat);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(statlist)){
			this.checkSave(statlist.toArray(new TeachEvaluateResultStat[0]));
			saveAll(statlist.toArray(new TeachEvaluateResultStat[0]));
		}
	}
	
	
	private static void itemToStat(TeachEvaluateItem item,TeachEvaluateResultStat stat){
		stat.setItemId(item.getId());
		stat.setItemName(item.getItemName());
		stat.setItemType(item.getItemType());
		stat.setItemNo(item.getItemNo());
	}
	public static void main(String[] args) {
		float a = 2.3f;
		int b = 2;
		System.out.println(a/b);
		int c = 1;
		System.out.println((float)c/b);
	}
	@Override
	public void deleteByProjectId(String projectId) {
		teachEvaluateResultStatDao.deleteByProjectId(projectId);
	}

	@Override
	public List<TeachEvaluateResultStat> findByProjectIds(String[] projectIds) {
		return teachEvaluateResultStatDao.findByProjectIds(projectIds);
	}
	
	@Override
	protected BaseJpaRepositoryDao<TeachEvaluateResultStat, String> getJpaDao() {
		return teachEvaluateResultStatDao;
	}

	@Override
	protected Class<TeachEvaluateResultStat> getEntityClass() {
		return TeachEvaluateResultStat.class;
	}
	@Override
	public Map<String, Float> getResultStatTeaSchExportDto(String projectId) {
		Map<String,Float> itemScoreMap = new HashMap<String, Float>();
		List<TeachEvaluateResultStat> statSchlist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_SCH);
		//school:全校 countScore 总分
		itemScoreMap.put("school,countScore", 0f);
		for(TeachEvaluateResultStat stat : statSchlist){
			if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				itemScoreMap.put("school,"+stat.getOptionId(), stat.getBfb());
			}else{
				if(StringUtils.isBlank(stat.getOptionId())){
					if(!itemScoreMap.containsKey("school,"+stat.getItemId())){
						itemScoreMap.put("school,"+stat.getItemId(), 0f);
					}
					itemScoreMap.put("school,countScore", itemScoreMap.get("school,countScore")+stat.getScore());
					itemScoreMap.put("school,"+stat.getItemId(), itemScoreMap.get("school,"+stat.getItemId())+stat.getScore());
				}else{
					if(!itemScoreMap.containsKey("school,"+stat.getOptionId())){
						itemScoreMap.put("school,"+stat.getOptionId(), 0f);
					}
					itemScoreMap.put("school,"+stat.getOptionId(), itemScoreMap.get("school,"+stat.getOptionId())+stat.getBfb());
				}
			}
		}
		List<TeachEvaluateResultStat> statGradelist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_GRADE);
		for(TeachEvaluateResultStat stat : statGradelist){
			if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				itemScoreMap.put(stat.getStatGradeId()+","+stat.getOptionId(), stat.getBfb());
			}else{
				if(StringUtils.isBlank(stat.getOptionId())){
					if(!itemScoreMap.containsKey(stat.getStatGradeId()+","+stat.getItemId())){
						itemScoreMap.put(stat.getStatGradeId()+","+stat.getItemId(), 0f);
					}
					itemScoreMap.put(stat.getStatGradeId()+","+stat.getItemId(), itemScoreMap.get(stat.getStatGradeId()+","+stat.getItemId())+stat.getScore());
					
					
					if(!itemScoreMap.containsKey(stat.getStatGradeId()+",countScore")){
						itemScoreMap.put(stat.getStatGradeId()+",countScore", 0f);
					}
					itemScoreMap.put(stat.getStatGradeId()+",countScore", itemScoreMap.get(stat.getStatGradeId()+",countScore")+stat.getScore());
				}else{
					if(!itemScoreMap.containsKey(stat.getStatGradeId()+","+stat.getOptionId())){
						itemScoreMap.put(stat.getStatGradeId()+","+stat.getOptionId(), 0f);
					}
					itemScoreMap.put(stat.getStatGradeId()+","+stat.getOptionId(), itemScoreMap.get(stat.getStatGradeId()+","+stat.getOptionId())+stat.getBfb());
				}
			}
		}
		List<TeachEvaluateResultStat> statClasslist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_CLASS);
		for(TeachEvaluateResultStat stat : statClasslist){
			if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				itemScoreMap.put(stat.getStatClassId()+","+stat.getOptionId(), stat.getBfb());
			}else{
				if(StringUtils.isBlank(stat.getOptionId())){
					if(!itemScoreMap.containsKey(stat.getStatClassId()+","+stat.getItemId())){
						itemScoreMap.put(stat.getStatClassId()+","+stat.getItemId(), 0f);
					}
					itemScoreMap.put(stat.getStatClassId()+","+stat.getItemId(), itemScoreMap.get(stat.getStatClassId()+","+stat.getItemId())+stat.getScore());
					
					
					if(!itemScoreMap.containsKey(stat.getStatClassId()+",countScore")){
						itemScoreMap.put(stat.getStatClassId()+",countScore", 0f);
					}
					itemScoreMap.put(stat.getStatClassId()+",countScore", itemScoreMap.get(stat.getStatClassId()+",countScore")+stat.getScore());
				}else{
					if(!itemScoreMap.containsKey(stat.getStatClassId()+","+stat.getOptionId())){
						itemScoreMap.put(stat.getStatClassId()+","+stat.getOptionId(), 0f);
					}
					itemScoreMap.put(stat.getStatClassId()+","+stat.getOptionId(), itemScoreMap.get(stat.getStatClassId()+","+stat.getOptionId())+stat.getBfb());
				}
			}
		}
		List<TeachEvaluateResultStat> statTeacherlist = teachEvaluateResultStatDao.findByProjectIdAndDimension(projectId,EvaluationConstants.STATE__DIMENSION_ONE);
		for(TeachEvaluateResultStat stat : statTeacherlist){
			if(StringUtils.equals(stat.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				itemScoreMap.put(stat.getStatTeacherId()+","+stat.getOptionId(), stat.getBfb());
			}else{
				if(StringUtils.isBlank(stat.getOptionId())){
					if(!itemScoreMap.containsKey(stat.getStatTeacherId()+","+stat.getItemId())){
						itemScoreMap.put(stat.getStatTeacherId()+","+stat.getItemId(), 0f);
					}
					itemScoreMap.put(stat.getStatTeacherId()+","+stat.getItemId(), itemScoreMap.get(stat.getStatTeacherId()+","+stat.getItemId())+stat.getScore());
					
					
					if(!itemScoreMap.containsKey(stat.getStatTeacherId()+",countScore")){
						itemScoreMap.put(stat.getStatTeacherId()+",countScore", 0f);
					}
					itemScoreMap.put(stat.getStatTeacherId()+",countScore", itemScoreMap.get(stat.getStatTeacherId()+",countScore")+stat.getScore());
				}else{
					if(!itemScoreMap.containsKey(stat.getStatClassId()+","+stat.getOptionId())){
						itemScoreMap.put(stat.getStatTeacherId()+","+stat.getOptionId(), 0f);
					}
					itemScoreMap.put(stat.getStatTeacherId()+","+stat.getOptionId(), itemScoreMap.get(stat.getStatTeacherId()+","+stat.getOptionId())+stat.getBfb());
				}
			}
		}
		return itemScoreMap;
	}
}
