package net.zdsoft.desktop.login.vo;


import javax.validation.constraints.NotNull;

/**
 * passport 参数
 * @author ke_shen@126.com
 * @since 2018/1/24 下午2:51
 */
public class LoginTicketDTO {

	@NotNull(message = "ticket不能为空", groups = {VerifyGroup.class, InvalidateGroup.class})
	private String ticket;
	@NotNull(message = "auth不能为空", groups = {VerifyGroup.class})
	private String auth;
	@NotNull(message = "passport sessionId 不能为空", groups = {VerifyGroup.class, InvalidateGroup.class})
	private String sid;

	public String getTicket() {
		return ticket;
	}

	public LoginTicketDTO setTicket(String ticket) {
		this.ticket = ticket;
		return this;
	}

	public String getAuth() {
		return auth;
	}

	public LoginTicketDTO setAuth(String auth) {
		this.auth = auth;
		return this;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public static interface VerifyGroup {}

	public static interface InvalidateGroup{}
}