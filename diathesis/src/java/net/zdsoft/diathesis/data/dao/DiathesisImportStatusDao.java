package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisImportStatus;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/15 14:12
 */
public interface DiathesisImportStatusDao extends BaseJpaRepositoryDao<DiathesisImportStatus,String> {

    @Query("from DiathesisImportStatus where unitId=?1 and fieldId=?2")
    DiathesisImportStatus findByUnitIdAndFieldId(String unitId, String fieldId);

    @Query("from DiathesisImportStatus where unitId=?1 and fieldId in ?2")
    List<DiathesisImportStatus> findByUnitIdAndFieldIdIn(String unitId, String[] fieldArr);
}
