package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.config.UnitIni;


public interface UnitIniRemoteService extends BaseRemoteService<UnitIni,String> {

	public String getUnitIni(String unitId, String iniId) ;

    public void updateNowvalue(String nowValue, String iniid,String unitId);
}
