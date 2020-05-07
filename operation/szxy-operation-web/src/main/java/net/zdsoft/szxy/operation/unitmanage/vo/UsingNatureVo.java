package net.zdsoft.szxy.operation.unitmanage.vo;

import lombok.Data;
import net.zdsoft.szxy.base.enu.UnitExtensionNature;

import java.util.ArrayList;
import java.util.List;

@Data
public class UsingNatureVo {

    private final static List<UsingNatureVo> usingNatureVo = new ArrayList<>(3);

    public static List<UsingNatureVo> getUsingNatureVo() {
        //lazy create
        if (usingNatureVo.isEmpty()) {
            synchronized (usingNatureVo) {
                if (usingNatureVo.isEmpty()) {
                    //ArrayList use arrayCopy -> one operation
                    List<UsingNatureVo> usingNatureVos = new ArrayList<>(3);
                    usingNatureVos.add(new UsingNatureVo("正式单位", UnitExtensionNature.OFFICIAL));
                    usingNatureVos.add(new UsingNatureVo("试用单位", UnitExtensionNature.TRIAL));
                    usingNatureVos.add(new UsingNatureVo("已停用单位", -1));
                    usingNatureVo.addAll(usingNatureVos);
                }
            }
        }
        return usingNatureVo;
    }

    private UsingNatureVo(String humanText, Integer usingNature) {
        this.humanText = humanText;
        this.usingNature = usingNature;
    }

    private String humanText;
    private Integer usingNature;
}
