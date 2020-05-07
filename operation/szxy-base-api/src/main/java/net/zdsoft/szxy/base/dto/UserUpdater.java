package net.zdsoft.szxy.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态更新的用户信息，除ID外其他的参数都可以为空
 * @author shenke
 * @since 2019/4/15 下午2:29
 */
@Data
public final class UserUpdater implements Serializable {

    private String id;
    private Integer displayOrder;
    private Integer userState;
    /**
     * 为空则会将用户数据中过期时间更新为空
     */
    private Date expireTime;
    /**
     * true 将会忽略expireTime
     */
    private boolean ignoreExpireTime;
    private String mobilePhone;
}
