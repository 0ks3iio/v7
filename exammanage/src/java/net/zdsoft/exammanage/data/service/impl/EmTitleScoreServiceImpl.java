package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmTitleScoreDao;
import net.zdsoft.exammanage.data.entity.EmTitleScore;
import net.zdsoft.exammanage.data.service.EmTitleScoreService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("emTitleScoreService")
public class EmTitleScoreServiceImpl extends BaseServiceImpl<EmTitleScore, String> implements EmTitleScoreService {

    @Autowired
    private EmTitleScoreDao emTitleScoreDao;

    @Override
    public void deleteByExamIdAndSubjectId(String examId, String subjectId) {
        emTitleScoreDao.deleteByExamIdAndSubjectId(examId, subjectId);
    }

    @Override
    public void deleteByExamIds(String[] examIds) {
        if (examIds != null && examIds.length > 0) {
            emTitleScoreDao.deleteByExamIdIn(examIds);
        }
    }

    @Override
    protected BaseJpaRepositoryDao<EmTitleScore, String> getJpaDao() {
        // TODO Auto-generated method stub
        return emTitleScoreDao;
    }

    @Override
    protected Class<EmTitleScore> getEntityClass() {
        // TODO Auto-generated method stub
        return EmTitleScore.class;
    }

}
