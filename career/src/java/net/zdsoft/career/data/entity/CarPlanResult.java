package net.zdsoft.career.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="career_plan_result")
public class CarPlanResult extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String schoolId;
	private String studentId;
	private String answer;
	private String resultType;
	@Column(name = "score_c")
	private Integer scoreC;
	@Column(name = "score_r")
	private Integer scoreR;
	@Column(name = "score_i")
	private Integer scoreI;
	@Column(name = "score_a")
	private Integer scoreA;
	@Column(name = "score_s")
	private Integer scoreS;
	@Column(name = "score_e")
	private Integer scoreE;
	private Integer isDeleted ;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	
	@Transient
	private String studentName;
	@Transient
	private String className;
	@Transient
	private String content;
	@Transient
	private List<String> typelist = new ArrayList<String>();
	
	@Override
	public String fetchCacheEntitName() {
		return "carPlanTestResult";
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public Integer getScoreC() {
		return scoreC;
	}

	public void setScoreC(Integer scoreC) {
		this.scoreC = scoreC;
	}

	public Integer getScoreR() {
		return scoreR;
	}

	public void setScoreR(Integer scoreR) {
		this.scoreR = scoreR;
	}

	public Integer getScoreI() {
		return scoreI;
	}

	public void setScoreI(Integer scoreI) {
		this.scoreI = scoreI;
	}

	public Integer getScoreA() {
		return scoreA;
	}

	public void setScoreA(Integer scoreA) {
		this.scoreA = scoreA;
	}

	public Integer getScoreS() {
		return scoreS;
	}

	public void setScoreS(Integer scoreS) {
		this.scoreS = scoreS;
	}

	public Integer getScoreE() {
		return scoreE;
	}

	public void setScoreE(Integer scoreE) {
		this.scoreE = scoreE;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<String> getTypelist() {
		return typelist;
	}

	public void setTypelist(List<String> typelist) {
		this.typelist = typelist;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
