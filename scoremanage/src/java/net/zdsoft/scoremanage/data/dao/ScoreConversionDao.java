package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ScoreConversion;

public interface ScoreConversionDao extends BaseJpaRepositoryDao<ScoreConversion, String>{

	@Query("From ScoreConversion where examId = ?1 order by score desc")
	List<ScoreConversion> findListByExamId(String examId);

	@Modifying
	@Query("delete from ScoreConversion where examId = ?1")
	void deleteByExamId(String examId);
	@Modifying
	@Query("delete from ScoreConversion where id in (?1)")
	void deleteAllByIds(String... id);

	void deleteByExamIdIn(String[] examIds);
	
}
