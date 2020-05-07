package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;

public interface StudevelopDutySituationDao extends BaseJpaRepositoryDao<StudevelopDutySituation,String>{

	@Query("From StudevelopDutySituation where studentId = ?1 and acadyear = ?2 and semester = ?3 ORDER BY openTime desc")
	public List<StudevelopDutySituation> getStudocDutySituationList(String stuId,String acadyear, String semester);

	@Query("From StudevelopDutySituation where acadyear = ?1 and semester = ?2 and studentId in (?3)")
	public List<StudevelopDutySituation> findListByCls(String acadyear,
			String semester, String[] array);

}
