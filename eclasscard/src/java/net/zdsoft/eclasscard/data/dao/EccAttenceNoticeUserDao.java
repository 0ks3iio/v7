package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeUser;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccAttenceNoticeUserDao extends BaseJpaRepositoryDao<EccAttenceNoticeUser, String>{

	@Query("From EccAttenceNoticeUser Where unitId = ?1 and type = ?2")
	public List<EccAttenceNoticeUser> findByUnitIdAndType(String unitId, String type);

}
