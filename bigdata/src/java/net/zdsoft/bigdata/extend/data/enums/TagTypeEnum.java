package net.zdsoft.bigdata.extend.data.enums;

/**
 * Created by wangdongdong on 2018/7/9 15:01.
 */
public enum TagTypeEnum {

    TYPE("类别", (short) 1),

    TAG("标签", (short) 2);

    private String name;

    private Short code;

    TagTypeEnum(String name, Short code) {
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
