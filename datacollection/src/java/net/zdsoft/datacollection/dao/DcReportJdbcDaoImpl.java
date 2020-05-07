package net.zdsoft.datacollection.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.datacollection.entity.DcDataModel;
import net.zdsoft.datacollection.entity.DcOperationData;
import net.zdsoft.datacollection.entity.DcReport;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SingleRowMapper;

@Repository
public class DcReportJdbcDaoImpl extends BaseDao<DcReport> implements DcReportJdbcDao {

	@Override
	public boolean checkReportTable(String tableName) {
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_" + tableName;
		}

		String key = "dc.table.exists@" + tableName;
		String exists = RedisUtils.get(key);
		if (StringUtils.equals(exists, "1")) {
			return true;
		}

		String sql = "select count(*) from all_tables where table_name = ?";
		boolean e = queryForInt(sql, StringUtils.upperCase(tableName)) > 0;
		if (e) {
			RedisUtils.set(key, "1");
			return true;
		}
		;
		return false;
	}

	@Override
	public boolean createReportTable(JSONObject data) {
		Set<String> keys = data.keySet();
		String reportCode = JsonUtils.getString(data, "_reportCode");
		String sql = "";
		sql = "create table dc_data_" + reportCode + "(";
		sql += "id char(32) default sys_guid()  not null,";
		for (String key : keys) {
			if (StringUtils.startsWith(key, "_") || StringUtils.contains(key, "@"))
				continue;
			sql += key + " varchar2(1000),";
		}
		sql += "unit_id char(32),";
		sql += "acadyear char(9),";
		sql += "semester number(1),";
		sql += "create_user_id char(32),";
		sql += "creation_time date,";
		sql += "primary key (id))";
		update(sql);
		return true;
	}

	@Override
	public boolean createReportMTable(JSONObject data) {
		Set<String> keys = data.keySet();
		String reportCode = JsonUtils.getString(data, "_reportCode");
		String sql = "";
		sql = "create table dc_data_m_" + reportCode + "(";
		sql += "id char(32) default sys_guid()  not null,";
		Set<String> ks = new HashSet<String>();
		for (String key : keys) {
			if (StringUtils.startsWith(key, "_") || !StringUtils.contains(key, "@"))
				continue;
			ks.add(StringUtils.substringBefore(key, "@") + " varchar2(1000),");
		}

		for (String key : ks) {
			sql += key;
		}
		sql += "data_id char(32),";
		sql += "report_code varchar2(100),";
		sql += "primary key (id))";
		update(sql);
		return true;
	}

	@Override
	public void saveReportData(JSONObject data) {
		JsonArray array = new JsonArray();
		array.add(data);
		saveReportData(array);
	}

	@Override
	public DcReport setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public JSONObject findData(String tableName, String dataId) {
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_" + tableName;
		}
		if (!checkReportTable(tableName)) {
			return new JSONObject();
		}
		String sql = "select * from " + tableName + " where id = ?";
		return query(sql, dataId, new SingleRowMapper<JSONObject>() {
			@Override
			public JSONObject mapRow(ResultSet rs) throws SQLException {
				ResultSetMetaData meta = rs.getMetaData();
				JSONObject json = new JSONObject();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					json.put(meta.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				return json;
			}
		});
	}

	@Override
	public List<JSONObject> findDataByUnitId(String tableName, String unitId) {
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_" + tableName;
		}
		if (!checkReportTable(tableName)) {
			return new ArrayList<JSONObject>();
		}

		String sql = "select * from " + tableName + " where unit_id = ? ";
		return query(sql, new Object[] { unitId }, new MultiRowMapper<JSONObject>() {
			@Override
			public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResultSetMetaData meta = rs.getMetaData();
				JSONObject json = new JSONObject();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					json.put(meta.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				return json;
			}
		});
	}

	@Override
	public List<JSONObject> findDataByUserId(String tableName, String userId) {
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_" + tableName;
		}
		if (!checkReportTable(tableName)) {
			return new ArrayList<JSONObject>();
		}
		String sql = "select * from " + tableName + " where create_user_id = ?";
		return query(sql, new Object[] { userId }, new MultiRowMapper<JSONObject>() {
			@Override
			public JSONObject mapRow(ResultSet rs, int num) throws SQLException {
				ResultSetMetaData meta = rs.getMetaData();
				JSONObject json = new JSONObject();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					json.put(meta.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				return json;
			}
		});
	}

	@Override
	public void remoteReportMByDataId(String tableName, String dataId) {
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_m_" + tableName;
		}
		if (!checkReportTable(tableName)) {
			return;
		}
		update("delete from " + tableName + " where data_id = ?", dataId);
	}

	@Override
	public void saveReportData(JsonArray array) {
		String dbDriver = "oracle";
		if (array.size() > 0) {
			List<String> columnSets = new ArrayList<String>();
			List<String> insertColumns = new ArrayList<String>();
			List<Object> valueSets = new ArrayList<Object>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject data = array.getJSONObject(i);
				String reportCode = JsonUtils.getString(data, "_reportCode");
				String tableName = reportCode;
				if (NumberUtils.isNumber(reportCode)) {
					tableName = "dc_data_" + tableName;
				}
				if (!data.containsKey("modify_time")) {
					String containModifyTime = RedisUtils.hget("dc.report.modifyTime.column", tableName);
					if (StringUtils.isBlank(containModifyTime)) {
						String checkColumnSql = "select count(*) from all_tab_columns where column_name = 'MODIFY_TIME' and table_name= ?";
						containModifyTime = "" + queryForInt(checkColumnSql, StringUtils.upperCase(tableName));
						RedisUtils.hset("dc.report.modifyTime.column", tableName, containModifyTime);
					}
					if (NumberUtils.toInt(containModifyTime) > 0) {
						data.put("modify_time", new Date());
					}
				}
				String sql = "";
				if (StringUtils.equals(dbDriver, "mysql")) {
					sql = "REPLACE INTO " + tableName + " SET ";
				} else if (StringUtils.equals(dbDriver, "oracle")) {
					sql = "merge into " + tableName + " a using (select ? id from dual) b "
							+ "on (a.id = b.id) when matched then "
							+ "update set {update} when not matched then insert ({insert}) values ({insertValues})";
				}

				columnSets.clear();
				valueSets.clear();
				insertColumns.clear();
				JSONObject json = array.getJSONObject(i);
				for (String key : json.keySet()) {
					if (StringUtils.startsWith(key, "_"))
						continue;
					if (StringUtils.equals(dbDriver, "oracle") && StringUtils.equalsIgnoreCase("id", key)) {
						continue;
					}
					insertColumns.add(key);
					columnSets.add(key + " = ?");
					valueSets.add(json.get(key));
				}
				if (StringUtils.equals(dbDriver, "mysql")) {
					sql += StringUtils.join(columnSets, ",");
					update(sql, valueSets.toArray(new Object[0]));
				} else if (StringUtils.equals(dbDriver, "oracle")) {
					sql = StringUtils.replace(sql, "{update}", StringUtils.join(columnSets, ","));
					insertColumns.add("id");
					sql = StringUtils.replace(sql, "{insert}", StringUtils.join(insertColumns, ","));
					sql = StringUtils.replace(sql, "{insertValues}",
							StringUtils.repeat("?", ",", insertColumns.size()));

					List<Object> params = new ArrayList<Object>();
					params.add(json.get("id"));
					params.addAll(valueSets);
					params.addAll(valueSets);
					params.add(json.get("id"));
					update(sql, params.toArray(new Object[0]));
				}
			}
		}
	}

	@Override
	public JSONArray findMData(String tableName, String dataId) {
		JSONArray array = new JSONArray();
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_m_" + tableName;
		}
		if (!checkReportTable(tableName)) {
			return new JSONArray();
		}
		String sql = "select * from " + tableName + " where data_id = ?";
		List<JSONObject> jsons = query(sql, dataId, new MultiRowMapper<JSONObject>() {
			@Override
			public JSONObject mapRow(ResultSet rs, int num) throws SQLException {
				ResultSetMetaData meta = rs.getMetaData();
				JSONObject json = new JSONObject();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					json.put(meta.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				return json;
			}
		});
		for (JSONObject json : jsons) {
			array.add(json);
		}
		return array;
	}

	@Override
	public List<JSONObject> findDatas(String tableName, String oderBys[], DcDataModel dcDataModel, Integer top) {
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_" + tableName;
		}
		if (!checkReportTable(tableName)) {
			return new ArrayList<JSONObject>();
		}

		List<String> columnList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		List<DcOperationData> dods = dcDataModel.getDcOperationDatas();
		for (int i = 0; i < dods.size(); i++) {
			DcOperationData dod = dods.get(i);
			String op = dod.getOperation();
			if (StringUtils.isBlank(op))
				op = "=";
			if (StringUtils.equalsIgnoreCase(op, "=")) {
				columnList.add(dod.getColumnName() + " = ?");
				valueList.add(dod.getDataValue());
			} else if (StringUtils.equalsIgnoreCase(op.trim(), "in")) {
				String cv = dod.getDataValue().toString();
				if (StringUtils.contains(cv, "(") && StringUtils.contains(cv, ")")) {
					cv = StringUtils.substringBetween(cv, "(", ")");
				}
				String[] ss = cv.split(",");
				columnList.add(dod.getColumnName() + " in (" + StringUtils.repeat("?", ",", ss.length) + ")");
				for (String s : ss) {
					if (StringUtils.startsWith(s, "\"") || StringUtils.startsWith(s, "\'"))
						s = StringUtils.substring(s, 1);
					if (StringUtils.endsWith(s, "\"") || StringUtils.endsWith(s, "\'"))
						s = StringUtils.substring(s, 0, s.length() - 1);
					valueList.add(s);
				}
			} else if (StringUtils.equalsIgnoreCase(op.trim(), "notin")) {
				dod.setOperation(" not in ");
				String cv = dod.getDataValue().toString();
				if (StringUtils.contains(cv, "(") && StringUtils.contains(cv, ")")) {
					cv = StringUtils.substringBetween(cv, "(", ")");
				}
				String[] ss = cv.split(",");
				columnList.add(dod.getColumnName() + " not in (" + StringUtils.repeat("?", ",", ss.length) + ")");
				for (String s : ss) {
					if (StringUtils.startsWith(s, "\"") || StringUtils.startsWith(s, "\'"))
						s = StringUtils.substring(s, 1);
					if (StringUtils.endsWith(s, "\"") || StringUtils.endsWith(s, "\'"))
						s = StringUtils.substring(s, 0, s.length() - 1);
					valueList.add(s);
				}
			} else if (StringUtils.equalsIgnoreCase(op, " like ")) {
				columnList.add(dod.getColumnName() + " like ?");
				valueList.add(StringUtils.contains(dod.getDataValue().toString(), "%") ? dod.getDataValue().toString()
						: "%" + dod.getDataValue().toString() + "%");
			} else {
				columnList.add(dod.getColumnName() + " " + op + "  ?");
				String dataType = dod.getDataType();
				if (StringUtils.equalsIgnoreCase(dataType, "date")) {
					try {
						Date date = DateUtils.parseDate(dod.getDataValue().toString(), "yyyy-MM-dd", "yyyy/MM/dd",
								"yyyyMMdd");
						if (StringUtils.equals(op, "<=") || StringUtils.equals(op, "<")) {
							date = DateUtils.addDays(date, 1);
						}

						if (StringUtils.equalsIgnoreCase(dod.getColumnName(), "wo_date")) {
							valueList.add(DateFormatUtils.format(date, "yyyy/MM/dd"));
						} else{
							valueList.add(date);
						}
					} catch (ParseException e) {
					}
				} else
					valueList.add(dod.getDataValue());
			}
		}
		String sql = "select * from " + tableName;
		if (CollectionUtils.isNotEmpty(columnList))
			sql = sql + " WHERE " + StringUtils.join(columnList, " AND ");
		if (ArrayUtils.isNotEmpty(oderBys)) {
			sql += " ORDER BY ";
			sql += StringUtils.join(oderBys, ",");
		}
		List<JSONObject> jos = null;

		if (top == null || top.intValue() == 0) {
			jos = query(sql, valueList.toArray(new Object[0]), new MultiRowMapper<JSONObject>() {
				@Override
				public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
					ResultSetMetaData meta = rs.getMetaData();
					JSONObject json = new JSONObject();
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						Object o = rs.getObject(i);
						int columnType = meta.getColumnType(i);
						if (ArrayUtils.contains(new int[] { Types.DATE, Types.TIME, Types.TIMESTAMP, Types.TIME },
								columnType)) {
							o = rs.getTimestamp(i);
						}
						json.put(meta.getColumnName(i).toLowerCase(), o);
					}
					return json;
				}
			});
		} else {
			jos = queryForTop(sql, valueList.toArray(new Object[0]), null, new MultiRowMapper<JSONObject>() {
				@Override
				public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
					ResultSetMetaData meta = rs.getMetaData();
					JSONObject json = new JSONObject();
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						Object o = rs.getObject(i);
						int columnType = meta.getColumnType(i);
						if (ArrayUtils.contains(new int[] { Types.DATE, Types.TIME, Types.TIMESTAMP, Types.TIME },
								columnType)) {
							o = rs.getTimestamp(i);
						}
						json.put(meta.getColumnName(i).toLowerCase(), o);
					}
					return json;
				}
			}, top);
		}
		return jos;
	}

	@Override
	public void remoteReportDataById(String tableName, String id) {
		if (NumberUtils.isNumber(tableName)) {
			tableName = "dc_data_" + tableName;
		}
		if (!checkReportTable(tableName)) {
			return;
		}
		String sql = "delete from " + tableName + " where id = ?";
		update(sql, id);
	}

	@Override
	public int findMaxCode(String codePrefix) {
		String sql = "select max(report_code) from dc_report where report_code like ?";
		String maxCode = query(sql, codePrefix + "%", new SingleRowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs) throws SQLException {
				return rs.getString(1);
			}
		});
		if (StringUtils.startsWith(maxCode, codePrefix)) {
			String v = StringUtils.substringAfter(maxCode, codePrefix);
			return NumberUtils.toInt(v);
		}

		return 0;
	}
}
