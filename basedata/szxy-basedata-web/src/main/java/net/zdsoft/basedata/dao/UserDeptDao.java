package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.UserDept;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface UserDeptDao extends BaseJpaRepositoryDao<UserDept, String> {

	@Query("from UserDept where userId IN  (?1)")
	List<UserDept> findByUserIds(String... userIds);
}
