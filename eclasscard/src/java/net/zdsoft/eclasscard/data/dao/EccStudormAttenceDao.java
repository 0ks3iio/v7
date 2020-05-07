package net.zdsoft.eclasscard.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccStudormAttence;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccStudormAttenceDao extends BaseJpaRepositoryDao<EccStudormAttence, String>{
	
	@Query("From EccStudormAttence Where dormAttId in (?1) and status = 0")
	public List<EccStudormAttence> findByInAttIdInit(String[] dormAttIds);
	@Query("From EccStudormAttence Where dormAttId in (?1) ")
	public List<EccStudormAttence> findByInAttIds(String[] dormAttIds);
	
	@Query("From EccStudormAttence Where dormAttId in (?1) and classId = ?2")
	public List<EccStudormAttence> findListByCon(String[] dormAttIds,String classId);
	
	@Modifying
	@Query("update EccStudormAttence set status=?1,clockDate=?2 where id =?3")
	public void updateStatus(int status,Date searchDate,String id);
	@Modifying
	@Query("update EccStudormAttence set status=?1 where id =?2")
	public void updateStatus(int status,String id);

	@Query("From EccStudormAttence Where dormAttId in (?1) and status = 1")
	public List<EccStudormAttence> findByDormAttIdsNeedPush(String[] attIds);
	@Query("From EccStudormAttence Where status = 1 and dormAttId in (?1) and studentId in (?2)")
	public List<EccStudormAttence> findListLeaveStudent(
			String[] dormAttenceIds, String[] studentIds);
}
