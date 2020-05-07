package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.framework.annotation.ColumnInfo;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * scoremanage ExamInfo
 *
 * @author XuW
 */
public class ExamInfoDto {

    private String id;
    @ColumnInfo(displayName = "单位id", hide = true)
    private String unitId;
    @ColumnInfo(displayName = "学年", hide = true)
    private String acadyear;
    @ColumnInfo(displayName = "学期", hide = true)
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
    @ColumnInfo(displayName = "是否新高类型", nullable = false, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
    private String isgkExamType;
    @ColumnInfo(displayName = "缺考学生是否统分", nullable = false, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
    private String isTotalScore;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "创建时间", disabled = true)
    @Column(updatable = false)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "修改时间", disabled = true)
    private Date modifyTime;
    @ColumnInfo(displayName = "是否删除", hide = true)
    private int isDeleted;
    @ColumnInfo(displayName = "考试年级范围")
    private String ranges;
    @ColumnInfo(displayName = "是否已同步到成绩分析")
    private int haveSynch;

    private String className;

    private String gradeName;

    private String subjectName;
    private String subjectId;
    private String subType;
    private String classId;
    private String teachClassType;//1 代表教学班


    public EmExamInfo getExamInfoByDto() {
        EmExamInfo exam = new EmExamInfo();
        exam.setUnitId(unitId);
        exam.setAcadyear(acadyear);
        exam.setSemester(semester);
        exam.setExamName(examName);
        exam.setExamCode(examCode);
        exam.setExamType(examType);
        exam.setExamUeType(examUeType);
        exam.setExamStartDate(examStartDate);
        exam.setExamEndDate(examEndDate);
        exam.setIsgkExamType(isgkExamType);
        exam.setCreationTime(creationTime);
        exam.setModifyTime(modifyTime);
        exam.setIsDeleted(isDeleted);//isTotalScore  ranges
        return exam;
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

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public int getHaveSynch() {
        return haveSynch;
    }

    public void setHaveSynch(int haveSynch) {
        this.haveSynch = haveSynch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeachClassType() {
        return teachClassType;
    }

    public void setTeachClassType(String teachClassType) {
        this.teachClassType = teachClassType;
    }


}
