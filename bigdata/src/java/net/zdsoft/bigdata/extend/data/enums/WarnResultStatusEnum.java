package net.zdsoft.bigdata.extend.data.enums;

/**
 * @author duhuachao
 * @date 2019/6/20
 */
public enum WarnResultStatusEnum {

    WARNING("预警中", 0),

    HISTORY("历史", 1),

    DISPOSE("待处置", 2);

    private String name;

    private int code;

    WarnResultStatusEnum(String name, int code) {
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
