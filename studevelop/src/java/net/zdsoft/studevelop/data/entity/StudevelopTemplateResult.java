package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

/**
 * Created by luf on 2018/12/14.
 */
@Entity
@Table(name="studevelop_template_result")
public class StudevelopTemplateResult extends BaseEntity<String> {
    private String templateItemId;
    private String templateOptionId;
    private String result;

    private String templateId;
    private String studentId;
    private Date creationTime;
    private Date modifyTime;

    private String subjectId;
    private String categoryId;


    private String acadyear;
    private String semester;
    @Transient
    private String objectType;

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTemplateItemId() {
        return templateItemId;
    }

    public void setTemplateItemId(String templateItemId) {
        this.templateItemId = templateItemId;
    }

    public String getTemplateOptionId() {
        return templateOptionId;
    }

    public void setTemplateOptionId(String templateOptionId) {
        this.templateOptionId = templateOptionId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@Override
    public String fetchCacheEntitName() {
        return "studevelopTemplateResult";
    }
}
