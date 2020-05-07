package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Tipsay;

public class TipsayDto {
	private Tipsay tipsay;
	
	private String tipsayId;
	private String className;
	private String subjectName;
	
	private String oldTeacherName;
	private String newTeacherName;
	//代课 管课
	private String typeName;
	//代课类型
	private String tipsayTypeName;
	//性别
	private Integer oldTeacherSex;
	private Integer newTeacherSex;
	//头像
	private String oldImg;
	private String newImg;
	
	private String dateStr;//12-10
	private String timeStr;//星期一上午第3节
	
	private String remark;
	//1:未成功的 2:当前时间之后的
	private boolean isCanDeleted;

	public String getTipsayId() {
		return tipsayId;
	}

	public void setTipsayId(String tipsayId) {
		this.tipsayId = tipsayId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getOldTeacherName() {
		return oldTeacherName;
	}

	public void setOldTeacherName(String oldTeacherName) {
		this.oldTeacherName = oldTeacherName;
	}

	public String getNewTeacherName() {
		return newTeacherName;
	}

	public void setNewTeacherName(String newTeacherName) {
		this.newTeacherName = newTeacherName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public Integer getOldTeacherSex() {
		return oldTeacherSex;
	}

	public void setOldTeacherSex(Integer oldTeacherSex) {
		this.oldTeacherSex = oldTeacherSex;
	}

	public String getOldImg() {
		return oldImg;
	}

	public void setOldImg(String oldImg) {
		this.oldImg = oldImg;
	}

	public String getNewImg() {
		return newImg;
	}

	public void setNewImg(String newImg) {
		this.newImg = newImg;
	}

	public Integer getNewTeacherSex() {
		return newTeacherSex;
	}

	public void setNewTeacherSex(Integer newTeacherSex) {
		this.newTeacherSex = newTeacherSex;
	}

	public boolean isCanDeleted() {
		return isCanDeleted;
	}

	public void setCanDeleted(boolean isCanDeleted) {
		this.isCanDeleted = isCanDeleted;
	}

	public Tipsay getTipsay() {
		return tipsay;
	}

	public void setTipsay(Tipsay tipsay) {
		this.tipsay = tipsay;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTipsayTypeName() {
		return tipsayTypeName;
	}

	public void setTipsayTypeName(String tipsayTypeName) {
		this.tipsayTypeName = tipsayTypeName;
	}
	
}
