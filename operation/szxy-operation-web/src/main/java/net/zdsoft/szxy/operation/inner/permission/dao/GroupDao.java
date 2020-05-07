package net.zdsoft.szxy.operation.inner.permission.dao;

import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/2 下午6:08
 */
@Repository
public interface GroupDao extends JpaRepository<Group, String> {

    @Query(value = "from Group where id in (?1)")
    List<Group> getGroupsByIds(String[] ids);

    @Modifying
    @Query(value = "update Group set name=?1 where id=?2")
    void updateGroupName(String groupName, String id);

    @Modifying
    @Query(value = "update Group set regionCode=?1 where id=?2")
    void updateRegionCode(String regionCode, String id);
}
