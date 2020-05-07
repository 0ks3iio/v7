package net.zdsoft.bigdata.extend.data.enums;

/**
 * Created by wangdongdong on 2018/7/13 13:47.
 */
public enum  WarningProjectTimeEnum {

    SETTING_TIME("自定义", (short) 1),

    ALL_TIME("永久", (short) 2);

    private String name;

    private Short code;

    WarningProjectTimeEnum(String name, Short code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }

}
