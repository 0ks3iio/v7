package net.zdsoft.basedata.extension.dao;

import net.zdsoft.basedata.extension.entity.ServerExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/26 上午11:13
 */
@Repository
public interface ServerExtemsionDao extends JpaRepository<ServerExtension, String> {

    @Query(value = "from ServerExtension where unitId=?1")
    List<ServerExtension> getEnableServerCodesByUnitId(String unitId);

    @Query(value = "from ServerExtension where unitId in ?1")
	List<ServerExtension> findByUnitIdIn(String... unitIds);

    @Modifying
	@Query("delete from ServerExtension where unitId in (?1)")
	void deleteByUnitIdIn(String... unitIds);
}
