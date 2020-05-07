package net.zdsoft.szxy.operation.usermanage.controller.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/4/15 下午4:41
 */
@Getter
@Setter
public final class FamilyVo {

    private String id;
    private String realName;
    private String unitName;
    private Date creationTime;
    private String mobilePhone;
    private String username;
}
