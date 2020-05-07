package net.zdsoft.szxy.operation.utils;

import net.zdsoft.szxy.utils.AssertUtils;

import java.util.Objects;

/**
 * @author shenke
 * @since 2019/3/11 上午11:48
 */
public final class ObjectUtils {

    /**
     * args 中任何一个和target 进行equals比较为true则返回true否则返回false
     * target不能为空
     * @param target not null
     * @param args null return false
     * @return
     */
    public static boolean equalsIn(Object target, Object ...args) {
        AssertUtils.notNull(target, "Target Object can not null");
        if (args == null) {
            return false;
        }
        for (Object arg : args) {
            if (Objects.equals(target, arg)) {
                return true;
            }
        }
        return false;
    }
}
