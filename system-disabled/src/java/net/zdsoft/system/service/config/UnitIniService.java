package net.zdsoft.system.service.config;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.config.UnitIni;

public interface UnitIniService extends BaseService<UnitIni, String> {

	public UnitIni getUnitIni(String unitId, String iniId) ;

    public void updateNowvalue(String nowValue, String iniid,String unitId);
    /**
     * 获取最大id
     * @return
     */
    public long findMaxId();
}
