package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupTimelinePlay;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.element.Timeline;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/14 下午7:06
 */
public class TimelinePlayRender implements Render {
    /**
     * 最小的刷新间隔限制为500ms
     */
    private static final Integer MIN_INTERVAL = 500;

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.timeline_play, diagram.getDiagramType())) {
            return;
        }

        GroupTimelinePlay timelinePlay = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.timeline_play, GroupTimelinePlay.class);
        if (timelinePlay == null) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Timeline timeline = eChartsRenderOption.getOp().getTimeline();
        if (timeline != null) {
            if (!new Integer(0).equals(timelinePlay.getTimelinePlayInterval())) {
                //如果刷新间隔小于500ms则限制最小为500ms一次
                if (timelinePlay.getTimelinePlayInterval() < MIN_INTERVAL) {
                    timelinePlay.setTimelinePlayInterval(MIN_INTERVAL);
                }
                timeline.setPlayInterval(timelinePlay.getTimelinePlayInterval());
            }
            timeline.setRewind(timelinePlay.getTimelineRewind());
        }
    }
}
