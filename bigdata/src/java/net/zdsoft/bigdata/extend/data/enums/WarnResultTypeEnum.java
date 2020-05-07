package net.zdsoft.bigdata.extend.data.enums;

/**
 * @author duhuachao
 * @date 2019/6/20
 */
public enum WarnResultTypeEnum {

    CAUTION("提醒",  1),

    TRACE("跟踪处置",  2);

    private String name;

    private int code;

    WarnResultTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }
}
