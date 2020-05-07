package net.zdsoft.officework.action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dto.TeachPlaceDto;
import net.zdsoft.officework.entity.OfficeClassroomRelevance;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.service.OfficeClassroomRelevanceService;
import net.zdsoft.officework.service.OfficeHealthDeviceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/officework")
public class OfficeAttanceSetAction extends BaseAction{
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private OfficeHealthDeviceService officeHealthDeviceService;
	@Autowired
	private OfficeClassroomRelevanceService officeClassroomRelevanceService;

	@RequestMapping("/calss/attance/index")
	public String classAttIndex(ModelMap map){
		return "/eclasscard/class/classAttSetIndex.ftl";
	}
	
	@RequestMapping("/calss/attance/placeset/list")
	public String classAttPlaceSet(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<OfficeHealthDevice> devices = officeHealthDeviceService.getByUnitAndType(unitId,OfficeConstants.HEALTH_DEVICE_TYPE_04);
		if(CollectionUtils.isEmpty(devices)){
			return "/eclasscard/class/classAttPlaceSetList.ftl";
		}
		
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceListByType(unitId, null),new TR<List<TeachPlace>>(){});
		List<OfficeClassroomRelevance> classroomRelevances = officeClassroomRelevanceService.findByUnitId(unitId);
		Map<String,TeachPlace> placeMap = EntityUtils.getMap(placeList, TeachPlace::getId);
		Map<String,OfficeClassroomRelevance> relevanceMap = EntityUtils.getMap(classroomRelevances, OfficeClassroomRelevance::getPlaceId);
		List<TeachPlaceDto> placeDtos = fillPlaceDto(placeMap,relevanceMap);
		map.put("placeDtos", placeDtos);
		return "/eclasscard/class/classAttPlaceSetList.ftl";
	}
	
	private List<TeachPlaceDto> fillPlaceDto(Map<String, TeachPlace> placeMap,
			Map<String, OfficeClassroomRelevance> relevanceMap) {
		List<TeachPlaceDto> placeDtos = Lists.newArrayList();
		for(String key : placeMap.keySet()){
			TeachPlace place = placeMap.get(key);
			if(place!=null && StringUtils.isNotBlank(place.getPlaceType())){
				String placeType = place.getPlaceType();
				Set<String> placeTypes = new HashSet<>(Arrays.asList(placeType.split(",")));
				if(placeTypes.contains("1") || placeTypes.contains("4") ){//此处取教学和教室类型
					TeachPlaceDto dto = new TeachPlaceDto();
					dto.setId(place.getId());
					dto.setName(place.getPlaceName());
					dto.setPlaceCode(place.getPlaceCode());
					if(relevanceMap.containsKey(place.getId())){
						OfficeClassroomRelevance relevance = relevanceMap.get(place.getId());
						if(relevance !=null){
							dto.setRelatedId(relevance.getClassroomId());
							dto.setRelatedName(relevance.getClassRoomName());
						}
					}
					placeDtos.add(dto);
				}
			}
		}
		return placeDtos;
	}
	
	@ResponseBody
	@RequestMapping("/calss/attance/placeset/save")
	public String classAttPlaceSetSave(String placeId,String relatedId,String relatedName){
		try{
			String unitId = getLoginInfo().getUnitId();
			if(StringUtils.isBlank(placeId)||StringUtils.isBlank(relatedId)||StringUtils.isBlank(relatedName)){
				return error("设置失败！参数错误");
			}
			OfficeClassroomRelevance classroomRelevance = officeClassroomRelevanceService.findByPlaceId(placeId);
			if(classroomRelevance == null){
				classroomRelevance = new OfficeClassroomRelevance();
				classroomRelevance.setId(UuidUtils.generateUuid());
				classroomRelevance.setSchoolId(unitId);
				classroomRelevance.setPlaceId(placeId);
				classroomRelevance.setClassroomId(relatedId);
				classroomRelevance.setClassRoomName(relatedName);
			}else{
				classroomRelevance.setClassroomId(relatedId);
				classroomRelevance.setClassRoomName(relatedName);
			}
			officeClassroomRelevanceService.save(classroomRelevance);
		}catch (Exception e) {
			e.printStackTrace();
			return error("设置失败！"+e.getMessage());
		}
		return success("设置成功");
	}
	
	@ResponseBody
	@RequestMapping("/calss/attance/placeset/delete")
	public String classAttPlaceSetDelete(String placeId){
		try{
			officeClassroomRelevanceService.deleteByPlaceId(placeId);
		}catch (Exception e) {
			e.printStackTrace();
			return error("清除失败！"+e.getMessage());
		}
		return success("清除成功");
	}
}
