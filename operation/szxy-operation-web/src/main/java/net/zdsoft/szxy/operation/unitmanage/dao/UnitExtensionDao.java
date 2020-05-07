package net.zdsoft.szxy.operation.unitmanage.dao;

import net.zdsoft.szxy.base.entity.UnitExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yangkj
 * @since 2019/1/16
 */
@Repository
public interface UnitExtensionDao extends JpaRepository<UnitExtension, String> {

    @Modifying
    @Query(value = "update UnitExtension set usingState = ?1 where unitId = ?2 ")
    void updateUsingStateByUnitId(Integer usingState, String unitId);

    @Modifying
    @Query(value = "update UnitExtension set usingNature = ?1 where unitId = ?2 ")
    void updateUsingNatureByUnitId(Integer usingNature, String unitId);

    @Modifying
    @Query(value = "update UnitExtension set expireTimeType = ?1 where unitId = ?2 ")
    void updateExpireTimeTypeByUnitId(Integer expireTimeType, String unitId);

    UnitExtension findByUnitId(String unitId);

    void deleteUnitExtensionsByUnitId(String unitId);

    /**
     * update单位星级
     * @param unitId
     * @param starLevel
     */
    @Transactional
    @Modifying
    @Query(
            value = "update UnitExtension set starLevel=?1 where unitId=?2 "
    )
    void updateStarLevelByUnitId(Integer starLevel, String unitId);

}
