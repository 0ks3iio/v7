package net.zdsoft.basedata.extension.enums;

/**
 * 运营平台 定义的
 * 单位过期类型、服务时间过期类型
 *
 * @author shenke
 * @since 2019/1/21 下午2:24
 */
public interface UnitExtensionExpireType {

    /**
     * 永久使用时间类型
     */
    Integer PERMANENT = 0;

    /**
     * 指定过期时间
     */
    Integer SPECIFY_TIME = 1;
}
