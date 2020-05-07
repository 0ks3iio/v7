package net.zdsoft.syncdata.custom.gansu.dto;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/19 13:54
 */
public class DepartmentDto {
    private String organId;
    private String organCode;
    private String organName;
    private String shortName;
    private String struId;
    private String parentId;
    private String struPath;
    private String struOrder;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getStruId() {
        return struId;
    }

    public void setStruId(String struId) {
        this.struId = struId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStruPath() {
        return struPath;
    }

    public void setStruPath(String struPath) {
        this.struPath = struPath;
    }

    public String getStruOrder() {
        return struOrder;
    }

    public void setStruOrder(String struOrder) {
        this.struOrder = struOrder;
    }
}
