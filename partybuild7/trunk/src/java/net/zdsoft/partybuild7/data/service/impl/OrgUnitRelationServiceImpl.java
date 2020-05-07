package net.zdsoft.partybuild7.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.dao.OrgUnitRelationDao;
import net.zdsoft.partybuild7.data.entity.OrgUnitRelation;
import net.zdsoft.partybuild7.data.service.OrgUnitRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("OrgUnitRelationService")
public class OrgUnitRelationServiceImpl extends BaseServiceImpl<OrgUnitRelation , String> implements OrgUnitRelationService {
    @Autowired
    private OrgUnitRelationDao orgUnitRelationDao;
    @Override
    protected BaseJpaRepositoryDao<OrgUnitRelation, String> getJpaDao() {
        return orgUnitRelationDao;
    }

    @Override
    protected Class<OrgUnitRelation> getEntityClass() {
        return OrgUnitRelation.class;
    }

    @Override
    public List<OrgUnitRelation> getAllByUnitId(String unitId) {
        return orgUnitRelationDao.getAllByUnitId(unitId);
    }
}
