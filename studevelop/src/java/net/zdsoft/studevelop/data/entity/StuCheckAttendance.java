package net.zdsoft.studevelop.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 考勤总计
 * @author gzjsd
 *
 */
@Entity
@Table(name = "studoc_check_attendance")
public class StuCheckAttendance extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;

	private String studentId;// 学生id
	private String acadyear;// 学年
	private String semester;// 学期
	private double businessVacation;//事假天数
	private double illnessVacation;//病假天数
	private Integer wasteVacation;//旷课节数
	private Integer leaveEarly;//早退节数
	private Integer late;//迟到节数
	private String remark;//备注
	@Transient
	private double studyDate;//实际上课天数
	@Transient
	private String registerBegin;//注册日期
	@Transient
	private String studyBegin;//下学期正式上课日期
	//辅助字段
	@Transient
	private String studentName;//学生姓名
	
	
	public String getStudentName() {
		return studentName;
	}



	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public String getAcadyear() {
		return acadyear;
	}



	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}



	public String getSemester() {
		return semester;
	}



	public void setSemester(String semester) {
		this.semester = semester;
	}



	/*public double getStudyDate() {
		return studyDate;
	}



	public void setStudyDate(double studyDate) {
		this.studyDate = studyDate;
	}*/



	public double getBusinessVacation() {
		return businessVacation;
	}



	public void setBusinessVacation(double businessVacation) {
		this.businessVacation = businessVacation;
	}



	public double getIllnessVacation() {
		return illnessVacation;
	}



	public void setIllnessVacation(double illnessVacation) {
		this.illnessVacation = illnessVacation;
	}



	public Integer getWasteVacation() {
		if(wasteVacation==null){
			return 0;
		}
		return wasteVacation;
	}



	public void setWasteVacation(Integer wasteVacation) {
		this.wasteVacation = wasteVacation;
	}



	public Integer getLeaveEarly() {
		if(leaveEarly==null){
			return 0;
		}
		return leaveEarly;
	}



	public void setLeaveEarly(Integer leaveEarly) {
		this.leaveEarly = leaveEarly;
	}



	public Integer getLate() {
		if(late==null){
			return 0;
		}
		return late;
	}



	public void setLate(Integer late) {
		this.late = late;
	}


/*
	public String getRegisterBegin() {
		return registerBegin;
	}



	public void setRegisterBegin(String registerBegin) {
		this.registerBegin = registerBegin;
	}



	public String getStudyBegin() {
		return studyBegin;
	}



	public void setStudyBegin(String studyBegin) {
		this.studyBegin = studyBegin;
	}
*/


	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	@Override
	public String fetchCacheEntitName() {
		return "StuCheckAttendance";
	}



	public double getStudyDate() {
		return studyDate;
	}



	public void setStudyDate(double studyDate) {
		this.studyDate = studyDate;
	}



	public String getRegisterBegin() {
		return registerBegin;
	}



	public void setRegisterBegin(String registerBegin) {
		this.registerBegin = registerBegin;
	}



	public String getStudyBegin() {
		return studyBegin;
	}



	public void setStudyBegin(String studyBegin) {
		this.studyBegin = studyBegin;
	}
	
	
}
