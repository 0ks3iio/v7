package net.zdsoft.officework.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.officework.entity.OfficeClassroomRelevance;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OfficeClassroomRelevanceDao  extends BaseJpaRepositoryDao<OfficeClassroomRelevance, String>{

	@Query("From OfficeClassroomRelevance Where placeId = ?1")
	public OfficeClassroomRelevance findByPlaceId(String placeId);

	@Query("From OfficeClassroomRelevance Where schoolId = ?1")
	public List<OfficeClassroomRelevance> findByUnitId(String unitId);

	@Modifying
	@Query("delete from OfficeClassroomRelevance Where placeId = ?1")
	public void deleteByPlaceId(String placeId);
}
