package net.zdsoft.desktop.componet;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.OperationLogRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Component
@Lazy(false)
public class UserStatisticTimer {

	@Autowired
	private UnitRemoteService unitRemoteService;
	
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	
	@Autowired
	private OperationLogRemoteService operationLogRemoteService;
	

	private RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Scheduled(cron = "0 0 1 * * ?")//凌晨1点
	public void pushProductionServerInfo() {
		RedisUtils.get("use-statistic-unit-timer-v7", 82800, () -> {//学校数
			try {
				List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnitClass(2), Unit.class);
				return unitList.size()+"";
			} catch (Exception e) {
				return "0";
			}
		});
		RedisUtils.get("use-statistic-student-timer-v7", 82800, () -> {//学生数
			try {
				List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnitClass(2), Unit.class);
				Set<String> unitIds=EntityUtils.getSet(unitList, e->e.getId());
				Map<String,Integer> studentCountMap = SUtils.dc(studentRemoteService.countListBySchoolIds(unitIds.toArray(new String[0])), Map.class);
				int students=0;
				if(studentCountMap!=null&&studentCountMap.size()>0){
					for (Entry<String, Integer> entry : studentCountMap.entrySet()) {
						students=entry.getValue()+students;
					}
				}
				return students+"";
			} catch (Exception e) {
				return "0";
			}
		});
		RedisUtils.get("use-statistic-teacher-timer-v7", 82800, () -> {//教职工
			try {
				List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnitClass(2), Unit.class);;
				List<Unit> unitListEdu = SUtils.dt(unitRemoteService.findByUnitClass(1), Unit.class);;
				unitListEdu.addAll(unitList);
				Set<String> unitIds=EntityUtils.getSet(unitListEdu, e->e.getId());
				int teachers=0;
				teachers=(int) teacherRemoteService.countByUnitIds(unitIds.toArray(new String[0]));
				return teachers+"";
			} catch (Exception e) {
				return "0";
			}
		});
	}

	@Scheduled(cron = "0 0 1 * * ?")
	public void pushProductionInfo() {
		RedisUtils.get("use-statistic-use-school-timer-v7", 82800, () -> {//在用学校数
			try {
				int useSchools=0;
//				List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnitClass(2), Unit.class);
//				Set<String> unitSet=EntityUtils.getSet(unitList, e->e.getId());
//				useSchools=(int) operationLogRemoteService.countUnits(unitSet.toArray(new String[0]));
				useSchools=(int) operationLogRemoteService.countSchool();
				return (useSchools)+"";
			} catch (Exception e) {
				return "0";
			}
		});
		RedisUtils.get("use-statistic-web-timer-v7", 82800, () -> {
			try {
				int webNum=0;
				//RestTemplate restTemplate = restTemplate();
				//String passportUrl = Evn.getString(Constant.PASSPORT_URL);
				//String url = passportUrl+"/api/getLoginStat.htm?secretKey=FF808081497E696001497E9AA3D90007";
				//String resultStr = restTemplate.getForObject(url,
				//		String.class);
				//JSONObject result = JSONObject.parseObject(resultStr);
				//if ("true".equals(result.getString("success"))) {
				//	webNum = result.getIntValue("loginAccount");
				//}
				webNum=(int) operationLogRemoteService.countUsersByDate();
				return webNum+"";
			} catch (Exception e) {
				return "0";
			}
		});
		RedisUtils.get("use-statistic-mobile-timer-v7", 82800, () -> {
			try {
				RestTemplate restTemplate = restTemplate();
				String url = "http://www1.wanpeng.com/getMapTypeTotalStatList.htm?secretKey=FF808081497E696001497E9AA3D90007&mapType=259";
				String resultStr = restTemplate.getForObject(url,
						String.class);
				JSONObject result = JSONObject.parseObject(resultStr);
				int webNum=0;
				if ("true".equals(result.getString("success"))&&result.containsKey("list")) {
					String listStr = result.getString("list");
					if(StringUtils.isNotBlank(listStr)){
						JSONArray scoreInfoArr=JSONArray.parseArray(listStr);
						for(int j=0;j<scoreInfoArr.size();j++){
							JSONObject jobb = scoreInfoArr.getJSONObject(j);
							if(StringUtils.equals(jobb.getString("code"),"familyNum")){
								webNum=Integer.valueOf(jobb.getString("count"))+webNum;
							}else if(StringUtils.equals(jobb.getString("code"),"studentNum")){
								webNum=Integer.valueOf(jobb.getString("count"))+webNum;
							}else if(StringUtils.equals(jobb.getString("code"),"teacherNum")){
								webNum=Integer.valueOf(jobb.getString("count"))+webNum;
							}
						}
					}
					
				}
				return webNum+"";
			} catch (Exception e) {
				return "0";
			}
		});
		RedisUtils.get("use-statistic-total-timer-v7", 82800, () -> {
			try {
				RestTemplate restTemplate = restTemplate();
				String url = "http://www1.wanpeng.com/getMapTypeTotalStatList.htm?secretKey=FF808081497E696001497E9AA3D90007&mapType=259";
				String resultStr = restTemplate.getForObject(url,
						String.class);
				JSONObject result = JSONObject.parseObject(resultStr);
				int mobileNum=0;
				if ("true".equals(result.getString("success"))&&result.containsKey("list")) {
					String listStr = result.getString("list");
					if(StringUtils.isNotBlank(listStr)){
						JSONArray scoreInfoArr=JSONArray.parseArray(listStr);
						for(int j=0;j<scoreInfoArr.size();j++){
							JSONObject jobb = scoreInfoArr.getJSONObject(j);
							if(StringUtils.equals(jobb.getString("code"),"allMonthFamilyLogin")){
								mobileNum=Integer.valueOf(jobb.getString("count"))+mobileNum;
							}else if(StringUtils.equals(jobb.getString("code"),"allMonthTeacherLogin")){
								mobileNum=Integer.valueOf(jobb.getString("count"))+mobileNum;
							}
						}
					}
					
				}
				
				int webNum=0;
				//String passportUrl = Evn.getString(Constant.PASSPORT_URL);
				//String paurl = passportUrl+"/api/getLoginStat.htm?secretKey=FF808081497E696001497E9AA3D90007";
				//String resultStrWeb = restTemplate.getForObject(paurl,
				//		String.class);
				//JSONObject webSesult = JSONObject.parseObject(resultStrWeb);
				//if ("true".equals(webSesult.getString("success"))) {
				//	webNum = webSesult.getIntValue("loginTimes");
				//}
				webNum=(int) operationLogRemoteService.countAllUsers();
				return (webNum+mobileNum+10000)+"";
			} catch (Exception e) {
				return "0";
			}
		});
	}

}
