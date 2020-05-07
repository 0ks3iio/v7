package net.zdsoft.stuwork.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dao.DyNightSchedulingDao;
import net.zdsoft.stuwork.data.entity.DyNightScheduling;
import net.zdsoft.stuwork.data.service.DyNightSchedulingService;

/**
 * @author yangsj  2017年11月30日下午3:00:50
 */
@Service
public class DyNightSchedulingServiceImpl extends BaseServiceImpl<DyNightScheduling, String> implements DyNightSchedulingService{
    
	@Autowired
	private DyNightSchedulingDao dyNightSchedulingDao;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<DyNightScheduling, String> getJpaDao() {
		// TODO Auto-generated method stub
		return dyNightSchedulingDao;
	}

	@Override
	protected Class<DyNightScheduling> getEntityClass() {
		// TODO Auto-generated method stub
		return DyNightScheduling.class;
	}

	@Override
	public List<DyNightScheduling> findByUserAndNightTime(String unitId, String teacherId, Date queryDate) {
		// TODO Auto-generated method stub
		return dyNightSchedulingDao.findByUserAndNightTime(unitId,teacherId,queryDate);
	}

	@Override
	public String doImport(String unitId, List<String[]> datas) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		String[] errorData=null;
		
		List<User> userList = SUtils.dt(userRemoteService.findByOwnerType(new String[]{unitId},User.OWNER_TYPE_TEACHER), new TR<List<User>>(){});
		Map<String,User> userMap = EntityUtils.getMap(userList, "username");
		
		List<Clazz> classList = Clazz.dt(classRemoteService.findBySchoolId(unitId));
		
		//查找出当前单位所有的排班信息
		List<DyNightScheduling> listDNS1 = findByUnitId(unitId);
		Map<Date, List<DyNightScheduling>> nigthTimeMap = EntityUtils.getListMap(listDNS1, "nightTime", null);
		Map<String, List<DyNightScheduling>> classIdMap = EntityUtils.getListMap(listDNS1, "classId", null);
		
		List<DyNightScheduling> listDNS = Lists.newArrayList();
		Map<String,String> dateExictMap = new HashMap<String, String>();
		Map<String,Boolean> isSuccessMap = new HashMap<String, Boolean>(); 
		for(int i =0;i< datas.size();i++){
			isSuccessMap.put(i+1+"", false);
			String[] dataArr = datas.get(i);
			int length=dataArr.length;
			if(length < 0 || StringUtils.isBlank(dataArr[0])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="值班日期";
				errorData[2]="";
				errorData[3]="值班日期不能为空";
				errorDataList.add(errorData);
				continue;
			}
			Date nightTime = null;
			try {
				nightTime = DateUtils.parseDate(dataArr[0].trim(), "yyyy-MM-dd");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="值班日期";
				errorData[2]=dataArr[0];
				errorData[3]="值班日期格式不对";
				errorDataList.add(errorData);
				continue;
			}
			if(dateExictMap.containsKey(dataArr[0].trim()) && isSuccessMap.get(dateExictMap.get(dataArr[0].trim()))){
				errorData = new String[4];
				errorData[0]=errorDataList.size() +1 +"";
				errorData[1]="日期";
				errorData[2]=dataArr[0];
				errorData[3]="第"+(i+1)+"条的日期与第"+dateExictMap.get(dataArr[0].trim())+"条重复，以第一条数据为准";
				errorDataList.add(errorData);
				continue;
			}else{
				dateExictMap.put(dataArr[0].trim(), i+1+"");
			}
			
			for (int j = 0; j < classList.size(); j++) {
				String teacherCode=length>(j+1)?dataArr[(j+1)]:"";
				if(StringUtils.isNotBlank(teacherCode)){
					teacherCode=teacherCode.trim();
				}else{
					teacherCode="";
					continue;
				}
				User user=userMap.get(teacherCode);
				if(user==null){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="教师的用户名";
					errorData[2]=teacherCode;
					errorData[3]="该教师的用户名在本单位不存在！";
					errorDataList.add(errorData);
					break;
				}
				DyNightScheduling dyNightScheduling;
				
				if(CollectionUtils.isNotEmpty(nigthTimeMap.get(nightTime)) && CollectionUtils.isNotEmpty(classIdMap.get(classList.get(j).getId())) ) {
					List<DyNightScheduling> listDNS2 = nigthTimeMap.get(nightTime);
					Map<String,DyNightScheduling> classMap = EntityUtils.getMap(listDNS2, "classId");
					dyNightScheduling = classMap.get(classList.get(j).getId());
				}else {
					dyNightScheduling = new DyNightScheduling();
					dyNightScheduling.setId(UuidUtils.generateUuid());
					dyNightScheduling.setUnitId(unitId);
					dyNightScheduling.setClassId(classList.get(j).getId());
					dyNightScheduling.setNightTime(nightTime);
				}
				dyNightScheduling.setTeacherId(user.getOwnerId());
				listDNS.add(dyNightScheduling);
				isSuccessMap.put(i+1+"", true);
			}
		}
		if(CollectionUtils.isNotEmpty(listDNS)) {
			DyNightScheduling[] inserts = listDNS.toArray(new DyNightScheduling[0]);
			saveAll(inserts);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", datas.size()-errorDataList.size());
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

	/**
	 * @param unitId
	 * @return
	 */
	private List<DyNightScheduling> findByUnitId(String unitId) {
		// TODO Auto-generated method stub
		return dyNightSchedulingDao.findByUnitId(unitId);
	}

	@Override
	public List<DyNightScheduling> findByUnitIdAndNightTime(String unitId, Date nightTime) {
		// TODO Auto-generated method stub
		return dyNightSchedulingDao.findByUserAndNightTime(unitId,nightTime);
	}

	@Override
	public DyNightScheduling findByTeaIdAndClaIdAndTime(String unitId, String classId, Date queryDate) {
		// TODO Auto-generated method stub
		return dyNightSchedulingDao.findByTeaIdAndClaIdAndTime(unitId,classId,queryDate);
	}

	@Override
	public void deleteByNightTimeAndClazzs(Date queryDate, String[] clazzList) {
		// TODO Auto-generated method stub
		dyNightSchedulingDao.deleteByNightTimeAndClazzs(queryDate,clazzList);
	}
}
