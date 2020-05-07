/* 
 * @(#)UserPermDto.java    Created on 2017年3月7日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dto.user;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.system.entity.user.Role;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月7日 上午10:18:23 $
 */
public class UserPermDto implements Serializable {

    private static final long serialVersionUID = -6967913896561922258L;
    private String userId;// 用户id
    private String realName;// 真实姓名
    private String username;// 账号
    private List<Role> roleList;// 用户所属用户组列表

    public UserPermDto() {

    }

    public UserPermDto(User user, List<Role> roleList) {
        this.userId = user.getId();
        this.realName = user.getRealName();
        this.username = user.getUsername();
        this.roleList = roleList;
    }

    public String getRoleStr() {
        String roleStr = "";
        if (CollectionUtils.isNotEmpty(roleList)) {
            int index = 1;
            for (Role role : roleList) {
                roleStr += role.getName();
                if (index < roleList.size()) {
                    roleStr += "</br>";
                }
            }
        }
        return roleStr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

}
