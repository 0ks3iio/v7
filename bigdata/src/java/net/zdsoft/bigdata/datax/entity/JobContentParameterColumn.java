package net.zdsoft.bigdata.datax.entity;

/**
 * Created by wangdongdong on 2019/4/26 14:31.
 */
public class JobContentParameterColumn {

    private String value;

    private String type;

    public String getValue() {
        return value;
    }

    public JobContentParameterColumn setValue(String value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public JobContentParameterColumn setType(String type) {
        this.type = type;
        return this;
    }
}
