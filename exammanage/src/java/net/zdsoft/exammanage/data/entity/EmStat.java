package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 学生统计结果
 */
@Entity
@Table(name = "exammanage_stat")
public class EmStat extends BaseEntity<String> {

    public static final String STAT_TOTAL = "00000000000000000000000000000000";
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String statObjectId;
    private String examId;
    private String schoolId;
    private String classId;
    private String subjectId;
    private String studentId;
    private Integer classRank;//（班级排名
    private Integer gradeRank;//（年级排名）
    private Integer eduRank;//（教育局排名）
    private Float score;//成绩
    private Float conScore;//赋分成绩
    private Float scoreT;//标准分T(年级)
    private Float scoreZ;//标准分Z
    private Float scoreLevel;//百分等级分数
    private Float abilityScore;//综合能力分（总分统计时有值）
    private Integer abilityRank;//综合能力分排名


    private String subType;//科目类型
    private String classType;//班级类型
    private String className;
    private String gradeName;

    private String conScoreRank;//赋分等级
    @Transient
    private String subjectName;
    @Transient
    private String subjectScore;
    @Transient
    private Integer orderId;
    @Transient
    private String studentName;
    @Transient
    private String examNum;
    @Transient
    private String studentCode;
    @Transient
    private Float compareExamScore;
    @Transient
    private Integer compareExamRank;
    @Transient
    private Integer compareGradeRank;
    @Transient
    private Float compareExamScoreT;
    @Transient
    private Float progressDegree;
    @Transient
    private Integer progressDegreeRankClass;
    @Transient
    private Integer progressDegreeRankGrade;
    @Transient
    private Float allScore;
    @Transient
    private Float FullScore;//满分值
    @Transient
    private EmStatRange classRange;
    @Transient
    private EmStatRange schoolRange;
    @Transient
    private Integer allRank;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getStatObjectId() {
        return statObjectId;
    }

    public void setStatObjectId(String statObjectId) {
        this.statObjectId = statObjectId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getClassRank() {
        return classRank;
    }

    public void setClassRank(Integer classRank) {
        this.classRank = classRank;
    }

    public Integer getGradeRank() {
        return gradeRank;
    }

    public void setGradeRank(Integer gradeRank) {
        this.gradeRank = gradeRank;
    }

    public Integer getEduRank() {
        return eduRank;
    }

    public void setEduRank(Integer eduRank) {
        this.eduRank = eduRank;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emStat";
    }

    public String getSubjectName() {
        if (subjectName == null) {
            ;
            subjectName = "";
        }
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Float getConScore() {
        return conScore == null ? 0f : conScore;
//		return conScore;
    }

    public void setConScore(Float conScore) {
        this.conScore = conScore;
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

    public String getConScoreRank() {
        return conScoreRank;
    }

    public void setConScoreRank(String conScoreRank) {
        this.conScoreRank = conScoreRank;
    }

    public String getSubjectScore() {
        if (subjectScore == null) {
            subjectScore = "";
        }
        return subjectScore;
    }

    public void setSubjectScore(String subjectScore) {
        this.subjectScore = subjectScore;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getExamNum() {
        return examNum;
    }

    public void setExamNum(String examNum) {
        this.examNum = examNum;
    }

    public Float getAllScore() {
        return allScore;
    }

    public void setAllScore(Float allScore) {
        this.allScore = allScore;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Float getCompareExamScore() {
        return compareExamScore;
    }

    public void setCompareExamScore(Float compareExamScore) {
        this.compareExamScore = compareExamScore;
    }

    public Integer getCompareExamRank() {
        return compareExamRank;
    }

    public void setCompareExamRank(Integer compareExamRank) {
        this.compareExamRank = compareExamRank;
    }

    public Float getCompareExamScoreT() {
        return compareExamScoreT;
    }

    public void setCompareExamScoreT(Float compareExamScoreT) {
        this.compareExamScoreT = compareExamScoreT;
    }

    public Float getProgressDegree() {
        if (progressDegree != null) {
            BigDecimal b1 = new BigDecimal(progressDegree);
            return b1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return 0.0f;
    }

    public void setProgressDegree(Float progressDegree) {
        this.progressDegree = progressDegree;
    }

    public Integer getProgressDegreeRankClass() {
        return progressDegreeRankClass;
    }

    public void setProgressDegreeRankClass(Integer progressDegreeRankClass) {
        this.progressDegreeRankClass = progressDegreeRankClass;
    }

    public Integer getProgressDegreeRankGrade() {
        return progressDegreeRankGrade;
    }

    public void setProgressDegreeRankGrade(Integer progressDegreeRankGrade) {
        this.progressDegreeRankGrade = progressDegreeRankGrade;
    }

    public Integer getAllRank() {
        return allRank;
    }

    public void setAllRank(Integer allRank) {
        this.allRank = allRank;
    }

    public Float getScoreT() {
        return scoreT == null ? 0f : scoreT;
    }

    public void setScoreT(Float scoreT) {
        this.scoreT = scoreT;
    }

    public Float getScoreZ() {
        return scoreZ == null ? 0f : scoreZ;
    }

    public void setScoreZ(Float scoreZ) {
        this.scoreZ = scoreZ;
    }

    public Float getScoreLevel() {
        return scoreLevel == null ? 0f : scoreLevel;
    }

    public void setScoreLevel(Float scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public Float getAbilityScore() {
        return abilityScore == null ? 0f : abilityScore;
    }

    public void setAbilityScore(Float abilityScore) {
        this.abilityScore = abilityScore;
    }

    public Integer getAbilityRank() {
        return abilityRank;
    }

    public void setAbilityRank(Integer abilityRank) {
        this.abilityRank = abilityRank;
    }

    public Float getFullScore() {
        return FullScore;
    }

    public void setFullScore(Float fullScore) {
        FullScore = fullScore;
    }

    public EmStatRange getClassRange() {
        return classRange;
    }

    public void setClassRange(EmStatRange classRange) {
        this.classRange = classRange;
    }

    public EmStatRange getSchoolRange() {
        return schoolRange;
    }

    public void setSchoolRange(EmStatRange schoolRange) {
        this.schoolRange = schoolRange;
    }

    public Integer getCompareGradeRank() {
        return compareGradeRank;
    }

    public void setCompareGradeRank(Integer compareGradeRank) {
        this.compareGradeRank = compareGradeRank;
    }


}
