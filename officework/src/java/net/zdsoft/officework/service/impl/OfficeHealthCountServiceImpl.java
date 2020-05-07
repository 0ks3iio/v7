package net.zdsoft.officework.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dao.OfficeHealthCountDao;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthCount;
import net.zdsoft.officework.service.OfficeHealthCountService;
/**
 * office_health_count
 * @author 
 * 
 */
@Service("officeHealthCountService")
public class OfficeHealthCountServiceImpl implements OfficeHealthCountService{
	
	@Autowired
	private OfficeHealthCountDao officeHealthCountDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UserRemoteService  userRemoteService;
	
	public void saveOrUpdate(List<OfficeHealthInfoDto> list, Date nowtime){
		if(list==null || list.size()==0)
			return;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Iterator<OfficeHealthInfoDto> iterator = list.iterator();
		Set<String> stuIds = new HashSet<String>();
		while(iterator.hasNext()){
			OfficeHealthInfoDto ent = iterator.next();
			stuIds.add(ent.getStudentId());
		}
		
		List<OfficeHealthCount> countList = officeHealthCountDao.findByDateOwnerIds(new String[]{df.format(nowtime)}, stuIds.toArray(new String[0]));
		Map<String, OfficeHealthCount> dayCountMap = new HashMap<String, OfficeHealthCount>();
		Map<String, OfficeHealthCount> hourCountMap = new HashMap<String, OfficeHealthCount>();//key:date_hour
		Iterator<OfficeHealthCount> dataiterator = countList.iterator();
		while(dataiterator.hasNext()){
			OfficeHealthCount ent = dataiterator.next();
			if(OfficeConstants.OFFICE_HEALTH_COUNT_TYPE_1 == ent.getType()){
				dayCountMap.put(df.format(ent.getDate())+ent.getOwnerId(), ent);
			}else{
				hourCountMap.put(df.format(ent.getDate())+"_"+ent.getHour()+ent.getOwnerId(), ent);//key:date_hour
			}
		}
		
		List<OfficeHealthCount> insertList = new ArrayList<OfficeHealthCount>();
		List<OfficeHealthCount> updateList = new ArrayList<OfficeHealthCount>();
		
		Iterator<OfficeHealthInfoDto> infoIterator = list.iterator();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowtime);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		//如果hour为凌晨  则说明nowtime为前一天的日期, 查询的数据则为前一天一整天的运动情况
		if(hour==0){
			hour = 23;
		}else{
			//hour不为0  则说明nowtime为当天的日期  如：01：05分  对应的应该是0点的数据，02：05分对应的应该1点的数据 需要-1
			hour--;
		}
		String checkDateStr = df.format(calendar.getTime());
		String hourKey = checkDateStr+"_"+hour;
		
		System.out.println("hourKey="+hourKey);
		String ownerType = User.OWNER_TYPE_STUDENT+"";
		
		Date checkDate = null;
		/*
		 * 定时任务每整点获取运动情况,然后更新当天的统计数据以及新增此时的小时运动统计
		 * 每一个小时的运动量=此时的运动量-当天的运动量（上一个小时）
		 */
		while(infoIterator.hasNext()){
			OfficeHealthCount daycount = new OfficeHealthCount(); 
			OfficeHealthCount hourcount = new OfficeHealthCount();;
			
			OfficeHealthInfoDto ent = infoIterator.next();
			try {
				checkDate = df.parse(checkDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			/*
			 * 统计一天的数据
			 * 每一小时获取到的数据 会自动更新成当天的数据
			 */
			boolean isDayData = true;
			OfficeHealthCount daycountOld =null;
			if(dayCountMap.containsKey(checkDateStr+ent.getStudentId())){
				daycountOld = dayCountMap.get(checkDateStr+ent.getStudentId());
				daycount.setId(daycountOld.getId());
				daycount.setCreationTime(new Date());
				daycount.setCalorie(daycountOld.getCalorie());
				daycount.setDistance(daycountOld.getDistance());
				daycount.setStep(daycountOld.getStep());
				updateList.add(daycount);
			}
			
			if(daycountOld==null){
				isDayData = false;
				insertList.add(daycount);
			}
			
			//小时
			OfficeHealthCount hourcountOld = null;
			if(hourCountMap.containsKey(hourKey+ent.getStudentId())){
				hourcountOld = hourCountMap.get(hourKey+ent.getStudentId());
				hourcount = new OfficeHealthCount();
				hourcount.setId(hourcountOld.getId());
				hourcount.setCreationTime(new Date());
				updateList.add(hourcount);
			}
			if(hourcountOld==null){
				insertList.add(hourcount);
			}
			
			
			//跟上一个小时的记录做比较 即上一个小时当天的记录
			hourcount.setUnitId(ent.getUnitId());
			hourcount.setOwnerId(ent.getStudentId());
			hourcount.setType(OfficeConstants.OFFICE_HEALTH_COUNT_TYPE_2);
			hourcount.setDate(checkDate);
			hourcount.setHour(hour);
			hourcount.setSourceType(ent.getSourceType());
			hourcount.setOwnerType(ownerType);
			if(isDayData){
				double calorie = (ent.getCalorie()==null?0:ent.getCalorie())-(daycount.getCalorie()==null?0:daycount.getCalorie());
				double distance = (ent.getDistance()==null?0:ent.getDistance())-(daycount.getDistance()==null?0:daycount.getDistance());
				int step = (ent.getStep()==null?0:ent.getStep())-(daycount.getStep()==null?0:daycount.getStep());
				
				hourcount.setCalorie(calorie>0?calorie:0);
				hourcount.setDistance(distance>0?distance:0);
				hourcount.setStep(step>0?step:0);
			}else{
				hourcount.setCalorie(ent.getCalorie());
				hourcount.setDistance(ent.getDistance());
				hourcount.setStep(ent.getStep());
			}
			
			//这段逻辑一定要在hour之后,因为hour的计算使用了上一个小时的daycount
			daycount.setUnitId(ent.getUnitId());
			daycount.setOwnerId(ent.getStudentId());
			if((ent.getCalorie()==null?0:ent.getCalorie())>=(daycount.getCalorie()==null?0:daycount.getCalorie())){
				daycount.setCalorie(ent.getCalorie());
			}
			if((ent.getDistance()==null?0:ent.getDistance())>=(daycount.getDistance()==null?0:daycount.getDistance())){
				daycount.setDistance(ent.getDistance());
			}
			if((ent.getStep()==null?0:ent.getStep())>=(daycount.getStep()==null?0:daycount.getStep())){
				daycount.setStep(ent.getStep());
			}
				
			daycount.setType(OfficeConstants.OFFICE_HEALTH_COUNT_TYPE_1);
			daycount.setDate(checkDate);
			daycount.setSourceType(ent.getSourceType());
			daycount.setOwnerType(ownerType);
		
			
		}
		if(insertList.size()>0)
			saveBatch(insertList);
		if(updateList.size()>0)
			updateBatch(updateList);
	}
	
	@Override
	public void saveBatch(List<OfficeHealthCount> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				OfficeHealthCount ent  = list.get(i);
				if(StringUtils.isBlank(ent.getId())){
					ent.setId(UuidUtils.generateUuid());
				}
				if(ent.getCreationTime()==null){
					ent.setCreationTime(new Date());
				}
			}
			officeHealthCountDao.saveAll(list);
		}

	}
	
	@Override
	public void updateBatch(List<OfficeHealthCount> list){
		if (CollectionUtils.isNotEmpty(list)) {
			officeHealthCountDao.saveAll(list);
		}
	}
	
	@Override
	public Integer getRankByStudentId(Student student,String dateType,String startTime,String endTime){
		List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(student.getClassId()), new TR<List<Student>>(){});
		Set<String> studentIds=new HashSet<String>();
		//获取所有该班级下的学生id
		if(CollectionUtils.isNotEmpty(studentList)){
			for(Student stu:studentList){
				studentIds.add(stu.getId());
			}
		}
		List<OfficeHealthCount> healthList;
		if (OfficeConstants.HEALTH_DATE_DAY.equals(dateType)) {
			healthList=officeHealthCountDao.getOfficeHealthCountByOwnerId(OfficeConstants.HEALTH_TYPE_HOURS,startTime,new ArrayList<String>(studentIds).toArray(new String[0]));
		} else {
			healthList=officeHealthCountDao.getOfficeHealthCountByOwnerId(OfficeConstants.HEALTH_TYPE_DAY,startTime,endTime,new ArrayList<String>(studentIds).toArray(new String[0]));
		}
		Map<String,Integer> healthMap = Maps.newHashMap(); 
		if(CollectionUtils.isNotEmpty(healthList)){
			for (OfficeHealthCount count : healthList) {
				if (healthMap.containsKey(count.getOwnerId())) {
					healthMap.put(count.getOwnerId(), healthMap.get(count.getOwnerId()) + count.getStep());
				} else {
					healthMap.put(count.getOwnerId(), count.getStep());
				}
			}
		}
		List<Map.Entry<String,Integer>> healthMapList = new ArrayList<Map.Entry<String,Integer>>(healthMap.entrySet());
		Collections.sort(healthMapList,new Comparator<Map.Entry<String,Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1,Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});
		Map<String,Integer> healthRankMap = Maps.newHashMap();
		Integer nowstpe = 0;
		int ranking = 1;
		if (CollectionUtils.isNotEmpty(healthMapList)) {
			for (int i=1;i<=healthMapList.size();i++) {
				if (Math.abs(nowstpe - healthMapList.get(i-1).getValue()) < 0.01) {
					healthRankMap.put(healthMapList.get(i-1).getKey(), ranking);
				} else {
					healthRankMap.put(healthMapList.get(i-1).getKey(), i);
					nowstpe = healthMapList.get(i-1).getValue();
					ranking = i;
				}
			}
		}
		return healthRankMap.get(student.getId()) != null ? healthRankMap.get(student.getId()) : 0;
	}
	
	@Override
	public Map<Integer,OfficeHealthCount> getOfficeHealthCountByStudentId(String studentId,String dateType,String startDate,String endDate){
		Map<Integer,OfficeHealthCount> healthMap = Maps.newLinkedHashMap();
		//获取数据库中的数据
		List<OfficeHealthCount> healthList;
		if (OfficeConstants.HEALTH_DATE_DAY.equals(dateType)) {
			healthList=officeHealthCountDao.getOfficeHealthCountByOwnerId(OfficeConstants.HEALTH_TYPE_HOURS,startDate,new String[]{studentId});
		} else {
			healthList=officeHealthCountDao.getOfficeHealthCountByOwnerId(OfficeConstants.HEALTH_TYPE_DAY,startDate,endDate,new String[]{studentId});
		}
		Calendar calendar =Calendar.getInstance();
		if (OfficeConstants.HEALTH_DATE_DAY.equals(dateType)) {
			for (OfficeHealthCount count : healthList) {
				healthMap.put(count.getHour(), count);
			}
		} else if (OfficeConstants.HEALTH_DATE_WEEK.equals(dateType)) {
			for (OfficeHealthCount count : healthList) {
				calendar.setTime(count.getDate());
				healthMap.put(calendar.get(Calendar. DAY_OF_WEEK), count);
			}
		} else {
			for (OfficeHealthCount count : healthList) {
				calendar.setTime(count.getDate());
				healthMap.put(calendar.get(Calendar. DAY_OF_MONTH), count);
			}
		}
		return healthMap;
	}

	@Override
	public List<OfficeHealthCount> findByLastDayData(String[] studentIds) {
		return officeHealthCountDao.findByLastDayData(studentIds);
	}

	@Override
	public List<OfficeHealthCount> findByTypeDateStuIds(String[] checkDates,
			String[] studentIds, int type) {
		return officeHealthCountDao.findByTypeDateOwnerIds(checkDates,studentIds,type);
	}

	@Override
	public List<OfficeHealthCount> findByDateStuIds(String[] checkDates,
			String[] studentIds) {
		return officeHealthCountDao.findByDateOwnerIds(checkDates, studentIds);
	}
}