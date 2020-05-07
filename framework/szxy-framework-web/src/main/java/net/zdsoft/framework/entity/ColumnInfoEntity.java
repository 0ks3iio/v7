package net.zdsoft.framework.entity;

public class ColumnInfoEntity {
    private int displayOrder;
    private String vsql;
    private String fitForSchoolType;
    private String fitForUnitClass;
    private String unitType;
    private boolean hide;
    private boolean disabled;
    private String format;
    private String mcodeId;
    private String displayName;
    private boolean nullable;
    private int maxLength;
    private int minLength;
    private int length;
    private String max;
    private String min;
    private String regex;
    private String regexTip;
    private String vtype;

    private String[] vselect;

    // -------------------附加
    private boolean readonly;

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getVsql() {
        return vsql;
    }

    public void setVsql(String vsql) {
        this.vsql = vsql;
    }

    public String getFitForSchoolType() {
        return fitForSchoolType;
    }

    public void setFitForSchoolType(String fitForSchoolType) {
        this.fitForSchoolType = fitForSchoolType;
    }

    public String getFitForUnitClass() {
        return fitForUnitClass;
    }

    public void setFitForUnitClass(String fitForUnitClass) {
        this.fitForUnitClass = fitForUnitClass;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMcodeId() {
        return mcodeId;
    }

    public void setMcodeId(String mcodeId) {
        this.mcodeId = mcodeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegexTip() {
        return regexTip;
    }

    public void setRegexTip(String regexTip) {
        this.regexTip = regexTip;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String[] getVselect() {
        return vselect;
    }

    public void setVselect(String[] vselect) {
        this.vselect = vselect;
    }

}
