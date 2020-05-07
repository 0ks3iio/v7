package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.OperationLogDataSyncService;
import net.zdsoft.basedata.service.RegionService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.AddressUtils;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.RedisUtils;
import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/common/operationLog")
public class OperationLogAction extends BaseAction {
	
	private Logger logger = Logger.getLogger(OperationLogAction.class);

	@Autowired
	private OperationLogDataSyncService operationLogDataSyncService;
	@Autowired
	private UserService userService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private RegionService regionService;

	@RequestMapping("/initJsonStr")
	public String initLogJsonStr() {
		Jedis jedis = RedisUtils.getJedis();
		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
				List<OperationLog> ls = operationLogDataSyncService.findByJsonIsNullTopN(2000);
				List<String> ids = new ArrayList<>(ls.size());
				List<String> jsonStrs = new ArrayList<>(ls.size());
				for (int i = 0; i < ls.size(); i++) {
					OperationLog l = ls.get(i);
					JSONObject json = dealLog4Json(jedis, l);
					ids.add(l.getId());
					jsonStrs.add(json.toJSONString());
				}
				if (CollectionUtils.isNotEmpty(ls)) {
					operationLogDataSyncService.updateJsonStr(ids.toArray(new String[0]), jsonStrs.toArray(new String[0]));
					logger.info(ids.size() + " logs completed!");
				} else {
					break;
				}
			}
			catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		RedisUtils.returnResource(jedis);
		return returnSuccess();
	}

	private JSONObject dealLog4Json(Jedis jedis, OperationLog log) {
		JSONObject json = new JSONObject();
		JSONObject address = dealWithIp(jedis, log);
		if (address != null) {
			json.putAll(address);
			String userId = log.getUserId();
			if (StringUtils.isNotBlank(userId)) {
				User user = userService.findOne(jedis, userId, RedisUtils.TIME_ONE_WEEK, User.class, new User());
				if (user != null) {
					json.put("user_id", userId);
					json.put("username", user.getUsername());
					json.put("owner_type", user.getOwnerType());
					json.put("name", user.getRealName());
					Date birthday = user.getBirthday();
					if (birthday != null)
						json.put("birthday", DateFormatUtils.format(user.getBirthday(), "yyyy-MM-dd"));
					json.put("sex", user.getSex());
				}
			}
			String unitId = log.getUnitId();
			if (StringUtils.isNotBlank(unitId)) {
				Unit unit = unitService.findOne(jedis, unitId, RedisUtils.TIME_ONE_WEEK, Unit.class, new Unit());
				if (unit != null) {
					json.put("unit_name", unit.getUnitName());
					json.put("unit_id", unit.getId());
					String regionCode = unit.getRegionCode();
					if (StringUtils.isNotBlank(regionCode)) {
						json.put("region_code", regionCode);
						address = anaylizeRegionCode(jedis, regionCode, "unit");
						if (address != null) {
							json.putAll(address);
						}
					}
				}
			}
		}
		return json;
	}

	private JSONObject dealWithIp(Jedis jedis, OperationLog log) {
		String ip = log.getIp();
		JSONObject address = null;
		if (StringUtils.isNotBlank(ip)) {
			String[] ips = StringUtils.split(ip, ",");
			String ipTrue = null;
			if (ips.length == 3) {
				ipTrue = ips[1];
			} else if (ips.length >= 2) {
				// 历史数据兼容，根据不同的内容，解析到正确的ip
				if (StringUtils.countMatches(ip, ",") == 1) {
					ipTrue = ips[0];
				} else {
					ipTrue = ips[1];
				}
			}
			if (StringUtils.isNotBlank(ipTrue)) {
				final String ipTrueFinal = ipTrue;
				JSONObject addr = RedisUtils.getObject(jedis, "Address.By.IP@" + ipTrue, JSONObject.class,
						() -> AddressUtils.getAddresses(ipTrueFinal));
				if (addr != null) {
					addr.put("login_country", JsonUtils.getString(addr, "country"));
					String regionCode = JsonUtils.getString(addr, "region_code");
					address = anaylizeRegionCode(jedis, regionCode, "login");
					if(address == null)
						address = new JSONObject();
					address.putAll(addr);
				}
			}
		}
		return address;
	}

	private JSONObject anaylizeRegionCode(Jedis jedis, String regionCode, String prefix) {
		if (StringUtils.isNotBlank(regionCode) && StringUtils.length(regionCode) == 6) {
			JSONObject addrJson = new JSONObject();
			String provinceCode = StringUtils.rightPad(StringUtils.substring(regionCode, 0, 2), 6, "0");
			String cityCode = StringUtils.rightPad(StringUtils.substring(regionCode, 0, 4), 6, "0");
			String countyCode = regionCode;
			Region region = RedisUtils.getObject(jedis, "Address.By.RegionCode@" + provinceCode, Region.class,
					() -> regionService.findByFullCode(provinceCode));

			addrJson.put(prefix + "_province", region == null ? "" : StringUtils.trimToEmpty(region.getRegionName()));
			if (StringUtils.equals(provinceCode, cityCode))
				addrJson.put(prefix + "_city", "");
			else {
				region = RedisUtils.getObject(jedis, "Address.By.RegionCode@" + provinceCode, Region.class,
						() -> regionService.findByFullCode(cityCode));
				addrJson.put(prefix + "_city", region == null ? "" : StringUtils.trimToEmpty(region.getRegionName()));
			}
			if (StringUtils.equals(cityCode, countyCode))
				addrJson.put(prefix + "_county", "");
			else {
				region = RedisUtils.getObject(jedis, "Address.By.RegionCode@" + provinceCode, Region.class,
						() -> regionService.findByFullCode(countyCode));
				addrJson.put(prefix + "_county", region == null ? "" : StringUtils.trimToEmpty(region.getRegionName()));
			}
			return addrJson;
		}
		return null;
	}

}
