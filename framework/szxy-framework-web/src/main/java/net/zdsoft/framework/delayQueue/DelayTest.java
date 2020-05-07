package net.zdsoft.framework.delayQueue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DelayTest {
	@Autowired
	private DelayQueueService delayQueueService;

	public void test() throws ParseException{
			DataDemo dd = new DataDemo(1);// 创建一个任务对象
			DelayItem<?> k = new DelayItem("111", "2017-07-27 19:40:00", dd);
			List<DelayItem<?>> itemList = new ArrayList<DelayItem<?>>();
			itemList.add(k);
			delayQueueService.addRecentlyTimeoutItems(itemList);
	};
	
	
	
	public static void main(String[] args) throws ParseException {
		
	}
}
