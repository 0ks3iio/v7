package net.zdsoft.api.base.dao;

import net.zdsoft.api.base.dto.OpenApiDeveloperAppCounter;
import net.zdsoft.api.base.entity.eis.OpenApiApp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/5/21 上午11:01
 */
@Repository
public interface OpenApiAppDao extends JpaRepository<OpenApiApp, String>, JpaSpecificationExecutor<OpenApiApp> {

    @Modifying
    @Query(value = "update OpenApiApp set status=?1 where id=?2")
    void modifyAppStatus(Integer status, String id);

    @Modifying
    @Query(value = "update OpenApiApp set developerId=?1 where id=?2")
    void bindDeveloperId(String developerId, String id);

    @Modifying
    @Query(value = "update OpenApiApp set deleted=1 where id=?1")
    @Override
    void deleteById(String s);

    @Query(value = "from OpenApiApp where developerId=?1 and deleted=0")
    List<OpenApiApp> getAppsByDeveloperId(String developerId);

    @Query(value = "select new net.zdsoft.api.base.dto.OpenApiDeveloperAppCounter(developerId, count(*)) from OpenApiApp where status in (0, 1, 2) and deleted=0 and developerId in (?1) group by developerId")
    List<OpenApiDeveloperAppCounter> getDeveloperAppCounter(String[] developerIds);

    @Query(value = "select iconUrl from OpenApiApp where id=?1")
    String getIconUrl(String id);

    @Query(value = "from OpenApiApp where id in ?1 and deleted=0")
    List<OpenApiApp>  findByIds(String[] appIds);
}
