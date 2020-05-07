package net.zdsoft.bigdata.v3.index.entity;

import java.io.Serializable;

public class HeadInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String platformName;
    private String logo;
    private String title;

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
