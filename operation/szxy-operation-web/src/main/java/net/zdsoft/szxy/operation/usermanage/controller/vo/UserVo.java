package net.zdsoft.szxy.operation.usermanage.controller.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/4/15 上午10:46
 */
@Getter
@Setter
public final class UserVo {

    private String id;
    private String username;
    private String realName;
    private String mobilePhone;
    private Date expireDate;
    private Integer userState;
    private boolean expire;

    private String unitName;
}
