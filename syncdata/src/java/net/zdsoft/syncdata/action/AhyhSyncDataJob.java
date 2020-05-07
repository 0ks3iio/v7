package net.zdsoft.syncdata.action;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.service.AhyhResolveService;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 安徽合肥瑶海区
 * 
 * @author linqz
 *
 */
@DisallowConcurrentExecution
public class AhyhSyncDataJob implements Job {
	private Logger log = Logger.getLogger(AhyhSyncDataJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
		log.info("-----------瑶海区数据同步");
		AhyhResolveService ahyhResolveService = Evn.getBean("ahyhResolveService");
		try {
			List<String> list = FileUtils.readLines(new File("C:\\Users\\linqz\\Desktop\\1.txt"), "utf8");
			for (String s : list) {
				ahyhResolveService.saveData(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// try {
		// ConnectionFactory connectionFactory;
		// Connection connection = null;
		// Session session = null;
		// Destination destination = null;
		// MessageConsumer consumer = null;
		// connectionFactory = new
		// ActiveMQConnectionFactory(Evn.getString("sync_ahyh_mq_url"));
		// try {
		// // 构造从工厂得到连接对象
		// if (connection == null)
		// connection = connectionFactory.createConnection();
		// // 启动
		// connection.start();
		// // 获取操作连接
		// if (session == null)
		// session = connection.createSession(Boolean.FALSE,
		// Session.AUTO_ACKNOWLEDGE);
		// // 获取session注意参数值 foo.bar 是一个服务器的queue，须在在ActiveMq的console配置
		// if (destination == null)
		// destination =
		// session.createQueue(Evn.getString("sync_ahyh_queue_name"));
		// if (consumer == null)
		// consumer = session.createConsumer(destination);
		// while (true) {
		// // 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
		// TextMessage message = (TextMessage) consumer.receive();
		// int i = 0;
		// //因为讯飞的MQ是一条一条记录存放队列的，而且，不分类，所有的都放到一个队列中，所以单个传送
		// if (null != message) {
		// ahyhResolveService.saveData(message.getText());
		// } else {
		// break;
		// }
		// Thread.sleep(10);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (null != connection) {
		// connection.close();
		// connection = null;
		// }
		// if (null != session) {
		// session.close();
		// session = null;
		// }
		// if (null != destination) {
		// destination = null;
		// }
		// if (null != consumer) {
		// consumer.close();
		// consumer = null;
		// }
		// } catch (Exception ignore) {
		// }
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		log.info(">>>>任务结束！");
	}

	public static void main(String[] args) throws ParseException {
	}

}
