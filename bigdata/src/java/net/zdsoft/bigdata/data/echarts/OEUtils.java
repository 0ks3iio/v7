package net.zdsoft.bigdata.data.echarts;

import freemarker.template.Configuration;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.Target;
import net.zdsoft.echarts.enu.TopEnum;
import net.zdsoft.echarts.enu.Trigger;
import net.zdsoft.framework.config.FreemarkerConfigureEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/7/20 下午3:38
 */
@Component
public class OEUtils extends FreemarkerConfigureEx {

    @Override
    public Map<String, Object> getExtendFreemarkerVariables(Configuration freemarkerConfiguration) {
        Map<String, Object> oe = new HashMap<>(1);
        oe.put("OEUtils", this);
        return oe;
    }

    /**
     * 是否转换
     * @param enable
     * @return
     */
    public String doYesNo(Boolean enable) {
        if (Boolean.TRUE.equals(enable)) {
            return "是";
        }
        return "否";
    }

    public String doXYLocation(String location) {
        if (PositionEnum.top.equals(PositionEnum.valueOf(location))
                || PositionEnum.right.equals(PositionEnum.valueOf(location))) {
            return "是";
        }
        return "否";
    }


    public String doActive(Boolean active) {
        if (Boolean.TRUE.equals(active)) {
            return "active";
        }
        return "";
    }

    public String doLegendLocation(String position) {
        if (LeftEnum.left.name().equals(position)) {
            return "左对齐";
        }
        if (LeftEnum.right.name().equals(position)) {
            return "右对齐";
        }
        if (LeftEnum.center.name().equals(position)) {
            return "居中";
        }
        if (TopEnum.top.name().equals(position)) {
            return "顶部";
        }
        if (TopEnum.bottom.name().equals(position)) {
            return "底部";
        }
        throw new IllegalArgumentException("illeagal position:" + position);
    }

    public String doLegendOrient(String orient) {
        if (Orient.horizontal.name().equals(orient)) {
            return "水平";
        }
        if (Orient.vertical.name().equals(orient)) {
            return "垂直";
        }
        throw new IllegalArgumentException("illeagal orient:" + orient);
    }

    public String doTooltipTrigger(String trigger) {
        if (Trigger.axis.name().equals(trigger)) {
            return "是";
        }
        else {
            return "否";
        }
    }
    public String doSLabelPosition(String ps) {
        if (StringUtils.isBlank(ps)) {
            throw new IllegalArgumentException("illeagal ps: " + ps);
        }
        if (PositionEnum.inside.name().equals(ps)) {
            return "图形内部";
        }
        if (PositionEnum.right.name().equals(ps)) {
            return "图形右侧";
        }
        if (PositionEnum.left.name().equals(ps)) {
            return "图形左侧";
        }
        if (PositionEnum.top.name().equals(ps)) {
            return "图形上方";
        }
        if (PositionEnum.bottom.name().equals(ps)) {
            return "图形下方";
        }
        if (PositionEnum.insideBottom.name().equalsIgnoreCase(ps)) {
            return "图形内部下方";
        }
        if (PositionEnum.insideBottomLeft.name().equalsIgnoreCase(ps)) {
            return "图形内部左下方";
        }
        if (PositionEnum.insideBottomRight.name().equalsIgnoreCase(ps)) {
            return "图形内部右下方";
        }
        if (PositionEnum.insideTop.name().equalsIgnoreCase(ps)) {
            return "图形内部上方";
        }
        if (PositionEnum.insideTopLeft.name().equalsIgnoreCase(ps)) {
            return "图形内部左上方";
        }
        if (PositionEnum.insideTopRight.name().equalsIgnoreCase(ps)) {
            return "图形内部右上方";
        }
        if (PositionEnum.insideLeft.name().equalsIgnoreCase(ps)) {
            return "图形内部左侧";
        }
        if (PositionEnum.insideRight.name().equalsIgnoreCase(ps)) {
            return "图形内部右侧";
        }
        if ("outside".equalsIgnoreCase(ps)) {
            return "图形外部";
        }
        if ("center".equalsIgnoreCase(ps)) {
            return "图形中心";
        }
        throw new IllegalArgumentException("illeagal ps: " + ps);
    }

    public String doSeriesType(String seriesType) {
        switch (seriesType) {
            case "bar": return "柱状图";
            case "line": return "线状图";
            case "scatter": return "散点图";
            default:
                return "默认";
        }
    }

    public String doLinkTarget(String linkTarget) {
        if (StringUtils.isBlank(linkTarget)) {
            return "新标签页";
        }
        Target target = Target.blank;
        try {
            target = Target.valueOf(linkTarget.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("不合法的LinkTarget");
        }
        switch (target) {
            case blank: return "新标签页";
            case self: return "当前窗口";
            default:
                return "";
        }
    }
}
