package net.zdsoft.syncdata.custom.lasa.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.lasa.service.CaSyncUserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service("caSyncUserService")
public class CaSyncUserServiceImpl implements CaSyncUserService{

	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	protected BaseSyncSaveRemoteService baseSyncSaveService;
	
	static Map<String, String> idCardTypeMap = new HashMap<>();
	static{
		idCardTypeMap.put("SF", "1"); //身份证
		idCardTypeMap.put("HZ", "A"); //护照 
		idCardTypeMap.put("HK", "B"); //户口薄
		idCardTypeMap.put("JR", "2"); //军官证
		idCardTypeMap.put("GA", "1"); //港澳台居民身份证
		idCardTypeMap.put("WJ", "1"); //武警身份证
		idCardTypeMap.put("BM", "1"); //边民出入境通行证
	}
	
	@Override
	public void saveUser(String result) throws Exception{
		JSONObject jsonObject = JSON.parseObject(result);
		User user = getUserInfo(jsonObject);
		baseSyncSaveService.saveUser(new User[] { user });
		doUpdateBaseEntity(user);
	}
	
	@Override
	public void delUser(String userIdCode) {
		User user = SUtils.dc(userRemoteService.findByUserIdCode(userIdCode), User.class);
		if(user != null){
			user.setUserState(User.USER_MARK_LOCK);
			baseSyncSaveService.saveUser(new User[] { user });
		}
	}
	
	// ---------------------------------私有方法区 -----------------------------------------------------
	/**
	 * 更新对应的基础数据
	 * @param user
	 */
	private void doUpdateBaseEntity(User user) {
		switch (user.getOwnerType()) {
		   case User.OWNER_TYPE_TEACHER:{
			   Teacher teacher = getTeacherInfo(user);
			   baseSyncSaveService.saveTeacher(new Teacher[] { teacher });
			   break;
		   }
		   case User.OWNER_TYPE_STUDENT:{
			   Student student = getStudentInfo(user);
			   baseSyncSaveService.saveStudent(new Student[] { student });
			   break;
		   }
		   case User.OWNER_TYPE_FAMILY:{
			   Family family = getFamilyInfo(user);
			   baseSyncSaveService.saveFamily(new Family[] { family });
			   break;
		   }
		}
	}
	
	private Teacher getTeacherInfo(User user) {
		Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(user.getOwnerId()), Teacher.class);
		teacher.setIsDeleted(user.getIsDeleted());
		teacher.setIdentityCard(user.getIdentityCard());
		teacher.setIdentityType(user.getIdentityType());
		teacher.setEmail(user.getEmail());
		teacher.setMobilePhone(user.getMobilePhone());
		teacher.setTeacherName(user.getRealName());
		return teacher;
	}
	
	private Student getStudentInfo(User user) {
		Student student = SUtils.dc(studentRemoteService.findOneById(user.getOwnerId()), Student.class);
		student.setIsDeleted(user.getIsDeleted());
		student.setIdentityCard(user.getIdentityCard());
		student.setIdentitycardType(user.getIdentityType());
		student.setEmail(user.getEmail());
		student.setMobilePhone(user.getMobilePhone());
		student.setStudentName(user.getRealName());
		return student;
	}
	
	private Family getFamilyInfo(User user) {
		Family family = SUtils.dc(familyRemoteService.findOneById(user.getOwnerId()), Family.class);
		family.setIsDeleted(user.getIsDeleted());
		family.setIdentityCard(user.getIdentityCard());
		family.setIdentitycardType(user.getIdentityType());
		family.setEmail(user.getEmail());
		family.setMobilePhone(user.getMobilePhone());
		family.setRealName(user.getRealName());
		return family;
	}
	
	private User getUserInfo(JSONObject jsonObject) throws ParseException {
		String identityCard    = jsonObject.getString("userIdcardNum");
		String realName        = jsonObject.getString("userName");
		String userIdCode      = jsonObject.getString("userIdCode");
		String email           = jsonObject.getString("userEmail");
		String phone           = jsonObject.getString("userPhone");
		String identityType    = getIdCardType (jsonObject.getString("userIdType"));
		String userRole        = jsonObject.getString("userRole");
		Date creationTime      = getCreateTime (jsonObject.getString("createTime"));
		User user = SUtils.dc(userRemoteService.findByOwnerTypeAndIdentityCard(Integer.parseInt(userRole), identityCard), User.class);
		user.setIdentityCard(identityCard);
		user.setIdentityType(identityType);
		user.setUserIdCode(userIdCode);
		user.setEmail(email);
		user.setMobilePhone(phone);
		user.setRealName(realName);
		user.setModifyTime(new Date());
		user.setCreationTime(creationTime);
		return user; 
	}
	
	private Date getCreateTime(String create) throws ParseException {
		JSONObject jsonObject = JSON.parseObject(create);
		String time = jsonObject.getString("time");
		Long timestamp = Long.parseLong(time);  
		return StringUtils.isNotBlank(time) ? new Date(timestamp) : new Date();
	}

	private String getIdCardType(String idcardType) {
		if(StringUtils.isNotBlank(idcardType)){
			idcardType = idCardTypeMap.get(idcardType);
		}
		return StringUtils.isNotBlank(idcardType) ? idcardType : "1";
	}
}
