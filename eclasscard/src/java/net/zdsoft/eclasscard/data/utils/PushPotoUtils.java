package net.zdsoft.eclasscard.data.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.PushPhotoTask;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.delayQueue.DelayItem;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushPotoUtils {

	private static final Logger log = LoggerFactory.getLogger(PushPotoUtils.class);
	
	private static final Set<String> cardList = new HashSet<String>();
	private static final ReentrantLock lock = new ReentrantLock();
	private static final String pushPhotoPre = "push-photo";
	private static String lastKey = "";

	private static EccTaskService getEccTaskService() {
		return Evn.getBean("eccTaskService");
	}
	
	/**
	 * 有操作，则5分后推送，这5分内有操作直接加入cardList
	 * @param cardNumbers
	 */
	public static void addPushCards(Set<String> cardNumbers,String unitId){
		lock.lock();
        try {
            if(CollectionUtils.isEmpty(cardList)){
            	cardList.addAll(cardNumbers);
        		if(StringUtils.isNotBlank(lastKey)){
        			getEccTaskService().deleteByKey(lastKey);
        		}
        		Calendar calendarEnd = Calendar.getInstance();
        		calendarEnd.add(Calendar.MINUTE, 5);
        		String pushTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
        		PushPhotoTask pTask = new PushPhotoTask(unitId);
        		lastKey = pushPhotoPre+pushTime+new Random().nextInt(1000);
        		DelayItem<PushPhotoTask> pushPhoto = new DelayItem<PushPhotoTask>(lastKey, pushTime, pTask);
        		List<DelayItem<?>> itemList = new ArrayList<DelayItem<?>>();
        		itemList.add(pushPhoto);
        		getEccTaskService().addEccTaskList(itemList);
            }else{
            	cardList.addAll(cardNumbers);
            }
        } catch (ParseException e) {
    		log.error("加入推送照片队列出错");
    		e.printStackTrace();
        } finally {
        	lock.unlock();
        }
	}
	
	/**
	 * 每次推送后移除已推送cardList
	 * @param cardNumbers
	 */
	public static void removePushCards(Set<String> cardNumbers){
		lock.lock();
        try {
        	cardList.removeAll(cardNumbers);
        } finally {
        	lock.unlock();
        }
	}
	
	/**
	 * 由延时task推送5分钟内有操作的班牌相册
	 * @param unitId
	 */
	public static void pushView(String unitId){
		lock.lock();
        try {
			if(CollectionUtils.isNotEmpty(cardList)){
				lastKey = "";
				EccNeedServiceUtils.postPhoto(cardList, unitId);
			}
        } finally {
        	lock.unlock();
        }
	}
	
	
	
}
