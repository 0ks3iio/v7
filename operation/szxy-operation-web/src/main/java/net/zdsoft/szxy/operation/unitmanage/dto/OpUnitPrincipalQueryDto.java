package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *  查询单位负责人相关Dto
 * @author zhanWenze
 * @since 2019年4月3日
 */
@Getter
@Setter
public class OpUnitPrincipalQueryDto {

    String parentId;
    Integer starLevel;
    String unitHeader;
    String unitHeaderPhone;

}
