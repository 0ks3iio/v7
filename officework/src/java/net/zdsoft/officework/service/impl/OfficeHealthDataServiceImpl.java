package net.zdsoft.officework.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dao.OfficeHealthDataDao;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthCount;
import net.zdsoft.officework.entity.OfficeHealthData;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.entity.OfficeHealthDoinoutInfo;
import net.zdsoft.officework.entity.OfficeHealthSleep;
import net.zdsoft.officework.remote.service.OfficeHealthDoinoutInfoRemoteService;
import net.zdsoft.officework.service.OfficeHealthCountService;
import net.zdsoft.officework.service.OfficeHealthDataService;
import net.zdsoft.officework.service.OfficeHealthDeviceService;
import net.zdsoft.officework.service.OfficeHealthDoinoutInfoService;
import net.zdsoft.officework.service.OfficeHealthHeartService;
import net.zdsoft.officework.service.OfficeHealthSleepService;
import net.zdsoft.officework.utils.OfficeUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service("officeHealthDataService")
public class OfficeHealthDataServiceImpl extends BaseServiceImpl<OfficeHealthData, String> implements OfficeHealthDataService{

	@Autowired
	private OfficeHealthDataDao officeHealthDataDao;
	@Autowired
	private OfficeHealthDoinoutInfoService officeHealthDoinoutInfoService;
	@Autowired
	private OfficeHealthDoinoutInfoRemoteService officeHealthDoinoutInfoRemoteService;
	@Autowired
	private OfficeHealthCountService officeHealthCountService;
	@Autowired
	private OfficeHealthDeviceService officeHealthDeviceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private OfficeHealthHeartService officeHealthHeartService;
	@Autowired
	private OfficeHealthSleepService officeHealthSleepService;
	
	@Override
	protected BaseJpaRepositoryDao<OfficeHealthData, String> getJpaDao() {
		return officeHealthDataDao;
	}

	@Override
	protected Class<OfficeHealthData> getEntityClass() {
		return OfficeHealthData.class;
	}

	@Override
	public void saveHealthData(List<OfficeHealthData> healthDatas) {
		//1.存储原始数据.
		saveAll(healthDatas.toArray(new OfficeHealthData[healthDatas.size()]));
		//2.异步对相应业务提供数据
		Thread handleRingData = new Thread(new Runnable() {
			@Override
			public void run() {
				handleServiceData(healthDatas);
			}
		},"handle-ring-data");
		handleRingData.start();
//		ThreadPoolManager.getInstance().addExecuteTask(handleRingData);
	}

	private void handleServiceData(List<OfficeHealthData> healthDatas) {
		
		List<OfficeHealthData> inOutDatas = healthDatas.stream().filter(line -> OfficeConstants.IN_OUT.equals(line.getType())).collect(Collectors.toList());
		List<OfficeHealthData> wolkDatas = healthDatas.stream().filter(line -> OfficeConstants.SPORT_WALK.equals(line.getType())).collect(Collectors.toList());
		List<OfficeHealthData> heartDatas = healthDatas.stream().filter(line -> OfficeConstants.SPORT_HEART.equals(line.getType())).collect(Collectors.toList());
		List<OfficeHealthData> sleepDatas = healthDatas.stream().filter(line -> (OfficeConstants.SPORT_NAPNEW.equals(line.getType())||OfficeConstants.SPORT_SLEEPNEW.equals(line.getType()))).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(inOutDatas))handleInOutData(inOutDatas);
		if(CollectionUtils.isNotEmpty(wolkDatas))handleWolkData(wolkDatas);
		if(CollectionUtils.isNotEmpty(heartDatas))handleHeartData(heartDatas);
		if(CollectionUtils.isNotEmpty(sleepDatas))handleSleepData(sleepDatas);
		Calendar calendar = Calendar.getInstance();
		long timeToEnd =  DateUtils.getEndDate(new Date()).getTime()-calendar.getTimeInMillis();
		int storageTime = (int) (timeToEnd/1000);//缓存时间,到当天23：59：59.999
		calendar.add(Calendar.DATE, -30);
		String dateTime = DateUtils.date2StringByDay(calendar.getTime());
		//原始数据量较大，每天删除一次30天前的01 和50，51类型记录
		if(RedisUtils.get(OfficeConstants.DELETE_OLD_DATA_REDIS_MSG+dateTime) == null){
			officeHealthDataDao.deleteOldData(dateTime);
			RedisUtils.set(OfficeConstants.DELETE_OLD_DATA_REDIS_MSG+dateTime,OfficeConstants.DELETE_OLD_DATA_REDIS_MSG+dateTime, storageTime);
		} 
		
	}
	
	/**
	 * 睡眠数据处理
	 * @param sleepDatas
	 */
	private void handleSleepData(List<OfficeHealthData> sleepDatas) {
		String schoolId ="";
		Map<String,String> teaCardMap = null;
		if (sleepDatas.get(0)!=null && StringUtils.isNotBlank(sleepDatas.get(0).getSerialNumber())){
			OfficeHealthDevice device = officeHealthDeviceService.findBySerialNumber(sleepDatas.get(0).getSerialNumber());
			if(device!=null){
				schoolId =  device.getUnitId();
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(schoolId),new TR<List<Teacher>>() {});
				teaCardMap = EntityUtils.getMap(teachers, Teacher::getCardNumber,Teacher::getId);
			}
		}
		if(StringUtils.isNotBlank(schoolId)&&teaCardMap!=null){
			sleepData(sleepDatas, teaCardMap,schoolId);
		}
	}

	private void sleepData(List<OfficeHealthData> sleepDatas,
			Map<String, String> teaCardMap, String schoolId) {
			if(RedisUtils.hasLocked(OfficeConstants.CLOCK_IN_REDIS_LOCK_PREFIX+OfficeConstants.SPORT_NAPNEW+schoolId)){//去除重复，加锁，使串行处理
				Map<String, OfficeHealthSleep> oldSleepMap = Maps.newHashMap();
				try{
					Set<String> ownerIds = Sets.newHashSet();
					Set<String> dateDays = Sets.newHashSet();
					List<OfficeHealthSleep> insertSleeps = Lists.newArrayList();
					for(OfficeHealthData data:sleepDatas){
						if(teaCardMap.containsKey(data.getWristbandId())){
							ownerIds.add(teaCardMap.get(data.getWristbandId()));
							dateDays.add(DateUtils.date2StringByDay(data.getUploadTime()));
						}
					}
					//去除重复数据
					if(CollectionUtils.isNotEmpty(ownerIds) && CollectionUtils.isNotEmpty(dateDays)){
						List<OfficeHealthSleep> sleepList = officeHealthSleepService.findByDateOwnerIds(dateDays.toArray(new String[0]), ownerIds.toArray(new String[0]));
						Iterator<OfficeHealthSleep> dataiterator = sleepList.iterator();
						while(dataiterator.hasNext()){
							OfficeHealthSleep ent = dataiterator.next();
							oldSleepMap.put(DateUtils.date2StringByDay(ent.getUploadTime())+ent.getOwnerId()+ent.getType(), ent);
						}
					}
					for(OfficeHealthData data:sleepDatas){
						if(teaCardMap.containsKey(data.getWristbandId())){
							String day = DateUtils.date2StringByDay(data.getUploadTime());
							String ownerId = teaCardMap.get(data.getWristbandId());
							if(!oldSleepMap.containsKey(day+ownerId+data.getType())){//老数据中没有，新增
								int sleepValue = 0;
								int sleepEffectValue = 0;
								if(OfficeConstants.NOT_WORN_VALUE == data.getDataValue() || data.getDataValue() == 0){
									
								}else{
									sleepEffectValue = Integer.parseInt(OfficeUtils.getHightByte(data.getDataValue()), 16)*5;
									sleepValue = OfficeUtils.getLowByteInt(data.getDataValue())*5;
								}
								OfficeHealthSleep healthSleep = new OfficeHealthSleep();
								healthSleep.setId(UuidUtils.generateUuid());
								healthSleep.setOwnerId(ownerId);
								healthSleep.setOwnerType(User.OWNER_TYPE_TEACHER+"");
								healthSleep.setSerialNumber(data.getSerialNumber());
								healthSleep.setSleepEffectValue(sleepEffectValue);
								healthSleep.setSleepValue(sleepValue);
								healthSleep.setType(data.getType());
								healthSleep.setUnitId(schoolId);
								healthSleep.setUploadTime(data.getUploadTime());
								healthSleep.setWristbandId(data.getWristbandId());
								healthSleep.setCreateTime(new Date());
								insertSleeps.add(healthSleep);
								oldSleepMap.put(day+ownerId+data.getType(), healthSleep);
							}
						}
					}
					if(CollectionUtils.isNotEmpty(insertSleeps)){
						officeHealthSleepService.saveAll(insertSleeps.toArray(new OfficeHealthSleep[insertSleeps.size()]));
					}
				}finally{
					RedisUtils.unLock(OfficeConstants.CLOCK_IN_REDIS_LOCK_PREFIX+OfficeConstants.SPORT_NAPNEW+schoolId);
				}
			}else{
				try {
					Thread.sleep(5000);//获取锁失败，5秒后重试，直到成功
				} catch (InterruptedException e) {
				}
				sleepData(sleepDatas, teaCardMap,schoolId);
			}
		
	}

	/**
	 * 心率数据处理
	 * @param heartDatas
	 */
	private void handleHeartData(List<OfficeHealthData> heartDatas) {
		//获取老师心率展示
		String schoolId ="";
		String serialNumber ="";
		Map<String,String> teaCardMap = null;
		if (heartDatas.get(0)!=null && StringUtils.isNotBlank(heartDatas.get(0).getSerialNumber())){
			serialNumber = heartDatas.get(0).getSerialNumber();
			OfficeHealthDevice device = officeHealthDeviceService.findBySerialNumber(heartDatas.get(0).getSerialNumber());
			if(device!=null){
				schoolId =  device.getUnitId();
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(schoolId),new TR<List<Teacher>>() {});
				teaCardMap = EntityUtils.getMap(teachers, Teacher::getCardNumber,Teacher::getId);
			}
		}
		if(StringUtils.isNotBlank(schoolId)&&StringUtils.isNotBlank(serialNumber)&&teaCardMap!=null){
			officeHealthHeartService.dealTeacherHeartData(schoolId,serialNumber,teaCardMap,heartDatas);
		}
		
	}
	

	/**
	 * 步数数据处理
	 * @param wolkDatas
	 */
	private void handleWolkData(List<OfficeHealthData> wolkDatas) {
		String schoolId ="";
		Map<String,String> stuCardMap = null;
		Map<String,String> teaCardMap = null;
		if (wolkDatas.get(0)!=null && StringUtils.isNotBlank(wolkDatas.get(0).getSerialNumber())){
			OfficeHealthDevice device = officeHealthDeviceService.findBySerialNumber(wolkDatas.get(0).getSerialNumber());
			if(device!=null){
				schoolId =  device.getUnitId();
				List<Student> students = SUtils.dt(studentRemoteService.findBySchoolId(schoolId),new TR<List<Student>>() {});
				stuCardMap = EntityUtils.getMap(students, Student::getCardNumber,Student::getId);
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(schoolId),new TR<List<Teacher>>() {});
				teaCardMap = EntityUtils.getMap(teachers, Teacher::getCardNumber,Teacher::getId);
			}
		}
		if(StringUtils.isNotBlank(schoolId)&&(stuCardMap!=null || teaCardMap !=null)){
			wolkDayAndHourData(wolkDatas, stuCardMap,teaCardMap,schoolId);
		}
	}
		
	//路程 = 步数 * 0.70（米）
	//跑步热量（kcal）＝体重（kg）×指数K = 体重（kg）*30*路程/24000   体重没有取50kg
	//指数K＝30÷速度（分钟/400米） 
	private void wolkDayAndHourData(List<OfficeHealthData> wolkDatas,Map<String,String> stuCardMap,Map<String,String> teaCardMap,String schoolId) {
		if(stuCardMap==null)stuCardMap = Maps.newHashMap();
		if(teaCardMap==null)teaCardMap = Maps.newHashMap();
		if(RedisUtils.hasLocked(OfficeConstants.CLOCK_IN_REDIS_LOCK_PREFIX+OfficeConstants.SPORT_WALK+schoolId)){//去除重复，加锁，使串行处理
			Map<String, OfficeHealthCount> dayCountMap = Maps.newHashMap();
			Map<String, OfficeHealthCount> hourCountMap = Maps.newHashMap();//key:date_hour
			try{
				List<OfficeHealthCount> list = Lists.newArrayList();
				Calendar calendar = Calendar.getInstance();
				Set<String> ownerIds = Sets.newHashSet();
				Set<String> dateDays = Sets.newHashSet();
				for(OfficeHealthData data:wolkDatas){
					if(stuCardMap.containsKey(data.getWristbandId())){
						ownerIds.add(stuCardMap.get(data.getWristbandId()));
						dateDays.add(DateUtils.date2StringByDay(data.getUploadTime()));
					}
					if(teaCardMap.containsKey(data.getWristbandId())){
						ownerIds.add(teaCardMap.get(data.getWristbandId()));
						dateDays.add(DateUtils.date2StringByDay(data.getUploadTime()));
					}
				}
				//去除重复数据
				if(CollectionUtils.isNotEmpty(ownerIds) && CollectionUtils.isNotEmpty(dateDays)){
					List<OfficeHealthCount> countList = officeHealthCountService.findByDateStuIds(dateDays.toArray(new String[0]), ownerIds.toArray(new String[0]));
					Iterator<OfficeHealthCount> dataiterator = countList.iterator();
					while(dataiterator.hasNext()){
						OfficeHealthCount ent = dataiterator.next();
						if(OfficeConstants.OFFICE_HEALTH_COUNT_TYPE_1 == ent.getType()){
							dayCountMap.put(DateUtils.date2StringByDay(ent.getDate())+ent.getOwnerId(), ent);
						}else{
							hourCountMap.put(DateUtils.date2StringByDay(ent.getDate())+"_"+ent.getHour()+ent.getOwnerId(), ent);//key:date_hour
						}
					}
				}
				for(OfficeHealthData data:wolkDatas){
					String ownerId = "";
					String ownerType = User.OWNER_TYPE_STUDENT+"";
					if(teaCardMap.containsKey(data.getWristbandId())){
						ownerId = teaCardMap.get(data.getWristbandId());
						ownerType = User.OWNER_TYPE_TEACHER+"";
					}else if(stuCardMap.containsKey(data.getWristbandId())){
						ownerId = stuCardMap.get(data.getWristbandId());
					}
					if(StringUtils.isNotBlank(ownerId)){
						calendar.setTime(data.getUploadTime());
						int hour = calendar.get(Calendar.HOUR_OF_DAY);
						//如果hour为凌晨  则说明nowtime为前一天的日期, 查询的数据则为前一天一整天的运动情况
						if(hour==0){
							calendar.add(Calendar.DATE, -1);
							data.setUploadTime(calendar.getTime());
							hour = 23;
							calendar.add(Calendar.DATE, 1);
						}else{
							//hour不为0  则说明nowtime为当天的日期  如：01：05分  对应的应该是0点的数据，02：05分对应的应该1点的数据 需要-1
							hour--;
						}
						String day = DateUtils.date2StringByDay(data.getUploadTime());
						int step = data.getDataValue();
						double distance = step * 0.70;
						double calorie = 50 * 30 * distance / 24000;
						BigDecimal b = new BigDecimal(calorie);   
						calorie = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
						boolean isAddHourData = true;
						if(!hourCountMap.containsKey(day+"_"+hour+ownerId)){
							OfficeHealthCount count = new OfficeHealthCount();
							count.setId(UuidUtils.generateUuid());
							count.setUnitId(schoolId);
							count.setCalorie(calorie);
							count.setDate(DateUtils.string2Date(DateUtils.date2StringByDay(data.getUploadTime())));
							count.setDistance(distance);
							count.setHour(hour);
							count.setSourceType(OfficeConstants.HEALTH_DATA_TB);
							count.setStep(step);
							count.setOwnerId(ownerId);
							count.setOwnerType(ownerType);
							count.setType(OfficeConstants.OFFICE_HEALTH_COUNT_TYPE_2);
							count.setCreationTime(new Date());
							hourCountMap.put(day+"_"+hour+ownerId, count);
						}else{
							isAddHourData = false;
						}
						if(!dayCountMap.containsKey(day+ownerId)&&isAddHourData){
							OfficeHealthCount dayCount = new OfficeHealthCount();
							dayCount.setId(UuidUtils.generateUuid());
							dayCount.setUnitId(schoolId);
							dayCount.setCalorie(calorie);
							dayCount.setDate(DateUtils.string2Date(DateUtils.date2StringByDay(data.getUploadTime())));
							dayCount.setDistance(distance);
							dayCount.setSourceType(OfficeConstants.HEALTH_DATA_TB);
							dayCount.setStep(step);
							dayCount.setOwnerId(ownerId);
							dayCount.setOwnerType(ownerType);
							dayCount.setType(OfficeConstants.OFFICE_HEALTH_COUNT_TYPE_1);
							dayCount.setCreationTime(new Date());
							dayCountMap.put(day+ownerId, dayCount);
						}else{
							if(isAddHourData){
								OfficeHealthCount dayCount = dayCountMap.get(day+ownerId);
								dayCount.setCalorie(dayCount.getCalorie()+calorie);
								dayCount.setStep(dayCount.getStep()+step);
								dayCount.setDistance(dayCount.getDistance()+distance);
								dayCountMap.put(day+ownerId, dayCount);
							}
						}
					}
				}
				list.addAll(hourCountMap.values());
				list.addAll(dayCountMap.values());
				officeHealthCountService.updateBatch(list);
			}finally{
				RedisUtils.unLock(OfficeConstants.CLOCK_IN_REDIS_LOCK_PREFIX+OfficeConstants.SPORT_WALK+schoolId);
			}
		}else{
			try {
				Thread.sleep(5000);//获取锁失败，5秒后重试，直到成功
			} catch (InterruptedException e) {
			}
			wolkDayAndHourData(wolkDatas, stuCardMap,teaCardMap,schoolId);
		}
	}
	
	
	
	/**
	 * 进出校数据处理
	 * @param inOutDatas
	 */
	private void handleInOutData(List<OfficeHealthData> inOutDatas) {
		List<OfficeHealthDoinoutInfo> list = new ArrayList<>();
		String schoolId = "";
		Map<String,Student> stuCardMap = null;
		if (inOutDatas.get(0)!=null && StringUtils.isNotBlank(inOutDatas.get(0).getSerialNumber())){
			OfficeHealthDevice device = officeHealthDeviceService.findBySerialNumber(inOutDatas.get(0).getSerialNumber());
			if(device!=null){
				schoolId =  device.getUnitId();
				stuCardMap = getSchoolCardMap(schoolId);
			}
		}
		Map<String, OfficeHealthInfoDto> stepMap = Maps.newHashMap();
		Set<String> stuIds = Sets.newHashSet();
		if(stuCardMap!=null){
			for(OfficeHealthData data:inOutDatas){
				if(stuCardMap.containsKey(data.getWristbandId())){
					Student student = stuCardMap.get(data.getWristbandId());
					if(student == null){
						continue;
					}
//					stuIds.add(student.getId());
					OfficeHealthDoinoutInfo doinoutInfo = new OfficeHealthDoinoutInfo();
					if(1==data.getDataValue()){
						doinoutInfo.setInOut(0);
					}else{
						doinoutInfo.setInOut(1);
					}
					doinoutInfo.setInOutTime(data.getUploadTime());
					doinoutInfo.setSourceType(OfficeConstants.HEALTH_DATA_TB);
					doinoutInfo.setStudentCode(student.getStudentCode());
					doinoutInfo.setStudentId(student.getId());
					doinoutInfo.setUnitId(schoolId);
					doinoutInfo.setWristbandId(data.getWristbandId());
					list.add(doinoutInfo);
				}
			}
		}
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
		}
		try {
			officeHealthDoinoutInfoRemoteService.dealInOutData(list, stepMap);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("天波健康手环进出校：业务处理异常");
		}
	}

	/**
	 * 2分钟获取一次
	 * @param schoolId
	 * @return
	 */
	public Map<String,Student> getSchoolCardMap(String schoolId) {
		 return RedisUtils.getObject("officework.data." + schoolId + ".map", 2*60, new TypeReference<Map<String,Student>>() {
	        }, new RedisInterface<Map<String,Student>>() {
	            @Override
	            public Map<String,Student> queryData() {
	            	List<Student> students = SUtils.dt(studentRemoteService.findBySchoolId(schoolId),new TR<List<Student>>() {});
	            	return EntityUtils.getMap(students, Student::getCardNumber);
	            }

	        });
	}
}
