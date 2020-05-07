package net.zdsoft.szxy.operation.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.zdsoft.szxy.base.enu.UnitRegionLevel;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/3/30 下午3:56
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDataRegionHolder {

    private static ThreadLocal<Set<String>> regionLocal = new ThreadLocal<>();

    static UserDataRegionHolder holder = new UserDataRegionHolder();

    public static Set<String> getRegions() {
        return regionLocal.get();
    }

    public static int getTopRegionLevel() {
        Set<String> regions = getRegions();
        if (CollectionUtils.isEmpty(regions) || regions.contains(Group.ALL_REGION)) {
            return UnitRegionLevel.REGION_COUNTRY;
        }
        int level = UnitRegionLevel.REGION_COUNTY;
        for (String region : regions) {
            if (region.length() == 4 && level>UnitRegionLevel.REGION_CITY) {
                level = UnitRegionLevel.REGION_CITY;
            }
            else if (region.length() == 6 && level>UnitRegionLevel.REGION_COUNTY) {
                level = UnitRegionLevel.REGION_CITY;
            }
            else if (region.length() == 2) {
                level = UnitRegionLevel.REGION_PROVINCE;
            }
            else {
                level = UnitRegionLevel.REGION_COUNTRY;
                break;
            }
        }
        return level;
    }

    static void remove() {
        regionLocal.remove();
    }

    static void setRegion(Set<String> regions) {
        regionLocal.set(regions);
    }
}
