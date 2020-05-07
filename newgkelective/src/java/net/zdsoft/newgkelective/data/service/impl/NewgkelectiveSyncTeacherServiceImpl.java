package net.zdsoft.newgkelective.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTeacherService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanExService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;
@Service("newgkelectiveSyncTeacherService")
public class NewgkelectiveSyncTeacherServiceImpl extends SyncBasedataService<Teacher, String> {

    @Autowired
    private NewGkSubjectTeacherService newGkSubjectTeacherService;
    @Autowired
    private NewGkTeacherPlanExService newGkTeacherPlanExService;
    @Autowired
    private NewGkTimetableTeacherService newGkTimetableTeacherService;

    @Override
    protected void add(Teacher... teacher) {

    }

    @Override
    protected void update(Teacher... teacher) {

    }

    @Override
    protected void delete(String... teacherIds) {
        // NEWGKELECTIVES_SUBJECT_TEACHER(预授课程老师)
        newGkSubjectTeacherService.deleteByTeacherIds(teacherIds);
        // NEWGKELECTIVE_TEACHER_PLAN_EX(具体教师安排)
        newGkTeacherPlanExService.deleteByTeacherIds(teacherIds);
        // NEWGKELECTIVE_TIMETABLE_TEACH(周课表老师)
        newGkTimetableTeacherService.deleteByTeacherIds(teacherIds);
    }

    @Override
    protected String preDelete(String... teacherId) {
        return null;
    }

    @Override
    protected String preUpdate(Teacher teacher) {
        return null;
    }

    @Override
    protected String preAdd(Teacher teacher) {
        return null;
    }
}
