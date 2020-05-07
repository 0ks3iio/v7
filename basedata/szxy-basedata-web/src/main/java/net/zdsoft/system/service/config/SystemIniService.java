package net.zdsoft.system.service.config;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.config.SystemIni;

import java.util.List;

public interface SystemIniService extends BaseService<SystemIni, String> {

    /**
     * 缓存五分钟
     * 
     * @param redisActionOpen
     * @return
     */
    SystemIni findOneByIniid(String iniid);

    SystemIni saveOne(SystemIni systemIni);

    /**
     * 缓存中获取
     * 
     * @param iniid
     * @return
     */
    String findValue(String iniid);

    List<SystemIni> getSystemIniByViewable(Integer viewable);

    // 刷新缓存
    void doRefreshCache(String... iniid);

    // 刷新缓存
    void doRefreshCacheAll();

    // 更新nowValue值
    void updateNowvalue(String nowvalue, String iniid);

    // 更新nowValue值类型
    void updateValueType(int valueType, String iniid);

}
