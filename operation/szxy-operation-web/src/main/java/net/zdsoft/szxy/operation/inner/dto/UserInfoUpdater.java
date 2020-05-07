package net.zdsoft.szxy.operation.inner.dto;

import lombok.Data;
import net.zdsoft.szxy.operation.validator.ChineseLength;
import net.zdsoft.szxy.operation.validator.Mobile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shenke
 * @since 2019/4/16 下午5:06
 */
@Data
public final class UserInfoUpdater {

    @NotBlank(message = "姓名不能为空")
    @ChineseLength(max = 32, message = "姓名长度过长")
    private String realName;
    @NotNull(message = "性别不能为空")
    private Integer sex;
    @NotNull(message = "手机号不能为空")
    @Mobile
    private String mobilePhone;
    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
