package net.zdsoft.basedata.dto;

import java.io.Serializable;

/**
 * @author shenke
 * @since 2019/2/20 上午9:21
 */
public final class OpenAccountInfo implements Serializable {

    /**
     * 学生或家长ID
     *
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码，没有加密的
     */
    private String password;
    /**
     * @see net.zdsoft.basedata.enums.UserOwnerTypeCode
     */
    private Integer ownerType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }
}
