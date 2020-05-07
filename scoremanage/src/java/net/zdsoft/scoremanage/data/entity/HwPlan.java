package net.zdsoft.scoremanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 考试方案
 * @author niuchao
 * @date 2019/11/5 9:18
 */
@Entity
@Table(name = "scoremanage_hw_plan")
public class HwPlan extends BaseEntity<String> {

    @ColumnInfo(displayName = "单位id")
    private String unitId;
    @ColumnInfo(displayName = "学年")
    private String acadyear;
    @ColumnInfo(displayName = "学期")
    private String semester;
    @ColumnInfo(displayName = "年级id")
    private String gradeId;
    @ColumnInfo(displayName = "年级名称")
    private String gradeName;
    @ColumnInfo(displayName = "考试ID")
    private String examId;
    @ColumnInfo(displayName = "考试名称")
    private String examName;
    @ColumnInfo(displayName = "考试编号")
    private String examCode;
    @ColumnInfo(displayName = "创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "修改时间")
    private Date modifyTime;
    @ColumnInfo(displayName = "操作人")
    private String operator;
    @ColumnInfo(displayName = "是否删除")
    private Integer isDeleted;

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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String fetchCacheEntitName() {
        return "hwPlan";
    }

}
