package net.zdsoft.szxy.operation.inner.permission.service;

import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/10 上午9:56
 */
public interface ModuleOperateService {


    List<ModuleOperate> getAllOperates();

    /**
     * 根据模块ID获取每个模块对应的功能点
     * @param moduleIds 模块ID列表
     * @return List
     */
    List<ModuleOperate> getModuleOperatesByModuleIds(String[] moduleIds);
}
