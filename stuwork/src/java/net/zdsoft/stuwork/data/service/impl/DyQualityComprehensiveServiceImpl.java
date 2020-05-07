package net.zdsoft.stuwork.data.service.impl;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyQualityComprehensiveDto;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.entity.DyStuMilitaryTraining;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;
import net.zdsoft.stuwork.data.entity.DyStuStudyingFarming;
import net.zdsoft.stuwork.data.entity.DyStuWeekCheckPerformance;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyQualityComprehensiveService;
import net.zdsoft.stuwork.data.service.DyStuEvaluationService;
import net.zdsoft.stuwork.data.service.DyStuMilitaryTrainingService;
import net.zdsoft.stuwork.data.service.DyStuPunishmentService;
import net.zdsoft.stuwork.data.service.DyStuStudyingFarmingService;
import net.zdsoft.stuwork.data.service.DyStuWeekCheckPerformanceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("dyQualityComprehensiveService")
public class DyQualityComprehensiveServiceImpl implements DyQualityComprehensiveService{
    @Autowired
	private DyStuPunishmentService dyStuPunishmentService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private DyStuEvaluationService dyStuEvaluationService;
    @Autowired
    private DyStuMilitaryTrainingService dyStuMilitaryTrainingService;
    @Autowired
    private DyStuWeekCheckPerformanceService dyStuWeekCheckPerformanceService;
    @Autowired
    private DyStuStudyingFarmingService dyStuStudyingFarmingService;
    @Autowired
    private DyBusinessOptionService dyBusinessOptionService;
    
    public Set<String> getChooseAcaAndSem(String nowAcadyear, String openAcadyear, int nowSemester, boolean isShow){
    	
//		int grade = Integer.parseInt(nowAcadyear.split("-")[0]) - Integer.parseInt(openAcadyear.split("-")[0]);
		Set<String> acadyearAndSemesterSet = new HashSet<String>();
		/*if(0==grade){//高一
			if(1==nowSemester){
				acadyearAndSemesterSet.add(openAcadyear+",1");
			}else{
				acadyearAndSemesterSet.add(openAcadyear+",1");
				acadyearAndSemesterSet.add(openAcadyear+",2");
			}
		}else if(1==grade){//高二
			if(1==nowSemester){
				acadyearAndSemesterSet.add(openAcadyear+",1");
				acadyearAndSemesterSet.add(openAcadyear+",2");
				acadyearAndSemesterSet.add(String.valueOf(Integer.parseInt(openAcadyear.split("-")[0])+1)+"-"+String.valueOf(Integer.parseInt(openAcadyear.split("-")[1])+1)+",1");
			}else{
				acadyearAndSemesterSet.add(openAcadyear+",1");
				acadyearAndSemesterSet.add(openAcadyear+",2");
				acadyearAndSemesterSet.add(String.valueOf(Integer.parseInt(openAcadyear.split("-")[0])+1)+"-"+String.valueOf(Integer.parseInt(openAcadyear.split("-")[1])+1)+",1");
				acadyearAndSemesterSet.add(String.valueOf(Integer.parseInt(openAcadyear.split("-")[0])+1)+"-"+String.valueOf(Integer.parseInt(openAcadyear.split("-")[1])+1)+",2");
			}
		}else{//高三
*/			acadyearAndSemesterSet.add(openAcadyear+",1");
			acadyearAndSemesterSet.add(openAcadyear+",2");
			acadyearAndSemesterSet.add(String.valueOf(Integer.parseInt(openAcadyear.split("-")[0])+1)+"-"+String.valueOf(Integer.parseInt(openAcadyear.split("-")[1])+1)+",1");
			acadyearAndSemesterSet.add(String.valueOf(Integer.parseInt(openAcadyear.split("-")[0])+1)+"-"+String.valueOf(Integer.parseInt(openAcadyear.split("-")[1])+1)+",2");
			acadyearAndSemesterSet.add(String.valueOf(Integer.parseInt(openAcadyear.split("-")[0])+2)+"-"+String.valueOf(Integer.parseInt(openAcadyear.split("-")[1])+2)+",1");
		//}
		//再加上初三下学期
		if(isShow){
			acadyearAndSemesterSet.add(String.valueOf(Integer.parseInt(openAcadyear.split("-")[0])-1)+"-"+String.valueOf(Integer.parseInt(openAcadyear.split("-")[1])-1)+",2");
		}
		return acadyearAndSemesterSet;
    }
    
    public String[] getAcadyears(String nowAcadyear, String openAcadyear, int nowSemester){
    	String[] acadyearArray = new String[4];
    	int grade = Integer.parseInt(nowAcadyear.split("-")[0]) - Integer.parseInt(openAcadyear.split("-")[0]);
		if(0==grade){//高一
			acadyearArray[0] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])-1)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])-1);
			acadyearArray[1] = nowAcadyear;
			acadyearArray[2] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])+1)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])+1);
			acadyearArray[3] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])+2)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])+2);
		}else if(1==grade){//高二
			acadyearArray[0] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])-2)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])-2);
			acadyearArray[1] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])-1)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])-1);
			acadyearArray[2] = nowAcadyear;
			acadyearArray[3] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])+1)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])+1);	
		}else{//高三
			acadyearArray[0] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])-3)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])-3);
			acadyearArray[1] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])-2)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])-2);
			acadyearArray[2] = String.valueOf(Integer.parseInt(nowAcadyear.split("-")[0])-1)+"-"+String.valueOf(Integer.parseInt(nowAcadyear.split("-")[1])-1);
			acadyearArray[3] = nowAcadyear;
		}
		return acadyearArray;
    }
	@Override
	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear){
		//学生处罚
		List<DyStuPunishment> dyStuPunishmentList = dyStuPunishmentService.getScortByStudentIdIn(studentIds);
		if(CollectionUtils.isNotEmpty(dyStuPunishmentList)){
			int nowYear= Integer.parseInt(acadyear.split("-")[0]);
			//直接获取统计后的数据
			Map<String,List<DyStuPunishment>> punishListMap=dyStuPunishmentList.stream().collect(Collectors.groupingBy(DyStuPunishment::getStudentId));
			List<DyStuPunishment> lastList=new ArrayList<>();
			for(Map.Entry<String, List<DyStuPunishment>> entry:punishListMap.entrySet()){
				int year=stuYearMap.get(entry.getKey());
				List<DyStuPunishment> inList=punishListMap.get(entry.getKey());
				for(DyStuPunishment in:inList){
					//处理该学生休学前的老数据
					if(StringUtils.isNotBlank(in.getAcadyear()) && Integer.parseInt(in.getAcadyear().split("-")[0])+year <= nowYear) {
						in.setAcadyear(getAcadyear(in.getAcadyear(),year));
						lastList.add(in);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lastList)){
				dyStuPunishmentService.saveAll(lastList.toArray(new DyStuPunishment[0]));
			}
		}
		//评语
		List<DyStuEvaluation> dyStuEvaluationList = dyStuEvaluationService.findByStudentIdIn(studentIds);
		if(CollectionUtils.isNotEmpty(dyStuEvaluationList)){
			int nowYear= Integer.parseInt(acadyear.split("-")[0]);
			//直接获取统计后的数据
			Map<String,List<DyStuEvaluation>> evaluationListMap=dyStuEvaluationList.stream().collect(Collectors.groupingBy(DyStuEvaluation::getStudentId));
			List<DyStuEvaluation> lastList=new ArrayList<>();
			for(Map.Entry<String, List<DyStuEvaluation>> entry:evaluationListMap.entrySet()){
				int year=stuYearMap.get(entry.getKey());
				List<DyStuEvaluation> inList=evaluationListMap.get(entry.getKey());
				for(DyStuEvaluation in:inList){
					//处理该学生休学前的老数据
					if(StringUtils.isNotBlank(in.getAcadyear()) && Integer.parseInt(in.getAcadyear().split("-")[0])+year <= nowYear) {
						in.setAcadyear(getAcadyear(in.getAcadyear(),year));
						lastList.add(in);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lastList)){
				dyStuEvaluationService.saveAll(lastList.toArray(new DyStuEvaluation[0]));
			}
		}
		//军训
		List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingService.findByStudentIds(studentIds);
		if(CollectionUtils.isNotEmpty(dyStuMilitaryTrainingList)){
			int nowYear= Integer.parseInt(acadyear.split("-")[0]);
			//直接获取统计后的数据
			Map<String,List<DyStuMilitaryTraining>> dyStuMilitaryListMap=dyStuMilitaryTrainingList.stream().collect(Collectors.groupingBy(DyStuMilitaryTraining::getStudentId));
			List<DyStuMilitaryTraining> lastList=new ArrayList<>();
			for(Map.Entry<String, List<DyStuMilitaryTraining>> entry:dyStuMilitaryListMap.entrySet()){
				int year=stuYearMap.get(entry.getKey());
				List<DyStuMilitaryTraining> inList=dyStuMilitaryListMap.get(entry.getKey());
				for(DyStuMilitaryTraining in:inList){
					//处理该学生休学前的老数据
					if(StringUtils.isNotBlank(in.getAcadyear()) && Integer.parseInt(in.getAcadyear().split("-")[0])+year <= nowYear) {
						in.setAcadyear(getAcadyear(in.getAcadyear(),year));
						lastList.add(in);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lastList)){
				dyStuMilitaryTrainingService.saveAll(lastList.toArray(new DyStuMilitaryTraining[0]));
			}
		}
		//学农
		List<DyStuStudyingFarming> dyStuStudyingFarmingList = dyStuStudyingFarmingService.findByStudentIds(studentIds);//学农
		if(CollectionUtils.isNotEmpty(dyStuStudyingFarmingList)){
			int nowYear= Integer.parseInt(acadyear.split("-")[0]);
			//直接获取统计后的数据
			Map<String,List<DyStuStudyingFarming>> studyingListMap=dyStuStudyingFarmingList.stream().collect(Collectors.groupingBy(DyStuStudyingFarming::getStudentId));
			List<DyStuStudyingFarming> lastList=new ArrayList<>();
			for(Map.Entry<String, List<DyStuStudyingFarming>> entry:studyingListMap.entrySet()){
				int year=stuYearMap.get(entry.getKey());
				List<DyStuStudyingFarming> inList=studyingListMap.get(entry.getKey());
				for(DyStuStudyingFarming in:inList){
					//处理该学生休学前的老数据
					if(StringUtils.isNotBlank(in.getAcadyear()) && Integer.parseInt(in.getAcadyear().split("-")[0])+year <= nowYear) {
						in.setAcadyear(getAcadyear(in.getAcadyear(),year));
						lastList.add(in);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lastList)){
				dyStuStudyingFarmingService.saveAll(lastList.toArray(new DyStuStudyingFarming[0]));
			}
		}
		//值周
		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIdIn(studentIds);//值周
		if(CollectionUtils.isNotEmpty(dyStuWeekCheckPerformanceList)){
			int nowYear= Integer.parseInt(acadyear.split("-")[0]);
			//直接获取统计后的数据
			Map<String,List<DyStuWeekCheckPerformance>> weekCheckPerListMap=dyStuWeekCheckPerformanceList.stream().collect(Collectors.groupingBy(DyStuWeekCheckPerformance::getStudentId));
			List<DyStuWeekCheckPerformance> lastList=new ArrayList<>();
			for(Map.Entry<String, List<DyStuWeekCheckPerformance>> entry:weekCheckPerListMap.entrySet()){
				int year=stuYearMap.get(entry.getKey());
				List<DyStuWeekCheckPerformance> inList=weekCheckPerListMap.get(entry.getKey());
				for(DyStuWeekCheckPerformance in:inList){
					//处理该学生休学前的老数据
					if(StringUtils.isNotBlank(in.getAcadyear()) && Integer.parseInt(in.getAcadyear().split("-")[0])+year <= nowYear) {
						in.setAcadyear(getAcadyear(in.getAcadyear(),year));
						lastList.add(in);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lastList)){
				dyStuWeekCheckPerformanceService.saveAll(lastList.toArray(new DyStuWeekCheckPerformance[0]));
			}
		}
	}
	public String getAcadyear(String acadyear,int l){
		return (Integer.parseInt(acadyear.split("-")[0])+l)+"-"+(Integer.parseInt(acadyear.split("-")[1])+l);
	}
	@Override
	public DyQualityComprehensiveDto getQualityScoreMap(Map<String, Integer> maxValueMap, String studentId, boolean isShow) {
		Map<String, Float> scoreMap = new HashMap<String, Float>();
		Map<String, String> nameMap = new HashMap<String, String>();
		
		String classId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getClassId();
		String unitId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getSchoolId();
		String openAcadyear = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getAcadyear();
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
		String nowAcadyear = sem.getAcadyear();
		int nowSemester = sem.getSemester();
		
		//该学生所涉及的学年学期
		Set<String> acadyearAndSemesterSet = getChooseAcaAndSem(nowAcadyear,openAcadyear,nowSemester,isShow);
		
		List<DyStuPunishment> dyStuPunishmentList = dyStuPunishmentService.getScortByStudentId(studentId);//学生处罚
		Map<String, Float> punishScoreTempMap = new HashMap<String, Float>();
		List<DyStuEvaluation> dyStuEvaluationList = dyStuEvaluationService.findByStudentId(studentId);//评语
		Map<String, Float> evaluationScoreTempMap = new HashMap<String, Float>();
		List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingService.findByStudentIds(new String[]{studentId});//军训
		List<DyStuStudyingFarming> dyStuStudyingFarmingList = dyStuStudyingFarmingService.findByUnitIdAndStudentIds(unitId, new String[]{studentId});//学农
		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentId(studentId);//值周
		Map<String, Float> performanceScoreTempMap = new HashMap<String, Float>();
		
		//List<DyBusinessOption> optionList = dyBusinessOptionService.findListByUnitId(unitId);
		//Map<String, Float> optionMap = new HashMap<String, Float>();
		//for(DyBusinessOption option : optionList){
		//	optionMap.put(option.getId(), option.getScore());
		//}
		
		
		//军训只有一次 放在高一      ——> 添加学年学期后不再放在高一
		if(dyStuMilitaryTrainingList.size()>0){
			DyStuMilitaryTraining militaryTraining = dyStuMilitaryTrainingList.get(0);
			if(acadyearAndSemesterSet.contains(militaryTraining.getAcadyear()+","+militaryTraining.getSemester())){
				scoreMap.put(militaryTraining.getAcadyear()+StuworkConstants.BUSINESS_TYPE_3, militaryTraining.getScore());
				//军训备注
				if(StringUtils.isNotBlank(militaryTraining.getRemark())) {
					nameMap.put(militaryTraining.getAcadyear()+StuworkConstants.BUSINESS_TYPE_3, militaryTraining.getGrade()+"(备注："+militaryTraining.getRemark()+")");
				}else {
					nameMap.put(militaryTraining.getAcadyear()+StuworkConstants.BUSINESS_TYPE_3, militaryTraining.getGrade());
				}
				
			}
		}
		if(dyStuStudyingFarmingList.size()>0){
			DyStuStudyingFarming studyingFarming = dyStuStudyingFarmingList.get(0);
			if(acadyearAndSemesterSet.contains(studyingFarming.getAcadyear()+","+studyingFarming.getSemester())){
				scoreMap.put(studyingFarming.getAcadyear()+StuworkConstants.BUSINESS_TYPE_5, studyingFarming.getScore());
				//学农备注
				if(StringUtils.isNotBlank(studyingFarming.getRemark())) {
					nameMap.put(studyingFarming.getAcadyear()+StuworkConstants.BUSINESS_TYPE_5, studyingFarming.getGrade()+"(备注："+studyingFarming.getRemark()+")");
				}else {
					nameMap.put(studyingFarming.getAcadyear()+StuworkConstants.BUSINESS_TYPE_5, studyingFarming.getGrade());
				}
				
			}
		}
				
		Map<String, String> punishNameTempMap = new HashMap<String, String>();
		Map<String, String> evaluationNameTempMap = new HashMap<String, String>();
		Map<String, String> performanceNameTempMap = new HashMap<String, String>();
		Set<String> acadyearSet = new HashSet<String>();
		for(String str : acadyearAndSemesterSet){
			float punishScore = 0;
			String punishNames = "";
			for(DyStuPunishment item : dyStuPunishmentList){
				if(str.split(",")[0].equals(item.getAcadyear()) && str.split(",")[1].equals(item.getSemester())){
					punishScore = punishScore + item.getScore();
					if(null != item.getPunishName()){
						//违纪增加备注
						if(StringUtils.isNotBlank(item.getPunishContent())) {
							punishNames = punishNames + item.getPunishName()+"(备注："+item.getPunishContent()+"),";
						}else {
							punishNames = punishNames + item.getPunishName()+",";
						}
						
					}
				}
			}
			punishScoreTempMap.put(str, punishScore);
			punishNameTempMap.put(str, punishNames);
			
			float evaluationScore = 0;
			String evaluationNames = "";
			for(DyStuEvaluation item : dyStuEvaluationList){
				if(str.split(",")[0].equals(item.getAcadyear()) && str.split(",")[1].equals(item.getSemester())){
					evaluationScore = evaluationScore + item.getScore();
					if(null != item.getGrade()){
						evaluationNames = evaluationNames + item.getGrade() + ",";
					}
				}
			}
			
			evaluationScoreTempMap.put(str, evaluationScore);
			evaluationNameTempMap.put(str, evaluationNames);
			
			float performanceScore = 0;
			String performanceNames = "";
			for(DyStuWeekCheckPerformance item : dyStuWeekCheckPerformanceList){
				if(str.split(",")[0].equals(item.getAcadyear()) && str.split(",")[1].equals(item.getSemester())){
					performanceScore = performanceScore + item.getScore();
					if(null != item.getGrade()){
						//值周表现增加备注
						if(StringUtils.isNotBlank(item.getRemark())) {
							performanceNames = performanceNames + item.getGrade() + "(备注："+item.getRemark()+"),";
						}else {
							performanceNames = performanceNames + item.getGrade() + ",";
						}
					}					
				}
			}
			performanceScoreTempMap.put(str, performanceScore);
			performanceNameTempMap.put(str, performanceNames);
			
			acadyearSet.add(str.split(",")[0]);
		}
		for(String acad : acadyearSet){
			float punishScore = 0;
			String punishNames = "";
			for(String key : punishScoreTempMap.keySet()){
				if(acad.equals(key.split(",")[0])){
					punishScore = punishScore + punishScoreTempMap.get(key);
					punishNames = punishNames + punishNameTempMap.get(key);
				}
			}
			scoreMap.put(acad+StuworkConstants.BUSINESS_TYPE_1, punishScore);
			if(punishNames.length()>0){
				punishNames = punishNames.substring(0, punishNames.length()-1);
			}
			nameMap.put(acad+StuworkConstants.BUSINESS_TYPE_1, punishNames);
			
			float evaluationScore = 0;
			String evaluationNames = "";
			for(String key : evaluationScoreTempMap.keySet()){
				if(acad.equals(key.split(",")[0])){
					evaluationScore = evaluationScore + evaluationScoreTempMap.get(key);
					evaluationNames = evaluationNames + evaluationNameTempMap.get(key);
				}
			}

			scoreMap.put(acad+StuworkConstants.BUSINESS_TYPE_2, evaluationScore);
			if(evaluationNames.length()>0){
				evaluationNames = evaluationNames.substring(0, evaluationNames.length()-1);
			}
			nameMap.put(acad+StuworkConstants.BUSINESS_TYPE_2, evaluationNames);
			
			float performanceScore = 0;
			String performanceNames = "";
			for(String key : performanceScoreTempMap.keySet()){
				if(acad.equals(key.split(",")[0])){
					performanceScore = performanceScore + performanceScoreTempMap.get(key);
					performanceNames = performanceNames + performanceNameTempMap.get(key);
				}
			}

			scoreMap.put(acad+StuworkConstants.BUSINESS_TYPE_4, performanceScore);
			if(performanceNames.length()>0){
				performanceNames = performanceNames.substring(0, performanceNames.length()-1);
			}
			nameMap.put(acad+StuworkConstants.BUSINESS_TYPE_4, performanceNames);
		}		
		
		String[] acadyearArray = getAcadyears(nowAcadyear, openAcadyear, nowSemester);
		List<String[]> arrayList = new ArrayList<String[]>();
		float countPunishScore = 0;
		float countPerformanceScore = 0;
		float countEvaluationScore = 0;
		float countMilitaryScore = 0;
		float countFarmingScore = 0;
		float floatVal = 0;
		for(String aca : acadyearArray){
			String[] array = new String[10];
			if(acadyearSet.contains(aca)){
				if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_2)){
					array[0] = "";//操行名称
				}else{
					array[0] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_2);//操行名称
				}
				if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_4)){
					array[1] = "";//值周表现
				}else{
					array[1] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_4);//值周表现
				}
                if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_1)){
                	array[2] = "";//违纪
                }else{
                	array[2] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_1);//违纪
                }
				if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_3)){
					array[3] = "";//军训
					array[8] = floatVal+"";
				}else{
					array[3] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_3);//军训
					array[8] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_3));
				}
				if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_5)){
					array[4] = "";//学农
					array[9] = floatVal+"";
				}else{
					array[4] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_5);//学农
					array[9] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_5));
				}
				if(null == scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_2)){
					array[5] = floatVal+"";
				}else{
					array[5] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_2));
				}
				if(null == scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_4)){
					array[6] = floatVal+"";
				}else{
					array[6] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_4));
				}
                if(null == scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_1)){
                	array[7] = floatVal+"";
                }else{
                	array[7] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_1));
                }
				countPunishScore = countPunishScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_1);
				countPerformanceScore = countPerformanceScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_4);
				countEvaluationScore = countEvaluationScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_2);
				if(null != scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_3)){
					countMilitaryScore = countMilitaryScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_3);
				}
				if(null != scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_5)){
					countFarmingScore = countFarmingScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_5);
				}
			}else{
				array[0] = "";//操行名称
				array[1] = "";//值周表现
				array[2] = "";//违纪
				array[3] = "";//军训
				array[4] = "";//学农
				array[5] = floatVal+"";
				array[6] = floatVal+"";
				array[7] = floatVal+"";
				array[8] = floatVal+"";
				array[9] = floatVal+"";
			}
			arrayList.add(array);
		}
		if(maxValueMap==null){
			maxValueMap=new HashMap<>();
		}
		if(countPerformanceScore>maxValueMap.get("ZZBX.MAX.NUMBER") && 0 != maxValueMap.get("ZZBX.MAX.NUMBER")){
			countPerformanceScore = maxValueMap.get("ZZBX.MAX.NUMBER");
	    }
		if(countEvaluationScore>maxValueMap.get("CXDD.MAX.NUMBER") && 0 != maxValueMap.get("CXDD.MAX.NUMBER")){
			countEvaluationScore = maxValueMap.get("CXDD.MAX.NUMBER");
	    }
		if(countMilitaryScore>maxValueMap.get("JX.MAX.NUMBER") && 0 != maxValueMap.get("JX.MAX.NUMBER")){
			countMilitaryScore = maxValueMap.get("JX.MAX.NUMBER");
	    }
		if(countFarmingScore>maxValueMap.get("XN.MAX.NUMBER") && 0 != maxValueMap.get("XN.MAX.NUMBER")){
			countFarmingScore = maxValueMap.get("XN.MAX.NUMBER");
		}
		float countNum = countPerformanceScore + countEvaluationScore + countMilitaryScore + countFarmingScore - countPunishScore;
		DyQualityComprehensiveDto dyQualityComprehensiveDto = new DyQualityComprehensiveDto();
		dyQualityComprehensiveDto.setAcadyearArray(acadyearArray);
		dyQualityComprehensiveDto.setCountNum(countNum);
		dyQualityComprehensiveDto.setValueList(arrayList);
		return dyQualityComprehensiveDto;
	}

	@Override
	public Map<String, Float> getAllStudentQualityScoreMap(
			Map<String, Integer> maxValueMap, String unitId, Map<String, Boolean> showMap) {
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
		String nowAcadyear = sem.getAcadyear();
		int nowSemester = sem.getSemester();
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, nowAcadyear), new TR<List<Clazz>>(){});
		Set<String> classIdSet = new HashSet<String>();
		Map<String, Clazz> clsMap = new HashMap<String, Clazz>();
		for(Clazz cls : classList){
			if(cls.getSection() == 3||cls.getSection() == 9){
				classIdSet.add(cls.getId());
				clsMap.put(cls.getId(), cls);
			}
		}
		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIdSet.toArray(new String[0])), new TR<List<Student>>(){});
		Set<String> studentIdSet = new HashSet<String>();
		for(Student student : studentList){
			studentIdSet.add(student.getId());
		}
		
		List<DyStuPunishment> dyStuPunishmentList = dyStuPunishmentService.getScortByStudentIdIn(studentIdSet.toArray(new String[0]));//学生处罚
		List<DyStuEvaluation> dyStuEvaluationList = dyStuEvaluationService.findByStudentIdIn(studentIdSet.toArray(new String[0]));//评语
		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIdIn(studentIdSet.toArray(new String[0]));//值周
		List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingService.findByStudentIdIn(studentIdSet.toArray(new String[0]));//军训
		List<DyStuStudyingFarming> dyStuStudyingFarmingList = dyStuStudyingFarmingService.findByUnitIdAndStudentIds(unitId, studentIdSet.toArray(new String[0]));//学农
		Map<String, Float> scoreMap = new HashMap<String, Float>();
		for(Student student : studentList){
			Clazz curClazz = clsMap.get(student.getClassId());
			boolean isShow = showMap.containsKey(curClazz.getGradeId())&&showMap.get(curClazz.getGradeId());
			Set<String> acadyearAndSemesterSet = getChooseAcaAndSem(nowAcadyear, curClazz.getAcadyear(), nowSemester, isShow);
			float punishScore = 0;
			for(DyStuPunishment item : dyStuPunishmentList){
				if(student.getId().equals(item.getStudentId()) && acadyearAndSemesterSet.contains(item.getAcadyear()+","+item.getSemester())){
					punishScore = punishScore + item.getScore();
				}
			}
			
			float evaluationScore = 0;
			for(DyStuEvaluation item : dyStuEvaluationList){
				if(student.getId().equals(item.getStudentId()) && acadyearAndSemesterSet.contains(item.getAcadyear()+","+item.getSemester())){
					evaluationScore = evaluationScore + item.getScore();
				}
			}
			if(evaluationScore>maxValueMap.get("CXDD.MAX.NUMBER") && 0 != maxValueMap.get("CXDD.MAX.NUMBER")){
				evaluationScore = maxValueMap.get("CXDD.MAX.NUMBER");
			}
			
			float performanceScore = 0;
			for(DyStuWeekCheckPerformance item : dyStuWeekCheckPerformanceList){
				if(student.getId().equals(item.getStudentId()) && acadyearAndSemesterSet.contains(item.getAcadyear()+","+item.getSemester())){
					performanceScore = performanceScore + item.getScore();
				}
			}
			if(performanceScore>maxValueMap.get("ZZBX.MAX.NUMBER") && 0 != maxValueMap.get("ZZBX.MAX.NUMBER")){
				performanceScore = maxValueMap.get("ZZBX.MAX.NUMBER");
			}
			
			float militaryTraining = 0;
			for(DyStuMilitaryTraining item : dyStuMilitaryTrainingList){
				if(item.getStudentId().equals(student.getId()) && acadyearAndSemesterSet.contains(item.getAcadyear()+","+item.getSemester())){
					militaryTraining = item.getScore();
				}
			}
			if(militaryTraining>maxValueMap.get("JX.MAX.NUMBER") && 0 != maxValueMap.get("JX.MAX.NUMBER")){
				militaryTraining = maxValueMap.get("JX.MAX.NUMBER");
			}
			
			float studyingFarming = 0;
			for(DyStuStudyingFarming item : dyStuStudyingFarmingList){
				if(item.getStudentId().equals(student.getId()) && acadyearAndSemesterSet.contains(item.getAcadyear()+","+item.getSemester())){
					studyingFarming = item.getScore();
				}
			}
			if(studyingFarming>maxValueMap.get("XN.MAX.NUMBER") && 0 != maxValueMap.get("XN.MAX.NUMBER")){
				studyingFarming = maxValueMap.get("XN.MAX.NUMBER");
			}
			
			float score = evaluationScore + performanceScore + militaryTraining + studyingFarming - punishScore;
			scoreMap.put(student.getId(), score);
		}
		return scoreMap;
	}

	@Override
	public Map<String, DyQualityComprehensiveDto> getQualityScoreMapByStudentIds(Map<String, Integer> maxValueMap, String classId, String[] studentIds, boolean isShow) {
		Map<String, DyQualityComprehensiveDto> dtoMap = new HashMap<>();
		
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		String openAcadyear = clazz.getAcadyear();
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, clazz.getSchoolId()), Semester.class);
		String nowAcadyear = sem.getAcadyear();
		int nowSemester = sem.getSemester();
		
		//该班级所涉及的学年学期
		Set<String> acadyearAndSemesterSet = getChooseAcaAndSem(nowAcadyear,openAcadyear,nowSemester,isShow);
		
		List<DyStuPunishment> stuPunishmentList = dyStuPunishmentService.getScortByStudentIdIn(studentIds);//学生处罚
		Map<String, List<DyStuPunishment>> stuPunishmentMap = EntityUtils.getListMap(stuPunishmentList, DyStuPunishment::getStudentId, Function.identity());
		List<DyStuEvaluation> stuEvaluationList = dyStuEvaluationService.findByStudentIdIn(studentIds);//评语
		Map<String, List<DyStuEvaluation>> stuEvaluationMap = EntityUtils.getListMap(stuEvaluationList, DyStuEvaluation::getStudentId, Function.identity());
		List<DyStuMilitaryTraining> stuMilitaryTrainingList = dyStuMilitaryTrainingService.findByStudentIds(studentIds);//军训
		Map<String, List<DyStuMilitaryTraining>> stuMilitaryTrainingMap = EntityUtils.getListMap(stuMilitaryTrainingList, DyStuMilitaryTraining::getStudentId, Function.identity());
		List<DyStuStudyingFarming> stuStudyingFarmingList = dyStuStudyingFarmingService.findByUnitIdAndStudentIds(clazz.getSchoolId(), studentIds);//学农
		Map<String, List<DyStuStudyingFarming>> stuStudyingFarmingMap = EntityUtils.getListMap(stuStudyingFarmingList, DyStuStudyingFarming::getStudentId, Function.identity());
		List<DyStuWeekCheckPerformance> stuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIdIn(studentIds);//值周
		Map<String, List<DyStuWeekCheckPerformance>> stuWeekCheckPerformanceMap = EntityUtils.getListMap(stuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getStudentId, Function.identity());
		
		for (String studentId : studentIds) {
			List<DyStuPunishment> dyStuPunishmentList = stuPunishmentMap.get(studentId);
			List<DyStuEvaluation> dyStuEvaluationList = stuEvaluationMap.get(studentId);
			List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = stuMilitaryTrainingMap.get(studentId);
			List<DyStuStudyingFarming> dyStuStudyingFarmingList = stuStudyingFarmingMap.get(studentId);
			List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = stuWeekCheckPerformanceMap.get(studentId);
			Map<String, Float> punishScoreTempMap = new HashMap<String, Float>();
			Map<String, Float> evaluationScoreTempMap = new HashMap<String, Float>();
			Map<String, Float> performanceScoreTempMap = new HashMap<String, Float>();
			Map<String, Float> scoreMap = new HashMap<String, Float>();
			Map<String, String> nameMap = new HashMap<String, String>();
			//军训只有一次 放在高一      ——> 添加学年学期后不再放在高一
			if(CollectionUtils.isNotEmpty(dyStuMilitaryTrainingList)){
				DyStuMilitaryTraining militaryTraining = dyStuMilitaryTrainingList.get(0);
				if(acadyearAndSemesterSet.contains(militaryTraining.getAcadyear()+","+militaryTraining.getSemester())){
					//军训备注
					scoreMap.put(militaryTraining.getAcadyear()+StuworkConstants.BUSINESS_TYPE_3, militaryTraining.getScore());
					if(StringUtils.isNotBlank(militaryTraining.getRemark())) {
						nameMap.put(militaryTraining.getAcadyear()+StuworkConstants.BUSINESS_TYPE_3, militaryTraining.getGrade()+"(备注："+militaryTraining.getRemark()+")");
					}else {
						nameMap.put(militaryTraining.getAcadyear()+StuworkConstants.BUSINESS_TYPE_3, militaryTraining.getGrade());
					}
					
				}
			}
			if(CollectionUtils.isNotEmpty(dyStuStudyingFarmingList)){
				DyStuStudyingFarming studyingFarming = dyStuStudyingFarmingList.get(0);
				if(acadyearAndSemesterSet.contains(studyingFarming.getAcadyear()+","+studyingFarming.getSemester())){
					//学农备注
					scoreMap.put(studyingFarming.getAcadyear()+StuworkConstants.BUSINESS_TYPE_5, studyingFarming.getScore());
					if(StringUtils.isNotBlank(studyingFarming.getRemark())) {
						nameMap.put(studyingFarming.getAcadyear()+StuworkConstants.BUSINESS_TYPE_5, studyingFarming.getGrade()+"(备注："+studyingFarming.getRemark()+")");
					}else {
						nameMap.put(studyingFarming.getAcadyear()+StuworkConstants.BUSINESS_TYPE_5, studyingFarming.getGrade());
					}
					
				}
			}
			
			Map<String, String> punishNameTempMap = new HashMap<String, String>();
			Map<String, String> evaluationNameTempMap = new HashMap<String, String>();
			Map<String, String> performanceNameTempMap = new HashMap<String, String>();
			Set<String> acadyearSet = new HashSet<String>();
			for(String str : acadyearAndSemesterSet){
				float punishScore = 0;
				String punishNames = "";
				if(CollectionUtils.isNotEmpty(dyStuPunishmentList)){
					for(DyStuPunishment item : dyStuPunishmentList){
						if(str.split(",")[0].equals(item.getAcadyear()) && str.split(",")[1].equals(item.getSemester())){
							punishScore = punishScore + item.getScore();
							if(null != item.getPunishName()){
								//违纪备注
								if(StringUtils.isNotBlank(item.getPunishContent())) {
									punishNames = punishNames + item.getPunishName()+"(备注："+item.getPunishContent()+"),";
								}else {
									punishNames = punishNames + item.getPunishName()+",";
								}
								
							}
						}
					}
				}
				punishScoreTempMap.put(str, punishScore);
				punishNameTempMap.put(str, punishNames);
				
				float evaluationScore = 0;
				String evaluationNames = "";
				if(CollectionUtils.isNotEmpty(dyStuEvaluationList)){
					for(DyStuEvaluation item : dyStuEvaluationList){
						if(str.split(",")[0].equals(item.getAcadyear()) && str.split(",")[1].equals(item.getSemester())){
							evaluationScore = evaluationScore + item.getScore();
							if(null != item.getGrade()){
								evaluationNames = evaluationNames + item.getGrade() + ",";
							}
						}
					}
				}
				evaluationScoreTempMap.put(str, evaluationScore);
				evaluationNameTempMap.put(str, evaluationNames);
				
				float performanceScore = 0;
				String performanceNames = "";
				if(CollectionUtils.isNotEmpty(dyStuWeekCheckPerformanceList)){
					for(DyStuWeekCheckPerformance item : dyStuWeekCheckPerformanceList){
						if(str.split(",")[0].equals(item.getAcadyear()) && str.split(",")[1].equals(item.getSemester())){
							performanceScore = performanceScore + item.getScore();
							if(null != item.getGrade()){
								//值周备注
								if(StringUtils.isNotBlank(item.getRemark())) {
									performanceNames = performanceNames + item.getGrade() + "(备注："+item.getRemark()+"),";
								}else {
									performanceNames = performanceNames + item.getGrade() + ",";
								}
								
							}					
						}
					}
				}
				performanceScoreTempMap.put(str, performanceScore);
				performanceNameTempMap.put(str, performanceNames);
				
				acadyearSet.add(str.split(",")[0]);
			}
			for(String acad : acadyearSet){
				float punishScore = 0;
				String punishNames = "";
				for(String key : punishScoreTempMap.keySet()){
					if(acad.equals(key.split(",")[0])){
						punishScore = punishScore + punishScoreTempMap.get(key);
						punishNames = punishNames + punishNameTempMap.get(key);
					}
				}
				scoreMap.put(acad+StuworkConstants.BUSINESS_TYPE_1, punishScore);
				if(punishNames.length()>0){
					punishNames = punishNames.substring(0, punishNames.length()-1);
				}
				nameMap.put(acad+StuworkConstants.BUSINESS_TYPE_1, punishNames);
				
				float evaluationScore = 0;
				String evaluationNames = "";
				for(String key : evaluationScoreTempMap.keySet()){
					if(acad.equals(key.split(",")[0])){
						evaluationScore = evaluationScore + evaluationScoreTempMap.get(key);
						evaluationNames = evaluationNames + evaluationNameTempMap.get(key);
					}
				}
				
				scoreMap.put(acad+StuworkConstants.BUSINESS_TYPE_2, evaluationScore);
				if(evaluationNames.length()>0){
					evaluationNames = evaluationNames.substring(0, evaluationNames.length()-1);
				}
				nameMap.put(acad+StuworkConstants.BUSINESS_TYPE_2, evaluationNames);
				
				float performanceScore = 0;
				String performanceNames = "";
				for(String key : performanceScoreTempMap.keySet()){
					if(acad.equals(key.split(",")[0])){
						performanceScore = performanceScore + performanceScoreTempMap.get(key);
						performanceNames = performanceNames + performanceNameTempMap.get(key);
					}
				}
				
				scoreMap.put(acad+StuworkConstants.BUSINESS_TYPE_4, performanceScore);
				if(performanceNames.length()>0){
					performanceNames = performanceNames.substring(0, performanceNames.length()-1);
				}
				nameMap.put(acad+StuworkConstants.BUSINESS_TYPE_4, performanceNames);
			}		
			
			String[] acadyearArray = getAcadyears(nowAcadyear, openAcadyear, nowSemester);
			List<String[]> arrayList = new ArrayList<String[]>();
			float countPunishScore = 0;
			float countPerformanceScore = 0;
			float countEvaluationScore = 0;
			float countMilitaryScore = 0;
			float countFarmingScore = 0;
			float floatVal = 0;
			for(String aca : acadyearArray){
				String[] array = new String[10];
				if(acadyearSet.contains(aca)){
					if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_2)){
						array[0] = "";//操行名称
					}else{
						array[0] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_2);//操行名称
					}
					if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_4)){
						array[1] = "";//值周表现
					}else{
						array[1] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_4);//值周表现
					}
					if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_1)){
						array[2] = "";//违纪
					}else{
						array[2] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_1);//违纪
					}
					if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_3)){
						array[3] = "";//军训
						array[8] = floatVal+"";
					}else{
						array[3] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_3);//军训
						array[8] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_3));
					}
					if(null == nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_5)){
						array[4] = "";//学农
						array[9] = floatVal+"";
					}else{
						array[4] = nameMap.get(aca+StuworkConstants.BUSINESS_TYPE_5);//学农
						array[9] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_5));
					}
					if(null == scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_2)){
						array[5] = floatVal+"";
					}else{
						array[5] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_2));
					}
					if(null == scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_4)){
						array[6] = floatVal+"";
					}else{
						array[6] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_4));
					}
					if(null == scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_1)){
						array[7] = floatVal+"";
					}else{
						array[7] = String.valueOf(scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_1));
					}
					countPunishScore = countPunishScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_1);
					countPerformanceScore = countPerformanceScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_4);
					countEvaluationScore = countEvaluationScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_2);
					if(null != scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_3)){
						countMilitaryScore = countMilitaryScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_3);
					}
					if(null != scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_5)){
						countFarmingScore = countFarmingScore + scoreMap.get(aca+StuworkConstants.BUSINESS_TYPE_5);
					}
				}else{
					array[0] = "";//操行名称
					array[1] = "";//值周表现
					array[2] = "";//违纪
					array[3] = "";//军训
					array[4] = "";//学农
					array[5] = floatVal+"";
					array[6] = floatVal+"";
					array[7] = floatVal+"";
					array[8] = floatVal+"";
					array[9] = floatVal+"";
				}
				arrayList.add(array);
			}
			if(maxValueMap==null){
				maxValueMap=new HashMap<>();
			}
			if(countPerformanceScore>maxValueMap.get("ZZBX.MAX.NUMBER") && 0 != maxValueMap.get("ZZBX.MAX.NUMBER")){
				countPerformanceScore = maxValueMap.get("ZZBX.MAX.NUMBER");
			}
			if(countEvaluationScore>maxValueMap.get("CXDD.MAX.NUMBER") && 0 != maxValueMap.get("CXDD.MAX.NUMBER")){
				countEvaluationScore = maxValueMap.get("CXDD.MAX.NUMBER");
			}
			if(countMilitaryScore>maxValueMap.get("JX.MAX.NUMBER") && 0 != maxValueMap.get("JX.MAX.NUMBER")){
				countMilitaryScore = maxValueMap.get("JX.MAX.NUMBER");
			}
			if(countFarmingScore>maxValueMap.get("XN.MAX.NUMBER") && 0 != maxValueMap.get("XN.MAX.NUMBER")){
				countFarmingScore = maxValueMap.get("XN.MAX.NUMBER");
			}
			float countNum = countPerformanceScore + countEvaluationScore + countMilitaryScore + countFarmingScore - countPunishScore;
			DyQualityComprehensiveDto dyQualityComprehensiveDto = new DyQualityComprehensiveDto();
			dyQualityComprehensiveDto.setAcadyearArray(acadyearArray);
			dyQualityComprehensiveDto.setCountNum(countNum);
			dyQualityComprehensiveDto.setValueList(arrayList);
			dtoMap.put(studentId, dyQualityComprehensiveDto);
		}
		return dtoMap;
	}
	
	


	
}
