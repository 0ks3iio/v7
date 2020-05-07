package net.zdsoft.eclasscard.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccTaskEntity;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.framework.delayQueue.DelayItem;
import net.zdsoft.framework.delayQueue.DelayQueueService;
import net.zdsoft.framework.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("eccTaskService")
public class EccTaskServiceImpl implements EccTaskService{

	@Autowired
	private DelayQueueService delayQueueService;
	
	@Override
	public void addEccTaskBandE(EccTask eccTaskStart, EccTask eccTaskEnd,
			String beginTime, String endTime, EccTaskEntity taskEntity,Date oldUpdateTime,boolean isEdit) {
		List<DelayItem<?>> itemList = new ArrayList<>();
		String updateTime = "";//编辑时，从延时队列删除task，走发布--订阅模式，是异步的；在key中加入updateTime；防止先加入队列，后从队列删除情况
		if(taskEntity.getUpdateTime() != null){
			updateTime = DateUtils.date2StringBySecond(taskEntity.getUpdateTime());
		}
		try {
			if(eccTaskStart!=null){
				DelayItem<EccTask> btStart = new DelayItem<>(taskEntity.getId()+"start"+updateTime,beginTime, eccTaskStart);
				itemList.add(btStart);
			}
			if(eccTaskEnd!=null){
				DelayItem<EccTask> btEnd = new DelayItem<>(taskEntity.getId()+"end"+updateTime, endTime, eccTaskEnd);
				itemList.add(btEnd);
			}
			delayQueueService.addRecentlyTimeoutItems(itemList);
			if(isEdit){
				deleteEccTaskBandE(taskEntity,oldUpdateTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteEccTaskBandE(EccTaskEntity taskEntity,Date oldUpdateTime) {
		String updateTime = "";
		if(oldUpdateTime != null){
			updateTime = DateUtils.date2StringBySecond(oldUpdateTime);
		}
		delayQueueService.addRemoveItem2Queue(taskEntity.getId()+"start"+updateTime);
		delayQueueService.addRemoveItem2Queue(taskEntity.getId()+"end"+updateTime);
	}

	@Override
	public void addEccTaskList(List<DelayItem<?>> itemList) {
		delayQueueService.addRecentlyTimeoutItems(itemList);
	}

	@Override
	public void deleteByKey(String key) {
		delayQueueService.addRemoveItem2Queue(key);
	}


}
