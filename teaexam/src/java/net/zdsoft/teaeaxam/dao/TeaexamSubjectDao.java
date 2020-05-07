package net.zdsoft.teaeaxam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;

public interface TeaexamSubjectDao extends BaseJpaRepositoryDao<TeaexamSubject, String>{
	@Modifying
	@Query("delete from TeaexamSubject WHERE examId=?1")
	public void deleteSubByExamId(String examId);
	@Query("FROM TeaexamSubject WHERE examId in (?1)")
	public List<TeaexamSubject> findByExamIds(String[] examIds);
	
	/**
	 * 同次考试下，相同时间考试的科目
	 * @param subInfoId
	 * @return
	 */
	@Query("SELECT ts FROM TeaexamSubject ts, TeaexamSubject es WHERE es.examId=ts.examId and es.startTime=ts.startTime and es.endTime=ts.endTime and es.id=?1")
	public List<TeaexamSubject> findSameTimeBySubId(String subInfoId);
}
