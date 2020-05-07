package net.zdsoft.bigdata.datax.entity.hbase;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:18.
 */
public class HbasePrimaryJobContentWriterParameter extends HbaseJobContentWriterParameter {

    private String batchSize;

    private List<String> column;

    public String getBatchSize() {
        return batchSize;
    }

    public HbasePrimaryJobContentWriterParameter setBatchSize(String batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public List<String> getColumn() {
        return column;
    }

    public HbasePrimaryJobContentWriterParameter setColumn(List<String> column) {
        this.column = column;
        return this;
    }
}
