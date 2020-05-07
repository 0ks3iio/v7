package net.zdsoft.basedata.remote.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.RegionService;
import net.zdsoft.framework.utils.SUtils;

@Service("regionRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class RegionRemoteServiceImpl extends BaseRemoteServiceImpl<Region, String> implements RegionRemoteService {

    @Autowired
    private RegionService regionService;

    @Override
    protected BaseService<Region, String> getBaseService() {
        return regionService;
    }

    @Override
    public String findByFullCode(String fullCode) {
        return SUtils.s(regionService.findByFullCode(fullCode));
    }

    @Override
    public String findSubRegionByFullCode(String regionCode) {
        return SUtils.s(regionService.findSubRegionByRegionCode(regionCode));
    }

    @Override
    public String findByFullNameLike(String type, String fullName) {
        return SUtils.s(regionService.findByFullNameLike(type, fullName));
    }

    @Override
    public String findByFullCode(String type, String fullCode) {
        return SUtils.s(regionService.findByFullCodeAndType(fullCode, type));
    }

    @Override
    public String findByType(String type) {
        return SUtils.s(regionService.findByType(type));
    }

    @Override
    public String findByFullCodeLike(String type, String fullCode) {
        return SUtils.s(regionService.findByFullCodeLike(type, fullCode));
    }

    @Override
    public String findByFullCodes(String type, String... fullCodes) {
        return SUtils.s(regionService.findByFullCodes(type, fullCodes));
    }

    @Override
    public String findByFullCodeLike(String type, String fullCode, String excludeFullCode) {
        return SUtils.s(regionService.findByFullCodeLike(type, fullCode, excludeFullCode));
    }

    @Override
    public String findByFullCodeLike(String type, String fullCode, String excludeFullCode, Integer unitClass) {
        return SUtils.s(regionService.findByFullCodeLike(type, fullCode, excludeFullCode, unitClass));
    }

    @Override
    public String findUnderlineRegions(String fullRegionCode) {
        return SUtils.s(regionService.findUnderlineRegions(fullRegionCode));
    }

    @Override
    public String findRegionsByFullCode(String regionCode) {
        return SUtils.s(regionService.findByFullCode(regionCode));
    }

    @Override
	public String findRegionsByRegionCode(String regionCode) {
		return SUtils.s(regionService.findRegionsByRegionCode(regionCode));
	}

	@Override
    public String findProviceRegionByType(String type) {
        return SUtils.s(regionService.findProviceRegionByType(type));
    }
	@Override
	public String findSameRegion(String regionCode,Integer length){
		 return SUtils.s(regionService.findSameRegion(regionCode,length));
	}

    @Override
    public Region getRegionByCodeOrName(String codeOrName) {
        Assert.notNull(codeOrName, "codeOrName不能为空");
        List<Region> regionList = regionService.getRegionByCodeOrName(codeOrName, codeOrName + "%");
        //优先根据regionCode判断
        for (Region region : regionList) {
            if (StringUtils.equals(region.getRegionCode(), codeOrName)) {
                return region;
            }
        }
        //当regionCode无法判断时取第一个Region
        if (!regionList.isEmpty()) {
            return regionList.get(0);
        }
        return null;
    }
}
