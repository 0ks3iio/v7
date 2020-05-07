package net.zdsoft.desktop.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.utils.PJHeadUrlUtils;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * @author yangsj  2017-4-13下午2:09:32
 */
@Controller
@RequestMapping("/desktop/mailList")
public class MailListAction extends DeskTopBaseAction{
	
	@Autowired
	private UserRemoteService userRemoteService;
	
	@ControllerInfo(ignoreLog=1, value="查找通讯录人员")
	@RequestMapping("/findUserName")
	public String findUserName(String userName,ModelMap map,HttpServletRequest request){
		List<JSONObject> sessionUsers = new ArrayList<JSONObject>();
		if(StringUtils.isBlank(userName)){
			 sessionUsers = RedisUtils.getObject("search.history"+getLoginInfo().getUserId(),new TR<List<JSONObject>>() {});
			 map.put("showString", "历史");
		}else{		
			//先取缓存
			String msg = RedisUtils.get("addRessBook.mail"+ getUserId() + getSession().getId());
			if(StringUtils.isBlank(msg)){
				JSONObject remoteMsgParam = new JSONObject();
				remoteMsgParam.put("userId",getLoginInfo().getUserId());
				remoteMsgParam.put("groupType", DeskTopConstant.ADDBOOK_DEPT_GROUP);		
				msg = this.<OpenApiOfficeService>getDubboService("openApiOfficeService").remoteAddressBookDetails(remoteMsgParam.toJSONString());	
			}		
			List<JSONObject> adList = SUtils.dt(msg, JSONObject.class);
			List<String> userOwenIds  = new ArrayList<String>();
			for (JSONObject jsonObject : adList) {
				String userObjects =jsonObject.getString("userDetails");
				List<JSONObject> userList = SUtils.dt(userObjects, JSONObject.class);
				for (JSONObject jsonObject2 : userList) {
					String dutyString = jsonObject2.getString("duty");
                	//职务的截取，显示
                	String dutyShow = StringUtils.substring(dutyString, 0, StringUtils.indexOf(dutyString, ",", StringUtils.indexOf(dutyString,",")+1));
                	jsonObject2.put("duty", dutyShow);
					if(jsonObject2.getString("userName").contains(userName)){
						sessionUsers.add(jsonObject2);
						userOwenIds.add(jsonObject2.getString("teacherId"));
					}
				}			
			}		
			//拼接头像url，并放入avatarUrl中
			String headUrl = request.getContextPath();
			if(userOwenIds.size()>= 1){				
				List<User> userList = SUtils.dt(userRemoteService.findByOwnerIds(userOwenIds.toArray(new String[userOwenIds.size()])), new TypeReference<List<User>>() {});
				for (User user : userList) {
					String avatarUrl = user.getAvatarUrl();
					user.setAvatarUrl(PJHeadUrlUtils.getShowAvatarUrl(headUrl, avatarUrl, getFileURL()));
				}
				//把头像url放进adlist中
				for (JSONObject jsonObject : sessionUsers) {					
						for (User user : userList) {
							if(Objects.equals(jsonObject.getString("teacherId"), user.getOwnerId())) {						
								jsonObject.put("avatarUrl",user.getAvatarUrl());
								break;
							}			
						}				
				}
			}
			map.put("showString", "结果");
		}
	   map.put("sessionUsers", sessionUsers);
	   if(CollectionUtils.isNotEmpty(sessionUsers) && StringUtils.isNotBlank(userName)){
		   List<JSONObject> sessionUserList = RedisUtils.getObject("search.history"+getLoginInfo().getUserId(),new TR<List<JSONObject>>() {});
		   List<JSONObject> userList = new ArrayList<JSONObject>();
		   List<JSONObject> repeatList = new ArrayList<JSONObject>();
		   if(CollectionUtils.isEmpty(sessionUserList)){
			   userList = sessionUsers;
		   }else{		   		   
			   for (JSONObject jsonObject : sessionUserList) {
				   for (JSONObject jsonObject1 : sessionUsers) {
					      if(Objects.equals(jsonObject.getString("teacherId"), jsonObject1.getString("teacherId"))){
					    	  repeatList.add(jsonObject);
					      }
				   }
			   }
			   userList.addAll(sessionUserList);
			   userList.addAll(0, sessionUsers);
			   userList.removeAll(repeatList);
			   if(userList.size()>20){
				   List<JSONObject> list1 = new ArrayList<JSONObject>();
				   list1 = userList.subList(20, userList.size());
				   list1.clear();
				   userList.removeAll(list1);		   
			   }
		   }
		   RedisUtils.setObject("search.history"+getLoginInfo().getUserId(), userList);	   
	   }	
	   
	   
	   String showMsg = StringUtils.isBlank(userName)?"没有搜索历史":"没有该用户，请重新输入";
	   if(CollectionUtils.isEmpty(sessionUsers)){
		   map.put("msg", showMsg);
	   }
	   return "/desktop/homepage/ap/addrBook/addrBook-searchHistory.ftl";
	}
   
	
	@ControllerInfo("通过通讯录发消息")
	@RequestMapping("/sendMsg")
	public String sendMsg(User user,String userName){
	   String nameString = user.getRealName();
  //     User user = SUtils.dc(userRemoteService.findByUsername(userName), User.class);
	   String ssString = userName;
	   List<User> userHistoryLists = new ArrayList<User>();
	   userHistoryLists.add(user);
       
	   return success("成功");
	}
}
