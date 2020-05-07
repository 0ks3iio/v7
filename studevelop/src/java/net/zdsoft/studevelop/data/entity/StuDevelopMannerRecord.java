package net.zdsoft.studevelop.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="studoc_manner_record")
public class StuDevelopMannerRecord extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String studentId;
	private String subjectId;
	private String acadyear;// 学年
	private String semester;// 学期
	private String manner;// 学习态度
	private String discovery; // 发现能力
	private String communication; // 交流能力 
	@Transient
	private String qzScore;// 期中考试成绩
	@Transient
	private String qmScore;// 期末考试成绩
	@Transient
	private String zpScore;// 综合评定成绩
	
	//辅助字段
	@Transient
	private String studentName;
	@Transient
	private String subjectName;
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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
	public String getManner() {
		return manner;
	}
	public void setManner(String manner) {
		this.manner = manner;
	}
	public String getDiscovery() {
		return discovery;
	}
	public void setDiscovery(String discovery) {
		this.discovery = discovery;
	}
	public String getCommunication() {
		return communication;
	}
	public void setCommunication(String communication) {
		this.communication = communication;
	}
	@Override
	public String fetchCacheEntitName() {
		return "stuDevelopMannerRecord";
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getQzScore() {
		return qzScore;
	}
	public void setQzScore(String qzScore) {
		this.qzScore = qzScore;
	}
	public String getQmScore() {
		return qmScore;
	}
	public void setQmScore(String qmScore) {
		this.qmScore = qmScore;
	}
	public String getZpScore() {
		return zpScore;
	}
	public void setZpScore(String zpScore) {
		this.zpScore = zpScore;
	}  
	
}
