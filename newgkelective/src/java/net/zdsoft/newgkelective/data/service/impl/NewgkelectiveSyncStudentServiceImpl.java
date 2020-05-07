package net.zdsoft.newgkelective.data.service.impl;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("newgkelectiveSyncStudentService")
public class NewgkelectiveSyncStudentServiceImpl extends SyncBasedataService<Student, String> {

    @Autowired
    private NewGkChoResultService newGkChoResultService;
    @Autowired
    private NewGkClassStudentService newGkClassStudentService;
    @Autowired
    private NewGkScoreResultService newGkScoreResultService;
    @Autowired
    private NewGKStudentRangeService newGKStudentRangeService;

    @Override
    protected void add(Student... student) {

    }

    @Override
    protected void update(Student... student) {

    }

    @Override
    protected void delete(String... studentId) {
        // NEWGKELECTIVE_CHOICE_RESULT(选课结果)
        newGkChoResultService.deleteByStudentIds(studentId);
        // NEWGKELECTIVE_CLASS_STUDENT(班级内学生)
        newGkClassStudentService.deleteByStudentIds(studentId);
        // NEWGKELECTIVE_SCORE_RESULT(成绩信息)
        newGkScoreResultService.deleteByStudentIds(studentId);
        // NEWGKELECTIVE_STU_RANGE(分班学生范围)
        newGKStudentRangeService.deleteByStudentIds(studentId);
    }

    @Override
    protected String preDelete(String... s) {
        return null;
    }

    @Override
    protected String preUpdate(Student student) {
        return null;
    }

    @Override
    protected String preAdd(Student student) {
        return null;
    }
}
