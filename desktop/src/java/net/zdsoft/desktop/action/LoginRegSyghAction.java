package net.zdsoft.desktop.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.SysProductParam;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.careerplan.remote.service.PaymentDetailsRemoteService;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.login.LoginConstant;
import net.zdsoft.desktop.login.action.EisLoginController;
import net.zdsoft.desktop.utils.CodeUtils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.ShengySign;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.SmsRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.service.sms.entity.ZDConstant;

@Controller
@RequestMapping("/homepage/register")
public class LoginRegSyghAction extends EisLoginController {

	protected static final Logger logger = LoggerFactory.getLogger(LoginRegSyghAction.class);
	@Autowired
	private SmsRemoteService smsRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
    @Resource
    private RegionRemoteService regionRemoteService;
    @Resource
    private PaymentDetailsRemoteService paymentDetailsRemoteService;
	
    @RequestMapping("/syghIndex.action")
	public String syghIndex(HttpServletRequest request, ModelMap map){
		return "redirect:"+this.backIndex(this.getLoginInfo(),isMobile(request)?"1":"0");
	}
    
	@RequestMapping("/syghReg/page")
	@ControllerInfo("进入注册页")
	public String showRegisterIndexPage(String step, String realName, String mobilePhone, String msgCode, HttpServletRequest request, ModelMap map) {
		boolean isSd = isSaidian();
		if(isMobile(request)){
			if(StringUtils.isBlank(step)){//第一步，填写姓名和手机号
				if(isSd) {
					return "/desktop/login/sygh/saidian/ydregister.ftl";
				}
				return "/desktop/login/sygh/ydregister.ftl";
			}else{
				if(StringUtils.isNotBlank(mobilePhone)){
					map.put("mobilePhone", mobilePhone);
				}else{
					return errorFtl(map, "请重新填写手机号码");
				}
				if(StringUtils.isNotBlank(realName)){
					map.put("realName", realName);
				}
				if("2".equals(step)){//第二步，获取验证码
					String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+mobilePhone;
					Long expireTime = RedisUtils.getJedis().ttl(RedisUtils.KEY_PREFIX+msgCodeKey);//单位是s
					if(expireTime>4*60){
						map.put("dTime", expireTime.intValue()-4*60);//倒计时
					}
					if(isSd) {
						return "/desktop/login/sygh/saidian/ydRegGetYzm.ftl";
					}
					return "/desktop/login/sygh/ydRegGetYzm.ftl";
				}else{//第三步，设置密码
					map.put("step", step);//3:注册设置密码4:找回密码
					map.put("msgCode", msgCode);//防止直接进入第三步
					if(isSd) {
						return "/desktop/login/sygh/saidian/ydRegSetPass.ftl";
					}
					return "/desktop/login/sygh/ydRegSetPass.ftl";
				}
			}
		}
		if(isSd) {
			return "/desktop/login/sygh/saidian/register.ftl";
		}
		return "/desktop/login/sygh/register.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/syghCheckMobilePhone")
	@ControllerInfo("检验手机号是否被注册")
	public String registerCheckMobile(String mobilePhone,ModelMap map) {
		List<User> userList = SUtils.dt(userRemoteService.findByMobilePhones(mobilePhone), User.class);
		if (CollectionUtils.isEmpty(userList)) {
			return success("手机号可用");
		}else{
			return error("手机号已被注册");
		}
	}
	
	@ResponseBody
	@RequestMapping("/syghGetMsgCode")
	@ControllerInfo("获得短信验证码")
	public String registerMsgCode(String mobilePhone, String type, ModelMap map) {
		try {
			String msgCountKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_count_"+mobilePhone;//发送次数
			String countStr = RedisUtils.get(msgCountKey);
			int count = 0;
			if(StringUtils.isNotBlank(countStr)){
				count = Integer.parseInt(countStr);
			}
			if(count>=5){//暂定每天最多发送5次
				return error("发送次数过多，请明天再来！");
			}
			
			String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+mobilePhone;
			Long expireTime = RedisUtils.getJedis().ttl(RedisUtils.KEY_PREFIX+msgCodeKey);//单位是s
			if(expireTime>4*60){
				return error("连续2次获取的时间太近，过1分钟再试试吧！");
			}
			
			String msgCode = CodeUtils.getNumberCode(6);
			String msgContent = "验证码"+msgCode;
			if("1".equals(type)){//注册
				msgContent += "，您正在进行注册，若非本人操作，请勿泄露。";
			}else{//找回密码
				msgContent += "，您正在找回密码，若非本人操作，请勿泄露。";
			}
			String result =smsRemoteService.sendSms(new String[]{mobilePhone}, msgContent,"71F91C3DE19A3D04E0531E1C440ADA8B",null);
			if(result!=null&&result.length()>3){
				result = result.substring(1,3);
			}
			if(!result.equals(ZDConstant.JSON_RESULT_CODE_00)){
				return error(ZDConstant.resultDescriptionMap.get(result));
			}
			RedisUtils.set(msgCodeKey, msgCode, 60*5);
			RedisUtils.set(msgCountKey,++count+"",60*60*24);
			return success("短信验证码获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			return error("短信验证码获取失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/syghCheckMsgCode")
	@ControllerInfo("校验短信验证码")
	public String registerCheckMsgCode(String mobilePhone, String msgCode, ModelMap map) {
		String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+mobilePhone;
		String  redisMsgCode= RedisUtils.get(msgCodeKey);
		if(StringUtils.isNotEmpty(redisMsgCode) && redisMsgCode.equals(msgCode)){
			return success("短信验证码正确");
		}else{
			return error("短信验证码错误,请确认后重新验证或者重新获取短信验证码");
		}
	}
	
	@ResponseBody
	@RequestMapping("/syghSaveUser")
	@ControllerInfo("保存用户")
	public String registerSaveUser(User user,String msgCode,String type, ModelMap map) {
		String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+user.getMobilePhone();
		String  redisMsgCode= RedisUtils.get(msgCodeKey);
		if(!(StringUtils.isNotBlank(redisMsgCode) && redisMsgCode.equals(msgCode))){
			if("1".equals(type)){//1是手机端注册
				return error("请重新获取验证码");
			}else{
				return error("短信验证码错误,请确认后重新提交或者重新获取短信验证码");
			}
		}
		try {
			if(StringUtils.isNotBlank(user.getRealName())&&StringUtils.getRealLength(user.getRealName())>64){
				return error("姓名不能超过64个字节（一个汉字为两个字节）");
			}
			List<User> userList = SUtils.dt(userRemoteService.findByMobilePhones(user.getMobilePhone()), User.class);
			if (CollectionUtils.isNotEmpty(userList)) {
				return error("手机号已被注册,可直接登录");
			}
			
			User oldUser = SUtils.dc(userRemoteService.findByUsername(user.getMobilePhone()), User.class);
			if(oldUser==null){
				user.setUsername(user.getMobilePhone());
			}else{
				user.setUsername(user.getMobilePhone()+(int)(Math.random()*10000));
			}
			
			String classId = systemIniRemoteService.findValue("DJ.SYGH.UNIT.CLASS");
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);

			user.setId(UuidUtils.generateUuid());
			user.setUserRole(1);
			user.setIconIndex(0);
			user.setUserState(1);
			user.setOwnerType(User.OWNER_TYPE_STUDENT);
			user.setUserType(User.USER_TYPE_COMMON_USER);
			Date date =new Date();
			user.setCreationTime(date);
			user.setModifyTime(date);
			user.setEventSource(0);
			if(StringUtils.isBlank(user.getRealName())){
				user.setRealName(user.getMobilePhone());
			}
			user.setPassword(PWD.encodeIfNot(user.getPassword()));
			if(clazz!=null){
				user.setUnitId(clazz.getSchoolId());
				String year = clazz.getAcadyear().substring(0, 4);
				user.setEnrollYear(Integer.parseInt(year));
			}

			Student student = new Student();
			student.setId(UuidUtils.generateUuid());
			student.setSchoolId(user.getUnitId());
			student.setEventSource(0);
			student.setIsLeaveSchool(Student.STUDENT_NORMAL);// 是否离校，默认在校
			student.setClassId(classId);
			student.setStudentName(user.getRealName());;
			student.setSex(1);
			student.setStudentCode(user.getMobilePhone());
			student.setNowState(Student.NOWSTATE_40);
			student.setIsFreshman(Student.FRESHMAN_9);
	    	student.setNation("01"); // 民族
	    	student.setHealth("10"); // 健康状况
	    	student.setStudyMode(2);// 就读方式
	    	student.setIsBoarding("1");
	    	student.setCountry("156");// 国籍
	    	student.setBackground("88");// 政治面貌
	        student.setIdentitycardType("1");// 证件类型-身份证
	        student.setSchLengthType("1");
			student.setCreationTime(date);
			student.setModifyTime(date);
			student.setIsDeleted(0);
			if(clazz!=null){
				student.setClassName(clazz.getClassName());
				Unit unit = SUtils.dc(unitRemoteService.findOneById(clazz.getSchoolId()), Unit.class);
				student.setEnrollYear(clazz.getAcadyear());// 班级的入学学年
				student.setRegionCode(unit.getRegionCode());
				List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId),Student.class);
				int classInnerCode = studentList.stream().filter(e->StringUtils.isNotBlank(e.getClassInnerCode())&&e.getClassInnerCode().matches("^\\d+$"))
						.mapToInt(e->Integer.parseInt(e.getClassInnerCode())).max().orElse(0);
				student.setClassInnerCode(String.valueOf(classInnerCode+1));//班内编号
				String year = clazz.getAcadyear().substring(0, 4);
				Calendar now = Calendar.getInstance();
				now.set(Calendar.YEAR, Integer.parseInt(year));
				now.set(Calendar.MONTH, Calendar.SEPTEMBER);
				now.set(Calendar.DAY_OF_MONTH, 1);
				student.setToSchoolDate(now.getTime());// 入学时间
			}
			
			Account account = new Account();
			account.setId(UuidUtils.generateUuid());
			account.setUsername(user.getUsername());
			account.setPassword(user.getPassword());
			account.setRealName(user.getRealName());
			account.setPhone(user.getMobilePhone());
			account.setFixedType(2);
			account.setType(11);

			user.setAccountId(account.getId());
			user.setOwnerId(student.getId());
			
			userRemoteService.saveUser(user, student, account);
			RedisUtils.del(msgCodeKey);
			return success(user.getUsername()+"_"+user.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			return error("注册失败");
		}
	}

	@RequestMapping("/syghRegSuccess/page")
	@ControllerInfo("进入注册成功页")
	public String showRegisterSuccessPage(String mobilePhone, String username, ModelMap map) {
		map.put("mobilePhone", mobilePhone);
		map.put("username", username);
		if(isSaidian()) {
			return "/desktop/login/sygh/saidian/registerSuccess.ftl";
		}
		return "/desktop/login/sygh/registerSuccess.ftl";
	}
	
	
	@RequestMapping("/syghPwd/page")
	@ControllerInfo("进入找回密码页")
	public String showGetbackPasswordPage(String mobilePhone, HttpServletRequest request, ModelMap map) {
		map.put("mobilePhone", mobilePhone);
		if(isMobile(request)){
			if(isSaidian()) {
				return "/desktop/login/sygh/saidian/ydgetbackPass.ftl";
			}
			return "/desktop/login/sygh/ydgetbackPass.ftl";
		}
		if(isSaidian()) {
			return "/desktop/login/sygh/saidian/getbackPassword.ftl";
		}
		return "/desktop/login/sygh/getbackPassword.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/syghResetPwd")
	@ControllerInfo("保存找回密码")
	public String getbackPassword(String mobilePhone,String msgCode, String password, String type, ModelMap map) {
		map.put("mobilePhone", mobilePhone);
		String msgCodeKey = DeskTopConstant.REGISTER_CODE_MSG_CACHE_KEY+"_"+mobilePhone;
		String  redisMsgCode= RedisUtils.get(msgCodeKey);
		if(!(StringUtils.isNotBlank(redisMsgCode) && redisMsgCode.equals(msgCode))){
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
	
	@RequestMapping("/syghPwdSuccess/page")
	@ControllerInfo("找回密码成功页")
	public String showGetbackSuccessPage(String mobilePhone, ModelMap map) {
		map.put("mobilePhone", mobilePhone);
		if(isSaidian()) {
			return "/desktop/login/sygh/saidian/getbackSuccess.ftl";
		}
		return "/desktop/login/sygh/getbackSuccess.ftl";
	}
	
	@RequestMapping("/syghLogin/page")
	@ControllerInfo("进入登录页")
	public String showLoginIndexPage(HttpServletRequest request, ModelMap map) {
		if(isMobile(request)){
			if(isSaidian()) {
				return "/desktop/login/sygh/saidian/ydlogin.ftl";
			}
			return "/desktop/login/sygh/ydlogin.ftl";
		}else{
			String copyRight = sysProductParamRemoteService.findProductParamValue(SysProductParam.COMPANY_COPYRIGHT);
			if(StringUtils.isNotBlank(copyRight)){
				copyRight = copyRight.replace("currentYear",String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			}
			map.put("copyRight", copyRight);
			if(isSaidian()) {
				return "/desktop/login/sygh/saidian/login.ftl";
			}
			return "/desktop/login/sygh/login.ftl";
		}
	}
	
	@ResponseBody
	@RequestMapping("/syghLogin/login")
	@ControllerInfo("登录")
	public String syghLogin(String mobilePhone, String password, String imgCode, String autoLogin, ModelMap map) {
		if(!"1".equals(autoLogin)){
			String redisCode = RedisUtils.get(DeskTopConstant.VERIFY_CODE_CACHE_KEY + getSession().getId());
			if(!imgCode.equalsIgnoreCase(redisCode)){
				return error("验证码错误");
			}
		}
		List<User> userList = SUtils.dt(userRemoteService.findByMobilePhones(mobilePhone),User.class);
		if(CollectionUtils.isEmpty(userList)){
			return error("该手机号还未注册");
		}
		userList = userList.stream().filter(e->StringUtils.isNotBlank(e.getPassword())&&password.equals(PWD.decode(e.getPassword()))).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(userList)){
			return error("密码错误");
		}else if(userList.size()>1){
			userList.sort((x,y)->{
				if(x.getUsername().equals(x.getMobilePhone())&&!y.getUsername().equals(y.getMobilePhone())){
					return -1;
				}else if(!x.getUsername().equals(x.getMobilePhone())&&y.getUsername().equals(y.getMobilePhone())){
					return 1;
				}else{
					if(x.getCreationTime()==null || y.getCreationTime()==null){
						return 0;
					}
					return x.getCreationTime().compareTo(y.getCreationTime());
				}
			});
		}
		User user = userList.get(0);
		
		return success(user.getUsername()+"_"+user.getPassword());//防止输入用户名直接跳转
	}

	@RequestMapping("/syghLogin/loginForPassport")
	@ControllerInfo("登录成功跳转")
	public String login(String username, HttpServletRequest request, ModelMap map) {
		try {
			//防止输入用户名直接跳转
			String[] nameAndPwd = username.split("_");
			username = nameAndPwd[0];
			String password = nameAndPwd[1];
			//判断当前登录的账号是否和未退出的账号一致
			LoginInfo currentLogin = null;
			if (getSession() != null && (currentLogin = ServletUtils.getLoginInfo(getSession())) != null) {
				if (!StringUtils.equals(currentLogin.getUserName(), username)) {
					//1.退出eis
					String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
					quitSession(currentLogin,username);
					//2.退出passport
					if(Evn.isPassport()){
						String backURL = new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
								.append("/homepage/register/syghLogin/page")
								.toString();
						return quitPassport(backURL,ticket);
					}
				}
			}
			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if(user != null) {
				if(currentLogin==null){
					initLoginInfo(getRequest().getSession(), user);
					currentLogin =this.getLoginInfo();
				}
				
				if (Evn.isPassport()) {
					//重定向daopassport
					String url=new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
							.append("/homepage/register/syghIndex.action")
							.toString();;
    				String passportLoginUrl = getRedirectUrl(user, url,null,Boolean.TRUE,null);
					map.addAttribute("passportLoginUrl", passportLoginUrl);
					if (StringUtils.isBlank(passportLoginUrl)) {
						return "redirect:" + passportLoginUrl;
					} else {
						return "/desktop/login/wenxuanLogin.ftl";
					}
				}
				return "redirect:"+this.backIndex(currentLogin, isMobile(request)?"1":"0");
			}else {
				return promptFlt(map,"该用户尚未同步");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map,"登录失败，数据解密异常");
		}
	}
	
    /**
     * 清除session
     * @param currentLogin
     * @param username
     */
	private void quitSession(LoginInfo currentLogin, String username) {
		HttpSession httpSession = getHttpSession();
		String pSessionId = (String) httpSession.getAttribute(LoginConstant.SESSION_ATTRIBUTE_NAME);
		//删除 passport session id 可以保证完全退出
		redisTemplate.delete(Constant.PASSPORT_TICKET_KEY + pSessionId);
		getSession().invalidate();
		getHttpSession().invalidate();
	}
	
	  /**
     * 退出之前的账号，重新登录
     * @param backURL
     * @return
     */
    private String quitPassport(String backURL,String ticket) {
		PassportClient client = PassportClientUtils.getPassportClient();
		return "redirect:"+client.getLogoutURL(ticket, backURL);
	}
	
	/**
	 * 
	 * @param user 
	 * @param url
	 * @param server
	 * @param isSimpleLogin  是否只作为 用户名 进行登陆
	 * @param cookieSaveType 是否记住账号和密码，实现自动登陆
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
					.append("/scriptLogin?action=login&d1=")
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
		
	private String backIndex(LoginInfo loginInfo, String isH5) {
		//直接组装数据
    	//根据user获取regin
    	String regionCode=loginInfo.getRegion();
    	Region region = SUtils.dc(regionRemoteService.findByFullCode(regionCode), Region.class);
    	if(region==null) {
    		//行政区域码不对
    		return "/desktop/error.ftl";
    	}
    	String reginName=region.getFullName();
    	Map<String, String> provinceMap = ShengySign.provinceMap;
    	String provinceNum="";
    	for(Entry<String, String> kk:provinceMap.entrySet()) {
    		if(reginName.contains(kk.getKey())) {
    			provinceNum=kk.getValue();
    			break;
    		}
    	}
    	if(StringUtils.isBlank(provinceNum)) {
    		//省匹配不上
    		return "/desktop/error.ftl";
    	}
    	//是不是已经购买vip 
    	String operTypeValue="&operType=6&operValue=0";
    	//PAY_TYPE_01 = "01";
    	boolean isVip=paymentDetailsRemoteService.checkByOrderTypeAndUserIdWithMaster("01", loginInfo.getUserId());;
    	if(isVip) {
    		//VIP用户
    		operTypeValue="&operType="+Evn.getCreeplanDjStatic()+"&operValue=1";
    	}
    	
    	//暂时不通过base_server获取域名 直接默认http://sy.yunschool.com
    	//openNickName 真实姓名
    	String parmUrl="&openUsername="+loginInfo.getUserName()+"&openUserProvince="+provinceNum+
    			"&openUserCity=&openUserArea=&openUserSchool=&openNickName="+UrlUtils.encode(loginInfo.getRealName(),"utf-8")+operTypeValue+"&operData=1&isH5="+isH5+"&remark=";
    	String url="http://sy.yunschool.com/openAuth/login?secretKey=2a532915f2d60fc9b875ea7e335f2fea" + 
    			"&sign="+ShengySign.getShengySign()+"&openUserId=" + loginInfo.getUserId()+parmUrl;
    	return url;
	}
	
	private boolean isMobile(HttpServletRequest request) {
        UserAgent ua = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = ua.getOperatingSystem();
        if(DeviceType.MOBILE.equals(os.getDeviceType())) {
            return true;
        }
        return false;
    }
	
	private boolean isSaidian() {
		return Evn.isSaidian();//是否赛点环境
	}
}
