package net.zdsoft.exammanage.data.entity;


import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "exammanage_exam_info")
public class EmExamInfo extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    @ColumnInfo(displayName = "单位id", hide = true)
    @Column(updatable = false)
    private String unitId;
    @ColumnInfo(displayName = "学年", hide = true)
    @Column(updatable = false)
    private String acadyear;
    @ColumnInfo(displayName = "学期", hide = true)
    @Column(updatable = false)
    private String semester;
    @ColumnInfo(displayName = "考试名称", nullable = false)
    private String examName;
    @ColumnInfo(displayName = "考试编号", nullable = false)
    private String examCode;
    @ColumnInfo(displayName = "考试类型", nullable = false, vtype = ColumnInfo.VTYPE_SELECT, mcodeId = "DM-KSLB")
    private String examType;
    @ColumnInfo(displayName = "统考类型", nullable = false, vtype = ColumnInfo.VTYPE_SELECT, mcodeId = "DM-TKLX")
    private String examUeType;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "考试开始时间", nullable = false, vtype = ColumnInfo.VTYPE_DATE, format = "yyyy-MM-dd")
    private Date examStartDate;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "考试结束时间", nullable = false, vtype = ColumnInfo.VTYPE_DATE, format = "yyyy-MM-dd")
    private Date examEndDate;
    @ColumnInfo(displayName = "考试对象")
    private String gradeCodes;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "创建时间", disabled = true)
    @Column(updatable = false)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "修改时间", disabled = true)
    private Date modifyTime;
    @ColumnInfo(displayName = "是否删除", hide = true)
    private int isDeleted;
    @ColumnInfo(displayName = "是否新高考", hide = true)
    private String isgkExamType;

    @ColumnInfo(displayName = "是否学生端报名")
    private String isStuSign;
    @ColumnInfo(displayName = "学生端报名开始时间")
    private Date signStartTime;
    @ColumnInfo(displayName = "学生端报名结束时间")
    private Date signEndTime;

    private String originExamId;


    @Transient
    private String examUeTypeName;//统考类型
    @Transient
    private String[] lkxzSelect;
    @Transient
    private Map<String, String> lkxzSelectMap;
    @Transient
    private String examNameOther;
    @Transient
    private String gradeCodeName;
    @Transient
    private String isStat;//是否统计 用于显示列表
    @Transient
    private String isEdit;//是否可修改 科目可修改
    @Transient
    private String isEduEdit;
    @Transient
    private Integer countNum;
    @Transient
    private Integer passNum;
    @Transient
    private Integer auditNum;
    @Transient
    private Integer arrangeNum;
    @Transient
    private Integer noArrangeNum;
    @Transient
    private Integer placeNum;
    @Transient
    private Integer noPlaceNum;
    @Transient
    private int arrangeResult;//1:编排中；0编排线程未进行
    @Transient
    private String subjectNames;
    @Transient
    private String state;

    public int getArrangeResult() {
        return arrangeResult;
    }

    public void setArrangeResult(int arrangeResult) {
        this.arrangeResult = arrangeResult;
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

    public String getGradeCodes() {
        return gradeCodes;
    }

    public void setGradeCodes(String gradeCodes) {
        this.gradeCodes = gradeCodes;
    }

    public String getGradeCodeName() {
        return gradeCodeName;
    }

    public void setGradeCodeName(String gradeCodeName) {
        this.gradeCodeName = gradeCodeName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emExamInfo";
    }

    public String getIsStat() {
        return isStat;
    }

    public void setIsStat(String isStat) {
        this.isStat = isStat;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getIsEduEdit() {
        return isEduEdit;
    }

    public void setIsEduEdit(String isEduEdit) {
        this.isEduEdit = isEduEdit;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Integer getPassNum() {
        return passNum;
    }

    public void setPassNum(Integer passNum) {
        this.passNum = passNum;
    }

    public Integer getAuditNum() {
        return auditNum;
    }

    public void setAuditNum(Integer auditNum) {
        this.auditNum = auditNum;
    }

    public Integer getArrangeNum() {
        return arrangeNum;
    }

    public void setArrangeNum(Integer arrangeNum) {
        this.arrangeNum = arrangeNum;
    }

    public Integer getNoArrangeNum() {
        return noArrangeNum;
    }

    public void setNoArrangeNum(Integer noArrangeNum) {
        this.noArrangeNum = noArrangeNum;
    }

    public Integer getPlaceNum() {
        return placeNum;
    }

    public void setPlaceNum(Integer placeNum) {
        this.placeNum = placeNum;
    }

    public Integer getNoPlaceNum() {
        return noPlaceNum;
    }

    public void setNoPlaceNum(Integer noPlaceNum) {
        this.noPlaceNum = noPlaceNum;
    }

    public String getIsgkExamType() {
        return isgkExamType;
    }

    public void setIsgkExamType(String isgkExamType) {
        this.isgkExamType = isgkExamType;
    }

    public String getOriginExamId() {
        return originExamId;
    }

    public void setOriginExamId(String originExamId) {
        this.originExamId = originExamId;
    }

    public String getIsStuSign() {
        return isStuSign;
    }

    public void setIsStuSign(String isStuSign) {
        this.isStuSign = isStuSign;
    }

    public Date getSignStartTime() {
        return signStartTime;
    }

    public void setSignStartTime(Date signStartTime) {
        this.signStartTime = signStartTime;
    }

    public Date getSignEndTime() {
        return signEndTime;
    }

    public void setSignEndTime(Date signEndTime) {
        this.signEndTime = signEndTime;
    }

    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
