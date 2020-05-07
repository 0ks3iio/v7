package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.zdsoft.szxy.base.entity.School;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.entity.UnitExtension;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/7 下午5:08
 */
@Getter
@Setter
@ToString
public final class UnitAddDto {
    private String username;
    private School school;
    private UnitExtension unitExtension;
    private Unit unit;
    private String serverExtensions;
    private String opUnitPrincipalDtos;
}
