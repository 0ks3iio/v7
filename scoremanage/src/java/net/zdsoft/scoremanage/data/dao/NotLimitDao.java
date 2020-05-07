package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.NotLimit;

public interface NotLimitDao extends BaseJpaRepositoryDao<NotLimit, String>{
	@Query("select teacherId from NotLimit where unitId = ?1")
	List<String> findByUnitId(String unitId);

	void deleteByUnitId(String unitId);
}
