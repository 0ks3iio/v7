package net.zdsoft.savedata.action.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.savedata.action.BaseSyncAction;
import net.zdsoft.savedata.utils.EncryptAES;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = { "/remote/openapi/sync", "/openapi/sync" })
public class FamilySyncAction extends BaseSyncAction{

	/**
	 * 保存家长的信息（批量）
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "update/family", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveFamilyAndUser (@RequestBody String data) {
		try {
			if(StringUtils.isNotBlank(data)) {
				String dataList = doExcuteDate(data);
				List<Family> saveFamilyList = new ArrayList<>();
				List<User> saveUserList = new ArrayList<>();
				if(StringUtils.isNotBlank(dataList)) {
					JSONArray jsonArray = JSON.parseArray(dataList);
					Set<String> famIdCardList = new HashSet<>();
					Map<String, User> ownerIdMap = new HashMap<>();
					Map<String, List<Family>> identityMap  = new HashMap<>();
					//得到需要更新的用户数据
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject js = jsonArray.getJSONObject(i);
						String userState    = js.getString("userState");
						String familyIdcardNum = js.getString("userIdcardNum");
						if("E".equals(userState)){
							famIdCardList.add(familyIdcardNum);
						}
					}
					if(CollectionUtils.isNotEmpty(famIdCardList)){
						List<Family> allFamilies = SUtils.dt(familyRemoteService.findByIdentityCardIn(famIdCardList.toArray(new String[famIdCardList.size()])), Family.class);
						identityMap = EntityUtils.getListMap(allFamilies, Family::getIdentityCard, Function.identity());
						Set<String> ownerIdSet = EntityUtils.getSet(allFamilies, Family::getId);
						if(CollectionUtils.isNotEmpty(ownerIdSet)){
							List<User> allUsers = SUtils.dt(userRemoteService.findByOwnerIds(ownerIdSet.toArray(new String[ownerIdSet.size()])), User.class);
							ownerIdMap = EntityUtils.getMap(allUsers, User::getOwnerId);
						}
					}
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject js = jsonArray.getJSONObject(i);
						String mobilePhone = js.getString("userMobilePhone");
						String studentName = js.getString("studentName");
						String studentIdcardNum = js.getString("studentIdcardNum");
						String relation = js.getString("relationship");
						if(StringUtils.isNotBlank(relation) && "99".equals(relation)){
							relation = "80";
						}
						String userState    = js.getString("userState");
						String email   = js.getString("userEmail");
						String familyIdcardNum = js.getString("userIdcardNum");
						String familyRealName = js.getString("userName");
						List<Student> allStudents = SUtils.dt(studentRemoteService.findByStudentNameAndIdentityCard(studentName,studentIdcardNum),
								Student.class);
						if(CollectionUtils.isNotEmpty(allStudents) && allStudents.size() == 1){
							Student student = allStudents.get(0);
							String studentId = student.getId();
							String schoolId  = student.getSchoolId();
							//封装家长数据
							Family family = null;
							if("N".equals(userState)){
								family = new Family();
								family.setId(UuidUtils.generateUuid());
								family.setCreationTime(new Date());
								family.setModifyTime(new Date());
								family.setIdentityCard(familyIdcardNum);
								family.setRelation(relation);
								family.setStudentId(studentId);
								family.setSchoolId(schoolId);
							}else{
								if(MapUtils.isNotEmpty(identityMap) && CollectionUtils.isNotEmpty(identityMap.get(familyIdcardNum))){
									List<Family> allFamilies = identityMap.get(familyIdcardNum);
									List<Family> saveFamilies = new ArrayList<Family>();
									for (Family f : allFamilies) {
										if(f.getIdentityCard().equals(familyIdcardNum) && f.getRelation().equals(relation) && f.getStudentId().equals(studentId)){
											saveFamilies.add(f);
										}
									}
									if(CollectionUtils.isNotEmpty(saveFamilies)){
										family = saveFamilies.get(0);
									}
									family.setModifyTime(new Date());
								}
							}
							family.setRealName(familyRealName);
							family.setEmail(email);
							family.setMobilePhone(mobilePhone);
							family.setLinkPhone(mobilePhone);
							saveFamilyList.add(family);
							
//							----------封装用户	
							User user;
							if("E".equals(userState) && (MapUtils.isNotEmpty(ownerIdMap)  &&  ownerIdMap.get(family.getId()) != null) ){
								user = ownerIdMap.get(family.getId());
								user.setModifyTime(new Date());
							}else{
								user = new User();
								user.setId(UuidUtils.generateUuid());
								user.setPassword(getFamilyPassword(family.getIdentityCard()));
								user.setOwnerType(User.OWNER_TYPE_FAMILY);
								user.setUserType(User.USER_TYPE_COMMON_USER);
								user.setUsername(getFamilyUserName());
								user.setOwnerId(family.getId());
								user.setUnitId(family.getSchoolId());
								user.setIdentityCard(family.getIdentityCard());
							}
							user.setRealName(familyRealName);
							user.setMobilePhone(mobilePhone);
							user.setEmail(email);
							
							saveUserList.add(user);
						}else{
							return returnMsg(BaseSaveConstant.BASE_ERROR_CODE,studentName+ "的身份证号" + studentIdcardNum +"数据有异常！！");
						}
					}
					//进行保存教师和学生数据
					if(CollectionUtils.isNotEmpty(saveFamilyList) && CollectionUtils.isNotEmpty(saveUserList)){
					   baseSyncSaveService.saveFamilyAndUser(saveFamilyList.toArray(new Family[saveFamilyList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
					}
				return returnMsg(BaseSaveConstant.BASE_SUCCESS_CODE,"数据保存成功");
			}
		  }
		  return returnSuccess();
		} catch (Exception e) {
			log.error("更新家长数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
	
	/**
	 * 把身份证前6位 作为密码
	 * @param familyIdcardNum
	 * @return
	 */
	private String getFamilyPassword(String familyIdcardNum) {
		return new PWD(familyIdcardNum.substring(0, 6)).encode();
	}

	/**
	 * 注册家长的账号
	 * @return
	 */
	private String getFamilyUserName() {
		return "ca_" + System.currentTimeMillis();
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
		return new String(EncryptAES.base64Decode(data));
	}
}
