package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccTaskEntity;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.framework.delayQueue.DelayItem;

public interface EccTaskService {

	/**
	 *  添加有开始结束时间的task
	 * @param eccTaskStart
	 * @param eccTaskEnd
	 * @param beginTime
	 * @param endTime
	 * @param taskEntity
	 * @param oldUpdateTime  task上次updateTime
	 * @param isEdit
	 */
	public void addEccTaskBandE(EccTask eccTaskStart,EccTask eccTaskEnd,String beginTime,String endTime,EccTaskEntity taskEntity,Date oldUpdateTime, boolean isEdit);
	
	/**
	 * 删除有开始结束时间的task
	 * @param taskEntity
	 */
	public void deleteEccTaskBandE(EccTaskEntity taskEntity,Date oldUpdateTime);
	
	/**
	 * 直接加入队列list
	 * @param itemList
	 */
	public void addEccTaskList(List<DelayItem<?>> itemList);
	
	/**
	 * 直接根据key删除
	 * @param key
	 */
	public void deleteByKey(String key);
}
