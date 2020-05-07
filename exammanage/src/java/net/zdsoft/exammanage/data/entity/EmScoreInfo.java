package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "exammanage_score_info")
public class EmScoreInfo extends BaseEntity<String> {

    /**
     * 成绩录入方式：分数或等第
     */
    public static final String ACHI_SCORE = "S";// 分数
    public static final String ACHI_GRADE = "G";// 等第

    public static final String SCORE_TYPE_0 = "0";//成绩状态正常
    public static final String SCORE_TYPE_1 = "1";//成绩状态缺考
    public static final String SCORE_TYPE_2 = "2";//成绩状态作弊
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String acadyear;
    private String semester;
    private String examId;
    private String subjectId;
    private String unitId;
    private String classId;//行政班
    private String teachClassId;//教学班--暂时不起作用
    private String studentId;
    private String scoreStatus;//成绩状态 DM-CJLX  正常 缺考 作弊
    private String score;//原始分
    private String toScore;//总评
    private Date creationTime;
    private Date modifyTime;
    private String operatorId;
    private String inputType;//成绩录入方式
    private String isInStat;//是否已经计入统计（统计之后是否可以修改）---统计分析后
    private String facet;//是否统分
    private String scoreRank;//选考赋分等级
    private String conScore;//选考赋分
    private String gkSubType;//科目考试类型 0:所有（非高考or语数英） 1：选考 2：学考
    private String rank;//排名
    @Transient
    private String subjectName;
    @Transient
    private String courseTypeName;

    @Transient
    private Float showScore;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getGkSubType() {
        return gkSubType;
    }

    public void setGkSubType(String gkSubType) {
        this.gkSubType = gkSubType;
    }

    public String getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(String scoreRank) {
        this.scoreRank = scoreRank;
    }

    public String getConScore() {
        if (StringUtils.isBlank(conScore)) {
            conScore = "0";
        }
        return conScore;
    }

    public void setConScore(String conScore) {
        this.conScore = conScore;
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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeachClassId() {
        return teachClassId;
    }

    public void setTeachClassId(String teachClassId) {
        this.teachClassId = teachClassId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(String scoreStatus) {
        this.scoreStatus = scoreStatus;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getToScore() {
        return toScore;
    }

    public void setToScore(String toScore) {
        this.toScore = toScore;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getIsInStat() {
        return isInStat;
    }

    public void setIsInStat(String isInStat) {
        this.isInStat = isInStat;
    }

    public String getFacet() {
        return facet;
    }

    public void setFacet(String facet) {
        this.facet = facet;
    }

    public Float getShowScore() {
        return showScore;
    }

    public void setShowScore(Float showScore) {
        this.showScore = showScore;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emScoreInfo";
    }

}

