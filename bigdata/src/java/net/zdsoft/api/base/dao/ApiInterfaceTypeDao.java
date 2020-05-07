package net.zdsoft.api.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.api.base.entity.eis.ApiInterfaceType;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ApiInterfaceTypeDao extends BaseJpaRepositoryDao<ApiInterfaceType, String>{

	@Query("From ApiInterfaceType where classify in (?1) ")
	List<ApiInterfaceType> findByClassifyIn(Integer... classify);

}
