package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupGeo;
import net.zdsoft.bigdata.datav.parameter.GroupLegend;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.coords.geo.Geo;
import net.zdsoft.echarts.enu.BorderType;
import net.zdsoft.echarts.enu.BottomEx;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.RightEx;
import net.zdsoft.echarts.enu.RoamEnum;
import net.zdsoft.echarts.enu.RoamEx;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.style.AreaColorItemStyle;
import net.zdsoft.echarts.style.Emphasis;
import net.zdsoft.echarts.style.Label;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/10/17 20:04
 */
public class GeoRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.map_common, diagram.getDiagramType())) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = ((EChartsRenderOption) ro);
        GroupGeo groupGeo = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.map_common, GroupGeo.class);
        if (groupGeo == null) {
            return;
        }
        Optional<Geo> geoOptional = eChartsRenderOption.getOp().geo().stream().findFirst();
        if (geoOptional.isPresent()) {
            Geo geo = geoOptional.get();
            AreaColorItemStyle<Geo> areaColorItemStyle = new AreaColorItemStyle<>();
            areaColorItemStyle.areaColor(groupGeo.getNormalColor());
            areaColorItemStyle.borderWidth(groupGeo.getBorderWidth());
            areaColorItemStyle.borderType(BorderType.valueOf(groupGeo.getBorderType()));
            areaColorItemStyle.borderColor(groupGeo.getBorderColor());
            geo.itemStyle(areaColorItemStyle);
            Emphasis<Geo> emphasis = new Emphasis<>();
            emphasis.itemStyle().color(groupGeo.getEmphasisColor());
            geo.emphasis(emphasis);

            Label<Geo> label = new Label<>();
            label.show(groupGeo.getMapLabelShow()).color(groupGeo.getMapLabelFontColor()).fontSize(groupGeo.getMapLabelFontSize());
            geo.label(label);
            //geo.left(LeftEx.create(2));
            //geo.top(TopEx.create(2));
            //geo.right(RightEx.create(2));
            //geo.bottom(BottomEx.create(0));
            //geo.roam(RoamEx.enable());
        }
    }
}
