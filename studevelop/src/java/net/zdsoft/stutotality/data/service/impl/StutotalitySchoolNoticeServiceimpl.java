package net.zdsoft.stutotality.data.service.impl;

import com.sun.org.apache.regexp.internal.RE;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.dao.StutotalitySchoolNoticeDao;
import net.zdsoft.stutotality.data.entity.StutotalitySchoolNotice;
import net.zdsoft.stutotality.data.service.StutotalitySchoolNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stutotalitySchoolNoticeService")
public class StutotalitySchoolNoticeServiceimpl extends BaseServiceImpl<StutotalitySchoolNotice,String> implements StutotalitySchoolNoticeService {

    @Autowired
    private StutotalitySchoolNoticeDao stutotalitySchoolNoticeDao;




    @Override
    protected BaseJpaRepositoryDao<StutotalitySchoolNotice, String> getJpaDao() {
        return stutotalitySchoolNoticeDao;
    }

    @Override
    protected Class<StutotalitySchoolNotice> getEntityClass() {
        return StutotalitySchoolNotice.class;
    }

    @Override
    public StutotalitySchoolNotice findByUnitIdAndAcadyearAndSemesterAndGradeId(String unitId, String acadyear, String semester, String gradeId) {
        return stutotalitySchoolNoticeDao.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId, acadyear, semester, gradeId);
    }

    @Override
    public List<StutotalitySchoolNotice> findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester) {
        return stutotalitySchoolNoticeDao.findByUnitIdAndAcadyearAndSemester(unitId, acadyear, semester);
    }

    @Override
    public void deleteByUnitId(String unitId) {
        stutotalitySchoolNoticeDao.deleteByUnitId(unitId);
    }
}
