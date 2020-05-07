package net.zdsoft.system.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.ServerRegion;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ke_shen@126.com
 * @since 2018/3/6 上午11:30
 */
public interface ServerRegionDao extends BaseJpaRepositoryDao<ServerRegion, String> {


    List<ServerRegion> findByRegion(String region);

    @Query(
            value = "select distinct region From ServerRegion WHERE domain=?1"
    )
    List<String> findRegionByDomain(String domain);


    @Query(
            value = "select distinct region from ServerRegion"
    )

    List<String> findAllRegion();

    ServerRegion findByRegionAndServerId(String region, int serverId);

    List<ServerRegion> findByServerIdInAndRegion(Integer[] serverId, String region);

    @Query("from ServerRegion where region = ?1 and unitId = ?2")
	List<ServerRegion> findByRegionAndUnitId(String regionCode, String unitId);

    
	List<ServerRegion> findByUnitId(String unitId);

}
