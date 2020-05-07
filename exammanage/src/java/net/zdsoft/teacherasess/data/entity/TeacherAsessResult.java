package net.zdsoft.teacherasess.data.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="teacherasess_asess_result")
public class TeacherAsessResult extends BaseEntity<String>{
	
	@ColumnInfo(displayName = "单位id", hide = true)
	@Column(updatable=false)
	private String unitId;
	@ColumnInfo(displayName = "考核方案id", hide = true)
	@Column(updatable=false)
	private String assessId;
	@ColumnInfo(displayName = "班级id", hide = true)
	@Column(updatable=false)
	private String classId;
	@ColumnInfo(displayName = "班级类型", hide = true)
	@Column(updatable=false)
	private String classType;
	@ColumnInfo(displayName = "班级名称", hide = true)
	@Column(updatable=false)
	private String className;
	@ColumnInfo(displayName = "科目Id", hide = true)
	@Column(updatable=false)
	private String subjcetId;
	@ColumnInfo(displayName = "教师Id", hide = true)
	@Column(updatable=false)
	private String teacherId;
	@ColumnInfo(displayName = "教师名称", hide = true)
	@Column(updatable=false)
	private String teacherName;
	@ColumnInfo(displayName = "本次方案系数", hide = true)
	private int convertParam;
	@ColumnInfo(displayName = "参考方案系数", hide = true)
	private int referConvertParam;
	@ColumnInfo(displayName = "进步学生数", hide = true)
	private int upStuNum;
	@ColumnInfo(displayName = "考核分", hide = true)
	private int asessScore;
	@ColumnInfo(displayName = "排名", hide = true)
	private int rank;
	@ColumnInfo(displayName = "进步率", hide = true)
	private float upScale;
	@ColumnInfo(displayName = "进步率排名", hide = true)
	private int upScaleRank;
	@ColumnInfo(displayName = "最终考核分", hide = true)
	private float score;
	@ColumnInfo(displayName = "最终考核分排名”", hide = true)
	private int scoreRank;
	
	@Override
	public String fetchCacheEntitName() {
		return "teacherAsessResult";
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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSubjcetId() {
		return subjcetId;
	}

	public void setSubjcetId(String subjcetId) {
		this.subjcetId = subjcetId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}	

	public int getConvertParam() {
		return convertParam;
	}

	public void setConvertParam(int convertParam) {
		this.convertParam = convertParam;
	}

	public int getReferConvertParam() {
		return referConvertParam;
	}

	public void setReferConvertParam(int referConvertParam) {
		this.referConvertParam = referConvertParam;
	}

	public int getUpStuNum() {
		return upStuNum;
	}

	public void setUpStuNum(int upStuNum) {
		this.upStuNum = upStuNum;
	}

	public int getAsessScore() {
		return asessScore;
	}

	public void setAsessScore(int asessScore) {
		this.asessScore = asessScore;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}


	public float getUpScale() {
		if(upScale!=0){
			BigDecimal b1 =new  BigDecimal(upScale);
			return b1.setScale(1,  BigDecimal.ROUND_HALF_UP).floatValue();
		}
		return upScale;
	}

	public void setUpScale(float upScale) {
		this.upScale = upScale;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getUpScaleRank() {
		return upScaleRank;
	}

	public void setUpScaleRank(int upScaleRank) {
		this.upScaleRank = upScaleRank;
	}

	public int getScoreRank() {
		return scoreRank;
	}

	public void setScoreRank(int scoreRank) {
		this.scoreRank = scoreRank;
	}

}
