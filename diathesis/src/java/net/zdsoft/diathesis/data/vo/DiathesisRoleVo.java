package net.zdsoft.diathesis.data.vo;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/4/23 15:03
 */
public class DiathesisRoleVo {
    private String roleId;
    private String roleName;
    private String roleCode;
    private List<DiathesisRoleUserVo> user;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<DiathesisRoleUserVo> getUser() {
        return user;
    }

    public void setUser(List<DiathesisRoleUserVo> user) {
        this.user = user;
    }
}
