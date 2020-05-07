package net.zdsoft.stuwork.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dy_week_check_role_user")
public class DyWeekCheckRoleUser extends BaseEntity<String>{
		
	private static final long serialVersionUID = 1L;
	
	public static final String CHECK_ADMIN = "01"; //总管理员
    public static final String CHECK_TEACHER = "02"; //值班干部权限
    public static final String CHECK_CLASS = "03"; //值周班权限
    public static final String CHECK_STUDENT = "04"; //学生处权限
    public static final String CHECK_DEFEND = "05"; //保卫处权限
    public static final String CHECK_GRADE = "06"; //年级组权限
    public static final String CHECK_HEALTH = "07"; //体育老师权限
	public static final String CHECK_HYGIENE = "08";//卫生检查

	public static final List<String> ROLE_LIST = new ArrayList<>();

	static {
		ROLE_LIST.add("01");
		ROLE_LIST.add("02");
		ROLE_LIST.add("03");
		ROLE_LIST.add("04");
		ROLE_LIST.add("05");
		ROLE_LIST.add("06");
		ROLE_LIST.add("07");
		ROLE_LIST.add("08");
	}

	private String schoolId;
	private String acadyear;
	private String semester;
	private String roleType;
	private String userId;
	private String section;
	private String grade;
	private int week;
	private int day;
	private Date dutyDate;
	private String classId;
	
	@Transient
	private int state;//0未开始 1未提交 2已提交
	@Transient
	private String roleName;
	@Transient
	private String gradeName;
	
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public void setRoleType(String roleType){
		this.roleType = roleType;
	}
	public String getRoleType(){
		return this.roleType;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getUserId(){
		return this.userId;
	}
	public void setSection(String section){
		this.section = section;
	}
	public String getSection(){
		return this.section;
	}
	public void setGrade(String grade){
		this.grade = grade;
	}
	public String getGrade(){
		return this.grade;
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
	public void setDutyDate(Date dutyDate){
		this.dutyDate = dutyDate;
	}
	public Date getDutyDate(){
		return this.dutyDate;
	}
	public void setClassId(String classId){
		this.classId = classId;
	}
	public String getClassId(){
		return this.classId;
	}
	@Override
	public String fetchCacheEntitName() {
		return "dyWeekCheckRoleUser";
	}
}