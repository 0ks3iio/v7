package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.stuwork.data.dto.DyCollectDto;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.entity.DyStuMilitaryTraining;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;
import net.zdsoft.stuwork.data.entity.DyStuStudyingFarming;
import net.zdsoft.stuwork.data.entity.DyStuWeekCheckPerformance;
import net.zdsoft.stuwork.data.entity.DyStudentRewardPoint;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyCollectService;
import net.zdsoft.stuwork.data.service.DyStuEvaluationService;
import net.zdsoft.stuwork.data.service.DyStuMilitaryTrainingService;
import net.zdsoft.stuwork.data.service.DyStuPunishmentService;
import net.zdsoft.stuwork.data.service.DyStuStudyingFarmingService;
import net.zdsoft.stuwork.data.service.DyStuWeekCheckPerformanceService;
import net.zdsoft.stuwork.data.service.DyStudentRewardPointService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyCollectService")
public class DyCollectServiceImpl implements DyCollectService{
	@Autowired
	private DyStuEvaluationService dyStuEvaluationService;
	@Autowired
	private DyBusinessOptionService dyBusinessOptionService;
	@Autowired
	private DyStudentRewardPointService dyStudentRewardPointService;
	@Autowired
	private DyStuMilitaryTrainingService dyStuMilitaryTrainingService;
	@Autowired
	private DyStuWeekCheckPerformanceService dyStuWeekCheckPerformanceService;
	@Autowired
	private DyStuStudyingFarmingService dyStuStudyingFarmingService;
	@Autowired
	private DyStuPunishmentService dyStuPunishmentService;
	
	public List<DyCollectDto> findCollectList4(String unitId,String acadyear, String semester, String[] studentIds){
		boolean flag=studentIds.length>=1000;
		//学生奖励
		Map<String,Map<String,List<DyStudentRewardPoint>>> rewardMap=getRewardMap(unitId, acadyear, semester,flag?null:studentIds);
		List<DyCollectDto> dtoList=new ArrayList<>();
		DyCollectDto dto=null;
		Map<String, List<DyStudentRewardPoint>> inMap=null;
		List<DyStudentRewardPoint> inList=null;
		Float inScore=null;
		Float inScore2=null;
		//组装最终数据 一个学生一条
		for (String stuId : studentIds) {
			dto=new DyCollectDto();
			dto.setStudentId(stuId);
			if(rewardMap.containsKey(stuId)){
				inMap=rewardMap.get(stuId);
				inScore=0.0f;
				inScore2=0.0f;
				if(inMap.containsKey("艺术节")){
					inList=inMap.get("艺术节");
					for (DyStudentRewardPoint dyStudentRewardPoint : inList) {
						if("团体".equals(dyStudentRewardPoint.getProjectName())){
							inScore+=dyStudentRewardPoint.getRewardPoint();
						}else if("个人".equals(dyStudentRewardPoint.getProjectName())){
							inScore2+=dyStudentRewardPoint.getRewardPoint();
						}
					}
				}
				dto.setyGroupScore(inScore);
				dto.setyPerScore(inScore2);
				inScore=0.0f;
				inScore2=0.0f;
				if(inMap.containsKey("体育节")){
					inList=inMap.get("体育节");
					for (DyStudentRewardPoint dyStudentRewardPoint : inList) {
						if("团体".equals(dyStudentRewardPoint.getProjectName())){
							inScore+=dyStudentRewardPoint.getRewardPoint();
						}else if("个人".equals(dyStudentRewardPoint.getProjectName())){
							inScore2+=dyStudentRewardPoint.getRewardPoint();
						}
					}
				}
				dto.settGroupScore(inScore);
				dto.settPerScore(inScore2);
				inScore=0.0f;
				inScore2=0.0f;
				if(inMap.containsKey("外语节")){
					inList=inMap.get("外语节");
					for (DyStudentRewardPoint dyStudentRewardPoint : inList) {
						if("团体".equals(dyStudentRewardPoint.getProjectName())){
							inScore+=dyStudentRewardPoint.getRewardPoint();
						}else if("个人".equals(dyStudentRewardPoint.getProjectName())){
							inScore2+=dyStudentRewardPoint.getRewardPoint();
						}
					}
				}
				dto.setwGroupScore(inScore);
				dto.setwPerScore(inScore2);
				inScore=0.0f;
				inScore2=0.0f;
				if(inMap.containsKey("科技节")){
					inList=inMap.get("科技节");
					for (DyStudentRewardPoint dyStudentRewardPoint : inList) {
						if("团体".equals(dyStudentRewardPoint.getProjectName())){
							inScore+=dyStudentRewardPoint.getRewardPoint();
						}else if("个人".equals(dyStudentRewardPoint.getProjectName())){
							inScore2+=dyStudentRewardPoint.getRewardPoint();
						}
					}
				}
				dto.setkGroupScore(inScore);
				dto.setkPerScore(inScore2);
				inScore=0.0f;
				inScore2=0.0f;
				if(inMap.containsKey("文化节")){
					inList=inMap.get("文化节");
					for (DyStudentRewardPoint dyStudentRewardPoint : inList) {
						if("团体".equals(dyStudentRewardPoint.getProjectName())){
							inScore+=dyStudentRewardPoint.getRewardPoint();
						}else if("个人".equals(dyStudentRewardPoint.getProjectName())){
							inScore2+=dyStudentRewardPoint.getRewardPoint();
						}
					}
				}
				dto.setWenGroupScore(inScore);
				dto.setWenPerScore(inScore2);
			}
			dtoList.add(dto);
		}
		return dtoList;
	}
	@Override
	public List<DyCollectDto> findCollectList2(String unitId,String acadyear, String semester, String[] studentIds) {
//		List<DyBusinessOption> optionList=dyBusinessOptionService.findListByUnitId(unitId);
//		Map<String,Float> scoreMap=EntityUtils.getMap(optionList, DyBusinessOption::getId,DyBusinessOption::getScore);
		boolean flag=studentIds.length>=1000;
//		boolean flag=true;
		//1 学生评语 操行等第
		List<DyStuEvaluation> evaList=dyStuEvaluationService.findByStudentIdIn(flag?null:studentIds, unitId, acadyear, semester);
		//key-studentId   一个学生 一个学年学期只有一条
		Map<String,DyStuEvaluation> evaluationMap=EntityUtils.getMap(evaList, DyStuEvaluation::getStudentId);
		//学生奖励
		Map<String,Map<String,List<DyStudentRewardPoint>>> rewardMap=getRewardMap(unitId, acadyear, semester, flag?null:studentIds);
		//军训管理
		List<DyStuMilitaryTraining> trainList=dyStuMilitaryTrainingService.findByUnitIdAndStudentIds(unitId, flag?null:studentIds, acadyear, semester);
		Map<String,Float> trainMap=new HashMap<>();
		Float score=null;
		if(CollectionUtils.isNotEmpty(trainList)){
			for (DyStuMilitaryTraining dyStuMilitaryTraining : trainList) {
				score=trainMap.get(dyStuMilitaryTraining.getStudentId());
				if(score==null){
					score=0.0f;
				}
				trainMap.put(dyStuMilitaryTraining.getStudentId(), score+dyStuMilitaryTraining.getScore());
			}
		}
		//值周表现
		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByUnitIdAndStuIds(unitId, acadyear, semester,flag?null:studentIds);
		Map<String,Float> weekCheckMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(dyStuWeekCheckPerformanceList)){
			for (DyStuWeekCheckPerformance dyStuWeekCheckPerformance : dyStuWeekCheckPerformanceList) {
				score=weekCheckMap.get(dyStuWeekCheckPerformance.getStudentId());
				if(score==null){
					score=0.0f;
				}
				weekCheckMap.put(dyStuWeekCheckPerformance.getStudentId(), score+dyStuWeekCheckPerformance.getScore());
			}
		}
		//学农管理
		List<DyStuStudyingFarming> dyStuStudyingFarmingList = dyStuStudyingFarmingService.findByUnitIdAndStuIds(unitId, acadyear, semester, flag?null:studentIds);
		Map<String,Float> studyingMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(dyStuStudyingFarmingList)){
			for (DyStuStudyingFarming dyStuStudyingFarming : dyStuStudyingFarmingList) {
				score=studyingMap.get(dyStuStudyingFarming.getStudentId());
				if(score==null){
					score=0.0f;
				}
				studyingMap.put(dyStuStudyingFarming.getStudentId(), score+dyStuStudyingFarming.getScore());
			}
		}
		//违纪处罚
		List<DyStuPunishment>  punishList=dyStuPunishmentService.findByUnitIdAndStuIdIn(unitId, acadyear, semester, flag?null:studentIds);
		Map<String,Float> punishMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(punishList)){
			for (DyStuPunishment punish : punishList) {
				score=punishMap.get(punish.getStudentId());
				if(score==null){
					score=0.0f;
				}
				punishMap.put(punish.getStudentId(), score+punish.getScore());
			}
		}
		List<DyCollectDto> dtoList=new ArrayList<>();
		DyCollectDto dto=null;
		DyStuEvaluation eva=null;
		Map<String, List<DyStudentRewardPoint>> inMap=null;
		List<DyStudentRewardPoint> inList=null;
		Float inScore=null;
		//组装最终数据 一个学生一条
		for (String stuId : studentIds) {
			dto=new DyCollectDto();
			dto.setStudentId(stuId);
			//品德操行等第
			if(evaluationMap.containsKey(stuId)){
				eva=evaluationMap.get(stuId);
				dto.setEvatr(eva.getGrade());
				dto.setEvaScore(eva.getScore());
			}
			//社会实践折分  进行累加军训、值周表现、学农三个部分的综合折分
			inScore=0.0f;
			if(trainMap.containsKey(stuId)){//军训
				inScore+=trainMap.get(stuId);
			}
			if(weekCheckMap.containsKey(stuId)){//值周表现
				inScore+=weekCheckMap.get(stuId);
			}
			if(studyingMap.containsKey(stuId)){//学农
				inScore+=studyingMap.get(stuId);
			}
			dto.setTryScore(inScore);
			//以下是学生奖励的数据
			if(rewardMap.containsKey(stuId)){
				inMap=rewardMap.get(stuId);
				//学生干部（就高）
				inScore=0.0f;
				if(inMap.containsKey("学生干部")){
					inList=inMap.get("学生干部");
					for (DyStudentRewardPoint in : inList) {
						if(inScore<=in.getRewardPoint()){
							inScore=in.getRewardPoint();
						}
					}
					dto.setStuPerScore(inScore);
				}
				//社团骨干折分
				inScore=0.0f;
				if(inMap.containsKey("社团骨干")){
					inList=inMap.get("社团骨干");
					for (DyStudentRewardPoint in : inList) {
						inScore+=in.getRewardPoint();
					}
					dto.setGroupScore(inScore);
				}
				//其它奖励  包括评优（就高）、先进（就高）、突出贡献3个部分的折分之和
				inScore=0.0f;
				if(inMap.containsKey("评优先进")){
					inList=inMap.get("评优先进");
					Float inScore2=0.0f;
					for (DyStudentRewardPoint in : inList) {
						if("评优".equals(in.getProjectRemark())){
							if(inScore<=in.getRewardPoint()){
								inScore=in.getRewardPoint();
							}
						}else if("先进".equals(in.getProjectRemark())){
							if(inScore2<=in.getRewardPoint()){
								inScore2=in.getRewardPoint();
							}
						}
					}
					inScore+=inScore2;//评优和先进累加
				}
				//继续与突出贡献累加 所以inScore不清0
				if(inMap.containsKey("突出贡献")){
					inList=inMap.get("突出贡献");
					for (DyStudentRewardPoint in : inList) {
						inScore+=in.getRewardPoint();
					}
				}
				dto.setOtherScore(inScore);
			}
			//处罚折分
			if(punishMap.containsKey(stuId)){
				dto.setPunishScore(punishMap.get(stuId));
			}
			dtoList.add(dto);
		}
		return dtoList;
	}
	public Map<String,Map<String,List<DyStudentRewardPoint>>> getRewardMap(String unitId,String acadyear, String semester, String[] studentIds){
		List<DyStudentRewardPoint> dyStudentRewardPointList = dyStudentRewardPointService.getStudentRewardPointByStudentId(unitId, studentIds, acadyear, semester, null,true);
		Map<String,Map<String,List<DyStudentRewardPoint>>> rewardMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(dyStudentRewardPointList)){
			Map<String,List<DyStudentRewardPoint>> listMap=new HashMap<>();
			for (DyStudentRewardPoint dyStudentRewardPoint : dyStudentRewardPointList) {
				listMap=rewardMap.get(dyStudentRewardPoint.getStudentId());
				if(listMap==null){
					listMap=new HashMap<>();
				}
				if(!listMap.containsKey(dyStudentRewardPoint.getRewardClasses())){
					listMap.put(dyStudentRewardPoint.getRewardClasses(), new ArrayList<>());
				}
				listMap.get(dyStudentRewardPoint.getRewardClasses()).add(dyStudentRewardPoint);
				rewardMap.put(dyStudentRewardPoint.getStudentId(), listMap);
			}
		}
		return rewardMap;
	}
}
