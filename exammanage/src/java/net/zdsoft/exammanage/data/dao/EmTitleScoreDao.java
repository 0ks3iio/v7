package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmTitleScore;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EmTitleScoreDao extends BaseJpaRepositoryDao<EmTitleScore, String> {
    public void deleteByExamIdAndSubjectId(String examId, String subjectId);

    public void deleteByExamIdIn(String[] examIds);
}
