package net.zdsoft.gkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;

public interface GkTeachClassExDao extends BaseJpaRepositoryDao<GkTeachClassEx, String>{

	@Query("From GkTeachClassEx where roundsId = ?1 and teachClassId in (?2)")
	List<GkTeachClassEx> findGkTeachClassExList(String roundId, String[] teachClassIds);
	@Query("From GkTeachClassEx where roundsId = ?1 ")
	List<GkTeachClassEx> findByGkRoundId(String roundId);
	@Modifying
	@Query("delete from GkTeachClassEx where id in (?1)")
	void deleteByIds(String[] ids);

}
