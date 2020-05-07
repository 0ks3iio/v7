package net.zdsoft.bigdata.data.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shenke
 * @since 2018/8/7 上午9:11
 */
public class DashboardVo {

    private String id;
    @NotEmpty(message = "大屏名称不能为空")
    private String name;
    @NotNull(message = "大屏宽度不能为空")
    @Min(message = "大屏宽度不能为0", value = 1L)
    private Integer width;
    @NotNull(message = "大屏高度不能为空")
    @Min(message = "大屏高度不能为0", value = 1L)
    private Integer height;
    @NotEmpty(message = "背景颜色不能为空")
    private String backgroundColor;
    private String titleBackgroundColor;
    private Integer dateTimeStyle;
    private String screenshot;

    private List<DashboardCockpitChartVo> dashboardCockpitChartVos;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<DashboardCockpitChartVo> getDashboardCockpitChartVos() {
        return dashboardCockpitChartVos;
    }

    public void setDashboardCockpitChartVos(List<DashboardCockpitChartVo> dashboardCockpitChartVos) {
        this.dashboardCockpitChartVos = dashboardCockpitChartVos;
    }

    public String getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    public void setTitleBackgroundColor(String titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public Integer getDateTimeStyle() {
        return dateTimeStyle;
    }

    public void setDateTimeStyle(Integer dateTimeStyle) {
        this.dateTimeStyle = dateTimeStyle;
    }
}
