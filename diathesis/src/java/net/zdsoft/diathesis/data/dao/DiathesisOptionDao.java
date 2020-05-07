package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisOption;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/3/27 18:28
 */
public interface DiathesisOptionDao extends BaseJpaRepositoryDao<DiathesisOption,String> {

	List<DiathesisOption> findByUnitIdAndProjectId(String unitId, String projectId);

	@Query("select id,contentTxt from DiathesisOption where unitId=?1 and projectId=?2 order by colNo")
	List<Object[]> findMapByUnitIdAndProjectId(String unitId, String projectId);

	@Modifying
   	@Query("delete from DiathesisOption where projectId in (?1)")
    void deleteByProjectId(String[] projectIds);

	List<DiathesisOption> findByProjectId(String projectId);

	/**
	 * 删除该项目下面的 在ids范围中的option
	 * @param projectId
	 * @param ids
	 */
	@Modifying
	@Query("delete from DiathesisOption where projectId=?1 and id in (?2)")
	void deleteByProjectIdAndIds(String projectId, String... ids);

	List<DiathesisOption> findByUnitId(String unitId);
	
	@Query("select id,contentTxt from DiathesisOption where unitId= ?1 and structureId in (?2) order by colNo")
	List<Object[]> findMapByUnitIdAndStructureIdIn(String unitId, String[] structureIds);

	@Query("from DiathesisOption where structureId in (?1) order by colNo")
    List<DiathesisOption> findListByStructureIdIn(String[] structureIds);

	@Modifying
	@Query("delete from DiathesisOption where projectId in (?1)")
    void deleteByProjectIdIn(List<String> projectIds);

	@Modifying
	@Query("delete from DiathesisOption where id in (?1)")
    void deleteByIdIn(List<String> ids);

	@Query("from DiathesisOption where projectId in (?1) order by colNo")
	List<DiathesisOption> findListByProjectIdIn(List<String> projectIds);
}
