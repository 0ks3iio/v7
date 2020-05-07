package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertResult;

public interface TeacherasessConvertResultDao extends BaseJpaRepositoryDao<TeacherasessConvertResult, String>{
	
	@Transactional
	@Modifying
	@Query("delete From TeacherasessConvertResult where convertId=?1")
	void deleteByConvertId(String convertId);
	@Transactional
	@Modifying
	@Query("delete From TeacherasessConvertResult where convertId=?1 and subjectId =?2 ")
	void deleteByConvertId(String convertId,String subjectId);

	List<TeacherasessConvertResult> findListByConvertIdAndSubjectId(String convertId, String subjectId);

	@Query("From TeacherasessConvertResult where unitId = ?1 and convertId=?2 and subjectId=?3 and studentId in (?4)")
	public List<TeacherasessConvertResult> findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(String unitId,String convertId,String subjectId,String...studentIds);
}
