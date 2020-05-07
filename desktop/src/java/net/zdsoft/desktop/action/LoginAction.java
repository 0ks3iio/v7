package net.zdsoft.desktop.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.login.action.EisLoginController;
import net.zdsoft.desktop.utils.CodeUtils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.SmsRemoteService;
import net.zdsoft.system.service.sms.entity.ZDConstant;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class LoginAction extends EisLoginController {

	protected static final Logger logger = LoggerFactory.getLogger(LoginAction.class);
	@Autowired
	private SmsRemoteService smsRemoteService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	
	/**
	 * 为passport登录页提供服务
	 */
	private String getIndexURL() {
		return UrlUtils.getPrefix(getRequest());
	}

	private String getPassportURL() {
		return sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ?
				sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.PASSPORT_SECOND_URL)
				: sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.PASSPORT_URL);
	}

	@ResponseBody
	@ControllerInfo("密码解密")
	@RequestMapping("/common/decodePwd")
	public String decodePwd(String pwd) {
		return PWD.decode(pwd);
	}

	@ResponseBody
	@RequestMapping(value = "switch/account", produces = "text/html; charset=UTF-8")
	public String switchAccount(@RequestParam(name = "c1", required = false) String userName,
								@RequestParam(name = "c2", required = false) String password,
								ModelMap map){
		try {
			if ( StringUtils.isBlank(userName) || StringUtils.isBlank(password) ) {
				return "redirect:/homepage/loginPage/page";
			}
			String pwd = null;
			User user = SUtils.dc(userRemoteService.findByUsername(userName),User.class);
			if ( user == null || !(pwd=PWD.decode(user.getPassword())).equals(PWD.decode(password)) ) {
				return "redirect:/homepage/loginPage/page";
			}
			map.put("redirectUrl",getRedirectUrl(user));
			getResponse().getWriter().write("<script src='" + getRedirectUrl(user)+"'></script>");
			return null;
		} catch (Exception e) {
			return "redirect:"+getIndexURL();
		}
	}

	String getRedirectUrl(User user){
		return getRedirectUrl(user,null);
	}

	String getRedirectUrl(User user,String url){
		return getRedirectUrl(user, url, null);
	}
	
	String getRedirectUrl(User user,String url,boolean isSimpleLogin){
		return getRedirectUrl(user, url,null,isSimpleLogin, null);
	}
	
	String getRedirectUrl(User user,String url, Server server){
		return getRedirectUrl(user, url, null, Boolean.FALSE, null);
	}
	
	String getNoSaveUserInfoRedirectUrl(User user,String cookieSaveType){
		return getRedirectUrl(user, null, null, Boolean.FALSE, cookieSaveType);
	}
	
	/**
	 * 
	 * @param user 
	 * @param url
	 * @param server
	 * @param isSimpleLogin  是否只作为 用户名 进行登陆
	 * @param cookieSaveType 是否记住账号和密码，实现自动登陆   1--不记住 3--记住
	 * @return
	 */
	String getRedirectUrl(User user,String url, Server server, boolean isSimpleLogin, String cookieSaveType){
		try {
			String serverId;
			if(server != null) {
				serverId = String.valueOf(server.getId());
			}else{
				serverId = Evn.getString(Constant.PASSPORT_SERVER_ID);
			}
			String contextPath = getRequest().getContextPath(); 
			String pwd = PWD.decode(user.getPassword());
			String password = DigestUtils.md5Hex(pwd) + DigestUtils.shaHex(pwd);
			StringBuilder passportScriptLogin = new StringBuilder();
			passportScriptLogin.append(getPassportURL())
					.append("/scriptLogin?action=simpleLogin&d1=")
					.append(new String(Base64.getEncoder().encode(URLEncoder.encode(user.getUsername(),"UTF-8").getBytes())))
					.append("&cookieSaveType=").append(StringUtils.isNotBlank(cookieSaveType)?cookieSaveType : "3")
					.append("&server=").append(serverId)
					.append("&root=").append(StringUtils.isEmpty(contextPath)?"1":"0")
					.append("&d2=").append(password)
					.append("&loginMode=")
					.append(isSimpleLogin ? "1" : "0")
					.append("&multiAccountByOneMatch=1&multiAccount=1")
					.append("&verifyCode=").append("&url=").append(URLEncoder.encode(StringUtils.isBlank(url)?getIndexURL():url,"UTF-8")).append("&id=").append(System.currentTimeMillis());
			return passportScriptLogin.toString();
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	//@RequestMapping("/logout/page")
	//@ControllerInfo("登出系统")
	//public String showLogout(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
	//						 ModelMap map, @RequestParam(name = "userName", required = false) String userName) {
	//	Session session = Session.get(httpSession.getId());
	//
	//	String ticket = session.getAttribute("ticket", String.class);
	//	if ( StringUtils.isBlank(ticket) ) {
	//		ticket = (String)httpSession.getAttribute("ticket");
	//	}
	//	String logoutBackUrl = null;
	//	User currentUser = SUtils.dc(userRemoteService.findById(getLoginInfo().getUserId()), User.class);
	//	//删除passportSessionId 以解决集群问题
	//	String sessionId = (String) httpSession.getAttribute("sessionId");
	//	redisTemplate.delete(Constant.PASSPORT_TICKET_KEY + sessionId);
	//	//切换账号
	//	if ( StringUtils.isNotBlank(userName) ) {
	//		User user = SUtils.dc(userRemoteService.findByUsername(userName), User.class);
	//		logoutBackUrl = user != null ? UrlUtils.ignoreLastRightSlash(Utils.getPrefix(request)) + "/fpf/switch/account?userName=" + user.getUsername() + "&password=" + PWD.encodeIfNot(user.getPassword()):null;
	//		String jsonPath = request.getSession().getServletContext().getRealPath("/desktop/homepage/template/functionAreaUserSet.json");
	//		getResponse().setContentType("application/json;charset=UTF-8");
	//		try {
	//			ResultDto resultDto = new ResultDto();
	//			if ( user == null || !user.getIsDeleted().equals(0) ){
	//				resultDto.setMsg("[" + userName +"]账号已被删除，请联系管理员")
	//						.setSuccess(Boolean.FALSE);
	//			}
	//			else if ( !PWD.decode(user.getPassword()).equals(PWD.decode(currentUser.getPassword())) ) {
	//				resultDto.setMsg("该账户密码已更改，切换失败")
	//						.setSuccess(Boolean.FALSE);
	//			} else {
	//				RedisUtils.del(DeskTopConstant.UNIFY_DESKTOP_KEY + getLoginInfo().getUserId());
	//				session.invalidate();
	//				httpSession.invalidate();
	//				resultDto.setMsg("可以切换")
	//						.setSuccess(Boolean.TRUE)
	//						.setBusinessValue(getLogoutUrl(ticket, logoutBackUrl));
	//			}
	//			FileUtils.writeStringToFile(new File(jsonPath), resultDto.toJSONString(),"UTF-8");
	//			return "/desktop/homepage/template/functionAreaUserSet.json";
	//		} catch (Exception e){
	//			e.printStackTrace();
	//		}
	//	}
	//
	//	RedisUtils.del(DeskTopConstant.UNIFY_DESKTOP_KEY + getLoginInfo().getUserId());
	//	session.invalidate();
	//	httpSession.invalidate();
	//
	//	String unifyLogoutUrl = getLogoutUrl(ticket, logoutBackUrl);
	//	return "redirect:" + (unifyLogoutUrl == null ? "/homepage/loginPage/page" : unifyLogoutUrl);
	//}

//	private String getLogoutUrl(String ticket, String logoutBackUrl) {
//		if (StringUtils.isNotBlank(ticket) && Evn.getBoolean("connection_passport")) {
//			try {
//				PassportClientUtils.getPassportClient().invalidate(ticket);
//				// 如果未置统一的登出地址，则登出到默认的地址（~）
//				String logOutUrl = Evn.getLogOutUrl();
//				String indexURL = getIndexURL();
//				String url = PassportClientUtils.getPassportClient().getLogoutURL(ticket, StringUtils.isNotBlank(logoutBackUrl)?logoutBackUrl:StringUtils.isBlank(logOutUrl) ? indexURL : logOutUrl);
//				return  url;
//			}
//			catch (Exception e) { }
//		}
//		return null;
//	}

	@RequestMapping("/smartLogout/page")
	@ControllerInfo("登出系统")
	public String doSmartLogout(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
								ModelMap map){
		String ticket = (String)httpSession.getAttribute("ticket");
		RedisUtils.del(DeskTopConstant.UNIFY_DESKTOP_KEY + getLoginInfo().getUserId());
		httpSession.invalidate();
		if (StringUtils.isNotBlank(ticket) && Evn.getBoolean("connection_passport")) {
			try {
				String indexURL = getIndexURL();
				PassportClientUtils.getPassportClient().invalidate(ticket);
				return "redirect:" + PassportClientUtils.getPassportClient().getLogoutURL(ticket, UrlUtils.ignoreLastRightSlash(indexURL)+"/smart.html");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/smart.html";
	}
	@ResponseBody
	@RequestMapping("/register/getMsgCode")
	@ControllerInfo("获得短信验证码")
	public String registerMsgCode(String mobilePhone, String type, ModelMap map) {
		try {
			String msgCountKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_count_"+mobilePhone;//发送次数
			String countStr = RedisUtils.get(msgCountKey);
			int count = 0;
			if(net.zdsoft.framework.utils.StringUtils.isNotBlank(countStr)){
				count = Integer.parseInt(countStr);
			}
			if(count>=5){//暂定每天最多发送5次
				return error("发送次数过多，请明天再来！");
			}

			String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+mobilePhone;
			Long expireTime = RedisUtils.getJedis().ttl(RedisUtils.KEY_PREFIX+msgCodeKey);//单位是s
			if(expireTime>9*60){
				return error("连续2次获取的时间太近，过1分钟再试试吧！");
			}

			String msgCode = CodeUtils.getNumberCode(6);
			String msgContent = "验证码："+msgCode;
			if("1".equals(type)){//注册
				msgContent += "，10分钟内输入有效且仅用于忘记密码。如非本人操作，请忽略此短信。";
			}else{//找回密码
				msgContent += "，10分钟内输入有效且仅用于忘记密码。如非本人操作，请忽略此短信。";
			}
			String result =smsRemoteService.sendSms(new String[]{mobilePhone}, msgContent,null);
			if(result!=null&&result.length()>3){
				result = result.substring(1,3);
			}
			if(!result.equals(ZDConstant.JSON_RESULT_CODE_00)){
				return error(ZDConstant.resultDescriptionMap.get(result));
			}
			RedisUtils.set(msgCodeKey, msgCode, 60*10);
			RedisUtils.set(msgCountKey,++count+"",60*60*24);
			return success("短信验证码获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error("短信验证码获取失败");
		}
	}

	@RequestMapping("/register/resetPwdPage")
	@ControllerInfo("进入找重置密码")
	public String showGetbackPasswordPage(HttpServletRequest request, ModelMap map) {
		map.put("indexUrl", this.getIndexURL());
		return "/desktop/login/getbackPassword.ftl";
	}

	@ResponseBody
	@RequestMapping("/register/resetPwd")
	@ControllerInfo("保存重置密码")
	public String getbackPassword(String mobilePhone,String msgCode, String password, String type, ModelMap map) {
		map.put("mobilePhone", mobilePhone);
		String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+mobilePhone;
		String  redisMsgCode= RedisUtils.get(msgCodeKey);
		if(!(net.zdsoft.framework.utils.StringUtils.isNotBlank(redisMsgCode) && redisMsgCode.equals(msgCode))){
			if("1".equals(type)){//1是手机端注册
				return error("请重新获取验证码");
			}else{
				return error("短信验证码错误,请确认后重新提交或者重新获取短信验证码");
			}
		}
		try {
			List<User> userList = SUtils.dt(userRemoteService.findByMobilePhones(mobilePhone), User.class);
			if (CollectionUtils.isEmpty(userList)) {
				return error("该手机号还未注册");
			}
			for (User user : userList) {
				userRemoteService.updatePassportPasswordByUsername(user.getUsername(), PWD.encodeIfNot(password));
			}
			RedisUtils.del(msgCodeKey);
			return success("设置密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error("设置密码失败");
		}
	}

	@RequestMapping("/register/resetPwdSuccess")
	@ControllerInfo("重置密码成功页")
	public String showGetbackSuccessPage(ModelMap map) {
		map.put("indexUrl", this.getIndexURL());
		return "/desktop/login/getbackSuccess.ftl";
	}

	@RequestMapping("/register/register")
	@ControllerInfo("进入注册页")
	public String showRegisterPage(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
								   ModelMap map) {


		return "/desktop/login/register.ftl";
	}
	@RequestMapping("/register/registerInfo")
	@ControllerInfo("进入注册详情页")
	public String showRegisterInfoPage(String ownerType,HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, ModelMap map) {

		map.put("ownerType", ownerType);
		List<Unit> unitList;
		Unit top = Unit.dc(unitRemoteService.findTopUnit(null));
		if (top.getUnitClass() == Unit.UNIT_CLASS_EDU) {
			String regionCode = top.getRegionCode();
			String unionCode = top.getUnionCode();
			Region proRegion = Region.dc(regionRemoteService.findByFullCode(unionCode.substring(0, 2)+"0000"));
			map.put("proRegion", proRegion);// 省
			int unitLevel = -1;
			if(unionCode.length() == 2){
				unitLevel = Unit.UNIT_REGION_PROVINCE;// 省级
			} else if(unionCode.length() == 4){
				unitLevel = Unit.UNIT_REGION_CITY;// 市级
			} else if(unionCode.length() == 6){
				unitLevel = Unit.UNIT_REGION_COUNTY;// 区县
			} else {
				unitLevel = Unit.UNIT_REGION_LEVEL;// 乡镇
			}
			if(unitLevel >= Unit.UNIT_REGION_CITY){
				Region cityRegion = Region.dc(regionRemoteService.findByFullCode(unionCode.substring(0, 4)+"00"));
				map.put("cityRegion", cityRegion);// 市
			}
			if(unitLevel >= Unit.UNIT_REGION_COUNTY){
				Region countyRegion = Region.dc(regionRemoteService.findByFullCode(regionCode));
				map.put("countyRegion", countyRegion);// 区县
			}
			map.put("unitLevel", unitLevel);
			if (unitLevel < Unit.UNIT_REGION_COUNTY) {
				String fc = regionCode;
				if(unitLevel == Unit.UNIT_REGION_PROVINCE){
					fc = regionCode.substring(0, 2) + "__" + "00";
				} else {
					fc = regionCode.substring(0, 4) + "__";
				}
				List<Region> regions = SUtils.dt(regionRemoteService.findByFullCodeLike("1", fc, regionCode), Region.class);
				map.put("regionList", regions);
			}
			unitList = Unit.dt(unitRemoteService.findByRegionCode(regionCode));
		} else {
			unitList = SUtils.dt(unitRemoteService.findAll(),Unit.class);
		}
		map.put("unitList", unitList);
		return "/desktop/login/registerInfo.ftl";
	}

	@ResponseBody
	@RequestMapping("/register/regionUnitList")
	@ControllerInfo("根据行政区划获取下级区划和单位信息")
	public String showUnits(String regionCode, HttpServletRequest request, ModelMap map){
		String forUnit = request.getParameter("forUnit");// 只为获取单位信息
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if(!"1".equals(forUnit) && regionCode.endsWith("00")){
			List<Region> regions = SUtils.dt(regionRemoteService.findByFullCodeLike("1",
					regionCode.substring(0, 4) + "__", regionCode), Region.class);
			dataMap.put("regionList", regions);
		}
		List<Unit> unitList = Unit.dt(unitRemoteService.findByRegionCode(regionCode));
		dataMap.put("unitList", unitList);
		return Json.toJSONString(dataMap);
	}

	@ResponseBody
	@RequestMapping("/register/registerCheckUser")
	@ControllerInfo("检验用户名是否重复")
	public String showRegisterCheckUser(String username,HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, ModelMap map) {

		User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
		if (user == null) {
			return success("用户名可用");
		}else{
			return error("用户名已被使用,请修改");
		}
	}

	@ResponseBody
	@RequestMapping("/register/registerMsgCode")
	@ControllerInfo("获得短信验证码")
	public String showRegisterMsgCode(String mobilePhone,String imgCode,HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, ModelMap map) {
		String imgCodeKey = DeskTopConstant.REGISTER_CODE_CACHE_KEY+"_"+getSession().getId();
		String redisImgCode = RedisUtils.get(imgCodeKey);
		if(StringUtils.isNotEmpty(redisImgCode)){
			RedisUtils.del(imgCodeKey);
			if(redisImgCode.equals(imgCode)){
				try {
					String msgCode = CodeUtils.getNumberCode(6);
					String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+mobilePhone;
					RedisUtils.set(msgCodeKey, msgCode, 60*5);
					String msgContent = "你的验证码是"+msgCode+",在5分钟内有效,请在有效时间内尽快注册,如果非本人操作请忽略本短信";
					//TODO
					System.out.println("短信验证码"+msgCode);
					System.out.println(msgContent);
					String result =smsRemoteService.sendSms(new String[]{mobilePhone}, msgContent,null);
					if(result!=null&&result.length()>3){
						result = result.substring(1,3);
					}
					if(!result.equals(ZDConstant.JSON_RESULT_CODE_00)){
						return error(ZDConstant.resultDescriptionMap.get(result));
					}
					return success("短信验证码获取成功");
				} catch (Exception e) {
					e.printStackTrace();
					return error("短信验证码获取失败");
				}
			}else{
				return error("图片验证码错误,请重新输入");
			}
		}else{
			return returnError("0","图片验证码已经失效,请刷新验证码重新获取");
		}

	}

	@ResponseBody
	@RequestMapping("/register/registerSaveUser")
	@ControllerInfo("保存用户")
	public String showRegisterSaveUser(User user,String msgCode,String imgCode,HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, ModelMap map) {
		if(user.getOwnerType().equals(User.OWNER_TYPE_STUDENT)){
			String imgCodeKey = DeskTopConstant.REGISTER_CODE_CACHE_KEY+"_"+getSession().getId();
			String  redisImgCode= RedisUtils.get(imgCodeKey);
			if(StringUtils.isNotEmpty(redisImgCode)){
				if(redisImgCode.equals(imgCode)){
					RedisUtils.del(imgCodeKey);
					try {

						User olduser = SUtils.dc(userRemoteService.findByUsername(user.getUsername()), User.class);
						if (olduser != null) {
							return error("用户名已被使用,请修改后重新注册");
						}

						user.setId(UuidUtils.generateUuid());
						user.setEnrollYear(0000);
						user.setUserRole(1);
						user.setIconIndex(0);
						user.setUserState(1);
						user.setPassword(PWD.encodeIfNot(user.getPassword()));
						Unit unit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
						user.setUnitId(unit.getId());
						List<User> users = new ArrayList<User>();


						Account account = new Account();
						account.setId(UuidUtils.generateUuid());
						account.setUsername(user.getUsername());
						account.setPassword(user.getPassword());
						account.setRealName(user.getRealName());
						account.setPhone(user.getMobilePhone());
						account.setFixedType(2);
						account.setType(11);

						PassportClientUtils.getPassportClient().addValidAccounts(
								new Account[] {account });

						user.setAccountId(account.getId());
						users.add(user);
						userRemoteService.saveAllEntitys(SUtils.s(users.toArray(new User[0])));

						return success("注册成功");
					} catch (Exception e) {
						e.printStackTrace();
						return error("注册失败");
					}
				}else{
					return error("图片验证码错误,请重新获取图片验证码");
				}
			}else{
				return error("图片验证码已经失效,请重新获取图片验证码");
			}
		}else{
			String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+user.getMobilePhone();
			String  redisMsgCode= RedisUtils.get(msgCodeKey);
			if(StringUtils.isNotEmpty(redisMsgCode)){
				if(redisMsgCode.equals(msgCode)){
					RedisUtils.del(msgCodeKey);
					try {

						User olduser = SUtils.dc(userRemoteService.findByUsername(user.getUsername()), User.class);
						if (olduser != null) {
							return error("用户名已被使用,请修改后重新注册");
						}

						user.setId(UuidUtils.generateUuid());
						user.setEnrollYear(0000);
						user.setUserRole(1);
						user.setIconIndex(0);
						user.setUserState(1);
						Unit unit = null;
						if(user.getOwnerType().equals(User.OWNER_TYPE_FAMILY)){
							unit = SUtils.dc(unitRemoteService.findTopUnit(), Unit.class);
							user.setUnitId(unit.getId());
						}else{
							unit = SUtils.dc(unitRemoteService.findOneById(user.getUnitId()), Unit.class);
						}
						user.setPassword(PWD.encodeIfNot(user.getPassword()));
						List<User> users = new ArrayList<User>();

						Account account = new Account();
						account.setId(UuidUtils.generateUuid());
						account.setUsername(user.getUsername());
						account.setPassword(user.getPassword());
						account.setRealName(user.getRealName());
						account.setPhone(user.getMobilePhone());
						account.setFixedType(2);
						account.setType(11);

						PassportClientUtils.getPassportClient().addValidAccounts(
								new Account[] {account });

						user.setAccountId(account.getId());
						users.add(user);
						userRemoteService.saveAllEntitys(SUtils.s(users.toArray(new User[0])));

						return success("注册成功");
					} catch (Exception e) {
						e.printStackTrace();
						return error("注册失败");
					}
				}else{
					return error("短信验证码错误,请确认后重新提交或者重新获取短信验证码");
				}
			}else{
				return error("短信验证码已经失效,请重新获取短信验证码");
			}
		}
	}

	@RequestMapping("/register/registerSuccess")
	@ControllerInfo("进入注册详情页")
	public String showRegisterSuccessPage(String username,String mobilePhone,String ownerType,HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, ModelMap map) {

		map.put("username", username);
		map.put("mobilePhone", mobilePhone);
		map.put("ownerType", ownerType);
		return "/desktop/login/registerSuccess.ftl";
	}

	private String getFilePath() {
		try {
			return sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.FILE_PATH);
		} catch (Exception e){
			return StringUtils.EMPTY;
		}
	}


	@ResponseBody
	@RequestMapping("/login/nowPwd")
	@ControllerInfo("免密码登录")
	public String loginNoPwd(String username, String password) {
		try {
			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if (Evn.isDevModel()) {
				if (user == null) {
					throw new ControllerException("username", "找不到此用户！");
				}
				initLoginInfo(getRequest().getSession(), user);
			}
			else {
				String pwd = user.getPassword();
				if (StringUtils.length(pwd) == 64) {
					pwd = PWD.decode(pwd);
				}
				if (StringUtils.equalsIgnoreCase(pwd, password)) {
					initLoginInfo(getRequest().getSession(), user);
				}
				else {
					return error("密码错误");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return success("登录成功");
	}

	@Autowired
	private DingLoginActionSupport dingLoginActionSupport;

	/**
	 * 普通用户在钉钉App上面点击app进入数字校园
	 * 需在钉钉上新增一个自建应用，url为 protocol + domain + port + /fpf/login/ding-talk </br>
	 * 需在dingAp-common.properties 增加 "部署地区.ding.coprId" "部署地区.ding.coprSecret" "部署地区.ding.agentId" </br>
	 * @return mv {@link ModelAndView}
	 */
	@RequestMapping("/login/ding-talk")
	public Object dingLogin() {
		String deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
		String coprId = environment.getProperty(deployRegion + ".ding.coprId");
		String coprSecret = environment.getProperty(deployRegion + ".ding.coprSecret");
		String backUrl = UrlUtils.encode(UrlUtils.getPrefix(getRequest()) + "/fpf/author/ding-talk",
				"UTF-8");
		//1、获取accessToken
		String accessToken = dingLoginActionSupport.getAccessToken(deployRegion, coprId, coprSecret);
		//2、获取jsTicket
		String jsTicket = dingLoginActionSupport.getJSTicket(deployRegion, accessToken);
		String nonceStr = UuidUtils.generateUuid();
		long timeStamp = System.currentTimeMillis();
		String url = UrlUtils.getPrefix(getRequest()) + "/fpf/login/ding-talk";
		String sinature = dingLoginActionSupport.sign(jsTicket, nonceStr, timeStamp, url);
		ModelAndView mv = new ModelAndView("/desktop/login/dingRedirect.ftl");
		mv.addObject("agentId", environment.getProperty(deployRegion + ".ding.agentId"))
				.addObject("coprId", coprId).addObject("coprSecret", coprSecret)
				.addObject("signature", sinature).addObject("timeStamp", timeStamp)
				.addObject("redirectUrl", UrlUtils.getPrefix(getRequest()) + "/fpf/author/ding-talk")
				.addObject("nonceStr", nonceStr).addObject("url",url);
		return mv;
	}

	@RequestMapping("/author/ding-talk")
	@ResponseBody
	public Object authorFromDingDing(@RequestParam(value = "code", required = true) String code,
									 @RequestParam(value = "state", required = false) String state,
									 RedirectAttributes redirectAttributes) {
		String deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);

		String exception = null;
		String dingId = null;
		try {
			String coprId = environment.getProperty(deployRegion + ".ding.coprId");
			String coprSecret = environment.getProperty(deployRegion + ".ding.coprSecret");
			String accessToken = dingLoginActionSupport.getAccessToken(deployRegion, coprId, coprSecret);
			//4、获取UserInfo
			dingId = dingLoginActionSupport.getDingUserId(accessToken, code);
			//dingUserInfo = getDingUserInfo(accessToken, code);
		} catch (DingLoginActionSupport.GetDingTokenOrInfoException | RestClientException e) {
			log.error("获取钉钉相关参数失败", e);
			exception = e.getMessage();
		}

		DingLoginActionSupport.DingJSONResponse dingJSONResponse = new DingLoginActionSupport.DingJSONResponse();
		dingJSONResponse.setServerPrefix(UrlUtils.getPrefix(getRequest()));
		//检查异常信息,
		if ( exception != null ) {
			dingJSONResponse.setRedirectUrl(UrlUtils.getPrefix(getRequest()) + "/fpf/loginForPassport.action");
			dingJSONResponse.setSuccess(false);
			dingJSONResponse.setErrmsg(exception);
			return dingJSONResponse;
		}

		try {
			User user = SUtils.dc(userRemoteService.getUserByDingDingId(dingId),
					User.class);
			//5、oa内部检查用户信息
			if ( user == null ) {
				//数字校园没有同步钉钉用户
				dingJSONResponse.setSuccess(false);
				dingJSONResponse.setRedirectUrl(UrlUtils.getPrefix(getRequest()) + "/fpf/loginForPassport.action");
				dingJSONResponse.setErrmsg("用户信息未同步");
				return dingJSONResponse;
			}
			//6、通过passport登录
			if (Evn.isPassport()) {
				dingJSONResponse.setSuccess(true);
				dingJSONResponse.setRedirectUrl(user.getId());
				return dingJSONResponse;
			}
			//7、不连接passport
			else {
				HttpSession httpSession = getRequest().getSession(true);
				initLoginInfo(httpSession, user);
				dingJSONResponse.setSuccess(true);
				dingJSONResponse.setRedirectUrl(UrlUtils.getPrefix(getRequest()) +
						"/fpf/loginUser/page?username=" + user.getUsername() +
						"&password=" + PWD.encodeIfNot(user.getPassword()));
				return dingJSONResponse;
			}
		} catch (Exception e) {
			dingJSONResponse.setSuccess(false);
			dingJSONResponse.setErrmsg("登录失败,异常信息:" + ExceptionUtils.getStackTrace(e));
			dingJSONResponse.setRedirectUrl(UrlUtils.getPrefix(getRequest()) + "/fpf/loginForPassport.action");
			return dingJSONResponse;
		}
	}

	@RequestMapping("/execute/script")
	public void dingPassportScriptExecutor(String userId) {
		try {
			User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
			PrintWriter printWriter = getResponse().getWriter();
			printWriter.write("<script src='" + getRedirectUrl(user) +"'></script>");
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			throw new DingLoginActionSupport.GetDingTokenOrInfoException("跳转passport脚本失败", e);
		}
	}
}
