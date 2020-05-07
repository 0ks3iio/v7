package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.TaskRecord;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRecordDao extends BaseJpaRepositoryDao<TaskRecord, String>{
	
	public static final String SQL_AFTER_ASC=" and isDeleted = 0 order by creationTime asc";
	public static final String SQL_AFTER_DESC=" and isDeleted = 0 order by creationTime desc";

	@Query("From TaskRecord where serverType = ?1 and type = ?2 and status = ?3"+SQL_AFTER_ASC)
	List<TaskRecord> findList(String serverType, String type, int status, Pageable pageable);
	
	@Query("From TaskRecord where serverType = ?1 and type = ?2 and status = ?3"+SQL_AFTER_ASC)
	List<TaskRecord> findList(String serverType, String type, int status);

	@Modifying
	@Query("update TaskRecord set status = 0 where (status = 1 or status = 4) and ?1 > modifyTime ")
	void updateJobNoHand(Date date);

	@Query("From TaskRecord where serverType = ?1 and type = ?2 and unitId = ?3 and businessType = ?4 and creationTime > ?5"+SQL_AFTER_DESC)
	List<TaskRecord> findListByUnitId(String serverType, String type, String unitId, String businessType, Date date, Pageable pageable);
	
	@Query("select count(*) From TaskRecord where serverType = ?1 and type = ?2 and unitId = ?3 and businessType = ?4 and creationTime > ?5"+SQL_AFTER_DESC)
	Integer countListByUnitId(String serverType, String type, String unitId, String businessType,Date date);
	
	@Query("From TaskRecord where creationTime < ?1")
	List<TaskRecord> findOldList(Date time);

	@Modifying
	@Query("delete from TaskRecord where id in (?1)")
	void deleteAllByIds(String... ids);
	
}
