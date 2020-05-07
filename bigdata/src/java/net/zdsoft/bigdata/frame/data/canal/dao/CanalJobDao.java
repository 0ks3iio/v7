package net.zdsoft.bigdata.frame.data.canal.dao;

import java.math.BigDecimal;
import java.util.Date;

import net.zdsoft.bigdata.frame.data.canal.entity.CanalJob;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CanalJobDao extends BaseJpaRepositoryDao<CanalJob, String> {

	@Modifying
	@Query("update CanalJob set totalSize=?2,lastCommitDate = ?3 Where id = ?1")
	public void updateJobById(String jobId, BigDecimal submitCount, Date lastCommitDate);
}
