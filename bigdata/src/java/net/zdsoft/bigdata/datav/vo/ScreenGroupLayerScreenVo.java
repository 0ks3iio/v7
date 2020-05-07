package net.zdsoft.bigdata.datav.vo;

/**
 * @author shenke
 * @since 2018/11/14 下午2:04
 */
public class ScreenGroupLayerScreenVo {

    private String screenId;
    private String shotUrl;
    private Boolean selected;
    private String screenName;

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getShotUrl() {
        return shotUrl;
    }

    public void setShotUrl(String shotUrl) {
        this.shotUrl = shotUrl;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
