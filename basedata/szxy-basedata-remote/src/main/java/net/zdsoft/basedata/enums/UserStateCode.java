package net.zdsoft.basedata.enums;

/**
 * 用户状态常量
 * 按照运营中心讨论的用户状态定义，先划分为四种状态
 * 未审核、正常、锁定、注销
 * 原有的用户状态按照base_mcode_detail 'DM-YHZT' 划分的
 * @author shenke
 * @since 2019/1/23 下午1:37
 */
public interface UserStateCode {

    /**
     * 未审核
     */
    Integer UN_CHECKED = 0;
    /**
     * 正常
     */
    Integer NORMAL = 1;
    /**
     * 注销（）
     */
    Integer LOGOUT = 3;
    /**
     * 锁定
     */
    Integer LOCK = 2;
}
