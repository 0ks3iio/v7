package net.zdsoft.szxy.operation.unitmanage.vo;

import lombok.Data;
import net.zdsoft.szxy.base.enu.UnitTypeCode;

import java.util.ArrayList;
import java.util.List;

@Data
public class UnitTypeVo {

    private final static List<UnitTypeVo> unitTypes = new ArrayList<>(4);

    public static List<UnitTypeVo> getUnitTypes() {
        //lazy create
        if (unitTypes.isEmpty()) {
            synchronized (unitTypes) {
                if (unitTypes.isEmpty()) {
                    //ArrayList use arrayCopy -> one operation
                    List<UnitTypeVo> unitTypeVos = new ArrayList<>(4);
                    unitTypeVos.add(new UnitTypeVo("下属教育局", UnitTypeCode.UNDERLING_EDUCATION));
                    unitTypeVos.add(new UnitTypeVo("托管中小学", UnitTypeCode.SCHOOL_ASP));
                    unitTypeVos.add(new UnitTypeVo("托管幼儿园", UnitTypeCode.SCHOOL_KINDERGARTEN));
                    unitTypeVos.add(new UnitTypeVo("非教育局单位", UnitTypeCode.NO_EDUCATION));
                    unitTypes.addAll(unitTypeVos);
                }
            }
        }
        return unitTypes;
    }

    private UnitTypeVo(String humanText, Integer unitType) {
        this.humanText = humanText;
        this.unitType = unitType;
    }

    private String humanText;
    private Integer unitType;
}
