package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuDevelopSchoolNoticeDao;
import net.zdsoft.studevelop.data.entity.StudevelopSchoolNotice;
import net.zdsoft.studevelop.data.service.StuDevelopSchoolNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/4/8.
 */
@Service("stuDevelopSchoolNoticeService")
public class StuDevelopSchoolNoticeServiceImpl extends BaseServiceImpl<StudevelopSchoolNotice,String> implements StuDevelopSchoolNoticeService {
    @Autowired
    private StuDevelopSchoolNoticeDao stuDevelopSchoolNoticeDao;
    @Override
    protected BaseJpaRepositoryDao<StudevelopSchoolNotice, String> getJpaDao() {
        return stuDevelopSchoolNoticeDao;
    }

    @Override
    protected Class<StudevelopSchoolNotice> getEntityClass() {
        return StudevelopSchoolNotice.class;
    }

    @Override
    public StudevelopSchoolNotice getSchoolNoticeByAcadyearSemesterUnitId(String acadyear, String semester, String schoolSection, String unitId) {
        return stuDevelopSchoolNoticeDao.getSchoolNoticeByAcadyearSemesterUnitId(acadyear,semester,schoolSection,unitId);
    }
}
