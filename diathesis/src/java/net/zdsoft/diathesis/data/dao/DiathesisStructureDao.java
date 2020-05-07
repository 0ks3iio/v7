package net.zdsoft.diathesis.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.diathesis.data.entity.DiathesisStructure;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @Author: panlf
 * @Date: 2019/3/27 18:31
 */
public interface DiathesisStructureDao extends BaseJpaRepositoryDao<DiathesisStructure,String> {

	@Query("from DiathesisStructure where unitId=?1 and projectId=?2 order by colNo, isShow desc")
	List<DiathesisStructure> findByProjectId(String unitId, String projectId);

	@Modifying
    @Query("delete from DiathesisStructure where projectId in (?1)")
    void deleteByProjectId(String[] projectIds);

    List<DiathesisStructure> findByProjectId(String projectId);

    @Modifying
    @Query("delete from DiathesisStructure where projectId=?1 and id in (?2)")
    void deleteByProjectIdAndIds(String projectId, String[] deleteStructureIds);

    @Query("from DiathesisStructure where projectId in (?1) order by colNo, isShow desc")
	List<DiathesisStructure> findByProjectIdIn( String[] projectIds);

    List<DiathesisStructure> findByUnitId(String unitId);

    @Modifying
    @Query("delete from DiathesisStructure where projectId in (?1)")
    void deleteByProjectIdIn(List<String> projectIds);

    @Modifying
    @Query("delete from DiathesisStructure where id in (?1)")
    void deleteByIdIn(List<String> ids);

    @Query("from DiathesisStructure where dataType='2' and projectId in (?1) order by colNo")
    List<DiathesisStructure> findListBySingleTypeAndProjectTypeIn(List<String> projectIds);
}
