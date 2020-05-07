package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.render.crete.custom.CustomBgInnerOption;
import net.zdsoft.bigdata.datav.render.crete.custom.CustomBgRenderOption;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/20 下午1:59
 */
public class CustomBgRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!DiagramEnum.CUSTOM_BG.getType().equals(diagram.getDiagramType())) {
            return ;
        }
        if (!NeedRenderChecker.contain(GroupKey.custom_bg_style, diagram.getDiagramType())) {
            return;
        }

        CustomBgInnerOption customBgInnerOption = NeedRenderGroupBuilder.buildGroup(parameters,
                GroupKey.custom_bg_style, CustomBgInnerOption.class);
        if (customBgInnerOption != null) {
            ((CustomBgRenderOption) ro).setOp(customBgInnerOption);
        }
    }

    @Override
    public boolean supportECharts() {
        return false;
    }

    @Override
    public boolean supportOthers() {
        return true;
    }
}
