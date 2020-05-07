package net.zdsoft.bigdata.datav.render.crete.timeline;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.element.Timeline;
import net.zdsoft.echarts.enu.BottomEnum;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.RightEx;
import net.zdsoft.echarts.series.data.Tooltip;
import net.zdsoft.echarts.style.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2018/11/13 下午1:50
 */
public class TimeLineCreator implements RenderOptionCreator<EChartsRenderOption> {

    private Logger logger = LoggerFactory.getLogger(TimeLineCreator.class);

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram,
                                      List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.TIMELINE.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        List<Value> values;
        try {
            values = parse(result);
            if (values.isEmpty()) {
                throw new EntryUtils.DataException("数据不能为空");
            }
        } catch (Exception e) {
            logger.error("TimeLine result parse error", e);
            throw new EntryUtils.DataException("数据解析异常");
        }

        Timeline timeline = new Timeline();
        List<Object> datas = new ArrayList<>(values.size());
        for (Value value : values) {
            TimeLineData data = new TimeLineData();
            data.setValue(value.getValue());
            data.setName(value.getName());
            datas.add(data);
        }
        timeline.setData(datas);
        timeline.setAxisType(AxisType.category);
        timeline.setBottom(BottomEnum.middle);
        timeline.setLeft(LeftEx.create(10));
        timeline.setRight(RightEx.create(10));
        timeline.setAutoPlay(Boolean.TRUE);
        timeline.setPlayInterval(1500);

        Option option = new Option();
        option.setTimeline(timeline);
        return EChartsRenderOption.of(option);
    }

    private List<Value> parse(Result result) {
        String s = null;
        if (result.getValue() instanceof String) {
            s = (String) result.getValue();
        } else {
            s = JSONObject.toJSONString(s);
        }
        return JSONObject.parseArray(s, Value.class);
    }
}
