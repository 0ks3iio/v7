package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccHonor;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface EccHonorDao extends BaseJpaRepositoryDao<EccHonor,String>{

	@Query("From EccHonor where id in (?1) And to_char(createTime,'yyyy-MM-dd') >= ?2 Order by createTime desc")
	public List<EccHonor> findByIdsAndTime(String[] honorIds, String time);
	
	@Query("From EccHonor where status != 3")
	public List<EccHonor> findListNotShow();

	@Query("From EccHonor where id in (?1) Order by createTime desc")
	public List<EccHonor> findByIdsDesc(String[] honorIds);

}
