package net.zdsoft.bigdata.datax.entity.hbase;

import net.zdsoft.bigdata.datax.entity.JobContentParameter;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:18.
 */
public class HbaseJobContentReaderParameter extends JobContentParameter {

    private HbaseConfig hbaseConfig;

    private String table;

    private String mode;

    private String maxVersion;

    private List<HbaseColumn> column;

    public HbaseConfig getHbaseConfig() {
        return hbaseConfig;
    }

    public HbaseJobContentReaderParameter setHbaseConfig(HbaseConfig hbaseConfig) {
        this.hbaseConfig = hbaseConfig;
        return this;
    }

    public String getTable() {
        return table;
    }

    public HbaseJobContentReaderParameter setTable(String table) {
        this.table = table;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public HbaseJobContentReaderParameter setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public String getMaxVersion() {
        return maxVersion;
    }

    public HbaseJobContentReaderParameter setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
        return this;
    }

    public List<HbaseColumn> getColumn() {
        return column;
    }

    public HbaseJobContentReaderParameter setColumn(List<HbaseColumn> column) {
        this.column = column;
        return this;
    }
}
