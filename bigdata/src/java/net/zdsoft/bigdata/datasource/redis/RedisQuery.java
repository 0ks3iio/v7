package net.zdsoft.bigdata.datasource.redis;

import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.datasource.AbstractQuery;
import net.zdsoft.bigdata.datasource.CheckResponse;
import net.zdsoft.bigdata.datasource.DataType;
import net.zdsoft.bigdata.datasource.QueryExtractor;
import net.zdsoft.bigdata.datasource.QueryStatement;
import net.zdsoft.bigdata.datasource.QueryStatementWithArgs;
import net.zdsoft.bigdata.datasource.Statement;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shenke
 * @since 2018/11/27 上午9:30
 */
@Component(RedisQuery.NAME)
public class RedisQuery extends AbstractQuery {

    public static final String NAME = "redisQuery";

    private Set<DataType> supportTypes = new HashSet<DataType>(1){{add(DatabaseType.REDIS);}};


    @Override
    protected <Q extends QueryStatement, VL> VL execute(Q queryStatement, QueryExtractor<VL> extractor) throws Throwable {
        RedisDatabaseAdapter redisDatabaseAdapter = RedisDatabaseAdapter.RedisDatabaseKeyBuilder
                .builder().buildSame((RedisDatabaseAdapter) queryStatement.getAdapter());
        String value ;
        if (queryStatement instanceof QueryStatementWithArgs) {
            value = RedisQueryUtils.doGetUseLuaScriptWithArgs(queryStatement.getQueryStatement(),
                    ((QueryStatementWithArgs) queryStatement).getArgs(), redisDatabaseAdapter);
        }
        else {
            value = RedisQueryUtils.doGetUseStandardKey(queryStatement.getQueryStatement(), redisDatabaseAdapter);
        }
        return extractor.extractData(value);
    }

    @Override
    protected Set<DataType> getSupportDatabaseType() {
        return supportTypes;
    }

    @Override
    public CheckResponse check(Statement statement) {
        Jedis jedis = null;
        try {
            RedisDatabaseAdapter adapter = (RedisDatabaseAdapter) statement.getAdapter();
            jedis = new Jedis(adapter.getDomain(), adapter.getPort());
            jedis.connect();
            return CheckResponse.ok();
        } catch (Throwable e) {
            return CheckResponse.error(e);
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
