package net.zdsoft.basedata.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.SysUserBind;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface SysUserBindDao extends BaseJpaRepositoryDao<SysUserBind, String>{

	SysUserBind findByUserId(String userId);

	SysUserBind findByRemoteUsername(String remoteUsername);

	@Modifying
    @Query("delete from SysUserBind where userId in (?1)")
	void deleteByUserIdIn(String[] userids);
	
	@Modifying
    @Query("delete from SysUserBind where remoteUserId in (?1)")
	void deleteByRemoteUserIdIn(String[] remoteUserIds);

	SysUserBind findByRemoteUserId(String remoteUserId);


}
