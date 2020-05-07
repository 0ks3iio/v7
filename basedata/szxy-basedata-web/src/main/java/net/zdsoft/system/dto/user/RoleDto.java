/* 
 * @(#)RoleDto.java    Created on 2017年3月7日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dto.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.system.entity.user.Role;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月7日 下午2:38:29 $
 */
public class RoleDto implements Serializable {

    private static final long serialVersionUID = -6520019271531571872L;
    private String roleId;// 用户组id
    private String name;// 用户组名称
    private Date modifyTime;// 修改时间
    private List<User> userList;// 用户组成员列表

    public RoleDto() {

    }

    public RoleDto(Role role, List<User> userList) {
        this.roleId = role.getId();
        this.name = role.getName();
        this.modifyTime = role.getModifyTime();
        this.userList = userList;
    }

    public String getModifyTimeStr() {
        if (null != modifyTime) {
            return DateUtils.date2StringByDay(modifyTime);
        }
        return "";
    }

    public String getUserStr() {
        String userStr = "";
        if (CollectionUtils.isNotEmpty(userList)) {
            int index = 1;
            for (User user : userList) {
                userStr += user.getRealName() + "&nbsp;&nbsp;&nbsp;&nbsp;";
                if (index % 3 == 0 && index < userList.size()) {
                    userStr += "</br>";
                }
                index++;
            }
        }
        return userStr;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
