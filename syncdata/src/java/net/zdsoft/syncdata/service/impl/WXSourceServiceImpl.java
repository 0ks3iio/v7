package net.zdsoft.syncdata.service.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeachAreaRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.syncdata.constant.WenXunConstant;
import net.zdsoft.syncdata.service.WXResolveService;
import net.zdsoft.syncdata.service.WXSourceService;
import net.zdsoft.syncdata.util.WXPlatformUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author yangsj  2018年4月26日下午5:33:23
 */
@Service("wXSourceService")
public class WXSourceServiceImpl implements WXSourceService {

	private Logger log = Logger.getLogger(WXSourceServiceImpl.class);

	private Map<String, String> msgMap = new HashMap<>(); //同步结果  orgcode --- code
	
	@Autowired
	private WXResolveService wXResolveService;
	@Autowired
	private TeachAreaRemoteService teachAreaRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	
	
	/**
	 * 把同步结果通知文轩接口
	 */
	@Override
	public void noticeResult(List<String> codeList,String syncode) {
		//获取token
			long timetemp = System.currentTimeMillis();
			String token = getToken(timetemp);
			Map<String, String> map = new HashMap<>();
			map.put("appid", WenXunConstant.WENXUN_APP_ID_VALUE);
			map.put("timestamp", timetemp+"");
			map.put("token", token);
			map.put("syncode", syncode);
			JSONArray jsonArray = new JSONArray();
			for (String orgcode : codeList) {
				JSONObject synmsg = new JSONObject();
				synmsg.put("orgcode", orgcode);
				synmsg.put("code",msgMap.get(orgcode) == null ? WenXunConstant.WX_SYN_SUCCESS_VALUE : WenXunConstant.WX_SYN_FAIL_VALUE);
				synmsg.put("msg", msgMap.get(orgcode) == null ? "同步成功" : "同步失败");
				jsonArray.add(synmsg);
			}
			map.put("synmsg", jsonArray.toJSONString());
			String jsonStr = null;
			try {
				jsonStr = UrlUtils.readContent(
						WenXunConstant.WENXUN_SYN_END_RESULT_URL,
						map, true);
			} catch (IOException e) {
				log.error("接收文轩返回结果失败------");
			}	
			JSONObject json = Json.parseObject(jsonStr);
			String retuenCode = json.getString("code");
			if(!retuenCode.equals("0")) {
				log.error(json.getString("msg"));
			}else {
				log.info(json.getString("msg"));
			}
	}

	@Override
	public List<String> saveSchool(List<String> codeList) {
		List<String> successCodeList = new ArrayList<String>();
		String beforeCacheKey = WenXunConstant.WENXUN_BEFORE_SCHOOL_REDIS_KEY;
		for (String code : codeList) {
			try {
				saveData("org",code,"saveUnit",beforeCacheKey+code);
			} catch (Exception e) {
				log.error("同步学校失败，schoolcode---"+code);
				msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
				continue;
			}
			successCodeList.add(code);
		}
		return successCodeList;
	}

	@Override
	public void saveTeacher(String code) {
		String beforeCacheKey = WenXunConstant.WENXUN_BEFORE_TEACHER_REDIS_KEY;
		try {
			saveData("teacher",code,"saveTeacher",beforeCacheKey+code);
		} catch (Exception e) {
			log.error("同步教师失败，schoolcode---"+code);
			msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
		}
	}
	
	@Override
	public void saveClass(String code) {
		String beforeCacheKey = WenXunConstant.WENXUN_BEFORE_CLAZZ_REDIS_KEY;
		try {
			saveData("class",code,"saveClass",beforeCacheKey+code);
		} catch (Exception e) {
			log.error("同步班级失败，schoolcode---"+code);
			msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
		}
	}


	@Override
	public void saveStudent(String code) {
		String beforeCacheKey = WenXunConstant.WENXUN_BEFORE_STUDENT_REDIS_KEY;
		try {
			saveData("student",code,"saveStudent",beforeCacheKey+code);
		} catch (Exception e) {
			log.error("同步学生失败，schoolcode---"+code);
			msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
		}
	}


	@Override
	public void saveFamily(String code) {
		String beforeCacheKey = WenXunConstant.WENXUN_BEFORE_FAMILY_REDIS_KEY;
		try {
			saveData("family",code,"saveFamily",beforeCacheKey+code);
		} catch (Exception e) {
			log.error("同步家长失败，schoolcode---"+code);
			msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
		}
	}
	

	@Override
	public void saveAdmin(String code) {
		String beforeCacheKey = WenXunConstant.WENXUN_BEFORE_ADMIN_REDIS_KEY;
		try {
			saveData("admin",code,"saveAdmin",beforeCacheKey+code);
		} catch (Exception e) {
			log.error("同步机构管理员失败，schoolcode---"+code);
			msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
		}
	}
	

	@Override
	public void saveTeachArea(List<String> codeList) {
		for (String code : codeList) {
			try {
				JSONObject param = new JSONObject();
				param.put("schoolCode", code);
				param.put("url", WenXunConstant.WENXUN_BASE_TEACH_AREA_DATA_URL);
				String data = getResultData(param);
				if(StringUtils.isBlank(data)) {
					msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
					continue;
				}
				wXResolveService.saveTeachArea(data,code);
			} catch (Exception e) {
				log.error("同步校区信息失败，schoolcode---"+code);
				msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
				continue;
			}
		}
	}

	@Override
	public void saveTeachPlace(List<String> codeList) {
		List<School> allSchool = SUtils.dt(
				schoolRemoteService.findByCodeIn(codeList.toArray(new String[codeList.size()])), 
				new TR<List<School>>() {
		});
		Map<String, School> allSchoolMap = EntityUtils.getMap(allSchool, School::getSchoolCode);
		Set<String> uidList = EntityUtils.getSet(allSchool, School::getId);
		List<TeachArea> allTeachArea = SUtils.dt(
				teachAreaRemoteService.findByUnitIdIn(uidList.toArray(new String[uidList.size()])), 
				new TR<List<TeachArea>>() {
		});
		Map<String,List<TeachArea>> uidMap = EntityUtils.getListMap(allTeachArea, TeachArea::getUnitId, Function.identity());
		List<TeachBuilding> allTeachBuilding = SUtils.dt(
				teachBuildingRemoteService.findByUnitIdIn(uidList.toArray(new String[uidList.size()])), 
				new TR<List<TeachBuilding>>() {
		});
		Map<String,List<TeachBuilding>> areaIdMap = EntityUtils.getListMap(allTeachBuilding, TeachBuilding::getTeachAreaId, Function.identity());
		if(allSchoolMap == null) {
			log.error("要拉取的schoolcode都不存在---");
			return;
		}
		
		for (String code : codeList) {
			try {
				if(allSchoolMap.get(code) != null) {
					String uid = allSchoolMap.get(code).getId();
					if(uidMap == null || CollectionUtils.isEmpty(uidMap.get(uid))) {
						log.error("教师校区不存在，schoolcode---"+code);
						msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
						continue;
					}else {
						List<TeachArea> tAreas =  uidMap.get(uid);
						for (TeachArea teachArea : tAreas) {
							String taId = teachArea.getId();
							if(areaIdMap == null || CollectionUtils.isEmpty(areaIdMap.get(taId))) {
								log.error("教师校区不存在，schoolcode---"+code);
								msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
								continue;
							}else {
								List<TeachBuilding> teachBuildings = areaIdMap.get(taId);
								for (TeachBuilding teachBuilding : teachBuildings) {
									JSONObject param = new JSONObject();
									param.put("schoolCode", code);
									param.put("teachAreaId", taId);
									param.put("buildingId", teachBuilding.getId());
									param.put("url", WenXunConstant.WENXUN_BASE_TEACH_PLACE_DATA_URL);
									String data = getResultData(param);
									if(StringUtils.isBlank(data)) {
										msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
										continue;
									}
									wXResolveService.saveTeachPlace(data,param.toJSONString());									
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("同步校区信息失败，schoolcode---"+code);
				msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
				continue;
			}
		}
	}

	@Override
	public void saveSchoolSemester() {
//		List<String> codeList = getCodeList();
//		for (String code : codeList) {
//			try {
//				JSONObject param = new JSONObject();
//				param.put("schoolCode", code);
//				param.put("url", WenXunConstant.WENXUN_BASE_SEMESTER_DATA_URL);
//				String data = getResultData(param);
//				wXResolveService.saveSchoolSemester(data,code);
//			} catch (Exception e) {
//				log.error("同步学年学期信息失败，schoolcode---"+code);
//				msgMap.put(code, WenXunConstant.WX_SYN_FAIL_VALUE);
//				continue;
//			}
//		}
	}
	
	public Map<String, String> getMsgMap() {
		return msgMap;
	}

	public void setMsgMap(Map<String, String> msgMap) {
		this.msgMap = msgMap;
	}
	
	
	/**
	 * @param code
	 * @param string
	 */
	private String getResultData(JSONObject param) throws Exception {
		long timetemp = System.currentTimeMillis();
		String token = getToken(timetemp);
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", WenXunConstant.WENXUN_APP_ID_VALUE);
		map.put("timestamp", timetemp+"");
		map.put("token", token);
		map.put("schoolCode", param.getString("schoolCode"));
		String teachAreaId = param.getString("teachAreaId");
		String buildingId = param.getString("buildingId");
		if(StringUtils.isNotBlank(teachAreaId)) {
			map.put("schoolDistrictId", teachAreaId);
		}
        if(StringUtils.isNotBlank(buildingId)) {
        	map.put("buildingId", buildingId);
		}
        String url = param.getString("url");
        String jsonStr = UrlUtils.readContent(url,map, true);	
		JSONObject json = Json.parseObject(jsonStr);
		String code = json.getString("code");
		if(!WenXunConstant.WX_SYN_SUCCESS_VALUE.equals(code)) {
			return null;
		}
		String data = json.getString(WenXunConstant.RESOLVE_DATA_NAME);
		return data;
	}

	/**
	 * 获取数据源，并保存数据
	 * @param name       获取数据源的方法名称 如：teacher
	 * @param code       机构代码
	 * @param methodName 方法名
	 * @param cacheKey   保存时间戳的key
	 * @exception        直接返回 return
	 * 
	 */
	private void saveData(String name,String code,String methodName,String cacheKey) throws Exception {
		String lastTime = RedisUtils.get(cacheKey);
		JSONObject param = new JSONObject();
		param.put("lasttime", lastTime);
		Method m = wXResolveService.getClass().getDeclaredMethod(methodName, String.class);
		//开始获取数据
		do {
			param = doInterface(name,code,param);
			String data;
			if(param == null) {
				data = null;
				break;
			}else {
				data = param.getString(WenXunConstant.RESOLVE_DATA_NAME);
			}
			//开始解析和保存数据  根据反射来执行方法
			m.invoke(wXResolveService, data);
		}while(param != null && !param.getString("cursor").equals(WenXunConstant.WENXUN_END_CURSOR_VALUE));
		log.info("------------保存数据结束");
		RedisUtils.set(cacheKey, param.getString("curtime"));
	}
    private  JSONObject doInterface(String method,String code,JSONObject param) {
    	try {
			Map<String, String> map = doGroupParam(method,code,param);
			String jsonStr = UrlUtils.readContent(
					WenXunConstant.WENXUN_BASE_DATA_URL,
					map, true);	
			JSONObject json = Json.parseObject(jsonStr);
			String data = json.getString(WenXunConstant.RESOLVE_DATA_NAME);
			String retuenCode = json.getString("code");
			if(!retuenCode.equals("0")) {
				log.error(json.getString("message"));
				return null;
			}
			JSONObject datajson = Json.parseObject(data);
			//调用解析的数据
			if("org".equals(method)) {
				method = "school";
			}
			if("class".equals(method)) {
				method = "classe";
			}
			String arrayData;
			if(datajson == null) {
				arrayData = null;
			}else {
				if("admin".equals(method)) {
					arrayData = datajson.getString(method);
				}else {
					arrayData = datajson.getString(method + "s");
				}
			}
			//返回参数的值
			JSONObject reParam = new JSONObject();
			reParam.put("cursor", json.getString("cursor"));
			reParam.put("lasttime", json.getString("lasttime"));
			reParam.put("curtime", json.getString("curtime"));
			reParam.put("data", arrayData);
			return reParam;
		} catch (Exception e) {
			log.error("------------获取数据源失败"+ e.getMessage());
			return null;
		}
	}
	
	private  Map<String, String> doGroupParam (String method,String code,JSONObject param) {
		long timetemp = System.currentTimeMillis();
		String token = getToken(timetemp);
		code = getOrgcode(code);
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", WenXunConstant.WENXUN_APP_ID_VALUE);
		map.put("timestamp", timetemp+"");
		map.put("token", token);
		map.put("method", method);
		map.put("orgcode", code);
		if(param != null) {
			String cursor = (String) param.get("cursor");
			String lasttime = (String) param.get("lasttime");
			String curtime = (String) param.get("curtime");
			if(StringUtils.isNotBlank(cursor)) {
				map.put("cursor", cursor);
			}
			if(StringUtils.isNotBlank(lasttime)) {
				map.put("lasttime", lasttime);
			}
			if(StringUtils.isNotBlank(curtime)) {
				map.put("curtime", curtime);
			}
		}
		return map;
	}
	
	private  String getToken(long timetemp) {
		WXPlatformUtils platform = new WXPlatformUtils();
		String token = WenXunConstant.WENXUN_APP_ID_VALUE + "_" + timetemp;
		try {
			token = platform.encrypt(token, WenXunConstant.WENXUN_APP_KEY_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	
	private  String getOrgcode(String code) {
		WXPlatformUtils platform = new WXPlatformUtils();
		try {
			code = platform.encrypt(code, WenXunConstant.WENXUN_APP_KEY_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	
	
	
	
	public static void main(String[] args) {
		WXPlatformUtils platform = new WXPlatformUtils();
//		long timetemp = System.currentTimeMillis();
//		String token = WenXunConstant.WENXUN_APP_ID_VALUE + "_" + timetemp;
//		try {
//			token = platform.encrypt(token, WenXunConstant.WENXUN_APP_KEY_VALUE);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		//192.168.0.144:8080/homepage/remote/openapi/login?uid=12E0890491838992A9C337A1C91FFA2E
		//192.168.0.144:8080/homepage/remote/openapi/login?uid=514AD22D33E7BA78C28140FECDF63A9E
		String code = null;
		try {
		 code = platform.encrypt("A2V6L000027", WenXunConstant.WENXUN_APP_KEY_VALUE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(code);
		
		String name = "A2V6L000027";
		
		
		
	}
}
