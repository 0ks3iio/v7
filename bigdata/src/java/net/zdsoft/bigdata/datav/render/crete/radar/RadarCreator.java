package net.zdsoft.bigdata.datav.render.crete.radar;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.element.inner.Indicator;
import net.zdsoft.echarts.series.Radar;
import net.zdsoft.echarts.series.data.RadarData;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author shenke
 * @since 2018/10/15 10:28
 */
public class RadarCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.COMMON_RADAR.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        Option option = new Option();
        LinkedHashSet<Indicator> indicators = createJIndicators(entryList);
        Map<String, Integer> indexMap = new HashMap<>(indicators.size());
        int index = 0;
        for (Indicator indicator : indicators) {
            indexMap.put(indicator.getName(), index);
            index ++;
        }

        Map<String, List<JData.Entry>> entryMap = new HashMap<>();
        for (JData.Entry entry : entryList) {
            entryMap.computeIfAbsent(entry.getName(), k->new ArrayList<>()).add(entry);
        }

        Map<String, RadarData> radarDataMap = new HashMap<>(entryMap.size());
        for (Map.Entry<String, List<JData.Entry>> dentry : entryMap.entrySet()) {
            RadarData radarData = radarDataMap.computeIfAbsent(dentry.getKey(), k-> new RadarData());
            Object[] value = new Object[indicators.size()];
            String name = null;
            for (JData.Entry entry : dentry.getValue()) {
                int count = indexMap.get(entry.getX());
                value[count] = entry.getY();
                if (name == null) {
                    name = entry.getName();
                }
            }
            radarData.name(name);
            radarData.value(value);
        }

        Radar radar = new Radar();
        radar.data(radarDataMap.values().toArray(new RadarData[0]));
        option.series().add(radar);
        option.radar().add(new net.zdsoft.echarts.coords.radar.Radar().indicator(indicators));

        return EChartsRenderOption.of(option);
    }

    private LinkedHashSet<Indicator> createJIndicators(List<JData.Entry> entries) {
        Integer max = entries.stream().map(JData.Entry::getY).filter(Objects::nonNull).map(o -> NumberUtils.toInt(o.toString()))
                .max(Comparator.comparingInt(o2 -> o2)).get();
        Set<String> names = new LinkedHashSet<>();
        for (JData.Entry entry : entries) {
            names.add(entry.getX());
        }
        LinkedHashSet<Indicator> indicators = new LinkedHashSet<>(names.size());
        for (String name : names) {
            Indicator indicator = new Indicator();
            indicator.setMax(Double.valueOf(max));
            indicator.setName(name);
            indicators.add(indicator);
        }
        return indicators;
    }
}
