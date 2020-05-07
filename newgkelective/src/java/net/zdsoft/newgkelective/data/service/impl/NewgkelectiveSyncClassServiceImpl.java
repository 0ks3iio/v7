package net.zdsoft.newgkelective.data.service.impl;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.newgkelective.data.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("newgkelectiveSyncClassService")
public class NewgkelectiveSyncClassServiceImpl extends SyncBasedataService<Clazz, String> {

    @Autowired
    private NewGkClassStudentService newGkClassStudentService;
    @Autowired
    private NewGkDivideClassService newGkDivideClassService;
    @Autowired
    private NewGkTeacherPlanExService newGkTeacherPlanExService;
    @Autowired
    private NewGkTimetableService newGkTimetableService;
    @Autowired
    private NewGkClassCombineRelationService newGkClassCombineRelationService;
    @Autowired
    private NewGkClassSubjectTimeService newGkClassSubjectTimeService;

    @Override
    protected void add(Clazz... clazz) {

    }

    @Override
    protected void update(Clazz... clazz) {

    }

    @Override
    protected void delete(String... classIds) {
        // NEWGKELECTIVE_CLAS_COMB_RELA(班级课程合并关联表)
        newGkClassCombineRelationService.deleteBySubjectIdOrClassIds(classIds);
        // NEWGKELECTIVE_CLAS_SUBJ_TIME(班级课程特征)
        newGkClassSubjectTimeService.deleteByClassIds(classIds);
        // NEWGKELECTIVE_CLASS_STUDENT(班级内学生)
        newGkClassStudentService.deleteByClassIds(classIds);
        // NEWGKELECTIVE_DIVIDE_CLASS(分班后班级) -> OLD_CLASS_ID
        newGkDivideClassService.deleteByClassIds(classIds);
        // NEWGKELECTIVE_TEACHER_PLAN_EX(具体教师安排) -> CLASSIDS
        // like "%%"
        newGkTeacherPlanExService.deleteByClassIds(classIds);
        // NEWGKELECTIVE_TIMETABLE(周课表)
        newGkTimetableService.deleteByClassIds(classIds);
    }

    @Override
    protected String preDelete(String... classId) {
        return null;
    }

    @Override
    protected String preUpdate(Clazz clazz) {
        return null;
    }

    @Override
    protected String preAdd(Clazz clazz) {
        return null;
    }
}
