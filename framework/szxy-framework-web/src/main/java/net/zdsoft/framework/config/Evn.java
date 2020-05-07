package net.zdsoft.framework.config;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;

import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;

@Lazy
public class Evn {
	private static Logger log = Logger.getLogger(Evn.class);

	private static ApplicationContext application;

	private static Map<String, String> map = new HashMap<>();
	private static Map<String, String> mapO = new HashMap<>();

	private static SchedulerFactory schedulerfactory = new StdSchedulerFactory();
	private static Scheduler scheduler = null;

	public static String getString(String key) {
		if (!BooleanUtils.toBoolean(map.get("fw.devModel"))) {
			String verifyKey = System.getProperty(key);
			if (StringUtils.isNotBlank(verifyKey)) {
				return verifyKey;
			}
		}
		return map.get(key);
	}

	public static int getInt(String key) {
		String s = getString(key);
		return NumberUtils.toInt(s);
	}

	public static boolean getBoolean(String key) {
		String s = getString(key);
		return StringUtils.isNotBlank(s) && ArrayUtils.contains(new String[] { "1", "true" }, s.toLowerCase());
	}

	public static boolean isDevModel() {
		return BooleanUtils.toBoolean(getString("fw.devModel"));
	}

	public static boolean isRedisEnable() {
		return BooleanUtils.toBoolean(getString("fw.redis.enable"));
	}

	public static boolean isMemcachedEnable() {
		return BooleanUtils.toBoolean(getString("fw.memcached.enable"));
	}

	public static boolean isPassport() {
		return getBoolean("connection_passport");
	}

	public static String getWebUrl() {
		return getString(Constant.EIS_WEB_URL);
	}

	public static String getLogOutUrl() {
		return getString(Constant.UNIFY_LOGOUT_URL);
	}

	public static boolean isScheduler() {
		return "true".equals(getString(Constant.EIS_SCHEDULER_START));
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		T t = null;
		if (application.containsBean(name)) {
			t = (T) application.getBean(name);
		} else if (application.containsBean(name + "Impl"))
			t = (T) application.getBean(name + "Impl");
		return t;
	}

	public static <T> Map<String, T> getBeans(Class<T> clazz) {
		return application.getBeansOfType(clazz);
	}

	public static <T> Class<T> convert(TypeReference<T> ref) {
		return (Class<T>) ((ParameterizedType) ref.getType()).getRawType();
	}

	public static <T extends BaseEntity<K>, K extends Serializable> void syncBasedataUpdate(Class<T> clazz, T t) {
		Map<String, SyncBasedataService> beansOfType = Evn.getBeans(SyncBasedataService.class);
		for (SyncBasedataService service : beansOfType.values()) {
			String className = EntityUtils.getGenericClassName(service.getClass().getSuperclass());
			if (StringUtils.equals(clazz.getSimpleName(), className)) {
				String msg = service.updateEntity(t);
				if (StringUtils.isNotBlank(msg))
					throw new RuntimeException(msg);
				break;
			}
		}
	}

	public static <T extends BaseEntity, K extends Serializable> void syncBasedataAdd(
			Class<SyncBasedataService<T, K>> clazz, T t) {
		Map<String, SyncBasedataService<T, K>> beansOfType = Evn.getBeans(clazz);
		for (SyncBasedataService service : beansOfType.values()) {
			String className = EntityUtils.getGenericClassName(service.getClass().getSuperclass());
			if (StringUtils.equals(clazz.getSimpleName(), className)) {
				String msg = service.addEntity(t);
				if (StringUtils.isNotBlank(msg))
					throw new RuntimeException(msg);
				break;
			}
		}
	}

	public static <T extends BaseEntity, K extends Serializable> void syncBasedataDelete(
			Class<T> clazz, K... k) {
		Map<String, SyncBasedataService> beansOfType = Evn.getBeans(SyncBasedataService.class);
		//先全部验证
		for (SyncBasedataService service : beansOfType.values()) {
			String className = EntityUtils.getGenericClassName(service.getClass().getSuperclass());
			if (StringUtils.equals(clazz.getSimpleName(), className)) {
				//String msg = service.deleteEntity(k);
				String msg = service.preDel(k);
				if (StringUtils.isNotBlank(msg))
					throw new RuntimeException(msg);
				break;
			}
		}
		//后统一删除
		for (SyncBasedataService service : beansOfType.values()) {
			String className = EntityUtils.getGenericClassName(service.getClass().getSuperclass());
			if (StringUtils.equals(clazz.getSimpleName(), className)) {
				service.deleteEntity(k);
			}
		}
	}

	/**
	 * 增加任务
	 * 
	 * @param job
	 * @param trigger
	 * @param runOnlyWithOption
	 *            是否只是在导入那台服务器运行任务
	 */
	public static void addJob(JobDetail job, Trigger trigger, boolean runOnlyWithOption) {
		boolean runAble = !runOnlyWithOption || isScheduler();
		if (runAble) {
			try {
				if (scheduler == null) {
					scheduler = schedulerfactory.getScheduler();
					scheduler.start();
				}
				scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e1) {
				log.info("任务调度失败," + job.getDescription());
			}
		}

	}

	public static void addJob(JobDetail job, Trigger trigger) {
		addJob(job, trigger, true);
	}

	public static Map<String, String> getMap() {
		return map;
	}

	public static Map<String, String> getMapO() {
		return mapO;
	}

	public static ApplicationContext getApplication() {
		return application;
	}

	public static SchedulerFactory getSchedulerfactory() {
		return schedulerfactory;
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}

	public static void setScheduler(Scheduler scheduler) {
		Evn.scheduler = scheduler;
	}

	public static void setApplication(ApplicationContext application) {
		Evn.application = application;
	}

	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static String getCreeplanDjStatic() {
		return getString("creeplan_DjStatic");
	}

	public static boolean isSaidian() {
		String ss=getString("creeplan_address");
		if("1".equals(ss)) {
			return true;
		}
		return  false;
	}
}
