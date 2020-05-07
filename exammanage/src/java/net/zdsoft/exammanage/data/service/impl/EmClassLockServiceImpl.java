package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmClassLockDao;
import net.zdsoft.exammanage.data.entity.EmClassLock;
import net.zdsoft.exammanage.data.service.EmClassLockService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emClassLockService")
public class EmClassLockServiceImpl extends BaseServiceImpl<EmClassLock, String> implements EmClassLockService {

    @Autowired
    private EmClassLockDao emClassLockDao;

    @Override
    public List<EmClassLock> findBySchoolIdAndExamIdAndSubjectId(String schoolId, String examId, String subjectId) {
        return emClassLockDao.findBySchoolIdAndExamIdAndSubjectId(schoolId, examId, subjectId);
    }

    @Override
    public EmClassLock findBySchoolIdAndExamIdAndSubjectIdAndClassId(String schoolId, String examId, String subjectId, String classId) {
        List<EmClassLock> classLockList = null;
        if (StringUtils.isNotBlank(classId)) {
            classLockList = emClassLockDao.findBySchoolIdAndExamIdAndSubjectIdAndClassId(schoolId, examId, subjectId, classId);
        } else
            classLockList = emClassLockDao.findBySchoolIdAndExamIdAndSubjectId(schoolId, examId, subjectId);
        if (CollectionUtils.isNotEmpty(classLockList)) {
            return classLockList.get(0);
        }
        return new EmClassLock();
    }

    @Override
    public void deleteBy(String examId, String subjectId) {
        emClassLockDao.deleteBy(examId, subjectId);
    }

    @Override
    public void deleteByExamIdIn(String[] examIds) {
        emClassLockDao.deleteByExamIdIn(examIds);
    }

    @Override
    protected BaseJpaRepositoryDao<EmClassLock, String> getJpaDao() {
        return emClassLockDao;
    }

    @Override
    protected Class<EmClassLock> getEntityClass() {
        return EmClassLock.class;
    }

}
