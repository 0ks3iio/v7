package net.zdsoft.api.base.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.api.base.dao.ApiInterfaceCountDao;
import net.zdsoft.api.base.entity.eis.ApiInterfaceCount;
import net.zdsoft.api.base.service.ApiInterfaceCountService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author yangsj  2018年1月5日上午10:19:23
 */
@Service("apiInterfaceCountService")
public class ApiInterfaceCountServiceImpl extends BaseServiceImpl<ApiInterfaceCount, String> implements ApiInterfaceCountService {
    
	@Autowired
	private ApiInterfaceCountDao openApiInterfaceCountDao;
	
	@Override
	protected BaseJpaRepositoryDao<ApiInterfaceCount, String> getJpaDao() {
		return openApiInterfaceCountDao;
	}

	@Override
	protected Class<ApiInterfaceCount> getEntityClass() {
		return ApiInterfaceCount.class;
	}

	private List<ApiInterfaceCount> findCount(String ticketKey, String type, String uri, Date start,
			Date end, String resultType, Pagination page) {
		@SuppressWarnings("serial")
		Specification<ApiInterfaceCount> specification = new Specification<ApiInterfaceCount>() {
			@Override
            public Predicate toPredicate(Root<ApiInterfaceCount> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
            	 if(StringUtils.isNotBlank(resultType)) {
            		 ps.add(cb.equal(root.get("resultType").as(String.class), resultType));
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
	            Page<ApiInterfaceCount> findAll = openApiInterfaceCountDao.findAll(specification, pageable);
	            page.setMaxRowCount((int) findAll.getTotalElements());

	            return findAll.getContent();
	        } else {
	            return openApiInterfaceCountDao.findAll(specification);
	        }
	}

	@Override
	public long findCountByType(String ticketKey, String type, Date startTime,
			Date endTime) {
		return openApiInterfaceCountDao.findCountByType(ticketKey,type,startTime,endTime);
	}

	@Override
	public long findCountByInterfaceId(String ticketKey, String interfaceId,
			Date startTime, Date endTime) {
		return openApiInterfaceCountDao.findCountByInterfaceId(ticketKey,interfaceId,startTime,endTime);
	}

	@Override
	public List<ApiInterfaceCount> findByResultType(String resultType) {
		return findCount(StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,null,null,resultType,null);
	}

	@Override
	public List<ApiInterfaceCount> findByResultTypeAndCreationTime(String resultType,
			Date mouthFirst, Date mouthEnd) {
		return openApiInterfaceCountDao.findByResultTypeAndCreationTime(resultType,mouthFirst,mouthEnd);
	}

	@Override
	public List<String> getDistinctTicketKey() {
		return openApiInterfaceCountDao.getDistinctTicketKey();
	}

	@Override
	public List<ApiInterfaceCount> findByTicketKeyAndCreationTime(String ticketKey, Date dateStart, Date dataEnd) {
		return openApiInterfaceCountDao.findByTicketKeyAndCreationTime(ticketKey,dateStart,dataEnd);
	}

	@Override
	public List<ApiInterfaceCount> findByTicketKey(String ticketKey) {
		return openApiInterfaceCountDao.findByTicketKey(ticketKey);
	}
}
