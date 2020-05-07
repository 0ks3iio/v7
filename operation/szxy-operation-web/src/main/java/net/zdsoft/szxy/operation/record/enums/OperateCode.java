package net.zdsoft.szxy.operation.record.enums;

/**
 * @author shenke
 * @since 2019/1/22 下午3:40
 */
public interface OperateCode<E extends Enum<E>> {

    /**
     * 操作类型码
     */
    Integer getOperateCode();

    /**
     * 操作名称
     */
    String getOperateName();
}
