package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.TeacherDutyDao;
import net.zdsoft.basedata.dao.UserDao;
import net.zdsoft.basedata.entity.TeacherDuty;
import net.zdsoft.basedata.service.TeacherDutyService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("teacherDutyService")
public class TeacherDutyServiceImpl extends BaseServiceImpl<TeacherDuty, String> implements TeacherDutyService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TeacherDutyDao teacherDutyDao;

    @Override
    protected BaseJpaRepositoryDao<TeacherDuty, String> getJpaDao() {
        return teacherDutyDao;
    }

    @Override
    protected Class<TeacherDuty> getEntityClass() {
        return TeacherDuty.class;
    }

    @Override
    public List<TeacherDuty> findByTeacherIds(String dutyCode, String[] teacherIds) {
        return teacherDutyDao.findByTeacherIds(dutyCode, teacherIds);
    }

    @Override
    public List<TeacherDuty> findByTeacherIds(String[] teacherIds) {
        return teacherDutyDao.findByTeacherIds(teacherIds);
    }

}
