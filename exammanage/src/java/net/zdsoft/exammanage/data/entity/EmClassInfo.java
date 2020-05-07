package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_class_info")
public class EmClassInfo extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @ColumnInfo(displayName = "考试id")
    @Column
    private String examId;
    @ColumnInfo(displayName = "班级id")
    private String classId;
    @ColumnInfo(displayName = "班级类型")
    private String classType;
    @ColumnInfo(displayName = "学校id")
    private String schoolId;

    @Transient
    private String className;
    @Transient
    private String classCode;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emClassInfo";
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

}
