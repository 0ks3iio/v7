package net.zdsoft.studevelop.data.dao;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuHealthRecord;

import java.util.List;

public interface StuHealthRecordDao extends BaseJpaRepositoryDao<StuHealthRecord,String>{

	@Query("From StuHealthRecord WHERE studentId = ?1 AND acadyear = ?2 AND semester = ?3")
	public List<StuHealthRecord> getHealthRecordByStuIdSemes(String stuId, String acadyear, String semester);

}
