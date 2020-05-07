package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author shenke
 * @since 2018/10/16 18:20
 */
@Entity
@Table(name = "bg_screen_style")
public class ScreenStyle extends BaseEntity<String> {

    private String screenId;
    private Integer width;
    private Integer height;
    private String backgroundColor;
    private String backgroundImage;
    private Integer dateTimeStyle;

    @Transient
    private String screenName;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
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
