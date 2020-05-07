package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


/**
 * @Author: panlf
 * @Date: 2019/3/27 18:31
 */
public interface DiathesisSetDao extends BaseJpaRepositoryDao<DiathesisSet,String> {
    @Query("from DiathesisSet d where d.unitId=?1 and d.isDeleted='0'")
    DiathesisSet findByUnitId(String unitId);

    @Modifying
    @Query(nativeQuery = true,value = "update NEWDIATHESIS_SET set AUDITOR_TYPES=replace(AUDITOR_TYPES,?2),INPUT_TYPES=replace(INPUT_TYPES,?2) where UNIT_ID=?1")
    void deleteRoleByUnitIdAndRoleCode(String unitId, String roleCode);
}
