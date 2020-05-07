package net.zdsoft.system.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_user_role")
public class UserRole extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    @Column(name = "userid")
    private String userId;
    @Column(name = "roleid")
    private String roleId;

    @Override
    public String fetchCacheEntitName() {
        return "userRole";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
