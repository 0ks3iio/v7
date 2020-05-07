package net.zdsoft.syncdata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.syncdata.dao.DYGradeDao;
import net.zdsoft.syncdata.entity.DYGrade;
import net.zdsoft.syncdata.service.DYGradeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dYGradeService")
public class DYGradeServiceImpl extends BaseServiceImpl<DYGrade, String> implements DYGradeService {

    @Autowired
    private DYGradeDao dyGradeDao;

    @Override
    protected BaseJpaRepositoryDao<DYGrade, String> getJpaDao() {
        return dyGradeDao;
    }

    @Override
    protected Class<DYGrade> getEntityClass() {
        return DYGrade.class;
    }

}
