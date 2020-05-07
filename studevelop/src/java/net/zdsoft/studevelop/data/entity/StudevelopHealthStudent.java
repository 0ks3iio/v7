package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */
@Entity
@Table(name="studoc_health_student")
public class StudevelopHealthStudent extends BaseEntity<String> {

    private String schoolId;
    private String acadyear;
    private String semester;
    private String classId;
    private String studentId;
    private Date creationTime;
    private Date modifyTime;
    @Transient
    List<StudevelopHealthStudentDetail> healthStudentDetail;

    public List<StudevelopHealthStudentDetail> getHealthStudentDetail() {
        return healthStudentDetail;
    }

    public void setHealthStudentDetail(List<StudevelopHealthStudentDetail> healthStudentDetail) {
        this.healthStudentDetail = healthStudentDetail;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
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

    @Override
    public String fetchCacheEntitName() {
        return "studevelopHealthStudent";
    }
}
