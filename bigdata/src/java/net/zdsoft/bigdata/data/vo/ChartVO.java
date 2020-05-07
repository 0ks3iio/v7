/**
 * FileName: ChartVO
 * Author:   shenke
 * Date:     2018/4/18 上午11:04
 * Descriptor: 基本图表页面展示数据使用
 */
package net.zdsoft.bigdata.data.vo;

import java.util.List;

/**
 * 将VO和Entity分开，避免自定义的参数污染Entity对象
 * @author shenke
 * @since 2018/4/18 上午11:04
 */
public class ChartVO {

    private String id;
    private String name;
    /** 列表展示时用于控制缩略图 */
    //private String chartTypeName;
    //private Integer type;
    /** 标签名称 */
    private List<String> tagList;
    /** 订阅人和单位集合 */
    private List<String> subscribes;

    /**
     * 报表模版缩略图
     */
    private String thumbnailPath;

    public String getId() {
        return id;
    }

    public ChartVO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChartVO setName(String name) {
        this.name = name;
        return this;
    }

    //public String getChartTypeName() {
    //    return chartTypeName;
    //}
    //
    //public ChartVO setChartTypeName(String chartTypeName) {
    //    this.chartTypeName = chartTypeName;
    //    return this;
    //}


    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public ChartVO setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
        return this;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public List<String> getSubscribes() {
        return subscribes;
    }

    public ChartVO setSubscribes(List<String> subscribes) {
        this.subscribes = subscribes;
        return this;
    }

    public ChartVO setTagList(List<String> tagList) {
        this.tagList = tagList;


        return this;
    }

    //@Override
    //public String toString() {
    //    return "ChartVO{" +
    //            "id='" + id + '\'' +
    //            ", name='" + name + '\'' +
    //            ", chartTypeName='" + chartTypeName + '\'' +
    //            '}';
    //}
}
