package net.zdsoft.teacherasess.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="teacherasess_asess_check")
public class TeacherAsessCheck extends BaseEntity<String>{
	
	private String unitId;
	private String assessId;
	private String subjectId;
	private String classId;
	private String classType;
	private String className;
	private String asessRankId;//空表示总
	private float lineScore;//得分
	@Transient
	private String lineScoreStr;
	private int lineRank;//排名
	
	public TeacherAsessCheck() {
	}
	
	public TeacherAsessCheck(String unitId,String assessId,String subjectId,String classId,
			String classType,String className,String asessRankId,float lineScore,int lineRank) {
		this.unitId = unitId;
		this.assessId = assessId;
		this.subjectId = subjectId;
		this.classId = classId;
		this.classType = classType;
		this.className = className;
		this.asessRankId = asessRankId;
		this.lineScore = lineScore;
		this.lineRank = lineRank;
	}
	
	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public float getLineScore() {
		return lineScore;
	}

	public void setLineScore(float lineScore) {
		this.lineScore = lineScore;
	}

	public int getLineRank() {
		return lineRank;
	}

	public void setLineRank(int lineRank) {
		this.lineRank = lineRank;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAssessId() {
		return assessId;
	}

	public void setAssessId(String assessId) {
		this.assessId = assessId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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

	public String getAsessRankId() {
		return asessRankId;
	}

	public void setAsessRankId(String asessRankId) {
		this.asessRankId = asessRankId;
	}

	public String getLineScoreStr() {
		return lineScoreStr;
	}

	public void setLineScoreStr(String lineScoreStr) {
		this.lineScoreStr = lineScoreStr;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teacherAsessCheck";
	}

}
