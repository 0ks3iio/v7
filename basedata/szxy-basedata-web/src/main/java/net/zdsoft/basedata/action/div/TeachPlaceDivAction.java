package net.zdsoft.basedata.action.div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.TeachBuildingService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/common/div/teachPlace")
public class TeachPlaceDivAction extends BaseDivAction{
	Logger logger = Logger.getLogger(TeachPlaceDivAction.class);
	
	private static String business_key = "teachPlace-popup-";
	private static String user_recent_key = "teachPlace-popup-recent-";
	
	private static String placeByBulid_key = "teachPlaceByBuild-popup-";

	@Autowired
	TeachPlaceService teachPlaceService;
	@Autowired
	McodeRemoteService mcodeRemoteService;

	@Autowired
	DeptService deptService;
	@Autowired
	TeachBuildingService teachBuildingService;
	
	/**
	 * 教学场地选择的结构如下 类型－－>场地 id 
	 * 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索教师的时候　可以看到部门 全拼 模糊搜索用
	 * 首字母拼音　模糊搜索用 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data
	 * 历史搜索data-history code　100001000010000 从上往下有关联关系 level 第几层　第一级为1 第二级为2...
	 * String[]{"id","名称","搜索显示tip","全拼","首字母拼音","类型","上一级id","code","level"}
	 */
	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData() {
		String dataJson = RedisUtils.get(business_key
				+ getLoginInfo().getUnitId());
		if (dataJson == null) {
			
			List<String[]> dataList = new LinkedList<String[]>();
			List<McodeDetail> teachPlaceTypes = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-CDLX"), McodeDetail.class);
			Map<String, String> teachPlaceTypeMap = new HashMap<String, String>();
			int startCode = 10000;
			for (McodeDetail mcodeDetail : teachPlaceTypes) {
				startCode++;
				String[] data = new String[9];
				data[0] = mcodeDetail.getThisId();
				data[1] = mcodeDetail.getMcodeContent();
				data[2] = mcodeDetail.getMcodeContent();
				data[3] = PinyinUtils.toHanyuPinyin(mcodeDetail.getMcodeContent(), false);
				data[4] = PinyinUtils.toHanyuPinyin(mcodeDetail.getMcodeContent(), true);
				data[5] = TYPE_TOP;
				data[6] = Constant.GUID_ZERO;
				data[7] = "1";
				data[8] = String.valueOf(startCode);
				codeMap.put(business_key + mcodeDetail.getThisId(),
						String.valueOf(startCode));
				teachPlaceTypeMap.put(mcodeDetail.getThisId(), mcodeDetail.getMcodeContent());
				dataList.add(data);
			}
			List<TeachPlace> teachPlaceList = teachPlaceService.findTeachPlaceListByType(getLoginInfo()
					.getUnitId(),"");
			List<TeachPlace> teachPlaceListOneType = Lists.newArrayList();
			splitType(teachPlaceList,teachPlaceListOneType);
			startCode = 10000;
			for (TeachPlace place : teachPlaceListOneType) {
				startCode++;
				String[] data = new String[9];
				data[0] = place.getId();
				data[1] = place.getPlaceName();
				data[2] = teachPlaceTypeMap.get(place.getPlaceType()) == null ? ""
						: teachPlaceTypeMap.get(place.getPlaceType());
				data[3] = PinyinUtils.toHanyuPinyin(place.getPlaceName(),
						false);
				data[4] = PinyinUtils.toHanyuPinyin(place.getPlaceName(),
						true);
				data[5] = TYPE_DATA;
				data[6] = place.getPlaceType();
				data[7] = "2";
				String businessCode = codeMap.get(business_key
						+ place.getPlaceType())
						+ String.valueOf(startCode);
				codeMap.put(business_key + place.getId(), businessCode);
				data[8] = businessCode;
				dataList.add(data);
			}
			RedisUtils.set(business_key + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), 5 * 60);
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

	private void splitType(List<TeachPlace> teachPlaceList,
			List<TeachPlace> teachPlaceListOneType) {
		for(TeachPlace place:teachPlaceList){
			String palceType = place.getPlaceType();
			if(StringUtils.isNotBlank(palceType)){
				String[] types =palceType.split(",");
				for(String type:types){
					TeachPlace teachPlace = new TeachPlace();
					teachPlace.setId(place.getId());
					teachPlace.setPlaceName(place.getPlaceName());
					teachPlace.setPlaceType(type);
					teachPlaceListOneType.add(teachPlace);
				}
			}
		}
	}

	@RequestMapping("/recentData")
	@ResponseBody
	@ControllerInfo(value = "获取最近的数据", parameter = "{ids}")
	public String putRecentPopUpData(String ids) {
		String[] placeIds = ids.split(",");
		for (int i = 0; i < placeIds.length; i++) {
			RedisUtils.addDataToList(user_recent_key
					+ getLoginInfo().getUserId(), placeIds[i], MAX_COUNT);
		}
		return "";
	}
	
	
	/**
	 * 教学场地选择的结构如下 楼层－－>场地 id 
	 * 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索教师的时候　可以看到部门 全拼 模糊搜索用
	 * 首字母拼音　模糊搜索用 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data
	 * 历史搜索data-history code　100001000010000 从上往下有关联关系 level 第几层　第一级为1 第二级为2...
	 * String[]{"id","名称","搜索显示tip","全拼","首字母拼音","类型","上一级id","code","level"}
	 */
	@RequestMapping("/popupData/build")
	@ResponseBody
	public String showByBuildPopUpData() {
		String unitId = getLoginInfo().getUnitId();
		String dataJson = RedisUtils.get(placeByBulid_key
				+ getLoginInfo().getUnitId());
		dataJson=null;
		if (dataJson == null) {
			
			List<String[]> dataList = new LinkedList<String[]>();
			List<TeachBuilding> buildList = teachBuildingService.findByUnitId(unitId);
			Set<String> buildIds=new HashSet<String>();
			if(CollectionUtils.isNotEmpty(buildList)){
				buildIds=EntityUtils.getSet(buildList, "id");
			}
			if(buildList==null){
				buildList=new ArrayList<TeachBuilding>();
			}
			//默认添加无楼层的场地
			TeachBuilding b=new TeachBuilding();
			b.setId(Constant.GUID_ONE);
			b.setBuildingName("无楼层");
			buildList.add(b);
			
			List<TeachPlace> teachPlaceList = teachPlaceService.findTeachPlaceListByType(unitId,"");
			
			String key=null;
			
			int startCode = 10000;
			int level=1;
			for(TeachBuilding build:buildList){
				startCode++;
				String[] data = new String[9];
				data[0] = build.getId();
				data[1] = build.getBuildingName();
				data[2] = "";
				data[3] = PinyinUtils.toHanyuPinyin(data[1], false);
				data[4] = PinyinUtils.toHanyuPinyin(data[1], true);
				data[5] = TYPE_TOP;
				data[6] = Constant.GUID_ZERO;
				data[7] = String.valueOf(level);
				data[8] = String.valueOf(startCode);
				codeMap.put(placeByBulid_key +build.getId() ,
						String.valueOf(startCode));
				dataList.add(data);
			}
			level++;
			startCode = 10000;
			if(CollectionUtils.isNotEmpty(teachPlaceList)){
				for(TeachPlace p:teachPlaceList){
					if(StringUtils.isNotEmpty(p.getTeachBuildingId()) && buildIds.contains(p.getTeachBuildingId())){
						key=p.getTeachBuildingId();
					}else{
						key=Constant.GUID_ONE;//无楼层 默认32个1
					}
					
					startCode++;
					String[] data = new String[9];
					data[0] = p.getId();
					data[1] = p.getPlaceName();
					data[2] = "";
					data[3] = PinyinUtils.toHanyuPinyin(data[1], false);
					data[4] = PinyinUtils.toHanyuPinyin(data[1], true);
					data[5] = TYPE_DATA;
					data[6] = key;
					data[7] = String.valueOf(level);
					
					String businessCode = codeMap.get(placeByBulid_key
							+ key)
							+ String.valueOf(startCode);
					codeMap.put(placeByBulid_key + p.getId(), businessCode);
					data[8] = String.valueOf(businessCode);
					
					dataList.add(data);
				}
			}
			
			RedisUtils.set(placeByBulid_key + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), 5 * 60);
			dataJson = JsonArray.toJSON(dataList).toString();

		}
		List<String> resultList = new LinkedList<String>();
		List<String> recentDataList = new ArrayList<String>();
		String recentDataJson = JsonArray.toJSON(recentDataList).toString();
		resultList.add(dataJson);
		resultList.add(recentDataJson);
		return JsonArray.toJSON(resultList).toString();
	}

}
