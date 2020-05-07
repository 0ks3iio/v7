package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface RegionDao extends BaseJpaRepositoryDao<Region, String> {

    // Region findByFullCode(String regionCode, String type);

    /**
     * 获取行政区划
     *
     * @param regionCode 完整的行政区划码
     * @param type       {@code Region.TYPE_1 Region.TYPE_2}
     * @return {@link Region}
     */
    Region findByFullCodeAndType(String regionCode, String type);

    @Query("From Region where type = ?1 and fullName like ?2 order by fullCode")
    List<Region> findByFullNameLike(String type, String fullName);

    @Query("From Region where type = ?1 and fullCode IN ?2 order by fullCode")
    List<Region> findByFullCodes(String type, String... fullCodes);

    /**
     * 根据fullcode模糊查询行政区划，排除掉内置的000000（全国）
     *
     * @param type
     * @param fullCode
     * @return
     */
    @Query("From Region where type = ?1 and fullCode like ?2 and fullCode <> '000000' order by fullCode")
    List<Region> findByFullCodeLike(String type, String fullCode);

    @Query("From Region where type = ?1 order by fullCode")
    List<Region> findByType(String type);

    @Query("From Region where type = ?1 and fullCode like ?2 and fullCode not like ?3 order by fullCode")
    List<Region> findByFullCodeLike(String type, String fullCode, String excludeFullCode);

    @Query(nativeQuery = true, value = "select * from base_region where type = ?1 and full_code like ?2 and full_code not like ?3 "
            + "and exists(select 1 from base_unit where is_deleted = 0 and region_code = base_region.full_code and unit_class = ?4)")
    List<Region> findByFullCodeLike(String type, String fullCode, String excludeFullCode, Integer unitClass);

    /**
     * @param regionCode
     * @return
     */
    @Query("From Region where  fullCode =?1 ")
    Region findRegionsByFullCode(String regionCode);

    @Query("From Region where fullCode like '%0000' and type=?1")
    List<Region> findProviceRegionByType(String type);

    @Query(nativeQuery = true, value = "select * from base_region where region_code like ?1% and length(region_code) = 6")
    List<Region> findRegionsByRegionCode(String regionCode);

    @Query(nativeQuery = true, value = "select * from base_region where region_code like ?1% and length(region_code) = ?2")
    List<Region> findSameRegion(String regionCode, Integer length);

    @Query(
            value = "from Region where regionCode=?1 or regionName like ?2"
    )
    List<Region> getRegionByCodeOrName(String code, String name);
}
