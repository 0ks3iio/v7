package net.zdsoft.syncdata.custom.changzhi.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.constant.BaseConstants;
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
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.custom.changzhi.constant.ChangZhiConstant;
import net.zdsoft.syncdata.custom.changzhi.service.CzSyncService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/6/24 16:41
 */
@Service("czSyncService")
public class CzSyncServiceImpl implements CzSyncService {
    private Logger logger = Logger.getLogger("CzSyncServiceImpl");
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
    @Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
    
    public void saveEdu() {
    	System.out.println("edu同步开始====="+ new Date());
    	String timeSpan = String.valueOf(System.currentTimeMillis() );
        String url = ChangZhiConstant.CZ_SYNC_URL + "/ApiDev/WebAPI/GetAllDeptlList.aspx?AppKey=" + ChangZhiConstant.CZ_APP_KEY + "&timeSpan=" + timeSpan + "&Authorization=" + ChangZhiConstant.getAuth(timeSpan);
        String key = ChangZhiConstant.CHANG_ZHI_UPDATE_TIME_EDU_KEY;
        String updateTimeStr = RedisUtils.get(key);
        Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(updateTimeStr)) {
            url +="&UpdateTime=" + updateTimeStr;
        }
        try {
			List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnionCode(ChangZhiConstant.CZ_REGION, Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_EDU), Unit.class);
			Map<String, Unit> regionUnitMap = EntityUtils.getMap(unitList, Unit::getUnionCode);
			String jsonData = UrlUtils.get(url + "&page=1",new String());
			JSONObject jsonDataonObject = Json.parseObject(jsonData);
			String state = jsonDataonObject.getString("State");
			if(ChangZhiConstant.CZ_STATE_FAIL.equals(state)) {
				logger.error("长治教育局同步失败，获取数据发生错误");
                return;
			}
			JSONArray jsonArray = (JSONArray) jsonDataonObject.get("Result");
            if (jsonArray == null || jsonArray.size() == 0) {
            	RedisUtils.set(key, format.format(now.getTime()));
                logger.error("长治教育局同步，没有可同步的数据");
                return;
            }
            List<Unit> saveUnits = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String regionCode = decode(jsonObject.getString("Code"));
                String name = decode(jsonObject.getString("Name"));
                String fcode = decode(jsonObject.getString("FATHER_CODE"));
                String dtype = decode(jsonObject.getString("depttype")); 
                if ("2".equals(dtype) || (ChangZhiConstant.CZ_REGION+"00").equals(regionCode)) {
                	continue;
				}
                String unionCode = regionCode;
                if(unionCode.endsWith("00")) {
                	unionCode = unionCode.substring(0, 4);
                }
                Unit edu = regionUnitMap.get(unionCode);
                if(edu == null) {
                	 if(fcode.endsWith("00")) {
                     	fcode = fcode.substring(0, 4);
                     }
                     Unit pa = regionUnitMap.get(fcode);
                     if(pa == null) {
     					continue;
                     }
                	edu = new Unit();
                	edu.setId(UuidUtils.generateUuid());
                	edu.setParentId(pa.getId());
                	edu.setUnionCode(unionCode);
                	edu.setRegionCode(regionCode);
                	edu.setDisplayOrder(unionCode);
                	edu.setUnitClass(Unit.UNIT_CLASS_EDU);
					edu.setCreationTime(new Date());
					edu.setUnitType(Unit.UNIT_EDU_SUB);
                	edu.setIsDeleted(0);
                	regionUnitMap.put(unionCode, edu);
                }
            	edu.setUnitName(name);
            	edu.setModifyTime(new Date());
                saveUnits.add(edu);
            }
            if(CollectionUtils.isNotEmpty(saveUnits))
				baseSyncSaveService.saveUnit(saveUnits.toArray(new Unit[saveUnits.size()]));
            
            RedisUtils.set(key, format.format(now.getTime()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("长治edu同步数据失败，" + e.getMessage());
		}
    }

    public void saveSchool() {
        String timeSpan = String.valueOf(System.currentTimeMillis() );
        String url = ChangZhiConstant.CZ_SYNC_URL + "/ApiDev/WebAPI/GetAllSchoolList.aspx?AppKey=" + ChangZhiConstant.CZ_APP_KEY + "&timeSpan=" + timeSpan + "&Authorization=" + ChangZhiConstant.getAuth(timeSpan);
        String key = ChangZhiConstant.CHANG_ZHI_UPDATE_TIME_SCHOOL_KEY;
        String updateTimeStr = RedisUtils.get(key);
        Date today = new Date();
        System.out.println("长治sch同步开始="+today);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(updateTimeStr)) {
            url +="&UpdateTime=" + updateTimeStr;
        }
        List<School> schoolList = SUtils.dt(schoolRemoteService.findListBy(new String[] {"isDeleted"}, new String[] {"0"}), School.class);
        Map<String, School> schooCodeMap = EntityUtils.getMap(schoolList, School::getSchoolCode);
        List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnionId(ChangZhiConstant.CZ_REGION, Unit.UNIT_MARK_NORAML, -1), Unit.class);
        Map<String, Unit> unitIdMap = new HashMap<>();
        Map<String, Unit> unitRegionCodeMap = new HashMap<>();
        for(Unit un : unitList) {
        	unitIdMap.put(un.getId(), un);
        	if (un.getUnitClass() == Unit.UNIT_CLASS_EDU) {
				unitRegionCodeMap.put(un.getUnitClass() + "_" + un.getRegionCode(), un);
			}
        }
        List<School> saveSchoolList = new ArrayList<>();
        List<Unit> unitUpdateList = new ArrayList<>();
        try {
            String jsonData = UrlUtils.get(url + "&page=1",new String());
            JSONObject jsonDataonObject = Json.parseObject(jsonData);
            String pageCount = jsonDataonObject.getString("PageCount");
            if(NumberUtils.toInt(pageCount) == 0) {
            	RedisUtils.set(key, DateUtils.date2StringByDay(today));
            	logger.error("长治school同步失败，没有数据");
            	return;
            }
            for (int m = 1; m <= Integer.valueOf(pageCount); m++) {
                String jsonDataTemp = UrlUtils.get(url + "&page="+m,new String());
                JSONObject jsonDataonObjectTemp = Json.parseObject(jsonDataTemp);
                String state = jsonDataonObject.getString("State");
                if(ChangZhiConstant.CZ_STATE_FAIL.equals(state)) {
    				continue;
    			}
                JSONArray jsonArray = (JSONArray) jsonDataonObjectTemp.get("Result");
                if(jsonArray == null || jsonArray.size() == 0) {
                	continue;
                }
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String schoolCode = decode(jsonObject.getString("schoolCode"));
                    String schoolName = decode(jsonObject.getString("schoolName"));
                    String deptState = decode(jsonObject.getString("deptState"));
                    String disCode = decode(jsonObject.getString("DIS_CODE"));
                    int isDeleted = 0;
                    if (StringUtils.isNotEmpty(deptState)) {
                        isDeleted = Integer.valueOf(isDeleted);
                    }
                    String unitType = decode(jsonObject.getString("UNIT_TYPE"));
                    if (StringUtils.isEmpty(unitType)) {
                        unitType = String.valueOf(Unit.UNIT_SCHOOL_ASP);
                    }
                    String xueduanList =  decode(jsonObject.getString("xueduanList"));
                    JSONArray xueduanArrayObject = (JSONArray) JSONArray.parse(xueduanList);
                    StringBuilder sb = new StringBuilder();
                    if (xueduanArrayObject != null) {
                        for (int j = 0; j < xueduanArrayObject.size(); j++) {
                            JSONObject xueduan = (JSONObject) xueduanArrayObject.get(j);
                            String pId = xueduan.getString("PID");
                            if (!ChangZhiConstant.sectionTransMap.containsKey(pId)) {
								continue;
							}
							if (sb.length() == 0) {
                                sb.append(ChangZhiConstant.sectionTransMap.get(pId));
                            }else{
                                sb.append(","+ChangZhiConstant.sectionTransMap.get(pId));
                            }
                        }
                    }
                    School school = schooCodeMap.get(schoolCode);
                    Unit parentUnit = unitRegionCodeMap.get(Unit.UNIT_CLASS_EDU + "_" + disCode);
                    if (school == null) {
                        school = new School();
                        school.setId(UuidUtils.generateUuid());
                        school.setSchoolCode(schoolCode);
                        school.setSchoolName(schoolName);
                        school.setRegionCode(disCode);
                        if (parentUnit != null) {
                            school.setParentId(parentUnit.getId());
                        }
                        school.setSchoolType(ChangZhiConstant.schTypeTransMap.get(unitType));
                        school.setIsDeleted(isDeleted);
                        school.setCreationTime(new Date());
                        if(sb.length() == 0){
                        	sb.append("1");
                        }
                        school.setSections(sb.toString());

                        Unit schUnit = new Unit();
                        schUnit.setId(school.getId());
                        schUnit.setUnitName(schoolName);
                        schUnit.setRegionCode(disCode);
                        schUnit.setUnionCode(disCode.substring(0,6) + StringUtils.leftPad(schoolCode.substring(6),6,"0"));
                        if (parentUnit != null) {
                            schUnit.setParentId(parentUnit.getId());
                        }
                        schUnit.setUnitType(Integer.valueOf(ChangZhiConstant.unitTypeTransMap.get(unitType)));
                        schUnit.setCreationTime(new Date());
                        schUnit.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
                        schUnit.setIsDeleted(isDeleted);
                        schUnit.setSchoolType(school.getSchoolType());
                        unitUpdateList.add(schUnit);
                    }else{
                        school.setSchoolName(schoolName);
                        school.setIsDeleted(isDeleted);
                        school.setModifyTime(new Date());
                        if(StringUtils.isEmpty(school.getSections())){
                        	if (sb.length() == 0) {
								sb.append("1");
							}
                        	school.setSections(sb.toString());
                        }
                        Unit unitSch = unitIdMap.get(school.getId());
                        if (unitSch != null) {
                            unitSch.setUnitName(schoolName);
                            unitSch.setIsDeleted(isDeleted);
                        }else{
                            unitSch  = new Unit();
                            unitSch.setId(school.getId());
                            unitSch.setUnitName(schoolName);
                            unitSch.setRegionCode(disCode);
                            if (parentUnit != null) {
                                unitSch.setParentId(parentUnit.getId());
                            }
                            unitSch.setUnitType(Integer.valueOf(ChangZhiConstant.unitTypeTransMap.get(unitType)));
                            unitSch.setCreationTime(new Date());
                            unitSch.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
                            unitSch.setIsDeleted(isDeleted);
                        }
                        unitSch.setUnionCode(disCode.substring(0,6) + StringUtils.leftPad(schoolCode.substring(6),6,"0"));
                        if(StringUtils.isEmpty(unitSch.getSchoolType())) {
                        	unitSch.setSchoolType(school.getSchoolType());
                        }
                        unitUpdateList.add(unitSch);
                    }
                    
                    saveSchoolList.add(school);
                }
            }

            if(CollectionUtils.isNotEmpty(saveSchoolList))
                baseSyncSaveService.saveSchool(saveSchoolList.toArray(new School[saveSchoolList.size()]));
            if(CollectionUtils.isNotEmpty(unitUpdateList))
                baseSyncSaveService.saveUnit(unitUpdateList.toArray(new Unit[unitUpdateList.size()]));
            
            RedisUtils.set(key, format.format(today));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("长治同步数据失败，" + e.getMessage());
        }
    }
    
    public void saveSyncTeacher() {
    	try {
    		JSONArray ta = new JSONArray();
			String ts = System.currentTimeMillis() + "";
			String auth = ChangZhiConstant.getAuth(ts);
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MINUTE, -1);
			String upTime = StringUtils.trimToEmpty(RedisUtils.get(ChangZhiConstant.CHANG_ZHI_UPDATE_TIME_TEACHER_KEY)); //TODO
			System.out.println("长治teacher同步开始："+now.getTime()+", 上次同步时间="+upTime);
			StringBuilder url = new StringBuilder(ChangZhiConstant.CZ_SYNC_URL + ChangZhiConstant.CZ_GETTEACHER_URL)
					.append("?AppKey=").append(ChangZhiConstant.CZ_APP_KEY);
			url.append("&timeSpan=").append(ts).append("&Authorization=").append(auth).append("&UpdateTime=").append(upTime);
			String jd = UrlUtils.get(url + "&page=1",new String());
            JSONObject jsonDataonObject = Json.parseObject(jd);
            String pageCount = jsonDataonObject.getString("PageCount");
            String state1 = jsonDataonObject.getString("State");
			if (ChangZhiConstant.CZ_STATE_NONE.equals(state1)) {
				logger.error("长治教师同步失败，没有数据");
				RedisUtils.set(ChangZhiConstant.CHANG_ZHI_UPDATE_TIME_TEACHER_KEY, DateUtils.date2StringByDay(now.getTime()));
				return;
			}
            if(NumberUtils.toInt(pageCount) == 0) {
            	logger.error("长治教师同步失败，总页数为0");
            	return;
            }
            for (int m = 1; m <= Integer.valueOf(pageCount); m++) {
				//AppKey=ZHBG&timeSpan=1561357442000&Authorization=77f2aafb6b5ceb1f15fdb1fa15f70ef91ca5e329f94d43e18af4d3c662bc4798
            	String jsonData = UrlUtils.get(url.toString()+"&page="+m, new String());
				JSONObject jo = Json.parseObject(jsonData);
				String state = jo.getString("State");
				if (ChangZhiConstant.CZ_STATE_NORMARL.equals(state)) {
					JSONArray ar = (JSONArray) jo.get("Result");
					if (ar != null && ar.size() > 0) {
						ta.addAll(ar);
					}
				}
			}
			if(ta.size() > 0) {
				System.out.println("拉取教师数据="+ta.size()+"条");
				List<String> ids = new ArrayList<>();
				for (int i = 0; i < ta.size(); i++) {
		            JSONObject po = (JSONObject) ta.get(i);
		            String usId = decode(po.getString("userID"));
		            if(StringUtils.isNotEmpty(usId)) {
		            	usId = StringUtils.leftPad(usId.replaceAll("-", ""), 32, "0");
		            	if(!ids.contains(usId)) {
		            		ids.add(usId);
		            	}
		            }
				}
				List<Teacher> exTeachers = Teacher.dt(teacherRemoteService.findListByIds(ids.toArray(new String[0])));
				Map<String,Teacher> teacherMap = EntityUtils.getMap(exTeachers, Teacher::getId);
				List<User> allTeacherList = SUtils.dt(userRemoteService.findByOwnerType(User.OWNER_TYPE_TEACHER),new TR<List<User>>() {});
				Map<String,User> allUserMap = EntityUtils.getMap(allTeacherList, User::getId);
				Map<String,User> userNameMap = EntityUtils.getMap(allTeacherList, User::getUsername);
				List<Unit> unitList = SUtils.dt(unitRemoteService.findAll(), Unit.class);
		        Map<String, Unit> codeUnitMap = EntityUtils.getMap(unitList, Unit::getUnionCode);
				Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(EntityUtils.getList(unitList, Unit::getId).toArray(new String[0])),
						new TypeReference<Map<String, List<Dept>>>(){});
		        
		        List<Teacher> saveTeacherList = new ArrayList<>();
				List<User> saveUserList = new ArrayList<>();
		        for (int i = 0; i < ta.size(); i++) {
		            JSONObject po = (JSONObject) ta.get(i);
		            String tid = decode(po.getString("userID"));
		            if(StringUtils.isEmpty(tid)) {
		            	logger.error("老师信息有误：[userID]为空");
		            	continue;
		            }
		            tid = StringUtils.leftPad(tid.replaceAll("-", ""), 32, "0");
		            String schCode = decode(po.getString("depId"));
		            if(StringUtils.isEmpty(schCode)) {
		            	logger.error("老师信息有误：[depId]为空");
		            	continue;
		            }
		            Unit sch;
		            if(schCode.length() > 6) {
		            	schCode = schCode.substring(0, 6)+StringUtils.leftPad(schCode.substring(6), 6, "0");
		            } else if(schCode.endsWith("00")){
		            	schCode = schCode.substring(0, 4);
		            }
		            sch = codeUnitMap.get(schCode);
		            if(sch == null || sch.getIsDeleted() == 1) {
		            	logger.error("老师信息有误：[depId="+schCode+"]对应的学校不存在或已被删除");
		            	continue;
		            }
		            String unitId = sch.getId();
		            String username = decode(po.getString("account"));
		            if(StringUtils.isEmpty(username)) {
		            	logger.error("老师信息有误：[account]为空");
		            	continue;
		            }
		            if(!username.matches(systemIniRemoteService.findValue(ChangZhiConstant.SYSTEM_NAME_EXPRESSION))) {
		            	logger.error("老师信息有误：[account="+username+"]不满足规则");
		    			continue;
		    		}
//		            Date ud = po.getDate("updateDate");
//		            if(upDate != null && ud != null) {
//		            	if(DateUtils.compareForDay(upDate, ud) <= 0) {
//		            		System.out.println(username + "教师数据没有变化，略过");
//		            		continue;
//		            	}
//		            }
		            String name = decode(po.getString("name"));
		            int isDel = NumberUtils.toInt(decode(po.getString("isDelete")));
		            String mail = decode(po.getString("mail"));
		            String phone = decode(po.getString("phone"));
		            String sex = decode(po.getString("sex"));
		            if("0".equals(sex)) {// 0_男 1_女
		            	sex = BaseConstants.MALE + "";
		            } else if("1".equals(sex)){
		            	sex = BaseConstants.FEMALE + "";
		            }
		            String zzmm = getZzmm(decode(po.getString("ZZMMM")));
		            String identityCard = decode(po.getString("SFZJH"));
		            
		            // 用户数据，判断依据以用户名为准
		            User user = null;
					if(allUserMap != null && !allUserMap.isEmpty() && allUserMap.get(tid) != null){
						user = allUserMap.get(tid);
					}
					if(user == null) {
						user = userNameMap.get(username);
					}
					if(user != null) {
						if(!user.getUnitId().equals(unitId)) {
							logger.error("老师信息有误：原用户所在单位["+user.getUnitId()+"]和数据匹配单位[unioncode="+schCode+"]不一样");
							continue;
						}
						if(user.getUserType() != null && 
								(user.getUserType() == User.USER_TYPE_TOP_ADMIN || user.getUserType() == User.USER_TYPE_UNIT_ADMIN)) {
							logger.error("老师信息有误：单位管理员账户，不做处理");
							continue;
						}
		        	}
					if(user == null){
						user = new User();
						user.setId(tid);
						user.setPassword(new PWD(ChangZhiConstant.DEFAULT_PASSWORD_VALUE).encode());
						user.setOwnerType(User.OWNER_TYPE_TEACHER);
						user.setRegionCode(sch.getRegionCode());
						user.setUsername(username);
						user.setOwnerId(tid);
					}
					// 教师数据
					Teacher t;
					if(teacherMap != null && !teacherMap.isEmpty() && teacherMap.get(tid) != null){
						t = teacherMap.get(tid);
						t.setModifyTime(new Date());
					}else{
						t = new Teacher();
						if (StringUtils.isNotEmpty(user.getOwnerId())) {
							t.setId(user.getOwnerId());
						} else {
							t.setId(tid);
						}
						t.setTeacherCode(StringUtils.leftPad(i+"", 6, "0"));
						t.setCreationTime(new Date());
					}
					t.setUnitId(unitId);
					t.setSex(NumberUtils.toInt(sex));
					t.setEmail(mail);
					t.setTeacherName(name);
					t.setPolity(zzmm);
					if(StringUtils.isNotBlank(identityCard) && identityCard.length() > 18 ){
						identityCard = identityCard.substring(0, 18);
					}
					t.setIdentityCard(identityCard);
					t.setIsDeleted(isDel);
					t.setMobilePhone(phone);
					t.setLinkPhone(phone);
					String state = decode(po.getString("accountState"));
					if(!"0".equals(state)) {
						t.setIncumbencySign("11");
					}
					if (MapUtils.isNotEmpty(deptMap)) {
						if (StringUtils.isEmpty(t.getDeptId())) {
							if (deptMap.get(unitId) != null) {
								List<Dept> depts = deptMap.get(unitId);
								for (Dept dept : depts) {
									if (dept.getIsDefault() != null && dept.getIsDefault() == 1) {
										t.setDeptId(dept.getId());
										break;
									}
								}
								if(StringUtils.isEmpty(t.getDeptId())) {
									t.setDeptId(depts.get(0).getId());
								}
							}
						}
					}
					saveTeacherList.add(t);
					// user
		            if("0".equals(state)) {
		            	user.setUserState(User.USER_MARK_LOCK);
		            } else if("1".equals(state)) {
		            	user.setUserState(User.USER_MARK_NORMAL);
		            }
					user.setUnitId(unitId);
//						//验证username 信息是否满足 用户名必须为4-20个字符(包括字母、数字、下划线、中文)，1个汉字为2个字符
//						username = doChargeUserName(username);
//						user.setUsername(username);
					user.setSex(t.getSex());
					user.setRealName(t.getTeacherName());
					if (user.getUserType() == null) {
						//判断是否是管理员
						user.setUserType(User.USER_TYPE_COMMON_USER);
					}
					user.setIsDeleted(t.getIsDeleted());
					user.setIdentityCard(t.getIdentityCard());
					user.setMobilePhone(phone);
					user.setEmail(t.getEmail());
					user.setDeptId(t.getDeptId());
					saveUserList.add(user);
				}
		        
				//进行保存数据
				if(CollectionUtils.isNotEmpty(saveTeacherList))
					baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
				if(CollectionUtils.isNotEmpty(saveUserList))
					baseSyncSaveService.saveUser(saveUserList.toArray(new User[saveUserList.size()]));
				logger.info("获取教师的信息数据为------------" + saveTeacherList.size());
			}
			RedisUtils.set(ChangZhiConstant.CHANG_ZHI_UPDATE_TIME_TEACHER_KEY, DateUtils.date2StringByDay(now.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取教师的信息数据失败------------" + e.getMessage());
		}
    }
    
    private String getZzmm(String zzmm) {
    	if(StringUtils.isEmpty(zzmm)) {
    		return null;
    	}
    	int zmcode = NumberUtils.toInt(zzmm);
    	if(zmcode == 0 || zmcode > 13) {
    		return "99";
    	}
    	return zzmm;
    }
    
    private String decode(String str) {
        if (str != null) {
            return new String(SecurityUtils.decodeBase64(str.getBytes()));
        }else{
            return null;
        }
    }
    
    public static void main(String[] args) {
//        CzSyncServiceImpl czSyncService = new CzSyncServiceImpl();
//        czSyncService.saveSchool();
    	String code = "14040100012";
    	System.out.println(StringUtils.leftPad(code.substring(6), 6, "0"));
    }

}
