package net.zdsoft.basedata.component;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SyncTriggerDataService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.config.LocalCacheUtils;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.SyncTriggerData;
import net.zdsoft.framework.utils.EntityUtils;

@Lazy(false)
@Component
public class SyncCacheDataJob {

	private static final Logger log = Logger.getLogger(SyncCacheDataJob.class);

	private static final int MONITOR_DURATION = 2;

	static Map<String, Date> cacheDataTime = new HashMap<>();

	static {
//		String cacheEntities = Evn.getString("cache_entities");
//		if (StringUtils.isNotBlank(cacheEntities))
//			System.out.println("本地缓存对象：" + cacheEntities);
//		new Thread(new TimeoutTimerThread()).start();
	}

	static class TimeoutTimerThread implements Runnable {
		public void run() {
			while (true) {
				try {
					int refreshTime = Evn.getInt("cache_refresh_time");
					TimeUnit.SECONDS.sleep(refreshTime == 0 ? MONITOR_DURATION : refreshTime);
					checkTime();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}

		/**
		 * 过期缓存的具体处理方法
		 * 
		 * @throws Exception
		 */
		private void checkTime() throws Exception {
			String cacheEntities = Evn.getString("cache_entities");
			if (StringUtils.isBlank(cacheEntities))
				return;
			SyncTriggerDataService syncTriggerDataService = Evn.getBean("syncTriggerDataService");
			Calendar c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) % 5 == 0 && c.get(Calendar.SECOND) < MONITOR_DURATION) {
				c.add(Calendar.MINUTE, -10);
				// 第一次启动，将10分钟之前变化数据加载到本地缓存
				syncTriggerDataService.deleteByCreationTimeBefore(c.getTime());
			}
			List<SyncTriggerData> datas = syncTriggerDataService.findAll();
			for (String objectType : cacheEntities.split(",")) {
				String column = null;
				// 有些不是默认id，而是其他字段，譬如系统参数表中的optionCode
				if (StringUtils.contains(objectType, "(") && StringUtils.contains(objectType, ")")) {
					column = StringUtils.substringBetween(objectType, "(", ")");
					objectType = StringUtils.substringBefore(objectType, "(");
				}
				String serviceName;
				if (objectType.equals("Clazz")) {
					serviceName = "classService";
				} else {
					serviceName = objectType.substring(0, 1).toLowerCase() + objectType.substring(1) + "Service";
				}
				BaseService baseService = Evn.getBean(serviceName);
				if (baseService == null)
					continue;
				Map<String, List<SyncTriggerData>> listMap = EntityUtils.getListMap(datas, SyncTriggerData::getDataType,
						Function.identity());
				List<SyncTriggerData> dataList = listMap.get(objectType);
				if (dataList == null)
					continue;

				final String objectTypeFile = objectType;
				dataList = dataList.stream().filter(x -> {
					Date date = x.getCreationTime();
					Date dateRecord = cacheDataTime.get(objectTypeFile);
					if (dateRecord == null)
						return true;
					return date.compareTo(dateRecord) > 0;
				}).sorted(Comparator.comparing(SyncTriggerData::getCreationTime).reversed())
						.collect(Collectors.toList());
				Set<String> ids = new HashSet<>();
				for (SyncTriggerData data : dataList) {
					String dataId = data.getDataId();
					if (ids.contains(dataId))
						continue;
					ids.add(dataId);

					BaseEntity u = baseService.findOneFromDB(baseService.convertKey(dataId.trim()));
					if (u != null) {
						if (StringUtils.isBlank(column)) {
							LocalCacheUtils.putValue(objectType, String.valueOf(u.getId()), u);
						} else {
							LocalCacheUtils.putValue(objectType, EntityUtils.getValue(u, column), u);
						}
						cacheDataTime.put(objectType, data.getCreationTime());
					}
				}
			}
		}
	}

}
