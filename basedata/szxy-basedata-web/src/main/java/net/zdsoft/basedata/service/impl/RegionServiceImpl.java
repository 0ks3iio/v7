package net.zdsoft.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.RegionDao;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.service.RegionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisUtils;

@Service("regionService")
public class RegionServiceImpl extends BaseServiceImpl<Region, String> implements RegionService {

    @Autowired
    private RegionDao regionDao;

    @Override
    protected BaseJpaRepositoryDao<Region, String> getJpaDao() {
        return regionDao;
    }

    @Override
    protected Class<Region> getEntityClass() {
        return Region.class;
    }

    @Override
    public Region findByFullCode(String regionCode) {
        return findByFullCodeAndType(regionCode, Region.TYPE_1);
    }

    @Override
    public Region findByFullCodeAndType(String regionCode, String type) {
        return regionDao.findByFullCodeAndType(regionCode, type);
    }

    @Override
    public List<Region> findUnderlineRegions(final String fullRegionCode) {

        return regionDao.findAll(new Specification<Region>() {
            @Override
            public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> criteriaQuery,
                    CriteriaBuilder criteriaBuilder) {
                List<Predicate> ps = Lists.newArrayList();
                String searchRegionCode = "";
                if (StringUtils.endsWith(fullRegionCode, "0000")) {
                    searchRegionCode = StringUtils.substring(fullRegionCode, 0, 2) + "__" + "00";
                }
                else if (StringUtils.endsWith(fullRegionCode, "00")) {
                    searchRegionCode = StringUtils.substring(fullRegionCode, 0, 4) + "__";
                }
                ps.add(criteriaBuilder.like(root.<String> get("fullCode"), searchRegionCode));
                ps.add(criteriaBuilder.equal(root.<String> get("type"), "1"));

                return criteriaQuery.where(ps.toArray(new Predicate[0]))
                        .orderBy(criteriaBuilder.desc(root.<String> get("regionCode"))).getRestriction();
            }
        });
    }

    @Override
    public List<Region> findSubRegionByRegionCode(final String regionCode) {
        return regionDao.findAll(new Specification<Region>() {
            @Override
            public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> criteriaQuery,
                    CriteriaBuilder criteriaBuilder) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(criteriaBuilder.like(root.<String> get("regionCode"), regionCode + "%"));
                return criteriaQuery.where(ps.toArray(new Predicate[0]))
                        .orderBy(criteriaBuilder.desc(root.<String> get("regionCode"))).getRestriction();
            }
        });
    }

    @Override
    public Map<String, String> findFullNameByFullCode(String[] fullCodes) {
        Map<String, String> fullCodeAndFullNameMap = new HashMap<String, String>();
        List<Region> regionList = regionDao.findByFullCodes(Region.TYPE_1, fullCodes);
        if (CollectionUtils.isNotEmpty(regionList)) {
            for (Region region : regionList) {
                fullCodeAndFullNameMap.put(region.getFullCode(), region.getFullName());
            }
        }
        return fullCodeAndFullNameMap;
    }

    @Override
    public List<Region> findProviceRegionByType(String type) {
        return regionDao.findProviceRegionByType(type);
    }
    public List<Region> findSameRegion(String regionCode,Integer length){
    	return regionDao.findSameRegion(regionCode,length);
    }

	@Override
	public List<Region> findByFullNameLike(String type, String fullName) {
		return regionDao.findByFullNameLike(type, fullName);
	}

	@Override
	public Region findByFullCode(String type, String fullCode) {
		return regionDao.findByFullCodeAndType(fullCode, type);
	}

	@Override
	public List<Region> findByType(String type) {
		return RedisUtils.getObject(RedisUtils.getKey(Region.class, "type", type), RedisUtils.TIME_FOREEVER,
				new TypeReference<List<Region>>() {
				}, () -> regionDao.findByType(type));
	}

	@Override
	public List<Region> findByFullCodeLike(String type, String fullCode) {
		return regionDao.findByFullCodeLike(type, fullCode);
	}

	@Override
	public List<Region> findByFullCodes(String type, String... fullCodes) {
		return regionDao.findByFullCodes(type, fullCodes);
	}

	@Override
	public List<Region> findByFullCodeLike(String type, String fullCode, String excludeFullCode) {
		return regionDao.findByFullCodeLike(type, fullCode, excludeFullCode);
	}

	@Override
	public List<Region> findByFullCodeLike(String type, String fullCode, String excludeFullCode, Integer unitClass) {
		return regionDao.findByFullCodeLike(type, fullCode, excludeFullCode, unitClass);
	}

	@Override
	public List<Region> findRegionsByRegionCode(String regionCode) {
		return regionDao.findRegionsByRegionCode(regionCode);
	}

	@Override
	public List<Region> getRegionByCodeOrName(String code, String name) {
		return regionDao.getRegionByCodeOrName(code, name);
	}
}
