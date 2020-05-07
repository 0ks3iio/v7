package net.zdsoft.eclasscard.data.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StudentLeaveDto {
	private String applyTime;
	private String studentName;
	private String className;
	private String state;
	private String type;
	private List<String> taskName = new ArrayList<String>();
	private String room;
	private String leaveTime;
	private float days;
	private String applyType;
	private String linkPhone;
	private String mateName;
	private String mateGx;
	private String address;
	private String remark;
	private String hasBed;
	private String undoTime;
	private String isUndo;
	
	private String startTime;
	private String endTime;
	private String studentId;
	private String leaveType;
	private String flowId;
	
	
	public String getUndoTime() {
		return undoTime;
	}
	public void setUndoTime(String undoTime) {
		this.undoTime = undoTime;
	}
	public String getIsUndo() {
		return isUndo;
	}
	public void setIsUndo(String isUndo) {
		this.isUndo = isUndo;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getHasBed() {
		return hasBed;
	}
	public void setHasBed(String hasBed) {
		this.hasBed = hasBed;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getTaskName() {
		return taskName;
	}
	public void setTaskName(List<String> taskName) {
		this.taskName = taskName;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	public float getDays() {
		return days;
	}
	public void setDays(float days) {
		this.days = days;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public String getMateName() {
		return mateName;
	}
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	public String getMateGx() {
		return mateGx;
	}
	public void setMateGx(String mateGx) {
		this.mateGx = mateGx;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public static void main(String[] args) {
//				String jsonStr ="[{\"studentId\":\"FF8080815C0D52DB015C15772D2E0015\",\"leaveType\":\"1\",\"applyTime\":\"2017-09-12\",\"state\":\"2\",\"task\":[],\"leaveTime\":\"2017-09-11 23:58:00至2017-09-12 23:58:00\",\"days\":1,\"remark\":\"锌咀嚼\"}]";
//				 "[{\"studentId\":\"76E55BA3D5174FFEB5D4E7FD3B1709F7\",\"leaveType\":\"1\",\"applyTime\":\"2017-09-12\",\"state\":\"4\",\"task\":[\"自审(梅振衣)：[审核不通过]不通过\"],\"leaveTime\":\"2017-09-13 00:03:00至2017-09-13 23:03:00\",\"days\":1,\"remark\":\"测试\"}]";
		String jsonStr ="[{\"studentId\":\"76E55BA3D5174FFEB5D4E7FD3B1709F7\",\"leaveType\":\"1\",\"applyTime\":\"2017-09-20\",\"state\":\"3\",\"task\":[\"自审(梅振衣)：[审核通过]测试\"],\"leaveTime\":\"2017-09-20 02:17:00至2017-09-20 22:17:00\",\"days\":0.8,\"remark\":\"ce2\"},{\"studentId\":\"76E55BA3D5174FFEB5D4E7FD3B1709F7\",\"leaveType\":\"1\",\"applyTime\":\"2017-09-11\",\"state\":\"3\",\"task\":[\"自审(梅振衣)：[审核通过]可以\"],\"leaveTime\":\"2017-09-12 00:00:00至2017-09-12 23:57:00\",\"days\":1,\"remark\":\"我只要自由\"},{\"studentId\":\"76E55BA3D5174FFEB5D4E7FD3B1709F7\",\"leaveType\":\"1\",\"applyTime\":\"2017-09-12\",\"state\":\"4\",\"task\":[\"自审(梅振衣)：[审核不通过]不通过\"],\"leaveTime\":\"2017-09-13 00:03:00至2017-09-13 23:03:00\",\"days\":1,\"remark\":\"测试\"}]";
				List<StudentLeaveDto> dtolist = new ArrayList<StudentLeaveDto>();
				JSONArray jsonArray = JSONArray.parseArray(jsonStr);
		    	if(jsonArray != null&&jsonArray.size()>0){
		    		StudentLeaveDto dto = null;
		    		for(int i = 0;i<jsonArray.size();i++){
		    			dto =new StudentLeaveDto();
		    			JSONObject jsonParam = jsonArray.getJSONObject(i);
		    			StudentLeaveDto.toDto(jsonParam,dto);
		    			System.out.println("remark:"+dto.getRemark());
		    			dtolist.add(dto);
		    		}
		    	}
		    	System.out.println(dtolist.size());
	}
	
	public static void toDto(JSONObject jsonParam, StudentLeaveDto dto) {
		if(jsonParam.containsKey("leaveType")){
    		dto.setType(jsonParam.getString("leaveType").toString());
    	}
		if(jsonParam.containsKey("applyTime")){
    		dto.setApplyTime(jsonParam.getString("applyTime").toString());
    	}
		if(jsonParam.containsKey("undoTime")){
			dto.setUndoTime(jsonParam.getString("undoTime").toString());
		}
		if(jsonParam.containsKey("isUndo")){
			dto.setIsUndo(jsonParam.getString("isUndo").toString());
		}
		if(jsonParam.containsKey("state")){
    		dto.setState(jsonParam.getString("state").toString());
    	}
		if(jsonParam.containsKey("leaveTime")){
    		dto.setLeaveTime(jsonParam.getString("leaveTime").toString());
    	}
		if(jsonParam.containsKey("days")){
    		dto.setDays(NumberUtils.toFloat(jsonParam.getString("days").toString()));
    	}
		if(jsonParam.containsKey("remark")){
    		dto.setRemark(jsonParam.getString("remark").toString());
    	}
		if(jsonParam.containsKey("linkPhone")){
    		dto.setLinkPhone(jsonParam.getString("linkPhone").toString());
    	}
		if(jsonParam.containsKey("applyType")){
    		dto.setApplyType(jsonParam.getString("applyType").toString());
    	}
		if(jsonParam.containsKey("hasBed")){
    		dto.setHasBed(jsonParam.getString("hasBed").toString());
    	}
		if(jsonParam.containsKey("address")){
    		dto.setAddress(jsonParam.getString("address").toString());
    	}
		if(jsonParam.containsKey("mateName")){
    		dto.setMateName(jsonParam.getString("mateName").toString());
    	}
		if(jsonParam.containsKey("mateGx")){
    		dto.setMateGx(jsonParam.getString("mateGx").toString());
    	}
		if(jsonParam.containsKey("task")){
			String[] tasks = jsonParam.getJSONArray("task").toArray(new String[0]);
			for (String task : tasks) {
				dto.getTaskName().add(task);
			}
		}
	}
}
