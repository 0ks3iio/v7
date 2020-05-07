package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Region;

public interface RegionRemoteService extends BaseRemoteService<Region,String> {

    /**
     * 获取地区信息
     * 
     * @param fullCode
     *            完整的行政区划码
     * @return List(Region)
     */
    public String findByFullCode(String fullCode);

    public String findByType(String type);

    public String findByFullCode(String type, String fullCode);

    public String findByFullCodes(String type, String... fullCodes);

    /**
     * 获取此地区的下级地区信息
     * 
     * @param regionCode
     *            完整的行政区划码
     * @return List(Region)
     */
    public String findSubRegionByFullCode(String regionCode);

    public String findByFullNameLike(String type, String fullName);

    public String findByFullCodeLike(String type, String fullCode);

    public String findByFullCodeLike(String type, String fullCode, String excludeFullCode);

    public String findByFullCodeLike(String type, String fullCode, String excludeFullCode, Integer unitClass);

    /**
     * 根据6位行政区划码检索直接下级行政区划列表
     * 
     * @param fullRegionCode
     * @return
     */
    public String findUnderlineRegions(final String fullRegionCode);

    /**
     * @param regionCode
     * @return
     */
    public String findRegionsByFullCode(String regionCode);
    
    /**取区县
     * @param regionCode
     * @return
     */
    public String findRegionsByRegionCode(String regionCode);

    /**
     * 获取省级行政区划
     * 
     * @author cuimq
     * @param type
     * @return
     */
    public String findProviceRegionByType(String type);
    /**
     * 获取某一批级别的行政区划
     * @param regionCode
     * @param length
     * @return
     */
    public String findSameRegion(String regionCode,Integer length);

    /**
     * 根据regionCode或者regionName获取行政区划
     * 优先根据RegionCode判断
     * regionCode = 'codeOrName' or regionName like 'codeOrName%'
     * @param codeOrName regionCode或者regionName
     * @return
     */
    Region getRegionByCodeOrName(String codeOrName);
}
