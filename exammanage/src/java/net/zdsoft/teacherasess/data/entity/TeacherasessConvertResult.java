package net.zdsoft.teacherasess.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teacherasess_convert_result")
public class TeacherasessConvertResult extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	private String unitId;
	private String convertId;
	private String studentId;
	private String subjectId;
	private String studentName;
	private String studentCode;
	private String classId;
	private String className;
	private float score;//折算分
	private float conScore; //赋分
	private int rank;//排名
	@Transient
	private String xkRank;

	public String getXkRank() {
		return xkRank;
	}

	public void setXkRank(String xkRank) {
		this.xkRank = xkRank;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getConvertId() {
		return convertId;
	}

	public void setConvertId(String convertId) {
		this.convertId = convertId;
	}

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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getConScore() {
		return conScore;
	}

	public void setConScore(float conScore) {
		this.conScore = conScore;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teacherasessConvert";
	}
	
}