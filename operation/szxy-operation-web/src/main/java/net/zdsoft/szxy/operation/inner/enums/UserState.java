package net.zdsoft.szxy.operation.inner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shenke
 * @since 2019/4/9 下午3:09
 */
@AllArgsConstructor
public enum UserState {

    /**
     * 停用
     */
    STOP(0, "停用"),
    /**
     * 正常
     */
    NORMAL(1, "正常");


    @Getter
    private Integer state;

    @Getter
    private String name;

    public static UserState from(Integer state) {
        if (NORMAL.getState().equals(state)) {
            return NORMAL;
        }
        else if (STOP.getState().equals(state)) {
            return STOP;
        }
        throw new RuntimeException("不合法的状态码");
    }
}
