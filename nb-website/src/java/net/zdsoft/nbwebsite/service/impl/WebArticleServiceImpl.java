package net.zdsoft.nbwebsite.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.nbwebsite.constant.WebsiteConstants;
import net.zdsoft.nbwebsite.dao.WebArticleDao;
import net.zdsoft.nbwebsite.entity.WebArticle;
import net.zdsoft.nbwebsite.service.WebArticleService;

/**
 * @author yangsj  2017-2-13下午4:11:38
 */
@Service("webArticleService")
public class WebArticleServiceImpl extends BaseServiceImpl<WebArticle, String> implements WebArticleService{
	
	@Autowired
	private WebArticleDao webArticleDao;
	
	@Override
	protected BaseJpaRepositoryDao<WebArticle, String> getJpaDao() {
		// TODO Auto-generated method stub
		return webArticleDao;
	}

	@Override
	protected Class<WebArticle> getEntityClass() {
		// TODO Auto-generated method stub
		return WebArticle.class;
	}

	@Override
	public String findAllRelease(List<String> unitIdsList) {
		// TODO Auto-generated method stub
		return webArticleDao.findAllRelease(unitIdsList);
	}

	@Override
	public String findAdoptReleaseByType(String commitUnitId, String type) {
		// TODO Auto-generated method stub
		return webArticleDao.findAdoptReleaseByType(commitUnitId,type);
	}

	@Override
	public String findClickReleaseByType(String commitUnitId, String type) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findClickReleaseByType(commitUnitId,type);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}
			
			n+=Integer.parseInt(webArticle.getClickNumber());
			
		}
		return String.valueOf(n);
	}

	@Override
	public String findPersonAdoptReleaseByType(String userId, String type) {
		// TODO Auto-generated method stub
		return webArticleDao.findPersonAdoptReleaseByType(userId,type);
	}

	@Override
	public String findPersonReleaseByType(String userId, String type) {
		// TODO Auto-generated method stub
		return webArticleDao.findPersonReleaseByType(userId,type);
	}

	@Override
	public String findPersonClickReleaseByType(String userId, String type) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findPersonClickReleaseByType(userId,type);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}
			
			n+=Integer.parseInt(webArticle.getClickNumber());
			
		}
		return String.valueOf(n);
		
	}

	@Override
	public String findAdoptReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findAdoptReleaseByTypeDate(commitUnitId,type,startDate,endDate);
	}

	@Override
	public String findReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findReleaseByTypeDate(commitUnitId,type,startDate,endDate);
	}

	@Override
	public String findClickReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findClickReleaseByTypeDate(commitUnitId,type,startDate,endDate);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}
			
			n+=Integer.parseInt(webArticle.getClickNumber());
			
		}
		return String.valueOf(n);
		
	//	return webArticleDao.findAdoptReleaseByTypeDate(commitUnitId,type,startDate,endDate);
	}

	@Override
	public String findPersonAdoptReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findPersonAdoptReleaseByTypeDate(userId,type,startDate,endDate);
	}

	@Override
	public String findPersonReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findPersonReleaseByTypeDate(userId,type,startDate,endDate);
	}

	@Override
	public String findPersonClickReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findPersonClickReleaseByTypeDate(userId,type,startDate,endDate);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}			
			n+=Integer.parseInt(webArticle.getClickNumber());			
		}
		return String.valueOf(n);		
	}



	@Override
	public String findPersonReleaseByCommitId(String commitId) {
		// TODO Auto-generated method stub
		return webArticleDao.findPersonReleaseByCommitId(commitId);
	}

	@Override
	public String findAdoptReleaseByUnitId(String commitUnitId) {
		// TODO Auto-generated method stub
		return webArticleDao.findAdoptReleaseByUnitId(commitUnitId);
	}

	@Override
	public String findAllReleaseByUnitId(String commitUnitId) {
		// TODO Auto-generated method stub
		return webArticleDao.findAllReleaseByUnitId(commitUnitId);
	}

	@Override
	public String findClickReleaseByUnitId(String commitUnitId) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findClickReleaseByUnitId(commitUnitId);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}
			
			n+=Integer.parseInt(webArticle.getClickNumber());
			
		}
		return String.valueOf(n);
	}

	@Override
	public String findReleaseByUnitIdDate(String commitUnitId, Date startDate,
			Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findReleaseByUnitIdDate(commitUnitId,startDate,endDate);
	}

	@Override
	public String findAdoptReleaseByUnitIdDate(String commitUnitId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findAdoptReleaseByUnitIdDate(commitUnitId,startDate,endDate);
	}

	@Override
	public String findClickReleaseByUnitIdDate(String commitUnitId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findClickReleaseByUnitIdDate(commitUnitId,startDate,endDate);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}
			
			n+=Integer.parseInt(webArticle.getClickNumber());
			
		}
		return String.valueOf(n);
	}

	@Override
	public String findAllRelease(String commitUnitId) {
		// TODO Auto-generated method stub
		return webArticleDao.findAllRelease(commitUnitId);
	}

	@Override
	public List<WebArticle> findArticles(final String createUserId,final String title, final String type, final String auditUserId,
			final String commitState, final Date startDate, final Date endDate,final boolean isWorkbench, Pagination page) {
		// TODO Auto-generated method stub
		return findAll(new Specification<WebArticle>() {

			@Override
			public Predicate toPredicate(Root<WebArticle> root, CriteriaQuery<?> query,
										 CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				List<Order> orders = Lists.newArrayList();
				if(StringUtils.isNotBlank(createUserId)) {
					ps.add(cb.equal(root.<String>get("createUserId"), createUserId));
				}
				if(StringUtils.isNotBlank(auditUserId)) {
					ps.add(cb.equal(root.<String>get("auditUserId"), auditUserId));
				}
				
				if(StringUtils.isNotBlank(type)) {
					ps.add(cb.equal(root.<String>get("type"), type));
				}
				if(StringUtils.isNotEmpty(title)){
					ps.add(cb.like(root.<String>get("title"), title+"%"));
				}
				if(isNotNull(startDate)){
					ps.add(cb.greaterThanOrEqualTo(root.<Timestamp>get("commitTime"), startDate));
				}
				if(isNotNull(endDate)){
					ps.add(cb.lessThanOrEqualTo(root.<Timestamp>get("commitTime"), DateUtils.addDays(endDate, 1)));
				}
				if(StringUtils.isNotBlank(commitState)) {
					ps.add(cb.equal(root.<String>get("commitState"), commitState));
				}
				//auditUserId
				
				ps.add(cb.equal(root.<String>get("isDeleted"), "0"));
				//排序
				if(StringUtils.equals(commitState, WebsiteConstants.STATE_PASSED)){
					orders.add(cb.asc(root.<Timestamp>get("topTime")));
				}
				if(StringUtils.isEmpty(commitState)){
					orders.add(cb.asc(root.<String>get("commitState")));
				}
				orders.add(cb.desc(root.<Timestamp>get("commitTime")));

				return query.where(ps.toArray(new Predicate[0]))
						.orderBy(orders)
						.getGroupRestriction();
			}
		}, page);
	}
	private boolean isNotNull(Object obj){
		return obj != null;
	}

	@Override
	public String findAllReleaseByUnitIdType(String commitUnitId, String type) {
		// TODO Auto-generated method stub
		return webArticleDao.findAllReleaseByUnitIdType(commitUnitId,type);
	}

	@Override
	public String findAllAdoptRelease(List<String> unitIdsList) {
		// TODO Auto-generated method stub
		
		return webArticleDao.findAllAdoptRelease(unitIdsList);
	}

	@Override
	public String findAllClickRelease(List<String> unitIdsList) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findAllClickRelease(unitIdsList);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}
			
			n+=Integer.parseInt(webArticle.getClickNumber());
			
		}
		return String.valueOf(n);
	}

	@Override
	public String findAllAdoptReleaseByDate(List<String> unitIdsList,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findAllAdoptReleaseByDate(unitIdsList,startDate,endDate);
	}

	@Override
	public String findAllReleaseByDate(List<String> unitIdsList,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleDao.findAllReleaseByDate(unitIdsList,startDate,endDate);
	}

	@Override
	public String findAllClickReleaseByDate(List<String> unitIdsList,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		List<WebArticle> lists=new ArrayList<WebArticle>();
		lists=webArticleDao.findAllClickReleaseByDate(unitIdsList,startDate,endDate);
		int n=0;
		for (WebArticle webArticle : lists) {
			if(StringUtils.isBlank(webArticle.getClickNumber())||webArticle.getClickNumber().equalsIgnoreCase("0")){
				continue;		
			}
			
			n+=Integer.parseInt(webArticle.getClickNumber());
			
		}
		return String.valueOf(n);
		
		
	}
    
	
	
}
