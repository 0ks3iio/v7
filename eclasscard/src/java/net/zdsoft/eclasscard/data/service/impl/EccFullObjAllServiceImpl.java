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
import net.zdsoft.eclasscard.data.dao.EccFullObjAllDao;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.eclasscard.data.entity.EccFullObj;
import net.zdsoft.eclasscard.data.entity.EccFullObjAll;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.eclasscard.data.service.EccFullObjAllService;
import net.zdsoft.eclasscard.data.service.EccFullObjService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.eclasscard.data.task.FullObjAllTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccFullObjAllService")
public class EccFullObjAllServiceImpl extends BaseServiceImpl<EccFullObjAll, String> implements EccFullObjAllService{

	@Autowired
	private EccFullObjAllDao eccFullObjAllDao;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccFullObjService eccFullObjService;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private EccBulletinService eccBulletinService;
	@Autowired
	private EccAttachFolderService eccAttachFolderService;
	@Override
	protected BaseJpaRepositoryDao<EccFullObjAll, String> getJpaDao() {
		return eccFullObjAllDao;
	}

	@Override
	protected Class<EccFullObjAll> getEntityClass() {
		return EccFullObjAll.class;
	}

	@Override
	public void saveFullObjAll(EccFullObjAll fullObjAll,String[] eccInfoIds,boolean isEdit) {
		List<EccFullObj> eccFullObjs = Lists.newArrayList();
		Date oldUpdateTime = null;
		if(isEdit){
			EccFullObjAll oldEccFullObjAll = findOne(fullObjAll.getId());
			oldUpdateTime = oldEccFullObjAll.getUpdateTime();
			fullObjAll.setCreateTime(oldEccFullObjAll.getCreateTime());
			fullObjAll.setStatus(EccConstants.ECC_SHOW_STATUS_0);
		}
		if(EccConstants.ECC_SENDTYPE_9.equals(fullObjAll.getSendType()) && eccInfoIds != null&&eccInfoIds.length>0){
			eccFullObjs.addAll(getEccFullObjs(fullObjAll,eccInfoIds));
		}
		if(isEdit){
			eccFullObjService.deleteBySourceIds(fullObjAll.getId());
		}
		fullObjAll.setUpdateTime(new Date());
		save(fullObjAll);
		if(CollectionUtils.isNotEmpty(eccFullObjs)){
			eccFullObjService.saveAll(eccFullObjs.toArray(new EccFullObj[eccFullObjs.size()]));
		}
		String beginTime = fullObjAll.getBeginTime();
		String endTime = fullObjAll.getEndTime();
		if(EccUtils.fullScreenTaskSet().contains(fullObjAll.getType()) && DateUtils.date2StringByMinute(new Date()).compareTo(endTime) < 0){
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
			EccTask fullObjAllTaskStart = new FullObjAllTask(fullObjAll.getId(),false);
        	EccTask fullObjAllTaskEnd = new FullObjAllTask(fullObjAll.getId(),true);
        	eccTaskService.addEccTaskBandE(fullObjAllTaskStart, fullObjAllTaskEnd, beginTime, endTime, fullObjAll,oldUpdateTime, isEdit);
		}
	}

	private List<EccFullObj> getEccFullObjs(EccFullObjAll fullObjAll, String[] eccInfoIds) {
		List<EccFullObj> eccFullObjs = Lists.newArrayList();
		if(eccInfoIds != null&&eccInfoIds.length>0){
			for(String infoId:eccInfoIds){
				EccFullObj fullObj = new EccFullObj();
				fullObj.setId(UuidUtils.generateUuid());
				fullObj.setEccInfoId(infoId);
				fullObj.setObjectId(fullObjAll.getObjectId());
				fullObj.setBeginTime(fullObjAll.getBeginTime());
				fullObj.setEndTime(fullObjAll.getEndTime());
				fullObj.setType(fullObjAll.getType());
				fullObj.setLockScreen(fullObjAll.isLockScreen());
				fullObj.setSourceId(fullObjAll.getId());
				fullObj.setSourceType(EccConstants.FULL_SCREEN_SOURCE_TYPE_02);
				fullObj.setStatus(fullObjAll.getStatus());
				fullObj.setCreateTime(new Date());
				fullObj.setUpdateTime(new Date());
				eccFullObjs.add(fullObj);
			}
		}
		return eccFullObjs;
	}

	@Override
	public List<EccFullObjAll> findByObjectId(String... objectId) {
		return eccFullObjAllDao.findByObjectId(objectId);
	}

	@Override
	public void deleteByObjectIds(String... objectId) {
		eccFullObjService.deleteByObjectIds(objectId);
		eccFullObjAllDao.deleteByObjectIds(objectId);
	}

	@Override
	public void updateFullObjAllStatus(List<EccFullObjAll> fullObjAlls) {
		if(CollectionUtils.isNotEmpty(fullObjAlls)){
			Set<String> ids = EntityUtils.getSet(fullObjAlls, EccFullObjAll::getId); 
			Map<String,Integer> allStatusMap = EntityUtils.getMap(fullObjAlls, EccFullObjAll::getId, EccFullObjAll::getStatus);
			List<EccFullObj> oldFullObjs = eccFullObjService.findBySourceIds(ids.toArray(new String[ids.size()]));
			if(CollectionUtils.isNotEmpty(oldFullObjs)){
				for(EccFullObj fullObj:oldFullObjs){
					if(allStatusMap.containsKey(fullObj.getSourceId())){
						fullObj.setStatus(allStatusMap.get(fullObj.getSourceId()));
					}
				}
				eccFullObjService.saveAll(oldFullObjs.toArray(new EccFullObj[oldFullObjs.size()]));
			}
			saveAll(fullObjAlls.toArray(new EccFullObjAll[fullObjAlls.size()]));
		}
	}

	@Override
	public List<EccFullObjAll> findByUnitId(String unitId, Pagination page) {
		List<EccFullObjAll> fullObjAlls = null;
		 Specification<EccFullObjAll> specification = new Specification<EccFullObjAll>() {
	            @Override
	            public Predicate toPredicate(Root<EccFullObjAll> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	                List<Predicate> ps = new ArrayList<>();
	                
                	ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
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
        	Page<EccFullObjAll> findAll = eccFullObjAllDao.findAll(specification, pageable);
        	page.setMaxRowCount((int) findAll.getTotalElements());
        	fullObjAlls = findAll.getContent();
		}else {
			fullObjAlls = eccFullObjAllDao.findAll(specification);
        }
		if(fullObjAlls == null) fullObjAlls = Lists.newArrayList();
		filldata(fullObjAlls);
		return fullObjAlls;
	}

	private void filldata(List<EccFullObjAll> fullObjAlls) {
		//填充页面需要展示的数据
		Map<String,String> objNameMap = Maps.newHashMap();
		Set<String> bulletinIds = fullObjAlls.stream().filter(line -> EccConstants.ECC_FULL_OBJECT_TYPE01.equals(line.getType())).map(EccFullObjAll::getObjectId).collect(Collectors.toSet());
		Set<String> floderIds = fullObjAlls.stream().filter(line -> (EccConstants.ECC_FULL_OBJECT_TYPE03.equals(line.getType()) ||
				EccConstants.ECC_FULL_OBJECT_TYPE04.equals(line.getType()) || EccConstants.ECC_FULL_OBJECT_TYPE05.equals(line.getType()))).map(EccFullObjAll::getObjectId).collect(Collectors.toSet());
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
		for(EccFullObjAll all:fullObjAlls){
			if(objNameMap.containsKey(all.getObjectId())){
				all.setObjectName(objNameMap.get(all.getObjectId()));
			}
			all.setBeginTime(EccUtils.dateStrRMyear(all.getBeginTime()));
			all.setEndTime(EccUtils.dateStrRMyear(all.getEndTime()));
		}
	}

	@Override
	public void fullObjAllTaskRun(String id, boolean isEnd) {
		EccFullObjAll fullObjAll = findOne(id);
		Set<String> infoIds = Sets.newHashSet();
		List<EccFullObj> fullObjs = eccFullObjService.findBySourceIds(id);
		for(EccFullObj fullObj:fullObjs){
			infoIds.add(fullObj.getEccInfoId());
		}
		if(fullObjAll!=null){
			if(CollectionUtils.isNotEmpty(infoIds)){
				EccNeedServiceUtils.postCheckFullScreen(infoIds);
			}
			if(isEnd){
				fullObjAll.setStatus(EccConstants.ECC_SHOW_STATUS_2);
			}else{
				fullObjAll.setStatus(EccConstants.ECC_SHOW_STATUS_1);
			}
			List<EccFullObjAll> fullObjAlls = Lists.newArrayList();
			fullObjAlls.add(fullObjAll);
			updateFullObjAllStatus(fullObjAlls);
		}
	}

	@Override
	public void addFullObjAllQueue() {
		List<EccFullObjAll> eccFullObjAlls = findListNotShow();
		//加入定时队列
        Calendar calendarEnd = Calendar.getInstance();
        for(EccFullObjAll objAll:eccFullObjAlls){
        	String beginTime = objAll.getBeginTime();
        	String endTime = objAll.getEndTime();
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
        	EccTask fullObjAllTaskStart = new FullObjAllTask(objAll.getId(),false);
        	EccTask fullObjAllTaskEnd = new FullObjAllTask(objAll.getId(),true);
        	eccTaskService.addEccTaskBandE(fullObjAllTaskStart, fullObjAllTaskEnd, beginTime, endTime, objAll,null, false);
        }
	}

	/**
	 * 查询需要加入到task中的全屏展示内容
	 * @return
	 */
	private List<EccFullObjAll> findListNotShow() {
		List<EccFullObjAll> fullObjAlls = eccFullObjAllDao.findListNotShow();
		List<EccFullObjAll> updateFullObjAlls= Lists.newArrayList();
		List<EccFullObjAll> returnFullObjAlls= Lists.newArrayList();
		for(EccFullObjAll fullObjAll:fullObjAlls){
			if(DateUtils.date2StringByMinute(new Date()).compareTo(fullObjAll.getEndTime())>=0){
				fullObjAll.setStatus(EccConstants.ECC_SHOW_STATUS_2);
				updateFullObjAlls.add(fullObjAll);
			}else if(EccUtils.fullScreenTaskSet().contains(fullObjAll.getType())){
				returnFullObjAlls.add(fullObjAll);
			}
		}
		if(CollectionUtils.isNotEmpty(updateFullObjAlls)){
			saveAll(updateFullObjAlls.toArray(new EccFullObjAll[updateFullObjAlls.size()]));
		}
		
		return returnFullObjAlls;
	}

	@Override
	public void deleteFullObjAll(String id) {
		EccFullObjAll fullObjAll = findOne(id);
		Set<String> infoIds = Sets.newHashSet();
		if(fullObjAll!=null){
			if(EccConstants.ECC_SHOW_STATUS_2 != fullObjAll.getStatus()){
				eccTaskService.deleteEccTaskBandE(fullObjAll, fullObjAll.getUpdateTime());
			}
			if(EccConstants.ECC_SHOW_STATUS_1 == fullObjAll.getStatus()){
				List<EccFullObj> fullObjs = eccFullObjService.findBySourceIds(id);
				for(EccFullObj fullObj:fullObjs){
					infoIds.add(fullObj.getEccInfoId());
				}
				
			}
		}
		delete(id);
		eccFullObjService.deleteBySourceIds(id);
		if(CollectionUtils.isNotEmpty(infoIds)){
			EccNeedServiceUtils.postCheckFullScreen(infoIds);
		}
	}

	@Override
	public void updateFullObjAllLock(String id,boolean isLock) {
		EccFullObjAll eccFullObjAll = findOneFromDB(id);
		List<EccFullObj> eccFullObjs = eccFullObjService.findBySourceIds(id);
		if(eccFullObjAll!=null){
			eccFullObjAll.setLockScreen(isLock);
			Set<String> sids = Sets.newHashSet();
			for(EccFullObj obj:eccFullObjs){
				obj.setLockScreen(isLock);
				if(EccConstants.ECC_SHOW_STATUS_1 == obj.getStatus())sids.add(obj.getEccInfoId());
			}
			if(CollectionUtils.isNotEmpty(sids)){
				EccNeedServiceUtils.fullScreenLockCheck(sids);
			}
			if(CollectionUtils.isNotEmpty(eccFullObjs)){
				eccFullObjService.saveAll(eccFullObjs.toArray(new EccFullObj[eccFullObjs.size()]));
			}
			save(eccFullObjAll);
		}
	}


}
