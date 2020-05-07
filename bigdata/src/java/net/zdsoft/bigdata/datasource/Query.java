package net.zdsoft.bigdata.datasource;

/**
 * 通用数据查询接口
 * 所有数据源查询都通过该接口查询
 * @author shenke
 * @since 2018/11/26 下午1:46
 */
public interface Query {

    /**
     * 可以自定义处理结果集,
     * @param queryStatement
     * @param extractor
     * @param <Q>
     * @return
     */
    <Q extends QueryStatement, VL> QueryResponse<VL> query(Q queryStatement, QueryExtractor<VL> extractor);

    /**
     * 是否支持
     * @param dataType
     * @return
     */
    boolean isSupport(DataType dataType);

    /**
     * 检查数据源是否可用
     * @param statement
     * @return
     */
    default CheckResponse check(Statement statement) {
        throw new RuntimeException(String.format("Not support check for data type %s", statement.getAdapter().getDataType().getType()));
    }

    default <T> T getNativeExecutor(Statement statement) throws Exception {
        throw new IllegalArgumentException("Not support Get");
    }
}