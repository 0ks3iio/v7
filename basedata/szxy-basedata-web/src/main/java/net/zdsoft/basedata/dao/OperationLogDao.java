package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface OperationLogDao extends BaseJpaRepositoryDao<OperationLog, String>, OperationLogJdbcDao{

	/**
	 * @param parameter
	 * @return
	 */
	@Query("select count(w)  From OperationLog as w  Where parameter = ?1 ")
	int findAppPopularity(String parameter);
	
	/**
	 * @param description
	 * @return
	 */
	@Query("from OperationLog where description like ?1")
	List<OperationLog> findByDescription(String description);

	/**
	 * @param ticketKey
	 * @param description
	 * @return
	 */
	@Query("from OperationLog where parameter = ?1 and description = ?2")
	List<OperationLog> findByParameterAndDescription(String ticketKey, String description);

	/**
	 * @param ticketKey
	 * @param string
	 * @return
	 */
	@Query("from OperationLog where parameter = ?1 and description like ?2")
	List<OperationLog> findAllByType(String ticketKey, String string);
	
	
	/**
	 * @param url
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@Query("from OperationLog where url = ?1 and logTime >= ?2 and logTime< ?3")
	List<OperationLog> findListByUrl(String url, Date beginDate,Date endDate);
	
	/**
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@Query("from OperationLog where logTime >= ?1 and logTime< ?2")
	List<OperationLog> findListByDate(Date beginDate,Date endDate);

	@Query("select count(distinct unitId) from OperationLog where unitId in (?1)")
	long countUnits(String[] unitIds);

	/**
	 *
	 * @return
	 */
	@Query(value = "select count(distinct user_id) from base_operation_log", nativeQuery = true)
	long countUsersByDate();

	
	@Query(value = "select count(*) from base_operation_log", nativeQuery = true)
	long countAllUsers();

	@Query(value = "select count(*) from base_unit  where  is_deleted=0 and unit_class=2 and id in(select unit_id from base_operation_log)", nativeQuery = true)
	long countSchool();
}

