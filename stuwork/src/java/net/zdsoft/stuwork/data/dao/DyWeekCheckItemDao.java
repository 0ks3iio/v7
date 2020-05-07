package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;

import org.springframework.data.jpa.repository.Query;

public interface DyWeekCheckItemDao extends BaseJpaRepositoryDao<DyWeekCheckItem, String>{

	@Query("From DyWeekCheckItem where schoolId=?1 ")
	public List<DyWeekCheckItem> findByRoleTypes(String schoolId);
	@Query("From DyWeekCheckItem where schoolId=?1 order by orderId")
	public List<DyWeekCheckItem> findBySchoolId(String unitId);
}
