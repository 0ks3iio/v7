package net.zdsoft.system.service.mcode.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.dao.BaseJdbcDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.dao.mcode.McodeDetailDao;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.entity.mcode.McodeList;
import net.zdsoft.system.service.mcode.McodeDetailService;
import net.zdsoft.system.service.mcode.McodeListService;

@Service("mcodeDetailService")
public class McodeDetailServiceImpl implements McodeDetailService {

    public static final String KEY = "mcodeDetail.mcodeId.";
    @Autowired
    private BaseJdbcDao baseJdbcDao;
    @Autowired
    private McodeDetailDao mcodeDetailDao;
    @Autowired
    private McodeListService mcodeListService;

    @Override
    public List<String[]> findBySql(String sql, Object[] params) {
//        List<Object[]> oss = baseJdbcDao.findBySql(sql, params);
//        List<String[]> rt = new ArrayList<String[]>();
//        for(Object[] os : oss){
//        	List<String> list = new ArrayList<String>();
//        	for(Object o : os){
//        		String s = o.toString();
//        		list.add(s);
//        	}
//        	rt.add(list.toArray(new String[0]));
//        }
//        return rt;
    	return new ArrayList<String[]>();
    }

    @Override
    public McodeDetail findByMcodeAndThisId(final String mcodeId, final String thisId) {
    	if(StringUtils.isBlank(thisId)){
    		return null;
    	}
        return RedisUtils.getObject(KEY + mcodeId + ".One." + thisId, 0, new TypeReference<McodeDetail>() {
        }, new RedisInterface<McodeDetail>() {

            @Override
            public McodeDetail queryData() {
                return mcodeDetailDao.findByMcodeIdAndThisId(mcodeId, thisId);
            }

        });
    }

    @Override
    public List<McodeDetail> findByMcodeIds(String... mcodeId) {
        List<McodeDetail> findMcode = new ArrayList<McodeDetail>();
        List<McodeDetail> linMcode = new ArrayList<McodeDetail>();
        for (String mc : mcodeId) {
            final String mcId = mc;
            linMcode = RedisUtils.getObject(KEY + mcId + ".List", 0, new TypeReference<List<McodeDetail>>() {
            }, new RedisInterface<List<McodeDetail>>() {

                @Override
                public List<McodeDetail> queryData() {
                    return mcodeDetailDao.findByMcodeId(mcId);
                }

            });
            if (CollectionUtils.isNotEmpty(linMcode)) {
                findMcode.addAll(linMcode);
            }
        }
        return findMcode;
    }

    @Override
    public Map<String, McodeDetail> findMapByMcodeId(final String mcodeId) {
        final Map<String, McodeDetail> map = new HashMap<String, McodeDetail>();
        return RedisUtils.getObject(KEY + mcodeId + ".Map", 0, new TypeReference<Map<String, McodeDetail>>() {
        }, new RedisInterface<Map<String, McodeDetail>>() {

            @Override
            public Map<String, McodeDetail> queryData() {
                List<McodeDetail> mcodes = findByMcodeIds(mcodeId);
                for (McodeDetail mcode : mcodes) {
                    map.put(mcode.getThisId(), mcode);
                }
                return map;
            }

        });
    }

    @Override
    public Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds(String... mcodeIds) {
        List<McodeDetail> findByMcodeId = findByMcodeIds(mcodeIds);
        Map<String, Map<String, McodeDetail>> map = new HashMap<String, Map<String, McodeDetail>>();
        for (McodeDetail item : findByMcodeId) {
            Map<String, McodeDetail> map2 = map.get(item.getMcodeId());
            if (map2 == null) {
                map2 = new HashMap<String, McodeDetail>();
                map.put(item.getMcodeId(), map2);
            }
            map2.put(item.getThisId(), item);
        }
        return map;
    }

    @Override
    public Map<String, List<McodeDetail>> findMapListByMcodeIds(String... mcodeIds) {
        List<McodeDetail> findByMcodeId = findByMcodeIds(mcodeIds);
        Map<String, List<McodeDetail>> map = new HashMap<String, List<McodeDetail>>();
        for (McodeDetail item : findByMcodeId) {
            List<McodeDetail> list = map.get(item.getMcodeId());
            if (list == null) {
                list = new ArrayList<McodeDetail>();
                map.put(item.getMcodeId(), list);
            }
            list.add(item);
        }
        return map;
    }

    @Override
    public List<McodeDetail> saveAll(final String mcodeId, final List<McodeDetail> list) {
        mcodeDetailDao.saveAll(list);
        List<McodeDetail> returnList = mcodeDetailDao.findByMcodeId(mcodeId);
        Map<String, McodeDetail> map = new HashMap<String, McodeDetail>();
        for (McodeDetail mcode : returnList) {
            map.put(mcode.getThisId(), mcode);
        }
        RedisUtils.setObject(KEY + mcodeId + ".List", returnList);
        RedisUtils.setObject(KEY + mcodeId + ".Map", map);
        return returnList;
    }

    @Override
    public void deteleOne(McodeDetail mcodeDetail) {
        mcodeDetailDao.delete(mcodeDetail);
        RedisUtils.delBeginWith(KEY + mcodeDetail.getMcodeId());
        doRefreshCache(mcodeDetail.getMcodeId());
    }

    @Override
    public void doRefreshCache(String... mcodeId) {
        List<McodeDetail> findByMcodeId = mcodeDetailDao.findByMcodeId(mcodeId);
        final Map<String, List<McodeDetail>> mapList = new HashMap<String, List<McodeDetail>>();
        List<McodeDetail> list = null;
        for (McodeDetail item : findByMcodeId) {
            list = mapList.get(item.getMcodeId());
            if (list == null) {
                list = new ArrayList<McodeDetail>();
                mapList.put(item.getMcodeId(), list);
            }
            list.add(item);
        }
        final Map<String, Map<String, McodeDetail>> mapMap = new HashMap<String, Map<String, McodeDetail>>();
        Map<String, McodeDetail> map2 = null;
        for (McodeDetail item : findByMcodeId) {
            map2 = mapMap.get(item.getMcodeId());
            if (map2 == null) {
                map2 = new HashMap<String, McodeDetail>();
                mapMap.put(item.getMcodeId(), map2);
            }
            map2.put(item.getThisId(), item);
        }
        for (String string : mcodeId) {
            final String mcId = string;
            RedisUtils.delBeginWith(KEY + mcId);
            list = mapList.get(mcId);
            if (CollectionUtils.isNotEmpty(list)) {
                for (McodeDetail item : list) {
                    final McodeDetail mcodeDetail = item;
                    RedisUtils.setObject(KEY + mcodeDetail.getMcodeId() + ".One." + mcodeDetail.getThisId(),
                            mcodeDetail);
                }
                RedisUtils.setObject(KEY + mcodeId + ".List", mapList.get(mcId));
                RedisUtils.setObject(KEY + mcodeId + ".Map", mapMap.get(mcId));
            }
        }
    }

    @Override
    public void doRefreshCacheAll() {
        List<McodeList> findAll = mcodeListService.findAll();
        Set<String> mcodeId = new HashSet<String>();
        for (McodeList item : findAll) {
            mcodeId.add(item.getMcodeId());
        }
        doRefreshCache(mcodeId.toArray(new String[0]));
    }

    @Override
    public List<McodeDetail> findByMcodeId(final String mcodeId, Pagination page) {
        if (page == null) {
            return findByMcodeIds(mcodeId);
        }
        Pageable pageable = Pagination.toPageable(page);
        Specification<McodeDetail> specification = new Specification<McodeDetail>() {
            @Override
            public Predicate toPredicate(Root<McodeDetail> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("mcodeId").as(String.class), mcodeId));
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("displayOrder").as(Integer.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        Page<McodeDetail> findAll = mcodeDetailDao.findAll(specification, pageable);
        page.setMaxRowCount((int) findAll.getTotalElements());
        return findAll.getContent();
    }

    @Override
    public void updateIsUsingById(int isUsing, String id) {
        mcodeDetailDao.updateIsUsingById(isUsing, id);
    }

}
