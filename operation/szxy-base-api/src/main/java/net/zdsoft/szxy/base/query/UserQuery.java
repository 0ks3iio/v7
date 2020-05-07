package net.zdsoft.szxy.base.query;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 查询用户的动态查询条件
 * @author shenke
 * @since 2019/4/15 下午1:55
 */
@Data
public final class UserQuery extends AbstractRegionsQuery implements Serializable {

    /**
     * 单位ID 可以为空
     */
    private String unitId;
    /**
     * 用户名 可为空
     */
    private String username;
    /**
     * 姓名 可为空
     */
    private String realName;
    /**
     * 手机号可为空
     */
    private String mobilePhone;
    /**
     * 用户状态 可为空
     */
    private Integer userState;
    /**
     * 分类 可以为空
     */
    private Integer ownerType;
    /**
     * regionCode 全匹配
     */
    private String regionCode;
}
