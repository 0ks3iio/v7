package net.zdsoft.szxy.base.wrapper;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

/**
 * 针对sysOption和base_sys_option数据的封装，便于编程
 * @author shenke
 * @since 2019/3/19 下午6:13
 */
@Data
public final class OptionValue implements Serializable {

    private static final String[] TRUE_STR = new String[]{"yes", "true", "1"};
    private static final String[] FALSE_STR = new String[]{"no", "false", "0"};

    private String value;

    public static OptionValue of(String value) {
        OptionValue optionValue = new OptionValue();
        optionValue.setValue(value);
        return optionValue;
    }

    /**
     *
     * yes true 1 ===> true
     * no false 0 ===> false
     *
     * @return
     */
    public Boolean bool() {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        for (String s : TRUE_STR) {
            if (s.equalsIgnoreCase(value)) {
                return true;
            }
        }
        for (String s : FALSE_STR) {
            if (s.equalsIgnoreCase(value)) {
                return false;
            }
        }
        throw new IllegalArgumentException(String.format("无法转换成Bool类型{%s}", value));
    }

    public Integer integer() {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return NumberUtils.toInt(value);
    }
}
