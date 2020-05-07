package net.zdsoft.bigdata.datasource;

import net.zdsoft.bigdata.daq.data.component.BigSqlAnalyseLogController;
import net.zdsoft.bigdata.daq.data.entity.BigSqlAnalyseLog;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/11/27 下午1:17
 */
@Lazy(false)
@Component(DatasourceQueryChecker.NAME)
public class DatasourceQueryChecker implements ApplicationContextAware {

    private List<Query> queryList;
    public static final String NAME = "datasourceQueryChecker";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Query> allQuerys = applicationContext.getBeansOfType(Query.class);
        queryList = new ArrayList<>(allQuerys.values());
    }

    private Query findQuery(DataType dataType) throws NoQueryException {
        return queryList.stream().filter(query -> query.isSupport(dataType))
                .findAny().orElseThrow(()->new NoQueryException(String.format("No Query for [%s]", dataType)));
    }

    /**
     * 这个
     * 根据给定的查询条件查找合适的Query进行查找
     * 当给定的DataType没有定义对应类型的Query实例则会抛出 {@link NoQueryException}
     * @see QueryExtractor 这个处理查询结果的回调接口十分重要
     * @param queryStatement 查询参数
     * @param extractor 查询结果处理回调函数，{@link QueryExtractor#extractData(Object)} 参数是原始查询结果
     * @param <Q>
     * @param <VL>
     * @return QueryResponse 封装了查询结果的
     * @throws NoQueryException
     */
    public <Q extends QueryStatement, VL> QueryResponse<VL> query(Q queryStatement, QueryExtractor<VL> extractor) {
        try {
            long startTime=System.currentTimeMillis();
            QueryResponse<VL> query = findQuery(queryStatement.getAdapter().getDataType()).query(queryStatement, extractor);
            long endTime = System.currentTimeMillis();
            long totalTime=endTime-startTime;

            BigSqlAnalyseLog bigSqlAnalyseLog=new BigSqlAnalyseLog();
            bigSqlAnalyseLog.setId(UuidUtils.generateUuid());
            bigSqlAnalyseLog.setBusinessName("");
            bigSqlAnalyseLog.setDbType(queryStatement.getAdapter().getDataType().toString().toUpperCase());
            bigSqlAnalyseLog.setOperationTime(new Date());
            bigSqlAnalyseLog.setSql(queryStatement.getQueryStatement());
            bigSqlAnalyseLog.setDuration(totalTime);

            BigSqlAnalyseLogController.submitBigSqlAnalyseLog(bigSqlAnalyseLog);
            return query;
        } catch (NoQueryException e) {
            return QueryResponse.error(e);
        }
    }

    /**
     * 校验数据源是否可用，目前支持jdbc、redis
     * @param statement
     * @return
     * @throws NoQueryException
     */
    public CheckResponse check(Statement statement) throws NoQueryException {
        return findQuery(statement.getAdapter().getDataType()).check(statement);
    }

    /**
     * 目前仅支持获取JdbcTemplate
     * @param statement
     * @param executor
     * @param <T>
     * @param <E>
     * @return
     * @throws Exception
     */
    public <T, E> T execute(Statement statement, DatasourceExecutor<E, T> executor) throws Exception {
        Assert.notNull(statement.getAdapter(), "Adapter not null");
        Assert.notNull(statement.getAdapter().getDataType(), "Adapter not null");
        return executor.execute(queryList.stream().filter(query -> query.isSupport(statement.getAdapter().getDataType()))
                .findAny().orElseThrow(()->new NoQueryException(String.format("No Query for [%s]", statement.getAdapter().getDataType())))
                .<E>getNativeExecutor(statement));
    }
}
