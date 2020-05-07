package net.zdsoft.officework.remote.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.dto.OfficeInOutAttDto;
import net.zdsoft.officework.entity.OfficeHealthCount;
import net.zdsoft.officework.entity.OfficeHealthData;
import net.zdsoft.officework.entity.OfficeHealthDoinoutInfo;
import net.zdsoft.officework.remote.service.OfficeHealthDoinoutInfoRemoteService;
import net.zdsoft.officework.service.OfficeHealthCountService;
import net.zdsoft.officework.service.OfficeHealthDataService;
import net.zdsoft.officework.service.OfficeHealthDoinoutInfoService;
import net.zdsoft.officework.utils.OfficeHealthUtils;
import net.zdsoft.officework.utils.OfficeUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service("officeHealthDoinoutInfoRemoteService")
public class OfficeHealthDoinoutInfoRemoteServiceImpl implements OfficeHealthDoinoutInfoRemoteService{
	private static final Logger log = Logger.getLogger(OfficeHealthDoinoutInfoRemoteServiceImpl.class);
	
	@Autowired
	private OfficeHealthDoinoutInfoService officeHealthDoinoutInfoService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private OfficeHealthDataService officeHealthDataService;
	@Autowired
	private OfficeHealthCountService officeHealthCountService;
	
	private static OpenApiOfficeService openApiOfficeService;
    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }
	
	private void println(String msg){
		System.out.println("华三健康手环 进出校："+msg);
		log.info(msg);
	}

	@Override
	public String doInOut(String dataStr){
		Date nowtime = new Date();
		println(dataStr + ",datetime="+nowtime.getTime());
		JSONObject retJson = new JSONObject();
		try {
			JSONObject data = null;
			if(StringUtils.isNotBlank(dataStr)){
				data = JSONObject.parseObject(dataStr);
			}
			if(data == null){
				retJson.put("code", -1);
				retJson.put("message", "推送数据格式不正确。");
				return SUtils.s(retJson);
			}
			Map<String,String> devSnMap = OfficeHealthUtils.getDevSnMap();
			if(!(devSnMap!=null && devSnMap.size()>0)){
				retJson.put("code", -1);
				retJson.put("message", "没有配置正确的设备序列号，请联系开发进行配置。");
				return SUtils.s(retJson);
			}
			
//			String devSn = data.containsKey("devSn")?data.getString("devSn"):"";
			String scenarioId = data.containsKey("scenarioId")?data.getString("scenarioId"):"";
			String schoolId = data.containsKey("schoolId")?data.getString("schoolId"):"";
			String devSn = scenarioId+"-"+schoolId;
			String unitId = "";
			if(devSnMap.containsKey(devSn)){
				unitId = devSnMap.get(devSn);
			}else{
				retJson.put("code", -1);
				retJson.put("message", "数据获取异常：返回数据没有devSn");
				return SUtils.s(retJson);
			}
			
			if(data.containsKey("studentGateInfo")){
				JSONArray array = (JSONArray) data.get("studentGateInfo");
				
				if(array.size()==0){
					retJson.put("code", -1);
					retJson.put("message", "没有合法的进出校记录");
					return SUtils.s(retJson);
				}
				
				List<OfficeHealthDoinoutInfo> list = new ArrayList<OfficeHealthDoinoutInfo>();
				
				StringBuilder msgStr = null;
				Set<String> stuCodeSet = new HashSet<String>();
				for(int i = 0; i < array.size(); i++){
					JSONObject studata = array.getJSONObject(i);
					String stuCode = studata.containsKey("studentId")?studata.getString("studentId"):"";
					String wristbandId = studata.containsKey("wristbandId")?studata.getString("wristbandId"):"";
					String inOut = studata.containsKey("inOut")?studata.getString("inOut"):"";
					
					if(StringUtils.isNotBlank(stuCode)){
						stuCodeSet.add(stuCode);
					}else{
						continue;
					}
						
					OfficeHealthDoinoutInfo info = new OfficeHealthDoinoutInfo();
					info.setStudentCode(stuCode);
					info.setInOut(Integer.valueOf(inOut));
					info.setUnitId(unitId);
					info.setWristbandId(wristbandId);
					info.setInOutTime(nowtime);
					info.setSourceType(OfficeConstants.HEALTH_DATA_H3C);
					list.add(info);
				}
				
				
				String result = studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0]));
				List<Student> stulist = SUtils.dt(result, new TypeReference<List<Student>>() {
	            });
				
				Map<String, Student> stuMap = new HashMap<String, Student>();
				Set<String> stuIds = Sets.newHashSet();
				Iterator<Student> stuIterator = stulist.iterator();
				while(stuIterator.hasNext()){
					Student stu = (Student) stuIterator.next();
					stuIds.add(stu.getId());
					stuMap.put(stu.getStudentCode(), stu);
				}
				
				Iterator<OfficeHealthDoinoutInfo> iteratorInfo = list.iterator();
				while(iteratorInfo.hasNext()){
					OfficeHealthDoinoutInfo ent = iteratorInfo.next();
					if(stuMap.containsKey(ent.getStudentCode())){
						Student stu = stuMap.get(ent.getStudentCode());
						if(stu!=null)
							ent.setStudentId(stu.getId());
						else{
							iteratorInfo.remove();
							if(msgStr==null){
								msgStr = new StringBuilder("学号："+ent.getStudentCode()+"找不到对应的数字校园信息");
							}else{
								msgStr.append(",").append("学号："+ent.getStudentCode()+"找不到对应的数字校园信息");
							}
						}
					}else{
						iteratorInfo.remove();
						if(msgStr==null){
							msgStr = new StringBuilder("学号："+ent.getStudentCode()+"找不到对应的数字校园信息");
						}else{
							msgStr.append(",").append("学号："+ent.getStudentCode()+"找不到对应的数字校园信息");
						}
					}
				}
				
				if(CollectionUtils.isEmpty(list)){
					retJson.put("code", -1);
					retJson.put("message", "没有合法的进出校记录  "+(msgStr==null?"":msgStr.toString()));
					return SUtils.s(retJson);
				}
				
				try {
					handleHeathData(list,stuIds );
				} catch (Exception e) {
					e.printStackTrace();
					//此情况华三接口推送仍然算推送成功
					System.out.println("华三健康手环进出校：业务处理异常");
				}
				
				retJson.put("code", 1);
				retJson.put("message", "推送成功。"+(msgStr==null?"":msgStr.toString()));
				return SUtils.s(retJson);
			}else{
				retJson.put("code", -1);
				retJson.put("message", "数据获取异常：返回数据没有studentGateInfo");
				return SUtils.s(retJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			retJson.put("code", -1);
			retJson.put("message", "数据获取异常："+e.getMessage());
			return SUtils.s(retJson);
		}
	}
	
	private void handleHeathData(List<OfficeHealthDoinoutInfo> list,Set<String> stuIds){
		//获取健康数据
		Map<String, OfficeHealthInfoDto> stepMap = Maps.newHashMap();
		if(CollectionUtils.isNotEmpty(stuIds)){//此处取数据库健康数据，可能健康数据会在在进校后才会由AP发送过来
			List<OfficeHealthCount> countList = officeHealthCountService.findByTypeDateStuIds(new String[]{DateUtils.date2StringByDay(new Date())}, stuIds.toArray(new String[0]), OfficeConstants.OFFICE_HEALTH_COUNT_TYPE_1);
			List<OfficeHealthInfoDto> datalist = new ArrayList<OfficeHealthInfoDto>();
			for(OfficeHealthCount count : countList){
				OfficeHealthInfoDto dto = new OfficeHealthInfoDto();
				dto.setCalorie(count.getCalorie());
				dto.setDistance(count.getDistance());
				dto.setSourceType(count.getSourceType());
				dto.setStep(count.getStep());
				dto.setStudentId(count.getOwnerId());
				datalist.add(dto);
			}
			stepMap = EntityUtils.getMap(datalist, OfficeHealthInfoDto::getStudentId);
//			sendInOutDataToWeiKe(list, stepMap);
		}
		dealInOutData(list, stepMap);
	}
	
	/**
	 * 微课推送的数据 schoolId，userId，cardNumber（手环id），ioType（进出 1.出 2.进），time（打卡时间），step，calorie，distance
	 */
	@Override
	public void sendInOutDataToWeiKe(List<OfficeHealthDoinoutInfo> list,Map<String, OfficeHealthInfoDto> stepMap){
		JSONArray array = new JSONArray();
		Iterator<OfficeHealthDoinoutInfo> iterator = list.iterator();
		while(iterator.hasNext()){
			OfficeHealthDoinoutInfo ent = iterator.next();
			OfficeHealthInfoDto dto = null;
			if(stepMap.containsKey(ent.getStudentId())){
				dto = stepMap.get(ent.getStudentId());
			}
			if(dto==null)
				dto = new OfficeHealthInfoDto();
			
			JSONObject wjson = new JSONObject();
			wjson.put("schoolId", ent.getUnitId());
			wjson.put("userId", ent.getStudentId());
			wjson.put("cardNumber", ent.getWristbandId());
			if(OfficeConstants.OFFICE_HEALTH_IN == ent.getInOut()){
				wjson.put("ioType", 2);
			}else{
				wjson.put("ioType", 1);
			}
			wjson.put("time", ent.getInOutTime().getTime());
			
			wjson.put("step", dto.getStep()==null?0:dto.getStep());
			if(dto.getCalorie()==null){
				wjson.put("calorie", 0);//微课  千卡
			}else{
				wjson.put("calorie", (int)OfficeUtils.formatDouble2(dto.getCalorie()/1000, 0));//微课  千卡
			}
			if(dto.getDistance()==null){
				wjson.put("distance", 0);//微课 公里
			}else{
				wjson.put("distance", OfficeUtils.formatDouble2(dto.getDistance()/1000, 1));//微课 公里
			}
			if(StringUtils.isBlank(ent.getPicUrl())){
				wjson.put("photoUrl", "");
			}else{
				wjson.put("photoUrl", ent.getPicUrl());
				wjson.put("tips", ent.getPicTips());
			}
			array.add(wjson);
		}
		
		OfficeHealthUtils.sendInOutDataToWeiKe(array);
	}

	@Override
	public String doTBInOut(String dataStr) {
		JSONObject retJson = new JSONObject();
		try {
			JSONArray data = null;
			if(StringUtils.isNotBlank(dataStr)){
				data = JSONArray.parseArray(dataStr);
			}
			if(data == null){
				retJson.put("code", -1);
				retJson.put("message", "推送数据格式不正确。");
				return SUtils.s(retJson);
			}
			if(data.size()==0){
				retJson.put("code", -1);
				retJson.put("message", "没有进出校记录");
				return SUtils.s(retJson);
			}
			List<OfficeHealthData> healthDatas = getTbData(data);
			if(CollectionUtils.isNotEmpty(healthDatas)){
				officeHealthDataService.saveHealthData(healthDatas);
			}
			retJson.put("code", 0);
			retJson.put("message", "保存数据成功");
			return SUtils.s(retJson);
		} catch (Exception e) {
			e.printStackTrace();
			retJson.put("code", -1);
			retJson.put("message", "数据获取异常："+e.getMessage());
			return SUtils.s(retJson);
		}
	}

	private List<OfficeHealthData> getTbData(JSONArray data) {
		List<OfficeHealthData> datas = Lists.newArrayList();
		for(int i = 0; i < data.size(); i++){
			JSONObject studata = data.getJSONObject(i);
			String deviceId = studata.containsKey("deviceId")?studata.getString("deviceId"):"";
			String cardNumber = studata.containsKey("cardNumber")?studata.getString("cardNumber"):"";
			Date recodeTime = studata.containsKey("recodeTime")?studata.getDate("recodeTime"):null;
			// 进出标识： 0为进，1为出
			String ioType = studata.containsKey("ioType")?studata.getString("ioType"):"";
			OfficeHealthData healthData = new OfficeHealthData();
			healthData.setId(UuidUtils.generateUuid());
			healthData.setSerialNumber(deviceId);
			healthData.setType(OfficeConstants.IN_OUT);
			healthData.setUploadTime(recodeTime);
			healthData.setCreateTime(new Date());
			healthData.setDataValue(Integer.valueOf(ioType));
			healthData.setWristbandId(cardNumber);
			datas.add(healthData);
		}
		return datas;
	}

	@Override
	public void dealInOutData(List<OfficeHealthDoinoutInfo> list,Map<String, OfficeHealthInfoDto> stepMap) {
		if(CollectionUtils.isNotEmpty(list)){
			//1.推送到微课
			sendInOutDataToWeiKe(list, stepMap);
			//2.推送到办公学生考勤统计
			try {
				sendToOficie(list);
			} catch (Exception e) {
				log.error("推送进出校办公失败："+e.getMessage());
				e.printStackTrace();
			}
			//3.记录数据
			officeHealthDoinoutInfoService.saveBatch(list);
		}
	}

	private void sendToOficie(List<OfficeHealthDoinoutInfo> list) {
		List<OfficeInOutAttDto> attDtos = Lists.newArrayList();
		for(OfficeHealthDoinoutInfo info:list){
			OfficeInOutAttDto attDto = new OfficeInOutAttDto();
			attDto.setAttendancenum(info.getWristbandId());
			attDto.setAttendanceTime(info.getInOutTime());
			attDto.setMark(info.getInOut()+"");
			if(Objects.equals(info.getInOut(), 0))attDto.setMachineNum(OfficeInOutAttDto.TO_OFFICE_MACHINENUM_OUT);
			attDto.setUnitId(info.getUnitId());
			attDtos.add(attDto);
		}
		String param = SUtils.s(attDtos);
		if(getOpenApiOfficeService() != null){
			getOpenApiOfficeService().sendDataToStuAttendances(param);
		}
	}
}
