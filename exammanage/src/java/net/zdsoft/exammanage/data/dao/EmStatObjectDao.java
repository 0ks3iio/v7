package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmStatObjectDao extends BaseJpaRepositoryDao<EmStatObject, String> {

    public EmStatObject findByUnitIdAndExamId(String unitId, String examId);

    @Modifying
    @Query("update EmStatObject set isStat= ?1 where id in (?2)")
    public void updateIsStat(String isStat, String[] id);

    public List<EmStatObject> findByUnitId(String unitId);

    public List<EmStatObject> findByUnitIdAndExamIdIn(String unitId, String[] examIds);

}
