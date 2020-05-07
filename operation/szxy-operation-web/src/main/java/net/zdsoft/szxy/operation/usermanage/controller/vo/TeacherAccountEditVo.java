package net.zdsoft.szxy.operation.usermanage.controller.vo;

import lombok.Data;

/**
 * @author shenke
 * @since 2019/2/18 上午10:51
 */
@Data
public final class TeacherAccountEditVo {

    private String id;
    private Integer orderNumber;
    private String username;
    private String creationTime;
    private String expireTime;
    private Integer userState;
    private String password;
}
