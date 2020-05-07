package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmOutTeacherDao;
import net.zdsoft.exammanage.data.entity.EmOutTeacher;
import net.zdsoft.exammanage.data.service.EmOutTeacherService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emOutTeacherService")
public class EmOutTeacherServiceImpl extends BaseServiceImpl<EmOutTeacher, String> implements EmOutTeacherService {

    @Autowired
    private EmOutTeacherDao emOutTeacherDao;

    @Override
    public List<EmOutTeacher> findByExamIdAndSchoolId(String examId, String unitId) {
        return emOutTeacherDao.findByExamIdAndSchoolId(examId, unitId);
    }

    @Override
    public List<EmOutTeacher> findBySchoolId(String unitId) {
        return emOutTeacherDao.findBySchoolId(unitId);
    }

    @Override
    public void saveOne(EmOutTeacher outTeacher) {
        emOutTeacherDao.saveAll(checkSave(outTeacher));
    }

    @Override
    public void saveAll(List<EmOutTeacher> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            emOutTeacherDao.saveAll(checkSave(insertList.toArray(new EmOutTeacher[]{})));
        }
    }

    @Override
    protected BaseJpaRepositoryDao<EmOutTeacher, String> getJpaDao() {
        return emOutTeacherDao;
    }

    @Override
    protected Class<EmOutTeacher> getEntityClass() {
        return EmOutTeacher.class;
    }
}
