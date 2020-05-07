package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmClassInfoDao;
import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.exammanage.data.service.EmClassInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("emClassInfoService")
public class EmClassInfoServiceImpl extends BaseServiceImpl<EmClassInfo, String> implements EmClassInfoService {
    @Autowired
    private EmClassInfoDao emClassInfoDao;

    @Override
    protected BaseJpaRepositoryDao<EmClassInfo, String> getJpaDao() {
        return emClassInfoDao;
    }

    @Override
    protected Class<EmClassInfo> getEntityClass() {
        return EmClassInfo.class;
    }

    @Override
    public List<EmClassInfo> findByExamIdAndSchoolId(String examId,
                                                     String schoolId) {
        if (StringUtils.isNotBlank(schoolId)) {
            return emClassInfoDao.findByExamIdAndSchoolId(examId, schoolId);
        } else {
            return emClassInfoDao.findByExamId(examId);
        }

    }

    @Override
    public List<EmClassInfo> saveAllEntitys(EmClassInfo... emClassInfos) {
        return emClassInfoDao.saveAll(checkSave(emClassInfos));
    }

    @Override
    public void deleteByExamIdAndSchoolId(String examId, String schoolId) {
        emClassInfoDao.deleteByExamIdAndSchoolId(examId, schoolId);
    }

    @Override
    public List<EmClassInfo> findBySchoolIdAndExamIdIn(String[] examIds,
                                                       String schoolId) {
        return emClassInfoDao.findBySchoolIdAndExamIdIn(schoolId, examIds);
    }

    @Override
    public EmClassInfo findByExamIdAndSchoolIdAndClassId(String examId,
                                                         String schoolId, String classId) {
        return emClassInfoDao.findByExamIdAndSchoolIdAndClassId(examId, schoolId, classId);
    }

    @Override
    public List<String> findExamIdByClassIds(String[] classIds) {
        if (classIds == null || classIds.length <= 0) {
            return new ArrayList<String>();
        }
        return emClassInfoDao.findExamIdByClassIds(classIds);
    }

    @Override
    public List<EmClassInfo> findByExamId(String examId) {
        return emClassInfoDao.findByExamId(examId);
    }

    @Override
    public List<EmClassInfo> findBySchoolIdAndSubjectInfoIdIn(String schoolId, String... subjectInfoIds) {
        return emClassInfoDao.findBySchoolIdAndSubjectInfoIdIn(schoolId, subjectInfoIds);
    }

    public void deleteByExamIds(String[] examIds) {
        emClassInfoDao.deleteByExamIds(examIds);
    }
}
