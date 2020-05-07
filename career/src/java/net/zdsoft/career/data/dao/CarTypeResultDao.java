package net.zdsoft.career.data.dao;

import java.util.Set;

import net.zdsoft.career.data.entity.CarTypeResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface CarTypeResultDao extends BaseJpaRepositoryDao<CarTypeResult,String>{

	public CarTypeResult findByResultType(String resultType);

	@Query("select resultType from CarTypeResult")
	public Set<String> findAllTypes();

}
