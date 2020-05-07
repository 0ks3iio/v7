package net.zdsoft.stuwork.data.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="dy_dorm_room")
public class DyDormRoom extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String buildingId;
	private String unitId;
	private String roomName;
	private String roomType;
	/**
	 * 1学生寝室   2老师寝室
	 */
	private String roomProperty;
	private int capacity;
	private String remark;
	private int floor;
	@Transient
	private String buildName;
	@Transient
	private List<DyDormBed> bedList;
	@Transient
	private DyDormCheckResult result;
	@Transient
	private DyDormCheckRemind remind;
	
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getBuildName() {
		return buildName;
	}
	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	@Override
	public String fetchCacheEntitName() {
		return "getRoom";
	}
	public List<DyDormBed> getBedList() {
		return bedList;
	}
	public void setBedList(List<DyDormBed> bedList) {
		this.bedList = bedList;
	}
	public DyDormCheckResult getResult() {
		return result;
	}
	public void setResult(DyDormCheckResult result) {
		this.result = result;
	}
	public DyDormCheckRemind getRemind() {
		return remind;
	}
	public void setRemind(DyDormCheckRemind remind) {
		this.remind = remind;
	}
	public String getRoomProperty() {
		return roomProperty;
	}
	public void setRoomProperty(String roomProperty) {
		this.roomProperty = roomProperty;
	}
	
}
