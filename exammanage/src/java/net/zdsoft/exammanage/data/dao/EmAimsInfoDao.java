package net.zdsoft.exammanage.data.dao;


import net.zdsoft.exammanage.data.entity.EmAimsInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmAimsInfoDao extends BaseJpaRepositoryDao<EmAimsInfo, String> {

    @Query("From EmAimsInfo where examId in (?1)")
    List<EmAimsInfo> findListByExamIdIn(String[] examIds);

    void deleteByExamId(String examId);

    @Query("From EmAimsInfo where isOpen = ?1 and examId in (?2)")
    List<EmAimsInfo> findListByOpenAndExamIdIn(String isOpen, String[] examIds);

}
