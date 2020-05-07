package net.zdsoft.qulity.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.qulity.data.dao.QualitStuRecoverDao;
import net.zdsoft.qulity.data.entity.QualitStuRecover;
import net.zdsoft.qulity.data.service.QualitStuRecoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("qualitStuRecoverService")
public class QualitStuRecoverServiceImpl extends BaseServiceImpl<QualitStuRecover, String> implements QualitStuRecoverService {
    @Autowired
    private QualitStuRecoverDao qualitStuRecoverDao;


    public List<QualitStuRecover> findListByUnitIDAndAcadyear(String unitId, String acadyear){
        return qualitStuRecoverDao.findListByUnitIdAndAcadyear(unitId,acadyear);
    }


    @Override
    protected BaseJpaRepositoryDao<QualitStuRecover, String> getJpaDao() {
        return qualitStuRecoverDao;
    }

    @Override
    protected Class<QualitStuRecover> getEntityClass() {
        return QualitStuRecover.class;
    }
}
