package net.zdsoft.bigdata.datax.entity.hbase;

/**
 * Created by wangdongdong on 2019/5/20 9:45.
 */
public class HbaseColumn {

    private Integer index;

    private String name;

    private String type;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
