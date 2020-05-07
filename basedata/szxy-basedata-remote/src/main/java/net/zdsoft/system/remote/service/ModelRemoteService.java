package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.server.Model;

public interface ModelRemoteService extends BaseRemoteService<Model, Integer> {

    /**
     * 根据用户ID获取有权限的模块列表
     * 
     * @param userId
     * @return List&lt;Model&gt;
     */
    public String findByUserId(String userId);

    /**
     * 根据用户ID获取有权限的模块列表
     * 
     * @author cuimq
     * @param userId
     * @param ownerType
     * @param unitId
     * @return
     */
    public String findByUserId(String userId, Integer ownerType, String unitId, Integer unitClass);

    /**
     * 根据subId统计需授权的应用订阅数
     * 
     * @param subId
     * @return
     */
    public int countAuthUserBySubId(Integer subId);

    String findListBySubId(Integer subId);
}
