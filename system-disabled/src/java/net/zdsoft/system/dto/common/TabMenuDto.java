package net.zdsoft.system.dto.common;

import java.io.Serializable;

public class TabMenuDto implements Serializable {
    private static final long serialVersionUID = 3713795833422521214L;

    private String menuName;
    private String url;
    private String tabCode;

    public TabMenuDto() {

    }

    public TabMenuDto(String menuName, String url, String tabCode) {
        this.menuName = menuName;
        this.url = url;
        this.tabCode = tabCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTabCode() {
        return tabCode;
    }

    public void setTabCode(String tabCode) {
        this.tabCode = tabCode;
    }

}
