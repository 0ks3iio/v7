package net.zdsoft.bigdata.datax.enums;

/**
 * Created by wangdongdong on 2019/5/5 9:52.
 */
public enum DataxJobStatusEnum {

    EXECUTING("正在执行", 0),

    COMPLETE("执行完成", 1);

    private String name;

    private Integer code;

    DataxJobStatusEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
