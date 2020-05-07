package net.zdsoft.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.base.entity.eis.OpenApiParameter;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface OpenApiParameterJpaDao extends BaseJpaRepositoryDao<OpenApiParameter, String> {

	List<OpenApiParameter> findByInterfaceIdIn(String... interfaceId);

	List<OpenApiParameter> findByInterfaceId(String interfaceId);

}
