package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Region;
import net.zdsoft.szxy.base.enu.RegionTypeCode;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午5:10
 */
@Repository
public interface RegionDao extends JpaRepository<Region, String> {

    @Record(type = RecordType.SQL)
    @Query(value = "from Region where fullCode=?1 and type=?2")
    Region getRegionByFullCode(String fullCode, String type);

    @Record(type = RecordType.SQL)
    @Query(value = "from Region where type=?1 and length(regionCode)=2 ")
    List<Region> getProvinceRegions(String type);

    @Record(type = RecordType.SQL)
    @Query(value = "from Region where type=?2 and fullCode like ?1")
    List<Region> getUnderlingRegionByRegionCode(String regionCode, String type);

    @Record(type = RecordType.SQL)
    @Query(value = "from Region where regionCode in (?1) and type='" + RegionTypeCode.TYPE_1 + "'")
    List<Region> getRegionsByRegionCodes(String[] regionCodes);

    /**
     * 根据regionCode查询
     * @param regionCode regionCode
     * @return
     */
    @Query(value = "from Region where regionCode=?1 and type='" + RegionTypeCode.TYPE_1 + "'")
    Region getRegionByRegionCode(String regionCode);
}
