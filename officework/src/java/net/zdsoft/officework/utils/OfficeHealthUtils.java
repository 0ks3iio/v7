package net.zdsoft.officework.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.constant.OfficeRemoteURLConstants;
import net.zdsoft.officework.dto.ClassRoomDto;
import net.zdsoft.officework.dto.OfficeHealthDto;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthData;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.service.OfficeHealthDeviceService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import LovicocoAPParser.Events.Device;
import LovicocoAPParser.Events.DeviceData;
import LovicocoAPParser.Events.DeviceDataCollect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class OfficeHealthUtils {
	/**获取单个学生的健康信息url*/
	public static final String H3C_GET_STUDENT_STEP_URL = "https://lvzhouapi.h3c.com/iot/ioteduapp_motion_restapi/studentdetailmotion";
	private static final Logger log = LoggerFactory.getLogger(OfficeHealthUtils.class);
	public static final int pageSize = 50;
	public static final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
	public static final SimpleDateFormat h3cStepDf=new SimpleDateFormat("yyyy/MM/dd");
	public static final SimpleDateFormat h3cHeatDf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static Map<String, String>  h3cPropertiesMap = new HashMap<String, String>();//健康数据配置
	public static Map<String, String> h3cDevSnMap = new HashMap<String, String>();//devsn--unitId
	public static Map<String, String> h3cUnitIdMap = new HashMap<String, String>();//unitId--devsn
	
	
	public static boolean isScheduler() {
		return "true".equals(getString(OfficeConstants.EIS_SCHEDULER_OFFICE_H3C_HEALTH_START));
	}
	public static boolean isDemo() {
		return ("true".equals(getString(OfficeConstants.OFFICE_H3C_ATTANCE_DEMO))) && Evn.isScheduler();
	}
	
	private static SystemIniRemoteService getSystemIniRemoteService() {
		return Evn.getBean("systemIniRemoteService");
	}
	private static OfficeHealthDeviceService getOfficeHealthDeviceService() {
		return Evn.getBean("officeHealthDeviceService");
	}
	
	/**
	 * 获取key对应的配置值
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return getH3cPropertiesMap().get(key);
	}
	
	/**
	 * 获取华三配置数据
	 * @return
	 */
	public static Map<String, String> getH3cPropertiesMap(){
		if(h3cPropertiesMap.size()>0)
			return h3cPropertiesMap;
		
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource h3cResource = null;
		try {
			h3cResource = new FileSystemResource("/opt/server_data/v7/h3chealth.properties");
		} catch (Exception e) {
		}
		
		if(!h3cResource.isReadable()){
			h3cResource = resourceLoader.getResource("conf/h3chealth.properties");
		}else{
		}
		
		if(!h3cResource.isReadable())
			return h3cPropertiesMap;
		
		try {
			Properties p =FileUtils.readProperties(h3cResource.getInputStream());
			
			for (Object key : p.keySet()) {
	            String value = p.getProperty(key.toString());
	            h3cPropertiesMap.put(key.toString(), value);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return h3cPropertiesMap;
	}
	
	public static Map<String, String> getDevSnMap(){
		if(h3cDevSnMap.size()>0)
			return h3cDevSnMap;
		Map<String, String> propertiesMap = getH3cPropertiesMap();
		if(propertiesMap.containsKey(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_UNITID_DEVSN)){
			String devSnStr = propertiesMap.get(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_UNITID_DEVSN);
			if(StringUtils.isNotBlank(devSnStr)){
				String[] unitIddevSns = devSnStr.split(",");
				for(int i = 0; i < unitIddevSns.length; i++){
					String unitIdDevSn = unitIddevSns[i];
					if(StringUtils.isNotBlank(unitIdDevSn)){
						String[] devSns = unitIdDevSn.split("_");
						if(devSns.length>=2)
							h3cDevSnMap.put(devSns[0], devSns[1]);//devsn--unitId
					}
				}
			}
		}
		return h3cDevSnMap;
	}
	
	public static Map<String, String> getUnitIdMap(){
		if(h3cUnitIdMap.size()>0)
			return h3cUnitIdMap;
		Map<String, String> propertiesMap = getH3cPropertiesMap();
		if(propertiesMap.containsKey(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_UNITID_DEVSN)){
			String devSnStr = propertiesMap.get(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_UNITID_DEVSN);
			if(StringUtils.isNotBlank(devSnStr)){
				String[] unitIddevSns = devSnStr.split(",");
				for(int i = 0; i < unitIddevSns.length; i++){
					String unitIdDevSn = unitIddevSns[i];
					if(StringUtils.isNotBlank(unitIdDevSn)){
						String[] devSns = unitIdDevSn.split("_");
						if(devSns.length>=2)
							h3cUnitIdMap.put(devSns[1], devSns[0]);
					}
				}
			}
		}
		return h3cUnitIdMap;
	}
	
	
	public static OfficeHealthDto getHealthDto(){
		OfficeHealthDto dto = new OfficeHealthDto();
//		Map<String, Set<String>> studentCodes = new HashMap<String, Set<String>>();
//		Map<String, Student> studentMap = new HashMap<String, Student>();
		Map<String, String> h3cMap = OfficeHealthUtils.getH3cPropertiesMap();
//		dto.setUrl(OfficeRemoteURLConstants.H3C_GET_STUDENT_STEP_URL);
//		if(h3cMap.containsKey(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_USERNAME)){
//			dto.setUsername(h3cMap.get(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_USERNAME));
//		}
//		if(h3cMap.containsKey(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_PASSWORD)){
//			dto.setPassword(h3cMap.get(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_PASSWORD));
//		}
		if(h3cMap.containsKey(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_UNITID_DEVSN)){
			dto.setDevSnMap(OfficeHealthUtils.getDevSnMap());
			
			String devsnStr = h3cMap.get(OfficeConstants.OFFICE_HEALTH_H3C_REQUEST_UNITID_DEVSN);
			Set<String> unitIds = new HashSet<String>();
			if(StringUtils.isNotBlank(devsnStr)){
				String[] unitIddevSns = devsnStr.split(",");
				for(int i = 0; i < unitIddevSns.length; i++){
					String unitIdDevSn = unitIddevSns[i];
					if(StringUtils.isNotBlank(unitIdDevSn)){
						String[] devSns = unitIdDevSn.split("_");
						if(devSns.length>=2)
							unitIds.add(devSns[1]);
					}
				}
			}
//			if(unitIds.size()==0){
//				return dto;
//			}else{
//				String result = getStudentRemoteService().findBySchoolIdIn(null, unitIds.toArray(new String[0]));
//				List<Student> stulist = SUtils.dt(result, new TypeReference<List<Student>>() {
//	            });
//				
//				Iterator<Student> iterator = stulist.iterator();
//				while(iterator.hasNext()){
//					Student stu = iterator.next();
//					if(StringUtils.isNotBlank(stu.getStudentCode())){
//						studentMap.put(stu.getSchoolId()+"_"+stu.getStudentCode(), stu);
//						if(studentCodes.containsKey(stu.getSchoolId())){
//							studentCodes.get(stu.getSchoolId()).add(stu.getStudentCode());
//						}else{
//							Set<String> set = new HashSet<String>();
//							set.add(stu.getStudentCode());
//							studentCodes.put(stu.getSchoolId(), set);
//						}
//					}
//				}
//			}
//			dto.setStudentCodes(studentCodes);
//			dto.setStudentMap(studentMap);
		}
		return dto;
	}
	
	/**
	 * 获取华三健康数据--步数、里程、卡路里等信息
	 * 只需传参数"beginDate"，无需携带"endDate"，若传当日时间，则代表获取的当日到获取时刻的步数累计值。
	 * @param devSn 设备序列号
	 * @param studentCodes
	 * @param beginDate 
	 * @param endDate 
	 * @return
	 */
	public static List<OfficeHealthInfoDto> getHealthInfos(String devSn){
		
		String url = OfficeRemoteURLConstants.H3C_GET_STUDENT_STEP_URL;
		
		String[] devSnSp = devSn.split("-");
		String scenarioId = devSnSp[0];
		String schoolId = devSnSp[1];
		String apikey = devSnSp[2];
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("scenarioId", scenarioId));
		param.add(new BasicNameValuePair("schoolId", schoolId));
		param.add(new BasicNameValuePair("pageSize", pageSize+""));
		param.add(new BasicNameValuePair("pageNum", "1"));
		String content = OfficeUtils.httpGetAuth(url, apikey,param);
		
		List<OfficeHealthInfoDto> list = new ArrayList<OfficeHealthInfoDto>();
		JSONObject retJson = JSONObject.parseObject(content);
		if(retJson==null){
			log.error("华三健康手环 定时任务 历史数据：没有返回内容   "+df.format(new Date()));
			return list;
		}
		int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
		if(code == 0){
			if(retJson.containsKey("data")){
				JSONObject data = retJson.getJSONObject("data");
				int totalCount = data.containsKey("totalCount")?data.getIntValue("totalCount"):0;
				if(data.containsKey("studentArr")){
					JSONArray stuList = data.getJSONArray("studentArr");
					if(stuList!=null && stuList.size()>0){
						OfficeHealthDevice device = getOfficeHealthDeviceService().findBySerialNumber(devSn);
						String unitId = "";
						if(device!=null){
							unitId = device.getUnitId();
						}else{
							return list;
						}
						//获取学生信息
						List<Student> stulist = SUtils.dt(getStudentRemoteService().findBySchoolId(unitId),new TR<List<Student>>() {});
						Map<String, Student> stuMap = new HashMap<String, Student>();
						Iterator<Student> stuIterator = stulist.iterator();
						while(stuIterator.hasNext()){
							Student stu = (Student) stuIterator.next();
							stuMap.put(stu.getStudentCode(), stu);
						}
						setDtoValue(stuMap, list, stuList, unitId);
						if(totalCount>pageSize){
							int numPage = (totalCount-1)/pageSize;
							for(int i=0;i<numPage;i++){
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								setPageData(scenarioId,schoolId,i+2,stuMap, list, unitId,apikey);
							}
						}
					}
				}
				
			}
		}
		return list;
	}
private static void setPageData(String scenarioId,String schoolId,int pageNum,Map<String, Student> stuMap,List<OfficeHealthInfoDto> list,String unitId,String apikey) {
	String url = OfficeRemoteURLConstants.H3C_GET_STUDENT_STEP_URL;
	List<NameValuePair> param = new ArrayList<NameValuePair>();
	param.add(new BasicNameValuePair("scenarioId", scenarioId));
	param.add(new BasicNameValuePair("schoolId", schoolId));
	param.add(new BasicNameValuePair("pageSize", pageSize+""));
	param.add(new BasicNameValuePair("pageNum", pageNum+""));
	String content = OfficeUtils.httpGetAuth(url, apikey,param);
	
	JSONObject retJson = JSONObject.parseObject(content);
	if(retJson==null){
		log.error("华三健康手环 定时任务 历史数据：没有返回内容   "+df.format(new Date()));
		return ;
	}
	int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
	if(code == 0){
		if(retJson.containsKey("data")){
			JSONObject data = retJson.getJSONObject("data");
			if(data.containsKey("studentArr")){
				JSONArray stuList = data.getJSONArray("studentArr");
				if(stuList!=null && stuList.size()>0){
					setDtoValue(stuMap, list, stuList, unitId);
				}
			}
			
		}
	}
}

/**
 * 获取单位下教室列表
 * @param devSn
 * @return
 */
public static List<ClassRoomDto> getClassroomIdList(String devSn){
		List<ClassRoomDto> roomDtos = Lists.newArrayList();
		String url = OfficeRemoteURLConstants.H3C_GET_CLASSROOM_LIST_URL;
		String[] devSnSp = devSn.split("-");
		String scenarioId = devSnSp[0];
		String schoolId = devSnSp[1];
		String apikey = devSnSp[2];
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("scenarioId", scenarioId));
		param.add(new BasicNameValuePair("schoolId", schoolId));
		String content = OfficeUtils.httpGetAuth(url, apikey,param);
		
		List<ClassRoomDto> list = new ArrayList<ClassRoomDto>();
		JSONObject retJson = JSONObject.parseObject(content);
		if(retJson==null){
			log.error("华三健康手环获取教室列表：没有返回内容   "+df.format(new Date()));
			return list;
		}
		int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
		if(code == 0){
			if(retJson.containsKey("data")){
				JSONArray stuList = retJson.getJSONArray("data");
				if(stuList!=null && stuList.size()>0){
					roomDtos = SUtils.dt(stuList.toJSONString(), ClassRoomDto.class);
				}
			}
		}
		return roomDtos;
	}


	public static Map<String,String> getStudentinClassroom(String devSn,String classroomId,String classroomName,String[] studentCodes){
		Map<String,String> inClassMap = Maps.newHashMap();
		String url = OfficeRemoteURLConstants.H3C_GET_STUDENT_IN_CLASSROOM_URL;
		
		String[] devSnSp = devSn.split("-");
		String scenarioId = devSnSp[0];
		String schoolId = devSnSp[1];
		String apikey = devSnSp[2];
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("scenarioId", scenarioId));
		param.add(new BasicNameValuePair("schoolId", schoolId));
		param.add(new BasicNameValuePair("classRoomId", classroomId));
		param.add(new BasicNameValuePair("classRoomName", classroomName));
		for(String code:studentCodes){
			param.add(new BasicNameValuePair("studentIdList[]", code));
		}
		
		String content = OfficeUtils.httpGetAuth(url,apikey,param);
		
		JSONObject retJson = JSONObject.parseObject(content);
		if(retJson==null){
			log.error("华三手环取学生是否在教室：没有返回内容   "+df.format(new Date()));
			return inClassMap;
		}
		int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
		if(code == 0){
			if(retJson.containsKey("data")){
				JSONObject data = retJson.getJSONObject("data");
				if(data.containsKey("studentStaArr")){
					JSONArray stuList = data.getJSONArray("studentStaArr");
					if(stuList!=null && stuList.size()>0){
						for(int i = 0; i < stuList.size(); i++){
							JSONObject stuJson = stuList.getJSONObject(i);
							if(stuJson.containsKey("studentId")){
								String studentCode = stuJson.getString("studentId");//学号
								String inClassroom = stuJson.getString("inClassroom");//1在教室，0不在教室
								if(StringUtils.isNotBlank(studentCode)){
									inClassMap.put(studentCode, inClassroom);
								}
							}
						}
					}
				}
			}
		}
		return inClassMap;
	}
	public static StudentRemoteService getStudentRemoteService(){
		return Evn.getBean("studentRemoteService");
    }
	
	/**
	 * 转换为健康数据
	 * @return
	 */
	public static List<OfficeHealthData> convertData(DeviceDataCollect data,String account){
		List<OfficeHealthData> healthDatas = Lists.newArrayList();
  	    for (Device item : data.getDeviceLst()) {
	  	      for (DeviceData dataitem : item.getDataLst()) {
	  	    	OfficeHealthData healthData = new OfficeHealthData();
	  	    	healthData.setId(UuidUtils.generateUuid());
	  	    	healthData.setSerialNumber(account);
	  	    	healthData.setWristbandId(item.getDeviceID());
	  	    	healthData.setType(getType(dataitem.getType()));
	  	    	healthData.setDataValue(dataitem.getValue());
	  	    	healthData.setUploadTime(getUplaodTime(item.getDate(),dataitem.getTime()));
	  	    	healthData.setCreateTime(new Date());
	  	    	healthDatas.add(healthData);
	  	      }
  	    }
		return healthDatas;
	}
	
	private static Date getUplaodTime(String date, String time) {
		if(StringUtils.isBlank(date) || StringUtils.isBlank(time)){
			return null;
		}
		return DateUtils.string2DateTime(date.trim()+" "+time.trim());
	}
	
	private static String getType(int value) {
		return Integer.toHexString(value);
	}
	
	/**
	 * 推送进出校信息到微课
	 * @param array
	 */
	public static void sendInOutDataToWeiKe(JSONArray array){
		Map<String,String> parmMap = new HashMap<>();
		parmMap.put("param", array.toString());
		try {
			String wkHeathUrl = getSystemIniRemoteService().findValue("WK.HEALTH.URL");
			if(StringUtils.isNotBlank(wkHeathUrl)){
				UrlUtils.post(wkHeathUrl, parmMap);
				//不能直接调用其他AP的工具类
//				EccUtils.sendHttpPost(wkHeathUrl, parmMap);
			}else{
				log.error("未配置微课健康数据推送url");
			}
		} catch (Exception e) {
			log.error("推送微课失败："+e.getMessage());
			e.printStackTrace();
		}
	}
	public static void setDtoValue(Map<String, Student> stuMap,List<OfficeHealthInfoDto> list,JSONArray stuList,String unitId){
		//组织步数等数据信息
		for(int i = 0; i < stuList.size(); i++){
			JSONObject stuJson = stuList.getJSONObject(i);
			if(stuJson.containsKey("studentId") && stuJson.containsKey("motionDetail")){
				String studentCode = stuJson.getString("studentId");//学号
				JSONObject sjson = stuJson.getJSONObject("motionDetail");
				if(StringUtils.isNotBlank(studentCode) && sjson.size()>0){
					Student stu = null;
					if(stuMap.containsKey(studentCode)){
						stu = stuMap.get(studentCode);
					}
					if(stu==null){
						continue;
					}
					int step = sjson.containsKey("step")?sjson.getIntValue("step"):0;
					double distance = sjson.containsKey("distance")?sjson.getDoubleValue("distance"):0;
					double calorie = sjson.containsKey("calorie")?sjson.getDoubleValue("calorie"):0;
					
					OfficeHealthInfoDto hc = new OfficeHealthInfoDto();
					Date recordDate = null;
					String recordDateStr = h3cStepDf.format(new Date());
					if(StringUtils.isNotBlank(recordDateStr)){
						try {
							recordDate = h3cStepDf.parse(recordDateStr);
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
					hc.setUnitId(unitId);
					hc.setStudentId(stu.getId());
					hc.setStudentCode(studentCode);
					hc.setDate(recordDate);
					hc.setStep(step);
					hc.setCalorie(calorie);
					hc.setDistance(OfficeUtils.formatDouble2(distance,2));
					hc.setSourceType(OfficeConstants.HEALTH_DATA_H3C);
					list.add(hc);
				}
			}
		}
	}
	
	public static void getStep(String devSn,  String stuCode) {
		String url = H3C_GET_STUDENT_STEP_URL;
		String[] devSnSp = devSn.split("-");
		String scenarioId = devSnSp[0];
		String schoolId = devSnSp[1];
		String apikey = devSnSp[2];
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("scenarioId", scenarioId));
		param.add(new BasicNameValuePair("schoolId", schoolId));
		param.add(new BasicNameValuePair("studentId", stuCode));
		param.add(new BasicNameValuePair("pageSize", "1"));
		param.add(new BasicNameValuePair("pageNum", "1"));
		String content = OfficeUtils.httpGetAuth(url,apikey,param);
		
		JSONObject retJson = JSONObject.parseObject(content);
		System.out.println(retJson);
		if(retJson!=null){
			int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
			if(code == 0){
				if(retJson.containsKey("data")){
					JSONObject data = retJson.getJSONObject("data");
					if(data.containsKey("studentArr")){
						JSONArray stuList = data.getJSONArray("studentArr");
						if(stuList!=null && stuList.size()>0){
							JSONObject stuJson = stuList.getJSONObject(0);
							if(stuJson.containsKey("studentId") && stuJson.containsKey("motionDetail")){
								String studentCode = stuJson.getString("studentId");//学号
								JSONObject sjson = stuJson.getJSONObject("motionDetail");
								if(StringUtils.isNotBlank(studentCode) && sjson.size()>0){
									int step = sjson.containsKey("step")?sjson.getIntValue("step"):0;
									double distance = sjson.containsKey("distance")?sjson.getDoubleValue("distance"):0;
									double calorie = sjson.containsKey("calorie")?sjson.getDoubleValue("calorie"):0;
								}
							}
						}
					}
				}
			}
		}
		
	}
	public static void main(String[] args) {
//		getClassroomIdList("652945-1-2101caa11b834ec09826ffa0516b3504");
//		getClassroomIdList("801034-1-daca6d584fa64a1e8bdecd9594131df9");
		List<OfficeHealthInfoDto> list = getHealthInfos("801034-1-daca6d584fa64a1e8bdecd9594131df9");
		System.out.println(Json.toJSONString(list));
//		getStep("801034-1-daca6d584fa64a1e8bdecd9594131df9","0820180107");
//		Map<String,String> map = getStudentinClassroom("801034-1-daca6d584fa64a1e8bdecd9594131df9", "522","1-1-1年班", new String[]{"000001","000002","000003","000004","000005","000006"});
		
//		System.out.println(Json.toJSONString(map));
//		getStudentinClassroom("652945-1-2101caa11b834ec09826ffa0516b3504", "4","教室2-305", new String[]{"000001","000002","000003","000004","000005","000006"});
//		List<OfficeHealthInfoDto> list = getHealthInfos("801034-1");
//
//		System.out.println(JSON.toJSONString(list));
//		System.out.println(list.size());
		//		getClassroomIdList("354941-1");
//		String startDate = DateUtils.date2String(DateUtils.addMinute(new Date(), -2), "yyyy/MM/dd HH:MM");
//		String endDate = DateUtils.date2String(new Date(), "yyyy/MM/dd HH:MM");
//		getStudentinClassroom("640627-1", "6", new String[]{"1006","1007","1008"});
	}
}
