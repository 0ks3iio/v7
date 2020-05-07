package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.SysProductParam;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author shenke
 * @since 2017.06.27
 */
public interface SysProductParamDao extends BaseJpaRepositoryDao<SysProductParam,String> {

    @Modifying
    @Query("UPDATE SysProductParam set paramValue = ?1 where paramCode = ?2")
    void updateParamValueByCode(String value, String code);
}
