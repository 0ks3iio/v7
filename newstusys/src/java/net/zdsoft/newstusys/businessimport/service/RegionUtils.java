package net.zdsoft.newstusys.businessimport.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.framework.config.Evn;

public class RegionUtils {
    private static Map<String, String> fullNameMapByCodeName = new HashMap<String, String>();
    private static RegionRemoteService regionRemoteService;

    public RegionUtils() {

    }

    /**
     * 通过行政区划名称得到行政区划码
     * 
     * @param regionName 行政区划名称
     * @return 行政区划码
     */
    public static String getRegionCodeByFullName(String regionName) {
        if (fullNameMapByCodeName.get(regionName) == null) {
            if (null == regionRemoteService) {
            	regionRemoteService = Evn.getBean("regionRemoteService");
            }
            List<Region> regionList = Region.dt(regionRemoteService.findByType("1"));
            for (Region re : regionList) {
                fullNameMapByCodeName.put(re.getFullName(), re.getFullCode());
                fullNameMapByCodeName.put(re.getFullCode(), re.getFullCode());
            }
        }
        return (String) fullNameMapByCodeName.get(regionName);
    }
}
