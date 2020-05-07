package net.zdsoft.eclasscard.data.action.div;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.EccUsedForDto;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Controller
@RequestMapping("/eclasscard/group/eccinfo")
public class EccInfoGroupSelectAction  extends BaseAction{
	Logger logger = Logger.getLogger(EccInfoGroupSelectAction.class);
	
	public static final String TYPE_HEAD = "head";
	public static final String TYPE_DATA = "data";
	public static final String TYPE_GROUP = "group_data";
	public static final String NEED_GROUP = "group";
	

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;

	
	@RequestMapping("/groupData")
	@ResponseBody
	public String showPopUpData(String[] dataIds) {
	 	String headJson= null;
	 	String dataJson= null;
	 	String xzJson= null;
	 	String fxzJson= null;
		List<String[]> headList = new LinkedList<String[]>();
		List<String[]> dataList = new LinkedList<String[]>();
		List<String[]> xzList = new LinkedList<String[]>();
		List<String[]> fxzList = new LinkedList<String[]>();
		Set<String> notContains = Sets.newHashSet();
		Set<String> dataIdsSet = Sets.newHashSet();
		if(dataIds!=null){
			dataIdsSet.addAll(Arrays.asList(dataIds));//已选中的id
		}
    	List<EccUsedForDto> usedForDtos = EccNeedServiceUtils.getEccUsedForList(notContains,getLoginInfo().getUnitId());
		Map<String, String> eccInfoTypeMap = new HashMap<String, String>();
		Map<String, Integer> choiseMap = new HashMap<String, Integer>();
		Map<String, Integer> typeAllMap = new HashMap<String, Integer>();
		List<EccInfo> eccInfos = eccInfoService.findByUnitId(getLoginInfo().getUnitId());
		fillData(eccInfos,dataList,xzList,fxzList,dataIdsSet);//封装参数，过滤无效数据等
		for (EccInfo eccInfo : eccInfos) {
			if(typeAllMap.containsKey(eccInfo.getType())){
				int num = typeAllMap.get(eccInfo.getType());
				typeAllMap.put(eccInfo.getType(), num+1);
			}else{
				typeAllMap.put(eccInfo.getType(), 1);
			}
			if(!dataIdsSet.contains(eccInfo.getId())){
				continue;
			}
			if(choiseMap.containsKey(eccInfo.getType())){
				int num = choiseMap.get(eccInfo.getType());
				choiseMap.put(eccInfo.getType(), num+1);
			}else{
				choiseMap.put(eccInfo.getType(), 1);
			}
		}
		for (EccUsedForDto usedForDto : usedForDtos) {
			String[] head = new String[8];
			head[0] = NEED_GROUP+"_"+TYPE_HEAD+"_"+usedForDto.getThisId();//类型标识
			head[1] = usedForDto.getContent();//类型名称
			head[2] = choiseMap.get(usedForDto.getThisId())==null?"0":choiseMap.get(usedForDto.getThisId())+"";//每种类型已选中数
			head[3] = TYPE_HEAD;//表示这是类型
			head[4] = "0";
			if(usedForDto.getThisId()=="10"){
				head[5] = "1";
			}else if(usedForDto.getThisId()=="20"){
				head[5] = "2";
			}else{
				head[5] = "0";
			}
			head[6] = typeAllMap.get(usedForDto.getThisId())==null?"0":typeAllMap.get(usedForDto.getThisId())+"";;//每个类型总数
			if(choiseMap.get(usedForDto.getThisId())==typeAllMap.get(usedForDto.getThisId())){
				head[7] = "checked";//全选
			}else{
				head[7] = "";//不全选
			}
			eccInfoTypeMap.put(usedForDto.getThisId(), usedForDto.getContent());
			headList.add(head);
		}
		headJson = JsonArray.toJSON(headList).toString();
		dataJson = JsonArray.toJSON(dataList).toString();
		xzJson = JsonArray.toJSON(xzList).toString();
		fxzJson = JsonArray.toJSON(fxzList).toString();
		List<String> resultList = new LinkedList<String>();
		resultList.add(headJson);
		resultList.add(dataJson);
		resultList.add(xzJson);
		resultList.add(fxzJson);
		return JsonArray.toJSON(resultList).toString();
	}

	/**
	 * 数据组装
	 * @param eccInfos
	 * @param dataList
	 * @param xzList
	 * @param fxzList
	 * @param dataIdsSet
	 */
	private void fillData(List<EccInfo> eccInfos,List<String[]> dataList,List<String[]> xzList,List<String[]> fxzList,Set<String> dataIdsSet){
		Set<String> classIdInfoSet = Sets.newHashSet();
        Set<String> palceIdInfoSet = Sets.newHashSet();
        Set<String> fxzPalceIdSet = Sets.newHashSet();
        Map<String,String> clzGradeMap = Maps.newHashMap();
        Map<String,String> placeBuildMap = Maps.newHashMap();
        Map<String,Boolean> isCheckAllMap = Maps.newHashMap(); 
        Map<String,Integer> classOrderMap = Maps.newHashMap(); 
        for(EccInfo info:eccInfos){
        	if(StringUtils.isNotBlank(info.getClassId()))
        		classIdInfoSet.add(info.getClassId());
        	if(StringUtils.isNotBlank(info.getPlaceId())){
        		if(EccConstants.ECC_MCODE_BPYT_2.equals(info.getType())){
        			fxzPalceIdSet.add(info.getPlaceId());
        		}
        		palceIdInfoSet.add(info.getPlaceId());
        	}
        }
        if(fxzPalceIdSet.size()>0){
        	fillFxzData(fxzPalceIdSet, placeBuildMap, isCheckAllMap, fxzList);
        }
        if(classIdInfoSet.size()>0){
        	fillClzData(eccInfos, classIdInfoSet, palceIdInfoSet, classOrderMap, clzGradeMap, isCheckAllMap, xzList);
        }
        if(palceIdInfoSet.size()>0){
        	fillName(eccInfos, palceIdInfoSet);
        	for (EccInfo eccInfo : eccInfos) {
        		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
        			String[] data = new String[8];
        			data[0] = eccInfo.getId();//数据id
        			data[1] = eccInfo.getPlaceName();//显示名称1
        			data[2] = eccInfo.getName();//显示名称2
        			data[3] = TYPE_DATA;//data还是head
        			data[4] = dataIdsSet.contains(eccInfo.getId())?"1":"0";//1.已选择，0未选择
        			data[5] = NEED_GROUP+"_"+TYPE_HEAD+"_"+eccInfo.getType();//头部类型
        			data[6] = clzGradeMap.get(eccInfo.getClassId());//分组id
        			data[7] = classOrderMap.get(eccInfo.getClassId())+"";//排序号
        			dataList.add(data);
        			if(!dataIdsSet.contains(eccInfo.getId()) && StringUtils.isNotBlank(clzGradeMap.get(eccInfo.getClassId()))){
        				isCheckAllMap.put(clzGradeMap.get(eccInfo.getClassId()),false);
        			}
        		}else if(EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
        			String[] data = new String[8];
        			data[0] = eccInfo.getId();
        			data[1] = eccInfo.getPlaceName();
        			data[2] = eccInfo.getName();
        			data[3] = TYPE_DATA;
        			data[4] = dataIdsSet.contains(eccInfo.getId())?"1":"0";
        			data[5] = NEED_GROUP+"_"+TYPE_HEAD+"_"+eccInfo.getType();
        			data[6] = placeBuildMap.get(eccInfo.getPlaceId());
        			data[7] = "0";
        			dataList.add(data);
        			if(!dataIdsSet.contains(eccInfo.getId()) && StringUtils.isNotBlank(placeBuildMap.get(eccInfo.getPlaceId()))){
        				isCheckAllMap.put(placeBuildMap.get(eccInfo.getPlaceId()),false);
        			}
        		}else{
        			String[] data = new String[8];
        			data[0] = eccInfo.getId();
        			data[1] = eccInfo.getPlaceName();
        			data[2] = eccInfo.getName();
        			data[3] = TYPE_DATA;
        			data[4] = dataIdsSet.contains(eccInfo.getId())?"1":"0";
        			data[5] = NEED_GROUP+"_"+TYPE_HEAD+"_"+eccInfo.getType();
        			data[6] = "";
        			data[7] = "0";
        			dataList.add(data);
        		}
    		}
        }
        compare(dataList);//数据层排序
        //设置分组选框
        for(String[] list:xzList){
        	if(isCheckAllMap.containsKey(list[0])&&isCheckAllMap.get(list[0])){
        		list[2] = "checked";
        	}
        }
        for(String[] list:fxzList){
        	if(isCheckAllMap.containsKey(list[0])&&isCheckAllMap.get(list[0])){
        		list[2] = "checked";
        	}
        }
        
	}
	
	/**
	 * 行政班数据
	 * @param eccInfos
	 * @param classIdInfoSet
	 * @param palceIdInfoSet
	 * @param classOrderMap
	 * @param clzGradeMap
	 * @param isCheckAllMap
	 * @param xzList
	 */
	private void fillClzData(List<EccInfo> eccInfos,Set<String> classIdInfoSet,Set<String> palceIdInfoSet,Map<String,Integer> classOrderMap,Map<String,String> clzGradeMap,Map<String,Boolean> isCheckAllMap,List<String[]> xzList){
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classIdInfoSet.toArray(new String[classIdInfoSet.size()])),new TR<List<Clazz>>() {});
    	Set<String> teachPlaceIds = (EntityUtils.getSet(clazzs, "teachPlaceId"));
    	Set<String> gradeIds = (EntityUtils.getSet(clazzs, "gradeId"));
    	for(int i=0;i<clazzs.size();i++){
    		classOrderMap.put(clazzs.get(i).getId(), i+1);
    		clzGradeMap.put(clazzs.get(i).getId(), clazzs.get(i).getGradeId());
    	}
    	if(gradeIds.size()>0){
    		List<Grade> grades = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()])),new TR<List<Grade>>() {});
    		for(Grade grade:grades){
    			String[] data = new String[4];
    			data[0] = grade.getId();
    			data[1] = grade.getGradeName();
    			data[2] = "";
    			isCheckAllMap.put(grade.getId(), true);
    			xzList.add(data);
    		}
    	}
    	palceIdInfoSet.addAll(teachPlaceIds);
    	Map<String,Clazz> classNameMap = EntityUtils.getMap(clazzs, "id");
    	List<EccInfo> removeInfos = Lists.newArrayList();
    	for(EccInfo info:eccInfos){
    		if(classNameMap.containsKey(info.getClassId())){
    			Clazz clazz = classNameMap.get(info.getClassId());
    			if(clazz!=null){
    				info.setClassName(clazz.getClassNameDynamic());
    				info.setPlaceId(clazz.getTeachPlaceId());
    			}
    		}else if(EccConstants.ECC_MCODE_BPYT_1.equals(info.getType())){
    			removeInfos.add(info);
    		}
    	}
    	eccInfos.removeAll(removeInfos);
	}
	/**
	 * 非行政班数据
	 * @param fxzPalceIdSet
	 * @param placeBuildMap
	 * @param isCheckAllMap
	 * @param fxzList
	 */
	private void fillFxzData(Set<String> fxzPalceIdSet,Map<String,String> placeBuildMap,Map<String,Boolean> isCheckAllMap,List<String[]> fxzList){
		List<TeachPlace> teachPlaces = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(fxzPalceIdSet.toArray(new String[0])),new TR<List<TeachPlace>>() {});
    	boolean isNoBuild = false;
    	for(TeachPlace place:teachPlaces){
    		if(StringUtils.isBlank(place.getTeachBuildingId())){
    			placeBuildMap.put(place.getId(), "wu-shu-lou-id");
    			isNoBuild = true;
    		}else{
    			placeBuildMap.put(place.getId(), place.getTeachBuildingId());
    		}
    	}
    	Set<String> teachBuildingIds = (EntityUtils.getSet(teachPlaces, "teachBuildingId"));
    	if(teachBuildingIds.size()>0){
    		Map<String, String> teachPlaceMap = SUtils.dt(teachBuildingRemoteService.findTeachBuildMap(teachBuildingIds.toArray(new String[0])),new TR<Map<String, String>>() {});
    		for(String buildId:teachPlaceMap.keySet()){
    			String[] data = new String[3];
    			data[0] = buildId;
    			data[1] = teachPlaceMap.get(buildId);
    			data[2] = "";
    			isCheckAllMap.put(buildId, true);
    			fxzList.add(data);
    		}
    	}
    	if(isNoBuild){
    		String[] data = new String[3];
			data[0] = "wu-shu-lou-id";
			data[1] = "其他";
			data[2] = "";
			isCheckAllMap.put("wu-shu-lou-id", true);
			fxzList.add(data);
    	}
	}
	
	/**
	 * data排序
	 * @param dataList
	 */
	private void compare(List<String[]> dataList){
		Collections.sort(dataList, new Comparator<String[]>() {
			@Override
			public int compare(String[] o1,
					String[] o2) {
				if(Integer.parseInt(o1[7]) == Integer.parseInt(o2[7])) {
					return 0;
				}else if(Integer.parseInt(o1[7]) < Integer.parseInt(o2[7])) {
					return -1;
				}else{
					return 1;
				}
			}
		});
	}
	
	/**
	 * 填充名称
	 * @param eccInfos
	 * @param palceIdInfoSet
	 */
	private void fillName(List<EccInfo> eccInfos,Set<String> palceIdInfoSet){
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
    				info.setPlaceName(info.getClassName());
    			}
    		}
    	}
	}
}
