package net.zdsoft.szxy.operation.inner.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shenke
 * @since 2019/4/16 下午5:10
 */
@Data
public final class UserPasswordUpdater {

    @NotNull(message = "原密码不能为空")
    private String password;
    @NotNull(message = "新密码不能为空")
    private String newPassword1;
    @NotNull(message = "确认密码不能为空")
    private String newPassword2;
}
