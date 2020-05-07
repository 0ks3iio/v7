package net.zdsoft.bigdata.frame.data.oracle;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.echarts.EntryUtils.DataException;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.datasource.*;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.framework.entity.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("oracleClientService")
public class OracleClientServiceImpl implements OracleClientService,
		ApplicationContextAware {
	private static Logger logger = LoggerFactory
			.getLogger(OracleClientServiceImpl.class);

	private static DatasourceQueryChecker datasourceQueryChecker;

	@Autowired
	private DatabaseService databaseService;

	public Adapter getAdapter(String dataSourceId) {

		Database db = databaseService.findOne(dataSourceId);
		if (db == null) {
			return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
					.build();
		}
		return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
				.characterEncoding(db.getCharacterEncoding())
				.dbName(db.getDbName()).username(db.getUsername())
				.domain(db.getDomain()).password(db.getPassword())
				.port(db.getPort()).type(DatabaseType.parse(db.getType()))
				.build();
	}

	@SuppressWarnings("rawtypes")
	public List<Json> getDataListFromOracle(String dataSourceId, String sql) {
		List<Json> resultList = new ArrayList<Json>();
		try {
			Adapter adapter = getAdapter(dataSourceId);
			QueryExtractor extractor = QueryExtractor
					.extractorResultSetForJSONList();
			QueryStatement<Adapter> queryStatement = new QueryStatement<>(
					adapter, sql);
			@SuppressWarnings("unchecked")
			QueryResponse queryResponse = datasourceQueryChecker.query(
					queryStatement, extractor);
			if (queryResponse.getError() != null) {
				throw new BigDataBusinessException(queryResponse.getError());
			} else {
				return resultList = parse(queryResponse.getQueryValue());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return resultList;
		}
	}

	private List<Json> parse(Object value) throws DataException {
		try {
			if (value == null) {
				return Collections.emptyList();
			}

			String text = null;
			if (value instanceof String) {
				text = (String) value;
			} else {
				text = JSONObject.toJSONString(value);
			}

			List<Json> list;
			DefaultJSONParser parser = new DefaultJSONParser(text,
					ParserConfig.getGlobalInstance());

			JSONLexer lexer = parser.lexer;
			int token = lexer.token();
			if (token == JSONToken.NULL) {
				lexer.nextToken();
				list = null;
			} else if (token == JSONToken.EOF && lexer.isBlankInput()) {
				list = null;
			} else {
				list = new ArrayList<>();
				parser.parseArray(Json.class, list);

				parser.handleResovleTask(list);
			}
			parser.close();

			return list;
		} catch (Exception e) {
			throw new DataException("数据解析异常");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		datasourceQueryChecker = applicationContext.getBean(
				DatasourceQueryChecker.NAME, DatasourceQueryChecker.class);
	}

}
