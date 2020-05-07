package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 整体统计结果
 */
@Entity
@Table(name = "exammanage_stat_range")
public class EmStatRange extends BaseEntity<String> {


    public static final String RANGE_CLASS = "1";
    public static final String RANGE_SCHOOL = "2";
    public static final String RANGE_EDU = "3";
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String statObjectId;
    private String examId;
    private String subjectId;
    private String rangeId;//班级id 学校id 教育局id
    private String rangeType;//1:班级 2:学校 3:教育局
    private Float avgScore;
    private Integer rank;//班级在年级排名 或者 学校在教育局排名
    private Float maxScore;
    private Float minScore;
    private Integer joinNum;//参加考试人数
    private Integer statNum;//统计人数
    private Integer cheatNum;//作弊人数
    private Integer missNum;//缺考人数
    private Float avgScoreT;
    //	private String spaceNum;//各分数段人数
//	private String frontAvgscore;//各百分比平均分
//	private String backAvgscore;//各百分比平均分
    @Transient
    private String subjectName;
    @Transient
    private Map<String, Float> avgscoreMap;//各百分比  包括前、后
    @Transient
    private String rangeName;
    @Transient
    private String className;
    @Transient
    private String teacherName;
    @Transient
    private String examName;
    @Transient
    private String classNameOrder;
    @Transient
    private Date examTime;
    @Transient
    private Float progressDegree;
    @Transient
    private Integer progressDegreeRank;
    @Transient
    private Float compareExamAvgScoreT;
    @Transient
    private Float fullScore;//满分值


    private Float conScoreUp;
    private Float conScoreLow;
    private Float conNormDeviation;
    private Float conAvgScore;

    private Integer conAvgRank;
    private Float normDeviation;
    private String subType;
    private String teacherIds;
    private Float sumScore;
    private Float conSumScore;

    private Integer failedNumber;
    private Integer goodNumber;

    public Integer getFailedNumber() {
        return failedNumber;
    }

    public void setFailedNumber(Integer failedNumber) {
        this.failedNumber = failedNumber;
    }

    public Float getConScoreUp() {
        return conScoreUp;
    }

    public void setConScoreUp(Float conScoreUp) {
        this.conScoreUp = conScoreUp;
    }

    public Float getConScoreLow() {
        return conScoreLow;
    }

    public void setConScoreLow(Float conScoreLow) {
        this.conScoreLow = conScoreLow;
    }

    public Float getConNormDeviation() {
        return conNormDeviation;
    }

    public void setConNormDeviation(Float conNormDeviation) {
        this.conNormDeviation = conNormDeviation;
    }

    public Float getConAvgScore() {
        return conAvgScore;
    }

    public void setConAvgScore(Float conAvgScore) {
        this.conAvgScore = conAvgScore;
    }


    public Integer getConAvgRank() {
        return conAvgRank;
    }


    public void setConAvgRank(Integer conAvgRank) {
        this.conAvgRank = conAvgRank;
    }

    public Float getNormDeviation() {
        return normDeviation;
    }

    public void setNormDeviation(Float normDeviation) {
        this.normDeviation = normDeviation;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(String teacherIds) {
        this.teacherIds = teacherIds;
    }

    public Float getSumScore() {
        return sumScore;
    }

    public void setSumScore(Float sumScore) {
        this.sumScore = sumScore;
    }

    public Float getConSumScore() {
        return conSumScore;
    }

    public void setConSumScore(Float conSumScore) {
        this.conSumScore = conSumScore;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getRangeId() {
        return rangeId;
    }

    public void setRangeId(String rangeId) {
        this.rangeId = rangeId;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public Float getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Float avgScore) {
        this.avgScore = avgScore;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Float getMaxScore() {
        if (maxScore != null) {
            BigDecimal b1 = new BigDecimal(maxScore);
            return b1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return maxScore;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public Float getMinScore() {
        if (minScore != null) {
            BigDecimal b1 = new BigDecimal(minScore);
            return b1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return minScore;
    }

    public void setMinScore(Float minScore) {
        this.minScore = minScore;
    }

    public Integer getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(Integer joinNum) {
        this.joinNum = joinNum;
    }

    public Integer getStatNum() {
        return statNum;
    }

    public void setStatNum(Integer statNum) {
        this.statNum = statNum;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Integer getCheatNum() {
        return cheatNum;
    }

    public void setCheatNum(Integer cheatNum) {
        this.cheatNum = cheatNum;
    }

    public Integer getMissNum() {
        return missNum;
    }

    public void setMissNum(Integer missNum) {
        this.missNum = missNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassNameOrder() {
        return classNameOrder;
    }

    public void setClassNameOrder(String classNameOrder) {
        this.classNameOrder = classNameOrder;
    }

    //	public String getSpaceNum() {
//		return spaceNum;
//	}
//
//	public void setSpaceNum(String spaceNum) {
//		this.spaceNum = spaceNum;
//	}
//
//	public String getFrontAvgscore() {
//		return frontAvgscore;
//	}
//
//	public void setFrontAvgscore(String frontAvgscore) {
//		this.frontAvgscore = frontAvgscore;
//	}
//
//	public String getBackAvgscore() {
//		return backAvgscore;
//	}
//
//	public void setBackAvgscore(String backAvgscore) {
//		this.backAvgscore = backAvgscore;
//	}

    @Override
    public String fetchCacheEntitName() {
        return "emStatRange";
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Map<String, Float> getAvgscoreMap() {
        return avgscoreMap;
    }

    public void setAvgscoreMap(Map<String, Float> avgscoreMap) {
        this.avgscoreMap = avgscoreMap;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }

    public Float getAvgScoreT() {
        if (avgScoreT != null) {
            BigDecimal b1 = new BigDecimal(avgScoreT);
            avgScoreT = b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return avgScoreT;
    }

    public void setAvgScoreT(Float avgScoreT) {
        this.avgScoreT = avgScoreT;
    }

    public Float getProgressDegree() {
        return progressDegree;
    }

    public void setProgressDegree(Float progressDegree) {
        this.progressDegree = progressDegree;
    }

    public Integer getProgressDegreeRank() {
        return progressDegreeRank;
    }

    public void setProgressDegreeRank(Integer progressDegreeRank) {
        this.progressDegreeRank = progressDegreeRank;
    }

    public Float getCompareExamAvgScoreT() {
        return compareExamAvgScoreT;
    }

    public void setCompareExamAvgScoreT(Float compareExamAvgScoreT) {
        this.compareExamAvgScoreT = compareExamAvgScoreT;
    }

    public Float getFullScore() {
        return fullScore;
    }

    public void setFullScore(Float fullScore) {
        this.fullScore = fullScore;
    }

    public Integer getGoodNumber() {
        return goodNumber;
    }

    public void setGoodNumber(Integer goodNumber) {
        this.goodNumber = goodNumber;
    }

}
