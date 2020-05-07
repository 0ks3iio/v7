package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Region;

public interface RegionService extends BaseService<Region, String> {

    /**
     * 获取行政区划</br> type默认用{@code Region.TYPE_1 }
     * 
     * @param regionCode
     *            完整的行政区划码
     * @return {@link Region}
     */
    Region findByFullCode(String regionCode);
    
    List<Region> findByFullNameLike(String type, String fullName);
    
    Region findByFullCode(String type, String fullCode);
    
    List<Region> findByType(String type);
    
    List<Region> findByFullCodeLike(String type, String fullCode);
    
    List<Region> findByFullCodes(String type, String... fullCodes);
    
    List<Region> findByFullCodeLike(String type, String fullCode, String excludeFullCode);
    
    List<Region> findByFullCodeLike(String type, String fullCode, String excludeFullCode, Integer unitClass);
    
    List<Region> findRegionsByRegionCode(String regionCode);
    
    List<Region> getRegionByCodeOrName(String codeOrName, String codeOrNameLike);

    /**
     * 获取行政区划</br>
     * 
     * @param regionCode
     *            完整的行政区划码
     * @param type
     *            {@code Region.TYPE_1,Region.TYPE_2}
     * @return {@link Region}
     */
    Region findByFullCodeAndType(String regionCode, String type);

    /**
     * 
     * @param regionCode
     *            is RegionCode not full code
     * @return
     */
    List<Region> findSubRegionByRegionCode(String regionCode);

    List<Region> findUnderlineRegions(final String fullRegionCode);

    /**
     * 获取完整行政区划名称
     * 
     * @author cuimq
     * @param fullCodes
     * @return
     */
    //@Cacheable(key = "'regionNameMap.fullCOd'")
    Map<String, String> findFullNameByFullCode(String[] fullCodes);

    /**
     * 获取省级行政区划
     * 
     * @author cuimq
     * @param type
     * @return
     */
    List<Region> findProviceRegionByType(String type);
    /**
     * 获取某一批级别的行政区划
     * @param regionCode
     * @param length
     * @return
     */
    List<Region> findSameRegion(String regionCode,Integer length);
}
