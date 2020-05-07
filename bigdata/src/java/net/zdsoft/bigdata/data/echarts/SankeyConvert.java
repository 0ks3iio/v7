package net.zdsoft.bigdata.data.echarts;

import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.Trigger;

import java.util.Optional;

/**
 * @author shenke
 * @since 18-8-27 上午11:04
 */
public class SankeyConvert {

    public static Option sankey(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {
        JData data = createJData(result);
        Option option = new Option();
        JConverter.convert(data, option);
        triggerExpose(option, (OptionExposeSankey) oe, name);
        return option;
    }

    private static JData createJData(Result result) throws EntryUtils.DataException {
        JData data = EntryUtils.parseLinkData(result.getValue(), EntryUtils.EntryMapping.entry);
        data.setType(SeriesEnum.sankey);
        return data;
    }

    private static void triggerExpose(Option option, OptionExposeSankey optionExpose, String title) {
        boolean canNotExpose = optionExpose == null || option == null;
        if (canNotExpose) {
            if (option != null) {
                exposeTitle(option, null, title);
            }
            return;
        }
        exposeTooltip(option, optionExpose);
        exposeTitle(option, optionExpose.getExposeTitle(), title);
        option.setColor(optionExpose.getColors());
    }

    private static void exposeTooltip(Option option, OptionExposeSankey optionExpose) {
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

    private static void exposeTitle(Option option, OptionExposeTitle exposeTitle, String title) {
        if (exposeTitle != null) {
            WrapConvert.exposeTitle(option, exposeTitle, title);
        } else {
            option.title().show(false).text(title);
        }
    }
}
