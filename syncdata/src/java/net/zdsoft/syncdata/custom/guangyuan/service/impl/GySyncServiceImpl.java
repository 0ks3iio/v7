package net.zdsoft.syncdata.custom.guangyuan.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.guangyuan.constant.GyBaseConstant;
import net.zdsoft.syncdata.custom.guangyuan.service.GySyncService;
import net.zdsoft.syncdata.custom.guangyuan.wsdl.ExternalInterface;
import net.zdsoft.syncdata.custom.guangyuan.wsdl.ExternalInterfaceSoap;
import net.zdsoft.syncdata.custom.guangyuan.wsdl.MySoapHeader;
import net.zdsoft.syncdata.util.SyncTimeUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;


@Service("gySyncService")
public class GySyncServiceImpl implements GySyncService {
	private Logger log = Logger.getLogger(GySyncServiceImpl.class);

	@Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;

	
	static Map<String, String> eduIdMap = new HashMap<>();
	static{
		eduIdMap.put("510812", GyBaseConstant.GY_CT_JYJ_UNIT_ID);
		eduIdMap.put("510824006", "0148cfd3f8fa4c699393c688941a8044"); //进修校
		eduIdMap.put("510824100", "0c0722b210ce4a43b78b1cd542df0df4"); //苍溪县陵江督导站
		eduIdMap.put("510824001", "598eb704fe274c048198879f65cd07e4"); //苍溪县电教教仪站
		eduIdMap.put("510824", "89ac2bfba65244cda3e137be53c02940");  //苍溪教育和科学技术局
		eduIdMap.put("510824800", "908c69dcc797448796e0828ef59f58bc");  //苍溪县文昌督导站
		eduIdMap.put("510824300", "a13c02efad704550a5b96ca26188e8f6"); //苍溪县五龙督导站
		eduIdMap.put("510824900", "a47852d51b264e179cf46794bd807f09"); //苍溪县龙山督导站
		eduIdMap.put("510824400", "b085b8ba7ac143ac83df9fe11c446dcd"); //苍溪县三川督导站
		eduIdMap.put("510824500", "b3a1571684da447dbfe6ab6ccd25f8ec"); //苍溪县元坝督导站
		eduIdMap.put("510824007", "b6120e284ac241229dd1bac52b44162f"); //核算中心
		eduIdMap.put("510824002", "d2babdf933a14e2fb32f4fa4614fc1d1"); //苍溪县中小学教学研究室
		eduIdMap.put("510824600", "dff86d07b8cd44f78065f7a84fe2c246"); //苍溪县歧坪督导站
		eduIdMap.put("510824700", "e846707ae7f4490caf67b96ecc0be64d"); //苍溪县东溪督导站
		eduIdMap.put("510824200", "f5ad3249ac784b6da52f7039971e33e3"); //苍溪县东青督导站
	}
	
	@Override
	public void saveEdu(String url) {
		ExternalInterfaceSoap soap = getSoap(url);
		String token = getToken(soap);
		String cc = getDate (url,GyBaseConstant.GY_UNIT_MODULE_NO_VALUE,soap,token);
		JSONArray array = Json.parseArray(cc);
		//先得到对应的idList
		List<Unit> saveUnitList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			
			String unitId = js.getString("ID").replaceAll("-", "");
			String name = js.getString("DEPTNAME");
			String parentno = js.getString("PARENTNO");
			String deptNo = js.getString("DEPENO");
			Integer regionLevel;
			String pid;
			if(deptNo.equals("510812") || deptNo.equals("510824")){
				pid = "e1139067cb4e4a9b8f9ca415ce4189e2";
				regionLevel = Unit.UNIT_REGION_COUNTY;
			}else{
				pid = eduIdMap.get(parentno);
				regionLevel = Unit.UNIT_REGION_LEVEL;
			}
			String schoolType = js.getString("SCHOOLTYPE");
			if(StringUtils.isBlank(schoolType)){
				Unit u = new Unit();
				u.setId(unitId);
				u.setUnitState(Unit.UNIT_MARK_NORAML);
				u.setAuthorized(1);
				u.setUseType(1);
				u.setRegionCode(GyBaseConstant.GY_SCHOOL_REGION_CODE_DEFAULT);
				u.setParentId(pid);
				u.setUnionCode(GyBaseConstant.GY_SCHOOL_REGION_CODE_DEFAULT + StringUtils.leftPad("", 6, "0"));
				u.setDisplayOrder(u.getUnionCode());
				u.setRegionLevel(regionLevel);
				u.setUnitName(name);
				//默认是学校
				u.setUnitClass(Unit.UNIT_CLASS_EDU);
				//根据学校类型来得出单位类型
				Integer unitType = Unit.UNIT_EDU_SUB;
				u.setUnitType(unitType);
				saveUnitList.add(u);
			}
		}
		System.out.println("保存教育局单位的数据是：-------------------" + saveUnitList.size());
		if(CollectionUtils.isNotEmpty(saveUnitList)){
			baseSyncSaveService.saveUnit(saveUnitList.toArray(new Unit[saveUnitList.size()]));
		}
	}
	
	
	@Override
	public void saveSchool(String url) {
		// TODO Auto-generated method stub
		ExternalInterfaceSoap soap = getSoap(url);
		String token = getToken(soap);
		String cc = getDate (url,GyBaseConstant.GY_UNIT_MODULE_NO_VALUE,soap,token);
		JSONArray array = Json.parseArray(cc);
		SimpleDateFormat sdf = SyncTimeUtils.getDataFormat();
		
		Map<String, String> schoolTypeMap = new HashMap<String, String>();
		schoolTypeMap.put("中心校（完小）", "211");
		schoolTypeMap.put("初级中学", "311");
		schoolTypeMap.put("幼儿园", "111");
		schoolTypeMap.put("九年一贯制", "312");
		schoolTypeMap.put("高级中学", "342");
		schoolTypeMap.put("中职学校", "365");
		schoolTypeMap.put("村小", "211");
		schoolTypeMap.put("完全中学", "341");
		Map<String, String> sectionMap = new HashMap<String, String>();
		sectionMap.put("211", "1");
		sectionMap.put("311", "2");
		sectionMap.put("111", "0");
		sectionMap.put("312", "1,2");
		sectionMap.put("342", "3");
		sectionMap.put("365", "3");
		sectionMap.put("341", "2,3");
		//先得到对应的idList
		List<School> saveSchools = new ArrayList<>();
		List<Unit> saveUnits = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject js = array.getJSONObject(i);
			School school = new School();
			
			String unitId = js.getString("ID").replaceAll("-", "");
			String schoolCode = js.getString("DEPENO");
			String name = js.getString("DEPTNAME");
//			String pid;
//			if(GyBaseConstant.GYCX_PLATFORM_URL.equals(url)){
//				pid = "0148cfd3f8fa4c699393c688941a8044";
//			}else{
//				pid = "9e2346c5c08d4f9c9a880a8a6d220f0b";
//			}
			String parentno = js.getString("PARENTNO"); //510824900007
			String pid = eduIdMap.get(parentno);
			if("5108240701".equals(schoolCode)){
				log.error("学校的父级教育局不存在：----DEPENO："+schoolCode  + "父级单位是："+parentno +"单位id：" +unitId );
			}
			
			String schoolType = js.getString("SCHOOLTYPE");
			if(StringUtils.isBlank(schoolType)){
				continue;
			}else{
				schoolType = schoolTypeMap.get(js.getString("SCHOOLTYPE"));
			}
			
			String sections = sectionMap.get(schoolType);
//			----------封装学校
			school.setId(unitId);
			school.setRunSchoolType(School.DEFAULT_RUN_SCHOOL_TYPE);
			school.setSchoolType(schoolType);
			school.setSchoolCode(schoolCode);
			school.setRegionCode(GyBaseConstant.GY_SCHOOL_REGION_CODE_DEFAULT);
			school.setSections(sections);
			school.setSchoolName(name);
			
			saveSchools.add(school);
//		    ----------封装单位	
			String regionCode = school.getRegionCode();
    		Unit u = new Unit();
			u.setId(school.getId());
			u.setEventSource(school.getEventSource());
			u.setOrgVersion(1);
			u.setUnitState(Unit.UNIT_MARK_NORAML);
			u.setAuthorized(1);
			u.setUseType(1);
			u.setCreationTime(school.getCreationTime());
			u.setRegionCode(regionCode);
			u.setParentId(pid);
			u.setUnionCode(regionCode + StringUtils.leftPad("", 6, "0"));
			u.setDisplayOrder(u.getUnionCode());
			u.setRegionLevel(
					StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
			u.setUnitName(school.getSchoolName());
			//默认是学校
			u.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
			u.setModifyTime(school.getModifyTime());
			u.setIsDeleted(school.getIsDeleted());
			u.setSchoolType(school.getSchoolType());
			u.setRunSchoolType(school.getRunSchoolType()); 
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
			saveUnits.add(u);
		}
		System.out.println("保存学校的数据是：--------" + saveSchools.size() + "------------单位的数据是：" + saveUnits.size());
		if(CollectionUtils.isNotEmpty(saveSchools))
			baseSyncSaveService.saveSchool(saveSchools.toArray(new School[saveSchools.size()]));
		if(CollectionUtils.isNotEmpty(saveUnits))
			baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]));
	}

	@Override
	public void saveTeacher(String url) {
		ExternalInterfaceSoap soap = getSoap(url);
		String token = getToken(soap);
		String cc = getDate (url,GyBaseConstant.GY_USER_MODULE_NO_VALUE,soap,token);
		JSONArray array = Json.parseArray(cc);
		
		List<JSONObject> saveJsonObjects = new ArrayList<JSONObject>();
		Set<String> deptNo = new HashSet<>();
		for(int i = 0; i < array.size(); i++){
			JSONObject js = array.getJSONObject(i);
			String usertype = js.getString("USERTYPE");
			String deptNo1 = js.getString("DEPTNO");
//			510812
			if(("3.0".equals(usertype) || "2.0".equals(usertype))){
				saveJsonObjects.add(js);
				deptNo.add(deptNo1);
			}
		}
		
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(deptNo.toArray(new String[deptNo.size()])), 
				new TR<List<School>>() {
		});
		Map<String, String> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode, School::getId);
		
		allSchoolMap.putAll(eduIdMap);
		
		System.out.println("保存教师的数据总数是---------" + saveJsonObjects.size());
		//先得到对应的idList
		List<Teacher> saveTeachers = new ArrayList<>();
		List<User> saveUsers = new ArrayList<>();
		Set<String> uidList = new HashSet<>();
        for (JSONObject jsonObject : saveJsonObjects) {
        	uidList.add(allSchoolMap.get(jsonObject.getString("DEPTNO")));
		}
		
		Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(uidList.toArray(new String[uidList.size()])),
				new TypeReference<Map<String, List<Dept>>>(){});
		int errorNum = 0;
		for (JSONObject jsonObject : saveJsonObjects) {
			String id = jsonObject.getString("ID").replaceAll("-", "");
			String username = jsonObject.getString("LOGINID");
			String realName = jsonObject.getString("NAME");
			String password = jsonObject.getString("LOGINPWD");
			String sex = jsonObject.getString("SEX");
			Integer sex1;
			if(StringUtils.isNotBlank(sex)){
				sex1 = "男".equals(sex) ? 1 : 0;
			}else{
				sex1 = 9;
			}
			Integer ownerType = 2;  //教师
			String phoneNum = jsonObject.getString("TEL");
			String email = jsonObject.getString("EMAIL");
			String identityCard = jsonObject.getString("IDCARDNO");
			String unitId = allSchoolMap.get(jsonObject.getString("DEPTNO"));
			String qq  = jsonObject.getString("QQ");
			String usertype = jsonObject.getString("USERTYPE");
			String regionCode = GyBaseConstant.GY_SCHOOL_REGION_CODE_DEFAULT;
//			----------封装用户	
			User user = new User();
			user.setId(id);
			user.setOwnerId(id);
			user.setUnitId(unitId);
			user.setIdentityCard(identityCard);
			user.setUsername(username);
			String newPwd;
			try {
				newPwd = decryptPwd(password, soap, token);
			} catch (Exception e) {
				errorNum++;
				newPwd = password;
				log.error("密码解密失败--------" + password + "用户的id：-----" + id);
			}
			user.setPassword(newPwd);
			user.setOwnerType(ownerType);
			user.setSex(sex1);
			user.setMobilePhone(phoneNum);
			user.setEmail(email);
			user.setQq(qq);
			user.setRealName(realName);
			user.setRegionCode(regionCode);
			//判断是否是管理员
			if("2.0".equals(usertype)){
				user.setUserType(User.USER_TYPE_UNIT_ADMIN);
			}else{
				user.setUserType(User.USER_TYPE_COMMON_USER);
			}
			saveUsers.add(user);
//          -----------封装用户
			Teacher teacher = new Teacher();
			
			teacher.setId(id);
			teacher.setUnitId(unitId);
			teacher.setTeacherCode("0000");
			teacher.setTeacherName(realName);
			teacher.setIdentityCard(identityCard);
			teacher.setEmail(email);
			teacher.setMobilePhone(phoneNum);
			teacher.setSex(sex1);
			teacher.setIncumbencySign("11");
			if(deptMap != null || deptMap.get(unitId) != null){				
				List<Dept> depts = deptMap.get(unitId);
				if(depts != null){
					for (Dept dept : depts) {
						if(dept.getIsDefault() == 1){
							teacher.setDeptId(dept.getId());
							break;
						}
					}
				}
			}
			saveTeachers.add(teacher);			
		}
		log.error("密码解密失败--------" + errorNum + "条数据-----");
		System.out.println("保存教师的数据是：--------" + saveTeachers.size() + "------------用户的人数是：" + saveUsers.size());
		if(CollectionUtils.isNotEmpty(saveTeachers))
			baseSyncSaveService.saveTeacher(saveTeachers.toArray(new Teacher[saveTeachers.size()]));
		if(CollectionUtils.isNotEmpty(saveUsers))
			baseSyncSaveService.saveUser(saveUsers.toArray(new User[saveUsers.size()]));
	}
	
	
	
	//连接服务，获取数据
	private String getDate(String url,String moduleNo,ExternalInterfaceSoap soap,String token){
		try {
			if(StringUtils.isNotBlank(token)){
				String deptNo;
				if(GyBaseConstant.GYCX_PLATFORM_URL.equals(url)){
					deptNo = GyBaseConstant.GYCX_DEPT_NO_VALUE;
				}else{
					deptNo = GyBaseConstant.GYCT_DEPT_NO_VALUE;
				}
				String dataString = soap.getSpecifyTable(deptNo, moduleNo, token);
				JSONArray array = Json.parseArray(dataString);
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String state = js.getString("State");
					if(GyBaseConstant.GY_TRUE_GET_RESULT.equals(state)){
						return js.getString("ListData");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取基础数据失败-----"+e.getMessage());
		}
		return null;
	}
	
	@Override
	public void saveUserToPassport() {
	   //得到所有的直属教育局和学校
	   Set<String> unitList =  new HashSet<String>();
	   List<Unit> parList = SUtils.dt(unitRemoteService.findByParentId("89ac2bfba65244cda3e137be53c02940"), Unit.class);
//	   Set<String> uidSet1 = EntityUtils.getSet(parList, Unit::getId);
	   Set<String> paSet = new HashSet<String>();
	   for (Unit unit : parList) {
		   if(unit.getUnitClass() == 1){
			   paSet.add(unit.getId());
		   }
	   }
	   List<Unit> parUnitList = SUtils.dt(unitRemoteService.findByParentIdAndUnitClass
			   (paSet.toArray(new String[paSet.size()]),1,2), Unit.class);
	   Set<String> uidSet = EntityUtils.getSet(parUnitList, Unit::getId);
//	   unitList.add("89ac2bfba65244cda3e137be53c02940");
//	   unitList.addAll(uidSet1);
	   unitList.addAll(uidSet);
	   
//	   Set<String> uidSet = EntityUtils.getSet(parList, Unit::getId);  1669
//	   uidSet.add("9e2346c5c08d4f9c9a880a8a6d220f0b");
       List<User> saveUser = SUtils.dt(userRemoteService.findByUnitIds(unitList.toArray(new String[unitList.size()])), User.class);
       //超过1000的情况
		List<User> teList;
		if (CollectionUtils.isNotEmpty(saveUser)) {
           if ( saveUser.size() > 1000 ) {
               List<User> allTeaId1 = Lists.newArrayList(saveUser);
               int loopNumber = saveUser.size()/1000;
               for (int i=0; i<(loopNumber+1); i++ ) {
	        	   if ( i+1 == (loopNumber+1) &&  allTeaId1.size() -(1000 * loopNumber) > 0) {
	        		   teList = allTeaId1.subList((i) * 1000, allTeaId1.size());
	        	   }else{
	        		   teList = allTeaId1.subList(i * 1000, (i+1)*1000);
	        	   }
                   System.out.println(teList.size()+ "----------");
                   baseSyncSaveService.saveUserToPassport(teList);
               }
           } else {
           	teList = Lists.newArrayList(saveUser);
            System.out.println(teList.size()+ "----------");
//           	baseSyncSaveService.saveUserToPassport(saveUser);
           }
       }
       
       System.out.println("用户的数据是-------" + saveUser.size());
//       baseSyncSaveService.saveUserToPassport(saveUser);
	}


	@Override
	public void pushUnit(String url) {
	   // TODO Auto-generated method stub]
		String key;
		String parentId;
	    if(GyBaseConstant.GYCX_PLATFORM_URL.equals(url)){
	    	key = GyBaseConstant.CANGXI_SCHOOL_REDIS_KEY;
	    	parentId = GyBaseConstant.GY_CX_JYJ_UNIT_ID;
		}else{
			key = GyBaseConstant.CHAOTIAN_SCHOOL_REDIS_KEY;
			parentId = GyBaseConstant.GY_CT_JYJ_UNIT_ID;
		}
	   String time = getModifyTime(key);
	   SimpleDateFormat sdf = getDataFormat();
	   //得到所有的直属教育局和学校
	   List<Unit> parList = SUtils.dt(unitRemoteService.findByParentId(parentId), Unit.class);
	   Set<String> paSet = new HashSet<String>();
	   for (Unit unit : parList) {
		   if(unit.getUnitClass() == 1){
			   paSet.add(unit.getId());
		   }
	   }
	   paSet.add(parentId);
	   List<Unit> parUnitList = SUtils.dt(unitRemoteService.findByParentIdAndUnitClass
			   (paSet.toArray(new String[paSet.size()]),1,2), Unit.class);
	   Set<String> uidSet = EntityUtils.getSet(parUnitList, Unit::getId);
	   
	   List<School> schoolList = SUtils.dt(schoolRemoteService.findListByIds(uidSet.toArray(new String[uidSet.size()])),
			   School.class);
	   Map<String, String> schoolCodeMap = EntityUtils.getMap(schoolList, School::getId, School::getSchoolCode);
	    //根据时间来区分调用那个接口
	   String endTime = time;
	   List<Unit> addUnits = new ArrayList<Unit>();
	   List<Unit> upUnits = new ArrayList<Unit>();
	   List<Unit> deUnits = new ArrayList<Unit>();
	   if(CollectionUtils.isNotEmpty(parUnitList)){
		   for (Unit unit : parUnitList) {
			   String creDate = sdf.format(unit.getCreationTime());
			   String modDate = sdf.format(unit.getModifyTime());
			   if("9bc8ace466c848b48fec956cec9aebef".equals(unit.getId())){
				   if(time.compareTo(creDate) < 0 && unit.getIsDeleted() == BaseSaveConstant.DEFAULT_IS_DELETED_VALUE){
					   addUnits.add(unit);
				   }else if (time.compareTo(modDate) < 0 && unit.getIsDeleted() == BaseSaveConstant.DEFAULT_IS_DELETED_VALUE) {
					   upUnits.add(unit);
				   }else if (unit.getIsDeleted() == BaseSaveConstant.TRUE_IS_DELETED_VALUE) {
					   deUnits.add(unit);
				   }
				   if(endTime.compareTo(modDate) < 0)
					   endTime = modDate;
			   }
		   }
	   }
	   
	  //调用接口来保存数据
	  boolean isTrueSave = Boolean.FALSE;
	  if(CollectionUtils.isNotEmpty(addUnits)){
		  String json = doDateUnitCharge(addUnits,schoolCodeMap);
		  System.out.println(json);
		  isTrueSave = doSaveUnitDate(url,GyBaseConstant.GY_DELETE_TYPE_VAL,json);
		  System.out.println(isTrueSave);
	  }
	  
	  if(isTrueSave){
		  if(CollectionUtils.isNotEmpty(upUnits)){
			  String json = doDateUnitCharge(upUnits,schoolCodeMap);
			  isTrueSave = doSaveUnitDate(url,GyBaseConstant.GY_UPDATE_TYPE_VAL,json);
		  }
	  }
	  
	  if(isTrueSave){
		  if(CollectionUtils.isNotEmpty(deUnits)){
			  String json = doDateUnitCharge(deUnits,schoolCodeMap);
			  isTrueSave = doSaveUnitDate(url,GyBaseConstant.GY_DELETE_TYPE_VAL,json);
		  }
	  }
	  //保存更新的最后时间
	  if(isTrueSave){
		  time = endTime;
		  RedisUtils.set(key, time);
		  System.out.println("这次更新的最后时间是--------" + time + "-------------");
	  }
	}

	@Override
	public void pushUser(String url) {
		// TODO Auto-generated method stub
		String key;
		String parentId;
	    if(GyBaseConstant.GYCX_PLATFORM_URL.equals(url)){
	    	key = GyBaseConstant.CANGXI_USER_REDIS_KEY;
	    	parentId = GyBaseConstant.GY_CX_JYJ_UNIT_ID;
		}else{
			key = GyBaseConstant.CHAOTIAN_USER_REDIS_KEY;
			parentId = GyBaseConstant.GY_CT_JYJ_UNIT_ID;
		}
	   parentId = "402896E94700810F0147008B2FF30010";
	   String time = getModifyTime(key);
	   SimpleDateFormat sdf = getDataFormat();
	   Date mDate = null;
		try {
			mDate = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   //得到所有的直属教育局和学校
	   List<Unit> parList = SUtils.dt(unitRemoteService.findByParentId(parentId), Unit.class);
	   Set<String> paSet = new HashSet<String>();
	   for (Unit unit : parList) {
		   if(unit.getUnitClass() == 1){
			   paSet.add(unit.getId());
		   }
	   }
	   paSet.add(parentId);
	   List<Unit> parUnitList = SUtils.dt(unitRemoteService.findByParentIdAndUnitClass
			   (paSet.toArray(new String[paSet.size()]),1,2), Unit.class);
	   Set<String> uidSet = EntityUtils.getSet(parUnitList, Unit::getId);
	   
	   List<User> allUserList = SUtils.dt(userRemoteService.findByOwnerTypeAndModifyTimeGreaterThan(User.OWNER_TYPE_TEACHER,mDate,
			   uidSet.toArray(new String[uidSet.size()])), User.class);
	   
	   String endTime = time;
	   List<User> addUsers = new ArrayList<>();
	   List<User> upUsers = new ArrayList<>();
	   List<User> deUsers = new ArrayList<>();
	   if(CollectionUtils.isNotEmpty(allUserList)){
		   for (User user : allUserList) {
			   String creDate = sdf.format(user.getCreationTime());
			   String modDate = sdf.format(user.getModifyTime());
			   if("466E835011FE4C61A35E3199161701EF".equals(user.getId())){
				   if(time.compareTo(creDate) < 0 && user.getIsDeleted() == BaseSaveConstant.DEFAULT_IS_DELETED_VALUE){
					   addUsers.add(user);
				   }else if (time.compareTo(modDate) < 0 && user.getIsDeleted() == BaseSaveConstant.DEFAULT_IS_DELETED_VALUE) {
					   upUsers.add(user);
				   }else if (user.getIsDeleted() == BaseSaveConstant.TRUE_IS_DELETED_VALUE) {
					   deUsers.add(user);
				   }
				   if(endTime.compareTo(modDate) < 0)
					   endTime = modDate;
			   }
		   }
	   }
	   
	   //调用接口来保存数据
	  boolean isTrueSave = Boolean.FALSE;
	  if(CollectionUtils.isNotEmpty(addUsers)){
		  String json = doDateUserCharge(addUsers);
		  System.out.println(json);
		  isTrueSave = doSaveUserDate(url,GyBaseConstant.GY_ADD_TYPE_VAL,json);
	  }
	  if(isTrueSave){
		  if(CollectionUtils.isNotEmpty(upUsers)){
			  String json = doDateUserCharge(upUsers);
//				  isTrueSave = doSaveUserDate(url,GyBaseConstant.GY_UPDATE_TYPE_VAL,json);
		  }
	  }
	  if(isTrueSave){
		  if(CollectionUtils.isNotEmpty(deUsers)){
			  String json = doDateUserCharge(deUsers);
//				  isTrueSave = doSaveUserDate(url,GyBaseConstant.GY_DELETE_TYPE_VAL,json);
		  }
	  }
	  //保存更新的最后时间
	  if(isTrueSave){
		  time = endTime;
		  RedisUtils.set(key, time);
		  System.out.println("这次更新的最后时间是--------" + time + "-------------");
	  }
	}
	//----------------------公共基础代码 ---------------------------------
	//32位id格式转化9bc8ace4-66c8-48b4-8fec-956cec9aebef
	private String doChargeId(String id){
		StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id.substring(0, 8));
        stringBuilder.append("-");
        stringBuilder.append(id.substring(8, 12));
        stringBuilder.append("-");
        stringBuilder.append(id.substring(12, 16));
        stringBuilder.append("-");
        stringBuilder.append(id.substring(16, 20));
        stringBuilder.append("-");
        stringBuilder.append(id.substring(20));
        return stringBuilder.toString();
	}
	
	private boolean doSaveUnitDate(String url,String type, String json) {
		ExternalInterfaceSoap soap = getSoap(url);
		String token = getToken(soap);
		System.out.println("token的值是：---" + token);
		boolean isTrueSave = Boolean.FALSE;
		try {
			if(StringUtils.isNotBlank(token)){
				String dataString = soap.synchroUnit(json, type, token);
				JSONArray array = Json.parseArray(dataString);
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String state = js.getString("State");
					if(GyBaseConstant.GY_TRUE_GET_RESULT.equals(state)){
						isTrueSave = Boolean.TRUE;
					}else{
						log.error("把单位数据推送失败-----"+dataString);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("把单位数据推送失败-----"+e.getMessage());
		}
		return isTrueSave;
	}
	
	private String doDateUnitCharge(List<Unit> addUnits, Map<String, String> schoolCodeMap) {
		Map<String, String> schoolTypeMap = new HashMap<String, String>();
		schoolTypeMap.put("211", "中心校（完小）");
		schoolTypeMap.put("311", "初级中学");
		schoolTypeMap.put("111", "幼儿园");
		schoolTypeMap.put("312", "九年一贯制");
		schoolTypeMap.put("342", "高级中学");
		schoolTypeMap.put("365", "中职学校");
		schoolTypeMap.put("341", "完全中学");
		  JSONArray jsonArray = new JSONArray();
		  for (Unit unit : addUnits) {
			  JSONObject jsonObject = new JSONObject();
			  String unitId = unit.getId();
			  jsonObject.put("deptid", doChargeId(unitId));
			  String schoolCode = "510812000";
			  if(schoolCodeMap.get(unitId) != null){
				  schoolCode = schoolCodeMap.get(unitId);
			  }
			  jsonObject.put("depeno", schoolCode);
			  jsonObject.put("deptparentid", doChargeId(unit.getParentId()));
			  jsonObject.put("deptname", unit.getUnitName());
//			  jsonObject.put("deptparentno", "510812");  //朝天
			  jsonObject.put("depttype", "学校级(含直属校)");
			  String schoolType = unit.getSchoolType();
			  if(StringUtils.isNotBlank(schoolType)){
				  schoolType = schoolTypeMap.get(schoolType);
			  }else{
				  schoolType = "中心校（完小）";
			  }
			  jsonObject.put("schooltype", schoolType);
			  jsonArray.add(jsonObject);
		  }
		 return jsonArray.toJSONString();
	}
	
	private boolean doSaveUserDate(String url,String type, String json) {
		ExternalInterfaceSoap soap = getSoap(url);
		String token = getToken(soap);
		boolean isTrueSave = Boolean.FALSE;
		try {
			if(StringUtils.isNotBlank(token)){
				String dataString = soap.synchroUser(json, type, token);
				JSONArray array = Json.parseArray(dataString);
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String state = js.getString("State");
					if(GyBaseConstant.GY_TRUE_GET_RESULT.equals(state)){
						isTrueSave = Boolean.TRUE;
					}else{
						log.error("把用户数据推送失败-----"+dataString);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("把用户数据推送失败-----"+e.getMessage());
		}
		return isTrueSave;
	}
	
	private String doDateUserCharge(List<User> addUser) {
		  JSONArray jsonArray = new JSONArray();
		  for (User user : addUser) {
			  JSONObject jsonObject = new JSONObject();
			  String userid = user.getId();
			  jsonObject.put("UserID", doChargeId(userid));
			  jsonObject.put("Loginid", user.getUsername());
			  jsonObject.put("Name", user.getRealName());
			  jsonObject.put("LoginPwd", PWD.decode(user.getPassword()));
			  jsonObject.put("Sex", user.getSex());
			  jsonObject.put("IdcardNo", user.getIdentityCard());
			  jsonObject.put("DeptNo", "");
			  jsonObject.put("DeptID", doChargeId("016a13440fe047779a1e9aaeb3642878"));
			  Integer userType = user.getUserType();
			  if(User.USER_TYPE_UNIT_ADMIN == userType){
				  userType = GyBaseConstant.GY_USER_ADMIN_TYPE_VAL;
			  }else{
				  userType = GyBaseConstant.GY_USER_TEACHER_TYPE_VAL;
			  }
			  jsonObject.put("UserType", userType);
			  jsonObject.put("GradeName", "");
			  jsonObject.put("ClassName", "");
			  jsonObject.put("Tel", user.getMobilePhone());
			  jsonObject.put("Email", user.getEmail());
			  jsonObject.put("QQ", user.getQq());
			  
			  jsonArray.add(jsonObject);
		  }
		 return jsonArray.toJSONString();
	}
	
	private String getModifyTime(String key) {
		String time = RedisUtils.get(key);
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		return time;
	}
	
	private SimpleDateFormat getDataFormat() {
		return new SimpleDateFormat("yyyyMMddHHmmss");
	}
		
	private static ExternalInterfaceSoap getSoap(String url) {
		URL uri = null;
		try {
			uri = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ExternalInterface externalInterface1 =  new ExternalInterface(uri);
		ExternalInterfaceSoap soap = externalInterface1.getExternalInterfaceSoap();
		return soap;
	}

	private String getToken(ExternalInterfaceSoap soap1) {
		String token = null;
		try {
			MySoapHeader header = new MySoapHeader();
			header.setUserName(GyBaseConstant.GY_LOGIN_USERNAME);
			header.setPassWord(GyBaseConstant.GY_LOGIN_PASSWORD);
			String result = soap1.checkLogin(header);
			JSONArray array = Json.parseArray(result);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				if(GyBaseConstant.GY_TRUE_GET_RESULT.equals(js.getString("State"))){
					token = js.getString("ListData");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取token失败----"+ e.getMessage());
		}
		return token;
	}
	
	//密码解密
	private String decryptPwd(String pwd, ExternalInterfaceSoap saSoap,String token){
		return saSoap.decryptString(pwd, token);
	}
}
