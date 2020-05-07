package net.zdsoft.bigdata.datav.render.crete.map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.QueryUtils;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramElement;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.ElementEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.bigdata.datav.service.DiagramElementService;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.geo.Geo;
import net.zdsoft.echarts.element.Continuous;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.SymbolEnum;
import net.zdsoft.echarts.series.EMap;
import net.zdsoft.echarts.series.EffectScatter;
import net.zdsoft.echarts.series.Lines;
import net.zdsoft.echarts.series.Scatter;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.EMapData;
import net.zdsoft.echarts.series.data.EffectScatterData;
import net.zdsoft.echarts.series.data.SData;
import net.zdsoft.echarts.series.data.ScatterData;
import net.zdsoft.echarts.style.Emphasis;
import net.zdsoft.framework.config.Evn;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 地图涉及到多个组件的问题
 * @author shenke
 * @since 2018/10/15 11:30
 */
public class MapCreator implements RenderOptionCreator<EChartsRenderOption> {

    private final Object beanLock = new Object();
    private DiagramElementService diagramElementService;

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.MAP.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        RootValue rootValue = getRootFromDiagram(diagram);
        Option option = new Option();
        createBaseGeo(option, rootValue);


        List<DiagramElement> elements = getDiagramElementService().getElementsByDiagramId(diagram.getId());
        if (!elements.isEmpty()) {
            for (DiagramElement element : elements) {
                if (ElementEnum.HEAT.getType().equals(element.getDiagramType())) {
                    createThermalMapElement(option, element, rootValue, diagram.getScreenId());
                }
                else if (ElementEnum.LINES.getType().equals(element.getDiagramType())) {
                    createLinesElement(option, element, rootValue, diagram.getScreenId());
                }
                else if (ElementEnum.EFFECT_SCATTER.getType().equals(element.getDiagramType())) {
                    createEffectScatterElement(option, element, rootValue, diagram.getScreenId());
                }
                else if (ElementEnum.SCATTER.getType().equals(element.getDiagramType())) {
                    createScatterElement(option, element, rootValue, diagram.getScreenId());
                }
            }
        }
        EChartsRenderOption chartsRenderOption = EChartsRenderOption.of(option);
        chartsRenderOption.setRegion(rootValue.getRegion());
        return chartsRenderOption;
    }

    /**
     * 区域热力层
     */
    private void createThermalMapElement(Option option, DiagramElement element, RootValue rootValue, String screenId) throws EntryUtils.DataException {
        JData data = createDataForCreateElement(rootValue, QueryUtils.query(element, screenId));
        data.setType(SeriesEnum.map);
        data.setMapType(null);
        JConverter.convert(data, option);
        Integer max = 0;
        int index = 0;
        for (Series series : option.series()) {
            if (SeriesEnum.map.equals(series.getType())) {
                ((EMap) series).geoIndex(index);
                for (SData sd : ((EMap) series).getData()) {
                    int temp = NumberUtils.toInt(((EMapData) sd).getValue().toString());
                    max = Math.max(temp, max);
                }
                ((EMap) series).geoIndex(0);
                break;
            }
            index ++;
        }
        Continuous continuous = new Continuous();
        continuous.setSeriesIndex(index);
        continuous.setShow(Boolean.FALSE);
        continuous.max(Double.valueOf(max));
        continuous.min(0d);
        option.visualMap(continuous);
    }

    /**
     * 飞线
     */
    private void createLinesElement(Option option, DiagramElement element, RootValue rootValue, String screenId) throws EntryUtils.DataException {
        JData data = createDataForCreateElement(rootValue, QueryUtils.query(element, screenId));
        data.setType(SeriesEnum.lines);
        JConverter.convert(data, option);

        for (Series series : option.series()) {
            if (series instanceof Lines) {
                ((Lines) series).effect().show(Boolean.TRUE)
                        .symbol(SymbolEnum.arrow).symbolSize(12)
                        .trailLength(0.1d);
                ((Lines) series).lineStyle()
                        .width(3)
                        .opacity(0.7)
                        .curveness(0.3);
            }
        }
    }

    private void createEffectScatterElement(Option option, DiagramElement element, RootValue rootValue, String screenId) throws EntryUtils.DataException {
        Result result = QueryUtils.query(element, screenId);
        JData data = createDataForCreateElement(rootValue, result);
        data.setType(SeriesEnum.effectScatter);
        JConverter.convert(data, option);
        Map<String, Object> ramp = getRMap(result);
        for (Series series : option.getSeries()) {
            if (series instanceof EffectScatter) {
                for (SData sd : ((EffectScatter) series).getData()) {
                    ((EffectScatterData) sd).symbolSize(ramp.get(((EffectScatterData) sd).getName()));
                }
            }
        }
    }

    private void createScatterElement(Option option, DiagramElement element, RootValue rootValue, String screenId) throws EntryUtils.DataException {
        Result result = QueryUtils.query(element, screenId);
        JData data = createDataForCreateElement(rootValue, result);
        data.setType(SeriesEnum.scatter);
        JConverter.convert(data, option);
        Map<String, Object> ramp = getRMap(result);
        for (Series series : option.getSeries()) {
            if (series instanceof Scatter) {
                ((Scatter) series).symbol(SymbolEnum.pin).symbolSize(30);
                for (SData sd : ((Scatter) series).getData()) {
                    ((ScatterData) sd).symbolSize(ramp.get(((ScatterData) sd).getName()));
                }
            }
        }
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
            String x = ((JSONObject) o).getString("x");
            rMap.put(x, r);
        }
        return rMap;
    }


    private JData createDataForCreateElement(RootValue rootValue, Result result) throws EntryUtils.DataException {
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setCoordSys(CoordinateSystem.geo);
        data.setSelfCoordSys(false);
        data.setMapType(rootValue.getRegion());
        data.setEntryList(entryList);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);
        return data;
    }

    private void createBaseGeo(Option option, RootValue rootValue) throws EntryUtils.DataException {
        if (rootValue == null || rootValue.getRegion() == null) {
            throw new EntryUtils.DataException("数据格式异常");
        }
        Geo geo = new Geo();
        geo.parent(option);
        geo.map(rootValue.getRegion());
        geo.zoom(Double.valueOf(Optional.ofNullable(rootValue.getZoom()).orElse(1)));
        Emphasis<Geo> emphasis = new Emphasis<>();
        geo.emphasis(emphasis);
        option.geo(Collections.singletonList(geo));
    }

    private RootValue getRootFromDiagram(Diagram diagram) throws EntryUtils.DataException {
        Result result = QueryUtils.query(diagram, diagram.getScreenId());
        Object value = result.getValue();
        String text;
        if (value instanceof String) {
            text = (String) value;
        } else {
            text = JSONObject.toJSONString(value);
        }

        return JSONObject.parseObject(text, RootValue.class);
    }

    private DiagramElementService getDiagramElementService() {
        if (diagramElementService == null) {
            synchronized (beanLock) {
                if (diagramElementService == null) {
                    diagramElementService = Evn.getBean("diagramElementService");
                }
            }
        }
        return diagramElementService;
    }
}
