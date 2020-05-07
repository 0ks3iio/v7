package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisRecordSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/8 17:48
 */
public interface DiathesisRecordSetDao extends BaseJpaRepositoryDao<DiathesisRecordSet, String> {

    @Query("from DiathesisRecordSet where id in ?1")
    List<DiathesisRecordSet> findByIdIn(List<String> ids);

    /**
     * 慎用, 模版单位查找使用, 普通单位可能会有多数据
     * @param unitId
     * @return
     */
    @Query("from DiathesisRecordSet where unitId=?1")
    List<DiathesisRecordSet> findByUnitId(String unitId);

    @Modifying
    @Query("delete from DiathesisRecordSet where id in ?1")
    void deleteByIds(String[] projectIds);
}
