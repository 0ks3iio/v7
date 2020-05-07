package net.zdsoft.system.service.server;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.SysPlatformModel;
import net.zdsoft.system.entity.server.Model;

/**
 * Created by shenke on 2017/3/7.
 */
public interface SysPlatformModelService extends BaseService<SysPlatformModel, Integer> {

    /**
     * 根据使用平台查找
     * 
     * @param platform
     *            -1
     * @return
     */
    List<SysPlatformModel> findByPlatform(Integer platform);

    /**
     * 根据使用平台查找授权模块信息
     * 
     * @author cuimq
     * @param platform
     * @return
     */
    List<Model> findModelsByPlatform(Integer platform);
}
