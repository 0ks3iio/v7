package net.zdsoft.szxy.operation.inner.permission.service;

import net.zdsoft.szxy.operation.inner.permission.entity.Module;
import net.zdsoft.szxy.operation.inner.permission.dto.OperateAuth;

import java.util.List;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/3/30 下午3:31
 */
public interface InnerPermissionService {

    /**
     * 获取指定用户所有可用的模块
     * @param userId 用户ID
     * @return List
     */
    List<Module> getModuleByUserId(String userId);

    /**
     * 获取指定用户所有可用的功能点数据
     * @param userId 用户ID
     * @return
     */
    Set<OperateAuth> getOperateByUserId(String userId);

    /**
     * 获取指定用户负责的行政区划的集合
     * @param userId 用户ID
     * @return
     */
    Set<String> getAuthRegionCodes(String userId);
}
