package net.zdsoft.stutotality.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * stutotality_school_notice
 * @author
 * 开学通知
 */
@Entity
@Table(name = "stutotality_school_notice")
public class StutotalitySchoolNotice extends BaseEntity<String> {
    /**
     *
     */
    private String unitId;
    /**
     *
     */
    private String acadyear;
    /**
     *
     */
    private String semester;
    /**
     *
     */
    private String gradeId;
    /**
     *
     */
    private String notice;
    /**
     *学期总天数
     */
    private Float studyDate;
    /**
     *
     */
    private Date creationTime;
    /**
     *
     */
    private Date modifyTime;

    @Transient
    private String gradeName;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

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

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Float getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(Float studyDate) {
        this.studyDate = studyDate;
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
        return "StutotalitySchoolNotice";
    }
}
