package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/18.
 */
@Entity
@Table(name="studoc_health_student_detail")
public class StudevelopHealthStudentDetail extends BaseEntity<String> {

    private String projectId;
    private String healthStudentId;
    private String projectValue;
    private Date creationTime;
    private Date modifyTime;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getHealthStudentId() {
        return healthStudentId;
    }

    public void setHealthStudentId(String healthStudentId) {
        this.healthStudentId = healthStudentId;
    }

    public String getProjectValue() {
        return projectValue;
    }

    public void setProjectValue(String projectValue) {
        this.projectValue = projectValue;
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

        return null;
    }
}
