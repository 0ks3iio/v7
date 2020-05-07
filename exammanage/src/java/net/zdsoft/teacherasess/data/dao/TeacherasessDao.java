package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsess;

public interface TeacherasessDao extends BaseJpaRepositoryDao<TeacherAsess, String>{
	
	@Query("From TeacherAsess where unitId=?1 and acadyear=?2 order by creationTime desc")
	public List<TeacherAsess> findByUnitIdAndAcayearWithMaster(String unitId,String acayear);
	
	@Modifying
    @Query("update from TeacherAsess set status =?1 Where id = ?2")
	public void updateforStatus(String status, String id);
	
	public void deleteByIdIn(String[] id);
}
