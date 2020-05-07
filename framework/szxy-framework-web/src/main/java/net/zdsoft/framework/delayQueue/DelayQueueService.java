package net.zdsoft.framework.delayQueue;

import java.util.List;

public interface DelayQueueService {

	// 添加任务放在DelayQueue中
	public void addRecentlyTimeoutItems(List<DelayItem<?>> itemList);

	// 添加删除任务到队列中
	public void addRemoveItem2Queue(String key);

	// 删除待处理的任务
	public void removeTimeoutItem(String key);
}
