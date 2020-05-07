package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_stat_line")
public class EmStatLine extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;

    private String statObjectId;
    private String statParmId;
    private String line;
    private String isDouble;//是否双上线统计
    private String doubleType;//1 单科+总分  2单科+赋分总分
    private String classId;
    private String classType;
    private String scoreNum;
    private float blance;
    private int rank;
    private String className;

    private String subjectId;
    private String type;//0:非7选3科目成绩  2：学考成绩  3：选考原始分成绩
    private String examId;

    public String getSubjectId() {
        return subjectId;
    }


    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getExamId() {
        return examId;
    }


    public void setExamId(String examId) {
        this.examId = examId;
    }


    public String getStatObjectId() {
        return statObjectId;
    }


    public void setStatObjectId(String statObjectId) {
        this.statObjectId = statObjectId;
    }


    public String getStatParmId() {
        return statParmId;
    }


    public void setStatParmId(String statParmId) {
        this.statParmId = statParmId;
    }


    public String getLine() {
        return line;
    }


    public void setLine(String line) {
        this.line = line;
    }


    public String getIsDouble() {
        return isDouble;
    }


    public void setIsDouble(String isDouble) {
        this.isDouble = isDouble;
    }


    public String getDoubleType() {
        return doubleType;
    }


    public void setDoubleType(String doubleType) {
        this.doubleType = doubleType;
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


    public String getScoreNum() {
        return scoreNum;
    }


    public void setScoreNum(String scoreNum) {
        this.scoreNum = scoreNum;
    }


    public float getBlance() {
        return blance;
    }


    public void setBlance(float blance) {
        this.blance = blance;
    }


    public int getRank() {
        return rank;
    }


    public void setRank(int rank) {
        this.rank = rank;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    @Override
    public String fetchCacheEntitName() {
        return "emStatLine";
    }
}
