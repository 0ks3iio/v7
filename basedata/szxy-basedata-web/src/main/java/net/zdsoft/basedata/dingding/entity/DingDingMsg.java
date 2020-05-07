package net.zdsoft.basedata.dingding.entity;

import net.zdsoft.framework.entity.BaseEntity;

public class DingDingMsg extends BaseEntity<String>{

	private static final long serialVersionUID = 2854801145174776478L;

	@Override
	public String fetchCacheEntitName() {
		return "dingdingMsg";
	}

}
