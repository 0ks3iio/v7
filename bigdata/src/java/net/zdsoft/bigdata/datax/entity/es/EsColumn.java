package net.zdsoft.bigdata.datax.entity.es;

/**
 * Created by wangdongdong on 2019/6/18 11:29.
 */
public class EsColumn {

    private String name;

    private String type;

    private String format;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
