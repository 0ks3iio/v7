package net.zdsoft.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_ware")
public class Ware extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 状态：正常
     */
    public static final int STATE_NORMAL = 1;

    /**
     * 定购规则：无须订购，个人需授权
     */
    public static final int ORDER_RULE_AUTHORIZE = 7;

    /**
     * 角色类型：学生
     */
    public static final int ROLE_TYPE_STUDENT = 1;
    /**
     * 角色类型：教师
     */
    public static final int ROLE_TYPE_TEACHER = 2;
    /**
     * 角色类型：家长
     */
    public static final int ROLE_TYPE_FAMILY = 3;

    /**
     * 角色类型：教育局职工
     */
    public static final int ROLE_TYPE_EMPLOYEE = 4;

    /**
     * 角色类型：管理员
     */
    public static final int ROLE_TYPE_ADMIN = 5;

    private String code;
    private String name;
    private Integer wareFee;
    private Integer state;
    private Integer serverId;
    private Integer serverTypeId;
    private Integer subscriberType;
    private Integer nums;
    private Integer orderType;
    private String unitClass;
    private String role;
    private Integer experienceMonth;
    private Integer isFee;
    private Integer teacherRule;
    private Integer studentRule;
    private Integer familyRule;
    private Integer adminRule;
    private String serverCode;
    private Integer isDeleted;
    private Integer eventSource;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWareFee() {
        return wareFee;
    }

    public void setWareFee(Integer wareFee) {
        this.wareFee = wareFee;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getServerTypeId() {
        return serverTypeId;
    }

    public void setServerTypeId(Integer serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

    public Integer getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(Integer subscriberType) {
        this.subscriberType = subscriberType;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getUnitClass() {
        return unitClass;
    }

    public void setUnitClass(String unitClass) {
        this.unitClass = unitClass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getExperienceMonth() {
        return experienceMonth;
    }

    public void setExperienceMonth(Integer experienceMonth) {
        this.experienceMonth = experienceMonth;
    }

    public Integer getIsFee() {
        return isFee;
    }

    public void setIsFee(Integer isFee) {
        this.isFee = isFee;
    }

    public Integer getTeacherRule() {
        return teacherRule;
    }

    public void setTeacherRule(Integer teacherRule) {
        this.teacherRule = teacherRule;
    }

    public Integer getStudentRule() {
        return studentRule;
    }

    public void setStudentRule(Integer studentRule) {
        this.studentRule = studentRule;
    }

    public Integer getFamilyRule() {
        return familyRule;
    }

    public void setFamilyRule(Integer familyRule) {
        this.familyRule = familyRule;
    }

    public Integer getAdminRule() {
        return adminRule;
    }

    public void setAdminRule(Integer adminRule) {
        this.adminRule = adminRule;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getEventSource() {
        return eventSource;
    }

    public void setEventSource(Integer eventSource) {
        this.eventSource = eventSource;
    }

    public static int getStateNormal() {
        return STATE_NORMAL;
    }

    @Override
    public String fetchCacheEntitName() {
        return "ware";
    }

}
