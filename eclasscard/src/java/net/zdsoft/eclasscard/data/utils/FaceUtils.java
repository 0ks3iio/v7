package net.zdsoft.eclasscard.data.utils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.zdsoft.eclasscard.data.dto.aliapi.SyncFacePicResponse;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.linkface.model.v20180720.CreateGroupRequest;
import com.aliyuncs.linkface.model.v20180720.CreateGroupResponse;
import com.aliyuncs.linkface.model.v20180720.DeleteFaceRequest;
import com.aliyuncs.linkface.model.v20180720.DeleteFaceResponse;
import com.aliyuncs.linkface.model.v20180720.QueryAddUserInfoRequest;
import com.aliyuncs.linkface.model.v20180720.QueryAddUserInfoResponse;
import com.aliyuncs.linkface.model.v20180720.QueryAllGroupsRequest;
import com.aliyuncs.linkface.model.v20180720.QueryAllGroupsResponse;
import com.aliyuncs.linkface.model.v20180720.QueryAuthenticationRequest;
import com.aliyuncs.linkface.model.v20180720.QueryAuthenticationResponse;
import com.aliyuncs.linkface.model.v20180720.QueryFaceRequest;
import com.aliyuncs.linkface.model.v20180720.QueryFaceResponse;
import com.aliyuncs.linkface.model.v20180720.QueryGroupUsersRequest;
import com.aliyuncs.linkface.model.v20180720.QueryGroupUsersResponse;
import com.aliyuncs.linkface.model.v20180720.QueryGroupUsersResponse.DataItem;
import com.aliyuncs.linkface.model.v20180720.QueryLicensesRequest;
import com.aliyuncs.linkface.model.v20180720.QueryLicensesResponse;
import com.aliyuncs.linkface.model.v20180720.QuerySyncPicScheduleRequest;
import com.aliyuncs.linkface.model.v20180720.QuerySyncPicScheduleResponse;
import com.aliyuncs.linkface.model.v20180720.RegisterFaceRequest;
import com.aliyuncs.linkface.model.v20180720.RegisterFaceResponse;
import com.aliyuncs.linkface.model.v20180720.SyncFacePicturesRequest;
import com.aliyuncs.linkface.model.v20180720.SyncFacePicturesResponse;
import com.aliyuncs.linkface.model.v20180720.UpdateFaceRequest;
import com.aliyuncs.linkface.model.v20180720.UpdateFaceResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class FaceUtils {
	private static final Logger log = LoggerFactory.getLogger(FaceUtils.class);
	private static String accessKeyID = "EXdfDYcMyuBMbOLq";//主账号AK信息
	private static String accessKeySecret = "zqX8o93cQUb04LFWxxCeyCALa3Yd6q";
	private static String regionId = "cn-shanghai";
	private static String faceProductCode = "LinkFace";
	private static String domain = "iotx-face.cn-shanghai.aliyuncs.com";

	private static DefaultAcsClient faceClient;

	static {
		faceClient = getFaceClient();
	}

	public static DefaultAcsClient getFaceClient() {
		DefaultAcsClient client = null;

		try {
			IClientProfile profile = DefaultProfile.getProfile(regionId,
					accessKeyID, accessKeySecret);
			DefaultProfile.addEndpoint(regionId, regionId, faceProductCode,
					domain);
			// 初始化client
			client = new DefaultAcsClient(profile);
		} catch (Exception e) {
			log.error("初始化client失败！exception:" + e.getMessage());
		}

		return client;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static AcsResponse executeFace(RpcAcsRequest request) {
		AcsResponse response = null;
		try {
			response = faceClient.getAcsResponse(request);
		} catch (Exception e) {
			log.error("执行失败：e:" + e.getMessage());
		}
		return response;
	}


	/**
	 * 查询设备人脸
	 * @param productKey
	 * @param deviceName
	 * @return
	 */
	public static String queryAddUserInfo(String productKey, String deviceName) {
		QueryAddUserInfoRequest request = new QueryAddUserInfoRequest();
		request.setDeviceName(deviceName);
		request.setProductKey(productKey);
		QueryAddUserInfoResponse response = (QueryAddUserInfoResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("查询设备图片成功:{}", jsonData);
			return jsonData;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("查询设备图片失败:{}", jsonData);
			return jsonData;
		}
		return "";
	}

	/**
	 * 创建用户组
	 */
	public static Boolean createGroup(String groupId) {
		CreateGroupRequest request = new CreateGroupRequest();
		request.setGroupId(groupId);
		CreateGroupResponse response = (CreateGroupResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			log.info("创建用户组成功: " + response.getCode() + response.getMessage());
			return response.getSuccess();
		} else if (response != null){
			log.error("创建用户组失败！requestId:" + response.getRequestId() + "原因："
					+ response.getCode() + response.getMessage());
		}
		return false;
	}
	
	/**
	 * 注册人脸
	 * 备注1：16101都是人脸注册人脸失败，一般为人脸不合格
	 *	备注2：注册的人脸图片的大小请不要超过2M
	 *	备注3：base64的数据只需要传base64的数据, 前端展示使用的data:image/jpg;base64这个前缀不用传
	 * @param groupId
	 * @param userId
	 * @param image
	 * @param userInfo
	 * @return
	 */
	public static boolean registerFace(String groupId,String userId,String image,String userInfo) {
		RegisterFaceRequest request = new RegisterFaceRequest();
		request.setGroupId(groupId);
		request.setUserId(userId);
		request.setUserInfo(userInfo);
		request.setImage(image);
		RegisterFaceResponse response = (RegisterFaceResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("注册人脸成功: {}", jsonData);
			return true;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("注册人脸失败: {}", jsonData);
			return false;
		}
		return false;
	}
	
	/**
	 * 获取授权信息
	 * @param productKey
	 * @param deviceName
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static String queryAuthentication(String productKey,String deviceName,Integer currentPage,Integer pageSize) {
		QueryAuthenticationRequest request = new QueryAuthenticationRequest();
		request.setProductKey(productKey);
		request.setDeviceName(deviceName);
		request.setCurrentPage(currentPage);
		request.setPageSize(pageSize);
		QueryAuthenticationResponse response = (QueryAuthenticationResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("获取授权信息成功: {}", jsonData);
			return jsonData;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("获取授权信息失败: {}", jsonData);
			return jsonData;
		}
		return null;
	}
	
	/**
	 * 查询许可证信息
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static String queryLicenses(Integer currentPage,Integer pageSize) {
		QueryLicensesRequest request = new QueryLicensesRequest();
		request.setCurrentPage(currentPage);
		request.setPageSize(pageSize);
		QueryLicensesResponse response = (QueryLicensesResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("查询可证信息成功: {}", jsonData);
			return jsonData;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("查询可证信息失败: {}", jsonData);
			return jsonData;
		}
		return null;
	}
	
	/**
	 * 同步设备图片
	 * @param groupId
	 * @param deviceName
	 * @return
	 */
	public static SyncFacePicResponse syncFacePictures(String groupId,String productKey,String deviceName) {
		SyncFacePicturesRequest request = new SyncFacePicturesRequest();
		request.setProductKey(productKey);
		request.setGroupId(groupId);
		request.setDeviceName(deviceName);
		SyncFacePicturesResponse response = (SyncFacePicturesResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("同步设备图片成功: {}", jsonData);
			return SUtils.dc(jsonData, SyncFacePicResponse.class);
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("同步设备图片失败: {}", jsonData);
			return SUtils.dc(jsonData, SyncFacePicResponse.class);
		}
		return new SyncFacePicResponse();
	}
	
	/**
	 * 查询设备人脸同步进度
	 * @param productKey
	 * @param deviceName
	 * @return
	 */
	public static String querySyncPicSchedule(String productKey,String deviceName) {
		QuerySyncPicScheduleRequest request = new QuerySyncPicScheduleRequest();
		request.setProductKey(productKey);
		request.setDeviceName(deviceName);
		QuerySyncPicScheduleResponse response = (QuerySyncPicScheduleResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("查询设备人脸同步进度成功: {}", jsonData);
			return response.getMessage();
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("查询设备人脸同步进度失败: {}", jsonData);
		}
		return null;
	}
	
	/**
	 * 查询人脸信息
	 * @param userId
	 * @return
	 */
	public static String queryFace(String userId) {
		QueryFaceRequest request = new QueryFaceRequest();
		request.setUserId(userId);
		QueryFaceResponse response = (QueryFaceResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("查询人脸信息成功: {}", jsonData);
			return jsonData;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("查询人脸信息失败: {}", jsonData);
			return jsonData;
		}
		return null;
	}
	
	/**
	 * 查询对应分组内人脸信息
	 * @param groupId
	 * @return
	 */
	public static String queryGroupFace(String groupId,Integer currentPage,Integer pageSize) {
		QueryGroupUsersRequest request = new QueryGroupUsersRequest();
		request.setGroupId(groupId);
		request.setCurrentPage(currentPage);
		request.setPageSize(pageSize);
		QueryGroupUsersResponse response = (QueryGroupUsersResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			List<DataItem> aa = response.getData();
			for(DataItem d:aa){
				System.out.print("'"+d.getUserId()+"',");
			}
			log.info("查询对应分组内人脸信息成功: {}", jsonData);
			return jsonData;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("查询对应分组内人脸信息失败: {}", jsonData);
			return jsonData;
		}
		return null;
	}
	
	/**
	 * 更新人脸图片
	 * @param productKey
	 * @param deviceName
	 * @return
	 */
	public static boolean updateFace(String userId,String image,String userInfo) {
		UpdateFaceRequest request = new UpdateFaceRequest();
		request.setUserId(userId);
		request.setImage(image);
		request.setUserInfo(userInfo);
		UpdateFaceResponse response = (UpdateFaceResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("更新人脸信息成功: {}", jsonData);
			return true;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("更新人脸信息失败: {}", jsonData);
		}
		return false;
	}
	
	/**
	 * 删除人脸
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static boolean deleteFace(String userId,String groupId) {
		DeleteFaceRequest request = new DeleteFaceRequest();
		request.setUserId(userId);
		request.setGroupId(groupId);
		DeleteFaceResponse response = (DeleteFaceResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("删除人脸图片成功: {}", jsonData);
			return true;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("删除人脸图片失败: {}", jsonData);
			return false;
		}
		return false;
	}
	
	/**
	 * 查询所有人脸分组
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static String queryAllGroups(Integer currentPage,Integer pageSize) {
		QueryAllGroupsRequest request = new QueryAllGroupsRequest();
		request.setCurrentPage(currentPage);
		request.setPageSize(pageSize);
		QueryAllGroupsResponse response = (QueryAllGroupsResponse) executeFace(request);
		if (response != null && response.getSuccess()) {
			String jsonData = JSONObject.toJSONString(response);
			log.info("查询所有人脸分组成功: {}", jsonData);
			return jsonData;
		} else if (response != null){
			String jsonData = JSONObject.toJSONString(response);
			log.error("查询所有人脸分组失败: {}", jsonData);
			return jsonData;
		}
		return null;
	}
	
	/**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
	public static String getImgStr(String imgFile){
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try 
        {
            in = new FileInputStream(imgFile);        
            data = new byte[in.available()];
            in.read(data);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }finally{
        	if(in!=null){
	        	try {
        			in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return new String(Base64.encodeBase64(data));
    }
	public static void main(String[] args) {
//		queryAddUserInfo("a14NgSnKZFJ", "ecc_card_test");
//		createGroup("123456789");
		
//		registerFace("00EA65CEE57ADE8DE050A8C09B006DCE", "1234565", getImgStr("F:/Test/pic/123456.jpeg"), "123456");
//		registerFace("00EA65CEE57ADE8DE050A8C09B006DCE", "123458", getImgStr("C:/Users/user/Desktop/facepic/123458.jpg"), "123458");
//		queryFace("10000001");
//		queryAllGroups(1, 10);
//		queryFace("10000001");
//		queryLicenses(1, 10);
		String productKey = "a1YRbjBEydO";
		String deviceName = "cs0001";
//		queryAddUserInfo("a1YRbjBEydO", "cs0001");
//		queryAuthentication(productKey, deviceName, 1, 2);
//		registerFace("00EA65CEE57ADE8DE050A8C09B006DCE", "55E6D8CBF244406E8637C30AC726619E", getImgStr("C:\\Users\\user\\Desktop\\facepic\\10002.jpg"), "55E6D8CBF244406E8637C30AC726614E");
		
//		syncFacePictures("00EA65CEE57ADE8DE050A8C09B006DCE",productKey, deviceName);
//		querySyncPicSchedule(productKey, deviceName);
		deleteFace("402880954E257991014E2587C3A10006", "00EA65CEE57ADE8DE050A8C09B006DCE");
//		queryFace("402880954E257991014E2587C3A10006");
//		String face = queryGroupFace("00EA65CEE57ADE8DE050A8C09B006DCE", 1, 50);
	}
}
