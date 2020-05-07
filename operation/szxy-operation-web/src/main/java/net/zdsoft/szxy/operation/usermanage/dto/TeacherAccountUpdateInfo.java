package net.zdsoft.szxy.operation.usermanage.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/2/18 下午2:29
 */
@Data
public final class TeacherAccountUpdateInfo {

    @NotBlank(message = "id不能为空")
    private String id;
    @NotNull(message = "排序号不能为空")
    @Min(value = 0, message = "排序号不能小于0")
    private Integer orderNumber;
    private Date expireDate;
    @Min(value = 0, message = "不合法的用户状态值")
    @Max(value = 3, message = "不合法的用户状态值")
    private Integer userState;

}
