package net.zdsoft.eclasscard.data.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccSignIn;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccSignInService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Controller
@RequestMapping("/eclasscard")
public class EccSignInAction extends BaseAction{

	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;	
	@Autowired
	private EccSignInService eccSignInService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private static OpenApiOfficeService openApiOfficeService;
    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }
	
	@RequestMapping("/signin/index")
	@ControllerInfo(value = "签到考勤index")
	public String signInIndex(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<Clazz> clazzs =  SUtils.dt(classRemoteService.findBySchoolId(unitId),new TR<List<Clazz>>() {});
		//班级权限
		Set<String> classPermission = stuworkRemoteService.findClassSetByUserId(getLoginInfo().getUserId());
				
		Iterator<Clazz> it = clazzs.iterator();
		while(it.hasNext()) {
			Clazz clazz = it.next();
			if (clazz.getIsGraduate()==1) {
				it.remove();
				continue;
			}
			if (!classPermission.contains(clazz.getId())) {
				it.remove();
			}
		}
		List<EccInfo> eccInfos = eccInfoService.findListByUnitAndType(unitId, EccConstants.ECC_MCODE_BPYT_6);
		Map<String,String> placeNameMap = getplaceName(eccInfos, unitId);
		map.put("startTime", EccUtils.getSundayOfLastWeek());
		map.put("endTime", new Date());
		map.put("clazzList", clazzs);
		map.put("placeNameMap", placeNameMap);
		return "/eclasscard/signIn/signInIndex.ftl";
	}
	
	@RequestMapping("/signin/list")
	@ControllerInfo(value = "签到考勤List")
	public String signInList(String classId, String placeId, String state, String startTime, String endTime, ModelMap map){
		List<EccSignIn> eccSignInsList = eccSignInService.findByIdAndTime(classId,placeId,startTime,endTime);
		Map<String,EccSignIn> eccSignInMap = EntityUtils.getMap(eccSignInsList, "ownerId");
		List<String> eccInfoIdList = EntityUtils.getList(eccSignInsList, "eccInfoId");
		
		String unitId = getLoginInfo().getUnitId();
		List<EccInfo> eccInfos = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(eccInfoIdList)){
			eccInfos =eccInfoService.findListByIdIn(eccInfoIdList.toArray(new String[0]));
		}
		Map<String,String> idAndPlaceMap = EntityUtils.getMap(eccInfos, "id", "placeId");
		Map<String,String> placeNameMap = getplaceName(eccInfos, unitId);
		List<EccSignIn> eccSignInList = Lists.newArrayList();
		String thisPlaceId = "";
		//获取学生请假接口
		Set<String> leaveStuIds = Sets.newHashSet();
		if(getOpenApiOfficeService()!=null&&StringUtils.isNotBlank(classId)){
			try{
				Date startDate = DateUtils.string2Date(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.string2Date(endTime, "yyyy-MM-dd HH:mm");
				leaveStuIds = getOpenApiOfficeService().getStusByClassId(unitId, classId, startDate, endDate);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(classId)){
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
			List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>() {});
			for (Student student : students) {
				EccSignIn eccSignIn = null;
				if (eccSignInMap.containsKey(student.getId())) {
					eccSignIn = eccSignInMap.get(student.getId());
					thisPlaceId = idAndPlaceMap.get(eccSignIn.getEccInfoId());
					if (EccConstants.SWIPE_STATE_2.equals(state)||EccConstants.SWIPE_STATE_3.equals(state)
							||((!EccConstants.ZERO32.equals(placeId)) && (!thisPlaceId.equals(placeId)))) {
						continue;
					}
					eccSignIn.setState(1);
					eccSignIn.setPlaceName(placeNameMap.get(thisPlaceId));
				} else {
					if (EccConstants.SWIPE_STATE_1.equals(state)) {
						continue;	
					}
					eccSignIn = new EccSignIn();
					if(leaveStuIds.contains(student.getId())){
						if (EccConstants.SWIPE_STATE_2.equals(state)){
							continue;	
						}
						eccSignIn.setState(3);
					}else{
						if (EccConstants.SWIPE_STATE_3.equals(state)){
							continue;	
						}
						eccSignIn.setState(0);
					}
				}
				eccSignIn.setStudentName(student.getStudentName());
				eccSignIn.setClassName(clazz.getClassNameDynamic());
				eccSignInList.add(eccSignIn);
			}
		}
		map.put("eccSignInList", eccSignInList);
		return "/eclasscard/signIn/signInList.ftl";
	}
	
	public Map<String,String> getplaceName(List<EccInfo> eccInfos,String unitId){
		Map<String,String> placeNameMap = Maps.newLinkedHashMap();
		Set<String> palceIdInfoSet = Sets.newHashSet();
        for(EccInfo info:eccInfos){
        	if(StringUtils.isNotBlank(info.getPlaceId())){
        		palceIdInfoSet.add(info.getPlaceId());
        	}
        }
		Map<String, String> teachPlaceMap =Maps.newConcurrentMap();
		if(CollectionUtils.isNotEmpty(palceIdInfoSet)){
			teachPlaceMap= SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(palceIdInfoSet.toArray(new String[0])),new TR<Map<String, String>>() {});
		}
    	String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(unitId);
    	List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, "buildingId","buildingName");
    	for(EccInfo info:eccInfos){
    		if(EccConstants.ECC_MCODE_BPYT_3.equals(info.getType())){
    			if(dormBuildMap.containsKey(info.getPlaceId())){
    				placeNameMap.put(info.getPlaceId(), dormBuildMap.get(info.getPlaceId()));
    			}
    		}else{
    			if(teachPlaceMap.containsKey(info.getPlaceId())){
    				placeNameMap.put(info.getPlaceId(), teachPlaceMap.get(info.getPlaceId()));
    			}
    		}
    	}
    	return placeNameMap;
	}
	
}
