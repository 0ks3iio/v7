package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupTimelineStyle;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.element.Timeline;
import net.zdsoft.echarts.element.inner.CheckpointStyle;
import net.zdsoft.echarts.element.inner.ControlStyle;
import net.zdsoft.echarts.enu.Align;
import net.zdsoft.echarts.enu.BottomEx;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.LineType;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.RightEnum;
import net.zdsoft.echarts.enu.SymbolEnum;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.enu.VerticalAlign;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.echarts.style.Label;
import net.zdsoft.echarts.style.LineStyle;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/13 下午4:34
 */
public class TimelieStyleRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.timeline_style, diagram.getDiagramType())) {
            return ;
        }

        GroupTimelineStyle timelineStyle = NeedRenderGroupBuilder.buildGroup(parameters,
                GroupKey.timeline_style, GroupTimelineStyle.class);
        if (timelineStyle == null) {
            return ;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Timeline timeline = eChartsRenderOption.getOp().getTimeline();
        if (timeline != null) {
            LineStyle lineStyle = new LineStyle();
            lineStyle.setWidth(timelineStyle.getTimelineLineWidth());
            lineStyle.setColor(timelineStyle.getTimelineLineColor());
            try {
                lineStyle.setType(LineType.valueOf(timelineStyle.getTimelineLineType()));
            } catch (IllegalArgumentException e) {
                //ignore
            }
            timeline.setLineStyle(lineStyle);

            ItemStyle itemStyle = new ItemStyle();
            itemStyle.setColor(timelineStyle.getTimelinePointColor());
            timeline.setItemStyle(itemStyle);
            timeline.setSymbolSize(timelineStyle.getTimelinePointSize());

            CheckpointStyle checkpointStyle = new CheckpointStyle();
            checkpointStyle.setSymbolSize(timelineStyle.getTimelinePointSize());
            timeline.setCheckpointStyle(checkpointStyle);
            timeline.setSymbol(SymbolEnum.circle);

            Label label = timeline.getLabel();
            if (label == null) {
                label = new Label();
                timeline.setLabel(label);
            }
            label.setColor(timelineStyle.getTimelineTextColor());

            ControlStyle controlStyle = new ControlStyle();
            controlStyle.setColor(timelineStyle.getTimelineControlColor());
            controlStyle.setBorderColor(timelineStyle.getTimelineControlColor());
            timeline.setControlStyle(controlStyle);

            try {
                timeline.setOrient(Orient.valueOf(timelineStyle.getTimelineOrient()));
                if (Orient.vertical.equals(timeline.getOrient())) {
                    timeline.setBottom(BottomEx.create(10));
                    timeline.setTop(TopEx.create(10));
                    timeline.setLeft(LeftEnum.center);
                    timeline.setRight(RightEnum.center);
                    //label.rotate(90);
                    label.verticalAlign(VerticalAlign.middle);
                    label.align(Align.center);
                    label.padding(10);
                }
            } catch (IllegalArgumentException e) {
                //
            }
        }
    }
}
