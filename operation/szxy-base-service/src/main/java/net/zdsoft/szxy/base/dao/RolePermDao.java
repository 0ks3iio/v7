package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.RolePerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午4:50
 */
@Repository
public interface RolePermDao extends JpaRepository<RolePerm, String> {

    /**
     * 根据角色ID查询 role_perm
     * @param roleIds 角色ID
     * @return
     */
    @Query(
            value = "from RolePerm where roleId in (?1)"
    )
    List<RolePerm> getRolePermsByRoleId(String[] roleIds);

}
