package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccSignIn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface EccSignInDao extends BaseJpaRepositoryDao<EccSignIn, String>{

	@Query("From EccSignIn where classId = ?1 and TO_CHAR(clockInTime,'yyyy-MM-dd HH24:MI') >= ?2 and TO_CHAR(clockInTime,'yyyy-MM-dd HH24:MI') <= ?3")
	public List<EccSignIn> findByClassIdAndTime(String classId, String startTime,String endTime);

	@Query("From EccSignIn where classId = ?1 and TO_CHAR(clockInTime,'yyyy-MM-dd HH24:MI') >= ?2 and TO_CHAR(clockInTime,'yyyy-MM-dd HH24:MI') <= ?3 and eccInfoId in (?4)")
	public List<EccSignIn> findByClassIdAndTimeAndPlaceIds(String classId,String startTime, String endTime, String[] infoIds);

}
