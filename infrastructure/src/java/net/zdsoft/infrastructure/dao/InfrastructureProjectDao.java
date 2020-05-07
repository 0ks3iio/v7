package net.zdsoft.infrastructure.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.infrastructure.entity.InfrastructureProject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

/**
 * Created by luf on 2018/11/14.
 */
public interface InfrastructureProjectDao extends BaseJpaRepositoryDao<InfrastructureProject ,String > {

    @Query( nativeQuery = true , value="select * from infrastr_project  where unit_id = ?1 order by creation_time desc")
    public List<InfrastructureProject> getInfrastructureListByUnitId(String unitId , Pageable pageable);

    @Query( nativeQuery = true , value="select count(*) from infrastr_project  where unit_id = ?1 order by creation_time desc")
    public int getInfrastructureListCountByUnitId(String unitId );

    @Query(nativeQuery = true , value="select * from infrastr_project  where project_School like  ?1 and unit_Id = ?2 order by creation_Time desc")
    public List<InfrastructureProject> getInfrastructureListByProjectSchool(String projectSchool, String unitId, Pageable pageable);

   @Query(nativeQuery = true , value="select count(*) from infrastr_project  where project_School like  ?1 and unit_Id = ?2 order by creation_Time desc")
    public int getInfrastructureListCountByProjectSchool(String projectSchool, String unitId);

}
