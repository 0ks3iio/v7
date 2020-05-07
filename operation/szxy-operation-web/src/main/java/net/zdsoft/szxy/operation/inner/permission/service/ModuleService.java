package net.zdsoft.szxy.operation.inner.permission.service;

import net.zdsoft.szxy.operation.inner.permission.entity.Module;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/9 下午1:17
 */
public interface ModuleService {

    /**
     * 获取所有的模块
     * @return list
     */
    List<Module> getAllModules();

    List<Module> getModulesByIds(String[] ids);

}
