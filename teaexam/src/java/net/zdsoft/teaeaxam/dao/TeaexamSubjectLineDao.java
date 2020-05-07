package net.zdsoft.teaeaxam.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLine;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface TeaexamSubjectLineDao extends BaseJpaRepositoryDao<TeaexamSubjectLine, String>{
	@Modifying
    @Query("delete from TeaexamSubjectLine where subjectInfoId = ?1")
    public void deleteBySubjectId(String subjectId);
	@Query("From TeaexamSubjectLine where subjectInfoId = ?1")
	public List<TeaexamSubjectLine> findBySubjectId(String subjectId);  
	@Query("From TeaexamSubjectLine where subjectInfoId in (?1)")
	public List<TeaexamSubjectLine> findBySubjectIds(String[] subjectIds);
}
