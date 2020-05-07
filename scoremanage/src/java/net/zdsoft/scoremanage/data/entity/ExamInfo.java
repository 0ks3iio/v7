package net.zdsoft.scoremanage.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "scoremanage_exam_info")
public class ExamInfo extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@ColumnInfo(displayName = "单位id", hide = true)
	@Column(updatable=false)
	private String unitId;
	@ColumnInfo(displayName = "学年", hide = true)
	@Column(updatable=false)
	private String acadyear;
	@ColumnInfo(displayName = "学期", hide = true)
	@Column(updatable=false)
	private String semester;
	@ColumnInfo(displayName = "考试名称", nullable = false)
	private String examName;
	@ColumnInfo(displayName = "考试编号", nullable = false)
	private String examCode;
	@ColumnInfo(displayName = "考试类型", nullable = false ,vtype = ColumnInfo.VTYPE_SELECT, mcodeId = "DM-KSLB")
	private String examType;
	@ColumnInfo(displayName = "统考类型", nullable = false ,vtype = ColumnInfo.VTYPE_SELECT, mcodeId = "DM-TKLX")
	private String examUeType;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "考试开始时间", nullable = false, vtype = ColumnInfo.VTYPE_DATE,format = "yyyy-MM-dd")
	private Date examStartDate;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "考试结束时间", nullable = false, vtype = ColumnInfo.VTYPE_DATE,format = "yyyy-MM-dd")
	private Date examEndDate;
	/*@ColumnInfo(displayName = "考号是否复制学号", nullable = false, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
	private Integer examnoIscode;*/
	@ColumnInfo(displayName = "是否新高类型", nullable = false, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
	private String isgkExamType;
	@ColumnInfo(displayName = "缺考学生是否统分", nullable = false,  vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
	private String isTotalScore;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "创建时间", disabled = true)
	@Column(updatable=false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "修改时间", disabled = true)
	private Date modifyTime;
	@ColumnInfo(displayName = "是否删除", hide = true)
	private int isDeleted;
	//private String isScoreLimit;
	@ColumnInfo(displayName = "考试年级范围")
	private String ranges;
	@ColumnInfo(displayName = "是否已同步到成绩分析")
	private String haveSynch;
	@Transient
	private String examUeTypeName;
	@Transient
	private String[] lkxzSelect;
	@Transient
	private Map<String,String> lkxzSelectMap;
	@Transient
	private String examNameOther;
	@Transient
	private String isStat;
	@Transient
	private String examId;
	@Transient
	private String gradeId;
	@Transient
	private String gradeName;
	@Transient
	private String isCheackSubject;
	@Transient
	private String setupScore;
	/**
	 * rangeCodeName  string[0]:code string[1]:name
	 */
	@Transient
	private List<String[]> rangeCodeName=new ArrayList<String[]>();
	
	
	public String getSetupScore() {
		return setupScore;
	}

	public void setSetupScore(String setupScore) {
		this.setupScore = setupScore;
	}

	public String getIsCheackSubject() {
		return isCheackSubject;
	}

	public void setIsCheackSubject(String isCheackSubject) {
		this.isCheackSubject = isCheackSubject;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getIsStat() {
		return isStat;
	}

	public void setIsStat(String isStat) {
		this.isStat = isStat;
	}

	public String getExamNameOther() {
		return examNameOther;
	}

	public void setExamNameOther(String examNameOther) {
		this.examNameOther = examNameOther;
	}

	public String getExamUeTypeName() {
		return examUeTypeName;
	}

	public void setExamUeTypeName(String examUeTypeName) {
		this.examUeTypeName = examUeTypeName;
	}

	public Map<String, String> getLkxzSelectMap() {
		return lkxzSelectMap;
	}

	public void setLkxzSelectMap(Map<String, String> lkxzSelectMap) {
		this.lkxzSelectMap = lkxzSelectMap;
	}

	public String[] getLkxzSelect() {
		return lkxzSelect;
	}

	public void setLkxzSelect(String[] lkxzSelect) {
		this.lkxzSelect = lkxzSelect;
	}
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
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

	/*public String getIsScoreLimit() {
		return isScoreLimit;
	}

	public void setIsScoreLimit(String isScoreLimit) {
		this.isScoreLimit = isScoreLimit;
	}*/

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getExamUeType() {
		return examUeType;
	}

	public void setExamUeType(String examUeType) {
		this.examUeType = examUeType;
	}

	public Date getExamStartDate() {
		return examStartDate;
	}

	public void setExamStartDate(Date examStartDate) {
		this.examStartDate = examStartDate;
	}

	public Date getExamEndDate() {
		return examEndDate;
	}

	public void setExamEndDate(Date examEndDate) {
		this.examEndDate = examEndDate;
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

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsgkExamType() {
		return isgkExamType;
	}

	public void setIsgkExamType(String isgkExamType) {
		this.isgkExamType = isgkExamType;
	}

	public String getIsTotalScore() {
		return isTotalScore;
	}

	public void setIsTotalScore(String isTotalScore) {
		this.isTotalScore = isTotalScore;
	}
	
	public String getRanges() {
		return ranges;
	}

	public void setRanges(String ranges) {
		this.ranges = ranges;
	}

	public List<String[]> getRangeCodeName() {
		return rangeCodeName;
	}

	public void setRangeCodeName(List<String[]> rangeCodeName) {
		this.rangeCodeName = rangeCodeName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "examInfo";
	}

	public String getHaveSynch() {
		return haveSynch;
	}

	public void setHaveSynch(String haveSynch) {
		this.haveSynch = haveSynch;
	}
}
