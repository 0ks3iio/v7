package net.zdsoft.stuwork.data.action;


import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;


@Controller
@RequestMapping("/stuwork")
public class DyDormManageAction extends BaseAction{
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;

	private int currentPageIndex,currentPageSize;
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
	@RequestMapping("/dorm/index/page")
    @ControllerInfo(value = "index")
    public String showIndex(ModelMap map, HttpSession httpSession) {
		return "/stuwork/dorm/dyDormIndex.ftl";
	}
	
	@RequestMapping("/dorm/bed/index/page")
	@ControllerInfo(value = "床位维护index")
	public String showBedIndex(ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		String acadyearStr=request.getParameter("acadyearStr");
		String semesterStr=request.getParameter("semesterStr");
		String unitId=getLoginInfo().getUnitId();
		Pagination page=createPagination();
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		if(StringUtils.isEmpty(acadyearStr)){
			Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,unitId),new TypeReference<Semester>(){});
			if(semester==null){
				return errorFtl(map, "当前时间不在学年学期范围内");
			}
			acadyearStr=semester.getAcadyear();
			semesterStr=String.valueOf(semester.getSemester());
		}
		
		map.put("buildingList", buildingList);
		map.put("acadyearList", acadyearList);
		map.put("roomProperty", request.getParameter("roomProperty"));
		map.put("acadyearStr",acadyearStr);
		map.put("semesterStr",semesterStr);
		return "/stuwork/dorm/dyDormBedIndex.ftl";
	}
	@RequestMapping("/dorm/bed/list/page")
	@ControllerInfo(value = "床位维护list")
	public String showBedList(ModelMap map,HttpServletRequest request, HttpSession httpSession){
		String buildingId=request.getParameter("buildingId");
		String roomType=request.getParameter("roomType");
		String acadyearStr=request.getParameter("acadyearStr");
		String semesterStr=request.getParameter("semesterStr");
		String floor=request.getParameter("floor");
		String roomName=request.getParameter("roomName");
		String roomProperty=request.getParameter("roomProperty");
		String roomState=request.getParameter("roomState");
		String unitId=getLoginInfo().getUnitId();
		Pagination page=createPagination();


		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		if(StringUtils.isEmpty(acadyearStr)){
			Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester(0,unitId),new TypeReference<Semester>(){});
			if(semester==null){
				return errorFtl(map, "当前时间不在学年学期范围内");
			}
			acadyearStr=semester.getAcadyear();
			semesterStr=String.valueOf(semester.getSemester());
		}
		//导入页返回 不带学期参数  （默认学期1 学期2 都将保存）
		/*if(StringUtils.isEmpty(semesterStr)){
			semesterStr="1";
		}*/
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(unitId,
				buildingId, roomType,acadyearStr,semesterStr,floor,roomName,roomState,roomProperty,page);

		map.put("buildingList", buildingList);
		map.put("roomList", roomList);
		map.put("acadyearList", acadyearList);
		map.put("buildingId",buildingId);
		map.put("roomType",roomType);
		map.put("roomProperty",roomProperty);
		map.put("acadyearStr",acadyearStr);
		map.put("semesterStr",semesterStr);
		sendPagination(request, map, page);
  		return "/stuwork/dorm/dyDormBedList.ftl";

	}
	@ResponseBody
	@RequestMapping("/dorm/bed/setClassName")
	@ControllerInfo("setClassName")
	public String setClassName(String  ownerId,ModelMap map) {
		try{
			Student student=SUtils.dt(studentRemoteService.findOneById(ownerId),new TypeReference<Student>() {});
			Clazz clazz=SUtils.dt(classRemoteService.findOneById(student!=null?student.getClassId():""),new TypeReference<Clazz>() {});
			if(clazz==null){
				return returnError();
			}
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("className", clazz.getClassNameDynamic());
			json.put("classId", clazz.getId());
			return json.toJSONString();
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
	}
	@ResponseBody
	@RequestMapping("/dorm/bed/saveAllBed")
	@ControllerInfo("住宿安排saveAllBed")
	public String doSaveAllBed(DyDormRoom room,String roomProperty,ModelMap map) {
		JSONObject jsonObject = new JSONObject();
		try{
			String returnMsg=dyDormBedService.saveAllBed(room.getBedList(),roomProperty);
			if(!returnMsg.equals("success")){
				jsonObject.put("code","-1");
				jsonObject.put("success",false);
				jsonObject.put("msg",returnMsg);
				return jsonObject.toJSONString();
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonObject.put("code","-1");
			jsonObject.put("success",false);
			jsonObject.put("msg",e.getMessage());
			return jsonObject.toJSONString();
		}
		jsonObject.put("currentPageIndex",currentPageIndex);
		jsonObject.put("currentPageSize",currentPageSize);
		jsonObject.put("success",true);
		jsonObject.put("msg","保存成功");
		jsonObject.put("code","0");
		return jsonObject.toJSONString();
	}
	@RequestMapping("/dorm/bed/export")
	@ControllerInfo("寝室信息导出")
	public void doExportBed(HttpServletRequest request){
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		String buildingId=request.getParameter("buildingId");
		String roomType=request.getParameter("roomType");
		String floor=request.getParameter("floor");
		String roomName=request.getParameter("roomName");
		String roomProperty=request.getParameter("roomProperty");
		String roomState=request.getParameter("roomState");
		
		boolean isStu="1".equals(roomProperty);
		
		String unitId = getLoginInfo().getUnitId();
		Map<String,String> buildingMap=EntityUtils.getMap(dyDormBuildingService.findByUnitId(unitId),"id", "name");
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(unitId, buildingId, roomType,acadyear,semester,floor,roomName,roomState,roomProperty,null);
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(CollectionUtils.isNotEmpty(roomList)){
			Map<String,String> valueMap = null;
			for(DyDormRoom room:roomList){
				List<DyDormBed> bedList=room.getBedList();
				if(CollectionUtils.isNotEmpty(bedList)){
					for(DyDormBed bed:bedList){
						valueMap=new HashMap<String, String>();
						valueMap.put("寝室楼", buildingMap.get(room.getBuildingId()));
						valueMap.put("寝室类别",room.getRoomType().equals("1")?"男寝室":"女寝室");
						valueMap.put("寝室号",room.getRoomName());
						valueMap.put("楼层",String.valueOf(room.getFloor()));
						valueMap.put("容纳人数",String.valueOf(room.getCapacity()));
						valueMap.put("床位号",String.valueOf(bed.getNo()));
						if(isStu){
							valueMap.put("班级",bed.getClassName());
							valueMap.put("学号",bed.getOwnerCode());
						}else{
							valueMap.put("教师号",bed.getOwnerCode());
						}
						valueMap.put("姓名",bed.getOwnerName());
						valueMap.put("备注",bed.getRemark());
						recordList.add(valueMap);
					}
				}
			}
		}
		List<String> tis = new ArrayList<String>();
		tis.add("寝室楼");
		tis.add("寝室类别");
		tis.add("寝室号");
		tis.add("楼层");
		tis.add("容纳人数");
		tis.add("床位号");
		if(isStu){
			tis.add("班级");
			tis.add("学号");
		}else{
			tis.add("教师号");
		}
		tis.add("姓名");
		tis.add("备注");
		sheetName2RecordListMap.put("DyDormBedExport",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put("DyDormBedExport", tis);
		String ss="";
		if("1".equals(semester)){
			ss="第一学期";
		}else{
			ss="第二学期";
		}
		ExportUtils ex = ExportUtils.newInstance();
		String str="1".equals(roomProperty)?"学生":"教师";
		ex.exportXLSFile(acadyear+"学年"+ss+str+"住宿安排导出", titleMap, sheetName2RecordListMap, getResponse());
	}
	@ResponseBody
	@RequestMapping("/dorm/bed/getResult")
	@ControllerInfo("住宿安排getResult")
	public String getResult(HttpServletRequest request,ModelMap map) {
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		String buildingId=request.getParameter("buildingId");
		String roomType=request.getParameter("roomType");
		String roomFloor=request.getParameter("roomFloor");
		String roomName=request.getParameter("roomName");
		String roomProperty=request.getParameter("roomProperty");
		String roomState=request.getParameter("roomState");
		
		JSONObject jsonObject = new JSONObject();
		try {
			String unitId=getLoginInfo().getUnitId();
			List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(unitId, buildingId, roomType,acadyear,semester,roomFloor,roomName,roomState,roomProperty,null);
			int allRoomNum=0;
			int ownerNum=0;
			int noRoomNum=0;
			if(CollectionUtils.isNotEmpty(roomList)){
				allRoomNum=roomList.size();
				Set<String> roomIds=roomList.stream().map(DyDormRoom::getId).collect(Collectors.toSet());
				List<DyDormBed> bedList=dyDormBedService.getDormBedsByRoomIds(unitId, roomIds.toArray(new String[]{}), acadyear, semester);
				Map<String,List<DyDormBed>> bedMap=new HashMap<>();
				if(CollectionUtils.isNotEmpty(bedList)){
					ownerNum=bedList.size();
					for(DyDormBed bed:bedList){
						if(!bedMap.containsKey(bed.getRoomId())){
							bedMap.put(bed.getRoomId(), new ArrayList<DyDormBed>());
						}
						bedMap.get(bed.getRoomId()).add(bed);
					}
				}
				for(DyDormRoom room:roomList){
					if(!bedMap.containsKey(room.getId())){
						noRoomNum++;
					}
				}
			}
			jsonObject.put("allRoomNum", allRoomNum);
			jsonObject.put("ownerNum", ownerNum);
			jsonObject.put("noRoomNum", noRoomNum);
			jsonObject.put("code","0");
		} catch (Exception e) {
			jsonObject.put("code","-1");
			e.printStackTrace();
		}
		return jsonObject.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/dorm/bed/clearAllBed")
	@ControllerInfo("住宿安排clearAllBed")
	public String doClearAllBed(String acadyearStr,String semesterStr,String roomId,String roomProperty,ModelMap map) {
		JSONObject jsonObject = new JSONObject();
		try{
			if(StringUtils.isNotBlank(acadyearStr)&&StringUtils.isNotBlank(semesterStr)){
				dyDormBedService.deletedByUARoomId(getLoginInfo().getUnitId(), acadyearStr,semesterStr,roomId,roomProperty);
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonObject.put("success",false);
			jsonObject.put("msg","操作失败");
			jsonObject.put("code","-1");
			jsonObject.toJSONString();
		}
		jsonObject.put("currentPageIndex",currentPageIndex);
		jsonObject.put("currentPageSize",currentPageSize);
		jsonObject.put("success",true);
		jsonObject.put("msg","操作成功");
		jsonObject.put("code","0");
		return jsonObject.toJSONString();
	}
	//------------------分割线-------------------分割线
	//------------------分割线-------------------分割线
	@RequestMapping("/dorm/room/index/page")
	@ControllerInfo(value = "寝室号维护index")
	public String showRoomIndex(ModelMap map,HttpServletRequest request, HttpSession httpSession){
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(getLoginInfo().getUnitId());
		Pagination page=createPagination();
		currentPageIndex = page.getPageIndex();
		currentPageSize = page.getPageSize();
		map.put("currentPageIndex",currentPageIndex);
		map.put("currentPageSize",currentPageSize);
		map.put("buildingList", buildingList);
		return "/stuwork/dorm/dyDormRoomIndex.ftl";
	}
	@RequestMapping("/dorm/room/list/page")
	@ControllerInfo(value = "寝室号维护list")
	public String showRoomList(ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		String buildingId=request.getParameter("buildingId");
		String roomType=request.getParameter("roomType");
		String floor=request.getParameter("floor");
		String roomName=request.getParameter("roomName");
		String roomProperty=request.getParameter("roomProperty");
		Pagination page=createPagination();
		currentPageIndex = page.getPageIndex();
		currentPageSize = page.getPageSize();

		String unitId=getLoginInfo().getUnitId();

		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);

		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(unitId, buildingId,floor,roomName, roomType,roomProperty, page);
		List<DyDormRoom> roomListLast=new  ArrayList<DyDormRoom>();
		if(CollectionUtils.isNotEmpty(buildingList)&& CollectionUtils.isNotEmpty(roomList)){
			Map<String,String> buildingMap=new HashMap<String, String>();
			for(DyDormBuilding building:buildingList){
				buildingMap.put(building.getId(), building.getName());
			}
			for(DyDormRoom room:roomList){
				room.setBuildName(buildingMap.get(room.getBuildingId()));
				roomListLast.add(room);
			}
		}

		map.put("currentPageIndex",currentPageIndex);
		map.put("currentPageSize",currentPageSize);
		map.put("buildingList", buildingList);
		map.put("roomList",dyDormRoomService.getListOrderBy(roomListLast));
		map.put("buildingId",buildingId);
		map.put("roomType",roomType);
		sendPagination(request, map, page);
		return "/stuwork/dorm/dyDormRoomList.ftl";
	}
	@RequestMapping("/dorm/room/edit/page")
	@ControllerInfo(value = "寝室信息维护edit")
	public String showRoomEdit(ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		String id=request.getParameter("id");
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(getLoginInfo().getUnitId());
		DyDormRoom room=null;
		if(StringUtils.isNotBlank(id)){
			room=dyDormRoomService.findOneBy("id", id);
		}
		if(room==null) room=new DyDormRoom();
		map.put("currentPageIndex",currentPageIndex);
		map.put("currentPageSize",currentPageSize);
		map.put("room", room);
		map.put("buildingList", buildingList);
		return "/stuwork/dorm/dyDormRoomEdit.ftl";
	}
	@ResponseBody
    @RequestMapping("/dorm/room/save")
	@ControllerInfo("寝室信息save")
    public String doRoomSave(DyDormRoom room,ModelMap map) {
		try{
			String unitId=getLoginInfo().getUnitId();
			List<DyDormRoom> roomList=dyDormRoomService.findByName(unitId,room.getRoomName(),room.getBuildingId(),null);
			boolean flag=CollectionUtils.isNotEmpty(roomList);
			if(StringUtils.isNotEmpty(room.getId())){
				if(flag){
					for(DyDormRoom dormRoom:roomList){
						if(!dormRoom.getId().equals(room.getId())){
							return error("同一寝室楼，寝室号名称不能相同");
						}
					}
				}
				room.setUnitId(unitId);
				dyDormRoomService.save(room);
//				dyDormRoomService.update(room, room.getId(), new String[]{"buildingId","roomName","roomType","capacity","floor"});
			}else{
				if(flag){
					return error("同一寝室楼，寝室号名称不能相同");
				}
				room.setUnitId(unitId);
				room.setId(UuidUtils.generateUuid());
				dyDormRoomService.save(room);
			}
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	@RequestMapping("/dorm/room/export")
	@ControllerInfo("寝室信息导出")
    public void doExportRoom(HttpServletResponse response, HttpServletRequest request){
		String buildingId=request.getParameter("buildingId");
		String floor=request.getParameter("floor");
		String roomName=request.getParameter("roomName");
		String roomType=request.getParameter("roomType");
		String roomProperty=request.getParameter("roomProperty");
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(getLoginInfo().getUnitId());
		Map<String,String> buildingMap= EntityUtils.getMap(buildingList,"id", "name");
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(getLoginInfo().getUnitId(), buildingId,floor,roomName, roomType,roomProperty, null);
		if(CollectionUtils.isNotEmpty(roomList)) {
			for (DyDormRoom dyDormRoom : roomList) {
				dyDormRoom.setBuildName(buildingMap.get(dyDormRoom.getBuildingId()));
			}
		}
		roomList.sort(new Comparator<DyDormRoom>() {
			@Override
			public int compare(DyDormRoom o1, DyDormRoom o2) {
				return o1.getBuildName().compareTo(o2.getBuildName());
			}
		});
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(CollectionUtils.isNotEmpty(roomList)){
            Map<String,String> valueMap = null;
            for(DyDormRoom room:roomList){
                valueMap=new HashMap<String, String>();
                valueMap.put("寝室楼", buildingMap.get(room.getBuildingId()));
                valueMap.put("寝室类别",room.getRoomType().equals("1")?"男寝室":"女寝室");
				valueMap.put("寝室属性",room.getRoomProperty().equals("1")?"学生寝室":"老师寝室");
                valueMap.put("寝室号",room.getRoomName());
                //valueMap.put("楼层",String.valueOf(room.getFloor()));
                //valueMap.put("容纳人数",String.valueOf(room.getCapacity()));
                valueMap.put("容纳人数",room.getCapacity()+"");
                valueMap.put("楼层",room.getFloor()+"");
                recordList.add(valueMap);
            }
        }
		List<String> tis = new ArrayList<String>();
		tis.add("寝室楼");
		tis.add("寝室类别");
		tis.add("寝室属性");
		tis.add("寝室号");
		tis.add("容纳人数");
		tis.add("楼层");
		String roomTypeName="";
		if(roomType.equals("1")){
			roomTypeName="男寝室";
		}else if(roomType.equals("2")){
			roomTypeName="女寝室";
		}
		String buildName ="";
		if(StringUtils.isNotBlank(buildingId)){
			buildName = buildingMap.get(buildingId);			}
		sheetName2RecordListMap.put("DyDormRoomExport",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put("DyDormRoomExport", tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(buildName+roomTypeName+"寝室信息导出", titleMap, sheetName2RecordListMap, getResponse());
	}
	@ControllerInfo("获取寝室楼的楼层")
	@RequestMapping("/dorm/room/getRoomFloor")
	@ResponseBody
	public String getRoomFloor(HttpServletRequest request){
		String buildingId=request.getParameter("buildingId");
		String roomProperty=request.getParameter("roomProperty");
    	JSONObject jsonObject = new JSONObject();
		String unitId=getLoginInfo().getUnitId();
		Pagination page=createPagination();
		try {
			if(StringUtils.isNotBlank(buildingId)) {
				List<DyDormRoom> dyDormRooms = dyDormRoomService.getDormRoomByCon(unitId, buildingId, null, roomProperty, null);
				List<DyDormRoom> dyDormRoomList = new ArrayList<>(dyDormRooms);
				dyDormRoomList.sort(new Comparator<DyDormRoom>() {
					@Override
					public int compare(DyDormRoom o1, DyDormRoom o2) {
						return o1.getFloor() - o2.getFloor();
					}
				});
				Set<String> floors = new HashSet<>();
				for (DyDormRoom dyDormRoom : dyDormRoomList) {
					floors.add(dyDormRoom.getFloor() + "");
				}
				jsonObject.put("code", "0");
				jsonObject.put("infolist", floors);
			}else {
				jsonObject.put("code", "0");
				jsonObject.put("infolist", null);
			}
		} catch (Exception e) {
			jsonObject.put("code","-1");
			e.printStackTrace();
		}
		return jsonObject.toJSONString();
	}
	@ControllerInfo("获取寝室楼的房间名")
	@RequestMapping("/dorm/room/getRoomName")
	@ResponseBody
	public String getRoomName(HttpServletRequest request){
		String buildingId=request.getParameter("buildingId");
		String roomFloor=request.getParameter("roomFloor");
		String roomProperty=request.getParameter("roomProperty");
		JSONObject jsonObject = new JSONObject();
		String unitId=getLoginInfo().getUnitId();
		Pagination page=createPagination();
		try {
			if(StringUtils.isNotBlank(buildingId)) {
				List<DyDormRoom> dyDormRooms = dyDormRoomService.getDormRoomByCon(unitId, buildingId, roomFloor, null, null, roomProperty, null);
				Set<String> roomNames = new HashSet<>();
				for (DyDormRoom dyDormRoom : dyDormRooms) {
					roomNames.add(dyDormRoom.getRoomName() + "");
				}
				jsonObject.put("code", "0");
				jsonObject.put("infolist", roomNames);
			}else {
				jsonObject.put("code", "0");
				jsonObject.put("infolist", null);
			}
		} catch (Exception e) {
			jsonObject.put("code","-1");
			e.printStackTrace();
		}
		return jsonObject.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/dorm/room/delete")
	@ControllerInfo("寝室信息delete")
	public String doRoomDelete(String id,ModelMap map) {
		try{
			List<DyDormBed> bedList=dyDormBedService.getDormBedsByRoomIds(getLoginInfo().getUnitId(), new String[]{id},null,null);
			if(CollectionUtils.isNotEmpty(bedList)){
				return error("该寝室下已有学生信息 不能删除");
			}
			if(StringUtils.isNotBlank(id)){
				dyDormRoomService.deletedById(id);
			}
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	//------------------分割线-------------------分割线
	@RequestMapping("/dorm/building/index/page")
	@ControllerInfo(value = "寝室楼维护index")
	public String showBuildingIndex(ModelMap map, HttpSession httpSession) {
		List<DyDormBuilding> dormBuildingList=dyDormBuildingService.findByUnitId(getLoginInfo().getUnitId());
		map.put("dormBuildingList", dormBuildingList);
		return "/stuwork/dorm/dyDormBuildingList.ftl";
	}
	
	@RequestMapping("/dorm/building/edit/page")
	@ControllerInfo(value = "寝室楼维护edit")
	public String showBuildingEdit(ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		String id=request.getParameter("id");
		DyDormBuilding building=null;
		if(StringUtils.isNotBlank(id)){
			 building=dyDormBuildingService.getDormBuildingById(id);
		}
		if(building==null){
			building=new DyDormBuilding();
		}
		map.put("building", building);
		return "/stuwork/dorm/dyDormBuildingEdit.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/dorm/building/save")
	@ControllerInfo("寝室楼save")
    public String doBuildingSave(DyDormBuilding building,ModelMap map) {
		try{
			String unitId=getLoginInfo().getUnitId();
			building.setName(building.getName().trim());
			List<DyDormBuilding> buildingList=dyDormBuildingService.getBuildsByName(unitId,building.getName());
			boolean flag=CollectionUtils.isNotEmpty(buildingList);
			
			building.setUnitId(unitId);
			if(StringUtils.isNotEmpty(building.getId())){
				if(flag){
					for(DyDormBuilding build:buildingList){
						if(!build.getId().equals(building.getId())){
							return error("寝室名称不能重复");
						}
					}
				}
				dyDormBuildingService.update(building, building.getId(), new String[]{"name"});
			}else{
				if(flag){
					return error("寝室名称不能重复");
				}
				building.setId(UuidUtils.generateUuid());
				dyDormBuildingService.save(building);
			}
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	@ResponseBody
	@RequestMapping("/dorm/building/delete")
	@ControllerInfo("寝室楼delete")
	public String doBuildingDelete(String id,ModelMap map) {
		try{
			//判断是否已安排学生
			boolean flag=dyDormBuildingService.getIsHaveStu(getLoginInfo().getUnitId(), id);
			if(flag){
				return error("该寝室楼下已有学生信息 不能删除");
			}
			if(StringUtils.isNotBlank(id)){
				dyDormBuildingService.deletedById(id);
				dyDormRoomService.deletedByBuildingId(id);
			}
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}
}
