package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.basedata.dao.OperationLogJdbcDao;
import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.MultiRowMapper;

@Repository
public class OperationLogDaoImpl extends BaseDao<OperationLog> implements OperationLogJdbcDao {

	@Override
	public List<Object[]> findUsageFunctions(String[] userIds, int topN, Date fromDate) {

		Object[] objs = new Object[] {};

		objs = ArrayUtils.addAll(objs, userIds);
		objs = ArrayUtils.add(objs, fromDate);
		objs = ArrayUtils.add(objs, topN);

		String sql = "select * from (select distinct b.model_name, count(*), row_number() over (order by count(*) desc) as rn from base_operation_log a, base_operation_url b "
				+ "where a.url = b.url and b.model_name is not null and a.user_id in ("
				+ StringUtils.repeat("?", ",", userIds.length)
				+ ") and a.log_time >= ? group by b.model_name) tt where tt.rn <= ?";
		return query(sql, objs, new MultiRowMapper<Object[]>() {
			@Override
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object[] os = new Object[2];
				os[0] = rs.getString(1);
				os[1] = rs.getInt(2);
				return os;
			}
		});
	}

	public List<Object[]> findUsageServers(String[] userIds, int topN, Date fromDate) {
		String sql = "select * from (select distinct c.name, count(*), row_number() over (order by count(*) desc) as rn from base_operation_log a, base_operation_url b, base_server c "
				+ "where c.code = b.server_code and a.url = b.url and b.model_name is not null and a.user_id  in ("
				+ StringUtils.repeat("?", ",", userIds.length)
				+ ") and a.log_time >= ? group by c.name) tt where tt.rn <= ?";

		Object[] objs = new Object[] {};

		objs = ArrayUtils.addAll(objs, userIds);
		objs = ArrayUtils.add(objs, fromDate);
		objs = ArrayUtils.add(objs, topN);

		return query(sql, objs, new MultiRowMapper<Object[]>() {
			@Override
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object[] os = new Object[2];
				os[0] = rs.getString(1);
				os[1] = rs.getInt(2);
				return os;
			}
		});
	}
	
	@Override
	public void updateJsonStr(String[] ids, String[] jsonStrs) {
		String sql = "update base_operation_log_bak set json_str = ? where id = ?";
		List<Object[]> ls = new ArrayList<>(ids.length);
		for(int i = 0; i < ids.length; i ++) {
			ls.add(new Object[] {jsonStrs[i], ids[i]});
		}
		batchUpdate(sql, ls, new int[] {Types.CHAR, Types.CHAR});
	}

	@Override
	public List<OperationLog> findByJsonIsNullTopN(int top) {
		String sql = "select * from base_operation_log_bak where json_str is null and rownum <= ?";
		return query(sql, new Object[] {top}, new MultiRow());
	}
	
	

	@Override
	public void insertLog(OperationLog... logs) {
		String sql = "INSERT INTO base_operation_log (id, url, log_time, description, user_id, unit_id, ip, client_type, "
				+ "client_version, owner_id, owner_type, spend_time, parameter, json_str,domain) values  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> listOfArgs = new ArrayList<>();
		for (OperationLog log : logs) {
			if (log == null)
				continue;
			listOfArgs.add(new Object[] { log.getId(), log.getUrl(), log.getLogTime(), log.getDescription(),
					log.getUserId(), log.getUnitId(), log.getIp(), log.getClientType(), log.getClientVersion(),
					log.getOwnerId(), log.getOwnerType(), log.getSpendTime(), log.getParameter(), log.getJsonStr(), log.getDomain() });
		}
		batchUpdate(sql, listOfArgs,
				new int[] { Types.CHAR, Types.CHAR, Types.TIMESTAMP, Types.CHAR, Types.CHAR, Types.CHAR, Types.CHAR,
						Types.INTEGER, Types.CHAR, Types.CHAR, Types.INTEGER, Types.FLOAT, Types.CHAR, Types.CHAR, Types.CHAR });
	}

	@Override
	public OperationLog setField(ResultSet rs) throws SQLException {
		OperationLog log = new OperationLog();
		log.setClientType(rs.getInt("client_type"));
		log.setClientVersion(rs.getString("client_version"));
		log.setDescription(rs.getString("description"));
		log.setId(rs.getString("id"));
		log.setIp(rs.getString("ip"));
		log.setJsonStr(rs.getString("json_str"));
		log.setLogTime(rs.getTimestamp("log_time"));
		log.setOwnerId(rs.getString("owner_id"));
		log.setOwnerType(rs.getInt("owner_type"));
		log.setParameter(rs.getString("parameter"));
		log.setUnitId(rs.getString("unit_id"));
		log.setUrl(rs.getString("url"));
		log.setUserId(rs.getString("user_id"));
		log.setDomain(rs.getString("domain"));
		return log;
	}

}
