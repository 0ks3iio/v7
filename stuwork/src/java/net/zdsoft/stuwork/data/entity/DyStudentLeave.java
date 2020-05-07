package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="office_student_leave")
public class DyStudentLeave extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String studentId;
	/**
	 * 
	 */
	private Date startTime;
	/**
	 * 
	 */
	private Date endTime;
	/**
	 * 
	 */
	private Float days;
	/**
	 * 
	 */
	private String leaveTypeId;
	/**
	 * 
	 */
	private String classId;
	/**
	 * 
	 */
	private String acadyear;
	/**
	 * 
	 */
	private Integer semester;
	/**
	 * 
	 */
	private String createUserId;
	@Transient
	private String createUserName;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String remark;
	/**
	 * 
	 */
	private String auditUserId;
	/**
	 * 1：未提交，2：已提交，3：通过，4：未通过
	 */
	private Integer state;
	/**
	 * 
	 */
	private Date auditTime;
	/**
	 * 
	 */
	private String auditRemark;
	/**
	 * 
	 */
	private int isDeleted;
	/**
	 * 
	 */
	private String unitId;
	
	private Integer backState;//回校状态，0：未判断，1：已回校，2：未回校已发短信通知
	private String invalidUserId;
	@Transient
	private String invalidUserName; 
	@Transient
	public static final Integer UN_KNOWN = 0;//未判断
	@Transient
	public static final Integer HAS_BACK_SCHOOL = 1;//已回校
	@Transient
	public static final Integer UN_BACK_SCHOOL = 2;//未回校
	
	//help----
	@Transient
	private int num;//序号
	@Transient
	private String leaveTypeName;
	@Transient
	private String stuName;
	@Transient
	private String auditUserName;//审核人姓名
	@Transient
	private String className;
	
	/**
	 * 设置
	 */
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	/**
	 * 获取
	 */
	public String getStudentId(){
		return this.studentId;
	}
	/**
	 * 设置
	 */
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}
	/**
	 * 获取
	 */
	public Date getStartTime(){
		return this.startTime;
	}
	/**
	 * 设置
	 */
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}
	/**
	 * 获取
	 */
	public Date getEndTime(){
		return this.endTime;
	}
	/**
	 * 设置
	 */
	public void setDays(Float days){
		this.days = days;
	}
	/**
	 * 获取
	 */
	public Float getDays(){
		return this.days;
	}
	/**
	 * 设置
	 */
	public void setLeaveTypeId(String leaveTypeId){
		this.leaveTypeId = leaveTypeId;
	}
	/**
	 * 获取
	 */
	public String getLeaveTypeId(){
		return this.leaveTypeId;
	}
	/**
	 * 设置
	 */
	public void setClassId(String classId){
		this.classId = classId;
	}
	/**
	 * 获取
	 */
	public String getClassId(){
		return this.classId;
	}
	/**
	 * 设置
	 */
	public void setAcadyear(String acadyear){
		this.acadyear = acadyear;
	}
	/**
	 * 获取
	 */
	public String getAcadyear(){
		return this.acadyear;
	}
	/**
	 * 设置
	 */
	public void setSemester(Integer semester){
		this.semester = semester;
	}
	/**
	 * 获取
	 */
	public Integer getSemester(){
		return this.semester;
	}
	/**
	 * 设置
	 */
	public void setCreateUserId(String createUserId){
		this.createUserId = createUserId;
	}
	/**
	 * 获取
	 */
	public String getCreateUserId(){
		return this.createUserId;
	}
	/**
	 * 设置
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 * 获取
	 */
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 * 设置
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}
	/**
	 * 获取
	 */
	public String getRemark(){
		return this.remark;
	}
	/**
	 * 设置
	 */
	public void setAuditUserId(String auditUserId){
		this.auditUserId = auditUserId;
	}
	/**
	 * 获取
	 */
	public String getAuditUserId(){
		return this.auditUserId;
	}
	/**
	 * 设置1：未提交，2：已提交，3：通过，4：未通过
	 */
	public void setState(Integer state){
		this.state = state;
	}
	/**
	 * 获取1：未提交，2：已提交，3：通过，4：未通过
	 */
	public Integer getState(){
		return this.state;
	}
	/**
	 * 设置
	 */
	public void setAuditTime(Date auditTime){
		this.auditTime = auditTime;
	}
	/**
	 * 获取
	 */
	public Date getAuditTime(){
		return this.auditTime;
	}
	/**
	 * 设置
	 */
	public void setAuditRemark(String auditRemark){
		this.auditRemark = auditRemark;
	}
	/**
	 * 获取
	 */
	public String getAuditRemark(){
		return this.auditRemark;
	}
	/**
	 * 设置
	 */
	public void setIsDeleted(int isDeleted){
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取
	 */
	public int getIsDeleted(){
		return this.isDeleted;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	public Integer getBackState() {
		return backState;
	}
	public void setBackState(Integer backState) {
		this.backState = backState;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getInvalidUserId() {
		return invalidUserId;
	}
	public void setInvalidUserId(String invalidUserId) {
		this.invalidUserId = invalidUserId;
	}
	public String getInvalidUserName() {
		return invalidUserName;
	}
	public void setInvalidUserName(String invalidUserName) {
		this.invalidUserName = invalidUserName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	@Override
	public String fetchCacheEntitName() {
		return "dyStudentLeave";
	}
   
}
