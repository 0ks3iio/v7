package net.zdsoft.system.service.config;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.config.UnitIni;

public interface UnitIniService extends BaseService<UnitIni, String> {

    UnitIni getUnitIni(String unitId, String iniId) ;

    void updateNowvalue(String nowValue, String iniid,String unitId);
    /**
     * 获取最大id
     * @return
     */
    long findMaxId();

    /**
     * 初始化单位的默认配置参数
     * @param unitId
     */
    void initUnitDefaultOptions(String unitId);

    void deleteUnitInisByUnitId(String unitId);

	List<UnitIni> getIniList(String iniId);
}
