package net.zdsoft.savedata.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.EduInfoRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.SubSchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EncryptAES;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.savedata.dto.BaseResultDto;
import net.zdsoft.savedata.entity.RelationBase;
import net.zdsoft.savedata.service.RelationBaseService;
import net.zdsoft.system.remote.service.RoleRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaseSyncAction extends BaseAction{
	private String area;
	private boolean isRelation = false;
	private boolean isEnableSecurity = false;
	
	@Autowired
	protected SemesterRemoteService semesterRemoteService;
	@Autowired
	protected RelationBaseService relationBaseService;
	@Autowired
	protected BaseSyncSaveRemoteService baseSyncSaveService;
	@Autowired
	protected UserRemoteService userRemoteService;
	@Autowired
	protected UnitRemoteService unitRemoteService;
	@Autowired
	protected SchoolRemoteService schoolRemoteService;
	@Autowired
	protected RoleRemoteService roleRemoteService;
	@Autowired
	protected TeacherRemoteService teacherRemoteService;
	@Autowired
	protected DeptRemoteService deptRemoteService;
	@Autowired
	protected ClassRemoteService classRemoteService;
	@Autowired
	protected GradeRemoteService gradeRemoteService;
	@Autowired
	protected SubSchoolRemoteService subSchoolRemoteService;
	@Autowired
	protected StudentRemoteService studentRemoteService;
	@Autowired
	protected FamilyRemoteService familyRemoteService;
	@Autowired
	protected EduInfoRemoteService eduInfoRemoteService;
    @Autowired
    protected SystemIniRemoteService systemIniRemoteService;

	protected static final Logger log = Logger.getLogger(BaseSyncAction.class);
		/**
		 * @param e
		 * @return
		 */
	public String returnMsg(Exception e) {
			return returnMsg("-1", "保存数据失败！", e.getMessage());
		}
	
	public String returnMsg(String code, String msg) {
		return returnMsg(code,msg,null,null);
	}
	
	public String returnMsg(String code, String msg, String detailError) {
			return returnMsg(code,msg,detailError,null);
		}
		
	public String returnMsg(String code, String msg, String detailError, String errorIndex) {
			BaseResultDto rd = new BaseResultDto();
			rd.setCode(code);
			rd.setMsg(msg);
			rd.setSuccess(true);
			if (StringUtils.isNotBlank(detailError)) {
				rd.setDetailError(detailError);
			}
			if(StringUtils.isNotBlank(errorIndex)){
				rd.setErrorIndex(errorIndex);
			}
			if(code.equals(BaseSaveConstant.BASE_ERROR_CODE)) {
				rd.setSuccess(Boolean.FALSE);	
			}
			return Json.toJSONString(rd);
		}
	
	
	/**
	 * 获取当前的学年学期
	 * @return
	 */
	public Semester getCurrentSemester() {
		String remoteSemester= "kade.remote.semester";
		Semester semester = Semester
				.dc(RedisUtils.get(remoteSemester, new RedisInterface<String>() {
					@Override
					public String queryData() {
						return semesterRemoteService.getCurrentSemester(SemesterRemoteService.RET_PRE);
					}
				}));
		return semester;
	}
	
	
	
	
	/**
	 * 进行数据的解析
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	protected String doExcuteDate(String jsonStr) throws Exception {
		JSONObject json = Json.parseObject(jsonStr);
		String data = json.getString(BaseSaveConstant.RESOLVE_DATA_NAME);
		area = json.getString(BaseSaveConstant.RESOLVE_DATA_AREA_NAME);
		return new String(EncryptAES.base64Decode(data));
	}
	
	//---------------------------------------公共的业务代码----------------------------------------------
	//进行数据的转化
	public String getSaveData(String jsonStr, String ticketKey) {
		try {
			if(getIsEnableSecurity()) {
				JSONObject jsonObject = JSON.parseObject(jsonStr);
				jsonStr = jsonObject.getString("data");
				jsonStr = EncryptAES.aesDecryptBase64(jsonStr, ticketKey);
			}
			return jsonStr;
		} catch (Exception e) {
//				logger.error("数据解析异常--------------------null"+ ucUserDto);
			return null;
		}
	}
		
	/**
	 * 是否加密传输
	 * @return
	 */
	private Boolean getIsEnableSecurity() {
		if (StringUtils.isNotBlank(Evn.getString(BaseSaveConstant.BASE_SYN_ENABLE_SECURITY))
    			&& "true".equalsIgnoreCase(Evn.getString(BaseSaveConstant.BASE_SYN_ENABLE_SECURITY))) {
			isEnableSecurity = Boolean.TRUE;
    	}
		return isEnableSecurity;
	}
	
	/**
	 * 数据的数量限制 
	 * @param dataArray
	 * @return
	 */
	protected <T> List<T> getLimitData(List<T> dataArray) {
		if(dataArray.size() > 1000) {
			dataArray = dataArray.subList(0, 1001);
		}
		return dataArray;
	}
	
	/**
	 * 得到非空字段的数量
	 * @param returnMsg
	 * @return
	 */
	protected int getErrorFieldNum(Map<String, String> returnMsg,StringBuilder detailError) {
		int errorField = 0;
		if(!returnMsg.isEmpty() && returnMsg.get(BaseSaveConstant.PROVING_BASE_SAVE_KEY) != null){
			errorField = Integer.valueOf(returnMsg.get(BaseSaveConstant.PROVING_BASE_SAVE_KEY));
		}
		detailError = detailError.append("数据的非空字段为空或主键id不符合"+errorField+"条数据;");
		return errorField;
	}
	
	
	/**
	 * @param schoolId
	 * @param allSchoolMap
	 * @return
	 */
	protected <K, V> boolean isErrorData(K k, Map<K, V> map) {
		return isErrorData(k,map,Boolean.TRUE);
	}
	
	/**
	 * @param k
	 * @param map
	 * @return
	 */
	protected <K, V> boolean isErrorData(K k, Map<K, V> map , Boolean isEquals) {
		if(isEquals) {
			return (k != null && (map == null || map.get(k) == null));
		}
		return (k != null && (map != null && map.get(k) != null));
	}

	/**
	 * @param ids
	 * @param map
	 */
	protected <K, V> int  getErrorNum(Set<K> ids, Map<K, V> map) {
		return getErrorNum(ids,map,Boolean.TRUE);
	}
	/**
	 * @param ids
	 * @param map
	 */
	protected <K, V> int  getErrorNum(Set<K> ids, Map<K, V> map,Boolean isEqual) {
		int result = 0;
		for (K t : ids) {
			if(isEqual) {
				if(t!= null && (map == null || map.get(t) == null)) {
					result++;
					continue;
				}
			}else {
				if(t!= null && (map != null && map.get(t) != null)) {
					result++;
					continue;
				}
			}
		}
		return result;
	}
	

	/**
	 * @param ss
	 * @param dMap
	 * @return
	 */
	protected <O, K> Map<K, O> getEndBaseMap(Map<String, String> relationMap, Map<K, O> entryMap) {
		Map<K, O> endMap = new HashMap<>();
		for (Entry<K, O> entry : entryMap.entrySet()) {
			String key = relationMap.get(entry.getKey());
			endMap.put((K) key, entry.getValue());
		}
		return endMap;
	}
	
	/**
	 * 根据findListByIds 得到数据，取得对应的条件map
	 * @param baseRemoteService
	 * @param ids
	 * @param clazz
	 * @param keyFunction
	 * @return Map
	 */
	protected <O, K> Map<K, O> getBaseMap(BaseRemoteService baseRemoteService, Set<K> ids ,Class<O> clazz,Function<O, K> keyFunction) {
		Map<K, O> allBaseMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(ids)) {
			List<O> allEntity = (List<O>) SUtils.dt(baseRemoteService.findListByIds(ids.toArray(new String[ids.size()])),clazz);
			allBaseMap = (Map<K, O>) EntityUtils.getMap(allEntity,keyFunction);
		}
		return allBaseMap;
	}
	
	/**
	 * 得到转化后的map 对象和集合
	 * @param relationList
	 * @param idSet
	 * @param baseIdMap
	 * @return
	 */
	protected Map<String, String> getRelationMap(Set<String> relationList, String type) {
		Map<String,String> baseIdMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(relationList)) {
			List<RelationBase> relationBases = relationBaseService.findByAreaAndTypeAndRelationIdIn
					(area,type,relationList.toArray(new String[relationList.size()]));
			if(CollectionUtils.isNotEmpty(relationBases)) {
				baseIdMap = EntityUtils.getMap(relationBases, RelationBase::getRelationId, RelationBase::getBaseId);
			}
		}
		return baseIdMap;
	}
	
	protected Set<String> getRelationSet(Set<String> relationList, String type) {
		return getRelationSet(relationList,type,null);
	}
	
	protected Set<String> getRelationSet(Set<String> relationList, String type,Map<String,String> baseIdMap) {
		Set<String> baseIdSet = new HashSet<>();
		if(CollectionUtils.isNotEmpty(relationList)) {
			List<RelationBase> relationBases = relationBaseService.findByAreaAndTypeAndRelationIdIn
					(area,type,relationList.toArray(new String[relationList.size()]));
			if(CollectionUtils.isNotEmpty(relationBases)) {
				baseIdSet.addAll(EntityUtils.getSet(relationBases, RelationBase::getBaseId));
				if(baseIdMap != null) {
					baseIdMap.putAll(EntityUtils.getMap(relationBases, RelationBase::getBaseId, RelationBase::getRelationId));
				}
			}
		}
		return baseIdSet;
	}
	
	
	protected void doSaveRelationBase(List<RelationBase> saveRBs) {
		if(CollectionUtils.isNotEmpty(saveRBs) && (!getIsRelation())) {
			relationBaseService.saveAll(saveRBs.toArray(new RelationBase[0]));
		}
	}
	
	/**
	 * @param saveRBs
	 * @param baseIdMap
	 * @param unit
	 * @param id
	 * @return
	 */
	protected String getBaseId(List<RelationBase> saveRBs,  String id, String type) {
		//根据类型+ 地区码 + listids 得到对应的集合
		if(StringUtils.isBlank(id) || getIsRelation()) {
			return id;
		}else {
			RelationBase relationBase = new RelationBase();
			relationBase.setId(UuidUtils.generateUuid());
			relationBase.setRelationId(id);
			relationBase.setBaseId(UuidUtils.generateUuid());
			relationBase.setType(type);
			relationBase.setArea(area);
			saveRBs.add(relationBase);
			return relationBase.getBaseId();
		}
	}
	
	protected Boolean getIsRelation() {
		if (StringUtils.isNotBlank(Evn.getString(BaseSaveConstant.BASE_SYN_RELATION))
    			&& "true".equalsIgnoreCase(Evn.getString(BaseSaveConstant.BASE_SYN_RELATION))) {
			isRelation = Boolean.TRUE;
    	}
		return isRelation;
	}
	
	
	// ----------------------------------返回消息代码 --------------------------------------------
	protected String getReturnMsg(int total, int failure, StringBuilder detailError, 
			Map<String, String> returnMsg){
		return getReturnMsg(total,failure,detailError,returnMsg,null);
	}
	
	protected String getReturnMsg(int total, int failure, StringBuilder detailError, 
			Map<String, String> returnMsg,List<RelationBase> saveRBs) {
		return getReturnMsg(total,failure,detailError,returnMsg,saveRBs,null);
	}
	
	protected String getReturnMsg(int total, int failure,
			StringBuilder detailError, Map<String, String> returnMsg,
			List<RelationBase> saveRBs, StringBuilder errorIdBuilder) {
		return getReturnMsg(total,failure,detailError,returnMsg,saveRBs,errorIdBuilder,null);
	}
	

	protected String getReturnMsg(int total, int failure,
			StringBuilder detailError, Map<String, String> returnMsg,
			List<RelationBase> saveRBs, StringBuilder errorIdBuilder,
			Map<String, String> baseIdMap) {
		if((StringUtils.isNotBlank(errorIdBuilder)) || (!getIsRelation() && baseIdMap != null && !baseIdMap.isEmpty())){
			String ids = returnMsg.get(BaseSaveConstant.PROVING_BASE_SAVE_ERROR_KEY);
			if(StringUtils.isNotBlank(ids)){
				String[] baseIds = ids.split(",");
				for (String bid : baseIds) {
					errorIdBuilder.append(baseIdMap.get(bid)+",");
				}
			}			
			returnMsg.put(BaseSaveConstant.PROVING_BASE_SAVE_ERROR_KEY, errorIdBuilder.toString());
		}
		doSaveRelationBase(saveRBs);
		int errorField = getErrorFieldNum(returnMsg,detailError);
		int successNum = total - errorField - failure;
		String msg = "信息成功保存"+successNum+"条，失败"+(errorField+failure)+"条";
		if(successNum == total){
			return returnSuccess(BaseSaveConstant.BASE_SUCCESS_CODE, msg);
		}else if (successNum == 0){
			return returnMsg(BaseSaveConstant.BASE_ERROR_CODE, "保存数据失败！", detailError.toString());
		}else{
			return returnMsg(BaseSaveConstant.BASE_HALF_SUCCESS_CODE, msg, detailError.toString(), returnMsg.get(BaseSaveConstant.PROVING_BASE_SAVE_ERROR_KEY));
		}
	}
	
	protected String getReturnMsg(int total, Map<String, String> returnMsg) {
		StringBuilder detailError = new StringBuilder().append("详细错误:");
		return  getReturnMsg(total,0,detailError,returnMsg);
	}
	
	
	public static void main(String[] args) {
		String oldPwd = PWD.decode("GLDFSFNQ335J4YSTFJ2K4GUAKLCWDPNFULFSFLFR27JHE47ZBDJG82EXMFY6SATN");
		System.out.println(oldPwd);
		String key = DigestUtils.md5Hex(oldPwd);
		System.out.println(key);
	}
	
	
	// ----------------------------------基础数据map转化代码 --------------------------------------------
	protected Map<String, Family> getFamilyMap(Set<String> ids) {
			Map<String, Family> familyMap = new HashMap<>();
			if(getIsRelation()) {
				familyMap.putAll(getBaseMap(familyRemoteService, ids, Family.class, Family::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_FAMILY_TYPE,baseIdMap);
				Map<String, Family> dMap  = getBaseMap(familyRemoteService, baseIdSet, Family.class, Family::getId);
				familyMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return familyMap;
		}
		
	protected Map<String, Student> getStudentMap(Set<String> ids) {
			Map<String, Student> studentMap = new HashMap<>();
			if(getIsRelation()) {
				studentMap.putAll(getBaseMap(studentRemoteService, ids, Student.class, Student::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_STUDENT_TYPE,baseIdMap);
				Map<String, Student> dMap  = getBaseMap(studentRemoteService, baseIdSet, Student.class, Student::getId);
				studentMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return studentMap;
		}
		
	protected Map<String, Clazz> getClazzMap(Set<String> ids) {
			Map<String, Clazz> clazzMap = new HashMap<>();
			if(getIsRelation()) {
				clazzMap.putAll(getBaseMap(classRemoteService, ids, Clazz.class, Clazz::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_CLAZZ_TYPE,baseIdMap);
				Map<String, Clazz> dMap  = getBaseMap(classRemoteService, baseIdSet, Clazz.class, Clazz::getId);
				clazzMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return clazzMap;
		}
		
	protected Map<String, Teacher> getTeacherMap(Set<String> ids) {
			Map<String, Teacher> teacherMap = new HashMap<>();
			if(getIsRelation()) {
				teacherMap.putAll(getBaseMap(teacherRemoteService, ids,Teacher.class, Teacher::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_TEACHER_TYPE,baseIdMap);
				Map<String, Teacher> dMap  = getBaseMap(teacherRemoteService, baseIdSet,Teacher.class, Teacher::getId);
				teacherMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return teacherMap;
		}
		
	protected Map<String, Unit> getUnitMap(Set<String> unitIds) {
			Map<String, Unit> unitMap = new HashMap<>();
			if(getIsRelation()) {
				unitMap.putAll(getBaseMap(unitRemoteService, unitIds,Unit.class, Unit::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(unitIds,BaseSaveConstant.BASE_UNIT_TYPE,baseIdMap);
				baseIdSet.addAll(getRelationSet(unitIds,BaseSaveConstant.BASE_SCHOOL_TYPE,baseIdMap));
				Map<String, Unit> dMap  = getBaseMap(unitRemoteService, baseIdSet,Unit.class, Unit::getId);
				unitMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return unitMap;
		}

	protected Map<String, User> getUserMap(Set<String> ids) {
			Map<String, User> userMap = new HashMap<>();
			if(getIsRelation()) {
				userMap.putAll(getBaseMap(userRemoteService, ids,User.class, User::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_USER_TYPE,baseIdMap);
				Map<String, User> dMap  = getBaseMap(userRemoteService, baseIdSet,User.class, User::getId);
				userMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return userMap;
		}
		
	protected Map<String, Dept> getDeptMap(Set<String> ids) {
			Map<String, Dept> deptMap = new HashMap<>();
			if(getIsRelation()) {
				deptMap.putAll(getBaseMap(deptRemoteService, ids, Dept.class, Dept::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_DEPT_TYPE,baseIdMap);
				Map<String, Dept> dMap  = getBaseMap(deptRemoteService, baseIdSet, Dept.class, Dept::getId);
				deptMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return deptMap;
		}
		
	protected Map<String, School> getSchoolMap(Set<String> unitIds) {
			Map<String, School> schoolMap = new HashMap<>();
			if(getIsRelation()) {
				schoolMap.putAll(getBaseMap(schoolRemoteService, unitIds, School.class, School::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(unitIds,BaseSaveConstant.BASE_SCHOOL_TYPE,baseIdMap);
				Map<String, School> dMap  = getBaseMap(schoolRemoteService, baseIdSet, School.class, School::getId);
				schoolMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return schoolMap;
		}
		/**
		 * 得到年级map
		 * @param ids
		 * @return
		 */
	protected Map<String, Grade> getGradeMap(Set<String> ids) {
			Map<String, Grade> gradeMap = new HashMap<>();
			if(getIsRelation()) {
				gradeMap.putAll(getBaseMap(gradeRemoteService, ids, Grade.class, Grade::getId));
			}else {
				Map<String, String> baseIdMap = new HashMap<>();
				Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_GRADE_TYPE,baseIdMap);
				Map<String, Grade> dMap  = getBaseMap(gradeRemoteService, baseIdSet, Grade.class, Grade::getId);
				gradeMap.putAll(getEndBaseMap(baseIdMap, dMap));
			}
			return gradeMap;
		}
	/**
	 * 得到校区map数据
	 * @param subschoolIds
	 * @return
	 */
	protected Map<String, SubSchool> getSubSchoolMap(Set<String> ids) {
		Map<String, SubSchool> subSchoolMap = new HashMap<>();
		if(getIsRelation()) {
			subSchoolMap.putAll(getBaseMap(subSchoolRemoteService, ids, SubSchool.class, SubSchool::getId));
		}else {
			Map<String, String> baseIdMap = new HashMap<>();
			Set<String> baseIdSet = getRelationSet(ids,BaseSaveConstant.BASE_SUBSCHOOL_TYPE,baseIdMap);
			Map<String, SubSchool> dMap  = getBaseMap(subSchoolRemoteService, baseIdSet, SubSchool.class, SubSchool::getId);
			subSchoolMap.putAll(getEndBaseMap(baseIdMap, dMap));
		}
		return subSchoolMap;
	}
}
