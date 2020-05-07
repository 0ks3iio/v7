package net.zdsoft.bigdata.data.echarts;

import com.google.common.collect.Lists;
import net.zdsoft.echarts.coords.enu.NameLocation;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.TopEnum;
import net.zdsoft.echarts.enu.Trigger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 2018/7/26 上午11:28
 */
public class OptionExposeLine extends OptionExposeCartesian2dBase {


    public static OptionExposeLine getDefaultOptionExpose() {
        OptionExposeLine default_oe = new OptionExposeLine();
        default_oe.setColors(Lists.newArrayList("#1f83f5", "#d042a4", "#1ebcd3", "#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d"));
        Collections.shuffle(default_oe.getColors());
        default_oe.setSmooth(false);

        //x轴
        default_oe.setShowX(true);
        default_oe.setxTitleFontSize(12);
        default_oe.setxTitleFontBold(false);
        default_oe.setxTitleFontItalic(false);
        default_oe.setxTitleFontColor("#ccc");
        default_oe.setxTitleLocation(NameLocation.end.name());
        default_oe.setShowXLine(true);
        default_oe.setxLineColor("#ccc");
        default_oe.setShowXTick(true);
        default_oe.setxTickColor("#ccc");
        default_oe.setShowXLabel(true);
        default_oe.setxLabelColor("#ccc");
        default_oe.setShowXSplitLine(false);
        default_oe.setxSplitLineColor("#ccc");
        default_oe.setxInverse(false);
        default_oe.setxPosition(PositionEnum.bottom.name());

        default_oe.setxLabelInside(Boolean.FALSE);
        default_oe.setxLabelRotate(0);
        default_oe.setxLabelMargin(8);
        default_oe.setyLabelInside(Boolean.FALSE);
        default_oe.setyLabelRotate(0);
        default_oe.setyLabelMargin(8);

        //y轴
        default_oe.setShowY(true);
        default_oe.setyTitleFontSize(12);
        default_oe.setyTitleFontBold(false);
        default_oe.setyTitleFontItalic(false);
        default_oe.setyTitleFontColor("#ccc");
        default_oe.setyTitleLocation(NameLocation.end.name());
        default_oe.setShowYLine(true);
        default_oe.setyLineColor("#ccc");
        default_oe.setShowYTick(true);
        default_oe.setyTickColor("#ccc");
        default_oe.setShowYLabel(true);
        default_oe.setyLabelColor("#ccc");
        default_oe.setShowYSplitLine(false);
        default_oe.setySplitLineColor("#ccc");
        default_oe.setyInverse(false);
        default_oe.setyPosition(PositionEnum.left.name());

        //图例
        default_oe.setShowLegend(true);
        default_oe.setLegendOrient(Orient.horizontal.name());
        default_oe.setLegendLeft(LeftEnum.center.name());
        default_oe.setLegendTop(TopEnum.top.name());
        default_oe.setLegendTextFontColor("#ccc");
        default_oe.setLegendTextFontSize(12);
        default_oe.setLegendTextFontBold(false);
        default_oe.setLegendTextFontItalic(false);
        default_oe.setLegendBackgroundColorTransparent(0);

        //提示框
        default_oe.setShowTooltip(true);
        default_oe.setTooltipTrigger(Trigger.item.name());
        default_oe.setTooltipBackgroundColor("#323232");
        default_oe.setTooltipBackgroundColorTransparent(7);
        default_oe.setTooltipBorderWidth(0);
        default_oe.setTooltipBorderColor("#ccc");
        default_oe.setTooltipConfine(true);

        //数据标签
        default_oe.setShowSLabel(false);
        default_oe.setsLabelTextFontSize(12);
        default_oe.setsLabelTextFontBold(false);
        default_oe.setsLabelTextFontItalic(false);
        default_oe.setsLabelTextFontColor("#ccc");
        default_oe.setsLabelBackgroundColorTransparent(0);
        default_oe.setsLabelBorderColorTransparent(0);
        default_oe.setsLabelPosition(PositionEnum.inside.name());

        default_oe.setExposeTitle(OptionExposeTitle.getDefault());
        return default_oe;
    }

    private List<OptionExposeSeries> exposeSeries;

    public List<OptionExposeSeries> getExposeSeries() {
        return exposeSeries;
    }

    public void setExposeSeries(List<OptionExposeSeries> exposeSeries) {
        this.exposeSeries = exposeSeries;
    }
}
