package net.zdsoft.szxy.operation.inner.controller.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/4/11 下午3:14
 */
@Getter
@Setter
public final class UserVo {

    private String id;
    private String realName;
    private String phone;
    private String username;
    private String groupNames;
    private String stateName;
    private Integer state;
    private Date creationTime;
}
