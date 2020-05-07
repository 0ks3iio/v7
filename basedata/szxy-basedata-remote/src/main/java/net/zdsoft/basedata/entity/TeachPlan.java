package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "timetable_teach_plan")
public class TeachPlan extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    
	
	private String acadyear; // 学年
	private int semester; // 学期
    private String unitId; // 学校Id
    private int classType;//班级类型  
	private String classId; // 班级Id
	private String subjectId; // 课程Id
	private String subjectName; // 课程名称
	private String teacherId; // 教师Id
	private String placeId;//场地Id
	/**
	 * place_relation 0:1根据关系自动匹配，就不需要选择场地，如果选择场地就不能设置自动匹配 place_relation和place_id 互斥
	 */
	private String placeRelation;
	private String className; //班级名称
	private int weekPeriod; //周课时
	private Integer rowJoint; //连排节次数
	private Integer rowNumber; //连排次数
	private String subjNo; //科目类型
	private int punchCard;//是否需要打卡
	// 单双周 
	private int weekType = CourseSchedule.WEEK_TYPE_NORMAL;
	// 与之对应的 绑定单双周课程 
	private String bindingCourse;
	
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public int getClassType() {
		return classType;
	}
	public void setClassType(int classType) {
		this.classType = classType;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	/**
	 * place_relation 0:1根据关系自动匹配，就不需要选择场地，如果选择场地就不能设置自动匹配 place_relation和place_id 互斥
	 */
	public String getPlaceRelation() {
		return placeRelation;
	}
	/**
	 * place_relation 0:1根据关系自动匹配，就不需要选择场地，如果选择场地就不能设置自动匹配 place_relation和place_id 互斥
	 */
	public void setPlaceRelation(String placeRelation) {
		this.placeRelation = placeRelation;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getWeekPeriod() {
		return weekPeriod;
	}
	public void setWeekPeriod(int weekPeriod) {
		this.weekPeriod = weekPeriod;
	}
	public Integer getRowJoint() {
		return rowJoint;
	}
	public void setRowJoint(Integer rowJoint) {
		this.rowJoint = rowJoint;
	}
	public Integer getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getSubjNo() {
		return subjNo;
	}
	public void setSubjNo(String subjNo) {
		this.subjNo = subjNo;
	}
	public int getPunchCard() {
		return punchCard;
	}
	public void setPunchCard(int punchCard) {
		this.punchCard = punchCard;
	}
	@Override
	public String fetchCacheEntitName() {
		return "teachPlan";
	}
	public int getWeekType() {
		return weekType;
	}
	public void setWeekType(int weekType) {
		this.weekType = weekType;
	}
	public String getBindingCourse() {
		return bindingCourse;
	}
	public void setBindingCourse(String bindingCourse) {
		this.bindingCourse = bindingCourse;
	}
	
}
