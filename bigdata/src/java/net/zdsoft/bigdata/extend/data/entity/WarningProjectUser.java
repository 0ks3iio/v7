package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author duhuachao
 * @date 2019/06/12
 */
@Entity
@Table(name = "bg_warning_project_user")
public class WarningProjectUser extends BaseEntity<String> {

    private String projectId;

    private String usersId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Transient
    private String userName;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "warningProjectUser";
    }
}
