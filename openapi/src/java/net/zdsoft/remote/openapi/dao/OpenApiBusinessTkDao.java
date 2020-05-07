package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiBusinessTk;

public interface OpenApiBusinessTkDao extends BaseJpaRepositoryDao<OpenApiBusinessTk,String>{

	@Query("From OpenApiBusinessTk where ticketkeyId = ?1 and type = ?2")
	List<OpenApiBusinessTk> findOpenApiBusinessTkList(String ticketkeyId, int type);
	@Query("From OpenApiBusinessTk where ticketkeyId = ?1 and type = ?2 and isUsing = ?3")
	List<OpenApiBusinessTk> findOpenApiBusinessTkList(String ticketkeyId, int type, int isUsing);
	
}
