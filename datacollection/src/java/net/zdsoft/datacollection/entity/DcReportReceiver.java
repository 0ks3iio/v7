package net.zdsoft.datacollection.entity;

import net.zdsoft.framework.entity.BaseEntity;

public class DcReportReceiver extends BaseEntity<String>{
	
	private String reportId;
	private Integer receiverType;
	// 接收者范围， 1=全校；2=
	private String receiverRange;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	
}
