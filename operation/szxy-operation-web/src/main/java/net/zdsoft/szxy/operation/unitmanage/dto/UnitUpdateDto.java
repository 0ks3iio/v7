package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Getter;
import lombok.Setter;
import net.zdsoft.szxy.base.entity.School;
import net.zdsoft.szxy.base.entity.Unit;

/**
 * @author shenke
 * @since 2019/3/11 上午10:53
 */
@Getter
@Setter
public final class UnitUpdateDto {

    private Unit unit;
    private School school;
}
