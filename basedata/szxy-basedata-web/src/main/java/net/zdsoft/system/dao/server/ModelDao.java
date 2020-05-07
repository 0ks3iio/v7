package net.zdsoft.system.dao.server;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.server.Model;

public interface ModelDao extends BaseJpaRepositoryDao<Model, Integer> {
    List<Model> findBySubSystem(Integer subSystem);

    List<Model> findByUnitClass(Integer unitClass);

    @Query("From Model where subSystem in ?1 and parentId in ?2 and unitClass=?3 and mark=1 order by displayOrder")
    List<Model> findBySubSystemIdsAndParentIdsAndUnitClass(Integer[] serverIds, Integer[] parentIds, Integer unitClass);

    @Modifying
    @Query("update Model set mark = -1 where mark=1")
    void updateAllModelStop();

    @Modifying
    @Query("update Model set mark = 1 where subSystem in (?1) and mark = -1")
    void updateModelActiveBySubIds(Integer[] subIds);

    List<Model> findBySubSystemIn(Integer[] subsystemArray);

    @Query("From Model where unitClass = ?1 and subSystem in ?2 and parm is null and mark=1")
    List<Model> findBySubSystems(Integer unitClass, Integer[] subSystem);

    @Query("From Model where id in ?1 and mark=1")
    List<Model> findByIds(Integer[] ids);

    @Query("From Model where id in ?1 and parm is null and mark=1")
    List<Model> findExceptMobileModelByIds(Integer[] ids);

    @Query("From Model where subSystem in ?1 and unitClass=?2 and parm is null and mark=1")
    List<Model> findExceptMobileModelBySubsystemAndUnitClass(Integer[] subSystems, Integer unitClass);

    @Query("From Model where subSystem = ?1 and unitClass = ?2 and mark=1")
    List<Model> findModelBySubSystemAndUnitClass(Integer subSystem, Integer unitClass);

    @Modifying
    @Query("update Model set mark = ?1 where subSystem = ?2 and unitClass = ?3")
    void updateMark(Integer mark, Integer subSystem, Integer unitClass);
}
