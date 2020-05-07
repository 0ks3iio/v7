package net.zdsoft.bigdata.dataimport.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.bigdata.dataimport.entity.ImportErrorInfo;
import net.zdsoft.bigdata.dataimport.utils.ExcelExportUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;

import redis.clients.jedis.Jedis;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.support.ExcelTypeEnum;

public abstract class AbstractImportListener extends
		AnalysisEventListener<List<String>> {

	// 临时生成随机的UUID
	public String businessId;

	public String unitId;

	public String userId;

	public long startTime;

	public String filePath;

	public int totalSize = 0;

	// 字段映射Map
	public Map<Integer, String> reflectMap = new HashMap<Integer, String>();

	// 字段映射Map
	public Map<String, String> columnMap = new HashMap<String, String>();

	// 头信息Map
	public Map<String, Integer> headDataMap = new HashMap<String, Integer>();

	public void invoke(List<String> object, AnalysisContext context) {
		// 标题处理
		if (context.getCurrentRowNum() == 0) {
			if (object != null && !StringUtils.isEmpty(object.get(0))) {
				int count = 0;
				for (String s : object) {
					if (!StringUtils.isEmpty(s)) {
						headDataMap.put(s, count);
					}
					count++;
				}
			}
			// 字段映射
			if (!reflect()) {
				RedisUtils.set("event-import-status-" + businessId, "error");
				RedisUtils.set("event-import-progress-" + businessId, "导入文件列名不匹配");
				removeBusinessData();
				throw new ExcelAnalysisException("导入文件列名不匹配");
			}
		} else {
			if (object != null && !StringUtils.isEmpty(object.get(0))) {
				Json data = new Json();
				for (int i = 0; i < object.size(); i++) {
					data.put(reflectMap.get(i), object.get(i));
				}
				data.put("rowNum", context.getCurrentRowNum());
				data.put("originalData", object);
				RedisUtils.lpush("event-import-data-" + businessId,
						data.toJSONString());
				totalSize++;
			}
		}
	}

	public void doAfterAllAnalysed(AnalysisContext context) {
		// 业务处理
		dealBiz();
	}

	public boolean reflect() {
		for (String key : headDataMap.keySet()) {
			if (columnMap.containsKey(key)) {
				reflectMap.put(headDataMap.get(key), columnMap.get(key));
			} else {
				return false;
			}
		}
		return true;
	}

	public String saveErrorMsg(String customFileFolder, ExcelTypeEnum excelType)
			throws Exception {
		SysOptionRemoteService sysOptionRemoteService = (SysOptionRemoteService) Evn
				.getBean("sysOptionRemoteService");
		String systemFilePath = sysOptionRemoteService
				.findValue(Constant.FILE_PATH);
		if (StringUtils.isEmpty(customFileFolder))
			customFileFolder = "default";
		String fileName = businessId + excelType.getValue();
		String filePath = systemFilePath + java.io.File.separator + "import"
				+ java.io.File.separator + "error" + java.io.File.separator
				+ customFileFolder;
		String fullFilePath = systemFilePath + java.io.File.separator
				+ "import" + java.io.File.separator + "error"
				+ java.io.File.separator + customFileFolder
				+ java.io.File.separator + fileName;

		Jedis jedis = RedisUtils.getJedis();
		List<ImportErrorInfo> errorDatas = new ArrayList<ImportErrorInfo>();
		try {
			while (true) {
				try {
					String s = RedisUtils.rpop("event-import-error-"
							+ businessId);
					if (StringUtils.isEmpty(s)) {
						break;
					}
					ImportErrorInfo errorInfo = SUtils.dc(s,
							ImportErrorInfo.class);
					errorDatas.add(errorInfo);
				} catch (Exception e) {
					break;
				}
			}
		} finally {
			RedisUtils.returnResource(jedis);
		}
		if (CollectionUtils.isNotEmpty(errorDatas)) {
			// 写入excel文件
			ExcelExportUtils.export(fileName, filePath, 3, errorDatas,
					ImportErrorInfo.class);
			return fullFilePath;
		}
		return StringUtils.EMPTY;
	}

	public void removeBusinessData() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(60000);
					RedisUtils.del("event-import-progress-" + businessId);
					RedisUtils.del("event-import-status-" + businessId);
					RedisUtils.del("event-import-detail-" + businessId);
					RedisUtils.del("event-import-data-" + businessId);
					RedisUtils.del("event-import-error-" + businessId);
				} catch (InterruptedException e) {
				}
			}
		}.start();
	}

	// 处理业务
	public abstract void dealBiz();

	// 字段
	public abstract void setColumns();
}
