package net.zdsoft.bigdata.datasource;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2018/11/26 下午4:20
 */
@FunctionalInterface
public interface QueryExtractor<R> {

    /**
     * 不同的数据源处理对应的结果类型也不相同
     * jdbc: typeof resultTypeDependingOnDatabaseType => ResultSet
     * redis：String
     * 特别需要注意的是如果需要对ResultSet做特殊处理那么请在该方法实现中处理
     * 不要返回ResultSet对象
     * @param resultTypeDependingOnDatabaseType
     * @return
     * @throws Throwable
     */
    R extractData(Object resultTypeDependingOnDatabaseType) throws Throwable;

    /**
     * 处理ResultSet
     * @return
     */
    static QueryExtractor<List<JSONObject>> extractorResultSetForJSONList() {
        return resultTypeDependingOnDatabaseType -> {
            ResultSet rs = (ResultSet) resultTypeDependingOnDatabaseType;
            ColumnMapRowMapper rowMapper = new LowerCaseColumnMapRowMapper();
            List<JSONObject> results = new ArrayList<>();
            int rowNum = 0;
            while (rs.next()) {
                results.add(new JSONObject(rowMapper.mapRow(rs, rowNum++)));
            }
            return results;
        };
    }

    static <R> QueryExtractor<R> identity() {
        return resultTypeDependingOnDatabaseType -> (R) resultTypeDependingOnDatabaseType;
    }
}
