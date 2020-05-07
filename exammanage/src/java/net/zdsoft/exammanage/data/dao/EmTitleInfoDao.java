package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmTitleInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EmTitleInfoDao extends BaseJpaRepositoryDao<EmTitleInfo, String> {
    public void deleteByExamIdAndSubjectId(String examId, String subjectId);

    public void deleteByExamIdIn(String[] examIds);
}
