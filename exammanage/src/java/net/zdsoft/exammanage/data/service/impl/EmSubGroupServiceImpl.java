package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmSubGroupDao;
import net.zdsoft.exammanage.data.entity.EmSubGroup;
import net.zdsoft.exammanage.data.service.EmSubGroupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emSubGroupService")
public class EmSubGroupServiceImpl extends BaseServiceImpl<EmSubGroup, String> implements EmSubGroupService {
    @Autowired
    private EmSubGroupDao emSubGroupDao;

    @Override
    protected BaseJpaRepositoryDao<EmSubGroup, String> getJpaDao() {
        return emSubGroupDao;
    }

    @Override
    public List<EmSubGroup> findListByExamId(String examId) {
        return emSubGroupDao.findByExamId(examId);
    }

    @Override
    public List<EmSubGroup> findByExamIdAndSubjectId(String examId, String subId) {
        return emSubGroupDao.findByExamIdAndSubjectId(examId, subId);
    }

    @Override
    public List<EmSubGroup> findByExamIdAndSubIdAndSubType(String unitId, String examId, String subId, String subType) {
        return emSubGroupDao.findByExamIdAndSubIdAndSubType(examId, subType, subId);
    }

    @Override
    protected Class<EmSubGroup> getEntityClass() {
        return EmSubGroup.class;
    }

    @Override
    public void deleteByExamIdAndUnitId(String examId, String unitId) {
        emSubGroupDao.deleteByExamIdAndUnitId(examId, unitId);
    }
}
