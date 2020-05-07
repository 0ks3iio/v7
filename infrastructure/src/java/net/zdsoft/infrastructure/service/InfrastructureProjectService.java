package net.zdsoft.infrastructure.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.infrastructure.entity.InfrastructureProject;

import java.util.List;

/**
 * Created by luf on 2018/11/14.
 */
public interface InfrastructureProjectService extends BaseService<InfrastructureProject, String> {

//    Pagination page

    public List<InfrastructureProject> getInfrastructureListByUnitId(String unitId ,Pagination page);

    public List<InfrastructureProject> getInfrastructureListBySchName(String schoolName ,String unitId ,Pagination page);
}
