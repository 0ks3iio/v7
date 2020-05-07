package net.zdsoft.desktop.login.validator;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.login.vo.LoginUserDTO;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.SUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ke_shen@126.com
 * @since 2018/1/25 下午1:20
 */
public class LoginVerifyValidator implements ConstraintValidator<LoginVerify, LoginUserDTO>{

	@Autowired
	private UserRemoteService userRemoteService;


	@Override
	public boolean isValid(LoginUserDTO value, ConstraintValidatorContext context) {
		String username = value.getUsername();
		User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
		return user != null && PWD.decode(user.getPassword()).equals(value.getPassword());
	}
}
