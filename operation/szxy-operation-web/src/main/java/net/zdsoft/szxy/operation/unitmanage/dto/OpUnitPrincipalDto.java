package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Data;

/**
 * 新增单位负责人
 * @author yangkj
 * @since 2019/4/9
 */
@Data
public class OpUnitPrincipalDto {
    private String realName;
    private Integer type;
    private String phone;
    private String remark;
}
