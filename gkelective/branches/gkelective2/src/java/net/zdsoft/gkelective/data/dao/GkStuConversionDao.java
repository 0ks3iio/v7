package net.zdsoft.gkelective.data.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkStuConversion;

public interface GkStuConversionDao extends BaseJpaRepositoryDao<GkStuConversion, String>{
	@Modifying
	@Query("DELETE FROM  GkStuConversion where roundsId = ?1")
	public void deleteByRoundsId(String roundsId);
}
