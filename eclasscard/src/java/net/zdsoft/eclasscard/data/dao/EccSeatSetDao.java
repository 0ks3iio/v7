package net.zdsoft.eclasscard.data.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccSeatSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccSeatSetDao extends BaseJpaRepositoryDao<EccSeatSet, String>{
	
	@Query("From EccSeatSet where unitId=?1 and classId=?2")
	EccSeatSet findOneByUnitIdAndClassId(String unitId, String classId);

	@Modifying
	@Query("delete from EccSeatSet where classId=?1")
    void deleteByClassId(String classId);
}
