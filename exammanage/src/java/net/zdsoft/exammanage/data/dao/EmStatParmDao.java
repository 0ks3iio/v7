package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmStatParm;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmStatParmDao extends BaseJpaRepositoryDao<EmStatParm, String> {

    @Query("From EmStatParm where statObjectId=?1 and subjectId=?2 ")
    EmStatParm findByObjSubjectId(String objId, String subjectId);

    @Modifying
    @Query("delete From EmStatParm where statObjectId=?1 and subjectId=?2 ")
    void deleteByObjSubjectId(String objId, String subjectId);

    List<EmStatParm> findByStatObjectId(String statObjectId);

    @Modifying
    @Query("update EmStatParm set isStat=?2 where id in (?1)")
    void updateStat(String[] ids, String isStat);

    List<EmStatParm> findByStatObjectIdAndExamId(String statObjectId,
                                                 String examId);

}
