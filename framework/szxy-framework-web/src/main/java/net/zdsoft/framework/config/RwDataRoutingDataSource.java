package net.zdsoft.framework.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RwDataRoutingDataSource extends AbstractRoutingDataSource {

	private Object writeDataSource; // 写数据源

	private Object readDataSource; // 读数据源

	public void setSlaves(Integer slaves) {
	}

	@Override
	protected Object determineCurrentLookupKey() {
		DynamicDataSourceGlobal dynamicDataSourceGlobal = DynamicDataSourceHolder.getDataSource();

		if (dynamicDataSourceGlobal == null || dynamicDataSourceGlobal == DynamicDataSourceGlobal.READ) {
			return DynamicDataSourceGlobal.READ.name();
		}

		return DynamicDataSourceGlobal.WRITE.name();
	}

}
