package net.zdsoft.bigdata.datax.entity.hbase;

/**
 * Created by wangdongdong on 2019/5/20 9:40.
 */
public class HbaseRowkeyColumn {

    private Integer index;

    private String type;

    private String value;

    public Integer getIndex() {
        return index;
    }

    public HbaseRowkeyColumn setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public HbaseRowkeyColumn setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public HbaseRowkeyColumn setValue(String value) {
        this.value = value;
        return this;
    }
}
