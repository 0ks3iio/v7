package net.zdsoft.basedata.extension.dao;

import net.zdsoft.basedata.extension.entity.UnitExtension;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 */
@Repository
public interface UnitExtensionDao extends BaseJpaRepositoryDao<UnitExtension, String> {

    @Query("from UnitExtension where unitId=?1")
    Optional<UnitExtension> getUnitExtensionByUnitId(String unitId);

    @Query("from UnitExtension where unitId in ?1") 
	List<UnitExtension> findByUnitIdIn(String... unitIds);
}
