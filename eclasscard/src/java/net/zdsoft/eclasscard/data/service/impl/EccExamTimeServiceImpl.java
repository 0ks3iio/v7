package net.zdsoft.eclasscard.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccExamTimeDao;
import net.zdsoft.eclasscard.data.entity.EccExamTime;
import net.zdsoft.eclasscard.data.entity.EccFullObj;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.eclasscard.data.service.EccExamTimeService;
import net.zdsoft.eclasscard.data.service.EccFullObjService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccOtherSetService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.eclasscard.data.task.ExamTimeTask;
import net.zdsoft.eclasscard.data.task.PushPhotoTask;
import net.zdsoft.eclasscard.data.task.UnitDoorTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.exammanage.remote.service.ExamManageRemoteService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.delayQueue.DelayItem;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccExamTimeService")
public class EccExamTimeServiceImpl extends BaseServiceImpl<EccExamTime,String> implements EccExamTimeService{
	
	@Autowired
	private EccExamTimeDao eccExamTimeDao;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private ExamManageRemoteService examManageRemoteService;
	@Autowired
	private EccFullObjService eccFullObjService;
	@Autowired
	private EccOtherSetService eccOtherSetService;
	
	@Override
	protected BaseJpaRepositoryDao<EccExamTime, String> getJpaDao() {
		return eccExamTimeDao;
	}

	@Override
	protected Class<EccExamTime> getEntityClass() {
		return EccExamTime.class;
	}

	@Override
	public String saveOrUpdate(JSONArray examTimeArray) {
		String result = "1";
		List<EccExamTime> eccExamTimes = Lists.newArrayList();
		for(int i=0;i<examTimeArray.size();i++){
			EccExamTime examTime = new EccExamTime(); 
			JSONObject json=examTimeArray.getJSONObject(i);
			String unitIds = json.getString("unitId");
			if(StringUtils.isNotBlank(unitIds)){
				String [] uIds = unitIds.split(",");
				for(String unitId:uIds){
					examTime.setId(UuidUtils.generateUuid());
					examTime.setExamId(json.getString("examId"));
					examTime.setSubjectId(json.getString("subjectId"));
					examTime.setSubType(json.getString("subType"));
					examTime.setBeginTime(json.getString("beginTime"));
					examTime.setEndTime(json.getString("endTime"));
					examTime.setUnitId(unitId);
					examTime.setCreateTime(new Date());
					examTime.setUpdateTime(new Date());
					examTime.setStatus(EccConstants.ECC_SHOW_STATUS_0);
					eccExamTimes.add(examTime);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(eccExamTimes)){
			Set<String> examIds = EntityUtils.getSet(eccExamTimes, "examId");
			deleteByExamIds(examIds.toArray(new String[examIds.size()]));
			saveAll(eccExamTimes.toArray(new EccExamTime[eccExamTimes.size()]));
			//向延迟队列中中加数据
			addTimeToQueue(eccExamTimes);
		}
		return result;
	}

	private void addTimeToQueue(List<EccExamTime> eccExamTimes) {
		for(EccExamTime examTime:eccExamTimes){
			String beginTime = examTime.getBeginTime();
			String endTime = examTime.getEndTime();
			endTime+=":00";
			Date bDate = DateUtils.string2Date(beginTime, "yyyy-MM-dd HH:mm");
			beginTime = DateUtils.date2StringBySecond(DateUtils.addMinute(bDate, -30));
			if(DateUtils.date2StringBySecond(DateUtils.addMinute(new Date(),1)).compareTo(endTime)>=0){//结束时间小于当前时间的，不放入task
				continue;
			}
			if(DateUtils.date2StringBySecond(new Date()).compareTo(beginTime)>=0){//当前时间在考试时间内，1分钟后执行task
				beginTime = DateUtils.date2StringBySecond(DateUtils.addMinute(new Date(),1));
			}
			EccTask examTimeStart = null;
			if(EccConstants.ECC_SHOW_STATUS_0.equals(examTime.getStatus())){
				examTimeStart = new ExamTimeTask(examTime.getId(), false);
			}
			EccTask examTimeEnd = new ExamTimeTask(examTime.getId(), true);
			eccTaskService.addEccTaskBandE(examTimeStart, examTimeEnd, beginTime, endTime, examTime, null, false);
		}
	}

	@Override
	public void deleteByExamIds(String[] examIds) {
		Set<String> fullObjIds = Sets.newHashSet();
		List<EccExamTime> eccExamTimes = findByExamIds(examIds);
		//删除延迟队列中已经有的数据
		for(EccExamTime examTime:eccExamTimes){
			if(DateUtils.date2StringByMinute(new Date()).compareTo(examTime.getEndTime())<=0){
				eccTaskService.deleteEccTaskBandE(examTime, examTime.getUpdateTime());
			}
			if(EccConstants.ECC_SHOW_STATUS_1 == examTime.getStatus()){
				fullObjIds.add(examTime.getId());
			}
		}
		Set<String> examTimeIds = EntityUtils.getSet(eccExamTimes, "id");
		if(CollectionUtils.isNotEmpty(eccExamTimes)){
			deleteAll(eccExamTimes.toArray(new EccExamTime[eccExamTimes.size()]));
		}
		if(CollectionUtils.isNotEmpty(fullObjIds)){
			List<EccFullObj> oldFullObjs = eccFullObjService.findByObjectId(fullObjIds.toArray(new String[fullObjIds.size()]));
			//关闭全屏展示
			Set<String> infoIds = EntityUtils.getSet(oldFullObjs, "eccInfoId");
			if(CollectionUtils.isNotEmpty(infoIds)){
//				List<EccInfo> eccInfos = eccInfoService.findListByIdIn(infoIds.toArray(new String[infoIds.size()]));
//				Set<String> sids = EntityUtils.getSet(eccInfos, "name");
				EccNeedServiceUtils.postCheckFullScreen(infoIds);
			}
		}
		if(CollectionUtils.isNotEmpty(eccExamTimes)){
			eccFullObjService.deleteByObjectIds(examTimeIds.toArray(new String[eccExamTimes.size()]));
		}
	}

	@Override
	public List<EccExamTime> findByExamIds(String[] examIds) {
		return eccExamTimeDao.findByExamIds(examIds);
	}

	@Override
	public void examTimeTaskRun(String id,boolean isEnd) {
		Set<String> sids = Sets.newHashSet();
		EccExamTime examTime = findOne(id);
		if(isEnd){
			//关闭全屏展示
			List<EccFullObj> oldFullObjs = eccFullObjService.findByObjectId(id);
			for(EccFullObj fullObj:oldFullObjs){
				fullObj.setStatus(EccConstants.ECC_SHOW_STATUS_2);
			}
			sids = EntityUtils.getSet(oldFullObjs, EccFullObj::getEccInfoId);
			if(CollectionUtils.isNotEmpty(sids)){
				EccNeedServiceUtils.postCheckFullScreen(sids);
				eccFullObjService.saveAll(oldFullObjs.toArray(new EccFullObj[oldFullObjs.size()]));
			}
			examTime.setStatus(EccConstants.ECC_SHOW_STATUS_2);
		}else{
			// 获取场地Set
			if(StringUtils.isNotBlank(examTime.getExamId())||StringUtils.isNotBlank(examTime.getSubjectId())){
				Set<String> examPlaceIds = examManageRemoteService.getExamSubjectPlaceIds(examTime.getExamId(), examTime.getSubjectId(),examTime.getSubType(),examTime.getUnitId());
				Date bDate = DateUtils.string2Date(examTime.getBeginTime(), "yyyy-MM-dd HH:mm");
				//TODO devtest	
//				examPlaceIds.add("23992CB8C3F94B78B623A58052363A1C");
				
				//placeId为包含在examPlaceIds里的班牌，推送展示
				handlePostInfo(examPlaceIds,id,examTime.getUnitId(),bDate);
				
			}
		}
	}
	
	/**
	 * 全屏展示对象存储
	 * @param infoIds
	 * @param id
	 */
	private void saveFullObj(Set<String> infoIds, EccExamTime examTime) {
		List<EccFullObj> eccFullObjs = Lists.newArrayList();
		String beginTime = DateUtils.date2StringByMinute(new Date());
		for(String infoId:infoIds){
			EccFullObj fullObj = new EccFullObj();
			fullObj.setId(UuidUtils.generateUuid());
			fullObj.setEccInfoId(infoId);
			fullObj.setObjectId(examTime.getId());
			fullObj.setBeginTime(beginTime);
			fullObj.setEndTime(examTime.getEndTime());
			fullObj.setType(EccConstants.ECC_FULL_OBJECT_TYPE02);
			fullObj.setCreateTime(new Date());
			fullObj.setLockScreen(true);
			fullObj.setSourceId(examTime.getId());
			fullObj.setSourceType(EccConstants.FULL_SCREEN_SOURCE_TYPE_02);
			fullObj.setStatus(EccConstants.ECC_SHOW_STATUS_1);
			fullObj.setUpdateTime(new Date());
			eccFullObjs.add(fullObj);
		}
		List<EccFullObj> oldFullObjs = eccFullObjService.findByObjectId(examTime.getId());
		
		if(CollectionUtils.isEmpty(oldFullObjs) && CollectionUtils.isNotEmpty(eccFullObjs)){
			eccFullObjService.saveAll(eccFullObjs.toArray(new EccFullObj[eccFullObjs.size()]));
		}
	}

	/**
	 * 获取要推送考场门贴的班牌
	 * @param examPlaceIds
	 * @param sids
	 */
	private void handlePostInfo(Set<String> examPlaceIds,String id,String unitId,Date bDate) {
		String[] types = {EccConstants.ECC_MCODE_BPYT_1,EccConstants.ECC_MCODE_BPYT_2,EccConstants.ECC_MCODE_BPYT_6};
		List<EccInfo> eccInfos = eccInfoService.findListByUnitAndType(unitId,types);
		fillPlaceId(eccInfos);//行政班场地要填充
		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(unitId, EccConstants.ECC_OTHER_SET_3);
		Set<String> infoIds = Sets.newHashSet();
		for(EccInfo eccInfo:eccInfos){//获取需要展示门贴的班牌
			if(examPlaceIds.contains(eccInfo.getPlaceId())){
				infoIds.add(eccInfo.getId());
			}
		}
		if(otherSet==null || !(Objects.equals(1, otherSet.getNowvalue())) || CollectionUtils.isEmpty(infoIds)){
			return;
		}
		int delay = 10;
		if(StringUtils.isNotEmpty(otherSet.getParam())){
			try {
				int tqTime = Integer.valueOf(otherSet.getParam());
				if(new Date().compareTo(DateUtils.addMinute(bDate, -tqTime))<0){//当前时间>（考试开始时间-提前时间）则延迟，否则立即执行
					delay = 30 - tqTime;
				}else{
					delay = 0;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(delay==0){
			examTimeUnitTaskRun(id, infoIds);
		}else{
			Date time = DateUtils.addMinute(new Date(), delay);
			String pushTime = DateUtils.date2StringBySecond(time);
			UnitDoorTask unitDoorTask = new UnitDoorTask(id, infoIds);
			List<DelayItem<?>> itemList = new ArrayList<DelayItem<?>>();
			DelayItem<UnitDoorTask> unitDoor;
			try {
				unitDoor = new DelayItem<UnitDoorTask>(id+unitId, pushTime, unitDoorTask);
				itemList.add(unitDoor);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			eccTaskService.addEccTaskList(itemList);
		}
	}
	
	/**
	 * 填充行政班placeId
	 * @param eccInfos
	 */
	private void fillPlaceId(List<EccInfo> eccInfos) {
		Set<String> classIdInfoSet = EntityUtils.getSet(eccInfos, "classId");
        if(CollectionUtils.isNotEmpty(classIdInfoSet)){
        	List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classIdInfoSet.toArray(new String[classIdInfoSet.size()])),new TR<List<Clazz>>() {});
        	Map<String,String> classNameMap = EntityUtils.getMap(clazzs, "id","teachPlaceId");
        	for(EccInfo info:eccInfos){
        		if(StringUtils.isBlank(info.getClassId())){
        			continue;
        		}
        		if(classNameMap.containsKey(info.getClassId())){
    				info.setPlaceId(classNameMap.get(info.getClassId()));
        		}
        	}
        }
	}

	@Override
	public void addExamTimeQueue() {
		List<EccExamTime> eccExamTimes = findByltEndTime();
		addTimeToQueue(eccExamTimes);
	}

	@Override
	public List<EccExamTime> findByltEndTime() {
		List<EccExamTime> examTimes = eccExamTimeDao.findListNotShow();
		List<EccExamTime> updateExamTimes= Lists.newArrayList();
		List<EccExamTime> returnExamTimes= Lists.newArrayList();
		for(EccExamTime examTime:examTimes){
			if(DateUtils.date2StringByMinute(new Date()).compareTo(examTime.getEndTime())>=0){
				examTime.setStatus(EccConstants.ECC_SHOW_STATUS_2);
				updateExamTimes.add(examTime);
			}else{
				returnExamTimes.add(examTime);
			}
		}
		if(CollectionUtils.isNotEmpty(updateExamTimes)){
			saveAll(updateExamTimes.toArray(new EccExamTime[updateExamTimes.size()]));
		}
		return returnExamTimes;
	}

	@Override
	public String deleteBySubjectInfoIds(String[] subjectInfoId) {
		if(subjectInfoId != null && subjectInfoId.length>0){
			eccExamTimeDao.deleteBySubjectInfoIds(subjectInfoId);
		}
		return "1";
	}

	@Override
	public void examTimeUnitTaskRun(String id,Set<String> infoIds) {
		//展示门贴页面
		EccExamTime examTime = findOne(id);
		examTime.setStatus(EccConstants.ECC_SHOW_STATUS_1);
		saveFullObj(infoIds,examTime);
		EccNeedServiceUtils.postExamDoorSticker(infoIds,id);
		//门贴在展示的时候再发布到单个班牌全屏展示列表
	}



}
