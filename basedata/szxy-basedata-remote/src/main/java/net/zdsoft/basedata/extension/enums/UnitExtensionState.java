package net.zdsoft.basedata.extension.enums;

/**
 * 运营平台扩展的单位状态
 * @author shenke
 * @since 2019/1/21 下午2:30
 */
public interface UnitExtensionState {

    /**
     * 正常
     */
    Integer NORMAL = 0;
    /**
     * 停用
     */
    Integer DISABLE = 1;
    /**
     * 到期停用
     */
    Integer EXPIRE = 2;
}
