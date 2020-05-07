package net.zdsoft.bigdata.datav.vo;

/**
 * @author shenke
 * @since 2018/10/17 9:43
 */
public class OrderScreenVo {

    private String id;
    private String name;
    private String orderUnitsName;
    private String orderUsersName;
    private Integer orderType;
    private boolean unitAuthorization;
    private boolean userAuthorization;
    private boolean delete;

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

    public String getOrderUnitsName() {
        return orderUnitsName;
    }

    public void setOrderUnitsName(String orderUnitsName) {
        this.orderUnitsName = orderUnitsName;
    }

    public String getOrderUsersName() {
        return orderUsersName;
    }

    public void setOrderUsersName(String orderUsersName) {
        this.orderUsersName = orderUsersName;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public boolean isUnitAuthorization() {
        return unitAuthorization;
    }

    public void setUnitAuthorization(boolean unitAuthorization) {
        this.unitAuthorization = unitAuthorization;
    }

    public boolean isUserAuthorization() {
        return userAuthorization;
    }

    public void setUserAuthorization(boolean userAuthorization) {
        this.userAuthorization = userAuthorization;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
