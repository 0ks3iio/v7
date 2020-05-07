package net.zdsoft.exammanage.data.entity;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_apply_student")
public class EmEnrollStudent extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    @ColumnInfo(displayName = "单位Id", nullable = false)
    private String unitId;
    @ColumnInfo(displayName = "学校Id", nullable = false)
    private String schoolId;
    @ColumnInfo(displayName = "考试Id", nullable = false)
    private String examId;
    @ColumnInfo(displayName = "学生Id", nullable = false)
    private String studentId;
    @ColumnInfo(displayName = "班级Id", nullable = false)
    private String classId;
    @ColumnInfo(displayName = "是否通过", nullable = false)
    private String hasPass;

    private String hasGood;

    @Transient
    private Clazz clazz = new Clazz();
    @Transient
    private String schoolName;
    @Transient
    private String showPictrueUrl;
    @Transient
    private Student student = new Student();

    public String getShowPictrueUrl() {
        return showPictrueUrl;
    }

    public void setShowPictrueUrl(String showPictrueUrl) {
        this.showPictrueUrl = showPictrueUrl;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getHasPass() {
        return hasPass;
    }

    public void setHasPass(String hasPass) {
        this.hasPass = hasPass;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emEnrollStudent";
    }

    public String getHasGood() {
        return hasGood;
    }

    public void setHasGood(String hasGood) {
        this.hasGood = hasGood;
    }

}
