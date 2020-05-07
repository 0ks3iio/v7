package net.zdsoft.syncdata.custom.xingyun.listen;

import java.io.IOException;
import java.util.List;

import net.zdsoft.basedata.constant.custom.XyConstant;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.syncdata.custom.xingyun.service.XingYunService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


//@Component
public class XingYunListener {
	@Autowired
	private XingYunService xingYunService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	
	private volatile boolean isEduDoFinish = Boolean.TRUE;
	private volatile boolean isSchDoFinish = Boolean.TRUE;
	private volatile boolean isStuDoFinish = Boolean.TRUE;
	private volatile boolean isFamDoFinish = Boolean.TRUE;
	
    private static final Logger log= LoggerFactory.getLogger(XingYunListener.class);

    @KafkaListener(id = "edu", topicPartitions =
        { @TopicPartition(topic = "${xingyun_topic}", partitions = {XyConstant.EDU_PARTITION_VALUE,XyConstant.EDU_TEACHER_PARTITION_VALUE})
        },groupId= XyConstant.GROUP_ID_VALUE, containerFactory="batchContainerFactory")
	public void edulisten(List<ConsumerRecord<?,?>> records, Acknowledgment ack) {
	    log.info("edu的 receive ---------------: "+records.size());
	    System.out.println("edu的 receive ---------------: "+records.size());
	    try {
	    	 isEduDoFinish = Boolean.FALSE;
	    	 turnOnXingYunUpdate();
	       	 JSONArray vArray = new JSONArray();
	       	 JSONArray tArray = new JSONArray();
	       	 records.forEach(record -> {
		       		if(record.value() != null && !"this is new test2222".equals(record.value())
	                		&& !"this is new test1111".equals(record.value())){
		       			System.out.println("获取的数据是--------" + getRSAdata(record.value().toString()));
		       			JSONArray array = Json.parseArray(getRSAdata(record.value().toString()));
		       			if(record.partition() == Integer.valueOf(XyConstant.EDU_TEACHER_PARTITION_VALUE)){
		       				tArray.addAll(array);
		       			}else{
		       				vArray.addAll(array);
		       			}
	                }
	            });
	       	 if(vArray != null && vArray.size() >= 1) {
	       		xingYunService.saveEdu(vArray.toJSONString());
	       	 }
	       	 if(tArray != null && tArray.size() >= 1) {
	       		xingYunService.saveTeacher(tArray.toJSONString());
	       	 }
	         isEduDoFinish = Boolean.TRUE;
	         turnOffXingYunUpdate();
	      } catch (Exception e) {
	          log.error("Kafka监听异常"+e.getMessage(),e);
	      } finally {
	          ack.acknowledge();
	      }
	}

	@KafkaListener(id = "school", topicPartitions =
	        { @TopicPartition(topic = "${xingyun_topic}", partitions = {XyConstant.SCHOOL_PARTITION_VALUE,XyConstant.TEACHER_PARTITION_VALUE})
	        },groupId= XyConstant.GROUP_ID_VALUE, containerFactory="batchContainerFactory")
	public void schoolListen(List<ConsumerRecord<?,?>> records, Acknowledgment ack) {
	    log.info("school的 receive ---------------: "+records.size());
	    System.out.println("school的 receive ---------------: "+records.size());
	    try {
	    	 isSchDoFinish = Boolean.FALSE;
	    	 turnOnXingYunUpdate();
	       	 JSONArray vArray = new JSONArray();
	         JSONArray tArray = new JSONArray();
	       	 records.forEach(record -> {
		       		if(record.value() != null && !"this is new test2222".equals(record.value())
	                		&& !"this is new test1111".equals(record.value())){
		       			System.out.println("获取的数据是--------" + getRSAdata(record.value().toString()));
	                	JSONArray array = Json.parseArray(getRSAdata(record.value().toString()));
	       		    	if(record.partition() == Integer.valueOf(XyConstant.TEACHER_PARTITION_VALUE)){
	       		    		tArray.addAll(array);
	       		    	}else{
	       		    		vArray.addAll(array);
	       		    	}
	                }
	            });
	       	 if(vArray != null && vArray.size() >= 1) {
	       		xingYunService.saveSchool(vArray.toJSONString());
	       	 }
	       	 if(tArray != null && tArray.size() >= 1) {
	       		 xingYunService.saveTeacher(tArray.toJSONString());
	       	 }
	       	 isSchDoFinish = Boolean.TRUE;
	       	 turnOffXingYunUpdate();
	      } catch (Exception e) {
	          log.error("Kafka监听异常"+e.getMessage(),e);
	      } finally {
	          ack.acknowledge();
	      }
	}
    
    
    //声明consumerID为demo，监听topicName为topic.quick.demo的Topic
    @KafkaListener(id = "studnet", topicPartitions =
            { @TopicPartition(topic = "${xingyun_topic}", partitions = {XyConstant.STUDENT_PARTITION_VALUE})
            },groupId=XyConstant.GROUP_ID_VALUE, containerFactory="batchContainerFactory")
    public void studnetlisten(List<ConsumerRecord<?,?>> records, Acknowledgment ack) {
        log.info("studnet的 receive ---------------: "+records.size());
        System.out.println("studnet的 receive ---------------: "+records.size());
        try {
        	 isStuDoFinish = Boolean.FALSE;
        	 turnOnXingYunUpdate();
        	 JSONArray vArray = new JSONArray();
        	 records.forEach(record -> {
        		 if(record.value() != null && !"this is new test2222".equals(record.value())
        				 && !"this is new test1111".equals(record.value())){
        			 System.out.println("获取的数据是--------" + getRSAdata(record.value().toString()));
        			 JSONArray array = Json.parseArray(getRSAdata(record.value().toString()));
        			 vArray.addAll(array);
        		 }
             });
        	 if(vArray != null && vArray.size() >= 1) {
 	       		xingYunService.saveClassAndStudent(vArray.toJSONString());
 	       	 }
            isStuDoFinish = Boolean.TRUE;
            turnOffXingYunUpdate();
        } catch (Exception e) {
            log.error("Kafka监听异常"+e.getMessage(),e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "family", topicPartitions =
            { @TopicPartition(topic = "${xingyun_topic}", partitions = {XyConstant.FAMILY_PARTITION_VALUE})
            },groupId=XyConstant.GROUP_ID_VALUE, containerFactory="batchContainerFactory")
    public void familylisten(List<ConsumerRecord<?,?>> records, Acknowledgment ack) {
	      log.info("family的 receive ---------------: "+records.size());
	      System.out.println("family的 receive ---------------: "+records.size());
	      try {
	    	 isFamDoFinish = Boolean.FALSE;
	    	 turnOnXingYunUpdate();
	       	 JSONArray vArray = new JSONArray();
	       	 records.forEach(record -> {
		       		if(record.value() != null){
		       			System.out.println("获取的数据是--------" + getRSAdata(record.value().toString()));
		       			JSONArray array = Json.parseArray(getRSAdata(record.value().toString()));
		       			vArray.addAll(array);
		       		}
	            });
	       	 if(vArray != null && vArray.size() >= 1) {
 	       		xingYunService.saveFamily(vArray.toJSONString());
 	       	 }
	   	     isFamDoFinish = Boolean.TRUE;
	   	     turnOffXingYunUpdate();
	      } catch (Exception e) {
	          log.error("Kafka监听异常"+e.getMessage(),e);
	      } finally {
	          ack.acknowledge();
	      }
    }
    
	@KafkaListener(id = "logoutUser", topicPartitions =
        { @TopicPartition(topic = "${xingyun_topic}", partitions = {XyConstant.USER_LOGOUT_VALUE})
        },groupId=XyConstant.GROUP_ID_VALUE, containerFactory="batchContainerFactory")
	public void logoutlisten(List<ConsumerRecord<?,?>> records, Acknowledgment ack) {
	      log.info("logoutUser的 receive ---------------: "+records.size());
	      System.out.println("logoutUser的 receive ---------------: "+records.size());
	      try {
	       	 records.forEach(record -> {
	       		if(record.value() != null){
	       			System.out.println("获取的数据是--------" + getRSAdata(record.value().toString()));
	       			JSONObject jsonObject = Json.parseObject(getRSAdata(record.value().toString()));
	       			doLogOut(jsonObject.getString("userId"));
	       		}
	         });
	      } catch (Exception e) {
	          log.error("Kafka监听异常"+e.getMessage(),e);
	      } finally {
	          ack.acknowledge();
	      }
}

	private void doLogOut(String userId) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(userId)){
			userId = doChangeId(userId);
			String token = RedisUtils.get(XyConstant.XINGYUN_FIRST_LOGIN_TICKET + userId);
			if(StringUtils.isNotBlank(token)){
				StringBuffer sBuffer = new StringBuffer();
				String ipString = Evn.getWebUrl();
				ipString = UrlUtils.ignoreLastRightSlash(ipString);
				sBuffer.append(ipString);
				sBuffer.append("/homepage/remote/openapi/lasa/logoutcallback");
				sBuffer.append("?");
				sBuffer.append("tokenid");
				sBuffer.append("=");
				sBuffer.append(token);
				RedisUtils.del(XyConstant.XINGYUN_FIRST_LOGIN_TICKET + userId);
				try {
					UrlUtils.get(sBuffer.toString(), StringUtils.EMPTY);
				} catch (IOException e) {
					e.printStackTrace();
					log.error("系统平台监听退出用户id " + userId + "失败！！", e);
				}
			}
		}
	}
	
	/**
	 * id不足32位的， 前面补为0
	 * @param id
	 * @return
	 */
	private String doChangeId(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		return StringUtils.leftPad(id, 32, "0");
	}
	
	
	/**
	 * 同步完成，修改配置
	 */
	private synchronized void turnOffXingYunUpdate() {
		// TODO Auto-generated method stub
		systemIniRemoteService.doRefreshCache(XyConstant.XY_USING_UPDATE_TRIGGER);
		String	nowValue = systemIniRemoteService.findValue(XyConstant.XY_USING_UPDATE_TRIGGER);
		if(nowValue.equals("0") && isEduDoFinish && isSchDoFinish && isStuDoFinish && isFamDoFinish){
			systemIniRemoteService.updateNowvalue(XyConstant.XY_USING_UPDATE_TRIGGER, "1");
		}
	}
	
	/**
	 * 打开允许同步配置
	 */
	private synchronized void turnOnXingYunUpdate() {
			// TODO Auto-generated method stub
		systemIniRemoteService.doRefreshCache(XyConstant.XY_USING_UPDATE_TRIGGER);
		String	nowValue = systemIniRemoteService.findValue(XyConstant.XY_USING_UPDATE_TRIGGER);
		if(nowValue.equals("1") && (isEduDoFinish || isSchDoFinish || isStuDoFinish || isFamDoFinish) ){
			systemIniRemoteService.updateNowvalue(XyConstant.XY_USING_UPDATE_TRIGGER, "0");
		}
	}
	
	/**
	 * 进行rsa 数据解密
	 */
    private String getRSAdata(String value){
        if (StringUtils.isNotBlank(XyConstant.XY_IS_ENCRYPTION) && "true".equals(XyConstant.XY_IS_ENCRYPTION)) {
        	return new RSA(null,XyConstant.XY_PUBLIC_KEY).decryptStr(value,KeyType.PublicKey);
        }else{
        	return value;
        }
    }
}
