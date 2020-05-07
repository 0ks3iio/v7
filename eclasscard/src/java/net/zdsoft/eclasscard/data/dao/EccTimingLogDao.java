package net.zdsoft.eclasscard.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccTimingLog;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccTimingLogDao extends BaseJpaRepositoryDao<EccTimingLog, String>{

	@Modifying
	@Query("update EccTimingLog set isDeleted=1,modifyTime=?3 where unitId=?1 and cardId=?2 and type='1' and creationTime <?3 and isDeleted=0")
	void updateAll(String unitId, String cardId, Date creationTime);
	@Query("from EccTimingLog where unitId in (?1) and type='1' and isDeleted=0  order by unitId,cardId,creationTime desc")
	List<EccTimingLog> findByUnitIdIn(String[] unitId);

}
