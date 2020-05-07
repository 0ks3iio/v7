package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.CountOnlineTime;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountOnlineTimeDao extends BaseJpaRepositoryDao<CountOnlineTime, String>{

	@Query("From CountOnlineTime where sessionId=?1")
	public List<CountOnlineTime> getCountOnlineTimeBySessId(String sessionId);
	@Query("FROM CountOnlineTime WHERE loginTime = (SELECT MAX(loginTime) FROM CountOnlineTime) and userId=?1")
	public List<CountOnlineTime> getCountOnlineTimeListByLastloginUserId(String userId);
}
