package net.zdsoft.szxy.operation.unitmanage.vo;

import lombok.Data;
import net.zdsoft.szxy.base.enu.UnitPrincipalType;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanWenze
 * @since 2019年4月11日
 * 负责人类型列表
 */
@Data
public class UnitPrincipalTypeVo {

    private String represent;
    private Integer unitPrincipalType;

    private final static List<UnitPrincipalTypeVo> unitPrincipalTypeVos = new ArrayList<>(3);

    public static List<UnitPrincipalTypeVo> getUnitPrincipalTypeVos() {
        // lazy create
        if (unitPrincipalTypeVos.isEmpty()) {
            List<UnitPrincipalTypeVo> unitPrincipalTypeVoList = new ArrayList<>(3);
            unitPrincipalTypeVoList.add(new UnitPrincipalTypeVo("负责人", UnitPrincipalType.PRINCIPAL));
            unitPrincipalTypeVoList.add(new UnitPrincipalTypeVo("销售", UnitPrincipalType.MARKET));
            unitPrincipalTypeVoList.add(new UnitPrincipalTypeVo("用户", UnitPrincipalType.USERS));
            unitPrincipalTypeVos.addAll(unitPrincipalTypeVoList);
        }
        return unitPrincipalTypeVos;
    }
    public UnitPrincipalTypeVo(String represent, Integer unitPrincipalType) {
        this.represent = represent;
        this.unitPrincipalType = unitPrincipalType;
    }
}
