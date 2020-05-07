package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmTitleInfoDao;
import net.zdsoft.exammanage.data.entity.EmTitleInfo;
import net.zdsoft.exammanage.data.service.EmTitleInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("emTitleInfoService")
public class EmTitleInfoServiceImpl extends BaseServiceImpl<EmTitleInfo, String> implements EmTitleInfoService {
    @Autowired
    private EmTitleInfoDao emTitleInfoDao;

    @Override
    public void deleteByExamIdAndSubjectId(String examId, String subjectId) {
        emTitleInfoDao.deleteByExamIdAndSubjectId(examId, subjectId);
    }

    @Override
    public void deleteByExamIds(String[] examIds) {
        if (examIds != null && examIds.length > 0) {
            emTitleInfoDao.deleteByExamIdIn(examIds);
        }
    }

    @Override
    protected BaseJpaRepositoryDao<EmTitleInfo, String> getJpaDao() {
        return emTitleInfoDao;
    }

    @Override
    protected Class<EmTitleInfo> getEntityClass() {
        return EmTitleInfo.class;
    }

}
