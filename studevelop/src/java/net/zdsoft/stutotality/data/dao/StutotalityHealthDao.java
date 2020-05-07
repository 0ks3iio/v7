package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityHealth;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * stutotality_health 
 * @author 
 * 
 */
public interface StutotalityHealthDao extends BaseJpaRepositoryDao<StutotalityHealth, String>{

	/*@Modifying
	@Query("delete from StutotalityHealth where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4")
	void deleteByHealthId(String unitId,String acadyear,String semester,String gradeId);
*/
	@Query(nativeQuery = true, value = "select * from stutotality_health where unit_id = ?1 order by order_number ")
	public List<StutotalityHealth> findHealthItemByUnitId(String unitId);


	public void deleteByUnitId(String unitId);


}
