package net.zdsoft.syncdatamq.service.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.HttpClientUtils;
import net.zdsoft.framework.utils.JdbcUtils;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdatamq.dao.SymDataModifyTimeDao;
import net.zdsoft.syncdatamq.entity.SymDataModifyTime;
import net.zdsoft.syncdatamq.service.SymDataModifyTimeService;
import net.zdsoft.syncdatamq.utils.ActiveMQUtils;

@Service
public class SymDataModifyTimeServiceImpl extends SymAbstractServiceImpl<SymDataModifyTime, String>
		implements SymDataModifyTimeService {

	private static final String MYSQL = "mysql";
	private static final String COLUMN_TYPE = "column_type";

	private static final Logger log = Logger.getLogger(SymDataModifyTimeServiceImpl.class);
	@Autowired
	private SymDataModifyTimeDao symDataModifyTimeDao;

	@Override
	protected BaseJpaRepositoryDao<SymDataModifyTime, String> getJpaDao() {
		return symDataModifyTimeDao;
	}

	@Override
	protected Class<SymDataModifyTime> getEntityClass() {
		return SymDataModifyTime.class;
	}

	@Override
	public String findDatas(String tableName, Map<String, Object> params) {
		return SUtils.s(symDataModifyTimeDao.findDatas(tableName, params));
	}

	@Override
	public String findData(String tableName, String... dataIds) {
		return symDataModifyTimeDao.findData(tableName, dataIds);
	}

	@Override
	public void dealReceive() {
		return;
	}

	@Override
	public String findData(String entityName, Date modifyTime) {
		return symDataModifyTimeDao.findData(entityName, modifyTime);
	}

	@Override
	public String findData(String entityName, Date modifyTime, String modifyTimeName, int fetchSize) {
		return symDataModifyTimeDao.findData(entityName, modifyTime, modifyTimeName, fetchSize);
	}

	@Override
	public boolean saveReceiveSymData(String receiveTableName, String dataTableName, String clientId,
			JdbcTemplate jdbcTemplate, String dbDriver, JSONObject symDataJson) throws Exception {

		try {
			String data = ActiveMQUtils.receiveMessageQueue(createQueueName(clientId, dataTableName));
			if (StringUtils.isBlank(data))
				return false;

			JSONObject dataJson;
			if (symDataJson != null && symDataJson.containsKey(receiveTableName)) {
				dataJson = symDataJson.getJSONObject(receiveTableName);
			} else {
				dataJson = new JSONObject();
			}

			boolean rtn = deal4ReceiveUrl(dataTableName, clientId, data, dataJson);
			if (rtn)
				return true;

			data = new String(SecurityUtils.decryptAESAndBase64(data.getBytes("utf8"),
					ActiveMQUtils.createEncryptKey(clientId, dataTableName)), "utf8");
			JSONObject json = JSONObject.parseObject(data);
			JSONArray array = json.getJSONArray("data");
			if (array.isEmpty())
				return false;

			if (dataJson.containsKey("send_table_name")) {
				dataTableName = dataJson.getString("send_table_name");
			}
			deal4ReceiveDB(receiveTableName, jdbcTemplate, dbDriver, dataJson, array);
			ActiveMQUtils.commitSession(createQueueName(clientId, dataTableName));
			log.error(" 接收" + clientId + "." + receiveTableName + "数据" + array.size() + "条");
		} catch (Exception e) {
			ActiveMQUtils.rollbackSession(createQueueName(clientId, dataTableName));
			throw e;
		}

		return true;
	}

	private void deal4ReceiveDB(String receiveTableName, JdbcTemplate jdbcTemplate, String dbDriver,
			JSONObject dataJson, JSONArray array) {
		JSONArray columnsJson = dataJson.getJSONArray("column_transfer");
		Map<String, JSONObject> columnMap = new HashMap<>();
		if (columnsJson != null) {
			for (int index = 0; index < columnsJson.size(); index++) {
				JSONObject c = columnsJson.getJSONObject(index);
				columnMap.put(c.getString("receive_column_name"), c);
			}
		}

		String sql4RowSet = null;
		if (StringUtils.equals(dbDriver, MYSQL)) {
			sql4RowSet = "select * from " + receiveTableName + " limit ?";
		} else {
			sql4RowSet = "select * from " + receiveTableName + " where rownum = ?";
		}

		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql4RowSet, 0);
		SqlRowSetMetaData metaData = rowSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		Map<String, Integer> columnTypeMap = new HashMap<>();
		Set<String> columnSet = new HashSet<>();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			int type = metaData.getColumnType(i);
			String columnNameLower = columnName.toLowerCase();
			columnSet.add(columnNameLower);
			columnTypeMap.put(columnNameLower, type);
		}

		String sql = "";
		List<Object[]> listOfArgs = new ArrayList<>();
		List<Integer> types = new ArrayList<>();
		List<Integer> insertTypes = new ArrayList<>();

		JSONObject jsonObject;
		List<String> updates = new ArrayList<>();
		List<String> inserts = new ArrayList<>();
		List<Object> values = new ArrayList<>();
		List<Object> values4Mysql = new ArrayList<>();
		String[] columns = null;

		Set<String> removedColumns = new HashSet<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = array.getJSONObject(i);
			String columnOnly = dataJson.getString("receive_column_only");
			if (StringUtils.isNotBlank(columnOnly)) {
				String[] remainedColumns = columnOnly.split(",");
				for (String c : json.keySet()) {
					if (!ArrayUtils.contains(remainedColumns, c.toLowerCase())) {
						removedColumns.add(c);
					}
				}
			} else {
				for (String c : json.keySet()) {
					if (!columnSet.contains(c.toLowerCase()))
						removedColumns.add(c);
				}
				String columnIgnore = dataJson.getString("receive_column_ignore");
				if (StringUtils.isNotBlank(columnIgnore)) {
					for (String c : columnIgnore.split(",")) {
						json.remove(c.trim().toUpperCase());
					}
				}
			}
			for (String rc : removedColumns) {
				json.remove(rc);
			}
			values.add(json.getString("ID"));
			String eventSource = dataJson.getString("eventSourceValue");
			if (StringUtils.isBlank(eventSource))
				eventSource = "2";

			if (i == 0) {
				jsonObject = json;
				columns = jsonObject.keySet().toArray(new String[0]);
				types.add(Types.CHAR);
				for (String column : columns) {
					if (StringUtils.equals("ID", column)) {
						inserts.add(column);
						Integer type = columnTypeMap.get(column.toLowerCase());
						insertTypes.add(type == null ? Types.CHAR : type);
						continue;
					}
					updates.add(column + " = ? ");
					inserts.add(column);
					Integer type = columnTypeMap.get(column.toLowerCase());
					insertTypes.add(type == null ? Types.CHAR : type);
					Integer cType = columnTypeMap.get(column.toLowerCase());
					types.add(cType == null ? Types.CHAR : cType);
				}
			}

			for (String column : columns) {
				String value = json.getString(column);
				if (StringUtils.isNotBlank(eventSource) && StringUtils.equalsIgnoreCase("event_source", column)) {
					value = eventSource;
				}
				Integer columnType = columnTypeMap.get(column.toLowerCase());
				if (ArrayUtils.contains(new Integer[] { Types.DATE, Types.TIME, Types.TIMESTAMP }, columnType)) {
					values4Mysql.add(json.getDate(column));
					values.add(json.getDate(column));
				} else if (ArrayUtils.contains(new Integer[] { Types.INTEGER, Types.BIGINT, Types.SMALLINT },
						columnType)) {
					values.add(json.getInteger(column));
					values4Mysql.add(json.getInteger(column));
				} else {
					if (StringUtils.equals("ID", column)) {
						values4Mysql.add(value);
					} else {
						values4Mysql.add(value);
						values.add(value);
					}
				}
			}
			for (String column : columns) {
				String value = json.getString(column);
				if (StringUtils.isNotBlank(eventSource) && StringUtils.equalsIgnoreCase("event_source", column)) {
					value = eventSource;
				}

				Integer columnType = columnTypeMap.get(column.toLowerCase());
				if (ArrayUtils.contains(new Integer[] { Types.DATE, Types.TIME, Types.TIMESTAMP }, columnType)) {
					values.add(json.getDate(column));
					values4Mysql.add(json.getDate(column));
				} else if (ArrayUtils.contains(new Integer[] { Types.INTEGER, Types.BIGINT, Types.SMALLINT },
						columnType)) {
					values.add(json.getInteger(column));
					values4Mysql.add(json.getInteger(column));
				} else {
					if (StringUtils.equals("ID", column)) {
						values.add(value);
					} else {
						values4Mysql.add(value);
						values.add(value);
					}
				}
			}
			if (StringUtils.equals(dbDriver, MYSQL)) {
				listOfArgs.add(values4Mysql.toArray(new Object[0]));
			} else {
				listOfArgs.add(values.toArray(new Object[0]));
			}
			values4Mysql.clear();
			values.clear();
		}

		if (StringUtils.equals(dbDriver, MYSQL)) {
			sql = "INSERT INTO " + receiveTableName + " ( " + StringUtils.join(inserts.toArray(new String[0]), ",")
					+ ") values ( " + StringUtils.repeat("?", ",", inserts.size()) + ") on DUPLICATE KEY update "
					+ StringUtils.join(updates.toArray(new String[0]), ",");
			types.remove(0);

			Integer[] types2 = ArrayUtils.addAll(insertTypes.toArray(new Integer[0]), types.toArray(new Integer[0]));
			if (!listOfArgs.isEmpty())
				batchUpdate(jdbcTemplate, sql, listOfArgs, ArrayUtils.toPrimitive(types2));
		} else if (StringUtils.equals(dbDriver, "oracle")) {
			sql = "merge into " + receiveTableName + " a using (select ? id from dual) b "
					+ "on (a.id = b.id) when matched then " + "update set "
					+ StringUtils.join(updates.toArray(new String[0]), ",") + " when not matched then insert ( "
					+ StringUtils.join(inserts.toArray(new String[0]), ",") + ") values ( "
					+ StringUtils.repeat("?", ",", inserts.size()) + ")";
			batchUpdate(jdbcTemplate, sql, listOfArgs, ArrayUtils.toPrimitive(
					ArrayUtils.addAll(types.toArray(new Integer[0]), insertTypes.toArray(new Integer[0]))));
		}
	}

	private boolean deal4ReceiveUrl(String dataTableName, String clientId, String data, JSONObject dataJson)
			throws IOException {
		if (dataJson.containsKey("receive_deal_data_url")) {
			String dealDataUrl = dataJson.getString("receive_deal_data_url");
			if (StringUtils.isNotBlank(dealDataUrl)) {
				if (!StringUtils.startsWithIgnoreCase(dealDataUrl, "http")) {
					dealDataUrl = Evn.getWebUrl() + dealDataUrl;
				}
				Map<String, String> map = new HashMap<>();
				map.put(ActiveMQUtils.ACTIVE_MQ_URL_PARAMETER, data);
				map.put("sck", clientId + "." + dataTableName);
				System.out.println(dealDataUrl);
				System.out.println(JSONObject.toJSONString(map));
				String rtn = HttpClientUtils.postSync(dealDataUrl, map);
				if (StringUtils.equalsIgnoreCase(Boolean.FALSE.toString(), rtn)) {
					throw new RuntimeException();
				}
				ActiveMQUtils.commitSession(createQueueName(clientId, dataTableName));
				return true;
			}
		}
		return false;
	}

	protected int[] batchUpdate(JdbcTemplate jdbcTemplate, String sql, List<Object[]> listOfArgs, int[] argTypes) {
		int batchSize = 500;
		if (listOfArgs == null || listOfArgs.isEmpty()) {
			return new int[0];
		}

		try {
			int destPos = 0;
			int size = listOfArgs.size();
			int[] totalResults = new int[size];
			int batchExecCount = (size % batchSize == 0) ? size / batchSize : size / batchSize + 1;

			for (int i = 0; i < batchExecCount; i++) {
				int from = batchSize * i;
				int to = 0;
				if (i == batchExecCount - 1) {
					to = size;
				} else {
					to = batchSize * (i + 1);
				}

				List<Object[]> batchListOfArgs = listOfArgs.subList(from, to);
				int[] batchResults = jdbcTemplate.batchUpdate(sql,
						new SpecialBatchPreparedStatementSetter(batchListOfArgs, argTypes));
				System.arraycopy(batchResults, 0, totalResults, destPos, batchResults.length);
				destPos += batchResults.length;
			}
			return totalResults;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	private class SpecialBatchPreparedStatementSetter implements BatchPreparedStatementSetter {

		private int[] argTypes = null;
		private List<Object[]> listOfArgs = null;

		public SpecialBatchPreparedStatementSetter(List<Object[]> listOfArgs, int[] argTypes) {
			this.listOfArgs = listOfArgs;
			this.argTypes = argTypes;
		}

		public int getBatchSize() {
			return listOfArgs.size();
		}

		public void setValues(PreparedStatement ps, int i) throws SQLException {
			Object[] args = listOfArgs.get(i);
			JdbcUtils.setSuitedParamsToStatement(args, argTypes, ps);
		}
	}

	public boolean saveSendSymData(String entityName, String clientId, String timeColumnName, String orderByColumnName,
			JdbcTemplate jdbcTemplate, JSONObject symDataJson) throws ParseException {

		List<SymDataModifyTime> dataModifyTimes = findListBy("clientId", clientId);
		Map<String, SymDataModifyTime> dataModifyTimeMap = EntityUtils.getMap(dataModifyTimes,
				SymDataModifyTime::getEntityName);

		SymDataModifyTime dataModifyTime = dataModifyTimeMap.get(entityName);
		Date modifyTime;
		if (dataModifyTime == null) {
			modifyTime = DateUtils.parseDate("1900-01-01", "yyyy-MM-dd");
		} else {
			modifyTime = dataModifyTime.getModifyTime();
		}

		String whereExtend = null;
		JSONObject dataJson = symDataJson.getJSONObject(entityName);
		String sendColumnOnly = null;
		String sendIgnoreColumns = null;
		List<JSONObject> jsons = null;
		if (dataJson != null) {
			String s = JsonUtils.getString(dataJson, "where_extend");
			whereExtend = StringUtils.isNotBlank(s) ? s : whereExtend;

			s = JsonUtils.getString(dataJson, "where_filter_column");
			timeColumnName = StringUtils.isNotBlank(s) ? s : timeColumnName;

			s = JsonUtils.getString(dataJson, "send_column_only");
			sendColumnOnly = StringUtils.isNotBlank(s) ? s : sendColumnOnly;

			s = JsonUtils.getString(dataJson, "send_column_ignore");
			sendIgnoreColumns = StringUtils.isNotBlank(s) ? s : sendIgnoreColumns;

			if (dataJson.containsKey("order_by_column")) {
				orderByColumnName = dataJson.getString("order_by_column");
			}
			s = JsonUtils.getString(dataJson, "send_deal_data_url");
			if(StringUtils.isNotBlank(s)) {
				try {
					s = StringUtils.replace(s, "{modifyTime}", DateFormatUtils.format(modifyTime, "yyyyMMddHHmmssSSS"));
					s = StringUtils.replace(s, "{timestamp}", String.valueOf(modifyTime.getTime()));
					String data = HttpClientUtils.getSync(s);
					jsons = SUtils.dt(data, JSONObject.class);
				} catch (IOException e) {
					return false;
				}
			}
		}

		if(dataJson == null || StringUtils.isBlank(JsonUtils.getString(dataJson, "send_deal_data_url"))) {
			 jsons = fetchData(entityName, sendColumnOnly, sendIgnoreColumns, timeColumnName,
					orderByColumnName, modifyTime, jdbcTemplate, whereExtend);
		}
		if (dataJson != null) {
			String dataSet = JsonUtils.getString(dataJson, "set_date");
			if (StringUtils.isNotBlank(dataSet)) {
				JSONArray dses = JsonArray.parseArray(dataSet);
				for (JSONObject json : jsons) {
					for (int i = 0; i < dses.size(); i++) {
						JSONObject ds = dses.getJSONObject(i);
						String name = ds.getString("name");
						String value = ds.getString("vlaue");
						json.put(name, value);
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(jsons)) {
			dataModifyTime = putModifyTime(entityName, clientId, timeColumnName, dataModifyTime, jsons);
			JSONObject mqJson = new JSONObject();
			mqJson.put("result", 1);
			mqJson.put("data", jsons);
			mqJson.put("message", "ok");
			mqJson.put("dataCount", jsons.size());
			ActiveMQUtils.sendMessageQueue(createQueueName(clientId, entityName), SecurityUtils
					.encryptAESAndBase64(SUtils.s(mqJson), ActiveMQUtils.createEncryptKey(clientId, entityName)));
			log.error(" 发送" + clientId + "." + entityName + "数据" + jsons.size() + "条 ("
					+ DateFormatUtils.format(dataModifyTime.getModifyTime(), "yyyyMMdd HH:mm:ss.SSS") + ")");
			return true;
		}
		return false;
	}

	private String createQueueName(String clientId, String entityName) {
		return clientId + "." + entityName;
	}

	private SymDataModifyTime putModifyTime(String entityName, String clientId, String timeColumnName,
			SymDataModifyTime dataModifyTime, List<JSONObject> jsons) throws ParseException {
		Date maxTime = DateUtils.parseDate("1900-01-01 00:00:00.000", "yyyy-MM-dd HH:mm:ss.SSS");
		for (JSONObject json : jsons) {
			Date date = json.getDate(timeColumnName.toUpperCase());
			if (date.after(maxTime))
				maxTime = date;
		}
		if (dataModifyTime == null) {
			dataModifyTime = new SymDataModifyTime();
			dataModifyTime.setClientId(clientId);
			dataModifyTime.setEntityName(entityName);
			dataModifyTime.setModifyTime(maxTime);
			dataModifyTime.setId(UuidUtils.generateUuid());
		} else {
			dataModifyTime.setModifyTime(maxTime);
		}
		save(dataModifyTime);
		return dataModifyTime;
	}

	private <T> List<T> queryForTop(JdbcTemplate jdbcTemplate, String sql, Object[] args, int[] argTypes,
			MultiRowMapper<T> multiRowMapper, int topLimit) {
		try {
			if (argTypes == null)
				return jdbcTemplate.query(sql, args, new MultiTopResultSetExtractor<T>(multiRowMapper, topLimit));
			else
				return jdbcTemplate.query(sql, args, argTypes,
						new MultiTopResultSetExtractor<T>(multiRowMapper, topLimit));
		} finally {
			log.debug("");
		}
	}

	private class MultiTopResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

		private MultiRowMapper<T> multiRowMapper = null;
		private int topLimit;

		public MultiTopResultSetExtractor(MultiRowMapper<T> multiRowMapper, int topLimit) {
			this.multiRowMapper = multiRowMapper;
			this.topLimit = topLimit;
		}

		public List<T> extractData(ResultSet rs) throws SQLException {
			List<T> data = new ArrayList<>();
			int index = 0;
			while (rs.next() && index < topLimit) {
				data.add(multiRowMapper.mapRow(rs, index++));
			}
			return data;
		}
	}

	private List<JSONObject> fetchData(String entityName, String selectColumns, String sendIgnoreColumns,
			String timeColumnName, String orderByColumnName, Date modifyTime, JdbcTemplate jdbcTemplate,
			String whereExtend) {
		String sql = "SELECT " + (StringUtils.isNotBlank(selectColumns) ? selectColumns : "*") + " FROM " + entityName
				+ " WHERE " + timeColumnName + " > ?  "
				+ (StringUtils.isNotBlank(whereExtend) ? " AND " + whereExtend : "")
				+ (StringUtils.isNotBlank(orderByColumnName) ? " ORDER BY " + orderByColumnName : "");
		String[] ignoreColumns = null;
		if (StringUtils.isNotBlank(sendIgnoreColumns)) {
			ignoreColumns = sendIgnoreColumns.split(",");
		}
		final String[] ignoreColumns2 = ignoreColumns;
		return queryForTop(jdbcTemplate, sql, new Object[] { modifyTime }, new int[] { Types.TIMESTAMP },
				new MultiRowMapper<JSONObject>() {
					@Override
					public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
						ResultSetMetaData md = rs.getMetaData();
						JSONObject json = new JSONObject();
						int count = md.getColumnCount();
						for (int i = 0; i < count; i++) {
							int type = md.getColumnType(i + 1);
							String name = md.getColumnName(i + 1);
							if (ignoreColumns2 != null && ArrayUtils.contains(ignoreColumns2, name.toLowerCase())) {
								continue;
							}
							if (type == Types.TIMESTAMP || type == Types.DATE) {
								json.put(name, rs.getTimestamp(i + 1));
							} else if (type == Types.INTEGER) {
								json.put(name, rs.getInt(i + 1));
							} else if (type == Types.FLOAT) {
								json.put(name, rs.getFloat(i + 1));
							} else {
								json.put(name, rs.getObject(i + 1));
							}
						}
						return json;
					}
				}, 1000);
	}

}
