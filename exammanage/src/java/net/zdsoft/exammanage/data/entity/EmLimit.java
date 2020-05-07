package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exammanage_limit")
public class EmLimit extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String examId;
    private String unitId;
    private String subjectId;
    private String teacherId;

    @Transient
    private String[] classIds;//行政班

    @Transient
    private String subjectName;
    @Transient
    private String classNames;
    @Transient
    private Set<String> classIdTypes = new HashSet<>();

    public Set<String> getClassIdTypes() {
        return classIdTypes;
    }

    public void setClassIdTypes(Set<String> classIdTypes) {
        this.classIdTypes = classIdTypes;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emLimit";
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String[] getClassIds() {
        return classIds;
    }

    public void setClassIds(String[] classIds) {
        this.classIds = classIds;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassNames() {
        return classNames;
    }

    public void setClassNames(String classNames) {
        this.classNames = classNames;
    }

}
