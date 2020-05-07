package net.zdsoft.bigdata.datax.entity.hbase;

import net.zdsoft.bigdata.datax.entity.JobContentParameter;

/**
 * Created by wangdongdong on 2019/4/26 14:18.
 */
public class HbaseJobContentWriterParameter extends JobContentParameter {

    private HbaseConfig hbaseConfig;

    private String table;

    private String nullMode;

    public HbaseConfig getHbaseConfig() {
        return hbaseConfig;
    }

    public HbaseJobContentWriterParameter setHbaseConfig(HbaseConfig hbaseConfig) {
        this.hbaseConfig = hbaseConfig;
        return this;
    }

    public String getTable() {
        return table;
    }

    public HbaseJobContentWriterParameter setTable(String table) {
        this.table = table;
        return this;
    }

    public String getNullMode() {
        return nullMode;
    }

    public HbaseJobContentWriterParameter setNullMode(String nullMode) {
        this.nullMode = nullMode;
        return this;
    }
}
