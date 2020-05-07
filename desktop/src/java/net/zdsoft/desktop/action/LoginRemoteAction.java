package net.zdsoft.desktop.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.custom.DgjyConstant;
import net.zdsoft.basedata.constant.custom.XyConstant;
import net.zdsoft.basedata.constant.custom.YingShuoConstant;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.entity.UserAhSync;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserAhSyncRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.constant.BjjyConstant;
import net.zdsoft.desktop.constant.ChangZhiConstant;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.constant.LaSajyConstant;
import net.zdsoft.desktop.constant.LeZhiConstant;
import net.zdsoft.desktop.constant.WenXunConstant;
import net.zdsoft.desktop.constant.XiGuConstant;
import net.zdsoft.desktop.login.LoginConstant;
import net.zdsoft.desktop.service.SyncAnhuidataService;
import net.zdsoft.desktop.utils.ShieldSyncApp_szxy;
import net.zdsoft.desktop.utils.Utils;
import net.zdsoft.desktop.utils.WXPlatformUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.HttpClientParam;
import net.zdsoft.framework.utils.HttpClientUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ModelRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iflytek.edu.ew.util.AssertionHolder;
import com.iflytek.edu.ew.validation.Assertion;
import com.iflytek.fsp.shield.java.sdk.model.ApiResponse;

//@Lazy(false)
@Controller
@RequestMapping(value = { "/homepage", "/fpf","/{region}/homepage", "/{region}/fpf"})
public class LoginRemoteAction extends LoginAction{
	
	private static RestTemplate restTemplate = new RestTemplate();
	@Autowired
    private ModelRemoteService modelRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private BaseSyncSaveRemoteService baseSyncSaveRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private SyncAnhuidataService syncAnhuidataService;
	@Autowired
	private UserAhSyncRemoteService userAhSyncRemoteService;

	boolean loginOn7 = false;// 跳转至7.0桌面
	// -------------------统一 跳转 7.0的首页地址，接收 参数 uid  
	@RequestMapping(value = "/remote/openapi/login/index")
	public String indexLogin(HttpServletRequest request,ModelMap map) {
		String uid = request.getParameter("uid");
		if(StringUtils.isBlank(uid)) {
			return "redirect:/remote/openapi/login/index";
		}
		try {
			uid =  new String(Base64.getDecoder().decode(uid), "UTF-8");
		} catch (Exception e) {
			return promptFlt(map,"uid参数解析失败");
		}
		User user = SUtils.dc(userRemoteService.findByUsername(uid), User.class);
		if(user != null) {
			String backUrl = "/homepage/remote/openapi/login/index?uid=" + Base64.getEncoder().encodeToString(user.getUsername().getBytes());
			String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
			if(StringUtils.isNotBlank(logoutResult)){	
				return logoutResult;
			}
			return loginUserName(user, null, "1", map);
 	    }else {
 			return promptFlt(map,"登录的用户不存在");
 	    }
	}
	//--------------------------湖南新邵对接代码----------------------------------
	@RequestMapping(value = "/remote/openapi/hnxs/loginEis")
	public String remoteHnxsLoginEis(HttpServletRequest request,ModelMap map) {
		String uid = request.getParameter("uid");
		if(StringUtils.isBlank(uid)) {
			return "redirect:/remote/openapi/hnxs/login";
		}
		if(doProvingDeploy(DeployRegion.DEPLOY_HUNANXINSHAO)){
			log.error("部署地区没有验证通过 ：-------------" + DeployRegion.DEPLOY_HUNANXINSHAO);
			return promptFlt(map,"不能访问地址");
		}
		try {
			uid =  new String(Base64.getDecoder().decode(uid), "UTF-8");
		} catch (Exception e) {
			return promptFlt(map,"uid参数解析失败");
		}
		User user = SUtils.dc(userRemoteService.findByUsername(uid), User.class);
		if(user != null) {
			String backUrl = "/homepage/remote/openapi/hnxs/loginEis?uid=" + Base64.getEncoder().encodeToString(user.getUsername().getBytes());
			String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
			if(StringUtils.isNotBlank(logoutResult)){	
				return logoutResult;
			}
			return loginUserName(user, null, "1", map);
 	    }else {
 			return promptFlt(map,"登录的用户不存在");
 	    }
	}
	//--------------------------安徽对接代码----------------------------------
	@RequestMapping(value = "/remote/openapi/ah/loginEis")
	public String remoteAhLoginEis(HttpServletRequest request,ModelMap map) {
		String uid = request.getParameter("uid");
		if(StringUtils.isBlank(uid)) {
			return "redirect:/remote/openapi/ah/login";
		}
		User user = SUtils.dc(userRemoteService.findByUsername(uid), User.class);
		if(user != null) {
			return loginUserName(user, null, "1", map);
 	   }else {
 			return promptFlt(map,"登录的用户不存在");
 	   }
	}
	
	@RequestMapping(value = "/remote/openapi/ah/login")
	public String remoteAhLogin(HttpServletRequest request,ModelMap map) {
		//http://127.0.0.1/fpf/remote/openapi/ah/login
		Assertion assertion = AssertionHolder.getAssertion();
		String userId = "4d7ebc580b4529f1109b3402f6060b69d1732652220aef736570b3d6b2653e496d195118dde76f2f3792193468f1fba230fe4ced242550fc";
		 if (assertion != null) {
			 userId = assertion.getPrincipal().getName();//openId
		 }
	
         // 调用API网关接口获取用户信息
		 ShieldSyncApp_szxy shieldSyncApp = new ShieldSyncApp_szxy();
         JSONObject userObj = new JSONObject();
         try {
        	 //获得该用户关联的学校
        	 JSONObject schObj = getJSONObject(shieldSyncApp.listSchoolByUser(userId));
        	 JSONArray schArray = JSONArray.parseArray(schObj.getString("data"));
        	 Set<String> unitAhId = new HashSet<>(); 
        	 for (int i = 0; i < schArray.size(); i++) {
        		unitAhId.add(schArray.getJSONObject(i).getString("id"));
        	 }
        	 System.out.println("同步");
        	 synchronized (userId) {
				//TODO 同步学校、用户、基础数据
        		 System.out.println("同步2");
				syncAnhuidataService.syncData(userId,
						unitAhId.toArray(new String[0]));
			}
			//进行eis登录
        	 List<UserAhSync> uSyncs = SUtils.dt(userAhSyncRemoteService.findByAhObjectId(userId), new TR<List<UserAhSync>>() {});
        	 
        	 User user = null;
        	 if(CollectionUtils.isNotEmpty(uSyncs)) {
        		 user = SUtils.dc(userRemoteService.findOneById(uSyncs.get(0).getObjectId()), User.class);
        	 }else {
        		 user = SUtils.dc(userRemoteService.findByUsername("tcsls"), User.class);
        	 }
        	 
        	 LoginInfo loginInfo = getLoginInfo();

         	log.error("没有登录，开始登录passport");
         	//1.退出eis
         	if (getHttpSession() != null && loginInfo != null && !loginInfo.getUserName().equals(user.getUsername())){
         		//1.退出eis
         		String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
 				quitSession();
 				//2.退出passport
 				if(Evn.isPassport()){
 					String paraName;
         				paraName = "/homepage/remote/openapi/loginEis?uid=" + user.getUsername();
 					String backURL =  new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
         					.append(paraName)
         					.toString();
 					return quitPassport(backURL,ticket);
 				}
         	}
         	//重新登录
         	log.error("登录的用户是："+ user.getUsername());
     		if(user != null) {
     			if (Evn.isPassport()) {
     				String url = getNativeIndexUrl();
					//重定向daopassport
					String passportLoginUrl = getRedirectUrl(user, url,null,Boolean.TRUE,"1");
					map.addAttribute("passportLoginUrl", passportLoginUrl);
					if (StringUtils.isBlank(passportLoginUrl)) {
						return "redirect:" + passportLoginUrl;
					} else {
						return "/desktop/login/wenxuanLogin.ftl";
					}
     			}
     			initLoginInfo(getRequest().getSession(), user);
				return "redirect:/desktop/index/page";
//     	   }else {
//     			return promptFlt(map,"登录的用户不存在");
     	   }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
		System.out.println(userObj.getJSONObject("data").getString("loginName"));
//		return "redirect:/homepage/loginPage/page";
		return "redirect:";
	}
	
	
	private JSONObject getJSONObject(ApiResponse apiResponse)
            throws UnsupportedEncodingException {
        // 发送请求，返回请求结果
        int httpStuts = apiResponse.getStatusCode();
        String resultStr = new String(apiResponse.getBody(), "UTF-8");
        logger.info("调用地址:{},返回的数据:{}", "", resultStr);
        if (httpStuts == HttpStatus.SC_OK) {// 200 请求成功
            JSONObject jsonResult = JSONObject.parseObject(resultStr);
            String code = jsonResult.getString("code");
            if ("1".equals(code)) {// 成功，1：成功，-1：失败
                return jsonResult;
            } else {
                logger.info("调用地址:{},调用失败原因:{}", "", jsonResult.getString("message"));
                return null;
            }
        } else {
            logger.info("调用地址:{},请求调用失败，失败请求状态{}", "", httpStuts);
            return null;
        }
    }
	
//  ----------------------------------- 乐智对接的代码 ------------------------------------------
	/**
	 * 1、乐智门户和资源平台中增加万朋的图标和链接
	 * 2、点击后通过URL传递一个角色参数和身份证号给万朋
	 * 3、如果是教师或学生角色就用身份证号去对比万朋基础数据库，如果对比成功，直接登录万朋管理平台，若没有转至万朋管理平台登录界面
	 * 4、若果是管理员就用账号取对比
	 * 5、传递的身份证号用ThreeDES加密处理
	 * 6、万朋管理平台屏蔽个人密码修改功能
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping("/remote/openapi/lezhi/login.action")
	public String loginForLeZhi(HttpServletRequest request, ModelMap map) {
		try {
			//String encryptParamsHex = request.getParameter(LeZhiConstant.LEZHI_ENCRYPT_PARAM);
			String encryptParamsHex   = request.getParameter("userid");
			byte encryptParamsByte [] = LeZhiConstant.hex2Byte(encryptParamsHex);
			byte userAccountJson[]=LeZhiConstant.decryptMode(LeZhiConstant.LEZHI_ENCRYPT_KEY.getBytes(),encryptParamsByte);
			
			String roleType = request.getParameter("role");
			String roleVal = new String(userAccountJson);
			User user=null;
			if(StringUtils.isBlank(roleType)) {
				if (getSession() != null && (ServletUtils.getLoginInfo(getSession())) != null) {
					return "redirect:" + getNativeIndexUrl();
				}
			} else {
				if (LeZhiConstant.ROLE_STUDENT.equals(roleType)) {
					if (StringUtils.isBlank(roleVal)) {
						//return promptFlt(map, "登录失败，学生身份证号为空");
						return "redirect:" + getNativeIndexUrl();
					} else {
						List<Student> studentList = SUtils.dt(studentRemoteService.findByIdentityCards(new String[]{roleVal}), Student.class);
						if (CollectionUtils.isEmpty(studentList)) {
							//return promptFlt(map, "登录失败，无该身份证号对应的学生");
							return "redirect:" + getNativeIndexUrl();
						}
						user = SUtils.dc(userRemoteService.findByOwnerId(studentList.get(0).getId()), User.class);
						if (user == null) {
							//return promptFlt(map, "登录失败，无该学生对应的用户");
							return "redirect:" + getNativeIndexUrl();
						}
					}
				} else if (LeZhiConstant.ROLE_TEACHER.equals(roleType)) {
					if (StringUtils.isBlank(roleVal)) {
						//return promptFlt(map, "登录失败，教师身份证号为空");
						return "redirect:" + getNativeIndexUrl();
					} else {
						List<Teacher> familyList = SUtils.dt(teacherRemoteService.findByIdentityCardNo(new String[]{roleVal}), Teacher.class);
						if (CollectionUtils.isEmpty(familyList)) {
							//return promptFlt(map, "登录失败，无该身份证号对应的教师");
							return "redirect:" + getNativeIndexUrl();
						}
						user = SUtils.dc(userRemoteService.findByOwnerId(familyList.get(0).getId()), User.class);
						if (user == null) {
							//return promptFlt(map, "登录失败，无该教师对应的用户");
							return "redirect:" + getNativeIndexUrl();
						}
					}
				} else if (LeZhiConstant.ROLE_ADMIN.equals(roleType)||LeZhiConstant.ROLE_SCHADMIN.equals(roleType)) {
					if (StringUtils.isBlank(roleVal)) {
						//return promptFlt(map, "登录失败，用户名为空");
						return "redirect:" + getNativeIndexUrl();
					} else {
						user = SUtils.dc(userRemoteService.findByUsername(roleVal), User.class);
						if (user == null) {
							//return promptFlt(map, "登录失败，用户名不存在");
							return "redirect:" + getNativeIndexUrl();
						}
					}
				} else {
					//return promptFlt(map, "登录失败，角色类型有误");
					return "redirect:" + getNativeIndexUrl();
				}
			}
			// 判断当前登录的账号是否和未退出的账号一致
			LoginInfo currentLogin = ServletUtils.getLoginInfo(getSession());
			if (getSession() != null && currentLogin != null) {
				if (!StringUtils.equals(currentLogin.getUserName(), roleVal)) {
					//1.退出eis
					String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
					quitSession();
					//2.退出passport
					if (Evn.isPassport()) {
						//退出之后重新再次登陆
						//UrlUtils.getPrefix(getRequest()) + UrlUtils.SEPARATOR_SIGN +"/desktop/index/page
						String backURL = //getNativeIndexUrl();  
								UrlUtils.getPrefix(getRequest()) + UrlUtils.SEPARATOR_SIGN +"/homepage/remote/openapi/lezhi/login.action?userid="+encryptParamsHex+"&role="+roleType;
						return quitPassport(backURL, ticket);
					}
				}
			}
			
			if(currentLogin==null){
				initLoginInfo(getRequest().getSession(), user);
			}
			
			if (Evn.isPassport()) {
				// 重定向daopassport
				String url = getNativeIndexUrl();
				String passportLoginUrl = getRedirectUrl(user, url, null, Boolean.TRUE, "1");
				map.addAttribute("passportLoginUrl", passportLoginUrl);
				if (StringUtils.isBlank(passportLoginUrl)) {
					return "redirect:" + passportLoginUrl;
				} else {
					return "/desktop/login/wenxuanLogin.ftl";
				}
			}
			//屏蔽修改密码
			return "redirect:/desktop/index/page";
		} catch (Exception e) {
			return "redirect:" + getNativeIndexUrl();
			//return promptFlt(map,"登录失败，数据解密异常");
		}
	}
	
	//  ----------------------星云对接的代码------------------------------------
	@RequestMapping("/remote/openapi/xingyun/login")
	public String loginForXingYun(HttpServletRequest request, ModelMap map) {
		try {
			String token   = request.getParameter(XyConstant.XY_TOKEN_NAME);
			String username   = request.getParameter("username");
			String pageUrl = request.getParameter(XyConstant.XY_PAGEURL_NAME);
			if(StringUtils.isBlank(token)){
				if (getSession() != null && (ServletUtils.getLoginInfo(getSession())) != null) {
		 			return "redirect:" + getNativeIndexUrl();
				}else {
					return "redirect:" + getXyLoginUrl();
				}
			}else{
				if(StringUtils.isBlank(username)){
					String ticket = getNewTicket(token);
					if(StringUtils.isBlank(ticket)){
						return promptFlt(map,"refresh 失败，ticket 已经失效");
					}else{
						getRequest().getSession().setAttribute(LaSajyConstant.LS_SAVE_TOKENID_KEY, ticket);
						username = getUserNameByTicket(ticket);
						if(StringUtils.isBlank(username)){
							return promptFlt(map,"登录失败，username为空");
						}else{
							User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
							if(user != null) {
								if(user.getOwnerType() == User.OWNER_TYPE_FAMILY){
									RedisUtils.set(XyConstant.XINGYUN_FIRST_LOGIN_TICKET + user.getUserIdCode() , ticket);
								}else{
									RedisUtils.set(XyConstant.XINGYUN_FIRST_LOGIN_TICKET + user.getId() , ticket);
								}
								//计时器
								RedisUtils.set(XyConstant.XINGYUN_LOGIN_TICKET + user.getId(), ticket);
								doCheckTicket(request, user.getId());
							}
						}
					}
				}
			}
			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if(user != null) {
				String backUrl = getBackUrl(username, "username", "/homepage/remote/openapi/xingyun/login");
				String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
				if(StringUtils.isNotBlank(logoutResult)){	
					return logoutResult;
				}
				String url; 
				if(StringUtils.isBlank(pageUrl)){
					url = getNativeIndexUrl();
				}else{
					pageUrl = RemoteCallUtils.decode(pageUrl);
					url = UrlUtils.getPrefix(getRequest()) + "/homepage/loginCall?url="+pageUrl;
				}
				return loginUserName(user, url, "2", map);
	 	    }else {
	 			return promptFlt(map,"该用户尚未同步");
	 	    }
		} catch (Exception e) {
			return promptFlt(map,"登录失败，数据解密异常");
		}
	}

	private String getJsonByPost(Map<String, String> bodyMap,String url ,Map<String, String> headerMap ,Map<String, String> paramMap  ,boolean isPost) throws IOException {
		HttpClientParam param = new HttpClientParam();
		param.setPost(isPost);
		param.setBodyMap(bodyMap);
		param.setHeadMap(headerMap);
		param.setParamMap(paramMap);
		param.setSessionId(null);
		return HttpClientUtils.exeUrlSync(url, param);

	}
	@RequestMapping("/remote/openapi/jieCheng/loginEis")
	public String loginFoJieChengEis(HttpServletRequest request ,ModelMap map) throws IOException {
		try {
			String code = request.getParameter("code");
			Map<String, String> entity = new HashMap<>();
			entity.put("code", code);
			entity.put("client_id", XyConstant.XINGYUN_JIECHENG_CLIENT_ID );
			entity.put("client_secret", XyConstant.XINGYUN_JIECHENG_CLIENT_SECRET);
			entity.put("grant_type", "authorization_code");
			System.out.println("捷诚的地址---" + XyConstant.XINGYUN_JIECHENG_PREFIX);
			String jsonStr = getJsonByPost(entity, XyConstant.XINGYUN_JIECHENG_PREFIX + "/authorization/oauth2/access_token" ,Maps.newHashMap(),Maps.newHashMap() ,true);
			JSONObject jsonObject =JSON.parseObject(jsonStr);
			String openid = jsonObject.getString("openid");
			String accessToken = jsonObject.getString("access_token");
			Map<String, String> entity2 = new HashMap<>();
			entity2.put("open_id", openid);
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Authorization", accessToken);
			String jsonStr2 = getJsonByPost(Maps.newHashMap(), XyConstant.XINGYUN_JIECHENG_PREFIX + "/common/get_user_info" ,headerMap ,entity2 ,false);
			JSONObject jsonObject2 =JSON.parseObject(jsonStr2);
			String userId = jsonObject2.getString("open_id");
			if(StringUtils.isBlank(userId)){
				return promptFlt(map,"open_id的参数是null");
			}
			userId = StringUtils.leftPad(userId, 32, "0");
			userId = userId.replaceAll("-", "");
			System.out.println("获取到的open_id的值是： ---------" + userId);
//			String username = jsonObject2.getString("account");
			User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
//			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if(user != null) {
				String backUrl = getBackUrl(request.getParameter("code"), "code", "/homepage/remote/openapi/jieCheng/loginEis");
				String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
				if(StringUtils.isNotBlank(logoutResult)){	
					return logoutResult;
				}
				return loginUserName(user, null, "1", map);
	 	    }else {
	 			return promptFlt(map,"该用户尚未同步");
	 	    }
			//判断当前登录的账号是否和未退出的账号一致
//			LoginInfo currentLogin = null;
//			if (getSession() != null && (currentLogin = ServletUtils.getLoginInfo(getSession())) != null) {
//				if (!StringUtils.equals(currentLogin.getUserName(), username)) {
//					//1.退出eis
//					String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
//					quitSession();
//					//2.退出passport
//					if(Evn.isPassport()){
//						String backURL = new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
//								.append("/homepage/remote/openapi/xingyun/login")
//								.append("?username=")
//								.append(username)
//								.toString();
//						return quitPassport(backURL,ticket);
//					}
//				}
//			}
//			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
//			if(user != null) {
//				if (Evn.isPassport()) {
//					String passportLoginUrl = getRedirectUrl(user,null,null, Boolean.FALSE, "1");
//					map.addAttribute("passportLoginUrl", passportLoginUrl);
//					if (StringUtils.isBlank(passportLoginUrl)) {
//						return "redirect:" + passportLoginUrl;
//					} else {
//						return "/desktop/login/wenxuanLogin.ftl";
//					}
//				}
//				initLoginInfo(getRequest().getSession(), user);
//				return "redirect:/desktop/index/page";
//			}else {
//				return promptFlt(map,"该用户尚未同步");
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map,"登录失败，数据解密异常");
		}

	}
	private void doCheckTicket(HttpServletRequest request, String id) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				String ticket = RedisUtils.get(XyConstant.XINGYUN_LOGIN_TICKET + id);
				System.out.println("第一次ticket ---" + ticket);
				String newTicket = getNewTicket(ticket);
				System.out.println("第二次ticket ---" + newTicket);
				if(StringUtils.isBlank(newTicket)){
					StringBuffer sBuffer = new StringBuffer();
		    		String ipString = Evn.getWebUrl();
		    		ipString = UrlUtils.ignoreLastRightSlash(ipString);
		    		sBuffer.append(ipString);
			    	sBuffer.append("/homepage/remote/openapi/lasa/logoutcallback");
			    	sBuffer.append("?");
			    	sBuffer.append("tokenid");
			    	sBuffer.append("=");
			    	sBuffer.append(RedisUtils.get(XyConstant.XINGYUN_FIRST_LOGIN_TICKET + id));
			    	RedisUtils.del(XyConstant.XINGYUN_FIRST_LOGIN_TICKET + id);
			    	try {
						UrlUtils.get(sBuffer.toString(), StringUtils.EMPTY);
					} catch (IOException e) {
						e.printStackTrace();
						log.error(e.getMessage(), e);
					} finally{
						System.gc();
			            cancel();
					}
				}else{
					RedisUtils.set(XyConstant.XINGYUN_LOGIN_TICKET + id, newTicket);
				}
			}
		};
		Timer timer = new Timer();
		long delay = 0;
		long intevalPeriod = 120 * 1000;
		timer.scheduleAtFixedRate(task, delay, intevalPeriod);
	}

	//--------------------------------------------------私有方法区 ------------------------------------------------------
	/**
	 * 得到星云统一登录地址
	 */
	private String getXyLoginUrl (){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(XyConstant.XY_LOGIN_URL);
		stringBuilder.append("?");
		stringBuilder.append(XyConstant.XY_APP_ID_NAME);
		stringBuilder.append("=");
		stringBuilder.append(XyConstant.XY_APP_ID_VAL);
		stringBuilder.append("&pageURL=");
		stringBuilder.append(" ");
		return stringBuilder.toString();
	}
	
	
	/**
     * 获取到ticket值
     * @return
     */
//    private String getTicket(String token) {
//		String url = XyConstant.XY_GET_TICKET_URL;
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.add(XyConstant.XY_HEAD_TICKET_NAME,  token);
//			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
//			System.out.println("接口请求的url是：---" + url);
//			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
//			String responseBody = responseEntity.getBody();
//			JSONObject json = Json.parseObject(responseBody);
//			System.out.println("接口返回的值是：---" + json);
//			String code = json.getString("code");
//			if("200".equals(code)){
//				String data = json.getString("data");
//				JSONObject dataJsonObject = Json.parseObject(data);
//				return	dataJsonObject.getString(XyConstant.XY_HEAD_TICKET_NAME);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
    
    /**
     * 获取到ticket值
     * @return
     */
    private String getNewTicket(String oldTicket) {
		String url = XyConstant.XY_GET_REFRESH_TICKET_URL;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add(XyConstant.XY_HEAD_TICKET_NAME,  oldTicket);
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			System.out.println("接口请求的新的url是：---" + url);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			System.out.println("接口返回的新的ticket是：---" + json);
			String code = json.getString("code");
			if("200".equals(code)){
				String data = json.getString("data");
				JSONObject dataJsonObject = Json.parseObject(data);
				return dataJsonObject.getString(XyConstant.XY_HEAD_TICKET_NAME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 获取登陆的账号
     * @param ticket
     * @return
     */
    private String getUserNameByTicket(String ticket) {
    	String payload = ticket.split("\\.")[1];
    	String userInfo = null;
		try {
			userInfo = new String(Base64.getDecoder().decode(payload), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	JSONObject json = Json.parseObject(userInfo);
		return json == null ? null : json.getString(XyConstant.XY_LOGIN_NAME);
    }
	
//  ----------------------西固对接的代码------------------------------------
//	@ResponseBody
	@RequestMapping(value = "/remote/openapi/xigu/login")
	public String loginForXiGu(HttpServletRequest request,ModelMap map){
		try {
			String name = getLoginName();
			String code = request.getParameter(XiGuConstant.APP_CODE);
			if(StringUtils.isNotBlank(code) && !"message".equalsIgnoreCase(code)){
				Server server = SUtils.dc(serverRemoteService.findByServerCode(code), Server.class);
				RedisUtils.set(XiGuConstant.XG_APP_ID_KEY, String.valueOf(server.getSubId()));
				RedisUtils.del(XiGuConstant.XG_SERVER_KEY);
			}else {
				RedisUtils.set(XiGuConstant.XG_SERVER_KEY, code);
			}
			if(StringUtils.isBlank(name)){
				return "";
			}
			String  wpUserName = getWpUserName(name);
			User user = SUtils.dc(userRemoteService.findByUsername(wpUserName), User.class);
			if(user != null){
				return "redirect:" + getXgLoginUrl(wpUserName);
			}else{
				String backURL = new StringBuilder().append("/homepage/remote/openapi/xigu/userInfo/")
						.append(wpUserName)
						.append("/edit")
						.toString();
				return "redirect:" + backURL;
			}
		} catch (Exception e) {
			return "redirect:";
		}
	}

	@RequestMapping("/remote/openapi/xigu/userInfo/{username}/edit")
	public String editUser(@PathVariable String username,HttpServletRequest request, ModelMap map) {
		String userInfo = getXGUserInfo(username);
		if(StringUtils.isNotBlank(userInfo)){
			JSONObject jsonObject = Json.parseObject(userInfo);
			User user = new User();
			int userType = getUserType(jsonObject.getString("id"));
			int type = getOwnerType(jsonObject.getString("enName"));
			int unitClass = Unit.UNIT_CLASS_SCHOOL;
			if(XiGuConstant.XIGU_EDU_TEACHER_TYPE == type){
				unitClass = Unit.UNIT_CLASS_EDU;
			}
			List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnitClass(unitClass), Unit.class);
			user.setIdentityCard(jsonObject.getString("idCardNo"));
			user.setMobilePhone(jsonObject.getString("phone"));
			user.setUsername(jsonObject.getString("loginName"));
			user.setRealName(jsonObject.getString("userName"));
			user.setUserType(userType);
			int ownerType = User.OWNER_TYPE_TEACHER;
			boolean isStudent = Boolean.FALSE;
			if(XiGuConstant.XIGU_SCHOOL_STUDENT_TYPE == type){
				ownerType = User.OWNER_TYPE_STUDENT;
				List<Clazz> allClazz = SUtils.dt(classRemoteService.findAll(), Clazz.class);
				allClazz = EntityUtils.filter2(allClazz, t->{
	        		return t.getIsDeleted() == 0;
	        	});
				map.put("allClazz", allClazz);
				isStudent = Boolean.TRUE;
			}
			user.setOwnerType(ownerType);
			map.put("unitList", unitList);
			map.put("user", user);
			map.put("isStudent", isStudent);
		}
		return "/desktop/synbase/saveUser.ftl";
	}
	
	private int getUserType(String openId) {
		int userType =  User.USER_TYPE_COMMON_USER;
		try {
			String url = getRoleInfo(XiGuConstant.XIGU_USER_INFO_URL,openId,XiGuConstant.XIGU_GET_USER_ROLE_METHOD);
			String jsonData = UrlUtils.get(url, new String());
			JSONObject jsonObject = Json.parseObject(jsonData);
		    String roleInfo = jsonObject.getString(XiGuConstant.RESOLVE_DATA_NAME);
		    if(StringUtils.isNotBlank(roleInfo)){
		    	JSONArray array = Json.parseArray(roleInfo);
		    	for (int i = 0; i < array.size(); i++) {
		    		if("ledcdistrictMng".equalsIgnoreCase(array.getString(i)) || "ledcschoolMng".equalsIgnoreCase(array.getString(i))){
		    			userType = User.USER_TYPE_UNIT_ADMIN;
		    		}
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
			log.error("调用接口获取用户类型失败：--------" + e.getMessage());
		}
		return userType;
	}

	@RequestMapping("/remote/openapi/xigu/{username}/login")
	public String loginForXiGu(@PathVariable String username,HttpServletRequest request, ModelMap map) {
		try {
			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if(user != null) {
				String backUrl = getBackUrl(request.getParameter("username"), "username", "/homepage/remote/openapi/xigu/login");
				String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
				if(StringUtils.isNotBlank(logoutResult)){	
					return logoutResult;
				}
				String url;
				String code = RedisUtils.get(XiGuConstant.XG_SERVER_KEY);
				if(StringUtils.isNotBlank(code) && "message".equalsIgnoreCase(code)){
					Server server = SUtils.dc(serverRemoteService.findByServerCode("message"),Server.class);
					url =   UrlUtils.getPrefix(getRequest()) + "/homepage/loginCall?url="+server.getUrlIndex(false);
				}else{
					String appId = RedisUtils.get(XiGuConstant.XG_APP_ID_KEY);
					if(StringUtils.isNotBlank(appId)){
						url = UrlUtils.getPrefix(getRequest()) + "/desktop/unify/unify-desktop.action?appId="+Integer.valueOf(appId);
					}else{
						url = getNativeIndexUrl();
					}
				}
				return loginUserName(user, url, "2", map);
	 	    }else {
	 			return promptFlt(map,"该用户尚未同步");
	 	    }
		} catch (Exception e) {
			return promptFlt(map,"登录失败，数据解密异常");
		}
	}
	
	@ResponseBody
	@RequestMapping("/remote/openapi/xigu/saveUser")
	public String saveUserInfo(@RequestBody String userInfo,HttpServletRequest request, ModelMap map) {
			User user;
			try {
				JSONObject jsonObject = SUtils.dc(userInfo, JSONObject.class);
				user = getUserInfo(jsonObject);
				String userName = user.getUsername();
				if(RedisUtils.hasLocked(userName)){
					 try{
						 User haveUser = SUtils.dc(userRemoteService.findByUsername(userName), User.class);
						 if(haveUser == null){
							 if(User.OWNER_TYPE_TEACHER == user.getOwnerType()){
									Teacher teacher = getTeacher(jsonObject, user);
									baseSyncSaveRemoteService.saveTeacher(new Teacher[]{teacher});
								}else {
									Student student = getStudent(jsonObject, user);
									baseSyncSaveRemoteService.saveStudent(new Student[]{student});
								}
								baseSyncSaveRemoteService.saveUser(new User[]{user});
						 }
					  }catch(Exception e){
						  e.printStackTrace();
					  }finally{
						  RedisUtils.unLock(userName);
					  }
				}
			} catch (Exception e) {
				return error("添加失败");
			}
			return successByValue(getXgLoginUrl(user.getUsername()));
	}
	
	private User getUserInfo(JSONObject jsonObject) {
		User user = new User();
    	user.setId(UuidUtils.generateUuid());
    	user.setOwnerType(Integer.valueOf(jsonObject.getString("ownerType")));
    	user.setSex(Integer.valueOf(jsonObject.getString("sex")));
    	user.setUsername(jsonObject.getString("userName"));
    	user.setUnitId(jsonObject.getString("unitId"));
    	user.setPassword(XiGuConstant.DEFAULT_PASS_WORD_VALUE);
    	user.setRealName(jsonObject.getString("name"));
    	user.setIdentityCard(jsonObject.getString("idCardNo"));
    	user.setMobilePhone(jsonObject.getString("phone"));
    	Unit unit = SUtils.dc(unitRemoteService.findOneById(user.getUnitId()), Unit.class);
    	user.setRegionCode(unit.getRegionCode());
    	user.setUserType(jsonObject.getInteger("userType"));
    	return  user;
	}

	private Teacher getTeacher(JSONObject jsonObject,User user) {
		// TODO Auto-generated method stub
		Teacher teacher = new Teacher();
		teacher.setTeacherName(user.getRealName());
		teacher.setSex(user.getSex());
		teacher.setTeacherCode(jsonObject.getString("teacherCode"));
		teacher.setId(UuidUtils.generateUuid());
		teacher.setUnitId(user.getUnitId());
		teacher.setIdentityCard(user.getIdentityCard());
		teacher.setMobilePhone(user.getMobilePhone());
		user.setOwnerId(teacher.getId());
		return teacher;
	}

	private Student getStudent(JSONObject jsonObject,User user) {
		// TODO Auto-generated method stub
		Student student = new Student();
		student.setStudentName(user.getRealName());
		student.setSex(user.getSex());
		student.setClassId(jsonObject.getString("classId"));
		student.setId(UuidUtils.generateUuid());
		student.setSchoolId(user.getUnitId());
		student.setIdentityCard(user.getIdentityCard());
		student.setMobilePhone(user.getMobilePhone());
		user.setOwnerId(student.getId());
		return student;
	}

	private String getXgLoginUrl(String wpUserName) {
		StringBuilder uBuilder = new StringBuilder();
		uBuilder.append("/homepage/remote/openapi/xigu/");
		uBuilder.append(wpUserName);
		uBuilder.append("/login");
	    return uBuilder.toString();
    }
	
	private Integer getOwnerType (String role){
		Integer userType = XiGuConstant.XIGU_SCHOOL_STUDENT_TYPE;
		if("ledcdistrictMng".equalsIgnoreCase(role) || "instructor".equalsIgnoreCase(role)
				|| "edupersonnel".equalsIgnoreCase(role)){
			userType = XiGuConstant.XIGU_EDU_TEACHER_TYPE;
		}
		if("ledcschoolMng".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)){
			userType = XiGuConstant.XIGU_SCHOOL_TEACHER_TYPE;
		}
		if("student".equalsIgnoreCase(role)){
			userType = XiGuConstant.XIGU_SCHOOL_STUDENT_TYPE;
		}
		return userType;
	}
	
	private String  getXGUserInfo(String username){
		String userInfo =  null;
		try {
			String url = getUserInfoUrl(XiGuConstant.XIGU_USER_INFO_URL,username,XiGuConstant.XIGU_GET_USER_INFO_METHOD);
			String jsonData = UrlUtils.get(url, new String());
			JSONObject jsonObject = Json.parseObject(jsonData);
		    userInfo = jsonObject.getString(XiGuConstant.RESOLVE_DATA_NAME);
		} catch (IOException e) {
			e.printStackTrace();
			userInfo = null;
			log.error("调用接口获取登录用户信息失败：--------" + e.getMessage());
		}
		return userInfo;
	}
	
	private String getWpUserName(String name){
		String wpName =  null;
		try {
			String url = new StringBuilder().append(XiGuConstant.XIGU_GET_WPLOGIN_NAME)
					.append("&loginName=").append(name).toString();
			String jsonData = UrlUtils.get(url, new String());
			JSONObject jsonObject = Json.parseObject(jsonData);
			wpName = jsonObject.getString("wpLoginName");
		} catch (IOException e) {
			wpName = null;
			log.error("调用接口获取万朋登录用户账号失败：--------" + e.getMessage());
		}
		return wpName;
	}
	
	private String getLoginName (){
		Assertion assertion = AssertionHolder.getAssertion();
		Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
		return attributes.get("loginName").toString();
	}
	
	private String getAccessToken(){
		String accessToken = RedisUtils.get(XiGuConstant.XG_TOKEN_REDIS_KEY);
		try {
			String url = getTokenUrl(XiGuConstant.XIGU_ACCESS_TOKEN_URL);
			String jsonData = UrlUtils.get(url, new String());
			JSONObject jsonObject = Json.parseObject(jsonData);
		    accessToken = jsonObject.getString(XiGuConstant.ACCESS_TOKEN_NAME);
		    //提前一分钟过期
		    Integer time = jsonObject.getInteger("expires_in");
			if(StringUtils.isNotBlank(accessToken) && time > 0)
			    RedisUtils.set(XiGuConstant.XG_TOKEN_REDIS_KEY, accessToken, time);
		} catch (IOException e) {
			e.printStackTrace();
			accessToken = null;
			log.error("调用接口获取登录用户信息失败：--------" + e.getMessage());
		}
		return accessToken;
	}
	
	
	private String getTokenUrl(String url) {
		StringBuilder uBuilder = new StringBuilder();
		uBuilder.append(url);
		uBuilder.append("?client_id=");
		uBuilder.append(XiGuConstant.XIGU_APPKEY);
		uBuilder.append("&client_secret=");
		uBuilder.append(XiGuConstant.XIGU_APPSECRET);
		uBuilder.append("&grant_type=client_credentials");
		return uBuilder.toString();
	}
	
	private String getUserInfoUrl(String url, String username, String method) {
		StringBuilder uBuilder = new StringBuilder();
		String commonUrl = getCommonUrl(url,method);
		uBuilder.append(commonUrl);
		uBuilder.append("&key=loginName");
		uBuilder.append("&value=");
		uBuilder.append(username);
		return uBuilder.toString();
	}
	
	private String getRoleInfo(String url, String userId, String method) {
		StringBuilder uBuilder = new StringBuilder();
		String commonUrl = getCommonUrl(url,method);
		uBuilder.append(commonUrl);
		uBuilder.append("&userId=");
		uBuilder.append(userId);
		return uBuilder.toString();
	}
	
    private String getCommonUrl(String url,String method) {
    	StringBuilder uBuilder = new StringBuilder();
		uBuilder.append(url);
		uBuilder.append("?method=" + method);
		uBuilder.append("&format=json");
		uBuilder.append("&version=1.0");
		uBuilder.append("&appkey=");
		uBuilder.append(XiGuConstant.XIGU_APPKEY);
		uBuilder.append("&access_token=");
		uBuilder.append(getAccessToken());
		return uBuilder.toString();
	}
//  ----------------------鹰硕对接的代码------------------------------------
	@RequestMapping("/remote/openapi/yingshuo/login")
	public String loginForYingShuo(HttpServletRequest request, ModelMap map) {
		try {
			String uid = request.getParameter(YingShuoConstant.TICKET_NAME);
			String moduleId = request.getParameter(YingShuoConstant.MODULE_ID);
			String twoParam = request.getParameter("twoParam");
			if(StringUtils.isBlank(uid) && StringUtils.isBlank(twoParam)){
				return promptFlt(map,"登录失败，ticket为空");
			}
			String username;
			if(StringUtils.isNotBlank(twoParam)){
				twoParam = StringEscapeUtils.unescapeHtml(twoParam);
				JSONObject jsonObject = JSON.parseObject(twoParam);
				username = jsonObject.getString(YingShuoConstant.NEW_USER_NAME);
				moduleId = jsonObject.getString(YingShuoConstant.MODULE_ID);
			}else{
				username = getUserName("https://proxy.schoolcloud.ys100.com/es/management/signal/check",uid);
				if(!StringUtils.startsWith(username, YingShuoConstant.YINGSHUO_BEFORE_USERNAME_VALUE)) {
					username = YingShuoConstant.YINGSHUO_BEFORE_USERNAME_VALUE + username;
				}
				System.out.println("得到的账号是---------" + username);
			}
			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if(user != null) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(YingShuoConstant.NEW_USER_NAME,username);
				jsonObject.put(YingShuoConstant.MODULE_ID, moduleId);
				String twoParam1;
				try {
					twoParam1 = URLEncoder.encode(jsonObject.toJSONString(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					twoParam1 = null;
				}
				String backUrl = getBackUrl(twoParam1, "twoParam", "/homepage/remote/openapi/yingshuo/login");
				String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
				if(StringUtils.isNotBlank(logoutResult)){	
					return logoutResult;
				}
				String url = UrlUtils.getPrefix(getRequest()) + "/desktop/unify/unify-desktop.action?module="+moduleId;
				return loginUserName(user, url, "2", map);
	 	    }else {
	 			return promptFlt(map,"该用户尚未同步");
	 	    }
		} catch (Exception e) {
			return promptFlt(map,"登录失败，数据解密异常");
		}
	}
	
	private String getUserName(String url, String uid) {
		String username = null;
		try {
			url = getUrl(url,YingShuoConstant.TICKET_NAME,uid);
			String jsonData = UrlUtils.get(url, new String());
			JSONObject jsonObject = Json.parseObject(jsonData);
			String retCode = jsonObject.getString("code");
			if(YingShuoConstant.SUCCESS_CODE_VALUE.equals(retCode)){
				String data =  jsonObject.getString("data");
				JSONObject dataJsonObject = Json.parseObject(data);
				username = dataJsonObject.getString("account");
			}
		} catch (IOException e) {
			e.printStackTrace();
			username = null;
			log.error("调用接口获取登录用户信息失败：--------" + e.getMessage());
		}
		return username;
	}

	private String getUrl(String url, String name, String value) {
		StringBuilder uBuilder = new StringBuilder();
		uBuilder.append(url);
		if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)){
			if(url.contains("?")){
				uBuilder.append("&"+name+"=");
			}else{
				uBuilder.append("?"+name+"=");
			}
			uBuilder.append(value);
		}
		return uBuilder.toString();
	}
	
	
	@RequestMapping("/remote/openapi/lasa/loginnew")
	public String loginForLaSaNew(HttpServletRequest request, ModelMap map) {
		String username = null;
		//第一步得到当前的username
//		String tokeid = request.getParameter(LaSajyConstant.LS_TOKEN_NAME);
//		String cacheTokenId = RedisUtils.get(LaSajyConstant.LS_SAVE_TOKENID_KEY + tokeid);
//		String modelId = request.getParameter(LaSajyConstant.LS_MODEL_VALUE);
//		String twoParam = request.getParameter("twoParam");
//		if(StringUtils.isBlank(tokeid) && StringUtils.isBlank(twoParam)){
//			return promptFlt(map,"登录失败，tokeid为空");
//		}
//		//第二步，验证tokenid
//		boolean isTrueBoolean = doProvingTokenId(tokeid);
////		logger.error("tokeid是否验证通过：----------------" + isTrueBoolean);
//		if(isTrueBoolean){
//			String random = getRandomByTokenId (tokeid);
////			logger.error("得到的random随机数是：----------------" + random);
//			if(StringUtils.isNotBlank(random)){
//				JSONObject result = getResultJson(tokeid,random);
////				logger.error("得到的result的结果是：----------------" + result);
//				if(result != null){
//					username = getUserName(result);
//				}
//			}
//		}
//		User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
//		logger.error("万朋存在用户的账号信息是：----------------" + username);
//		if(user == null){
//			return promptFlt(map,"该用户尚未同步");
//		}
		map.put("uid", "tcsls");
		return "/desktop/login/LasaTestLogin.ftl";
	}
	
	
	

	//-------------------------------拉萨对接的地址--------------------------
	@RequestMapping("/remote/openapi/lasa/login")
	public String loginForLaSa(HttpServletRequest request, ModelMap map) {
		try {
//			if(doProvingDeploy(DeployRegion.DEPLOY_LASA)){
//				log.error("部署地区没有验证通过 ：-------------" + DeployRegion.DEPLOY_LASA);
//				return promptFlt(map,"不能访问地址");
//			}
			String username = null;
			boolean isTrueBoolean = false;
			String url = null;
			//第一步得到当前的username
			String tokeid = request.getParameter(LaSajyConstant.LS_TOKEN_NAME);
			String cacheTokenId = RedisUtils.get(LaSajyConstant.LS_SAVE_TOKENID_KEY + tokeid);
			String modelId = request.getParameter(LaSajyConstant.LS_MODEL_VALUE);
			String twoParam = request.getParameter("twoParam");
			if(StringUtils.isBlank(tokeid) && StringUtils.isBlank(twoParam)){
				return promptFlt(map,"登录失败，tokeid为空");
			}
			if(StringUtils.isNotBlank(twoParam)){
				twoParam = StringEscapeUtils.unescapeHtml(twoParam);
				JSONObject jsonObject = JSON.parseObject(twoParam);
				tokeid = jsonObject.getString(LaSajyConstant.LS_TOKEN_NAME);
				modelId = jsonObject.getString(LaSajyConstant.LS_MODEL_VALUE);
			}
			if(StringUtils.isNotBlank(modelId)){
				url = getIndexByServerCode(modelId);
			}else {
				url = "/desktop/index/page";
			}
			if(StringUtils.isNotEmpty(tokeid)){
				if(userIsLogin(username) || (StringUtils.isNotBlank(cacheTokenId) && tokeid.equals(cacheTokenId))){
					return "redirect:" + url;
				}else{
					//第二步，验证tokenid
					isTrueBoolean = doProvingTokenId(tokeid);
//					logger.error("tokeid是否验证通过：----------------" + isTrueBoolean);
					if(isTrueBoolean){
						String random = getRandomByTokenId (tokeid);
//						logger.error("得到的random随机数是：----------------" + random);
						if(StringUtils.isNotBlank(random)){
							JSONObject result = getResultJson(tokeid,random);
//							logger.error("得到的result的结果是：----------------" + result);
							if(result != null){
								username = getUserName(result);
							}
						}
					}
				}
			}
//			logger.error("用户的账号信息是：----------------" + username);
			//根据身份证来得到对应的账号
			if(StringUtils.isBlank(username)){
				return "redirect:" + LaSajyConstant.LS_LOGIN_OUT_INDEX_URL;
//				return promptFlt(map,"获取到的用户信息是空");
			}
			if(isTrueBoolean){
				//判断当前登录的账号是否和未退出的账号一致
				LoginInfo currentLogin = null;
				if (getSession() != null && (currentLogin = ServletUtils.getLoginInfo(getSession())) != null) {
					if (!StringUtils.equals(currentLogin.getUserName(), username)) {
						//1.退出eis
						String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
						quitSession();
						//2.退出passport
						if(Evn.isPassport()){
							String paraName;
							if(StringUtils.isBlank(modelId)){
								paraName = "/homepage/remote/openapi/lasa/login?tokenid=" + tokeid;
							}else{
								JSONObject jsonObject = new JSONObject();
								jsonObject.put(LaSajyConstant.LS_TOKEN_NAME, tokeid);
								jsonObject.put(LaSajyConstant.LS_MODEL_VALUE, modelId);
								String twoParam1;
								try {
									twoParam1 = URLEncoder.encode(jsonObject.toJSONString(),"UTF-8");
								} catch (UnsupportedEncodingException e) {
									twoParam1 = null;
								}
								paraName = "/homepage/remote/openapi/lasa/login?twoParam=" +  twoParam1;
							}
							String backURL =  new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
									.append(paraName)
									.toString();
							return quitPassport(backURL,ticket);
						}
					}
				}
				User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
				logger.error("万朋存在用户的账号信息是：----------------" + username);
				if(user != null) {
					//把tokenid 和 user 放在缓存中
					if (Evn.isPassport()) {
						RedisUtils.set(LaSajyConstant.LS_SAVE_TOKENID_KEY + tokeid, tokeid);
						RedisUtils.set(LaSajyConstant.LS_SAVE_TOKENID_BY_USERID_KEY + user.getId() , tokeid);
						getRequest().getSession().setAttribute(LaSajyConstant.LS_SAVE_TOKENID_KEY, tokeid);
						//重定向daopassport
						String passportLoginUrl;
						if(StringUtils.isNotBlank(modelId)){
							//重定向daopassport
							url = getIndexByServerCode(modelId);
//							System.out.println("重定向的地址：--------------" + url);
							url =   UrlUtils.getPrefix(getRequest()) + "/homepage/loginCall?url="+url;
		    				passportLoginUrl = getRedirectUrl(user,url,null, Boolean.TRUE, "1");
						}else {
							passportLoginUrl = getNoSaveUserInfoRedirectUrl(user,"1");
//							logger.error("单点跳转的地址信息是：----------------" + passportLoginUrl);
						}
						map.addAttribute("passportLoginUrl", passportLoginUrl);
						if (StringUtils.isBlank(passportLoginUrl)) {
							return "redirect:" + passportLoginUrl;
						} else {
							return "/desktop/login/wenxuanLogin.ftl";
						}
					}
					initLoginInfo(getRequest().getSession(), user);
					return "redirect:/desktop/index/page";
				}else {
					return "redirect:" + LaSajyConstant.LS_LOGIN_OUT_INDEX_URL;
//					return promptFlt(map,"该用户尚未同步");
				}
			}
			return promptFlt(map,"登录失败，tokenid失效");
		} catch (Exception e) {
			return promptFlt(map,"登录失败，数据异常");
		}
	}

	/**
	 * 根据code 得到indexUrl
	 * @param modelId
	 * @return
	 */
	private String getIndexByServerCode(String code) {
		Server server = SUtils.dc(serverRemoteService.findByServerCode(code), Server.class);
		return server.getUrlIndex(false);
	}

	/**
	 * 根据 modelid 得到 封装的地址
	 * @param modelId
	 * @return
	 */
	private String getFullUrlByMcodeId(String modelId) {
		Model model = SUtils.dc(modelRemoteService.findOneById(Integer.valueOf(modelId)), Model.class);
		Server server = SUtils.dc(serverRemoteService.findBySubId(model.getSubSystem()), Server.class);
		String serverUrl = server.getUrl();
		String protocolModelUrl = UrlUtils.ignoreLastRightSlash(serverUrl) + UrlUtils.SEPARATOR_SIGN + UrlUtils.ignoreFirstLeftSlash(model.getUrl());
		String paramAndProtocolUrl = UrlUtils.addQueryString(protocolModelUrl, "appId", Objects.getVaule(model.getSubSystem(), "-1"));
		return UrlUtils.ignoreLastRightSlash(serverUrl) + UrlUtils.SEPARATOR_SIGN + DeskTopConstant.UNIFY_LOGIN_URL +
                "?url=" + paramAndProtocolUrl;
	}

	/**
	 * 得到对应的账号信息
	 * @param idcardnumber
	 * @return
	 */
	private String getUserName(JSONObject jsObject) {
		String identityCard = jsObject.getString(LaSajyConstant.LS_IDCARDNUMBER);
		identityCard = doChangeParam(identityCard);
		List<User> users = SUtils.dt(userRemoteService.findbyIdentityCard(identityCard), User.class);
		if(CollectionUtils.isNotEmpty(users)){
			return users.get(0).getUsername();
		}
		return null;
	}

	private String doChangeParam(String param) {
		JSONArray uArray = Json.parseArray(param);
		if(CollectionUtils.isNotEmpty(uArray)){
			param =  (String) uArray.get(0);
		}
		return param;
	}

	/**
	 * 得到当前登录的身份证号码
	 * @param tokeid
	 * @param random
	 * @return
	 */
	private JSONObject getResultJson(String tokeid, String random) {
		String url = LaSajyConstant.LS_GET_USERINFO_URL + "?tokenId=" + tokeid 
				+ "&random=" + random;
		try {
			String result = UrlUtils.get(url, new String());
			return Json.parseObject(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 得到random值
	 * @param tokeid
	 * @return
	 */
	private String getRandomByTokenId(String tokeid) {
		try {
			String url = LaSajyConstant.LS_GET_RANDOM_URL + "?tokenId=" + tokeid;
			String result = UrlUtils.get(url, new String());
			if(StringUtils.isNotBlank(result)){
				JSONObject jsObject = Json.parseObject(result);
				return jsObject.getString(LaSajyConstant.LS_RANDOM_NAME);
			}
		} catch (IOException e) {
			log.error("获取random失败",e);
		}
		return null;
	}

	/**
	 * 验证tokenid的合法性
	 * @param tokeid
	 * @return
	 */
    private boolean doProvingTokenId(String tokeid) {
    	try {
    		String url = LaSajyConstant.LS_PROVING_TOKEN_URL + "?tokenid=" + tokeid;
    		logger.error("验证token的url地址：----------------" + url);
    		String result = UrlUtils.get(url, new String());
			logger.error("得到的结果是：----------------" + result);
			if(StringUtils.isNotBlank(result)){
				result = result.replaceAll("\r", "");
				result = result.replaceAll("\n", "");
				return result.equalsIgnoreCase(LaSajyConstant.LS_PROVING_TOKEN_NAME) ? Boolean.TRUE : Boolean.FALSE;
			}
		} catch (IOException e) {
			return Boolean.FALSE;
		}
		return false;
	}
//  ----------------------文轩对接的代码------------------------------------
	@RequestMapping("/remote/openapi/login")
	public String loginForWenXuan(HttpServletRequest request, ModelMap map) {
		try {
			String username = request.getParameter("uid");
			String witaccount = request.getParameter("witaccount");
			String targetUrl = request.getParameter("targetUrl");
			String twoParam = request.getParameter("twoParam");
			if(StringUtils.isNotBlank(twoParam)){
				twoParam = StringEscapeUtils.unescapeHtml(twoParam);
				JSONObject jsonObject = JSON.parseObject(twoParam);
				username = jsonObject.getString("uid");
				targetUrl = jsonObject.getString("targetUrl");
			}
			//解密
			username = WXPlatformUtils.decrypt(username, WenXunConstant.WENXUN_APP_KEY_VALUE);
            if(StringUtils.isNotBlank(witaccount) && StringUtils.isNotBlank(username)){
				RedisUtils.set(WenXunConstant.WENXUN_WAIT_ACCOUNT_REDIS_KEY+username, witaccount);
			}
			if(!StringUtils.startsWith(username, WenXunConstant.WENXUN_BEFORE_USERNAME_VALUE)) {
				username = WenXunConstant.WENXUN_BEFORE_USERNAME_VALUE + username;
			}
			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if(user != null) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("uid",request.getParameter("uid"));
				jsonObject.put("targetUrl", targetUrl);
				String twoParam1;
				try {
					twoParam1 = URLEncoder.encode(jsonObject.toJSONString(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					twoParam1 = null;
				}
				String backUrl = getBackUrl(twoParam1, "twoParam", "/homepage/remote/openapi/login");
				String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
				if(StringUtils.isNotBlank(logoutResult)){	
					return logoutResult;
				}
				return loginUserName(user, targetUrl, "3", map);
	 	    }else {
	 			return promptFlt(map,"该用户尚未同步");
	 	    }
		} catch (Exception e) {
			return promptFlt(map,"登录失败，数据解密异常");
		}
	}
	
	//------------------------------东莞对接的代码---------------------------------------
	@RequestMapping("loginCall")
	public String LoginCallbackHandler(String url) {
		if((!doProvingDeploy(DeployRegion.DEPLOY_XINGYUN)) && StringUtils.isNotBlank(url)){
			url = url + "&modelId=69002&appId=69";
		}
		return "redirect:" + url;
	}
	
	@RequestMapping(value = "/remote/openapi/dgjy/login")
	public String loginForDgjy(HttpServletRequest request,HttpServletResponse response, ModelMap map) {
		//判断部署地区是否是东莞
		if(doProvingDeploy(DeployRegion.DEPLOY_DONGGUAN)){
			return promptFlt(map,"不能访问地址");
		}
		boolean isTrueBoolean = false;
		//第一步得到当前的username
		String identity = request.getParameter("identity");
		String username = null;
		if(StringUtils.isNotBlank(identity)){
			identity = StringEscapeUtils.unescapeHtml(identity);
			JSONObject jsObject = Json.parseObject(identity);
			username = jsObject.getString(DgjyConstant.DG_USER_NAME.toUpperCase());
		}
		String state = request.getParameter(DgjyConstant.DG_STATE_NAME);
		String twoParam = request.getParameter("twoParam");
		if(StringUtils.isBlank(state) && StringUtils.isBlank(username) && StringUtils.isBlank(twoParam)){
			return promptFlt(map,"当前的账号是空，请重新登录");
		}
		if((StringUtils.isNotBlank(username) || StringUtils.isNotBlank(twoParam))&& StringUtils.isBlank(state)){
			JSONObject jsonObject = new JSONObject();
			String urlString;
			if(StringUtils.isNotBlank(username)){
				urlString = request.getParameter("url");
				jsonObject.put(DgjyConstant.DG_USER_NAME, username);
				jsonObject.put(DgjyConstant.DG_URL_NAME, urlString);
			}else{
				twoParam = StringEscapeUtils.unescapeHtml(twoParam);
				jsonObject = JSON.parseObject(twoParam);
				urlString = jsonObject.getString(DgjyConstant.DG_URL_NAME);
				username = jsonObject.getString(DgjyConstant.DG_USER_NAME);
			}
			try {
				state = URLEncoder.encode(jsonObject.toJSONString(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				state = null;
			}
			request.setAttribute(DgjyConstant.DG_STATE_NAME, state);
			request.getSession().setAttribute("dg_userName", username);
			request.getSession().setAttribute("dg_goUrl", urlString);
		}
		//第二步进行登录信息验证
		try {
		   isTrueBoolean =	attemptAuthentication(request, response);
		} catch (ServletException | IOException e) {
			return promptFlt(map,"验证信息异常，请稍后登录");
		}
		//第三步开始验证登录
		try {
			if(isTrueBoolean && StringUtils.isNotBlank(state)){
				state = StringEscapeUtils.unescapeHtml(state);
				JSONObject jsonObject = JSON.parseObject(state);
				String endName = jsonObject.getString(DgjyConstant.DG_USER_NAME);
				String beforeName =   (String) request.getSession().getAttribute("dg_userName");
				String url = (String) request.getSession().getAttribute("dg_goUrl");
				log.error("最后的登录信息：" + endName);
				if(StringUtils.isNotBlank(endName) && StringUtils.isNotBlank(beforeName) && StringUtils.isNotBlank(endName) && beforeName.equals(endName)){
					//判断当前登录的账号是否和未退出的账号一致
					  username = beforeName;
					  log.error("是否开始登录信息：" + endName);
					  return doLoginPassport(map, username, url);
				}else {
					  return promptFlt(map,"前后的账号不一致，请重新登录");
				}
			}else{
				return promptFlt(map,"还没有登录成功");
			}
		} catch (Exception e) {
			System.out.println("登录的异常 ----" + e);
			return promptFlt(map,"登录出现异常，请稍后登录");
		}
	}
	
	private String doLoginPassport(ModelMap map, String username, String url) {
		log.error("进入单点登录的页面");
		LoginInfo loginInfo = getLoginInfo();
        if(userIsLogin(username))	{	
        	    return "redirect:" + url;
        } else{
        	log.error("没有登录，开始登录passport");
        	//1.退出eis
        	if (getHttpSession() != null && loginInfo != null && !loginInfo.getUserName().equals(username)){
        		//1.退出eis
        		String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
				quitSession();
				//2.退出passport
				if(Evn.isPassport()){
					String paraName;
        			if(StringUtils.isBlank(url)){
        				paraName = "/homepage/remote/openapi/login?uid=" + username;
        			}else{
        				JSONObject jsonObject = new JSONObject();
        				jsonObject.put(DgjyConstant.DG_USER_NAME, username);
        				jsonObject.put(DgjyConstant.DG_URL_NAME, url);
        				String twoParam;
        				try {
        					twoParam = URLEncoder.encode(jsonObject.toJSONString(),"UTF-8");
        				} catch (UnsupportedEncodingException e) {
        					twoParam = null;
        				}
        				paraName = DgjyConstant.DG_REDIRECT_URI + "?twoParam=" +  twoParam;
        			}
					String backURL =  new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
        					.append(paraName)
        					.toString();
					return quitPassport(backURL,ticket);
				}
        	}
        	//重新登录
        	User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
        	log.error("登录的用户是："+ user.getUsername());
    		if(user != null) {
    			if (Evn.isPassport()) {
    				//重定向daopassport
    				url =   UrlUtils.getPrefix(getRequest()) + "/homepage/loginCall?url="+url;
    				String passportLoginUrl = getRedirectUrl(user, url,null,Boolean.TRUE,"1");
    				map.addAttribute("passportLoginUrl", passportLoginUrl);
    				if (StringUtils.isBlank(passportLoginUrl)) {
    					return "redirect:" + passportLoginUrl;
    				} else {
    					return "/desktop/login/wenxuanLogin.ftl";
    				}
    			}
    			initLoginInfo(getRequest().getSession(), user);
    			return "redirect:" + (StringUtils.isNotBlank(url) ? url : "/desktop/index/page");
    	   }else {
    			return promptFlt(map,"登录的用户不存在");
    	   }
        }
  }
	
    public boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
	    boolean isTrue = false;
        //获得id_token
		final String idToken = request.getParameter(DgjyConstant.DG_ID_TOKEN_NAME);
    	if(idToken == null || idToken.trim().length() == 0) {
    		String stateString  = (String) request.getAttribute(DgjyConstant.DG_STATE_NAME);
    		String serverName = getRequest().getScheme() //当前链接使用的协议
                    +"://" + getRequest().getServerName()//服务器地址
                    + getRequest().getContextPath();
    		System.out.println("serverName ----------" + serverName);
    		String redirectUri = new StringBuilder().append(serverName).append(DgjyConstant.DG_REDIRECT_URI).toString();
    		String loginUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s&nonce=%s&response_mode=%s&state=%s", 
    				DgjyConstant.DG_LOGIN_AUTHORIZE_URL, DgjyConstant.DG_CLIENT_ID_VALUE, redirectUri, DgjyConstant.DG_RESPONSE_TYPE_VALUE, 
    				DgjyConstant.DG_SCOPE_VALUE, DgjyConstant.DG_NONCE_VALUE, DgjyConstant.DG_RESPONSE_MODE_VALUE, stateString);
    		response.sendRedirect(loginUrl);
    	} else {
    		//1. 获得授权码
//    		final String idToken1 = request.getParameter("id_token");
//    		final String accessToken = request.getParameter("access_token");
//    		try {
//                String kid = JwtHelper.headers(idToken1).get("kid");
//                //2. 验证Server已经授权
//                final Jwt tokenDecoded = JwtHelper.decodeAndVerify(idToken1, verifier(kid));
//                final Map<String, String> authInfo = new ObjectMapper().readValue(tokenDecoded.getClaims(), Map.class);
//                logger.info("LoginSuccessHandler###################authInfo::::{}", new ObjectMapper().writeValueAsString(authInfo));
//                //3. 验证授权没过期
//                verifyClaims(authInfo);
//                //4. 获取用户详细信息
//                String userInfoJson = HttpClientUtils.loadUserInfo(authConfig.getUserinfoUri(), accessToken);
//                logger.info("LoginSuccessHandler###################userInfoJson::::{}", userInfoJson);
//                /*====================================================
//    			 * 5. 封装票据UserDetails到上下文
//    			 * 此处代码仅作参考，根据自身的项目情况做集成
//    			 *====================================================*/
//                AuthedUserInfo userinfo = new AuthedUserInfo();
//                userinfo.setIdToken(idToken1);
//                userinfo.setAccessToken(accessToken);
//                userinfo.setSub(authInfo.get("sub"));
//                // authInfo 其他自己选吧
//                // userInfoJson 其他自己选吧
//                request.getSession().setAttribute("loginedUserInfo", userinfo);
//            } catch (final Exception e) {
//                isTrue = Boolean.FALSE;
//            }
    		isTrue = Boolean.TRUE;
    	}
    	return isTrue;
    }
    
//    -----------------------------滨江对接的代码----------------------------------------
    @RequestMapping(value = "/remote/openapi/binjiang/login")
	public String loginForBjjy(HttpServletRequest request,HttpServletResponse response, ModelMap map) {
		//判断部署地区是否是滨江
		if(doProvingDeploy(DeployRegion.DEPLOY_BINJIANG)){
			return promptFlt(map,"不能访问地址");
		}
		boolean isTrueBoolean = false;
		//第一步得到当前的username
		String username = request.getParameter("uid");
		if(StringUtils.isNotBlank(username)){
			return doLogin(request, username, map);
		}else{
			//走鼎永那边的平台认证
			String code = request.getParameter(BjjyConstant.BJ_CODE_NAME);
			String state = request.getParameter(BjjyConstant.BJ_STATE_NAME);
			if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(state)){
				if(BjjyConstant.BJ_STATE_VALUE.equals(state)){
					username = getUserName(code);
					isTrueBoolean = Boolean.TRUE;
				}
			}else{
				try {
					isTrueBoolean = attemptAuthen(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
					return promptFlt(map,"登录出现异常，请稍后登录");
				}
			}
		}
		//开始登陆
		if( isTrueBoolean && StringUtils.isNotBlank(username)){
			try {
				return doLogin(request, username, map);
			}catch (Exception e) {
				return promptFlt(map,"登录出现异常，请稍后登录");
			}
		}
		return promptFlt(map,"登录出现异常，请稍后登录");
	}
    
    
    
    /**
     * 请求鼎永那边的认证地址
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private boolean attemptAuthen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    boolean isTrue = false;
        //获得id_token
		final String code = request.getParameter(BjjyConstant.BJ_CODE_NAME);
    	if(code == null || code.trim().length() == 0) {
    		String redirectUri = getRedirectUrl(BjjyConstant.BJ_REDIRECT_URI);
    		String loginUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s", 
    				BjjyConstant.BJ_GET_CODE_URL,BjjyConstant.BJ_CLIENT_ID_VAL, redirectUri, BjjyConstant.BJ_CODE_NAME, 
    				BjjyConstant.BJ_SCODE_VALUE, BjjyConstant.BJ_STATE_VALUE);
    		response.sendRedirect(loginUrl);
    	} else {
    		isTrue = Boolean.TRUE;
    	}
    	return isTrue;
    }
    
    
    /**
     * 根据用户名来登录我们的系统
     * @param userName
     * @return
     */
    private String doLogin(HttpServletRequest request,String username, ModelMap map) {
    	User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
    	if(user != null) {
			String backUrl = getBackUrl(Base64.getEncoder().encodeToString(username.getBytes()), "uid", "/homepage/remote/openapi/login/index");
			String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
			if(StringUtils.isNotBlank(logoutResult)){	
				return logoutResult;
			}
			Server server = null;
			String  url = null;
			if (!loginOn7) {
				server = SUtils.dc(serverRemoteService.findByServerCode("eis_basedata"), Server.class);
				url = server.getUrl();
			}else{
				url = getNativeIndexUrl();
			}
			System.out.println("重定向的地址：--------------" + url);
			return loginUserName(user, url, "2", map);
 	    }else {
 			return promptFlt(map,"该用户尚未同步");
 	    }
//    	LoginInfo currentLogin = null;
//		if (getSession() != null && (currentLogin = ServletUtils.getLoginInfo(getSession())) != null) {
//			//1.退出eis
//			HttpSession httpSession = getHttpSession();
//			String ticket = (String) httpSession.getAttribute(LoginConstant.TICKET_KEY);
//			if (!StringUtils.equals(currentLogin.getUserName(), username)) {
//				String pSessionId = (String) httpSession.getAttribute(LoginConstant.SESSION_ATTRIBUTE_NAME);
//				//删除 passport session id 可以保证完全退出
//				redisTemplate.delete(Constant.PASSPORT_TICKET_KEY + pSessionId);
//				getSession().invalidate();
//				getHttpSession().invalidate();
//				//2.退出passport
//				if (Evn.isPassport()) {
//					PassportClient client = PassportClientUtils.getPassportClient();
//					return "redirect:"+client.getLogoutURL(ticket, new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
//							.append("/homepage/remote/openapi/login/index")
//							.append("?uid=")
//							.append(username)
//							.toString());
//				}
//			}
//		}
//		log.debug("当前登录的账号是：-----------------" + username);
//		User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
//		if(user != null) {
//			if (Evn.isPassport()) {
//				//重定向daopassport
//				Server server = null;
//				String  url = null;
//				if (!loginOn7) {
//					server = SUtils.dc(serverRemoteService.findByServerCode("eis_basedata"), Server.class);
//					url = server.getUrl();
//				}
//				System.out.println("重定向的地址：--------------" + url);
//				String passportLoginUrl = getRedirectUrl(user,url,server,Boolean.TRUE,null);
//				map.addAttribute("passportLoginUrl", passportLoginUrl);
//				if (StringUtils.isBlank(passportLoginUrl)) {
//					return "redirect:" + passportLoginUrl;
//				} else {
//					return "/desktop/login/wenxuanLogin.ftl";
//				}
//			}
//			initLoginInfo(getRequest().getSession(), user);
//			return "redirect:/desktop/index/page";
//		}else {
//			return promptFlt(map,"该用户尚未同步");
//		}
	}
    /**
     * 获取登陆的账号
     * @param code
     * @param state
     * @return
     */
    private String getUserName(String code) {
    	String username = null;
    	String redirectUri = getRedirectUrl(BjjyConstant.BJ_REDIRECT_URI);
		String accessToken = getAccessToken(code,redirectUri);
		if(StringUtils.isNotBlank(accessToken)){
			 username = getUserNameByToken(accessToken);
	    }
		return username;
    }
    
	/**
     * 获取到token值
     * @param code
     * @param redirectUri
     * @return
     */
    private  String getAccessToken(String code, String redirectUri) {
		String url = BjjyConstant.BJ_GET_ACCESS_TOKEN_URL;
		String authorization = BjjyConstant.BJ_CLIENT_ID_VAL + ":" + BjjyConstant.BJ_CLIENT_SECRET_VAL;
		try {
			 authorization = Base64.getEncoder().encodeToString(authorization.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			authorization = null;
			log.error("authorization参数进行base64编码失败----------" + e.getMessage());
			return null;
		}
		authorization = BjjyConstant.BJ_GET_TOKEN_PREFIX + authorization;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("Authorization", authorization);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "authorization_code");
			map.add("code", code);
			map.add("redirect_uri", redirectUri);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			String responseBody = responseEntity.getBody();
			JSONObject json = Json.parseObject(responseBody);
			return URLEncoder.encode(json.getString("access_token"), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 根据token获取当前登陆的账号
     * @param accessToken
     * @return
     */
    private  String getUserNameByToken(String accessToken) {
		String url = BjjyConstant.BJ_GET_USER_INFO_URL;
		String authorization = BjjyConstant.BJ_GET_USER_TOKEN_PREFIX + accessToken;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authorization);
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			return URLEncoder.encode(json.getString("account"), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 得到返回的地址
     * @return
     */
    private String getRedirectUrl(String url){
    	return new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
				.append(url)
				.toString();
    }
    
    // ---------------长治对接代码--------------------------------------------------------------------------------
    @RequestMapping("/remote/openapi/changzhi/czlogin")
    public String czLogin(HttpServletRequest req, ModelMap map) {
    	try {
			String token = req.getParameter("token");
//			token = "04f9ac5efd7d8a3b480b71162ac9b2381561347125";//TODO
			if (StringUtils.isEmpty(token)) {
				log.error("登录失败：[token]为空！");
				return promptFlt(map, "登录出现异常，参数问题[token参数为空]");
			}
			String sig = ChangZhiConstant.signMd5(token);
			// url?token=&app_key=?&sig=
			StringBuilder url = new StringBuilder(ChangZhiConstant.CZ_GETUSER_URL).append("?").append("token=")
					.append(token).append("&app_key=").append(ChangZhiConstant.CZ_APP_KEY).append("&sig=").append(sig);
			String jsonData = UrlUtils.get(url.toString(), new String());
//			System.out.println(jsonData);
			JSONArray ar = Json.parseArray(jsonData);
			JSONObject jsonObject = ar.getJSONObject(0);//Json.parseObject(jsonData);
			String username = jsonObject.getString(ChangZhiConstant.CZ_LOGIN_USERNAME);
//			username = "hfyz";
			if (StringUtils.isNotBlank(username)) {
				loginOn7 = true;
				return doLogin(req, username, map);
			} 
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return promptFlt(map,"登录出现异常，请稍后登录");
    }
    
    // -------------------------------------------------------公共方法区 ------------------------------------------
    /**
     * 验证部署地区
     * @param region
     * @return
     */
    private boolean doProvingDeploy(String region) {
		String	deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
		return !region.equals(deployRegion);
	}
    
    /**
     * 推出之前的账号，重新登录
     * @param backURL
     * @return
     */
    private String quitPassport(String backURL,String ticket) {
		PassportClient client = PassportClientUtils.getPassportClient();
		return "redirect:"+client.getLogoutURL(ticket, backURL);
	}

    /**
     * 清除session
     * @param currentLogin
     * @param username
     */
	private void quitSession() {
		HttpSession httpSession = getHttpSession();
			String pSessionId = (String) httpSession.getAttribute(LoginConstant.SESSION_ATTRIBUTE_NAME);
			//删除 passport session id 可以保证完全退出
			redisTemplate.delete(Constant.PASSPORT_TICKET_KEY + pSessionId);
			getSession().invalidate();
			getHttpSession().invalidate();
//		}
	}
	
	/**
	 * 判断用户是否已经登录
	 * @param username
	 * @return
	 */
	private boolean userIsLogin(String username) {
		boolean preview = false;
		if(getHttpSession() != null){
			String pSessionId1 = (String) getHttpSession().getAttribute(LoginConstant.SESSION_ATTRIBUTE_NAME);
        	String pString = redisTemplate.opsForValue().get(Constant.PASSPORT_TICKET_KEY + pSessionId1);
        	preview = StringUtils.isNotBlank(pString);
		}
        return (preview && getSession() != null && ServletUtils.getLoginInfo(getSession()) != null && ServletUtils.getLoginInfo(getSession()).getUserName().equals(username));
	}
	
	
	@ResponseBody
	@RequestMapping("/remote/openapi/lasa/logoutcallback")
	public String loginOutLasa(HttpServletRequest request, ModelMap map) {
		String code = "500";
		String msg = "";
		try {
			String tokeid = request.getParameter(LaSajyConstant.LS_TOKEN_NAME);
			if(StringUtils.isBlank(tokeid)){
			    return returnSuccess(code,"退出失败，tokeid为空");
			}
			//得到登陆的 passportSessionId 和 ticket
			String passportSessionId = RedisUtils.get(tokeid);
			logger.error("登录用户的passportSessionId值是：----------------" + passportSessionId);
			String ticket = RedisUtils.get(LoginConstant.TICKET_KEY + tokeid);
			logger.error("登录用户的ticket值是：----------------" + ticket);
			//2.退出passport
			PassportClient client = PassportClientUtils.getPassportClient();
			String  urlString = client.getLogoutURL(ticket, null);
			System.out.println(urlString+"----------");
			try {
				HttpHeaders headers = new HttpHeaders();
				String session = "JSESSIONID=" + passportSessionId;
				headers.set("Cookie", session);
				HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
				ResponseEntity<String> responseBody = restTemplate.exchange(urlString, HttpMethod.GET, requestEntity, String.class);
			} catch (Exception e) {
				log.error(e);
			}
			//1.退出eis
			redisTemplate.delete(Constant.PASSPORT_TICKET_KEY + passportSessionId);
			//删除对应的缓存
			RedisUtils.del(tokeid);
			RedisUtils.del(LoginConstant.TICKET_KEY + tokeid);
			code = "0";
			msg = "管理平台成功退出";
			return returnSuccess(code,msg);
		} catch (Exception e) {
			return returnSuccess(code,"退出异常，请稍后退出");
		}
	}
	
	/**
	 * 通用的 得到 backUrl
	 * @param param 请求地址后带的参数值  
	 * @param paramName 请求地址后带的参数名称
	 * @param url   跳转的地址
	 * @return
	 */
	private String getBackUrl(String param, String paramName, String url) {
		if(StringUtils.isNotBlank(url)){
			url = url + "?" + paramName + "=" + param;
		}
		return url;
	}
	/**
	 * 
	 * 通用的退出当前账号的方法
	 * @param loginUsername 最新需要登录的用户名
	 * @param backUrl  退出当前登录的账号，重新登录的地址
	 */
	private String logoutBeforeUsername(String loginUsername,String backUrl){
		LoginInfo loginInfo = getLoginInfo();
		//1.退出eis
     	if (getHttpSession() != null && loginInfo != null && !loginInfo.getUserName().equals(loginUsername)){
     		//1.退出eis
     		String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
				quitSession();
				//2.退出passport
				if(Evn.isPassport()){
					String backURL =  new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
     					.append(backUrl)
     					.toString();
					return quitPassport(backURL,ticket);
				}
     	}
		return null;
	}
	
	/**
	 * 通用登录passport 的方法
	 * @param urlType  1--默认7.0的首页  2--自己特定的地址  3--统一先登录7.0后再重定向到 目标地址（/homepage/loginCall?url= ）
	 * @param user 
	 * @param url 
	 */
	private String  loginUserName(User user,String url, String urlType,ModelMap map){
		if (Evn.isPassport()) {
			String endUrl = null;
			if(StringUtils.isNotBlank(url)){
				if("2".equals(urlType)){
					endUrl =   url;
				}else if ("3".equals(urlType)){
					endUrl =   UrlUtils.getPrefix(getRequest()) + "/homepage/loginCall?url="+url;
				}
			}else{
				endUrl = getNativeIndexUrl();
			}
			//重定向daopassport
			String passportLoginUrl = getRedirectUrl(user, endUrl,null,Boolean.TRUE, "1");
			map.addAttribute("passportLoginUrl", passportLoginUrl);
			if (StringUtils.isBlank(passportLoginUrl)) {
				return "redirect:" + passportLoginUrl;
			} else {
				return "/desktop/login/wenxuanLogin.ftl";
			}
		}
		initLoginInfo(getRequest().getSession(), user);
		return "redirect:/desktop/index/page";
	}
}
