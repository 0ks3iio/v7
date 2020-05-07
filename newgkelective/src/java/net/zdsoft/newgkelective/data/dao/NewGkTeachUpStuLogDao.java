package net.zdsoft.newgkelective.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTeachUpStuLog;

import java.util.List;

public interface NewGkTeachUpStuLogDao extends BaseJpaRepositoryDao<NewGkTeachUpStuLog, String> {

    List<NewGkTeachUpStuLog> findByChoiceIdOrderByModifyTimeDesc(String choiceId);

    List<NewGkTeachUpStuLog> findByChoiceIdAndStudentIdInOrderByModifyTimeDesc(String choiceId, String[] studentIds);

}
