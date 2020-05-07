package net.zdsoft.basedata.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.dao.OperationLogDao;
import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.OperationLogDataSyncService;
import net.zdsoft.basedata.service.RegionService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.AddressUtils;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import redis.clients.jedis.Jedis;

@Service
public class OperationLogDataSyncServiceImpl extends
		BaseServiceImpl<OperationLog, String> implements
		OperationLogDataSyncService {

	@Autowired
	private OperationLogDao operationLogDao;

	@Autowired
	private UserService userService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private RegionService regionService;

	@Override
	protected BaseJpaRepositoryDao<OperationLog, String> getJpaDao() {
		return operationLogDao;
	}
	
	@Override
	public List<OperationLog> findByJsonIsNullTopN(int top) {
		return operationLogDao.findByJsonIsNullTopN(top);
	}



	@Override
	protected Class<OperationLog> getEntityClass() {
		return OperationLog.class;
	}
	
	@Override
	public void insertLog(OperationLog... logs) {
		operationLogDao.insertLog(logs);
	}

	@Override
	public void updateJsonStr(String[] ids, String[] jsonStrs) {
		operationLogDao.updateJsonStr(ids, jsonStrs);
	}

	@Override
	public List<Object[]> findUsageFunctions(String[] userIds, int topN,
			Date fromDate) {
		return operationLogDao.findUsageFunctions(userIds, topN, fromDate);
	}

	@Override
	public List<Object[]> findUsageServers(String[] userIds, int topN,
			Date fromDate) {
		return operationLogDao.findUsageServers(userIds, topN, fromDate);
	}

	@Override
	public int saveLog() {
		Jedis jedis = RedisUtils.getJedis();
		List<OperationLog> logs = new ArrayList<>();
		try {
			Calendar c = Calendar.getInstance();
			String day = DateFormatUtils.format(c, "yyyy-MM-dd");
			c.add(Calendar.DAY_OF_YEAR, -1);
			String yestoday = DateFormatUtils.format(c, "yyyy-MM-dd");
			int max = 300;
			for (int i = 0; i < max; i++) {
				try {
					String s = RedisUtils.lpop("operationLog");
					// 7.0中没有日志，则获取6.0的日志（6.0是按照日期来存放的）
					if (StringUtils.isBlank(s) || StringUtils.length(s) <= 2) {
						s = RedisUtils.lpop("operation.log." + day);
					}
					// 7.0中没有日志，则获取6.0的前一天日志（为了避免正好是0点的时候，漏取了日志信息）
					if (StringUtils.isBlank(s) || StringUtils.length(s) <= 2) {
						s = RedisUtils.lpop("operation.log." + yestoday);
					}
					if (StringUtils.isBlank(s) || StringUtils.length(s) <= 2) {
						break;
					}
					OperationLog log = SUtils.dc(s, OperationLog.class);
					if (log != null) {
						JSONObject json = dealLog4Json(jedis, log);
						log.setJsonStr(json.toJSONString());
						if (StringUtils.isBlank(log.getId())) {
							log.setId(UuidUtils.generateUuid());
						}
						logs.add(log);
					} else {
						break;
					}
				} catch (Exception e) {
					break;
				}
			}
		} finally {
			RedisUtils.returnResource(jedis);
		}
		if (CollectionUtils.isNotEmpty(logs))
			operationLogDao.insertLog(logs.toArray(new OperationLog[0]));

		return logs.size();
	}

	private JSONObject dealLog4Json(Jedis jedis, OperationLog log) {
		JSONObject json = new JSONObject();
		JSONObject address = dealWithIp(jedis, log);
		if (address != null) {
			json.putAll(address);
		}
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

	@Override
	public int findAppPopularity(String parameter) {
		return operationLogDao.findAppPopularity(parameter);
	}

	@Override
	public List<OperationLog> findByDescription(String description) {
		return operationLogDao.findByDescription(description + "%");
	}

	@Override
	public List<OperationLog> findByParameterAndDescription(String ticketKey,
			String description) {
		return operationLogDao.findByParameterAndDescription(ticketKey,
				description);
	}

	@Override
	public List<OperationLog> findAllByType(String openapiDescription) {
		// TODO Auto-generated method stub
		return findAllByType(null, openapiDescription);
	}

	@Override
	public List<OperationLog> findAllByType(String ticketKey, String type) {
		// TODO Auto-generated method stub
		return findCountBySpace(ticketKey, type, null, null);
	}

	@Override
	public List<OperationLog> findCountBySpace(String ticketKey, String type,
			Date start, Date end) {
		// TODO Auto-generated method stub
		return findAll(new Specification<OperationLog>() {

			private static final long serialVersionUID = -7260417850535358978L;

			@Override
			public Predicate toPredicate(Root<OperationLog> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				if (ticketKey != null) {
					ps.add(cb.equal(root.get("parameter").as(String.class),
							ticketKey));
				}
				if (type.equals("开发者调用接口：")) {
					ps.add(cb.like(root.<String> get("description"), "%" + type
							+ "%"));
				} else {
					ps.add(cb.like(root.<String> get("description"), type + "%"));
				}
				if (start != null) {
					ps.add(cb.greaterThanOrEqualTo(
							root.<Timestamp> get("logTime"), start));
				}
				if (end != null) {
					ps.add(cb.lessThanOrEqualTo(
							root.<Timestamp> get("logTime"),
							DateUtils.addDays(end, 1)));
				}
				cq.where(ps.toArray(new Predicate[0]));
				return cq.getRestriction();
			}
		});
	}

	public List<OperationLog> findListByUrl(String url, Date beginDate,
			Date endDate) {
		return operationLogDao.findListByUrl(url, beginDate, endDate);
	}

	public List<OperationLog> findListByDate(Date beginDate, Date endDate) {
		return operationLogDao.findListByDate(beginDate, endDate);
	}
}