package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Date: 2019/03/28
 */
@Entity
@Table(name = "newdiathesis_score_type")
public class DiathesisScoreType extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private String unitId;
    private String gradeId;
    private String year;
    private String gradeCode;
    private Integer semester;
    /**
     * 1:必修 2:选修
     * 1:选考
     * 1:学生自评 2:家长评价 3:学生互评 4:导师评价 5:班主任评价
     */
    private String scoreType;
    private String examName;
    /**
     * 1:学科成绩录入 2:学业水平录入 3:综合素质录入 4:综合素质统计   5: 学生之间的互评
     */
    private String type;
    private String limitedTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    private String operator;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(String limitedTime) {
        this.limitedTime = limitedTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String fetchCacheEntitName() {
        return "newDiathesisScoreType";
    }
}
