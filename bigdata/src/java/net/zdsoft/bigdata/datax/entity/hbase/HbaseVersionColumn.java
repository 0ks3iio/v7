package net.zdsoft.bigdata.datax.entity.hbase;

/**
 * Created by wangdongdong on 2019/5/20 9:48.
 */
public class HbaseVersionColumn {

    private Integer index;

    private String value;

    public Integer getIndex() {
        return index;
    }

    public HbaseVersionColumn setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public String getValue() {
        return value;
    }

    public HbaseVersionColumn setValue(String value) {
        this.value = value;
        return this;
    }
}
