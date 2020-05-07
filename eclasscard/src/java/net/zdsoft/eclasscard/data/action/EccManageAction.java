package net.zdsoft.eclasscard.data.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.constant.EccUsedFor;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.EccUsedForDto;
import net.zdsoft.eclasscard.data.entity.EccClientLog;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccClientLogService;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccOtherSetService;
import net.zdsoft.eclasscard.data.service.EccPermissionService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/eclasscard")
public class EccManageAction extends BaseAction{
	
	@Autowired
    private McodeRemoteService mcodeRemoteService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private EccPermissionService eccPermissionService;
	@Autowired
	private EccClientLogService eccClientLogService;
	
	@Autowired
	private EccOtherSetService eccOtherSetService;
	@Autowired
	private UnitRemoteService unitRemoteService;

	@RequestMapping("/manage/page")
	public String managePage(String name,String ip,String type,ModelMap map){
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Grade>>() {});
		List<EccUsedForDto> usedForDtos = EccNeedServiceUtils.getEccUsedForList(Sets.newHashSet(),getLoginInfo().getUnitId());
		map.put("usedForDtos", usedForDtos);
		map.put("grades", grades);
		//获取当前单位的学校标识
		Unit school = SUtils.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()),Unit.class);
		map.put("unitCode", school.getUnionCode());
		return "/eclasscard/manage/manageIndex.ftl";
	}
	
	@RequestMapping("/manage/list")
    public String manageList(HttpServletRequest request,String name,String gradeId,String type,ModelMap map){
		String  unitId = getLoginInfo().getUnitId();
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<EccInfo> eccInfos = eccInfoService.findByNameAndType(name, type, gradeId,0,unitId, page);
		String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(getLoginInfo().getUnitId());
		List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, DormBuildingDto::getBuildingId,DormBuildingDto::getBuildingName);
		for(EccInfo eccInfo:eccInfos){
			if(EccConstants.ECC_MCODE_BPYT_3.equals(eccInfo.getType())){
				if(dormBuildMap.containsKey(eccInfo.getPlaceId())){
					eccInfo.setPlaceName(dormBuildMap.get(eccInfo.getPlaceId()));
				}else if(StringUtils.isNotBlank(eccInfo.getPlaceId())){
					eccInfo.setPlaceName("（已删除）");
				}
			}
		}
		map.put("usedForMap", EccUsedFor.getEccUsedForMap());
		map.put("eccInfos", eccInfos);
		boolean isStandard = false;
		boolean isUseFace = false;
		if (UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(unitId))) {
			isStandard = true;
		}
		EccOtherSet faceSet = eccOtherSetService.findByUnitIdAndType(unitId, EccConstants.ECC_OTHER_SET_6);
		if (faceSet == null || Objects.equals(1,faceSet.getNowvalue())) {
			isUseFace = true;
		}
		map.put("isStandard", isStandard);
		map.put("isUseFace", isUseFace);
		sendPagination(request, map, page);
		return "/eclasscard/manage/manageList.ftl";
	}
	
	@RequestMapping("/manage/edit")
	public String manageEdit(String id,ModelMap map){
		if(StringUtils.isNotBlank(id)){
			EccInfo eccInfo = eccInfoService.findOne(id);
			if(eccInfo==null){
				eccInfo = new EccInfo();
			}
			if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(new String[]{eccInfo.getClassId()}),new TR<List<Clazz>>() {});
				if(CollectionUtils.isNotEmpty(clazzs)){
					Clazz clazz = clazzs.get(0);
					if(clazz!=null){
						eccInfo.setClassName(clazz.getClassNameDynamic());
						eccInfo.setPlaceId(clazz.getTeachPlaceId());
						eccInfo.setPlaceName(clazz.getTeachPlaceName());
					}
				}
			}
			if(StringUtils.isNotBlank(eccInfo.getPlaceId())){
				Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(new String[]{eccInfo.getPlaceId()}),new TR<Map<String, String>>() {});
				if(teachPlaceMap.containsKey(eccInfo.getPlaceId())){
					eccInfo.setPlaceName(teachPlaceMap.get(eccInfo.getPlaceId()));
				}
			}
			String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(getLoginInfo().getUnitId());
			List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
			List<EccUsedForDto> usedForDtos = EccNeedServiceUtils.getEccUsedForList(Sets.newHashSet(),getLoginInfo().getUnitId());
			map.put("usedForDtos", usedForDtos);
			map.put("dbArray", buildingDtos);
			map.put("eccInfo", eccInfo);
		}
		return "/eclasscard/manage/manageEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/manage/delete")
	public String manageDelete(String id,ModelMap map){
		try {
			eccInfoService.deleteAllById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功"); 
	}
	
	@ResponseBody
	@RequestMapping("/manage/save")
	public String manageSave(EccInfo eccInfo){
		try{
			if(StringUtils.isNotBlank(eccInfo.getClassId())){
				Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
				if(clazz!=null&&StringUtils.isNotBlank(eccInfo.getPlaceId())){
					if(StringUtils.isBlank(clazz.getTeachPlaceId())){
						clazz.setTeachPlaceId(eccInfo.getPlaceId());
						classRemoteService.saveAllEntitys(SUtils.s(new Clazz[]{clazz}));
					}
				}
			}
			if("10".equals(eccInfo.getType())){
				eccInfo.setPlaceId("");
			}
			if(StringUtils.isNotBlank(eccInfo.getId())){
				EccInfo oldEccInfo = eccInfoService.findOne(eccInfo.getId());
				if(StringUtils.isNotBlank(eccInfo.getType())&&!eccInfo.getType().equals(oldEccInfo.getType())){
					eccPermissionService.deleteByEccName(eccInfo.getName(),eccInfo.getUnitId());
				}
				oldEccInfo.setClassId(eccInfo.getClassId());
				oldEccInfo.setPlaceId(eccInfo.getPlaceId());
				oldEccInfo.setType(eccInfo.getType());
				oldEccInfo.setRemark(eccInfo.getRemark());
				eccInfoService.save(oldEccInfo);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/client/log/list")
    public String clientLogList(HttpServletRequest request,String cardId,ModelMap map){
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<EccClientLog> clientLogs = eccClientLogService.findEccClientLogByInfo(cardId, page);
		map.put("clientLogs", clientLogs);
		sendPagination(request, map, page);
		return "/eclasscard/manage/clientLogList.ftl";
	}
	
	
	
	@RequestMapping("/get/classPlace/page")
	public void getClass(String classId,ModelMap map){
		Clazz clazz = SUtils.dt(classRemoteService.findOneById(classId),new TR<Clazz>() {});
		JSONObject jsonObject = new JSONObject();
		if(clazz!=null && StringUtils.isNotBlank(clazz.getTeachPlaceId())){
			Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(new String[]{clazz.getTeachPlaceId()}),new TR<Map<String, String>>() {});
			if(teachPlaceMap.containsKey(clazz.getTeachPlaceId())){
				jsonObject.put("placeId",clazz.getTeachPlaceId());
				jsonObject.put("placeName",teachPlaceMap.get(clazz.getTeachPlaceId()));
			}
		}
		try {
			ServletUtils.print(getResponse(), jsonObject.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping("/manage/saveUrl")
	public String manageDelete(String id, String url, ModelMap map){
		try {
			EccInfo eccInfo = eccInfoService.findOne(id);
			eccInfo.setUrl(url);
			eccInfoService.save(eccInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功"); 
	}
	
}
