package net.zdsoft.background.common;

import java.util.ArrayList;
import java.util.List;

public class ServicesConfig {
	private List<ServiceConfig> serviceConfigs = new ArrayList<ServiceConfig>();

	public void addServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfigs.add(serviceConfig);
	}

	public List<ServiceConfig> getServiceConfigs() {
		return this.serviceConfigs;
	}
}
