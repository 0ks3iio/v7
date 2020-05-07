package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuLeagueRecord;

public interface StuLeagueRecordDao extends BaseJpaRepositoryDao<StuLeagueRecord,String>{

	@Query("From StuLeagueRecord WHERE studentId = ?1 and acadyear = ?2 and semester = ?3 ORDER BY joinDate desc")
	public List<StuLeagueRecord> getStuLeagueRecordList(String stuId, String acadyear,String semester);

	@Query("From StuLeagueRecord where acadyear = ?1 and semester = ?2 and studentId in (?3) ORDER BY joinDate desc")
	public List<StuLeagueRecord> findListByCls(String acadyear,
			String semester, String[] array);
	
	
}
