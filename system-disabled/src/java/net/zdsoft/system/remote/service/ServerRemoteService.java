package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.server.Server;

public interface ServerRemoteService extends BaseRemoteService<Server, Integer> {

    /**
     * @param serverCode
     * @return
     */
    String findByServerCode(String serverCode);
    
    /**
     * 根据开发者ID获取应用信息
     * 
     * @param devId
     * @return
     */
    String getAppsByDevId(String devId);
    
    /**
     * 获取应用
     * 
     * @param appId
     * @return
     */
    Server getAppByAppId(int appId);

    /**
     * 查找推荐应用列表
     * 
     * @author cuimq
     * @param ownerType
     * @param unitId
     * @param unitClass
     * @return
     */
    String findByOwnerTypeAndUnitIdAndUnitClass(Integer ownerType, String unitId, Integer unitClass);

    /**
     * 查找没有模块的子系统
     * 
     * @return
     */
    String findNonModelsServer();

    /**
     * 统计应用免费订阅数
     * 
     * @param serverId
     * @return
     */
    int countFreeOrderNum(Integer serverId, Integer[] userTypes);
    
    String findBySubId(Integer subId);

	String findByIndexUrl(String url);

}
