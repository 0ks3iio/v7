package net.zdsoft.desktop.login.vo;

import net.zdsoft.desktop.login.validator.LoginVerify;

import javax.validation.constraints.NotNull;

/**
 * @author ke_shen@126.com
 * @since 2018/1/25 下午1:16
 */
@LoginVerify(message = "用户名或密码错误")
public class LoginUserDTO {

	@NotNull(message = "用户名不能为空")
	private String username;
	@NotNull(message = "密码不能为空")
	private String password;
	private String verifyCode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
}
