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
 * @author niuchao
 * @date 2019/11/14 11:17
 */
@Entity
@Table(name = "scoremanage_hw_reinstate")
public class HwReinstate extends BaseEntity<String> {
    @ColumnInfo(displayName = "单位id")
    private String unitId;
    @ColumnInfo(displayName = "学年")
    private String acadyear;
    @ColumnInfo(displayName = "学期")
    private String semester;
    @ColumnInfo(displayName = "学生id")
    private String studentId;
    @ColumnInfo(displayName = "对应原年级学年")
    private String oldAcadyear;
    @ColumnInfo(displayName = "原年级id")
    private String oldGradeId;
    @ColumnInfo(displayName = "现年级id")
    private String gradeId;
    @ColumnInfo(displayName = "原考试id")
    private String oldExamId;
    @ColumnInfo(displayName = "现考试id")
    private String examId;
    @ColumnInfo(displayName = "创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "修改时间")
    private Date modifyTime;
    @ColumnInfo(displayName = "操作人")
    private String operator;

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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getOldAcadyear() {
        return oldAcadyear;
    }

    public void setOldAcadyear(String oldAcadyear) {
        this.oldAcadyear = oldAcadyear;
    }

    public String getOldGradeId() {
        return oldGradeId;
    }

    public void setOldGradeId(String oldGradeId) {
        this.oldGradeId = oldGradeId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getOldExamId() {
        return oldExamId;
    }

    public void setOldExamId(String oldExamId) {
        this.oldExamId = oldExamId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
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

    @Override
    public String fetchCacheEntitName() {
        return "hwReinstate";
    }
}
