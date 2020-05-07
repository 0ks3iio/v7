package net.zdsoft.stuwork.data.action;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.entity.DyStuMilitaryTraining;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;
import net.zdsoft.stuwork.data.entity.DyStuWeekCheckPerformance;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyStuEvaluationService;
import net.zdsoft.stuwork.data.service.DyStuMilitaryTrainingService;
import net.zdsoft.stuwork.data.service.DyStuPunishmentService;
import net.zdsoft.stuwork.data.service.DyStuWeekCheckPerformanceService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stuwork")
public class DyBusinessOptionAction extends BaseAction{
	@Autowired
	private DyBusinessOptionService dyBusinessOptionService;
	@Autowired
    private DyStuMilitaryTrainingService dyStuMilitaryTrainingService;
	@Autowired
	private DyStuWeekCheckPerformanceService dyStuWeekCheckPerformanceService;
	@Autowired
	private DyStuPunishmentService dyStuPunishmentService;	
	@Autowired
	private DyStuEvaluationService dyStuEvaluationService;
	@RequestMapping("/evaluation/score/index/page")
	@ControllerInfo(value = "index")
	public String showIndex(ModelMap map){
		return "/stuwork/stuEvaluation/evaluationScoreIndex.ftl";
	}
	@RequestMapping("/evaluation/score/list/page")
	@ControllerInfo(value = "折分设置页面")
	public String showList(ModelMap map,String businessType){
		if(StringUtils.isBlank(businessType)){
			businessType=StuworkConstants.BUSINESS_TYPE_1;
		}
		String unitId = getLoginInfo().getUnitId();
		List<DyBusinessOption> boptionList=dyBusinessOptionService.findListByUnitIdAndType(unitId, businessType);
		Set<String> bopSet = EntityUtils.getSet(boptionList, DyBusinessOption::getId);
		
		Set<String> yyBopSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(bopSet)){
			String[] bopArr = bopSet.toArray(new String[bopSet.size()]);
			if(StuworkConstants.BUSINESS_TYPE_1.equals(businessType)){
				List<DyStuPunishment> dyStuPunishmentList = dyStuPunishmentService.findByUnitIdAndPunishTypeIds(unitId, bopArr);
				if(CollectionUtils.isNotEmpty(dyStuPunishmentList)){
					yyBopSet = EntityUtils.getSet(dyStuPunishmentList, DyStuPunishment::getPunishTypeId);
				}
			}else if(StuworkConstants.BUSINESS_TYPE_2.equals(businessType)){
				List<DyStuEvaluation> dyStuEvaluationList = dyStuEvaluationService.findByUnitIdAndGradeIds(unitId, bopArr);
				if(CollectionUtils.isNotEmpty(dyStuEvaluationList)){
					yyBopSet = EntityUtils.getSet(dyStuEvaluationList, DyStuEvaluation::getGradeId);
				}
			}else if(StuworkConstants.BUSINESS_TYPE_3.equals(businessType)){
				List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingService.findByUnitIdAndGradeIds(unitId, bopArr);
				if(CollectionUtils.isNotEmpty(dyStuMilitaryTrainingList)){
					yyBopSet = EntityUtils.getSet(dyStuMilitaryTrainingList, DyStuMilitaryTraining::getGradeId);
				}
			}else if(StuworkConstants.BUSINESS_TYPE_4.equals(businessType)){
				List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByUnitIdAndGradeIds(unitId, bopArr);
				if(CollectionUtils.isNotEmpty(dyStuWeekCheckPerformanceList)){
					yyBopSet = EntityUtils.getSet(dyStuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getGradeId);
				}
			}else if(StuworkConstants.BUSINESS_TYPE_5.equals(businessType)){
				List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByUnitIdAndGradeIds(unitId, bopArr);
				if(CollectionUtils.isNotEmpty(dyStuWeekCheckPerformanceList)){
					yyBopSet = EntityUtils.getSet(dyStuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getGradeId);
				}
			}
		}
		int i = 1;
		for(DyBusinessOption item : boptionList){
			item.setOrderId(i);
		    i++;
		}
		
		map.put("yyBopSet",yyBopSet);
		map.put("boptionList",boptionList);
		map.put("businessType", businessType);
		return "/stuwork/stuEvaluation/evaluationScoreSetList.ftl";
	}
	@RequestMapping("/evaluation/score/addOption")
	@ControllerInfo(value = "新增维护等第页面")
	public String addOption(ModelMap map,String id,String businessType){		
		DyBusinessOption boption=null;
		if(StringUtils.isNotBlank(id)){
			boption=dyBusinessOptionService.findOne(id);
		}
		if(boption==null){
			boption=new DyBusinessOption();
			String type = "";
			if(StuworkConstants.BUSINESS_TYPE_1.equals(businessType)){
				type = StuworkConstants.BUSINESS_TYPE_1;
			}else if(StuworkConstants.BUSINESS_TYPE_2.equals(businessType)){
				type = StuworkConstants.BUSINESS_TYPE_2;
			}else if(StuworkConstants.BUSINESS_TYPE_3.equals(businessType)){
				type = StuworkConstants.BUSINESS_TYPE_3;
			}else if(StuworkConstants.BUSINESS_TYPE_4.equals(businessType)){
				type = StuworkConstants.BUSINESS_TYPE_4;
			}else{
				type = StuworkConstants.BUSINESS_TYPE_5;
			}
			List<DyBusinessOption> boptionList=dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), type);
			Set<Integer> orderIdSet = new HashSet<Integer>();
			for(DyBusinessOption item : boptionList){
				orderIdSet.add(item.getOrderId());
			}
			if(CollectionUtils.isEmpty(boptionList)){
				boption.setOrderId(1);
			}else{
				boption.setOrderId(boptionList.get(boptionList.size()-1).getOrderId()+1);
			}
			
		}		
		map.put("boption", boption);
		map.put("businessType", businessType);
		return "/stuwork/stuEvaluation/evaluationOptionEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/evaluation/score/changeOrderId")
	public String getStuCode(String businessType){
		List<DyBusinessOption> boptionList=dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), businessType);
		Set<Integer> orderIdSet = new HashSet<Integer>();
		for(DyBusinessOption item : boptionList){
			orderIdSet.add(item.getOrderId());
		}
		if(CollectionUtils.isEmpty(boptionList)){
			return success("1");
		}else{
			return success(String.valueOf(Collections.max(orderIdSet)+1));
		}
	}
	
	@ResponseBody
	@RequestMapping("/evaluation/score/saveOption")
	@ControllerInfo(value = "保存等第")
	public String saveOption(ModelMap map, DyBusinessOption boption){
		try {
			String type = "";
			if(StuworkConstants.BUSINESS_TYPE_2.equals(boption.getBusinessType())){
				type = StuworkConstants.BUSINESS_TYPE_2;
			}else if(StuworkConstants.BUSINESS_TYPE_3.equals(boption.getBusinessType())){
				type = StuworkConstants.BUSINESS_TYPE_3;
			}else if(StuworkConstants.BUSINESS_TYPE_4.equals(boption.getBusinessType())){
				type = StuworkConstants.BUSINESS_TYPE_4;
			}else if(StuworkConstants.BUSINESS_TYPE_5.equals(boption.getBusinessType())){
				type = StuworkConstants.BUSINESS_TYPE_5;
			}else{
				type = StuworkConstants.BUSINESS_TYPE_1;
			}
			if(StuworkConstants.BUSINESS_TYPE_1.equals(type)){
				if(0 == boption.getHasScore() || 1 == boption.getIsCustom()){
					boption.setScore(0);
				}
			}else{
				boption.setHasScore(1);
				boption.setIsCustom(0);
			}
			if(StringUtils.isNotBlank(boption.getId())){
				dyBusinessOptionService.update(boption,boption.getId(),new String[]{"optionName","score","isCustom","hasScore","orderId"});
			}else{
				/*if(flag){
					return error("名称不能重复");
				}
				if(flag2){
					return error("序号不能重复");
				}*/
				boption.setId(UuidUtils.generateUuid());
				boption.setUnitId(getLoginInfo().getUnitId());
				//boption.setBusinessType(StuworkConstants.BUSINESS_TYPE_2);
				//boption.setHasScore(1);
				//boption.setIsCustom(0);
				//boption.setOrderId(dyBusinessOptionList.size()+1);
				dyBusinessOptionService.save(boption);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/evaluation/score/deleteOption")
	@ControllerInfo(value = "删除等第以及折分")
	public String deleteOption(ModelMap map, String id, String businessType){
		try {
			//dyBusinessOptionService.deleteAndOrder(id, getLoginInfo().getUnitId(), businessType);
			dyBusinessOptionService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/evaluation/score/saveScore")
	@ControllerInfo(value = "保存折分")
	public String saveScore(ModelMap map, DyBusinessOptionDto optionDto){
		try {
			if(CollectionUtils.isEmpty(optionDto.getDyBusinessOptionList())){
				return returnError("-1","无数据");
			}
			dyBusinessOptionService.saveAll(optionDto.getDyBusinessOptionList().toArray(new DyBusinessOption[0]));
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	
	
	/*@RequestMapping("/studentManage/punishType")
	public String findBusinessOptionList(ModelMap map){
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_1);
		for(DyBusinessOption item : dyBusinessOptionList){
			if(StuworkConstants.PUNISH_TYPE_NAME_1.equals(item.getOptionName())){
				float score0 = item.getScore();
				map.put("score0", score0);
			}else if(StuworkConstants.PUNISH_TYPE_NAME_2.equals(item.getOptionName())){
				float score1 = item.getScore();
				map.put("score1", score1);
			}else if(StuworkConstants.PUNISH_TYPE_NAME_3.equals(item.getOptionName())){
				float score2 = item.getScore();
				map.put("score2", score2);
			}else if(StuworkConstants.PUNISH_TYPE_NAME_4.equals(item.getOptionName())){
				float score3 = item.getScore();
				map.put("score3", score3);
			}else if(StuworkConstants.PUNISH_TYPE_NAME_5.equals(item.getOptionName())){
				float score4 = item.getScore();
				map.put("score4", score4);
			}
		}
		return "/stuwork/studentManage/studentPunishType.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/studentManage/punishTypeSave")
	public String save(DyBusinessOptionDto dyBusinessOptionDto){
		try{
			List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionDto.getDyBusinessOptionList();
			for(DyBusinessOption item : dyBusinessOptionList){
				item.setId(UuidUtils.generateUuid());
				item.setBusinessType(StuworkConstants.BUSINESS_TYPE_1);
				item.setUnitId(getLoginInfo().getUnitId());
				item.setHasScore(1);
				item.setIsCustom(0);
			}
			dyBusinessOptionService.save(dyBusinessOptionList,getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_1);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}*/
}
