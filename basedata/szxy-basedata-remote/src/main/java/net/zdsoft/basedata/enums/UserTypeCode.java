package net.zdsoft.basedata.enums;

/**
 * 用户类型
 * @author shenke
 * @since 2019/1/23 下午1:44
 */
public interface UserTypeCode {

    /**
     * 顶级单位管理员
     */
    Integer TOP_ADMIN = 0;

    /**
     * 单位管理员
     */
    Integer UNIT_ADMIN = 1;

    /**
     * 普通用户
     */
    Integer COMMON_USER = 2;
}
