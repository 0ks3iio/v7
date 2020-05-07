package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shenke
 * @since 2019/3/4 下午5:27
 */
@Getter
@Setter
public final class UnitQuery {

    private String parentId;
    private Integer unitType;
    private Integer usingNature;
}
