package net.zdsoft.syncdata.custom.xingyun.action;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.constant.custom.XyConstant;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.syncdata.custom.xingyun.service.XingYunService;
import net.zdsoft.syncdata.custom.yingshuo.service.impl.YingShuoServiceImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * 开通机构的应用
 * @author yangsj
 *
 */
@Controller
@RequestMapping("/syncdata/xy")
@Lazy
public class UnitServerAction extends BaseAction{
	
	@Autowired
	private XingYunService xingYunService;
	
	@Autowired
    private KafkaTemplate kafkaTemplate;

	@ResponseBody
	@RequestMapping(value = "/sendValue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void testDemo(Integer partition, @RequestBody String data ) throws InterruptedException {
		JSONObject json = Json.parseObject(data);
		String dataMsg = json.getString(BaseSaveConstant.RESOLVE_DATA_NAME);
        kafkaTemplate.send(XyConstant.TOPIC_VALUE, partition, null, dataMsg);
        Thread.sleep(5000);
    }
	
	
	@RequestMapping("/syncEdu")
	public String syncEdu(HttpServletRequest request, ModelMap map) {
		try {
			xingYunService.saveEdu(getValue(XyConstant.EDU_PARTITION_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步edu失败："+e.getMessage());
		}
		return promptFlt(map, "同步edu结束，请查看结果。");
	}
	
	@RequestMapping("/syncSchool")
	public String syncSchool(HttpServletRequest request, ModelMap map) {
		try {
			xingYunService.saveSchool(getValue(XyConstant.SCHOOL_PARTITION_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步学校失败："+e.getMessage());
		}
		return promptFlt(map, "同步学校结束，请查看结果。");
	}

	@RequestMapping("/syncClassAndStudent")
	public String syncClassAndStudent(HttpServletRequest request, ModelMap map) {
		try {
			xingYunService.saveClassAndStudent(getValue(XyConstant.STUDENT_PARTITION_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步班级失败："+e.getMessage());
		}
		return promptFlt(map, "同步班级结束，请查看结果。");
	}
	
	@RequestMapping("/syncStudent")
	public String syncStudent(HttpServletRequest request, ModelMap map) {
		try {
			xingYunService.saveStudent(getValue(XyConstant.STUDENT_PARTITION_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步学生失败："+e.getMessage());
		}
		return promptFlt(map, "同步学生结束，请查看结果。");
	}
	
	@RequestMapping("/syncFamily")
	public String syncFamily(HttpServletRequest request, ModelMap map) {
		try {
			xingYunService.saveFamily(getValue(XyConstant.FAMILY_PARTITION_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步教师失败："+e.getMessage());
		}
		return promptFlt(map, "同步教师结束，请查看结果。");
	}
	
	@RequestMapping("/syncEduTeacher")
	public String syncEduTeacher(HttpServletRequest request, ModelMap map) {
		try {
			xingYunService.saveTeacher(getValue(XyConstant.EDU_TEACHER_PARTITION_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步教师失败："+e.getMessage());
		}
		return promptFlt(map, "同步教师结束，请查看结果。");
	}
	
	@RequestMapping("/syncSchTeacher")
	public String syncSchTeacher(HttpServletRequest request, ModelMap map) {
		try {
			xingYunService.saveTeacher(getValue(XyConstant.TEACHER_PARTITION_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步教师失败："+e.getMessage());
		}
		return promptFlt(map, "同步教师结束，请查看结果。");
	}
	
	@ResponseBody
	@RequestMapping(
			value = "/syncFamily1",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String syncFamily1(@RequestBody String valueString , ModelMap map) {
		try {
			xingYunService.saveFamily(valueString);
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步教师失败："+e.getMessage());
		}
		return promptFlt(map, "同步教师结束，请查看结果。");
	}
	
	@ResponseBody
	@RequestMapping(
			value = "/syncStudent1",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String syncStudent1(@RequestBody String valueString , ModelMap map) {
		try {
			xingYunService.saveClassAndStudent(valueString);
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步学生失败："+e.getMessage());
		}
		return promptFlt(map, "同步教师结束，请查看结果。");
	}
	
	
	@ResponseBody
	@RequestMapping(
			value = "/syncSchTeacher1",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String syncSchTeacher1(@RequestBody String valueString , ModelMap map) {
		try {
			xingYunService.saveTeacher(valueString);
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步教师失败："+e.getMessage());
		}
		return promptFlt(map, "同步教师结束，请查看结果。");
	}
	
	@ResponseBody
	@RequestMapping("/syncSchool1")
	public String syncSchool1(@RequestBody String valueString, ModelMap map) {
		try {
			xingYunService.saveSchool(valueString);
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步学校失败："+e.getMessage());
		}
		return promptFlt(map, "同步学校结束，请查看结果。");
	}
	
	
	private static String getValue(String num){
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop6:9092");
        props.put("group.id", "xxxxx999999sdfsdfsdfds111");
        props.put("enable.auto.commit", "false"); //是否自动提交
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest"); //消费规则，默认earliest 
        props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.assign(Arrays.asList(new TopicPartition("1143051381184397312",Integer.valueOf(num))));
        try {
            while (true) {
            	JSONArray vArray = new JSONArray();
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic = %s, partition = %s, offset = %d,customer = %s, country = %s\n",
                    record.topic(), record.partition(),
                            record.offset(), record.key(), record.value());
                    if(record.value() != null && !"this is new test2222".equals(record.value())
                    		&& !"this is new test1111".equals(record.value())){
                    	JSONArray array = Json.parseArray(record.value().toString());
                    	vArray.addAll(array);
                    }
                }
                return vArray.toJSONString();
            }
        } catch (Exception e) {
//	            log.error("Unexpected error", e);
        } finally {
            try {
//	                consumer.commitSync();
            } finally {
//	                consumer.close();
            }
        }
			return null;
		
	}
	
	
	
	private Logger log = LoggerFactory.getLogger(YingShuoServiceImpl.class);
	public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop6:9092");
        props.put("group.id", "xxxxx999999sdfsdfsdfdssdff");
        props.put("enable.auto.commit", "false"); //是否自动提交
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest"); //消费规则，默认earliest 
        props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.assign(Arrays.asList(new TopicPartition("1143051381184397312",0)));
//        consumer.subscribe(Arrays.asList("1143051381184397312"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic = %s, partition = %s, offset = %d,customer = %s, country = %s\n",
                    record.topic(), record.partition(),
                            record.offset(), record.key(), record.value());
                    //处理具体的业务
//                    String sssString = getRSAdata(record.value());
//                    System.out.println(sssString);
                }
//                consumer.commitAsync();
            }
        } catch (Exception e) {
//            log.error("Unexpected error", e);
        } finally {
            try {
//                consumer.commitSync();
            } finally {
//                consumer.close();
            }
        }
    }
	
	/**
	 * 进行rsa 数据解密
	 */
    private static String getRSAdata(String value){
    	String keyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxY3mLo0xtS6TDQ6+iWLYubIpiqg+HD5q0c7I1Xk7jmEWYZe+XRfLDDwWeB5bsu7FC3Ua9y+5Ves/rDdltxlifzYwCHlDlSErYjM5hRbQ8top9RdllWBVsGuWSrAlouv+V48WqiI0OPYtYnqWqQ2JFWf06Mxq3Y4PPNTC/STjRpbi+qR09HHfnP7n2j9C07HS06fFK1fzU0nMAIfg/f4vZoTk2rnruNEJCoFn1uI0GZ7eDnfDl5kjkizytMwQ9XrXRJyHB9QdLe1VYgfLOKVniHuhfifx9ShEKhkySXCzcgJbBVpEw59RPtdK1fKOiG01OJ0S5qFdXyUUU5qmBUsqIwIDAQAB";
    	String value1 = new RSA(null,keyString).decryptStr(value,KeyType.PublicKey);
//    	System.out.println("-------" + value1);
    	return value1;
    }
}
