package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisProject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/3/27 18:29
 */
public interface DiathesisProjectDao extends BaseJpaRepositoryDao<DiathesisProject,String> {


    @Query("from DiathesisProject d where d.unitId=?1 and d.projectType='1' order by d.sortNumber")
    List<DiathesisProject> findTopProjectByUnitId(String unitId);

    //todo tocheck  -- Modified 2019.6.4
	@Query("from DiathesisProject where unitId=?1 and projectType in (?2) order by projectType, sortNumber")
	List<DiathesisProject> findByUnitIdAndProjectTypeIn(String unitId, String[] projectTypes);


    @Modifying
    @Query("delete from DiathesisProject where id in (?1)")
    void deleteProjectById(String[] ids);

    /**
     * 查询所有子集项目,包括自己
     * 删除类目的时候专用
     * @param id
     * @return
     */
    //todo tocheck
    @Query(value = "select * from newdiathesis_project start " +
            " with id=?1 connect by prior id = parent_id",nativeQuery = true)
    List<DiathesisProject> findAllChildProject(String id);


    DiathesisProject findByIdAndProjectType(String id, String projectType);

    //todo tocheck
    @Query("from DiathesisProject d where d.unitId=?1  order by d.projectType,d.sortNumber")
    List<DiathesisProject> findListByUnitId(String unitId);


    @Query("from DiathesisProject where unitId=?2 and parentId=?1 and projectType='3' order by sortNumber")
    List<DiathesisProject> findRecordByParentIdAndUnitId(String parentId,String unitId);

    @Query("from DiathesisProject where unitId=?2 and parentId=?1 order by sortNumber")
    List<DiathesisProject> findByUnitIdAndParentId(String parentId, String unitId);

    @Query(value = "select * from newdiathesis_project where unit_id=?2 start  " +
            " with id=?1 connect by prior id = parent_id",nativeQuery = true)
    List<DiathesisProject> findChildByParentIdAndUnitId(String parentId, String unitId);

    @Query("from DiathesisProject where unitId=?1 and parentId in (?2) order by sortNumber")
    List<DiathesisProject> findByUnitIdAndParentIdIn(String unitId, String[] parentIds);

    @Query("select count(*) from DiathesisProject where unitId=?1 and projectType='1' order by sortNumber")
    Integer countTopProjectByUnitId(String unitId);

    @Modifying
    @Query("delete from DiathesisProject where unitId=?1 and parentId in (?2)")
    void deleteByUnitIdAndParentIdIn(String unitId, List<String> parentIds);

    @Modifying
    @Query("delete from DiathesisProject where id in (?1)")
    void deleteByIdIn(List<String> ids);

    @Query(nativeQuery = true ,value = "select parent_id,count(1) from newdiathesis_project where unit_id=?2 and parent_id in ?1 and " +
            " project_type = ?3 group by parent_id")
    List<Object[]> countTopProjectMap(List<String> topProjectIds, String unitId,String projectType);

}
