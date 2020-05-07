package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计参数
 */
@Entity
@Table(name = "exammanage_stat_parm")
public class EmStatParm extends BaseEntity<String> {

    public static final String SPACE_PASSIVE = "1";//统一间距
    public static final String SPACE_AUTO = "2";//自定义分段
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String examId;
    private String subjectId;
    private String statObjectId;//统计对象
    private String isCheat;//作弊是否包括
    private String isMiss;//缺考是否包括
    private String isZero;//0分是否包括
    private String isOnlystat;//是否只统计统分的人
    private String isStat;//是否已经统计或者锁

    private String sourseType;//学生来源  DM-XSLY

    private String statSpaceType;//分数段间距类型  1:统一间距，2:自定义分段
    // 1:统一间距，
    private Float spaceScore;//间距
    private Float lowScore;
    private Float upScore;
    @Transient
    private String statRankType;
    @Transient
    private Integer lowRank;
    @Transient
    private Integer upRank;
    @Transient
    private Integer spaceRank;

    //前百分比
    private String rankStatFront;//是否有统计前百分比
    //后百分比
    private String rankStatBack;//是否有统计后百分比

    private String needRankStat;//是否需要名次段统计

    private String needLineStat;//是否需要上线统计
    private String lineSpaces;//上线设置

    private String needGoodLine;//是否需要优秀人数统计
    private Float goodLine;//优秀分数线
    private String joinSum;//是否计入总分统计

    private Float failedLine;

    @Transient
    private List<EmSpaceItem> emSpaceItemList1 = new ArrayList<EmSpaceItem>();//间距
    @Transient
    private List<EmSpaceItem> emSpaceItemList2 = new ArrayList<EmSpaceItem>();//前百分比
    @Transient
    private List<EmSpaceItem> emSpaceItemList3 = new ArrayList<EmSpaceItem>();//后百分比
    @Transient
    private List<EmSpaceItem> emSpaceItemList9 = new ArrayList<EmSpaceItem>();//名次段


    public String getStatRankType() {
        return statRankType;
    }

    public void setStatRankType(String statRankType) {
        this.statRankType = statRankType;
    }

    public Integer getLowRank() {
        return lowRank;
    }

    public void setLowRank(Integer lowRank) {
        this.lowRank = lowRank;
    }

    public Integer getUpRank() {
        return upRank;
    }

    public void setUpRank(Integer upRank) {
        this.upRank = upRank;
    }

    public Integer getSpaceRank() {
        return spaceRank;
    }

    public void setSpaceRank(Integer spaceRank) {
        this.spaceRank = spaceRank;
    }

    public Float getFailedLine() {
        if (failedLine == null) {
            return 0f;
        }
        return failedLine;
    }

    public void setFailedLine(Float failedLine) {
        this.failedLine = failedLine;
    }

    public String getNeedGoodLine() {
        return needGoodLine;
    }

    public void setNeedGoodLine(String needGoodLine) {
        this.needGoodLine = needGoodLine;
    }

    public Float getGoodLine() {
        if (goodLine == null) {
            return 0f;
        }
        return goodLine;
    }

    public void setGoodLine(Float goodLine) {
        this.goodLine = goodLine;
    }

    public String getJoinSum() {
        return joinSum;
    }

    public void setJoinSum(String joinSum) {
        this.joinSum = joinSum;
    }

    public String getNeedLineStat() {
        return needLineStat;
    }

    public void setNeedLineStat(String needLineStat) {
        this.needLineStat = needLineStat;
    }

    public String getLineSpaces() {
        return lineSpaces;
    }

    public void setLineSpaces(String lineSpaces) {
        this.lineSpaces = lineSpaces;
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

    public String getStatObjectId() {
        return statObjectId;
    }

    public void setStatObjectId(String statObjectId) {
        this.statObjectId = statObjectId;
    }

    public String getIsCheat() {
        return isCheat;
    }

    public void setIsCheat(String isCheat) {
        this.isCheat = isCheat;
    }

    public String getIsMiss() {
        return isMiss;
    }

    public void setIsMiss(String isMiss) {
        this.isMiss = isMiss;
    }

    public String getIsZero() {
        return isZero;
    }

    public void setIsZero(String isZero) {
        this.isZero = isZero;
    }

    public String getIsStat() {
        return isStat;
    }

    public void setIsStat(String isStat) {
        this.isStat = isStat;
    }

    public String getSourseType() {
        return sourseType;
    }

    public void setSourseType(String sourseType) {
        this.sourseType = sourseType;
    }

    public String getStatSpaceType() {
        return statSpaceType;
    }

    public void setStatSpaceType(String statSpaceType) {
        this.statSpaceType = statSpaceType;
    }

    public Float getSpaceScore() {
        return spaceScore;
    }

    public void setSpaceScore(Float spaceScore) {
        this.spaceScore = spaceScore;
    }

    public Float getLowScore() {
        return lowScore;
    }

    public void setLowScore(Float lowScore) {
        this.lowScore = lowScore;
    }

    public Float getUpScore() {
        return upScore;
    }

    public void setUpScore(Float upScore) {
        this.upScore = upScore;
    }

    public String getRankStatFront() {
        return rankStatFront;
    }

    public void setRankStatFront(String rankStatFront) {
        this.rankStatFront = rankStatFront;
    }

    public String getRankStatBack() {
        return rankStatBack;
    }

    public void setRankStatBack(String rankStatBack) {
        this.rankStatBack = rankStatBack;
    }

    public String getIsOnlystat() {
        return isOnlystat;
    }

    public void setIsOnlystat(String isOnlystat) {
        this.isOnlystat = isOnlystat;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emStatParm";
    }

    public List<EmSpaceItem> getEmSpaceItemList1() {
        return emSpaceItemList1;
    }

    public void setEmSpaceItemList1(List<EmSpaceItem> emSpaceItemList1) {
        this.emSpaceItemList1 = emSpaceItemList1;
    }

    public List<EmSpaceItem> getEmSpaceItemList2() {
        return emSpaceItemList2;
    }

    public void setEmSpaceItemList2(List<EmSpaceItem> emSpaceItemList2) {
        this.emSpaceItemList2 = emSpaceItemList2;
    }

    public List<EmSpaceItem> getEmSpaceItemList3() {
        return emSpaceItemList3;
    }

    public void setEmSpaceItemList3(List<EmSpaceItem> emSpaceItemList3) {
        this.emSpaceItemList3 = emSpaceItemList3;
    }

    public List<EmSpaceItem> getEmSpaceItemList9() {
        return emSpaceItemList9;
    }

    public void setEmSpaceItemList9(List<EmSpaceItem> emSpaceItemList9) {
        this.emSpaceItemList9 = emSpaceItemList9;
    }

    public String getNeedRankStat() {
        return needRankStat;
    }

    public void setNeedRankStat(String needRankStat) {
        this.needRankStat = needRankStat;
    }

}
