package net.zdsoft.desktop.login.action;

import static net.zdsoft.desktop.login.LoginConstant.PASSPORT_PAGE_ACTION;
import static net.zdsoft.desktop.login.LoginConstant.SESSION_ATTRIBUTE_NAME;
import static net.zdsoft.desktop.login.LoginConstant.TICKET_KEY;
import static net.zdsoft.desktop.login.LoginConstant.USER_STATE_NORMAL;
import static net.zdsoft.desktop.login.LoginConstant.USE_PASSPORT_LOGIN_CODE;
import static net.zdsoft.framework.entity.Constant.PASSPORT_TICKET_KEY;
import static net.zdsoft.framework.utils.PWD.decode;
import static net.zdsoft.framework.utils.PWD.encodeIfNot;
import static net.zdsoft.framework.utils.SUtils.dc;
import static net.zdsoft.system.constant.Constant.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.custom.XyConstant;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.SysProductParam;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.SysProductParamRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.constant.LaSajyConstant;
import net.zdsoft.desktop.login.vo.LoginOptionDTO;
import net.zdsoft.desktop.login.vo.LoginUserDTO;
import net.zdsoft.desktop.utils.SystemUtil;
import net.zdsoft.desktop.utils.WebFileUtils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.system.entity.LoginDomain;
import net.zdsoft.system.remote.service.LoginDomainRemoteService;
import net.zdsoft.system.remote.service.ServerRegionRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

/**
 * 登录action
 * 
 * @author ke_shen@126.com
 * @since 2018/1/24 下午2:42
 */
public class EisLoginController extends EisLoginPassportSupportController
		implements EnvironmentAware {

	private Logger logger = LoggerFactory.getLogger(EisLoginController.class);

	@Autowired
	protected SystemIniRemoteService systemIniRemoteService;
	@Autowired
	protected SysProductParamRemoteService sysProductParamRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	protected UnitRemoteService unitRemoteService;
	@Autowired
	protected ServerRemoteService serverRemoteService;
	@Autowired
	protected ServerRegionRemoteService serverRegionRemoteService;
	@Autowired
	protected LoginDomainRemoteService loginDomainRemoteService;
	@Autowired
	protected StudentRemoteService studentRemoteService;
	protected Environment environment;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		try {
			IconUtils iconUtils = new IconUtils(
					sysOptionRemoteService.findValue(FILE_PATH),
					serverRemoteService);
			// 大多数的开发环境都是windows，就不复制了
			if (!SystemUtil.getOsInfo().isWindows()) {
				iconUtils.init();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("图标初始化工具创建成功");
			}
		} catch (Exception e) {
			// 即使图标初始化失败，也不能影响整个bean的创建
			logger.error("图标初始化失败", e);
		}
	}

	/** passport登录页面 */
	@RequestMapping(value = { "/login/loginForPassport.action" })
	public Object loginForPassport(
			@RequestParam(required = false) boolean preview,
			@RequestParam(required = false) String loginError,
			@PathVariable(value = "region", required = false) String regionMain,
			@RequestParam(value = "call", required = false) String callBack,
            @RequestParam(value = "root", required = false) String root) {
		// 根据当前的会话中来得到是否是独立部署的页面
		String domianName = null;

		String redisKey = getRequest().getServerName();
		doLoginDomain(regionMain);
		HttpSession session = getSession();
		if (session != null && session.getAttribute("domainName") != null) {
			LoginDomain loginDomain = SUtils.dc(loginDomainRemoteService
					.findByRegionAdmin(session.getAttribute("domainName")
							.toString()), LoginDomain.class);
			if (loginDomain != null) {
				domianName = loginDomain.getRegionAdmin();
			}
			redisKey = redisKey + domianName;
		}

		ModelAndView mv = createMv();
		mv.addObject("urlPrefix", UrlUtils.getPrefix(getRequest()));
		// 缓存
		LoginOptionDTO loginOptionDTO = RedisUtils.getObject("loginOption"
				+ redisKey, LoginOptionDTO.class);
		String deployRegion = StringUtils.trimToEmpty(serverRegionRemoteService
				.findRegionByDomain(getRequest().getServerName()));
		// 2、根据部署地区取得登录页相关参数信息
		if (StringUtils.isBlank(deployRegion)) {
			deployRegion = StringUtils.trimToEmpty(systemIniRemoteService
					.findValue(DeployRegion.SYS_OPTION_REGION));
		}
		mv.addObject("callBack", callBack);
		if (getLoginInfo() == null) {
			mv.addObject("deployRegion", deployRegion);
		}
		if (loginOptionDTO != null && !preview) {
			mv.addObject("loginOption", loginOptionDTO).setViewName(getLoginViewName());
			return mv;
		}

		// 在进入登陆页之前检查是否登录
		if (getSession() != null
				&& ServletUtils.getLoginInfo(getSession()) != null && !preview) {
			return "redirect:/desktop/index/page";
		}

		LoginOptionDTO.Builder optionVoBuilder = LoginOptionDTO.newBuilder();

		// 1、检查是否多域名部署
		String fileUrl = sysOptionRemoteService.findValue("FILE.URL");
		fileUrl = UrlUtils.ignoreLastRightSlash(fileUrl)
				+ UrlUtils.SEPARATOR_SIGN;
		String filePath = sysOptionRemoteService.findValue("FILE.PATH");

		// 2.1加载配置文件
		String configDir;
		if (StringUtils.isNotBlank(domianName)) {
			configDir = filePath + File.separator + LOGIN_PAGE_DIR
					+ File.separator + domianName + LOGIN_PAGE_CONF_NAME;
		} else {
			configDir = filePath + File.separator + LOGIN_PAGE_DIR
					+ File.separator + deployRegion + LOGIN_PAGE_CONF_NAME;
		}
		File file = new File(configDir);
		if (file.exists()) {
			try {
				Properties loginConfig = new Properties();
				FileInputStream inputStream = new FileInputStream(configDir);
				loginConfig.load(inputStream);
				inputStream.close();
				RedisUtils.set("favicon", loginConfig.getProperty("favicon"));
				optionVoBuilder
						.logoName(loginConfig.getProperty(LOGIN_PAGE_LOGO_NAME))
						.headerName(loginConfig.getProperty(LOGIN_PAGE_TITLE))
						.loginBgUrl(
								fileUrl
										+ loginConfig
												.getProperty(LOGIN_PAGE_BG_PATH))
						.logoUrl(
								fileUrl
										+ loginConfig
												.getProperty(LOGIN_PAGE_LOGO_BG_PATH))
						// .loginBgUrl(fileUrl +
						// "desktop/login/images/body-bg-shya.png")
						.footer(loginConfig
								.getProperty(SysProductParam.COMPANY_COPYRIGHT))
						.commonHeader(
								loginConfig.getProperty(COMMON_PAGE_TITLE))
						.warn(loginConfig.getProperty(LOGIN_PAGE_WARN))
                        .showForgetPassword(BooleanUtils.toBoolean(loginConfig.getProperty(FORGET_PASSWORD)));
				String player = loginConfig.getProperty(LOGIN_PAGE_PLAYER);
				if (StringUtils.isNotBlank(player)) {
					optionVoBuilder.player(BooleanUtils.toBoolean(player));
				}
				
				String enablePageLogoImage = loginConfig.getProperty(ENABLE_LOGIN_PAGE_LOGO_BG_PATH);
				if (StringUtils.isNotBlank(enablePageLogoImage)) {
					optionVoBuilder.enablePageLogoImage(BooleanUtils.toBoolean(enablePageLogoImage));
				}
				
				String enablePageLogoName = loginConfig.getProperty(ENABLE_LOGIN_PAGE_LOGO_NAME);
				if (StringUtils.isNotBlank(enablePageLogoName)) {
					optionVoBuilder.enablePageLogoName(BooleanUtils.toBoolean(enablePageLogoName));
				}
				
			} catch (IOException e) {
				logger.error("加载登录页配置文件失败,使用默认配置", e);
			}
		}

		HttpServletRequest request = getRequest();
		// 3加载通用参数（passport）
		if (DeployRegion.DEPLOY_DAQIONG.equals(deployRegion)) {
			optionVoBuilder.showQQ(true).showRegister(true);
		}

		String passportUrl = Evn.getString(Constant.PASSPORT_URL);
		if (sysOptionRemoteService.isSecondUrl(getRequest().getServerName())) {
			passportUrl = PassportClientUtils.getPassportClient().getPassportURL(request.getServerName());
		}

		optionVoBuilder
				.connectPassport(Evn.isPassport())
				.serverId(Evn.getString(Constant.PASSPORT_SERVER_ID))
				.passportUrl(passportUrl)
				.verifyKey(Evn.getString(Constant.PASSPORT_VERIFYKEY))
				.root(StringUtils.isBlank(request.getContextPath()) ? "1" : "0");
		if (StringUtils.isNotBlank(callBack) && StringUtils.isNotBlank(root)) {
		    optionVoBuilder.root(root);
        }
		optionVoBuilder.indexUrl(getNativeIndexUrl());
		User topAdmin = SUtils.dc(
				RedisUtils.get("topAdmin", new RedisInterface<String>() {
					@Override
					public String queryData() {
						return userRemoteService.findTopAdmin();
					}
				}), User.class);
		optionVoBuilder.initLicense(topAdmin != null);
		optionVoBuilder.verifyCode("Y".equals(systemIniRemoteService
				.findValue("VERIFYCODE.SWITCH")));

		LoginOptionDTO optionVo = optionVoBuilder.build();

		// 兼容之前版本的做法
		if (StringUtils.isBlank(optionVo.getLogoName())) {
			String logoName = DeployRegion.DEPLOY_MAP.get(deployRegion);
			optionVo.setLogoName(logoName);
		}
		if (StringUtils.isBlank(optionVo.getLogoUrl())
				|| StringUtils.equals(fileUrl, optionVo.getLogoUrl())) {
			String innerLogoBgName = "/desktop/login/images/logo-"
					+ deployRegion + ".png";
			String logoBgPath = request.getSession().getServletContext()
					.getRealPath(innerLogoBgName);
			if (!WebFileUtils.existsOfAbsolutePath(logoBgPath)) {
				optionVo.setLogoUrl(request.getContextPath()
						+ "/desktop/login/images/logo.png");
			} else {
				optionVo.setLogoUrl(request.getContextPath() + innerLogoBgName);
			}
		}
		if (StringUtils.isBlank(optionVo.getLoginBgUrl())
				|| StringUtils.equals(fileUrl, optionVo.getLoginBgUrl())) {
			String innerLoginBgName = "/desktop/login/images/body-"
					+ deployRegion + ".png";
			String innerAbsolutePath = request.getSession().getServletContext()
					.getRealPath(innerLoginBgName);
			if (!WebFileUtils.existsOfAbsolutePath(innerAbsolutePath)) {
				optionVo.setLoginBgUrl(request.getContextPath()
						+ "/desktop/login/images/body-bg.png");
				optionVo.setPlayer(true);
			} else {
				optionVo.setLoginBgUrl(request.getContextPath()
						+ innerLoginBgName);
				optionVo.setPlayer(false);
			}
		} else {
			if (!optionVo.isPlayer()) {
				optionVo.setPlayer(false);
			}
		}

		// 处理版权信息
		if (StringUtils.isBlank(optionVo.getFooter())) {
			String copyRight = sysProductParamRemoteService
					.findProductParamValue(SysProductParam.COMPANY_COPYRIGHT);
			optionVo.setFooter(copyRight);
		}

		String copyRight = optionVo.getFooter();
		if (StringUtils.isNotBlank(copyRight)) {
			copyRight = copyRight.replace("currentYear",
					String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			optionVo.setFooter(copyRight);
		}

		optionVo.setPreview(preview);

		mv.addObject("loginOption", optionVo);

		RedisUtils.setObject("loginOption" + redisKey, optionVo, 60 * 60);

		mv.setViewName(getLoginViewName());
		return mv;
	}

	private String getLoginViewName() {
		//检查是否是七台河
		if (DeployRegion.DEPLOY_QTH.equals(systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION))) {
			return "/desktop/login/qth/login.ftl";
		}
		return "/desktop/login/login.ftl";
	}

	@RequestMapping("/loginPage/page")
	public ModelAndView loginForEis(
			@PathVariable(value = "region", required = false) String regionMain,
			@RequestParam(value = "url", required = false) String url) {
		// 判断是否是多域名登陆
		// String regionMain;

		doLoginDomain(regionMain);

		// if(StringUtils.isNotBlank(regionMain)){
		// System.out.println(regionMain);
		// }
		ModelAndView mv = createMv();
		HttpSession httpSession = getHttpSession();
		LoginInfo loginInfo = (LoginInfo) httpSession.getAttribute("loginInfo");
		// Session session = Session.get(httpSession.getId());
		if (loginInfo != null) {
			mv.setViewName("redirect:/desktop/index/page");
			return mv;
		}
		User topAdmin = SUtils.dc(
				RedisUtils.get("topAdmin", new RedisInterface<String>() {
					@Override
					public String queryData() {
						return userRemoteService.findTopAdmin();
					}
				}), User.class);
		if (StringUtils.isNotBlank(regionMain)) {
			mv.setViewName("redirect:/" + regionMain
					+ "/fpf/login/loginForPassport.action");
		} else {
			mv.setViewName("redirect:/fpf/login/loginForPassport.action");
		}
		if (topAdmin == null) {
			return mv;
		}
		if (Evn.isPassport()) {
			String indexURL = UrlUtils.getPrefix(getRequest())
					+ UrlUtils.SEPARATOR_SIGN + "/desktop/index/page";
			String value = systemIniRemoteService
					.findValue(USE_PASSPORT_LOGIN_CODE);
			boolean useEisLoginForPassport = BooleanUtils.toBoolean(NumberUtils
					.toInt(value, 0)) || BooleanUtils.toBoolean(value);
			String defaultLoginUrl = useEisLoginForPassport ? UrlUtils
					.getPrefix(getRequest()) + "/" + PASSPORT_PAGE_ACTION
					: null;
			if (StringUtils.isNotBlank(url)) {
				indexURL = url;
				defaultLoginUrl = UrlUtils.addQueryString(defaultLoginUrl, "call", url);
			}
			
			//判断是否维护了 第三方统一的登录地址
			defaultLoginUrl = doInPutUrl(defaultLoginUrl);
			
			//星云部署的跳转到星云的登陆页面
			String	deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
			if(DeployRegion.DEPLOY_XINGYUN.equals(deployRegion)){
				defaultLoginUrl = XyConstant.XY_LOGIN_URL;
			}
			
			String s = PassportClientUtils.getPassportClient().getLoginURL(
					getRequest().getServerName(),
					indexURL, getRequest().getContextPath(), defaultLoginUrl);
			mv.setViewName("redirect:" + s);
		}

		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/loginUser/page", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo(ignoreLog = ControllerInfo.LOG_FORCE_WRITE, value = "登录系统")
	public Object loginForNativeEis(@Validated LoginUserDTO loginUserDTO,
			BindingResult errors) {
		if (errors.hasErrors()) {
			String msg = getSingleErrorMsg(errors);
			String truePassword = "";
			User user = dc(userRemoteService.findByUsername(loginUserDTO
					.getUsername()), User.class);
			if (user != null) {
				truePassword = PWD.decode(user.getPassword());
			}
//			if (Evn.isDevModel()) {
//				User user = dc(userRemoteService.findByUsername(loginUserDTO
//						.getUsername()), User.class);
//				if (user != null) {
//					truePassword = PWD.decode(user.getPassword());
//				}
//			}
			logger.error("登录异常, {}, username:{}, password:{}", msg,
					loginUserDTO.getUsername(), loginUserDTO.getPassword()
							+ (StringUtils.isNotBlank(truePassword) ? "->"
									+ truePassword : ""));
			return error(msg);
		}
		User user = dc(
				userRemoteService.findByUsername(loginUserDTO.getUsername()),
				User.class);
		Date loginDate = user.getLoginDate();
		if(loginDate != null) {
			String pwdValidityPeriodDayStr = systemIniRemoteService.findValue(net.zdsoft.system.constant.Constant.USER_LOGIN_VALIDITY_PERIOD);
	        pwdValidityPeriodDayStr = StringUtils.isNotBlank(pwdValidityPeriodDayStr)?pwdValidityPeriodDayStr:"60";
	        int pwdValidityPeriodDay = NumberUtils.toInt(pwdValidityPeriodDayStr);
	        boolean result = false;
	        result = new Date().before(DateUtils.addDay(loginDate, pwdValidityPeriodDay));
	        if(!result) {
	        	String msg = "账号因长时间未登录已被系统锁定，请修改密码登陆或联系管理员";
	        	logger.error("登录异常, {}, username:{}, password:{}", msg,
						loginUserDTO.getUsername(), loginUserDTO.getPassword());
	        	return error(msg);
	        }
		}
		user.setLoginDate(new Date());
		userRemoteService.update(user, user.getId(), new String[] {"loginDate"});
		initLoginInfo(getHttpSession(), user);
		return success("登录成功");
	}

	@RequestMapping(value = "logout/page")
	@ControllerInfo(ignoreLog = ControllerInfo.LOG_FORCE_WRITE, value = "退出系统")
	public ModelAndView logoutEis(
			@RequestParam(required = false, name = "userName") String username,
			@RequestParam(required = false) String psId,
			@RequestParam(required = false, name = "call") String callBackUrl) {
		// 检查是否是切换账号
		if (StringUtils.isNotBlank(username)) {
			return createMv((forward("/homepage/nextUser/login")));
		}
		return createMv(redirect(invalidateSession(null, psId, callBackUrl)));
	}

	private String invalidateSession(User user, String passportSessionId, String callBackUrl) {
		try {
			HttpSession httpSession = getHttpSession();
			String userId = null;
			if(ServletUtils.getLoginInfo(getHttpSession()) != null){
				userId = ServletUtils.getLoginInfo(getHttpSession()).getUserId();
			}
			String ticket = (String) httpSession.getAttribute(TICKET_KEY);
			String pSessionId = (String) httpSession
					.getAttribute(SESSION_ATTRIBUTE_NAME);
			String domainName = (String) httpSession.getAttribute("domainName");
			//如果部署地区是拉萨的，需要退出统一认证平台
			String	deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
			if (DeployRegion.DEPLOY_LASA.equals(deployRegion) && StringUtils.isNotBlank(userId)) {
				String tokenId = RedisUtils.get(LaSajyConstant.LS_SAVE_TOKENID_BY_USERID_KEY + userId);
				if(StringUtils.isNotBlank(tokenId)){
					RedisUtils.del(LaSajyConstant.LS_SAVE_TOKENID_KEY + tokenId);
					RedisUtils.del(LaSajyConstant.LS_SAVE_TOKENID_BY_USERID_KEY + userId);
					String url = LaSajyConstant.LS_LOGIN_OUT_URL + "?" + LaSajyConstant.LS_TOKEN_NAME + "=" + tokenId;
					try {
						String result = UrlUtils.get(url, new String());
						if(StringUtils.isNotBlank(result)){
							JSONObject jsonObject = JSON.parseObject(result);
							String code = jsonObject.getString(LaSajyConstant.LS_RESULT_CODE);
							if(LaSajyConstant.LS_FAILED_CODE.equals(code)){
								getLogger().error("退出失败");
								return "/homepage/loginPage/page";
							}
						}
					} catch (IOException e) {
						getLogger().error("退出平台失败", e);
					}
				}
			}
			
			//判断是否维护了 第三方统一的登录地址
			if(StringUtils.isBlank(callBackUrl)){
				callBackUrl = doInPutUrl(callBackUrl);
			}
            
			//单点退出地址
			if(StringUtils.isNotBlank(userId) ){
				if(DeployRegion.DEPLOY_XINGYUN.equals(deployRegion)){
					String url = XyConstant.XY_LOGIN_OUT_URL + "?pageURL=" + XyConstant.XY_LOGIN_URL +
							"&userId=" + userId + "&" + XyConstant.XY_APP_ID_NAME
							+ "=" + XyConstant.XY_APP_ID_VAL + "&appToken=" + XyConstant.XY_APP_TOKEN_VAL; 
					callBackUrl = url;
					System.out.println("单点退出的地址：------------" + callBackUrl);
				}
			}
            
			// 删除 passport session id 可以保证完全退出
			redisTemplate.delete(PASSPORT_TICKET_KEY
					+ (StringUtils.isNotBlank(pSessionId) ? pSessionId
							: passportSessionId));
			httpSession.invalidate();
			if (Evn.isPassport()) {
				if (StringUtils.isNotBlank(ticket)) {
					PassportClient client = PassportClientUtils
							.getPassportClient();
					client.invalidate(ticket);
					if (user == null) {
						if (DeployRegion.DEPLOY_LASA.equals(deployRegion)) {
							return client.getLogoutURL(ticket, LaSajyConstant.LS_LOGIN_OUT_INDEX_URL);
						}else{
							if (StringUtils.isNotBlank(domainName) && !"homepage".equalsIgnoreCase(domainName)) {
								String pageUrl = UrlUtils.getPrefix(getRequest())
										+ UrlUtils.SEPARATOR_SIGN + domainName
										+ "/homepage/loginPage/page";
								if (StringUtils.isNotBlank(callBackUrl)) {
									return client.getLogoutURL(ticket, callBackUrl);
								}
								return client.getLogoutURL(ticket, pageUrl);
							}
							if (StringUtils.isNotBlank(callBackUrl)) {
								return client.getLogoutURL(ticket, callBackUrl);
							}
							return client.getLogoutURL(ticket, getNativeIndexUrl());
						}
					} else {
						String password = encodeIfNot(user.getPassword());
						return client.getLogoutURL(
								ticket,
								new StringBuilder()
										.append(UrlUtils
												.getPrefix(getRequest()))
										.append("/fpf/switch/account")
										.append("?c1=")
										.append(user.getUsername())
										.append("&c2=").append(password)
										.toString());
					}
				}
				// 伪集群（有可能退出请求访问到另外一台未登录的tomcat上面，注：session粘性插件出现问题的情况下）
				else {
					redisTemplate.delete(PASSPORT_TICKET_KEY
							+ passportSessionId);
				}
			}
			if (StringUtils.isNotBlank(callBackUrl)) {
				return callBackUrl;
			}
			return "/homepage/loginPage/page";
		} catch (PassportException e) {
			getLogger().error("退出失败", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping(value = "nextUser/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Object nextUserLogin(
			@RequestParam(name = "userName") String username,
			@RequestParam(required = false) String passportSessionId) {
		User nextUser = dc(userRemoteService.findByUsername(username),
				User.class);
		if (nextUser == null
				|| !USER_STATE_NORMAL.equals(nextUser.getUserState())) {
			getRequest().getLocale().getDisplayLanguage();
			return error(MessageFormat.format("【{0}】已被删除或停用，请联系管理员", username));
		}
		User currentUser = dc(
				userRemoteService.findOneById(getLoginInfo().getUserId()),
				User.class);
		// 校验密码
		if (!decode(nextUser.getPassword()).equals(
				decode(currentUser.getPassword()))) {
			return error(MessageFormat.format("【{0}】密码已被修改，请手动登录", username));
		}
		ResultDto resultDto = new ResultDto();
		return resultDto
				.setMsg("OK")
				.setSuccess(true)
				.setCode("0")
				.setBusinessValue(
						invalidateSession(nextUser, passportSessionId, null));
	}

	@Override
	protected String getNativeIndexUrl() {
		// return UrlUtils.getPrefix(getRequest()) + UrlUtils.SEPARATOR_SIGN +
		// "/desktop/index/page";
		return sysOptionRemoteService
				.findValue(net.zdsoft.system.constant.Constant.INDEX_URL);
	}

	@Override
	protected void initLoginInfo(HttpSession httpSession, User user) {
		// Session session = Session.get(httpSession.getId());
		// if (session == null) {
		// session = new Session(httpSession.getId());
		// }
		LoginInfo loginInfo = new LoginInfo();
		if (user.getOwnerType() == User.OWNER_TYPE_TEACHER) {
			Teacher teacher = teacherRemoteService.findOneObjectById(
					user.getOwnerId(), new String[] { "id", "deptId" });
			if (teacher != null) {
				loginInfo.setOwnerId(teacher.getId());
				loginInfo.setDeptId(teacher.getDeptId());
			}
		}
		if (user.getOwnerType() == User.OWNER_TYPE_STUDENT) {
			Student stu = studentRemoteService.findOneObjectById(
					user.getOwnerId(), new String[] { "id", "classId","sex","birthday" });
			if (stu != null) {
				loginInfo.setOwnerId(stu.getId());
				loginInfo.setClassId(stu.getClassId());
				loginInfo.setSex(stu.getSex());
				loginInfo.setBirthday(stu.getBirthday());
			}
		}
		loginInfo.setOwnerType(user.getOwnerType());
		loginInfo.setUnitId(user.getUnitId());
		loginInfo.setUserId(user.getId());
		loginInfo.setOwnerId(user.getOwnerId());
		loginInfo.setUserName(user.getUsername());
		loginInfo.setRealName(user.getRealName());
		loginInfo.setUserType(user.getUserType());

		if (Integer.valueOf(User.OWNER_TYPE_SUPER).equals(user.getOwnerType())) {
			httpSession.setAttribute(
					net.zdsoft.system.constant.Constant.KEY_OPS_USER, user);
		}
		if (StringUtils.isNotBlank(user.getUnitId())) {
			Unit unit = unitRemoteService.findOneObjectById(user.getUnitId());
			if (unit != null) {
				loginInfo.setUnitClass(unit.getUnitClass());
				loginInfo.setRegion(unit.getRegionCode());
				loginInfo.setUnitName(unit.getUnitName());
			}
		}
		httpSession.setAttribute("loginInfo", loginInfo);
	}

	@Override
	protected Logger getLogger() {
		return this.logger;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * 初始化模块图标，系统图标以及默认头像
	 * 
	 * @author ke_shen@126.com
	 * @since 2018/1/24 下午10:38
	 */
	static class IconUtils {
		private Logger logger = LoggerFactory.getLogger(IconUtils.class);

		private String serverFilePath;
		private ServerRemoteService serverRemoteService;

		private IconUtils(String serverFilePath,
				ServerRemoteService serverRemoteService) {
			this.serverFilePath = serverFilePath;
			this.serverRemoteService = serverRemoteService;
		}

		/**
		 * 仅仅在LoginAction初始化的时候检查一次
		 */
		void init() {
			initSystemIcons();
			initModelIcons();
			initDefaultHeadIcons();
			if (logger.isDebugEnabled()) {
				logger.debug("icon init over");
			}
		}

		private Logger getLogger() {
			if (this.logger == null) {
				this.logger = LoggerFactory.getLogger(IconUtils.class);
			}
			return this.logger;
		}

		private void initSystemIcons() {
			try {
				String warIconPath = this.getClass().getResource("/systempic")
						.getFile();
				File warIconDir = new File(warIconPath);
				File serverIconDir = new File(serverFilePath + File.separator
						+ "store" + File.separator + "systempic");

				Map<String, String> codeIconUrl = copyWar2Server(warIconDir,
						serverIconDir, true);
				// 更新base_server
				if (!codeIconUrl.isEmpty()) {
					serverRemoteService.update(codeIconUrl, "code", "iconUrl");
				}
			} catch (Exception e) {
				log.error("eis7 desktop system icon init failed !", e);
			}

		}

		private void initModelIcons() {
			try {
				String warIconPath = this.getClass().getResource("/modelpic")
						.getPath();
				File warIconDir = new File(warIconPath);
				File serverIconDir = new File(serverFilePath + File.separator
						+ "store" + File.separator + "modelpic");
				copyWar2Server(warIconDir, serverIconDir);
			} catch (Exception e) {
				log.error("eis7 desktop model icon init failed !", e);
			}
		}

		private void initDefaultHeadIcons() {
			try {
				String iconPath = this.getClass().getResource("/portrait")
						.getFile();
				File warIconDir = new File(iconPath);
				File serverIconDir = new File(serverFilePath + File.separator
						+ "store" + File.separator + "portrait");
				File serverIconDir2 = new File(serverFilePath + File.separator
						+ "portrait");
				copyWar2Server(warIconDir, serverIconDir);
				copyWar2Server(warIconDir, serverIconDir2);
			} catch (Exception e) {
				log.error("user icon init failed !", e);
			}
		}

		private void copyWar2Server(File warIconDir, File serverIconDir)
				throws IOException {
			copyWar2Server(warIconDir, serverIconDir, false);
		}

		private Map<String, String> copyWar2Server(File warIconDir,
				File serverIconDir, boolean isSystem) throws IOException {
			if (!WebFileUtils.createDirs(serverIconDir)) {
				getLogger().error("无法创建图标文件夹，copy图标失败");
				return Collections.emptyMap();
			}

			File[] warIconArray = warIconDir.listFiles();
			Assert.notNull(warIconArray);
			File[] serverIconArray = serverIconDir.listFiles();

			Map<String, File> serverIcons = Maps.newHashMap();
			if (serverIconArray != null) {
				serverIcons = Arrays.stream(serverIconArray).collect(
						Collectors.toMap(File::getName, file -> file));
			}

			Map<String, String> codeIconUrl = new HashMap<>(16);
			for (File warIcon : warIconArray) {
				String iconName = warIcon.getName();
				if (serverIcons.get(iconName) != null
						&& serverIcons.get(iconName).exists()
						&& !warIcon.isDirectory()) {
					continue;
				}
				if (isSystem) {
					String code;
					String warIconName = warIcon.getName();
					if (warIconName.startsWith("pic_")) {
						code = warIconName.substring(
								warIconName.indexOf("_") + 1,
								warIconName.lastIndexOf("."));
					} else {
						code = warIconName.substring(0,
								warIconName.lastIndexOf("."));
					}
					codeIconUrl.put(code, "/store/systempic/" + warIconName);
				}
				if (warIcon.isDirectory()) {
					FileUtils.copyDirectoryToDirectory(warIcon, serverIconDir);
					continue;
				}
				FileUtils.copyFileToDirectory(warIcon, serverIconDir);
			}
			WebFileUtils.authorFile(serverIconDir.getAbsolutePath(), 777);
			return codeIconUrl;
		}
	}

	// 获取最新的域名参数值
	private void doLoginDomain(String regionMain) {
		HttpSession session = getSession();
		if (session != null && session.getAttribute("domainName") != null) {
			String oldDomain = session.getAttribute("domainName").toString();
			if (StringUtils.isNotBlank(regionMain)) {
				if (!regionMain.equals(oldDomain)) {
					session.setAttribute("domainName", regionMain);
				}
			}
		}
	}
	
	/**
	 * 得到第三方 应用的统一登录地址 
	 * @param defaultLoginUrl
	 */
	private String doInPutUrl(String defaultLoginUrl) {
		String	thirdLoginUrl = systemIniRemoteService.findValue(DeskTopConstant.SYSTEM_THIRD_APP_LOGIN_URL);
        if( StringUtils.isNotBlank(thirdLoginUrl)){
        	return thirdLoginUrl;
        }
        return defaultLoginUrl;
	}
}
