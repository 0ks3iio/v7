package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2018/8/6 下午6:27
 */
@Table
@Entity(name = "bg_cockpit_style")
public class CockpitStyle extends BaseEntity<String> {

    private String cockpitId;
    private Integer width;
    private Integer height;
    private String backgroundColor;
    private String backgroundImage;
    private Integer dateTimeStyle;



    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getCockpitId() {
        return cockpitId;
    }

    public void setCockpitId(String cockpitId) {
        this.cockpitId = cockpitId;
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

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Integer getDateTimeStyle() {
        return dateTimeStyle;
    }

    public void setDateTimeStyle(Integer dateTimeStyle) {
        this.dateTimeStyle = dateTimeStyle;
    }
}
