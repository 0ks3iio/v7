package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkGroupClass;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkGroupClassDao extends BaseJpaRepositoryDao<GkGroupClass,String>{
	@Query("From GkGroupClass where roundsId = ?1  order by groupType,subjectIds,groupName")
	public List<GkGroupClass> findByRoundsId(String roundsId);
	@Query("From GkGroupClass where roundsId = ?1  and groupType=?2 order by groupType,subjectIds,groupName ")
	public List<GkGroupClass> findByRoundsIdAndGroupType(String roundsId,String groupType);
	@Modifying
	@Query("DELETE FROM GkGroupClass WHERE id in (?1)")
	public void deleteByIdIn(String[] ids);

	@Query("From GkGroupClass where subjectIds =?1 and roundsId = ?2 order by groupType,subjectIds,groupName ")
	public List<GkGroupClass> findGkGroupClassBySubjectIds(String subjectIds,String roundsId);

}
