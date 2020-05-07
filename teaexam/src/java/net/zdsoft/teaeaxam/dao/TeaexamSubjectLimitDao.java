package net.zdsoft.teaeaxam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLimit;

public interface TeaexamSubjectLimitDao extends BaseJpaRepositoryDao<TeaexamSubjectLimit, String>{
    @Modifying
    @Query("delete from TeaexamSubjectLimit where subjectInfoId = ?1")
	public void deleteBySubjectId(String subjectId);
    @Query("from TeaexamSubjectLimit where examId = ?1 and subjectInfoId = ?2")
    public TeaexamSubjectLimit findByExamIdAndSubId(String examId, String subjectId);
    @Query("from TeaexamSubjectLimit where examId = ?1 and subjectInfoId = ?2")
    public List<TeaexamSubjectLimit> limitList(String examId, String subjectId);
    @Query("from TeaexamSubjectLimit where examId in (?1)")
    public List<TeaexamSubjectLimit> limitList(String[] examIds);
    @Query("from TeaexamSubjectLimit where subjectInfoId in (?1)")
    public List<TeaexamSubjectLimit> findBySubjectIds(String[] subIds);
}
