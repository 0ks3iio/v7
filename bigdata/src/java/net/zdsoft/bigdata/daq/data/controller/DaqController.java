package net.zdsoft.bigdata.daq.data.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.bigdata.daq.data.job.DaqOperationLogJob;
import net.zdsoft.bigdata.frame.data.kafka.KafkaProducerAdapter;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/openapi/dac")
public class DaqController extends BaseAction {

	@ResponseBody
	@RequestMapping(value = "/init/data")
	@ControllerInfo(value = "初始化数据")
	public String dacDebug(HttpServletRequest request) throws IOException {
		try {
			DaqOperationLogJob job = new DaqOperationLogJob();
			job.initLog();
			return "初始化数据成功!";
		} catch (Exception e) {
			e.printStackTrace();
			return "初始化数据失败!" + e.getMessage();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/kafka/test")
	@ControllerInfo(value = "kafka测试")
	public String kafkaTest(HttpServletRequest request) throws IOException {
		Json result = new Json();
		result.put("student_id", "111111");
		result.put("student_name", "测试学生");
		DateTimeFormatter dtFormatter = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss");
		String timestamp = dtFormatter.parseDateTime(
				DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"))
				.toString();
		result.put("timestamp", timestamp);
		try {
			KafkaProducerAdapter producer = KafkaProducerAdapter.getInstance();
			producer.send("test", result.toString());
			System.out.println("==============="+result.toString());
			return "kafka测试成功!";
		} catch (Exception e) {
			e.printStackTrace();
			return "kafka测试失败!" + e.getMessage();
		}
	}

}
