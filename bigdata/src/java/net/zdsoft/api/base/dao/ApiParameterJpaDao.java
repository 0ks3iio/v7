package net.zdsoft.api.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.api.base.entity.eis.ApiParameter;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ApiParameterJpaDao extends BaseJpaRepositoryDao<ApiParameter, String> {

	List<ApiParameter> findByInterfaceIdIn(String... interfaceId);

	List<ApiParameter> findByInterfaceId(String interfaceId);

	@Modifying
    @Query("delete from ApiParameter where interfaceId = ?1")
	void deleteByInterfaceId(String interfaceId);

}
