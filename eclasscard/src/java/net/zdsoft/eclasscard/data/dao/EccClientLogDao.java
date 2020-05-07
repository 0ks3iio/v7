package net.zdsoft.eclasscard.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccClientLog;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccClientLogDao extends BaseJpaRepositoryDao<EccClientLog, String>{

	@Modifying
    @Query("delete from EccClientLog  Where creationTime < ?1 ")
	void deleteBeforeByDay(Date date);

}
