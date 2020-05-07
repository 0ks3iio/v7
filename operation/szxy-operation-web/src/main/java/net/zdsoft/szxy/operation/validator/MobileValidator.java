package net.zdsoft.szxy.operation.validator;

import net.zdsoft.szxy.utils.PhoneUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author shenke
 * @since 2019/4/9 上午11:21
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return PhoneUtils.isPhone(value);
    }
}
