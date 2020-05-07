package net.zdsoft.szxy.base.enu;

/**
 * 子系统状态常量
 * @author shenke
 * @since 2019/3/22 下午12:58
 */
public final class ServerStatus {

    //（停用（0）、上线（1））第三方应用状态（停用（0）、上线（1）、下线（2）、未提交（3）、审核中（4）、未通过（5）)

    public static final Integer STOP = 0;

    public static final Integer ON_LINE = 1;

    public static final Integer OFF_LINE = 2;

    public static final Integer UN_COMMIT = 3;

    public static final Integer AUDIT = 4;

    public static final Integer NOT_PASS = 5;
}
