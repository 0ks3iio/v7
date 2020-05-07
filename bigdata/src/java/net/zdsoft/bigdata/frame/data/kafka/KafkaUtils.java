package net.zdsoft.bigdata.frame.data.kafka;

import java.util.Properties;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.framework.config.Evn;

import org.apache.kafka.common.security.JaasUtils;

@SuppressWarnings("deprecation")
public class KafkaUtils {

	public void createTopic(String topic, int replicationFactor, int partitions)
			throws Exception {
		OptionDto zookeeperDto = Evn.<OptionService> getBean("optionService")
				.getAllOptionParam("zookeeper");
		if (zookeeperDto == null || zookeeperDto.getStatus() == 0) {
			throw new BigDataBusinessException("zookeeper初始化失败");
		}
		String zookeeperUrl = zookeeperDto.getFrameParamMap().get(
				"zookeeper_url");
		ZkUtils zkUtils = ZkUtils.apply(zookeeperUrl, 30000, 30000,
				JaasUtils.isZkSecurityEnabled());
		// 创建一个单分区单副本名为t1的topic
		AdminUtils.createTopic(zkUtils, topic, partitions, replicationFactor,
				new Properties(), RackAwareMode.Enforced$.MODULE$);
		zkUtils.close();
	}

	public boolean isExistsTopic(String topic) throws Exception {
		OptionDto zookeeperDto = Evn.<OptionService> getBean("optionService")
				.getAllOptionParam("zookeeper");
		if (zookeeperDto == null || zookeeperDto.getStatus() == 0) {
			throw new BigDataBusinessException("zookeeper初始化失败");
		}
		String zookeeperUrl = zookeeperDto.getFrameParamMap().get(
				"zookeeper_url");

		ZkUtils zkUtils = ZkUtils.apply(zookeeperUrl, 30000, 30000,
				JaasUtils.isZkSecurityEnabled());
		boolean exists = AdminUtils.topicExists(zkUtils, topic);
		zkUtils.close();
		return exists;
	}

	public void test() throws Exception {
		ZkUtils zkUtils = ZkUtils.apply("192.168.20.53:2181", 30000, 30000,
				JaasUtils.isZkSecurityEnabled());
		// 创建一个单分区单副本名为t1的topic
		AdminUtils.createTopic(zkUtils, "321", 1, 1, new Properties(),
				RackAwareMode.Enforced$.MODULE$);
		zkUtils.close();
	}

}
