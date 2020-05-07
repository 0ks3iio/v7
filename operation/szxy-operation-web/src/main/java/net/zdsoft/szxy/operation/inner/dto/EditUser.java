package net.zdsoft.szxy.operation.inner.dto;

import lombok.Data;
import net.zdsoft.szxy.operation.validator.Mobile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shenke
 * @since 2019/4/9 上午11:10
 */
@Data
public final class EditUser {

    private String id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "姓名不能为空")
    private String realName;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不合法")
    private String email;
    @NotBlank(message = "手机号不能为空")
    @Mobile
    private String phone;
    @NotNull(message = "性别不能为空")
    private Integer sex;
    private String regionCode;
}
