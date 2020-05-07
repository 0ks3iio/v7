package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisProjectEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/13 17:53
 */
public interface DiathesisProjectExDao extends BaseJpaRepositoryDao<DiathesisProjectEx,String> {


    @Query("from DiathesisProjectEx where unitId=?1 and projectId in ?2")
    List<DiathesisProjectEx> findByUnitIdAndProjectIdIn(String unitId, List<String> projectIds);

    @Query("from DiathesisProjectEx where unitId=?1 and projectId=?2")
    DiathesisProjectEx findByUnitIdAndProjectId(String unitId, String projectId);

    @Query(nativeQuery = true,
            value = "select COUNT(*) from newdiathesis_project_ex where unit_id=?1 and (input_types like ?2 or auditor_types like ?2)")
    Integer countInputTypesByUnitIdAndRoleCode(String unitId, String roleCode);

    @Modifying
    @Query("delete from DiathesisProjectEx where unitId =?2 and projectId in ?1")
    void deleteByProjectIdsAndUnitId(String[] projectIds, String unitId);

    @Modifying
    @Query("delete from DiathesisProjectEx where projectId in ?1")
    void deleteByProjectIds(String[] projectIds);


    @Modifying
    @Query("delete from DiathesisProjectEx where  id in ?1")
    void deleteByIds(List<String> delExIds);

}
