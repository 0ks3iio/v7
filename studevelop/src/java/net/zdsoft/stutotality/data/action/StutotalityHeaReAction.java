package net.zdsoft.stutotality.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stutotality.data.dto.HealthDto;
import net.zdsoft.stutotality.data.entity.*;
import net.zdsoft.stutotality.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
@RequestMapping("/stutotality")
public class StutotalityHeaReAction extends BaseAction{
	private static Logger logger = LoggerFactory.getLogger(StutotalityHeaReAction.class);
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StutotalityHealthService stutotalityHealthService;
	@Autowired
	private StutotalityHealthOptionService stutotalityHealthOptionService;
	@Autowired
	private StutotalityRewardService stutotalityRewardService;
	@Autowired
	private StutotalityItemService stutotalityItemService;
	@Autowired
	private StutotalitySchoolNoticeService stutotalitySchoolNoticeService;
    @Autowired
    private StutotalityScaleService stutotalityScaleService;
    @Autowired
    private StutotalityItemOptionService stutotalityItemOptionService;
	@RequestMapping("/healthItem/list")
	@ControllerInfo(value = "显示所有年级身心项目达标标准")
	public String getHealthItemList(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY}), Grade.class);
		map.put("gradeList",gradeList);
		List<StutotalityHealth> healthItemList = stutotalityHealthService.findHealthItemByUnitId(unitId);

		/*
		 if (CollectionUtils.isEmpty(healthItemList)){
			List<StutotalityHealth> healthItemByUnitId = stutotalityHealthService.findHealthItemByUnitId(BaseConstants.ZERO_GUID);
			Set<String> itemIds=EntityUtils.getSet(healthItemByUnitId,StutotalityHealth::getId);
			List<StutotalityHealthOption> allOptionList = stutotalityHealthOptionService.getListByTypeIds(typeIds.toArray(new String[0]));
			Map<String,List<StutotalityHealthOption>> optionsMap=allOptionList.stream().collect(Collectors.groupingBy(StutotalityHealthOption::getItemId));

		 }
		*/

		if (CollectionUtils.isEmpty(healthItemList)){
			List<StutotalityHealth> healthItemByUnitId = stutotalityHealthService.findHealthItemByUnitId(BaseConstants.ZERO_GUID);
			List<StutotalityHealth> healthList = new ArrayList<>();
			Map<String,String> healthIdOfIdMap=new HashMap<>();
			for (StutotalityHealth healthItem : healthItemByUnitId){
				StutotalityHealth stutotalityHealth = new StutotalityHealth();
				stutotalityHealth.setId(UuidUtils.generateUuid());
				stutotalityHealth.setUnitId(unitId);
				stutotalityHealth.setHealthName(healthItem.getHealthName());
				stutotalityHealth.setOrderNumber(healthItem.getOrderNumber());
				stutotalityHealth.setCanDeleted(healthItem.getCanDeleted());
				stutotalityHealth.setCreationTime(new Date());
				stutotalityHealth.setModifyTime(new Date());
				healthList.add(stutotalityHealth);
				healthIdOfIdMap.put(healthItem.getId(),stutotalityHealth.getId());
			}
			// 存入数据库中
			stutotalityHealthService.saveAll(healthList.toArray(new StutotalityHealth[0]));
			List<StutotalityHealthOption> optionList = stutotalityHealthOptionService.findByUnitId(unitId);
			List<StutotalityHealthOption> stutotalityHealthOptions = new ArrayList<StutotalityHealthOption>();
			if (CollectionUtils.isEmpty(optionList)){
				List<StutotalityHealthOption> healthOptionList = stutotalityHealthOptionService.findByUnitId(BaseConstants.ZERO_GUID);
				for(StutotalityHealthOption healthOption : healthOptionList){
					StutotalityHealthOption stutotalityHealthOption = new StutotalityHealthOption();
					stutotalityHealthOption.setId(UuidUtils.generateUuid());
					stutotalityHealthOption.setUnitId(unitId);
					stutotalityHealthOption.setGradeCode(healthOption.getGradeCode());
					stutotalityHealthOption.setHealthId(healthIdOfIdMap.get(healthOption.getHealthId()));
					stutotalityHealthOption.setHealthStandard(healthOption.getHealthStandard());
					stutotalityHealthOption.setCreationTime(new Date());
					stutotalityHealthOption.setModifyTime(new Date());
					stutotalityHealthOptions.add(stutotalityHealthOption);
				}
				stutotalityHealthOptionService.saveAll(stutotalityHealthOptions.toArray(new StutotalityHealthOption[0]));
			}
			//key healthId+_+gradeCode
			Map<String,String> valueMap=new HashMap<>();
			//key healthId  value 达标情况不为空的情况下是true
			Map<String,Boolean> healthIdMap=new HashMap<>();
			if(!CollectionUtils.isEmpty(stutotalityHealthOptions)){
				stutotalityHealthOptions.forEach(option->{
					valueMap.put(option.getHealthId()+"_"+option.getGradeCode(),option.getHealthStandard()==null?"null":option.getHealthStandard());
					if(!healthIdMap.containsKey(option.getHealthId())){
						healthIdMap.put(option.getHealthId(),StringUtils.isNotBlank(option.getHealthStandard()));
					}else{
						if(!healthIdMap.get(option.getHealthId())){
							healthIdMap.put(option.getHealthId(),StringUtils.isNotBlank(option.getHealthStandard()));
						}
					}
				});
			}
			map.put("healthIdMap",healthIdMap);
			map.put("valueMap",valueMap);
			map.put("healthItemList",healthList);
		}else {
			List<StutotalityHealthOption> optionList = stutotalityHealthOptionService.findByUnitId(unitId);
			//key healthId+_+gradeCode
			Map<String,String> valueMap=new HashMap<>();
			//key healthId  value 达标情况不为空的情况下是true
			Map<String,Boolean> healthIdMap=new HashMap<>();
			if(!CollectionUtils.isEmpty(optionList)){
				optionList.forEach(option->{
					valueMap.put(option.getHealthId()+"_"+option.getGradeCode(),option.getHealthStandard()==null?"null":option.getHealthStandard());
					if(!healthIdMap.containsKey(option.getHealthId())){
						healthIdMap.put(option.getHealthId(),StringUtils.isNotBlank(option.getHealthStandard()));
					}else{
						if(!healthIdMap.get(option.getHealthId())){
							healthIdMap.put(option.getHealthId(),StringUtils.isNotBlank(option.getHealthStandard()));
						}
					}
				});
			}
			map.put("healthItemList",healthItemList);
			map.put("healthIdMap",healthIdMap);
			map.put("valueMap",valueMap);
		}
		return  "/stutotality/health/stutotalityHealthList.ftl";
		
	}


//========================= 编辑达标标准 ===============================

	@RequestMapping("/healthStandard/edit")
	@ControllerInfo(value = "编辑达标标准/准备数据")
	@ResponseBody
	public List<StutotalityHealthOption> editHealthStandard(String healthId){
		String unitId = getLoginInfo().getUnitId();
		List<StutotalityHealthOption> healthItemByGrade = stutotalityHealthOptionService.findByHealthIdAndUnitId(unitId,healthId);
		List<Grade> gradeList=SUtils.dt(gradeRemoteService.findBySchoolId(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY}),Grade.class);
		Map<String,String> gradeCodeMap=EntityUtils.getMap(gradeList,Grade::getGradeCode,Grade::getGradeName);
		for (StutotalityHealthOption healthOption : healthItemByGrade) {
			healthOption.setGradeName(gradeCodeMap.get(healthOption.getGradeCode()));
			if (StringUtils.isBlank(healthOption.getHealthStandard())){
				healthOption.setHealthStandard("");
			}
		}
		return healthItemByGrade ;
	}

	@RequestMapping("/healthStandard/doEdit")
	@ControllerInfo(value = "开始编辑各个年级的达标标准")
	@ResponseBody
	public String toEditHealthStandard(HealthDto healthDto){
		try{
			String unitId = getLoginInfo().getUnitId();
			List<StutotalityHealthOption> optionList = healthDto.getOptionList();
			for (StutotalityHealthOption option : optionList) {
				option.setUnitId(unitId);
				//option.setId(UuidUtils.generateUuid());
				//option.setCreationTime(new Date());
				option.setModifyTime(new Date());
				stutotalityHealthOptionService.deleteByhealthId(option.getHealthId());
			}
			stutotalityHealthOptionService.saveAll(optionList.toArray(new StutotalityHealthOption[0]));
		}catch (Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

//========================= 编辑身心项目 ===============================

	@RequestMapping("/healthItem/edit")
	@ControllerInfo(value = "准备数据/编辑身心项目")
	public String editHealthItem(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<StutotalityHealth> healthItemList = stutotalityHealthService.findHealthItemByUnitIdWithMaster(unitId);
		Map<String,List<StutotalityHealthOption>> optionMap = new HashMap<String,List<StutotalityHealthOption>>();
		List<StutotalityHealthOption> optionList = stutotalityHealthOptionService.findByUnitIdWithMaster(unitId);
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY}), Grade.class);
		Map<String,String> gradeCodeMap=EntityUtils.getMap(gradeList,Grade::getGradeCode,Grade::getGradeName);
		for (StutotalityHealthOption option : optionList){
			List<StutotalityHealthOption> list = optionMap.get(option.getHealthId());
			option.setGradeName(gradeCodeMap.get(option.getGradeCode()));
			if (CollectionUtils.isEmpty(list)){
				list = new ArrayList<>();
				optionMap.put(option.getHealthId(),list);
			}
			list.add(option);
		}
		int lastIndex=0;
		for(StutotalityHealth health : healthItemList){
			List<StutotalityHealthOption> list = optionMap.get(health.getId());
			health.setHealthOptions(list);
			lastIndex=health.getOrderNumber();
		}
		map.put("lastIndex",lastIndex);
		map.put("gradeList",gradeList);
		map.put("healthItemList",healthItemList);
		return "/stutotality/health/stutotalityHealthEdit.ftl";
	}
	@RequestMapping("/healthItem/addOrUpdate")
	@ControllerInfo(value = "开始编辑身心项目")
	@ResponseBody
	public String toEditHealthItem(HealthDto healthDto){
		try {
			String unitId = getLoginInfo().getUnitId();
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY}), Grade.class);
			List<StutotalityHealth> healthList = healthDto.getHealthList();
			ArrayList<StutotalityHealth> stutotalityHealths = new ArrayList<>();
			List<StutotalityHealthOption> stutotalityHealthOption = new ArrayList<>();
			int i=1;
			for (StutotalityHealth stutotalityHealth : healthList) {
				if (StringUtils.isBlank(stutotalityHealth.getHealthName())){
					continue;
				}
				if (StringUtils.isBlank(stutotalityHealth.getId())){
					String healthId = UuidUtils.generateUuid();
					stutotalityHealth.setId(healthId);
					stutotalityHealth.setUnitId(unitId);
					stutotalityHealth.setOrderNumber(i);
					stutotalityHealth.setCanDeleted(1);
					stutotalityHealth.setCreationTime(new Date());
					stutotalityHealths.add(stutotalityHealth);
					for(Grade grade : gradeList){
						StutotalityHealthOption healthOption = new StutotalityHealthOption();
						healthOption.setId(UuidUtils.generateUuid());
						healthOption.setUnitId(unitId);
						healthOption.setHealthId(healthId);
						healthOption.setGradeCode(grade.getGradeCode());
						healthOption.setCreationTime(new Date());
						stutotalityHealthOption.add(healthOption);
					}
				}else {
					stutotalityHealth.setUnitId(unitId);
					if(stutotalityHealth.getCreationTime()==null){
						stutotalityHealth.setCreationTime(new Date());
					}
					stutotalityHealth.setModifyTime(new Date());
					stutotalityHealth.setOrderNumber(i);
					stutotalityHealths.add(stutotalityHealth);
				}
				i++;
			}
			stutotalityHealthOptionService.saveAll(stutotalityHealthOption.toArray(new StutotalityHealthOption[0]));
			stutotalityHealthService.deleteByUnitId(unitId);
			stutotalityHealthService.saveAll(stutotalityHealths.toArray(new StutotalityHealth[0]));
		}catch (Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	@RequestMapping("/healthItem/readyUpdateGradeCode")
	@ControllerInfo(value = "准备修改年级")
	@ResponseBody
	public String EditGradeCode(String healthId){
		String unitId = getLoginInfo().getUnitId();
		List<StutotalityHealthOption> healthItemByGrade = stutotalityHealthOptionService.findByHealthIdAndUnitId(unitId,healthId);
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY}), Grade.class);
		JSONObject json=new JSONObject();
		json.put("healthItemByGrade",healthItemByGrade);
		json.put("gradeList",gradeList);
		return JSON.toJSONString(json);
	}

	@RequestMapping("/healthItem/updateGradeCode")
	@ControllerInfo(value = "修改年级")
	@ResponseBody
	public String toEditGradeCode(String healthId,String healthName,Integer healthIndex, HealthDto healthDto){
		String unitId=getLoginInfo().getUnitId();
		JSONObject json=new JSONObject();
		try{
			//key是年级code
			Map<String,StutotalityHealthOption> codeOptionMap=null;
			if(StringUtils.isBlank(healthId)){
				healthId=UuidUtils.generateUuid();
				json.put("healthId",healthId);
				StutotalityHealth health=new StutotalityHealth();
				health.setId(healthId);
				health.setUnitId(unitId);
				health.setHealthName(healthName);
				health.setOrderNumber(healthIndex);
				health.setCreationTime(new Date());
				health.setModifyTime(new Date());
				stutotalityHealthService.save(health);
				codeOptionMap=new HashMap<>();
			}else{
				codeOptionMap=EntityUtils.getMap(stutotalityHealthOptionService
						.findByHealthIdAndUnitId(unitId,healthId),StutotalityHealthOption::getGradeCode);
			}
			List<StutotalityHealthOption> optionList=healthDto.getOptionList();
			List<StutotalityHealthOption> insertList=new ArrayList<>();
			if(CollectionUtils.isNotEmpty(optionList)){
				StutotalityHealthOption oldOption=null;
				for(StutotalityHealthOption option:optionList){
					if(StringUtils.isBlank(option.getGradeCode())){
						continue;
					}
					if(codeOptionMap.containsKey(option.getGradeCode())){
						oldOption=codeOptionMap.get(option.getGradeCode());
						insertList.add(oldOption);
					}else{
						option.setId(UuidUtils.generateUuid());
						option.setUnitId(unitId);
						option.setHealthId(healthId);
						option.setCreationTime(new Date());
						option.setModifyTime(new Date());
						insertList.add(option);
					}
				}
			}
			stutotalityHealthOptionService.deleteByhealthId(healthId);
			if(CollectionUtils.isNotEmpty(insertList)){
				stutotalityHealthOptionService.saveAll(insertList.toArray(new StutotalityHealthOption[0]));
			}
			json.put("success",true);
		}catch (Exception e){
			e.printStackTrace();
			json.put("success",false);
			return json.toJSONString();
		}
		return json.toJSONString();
	}


// ========================= 其他項目（获奖情况&&兴趣特长） ===============================
	@RequestMapping("/rewardAndInterest/index")
	@ControllerInfo(value = "其他项目index(获奖情况和兴趣)")
	public String getRewardAndInterestList(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
		//当前学年 学期
		String acadyear = semesterObj.getAcadyear();
		String semester = semesterObj.getSemester() + "";
		List<StutotalityReward> rewardList = stutotalityRewardService.findByUnitIdAndAcadyearAndSemesterWithMaster(unitId, acadyear, semester);
		try {
			if (CollectionUtils.isEmpty(rewardList)) {
				List<StutotalityReward> stutotalityRewardList = stutotalityRewardService.findByUnitId(BaseConstants.ZERO_GUID);
				List<StutotalityReward> stutotalityRewards = new ArrayList<StutotalityReward>();
				for (StutotalityReward reward : stutotalityRewardList) {
					StutotalityReward stutotalityReward = new StutotalityReward();
					stutotalityReward.setId(UuidUtils.generateUuid());
					stutotalityReward.setUnitId(unitId);
					stutotalityReward.setAcadyear(acadyear);
					stutotalityReward.setSemester(semester);
					stutotalityReward.setGradeId(BaseConstants.ZERO_GUID);
					stutotalityReward.setRewardName(reward.getRewardName());
					stutotalityReward.setStarNumber(reward.getStarNumber());
					stutotalityReward.setCreationTime(new Date());
					stutotalityRewards.add(stutotalityReward);
				}
				stutotalityRewardService.saveAll(stutotalityRewards.toArray(new StutotalityReward[0]));
				map.put("rewardList", stutotalityRewards);
			}else{
				map.put("rewardList", rewardList);
			}
			List<StutotalityItem> interestList = stutotalityItemService.findByUnitIdAndSubjectTypeWithMaster(unitId, new String[]{"9"});
			if (CollectionUtils.isEmpty(interestList)) {
				List<StutotalityItem> items = new ArrayList<>();
				List<StutotalityItem> subjectType = stutotalityItemService.findByUnitIdAndSubjectType(BaseConstants.ZERO_GUID, new String[]{"9"});
				for (StutotalityItem item : subjectType) {
					StutotalityItem stutotalityItem = new StutotalityItem();
					stutotalityItem.setId(UuidUtils.generateUuid());
					stutotalityItem.setUnitId(unitId);
					stutotalityItem.setGradeId(BaseConstants.ZERO_GUID);
					stutotalityItem.setItemName(item.getItemName());
					stutotalityItem.setSubjectType("9");
					stutotalityItem.setCreationTime(new Date());
					items.add(stutotalityItem);
				}
				stutotalityItemService.saveAll(items.toArray(new StutotalityItem[0]));
				map.put("interestList", subjectType);
			}else{
				map.put("interestList", interestList);
			}
			//全优生科目
             List<StutotalityItem> socreItems = stutotalityItemService.findByUnitIdAndYearAndSemesterAndSubjectType(unitId,acadyear,semester,new String[]{"2"});
            if(CollectionUtils.isNotEmpty(socreItems)){
                List<StutotalityItem> newItems = new ArrayList<>();
                List<String> itemName = new ArrayList<>();
                for (StutotalityItem socreItem : socreItems) {
                    if(StringUtils.isNotBlank(socreItem.getSubjectId())){
                        if(!itemName.contains(socreItem.getItemName())){
                            newItems.add(socreItem);
                            itemName.add(socreItem.getItemName());
                        }
                    }
                }
                map.put("socreItemList", newItems);
                // 查全优生占比  全优生科目 达标线
                List<StutotalityScale> scales = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId, BaseConstants.ZERO_GUID, "4");
                if(CollectionUtils.isNotEmpty(scales)){
                    StutotalityScale scale = scales.get(0);
                    map.put("scale",scale.getScale());
                    map.put("score",scale.getStandardScore());
                }
                List<StutotalityItem> item8 = stutotalityItemService.findByUnitIdAndSubjectType(unitId, new String[]{"8"});
                if(CollectionUtils.isNotEmpty(item8)){
                    StutotalityItem item = item8.get(0);
                    map.put("subjectId",item.getSubjectId());
                }
            }
        } catch (Exception e) {
			e.printStackTrace();
		}
		return "/stutotality/health/stutotalityRewardAndInterest.ftl";
	}

	@RequestMapping("/rewardRank/list")
	@ControllerInfo(value = "保存其他项目(获奖情况和兴趣)")
	@ResponseBody
	public String editRewardAndInterest(HealthDto healthDto){
		try {
			String unitId = getLoginInfo().getUnitId();
			Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
			//当前学年 学期
			String acadyear = semesterObj.getAcadyear();
			String semester = semesterObj.getSemester() + "";
			List<StutotalityReward> rewardList = healthDto.getRewardList();
			List<StutotalityItem> interestList = healthDto.getInterestList();
			// 达标科目 开始
            StutotalityItem item = healthDto.getStutotalityItem();
            if(StringUtils.isNotBlank(item.getSubjectId())){
                List<StutotalityItem> item8 = stutotalityItemService.findByUnitIdAndSubjectType(unitId, new String[]{"8"});
                if(CollectionUtils.isEmpty(item8)){
                    String itemId = UuidUtils.generateUuid();
                    item.setId(itemId);
                    item.setUnitId(unitId);
                    item.setGradeId(BaseConstants.ZERO_GUID);
                    item.setSubjectType("8");
                    item.setCreationTime(new Date());
                    item.setModifyTime(new Date());
                    stutotalityItemService.save(item);
                }else {
                    StutotalityItem oldItem = item8.get(0);
                    oldItem.setItemName(item.getItemName());
                    oldItem.setSubjectId(item.getSubjectId());
                    item.setModifyTime(new Date());
                    stutotalityItemService.delete(oldItem.getId());
                    stutotalityItemService.save(oldItem);
                }
            }
            //达标科目结束 全优生占比开始
            List<StutotalityScale> scales = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId, BaseConstants.ZERO_GUID, "4");
            if(CollectionUtils.isEmpty(scales)){
                StutotalityScale s = new StutotalityScale();
                s.setId(UuidUtils.generateUuid());
                s.setUnitId(unitId);
                s.setType("4");
                s.setScale(healthDto.getScale());
                s.setCreationTime(new Date());
                s.setModifyTime(new Date());
                s.setGradeId(BaseConstants.ZERO_GUID);
                s.setStandardScore(healthDto.getScore());
                stutotalityScaleService.save(s);
            }else {
                StutotalityScale sc = scales.get(0);
                sc.setScale(healthDto.getScale());
                sc.setStandardScore(healthDto.getScore());
                stutotalityScaleService.delete(sc.getId());
                stutotalityScaleService.save(sc);
            }
            //全优生占比结束
            stutotalityRewardService.deleteByUnitIdAndAcadyearAndSemester(unitId,acadyear,semester);
			stutotalityItemService.deleteByUnitIdAndSubjectType(unitId,"9");
			ArrayList<StutotalityReward> rewards = new ArrayList<>();
			for (StutotalityReward reward : rewardList) {
				if (StringUtils.isBlank(reward.getRewardName())){
					continue;
				}
				if (StringUtils.isBlank(reward.getId())){
					reward.setId(UuidUtils.generateUuid());
					reward.setUnitId(unitId);
					reward.setAcadyear(acadyear);
					reward.setSemester(semester);
					reward.setGradeId(BaseConstants.ZERO_GUID);
					reward.setCreationTime(new Date());
					reward.setModifyTime(new Date());
					rewards.add(reward);
				}else{
					reward.setUnitId(unitId);
					reward.setAcadyear(acadyear);
					reward.setSemester(semester);
					reward.setGradeId(BaseConstants.ZERO_GUID);
					reward.setModifyTime(new Date());
					rewards.add(reward);
				}
			}
			stutotalityRewardService.saveAll(rewards.toArray(new StutotalityReward[0]));
			List<StutotalityItem> items = new ArrayList<>();
			int i=1;
			for (StutotalityItem interest : interestList) {
				if (StringUtils.isBlank(interest.getItemName())){
					continue;
				}
				interest.setId(UuidUtils.generateUuid());
				interest.setUnitId(unitId);
				interest.setGradeId(BaseConstants.ZERO_GUID);
				interest.setSubjectType("9");
				interest.setCreationTime(new Date());
				interest.setOrderNumber(i);
				items.add(interest);
				i++;
			}
			stutotalityItemService.saveAll(items.toArray(new StutotalityItem[0]));
		}catch (Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

//========================= 开学通知 ===============================

	@RequestMapping("/schoolNotice/index")
	@ControllerInfo(value = "开学通知index")
	public String schoolNotice(ModelMap map){
		String unitId=getLoginInfo().getUnitId();
		Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
		if(semesterObj != null) {
			//当前学年 学期
			String acadyear = semesterObj.getAcadyear();
			String semester = semesterObj.getSemester() + "";
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY}, acadyear), new TR<List<Grade>>() {
			});
			List<StutotalitySchoolNotice> noticeList = stutotalitySchoolNoticeService.findByUnitIdAndAcadyearAndSemester(unitId, acadyear, semester);
			List<StutotalitySchoolNotice> lastList=new ArrayList<>();
			Map<String,StutotalitySchoolNotice> noticeMap=EntityUtils.getMap(noticeList,StutotalitySchoolNotice::getGradeId);//noticeList
			StutotalitySchoolNotice notice=null;
			for(Grade g:gradeList){
				notice=new StutotalitySchoolNotice();
				notice.setGradeName(g.getGradeName());
				notice.setGradeId(g.getId());
				if(noticeMap.containsKey(g.getId())){
					notice.setNotice(noticeMap.get(g.getId()).getNotice());
				}
				lastList.add(notice);
			}
			map.put("lastList", lastList);
		}
		return "/stutotality/health/stutotalitySchoolNotice.ftl";
	}


	@RequestMapping("/schoolNotice/save")
	@ControllerInfo(value = "保存开学通知")
	@ResponseBody
	public String saveSchoolNotice(HealthDto healthDto){
		try {
			String unitId=getLoginInfo().getUnitId();
			Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
			if(semesterObj != null) {
				//当前学年 学期
				String acadyear = semesterObj.getAcadyear();
				String semester = semesterObj.getSemester() + "";
				List<StutotalitySchoolNotice> noticeList = healthDto.getNoticeList();
				stutotalitySchoolNoticeService.deleteByUnitId(unitId);
				for (StutotalitySchoolNotice notice : noticeList) {
					notice.setId(UuidUtils.generateUuid());
					notice.setUnitId(unitId);
					notice.setAcadyear(acadyear);
					notice.setSemester(semester);
				}
				stutotalitySchoolNoticeService.saveAll(noticeList.toArray(new StutotalitySchoolNotice[0]));
			}
		}catch (Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}


}