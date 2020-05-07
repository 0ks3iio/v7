package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisSubjectField;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/13 10:07
 */
public interface DiathesisSubjectFieldDao extends BaseJpaRepositoryDao<DiathesisSubjectField,String> {
    @Query("from DiathesisSubjectField where unitId=?1 and subjectType=?2 order by sortNum")
    List<DiathesisSubjectField> findByUnitAndSubjectType(String unitId, String subjectType);

    @Modifying
    @Query("delete from DiathesisSubjectField where unitId=?1 ")
    void deleteByUnitId(String unitId);

    @Query("from DiathesisSubjectField where unitId=?1 order by sortNum")
    List<DiathesisSubjectField> findByUnitId(String unitId);

    @Modifying
    @Query("delete from DiathesisSubjectField where id in ?1 ")
    void deleteByIds(List<String> delFieldList);
}
