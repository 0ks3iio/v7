package net.zdsoft.syncdatamq.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.syncdatamq.entity.SymDataModifyTime;

public interface SymDataModifyTimeService extends BaseService<SymDataModifyTime, String> {

	public void dealReceive();

	public String findData(String entityName, String... ids);

	public String findData(String entityName, Date modifyTime);

	public String findDatas(String tableName, Map<String, Object> params);

	public String findData(String entityName, Date modifyTime, String modifyTimeName, int fetchSize);

	public boolean saveSendSymData(String entityName, String clientId, String timeColumnName,
			String orderByColumnName, JdbcTemplate jdbcTemplate, JSONObject symDataJson) throws ParseException;

	public boolean saveReceiveSymData(String receiveTableName, String dataTableName, String clientId, JdbcTemplate jdbcTemplate, String dbDriver, JSONObject dataJson)
			throws Exception;

}
