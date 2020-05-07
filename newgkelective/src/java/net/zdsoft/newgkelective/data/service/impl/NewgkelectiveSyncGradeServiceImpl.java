package net.zdsoft.newgkelective.data.service.impl;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.newgkelective.data.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("newgkelectiveSyncGradeService")
public class NewgkelectiveSyncGradeServiceImpl extends SyncBasedataService<Grade, String> {

    @Autowired
    private NewGkArrayService newGkArrayService;
    @Autowired
    private NewGkChoiceService newGkChoiceService;
    @Autowired
    private NewGkDivideService newGkDivideService;
    @Autowired
    private NewGkReferScoreService newGkReferScoreService;
    @Override
    protected void add(Grade... grade) {

    }

    @Override
    protected void update(Grade... grade) {

    }

    @Override
    protected void delete(String... gradeIds) {
        // NEWGKELECTIVE_ARRAY(排课)
        newGkArrayService.deleteByGradeIds(gradeIds);
        // NEWGKELECTIVE_CHOICE(选课)
        newGkChoiceService.deleteByGradeIds(gradeIds);
        // NEWGKELECTIVE_DIVIDE(分班)
        newGkDivideService.deleteByGradeIds(gradeIds);
        // NEWGKELECTIVE_REFER_SCORE(参考成绩)
        newGkReferScoreService.deleteByGradeIds(gradeIds);
    }

    @Override
    protected String preDelete(String... s) {
    	
        return null;
    }

    @Override
    protected String preUpdate(Grade grade) {
        return null;
    }

    @Override
    protected String preAdd(Grade grade) {
        return null;
    }
}
