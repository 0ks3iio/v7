package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccHonorTo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EccHonorToDao extends BaseJpaRepositoryDao<EccHonorTo,String>{

	public List<EccHonorTo> findByObjectIdIn(String[] objectIds);

	public List<EccHonorTo> findByHonorIdIn(String[] honorIds);

	@Modifying
	@Query("delete from EccHonorTo where honorId = ?1")
	public void deleteByHonorId(String id);

}
