package net.zdsoft.bigdata.frame.data.hive;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.echarts.EntryUtils.DataException;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.datasource.*;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.framework.entity.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("hiveClientService")
public class HiveClientlientServiceImpl implements HiveClientService,
		ApplicationContextAware {
	private static Logger logger = LoggerFactory
			.getLogger(HiveClientlientServiceImpl.class);

	private static DatasourceQueryChecker datasourceQueryChecker;

	private static Database database;

	@Autowired
	private OptionService optionService;

	public Adapter getAdapter(String dbName) {
		OptionDto hiveDto = optionService.getAllOptionParam("hive");
		if (hiveDto == null || hiveDto.getStatus() == 0) {
			return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
					.build();
		}
		initDatabase(hiveDto, dbName);
		return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
				.characterEncoding(database.getCharacterEncoding())
				.dbName(database.getDbName()).username(database.getUsername())
				.domain(database.getDomain()).password(database.getPassword())
				.port(database.getPort())
				.type(DatabaseType.parse(database.getType())).build();
	}

	private void initDatabase(OptionDto mysqlDto, String dbName) {
		database = new Database();
		database.setPort(Integer.valueOf(mysqlDto.getFrameParamMap()
				.get("port")));
		database.setDomain(mysqlDto.getFrameParamMap().get("domain"));
		database.setDbName(dbName);
		database.setUsername(mysqlDto.getFrameParamMap().get("user"));
		database.setPassword(mysqlDto.getFrameParamMap().get("password"));
		database.setType(DatabaseType.HADOOP_HIVE.getType());
	}

	@SuppressWarnings("rawtypes")
	public List<Json> getDataListFromHive(String database, String sql,
			List<Json> paramList, List<Json> resultFieldList) {
		List<Json> resultList = new ArrayList<Json>();
		try {
			Adapter adapter = getAdapter(database);
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

	public boolean execSql(String database, String sql) {
		boolean execResult = false;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Adapter adapter = getAdapter(database);
			Statement<Adapter> statement = new Statement<>(adapter);
			JdbcTemplate template = datasourceQueryChecker.execute(statement,
					DatasourceExecutor.jdbcTemplate());
			conn = template.getDataSource().getConnection();
			ps = conn.prepareStatement(sql);
			ps.execute();
			return true;
		} catch (Exception e) {
			logger.error("数据库异常", e);
			return execResult;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("数据库PreparedStatement关闭异常", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("数据库Connection关闭异常", e);
				}
			}
		}
	}

	public void execSqls(String database, List<String> sqls) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Adapter adapter = getAdapter(database);
			Statement<Adapter> statement = new Statement<>(adapter);
			JdbcTemplate template = datasourceQueryChecker.execute(statement,
					DatasourceExecutor.jdbcTemplate());
			conn = template.getDataSource().getConnection();
			for (String sql : sqls) {
				conn.setAutoCommit(false);// 1,首先把Auto commit设置为false,不让它自动提交
				ps = conn.prepareStatement(sql);
				ps.execute();
				conn.commit();// 2,进行手动提交（commit）
			}
		} catch (Exception e) {
			logger.error("数据库异常", e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("数据库PreparedStatement关闭异常", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("数据库Connection关闭异常", e);
				}
			}
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
