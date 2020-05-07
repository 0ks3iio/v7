package net.zdsoft.syncdata.custom.gansu.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.constant.custom.GanSuConstant;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.syncdata.custom.gansu.service.GanSuUserService;
import net.zdsoft.syncdata.util.GanSuSecurityUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

@Service("ganSuUserService")
public class GanSuUserServiceImpl implements GanSuUserService {
    
	private static Logger log = LoggerFactory.getLogger(GanSuUserServiceImpl.class);
	
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	
	
	@Override
	public void deleteUser(){
		// TODO Auto-generated method stub
		String lastTime = RedisUtils.get(GanSuConstant.GS_DELETE_USER_LASTTIME_KEY);
		if(StringUtils.isBlank(lastTime)){
			lastTime = "1566387794";
		}
		try {
			String userNames = getRemoteUserInfo(lastTime);
			if(StringUtils.isNotBlank(userNames)){
				List<String> newUserNameList = new ArrayList<>();
				String[] userName = userNames.split("\\|");
				for (String s : userName) {
					newUserNameList.add(GanSuConstant.GS_BEFORE_USERNAME_VALUE + s);
				}
				List<User> userList  = SUtils.dt(userRemoteService.findByUsernames(newUserNameList.toArray(new String[newUserNameList.size()])), User.class);
				if(CollectionUtils.isNotEmpty(userList)){
					Map<Integer, List<String>> ownerTypeMap = EntityUtils.getListMap(userList, User::getOwnerType, User::getOwnerId);
					userList.forEach(c->{
						c.setIsDeleted(BaseSaveConstant.TRUE_IS_DELETED_VALUE);
						c.setModifyTime(new Date());
					});
					userRemoteService.saveAll(userList.toArray(new User[0]));
					for (Map.Entry<Integer, List<String>> entry : ownerTypeMap.entrySet()) {
						Integer ownerType = entry.getKey();
						List<String> ownerIdList = entry.getValue();
						String[] owernIdArray = ownerIdList.toArray(new String[ownerIdList.size()]);
						if(User.OWNER_TYPE_TEACHER == ownerType){
							List<Teacher> hasTeachers = SUtils.dt(teacherRemoteService.findListByIds(owernIdArray), Teacher.class);
							hasTeachers.forEach(c->{
								c.setIsDeleted(BaseSaveConstant.TRUE_IS_DELETED_VALUE);
								c.setModifyTime(new Date());
							});
							teacherRemoteService.saveAll(hasTeachers.toArray(new Teacher[0]));
						}
						if(User.OWNER_TYPE_STUDENT == ownerType){
							List<Student> hasStudents = SUtils.dt(studentRemoteService.findListByIds(owernIdArray), Student.class);
							hasStudents.forEach(c->{
								c.setIsDeleted(BaseSaveConstant.TRUE_IS_DELETED_VALUE);
								c.setModifyTime(new Date());
							});
							studentRemoteService.saveAll(hasStudents.toArray(new Student[0]));
						}
						if(User.OWNER_TYPE_FAMILY == ownerType){
							List<Family> hasFamilys = SUtils.dt(familyRemoteService.findListByIds(owernIdArray), Family.class);
							hasFamilys.forEach(c->{
								c.setIsDeleted(BaseSaveConstant.TRUE_IS_DELETED_VALUE);
								c.setModifyTime(new Date());
							});
							familyRemoteService.saveAll(hasFamilys.toArray(new Family[0]));
						}
					}
				}
			}
			RedisUtils.set(GanSuConstant.GS_DELETE_USER_LASTTIME_KEY, String.valueOf(System.currentTimeMillis()/1000));
		} catch (Exception e) {
			RedisUtils.set(GanSuConstant.GS_DELETE_USER_LASTTIME_KEY, lastTime);
			log.error("删除用户失败--------", e);
			e.printStackTrace();
		}
	}
	  @SuppressWarnings("deprecation")
	  public static String getRemoteUserInfo(String lastTime) throws IOException {
	        //token
	        String token = getToken();
	        System.out.println("token--------------" + token);
	        long ctime = System.currentTimeMillis();
	        String param = ctime + "|" + GanSuConstant.GS_MSID + "|" + lastTime ;
	        System.out.println("param--------------" + param);
	        param = GanSuSecurityUtil.jiami(param);
	        param = URLEncoder.encode(param);
	        Map<String,String> paramMap = Maps.newHashMap();
	        paramMap.put("param",param);
	        paramMap.put("access_token",token);
	        paramMap.put("client_id",GanSuConstant.GS_APPID);
	        String result = UrlUtils.readContent(GanSuConstant.GS_PUSH_REMOVE_URL,paramMap,Boolean.TRUE);
	        JSONObject obj = JSONObject.parseObject(result);
	        String userXml = obj.getString("result");
	        return GanSuSecurityUtil.jiemi(userXml);
	    }
	
		//获取token
	    public static String getToken() {
	    	String token = null;
	    	Map<String,String> paramMap = Maps.newHashMap();
	    	paramMap.put("client_id",GanSuConstant.GS_APPID);
	    	paramMap.put("client_secret",GanSuConstant.GS_CLIENT_SECRET);
	    	paramMap.put("grant_type","client_credentials");
	        JSONObject obj;
			try {
				String result = UrlUtils.post(GanSuConstant.GS_TOKEN_URL, paramMap);
				obj = JSONObject.parseObject(result);
				token = obj.getString("access_token");
			} catch (IOException e) {
				log.error("获取token失败 ----" + e.getMessage());
				return null;
			}
	        return token;
	    }
	    
	    public static void main(String[] args) {
	    	String sss = "11698256|11698257|11698258|11698259|";
	    	String[] userName = sss.split("\\|");
	    	System.out.println(userName);
		}
}
