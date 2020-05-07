/* 
 * @(#)TeachPlaceServiceImpl.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dao.TeachPlaceDao;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Validators;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TeachPlaceServiceImpl extends BaseServiceImpl<TeachPlace, String> implements TeachPlaceService {

    @Autowired
    private TeachPlaceDao teachPlaceDao;

    @Override
    public Map<String, String> findTeachPlaceMap(final String unitId, final String type) {
        // 动态拼接查询条件
        Specification<TeachPlace> specification = new Specification<TeachPlace>() {
            @Override
            public Predicate toPredicate(Root<TeachPlace> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                if (!Validators.isEmpty(type)) {
                    ps.add(cb.like(root.get("placeType").as(String.class), "%" + type + "%"));
                }
                cq.where(ps.toArray(new Predicate[ps.size()]));
                return cq.getRestriction();
            }
        };

        List<TeachPlace> teachPlaces = findAll(specification);

        return EntityUtils.getMap(teachPlaces, "id", "placeName");
    }

    @Override
    protected BaseJpaRepositoryDao<TeachPlace, String> getJpaDao() {
        return teachPlaceDao;
    }

    @Override
    protected Class<TeachPlace> getEntityClass() {
        return TeachPlace.class;
    }

    @Override
    public Map<String, String> findTeachPlaceMap(String[] ids) {
		List<Object[]> list = null;
		if(ids != null && ids.length>0) {
			list = teachPlaceDao.findPartPlaByIds(ids);
		}
		Map<String, String> map =new HashMap<>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] strs :list){
				map.put((String)strs[0], (String)strs[1]);
			}
		}
		return map;
    }

	@Override
	public List<TeachPlace> findTeachPlaceListByType(final String unitId, final String type) {
		 // 动态拼接查询条件
        Specification<TeachPlace> specification = new Specification<TeachPlace>() {
            @Override
            public Predicate toPredicate(Root<TeachPlace> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                if (!Validators.isEmpty(type)) {
                	ps.add(cb.equal(root.get("placeType").as(String.class), type));
                }
                cq.where(ps.toArray(new Predicate[ps.size()]));
                return cq.getRestriction();
            }
        };

        return findAll(specification);
	}

	@Override
	public Map<String, Object> findTeachPlaceMapByAttr(String[] ids, String attrName) {
		List<TeachPlace> list = teachPlaceDao.findListBy(TeachPlace.class, null, null, "id", ids, new String[] {"id",attrName});
		Map<String,Object> linkedMap = new LinkedHashMap<>();
		try {
			Field field = TeachPlace.class.getDeclaredField(attrName);
			field.setAccessible(true);
			for (TeachPlace tp : list) {
				linkedMap.put(tp.getId(), field.get(tp));
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}  catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        return linkedMap;
	}

	@Override
	public List<TeachPlace> findByUnitIdIn(String[] unitIds) {
		return teachPlaceDao.findByUnitIdIn(unitIds);
	}
}
