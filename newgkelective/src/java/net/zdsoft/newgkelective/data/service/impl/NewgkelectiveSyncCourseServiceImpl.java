package net.zdsoft.newgkelective.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkClassBatchService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkCourseHeapService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTeacherService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
@Service("newgkelectiveSyncCourseService")
public class NewgkelectiveSyncCourseServiceImpl extends SyncBasedataService<Course, String> {

    @Autowired
    private NewGkChoResultService newGkChoResultService;
    @Autowired
    private NewGkClassBatchService newGkClassBatchService;
    @Autowired
    private NewGkCourseHeapService newGkCourseHeapService;
    @Autowired
    private NewGkDivideClassService newGkDivideClassService;
    @Autowired
    private NewGkOpenSubjectService newGkOpenSubjectService;
    @Autowired
    private NewGkScoreResultService newGkScoreResultService;
    @Autowired
    private NewGKStudentRangeService newGKStudentRangeService;
    @Autowired
    private NewGKStudentRangeExService newGKStudentRangeExService;
    @Autowired
    private NewGkSubjectTeacherService newGkSubjectTeacherService;
    @Autowired
    private NewGkSubjectTimeService newGkSubjectTimeService;
    @Autowired
    private NewGkTeacherPlanService newGkTeacherPlanService;
    @Autowired
    private NewGkTimetableService newGkTimetableService;
    @Autowired
    private NewGkClassCombineRelationService newGkClassCombineRelationService;
    @Autowired
    private NewGkClassSubjectTimeService newGkClassSubjectTimeService;

    @Override
    protected void add(Course... course) {

    }

    @Override
    protected void update(Course... course) {

    }

    @Override
    protected void delete(String... subjectIds) {
        // NEWGKELECTIVE_CHOICE_RESULT(选课结果)
        newGkChoResultService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_CLAS_COMB_RELA(班级课程合并关联表)
        newGkClassCombineRelationService.deleteBySubjectIdOrClassIds(subjectIds);
        // NEWGKELECTIVE_CLAS_SUBJ_TIME(班级课程特征)
        newGkClassSubjectTimeService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_CLASS_BATCH(班级批次详情表) -> SUBJECTIDS AND SUBJECTID
        newGkClassBatchService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_COURSE_HEAP(算法分摊)
        newGkCourseHeapService.deleteBySubjectIds(subjectIds);
        // like "%%"
        // NEWGKELECTIVE_DIVIDE_CLASS(分班后班级)
        newGkDivideClassService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_OPEN_SUBJECT(分班开设科目)
        newGkOpenSubjectService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_RELATE_SUBTIME(连排数据关联)
        //newGkRelateSubtimeService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_SCORE_RESULT(成绩信息)
        newGkScoreResultService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_SECTION_BEGIN(算法轮次初始状态)
        //newGkSectionBeginService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_SECTION_END( 算法轮次结果表)
        //newGkSectionEndService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_SECTOIN_RESULT(算法轮次班级结果)
        //newGkSectionResultService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_STU_RANGE(分班学生范围)
        newGKStudentRangeService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_STU_RANGE_EX(分班学生范围扩展表)
        newGKStudentRangeExService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_SUBJECT_TEACHER(预授课程老师)
        newGkSubjectTeacherService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_SUBJECT_TIME(科目周课时)
        newGkSubjectTimeService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_TEACHER_PLAN(教师安排)
        newGkTeacherPlanService.deleteBySubjectIds(subjectIds);
        // NEWGKELECTIVE_TIMETABLE(周课表)
        newGkTimetableService.deleteBySubjectIds(subjectIds);
    }

    @Override
    protected String preDelete(String... subjectId) {
      
        return null;
    }

    @Override
    protected String preUpdate(Course course) {
        return null;
    }

    @Override
    protected String preAdd(Course course) {
        return null;
    }
}
