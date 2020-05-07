package net.zdsoft.stuwork.data.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.entity.DyLeaveType;
import net.zdsoft.stuwork.data.entity.DyStudentLeave;
import net.zdsoft.stuwork.data.service.DyLeaveTypeService;
import net.zdsoft.stuwork.data.service.DyStudentLeaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stuwork") 
public class DyStudentLeaveAction extends BaseAction{
	@Autowired
	private DyStudentLeaveService dyStudentLeaveService;
	@Autowired
	private DyLeaveTypeService dyLeaveTypeService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@RequestMapping("/studentLeave/studentLeavePage")
    @ControllerInfo(value = "")
	public String studentLeaveHead(ModelMap map){
		return "/stuwork/studentLeave/studentLeaveHead.ftl";
	}
	
	@RequestMapping("/studentLeave/edit")
    @ControllerInfo(value = "")
	public String studentLeaveEdit(String id, ModelMap map){
		List<DyLeaveType> dyLeaveTypeList = dyLeaveTypeService.findLeaveTypeListByState(getLoginInfo().getUnitId(), 1); //学生类型\
		DyStudentLeave dyStudentLeave = dyStudentLeaveService.findOneBy("id", id);
		map.put("dyLeaveTypeList", dyLeaveTypeList);
		map.put("dyStudentLeave", dyStudentLeave);
		return "/stuwork/studentLeave/studentLeaveEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/studentLeave/delete")
    @ControllerInfo(value = "")
	public String delete(String id, ModelMap map){
		try{
			dyStudentLeaveService.delete(id);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();		
	}
	
	@ResponseBody
	@RequestMapping("/studentLeave/save")
    @ControllerInfo(value = "")
	public String save(DyStudentLeave dyStudentLeave, int state, ModelMap map){
		try{
			Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
			String studentId = getLoginInfo().getOwnerId();
			List<DyStudentLeave> dyStudentLeaveList = dyStudentLeaveService.findDyStudentLeaveByStuId(studentId, null);
			if(StringUtils.isNotBlank(dyStudentLeave.getId())){
				List<DyStudentLeave> dyStudentLeaveList2 = new ArrayList<DyStudentLeave>();
				for(DyStudentLeave item: dyStudentLeaveList){
					if(item.getId().equals(dyStudentLeave.getId())){
						dyStudentLeaveList2.add(item);
					}	               
				}
				dyStudentLeaveList.removeAll(dyStudentLeaveList2);
			}
			for(DyStudentLeave item : dyStudentLeaveList){
				if((dyStudentLeave.getStartTime().getTime()>item.getStartTime().getTime() && dyStudentLeave.getStartTime().getTime()<item.getEndTime().getTime())
						|| dyStudentLeave.getEndTime().getTime()>item.getStartTime().getTime() && dyStudentLeave.getEndTime().getTime()<item.getEndTime().getTime()
						|| dyStudentLeave.getStartTime().getTime()<item.getStartTime().getTime() && dyStudentLeave.getEndTime().getTime()>item.getEndTime().getTime()){
					return error("该时间段已有请假申请，请重新输出请假日期！");
				}
			}			
			Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			if(StringUtils.isBlank(dyStudentLeave.getId())){
				dyStudentLeave.setId(UuidUtils.generateUuid());
			}
			dyStudentLeave.setAcadyear(sem.getAcadyear());
			dyStudentLeave.setSemester(sem.getSemester());
			dyStudentLeave.setStudentId(studentId);
			dyStudentLeave.setClassId(student.getClassId());
			dyStudentLeave.setCreateTime(new Date());
			dyStudentLeave.setIsDeleted(0);
			dyStudentLeave.setUnitId(getLoginInfo().getUnitId());
			dyStudentLeave.setBackState(0);//未判断
			dyStudentLeave.setState(state);
			dyStudentLeave.setCreateUserId(getLoginInfo().getUserId());
			dyStudentLeaveService.save(dyStudentLeave);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();		
	}
		
	@RequestMapping("/studentLeave/studentLeaveList")
    @ControllerInfo(value = "")
	public String studentLeaveList(String startTime, String endTime, String state, ModelMap map){
		Date queryStartDate = null;
		Date queryEndDate = null;
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
			String qStartTime = startTime.split(",")[0]+" "+startTime.split(",")[1];
			String qEndTime = endTime.split(",")[0]+ " "+endTime.split(",")[1];
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			try {
				queryStartDate = format.parse(qStartTime);
				queryEndDate = format.parse(qEndTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<DyStudentLeave> dyStudentLeaveList;
		Pagination page = createPagination();
		page.setPageSize(10);
		if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime) && StringUtils.isBlank(state)){
			dyStudentLeaveList = dyStudentLeaveService.findDyStudentLeaveByStuId(getLoginInfo().getOwnerId(), page);
		}else if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime) && StringUtils.isBlank(state)){
			dyStudentLeaveList = dyStudentLeaveService.findDyStudentLeaveByTime(queryStartDate, queryEndDate, getLoginInfo().getOwnerId(), page);
		}else if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime) && StringUtils.isNotBlank(state)){
			dyStudentLeaveList = dyStudentLeaveService.findDyStudentLeaveByState(Integer.parseInt(state), getLoginInfo().getOwnerId(), page);
		}else if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime) && StringUtils.isNotBlank(state)){
			dyStudentLeaveList = dyStudentLeaveService.findDyStudentLeaveByStateAndTime(queryStartDate, queryEndDate, Integer.parseInt(state), getLoginInfo().getOwnerId(), page);
		}else{
			dyStudentLeaveList = new ArrayList<DyStudentLeave>();
		}
		List<DyLeaveType> dyLeaveTypeList = dyLeaveTypeService.findLeaveTypeListByState(getLoginInfo().getUnitId(), 1); //学生类型
		Map<String, String> leaveTypeMap = new HashMap<String, String>();
		for(DyLeaveType item : dyLeaveTypeList){
			leaveTypeMap.put(item.getId(), item.getName());
		}
		Student student = SUtils.dc(studentRemoteService.findOneById(getLoginInfo().getOwnerId()), Student.class);
		for(DyStudentLeave item : dyStudentLeaveList){
			item.setLeaveTypeName(leaveTypeMap.get(item.getLeaveTypeId()));
			item.setStuName(student.getStudentName());
		}
		map.put("dyStudentLeaveList", dyStudentLeaveList);
		map.put("pagination", page);
		return "/stuwork/studentLeave/studentLeaveList.ftl";
	}

}
