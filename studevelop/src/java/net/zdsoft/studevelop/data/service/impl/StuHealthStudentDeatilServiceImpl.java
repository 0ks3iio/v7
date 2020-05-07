package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuHealthStudentDetailDao;
import net.zdsoft.studevelop.data.entity.StudevelopHealthStudentDetail;
import net.zdsoft.studevelop.data.service.StuHealthStudentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/4/18.
 */
@Service("StuHealthStudentDetailService")
public class StuHealthStudentDeatilServiceImpl extends BaseServiceImpl<StudevelopHealthStudentDetail,String> implements StuHealthStudentDetailService {
    @Autowired
    private StuHealthStudentDetailDao stuHealthStudentDetailDao;
    @Override
    protected BaseJpaRepositoryDao<StudevelopHealthStudentDetail, String> getJpaDao() {
        return stuHealthStudentDetailDao;
    }

    @Override
    protected Class<StudevelopHealthStudentDetail> getEntityClass() {
        return StudevelopHealthStudentDetail.class;
    }
}
