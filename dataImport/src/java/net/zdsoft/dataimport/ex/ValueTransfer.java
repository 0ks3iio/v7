package net.zdsoft.dataimport.ex;

import java.lang.reflect.Field;

/**
 * z数据转换
 * @author shenke
 * @since 17-8-13 下午8:37
 */
public interface ValueTransfer {

    Object transfer(Object srcValue, Field descField);

    int order();
}
