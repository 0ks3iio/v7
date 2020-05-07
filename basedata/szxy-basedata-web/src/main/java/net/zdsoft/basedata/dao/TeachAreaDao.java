package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachAreaDao extends BaseJpaRepositoryDao<TeachArea, String>{

	public static final String SQL_AFTER=" and isDeleted = 0 order by areaCode";
	
	@Query("From TeachArea where unitId = ?1"+SQL_AFTER)
	List<TeachArea> findByUnitId(String unitId);

	@Modifying
	@Query("update TeachArea set isDeleted=1 where id in (?1)")
	void updateIsDelete(String... ids);

	@Query("From TeachArea where unitId in ?1"+SQL_AFTER)
	List<TeachArea> findByUnitIdIn(String[] uidList);
	
}
