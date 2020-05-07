package net.zdsoft.diathesis.data.vo;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/4/28 20:28
 */
public class DiathesisRoleUsersVo {
    private String roleId;
    private List<String> userIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
