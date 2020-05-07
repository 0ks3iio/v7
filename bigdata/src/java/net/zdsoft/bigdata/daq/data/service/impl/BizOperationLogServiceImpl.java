package net.zdsoft.bigdata.daq.data.service.impl;

import net.zdsoft.bigdata.daq.data.service.BizOperationLogService;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service("bizOperationLogService")
public class BizOperationLogServiceImpl implements BizOperationLogService {

	private static final String BIZ_OPERATION_LOG_INDEX = "biz_operation_log";
	private static final String MODULE_LOG_INDEX = "module_operation_log";
	private static final String SQL_ANALYSE_INDEX = "sql_analyse_log";

	@Autowired
	private EsClientService esClientService;

	@Override
	public void saveBizOperationLog() {
		if (!esClientService.isIndexExists(BIZ_OPERATION_LOG_INDEX)) {
			esClientService.createBizOperationIndex();
		}
		Jedis jedis = RedisUtils.getJedis();
		List<String> logs = new ArrayList<>();
		try {
			int max = 300;
			for (int i = 0; i < max; i++) {
				try {
					String s = RedisUtils.lpop("bizOperationLog");
					if (StringUtils.isBlank(s)) {
						break;
					}
					logs.add(s);
				} catch (Exception e) {
					break;
				}
			}
		} finally {
			RedisUtils.returnResource(jedis);
		}
		if (CollectionUtils.isNotEmpty(logs)) {
			esClientService.insertDatas(BIZ_OPERATION_LOG_INDEX,
					"business", logs);
		}
	}
	
	@Override
	public void saveModuleOperationLog() {
		if (!esClientService.isIndexExists(MODULE_LOG_INDEX)) {
			esClientService.createModuleOperationIndex();
		}
		Jedis jedis = RedisUtils.getJedis();
		List<String> logs = new ArrayList<>();
		try {
			int max = 300;
			for (int i = 0; i < max; i++) {
				try {
					String s = RedisUtils.lpop("bgmoduleOperationLog");
					if (StringUtils.isBlank(s)) {
						break;
					}
					logs.add(s);
				} catch (Exception e) {
					break;
				}
			}
		} finally {
			RedisUtils.returnResource(jedis);
		}
		if (CollectionUtils.isNotEmpty(logs)) {
			esClientService.insertDatas(MODULE_LOG_INDEX, MODULE_LOG_INDEX,
					logs);
		}
	}

	@Override
	public void saveSqlAnalyseLog() {
		if (!esClientService.isIndexExists(SQL_ANALYSE_INDEX)) {
			esClientService.createSqlAnalyseIndex();
		}
		Jedis jedis = RedisUtils.getJedis();
		List<String> logs = new ArrayList<>();
		try {
			int max = 300;
			for (int i = 0; i < max; i++) {
				try {
					String s = RedisUtils.lpop("sqlAnalyseLog");
					if (StringUtils.isBlank(s)) {
						break;
					}
					logs.add(s);
				} catch (Exception e) {
					break;
				}
			}
		} finally {
			RedisUtils.returnResource(jedis);
		}
		if (CollectionUtils.isNotEmpty(logs)) {
			esClientService.insertDatas(SQL_ANALYSE_INDEX,
                    SQL_ANALYSE_INDEX, logs);
		}
	}

}
