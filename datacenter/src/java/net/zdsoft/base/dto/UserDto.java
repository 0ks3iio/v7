package net.zdsoft.base.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String unitId;
    private Integer ownerType;
    private String username;// 用户名
    private String realName;// 姓名
    private Integer displayOrder;
    private Integer sex;
    private String deptId;// 部门id
    private String avatarUrl;// 头像
    private String deptName;
    private String unitName;

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the unitId.
     */
    public String getUnitId() {
        return unitId;
    }

    /**
     * @param unitId
     *            The unitId to set.
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    /**
     * @return Returns the ownerType.
     */
    public Integer getOwnerType() {
        return ownerType;
    }

    /**
     * @param ownerType
     *            The ownerType to set.
     */
    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return Returns the realName.
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName
     *            The realName to set.
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return Returns the displayOrder.
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * @return Returns the sex.
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex
     *            The sex to set.
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return Returns the deptId.
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     *            The deptId to set.
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * @return Returns the avatarUrl.
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * @param avatarUrl
     *            The avatarUrl to set.
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * @return Returns the deptName.
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * @param deptName
     *            The deptName to set.
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * @return Returns the unitName.
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName
     *            The unitName to set.
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

}
