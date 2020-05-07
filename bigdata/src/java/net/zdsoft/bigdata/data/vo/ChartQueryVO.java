/**
 * FileName: ChartQueryVO
 * Author:   shenke
 * Date:     2018/4/24 上午11:33
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

import java.util.List;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;
import net.zdsoft.bigdata.data.code.ChartCategory;

/**
 * @author shenke
 * @since 2018/4/24 上午11:33
 */
public class ChartQueryVO {

    private String id;
    private String name;
    private Integer chartType;

    /**
     * @see ChartCategory
     */
    @Deprecated
    private SeriesType seriesType;
    private ChartCategory chartCategory;
    private String chartTypeName;
    private List<String> tagList;

    /**
     * 报表模版缩略图
     */
    private String thumbnailPath;

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

    public Integer getChartType() {
        return chartType;
    }

    public void setChartType(Integer chartType) {
        this.chartType = chartType;
    }

    public String getChartTypeName() {
        return chartTypeName;
    }

    public void setChartTypeName(String chartTypeName) {
        this.chartTypeName = chartTypeName;
    }

    public SeriesType getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(SeriesType seriesType) {
        this.seriesType = seriesType;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public ChartCategory getChartCategory() {
        return chartCategory;
    }

    public void setChartCategory(ChartCategory chartCategory) {
        this.chartCategory = chartCategory;
    }

    public static final class Builder {
        private String id;
        private String name;
        private Integer chartType;
        private SeriesType seriesType;
        private String chartTypeName;
        private String thumbnailPath;
        private List<String> tagList;
        private ChartCategory chartCategory;

        private Builder() {
        }

        public static Builder aChartQueryVO() {
            return new Builder();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder chartType(Integer chartType) {
            this.chartType = chartType;
            return this;
        }

        public Builder seriesType(SeriesType seriesType) {
            this.seriesType = seriesType;
            return this;
        }

        public Builder chartTypeName(String chartTypeName) {
            this.chartTypeName = chartTypeName;
            return this;
        }

        public Builder tagList(List<String> tagList) {
            this.tagList = tagList;
            return this;
        }

        public Builder thumbnailPath(String thumbnailPath){
            this.thumbnailPath = thumbnailPath;
            return  this;
        }

        public Builder chartCategory(ChartCategory chartCategory) {
            this.chartCategory = chartCategory;
            return this;
        }

        public Builder but() {
            return aChartQueryVO().id(id).name(name).chartType(chartType)
                    .seriesType(seriesType).chartTypeName(chartTypeName)
                    .tagList(tagList).chartCategory(chartCategory);
        }

        public ChartQueryVO build() {
            ChartQueryVO chartQueryVO = new ChartQueryVO();
            chartQueryVO.setId(id);
            chartQueryVO.setName(name);
            chartQueryVO.setChartType(chartType);
            chartQueryVO.setSeriesType(seriesType);
            chartQueryVO.setChartTypeName(chartTypeName);
            chartQueryVO.setTagList(tagList);
            chartQueryVO.setThumbnailPath(thumbnailPath);
            chartQueryVO.setChartCategory(chartCategory);
            return chartQueryVO;
        }
    }
}
