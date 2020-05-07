package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormCheckDiscipline;
import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;
import net.zdsoft.stuwork.data.entity.DyDormCheckRole;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormCheckDisciplineService;
import net.zdsoft.stuwork.data.service.DyDormCheckRemindService;
import net.zdsoft.stuwork.data.service.DyDormCheckResultService;
import net.zdsoft.stuwork.data.service.DyDormCheckRoleService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;
import net.zdsoft.stuwork.data.service.DyPermissionService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/stuwork")
public class DyDormCheckAction extends BaseAction{
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private DyDormCheckRoleService dyDormCheckRoleService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private DyDormCheckResultService dyDormCheckResultService;
	@Autowired
	private DyDormCheckRemindService dyDormCheckRemindService;
	@Autowired
	private DyDormCheckDisciplineService dyDormCheckDisciplineService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyPermissionService dyPermissionService;
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	@RequestMapping("/dorm/check/index/page")
	@ControllerInfo(value = "index")
	public String showIndex(ModelMap map, HttpSession httpSession){
		return "/stuwork/dorm/check/dyDormCheckIndex.ftl";
	}
	@RequestMapping("/dorm/checkDis/index/page")
	@ControllerInfo(value = "汇总页面index")
	public String getDisPoolIndex(ModelMap map){
		return "/stuwork/dorm/checkDis/dyDormDisIndex.ftl";
	}
	@RequestMapping("/dorm/checkDis/list")
	@ControllerInfo(value = "汇总页面list")
	public String getDisPoolList(ModelMap map,DormSearchDto dormDto){
		String unitId=getLoginInfo().getUnitId();
		if(StringUtils.isBlank(dormDto.getAcadyear())){
			Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,unitId),new TypeReference<Semester>(){});//TODO
			if(semester==null){
				return errorFtl(map,"当前时间不在学年学期范围内");
			}
			dormDto.setAcadyear(semester.getAcadyear());
			dormDto.setSemesterStr(semester.getSemester()+"");
		}
		String acadyear=dormDto.getAcadyear();
		//String semesterStr=dormDto.getSemesterStr();
		
		School school=SUtils.dt(schoolRemoteService.findOneById(unitId),new TypeReference<School>(){});
		if(school==null){
			return errorFtl(map, "找不到学校");
		}
		String[] sections=school.getSections().split(",");
		List<Integer> sectionList = new ArrayList<Integer>();
		for(String section:sections){
			sectionList.add(Integer.parseInt(section.trim()));
		}
		//判断 0时  是初次进入 还是选择了幼儿园
		int section=dormDto.getSection();
		if(section==0){
			boolean flag=false;
			for(String sectionStr:sections){
				if(sectionStr.equals("0")){
					flag=true;
					break;
				}
			}
			if(!flag){
				section=Integer.parseInt(sections[0].trim());
				dormDto.setSection(section);
			}
		}
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
		
		List<Clazz> cList=SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId,acadyear),new TR<List<Clazz>>(){});
		List<Clazz> clazzList=new ArrayList<Clazz>();
		for(Clazz clazz:cList){
			if(clazz.getSection()==dormDto.getSection()){
				clazzList.add(clazz);
			}
		}
		
		//权限班级
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
		Iterator<Clazz> it = clazzList.iterator();
		while(it.hasNext()) {
			Clazz clazz = it.next();
			if (!classPermission.contains(clazz.getId())) {
				it.remove();
			}
		}
		
		Set<String> classIds=new HashSet<String>();
		if(StringUtils.isNotBlank(dormDto.getClassId())){
			classIds.add(dormDto.getClassId());
		}else{
			for(Clazz clazz:clazzList){
				classIds.add(clazz.getId());
			}
		}
		List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
		
		List<DyDormCheckDiscipline> checkDisList=dyDormCheckDisciplineService.getPoolByCon(unitId,classIds.toArray(new String[0]),dormDto);
		
		
		map.put("checkDisList",checkDisList);
		map.put("acadyearList",acadyearList);
		map.put("sectionList",sectionList);
		map.put("buildingList",buildingList);
		map.put("clazzList",clazzList);
		map.put("studentList",studentList);
		map.put("dormDto", dormDto);
		return "/stuwork/dorm/checkDis/dyDormDisList.ftl";
	}
	@RequestMapping("/dorm/checkDis/detail")
	@ControllerInfo(value = "汇总页面detail")
	public String getDisDetailList(ModelMap map,DormSearchDto dormDto){
		List<DyDormCheckDiscipline> checkDisList=dyDormCheckDisciplineService.getDetailByCon(getLoginInfo().getUnitId(), dormDto);
		if(CollectionUtils.isNotEmpty(checkDisList)){
			DyDormCheckDiscipline dis=checkDisList.get(0);
			DyDormRoom room=dyDormRoomService.findOne(dis.getRoomId());
			Student stu=SUtils.dc(studentRemoteService.findOneById(dis.getStudentId()),Student.class);
			map.put("roomName", room==null?"":room.getRoomName());
			map.put("studentName",stu==null?"":stu.getStudentName());
		}
		map.put("checkDisList", checkDisList);
		return "/stuwork/dorm/checkDis/dyDormDisDetail.ftl";
	}
	@RequestMapping("/dorm/check/listDis/page")
	@ControllerInfo(value = "checkDislist")
	public String showCheckDisList(ModelMap map,HttpServletRequest request,DormSearchDto dormDto){
		Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),new TypeReference<Semester>(){});//TODO
		if(semester==null){
			return errorFtl(map,"当前时间不在学年学期范围内");
		}
		String schoolId=getLoginInfo().getUnitId();
		String	acadyear=semester.getAcadyear();
		String	semesterStr=String.valueOf(semester.getSemester());
		dormDto.setUnitId(schoolId);
		dormDto.setAcadyear(acadyear);
		dormDto.setSemesterStr(semesterStr);
		Pagination page=createPagination();
		
		//当前学年学期的数据 有权限的寝室楼
		List<DyDormBuilding> buildingList=getBuildingList(getLoginInfo().getUserId(), acadyear, semesterStr);
		if(CollectionUtils.isEmpty(buildingList)){
			return errorFtl(map,"无当前学年学期维护寝室楼权限");
		}
		Set<String> buildingSet=new HashSet<String>();
		String buildingIdStr=null;
		for(DyDormBuilding building:buildingList){
			buildingSet.add(building.getId());
			if(StringUtils.isBlank(buildingIdStr)){
				buildingIdStr=building.getId();
			}else{
				buildingIdStr+=","+building.getId();
			}
		}
		List<DyDormCheckDiscipline> checkDisList=dyDormCheckDisciplineService.getCheckDisList(dormDto,buildingSet.toArray(new String[0]), page);
		
		
		map.put("checkDisList", checkDisList);
		map.put("buildingIds",buildingIdStr);
		map.put("dormDto", dormDto);
		sendPagination(request, map, page);
		return "/stuwork/dorm/check/dyDormCheckDisList.ftl";
	}
	@RequestMapping("/dorm/check/editDis")
	@ControllerInfo(value = "checkDisEdit")
	public String checkDisEdit(ModelMap map,HttpServletRequest request){
		String semesterStr=request.getParameter("semesterStr");
		String acadyear=request.getParameter("acadyear");
		
		String buildingIdsStr=request.getParameter("buildingIds");
		String[] buildingIds=buildingIdsStr.split(",");
		List<DyDormBuilding> buildingList=dyDormBuildingService.findListByIdIn(buildingIds);
		if(CollectionUtils.isNotEmpty(buildingList)){
			String buildingId=buildingList.get(0).getId();
			List<DyDormRoom>  roomList=dyDormRoomService.findByName(getLoginInfo().getUnitId(), null, buildingId,StuworkConstants.STU_TYPE);
			map.put("roomList", roomList);
		}
		map.put("nowDate", new Date());
		map.put("semesterStr", semesterStr);
		map.put("acadyear", acadyear);
		map.put("buildingList", buildingList);
		return "/stuwork/dorm/check/dyDormCheckDisEdit.ftl";
	}
	@ResponseBody
	@RequestMapping("/dorm/check/setRoomList")
	@ControllerInfo(value = "setRoomList")
	public String setRoomList(String buildingId,String roomProperty){
		List<DyDormRoom>  roomList=dyDormRoomService.findByName(getLoginInfo().getUnitId(), null, buildingId,roomProperty);
		JSONArray array=new JSONArray();
		if(CollectionUtils.isNotEmpty(roomList)){
			for(DyDormRoom room:roomList){
				JSONObject json=new JSONObject();
				json.put("roomId", room.getId());
				json.put("roomName", room.getRoomName());
				array.add(json);
			}
		}
		JSONObject json=new JSONObject();
		json.put("roomList", array);
		return json.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/dorm/check/setStudentList")
	@ControllerInfo(value = "setStudentList")
	public String setStudentList(String roomId,String acadyear,String semesterStr){
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByRoomIds(getLoginInfo().getUnitId(), new String[]{roomId},acadyear,semesterStr);
		JSONArray array=new JSONArray();

		Set<String> studentIds=new HashSet<String>(); 
		if(CollectionUtils.isNotEmpty(bedList)){
			for(DyDormBed bed:bedList){
				studentIds.add(bed.getOwnerId());
			}
			List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>(){});
			if(CollectionUtils.isNotEmpty(studentList)){
				for(Student student:studentList){
					JSONObject json=new JSONObject();
					json.put("studentId", student.getId());
					json.put("studentName", student.getStudentName());
					array.add(json);
				}
			}
		}
		JSONObject json=new JSONObject();
		json.put("studentList", array);
		return json.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/dorm/check/saveDis")
	@ControllerInfo(value = "checkDissave")
	public String checkDisSave(ModelMap map,HttpServletRequest request,DyDormCheckDiscipline checkDis){
		try{
			String schoolId=getLoginInfo().getUnitId();
			checkDis.setSchoolId(schoolId);
			DateInfo dateInfo=SUtils.dt(dateInfoRemoteService.findByDate(schoolId, checkDis.getAcadyear(), 
					Integer.parseInt(checkDis.getSemester()), checkDis.getCheckDate()),new TypeReference<DateInfo>() {});
			int	week=2;//TODO
			int	day=4;
			if(dateInfo!=null){
				week=dateInfo.getWeek();
				day=dateInfo.getWeekday();
			}
			checkDis.setWeek(week);
			checkDis.setDay(day);
			checkDis.setOperatorId(getLoginInfo().getUserId());
			
			Student student=SUtils.dt(studentRemoteService.findOneById(checkDis.getStudentId()),new TypeReference<Student>(){});
			checkDis.setClassId(student.getClassId());
			checkDis.setId(UuidUtils.generateUuid());
			dyDormCheckDisciplineService.save(checkDis);
			
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/dorm/check/deleteDis")
	@ControllerInfo(value = "checkDisdelete")
	public String checkDisDelete(ModelMap map,String id){
		try{
			dyDormCheckDisciplineService.delete(id);
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/dorm/check/getRoomListDis")
	@ControllerInfo(value = "getRoomList")
	public String getRoomList(ModelMap map,HttpServletRequest request){
		try{
			String buildingId=request.getParameter("buildingId");
			List<DyDormRoom> roomList=dyDormRoomService.findListBy("buildingId", buildingId);
			map.put("roomList", roomList);
			return Json.toJSONString(roomList);
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
	}
	@RequestMapping("/dorm/check/list/page")
    @ControllerInfo(value = "checklist")
    public String showList(ModelMap map,HttpServletRequest request,DormSearchDto searchDto) {
		String schoolId=getLoginInfo().getUnitId();
		Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),new TypeReference<Semester>(){});
		if(semester==null){
			return errorFtl(map,"当前时间不在学年学期范围内");
		}
		//给学年学期赋值
		String	acadyear=semester.getAcadyear();
		String	semesterStr=String.valueOf(semester.getSemester());
		searchDto.setAcadyear(acadyear);
		searchDto.setSemesterStr(semesterStr);
		
		Pagination page=createPagination();
		
		//当前学年学期的数据 有权限的寝室楼
		List<DyDormBuilding> buildingList=getBuildingList(getLoginInfo().getUserId(), acadyear, semesterStr);
		if(CollectionUtils.isEmpty(buildingList)){
			return errorFtl(map,"无当前学年学期维护寝室楼权限");
		}
		
		String buildingId=searchDto.getSearchBuildId();
		if(StringUtils.isBlank(buildingId)){
			buildingId=buildingList.get(0).getId();
			searchDto.setSearchBuildId(buildingId);
		}
		String buildingName=dyDormBuildingService.findOne(buildingId).getName();
		//得到对应的寝室号
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(schoolId, buildingId, null,StuworkConstants.STU_TYPE, page);

		Date searchDate=searchDto.getSearchDate();
		if(searchDto.getSearchDate()==null){
			searchDate=DateUtils.string2Date(DateUtils.date2String(new Date()),"yyyy-MM-dd");
			searchDto.setSearchDate(searchDate);
		}
		
		//得到星期几 周次 初始化
		DateInfo dateInfo=SUtils.dt(dateInfoRemoteService.findByDate(schoolId, acadyear, Integer.parseInt(semesterStr), searchDate),new TypeReference<DateInfo>() {});
		int	week=0;
		int	day=0;
		if(dateInfo!=null){
			week=dateInfo.getWeek();
			day=dateInfo.getWeekday();
		}else{
			return errorFtl(map, "当前时间不在周次范围内");
		}
		searchDto.setWeek(week);
		searchDto.setDay(day);
		
		List<DyDormRoom> roomResultList=new ArrayList<DyDormRoom>();
		if(StringUtils.isNotBlank(searchDto.getSearchRoomId())){
			roomResultList.add(dyDormRoomService.findOne(searchDto.getSearchRoomId()));
		}else{
			roomResultList.addAll(roomList);
		}
		//得到某天 某寝室楼下的考勤信息  为得到key为roomId的map
		Map<String,DyDormCheckResult> resultMap=dyDormCheckResultService.getResultMap(schoolId, buildingId,DateUtils.date2String(searchDate));
		//得到某天 某寝室楼下的提醒信息  为得到key为roomId的map
		Map<String,DyDormCheckRemind> remindMap=dyDormCheckRemindService.getRemindMap(schoolId, buildingId,DateUtils.date2String(searchDate));
		//对应的考勤信息放入对应的room
		if(CollectionUtils.isNotEmpty(roomResultList)){
			for(DyDormRoom room:roomResultList){
				DyDormCheckResult result=resultMap.get(room.getId());
				DyDormCheckRemind remind=remindMap.get(room.getId());
				if(result==null) result=new DyDormCheckResult();
				if(remind==null) remind=new DyDormCheckRemind();
				
				room.setResult(result);
				room.setRemind(remind);
			}
		}
		
		map.put("roomResultList",roomResultList);
		map.put("roomList",roomList);
		map.put("buildingList",buildingList);
		map.put("buildingName",buildingName);
		map.put("searchDto",searchDto);
		sendPagination(request, map, page);
		return "/stuwork/dorm/check/dyDormCheckEditList.ftl";
	}
	@ResponseBody
	@RequestMapping("/dorm/check/saveResult")
	@ControllerInfo(value = "saveResultAndRemindList")
	public String saveResult(DormSearchDto dormDto,ModelMap map) {
		try{
			dyDormCheckResultService.saveResultAndRemindList(dormDto,getLoginInfo().getUnitId(),getLoginInfo().getUserId());
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	//得到当前登录人的 可维护寝室楼的数据  null或空则表示无权限
	public List<DyDormBuilding> getBuildingList(String userId,String acadyear,String semester){
		List<DyDormCheckRole> checkRoleList=dyDormCheckRoleService.findListBy(new String[]{"userId","acadyear","semester"}
		,new String[]{userId,acadyear,semester});
		Set<String> buildingIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(checkRoleList)){
			for(DyDormCheckRole checkRole:checkRoleList){
				buildingIds.add(checkRole.getBuildingId());
			}
		}
		List<DyDormBuilding> buildingList=dyDormBuildingService.findListByIds(buildingIds.toArray(new String[0]));
		Collections.sort(buildingList,new Comparator<DyDormBuilding>(){
			@Override
			public int compare(DyDormBuilding o1, DyDormBuilding o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		return buildingList;
	}
}
