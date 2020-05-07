package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.echarts.Option;

/**
 * @author shenke
 * @since 2018/9/27 15:30
 */
public class EChartsRenderOption extends RenderOption<Option> {

    private String region;

    public static EChartsRenderOption ofEmpty() {
        EChartsRenderOption eChartsRenderOption = new EChartsRenderOption();
        Option option = new Option();
        eChartsRenderOption.setOp(option);
        return eChartsRenderOption;
    }

    public static EChartsRenderOption of(Option option) {
        EChartsRenderOption eChartsRenderOption = new EChartsRenderOption();
        eChartsRenderOption.setOp(option);
        return eChartsRenderOption;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
