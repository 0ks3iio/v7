package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceType;

public interface OpenApiInterfaceTypeDao extends BaseJpaRepositoryDao<OpenApiInterfaceType, String>{

	@Query("From OpenApiInterfaceType where classify in (?1) ")
	List<OpenApiInterfaceType> findByClassifyIn(Integer... classify);

}
