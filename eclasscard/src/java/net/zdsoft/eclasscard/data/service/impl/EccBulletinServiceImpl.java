package net.zdsoft.eclasscard.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccBulletinDao;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.eclasscard.data.entity.EccBulletinTo;
import net.zdsoft.eclasscard.data.entity.EccFullObjAll;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.eclasscard.data.service.EccBulletinToService;
import net.zdsoft.eclasscard.data.service.EccFullObjAllService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.BulletinTask;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
@Service("eccBulletinService")
public class EccBulletinServiceImpl extends BaseServiceImpl<EccBulletin, String> implements
		EccBulletinService {

	@Autowired
	private EccBulletinDao eccBulletinDao;
	@Autowired
	private EccBulletinToService eccBulletinToService;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccFullObjAllService eccFullObjAllService;
	
	@Override
	protected BaseJpaRepositoryDao<EccBulletin, String> getJpaDao() {
		return eccBulletinDao;
	}

	@Override
	protected Class<EccBulletin> getEntityClass() {
		return EccBulletin.class;
	}

	@Override
	public List<EccBulletin> findList(final String eccInfoId,final String userId,final String unitId, final String startTime,
			final String endTime, final Integer status, Pagination page) {
		Set<String> bulletinIds = Sets.newHashSet();
		List<EccBulletin> bulletins = Lists.newArrayList();
		 if (StringUtils.isNotBlank(eccInfoId)) {
			 List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("eccInfoId", eccInfoId);
			 bulletinIds  = EntityUtils.getSet(bulletinTos, "bulletinId");
			 if(bulletinIds.size()<1){
				 return bulletins;
			 }
		 }
		 final String[] bIds = bulletinIds.toArray(new String[0]);
		 Specification<EccBulletin> specification = new Specification<EccBulletin>() {

            @Override
            public Predicate toPredicate(Root<EccBulletin> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                
                if (StringUtils.isNotBlank(startTime)&&StringUtils.isBlank(endTime)) {
                	ps.add(cb.lessThanOrEqualTo(root.get("endTime").as(String.class), startTime));
                }
                if (StringUtils.isNotBlank(endTime)&&StringUtils.isBlank(startTime)) {
                	ps.add(cb.greaterThanOrEqualTo(root.get("startTime").as(String.class), endTime));
                }
                if (StringUtils.isNotBlank(endTime)&&StringUtils.isNotBlank(startTime)) {
                	ps.add(cb.between(root.get("startTime").as(String.class), startTime, endTime));
                	ps.add(cb.or(cb.between(root.get("endTime").as(String.class), startTime, endTime)));
                }
                if (status!=null) {
                	ps.add(cb.equal(root.get("status").as(Integer.class), status));
                }
                if (StringUtils.isNotBlank(eccInfoId)) {
                	if(bIds.length>0){
                		 In<String> in = cb.in(root.get("id").as(String.class));
                         for (int i = 0; i < bIds.length; i++) {
                             in.value(bIds[i]);
                         }
                         ps.add(in);
                	}
//                	ps.add(cb.equal(root.get("userId").as(String.class), userId));
                }else{
                	ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                	
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("createTime").as(Date.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
        if (page != null) {
        	Pageable pageable = Pagination.toPageable(page);
        	Page<EccBulletin> findAll = eccBulletinDao.findAll(specification, pageable);
        	page.setMaxRowCount((int) findAll.getTotalElements());
        	return findAll.getContent();
        }
        else {
        	return eccBulletinDao.findAll(specification);
        }
	}
	@Override
	public void saveBulletin(EccBulletin eccBulletin, String[] eccInfoIds,String userId,String unitId) throws ParseException {
		boolean isEdit = false;
		Date oldUpdateTime = null;
		Set<String> infoIds = Sets.newHashSet();
		if(StringUtils.isBlank(eccBulletin.getId())){
			eccBulletin.setId(UuidUtils.generateUuid());
			eccBulletin.setUserId(userId);
			eccBulletin.setUnitId(unitId);
			eccBulletin.setStatus(EccConstants.ECC_SHOW_STATUS_0);
			eccBulletin.setCreateTime(new Date());
			eccBulletin.setUpdateTime(new Date());
		}else{
			isEdit = true;
			EccBulletin bulletinOld = findOne(eccBulletin.getId());
			if(bulletinOld!=null){
				eccBulletin.setCreateTime(bulletinOld.getCreateTime());
				eccBulletin.setStatus(bulletinOld.getStatus());
				eccBulletin.setUnitId(unitId);
				eccBulletin.setUserId(userId);
				eccBulletin.setUpdateTime(new Date());
				if(bulletinOld.getUpdateTime() != null){
					oldUpdateTime = bulletinOld.getUpdateTime();
				}
			}
			eccBulletinToService.deleteByBulletinId(eccBulletin.getId());
		}
		
		List<EccBulletinTo> bulletinTos = Lists.newArrayList();
		if(eccInfoIds != null&&eccInfoIds.length>0){
			for(String eccInfoId:eccInfoIds){
				EccBulletinTo bulletinTo = new EccBulletinTo();
				bulletinTo.setId(UuidUtils.generateUuid());
				bulletinTo.setBulletinId(eccBulletin.getId());
				bulletinTo.setEccInfoId(eccInfoId);
				bulletinTos.add(bulletinTo);
				infoIds.add(eccInfoId);
			}
		}
		if(!EccConstants.ECC_BULLETIN_TEMPLETTYPE_9.equals(eccBulletin.getTempletType())){
			eccBulletin.setGrounding("");
		}
		eccBulletin.setUpdateTime(new Date());
		save(eccBulletin);
		if(bulletinTos.size()>0){
			eccBulletinToService.saveAll(bulletinTos.toArray(new EccBulletinTo[bulletinTos.size()]));
		}
		if(EccConstants.ECC_BULLETIN_TYPE_3.equals(eccBulletin.getType()) && infoIds.size()>0){
			saveFullObjAll(infoIds, eccBulletin);
		}
		//加入定时队列
		String beginTime = eccBulletin.getBeginTime();
		String endTime = eccBulletin.getEndTime();
		if(DateUtils.date2StringByMinute(new Date()).compareTo(endTime) < 0){
			Calendar calendarEnd = Calendar.getInstance();
			if(DateUtils.date2StringByMinute(new Date()).compareTo(beginTime)>=0){
				calendarEnd.add(Calendar.SECOND, 5);//开始时间在当前时间之前，5秒后显示
				beginTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
				calendarEnd.add(Calendar.SECOND, -5);
			}else{
				beginTime+=":00";
			}
			// 初始化日期
			calendarEnd.setTime(DateUtils.string2DateTime(endTime+":00"));
			calendarEnd.add(Calendar.MINUTE, 1);
			endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
			BulletinTask bulletinTaskStart = new BulletinTask(eccBulletin.getId(),false);
			BulletinTask bulletinTaskEnd = new BulletinTask(eccBulletin.getId(),true);
			eccTaskService.addEccTaskBandE(bulletinTaskStart, bulletinTaskEnd, beginTime, endTime, eccBulletin,oldUpdateTime, isEdit);
		}
	}

	@Override
	public List<EccBulletin> findListOnEcc(String eccInfoId) {
		List<EccBulletin> bulletins = Lists.newArrayList();
		List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("eccInfoId", eccInfoId);
		Set<String> bulletinIds = EntityUtils.getSet(bulletinTos, "bulletinId");
		if(bulletinIds.size()>0){
			bulletins =findListPeriod(bulletinIds.toArray(new String[bulletinIds.size()]));
			if(bulletins.size()>0){
				for(EccBulletin bulletin:bulletins){
					bulletin.setStatus(EccConstants.ECC_SHOW_STATUS_1);
				}
				saveAll(bulletins.toArray(new EccBulletin[bulletins.size()]));
			}
		}
		return bulletins;
	}

	@Override
	public List<EccBulletin> findListPeriod(final String[] ids) {
		Specification<EccBulletin> specification = new Specification<EccBulletin>() {
            @Override
            public Predicate toPredicate(Root<EccBulletin> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<>();
                String nowTime = DateUtils.date2StringBySecond(new Date());
            	ps.add(cb.greaterThanOrEqualTo(root.get("endTime").as(String.class),nowTime));
            	ps.add(cb.lessThanOrEqualTo(root.get("beginTime").as(String.class),nowTime));
                if (null != ids&&ids.length > 0) {
                    In<String> in = cb.in(root.get("id").as(String.class));
                    for (int i = 0; i < ids.length; i++) {
                        in.value(ids[i]);
                    }
                    ps.add(in);
                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(cb.desc(root.get("createTime").as(Date.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
		return eccBulletinDao.findAll(specification);
	}

	@Override
	public void upadteStatus(String nowTime) {
//		eccBulletinDao.upadteStatus(nowTime);
		
	}

	@Override
	public void deleteBulletin(String id) {
		EccBulletin bulletin = findOne(id);
		List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("bulletinId", id);
		eccBulletinToService.deleteByBulletinId(id);
		delete(id);
		if(bulletin.getType()==EccConstants.ECC_BULLETIN_TYPE_3){
			eccFullObjAllService.deleteByObjectIds(id);
		}
		if (!EccConstants.ECC_SHOW_STATUS_2.equals(bulletin.getStatus())) {
			eccTaskService.deleteEccTaskBandE(bulletin,bulletin.getUpdateTime());
		}
		if(EccConstants.ECC_SHOW_STATUS_1.equals(bulletin.getStatus())){
			Set<String> sids1 = Sets.newHashSet();
			for(EccBulletinTo bulletinTo:bulletinTos){
				sids1.add(bulletinTo.getEccInfoId());
			}
			if(bulletin!=null){
				if(sids1.size()>0){
					if(bulletin.getType()==EccConstants.ECC_BULLETIN_TYPE_3){
						EccNeedServiceUtils.postCheckFullScreen(sids1);
					}else{
						EccNeedServiceUtils.postBulletin(bulletin.getType(),sids1,bulletin.getUnitId(),bulletin.getId());
					}
				}
			}
		}
	}

	@Override
	public void addBulletinQueue(){
		List<EccBulletin> bulletins = updatefindListNotShow();
		//加入定时队列
        Calendar calendarEnd = Calendar.getInstance();
        for(EccBulletin eccBulletin:bulletins){
        	String beginTime = eccBulletin.getBeginTime();
        	String endTime = eccBulletin.getEndTime();
        	if(DateUtils.date2StringByMinute(new Date()).compareTo(beginTime)>0){
        		calendarEnd.add(Calendar.SECOND, 5);//开始时间在当前时间之前，5秒后显示
        		beginTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
        	}else{
        		beginTime+=":00";
        	}
        	// 初始化日期
        	calendarEnd.setTime(DateUtils.string2DateTime(endTime+":00"));
        	calendarEnd.add(Calendar.MINUTE, 1);
        	endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
        	EccTask bulletinTaskStart = new BulletinTask(eccBulletin.getId(),false);
        	EccTask bulletinTaskEnd = new BulletinTask(eccBulletin.getId(),true);
        	eccTaskService.addEccTaskBandE(bulletinTaskStart, bulletinTaskEnd, beginTime, endTime, eccBulletin,null, false);
        }
		
	}

	private List<EccBulletin> updatefindListNotShow() {
		List<EccBulletin> bulletins = eccBulletinDao.findListNotShow();
		List<EccBulletin> updatebulletins = Lists.newArrayList();
		for(EccBulletin bulletin:bulletins){
			if(DateUtils.date2StringByMinute(new Date()).compareTo(bulletin.getEndTime())>=0){
				bulletin.setStatus(EccConstants.ECC_SHOW_STATUS_2);
				updatebulletins.add(bulletin);
			}
		}
		if(updatebulletins.size()>0){
			saveAll(updatebulletins.toArray(new EccBulletin[updatebulletins.size()]));
		}
		bulletins.removeAll(updatebulletins);
		return bulletins;
	}
	
	@Override
	public void bulletinTaskRun(String bulletinId,boolean isEnd){
		List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("bulletinId", bulletinId);
		Set<String> sids = Sets.newHashSet();
		for(EccBulletinTo bulletinTo:bulletinTos){
			sids.add(bulletinTo.getEccInfoId());
		}
		EccBulletin bulletin = findOne(bulletinId);
		if(bulletin!=null){
			if(sids.size()>0){
				if(bulletin.getType()==EccConstants.ECC_BULLETIN_TYPE_3){
					EccNeedServiceUtils.postCheckFullScreen(sids);
				}else{
					EccNeedServiceUtils.postBulletin(bulletin.getType(),sids,bulletin.getUnitId(),bulletin.getId());
				}
			}
			if(isEnd){
				bulletin.setStatus(EccConstants.ECC_SHOW_STATUS_2);
			}else{
				bulletin.setStatus(EccConstants.ECC_SHOW_STATUS_1);
			}
			save(bulletin);
			if(bulletin.getType()==EccConstants.ECC_BULLETIN_TYPE_3){
				List<EccFullObjAll> oldFullObjAlls = eccFullObjAllService.findByObjectId(bulletinId);
				for(EccFullObjAll fullObj:oldFullObjAlls){
					if(isEnd){
						fullObj.setStatus(EccConstants.ECC_SHOW_STATUS_2);
					}else{
						fullObj.setStatus(EccConstants.ECC_SHOW_STATUS_1);
					}
				}
				if(CollectionUtils.isNotEmpty(oldFullObjAlls)){
					eccFullObjAllService.updateFullObjAllStatus(oldFullObjAlls);
				}
			}
		}
	}

	/**
	 * 标准版-班牌公告list
	 */
	@Override
	public List<EccBulletin> findStandardList(String eccInfoId, String userId,String unitId,String bulletinLevel,Pagination page) {
		Set<String> bulletinIds = Sets.newHashSet();
		List<EccBulletin> bulletins = Lists.newArrayList();
		 if (StringUtils.isNotBlank(eccInfoId)) {
			 List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("eccInfoId", eccInfoId);
			 bulletinIds  = EntityUtils.getSet(bulletinTos, "bulletinId");
			 if(bulletinIds.size()<1){
				 return bulletins;
			 }
		 }
		 final String[] bIds = bulletinIds.toArray(new String[0]);
		 Specification<EccBulletin> specification = new Specification<EccBulletin>() {

            @Override
            public Predicate toPredicate(Root<EccBulletin> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<>();
                
                if (StringUtils.isNotBlank(bulletinLevel)) {
                	ps.add(cb.equal(root.get("bulletinLevel").as(Integer.class), Integer.parseInt(bulletinLevel)));
                }
                if (StringUtils.isNotBlank(eccInfoId)) {
                	if(bIds.length>0){
                		 In<String> in = cb.in(root.get("id").as(String.class));
                         for (int i = 0; i < bIds.length; i++) {
                             in.value(bIds[i]);
                         }
                         ps.add(in);
                	}
                }else{
                	ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(cb.asc(root.get("status").as(Integer.class)));
                orderList.add(cb.desc(root.get("createTime").as(Date.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
        if (page != null) {
        	Pageable pageable = Pagination.toPageable(page);
        	Page<EccBulletin> findAll = eccBulletinDao.findAll(specification, pageable);
        	page.setMaxRowCount((int) findAll.getTotalElements());
        	return findAll.getContent();
        }
        else {
        	return eccBulletinDao.findAll(specification);
        }
	}

	@Override
	public List<EccBulletin> saveOrFindListOnEccAndType(String eccInfoId, Integer type) {
		List<EccBulletin> bulletins = Lists.newArrayList();
		List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("eccInfoId", eccInfoId);
		Set<String> bulletinIds = EntityUtils.getSet(bulletinTos, "bulletinId");
		if(CollectionUtils.isNotEmpty(bulletinIds)){
			bulletins =findBulletinList(bulletinIds.toArray(new String[bulletinIds.size()]),type);
			if(CollectionUtils.isNotEmpty(bulletins)){
				for(EccBulletin bulletin:bulletins){
					bulletin.setStatus(EccConstants.ECC_SHOW_STATUS_1);
				}
				saveAll(bulletins.toArray(new EccBulletin[bulletins.size()]));
			}
		}
		return bulletins;
	}
	
	@Override
	public List<EccBulletin> findBulletinList(final String[] ids,final Integer type) {
		Specification<EccBulletin> specification = new Specification<EccBulletin>() {
            @Override
            public Predicate toPredicate(Root<EccBulletin> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                String nowTime = DateUtils.date2StringBySecond(new Date());
            	ps.add(cb.greaterThanOrEqualTo(root.get("endTime").as(String.class),nowTime));
            	ps.add(cb.lessThanOrEqualTo(root.get("beginTime").as(String.class),nowTime));
            	ps.add(cb.equal(root.get("type").as(Integer.class), type));
            	if (null != ids&&ids.length > 0) {
                    In<String> in = cb.in(root.get("id").as(String.class));
                    for (int i = 0; i < ids.length; i++) {
                        in.value(ids[i]);
                    }
                    ps.add(in);
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("createTime").as(Date.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
		return eccBulletinDao.findAll(specification);
	}
	
	/**
	 * 全屏展示对象存储
	 * @param infoIds
	 * @param id
	 */
	private void saveFullObjAll(Set<String> infoIds, EccBulletin bulletin) {
		eccFullObjAllService.deleteByObjectIds(bulletin.getId());
		EccFullObjAll oldfullObjAll = new EccFullObjAll();
		oldfullObjAll.setCreateTime(new Date());
		oldfullObjAll.setId(UuidUtils.generateUuid());
		oldfullObjAll.setLockScreen(bulletin.isLockScreen());
		oldfullObjAll.setType(EccConstants.ECC_FULL_OBJECT_TYPE01);
		oldfullObjAll.setUnitId(bulletin.getUnitId());
		oldfullObjAll.setBeginTime(bulletin.getBeginTime());
		oldfullObjAll.setEndTime(bulletin.getEndTime());
		oldfullObjAll.setSendType(bulletin.getSendType());
		oldfullObjAll.setStatus(bulletin.getStatus());
		oldfullObjAll.setUserId(bulletin.getUserId());
		oldfullObjAll.setObjectId(bulletin.getId());
		eccFullObjAllService.saveFullObjAll(oldfullObjAll, infoIds.toArray(new String[0]),false);
	}
}
