package net.zdsoft.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface OpenApiInterfaceTypeDao extends BaseJpaRepositoryDao<OpenApiInterfaceType, String>{

	@Query("From OpenApiInterfaceType where classify in (?1) ")
	List<OpenApiInterfaceType> findByClassifyIn(Integer... classify);

}
