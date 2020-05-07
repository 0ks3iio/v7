package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityHealthOption;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface StutotalityHealthOptionDao extends BaseJpaRepositoryDao<StutotalityHealthOption, String> {

	@Query("from StutotalityHealthOption where healthId = ?1 order by gradeCode asc")
	public List<StutotalityHealthOption> findByHealthId(String healthId);

	@Query("from StutotalityHealthOption where unitId = ?1 order by gradeCode asc")
	public List<StutotalityHealthOption> findByUnitId(String unitId);

	@Query("from StutotalityHealthOption where unitId = ?1 and healthId = ?2 order by gradeCode asc")
	public List<StutotalityHealthOption> findByUnitIdAndHealthId(String unitId,String healthId);

	@Query(nativeQuery = true,value = "select * from stutotality_health_option where health_id IN ?1 ")
	public List<StutotalityHealthOption> findHealthItemsByIds(String[] healthIds);

	@Modifying
	@Query("delete from StutotalityHealthOption where healthId = ?1 ")
	public void deleteByhealthId(String healthId);

    List<StutotalityHealthOption> findByUnitIdAndGradeCode(String unitId, String gradeCode);
}
