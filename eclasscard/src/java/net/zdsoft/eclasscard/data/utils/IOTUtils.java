package net.zdsoft.eclasscard.data.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.iot.model.v20170420.ApplyDeviceWithNamesRequest;
import com.aliyuncs.iot.model.v20170420.ApplyDeviceWithNamesResponse;
import com.aliyuncs.iot.model.v20170420.PubBroadcastRequest;
import com.aliyuncs.iot.model.v20170420.PubBroadcastResponse;
import com.aliyuncs.iot.model.v20170420.PubRequest;
import com.aliyuncs.iot.model.v20170420.PubResponse;
import com.aliyuncs.iot.model.v20170420.QueryApplyStatusRequest;
import com.aliyuncs.iot.model.v20170420.QueryApplyStatusResponse;
import com.aliyuncs.iot.model.v20170420.QueryDeviceRequest;
import com.aliyuncs.iot.model.v20170420.QueryDeviceResponse;
import com.aliyuncs.iot.model.v20170420.QueryPageByApplyIdRequest;
import com.aliyuncs.iot.model.v20170420.QueryPageByApplyIdResponse;
import com.aliyuncs.iot.model.v20170420.RRpcRequest;
import com.aliyuncs.iot.model.v20170420.RRpcResponse;
import com.aliyuncs.iot.model.v20170420.RegistDeviceRequest;
import com.aliyuncs.iot.model.v20170420.RegistDeviceResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import net.zdsoft.eclasscard.data.dto.RrpcMsg;
import net.zdsoft.framework.entity.Json;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IOTUtils {
	private static final Logger log = LoggerFactory.getLogger(IOTUtils.class);
	private static String accessKeyID = "EXdfDYcMyuBMbOLq";
	private static String accessKeySecret = "zqX8o93cQUb04LFWxxCeyCALa3Yd6q";
	private static String regionId = "cn-shanghai";
	private static String productCode = "Iot";
	private static String domain = "iot.cn-shanghai.aliyuncs.com";

	private static DefaultAcsClient client;

	static {
		client = getIotClient();
	}

	public static DefaultAcsClient getIotClient() {
		DefaultAcsClient client = null;

		try {
			IClientProfile profile = DefaultProfile.getProfile(regionId,
					accessKeyID, accessKeySecret);
			DefaultProfile.addEndpoint(regionId, regionId, productCode, domain);
			// 初始化client
			client = new DefaultAcsClient(profile);
		} catch (Exception e) {
			log.error("初始化client失败！exception:" + e.getMessage());
		}

		return client;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static AcsResponse executeTest(RpcAcsRequest request) {
		AcsResponse response = null;
		try {
			response = client.getAcsResponse(request);
		} catch (Exception e) {
			log.error("执行失败：e:" + e.getMessage());
		}
		return response;
	}


	/**
	 * 发送rrpc消息
	 *
	 * @param productKey
	 *            产品pk
	 * @param deviceName
	 *            设备名称
	 * @param msg
	 *            消息内容
	 */
	public static boolean rrpcTest(String productKey, String deviceName, String msg) {
		RRpcRequest request = new RRpcRequest();
		request.setProductKey(productKey);
		request.setDeviceName(deviceName);
		request.setRequestBase64Byte(Base64.encodeBase64String(msg.getBytes()));
		request.setTimeout(5000);
		RRpcResponse response = (RRpcResponse) executeTest(request);
		if (response != null) {
			if (response.getSuccess()) {
				log.info("rrpc消息发送成功！messageId:"
						+ response.getMessageId()
						+ ",payloadBase64Byte："
						+ new String(Base64.decodeBase64(response
								.getPayloadBase64Byte())));
				return true;
			} else {
				log.error("rrpc消息发送失败！requestId:" + response.getRequestId()
						+ "原因：" + response.getErrorMessage());
			}
		}
		return false;
	}

	/**
	 * 查询设备列表
	 *
	 * @param productKey
	 *            产品PK
	 * @param pageSize
	 *            每页显示条数 非必须
	 * @param currentPage
	 *            当前页 非必须
	 */
	public static void QueryDeviceTest(String productKey, Integer pageSize,
			Integer currentPage) {
		QueryDeviceRequest request = new QueryDeviceRequest();
		request.setProductKey(productKey);
		request.setPageSize(pageSize);
		request.setCurrentPage(currentPage);
		QueryDeviceResponse response = (QueryDeviceResponse) executeTest(request);
		if (response != null && response.getSuccess()) {
			log.info("查询设备成功！ " + JSONObject.toJSONString(response));
		} else if (response != null){
			log.error("查询设备失败！requestId:" + response.getRequestId() + "原因："
					+ response.getErrorMessage());
		}

	}

	/**
	 * 分页查询批量申请的设备
	 *
	 * @param applyId
	 *            申请单id
	 * @param pageSize
	 *            每页显示条数
	 * @param currentPage
	 *            当前页
	 */
	public static void queryPageByApplyIdTest(Long applyId, Integer pageSize,
			Integer currentPage) {
		QueryPageByApplyIdRequest request = new QueryPageByApplyIdRequest();
		request.setApplyId(applyId);
		request.setPageSize(pageSize);
		request.setCurrentPage(currentPage);
		QueryPageByApplyIdResponse response = (QueryPageByApplyIdResponse) executeTest(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("分页查询批量申请的设备成功！ : {}", jsonData);
		} else if (response != null){
			log.error("分页查询批量申请的设备失败！requestId:" + response.getRequestId()
					+ "原因：" + response.getErrorMessage());
		}

	}

	/**
	 * pub消息
	 *
	 * @param productKey
	 *            pk
	 * @param topic
	 *            topic
	 * @param msg
	 *            消息内容
	 */
	public static void pubTest(String productKey, String topic, String msg) {
		PubRequest request = new PubRequest();
		request.setProductKey(productKey);
		request.setTopicFullName(topic);
		request.setMessageContent(Base64.encodeBase64String(msg.getBytes()));
		request.setQos(1);
		PubResponse response = (PubResponse) executeTest(request);
		if (response != null && response.getSuccess()) {
			log.info("发送消息成功！messageId：" + response.getMessageId());
		} else if (response != null){
			log.error("发送消息失败！requestId:" + response.getRequestId() + "原因："
					+ response.getErrorMessage());
		}

	}

	/**
	 * 发送广播消息
	 *
	 * @param productKey
	 *            产品pk
	 * @param topic
	 *            广播topic /broadcast/${pk}/+
	 * @param msg
	 *            消息内容
	 */
	public static void pubBroadcastTest(String productKey, String topic,
			String msg) {
		PubBroadcastRequest request = new PubBroadcastRequest();
		request.setProductKey(productKey);
		request.setTopicFullName(topic);
		request.setMessageContent(Base64.encodeBase64String(msg.getBytes()));
		PubBroadcastResponse response = (PubBroadcastResponse) executeTest(request);
		if (response != null && response.getSuccess()) {
			log.info("发送消息成功！");
		} else if (response != null){
			log.error("发送消息失败！requestId:" + response.getRequestId() + "原因："
					+ response.getErrorMessage());

		}

	}

	/**
	 * 注册设备
	 *
	 * @param productKey
	 *            产品pk
	 * @param deviceName
	 *            设备名称 非必须
	 * @return 设备名称
	 */
	public static String registDeviceTest(String productKey, String deviceName) {
		RegistDeviceRequest request = new RegistDeviceRequest();
		request.setProductKey(productKey);
		request.setDeviceName(deviceName);
		RegistDeviceResponse response = (RegistDeviceResponse) executeTest(request);
		if (response != null && response.getSuccess()) {
			log.info("创建设备成功！deviceName:" + response.getDeviceName()
					+ ",deviceSecret:" + response.getDeviceSecret());
			return response.getDeviceName();
		} else if (response != null){
			log.error("创建设备失败！requestId:" + response.getRequestId() + "原因："
					+ response.getErrorMessage());
		}
		return null;
	}

	/**
	 * 批量申请设备
	 *
	 * @param productKey
	 *            产品pk
	 * @return 申请单id
	 */
	public static Long applyDeviceWithNamesTest(String productKey,
			List<String> deviceNames) {
		ApplyDeviceWithNamesRequest request = new ApplyDeviceWithNamesRequest();
		request.setProductKey(productKey);
		request.setDeviceNames(deviceNames);
		ApplyDeviceWithNamesResponse response = (ApplyDeviceWithNamesResponse) executeTest(request);

		if (response != null && response.getSuccess()) {
			log.info("批量申请设备成功！ applyId: " + response.getApplyId());
			return response.getApplyId();

		} else if (response != null){
			log.error("批量申请设备失败！requestId:" + response.getRequestId() + "原因："
					+ response.getErrorMessage());

		}
		return null;

	}

	/**
	 * 查询申请单是否执行完毕
	 *
	 * @param applyId
	 *            申请单id
	 * @return 是否执行完毕
	 */
	public static Boolean queryApplyStatusTest(Long applyId) {
		QueryApplyStatusRequest request = new QueryApplyStatusRequest();
		request.setApplyId(applyId);
		QueryApplyStatusResponse response = (QueryApplyStatusResponse) executeTest(request);
		if (response != null && response.getSuccess()) {
			log.info("查询申请单执行状态成功！ 是否执行完成: " + response.getFinish());
			return response.getFinish();
		} else if (response != null){
			log.error("查询申请单执行状态失败！requestId:" + response.getRequestId()
					+ "原因：" + response.getErrorMessage());

		}
		return false;
	}
	
	
	public static void main(String[] args) {
		//下发天河中学人脸
		String unitId = "F077BEB92FE1421C953B04EB376BC4ED";
		String productKey = "a1YRbjBEydO";
		String deviceName = "thzx001";
		RrpcMsg rrpcMsg = new RrpcMsg();
		RrpcMsg.Params params = new RrpcMsg().new Params();
		params.setFacePicURL("http://xk.msyk.cn/eccShow/eclasscard/face/pictures/url?unitId="+unitId);
		params.setGroupID(unitId);
		rrpcMsg.setParams(params);
		rrpcMsg.setMethod(RrpcMsg.SYNC_FACE_PICTURES);
		boolean flag = IOTUtils.rrpcTest(productKey, deviceName, Json.toJSONString(rrpcMsg));
	}
}
