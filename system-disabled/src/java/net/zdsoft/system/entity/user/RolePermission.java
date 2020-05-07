package net.zdsoft.system.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_role_perm")
public class RolePermission extends BaseEntity<String> {

    private static final long serialVersionUID = 1378797699610994757L;

    @Column(name = "moduleid")
    private String moduleId;

    @Column(name = "operids")
    private String operationIds;

    @Column(name = "roleid")
    private String roleId;

    @Transient
    private Integer type; // 3=role, 1=user

    @Override
    public String fetchCacheEntitName() {
        return "rolePerm";
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getOperationIds() {
        return operationIds;
    }

    public void setOperationIds(String operationIds) {
        this.operationIds = operationIds;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
