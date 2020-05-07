package net.zdsoft.bigdata.datav.render.crete.pie;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupLabel;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.enu.PositionEx;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.PieData;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 单值百分比环形图
 * @author shenke
 * @since 2018/10/10 14:46
 */
public class ProportionAnnularCreator extends AbstractPieCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.PROPORTION_ANNULAR.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        String originXName = null;
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        if (entryList.isEmpty()) {
            throw new EntryUtils.DataException("数据不能为空");
        }
        if (entryList.size() > 1) {
            JData.Entry entry = entryList.get(0);
            entryList = new ArrayList<>();
            entryList.add(entry);
        }
        Object value = entryList.get(0).getY();
        originXName = entryList.get(0).getX();
        if (value == null) {
            throw new EntryUtils.DataException("数据不能为空");
        }
        if (NumberUtils.isNumber(value.toString())) {
            double doubleValue = NumberUtils.toDouble(value.toString());
            if ( doubleValue > 1) {
                throw new EntryUtils.DataException("数据值不能大于1");
            }
            entryList.add(createOtherEntry(1-doubleValue));
        } else {
            throw new EntryUtils.DataException("数据值必须是数字");
        }

        Option option = createOption(entryList);

        Optional<EarlyParameter> earlyParameter = earlyParameters.stream().filter(ep -> GroupKey.label.name().equals(ep.getGroupKey())).findAny();
        GroupLabel groupLabel = null;
        if (earlyParameter.isPresent()) {
            groupLabel = NeedRenderGroupBuilder.buildGroup(earlyParameter.get().getEarlyParameters(), GroupKey.label, GroupLabel.class);
        }

        earlyRender(option, originXName, groupLabel);
        return EChartsRenderOption.of(option);
    }

    private JData.Entry createOtherEntry(Object value) {
        JData.Entry entry = new JData.Entry();
        entry.setX("其他");
        entry.setY(value);
        return entry;
    }

    private void earlyRender(Option option, String originName, GroupLabel groupLabel) {
        for (Series series : option.series()) {
            ((Pie) series).radius(new Object[]{"60%", "80%"});
            for (Object sd : series.getData()) {
                if (!Objects.equals(((PieData) sd).getName(), originName)) {
                    ((PieData) sd).itemStyle().color("transparent");
                    ((PieData) sd).label().show(Boolean.FALSE);
                } else {
                    if (groupLabel != null) {
                        ((PieData) sd).label().position(PositionEx.create("center")).formatter(groupLabel.getLabelFormatter());
                    } else {
                        ((PieData) sd).label().position(PositionEx.create("center")).formatter("{b}\n{d}%");
                    }

                }
            }
        }
    }

}
