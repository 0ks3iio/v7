package net.zdsoft.bigdata.data.echarts;

import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.TopEnum;
import net.zdsoft.echarts.enu.Trigger;
import net.zdsoft.echarts.series.TreeMap;
import net.zdsoft.echarts.series.inner.Breadcrumb;
import net.zdsoft.echarts.style.ItemStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

/**
 * @author shenke
 * @since 18-8-27 下午2:23
 */
public class TreeMapConvert {

    public static Option treeMap(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {
        JData data = createData(result);
        Option option = new Option();
        JConverter.convert(data, option);
        option.series().stream().findFirst().ifPresent(series -> {
            ((TreeMap) series).setLeafDepth(1);
        });

        triggerExpose((OptionExposeTreeMap) oe, option, name);
        return option;
    }

    private static JData createData(Result result) throws EntryUtils.DataException {
        JData data = new JData();
        data.setType(SeriesEnum.treemap);
        data.setEntryList(EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry));
        return data;
    }

    private static void triggerExpose(OptionExposeTreeMap optionExpose, Option option, String title) {
        if (optionExpose == null ||  option == null)  {
            return;
        }

        exposeTitle(option, optionExpose.getExposeTitle(), title);
        exposeTooltip(option, optionExpose);
        exposeBreadcrumb(option, optionExpose);
        exposeCommon(option, optionExpose);
        option.setColor(optionExpose.getColors());
    }

    private static void exposeTitle(Option option, OptionExposeTitle exposeTitle, String title) {
        if (exposeTitle != null) {
            WrapConvert.exposeTitle(option, exposeTitle, title);
        } else {
            option.title().text(title).show(false);
        }
    }

    private static void exposeTooltip(Option option, OptionExposeTreeMap optionExpose) {
        Tooltip tooltip = new Tooltip().parent(option);
        tooltip.show(Optional.ofNullable(optionExpose.getShowTooltip()).orElse(Boolean.FALSE))
                .trigger(Trigger.valueOf(optionExpose.getTooltipTrigger()))
                .confine(optionExpose.getTooltipConfine())
                .formatter(optionExpose.getTooltipFormatter())
                .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getTooltipBackgroundColor(), optionExpose.getTooltipBackgroundColorTransparent()))
                .borderWidth(optionExpose.getTooltipBorderWidth())
                .borderColor(optionExpose.getTooltipBorderColor());
        option.tooltip(tooltip);
    }

    private static void exposeBreadcrumb(Option option, OptionExposeTreeMap optionExpose) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setShow(optionExpose.getShowBreadCrumb());
        if (StringUtils.isNotBlank(optionExpose.getLeft())) {
            breadcrumb.setLeft(LeftEnum.valueOf(optionExpose.getLeft()));
        }
        if (StringUtils.isNotBlank(optionExpose.getTop())) {
            breadcrumb.setTop(TopEnum.valueOf(optionExpose.getTop()));
        }
        breadcrumb.setHeight(optionExpose.getHeight());
        ItemStyle<Breadcrumb, ItemStyle> itemStyle = new ItemStyle<>();
        itemStyle.setBorderWidth(optionExpose.getBorderWidth());
        itemStyle.setBorderColor(optionExpose.getBorderColor());
        itemStyle.setColor(optionExpose.getBreadColor());
        breadcrumb.setItemStyle(itemStyle);
        option.series().stream().findFirst().ifPresent(series -> {
            ((TreeMap) series).setBreadcrumb(breadcrumb);
        });
    }

    private static void exposeCommon(Option option, OptionExposeTreeMap optionExpose) {
        option.series().stream().findFirst().ifPresent(series -> {
            ((TreeMap) series).setLeafDepth(optionExpose.getLeafDepth());
            ((TreeMap) series).setDrillDownIcon(optionExpose.getDrillDownIcon());
            if (StringUtils.isNotBlank(optionExpose.getVisibleMin())) {
                ((TreeMap) series).setVisibleMin(NumberUtils.toDouble(optionExpose.getVisibleMin(), 10d));
            }
            if (StringUtils.isNotBlank(optionExpose.getChildrenVisibleMin()))  {
                ((TreeMap) series).setChildrenVisibleMin(NumberUtils.toDouble(optionExpose.getChildrenVisibleMin()));
            }
        });
    }
}
