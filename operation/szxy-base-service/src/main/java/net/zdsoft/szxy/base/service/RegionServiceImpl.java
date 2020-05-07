package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.RegionRemoteService;
import net.zdsoft.szxy.base.dao.RegionDao;
import net.zdsoft.szxy.base.entity.Region;
import net.zdsoft.szxy.base.enu.RegionTypeCode;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/21 下午5:45
 */
@Service("regionRemoteService")
public class RegionServiceImpl implements RegionRemoteService {

    @Resource
    private RegionDao regionDao;

    private String checkForDefaultType(String type) {
        if (StringUtils.isBlank(type)) {
            type = RegionTypeCode.TYPE_1;
        }
        return type;
    }


    @Override
    public List<Region> getProvinceRegionByType(String type) {
        return regionDao.getProvinceRegions(checkForDefaultType(type));
    }

    @Override
    public List<Region> getUnderlingRegionByFullCode(String fullCode, String type) {
        AssertUtils.notNull(fullCode, "FullCode can't null");
        Region region = getRegionByFullCode(fullCode, type);
        if (region != null) {
            String searchCode = region.getRegionCode();
            if (region.getRegionCode().length() == 4) {
                searchCode = region.getRegionCode() + "__";
            }
            else if (region.getRegionCode().length() == 2) {
                searchCode = region.getRegionCode() + "__00";
            }
            return regionDao.getUnderlingRegionByRegionCode(searchCode, checkForDefaultType(type));
        }
        return Collections.emptyList();
    }

    @Override
    public Region getRegionByFullCode(String fullCode, String type) {
        AssertUtils.notNull(fullCode, "FullCode can't null");
        return regionDao.getRegionByFullCode(fullCode, checkForDefaultType(type));
    }

    @Record(type = RecordType.Call)
    @Override
    public Region getRegionByRegionCode(String regionCode) {
        return regionDao.getRegionByRegionCode(regionCode);
    }

    @Record(type = RecordType.Call)
    @Override
    public List<Region> getRegionsByRegionCodes(String[] regionCodes) {
        if (ArrayUtils.isEmpty(regionCodes)) {
            return Collections.emptyList();
        }
        return regionDao.getRegionsByRegionCodes(regionCodes);
    }
}
