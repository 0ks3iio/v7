package net.zdsoft.stuwork.data.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;

public class DyWeekCheckResultDto {
	private DyWeekCheckItem item;
	
	private List<DyWeekCheckResult> result = new ArrayList<DyWeekCheckResult>();

	//汇总是用到
	private String itemName;
	private String itemId;
	private int itemType;
	private int day;
	private String dayName;
	private int week;
	private float score;
	private int itemOrder;
	
	private boolean unCheck;//是否不需要考核
	private boolean allUnSubmint;//是否所有人都没有提交
	private Set<String> unSubRole = new HashSet<String>();//没有提交的角色
	private String remark;
	
	
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isUnCheck() {
		return unCheck;
	}

	public void setUnCheck(boolean unCheck) {
		this.unCheck = unCheck;
	}

	public boolean isAllUnSubmint() {
		return allUnSubmint;
	}

	public void setAllUnSubmint(boolean allUnSubmint) {
		this.allUnSubmint = allUnSubmint;
	}

	public Set<String> getUnSubRole() {
		return unSubRole;
	}

	public void setUnSubRole(Set<String> unSubRole) {
		this.unSubRole = unSubRole;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public DyWeekCheckItem getItem() {
		return item;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void setItem(DyWeekCheckItem item) {
		this.item = item;
	}

	public List<DyWeekCheckResult> getResult() {
		return result;
	}

	public void setResult(List<DyWeekCheckResult> result) {
		this.result = result;
	}
	
	
	
}
