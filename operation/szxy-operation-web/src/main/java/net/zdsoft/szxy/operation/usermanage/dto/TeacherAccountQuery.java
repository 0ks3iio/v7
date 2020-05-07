package net.zdsoft.szxy.operation.usermanage.dto;

import lombok.Data;

/**
 * 教师账号信息查询条件
 * @author shenke
 * @since 2019/1/30 下午8:56
 */
@Data
public final class TeacherAccountQuery {

    private Integer userState;
    private String username;
    private String mobilePhone;
    private String realName;
}
