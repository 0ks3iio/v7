package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.ExammanageAuditionDao;
import net.zdsoft.exammanage.data.entity.ExammanageAudition;
import net.zdsoft.exammanage.data.service.ExammanageAuditionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("exammanageAuditionService")
public class ExammanageAuditionServiceImpl extends BaseServiceImpl<ExammanageAudition, String> implements ExammanageAuditionService {
    @Autowired
    private ExammanageAuditionDao exammanageAuditionDao;

    @Override
    protected BaseJpaRepositoryDao<ExammanageAudition, String> getJpaDao() {
        return exammanageAuditionDao;
    }

    @Override
    protected Class<ExammanageAudition> getEntityClass() {
        return ExammanageAudition.class;
    }
}
