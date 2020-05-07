package net.zdsoft.szxy.operation.inner.permission.service;

import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/9 下午2:07
 */
public interface UserModuleRelationService {

    /**
     * 获取指定用户的模块、功能点数据
     * @param userId 用户ID
     * @return
     */
    List<UserModuleRelation> getRelationsByUserId(String userId);

    /**
     * 更新指定用户的权限
     * @param userModuleRelations 新的权限关系数据
     * @param userId 指定的用户ID
     */
    void updateUserModules(List<UserModuleRelation> userModuleRelations, String userId);
}
