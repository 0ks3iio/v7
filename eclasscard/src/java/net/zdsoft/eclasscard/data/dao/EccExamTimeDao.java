package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccExamTime;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccExamTimeDao extends BaseJpaRepositoryDao<EccExamTime,String>{

	@Query("From EccExamTime Where examId in (?1)")
	public List<EccExamTime> findByExamIds(String[] examIds);

	@Query("From EccExamTime Where endTime > ?1")
	public List<EccExamTime> findByltEndTime(String nowTime);

	@Modifying
    @Query("delete from EccExamTime Where subjectId in (?1)")
	public void deleteBySubjectInfoIds(String[] subjectInfoId);

	@Query("From EccExamTime Where status != 2")
	public List<EccExamTime> findListNotShow();


}
