package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyLeaveType;

public interface DyLeaveTypeDao extends BaseJpaRepositoryDao<DyLeaveType, String>{
    @Query("From DyLeaveType where unitId = ?1 and state = ?2 and isDeleted = 0")
	public List<DyLeaveType> findLeaveTypeListByState(String unitId, int state);
}
