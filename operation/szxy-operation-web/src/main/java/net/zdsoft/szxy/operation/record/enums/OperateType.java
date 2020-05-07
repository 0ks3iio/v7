package net.zdsoft.szxy.operation.record.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 记录类型
 *
 * @author panlf
 */
public enum OperateType implements OperateCode<OperateType> {

    /**
     * 单位停用
     */
    UNIT_STOP(0, "单位停用"),

    /**
     * 单位新增
     */
    UNIT_ADD(1, "单位新增"),

    /**
     * 单位使用转正式
     */
    UNIT_TO_FORMAL(2, "单位性质变更"),

    /**
     * 单位续期
     */
    UNIT_TO_RENEWAL(3, "单位续期"),

    /**
     * 单位恢复使用
     */
    UNIT_TO_RECOVER(4, "单位恢复"),


    /**
     * 系统停用
     */
    SYSTEM_STOP(5, "系统停用"),

    /**
     * 系统新增授权
     */
    SYSTEM_ADD(6, "系统新增授权"),

    /**
     * 系统试用转正式
     */
    SYSTEM_TO_FORMAL(7, "系统性质变更"),

    /**
     * 系统续期
     */
    SYSTEM_TO_RENEWAL(8, "系统续期"),

    /**
     * 系统恢复使用
     */
    SYSTEM_TO_RECOVER(9, "系统恢复");



    private Integer code;
    private String name;
    private static Map<Integer, OperateType> cache = new HashMap<>(16);

    static {
        cache = Arrays.stream(OperateType.values())
                .collect(Collectors.toMap(OperateCode::getOperateCode, Function.identity()));
    }

    OperateType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }



    @Override
    public Integer getOperateCode() {
        return this.code;
    }

    @Override
    public String getOperateName() {
        return this.name;
    }

    public static OperateType valueOf(Integer code) throws NotSupportOperateCodeException {
        OperateType coder;
        if ((coder = cache.get(code)) == null && code!=null) {
            throw new NotSupportOperateCodeException("不支持的操作类型码");
        }
        return coder;
    }

    public static Optional<OperateType> of(Integer code) {
        return Optional.ofNullable(cache.get(code));
    }
}
