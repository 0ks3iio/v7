package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;

public interface OpenApiParameterJpaDao extends BaseJpaRepositoryDao<OpenApiParameter, String> {

    List<OpenApiParameter> findByUriIn(String... uris);

    @Modifying
	@Query("DELETE FROM OpenApiParameter WHERE uri= ?1")
	void deleteByUri(String uri);

    
	List<OpenApiParameter> findByUri(String uri);

	@Modifying
	@Query("UPDATE OpenApiParameter SET uri= ?1 where uri = ?2 ")
	void updateUriByOlduri(String newUri, String oldUri);

}
