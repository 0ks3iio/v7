package net.zdsoft.background.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.keel.util.ObjectUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.log4j.Logger;

public class ServiceManager {
	private static Logger logger = Logger.getLogger(ServiceManager.class);
	
	private final ServicesConfig servicesConfig;
	private final List<AbstractService> serviceList = new ArrayList<AbstractService>();
	private static ServiceManager instance = new ServiceManager();

	public static ServiceManager getInstance() {
		return instance;
	}

	private ServiceManager() {
		this.servicesConfig = loadConfig("serviceConfig.xml");
	}

	public ServicesConfig loadConfig(String serviceConfigFile) {
		return parseConfig(new ConfigRuleSet(), serviceConfigFile);
	}

	private ServicesConfig parseConfig(RuleSet ruleSet, String configName) {
		Digester digester = new Digester();
		digester.addRuleSet(ruleSet);
		InputStream in = null;
		try {
			in = new BufferedInputStream (new FileInputStream(configName));
			return (ServicesConfig) digester.parse(in);
		} catch (Exception e) {
			logger.error("Parse config file error", e);
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e1) {
				logger.error("Close stream error", e1);
			}
		}
	}

	public void startAllService() {
		for (ServiceConfig serviceConfig : this.servicesConfig.getServiceConfigs()) {
			if (!"false".equals(serviceConfig.getParamValue("enabled"))) {
				logger.info("Starting service: " + serviceConfig.getName());
				AbstractService service = (AbstractService) ObjectUtils.createInstance(serviceConfig.getClassName());
				for (String paramName : serviceConfig.getParamNames()) {
					try {
						BeanUtils.setProperty(service, paramName,serviceConfig.getParamValue(paramName));
					} catch (Throwable e) {
						logger.warn(serviceConfig.getName()+ " Could not setProperty[" + paramName + "]");
					}
				}
				this.serviceList.add(service);
				service.setServiceConfig(serviceConfig);
				service.start();
				if (serviceConfig.getServiceInterval() > 0) {
					Thread thread = new Thread(service, service.getName());
					thread.start();
				}
			}
		}
		logger.info("All services started");
	}

	public List<AbstractService> getServiceList() {
		return this.serviceList;
	}

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		getInstance().startAllService();
	}
}
