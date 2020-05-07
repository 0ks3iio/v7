package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface EccBulletinDao extends BaseJpaRepositoryDao<EccBulletin, String>{

//	@Modifying
//    @Query("update from EccBulletin set status = 3 Where status != 3 and to_char(endTime,'yyyy-MM-dd HH:mm') < ?")
//	public void upadteStatus(String nowTime);

	@Query("From EccBulletin Where status != 2")
	public List<EccBulletin> findListNotShow();

}
