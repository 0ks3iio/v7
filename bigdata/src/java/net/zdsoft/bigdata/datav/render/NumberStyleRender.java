package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupNumberStyle;
import net.zdsoft.bigdata.datav.render.crete.custom.AbstractNumber;
import net.zdsoft.bigdata.datav.render.crete.custom.CustomNumberRenderOption;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/30 上午10:53
 */
public class NumberStyleRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!DiagramEnum.DYNAMIC_NUMBER.getType().equals(diagram.getDiagramType())
                && !DiagramEnum.UP_NUMBER.getType().equals(diagram.getDiagramType())) {
            return;
        }
        CustomNumberRenderOption renderOption = (CustomNumberRenderOption) ro;
        AbstractNumber abstractNumber = renderOption.getOp();

        GroupNumberStyle numberStyle = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.custom_number_style, GroupNumberStyle.class);
        if (numberStyle != null) {
            abstractNumber.setFontColor(numberStyle.getNumberFontColor());
            abstractNumber.setFontSize(numberStyle.getNumberFontSize());
        }
    }

    @Override
    public boolean supportOthers() {
        return true;
    }

    @Override
    public boolean supportECharts() {
        return false;
    }
}
