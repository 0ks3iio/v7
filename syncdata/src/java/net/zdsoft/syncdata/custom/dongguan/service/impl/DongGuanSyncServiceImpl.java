package net.zdsoft.syncdata.custom.dongguan.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.constant.custom.DgjyConstant;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.HttpClientParam;
import net.zdsoft.framework.utils.HttpClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.base.service.impl.BaseCustomServiceImpl;
import net.zdsoft.syncdata.custom.dongguan.service.DongGuanSyncService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Service("dongGuanSyncService")
public class DongGuanSyncServiceImpl extends BaseCustomServiceImpl implements DongGuanSyncService {
	
	Logger logger = LoggerFactory.getLogger(DongGuanSyncServiceImpl.class);

	private static RestTemplate restTemplate = new RestTemplate();
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	
	@Override
	public void saveUnitAndAdmin() {
		try {
			String endTime = DateUtils.date2StringBySecond(new Date());
			String startTime = RedisUtils.get("syncdata.dongguan.new.modify.time");
			for (int i = 0; i < 1000; i++) {
				int pageIndex = i + 1;
				int pageSize = 1000;
				String searchuser = "true";
				JSONObject param = new JSONObject();
				param.put("starttime", startTime);
				param.put("endtime", endTime);
				param.put("pageindex", pageIndex);
				param.put("pagesize", pageSize);
				param.put("searchuser", searchuser);
				StringBuilder urlBuilder = new StringBuilder();
				urlBuilder.append("https://hui.dgjy.net:7777/v1/Company/Sync?param=");
				urlBuilder.append(param);
				String dataString  = getDate1(urlBuilder.toString());
				if(StringUtils.isBlank(dataString)){
					logger.error("获取的机构信息是 null -------");
					break;
				}
				JSONObject dataJsonObject = Json.parseObject(dataString);
				String unitData = dataJsonObject.getString("institutionList");
				String adminData = dataJsonObject.getString("adminList");
				if(StringUtils.isBlank(unitData) || (StringUtils.isNotBlank(unitData) && (JSON.parseArray(unitData).size() < 1))){
					break;
				}
				Set<String> saveUnitList = doSaveUnit(unitData);
				if(StringUtils.isNotBlank(adminData) && (JSON.parseArray(adminData).size() > 0) && CollectionUtils.isNotEmpty(saveUnitList)){
					doSaveAdmin(adminData,saveUnitList);
				}
			}
			RedisUtils.set("syncdata.dongguan.new.modify.time", endTime);
		} catch (Exception e) {
			logger.error("东莞同步学校数据失败-------" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new DongGuanSyncServiceImpl().saveUnitAndAdmin();
	}

	/**
	 * 保存单位数据
	 * @param unitData
	 */
	private Set<String> doSaveUnit(String unitData) {
		Set<String> saveUnitSet = new HashSet<>();
		if(StringUtils.isNotBlank(unitData)){
			Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
			List<Unit> allUnit = SUtils.dt(unitRemoteService.findByUnitClass(Unit.UNIT_CLASS_EDU), Unit.class);
			Map<String, Unit> eduIdMap = EntityUtils.getMap(allUnit, Unit::getId);
			JSONArray dataArray = JSON.parseArray(unitData);
			Set<String> unitSets = new HashSet<>();
			for (int i = 0; i < dataArray.size(); i++) {
				JSONObject js = dataArray.getJSONObject(i);
				unitSets.add(doChangeId(js.getString("F_COMPANYID")));
			}
			if(CollectionUtils.isNotEmpty(unitSets)){
				List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(unitSets.toArray(new String[unitSets.size()])), new TR<List<Unit>>() {});
				Map<String, Unit> uidMap = EntityUtils.getMap(allUnits, Unit::getId);
				List<School> allSchool = SUtils.dt(schoolRemoteService.findListByIds(unitSets.toArray(new String[unitSets.size()])),
						new TR<List<School>>() {});
				Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getId);
				//先得到对应的idList
				List<School> saveSchools = new ArrayList<>();
				List<Unit> saveUnits = new ArrayList<>();
				logger.info("获取学校的信息数据为------------" + dataArray.size());
				Map<String, School> saveSchoolMap = new HashMap<>();
				Map<String, Unit> saveUnitMap = new HashMap<>();
				int n = 0;
				for (int i = 0; i < dataArray.size(); i++) {
					JSONObject js = dataArray.getJSONObject(i);
					String unitClass = js.getString("F_CATEGORY");
					String sid = doChangeId(js.getString("F_COMPANYID"));
					String schoolCode = js.getString("F_SCCODE");
					String schoolName = js.getString("F_FULLNAME");
					String typeParam = js.getString("F_INDUSTRY_NAME");
					if(Unit.UNIT_CLASS_SCHOOL == Integer.valueOf(unitClass) && StringUtils.isBlank(typeParam)){
//						logger.error("学校的机构中，学校类型的值是 空----"+ sid);
						n ++ ;
						continue;
					}
					String schoolType = doChangeSchoolType(typeParam);   //学校类型
					Integer runSchoolType = doChangeRunSchoolType(js.getString("F_SCHOOLTYPE"));
					String parentId = doChangeId(js.getString("F_PARENTID"));  
					if(StringUtils.isBlank(parentId)){
						n ++ ;
						continue;
					}
					Integer isDeleted = StringUtils.isNotBlank(js.getString("F_DELETEMARK")) ?  Integer.valueOf(js.getString("F_DELETEMARK")) : BaseSaveConstant.DEFAULT_IS_DELETED_VALUE;
					String state = String.valueOf(Unit.UNIT_MARK_NORAML);
					String sections = doChargeSection(schoolType);  // TODO 学段需要提供 
					String regionCode = js.getString("F_COUNTYID");
					if(StringUtils.isBlank(regionCode)){
						n ++ ;
						continue;
					}else{
						if(regionCode.length() > 6)
							regionCode = regionCode.substring(0, 6);
					}
					String address = js.getString("F_ADDRESS");
					if(StringUtils.isNotBlank(address) && address.getBytes().length> 60 ){
						address = address.trim().substring(0, 20);
					}
					String mobilePhone = js.getString("F_OUTERPHONE");
					String email = js.getString("F_EMAIL");
					String shortName = js.getString("F_SHORTNAME");
					if(StringUtils.isNotBlank(shortName) && shortName.length() > 16){
						shortName = shortName.substring(0, 16);
					}
//			----------封装学校
					if(Unit.UNIT_CLASS_SCHOOL == Integer.valueOf(unitClass)){
						School school;
						if(allSchoolMap != null && !allSchoolMap.isEmpty() && allSchoolMap.get(sid) != null){
							school = allSchoolMap.get(sid);
							school.setModifyTime(new Date());
						}else if (MapUtils.isNotEmpty(saveSchoolMap) && saveSchoolMap.get(sid) != null){
							school = saveSchoolMap.get(sid);
							school.setModifyTime(new Date());
						}else{
							school = new School();
							school.setRunSchoolType(runSchoolType);
							school.setRegionCode(regionCode);  // TODO 地区码 regionCode 需要提供 
							school.setCreationTime(new Date());
						}
						school.setId(sid);
						schoolType = StringUtils.isBlank(schoolType)  ? "211" : schoolType;
						school.setSchoolType(schoolType);
						school.setSchoolCode(StringUtils.isBlank(schoolCode)  ? "0000" : schoolCode);
						if(StringUtils.isBlank(sections)){
							sections = "1";
						}
						school.setSections(sections);
						school.setSchoolName(schoolName);
						school.setIsDeleted(isDeleted);
						school.setAddress(address);
						school.setMobilePhone(mobilePhone);
						school.setLinkPhone(mobilePhone);
						school.setShortName(shortName);
						school.setEmail(email);
						saveSchoolMap.put(sid, school);
						saveSchools.add(school);
					}
//		    ----------封装单位	
					Unit u;
					if(uidMap != null && !uidMap.isEmpty() && uidMap.get(sid) != null){
						u = uidMap.get(sid);
					}else if (MapUtils.isNotEmpty(saveUnitMap) && saveUnitMap.get(sid) != null){
						u = saveUnitMap.get(sid);
						u.setModifyTime(new Date());
					}else{
						u = new Unit();
						u.setId(sid);
						if(StringUtils.isBlank(state)){
							u.setUnitState(Unit.UNIT_MARK_NORAML);
						}
						u.setRegionCode(regionCode);
						if(RedisUtils.get("syncdata.dongguan.unit.unioncode.max.regioncode." + regionCode) == null) {
							RedisUtils.set("syncdata.dongguan.unit.unioncode.max.regioncode." + regionCode, String.valueOf(allUnits.size()));
						}
						Long max = RedisUtils.incrby("syncdata.dongguan.unit.unioncode.max.regioncode." + regionCode, 1);
						logger.info("-----------新增单位的unioncode的值是-----" + max);
						if(StringUtils.isEmpty(regionCode)) {
							u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
						}else {
							u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
						}
						u.setDisplayOrder(u.getUnionCode());
						u.setRegionLevel(
								StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
						u.setUnitClass(Integer.valueOf(unitClass));
					}
					u.setParentId(StringUtils.isNotBlank(parentId)? parentId : topUnit.getId());
					if(StringUtils.isNotBlank(state)){
						u.setUnitState(Integer.valueOf(state));
					}
					u.setUnitName(schoolName);
					//默认是学校
					u.setIsDeleted(isDeleted);
					if(Unit.UNIT_CLASS_SCHOOL == Integer.valueOf(unitClass)){
						u.setSchoolType(schoolType);
						u.setRunSchoolType(runSchoolType); 
						//根据学校类型来得出单位类型
						Integer unitType;
						if(schoolType.startsWith("1")){
							unitType = Unit.UNIT_SCHOOL_KINDERGARTEN;
						}else if (schoolType.startsWith("2")) {
							unitType = Unit.UNIT_SCHOOL_ASP;
						}else if (schoolType.startsWith("3")) {
							unitType = Unit.UNIT_SCHOOL_ASP;
						}else if (schoolType.startsWith("5")) {
							unitType = Unit.UNIT_NOTEDU_NOTSCH;
						}else if (schoolType.startsWith("9")) {
							unitType = Unit.UNIT_NOTEDU_NOTSCH;
						}else{
							unitType = Unit.UNIT_SCHOOL_ASP;
						}
						u.setUnitType(unitType);
					}else{
						u.setUnitType(Unit.UNIT_EDU_SUB);
					}
					u.setAddress(address);
					u.setMobilePhone(mobilePhone);
					u.setLinkPhone(mobilePhone);
					u.setEmail(email);
					saveUnitMap.put(sid, u);
					saveUnits.add(u);
				}
				System.out.println("一共过滤了--" + n);
				if(CollectionUtils.isNotEmpty(saveSchools)){
					baseSyncSaveService.saveSchool(saveSchools.toArray(new School[saveSchools.size()]));
				}
				if(CollectionUtils.isNotEmpty(saveUnits)){
					baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]),null,Boolean.FALSE);
					saveUnitSet.addAll(EntityUtils.getSet(saveUnits, Unit::getId));
				}
			}
		}
		return saveUnitSet;
	}

	/**
	 * 保存单位管理员数据
	 * @param adminData
	 */
	private void doSaveAdmin(String adminData,Set<String> saveUnitList) {
		if(StringUtils.isNotBlank(adminData)){
			JSONArray array = Json.parseArray(adminData);
			System.out.println("获取到教师的数据：--------" +  array.size());
			Set<String> unitIds = new HashSet<>();
			List<Teacher> saveTeacherList = new ArrayList<>();
			List<User> saveUserList = new ArrayList<>();
			if(array != null && array.size() >= 1 ){
				Set<String> useridSet = new HashSet<>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					unitIds.add(doChangeId(js.getString("UNIT_ID")));
					useridSet.add(doChangeId(js.getString("ID")));
				}
				List<Teacher> allUnitTeacher = SUtils.dt(teacherRemoteService.findByUnitIdIn(unitIds.toArray(new String[unitIds.size()])), new TR<List<Teacher>>() {});
				Map<String,Teacher> teacherMap = EntityUtils.getMap(allUnitTeacher, Teacher::getId);
				List<User> allTeacherList = SUtils.dt(userRemoteService.findListByIds(useridSet.toArray(new String[useridSet.size()])),new TR<List<User>>() {});
				Map<String,User> allUserNameMap = EntityUtils.getMap(allTeacherList, User::getUsername);
				Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(unitIds.toArray(new String[unitIds.size()])), new TypeReference<Map<String, List<Dept>>>(){});
				List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[unitIds.size()])), new TR<List<Unit>>() {});
				allUnits = EntityUtils.filter2(allUnits, t->{
	        		return t.getIsDeleted() == 0;
	        	});		
				Map<String, String> regionCodeMap = EntityUtils.getMap(allUnits, Unit::getId, Unit::getRegionCode);
				Set<String> userNameSet = new HashSet<String>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String userId = doChangeId(js.getString("ID"));
					String tid = doChangeId(js.getString("OWNER_ID"));
					String teacherName = js.getString("REAL_NAME");
					String username = js.getString("USERNAME");  //登陆名称
					//判断账号是否已经添加了，是的话直接跳过，
					if(CollectionUtils.isNotEmpty(userNameSet) && userNameSet.contains(username)){
						System.out.println("账号出现重复的-------------" + username);
						continue;
					} 
					userNameSet.add(username);
					String unitId = doChangeId(js.getString("UNIT_ID"));
					//判断该单位是否已经成功保存
					if(!saveUnitList.contains(unitId)){
						continue;
					}
					String teacherCode = "000000";
					Integer isDeleted = js.getInteger("IS_DELETED");
					Integer userType = User.USER_TYPE_UNIT_ADMIN;
					//-------封装教师数据
					Teacher t;
					if(teacherMap != null && !teacherMap.isEmpty() && teacherMap.get(tid) != null){
						t = teacherMap.get(tid);
						t.setModifyTime(new Date());
					}else{
						t = new Teacher();
						t.setId(tid);
						t.setCreationTime(new Date());
						if(MapUtils.isNotEmpty(regionCodeMap) && StringUtils.isNotBlank(regionCodeMap.get(unitId))){
							t.setRegionCode(regionCodeMap.get(unitId));
						}
					}
					Long displayOrder;
					if(RedisUtils.get("syncdata.dongguan.teacher.displayorder.max." + unitId) == null) {
						if(CollectionUtils.isEmpty(allUnitTeacher)) {
							displayOrder = (long)1;
						}else {
							displayOrder = (long) allUnitTeacher.size();
						}
					}
					displayOrder = RedisUtils
							.incrby("syncdata.dongguan.teacher.displayorder.max." + unitId, 1);
					t.setDisplayOrder( t.getDisplayOrder() == null ? displayOrder.intValue() : t.getDisplayOrder());
					t.setTeacherCode(teacherCode);
					t.setUnitId(unitId);
					t.setTeacherName(teacherName);
					t.setIsDeleted(isDeleted);
					saveTeacherList.add(t);
//									----------封装用户	
					if(StringUtils.isNotBlank(username)){
						if(MapUtils.isEmpty(allUserNameMap) || allUserNameMap.get(username) == null){
							User user = new User();
							user.setId(userId);
							user.setPassword(getDefaultPwd());
							user.setOwnerType(User.OWNER_TYPE_TEACHER);
							user.setUsername(username);
							user.setDisplayOrder(t.getDisplayOrder());
							user.setOwnerId(tid);
							user.setUnitId(unitId);
							user.setSex(t.getSex());
							user.setRealName(t.getTeacherName());
							//判断是否是管理员
							user.setUserType(userType);
							user.setIsDeleted(t.getIsDeleted());
							user.setRegionCode(t.getRegionCode());
							saveUserList.add(user);
						}
						if(MapUtils.isNotEmpty(allUserNameMap) && allUserNameMap.get(username) != null){
							User user = allUserNameMap.get(username);
							user.setIsDeleted(t.getIsDeleted());
							saveUserList.add(user);
						}
					}
				}
				for (Teacher teacher : saveTeacherList) {
					if(teacherMap == null || teacherMap.isEmpty() || teacherMap.get(teacher.getId()) == null){
						String uId = teacher.getUnitId();
						if(deptMap != null || deptMap.get(uId) != null){				
							List<Dept> depts = deptMap.get(uId);
							if(CollectionUtils.isNotEmpty(depts)){
								for (Dept dept : depts) {
									if(dept.getIsDefault() != null && dept.getIsDefault() == 1){
										teacher.setDeptId(dept.getId());
										break;
									}
								}
							}else{
								teacher.setDeptId(Unit.TOP_UNIT_GUID);  //教师单位不存在时， 部门id 保存为 32个 0
							}
						}
					}
				}
			}
			//进行保存教师和学生数据
			System.out.println("数据同步到教师的数据：--------" +  saveTeacherList.size() + "同步用户的数据是：------" + saveUserList.size());
			if(CollectionUtils.isNotEmpty(saveTeacherList)){
				if(CollectionUtils.isNotEmpty(saveUserList)){
					baseSyncSaveService.saveTeacherAndUser(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
				}else{
					baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
				}
			}
		}
	}
	
	private String getDate1(String url) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + getAccessToken());
		Map<String, String> querys = new HashMap<>();
		HttpClientParam p = new HttpClientParam();
		p.setHeadMap(headers);
		p.setParamMap(querys);
		String returnStr;
		try {
			returnStr = HttpClientUtils.exeUrlSync(url, p);
			if (StringUtils.isNotBlank(returnStr)) {
				JSONObject jsonObject = JSONObject.parseObject(returnStr);
				String code = jsonObject.getString("Code");
				if("0".equals(code)){
					return jsonObject.getString("Data");
				}else{
					logger.error("获取机构信息失败---" + jsonObject.getString("Msg"));
					return null;
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
        }
		return null;
	}
	
	private String getDate(String url) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    headers.set("Authorization", "Bearer " + getAccessToken());
		String jsonData = null;
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		jsonData = responseBody.getBody();
		if(StringUtils.isBlank(jsonData))
			return null;
		JSONObject jsonObject = Json.parseObject(jsonData);
		String code = jsonObject.getString("Code");
		if("0".equals(code)){
			return jsonObject.getString("Data");
		}else{
			logger.error("获取机构信息失败---" + jsonObject.getString("Msg"));
			return null;
		}
	}
	
	
	/**
	 * 得到 runSchoolType  
	 * 1 公办 -->  831 ;  2 民办 --> 999
	 * @param runSchoolType
	 * @return
	 */
	private Integer doChangeRunSchoolType(String runSchoolType) {
		Integer rsType = School.DEFAULT_RUN_SCHOOL_TYPE;
		if(StringUtils.isNotBlank(runSchoolType)){
			if("2".equals(runSchoolType))
				rsType = 999;
		}
		return rsType;
	}
	
	private String getAccessToken(){
		String accessToken;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("client_id", DgjyConstant.DG_CLIENT_ID_VALUE);
			map.add("client_secret", DgjyConstant.DG_CLIENT_SECRET_VALUE);
			map.add("grant_type", DgjyConstant.DG_GRANT_TYPE_VALUE);
			map.add("scope", DgjyConstant.DG_GET_SCOPE_VALUE);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(DgjyConstant.DG_GET_ACCESSTOKEN_URI, requestEntity, String.class);
			String responseBody = responseEntity.getBody();
			JSONObject json = Json.parseObject(responseBody);
			return URLEncoder.encode(json.getString("access_token"), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
			accessToken = null;
			logger.error("调用接口获取登录用户信息失败：--------" + e.getMessage());
		}
		return accessToken;
	}
}
