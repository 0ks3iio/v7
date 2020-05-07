package net.zdsoft.gkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;

public interface GkTeachClassStoreDao extends BaseJpaRepositoryDao<GkTeachClassStore, String>{

	@Modifying
	@Query("DELETE FROM GkTeachClassStore WHERE id in (?1)")
	public void deleteByids(String[] ids);

	@Query("FROM GkTeachClassStore WHERE roundsId = ?1 ")
	public List<GkTeachClassStore> findByRoundsId(String roundsId);

}
