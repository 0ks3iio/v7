package net.zdsoft.eclasscard.data.action.div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.EccUsedForDto;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Sets;

@Controller
@RequestMapping("/ecalsscard/div/eccinfo")
public class EccInfoDivAction  extends BaseDivAction{
	Logger logger = Logger.getLogger(EccInfoDivAction.class);
	
	private static String business_key = "eccinfo-popup-";
	private static String user_recent_key = "eccinfo-popup-recent-";
	

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;

	
	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData() {
		 String dataJson= RedisUtils.get(business_key
				+ getLoginInfo().getUnitId());
		if (dataJson == null) {
			List<String[]> dataList = new LinkedList<String[]>();
			Set<String> notContains = Sets.newHashSet();
	    	List<EccUsedForDto> usedForDtos = EccNeedServiceUtils.getEccUsedForList(notContains,getLoginInfo().getUnitId());
//			List<McodeDetail> eccInfoTypes = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-BPYT"),new TR<List<McodeDetail>>() {});
			Map<String, String> eccInfoTypeMap = new HashMap<String, String>();
			int startCode = 10000;
			for (EccUsedForDto usedForDto : usedForDtos) {
				startCode++;
				String[] data = new String[9];
				data[0] = usedForDto.getThisId();
				data[1] = usedForDto.getContent();
				data[2] = usedForDto.getContent();
				data[3] = PinyinUtils.toHanyuPinyin(usedForDto.getContent(), false);
				data[4] = PinyinUtils.toHanyuPinyin(usedForDto.getContent(), true);
				data[5] = TYPE_TOP;
				data[6] = Constant.GUID_ZERO;
				data[7] = "1";
				data[8] = String.valueOf(startCode);
				codeMap.put(business_key + usedForDto.getThisId(),
						String.valueOf(startCode));
				eccInfoTypeMap.put(usedForDto.getThisId(), usedForDto.getContent());
				dataList.add(data);
			}
			List<EccInfo> eccInfos = eccInfoService.findListBy("unitId", getLoginInfo().getUnitId());
			fillClsAndPlaceName(eccInfos);
			startCode = 10000;
			for (EccInfo eccInfo : eccInfos) {
				startCode++;
				String[] data = new String[9];
				data[0] = eccInfo.getId();
				data[1] = eccInfo.getPlaceName()+"("+eccInfo.getName()+")";
				data[2] = eccInfoTypeMap.get(eccInfo.getType()) == null ? ""
						: eccInfoTypeMap.get(eccInfo.getType());
				data[3] = PinyinUtils.toHanyuPinyin(eccInfo.getPlaceName(),
						false);
				data[4] = PinyinUtils.toHanyuPinyin(eccInfo.getPlaceName(),
						true);
				data[5] = TYPE_DATA;
				data[6] = eccInfo.getType();
				data[7] = "2";
				String businessCode = codeMap.get(business_key
						+ eccInfo.getType())
						+ String.valueOf(startCode);
				codeMap.put(business_key + eccInfo.getId(), businessCode);
				data[8] = businessCode;
				dataList.add(data);
			}
			RedisUtils.set(business_key + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), 1 * 60);
			dataJson = JsonArray.toJSON(dataList).toString();
		}
		List<String> recentDataList = RedisUtils
				.queryDataFromList(user_recent_key
						+ getLoginInfo().getUserId(),true);
		if (CollectionUtils.isEmpty(recentDataList))
			recentDataList = new ArrayList<String>();
		String recentDataJson = JsonArray.toJSON(recentDataList).toString();
		List<String> resultList = new LinkedList<String>();
		resultList.add(dataJson);
		resultList.add(recentDataJson);
		return JsonArray.toJSON(resultList).toString();
	}

	@RequestMapping("/recentData")
	@ResponseBody
	@ControllerInfo(value = "获取最近的数据", parameter = "{ids}")
	public String putRecentPopUpData(String ids) {
		String[] eccInfoIds = ids.split(",");
		for (int i = 0; i < eccInfoIds.length; i++) {
			RedisUtils.addDataToList(user_recent_key
					+ getLoginInfo().getUserId(), eccInfoIds[i], MAX_COUNT);
		}
		return "";
	}

	private void fillClsAndPlaceName(List<EccInfo> eccInfos){
		Set<String> classIdInfoSet = Sets.newHashSet();
        Set<String> palceIdInfoSet = Sets.newHashSet();
        for(EccInfo info:eccInfos){
        	if(StringUtils.isNotBlank(info.getClassId()))
        		classIdInfoSet.add(info.getClassId());
        	if(StringUtils.isNotBlank(info.getPlaceId()))
        		palceIdInfoSet.add(info.getPlaceId());
        }
        if(classIdInfoSet.size()>0){
        	List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classIdInfoSet.toArray(new String[classIdInfoSet.size()])),new TR<List<Clazz>>() {});
        	Set<String> teachPlaceIds = (EntityUtils.getSet(clazzs, "teachPlaceId"));
        	palceIdInfoSet.addAll(teachPlaceIds);
        	Map<String,Clazz> classNameMap = EntityUtils.getMap(clazzs, "id");
        	for(EccInfo info:eccInfos){
        		if(classNameMap.containsKey(info.getClassId())){
        			Clazz clazz = classNameMap.get(info.getClassId());
        			if(clazz!=null){
        				info.setClassName(clazz.getClassNameDynamic());
        				info.setPlaceId(clazz.getTeachPlaceId());
        			}
        		}
        	}
        }
        if(palceIdInfoSet.size()>0){
        	Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(palceIdInfoSet.toArray(new String[0])),new TR<Map<String, String>>() {});
        	String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(getLoginInfo().getUnitId());
        	List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
    		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, "buildingId","buildingName");
        	for(EccInfo info:eccInfos){
        		if(EccConstants.ECC_MCODE_BPYT_3.equals(info.getType())){
        			if(dormBuildMap.containsKey(info.getPlaceId())){
        				info.setPlaceName(dormBuildMap.get(info.getPlaceId()));
        			}else if(StringUtils.isNotBlank(info.getPlaceId())){
        				info.setPlaceName("（已删除）");
        			}
        		}else{
        			if(teachPlaceMap.containsKey(info.getPlaceId())){
        				info.setPlaceName(teachPlaceMap.get(info.getPlaceId()));
        			}else if(StringUtils.isNotBlank(info.getClassId())){
        				info.setPlaceName("已删除");
        			}else if(StringUtils.isNotBlank(info.getPlaceId())){
        				info.setPlaceName("（已删除）");
        			}
        			if(StringUtils.isNotBlank(info.getClassName()) && StringUtils.isNotBlank(info.getPlaceName())){
        				info.setPlaceName(info.getClassName()+"（"+info.getPlaceName()+"）");
        			}
        		}
        	}
        }
	}
}
