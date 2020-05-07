package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 学生任职情况
 * @author gzjsd
 *
 */
@Entity
@Table(name = "studoc_duty_situation")
public class StudevelopDutySituation extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String studentId;// 学生id
	private String acadyear;// 学年
	private String semester;// 学期
	private String dutyName;// 所任职位
	private String dutyContent;// 工作内容
	private String dutySituation;// 工作表现情况
	private Date openTime;// 任职开始时间
	private Date endTime;// 结束时间
	private String remark;// 备注栏
	@Transient
	private String studentName;
	@Transient
	private String classId;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

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



	public String getDutyName() {
		return dutyName;
	}



	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}



	public String getDutyContent() {
		return dutyContent;
	}



	public void setDutyContent(String dutyContent) {
		this.dutyContent = dutyContent;
	}



	public String getDutySituation() {
		return dutySituation;
	}



	public void setDutySituation(String dutySituation) {
		this.dutySituation = dutySituation;
	}



	public Date getOpenTime() {
		return openTime;
	}



	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}



	public Date getEndTime() {
		return endTime;
	}



	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	@Override
	public String fetchCacheEntitName() {
		return "StudocDutySituation";
	}
	
}
