package net.zdsoft.qulity.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "quality_score")
public class QualityScore extends BaseEntity<String> {
	private static final long serialVersionUID = 6496862172463014998L;

	private String unitId;
	private String gradeId;
	private String classId;
	private String studentId;
	private Float totalScore;
	private Integer gradeRank;
	private Integer classRank;
	private Float compreScore;//综合素质总分
	private Integer compreGradeRank;//综合素质总分年级排名
	private Float englishScore;//英语笔试总折分
	private Integer englishGradeRank;//英语笔试总折分年级排名
	private String type;
	@Temporal(TemporalType.TIMESTAMP)
	private Date statisticalTime;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getGradeRank() {
		return gradeRank;
	}

	public void setGradeRank(Integer gradeRank) {
		this.gradeRank = gradeRank;
	}

	public Integer getClassRank() {
		return classRank;
	}

	public void setClassRank(Integer classRank) {
		this.classRank = classRank;
	}

	public Float getCompreScore() {
		return compreScore;
	}

	public void setCompreScore(Float compreScore) {
		this.compreScore = compreScore;
	}

	public Integer getCompreGradeRank() {
		return compreGradeRank;
	}

	public void setCompreGradeRank(Integer compreGradeRank) {
		this.compreGradeRank = compreGradeRank;
	}

	public Float getEnglishScore() {
		return englishScore;
	}

	public void setEnglishScore(Float englishScore) {
		this.englishScore = englishScore;
	}

	public Integer getEnglishGradeRank() {
		return englishGradeRank;
	}

	public void setEnglishGradeRank(Integer englishGradeRank) {
		this.englishGradeRank = englishGradeRank;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStatisticalTime() {
		return statisticalTime;
	}

	public void setStatisticalTime(Date statisticalTime) {
		this.statisticalTime = statisticalTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "qualityScore";
	}

}
