package net.zdsoft.szxy.operation.inner.permission.dao;

import net.zdsoft.szxy.operation.inner.permission.entity.GroupModuleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/4/2 下午6:14
 */
@Repository
public interface GroupModuleRelationDao extends JpaRepository<GroupModuleRelation, String> {

    @Query(value = "from GroupModuleRelation where groupId in (?1)")
    List<GroupModuleRelation> getGroupModuleRelationsByGroupIds(String[] groupIds);

    @Query(value = "from GroupModuleRelation where groupId=?1")
    List<GroupModuleRelation> getGroupModuleRelationsByGroupId(String groupId);

    @Query(value = "select moduleId from GroupModuleRelation where groupId in (?1)")
    Set<String> getModuleIdByGroupIds(String[] groupIds);

    @Modifying
    void deleteByGroupId(String groupId);
}
