package net.zdsoft.bigdata.datasource;

/**
 * @author shenke
 * @since 2018/11/27 下午3:53
 */
public class NoopAdapter extends Adapter {

    private DataType dataType;

    private NoopAdapter(DataType dataType) {
        this.dataType = dataType;
    }

    public static NoopAdapter noopAdapter(DataType dataType) {
        return new NoopAdapter(dataType);
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }
}
