package net.zdsoft.desktop.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.entity.UserAhSync;
import net.zdsoft.basedata.remote.UnionCodeAlreadyExistsException;
import net.zdsoft.basedata.remote.UsernameAlreadyExistsException;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserAhSyncRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.service.SyncAnhuidataService;
import net.zdsoft.desktop.utils.AhPage;
import net.zdsoft.desktop.utils.ShieldSyncApp_szxy;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.power.constant.PowerConstant;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.RolePermission;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.RolePermissionRemoteService;
import net.zdsoft.system.remote.service.RoleRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.remote.service.UserRoleRemoteService;

@Service
public class SyncAnhuidataServiceImpl implements SyncAnhuidataService{
	
	private Logger log = Logger.getLogger(SyncAnhuidataServiceImpl.class);
	
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private BaseSyncSaveRemoteService baseSyncSaveRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private RoleRemoteService roleRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UserRoleRemoteService UserRoleRemoteService;
	@Autowired
	private UserAhSyncRemoteService userAhSyncRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private ModelRemoteService modelRemoteService;
	@Autowired
	private RolePermissionRemoteService rolePermissionRemoteService;
	
	/**同步学校、单位、部门、年级、班级
	 * @throws UnsupportedEncodingException 
	 * @throws UnionCodeAlreadyExistsException 
	 * @throws UsernameAlreadyExistsException **/
	private void syncUnitDate(String ahUnitId,ShieldSyncApp_szxy shieldSyncApp,
			List<UserAhSync> syncs,Map<String, String> ahObjectIdMap,Set<String> classIdSet) 
			throws UnsupportedEncodingException, UsernameAlreadyExistsException, UnionCodeAlreadyExistsException {
		//获得安徽学校信息 
		String ahSchool = new String(shieldSyncApp.getSchool(ahUnitId).getBody(), "UTF-8");
		JSONObject roleObj =  JSONObject.parseObject(ahSchool);
		JSONObject json = JSONObject.parseObject(roleObj.getString("data"));
		String uuId = UuidUtils.generateUuid();
		School school=new School();
		school.setId(uuId);
		school.setSchoolName(json.getString("schoolName"));
		school.setSchoolCode(json.getString("schoolCode"));
		school.setAddress(json.getString("address"));
		StringBuilder str = new StringBuilder();
		if(json.getString("phaseCode").contains("02")) {
			str.append("0,");
		}
		if(json.getString("phaseCode").contains("03")) {
			str.append("1,");
		}
		if(json.getString("phaseCode").contains("04")) {
			str.append("2,");
		}
		if(json.getString("phaseCode").contains("05")) {
			str.append("3,");
		}
		school.setSections(str.substring(0, str.length()-1));
		String schoolType = doChargeSchoolType(school.getSections());
		school.setSchoolType(schoolType);
		String areaId=json.getString("areaId");
		String areaStr = new String(shieldSyncApp.getAreaById(areaId).getBody(), "UTF-8");
		if(StringUtils.isNotBlank(areaStr)){
			JSONObject areaObj =  JSONObject.parseObject(areaStr);
			JSONObject areaJson = JSONObject.parseObject(areaObj.getString("data"));
			school.setRegionCode(areaJson.getString("areaCode"));
		}
		school.setBuildDate(new Date());
		school.setIsDeleted(0);
		school.setEventSource(getDefaultSource());
		school.setEmail(json.getString("email"));
		school.setCreationTime(new Date());
		school.setModifyTime(new Date());
		System.out.println(school.getId());
		Unit u = new Unit();
		u.setAhUnitId(json.getString("id"));
		u.setId(uuId);
		u.setEventSource(getDefaultSource());
		u.setOrgVersion(1);
		u.setUnitState(Unit.UNIT_MARK_NORAML);
		u.setAuthorized(1);
		u.setUseType(1);
		u.setCreationTime(new Date());
		u.setRegionCode(school.getRegionCode());
		String regionCode = StringUtils.substring(u.getRegionCode(), 0, 6);
//		u.setParentId(Constant.GUID_ONE);
		//先定死一个教育局的id
//		u.setParentId("15445527248596166816367689119154");//ces
//		u.setParentId("402896E94700810F0147008B2FF30010");//本地
//		u.setParentId("15440827943011023062241308832050");//美视
		u.setParentId("40288115584281750158A8B0222B00DD");
		Long max = RedisUtils.incrby("syncdata.ahdy.unit.unioncode.max.regioncode." + u.getRegionCode(), 1);
		if(StringUtils.isEmpty(regionCode)) {
			u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
		}else {
			u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
		}
		u.setDisplayOrder(u.getUnionCode());
		u.setRegionLevel(
				StringUtils.endsWith(regionCode, "0000") ? 3 : StringUtils.endsWith(regionCode, "00") ? 4 : 5);
		u.setUnitName(json.getString("schoolName"));
		//默认是学校
		u.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
		u.setModifyTime(new Date());
		u.setIsDeleted(0);
		u.setSchoolType(school.getSchoolType());
		u.setRunSchoolType(school.getRunSchoolType());
		if(school.getSections().contains("0")) {
			u.setUnitType(Unit.UNIT_SCHOOL_KINDERGARTEN);
		}else {
			u.setUnitType(Unit.UNIT_SCHOOL_ASP);
		}
		String username = "ahjy_" + System.currentTimeMillis();
		unitRemoteService.createUnit(u, school, username, "zdsoft123");
		UserAhSync sync = new UserAhSync();
		sync.setId(UuidUtils.generateUuid());
		sync.setObjectId(u.getId());
		sync.setAhObjectId(ahUnitId);
		sync.setObjectType(UserAhSync.SYNC_TYPE_UNIT);
		sync.setAhUnitId(ahUnitId);
		syncs.add(sync);
//		Map<String,String> ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//		ahObjectIdMapJs.put(ahUnitId,  u.getId());
		ahObjectIdMap.put(ahUnitId,  u.getId());
//		RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
		
		//同步年级、班级数据
		List<Grade> saveGrades = new ArrayList<>();
		List<Clazz> saveClazzs = new ArrayList<>();
		String ahGradeStr = new String(shieldSyncApp.listAllPhaseGradeInSchool(ahUnitId).getBody(), "UTF-8");
		JSONObject ahGradeDataObj =  JSONObject.parseObject(ahGradeStr);
		JSONArray gradeData = JSONArray.parseArray(ahGradeDataObj.getString("data"));
		Map<String,String> codeMap=new HashMap<>();
		codeMap.put("02", "0");
		codeMap.put("03", "1");
		codeMap.put("04", "2");
		codeMap.put("05", "3");
		for (int j = 0; j < gradeData.size(); j++) {
			Calendar calendar = Calendar.getInstance();//日历对象
	        calendar.set(Calendar.MONTH, 7);
	        calendar.set(Calendar.DATE,31);
	        Integer acady=0;
			if(DateUtils.compareForDay(new Date(), calendar.getTime())<=0){
				acady=Integer.parseInt(DateUtils.date2String(new Date(), "yyyy"));
			}else{
				acady=Integer.parseInt(DateUtils.date2String(new Date(), "yyyy"))+1;
			}
			JSONObject gadeObj = gradeData.getJSONObject(j);
			String section=codeMap.get(gadeObj.getString("phaseCode"));//02,03,04
			String gradeListStr=gadeObj.getString("gradeList");
			JSONArray gradeArray = JSONArray.parseArray(gradeListStr);
			for (int k = 0; k < gradeArray.size(); k++) {
				//遍历每一个年级
				JSONObject gradeJs = gradeArray.getJSONObject(k);
//				ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
				if(ahObjectIdMap.containsKey(gradeJs.getString("id"))) {
					//同步过的年级不需要再同步
					continue;
				}
				Grade grade=new Grade();
				grade.setId(UuidUtils.generateUuid());
				grade.setGradeName(gradeJs.getString("gradeName"));
				grade.setSchoolId(uuId);
				grade.setOpenAcadyear((acady-k-1)+"-"+(acady-k));
				grade.setSection(Integer.valueOf(section));
				if(Integer.valueOf(section)==0){
					grade.setSchoolingLength(3);
				}else if(Integer.valueOf(section)==1){
					grade.setSchoolingLength(6);
				}else if(Integer.valueOf(section)==2){
					grade.setSchoolingLength(3);
				}else if(Integer.valueOf(section)==3){
					grade.setSchoolingLength(3);
				}
				grade.setCreationTime(new Date());
				grade.setModifyTime(new Date());
				grade.setGradeCode(section+(k+1));
				grade.setWeekDays(7);
				grade.setEventSource(getDefaultSource());
				saveGrades.add(grade);
				ahObjectIdMap.put(gradeJs.getString("id"), grade.getId());
//				ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//				ahObjectIdMapJs.put(gradeJs.getString("id"),grade.getId());
//				RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
				UserAhSync gradeSync = new UserAhSync();
				gradeSync.setAhObjectId(gradeJs.getString("id"));
				gradeSync.setObjectType(UserAhSync.SYNC_TYPE_GRADE);
				gradeSync.setAhUnitId(ahUnitId);
				gradeSync.setObjectId(grade.getId());
				gradeSync.setId(UuidUtils.generateUuid());
				syncs.add(gradeSync);
				//同步生成每个年级下的班级数据
				boolean canClass=true;
				int m=1;
				String classCode=acady+Integer.valueOf(section)+"";
				while(canClass){
					JSONObject param=new JSONObject();
					param.put("pageIndex", m);
					param.put("limit",500);
					param.put("orderBy","createTime");
					String classStr = new String(shieldSyncApp.listOrgClassByPhaseGradeInSchool(ahUnitId,gadeObj.getString("phaseCode"),gradeJs.getString("gradeCode"),param.toJSONString()).getBody(), "UTF-8");
					if(StringUtils.isNotBlank(classStr)&&JSONObject.parseObject(classStr).containsKey("data")&&!StringUtils.equals((JSONObject.parseObject(classStr).getString("data")),"[]")){
						JSONObject classObj =  JSONObject.parseObject(classStr);
						JSONArray classArray = JSONArray.parseArray(classObj.getString("data"));
						for(int p=0;p<classArray.size();p++){
							JSONObject classJs = classArray.getJSONObject(p);
//							ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
							if(ahObjectIdMap.containsKey(classJs.getString("id"))) {
								//同步过的班级不需要再同步
								continue;
							}
							Clazz clazz=new Clazz();
							clazz.setId(UuidUtils.generateUuid());
							clazz.setSchoolId(uuId);
							if(StringUtils.isNotBlank(classJs.getString("className"))&&classJs.getString("className").length()>15){
								String className=StringUtils.substring(classJs.getString("className"), 0, 15);
								clazz.setClassName(className);
							}else{
								clazz.setClassName(classJs.getString("className"));
							}
							clazz.setAcadyear((acady-k-1)+"-"+(acady-k));
							clazz.setSchoolingLength(grade.getSchoolingLength());
							clazz.setGradeId(grade.getId());
							clazz.setSection(grade.getSection());
							clazz.setArtScienceType(0);
							clazz.setCreationTime(new Date());
							clazz.setModifyTime(new Date());
							clazz.setEventSource(getDefaultSource());
							// 班级编号+1
							String nextClassCode = incrementString(classCode);
							clazz.setClassCode(nextClassCode);
							clazz.setIsGraduate(0);
							classIdSet.add(classJs.getString("id"));
							saveClazzs.add(clazz);
							ahObjectIdMap.put(classJs.getString("id"), clazz.getId());
//							ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//							ahObjectIdMapJs.put(classJs.getString("id"), clazz.getId());
//							RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
							
							UserAhSync clsSync = new UserAhSync();
							clsSync.setAhObjectId(classJs.getString("id"));
							clsSync.setObjectType(UserAhSync.SYNC_TYPE_CLASS);
							clsSync.setAhUnitId(ahUnitId);
							clsSync.setObjectId(clazz.getId());
							clsSync.setId(UuidUtils.generateUuid());
							syncs.add(clsSync);
						}
					}else{
						canClass=false;
					}
					m++;
				}
				
			}
		}
		
		if(CollectionUtils.isNotEmpty(saveGrades)){
			gradeRemoteService.saveAll(SUtils.s(saveGrades.toArray(new Grade[0])));
		}
		System.out.println("保存年级数据是：-------------------" + saveGrades.size());
		if(CollectionUtils.isNotEmpty(saveClazzs)){
			classRemoteService.saveAll(SUtils.s(saveClazzs.toArray(new Clazz[0])));
		}
		System.out.println("保存班级数据是：-------------------" + saveClazzs.size());
		
	}
	
	private static List<RolePermission> initRoleMould(Set<Integer> modIds,String roleId) {
		// 默认角色绑定系统管理模块
//		Integer[] selectedModSet = new Integer[] {86300, 86301, 86302, 86303, 3000, 3001, 990561, 3003, 99007, 3004, 3005, 3006, 507, 34028, 509, 99019, 99001, 99010, 99029, 3509, 99011};
		List<RolePermission> rolePermList = new ArrayList<>();
		for (int moduleId : modIds) {
			RolePermission rolePerm = new RolePermission();
			rolePerm.setRoleId(roleId);
			rolePerm.setModuleId(moduleId+"");
			rolePerm.setId(UuidUtils.generateUuid());
			rolePermList.add(rolePerm);
		}
		return rolePermList;
	}
	
	@Override
	public void syncData(String ahUserId,String[] ahUnitIds) throws UnsupportedEncodingException{
		String teaPwd = systemIniRemoteService.findValue("SYSTEM.TEACHER.PASSWORD");//教职工
		ShieldSyncApp_szxy shieldSyncApp=new ShieldSyncApp_szxy();
		if(ahUnitIds==null || ahUnitIds.length == 0) {
			return;
		}
		List<UserAhSync> syncAhUnitData = 
				SUtils.dt(userAhSyncRemoteService.findByObjectType(UserAhSync.SYNC_TYPE_UNIT), new TR<List<UserAhSync>>() {});
				
				
		//key:ahUnitId value:unitId
		Map<String, String> ahUnitIdMap = EntityUtils.getMap(syncAhUnitData, UserAhSync::getAhObjectId,UserAhSync::getObjectId);
		//此map用来保存第三方的id在eis中的对应id
		Map<String, String> ahObjectIdMap = new HashMap<>();
//		Map<String, String> ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//		if(MapUtils.isEmpty(ahObjectIdMapJs)){
//			ahObjectIdMapJs=new HashMap<>();
//		}
		for (String objId : ahUnitIdMap.keySet()) {
//			ahObjectIdMapJs.put(objId, ahUnitIdMap.get(objId));
			ahObjectIdMap.put(objId, ahUnitIdMap.get(objId));
		}
//		RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
		
		Set<String> anClassIdSet=new HashSet<>();
		//同步单位优先
		List<UserAhSync> syncs = new ArrayList<>();
		for (String ahUnitId : ahUnitIds) {
			if(ahUnitIdMap.containsKey(ahUnitId)) {
				//单位已经同步过就不需要再同步
				System.out.println("当前单位已经同步过了，不需要同步");
				continue;
			}
			System.out.println("当前单位没有同步过，同步学校、单位管理员、部门、年级、班级信息");
			try {
				syncUnitDate(ahUnitId,shieldSyncApp,syncs,ahObjectIdMap,anClassIdSet);
				//处理角色
				List<Model> models = SUtils.dt(modelRemoteService.findListBySubId(99),new TR<List<Model>>() {});
				Set<Integer> modIds = new HashSet<>();
				for (Model m : models) {
					if(m.getMark() == 1 && m.getUnitClass() == 2) {
						modIds.add(m.getId());
					}
				}
//				ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
				List<Role> roleList = SUtils.dt(roleRemoteService.findByUnitIdAndRoleType(ahObjectIdMap.get(ahUnitId),
		 				PowerConstant.ROLE_TYPE_OPER),Role.class);
				Role role =null;
				for (Role r : roleList) {
					if(StringUtils.equals("default", r.getIdentifier())) {
						role = r;
						break;
					}
				}
				List<RolePermission> rolePermList = initRoleMould(modIds, role.getId());
				role.setSubSystem("99");
				if(CollectionUtils.isNotEmpty(rolePermList)) {
					//TODO
					rolePermissionRemoteService.saveAll(rolePermList.toArray(new RolePermission[0]));
					roleRemoteService.save(role);
				}
			} catch (UsernameAlreadyExistsException | UnionCodeAlreadyExistsException e) {
				System.out.println("同步当前单位报错：ahUnitId"+ahUnitId);
				e.printStackTrace();
			}
			System.out.println("单位同步完成");
		}
		//同步当前用户的信息
		String ahSyncUnitId = ahUnitIds[0];
		List<UserAhSync> syncAhData = 
				SUtils.dt(userAhSyncRemoteService.findByAhUnitId(ahSyncUnitId), new TR<List<UserAhSync>>() {});
//		ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
		for (UserAhSync userAhSync : syncAhData) {
//			ahObjectIdMapJs.put(userAhSync.getAhObjectId(), userAhSync.getObjectId());
			ahObjectIdMap.put(userAhSync.getAhObjectId(), userAhSync.getObjectId());
		}
//		RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
//		Map<String,String> anhuiCountMapJs=RedisUtils.getObject("anhuiCountMap", new TR<Map<String,String>>());
		Map<String,Integer> anhuiCountMap=new HashMap<>();
//		if(MapUtils.isEmpty(anhuiCountMapJs)){
//			anhuiCountMapJs=new HashMap<>();//过滤用户名一样的情况
//		}
//		RedisUtils.setObject("anhuiCountMap", anhuiCountMapJs);
		Set<String> ahTeaUserIds = new HashSet<>();
		List<String> ahTeaUserIds2 = new ArrayList<>();
		AhPage ahPage = new AhPage();
		ahPage.setLimit(500);
		ahPage.setOrderBy("createTime");
		int countTea = 0;
		for(;true;) {
			ahPage.setPageIndex(ahPage.getPageIndex()+1);
			String ahTeaListStr = new String(shieldSyncApp.listOrgTeacherInSchool(ahSyncUnitId,SUtils.s(ahPage)).getBody(), "UTF-8");
			JSONObject ahTeaListBody =  JSONObject.parseObject(ahTeaListStr);
			JSONArray ahTeaArray = JSONObject.parseArray(ahTeaListBody.getString("data"));
			for(int j=0;j<ahTeaArray.size();j++){
				JSONObject ahTeaUser = ahTeaArray.getJSONObject(j);
				String ahId = ahTeaUser.getString("id");
				ahTeaUserIds.add(ahId);
				if(!ahObjectIdMap.containsKey(ahId)) {
					ahTeaUserIds2.add(ahId);
				}
			}
			countTea = countTea + ahTeaArray.size();
			if(ahTeaArray.size()<500) {
				break;
			}
		}
		System.out.println("该单位共教师有："+countTea+"条;"+ahTeaUserIds2.size()+"条教师数据待同步");
//		ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
		if(ahObjectIdMap.containsKey(ahUserId)) {
			System.out.println("当前用户已经同步过了，直接去登录");
		}else {
			System.out.println("开始同步当前登录用户信息");
			//获得同步单位的教师
			if(StringUtils.isNotBlank(ahUserId)) {
				if(ahTeaUserIds.contains(ahUserId)) {
					//当前用户是教师
					System.out.println("用户为教师身份");
					syncTeaUserData(new String[]{ahUserId},ahSyncUnitId,shieldSyncApp,syncs,ahObjectIdMap,teaPwd,anhuiCountMap);
				}else {
					//当前用户是学生
					System.out.println("用户为学生身份");
					syncStuUserData(new String[]{ahUserId},ahSyncUnitId,shieldSyncApp,syncs,ahObjectIdMap,null,null);
				}
				System.out.println("当前同步用户已经成功");
			}
		}
		if(CollectionUtils.isNotEmpty(syncs)) {
			System.out.println("保存同步表信息："+syncs.size()+"条");
			userAhSyncRemoteService.saveAll(syncs.toArray(new UserAhSync[] {}));
		}
		//TODO 另外开一个线程，单独同步剩下的该单位用户
		List<List<String>> resultList=null;
		resultList=averageNum(new ArrayList<String>(ahTeaUserIds2), 5);//创建5个线程
		List<List<String>> classList=null;
		classList=averageNum(new ArrayList<String>(anClassIdSet), 5);//创建5个线程
//		SyncUserTask syncUser = new SyncUserTask(shieldSyncApp, ahSyncUnitId, ahTeaUserIds, ahObjectIdMap, userAhSyncRemoteService,anClassIdSet,countMap);
//		syncUser.start();
		String taskNumStr  =RedisUtils.get("ahTaskNum_"+ahSyncUnitId);
		if(StringUtils.isBlank(taskNumStr) || "0".equals(taskNumStr)) {
			//没有子线程在跑
			RedisUtils.set("ahTaskNum_"+ahSyncUnitId, resultList.size()+classList.size()+"");
			for (List<String> list : resultList) {
				System.out.println("教师被分配的数目:"+list.size());
				SyncUserTask syncUser = new SyncUserTask(shieldSyncApp, ahSyncUnitId, new HashSet(list), ahObjectIdMap, userAhSyncRemoteService,null,anhuiCountMap);
				syncUser.start();
			}
			for (List<String> list : classList) {
				SyncUserTask syncUser = new SyncUserTask(shieldSyncApp, ahSyncUnitId, null, ahObjectIdMap, userAhSyncRemoteService,new HashSet(list),anhuiCountMap);
				syncUser.start();
			}
		}
	}
	
	public class SyncUserTask extends Thread {
    	
        private ShieldSyncApp_szxy shieldSyncApp;
        private String ahUnitId;
        private Set<String> ahTeaUserIds;
        private Map<String, String> ahObjectIdMap;
        private UserAhSyncRemoteService userAhSyncRemoteService;
        private Set<String> ahClsIds;//需要同步学生的安徽班级id
        private Map<String,Integer> countMap;
        
        
        public SyncUserTask(ShieldSyncApp_szxy shieldSyncApp,String ahUnitId,
        		Set<String> ahTeaUserIds,Map<String, String> ahObjectIdMap,
        		UserAhSyncRemoteService userAhSyncRemoteService,Set<String> ahClsIds,Map<String,Integer> countMap) {
            this.shieldSyncApp = shieldSyncApp;
            this.ahUnitId = ahUnitId;
            this.ahTeaUserIds = ahTeaUserIds;
            this.userAhSyncRemoteService = userAhSyncRemoteService;
            this.ahClsIds=ahClsIds;
            this.ahObjectIdMap=ahObjectIdMap;
            this.countMap=countMap;
        }

        public void run() {
        	String teaPwd = systemIniRemoteService.findValue("SYSTEM.TEACHER.PASSWORD");//教职工
        	List<UserAhSync> syncs = new ArrayList<>();
        	if(CollectionUtils.isNotEmpty(ahTeaUserIds)) {
        		//同步id
    			System.out.println(Thread.currentThread().getName()+"同步教师用户数目："+ahTeaUserIds.size());
				syncTeaUserData(ahTeaUserIds.toArray(new String[0]),ahUnitId,shieldSyncApp,syncs,ahObjectIdMap,teaPwd,countMap);
				System.out.println(Thread.currentThread().getName()+":教师用户同步成功");
        	}
        	//TODO 同步学生
        	if(CollectionUtils.isNotEmpty(ahClsIds)) {
    			System.out.println("同步班级数目："+ahClsIds.size());
    			syncStuUserData(null, ahUnitId, shieldSyncApp, null, ahObjectIdMap, ahClsIds,countMap);
        	}
        	//同步表信息保存
        	userAhSyncRemoteService.saveAll(syncs.toArray(new UserAhSync[] {}));
        	String taskNumStr  =RedisUtils.get("ahTaskNum_"+ahUnitId);
        	if(StringUtils.isNotBlank(taskNumStr)) {
        		int taskNum = org.apache.commons.lang3.math.NumberUtils.toInt(taskNumStr);
        		RedisUtils.set("ahTaskNum_"+ahUnitId,taskNum-1+"");
        	}
        } 
    }
    
    public  List<List<String>> averageNum(ArrayList<String> source,int n){
	    List<List<String>> result=new ArrayList<List<String>>();  
	    if(source.size()>=n){
	    	int remaider=source.size()%n;  //(先计算出余数)  
	    	int number=source.size()/n;  //然后是商  
	    	int offset=0;//偏移量  
	    	for(int i=0;i<n;i++){  
	    		List<String> value=null;  
	    		if(remaider>0){  
	    			value=source.subList(i*number+offset, (i+1)*number+offset+1);  
	    			remaider--;
	    			offset++;  
	    		}else{  
	    			value=source.subList(i*number+offset, (i+1)*number+offset);  
	    		}  
	    		result.add(value);  
	    	}  
	    }else{
	    	result.add(source);
	    }
	    return result;  
	} 
	
	
	//同步学生数据
	private void syncStuUserData(String[] ahUserIds, String ahSyncUnitId, ShieldSyncApp_szxy shieldSyncApp,
			List<UserAhSync> syncs, Map<String, String> ahObjectIdMap,Set<String> anClassIdSet,Map<String,Integer> countMap) {
		
		if(CollectionUtils.isNotEmpty(anClassIdSet)){
			String stuStr="";//生成学生
			for (String clazzId : anClassIdSet) {
				List<Student> saveStudents=new ArrayList<>();
				List<User> classUsers=new ArrayList<>();
				List<UserAhSync> desSyncs=new ArrayList<>();
				int n=1;
				boolean canStu=true;
				while(canStu){
					JSONObject param=new JSONObject();
					param.put("pageIndex", n);
					param.put("limit",500);
					param.put("orderBy","createTime");
					try {//教师端
						stuStr = new String(shieldSyncApp.listOrgStudentInOrgClass(clazzId,param.toJSONString()).getBody(), "UTF-8");
						if(StringUtils.isNotBlank(stuStr)&&JSONObject.parseObject(stuStr).containsKey("data")&&!StringUtils.equals((JSONObject.parseObject(stuStr).getString("data")),"[]")){
							JSONObject stuObj =  JSONObject.parseObject(stuStr);
							JSONArray stuArray = JSONArray.parseArray(stuObj.getString("data"));
							if(stuArray.size()<500) {
								canStu=false;
							}
							String teaPwd = systemIniRemoteService.findValue("SYSTEM.TEACHER.PASSWORD");
							for(int p=0;p<stuArray.size();p++){
								JSONObject stuJs = stuArray.getJSONObject(p);
//								Map<String,String> ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
								if(StringUtils.equals("1", stuJs.getString("delFlag"))||ahObjectIdMap.containsKey(stuJs.getString("id"))){
									continue;
								}
								Student student=new Student();
								student.setId(UuidUtils.generateUuid());
								student.setSchoolId(ahObjectIdMap.get(ahSyncUnitId));
								student.setClassId(ahObjectIdMap.get(clazzId));
								student.setStudentName(StringUtils.isBlank(stuJs.getString("userName"))?stuJs.getString("loginName"):stuJs.getString("userName"));
								student.setIdentityCard(stuJs.getString("idCardNo"));
								student.setStudentCode(stuJs.getString("classNo"));
								Integer sex=0;
								if(stuJs.getInteger("genderCode")!=null){
									if(stuJs.getInteger("genderCode")==0){
										sex=2;
									}else{
										sex=stuJs.getInteger("genderCode");
									}
								}
								student.setSex(sex);
								student.setBirthday(stuJs.getDate("birthDate"));
								student.setCreationTime(new Date());
								student.setModifyTime(new Date());
								student.setEventSource(getDefaultSource());
								student.setIsLeaveSchool(0);
								student.setIsDeleted(0);
								saveStudents.add(student);
								
								User user=new User();
								user.setRealName(stuJs.getString("userName"));
								user.setAhUserId(stuJs.getString("id"));
								String userName=stuJs.getString("loginName");
								
								user.setUsername(getUserName(userName, countMap));
								
								if(StringUtils.equals(teaPwd, "0")) {
									user.setPassword("zdsoft123");
								}else if(StringUtils.equals(teaPwd, "1")){
									user.setPassword(generateTeacherUserPwd1(user.getUsername()+"987"));
								}else {
									user.setPassword("zdsoft123");
								}
								user.setIsDeleted(0);
								user.setAddress(stuJs.getString("homeAddr"));
								user.setEmail(stuJs.getString("email"));
								user.setUpPwdDate(new Date());
								user.setLoginDate(new Date());
								user.setMobilePhone(stuJs.getString("phone"));
								user.setCreationTime(new Date());
								user.setId(UuidUtils.generateUuid());
								//						user.setIdentityCard(stuJs.getString("idCardNo"));
								user.setOwnerId(student.getId());
								user.setOwnerType(User.OWNER_TYPE_STUDENT);
								user.setAccountId(UuidUtils.generateUuid());
								user.setUnitId(ahObjectIdMap.get(ahSyncUnitId));
								int codeInt = (int)((Math.random()*9+1)*100000);//随机生成6位数
								user.setDisplayOrder(codeInt);
								classUsers.add(user);
								
								ahObjectIdMap.put(stuJs.getString("id"), user.getId());
//								ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//								ahObjectIdMapJs.put(stuJs.getString("id"), user.getId());
//								RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
								
								UserAhSync sync = new UserAhSync();
								sync.setId(UuidUtils.generateUuid());
								sync.setObjectId(user.getId());
								sync.setAhObjectId(stuJs.getString("id"));
								sync.setObjectType(UserAhSync.SYNC_TYPE_USER);
								sync.setAhUnitId(ahSyncUnitId);
								desSyncs.add(sync);
								
							}
						}else{
							canStu=false;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					n++;
					//保存用户
					saveUser(classUsers.toArray(new User[0]));
					System.out.println("保存用户数据是：-------------------" + classUsers.size());
					//保存学生
					if(CollectionUtils.isNotEmpty(saveStudents)){
						studentRemoteService.saveAll(SUtils.s(saveStudents.toArray(new Student[0])));
					}
					System.out.println("保存学生数据是：-------------------" + classUsers.size());
					//保存用户信息
					if(CollectionUtils.isNotEmpty(desSyncs)){
						userAhSyncRemoteService.saveAll(desSyncs.toArray(new UserAhSync[] {}));
					}
				}
			}
		}else{
			List<UserAhSync> syncAhClassData = 
					SUtils.dt(userAhSyncRemoteService.findByObjectType(UserAhSync.SYNC_TYPE_CLASS), new TR<List<UserAhSync>>() {});
					
					
			Map<String, String> ahClassMap = EntityUtils.getMap(syncAhClassData, UserAhSync::getAhObjectId,UserAhSync::getObjectId);
			for (String objId : ahClassMap.keySet()) {
				ahObjectIdMap.put(objId, ahClassMap.get(objId));
			}
			String stuStr="";//生成学生
			String classStr="";//班级
			String anUserId = ahUserIds[0];
			try {
				String teaPwd = systemIniRemoteService.findValue("SYSTEM.TEACHER.PASSWORD");
				List<Student> saveStudents=new ArrayList<>();
				List<User> classUsers=new ArrayList<>();
				List<UserAhSync> desSyncs=new ArrayList<>();
				stuStr = new String(shieldSyncApp.getUserByUserId(anUserId).getBody(), "UTF-8");
				JSONObject param=new JSONObject();
				param.put("pageIndex", 1);
				param.put("limit",20);
				param.put("orderBy","createTime");
				classStr = new String(shieldSyncApp.listOrgClassByStudent(anUserId,param.toJSONString()).getBody(), "UTF-8");
				if(StringUtils.isNotBlank(stuStr)&&JSONObject.parseObject(stuStr).containsKey("data")&&!StringUtils.equals((JSONObject.parseObject(stuStr).getString("data")),"[]")){
					JSONObject stuObj =  JSONObject.parseObject(stuStr);
					Student student=new Student();
					student.setId(UuidUtils.generateUuid());
//					Map<String,String> ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
					student.setSchoolId(ahObjectIdMap.get(ahSyncUnitId));
					String clazzId="";
					if(StringUtils.isNotBlank(classStr)&&JSONObject.parseObject(classStr).containsKey("data")&&!StringUtils.equals((JSONObject.parseObject(classStr).getString("data")),"[]")){
						JSONObject classObj =  JSONObject.parseObject(classStr);
						JSONArray classArray = JSONArray.parseArray(classObj.getString("data"));
						out:for(int p=0;p<classArray.size();p++){
							JSONObject calssJs = classArray.getJSONObject(p);
							if(StringUtils.isNotBlank(calssJs.getString("id"))){
								clazzId=calssJs.getString("id");
								break out;
							}
						}
					}
					student.setClassId(ahObjectIdMap.get(clazzId));
					if(StringUtils.isBlank(stuObj.getString("userName"))){
						System.out
						.println("学生姓名为空");
					}
					student.setStudentName(StringUtils.isBlank(stuObj.getString("userName"))?stuObj.getString("loginName"):stuObj.getString("userName"));
					student.setIdentityCard(stuObj.getString("idCardNo"));
					Integer sex=0;
					if(stuObj.getInteger("genderCode")!=null){
						if(stuObj.getInteger("genderCode")==0){
							sex=2;
						}else{
							sex=stuObj.getInteger("genderCode");
						}
					}
					student.setSex(sex);
					student.setBirthday(stuObj.getDate("birthDate"));
					student.setCreationTime(new Date());
					student.setModifyTime(new Date());
					student.setEventSource(getDefaultSource());
					student.setIsLeaveSchool(0);
					student.setIsDeleted(0);
					saveStudents.add(student);
					
					User user=new User();
					user.setRealName(stuObj.getString("userName"));
					user.setAhUserId(stuObj.getString("id"));
					String userName=stuObj.getString("loginName");
					user.setUsername(getUserName(userName, countMap));
					
					if(StringUtils.equals(teaPwd, "0")) {
						user.setPassword("zdsoft123");
					}else if(StringUtils.equals(teaPwd, "1")){
						user.setPassword(generateTeacherUserPwd1(user.getUsername()+"987"));
					}else {
						user.setPassword("zdsoft123");
					}
					user.setIsDeleted(0);
					user.setAddress(stuObj.getString("homeAddr"));
					user.setEmail(stuObj.getString("email"));
					user.setUpPwdDate(new Date());
					user.setLoginDate(new Date());
					user.setMobilePhone(stuObj.getString("phone"));
					user.setCreationTime(new Date());
					user.setId(UuidUtils.generateUuid());
					//						user.setIdentityCard(stuJs.getString("idCardNo"));
					user.setOwnerId(student.getId());
					user.setOwnerType(User.OWNER_TYPE_STUDENT);
					user.setAccountId(UuidUtils.generateUuid());
					user.setUnitId(ahObjectIdMap.get(ahSyncUnitId));
					int codeInt = (int)((Math.random()*9+1)*100000);//随机生成6位数
					user.setDisplayOrder(codeInt);
					
					classUsers.add(user);
					
					ahObjectIdMap.put(anUserId, user.getId());
//					ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//					ahObjectIdMapJs.put(anUserId, user.getId());
//					RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
					
					UserAhSync sync = new UserAhSync();
					sync.setId(UuidUtils.generateUuid());
					sync.setObjectId(user.getId());
					sync.setAhObjectId(stuObj.getString("id"));
					sync.setObjectType(UserAhSync.SYNC_TYPE_USER);
					sync.setAhUnitId(ahSyncUnitId);
					desSyncs.add(sync);
					
					//保存用户
					if(CollectionUtils.isNotEmpty(classUsers)) {
						saveUser(classUsers.toArray(new User[0]));
					}
					System.out.println("保存用户数据是：-------------------" + classUsers.size());
					//保存学生
					if(CollectionUtils.isNotEmpty(saveStudents)){
						studentRemoteService.saveAll(SUtils.s(saveStudents.toArray(new Student[0])));
					}
					System.out.println("保存学生数据是：-------------------" + classUsers.size());
					//保存用户信息
					if(CollectionUtils.isNotEmpty(desSyncs)){
						userAhSyncRemoteService.saveAll(desSyncs.toArray(new UserAhSync[] {}));
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	//同步教师数据
	private void syncTeaUserData(String[] ahUserIds,String ahSyncUnitId, 
			ShieldSyncApp_szxy shieldSyncApp, List<UserAhSync> syncs,
			Map<String, String> ahObjectIdMap,String teaPwd,Map<String,Integer> countMap) {
		List<User> teaUser = new ArrayList<>();
		List<Teacher> teachers = new ArrayList<>();
//		Map<String,String> map=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//		String unitId = map.get(ahSyncUnitId);
		String unitId = ahObjectIdMap.get(ahSyncUnitId);
		List<Dept> depts = SUtils.dt(deptRemoteService.findByUnitId(unitId), new TR<List<Dept>>() {});
		Dept d = depts.get(0);
		Set<String> userIds = new HashSet<>();
		if(ArrayUtils.isNotEmpty(ahUserIds)){//
			for (String ahUserId : ahUserIds) {
//				Map<String,String> ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
				if(ahObjectIdMap.containsKey(ahUserId)) {
					continue;
				}
				String ahUserStr="";
				try {
					ahUserStr = new String(shieldSyncApp.getUserByUserId(ahUserId).getBody(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					System.out.println(ahUserId+"用户远程获取数据失败");
					continue;
				}
				if(StringUtils.isNotBlank(ahUserStr)&&JSONObject.parseObject(ahUserStr).containsKey("data")
						&&!StringUtils.equals((JSONObject.parseObject(ahUserStr).getString("data")),"[]")){
					JSONObject ahUserBody =  JSONObject.parseObject(ahUserStr);
					JSONObject ahUser = JSONObject.parseObject(ahUserBody.getString("data"));
					User user=new User();
					Teacher teacher=new Teacher();
					
					teacher.setId(UuidUtils.generateUuid());
					
					teacher.setDeptId(d.getId());
					teacher.setUnitId(unitId);
					int codeInt = (int)((Math.random()*9+1)*100000);//随机生成6位数
					teacher.setTeacherCode(codeInt+"");
					teacher.setTeacherName(StringUtils.isBlank(ahUser.getString("userName"))?"无姓名":ahUser.getString("userName"));
					Integer sex=0;
					if(ahUser.getInteger("genderCode")!=null){
						if(ahUser.getInteger("genderCode")==0){
							sex=2;
						}else{
							sex=ahUser.getInteger("genderCode");
						}
					}
					teacher.setSex(sex);
					user.setRealName(teacher.getTeacherName());
					user.setAhUserId(ahUserId);
					String userName=ahUser.getString("loginName");
					user.setUsername(getUserName(userName, countMap));
					
					if(StringUtils.equals(teaPwd, "0")) {
						user.setPassword("zdsoft123");
					}else if(StringUtils.equals(teaPwd, "1")){
						user.setPassword(generateTeacherUserPwd1(user.getUsername()+"987"));
					}else {
						user.setPassword("zdsoft123");
					}
					user.setIsDeleted(0);
					user.setAddress(ahUser.getString("homeAddr"));
					user.setEmail(ahUser.getString("email"));
					user.setUpPwdDate(new Date());
					user.setLoginDate(new Date());
					user.setMobilePhone(ahUser.getString("phone"));
					user.setCreationTime(new Date());
					user.setId(UuidUtils.generateUuid());
//			user.setIdentityCard(job.getString("idCardNo"));
					user.setOwnerId(teacher.getId());
					user.setOwnerType(User.OWNER_TYPE_TEACHER);
					user.setDeptId(d.getId());
					user.setAccountId(UuidUtils.generateUuid());
					user.setUnitId(unitId);
					user.setDisplayOrder(codeInt);
					
					teacher.setEmail(ahUser.getString("email"));
//			teacher.setIdentityCard(job.getString("idCardNo"));
					doDefeuleValue(user);
					teaUser.add(user);
					teachers.add(teacher);
					
					UserAhSync sync = new UserAhSync();
					sync.setId(UuidUtils.generateUuid());
					sync.setObjectId(user.getId());
					sync.setAhObjectId(ahUserId);
					sync.setObjectType(UserAhSync.SYNC_TYPE_USER);
					sync.setAhUnitId(ahSyncUnitId);
					syncs.add(sync);
					ahObjectIdMap.put(ahUserId, user.getId());
//					ahObjectIdMapJs=RedisUtils.getObject("anhuiMap", new TR<Map<String,String>>());
//					ahObjectIdMapJs.put(ahUserId, user.getId());
//					RedisUtils.setObject("anhuiMap", ahObjectIdMapJs);
					userIds.add(user.getId());
				}
			}
		}
		List<Role> roleList = SUtils.dt(roleRemoteService.findByUnitIdAndRoleType(unitId,
 				PowerConstant.ROLE_TYPE_OPER),Role.class);
		if(CollectionUtils.isNotEmpty(teachers)) {
			baseSyncSaveRemoteService.saveTeacherAndUser(teachers.toArray(new Teacher[] {}), teaUser.toArray(new User[] {}));
			Set<String> roleIds = new HashSet<>();
			for (Role role : roleList) {
				if(StringUtils.equals("default", role.getIdentifier())) {
					roleIds.add(role.getId());
				}
			}
			//新增角色
			if(roleIds.size() > 0)
				roleRemoteService.insertUserRole(roleIds.toArray(new String[0]), userIds.toArray(new String[0]));
		}
		
	}
	public static void main(String[] args) {
		System.out.println(isValidName("阿达aaa@"));
		
	}
	public static boolean isValidName(String username) {
		if (username == null || "".equals(username)) {
			return false;
		}

		String regex = "[a-zA-Z\u4e00-\u9fa5]{1}|"
				+ "[a-zA-Z\u4e00-\u9fa5]{1}[\u4e00-\u9fa5\\w\\.\\·]*[\u4e00-\u9fa5\\w]{1}";
		return java.util.regex.Pattern.matches(regex, username);
	}
	public static String getUserName(String str,Map<String, Integer> map) {
		if(StringUtils.isBlank(str)){
			String a=RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz1234567890");
			str = "ahjy_"+a;
			return str;
		}
		if(str.length()>15){
			str="ahjy_"+StringUtils.substring(str, str.length()-15, str.length());
		}else{
			str="ahjy_"+str;
		}
		if(isValidName(str)) {
//			Map<String,Integer> anhuiCountMapJs=RedisUtils.getObject("anhuiCountMap", new TR<Map<String,Integer>>());
			if(map.containsKey(str)) {
				 String a=RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz1234567890");
				str = "ahjy_"+a;
			}
		}else {
			String a=RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz1234567890");
			str = "ahjy_"+a;
		}
		
//		Map<String,Integer> anhuiCountMapJs=RedisUtils.getObject("anhuiCountMap", new TR<Map<String,Integer>>());
		map.put(str,1);
//		RedisUtils.setObject("anhuiCountMap", anhuiCountMapJs);
//		map.put(str, 1);
		return str;
	}
	/**
	 * 将以数字结尾的字符串数字部分加1，即数字格式的编号递增1个号码（例如：班级编号的递增）
	 * 
	 * @param numberStr
	 *            以数字结尾的字符串
	 * @return String
	 */
	private static String incrementString(String numberStr) {
		String rtnStr = null;

		if (numberStr == null || "".equals(numberStr)) {
			return rtnStr;
		}
		int length = numberStr.length();
		int pos = getNumPositionRight(numberStr, false);

		// 不是以数字结尾
		if (pos == -1) {
			return numberStr + "001";
		}

		// 以数字结尾
		long number = Long.parseLong(numberStr.substring(pos));
		number++;
		String tempStr = "" + number;
		int tempLength = tempStr.length();

		if (pos == 0) {
			// 全部是数字:20060205
			if (tempLength < length) {
				rtnStr = generatRepeatStr("0", (length - tempLength)) + tempStr;
			} else {
				rtnStr = tempStr;
			}
		} else {
			// 部分是数字，前半部分有非数字字符，如CM1201
			if (tempLength < (length - pos)) {
				rtnStr = numberStr.substring(0, pos)
						+ generatRepeatStr("0", (length - pos - tempLength))
						+ tempStr;
			} else {
				rtnStr = numberStr.substring(0, pos) + tempStr;
			}
		}

		return rtnStr;
	}
	
	private static String generatRepeatStr(String repeatStr, int repeatCount) {
		String rtnStr = "";

		if ((repeatCount < 1) || ("".equals(repeatStr)) || (repeatStr == null)) {
			return rtnStr;
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < repeatCount; i++) {
			buf.append(repeatStr);
		}

		rtnStr = buf.toString();

		return rtnStr;
	}
	
	private static int getNumPositionRight(String str, boolean flagZero) {
		int length = str.length();
		int i = length;
		while (i > 0) {
			char c = str.charAt(i - 1);

			// 如果不是数字
			if (!(Character.isDigit(c))) {
				break;
			}
			// 如果碰到0就结束
			if (flagZero) {
				if (c == '0')
					break;
			}
			i--;
		}

		// 若右边没有时，返回-1
		if (i == length) {
			i = -1;
		}
		return i;
	}
	
	
	public void saveTeacher(Teacher[] teachers) {
		saveTeacher(teachers,null);
	}
	
	public void saveTeacher(Teacher[] teachers, Map<String, String> returnMsg) {
		saveTeacherAndUser(teachers,null,returnMsg);
	}
	
	public void saveTeacherAndUser(Teacher[] teachers, User[] users) {
		saveTeacherAndUser(teachers,users,null);
	}
	
	public void saveTeacherAndUser(Teacher[] teachers, User[] users, Map<String, String> returnMsg) {
		try {
			List<Teacher> saveTeachers = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (Teacher teacher : teachers) {
			   teacher.setEventSource(getDefaultSource());
			   if(teacher.getInPreparation() == null)
				   teacher.setInPreparation("1");  //是否在编  1 --是 0 --否
			   if(teacher.getIncumbencySign() == null)
				   teacher.setIncumbencySign("11"); //教师状态---在职   DQZTM
			   if(teacher.getIsLeaveSchool() == null) 
				   teacher.setIsLeaveSchool("0");  // 0--在校 1--离校
			   if(teacher.getWeaveUnitId() == null)
				   teacher.setWeaveUnitId(teacher.getUnitId());  //编制单位id  默认是当前的工作单位unitid
			   if(teacher.getCreationTime() == null)
				   teacher.setCreationTime(new Date());
			   if(teacher.getModifyTime() == null)
				   teacher.setModifyTime(new Date());
			   if(teacher.getIsDeleted() == null)
				   teacher.setIsDeleted(0);
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = teacher.getId();
			   String unitId = teacher.getUnitId();
			   String teacherCode = teacher.getTeacherCode();
			   Integer sex = teacher.getSex();
			   //主要的字段非空
			   String teacherName = teacher.getTeacherName();
			   String nation = teacher.getNation();
			   paramList.add(unitId);
			   paramList.add(teacherCode);
			   paramList.add(sex);
			   paramList.add(teacherName);
			   if(StringUtils.isBlank(nation)) {
				   nation = "01"; //默认汉族
				   teacher.setNation(nation);
			   }
			   paramList.add(nation);
			   if(doJudgeField(paramList,id)){
				   errorMsg.append(id +",");
				   log.error("教师中有关键的字段为空-----");
				   continue;
			   }
			   saveTeachers.add(teacher);
			}
			System.out.println("保存教师到数据库的数据：--------" + saveTeachers.size());
			if(CollectionUtils.isNotEmpty(saveTeachers)) {
				teacherRemoteService.saveAll(SUtils.s(saveTeachers.toArray(new Teacher[0])));
				Map<String, Teacher> teachMap = EntityUtils.getMap(saveTeachers, Teacher::getId);
				List<User> endSaveUser = new ArrayList<>();
				if(ArrayUtils.isNotEmpty(users)){
					for (User user : users) {
						String ownerId = user.getOwnerId();
						if(teachMap.get(ownerId) != null){
							endSaveUser.add(user);
						}
					}
				}
				if(CollectionUtils.isNotEmpty(endSaveUser)){
					saveUser(endSaveUser.toArray(new User[0]));
				}
			}
		} catch (Exception e) {
			log.error("保存教师数据失败"+ e);
		}
	}
	
	public boolean isUsernameNotCaseSensitive() {
		String value = systemIniRemoteService.findValue("SYSTEM.USERNAME.CASE.SENSITIVE.SWITCH");
        return !(BooleanUtils.toBoolean(NumberUtils.toInt(value, 0)) || BooleanUtils.toBoolean(value));
    }
	
	public String generateTeacherUserPwd1(String str) {
		String password = "";
		if (str.length() >= 8) {
			password = str;
		} else {
			password = StringUtils.leftPad(str, 8, "0");
		}
		return password;
	}
	
	private Integer getDefaultSource (){
		return BaseSaveConstant.DEFAULT_EVENT_SOURCE_VALUE;
	}
	
	private boolean doJudgeField(List<Object> paramList, String id) {
    	return doJudgeField(paramList, null, id);
	}
	private boolean doJudgeField(List<Object> paramList,String regionCode, String... id) {
		boolean isDo = Boolean.FALSE;
		if(validateId(id) ||  validateNoEmpty(paramList)) {
			isDo = Boolean.TRUE;
		}
		if(StringUtils.isNotBlank(regionCode)){
			if(isDo || validateRegionCode(regionCode)){
				isDo = Boolean.TRUE;
			}
		}
		return isDo;
	}
	
	/**
	 * 验证主键id
	 * @param id
	 * @return 
	 */
	private static Boolean validateId(String... ids) {
		for (String id : ids) {
			if(StringUtils.isBlank(id) || id.length() != 32 ) {
				return true;
			}
		}
		return false;
	}
	
	//判断字段不能为空
	private static boolean validateNoEmpty(List<Object> ts) {
		if(CollectionUtils.isNotEmpty(ts)){
			for (Object t : ts) {
				if(t instanceof String && StringUtils.isBlank((String) t)) {
					return true;
				}
				if(t instanceof Integer && null == t) {
					return true;
				}
				if(null == t) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 验证regionCode
	 * @param regionCode
	 */
	private boolean validateRegionCode(String regionCode) {
		return (StringUtils.isBlank(regionCode) || regionCode.length() != 6 );
	}
	
	//初始化默认角色
	private void addDefaultRole(List<Role> roles, List<Role> roles1, Unit u) {
		//角色的添加和修改
		List<Role> unitRoles = SUtils.dt(roleRemoteService.findByUnitId(u.getId()), new TR<List<Role>>() {
		});
		if (CollectionUtils.isEmpty(unitRoles)) {
			for (Role role : roles1) {
				Role r = EntityUtils.copyProperties(role, Role.class);
				r.setId(UuidUtils.generateUuid());
				r.setUnitId(u.getId());
								//r.setName("");  ///这个角色的名字不用？？？ 数据库中是not null
				roles.add(r);
			}
		}
	}
		
	/**
	 * 新增单位管理员账号
	 */
	private Teacher doDefaultAdminTea(List<Teacher> teachers, Unit u, Dept d) {
		Teacher t = new Teacher();
		t.setTeacherName("管理员");
		t.setTeacherCode("000000");
		t.setId("B" + StringUtils.substring(UuidUtils.generateUuid(), 1));
		t.setUnitId(u.getId());
		t.setIncumbencySign("11");
		t.setDeptId(d.getId());
		t.setSex(McodeDetail.SEX_MALE);
		t.setCreationTime(new Date());
		t.setIsDeleted(0);
		t.setModifyTime(new Date());
		t.setEventSource(getDefaultSource());
		teachers.add(t);
		return t;
	}
	
	/**
	 * 新增单位管理员用户
	 */
	private User addDefaultAdminUser(Unit u, Dept d, Teacher t) {
		User ur = new User();
		ur.setUsername("ahjy_" + System.currentTimeMillis());
		ur.setOwnerType(User.OWNER_TYPE_TEACHER);
		ur.setRegionCode(u.getRegionCode());
		ur.setId("A" + StringUtils.substring(UuidUtils.generateUuid(), 1));
		ur.setUnitId(u.getId());
		ur.setCreationTime(new Date());
		ur.setIsDeleted(0);
		ur.setUserState(1);
	    ur.setPassword("123456");
		ur.setUserRole(2);
		ur.setIconIndex(0);
		ur.setRegionCode(u.getRegionCode());
		ur.setSex(t.getSex());
		ur.setRealName("管理员");
		ur.setAccountId("C" + StringUtils.substring(ur.getId(), 1));
		ur.setModifyTime(new Date());
		ur.setEventSource(getDefaultSource());
		ur.setRoleType(0);
		ur.setEnrollYear(0000);
		ur.setDeptId(d.getId());
		ur.setOwnerId(t.getId());
		if(Unit.TOP_UNIT_GUID.equals(u.getParentId())){
			ur.setUserType(User.USER_TYPE_TOP_ADMIN);
		}else{
			ur.setUserType(User.USER_TYPE_UNIT_ADMIN);
		}
		return ur;
	}
	
	
		//初始化默认部门
	private Dept addDefaultDept(List<Dept> depts, Unit u) {
		Dept d = new Dept();
		String deptName = "默认部门";
		if(u.getUnitClass() == Unit.UNIT_CLASS_EDU)
			deptName = "管理员组";
		if(u.getUnitClass() == Unit.UNIT_CLASS_SCHOOL)
			deptName = "学校管理员组";
		d.setDeptName(deptName);
		d.setDeptCode("000001");
		d.setDeptType(1);
		d.setUnitId(u.getId());
		d.setCreationTime(new Date());
		d.setIsDeleted(0);
		d.setModifyTime(new Date());
		d.setEventSource(getDefaultSource());
		d.setParentId(Constant.GUID_ZERO);
		d.setInstituteId(Constant.GUID_ZERO);
		d.setId("D" + StringUtils.substring(u.getId(), 1));
		d.setIsDefault(1);
		depts.add(d);
		return d;
	}
		
	public void saveRole(Role[] roles) {
	    try {
			List<Role> saveRoles = new ArrayList<>();
			for (Role role : roles) {
			   List<Object> paramList = new ArrayList<>(); 
			   //数据库中非空的字段
			   String id = role.getId();
			   String unitId = role.getUnitId();
			   String deptName = role.getName();
			   //主要的字段非空
			   paramList.add(unitId);
			   paramList.add(deptName);
			   if(validateId(id)|| validateNoEmpty(paramList)) {
				   log.error("角色中有关键的字段为空-----");
				   continue;
			   }
			   saveRoles.add(role);
			}
			if(CollectionUtils.isNotEmpty(saveRoles)) {
				roleRemoteService.saveAll(SUtils.s(saveRoles.toArray(new Role[0])));
			}
		} catch (Exception e) {
			log.error("保存角色数据失败"+ e);
		}
		
	}
	
	
	
	/**
	 * @param user
	 * 进行默认值的赋值
	 */
	private void doDefeuleValue(User user) {
		Date creationTime = user.getCreationTime();
		   if(creationTime== null) {
			   user.setCreationTime(new Date());
		   }
		   Date modifyTime = user.getModifyTime();
		   if(modifyTime== null) {
			   user.setModifyTime(new Date());
		   }
		   Integer isDeleted = user.getIsDeleted();
		   if(isDeleted== null) {
			   user.setIsDeleted(BaseSaveConstant.DEFAULT_IS_DELETED_VALUE);
		   }
		   Integer eventSource = user.getEventSource();
		   if(eventSource== null) {
			   user.setEventSource(getDefaultSource());
		   }
	}
	
	//同步passport
	public void syncToPassport(List<User> urs) {
		if (!Evn.isPassport())
			return;
		List<String> accountIds = EntityUtils.getList(urs, User::getAccountId);
		Map<String, User> userMap = EntityUtils.getMap(urs, User::getAccountId);
		Account[] accounts = null;
		try {
			accounts = PassportClient.getInstance().queryAccounts(accountIds.toArray(new String[0]));
		} catch (PassportException e1) {
			log.error("passport根据accountid查询已经账号异常"+e1.getMessage());
			return;
		}
		List<Account> updateAccounts = new ArrayList<>();
		for (Account a : accounts) {
			if (a != null) {
				User user = userMap.get(a.getId());
				getAccountByUser(user,a);
				updateAccounts.add(a);
			}
		}
		List<String> returnedAccountIds = EntityUtils.getList(updateAccounts, Account::getId);
		@SuppressWarnings("unchecked")
		List<String> remainedAccountIds = ListUtils.subtract(accountIds, returnedAccountIds);
		List<Account> as2 = new ArrayList<>();
		List<String> an2 = new ArrayList<>();

		for (String accountId : remainedAccountIds) {
			User user = userMap.get(accountId);
			Account account = new Account();
			getAccountByUser(user, account);
			an2.add(user.getUsername());
			as2.add(account);
		}
		
		Set<String> names = new HashSet<>();
		try {
			names = PassportClient.getInstance().queryExistsAccountUsernames(an2.toArray(new String[0]));
		} catch (PassportException e1) {
			log.error("passport获取已经存在的账号异常"+e1.getMessage());
			return;
		}
		// PassportClient.getInstance().
		List<Account> as22 = new ArrayList<>();
		for (Account at : as2) {
			if (names.contains(at.getUsername())) {
				log.error(at.getUsername() + "已经存在");
				continue;
			}
			as22.add(at);
		}
		Account[] ats = null;
		try {
			ats = PassportClient.getInstance().addAccounts(as22.toArray(new Account[0]));
		} catch (Exception e) {
			log.error("同步passport信息失败"+e.getMessage());
		}
		List<User> sequenceUser = new ArrayList<>();
		if(ats != null) {
			for (Account at : ats) {
				if (at == null) {
					continue;
				}
				String accountId = at.getId();
				User u = userMap.get(accountId);
				if (u != null && (u.getSequence() == null || u.getSequence() == 0)) {
					u.setSequence(at.getSequence());
					sequenceUser.add(u);
				}
			}
			if (CollectionUtils.isNotEmpty(sequenceUser)) {
				userRemoteService.saveAll(SUtils.s(sequenceUser.toArray(new User[0])));
			}
		}
		
		//更新passport数据
		if(CollectionUtils.isNotEmpty(updateAccounts)){
			try {
				PassportClient.getInstance().modifyAccounts(updateAccounts.toArray(new Account[updateAccounts.size()]),
						ArrayUtils.toArray("phone","realName","sex"));
			} catch (PassportException e) {
				log.error("更新passport信息失败"+e.getMessage());
			}
		}
	}
	
	private void getAccountByUser(User user, Account account) {
		account.setUsername(user.getUsername());
		account.setRealName(user.getRealName());
		account.setSex(user.getSex());
		account.setFixedType(user.getOwnerType());
		account.setId(user.getAccountId());
//			account.setModifyTime(new Date());   加上这个字段，测试那边的环境可能会一直卡在这个地方，不会往下执行
		account.setPassword(user.getPassword());
		account.setType(Account.TYPE_STUDENT);
		String mobilePhone = user.getMobilePhone();
		if(StringUtils.isNotBlank(mobilePhone)){
			account.setPhone(mobilePhone);
		}
	}
	
	public void saveSchool(School[] schools) {
		saveSchool(schools,null,false);
	}
	
	public void saveSchool(School[] schools,Map<String, String> returnMsg,Boolean isSaveUnit) {
		try {
			List<School> saveSchools = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (School school : schools) {
				if(StringUtils.isBlank(school.getRegionPropertyCode())) 
					school.setRegionPropertyCode("121");  //区域属性码  默认取值---  镇中心区
				if(school.getInfantYear() == null)
					school.setInfantYear(3); //幼儿园学制
				if(school.getGradeYear() == null)
					school.setGradeYear(6);  //小学学制
				if(school.getJuniorYear() == null)
					school.setJuniorYear(3); //初中学制
				if(school.getSeniorYear() == null)
					school.setSeniorYear(3); //高中学制
				if(school.getCreationTime() == null)
					school.setCreationTime(new Date());
				if(school.getModifyTime() == null)
					school.setModifyTime(new Date());
				List<Object> paramList = new ArrayList<>(); 
				//先判断数据库中非空的字段是否满足条件   
				String id = school.getId();
				String schoolName = school.getSchoolName();
				//判断关键的字段是否不为空
				String regionCode = school.getRegionCode();
				String schoolType = school.getSchoolType();
				String schoolCode = school.getSchoolCode();
				String sections = school.getSections();
				paramList.add(schoolName);
				paramList.add(schoolType);
				paramList.add(schoolCode);
				paramList.add(sections);
				if(validateId(id) || validateRegionCode(regionCode) || validateNoEmpty(paramList)) {
					   errorMsg.append(id +",");
					   continue;
				}
				saveSchools.add(school);
			}
			System.out.println("保存学校单位的数据是：-------------------" + saveSchools.size());
			if(CollectionUtils.isNotEmpty(saveSchools)) {
				schoolRemoteService.saveAll(SUtils.s(saveSchools.toArray(new School[0])));
			}
			if(isSaveUnit) {
				saveUnitBySchool(saveSchools.toArray(new School[0]));
			}
		} catch (Exception e) {
			log.error("保存学校数据失败"+ e);
		}
	}
	
	private void saveUnitBySchool(School[] schools) {
		Set<String> regionCodeList = new HashSet<>();
		Set<String> unionCodeList = new HashSet<>();
		Set<String> parentIdList = new HashSet<>();
		for (School school : schools) {
			String regionCode = school.getRegionCode();
			if(StringUtils.isNotBlank(regionCode)) {
				if(regionCode.length() == 9) {
					unionCodeList.add(regionCode);
				}else {
					regionCodeList.add(school.getRegionCode());
					regionCodeList.add(StringUtils.substring(school.getRegionCode(), 0, 4) + "00");
					regionCodeList.add(StringUtils.substring(school.getRegionCode(), 0, 2) + "0000");
				}
			}
			String parentId = school.getParentId();
			if(StringUtils.isNotBlank(parentId)){
				parentIdList.add(parentId);
			}
		}
		Map<String, Unit> eduMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(regionCodeList)) {
			List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_EDU, 
					regionCodeList.toArray(new String[0])), new TR<List<Unit>>() {
			});
			eduMap = EntityUtils.getMap(edus, Unit::getRegionCode);
		}
		Map<String, Unit> lecelEduMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(unionCodeList)) {
			List<Unit> lecelEdus = SUtils.dt(unitRemoteService.findByUnitClassAndUnionCode(Unit.UNIT_CLASS_EDU, 
					unionCodeList.toArray(new String[0])), new TR<List<Unit>>() {
			});
			lecelEduMap = EntityUtils.getMap(lecelEdus, Unit::getUnionCode);
		}
		
		Map<String, Unit> pidMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(parentIdList)){
			List<Unit> pidEdus = SUtils.dt(unitRemoteService.findListByIds(parentIdList.toArray(new String[0])), new TR<List<Unit>>() {
			});
			pidMap = EntityUtils.getMap(pidEdus, Unit::getId);
		}
		
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		List<Unit> saveUnits = new ArrayList<>();
			//通过新增学校来新增单位
		boolean isdo = true;
		for (School school : schools) {
			String regionCode = school.getRegionCode();
			String parentId = school.getParentId();
    		Unit u = new Unit();
			u.setId(school.getId());
			u.setEventSource(school.getEventSource());
			u.setOrgVersion(1);
			u.setUnitState(Unit.UNIT_MARK_NORAML);
			u.setAuthorized(1);
			u.setUseType(1);
			u.setCreationTime(school.getCreationTime());
			u.setRegionCode(regionCode);
			Unit edu;
			if(regionCode.length() == 9) {
				edu = lecelEduMap.get(regionCode);
				if (edu != null) {
					u.setParentId(edu.getId());
					isdo = false;
				}
			}
			
			if(StringUtils.isNotBlank(parentId) &&  pidMap != null && pidMap.get(parentId) != null){
				u.setParentId(parentId);
				String unionCode = pidMap.get(parentId).getUnionCode();
				Long max = RedisUtils.incrby("syncdata.bjdy.unit.unioncode.max.regioncode." + unionCode, 1);
				Integer length = 12 - unionCode.length();
				u.setUnionCode(unionCode + StringUtils.leftPad(max + "", length, "0"));
			}else{
				if(isdo) {
					String regionCode3 = StringUtils.substring(regionCode, 0, 6);
					String regionCode1 = StringUtils.substring(regionCode, 0, 4) + "00";
					String regionCode2 = StringUtils.substring(regionCode, 0, 2) + "0000";
					edu = eduMap.get(regionCode3);
					if (edu != null) {
						u.setParentId(edu.getId());
					} else if (eduMap.get(regionCode1) != null) {
						u.setParentId(eduMap.get(regionCode1).getId());
					} else if (eduMap.get(regionCode2) != null) {
						u.setParentId(eduMap.get(regionCode2).getId());
					}else{
						u.setParentId(topUnit == null ? Constant.GUID_ZERO : topUnit.getId());
					}
				}
				Long max = RedisUtils.incrby("syncdata.bjdy.unit.unioncode.max.regioncode." + u.getRegionCode(), 1);
				if(StringUtils.isEmpty(regionCode)) {
					u.setUnionCode(StringUtils.leftPad(max + "", 6, "0"));
				}else {
					u.setUnionCode(regionCode + StringUtils.leftPad(max + "", 6, "0"));
				}
			}
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
			String schoolType = school.getSchoolType();
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
		
	}
	
	public void saveUser(User[] users) {
		saveUser(users, null);
	}
	
	public void saveUser(User[] users,Map<String, String> returnMsg) {
		try {
			List<User> saveUsers = new ArrayList<>();
			StringBuilder errorMsg = new StringBuilder();
			for (User user : users) {
				System.out.println("同步passport用户名："+user.getUsername()+"学生账号id:"+user.getOwnerId());
				user.setEventSource(getDefaultSource());
			   if(user.getCreationTime() == null)
				   user.setCreationTime(new Date());
			   if(user.getModifyTime() == null)
				   user.setModifyTime(new Date());
			   if(user.getIsDeleted() == null)
				   user.setIsDeleted(0);
			   if(user.getUserState() == null)
				   user.setUserState(1);  //DM-YHZT  0-未审核  1-正常  2-锁定 3-离职锁定
			   if(user.getUserType() == null)
				   user.setUserType(User.USER_TYPE_COMMON_USER);
			   if(user.getUserRole() == null)
				   user.setUserRole(2); //默认是2
			   if(user.getEnrollYear() == null)
				   user.setEnrollYear(0000);
			   if(user.getIconIndex() == null)
				   user.setIconIndex(0);
			   if(user.getSex() == null)
				   user.setSex(1);
			   List<Object> paramList = new ArrayList<>(); 
			   Set<String> idList = new HashSet<>();
			   //数据库中非空的字段
			   String id = user.getId();
			   String unitId = user.getUnitId();
			   String ownerId = user.getOwnerId();
			   Integer ownerType = user.getOwnerType();
			   String username = user.getUsername();
			   doDefeuleValue(user);
			   //主要的字段非空
			   String regionCode = user.getRegionCode();
			   String password = user.getPassword();
			   if(user.getUserRole() == null)
				   user.setUserRole(2);
			   if(user.getIconIndex() == null)
				   user.setIconIndex(0);
			   if(user.getEnrollYear() == null)
				   user.setEnrollYear(Calendar.getInstance().get(Calendar.YEAR));
			   if(user.getUserState() == null)
				   user.setUserState(1);
			   if(user.getUserType() == null)
				   user.setUserType(1);
			   if(StringUtils.isBlank(user.getAccountId()))
				   user.setAccountId(user.getId());
			   idList.add(id);
			   idList.add(unitId);
			   idList.add(ownerId);
			   paramList.add(ownerType);
			   paramList.add(username);
			   paramList.add(password);
			   if(doJudgeField(paramList, regionCode, idList.toArray(new String[idList.size()]))){
				       errorMsg.append(id +",");
				       log.error("用户中有关键的字段为空-----");
					   continue;
			   }
			   //密码进行加密
			   if(password.trim().length() != 64){
				   user.setPassword(new PWD(password).encode());
			   }
			   saveUsers.add(user);
			}
			System.out.println("保存用户到数据库的数据：--------" + saveUsers.size());
			if(CollectionUtils.isNotEmpty(saveUsers)) {
				userRemoteService.saveAll(SUtils.s(saveUsers.toArray(new User[0])));
				//同步passport
				syncToPassport(saveUsers);
			}
		} catch (Exception e) {
			log.error("保存用户数据失败"+ e);
		}
	}
	
	/**
	 * 学校类型转化
	 * @param sections
	 * @return
	 */
	private String doChargeSchoolType(String sections) {
		String schoolType = "211";
		switch (sections) {
		case "0":
		    schoolType = "111";
		    break;
        case "1":
        	schoolType = "211";
			break;
		case "2":
			schoolType = "311";
			break;
        case "3":
        	schoolType = "342";
			break;
        case "1,2":
        	schoolType = "312";
			break;
        case "2,1":
        	schoolType = "312";
			break;
        case "2,3":
        	schoolType = "341";
			break;
        case "3,2":
        	schoolType = "341";
			break;
        case "1,2,3":
        	schoolType = "345";
        	break;
        case "1,3,2":
        	schoolType = "345";
        	break;
        case "3,1,2":
        	schoolType = "345";
        	break;
        case "2,1,3":
        	schoolType = "345";
        	break;
        case "2,3,1":
        	schoolType = "345";
        	break;
        case "3,2,1":
        	schoolType = "345";
        	break;
		default:
			schoolType = "345";
			break;
		}
		return schoolType;
	}

}
