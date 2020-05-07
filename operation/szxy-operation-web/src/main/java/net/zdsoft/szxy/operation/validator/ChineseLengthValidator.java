package net.zdsoft.szxy.operation.validator;

import net.zdsoft.szxy.utils.Validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author shenke
 * @since 2019/4/10 下午1:59
 */
public class ChineseLengthValidator implements ConstraintValidator<ChineseLength, String> {

    private int max;

    @Override
    public void initialize(ChineseLength constraintAnnotation) {
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Validators.getRealLength(value) > max) {
            return false;
        }
        return true;
    }
}
