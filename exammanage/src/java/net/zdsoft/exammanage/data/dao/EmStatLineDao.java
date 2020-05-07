package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmStatLine;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmStatLineDao extends BaseJpaRepositoryDao<EmStatLine, String> {

    void deleteByStatObjectIdIn(String[] statObjectId);

    @Query("From EmStatLine where statObjectId=?1 and isDouble=?2 and subjectId = ?3 and type = ?4")
    List<EmStatLine> findList(String statObjectId, String isDouble, String subId, String subType);

    @Query("From EmStatLine where statObjectId=?1 and isDouble=1 and subjectId = ?2 and type = ?3 and doubleType=?4")
    List<EmStatLine> findDoubleList(String id, String subId, String subType, String sumType);
}
