package net.zdsoft.bigdata.datav.render.crete.scatter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.series.Scatter;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.SData;
import net.zdsoft.echarts.series.data.ScatterData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/10/10 16:34
 */
public class ScatterEffectCreator extends AbstractScatterCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.SCATTER_EFFECT.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        Option option = createOption(entryList);
        earlyRender(option, getRMap(result));
        return EChartsRenderOption.of(option);
    }

    private Map<String, Object> getRMap(Result result) {
        JSONArray array = null;
        Object value = result.getValue();
        if (!(value instanceof String)) {
            value = JSONObject.toJSONString(value);
        }
        array = JSONObject.parseArray((String) value);
        Map<String, Object> rMap = new HashMap<>();
        for (Object o : array) {
            String r = ((JSONObject) o).getString("r");
            String s = ((JSONObject) o).getString("s");
            String x = ((JSONObject) o).getString("x");
            rMap.put(s + x, r);
        }
        return rMap;
    }

    private void earlyRender(Option option, Map<String, Object> rmap) {
        for (Series series : option.series()) {
            for (SData sd : ((Scatter) series).getData()) {
                ((ScatterData) sd).symbolSize(rmap.get(series.getName() + ((ScatterData) sd).getName()));
            }
        }
    }
}
