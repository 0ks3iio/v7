package net.zdsoft.szxy.operation.inner.dto;

import lombok.Data;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.validator.ChineseLength;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author shenke
 * @since 2019/4/10 上午11:20
 */
@Data
public final class EditGroup {

    @NotBlank(message = "分组名称不能为空")
    @ChineseLength(max = 64, message = "分组名称过长")
    private String name;
    private String regionCode;

    private List<OpUser> users;
    private List<EditGroupModule> modules;
}
