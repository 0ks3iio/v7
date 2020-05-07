package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuDevelopHolidayNoticeDao;
import net.zdsoft.studevelop.data.entity.StuDevelopHolidayNotice;
import net.zdsoft.studevelop.data.service.StuDevelopHolidayNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("StuDevelopHolidayNoticeService")
public class StuDevelopHolidayNoticeServiceImpl extends BaseServiceImpl<StuDevelopHolidayNotice ,String>  implements StuDevelopHolidayNoticeService {
    @Autowired
    private StuDevelopHolidayNoticeDao stuDevelopHolidayNoticeDao;

    @Override
    protected BaseJpaRepositoryDao<StuDevelopHolidayNotice, String> getJpaDao() {
        return stuDevelopHolidayNoticeDao;
    }

    @Override
    protected Class<StuDevelopHolidayNotice> getEntityClass() {
        return StuDevelopHolidayNotice.class;
    }

    @Override
    public void save(StuDevelopHolidayNotice stuDevelopHolidayNotice) {
        stuDevelopHolidayNoticeDao.save(stuDevelopHolidayNotice);
    }

    @Override
    public List<StuDevelopHolidayNotice> getStuDevelopHolidayNoticeByUnitId(String acadyear, String semester, String unitId) {
        return stuDevelopHolidayNoticeDao.getStuDevelopHolidayNoticeByUnitId(acadyear,semester,unitId);
    }
}
