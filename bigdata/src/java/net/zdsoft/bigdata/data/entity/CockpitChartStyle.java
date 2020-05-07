package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2018/8/6 下午5:46
 */
@Entity
@Table(name = "bg_cockpit_chart_style")
public class CockpitChartStyle extends BaseEntity<String> {

    /**
     * z-index
     */
    private Integer zIndex;
    private Integer width;
    private Integer height;
    /**
     * 相对顶部的距离
     */
    private Integer top;
    /**
     * 相对左边的距离
     */
    private Integer left;
    private String cockpitChartId;
    @Enumerated(EnumType.ORDINAL)
    private BorderType borderType;
    private String backgroundColor;


    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getCockpitChartId() {
        return cockpitChartId;
    }

    public void setCockpitChartId(String cockpitChartId) {
        this.cockpitChartId = cockpitChartId;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
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

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
