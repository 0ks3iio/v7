package net.zdsoft.system.remote.dto;

import java.util.Set;

/**
 * @author shenke
 * @since 2019/3/13 上午11:10
 */
public final class UnitServerClassify {

    private String id;
    private String name;
    private Integer orderNumber;
    private Set<String> serverCodes;
    private String iconPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<String> getServerCodes() {
        return serverCodes;
    }

    public void setServerCodes(Set<String> serverCodes) {
        this.serverCodes = serverCodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
