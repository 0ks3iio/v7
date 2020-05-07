package net.zdsoft.desktop.login.action;

import static net.zdsoft.desktop.login.LoginConstant.LOGIN_ERROR_NAME;
import static net.zdsoft.desktop.login.LoginConstant.SESSION_ATTRIBUTE_NAME;
import static net.zdsoft.desktop.login.LoginConstant.SESSION_ID_KEY;
import static net.zdsoft.desktop.login.LoginConstant.TICKET_KEY;
import static net.zdsoft.framework.entity.Constant.PASSPORT_TICKET_KEY;
import static net.zdsoft.framework.utils.SUtils.dc;
import static net.zdsoft.framework.utils.SUtils.s;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.extension.remote.service.UnitExtensionRemoteService;
import net.zdsoft.basedata.extension.remote.service.UnitState;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.AbstractDesktopController;
import net.zdsoft.desktop.constant.LaSajyConstant;
import net.zdsoft.desktop.login.LoginConstant;
import net.zdsoft.desktop.login.vo.LoginTicketDTO;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.license.entity.LicenseInfo;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.system.remote.service.LicenseRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

/**
 * eis单点登录，passport 扩展
 * @author ke_shen@126.com
 * @since 2018/1/24 下午2:45
 */
public abstract class EisLoginPassportSupportController extends AbstractDesktopController {

	@Autowired
	protected UserRemoteService userRemoteService;
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	@Autowired
	protected LicenseRemoteService licenseRemoteService;
	@Autowired
	protected SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private UnitExtensionRemoteService unitExtensionRemoteService;
	//passport 退出回掉接口
	@RequestMapping(value = { "/login/invalidate.action", "/login/invalidate" })
	public String invalidatePassport(@Validated(LoginTicketDTO.InvalidateGroup.class)
											 LoginTicketDTO ticketDTO,
									 BindingResult errors) {
		String redirectEisLoginUrl = "redirect:/homepage/loginPage/page";

		if (errors.hasErrors()) {
			getLogger().error(errors.toString());
			return redirectEisLoginUrl;
		}

		//集群环境通过passport sessionId保持集群的可行性
		//注销需先删除redis passport sessionId
		redisTemplate.delete(PASSPORT_TICKET_KEY + ticketDTO.getSid());

		//获取Eis sessionId, 若sessionId不存在，则直接转向登录页
		String sessionId = RedisUtils.get(SESSION_ID_KEY + ticketDTO.getTicket());
		if (StringUtils.isBlank(sessionId)) {
			return redirectEisLoginUrl;
		}

		HttpSession httpSession = getRequest().getSession(false);
		if (httpSession != null) {
			httpSession.invalidate();
		}
		return redirectEisLoginUrl;
	}

	/**
	 * 缓存一个小时，可重复调用
	 * @return true 过期，序列哈为空也标记为过期状态
	 */
	protected boolean isExpire() {
		String expireKey = "eis.v7.licenseExpire";
		String expire = redisTemplate.opsForValue().get(expireKey);
		if (StringUtils.isNotBlank(expire)) {
			return Boolean.valueOf(expire);
		} else {
			LicenseInfo licenseInfo = SUtils.dc(licenseRemoteService.getLicenseInfo(), LicenseInfo.class);
			boolean isExpire = false;
			if (licenseInfo == null) {
				//序列号为空，不缓存
				return true;
			} else {
				isExpire = licenseInfo.getExpireDate().before(new Date());
			}
			redisTemplate.opsForValue().set(expireKey, String.valueOf(isExpire), 24, TimeUnit.HOURS);
			return isExpire;
		}
	}

	/** 单点登录passport 校验 */
	@RequestMapping(value = { "/login/verify.action", "/login/verify" })
	public Object verifyPassport(@Validated(LoginTicketDTO.VerifyGroup.class)
										 LoginTicketDTO ticketDTO,
								 BindingResult errors) {
		//由于登录引起的出错信息应当在登录页面展示
		String eisLoginUrl = "/fpf/login/loginForPassport.action";
		try {
			if (errors.hasErrors()) {
				return forward(eisLoginUrl, LOGIN_ERROR_NAME, getSingleErrorMsg(errors));
			}
			if (isExpire()) {
				return forward(eisLoginUrl, LOGIN_ERROR_NAME, "序列号已过期");
			}


			//检查用户信息 startx
			Account account = PassportClientUtils.getPassportClient().checkTicket(ticketDTO.getTicket());
			if (account == null) {
				getLogger().error("passport acount is null");
				return forward(eisLoginUrl, LOGIN_ERROR_NAME, "passport account 不存在");
			}
			String username = account.getUsername();
			User user = dc(userRemoteService.findByUsername(username), User.class);
			if (user == null) {
				getLogger().error("eis user is null, username is {} from passport", account.getUsername());
				return forward(eisLoginUrl, LOGIN_ERROR_NAME, "用户信息不存在" + account.getUsername());
			}
			else if (!Integer.valueOf(1).equals(user.getUserState())) {
				getLogger().error("userState is error {}", user.getUserState());
				return forward(eisLoginUrl, LOGIN_ERROR_NAME, account.getUsername() + "用户状态异常");
			}
			else if(user.getExpireDate() != null && DateUtils.truncatedCompareTo(user.getExpireDate(), new Date(), Calendar.DATE) < 0) {
				return forward(eisLoginUrl, LOGIN_ERROR_NAME, account.getUsername() + "用户账号已到期，无法使用系统，如需使用请联系管理员更新账号到期时间！");
			}
			else {
				Date loginDate = user.getLoginDate();
				if(loginDate != null) {
					String pwdValidityPeriodDayStr = systemIniRemoteService.findValue(net.zdsoft.system.constant.Constant.USER_LOGIN_VALIDITY_PERIOD);
			        pwdValidityPeriodDayStr = StringUtils.isNotBlank(pwdValidityPeriodDayStr)?pwdValidityPeriodDayStr:"60";
			        int pwdValidityPeriodDay = NumberUtils.toInt(pwdValidityPeriodDayStr);
			        boolean result = false;
			        result = new Date().before(net.zdsoft.framework.utils.DateUtils.addDay(loginDate, pwdValidityPeriodDay));
			        if(!result) {
			        	return forward(eisLoginUrl, LOGIN_ERROR_NAME, account.getUsername() + "账号因长时间未登录已被系统锁定，请修改密码登陆或联系管理员");
			        }
				}
				user.setLoginDate(new Date());
				userRemoteService.update(user, user.getId(), new String[] {"loginDate"});
			}
			//检查用户信息 over

			//检查单位信息
			UnitState state = unitExtensionRemoteService.isEnable(user.getUnitId());
			if (!state.isEnable()) {
				return forward(eisLoginUrl, LOGIN_ERROR_NAME, state.unavailableInformation());
			}

			
			//初始化Eis session信息
			HttpSession httpSession = getRequest().getSession();
			String ticket = ticketDTO.getTicket();
			httpSession.setAttribute(TICKET_KEY, ticket);
			httpSession.setAttribute(SESSION_ATTRIBUTE_NAME, ticketDTO.getSid());
			httpSession.setAttribute(TICKET_KEY, ticket);
			RedisUtils.set(SESSION_ID_KEY + ticket, httpSession.getId());

			redisTemplate.opsForValue().set(PASSPORT_TICKET_KEY + ticketDTO.getSid(), ticketDTO.getSid(),
					httpSession.getMaxInactiveInterval(), TimeUnit.SECONDS);
			String tokenId = (String) httpSession.getAttribute(LaSajyConstant.LS_SAVE_TOKENID_KEY);
			if(StringUtils.isNotBlank(tokenId)){
				RedisUtils.set(tokenId, ticketDTO.getSid());
				RedisUtils.set(LoginConstant.TICKET_KEY + tokenId, ticket);
			}
			initLoginInfo(httpSession, user);

			//跳转
			String targetUrl = getRequest().getParameter("url");
			if (StringUtils.isNotBlank(targetUrl)) {
				return redirect(targetUrl);
			}

			//passport 未携带目标地址，重定向到首页（可能是5 6 7）需区分内外网
			String eisUrl = sysOptionRemoteService.getIndexUrl(getRequest().getServerName());
			if (StringUtils.isNotBlank(eisUrl)) {
				return redirect(eisUrl);
			}
			return redirect(getNativeIndexUrl());
		} catch (PassportException e) {
			getLogger().error("passport verify 出错", e);
			return forward(eisLoginUrl, LOGIN_ERROR_NAME, e.getErrorMessage());
		}
	}

	/** 获取当前服务首页地址 需要区分内外网*/
	protected abstract String getNativeIndexUrl();

	protected abstract void initLoginInfo(HttpSession httpSession, User user);
}
