package net.zdsoft.stutotality.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "stutotality_check_stu")
public class StutotalityCheckStu extends BaseEntity<String> {

    private String unitId;

    private String gradeId;

    private String acadyear;

    private String semester;

    private String classId;

    private String studentId;
    /**
     * 学生成绩录入是否已完成
     */
    private int haveOver;

    private Date creationTime;

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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getHaveOver() {
        return haveOver;
    }

    public void setHaveOver(int haveOver) {
        this.haveOver = haveOver;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "StutotalityCheckStu";
    }
}
