package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
	
@Entity
@Table(name = "dy_week_check_result")
public class DyWeekCheckResult extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String schoolId;
	private String acadyear;
	private String semester;
	private int type;
	private String itemId;
	private String itemName;
	private float totalScore;
	private float score;
	private String roleType;
	private int week;
	private int day;
	private Date checkDate;
	private Date createTime;
	private Date updateTime;
	private String operator;
	private String classId;
	private String remark;
	
	@Transient
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}
	public String getSchoolId(){
		return this.schoolId;
	}
	public void setAcadyear(String acadyear){
		this.acadyear = acadyear;
	}
	public String getAcadyear(){
		return this.acadyear;
	}
	public void setSemester(String semester){
		this.semester = semester;
	}
	public String getSemester(){
		return this.semester;
	}
	public void setType(int type){
		this.type = type;
	}
	public int getType(){
		return this.type;
	}
	public void setItemId(String itemId){
		this.itemId = itemId;
	}
	public String getItemId(){
		return this.itemId;
	}
	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	public String getItemName(){
		return this.itemName;
	}
	public void setTotalScore(float totalScore){
		this.totalScore = totalScore;
	}
	public float getTotalScore(){
		return this.totalScore;
	}
	public void setScore(float score){
		this.score = score;
	}
	public float getScore(){
		return this.score;
	}
	public void setRoleType(String roleType){
		this.roleType = roleType;
	}
	public String getRoleType(){
		return this.roleType;
	}
	public void setWeek(int week){
		this.week = week;
	}
	public int getWeek(){
		return this.week;
	}
	public void setDay(int day){
		this.day = day;
	}
	public int getDay(){
		return this.day;
	}
	public void setCheckDate(Date checkDate){
		this.checkDate = checkDate;
	}
	public Date getCheckDate(){
		return this.checkDate;
	}
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	public Date getCreateTime(){
		return this.createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	public Date getUpdateTime(){
		return this.updateTime;
	}
	public void setOperator(String operator){
		this.operator = operator;
	}
	public String getOperator(){
		return this.operator;
	}
	@Override
	public String fetchCacheEntitName() {
		return "dyWeekCheckResult";
	}
}