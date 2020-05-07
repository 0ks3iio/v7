package net.zdsoft.remote.openapi.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.remote.openapi.dao.OpenApiInterfaceCountDao;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceCountService;
import net.zdsoft.system.entity.server.Server;

/**
 * @author yangsj  2018年1月5日上午10:19:23
 */
@Service("openApiInterfaceCountService")
public class OpenApiInterfaceCountServiceImpl extends BaseServiceImpl<OpenApiInterfaceCount, String> implements OpenApiInterfaceCountService {
    
	@Autowired
	private OpenApiInterfaceCountDao openApiInterfaceCountDao;
	
	@Override
	protected BaseJpaRepositoryDao<OpenApiInterfaceCount, String> getJpaDao() {
		return openApiInterfaceCountDao;
	}

	@Override
	protected Class<OpenApiInterfaceCount> getEntityClass() {
		return OpenApiInterfaceCount.class;
	}

	@Override
	public List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey) {
		return findDoInterfaceNum(ticketKey, null);
	}

	@Override
	public List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey, String type) {
		return findDoInterfaceNum(ticketKey,type,null);
	}

	@Override
	public List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey, String type, String uri) {
		return findCount(ticketKey,type,uri,null,null, null);
	}
	
	@Override
	public List<OpenApiInterfaceCount> findDoInterfaceNum(String ticketKey, String interfaceType, Date start,
			Date end) {
		return findCount(ticketKey,interfaceType,null,start,end, null);
	}
	
	@Override
	public List<OpenApiInterfaceCount> findDoInterfaceNumAndPage(String ticketKey, String interfaceType, Date start,
			Date end, Pagination page) {
		return findCount(ticketKey,interfaceType,null,start,end, page);
	}

	@Override
	public List<OpenApiInterfaceCount> findByTicketKeyAndTypeIn(String ticketKey, String[] types) {
		return openApiInterfaceCountDao.findByTicketKeyAndTypeIn(ticketKey,types);
	}

	private List<OpenApiInterfaceCount> findCount(String ticketKey, String type, String uri, Date start,
			Date end, Pagination page) {
		Specification<OpenApiInterfaceCount> specification = new Specification<OpenApiInterfaceCount>() {
			@Override
            public Predicate toPredicate(Root<OpenApiInterfaceCount> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
            	 List<Predicate> ps = new ArrayList<Predicate>();
            	 if(StringUtils.isNotBlank(ticketKey)) {
            		 ps.add(cb.equal(root.get("ticketKey").as(String.class), ticketKey));
            	 }
            	 if(StringUtils.isNotBlank(type)) {
            		 ps.add(cb.equal(root.get("type").as(String.class), type));
            	 }
            	 if(StringUtils.isNotBlank(uri)) {
            		 ps.add(cb.equal(root.get("uri").as(String.class), uri));
            	 }
            	 if(start != null) {
            		 ps.add(cb.greaterThanOrEqualTo(root.<Timestamp>get("creationTime"), start));
            	 }
            	 if(end != null) {
            		 ps.add(cb.lessThanOrEqualTo(root.<Timestamp>get("creationTime"), end));
            	 }
            	 cq.where(ps.toArray(new Predicate[0]));
            	 return cq.getRestriction();
            }
		 };
		 if (page != null) {
	            Pageable pageable = Pagination.toPageable(page);
	            Page<OpenApiInterfaceCount> findAll = openApiInterfaceCountDao.findAll(specification, pageable);
	            page.setMaxRowCount((int) findAll.getTotalElements());

	            return findAll.getContent();
	        } else {
	            return openApiInterfaceCountDao.findAll(specification);
	        }
	}

	@Override
	public List<OpenApiInterfaceCount> findByType(String type) {
		return findCount(null,type,null,null,null,null);
	}

	@Override
	public Map<String, Integer> findCountByType(String type) {
		Map<String, Integer> map = new HashMap<>();
		List<Object[]> countInfo;
		if(StringUtils.isNotBlank(type)){
			countInfo = openApiInterfaceCountDao.findCountGroupByTicketKeyByType(type);
		}else{
			countInfo = openApiInterfaceCountDao.findCountGroupByTicketKey();
		}
		if(CollectionUtils.isEmpty(countInfo)) {
			return map;
		}
		for (Object[] objects : countInfo) {
			String ticketKey = (String)objects[0];
			Long count = (Long)objects[1];
			map.put(ticketKey, count.intValue());
		}
		return map;
	}

	@Override
	public Map<String, Integer> getTypeCountMap(String ticketKey, String[] types) {
		Map<String, Integer> map = new HashMap<>();
		List<Object[]> countInfo = openApiInterfaceCountDao.getTypeCountMap(ticketKey, types);
		for (Object[] objects : countInfo) {
			String type = (String)objects[0];
			Long count = (Long)objects[1];
			map.put(type, count.intValue());
		}
		return map;
	}
}
