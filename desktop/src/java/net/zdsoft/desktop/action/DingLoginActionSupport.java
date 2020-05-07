package net.zdsoft.desktop.action;

import com.alibaba.fastjson.annotation.JSONField;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

/**
 * 接入钉钉单点（通用）</br>
 * @author ke_shen@126.com
 * @since 2017/12/14 上午9:22
 */
@Component
public class DingLoginActionSupport extends BaseAction implements EnvironmentAware {

	protected Environment environment;

	@Autowired
	private RestTemplate restTemplate;
	//@Autowired(required = false)
	//private RedisCacheManager redisCacheManager;


	//protected Cache createCache(String name) {
	//	if (redisCacheManager == null) {
	//		return null;
	//	}
	//	return redisCacheManager.getCache(name);
	//}

	/**
	 * 不需要该接口
	 * @param accessToken
	 * @param code
	 * @return
	 */
	protected DingUserInfo getDingUserInfo(String accessToken, String code){
		String dingUserId = getDingUserId(accessToken, code);
		String getDingUserInfoUrl = environment.getProperty("ding.getUserInfoUrl");
		DingUserInfo dingUserInfo = restTemplate.getForObject(getDingUserInfoUrl, DingUserInfo.class, accessToken, dingUserId);
		if (dingUserInfo.errcode == 0) {
			return dingUserInfo;
		} else {
			String errorMsg = MessageFormat.format("获取钉钉UserInfo失败,错误码(errcode):{0}, 错误信息:{1}",
					dingUserInfo.errcode, dingUserInfo.errmsg);
			throw new GetDingTokenOrInfoException(errorMsg);
		}
	}

	/**
	 * dingUserId  就是base_user 中对用的dingDingId
	 * @param accessToken
	 * @param code
	 * @return
	 */
	protected String getDingUserId(String accessToken, String code) {
		String getDingUserIdUrl = environment.getProperty("ding.getDingUserIdUrl");
		DingUserIdInfo dingUserIdInfo = restTemplate.getForObject(getDingUserIdUrl, DingUserIdInfo.class, accessToken, code);
		if ( dingUserIdInfo.errcode == 0 ) {
			return dingUserIdInfo.userid;
		}
		else {
			String errorMsg = MessageFormat.format("获取钉钉UserIdInfo失败,错误码(errcode):{0}, 错误信息:{1}",
					dingUserIdInfo.errcode, dingUserIdInfo.errmsg);
			throw new GetDingTokenOrInfoException(errorMsg);
		}
	}

	protected String sign(String ticket, String nonceStr, long timeStamp, String url) {
		String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
				+ "&url=" + url;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return byteToHex(sha1.digest());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new GetDingTokenOrInfoException("计算sinature失败", e);
		}
	}

	private String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash){
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	protected String getJSTicket(String deployRegion, String accessToken) {
		String jsTicketKey = deployRegion + ".ding.jsTicket";
		String jsTicket = RedisUtils.get(jsTicketKey);
		if (StringUtils.isBlank(jsTicket)) {
			String getJSTicketUrl = environment.getProperty("ding.getJSTicketUrl");

			DingJSTicket dingJSTicket = restTemplate.getForObject(getJSTicketUrl, DingJSTicket.class, accessToken);
			if (dingJSTicket.errcode == 0) {
				jsTicket = dingJSTicket.ticket;
				RedisUtils.set(jsTicketKey, jsTicket, (int)TimeUnit.MINUTES.toSeconds(119));
			} else {
				String errorMsg = MessageFormat.format("获取钉钉JSTicket失败,errcode:{0}, 错误信息:{1}",
						dingJSTicket.errcode, dingJSTicket.errmsg);
				throw new GetDingTokenOrInfoException(errorMsg);
			}
		}
		return jsTicket;
	}

	protected String getAccessToken(String deployRegion, String coprId, String coprSecret) {
		String accessTokenKey = deployRegion + ".ding.accessToken";
		String accessToken = RedisUtils.get(accessTokenKey);
		if (StringUtils.isBlank(accessToken)) {
			String getAccessTokenUrl = environment.getProperty("ding.getAccessTokenUrl");
			DingAccessToken dingAccessToken = restTemplate.getForObject(getAccessTokenUrl,
					DingAccessToken.class, coprId, coprSecret);
			if ( 0 == dingAccessToken.errcode) {
				accessToken = dingAccessToken.access_token;
			} else {
				String errorMsg = MessageFormat.format("获取钉钉AccessToken异常,错误码(errcode):{0}, 错误信息:{1}",
						dingAccessToken.errcode, dingAccessToken.errmsg);
				throw new GetDingTokenOrInfoException(errorMsg);
			}
			//提前一分钟过期
			RedisUtils.set(accessTokenKey, accessToken, (int)TimeUnit.MINUTES.toSeconds(119));
		}
		return accessToken;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public static class GetDingTokenOrInfoException extends RuntimeException {
		public GetDingTokenOrInfoException(String message) {
			super(message);
		}

		public GetDingTokenOrInfoException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	protected static class DingResponseEntity {
		protected int errcode;
		protected String errmsg;

		public void setErrcode(int errcode) {
			this.errcode = errcode;
		}

		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}

		public int getErrcode() {
			return errcode;
		}

		public String getErrmsg() {
			return errmsg;
		}
	}

	static class DingAccessToken extends DingResponseEntity {
		@JSONField(name = "access_token")
		private String access_token;

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}
	}

	static class DingJSTicket extends DingResponseEntity {
		private String ticket;
		@JSONField(name = "expires_in")
		private long expiresIn;

		public String getTicket() {
			return ticket;
		}

		public void setTicket(String ticket) {
			this.ticket = ticket;
		}

		public long getExpiresIn() {
			return expiresIn;
		}

		public void setExpiresIn(long expiresIn) {
			this.expiresIn = expiresIn;
		}
	}

	static class DingUserIdInfo extends DingResponseEntity {
		private String userid;
		private String deviceId;
		private boolean is_sys;
		private String sys_level;

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public boolean isIs_sys() {
			return is_sys;
		}

		public void setIs_sys(boolean is_sys) {
			this.is_sys = is_sys;
		}

		public String getSys_level() {
			return sys_level;
		}

		public void setSys_level(String sys_level) {
			this.sys_level = sys_level;
		}
	}

	static class DingUserInfo extends DingResponseEntity {
		private String mobile;
		private String name;
		private String avastar;
		private String dingId;

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAvastar() {
			return avastar;
		}

		public void setAvastar(String avastar) {
			this.avastar = avastar;
		}

		public String getDingId() {
			return dingId;
		}

		public void setDingId(String dingId) {
			this.dingId = dingId;
		}
	}

	static class DingJSONResponse extends DingResponseEntity {
		private boolean success;
		private String redirectUrl;
		private String serverPrefix;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getRedirectUrl() {
			return redirectUrl;
		}

		public void setRedirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
		}

		public String getServerPrefix() {
			return serverPrefix;
		}

		public void setServerPrefix(String serverPrefix) {
			this.serverPrefix = serverPrefix;
		}
	}
}
