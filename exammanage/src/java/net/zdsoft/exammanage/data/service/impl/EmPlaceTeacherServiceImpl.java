package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmPlaceTeacherDao;
import net.zdsoft.exammanage.data.entity.EmPlaceTeacher;
import net.zdsoft.exammanage.data.service.EmPlaceTeacherService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emPlaceTeacherService")
public class EmPlaceTeacherServiceImpl extends BaseServiceImpl<EmPlaceTeacher, String> implements EmPlaceTeacherService {

    @Autowired
    private EmPlaceTeacherDao emPlaceTeacherDao;


    @Override
    public List<EmPlaceTeacher> findByExamIdAndType(String examId, String type) {
        return emPlaceTeacherDao.findByExamIdAndType(examId, type);
    }

    @Override
    public void saveAndDel(EmPlaceTeacher emPlaceTeacher, String emPlaceTeacherId) {
        if (StringUtils.isNotBlank(emPlaceTeacherId)) {
            emPlaceTeacherDao.deleteById(emPlaceTeacherId);
        }
        emPlaceTeacherDao.saveAll(checkSave(emPlaceTeacher));
    }

    @Override
    public List<EmPlaceTeacher> findByExamIdAndSubjectIdAndType(String examId, String subjectId, String tEACHER_TYPE1) {
        return emPlaceTeacherDao.findByExamIdAndSubjectIdAndType(examId, subjectId, tEACHER_TYPE1);
    }

    @Override
    protected BaseJpaRepositoryDao<EmPlaceTeacher, String> getJpaDao() {
        return emPlaceTeacherDao;
    }

    @Override
    public void saveAllAndDel(String examId, List<EmPlaceTeacher> invigilateList, String tEACHER_TYPE) {
        emPlaceTeacherDao.deleteByExamId(examId, tEACHER_TYPE);
        for (EmPlaceTeacher emPlaceTeacher : invigilateList) {
            checkSave(emPlaceTeacher);
        }
        emPlaceTeacherDao.saveAll(invigilateList);
    }


    @Override
    public List<EmPlaceTeacher> findByExamId(String examId) {
        return emPlaceTeacherDao.findByExamId(examId);
    }

    @Override
    public List<EmPlaceTeacher> findByUnitIdAndStartTimeAndType(String unitId, String startTime, String endTime, String tEACHER_TYPE) {
        return emPlaceTeacherDao.findByUnitIdAndStartTimeAndType(unitId, startTime, endTime, tEACHER_TYPE);
    }

    @Override
    protected Class<EmPlaceTeacher> getEntityClass() {
        return EmPlaceTeacher.class;
    }

    @Override
    public void deleteByEmPlaceIds(String... emPlaceIds) {
        if (emPlaceIds != null && emPlaceIds.length > 0) {
            emPlaceTeacherDao.deleteByExamPlaceIdIn(emPlaceIds);
        }

    }

    @Override
    public void deleteAllPlaceTeacher(String subjectId, String examId,
                                      String unitId) {
        emPlaceTeacherDao.deleteAllPlaceTeacher(subjectId, examId, unitId);
    }

    @Override
    public List<EmPlaceTeacher> findByTeacherIn(String unitId, String examId,
                                                String teacherId) {
        return emPlaceTeacherDao.findByTeacherIn(unitId, examId, "%" + teacherId + "%");
    }

    @Override
    public List<EmPlaceTeacher> findByExamIdAndPlaceIdAndSubjectId(
            String examId, String placeId, String subjectId, String type) {
        return emPlaceTeacherDao.findByExamIdAndPlaceIdAndSubjectId(examId, placeId, subjectId, type);
    }


}
