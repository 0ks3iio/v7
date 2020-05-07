package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccFullObjDao;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.eclasscard.data.entity.EccFullObj;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.eclasscard.data.service.EccFullObjService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.eclasscard.data.task.FullObjTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service("eccFullObjService")
public class EccFullObjServiceImpl extends BaseServiceImpl<EccFullObj, String> implements EccFullObjService{

	@Autowired
	private EccFullObjDao eccFullObjDao;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccBulletinService eccBulletinService;
	@Autowired
	private EccAttachFolderService eccAttachFolderService;
	
	@Override
	protected BaseJpaRepositoryDao<EccFullObj, String> getJpaDao() {
		return eccFullObjDao;
	}

	@Override
	protected Class<EccFullObj> getEntityClass() {
		return EccFullObj.class;
	}

	@Override
	public List<EccFullObj> findByBetweenTime(String eccInfoId) {
		String nowTime = DateUtils.date2StringByMinute(new Date());
		return eccFullObjDao.findByBetweenTime(eccInfoId, nowTime);
	}

	@Override
	public List<EccFullObj> findByObjectId(String... objectId) {
		return eccFullObjDao.findByObjectId(objectId);
	}

	@Override
	public void deleteByObjectIds(String... objectId) {
		eccFullObjDao.deleteByObjectIds(objectId);
	}

	@Override
	public void deleteBySourceIds(String... sourceId) {
		eccFullObjDao.deleteBySourceIds(sourceId);
	}

	@Override
	public List<EccFullObj> findBySourceIds(String... sourceIds) {
		return eccFullObjDao.findBySourceIds(sourceIds);
	}

	@Override
	public List<EccFullObj> findByEccInfoId(String eccInfoId, Pagination page) {
		List<EccFullObj> fullObjs = null;
		 Specification<EccFullObj> specification = new Specification<EccFullObj>() {
	            @Override
	            public Predicate toPredicate(Root<EccFullObj> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	                List<Predicate> ps = new ArrayList<>();
	                
	               	ps.add(cb.equal(root.get("eccInfoId").as(String.class), eccInfoId));
	               	ps.add(cb.notEqual(root.get("type").as(String.class), EccConstants.ECC_FULL_OBJECT_TYPE02));//门贴不在列表展示
	               	ps.add(cb.notEqual(root.get("status").as(Integer.class), EccConstants.ECC_SHOW_STATUS_2));
	                List<Order> orderList = new ArrayList<>();
	                orderList.add(cb.desc(cb.selectCase()
	                        .when(cb.equal(root.get("status"),"1"),1)
	                        .otherwise(0)));
	                orderList.add(cb.desc(root.get("createTime").as(Date.class)));

	                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	                return cq.getRestriction();
	            }
			};
		if (page != null) {
	       	Pageable pageable = Pagination.toPageable(page);
	       	Page<EccFullObj> findAll = eccFullObjDao.findAll(specification, pageable);
	       	page.setMaxRowCount((int) findAll.getTotalElements());
	       	fullObjs = findAll.getContent();
		}else {
			fullObjs = eccFullObjDao.findAll(specification);
        }
		if(fullObjs == null) fullObjs = Lists.newArrayList();
		filldata(fullObjs);
		return fullObjs;
	}

	private void filldata(List<EccFullObj> fullObjs) {
		//填充页面需要展示的数据
		Map<String,String> objNameMap = Maps.newHashMap();
		Set<String> bulletinIds = fullObjs.stream().filter(line -> EccConstants.ECC_FULL_OBJECT_TYPE01.equals(line.getType())).map(EccFullObj::getObjectId).collect(Collectors.toSet());
		Set<String> floderIds = fullObjs.stream().filter(line -> (EccConstants.ECC_FULL_OBJECT_TYPE03.equals(line.getType()) ||
				EccConstants.ECC_FULL_OBJECT_TYPE04.equals(line.getType()) || EccConstants.ECC_FULL_OBJECT_TYPE05.equals(line.getType()))).map(EccFullObj::getObjectId).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(bulletinIds)){
			List<EccBulletin> bulletins = eccBulletinService.findListByIds(bulletinIds.toArray(new String[bulletinIds.size()]));
			for(EccBulletin bulletin:bulletins){
				if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_3.equals(bulletin.getTempletType())){
					objNameMap.put(bulletin.getId(), "欢迎致词");
				}else if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_2.equals(bulletin.getTempletType())){
					objNameMap.put(bulletin.getId(), "喜报");
				}else if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_1.equals(bulletin.getTempletType())){
					objNameMap.put(bulletin.getId(), "标准公告");
				}else{
					objNameMap.put(bulletin.getId(), "自定义公告");
				}
			}
		}
		if(CollectionUtils.isNotEmpty(floderIds)){
			List<EccAttachFolder> eccAttachFolders = eccAttachFolderService.findListByIds(floderIds.toArray(new String[floderIds.size()]));
			for(EccAttachFolder folder:eccAttachFolders){
				objNameMap.put(folder.getId(), folder.getTitle());
			}
		}
		for(EccFullObj obj:fullObjs){
			if(objNameMap.containsKey(obj.getObjectId())){
				obj.setObjectName(objNameMap.get(obj.getObjectId()));
			}
			obj.setBeginTime(EccUtils.dateStrRMyear(obj.getBeginTime()));
			obj.setEndTime(EccUtils.dateStrRMyear(obj.getEndTime()));
		}
	}

	@Override
	public void addFullObjQueue() {
		List<EccFullObj> eccFullObjs = findListNotShow();
		//加入定时队列
        Calendar calendarEnd = Calendar.getInstance();
        for(EccFullObj fullObj:eccFullObjs){
        	String beginTime = fullObj.getBeginTime();
        	String endTime = fullObj.getEndTime();
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
        	EccTask fullObjTaskStart = new FullObjTask(fullObj.getId(),false);
        	EccTask fullObjTaskEnd = new FullObjTask(fullObj.getId(),true);
        	eccTaskService.addEccTaskBandE(fullObjTaskStart, fullObjTaskEnd, beginTime, endTime, fullObj,null, false);
        }
	}
	
	/**
	 * 查询需要加入到task中的全屏展示内容
	 * @return
	 */
	private List<EccFullObj> findListNotShow() {
		List<EccFullObj> fullObjs = eccFullObjDao.findListNotShow();
		List<EccFullObj> updateFullObjs= Lists.newArrayList();
		List<EccFullObj> returnFullObjs= Lists.newArrayList();
		for(EccFullObj fullObj:fullObjs){
			if(DateUtils.date2StringByMinute(new Date()).compareTo(fullObj.getEndTime())>=0){
				fullObj.setStatus(EccConstants.ECC_SHOW_STATUS_2);
				updateFullObjs.add(fullObj);
			}else if(EccUtils.fullScreenTaskSet().contains(fullObj.getType()) && EccConstants.FULL_SCREEN_SOURCE_TYPE_01.equals(fullObj.getSourceType())){
				returnFullObjs.add(fullObj);
			}
		}
		if(CollectionUtils.isNotEmpty(updateFullObjs)){
			saveAll(updateFullObjs.toArray(new EccFullObj[updateFullObjs.size()]));
		}
		return returnFullObjs;
	}

	@Override
	public void saveWithTask(EccFullObj eccFullObj, boolean isEdit) {
		Date oldUpdateTime = null;
		if(isEdit){
			EccFullObj oldFullObj = findOne(eccFullObj.getId());
			oldUpdateTime = oldFullObj.getUpdateTime();
			eccFullObj.setCreateTime(oldFullObj.getCreateTime());
			eccFullObj.setSourceId(oldFullObj.getSourceId());
			eccFullObj.setSourceType(oldFullObj.getSourceType());
			eccFullObj.setStatus(EccConstants.ECC_SHOW_STATUS_0);
			eccFullObj.setUpdateTime(new Date());
		}
		save(eccFullObj);
		String beginTime = eccFullObj.getBeginTime();
		String endTime = eccFullObj.getEndTime();
		if(EccUtils.fullScreenTaskSet().contains(eccFullObj.getType()) && DateUtils.date2StringByMinute(new Date()).compareTo(endTime) < 0){
			//加入定时队列
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
			EccTask fullObjTaskStart = new FullObjTask(eccFullObj.getId(),false);
        	EccTask fullObjTaskEnd = new FullObjTask(eccFullObj.getId(),true);
        	eccTaskService.addEccTaskBandE(fullObjTaskStart, fullObjTaskEnd, beginTime, endTime, eccFullObj,oldUpdateTime, isEdit);
		}
	}

	@Override
	public void fullObjTaskRun(String id, boolean isEnd) {
		EccFullObj fullObj = findOne(id);
		Set<String> sids1 = Sets.newHashSet();
		if(fullObj!=null){
			String infoId = fullObj.getEccInfoId();
			if(StringUtils.isNotBlank(infoId)){
					sids1.add(infoId);
			}
			if(CollectionUtils.isNotEmpty(sids1)){
				EccNeedServiceUtils.postCheckFullScreen(sids1);
			}
			if(isEnd){
				fullObj.setStatus(EccConstants.ECC_SHOW_STATUS_2);
			}else{
				fullObj.setStatus(EccConstants.ECC_SHOW_STATUS_1);
			}
			save(fullObj);
		}
	}

	@Override
	public void deleteFullObj(String id) {
		EccFullObj fullObj = findOne(id);
		Set<String> sids1 = Sets.newHashSet();
		delete(id);
		if(fullObj!=null){
			if(EccConstants.ECC_SHOW_STATUS_2 != fullObj.getStatus()){
				eccTaskService.deleteEccTaskBandE(fullObj, fullObj.getUpdateTime());
			}
			if(EccConstants.ECC_SHOW_STATUS_1 == fullObj.getStatus()){
				String infoId = fullObj.getEccInfoId();
				if(StringUtils.isNotBlank(infoId)){
						sids1.add(infoId);
				}
				if(CollectionUtils.isNotEmpty(sids1)){
					EccNeedServiceUtils.postCheckFullScreen(sids1);
				}
			}
		}
	}

	@Override
	public void updateFullObjAllLock(String id, boolean isLock) {
		EccFullObj eccFullObj = findOneFromDB(id);
		if(eccFullObj!=null){
			eccFullObj.setLockScreen(isLock);
			Set<String> sids = Sets.newHashSet();
			if(EccConstants.ECC_SHOW_STATUS_1 == eccFullObj.getStatus())sids.add(eccFullObj.getEccInfoId());
			if(CollectionUtils.isNotEmpty(sids)){
				EccNeedServiceUtils.fullScreenLockCheck(sids);
			}
			save(eccFullObj);
		}
		
	}


}
