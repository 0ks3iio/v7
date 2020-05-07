package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.Region;
import net.zdsoft.szxy.monitor.Rpc;

import java.util.List;

/**
 * 基础数据： 行政区划
 * @author shenke
 * @since 2019/3/19 上午11:32
 */
@Rpc(domain = RecordName.name)
public interface RegionRemoteService {

    /**
     * 获取省级行政区划
     * @see net.zdsoft.szxy.base.enu.RegionTypeCode
     * @param type 默认{@link net.zdsoft.szxy.base.enu.RegionTypeCode#TYPE_1}
     * @return
     */
    List<Region> getProvinceRegionByType(String type);

    /**
     * 获取下属直属行政区划信息
     * @param fullCode 不能为空
     * @param type 默认{@link net.zdsoft.szxy.base.enu.RegionTypeCode#TYPE_1}
     * @return
     */
    List<Region> getUnderlingRegionByFullCode(String fullCode, String type);

    /**
     * 根据FullCode获取行政区划信息
     * @param fullCode
     * @param type 默认{@link net.zdsoft.szxy.base.enu.RegionTypeCode#TYPE_1}
     * @return
     */
    Region getRegionByFullCode(String fullCode, String type);

    /**
     * 根据regionCode 查询 默认为{@link net.zdsoft.szxy.base.enu.RegionTypeCode#TYPE_1}
     * @param regionCode regionCode
     * @return
     */
    Region getRegionByRegionCode(String regionCode);

    /**
     * 获取 指定regionCode的行政区划信息
     * @param regionCodes regionCode列表
     * @return List
     */
    List<Region> getRegionsByRegionCodes(String[] regionCodes);
}
