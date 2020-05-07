package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午4:46
 */
@Repository
public interface RoleDao extends JpaRepository<Role, String> {

    /**
     * 获取指定单位的角色列表
     * @param unitId 单位ID
     * @return
     */
    @Query(value = "from Role where unitId=?1")
    List<Role> getRolesByUnitId(String unitId);
}
