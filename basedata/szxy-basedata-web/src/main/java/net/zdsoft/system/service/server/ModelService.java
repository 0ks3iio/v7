package net.zdsoft.system.service.server;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.server.Model;

import java.util.List;

public interface ModelService extends BaseService<Model, Integer> {

    List<Model> findBySubSystemId(Integer subSystem);

    @Override
    List<Model> findAll();

    Model findByIntId(Integer id);
    
    List<Model> findByUnitClass(Integer unitClass);

    /**
     * 获取用户有权限的模块列表
     * 
     * @param userId
     * @return
     */
    List<Model> findByUserId(String userId);

    /**
     * 根据应用id和父节点id查找应用
     * 
     * @author cuimq
     * @param serverIds
     * @param parentIds
     * @return
     */
    List<Model> findBySubSystemIdsAndParentIdsAndUnitClass(Integer[] serverIds, Integer[] parentIds, Integer unitClass);

    /**
     * 根据用户及用户单位信息查找所有已授权模块或默认授权模块
     * 
     * @author cuimq
     * @param userId
     * @param unitId
     * @param unitClass
     * @return
     */
    List<Model> findAllEnableModelByUser(String userId, String unitId, Integer unitClass);
}
