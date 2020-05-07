package net.zdsoft.szxy.operation.unitmanage.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *  星级列表
 * @author zhanWenze
 * @since 2019年4月4日
 */
@Data
public class UnitStarLevelVo {

    private String represent;
    private Integer unitStarLevel;

    private final static List<UnitStarLevelVo> unitStarLevels = new ArrayList<>();

    public static List<UnitStarLevelVo> getUnitLevels() {
        // lazy create
        if (unitStarLevels.isEmpty()) {
            ArrayList<UnitStarLevelVo> unitLevelVos = new ArrayList<>(4);
            unitLevelVos.add(new UnitStarLevelVo("重要",1));
            unitLevelVos.add(new UnitStarLevelVo("一般",2));
            unitLevelVos.add(new UnitStarLevelVo("不重要",3));
            unitStarLevels.addAll(unitLevelVos);
        }
        return unitStarLevels;
    }

    public UnitStarLevelVo(String represent, Integer unitStarLevel) {
        this.represent = represent;
        this.unitStarLevel = unitStarLevel;
    }
}
