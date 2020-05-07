package net.zdsoft.desktop.action.docking;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.AbstractController;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.desktop.login.LoginConstant;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.SmsRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;

public class SimpleLoginAction extends AbstractController{

	protected static RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	protected UserRemoteService userRemoteService;
	@Autowired
	protected SysOptionRemoteService sysOptionRemoteService;
	@Autowired
	protected SmsRemoteService smsRemoteService;
	@Autowired
	protected ClassRemoteService classRemoteService;
	@Autowired
	protected UnitRemoteService unitRemoteService;
	@Autowired
	protected SystemIniRemoteService systemIniRemoteService;
    @Resource
    protected RegionRemoteService regionRemoteService;
    @Autowired
	protected RedisTemplate<String, String> redisTemplate;
    @Autowired
    protected BaseSyncSaveRemoteService baseSyncSaveService;
    @Autowired
	private TeacherRemoteService teacherRemoteService;
    @Autowired
	protected StudentRemoteService studentRemoteService;
    
    
    
	/**
	 * 通用的登录方法
	 * @param targetUrl   目标url
	 * @param requestUrl  切换地址
	 */
	public String loginForCommon(HttpServletRequest request, String targetUrl, String requestUrl, String requestName, ModelMap map) {
		try {
			String username = request.getParameter("uid");
			//判断当前登录的账号是否和未退出的账号一致
			LoginInfo currentLogin = null;
			if (getSession() != null && (currentLogin = ServletUtils.getLoginInfo(getSession())) != null) {
				if (!StringUtils.equals(currentLogin.getUserName(), username)) {
					//1.退出eis
					String ticket = (String) getHttpSession().getAttribute(LoginConstant.TICKET_KEY);
					quitSession();
					//2.退出passport
					if(Evn.isPassport()){
						String backURL = new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
								.append(targetUrl)
								.append("?")
								.append(requestName)
								.append("=")
								.append(request.getParameter(requestName))
								.toString();
						return quitPassport(backURL,ticket);
					}
				}
			}
			User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
			if(user != null) {
				if (Evn.isPassport()) {
					//重定向daopassport
					String passportLoginUrl = getRedirectUrl(user, targetUrl, null,Boolean.TRUE,null);
					map.addAttribute("passportLoginUrl", passportLoginUrl);
					if (StringUtils.isBlank(passportLoginUrl)) {
						return "redirect:" + passportLoginUrl;
					} else {
						return "/desktop/login/wenxuanLogin.ftl";
					}
				}
				return "redirect:/desktop/index/page";
			}else {
				return promptFlt(map,"该用户尚未同步");
			}
		} catch (Exception e) {
			return promptFlt(map,"登录失败，数据解密异常");
		}
	}
	
	
	
	/**
     * 验证部署地区
     * @param region
     * @return
     */
    protected boolean doProvingDeploy(String region) {
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
     */
	private void quitSession() {
		HttpSession httpSession = getHttpSession();
			String pSessionId = (String) httpSession.getAttribute(LoginConstant.SESSION_ATTRIBUTE_NAME);
			//删除 passport session id 可以保证完全退出
			redisTemplate.delete(Constant.PASSPORT_TICKET_KEY + pSessionId);
			getSession().invalidate();
			getHttpSession().invalidate();
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
	
	
	/**
	 * 为passport登录页提供服务
	 */
	private String getIndexURL() {
		return UrlUtils.getPrefix(getRequest());
	}
	
	protected String getNativeIndexUrl() {
		return sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.INDEX_URL);
	}

	private String getPassportURL() {
		return sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ?
				sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.PASSPORT_SECOND_URL)
				: sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.PASSPORT_URL);
	}
	
	/**
	 * 
	 * 通用的退出当前账号的方法
	 * @param loginUsername 最新需要登录的用户名
	 * @param backUrl  退出当前登录的账号，重新登录的地址
	 */
	protected String logoutBeforeUsername(String loginUsername,String backUrl){
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
	protected String  loginUserName(User user,String url, String urlType,ModelMap map){
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
	
	
	/**
	 * 得到默认加密密码
	 * @return
	 */
	protected String getDefaultPwd() {
		return new PWD(BaseSaveConstant.DEFAULT_PASS_WORD_VALUE).encode();
	}
	
	/**
	 * id不足32位的， 前面补为0
	 * @param id
	 * @return
	 */
	protected String doChangeId(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		return StringUtils.leftPad(id, 32, "0");
	}
	
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
}
