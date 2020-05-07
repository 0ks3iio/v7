/*
* Project: v7
* Author : shenke
* @(#) CommonServiceImpl.java Created on 2016-10-11
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.nbwebsite.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
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
import net.zdsoft.nbwebsite.service.CommonService;

/**
 * @description: 
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-11上午11:05:29
 */
@Service("commonService")
public class CommonServiceImpl extends BaseServiceImpl<WebArticle, String> implements CommonService{

	@Autowired
	private WebArticleDao webArticleDao;
	
	@Override
	protected BaseJpaRepositoryDao<WebArticle, String> getJpaDao() {
		return webArticleDao;
	}

	@Override
	protected Class<WebArticle> getEntityClass() {
		return WebArticle.class;
	}

	public List<WebArticle> getCommonList(final String unitId, final String type,
										  final String title, final Date commitStartDate, final Date commitEndDate,
										  final String commitState , final boolean isManager,
										  final String createUserId ,Pagination page,final boolean isCancel) {

		return getCommonList(unitId,type,title,commitStartDate,commitEndDate,commitState,isManager,createUserId,page,false,isCancel);
	}

	@Override
	public List<WebArticle> getCommonList(final String unitId, final String type,
										  final String title, final Date commitStartDate, final Date commitEndDate,
										  final String commitState , final boolean isManager,
										  final String createUserId ,Pagination page, final boolean isWorkbench, final boolean isCancel) {
		return findAll(new Specification<WebArticle>() {

			@Override
			public Predicate toPredicate(Root<WebArticle> root, CriteriaQuery<?> query,
										 CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				List<Order> orders = Lists.newArrayList();
				if(StringUtils.isNotBlank(type)) {
					ps.add(cb.equal(root.<String>get("type"), type));
				}
				if(StringUtils.isNotEmpty(unitId)) {
					if(!isManager)
						ps.add(cb.equal(root.<String>get("commitUnitId"), unitId));
				}
				if(StringUtils.isNotEmpty(title)){
					ps.add(cb.like(root.<String>get("title"), title+"%"));
				}
				if(isNotNull(commitStartDate)){
					ps.add(cb.greaterThanOrEqualTo(root.<Timestamp>get("commitTime"), commitStartDate));
				}
				if(isNotNull(commitEndDate)){
					ps.add(cb.lessThanOrEqualTo(root.<Timestamp>get("commitTime"), DateUtils.addDays(commitEndDate, 1)));
				}
				if(StringUtils.isNotEmpty(commitState)){
					if(isWorkbench){
						if(WebsiteConstants.STATE_UNCOMMIT.equals(commitState)){
							ps.add(root.<String>get("commitState").in(new String[]{commitState, WebsiteConstants.STATE_RETURN}));
						}else{
							ps.add(cb.equal(root.<String>get("commitState"), commitState));
						}
					}else {
						if (WebsiteConstants.STATE_UNCOMMIT.equals(commitState) && isManager) {
							ps.add(root.<String>get("commitState").in(new String[]{commitState, WebsiteConstants.STATE_RETURN}));
						} else {
							if(WebsiteConstants.STATE_PASSED.equals(commitState)){
								ps.add(root.<String>get("commitState").in(new String[]{commitState,WebsiteConstants.STATE_CANCEL}));
							}else {
								ps.add(cb.equal(root.<String>get("commitState"), commitState));
							}
						}
					}
				}else{
					if(isWorkbench){
						ps.add(root.<String>get("commitState").in(new String[]{WebsiteConstants.STATE_PASSED,WebsiteConstants.STATE_UNPASS,WebsiteConstants.STATE_CANCEL}));
					}
				}

				if(isNotNull(commitEndDate) || isNotNull(commitStartDate)){
					ps.add(cb.notEqual(root.<String>get("commitState"), WebsiteConstants.STATE_UNCOMMIT));
				}

				if(StringUtils.isNotEmpty(createUserId)){
					if(!isManager ||(isManager && WebsiteConstants.STATE_UNCOMMIT.equals(commitState))){
						ps.add(cb.equal(root.<String>get("createUserId"), createUserId));
					}
				}
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
	public List<WebArticle> getArticleByType(final String type, Pagination page,final boolean isCancel) {
		return getArticleByUnitIdAndType(null, type, page,isCancel);
	}

	@Override
	public List<WebArticle> getArticleByUnitIdAndType(final String unitId,
			final String type, Pagination page, final boolean isCancel) {
		return findAll(new Specification<WebArticle>() {

			@Override
			public Predicate toPredicate(Root<WebArticle> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Order> orders = Lists.newArrayList();
				List<Predicate> ps = Lists.newArrayList();
				if(StringUtils.isNotEmpty(unitId)){
					ps.add(cb.equal(root.<String>get("commitUnitId"),unitId));
				}
				ps.add(cb.equal(root.<String>get("isDeleted"), "0"));
				ps.add(cb.equal(root.<String>get("commitState"),WebsiteConstants.STATE_PASSED));
		//		criteriaBuilder.or(criteriaBuilder.equal(root.get(RepairOrder_.localRepairStatus), LocalRepairStatus.repairing),criteriaBuilder.equal(root.get(RepairOrder_.localRepairStatus), LocalRepairStatus.diagnos)))
				ps.add(cb.or(cb.equal(root.<String>get("type"),type), cb.equal(root.<String>get("pushType"),type)));
		//		ps.add(cb.equal(root.<String>get("type"),type));
				orders.add(cb.asc(root.<Timestamp>get("topTime")));
				orders.add(cb.desc(root.<Timestamp>get("commitTime")));
				return query.where(ps.toArray(new Predicate[0]))
						.orderBy(orders)
						.getRestriction();
			}
		}, page);
	}

	@Override
	public List<WebArticle> getArticleByUnitIdsAndType(final String[] unitIds,
													  final String type, Pagination page,final boolean isDeleted) {
		return findAll(new Specification<WebArticle>() {

			@Override
			public Predicate toPredicate(Root<WebArticle> root,
										 CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Order> orders = Lists.newArrayList();
				List<Predicate> ps = Lists.newArrayList();
				if(ArrayUtils.isNotEmpty(unitIds)){
					ps.add(root.<String>get("commitUnitId").in(unitIds));
				}
				ps.add(cb.equal(root.<String>get("isDeleted"), "0"));
				ps.add(cb.equal(root.<String>get("commitState"),WebsiteConstants.STATE_PASSED));
				ps.add(cb.equal(root.<String>get("type"),type));
				orders.add(cb.asc(root.<Timestamp>get("topTime")));
				orders.add(cb.desc(root.<Timestamp>get("commitTime")));
				return query.where(ps.toArray(new Predicate[0]))
						.orderBy(orders)
						.getRestriction();
			}
		}, page);
	}

	@Override
	public List<WebArticle> getArticleOfTopByType(final String type, Pagination page,final boolean isDeleted) {
		return findAll(new Specification<WebArticle>() {

			@Override
			public Predicate toPredicate(Root<WebArticle> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Order> orders = Lists.newArrayList();
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.<String>get("type"),type));
				ps.add(cb.equal(root.<String>get("isTop"), "1"));
					ps.add(cb.equal(root.<String>get("isDeleted"), "0"));
				ps.add(cb.equal(root.<String>get("commitState"),WebsiteConstants.STATE_PASSED));
				orders.add(cb.desc(root.<Timestamp>get("commitTime")));
				orders.add(cb.asc(root.<Timestamp>get("topTime")));
				return query.where(ps.toArray(new Predicate[0]))
						.orderBy(orders)
						.getRestriction();
			}
		}, page);
	}

	@Override
	public Integer addClickNumber(String id) {
		WebArticle webArticle = findOne(id);
		Integer old = Integer.parseInt(StringUtils.isEmpty(webArticle.getClickNumber())?"0":webArticle.getClickNumber());
		webArticle.setClickNumber(Integer.toString(old+1));
		saveAllEntitys(new WebArticle[]{webArticle});
		return old+1;
	}

	@Override
	public List<WebArticle> saveAllEntitys(WebArticle... webArticles) {
		return webArticleDao.save(checkSave(webArticles));
	}
}
