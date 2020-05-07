package net.zdsoft.syncdata.custom.xunfei.entity;

import net.zdsoft.basedata.entity.Unit;

public class XFUnit extends Unit {
    
    private static final long serialVersionUID = 1L;
    private Integer schoolIntId;
    private String schoolIc;
    private String functionTypeCode;
    private String areaTypeCode;

    public Integer getSchoolIntId() {
        return schoolIntId;
    }

    public void setSchoolIntId(Integer schoolIntId) {
        this.schoolIntId = schoolIntId;
    }

    public String getSchoolIc() {
        return schoolIc;
    }

    public void setSchoolIc(String schoolIc) {
        this.schoolIc = schoolIc;
    }

    public String getFunctionTypeCode() {
        return functionTypeCode;
    }

    public void setFunctionTypeCode(String functionTypeCode) {
        this.functionTypeCode = functionTypeCode;
    }

    public String getAreaTypeCode() {
        return areaTypeCode;
    }

    public void setAreaTypeCode(String areaTypeCode) {
        this.areaTypeCode = areaTypeCode;
    }
    
    
}
