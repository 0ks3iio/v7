package net.zdsoft.infrastructure.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.infrastructure.dao.InfrastructureProjectDao;
import net.zdsoft.infrastructure.entity.InfrastructureProject;
import net.zdsoft.infrastructure.service.InfrastructureProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luf on 2018/11/14.
 */
@Service("infrastructureProjectService")
public class InfrastructureProjectServiceImpl extends BaseServiceImpl<InfrastructureProject , String> implements InfrastructureProjectService {

    @Autowired
    private InfrastructureProjectDao infrastructureProjectDao;
    @Override
    protected BaseJpaRepositoryDao<InfrastructureProject, String> getJpaDao() {
        return infrastructureProjectDao;
    }

    @Override
    protected Class<InfrastructureProject> getEntityClass() {
        return InfrastructureProject.class;
    }

    @Override
    public List<InfrastructureProject> getInfrastructureListByUnitId(String unitId, Pagination page) {
        List<InfrastructureProject> list = infrastructureProjectDao.getInfrastructureListByUnitId(unitId , page.toPageable());
        int count = infrastructureProjectDao.getInfrastructureListCountByUnitId(unitId);
        page.setMaxRowCount(count);
        return list;
    }

    @Override
    public List<InfrastructureProject> getInfrastructureListBySchName(String schoolName, String unitId, Pagination page) {
        List<InfrastructureProject> list = infrastructureProjectDao.getInfrastructureListByProjectSchool("%" +schoolName+"%",unitId,page.toPageable());
        int count = infrastructureProjectDao.getInfrastructureListCountByProjectSchool("%" +schoolName+"%" , unitId);
        page.setMaxRowCount(count);
        return list;
    }
}
