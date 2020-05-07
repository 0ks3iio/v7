package net.zdsoft.bigdata.datax.enums;

/**
 * Created by wangdongdong on 2019/5/5 9:24.
 */
public enum  DataxJobResultEnum {

    SUCCESS("成功", 1),

    FAILED("失败", 2);

    private String name;

    private Integer code;

    DataxJobResultEnum(String name, Integer code) {
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
