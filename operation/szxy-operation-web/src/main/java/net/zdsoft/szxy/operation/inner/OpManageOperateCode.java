package net.zdsoft.szxy.operation.inner;

/**
 * @author shenke
 * @since 2019/4/9 上午9:55
 */
public final class OpManageOperateCode {

    /**
     * 新增 运营用户
     */
    public static final String OP_USER_ADD = "management-user-add";
    /**
     * 删除 运营用户
     */
    public static final String OP_USER_DELETE = "management-user-delete";
    /**
     * 编辑 运营用户
     */
    public static final String OP_USER_EDIT = "management-user-edit";
    /**
     * 状态变更 运营用户
     */
    public static final String OP_USER_STATE_CHANGE = "management-user-state-change";
    /**
     * 重置密码 运营用户
     */
    public static final String OP_USER_RESET_PASSWORD = "management-user-reset-password";
    /**
     * 权限变更 运营用户
     */
    public static final String OP_USER_AUTH = "management-user-auth";

    /**
     * 分组 新增
     */
    public static final String GROUP_ADD = "management-group-add";
    /**
     * 分组 删除、解散
     */
    public static final String GROUP_DELETE = "management-group-delete";
    /**
     * 分组 权限变更
     */
    public static final String GROUP_AUTH_CHANGE = "management-group-auth";
    /**
     * 分组 成员管理
     */
    public static final String GROUP_MEMBER_CHANGE = "management-group-user";
    /**
     * 分组 名称变更
     */
    public static final String GROUP_NAME_CHANGE = "management-group-name";

}
