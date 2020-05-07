package net.zdsoft.bigdata.system.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.bigdata.system.entity.BgUserAuth;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BgUserAuthDao extends BaseJpaRepositoryDao<BgUserAuth, String> {

	@Query("From BgUserAuth order by modifyTime ")
	public List<BgUserAuth> findAllUserList();

	@Modifying
	@Query("update BgUserAuth set status=?2,modifyTime = ?3 Where id = ?1")
	public void updateStatusById(String id, Integer status, Date modifyTime);


	@Modifying
	@Query("update BgUserAuth set isSuperUser=?2,modifyTime = ?3 Where id = ?1")
	public void updateSuperUseryId(String id, Integer isSuperUser, Date modifyTime);
}
