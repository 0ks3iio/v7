package net.zdsoft.szxy.operation.inner.permission.dao;

import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/4/2 下午6:25
 */
@Repository
public interface UserModuleRelationDao extends JpaRepository<UserModuleRelation, String> {

    @Query(value = "select moduleId from UserModuleRelation where userId=?1")
    Set<String> getModuleIdByUserId(String userId);

    @Query(value = "from UserModuleRelation where userId=?1")
    List<UserModuleRelation> getUserModuleRelationsByUserId(String userId);

    /**
     * 删除指定用户的模块权限
     * @param userId 用户ID
     */
    @Modifying
    void deleteByUserId(String userId);
}
