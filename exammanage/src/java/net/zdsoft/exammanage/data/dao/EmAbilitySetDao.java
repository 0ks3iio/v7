package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmAbilitySet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmAbilitySetDao extends BaseJpaRepositoryDao<EmAbilitySet, String> {

    @Query("FROM EmAbilitySet where statObjectId = ?1 and examId= ?2 ")
    List<EmAbilitySet> findListByObjAndExamId(String statObjectId, String examId);

    @Modifying
    @Query("delete From EmAbilitySet where statObjectId=?1 and examId=?2 ")
    void deleteByObjAndExamId(String statObjectId, String examId);
}
