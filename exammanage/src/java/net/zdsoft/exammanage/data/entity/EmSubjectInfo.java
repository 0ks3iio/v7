package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "exammanage_subject_info")
public class EmSubjectInfo extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ColumnInfo(displayName = "单位id", nullable = false)
    private String unitId;
    @ColumnInfo(displayName = "考试id", nullable = false)
    private String examId;
    @ColumnInfo(displayName = "科目id", nullable = false)
    private String subjectId;
    @ColumnInfo(displayName = "成绩录入方式", nullable = false, mcodeId = "DM-CJLRXSFS")
    private String inputType;
    @ColumnInfo(displayName = "满分值")
    private Float fullScore;
    @ColumnInfo(displayName = "等分", mcodeId = "DM-DDMC")
    private String gradeType;
    @ColumnInfo(displayName = "")
    private String isLock;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "考试开始时间")
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "考试结束时间")
    private Date endDate;
    @ColumnInfo(displayName = "考生范围类型  1：选考 2：学考  0：选考与学考")
    private String gkSubType;
    @ColumnInfo(displayName = "学考开始时间,新高考类型时启用")
    private Date gkStartDate;
    @ColumnInfo(displayName = "学考结束时间,新高考类型时启用")
    private Date gkEndDate;

    @Transient
    private String[] classIds;
    @Transient
    private String[] teachClassIds;
    @Transient
    private String courseName;
    @Transient
    private Map<String, String> classIdsMap = new HashMap<String, String>();//为页面上判断
    @Transient
    private Map<String, String> teachClassIdsMap = new HashMap<String, String>();//为页面上判断
    @Transient
    private String examSize;
    @Transient
    private String daytime;

    @Transient
    private String strStartDate;
    @Transient
    private String strGkStartDate;
    @Transient
    private String strGkEndDate;
    @Transient
    private String strEndDate;
    @Transient
    private boolean isYsy;

    public String getStrStartDate() {
        return strStartDate;
    }

    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    public String getStrEndDate() {
        return strEndDate;
    }

    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Map<String, String> getTeachClassIdsMap() {
        return teachClassIdsMap;
    }

    public void setTeachClassIdsMap(Map<String, String> teachClassIdsMap) {
        this.teachClassIdsMap = teachClassIdsMap;
    }

    public String[] getTeachClassIds() {
        return teachClassIds;
    }

    public void setTeachClassIds(String[] teachClassIds) {
        this.teachClassIds = teachClassIds;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public Float getFullScore() {
        return fullScore;
    }

    public void setFullScore(Float fullScore) {
        this.fullScore = fullScore;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String[] getClassIds() {
        return classIds;
    }

    public void setClassIds(String[] classIds) {
        this.classIds = classIds;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public boolean isYsy() {
        return isYsy;
    }

    public void setYsy(boolean isYsy) {
        this.isYsy = isYsy;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emSubjectInfo";
    }

    public Map<String, String> getClassIdsMap() {
        return classIdsMap;
    }

    public void setClassIdsMap(Map<String, String> classIdsMap) {
        this.classIdsMap = classIdsMap;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public String getExamSize() {
        return examSize;
    }

    public void setExamSize(String examSize) {
        this.examSize = examSize;
    }

    public String getGkSubType() {
        return gkSubType;
    }

    public void setGkSubType(String gkSubType) {
        this.gkSubType = gkSubType;
    }

    public Date getGkEndDate() {
        return gkEndDate;
    }

    public void setGkEndDate(Date gkEndDate) {
        this.gkEndDate = gkEndDate;
    }

    public String getStrGkEndDate() {
        return strGkEndDate;
    }

    public void setStrGkEndDate(String strGkEndDate) {
        this.strGkEndDate = strGkEndDate;
    }

    public Date getGkStartDate() {
        return gkStartDate;
    }

    public void setGkStartDate(Date gkStartDate) {
        this.gkStartDate = gkStartDate;
    }

    public String getStrGkStartDate() {
        return strGkStartDate;
    }

    public void setStrGkStartDate(String strGkStartDate) {
        this.strGkStartDate = strGkStartDate;
    }
}
