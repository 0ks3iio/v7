package net.zdsoft.comprehensive.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.comprehensive.entity.CompreParameter;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface CompreParameterDao extends BaseJpaRepositoryDao<CompreParameter, String> {

//分割线，以上为原有方法，方便后续删除
	
	@Query("From CompreParameter where type = ?1 and unitId = ?2 order by type,parkey")
	List<CompreParameter> findCompreParameterList(String type, String unitId);

	List<CompreParameter> findByUnitId(String unitId);
}
