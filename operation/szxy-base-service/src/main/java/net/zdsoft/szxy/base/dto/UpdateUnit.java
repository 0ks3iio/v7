package net.zdsoft.szxy.base.dto;

import lombok.Data;

/**
 * @author shenke
 * @since 2019/3/21 下午12:37
 */
@Data
public final class UpdateUnit {

    private String unitId;
    private String unitName;
    private String regionCode;
    private String parentUnitId;
}
