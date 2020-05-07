package net.zdsoft.bigdata.system.dao;

import java.util.List;

import net.zdsoft.bigdata.system.entity.BgRolePerm;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface BgRolePermDao extends BaseJpaRepositoryDao<BgRolePerm, String> {

	@Query("From BgRolePerm where roleId=?1 ")
	public List<BgRolePerm> findRolePermListByRoleId(String roleId);
	
	@Query(
            value = "from BgRolePerm where roleId in (?1)"
    )
    public List<BgRolePerm> findRolePermListByRoleIds(String[] roleIds);
	
	public void deleteByRoleId(String roleId);

}
