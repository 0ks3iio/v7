package net.zdsoft.remote.openapi.action.business;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.dto.DeveloperDto;
import net.zdsoft.remote.openapi.entity.DeveloperPower;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;
import net.zdsoft.remote.openapi.remote.service.DeveloperRemoteService;
import net.zdsoft.remote.openapi.service.DeveloperPowerService;
import net.zdsoft.remote.openapi.service.EntityTicketService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceCountService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 关于用户的业务接口
 * @author yangsj
 *
 */
@Controller
@RequestMapping(value = { "/remote/openapi/sync", "/openapi/sync", "/sync/remote/openapi" })
public class UserSyncAction extends OpenApiBaseAction{
	private final String interfaceType = "userpwd";
	@Autowired
	DeveloperRemoteService developerRemoteService;
	@Autowired
    DeveloperPowerService developerPowerService;
	@Autowired
	private OpenApiInterfaceCountService openApiInterfaceCountService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService; 
	@Autowired
	private EntityTicketService entityTicketService;
	
	/**
	 * 获取用户的密码信息
	 * @param unitId
	 * @param userName
	 * @return 数据的json格式
	 */
	@RequestMapping("/findUser")
	@ResponseBody
	public String getUserList(String unitId, String userName, String pageIndex,String limit,HttpServletRequest request) {
		if (isOverMaxNumDay(interfaceType, request, "/openapi/sync/findUser")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("/openapi/sync/findUser",request);
		JSONObject json = new JSONObject();
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		String ticketKey = request.getHeader("ticketKey");
        if (StringUtils.isBlank(ticketKey)) {
            ticketKey = request.getParameter("ticketKey");
        }
        
        List<EntityTicket> entityTickets =  entityTicketService.findByTicketKeyAndTypeIn(ticketKey, interfaceType);
        Map<String, EntityTicket> columnMap = null;
        if(CollectionUtils.isNotEmpty(entityTickets)){
        	columnMap = EntityUtils.getMap(entityTickets, EntityTicket::getEntityColumnName);
        }
        DeveloperDto developer = developerRemoteService.findByTicketKey(ticketKey);
		List<DeveloperPower> developerPowers = developerPowerService.findByDeveloperId(developer.getId());
		Set<String> endunitSet = new HashSet<>();
		if(CollectionUtils.isNotEmpty(developerPowers)) {
			Set<String> unitIdList = EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
			if(StringUtils.isNotBlank(unitId)){
				if(unitIdList.contains(unitId)){
					endunitSet.add(unitId);
				}
			}else {
				endunitSet = unitIdList;
			}
		}else {
			if(StringUtils.isNotBlank(unitId)){
				endunitSet.add(unitId);
			}
		}
		Pagination page= new Pagination();
		page.setPageSize(StringUtils.isNotBlank(limit) ? Integer.valueOf(limit) : 1000);
		page.setPageIndex(StringUtils.isNotBlank(pageIndex) ? Integer.valueOf(pageIndex) : 1);
		List<User> userList = User.dt(userRemoteService.findByUserNameAndUnitIn(userName, endunitSet, SUtils.s(page)),page);
		JSONArray data = new JSONArray();
		if(CollectionUtils.isNotEmpty(userList)){
			for (User user : userList) {
				JSONObject js = new JSONObject();
				if( columnMap != null){
					if(columnMap.get("username") != null){
						js.put("username", user.getUsername());
					}
					if(columnMap.get("password") != null){
						//进行解密是否报错
						String pwd;
						try {
							pwd = PWD.decode(user.getPassword());
						} catch (Exception e) {
							return returnOpenJsonError(e.getMessage());
						}
						js.put("password", pwd);
					}
				}
				data.add(js);
			}
		}
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("resultCode", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}
	
	/**
	 * 更新用户的密码接口
     * @param request
     * @param type 1 --用户 2 --手机号
     * @param param
     * @param newPwd
     * @param oldPwd (md5 32位小写加密)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUserPwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateUserPwd(HttpServletRequest request,String type,String param,String newPwd,String oldPwd) {
        try {
        	List<String> userNames = new ArrayList<>();
        	if(StringUtils.isNotBlank(param) && StringUtils.isNotBlank(newPwd) && StringUtils.isNotBlank(type)) {
        		if(StringUtils.isNotBlank(oldPwd)){
        			if("1".equals(type)) {
        				userNames.add(param);
        			}
        			if("2".equals(type)) {
        				String users = userRemoteService.findByMobilePhones(param);
        				if(StringUtils.isNotBlank(users)) {
        					List<User> userList = SUtils.dt(users, User.class);
        					userNames.addAll(EntityUtils.getSet(userList, User::getUsername));
        				}
        			}
        		}else{
        			if("3".equals(type)){
        				//进行解密是否报错
        				try {
							newPwd = PWD.decode(newPwd);
						} catch (Exception e) {
							return returnOpenJsonError(e.getMessage());
						}
        				userNames.add(param);
        			}
        		}
        		//先加密
        		if( newPwd.trim().length() != 32) {
        			newPwd = new PWD(newPwd).encode();
                }
        		StringBuilder detailError = new StringBuilder().append("详细错误:");
        		if(CollectionUtils.isNotEmpty(userNames)) {
        			int errNum =0;
        			int saveNum = 0;
        			for (String name : userNames) {
    					User user = SUtils.dc(userRemoteService.findByUsername(name), User.class);
                		if(user == null) {
                			errNum++;
                			continue;
                		}else {
                			if("3".equals(type)){
                				userRemoteService.updatePassportPasswordByUsername(name, newPwd);
                			}else{
                				String key = PWD.decode(user.getPassword());
                				if(key.trim().length() != 32) {
                					key = DigestUtils.md5Hex(key);
                				}
                				if(key.equals(oldPwd)) {
                					saveNum++;
                					userRemoteService.updatePassportPasswordByUsername(name, newPwd);
                				}else {
                					errNum++;
                					continue;
                				}
                			}
                		}
					}
        			detailError.append("用户不存在或旧密码不对"+errNum +"条");
        			if(errNum > 0){
        				return  returnError("-1", "更新密码成功"+saveNum+"条"+detailError.toString());
        			}else{
        				return  returnSuccess("1", "更新密码成功");
        			}
        		}
        	}else {
        		return  returnError("-1", "必填字段为空");
        	}
        	return returnError("-1", "更新密码失败！");
        }catch (Exception e) {
        	log.error("更新密码失败,错误信息是：-------"+e.getMessage());
        	return error(e.getMessage());
        }
    }
    
    /**
     * 更新用户的字段信息
     * @param request
     * @param userName   用户名
     * @param paramName  字段名称
     * @param paramValue 字段的赋值
     * @param oldPwd     (md5 32位小写加密)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUserField", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateUserField(HttpServletRequest request,String userName,String oldPwd,String paramName,String paramValue) {
        try {
        	int errorNum = 0;
        	if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(oldPwd) && StringUtils.isNotBlank(paramName)
        			&& StringUtils.isNotBlank(paramValue)){
        		User user = SUtils.dc(userRemoteService.findByUsername(userName), User.class);
        		if(user == null) {
        			errorNum++;
        		}else {
        			String key = PWD.decode(user.getPassword());
        			key = DigestUtils.md5Hex(key);
        			if(key.equals(oldPwd)) {
        				if(!(paramName.equals("userName") && paramName.equals("accountId"))){
        					 Field[] field = user.getClass().getDeclaredFields(); 
        					    for (int i = 0; i < field.length; i++) {
        					    	field[i].setAccessible(true);
        					    	if(paramName.equals(field[i].getName())){
        					    		field[i].set(user, paramValue);
        					    		break;
        					    	}
        					    }
        				}
        				//更新手机号的处理
        				if(paramName.equals("mobilePhone") || paramName.equals("realName")){
        		            if(Evn.isPassport()){
        		            	Account account = PassportClientUtils.getPassportClient().queryAccount(user.getAccountId());
        			            account.setPhone(user.getMobilePhone());
        			            account.setRealName(user.getRealName());
        			            account.setBirthday(user.getBirthday());
        		            	PassportClientUtils.getPassportClient().modifyAccount(account, ArrayUtils.toArray("phone","realName","birthday"));
        		            }
        				}
        				
        				//更新对应的实体信息
        				String ownerId = user.getOwnerId();
    		            if(User.OWNER_TYPE_FAMILY == user.getOwnerType()){
    		            	Family family = SUtils.dc(familyRemoteService.findOneById(ownerId), Family.class);
    		            	family.setRealName(user.getRealName());
    		            	family.setMobilePhone(user.getMobilePhone());
    		            	familyRemoteService.save(family);
    		            }else if (User.OWNER_TYPE_STUDENT == user.getOwnerType()) {
							Student student = SUtils.dc(studentRemoteService.findOneById(ownerId), Student.class);
    		            	student.setStudentName(user.getRealName());
    		            	student.setMobilePhone(user.getMobilePhone());
    		            	studentRemoteService.save(student);
						}else if (User.OWNER_TYPE_TEACHER == user.getOwnerType()) {
							Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(ownerId), Teacher.class);
							teacher.setTeacherName(user.getRealName());
							teacher.setMobilePhone(user.getMobilePhone());
							teacherRemoteService.save(teacher);
						}else {
							errorNum++;
						}
        			}else{
        				errorNum++;
        			}
        			if(errorNum < 1){
        				userRemoteService.save(user);
    					return returnSuccess("1", "更新用户信息成功");
    				}
        		}
        	}else{
        		return  returnError("-1", "必填字段为空");
        	}
        	return returnError("-1", "更新用户信息失败");
        } catch (PassportException e) {
        	log.error("passport更新个人信息失败",e);
        	return error("个人信息更新失败");
        } catch (Exception e) {
        	log.error("更新用户信息失败,错误信息是：-------", e);
        	return error(e.getMessage());
        }
    }
	
    /**
     * 根据用户名 + 密码   获取用户的信息
     * @param request
     * @param userName
     * @param pwd (md5 32位小写加密)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fingUserByUserInfo", method = RequestMethod.GET)
    public String fingUserByUserInfo(HttpServletRequest request,String userName,String pwd) {
        try {
        	if(StringUtils.isNotBlank(userName) &&  StringUtils.isNotBlank(pwd)) {
    			User user = SUtils.dc(userRemoteService.findByUsername(userName), User.class);
    			if(user != null){
    				String key = PWD.decode(user.getPassword());
        			if(key.trim().length() != 32) {
        				key = DigestUtils.md5Hex(key);
        			}
        			if(key.equals(pwd)) {
        				returnSuccess("1",JSONObject.toJSONString(user));
        			}	
    			}
        	}else {
        		return returnError("-1", "必填字段为空");
        	}
        	return returnError("-1", "用户信息不存在！");
        }catch (Exception e) {
        	log.error("更新密码失败,错误信息是：-------"+e);
        	return returnError("-1", "查找用户信息失败！" + e.getMessage());
        }
    }
    
    /**
	 * @param string
	 * @param request
	 */
	private void doSaveInterfaceCount(String uri, HttpServletRequest request) {
		doSaveInterfaceCount(interfaceType, request, uri);
	}
}
