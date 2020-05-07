package net.zdsoft.bigdata.data.vo;

import javax.validation.constraints.NotBlank;

/**
 * @author shenke
 * @since 2018/8/8 上午11:22
 */
public class DashboardTextChartVo {

    private String id;
    @NotBlank(message = "标题不能为空")
    private String title;

    private DashboardTextChartStyleVo dashboardTextChartStyleVo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DashboardTextChartStyleVo getDashboardTextChartStyleVo() {
        return dashboardTextChartStyleVo;
    }

    public void setDashboardTextChartStyleVo(DashboardTextChartStyleVo dashboardTextChartStyleVo) {
        this.dashboardTextChartStyleVo = dashboardTextChartStyleVo;
    }
}
