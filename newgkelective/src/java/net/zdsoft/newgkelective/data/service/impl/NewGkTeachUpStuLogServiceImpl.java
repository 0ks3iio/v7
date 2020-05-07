package net.zdsoft.newgkelective.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkTeachUpStuLogDao;
import net.zdsoft.newgkelective.data.entity.NewGkTeachUpStuLog;
import net.zdsoft.newgkelective.data.service.NewGkTeachUpStuLogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("newGkTeachUpStuLogService")
public class NewGkTeachUpStuLogServiceImpl extends BaseServiceImpl<NewGkTeachUpStuLog, String> implements NewGkTeachUpStuLogService {

    @Autowired
    private NewGkTeachUpStuLogDao newGkTeachUpStuLogDao;

    @Override
    protected BaseJpaRepositoryDao<NewGkTeachUpStuLog, String> getJpaDao() {
        return newGkTeachUpStuLogDao;
    }

    @Override
    protected Class<NewGkTeachUpStuLog> getEntityClass() {
        return NewGkTeachUpStuLog.class;
    }

    @Override
    public List<NewGkTeachUpStuLog> findByChoiceId(String choiceId, List<String> studentIds) {
        if (CollectionUtils.isNotEmpty(studentIds)) {
            return newGkTeachUpStuLogDao.findByChoiceIdAndStudentIdInOrderByModifyTimeDesc(choiceId, studentIds.toArray(new String[0]));
        } else {
            return newGkTeachUpStuLogDao.findByChoiceIdOrderByModifyTimeDesc(choiceId);
        }
    }
}
