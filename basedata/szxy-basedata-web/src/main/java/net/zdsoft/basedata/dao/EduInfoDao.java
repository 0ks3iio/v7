package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.EduInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EduInfoDao extends BaseJpaRepositoryDao<EduInfo, String>{

    @Modifying
    @Query(
            value = "update EduInfo set isDeleted=0 where id=?1"
    )
    void deleteEduInfosByUnitId(String unitId);
}
