package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.RhKeyUnit;

public interface RhKeyUnitRemoteService extends BaseRemoteService<RhKeyUnit, String>{
	
	public String findByUnitIdAnduKeyId(String unitId, String uKeyId);
	
	public String findByUkey(String uKeyId);
}
