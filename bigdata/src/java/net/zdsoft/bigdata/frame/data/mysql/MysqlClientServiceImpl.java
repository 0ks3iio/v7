package net.zdsoft.bigdata.frame.data.mysql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.echarts.EntryUtils.DataException;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.datasource.Statement;
import net.zdsoft.bigdata.datasource.*;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.bigdata.extend.data.utils.MultiConvertUtils;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.JdbcUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

@Service("mysqlClientService")
public class MysqlClientServiceImpl implements MysqlClientService,
		ApplicationContextAware {
	private static Logger logger = LoggerFactory
			.getLogger(MysqlClientServiceImpl.class);

	private static DatasourceQueryChecker datasourceQueryChecker;

	private static Database database;

	@Autowired
	private OptionService optionService;

	@Autowired
	private DatabaseService databaseService;

	public Adapter getAdapter(String dataSourceId, String dbName) {
		if (StringUtils.isBlank(dataSourceId)) {
			OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
			if (mysqlDto == null || mysqlDto.getStatus() == 0) {
				return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
						.build();
			}
			initDatabase(mysqlDto, dbName);
			database.setCharacterEncoding("utf-8");

			return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
					.characterEncoding(database.getCharacterEncoding())
					.dbName(database.getDbName())
					.username(database.getUsername())
					.domain(database.getDomain())
					.password(database.getPassword()).port(database.getPort())
					.type(DatabaseType.parse(database.getType())).build();
		} else {
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
	}
	
	private void initDatabase(OptionDto mysqlDto, String dbName) {
		database = new Database();
		database.setPort(Integer.valueOf(mysqlDto.getFrameParamMap()
				.get("port")));
		database.setDomain(mysqlDto.getFrameParamMap().get("domain"));
		if (StringUtils.isBlank(dbName))
			database.setDbName(mysqlDto.getFrameParamMap().get("database"));
		else
			database.setDbName(dbName);
		database.setUsername(mysqlDto.getFrameParamMap().get("user"));
		database.setPassword(mysqlDto.getFrameParamMap().get("password"));
		database.setType(DatabaseType.MYSQL.getType());
	}

	@SuppressWarnings("rawtypes")
	public List<Json> getDataListFromMysql(String dataSourceId,String database, String sql,
			List<Json> paramList, List<Json> resultFieldList) {
		List<Json> resultList = new ArrayList<Json>();
		try {
			Adapter adapter = getAdapter(dataSourceId,database);
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
			logger.error(e.getMessage(), e);
			return resultList;
		}
	}

	@Override
	public List<Json> getDataListFromMysqlThrowException(String dataSourceId, String database, String sql, List<Json> paramList, List<Json> resultFieldList) throws BigDataBusinessException {
		List<Json> resultList = new ArrayList<Json>();
		try {
			Adapter adapter = getAdapter(dataSourceId,database);
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
				resultList = parse(queryResponse.getQueryValue());
				// 排序
				for (Json json : resultFieldList) {
					if (org.apache.commons.lang3.StringUtils.isNotBlank(json.getString("orderJson"))) {
						//{"小学":"001","初中":"002","高中":"003"}
						Map<String, String> orderMap = JSON.parseObject(json.getString("orderJson"),
								new TypeReference<HashMap<String, String>>() {
								});

						resultList.sort((o1, o2) -> {
							String c1 = orderMap.get(o1.getString(json.getString("field")));
							String c2 = orderMap.get(o2.getString(json.getString("field")));
							if (c1 == null || c2== null) {
								return 0;
							}
							return c1.compareTo(c2);
						});
					}
				}
				return resultList;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BigDataBusinessException(e.getMessage(), e);
		}
	}

	public boolean execSql(String dataSourceId,String database, String sql, Object[] objst) {
		boolean execResult = false;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Adapter adapter = getAdapter( dataSourceId,database);
			Statement<Adapter> statement = new Statement<>(adapter);
			JdbcTemplate template = datasourceQueryChecker.execute(statement,
					DatasourceExecutor.jdbcTemplate());
			sql = JdbcUtils.getSQL(sql, objst);
			conn = template.getDataSource().getConnection();
			ps = conn.prepareStatement(sql);
			execResult = ps.execute();
			return execResult;
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

	public void execSqls(String dataSourceId,String database, List<String> sqls) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Adapter adapter = getAdapter(dataSourceId,database);
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

	public Json execProc(String dataSourceId,String database, String sql, List<Json> inParams,
			List<Json> outParams) {
		CallableStatement cstm = null;
		Connection conn = null;
		try {
			Adapter adapter = getAdapter(dataSourceId,database);
			Statement<Adapter> statement = new Statement<>(adapter);
			JdbcTemplate template = datasourceQueryChecker.execute(statement,
					DatasourceExecutor.jdbcTemplate());
			conn = template.getDataSource().getConnection();
			cstm = conn.prepareCall(sql); // 实例化对象cstm
			int index = 1;
			for (Json param : inParams) {
				String dataType = param.getString("dataType");
				if ("string".equals(dataType)) {
					cstm.setString(index, param.getString("value")); // 存储过程输入参数
				} else if ("int".equals(dataType)) {
					cstm.setInt(index, param.getIntValue("value")); // 存储过程输入参数
				}
				index++;
			}
			int copyIndex = index;
			for (Json param : outParams) {
				String dataType = param.getString("dataType");
				if ("string".equals(dataType)) {
					cstm.registerOutParameter(index, Types.VARCHAR); // 设置返回值类型
																		// 即返回值
				} else if ("int".equals(dataType)) {
					cstm.registerOutParameter(index, Types.INTEGER); // 设置返回值类型
																		// 即返回值
				}
				index++;
			}
			cstm.execute(); // 执行存储过程
			Json result = new Json();
			for (Json param : outParams) {
				String dataType = param.getString("dataType");
				if ("string".equals(dataType)) {
					result.put(param.getString("field"),
							cstm.getString(copyIndex));
				} else if ("int".equals(dataType)) {
					result.put(param.getString("field"), cstm.getInt(copyIndex));
				}
				copyIndex++;
			}
			return result;
		} catch (Exception e) {
			logger.error("数据库异常", e);
			return null;
		} finally {
			if (cstm != null) {
				try {
					cstm.close();
				} catch (SQLException e) {
					logger.error("数据库cstm关闭异常", e);
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

	public String getDataListFromMysql(String dataSourceId,String dbName, String sql,
			List<Json> paramList, List<Json> resultFieldList,
			List<Json> rowDimensionList, List<Json> columnDimensionList,
			List<Json> indexList) {
		List<Json> dataList = getDataListFromMysql(dataSourceId,dbName, sql, paramList,
				resultFieldList);
		return MultiConvertUtils.getMultiConvertData(dataList, paramList,
				resultFieldList, rowDimensionList, columnDimensionList,
				indexList);
	}

	@SuppressWarnings("rawtypes")
	public Json getDbUsageInfo(String dataSourceId,String dbName) {
		String sql = "select truncate(sum(data_length)/1024/1024,2) as size,sum(table_rows) as num from tables where TABLE_SCHEMA = '@DBNAME' ";
		sql = sql.replace("@DBNAME", dbName);

		List<Json> resultList = new ArrayList<Json>();
		Json result = new Json();
		result.put("totalsize", "0MB");
		result.put("recordnum", "0");
		try {
			Adapter adapter = getAdapter(dataSourceId,"information_schema");
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
				resultList = parse(queryResponse.getQueryValue());
				if (CollectionUtils.isNotEmpty(resultList)) {
					result = resultList.get(0);
					DecimalFormat df = new DecimalFormat("0.00");
					Long totalSize = result.getLong("size");
					if (totalSize < 1024) {
						result.put("totalsize", String.valueOf(totalSize)
								+ "MB");
					} else {
						if (totalSize / 1024 < 1024) {
							result.put("totalsize",
									df.format((float) totalSize / 1024) + "GB");
						} else {
							result.put("totalsize",
									df.format((float) totalSize / 1024 / 1024)
											+ "TB");
						}
					}
					result.put("recordnum",
							String.valueOf(result.getLong("num")));
				}
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return result;
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
