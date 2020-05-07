/**
 * FileName: ChartClassification.java
 * Author:   shenke
 * Date:     2018/5/28 下午1:19
 * Descriptor:
 */
package net.zdsoft.bigdata.data.code;

import java.util.List;

/**
 * @author shenke
 * @since 2018/5/28 下午1:19
 */
public class ChartClassification extends Classification {

    /**
     * 该字段和 getName() 进行了区别，为了大屏列表过滤使用
     * @see ChartSeries#name()
     * */
    private String seriesName;
    private List<ChartCategoryClassification> chartCategoryClassifications;

    public List<ChartCategoryClassification> getChartCategoryClassifications() {
        return chartCategoryClassifications;
    }

    public void setChartCategoryClassifications(List<ChartCategoryClassification> chartCategoryClassifications) {
        this.chartCategoryClassifications = chartCategoryClassifications;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public static class ChartCategoryClassification extends Classification {

        private String seriesName;
        private String thumbnail;
        private Integer chartType;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Integer getChartType() {
            return chartType;
        }

        public void setChartType(Integer chartType) {
            this.chartType = chartType;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        @Override
        public String toString() {
            return "ChartCategoryClassification{" +
                    "thumbnail='" + thumbnail + '\'' +
                    ", name='" + name + '\'' +
                    ", order=" + order +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChartClassification{" +
                "chartCategoryClassifications=" + chartCategoryClassifications +
                ", name='" + name + '\'' +
                ", order=" + order +
                '}';
    }
}
