package net.zdsoft.szxy.operation.security;

import lombok.Getter;

/**
 * 数字校园定义的特殊的响应码
 * @author shenke
 * @since 2019/2/25 下午2:20
 */
public enum SzxyResponseStatusCode {
    /**
     * 未登录, 登录超时
     */
    NO_LOGIN(4001),
    /**
     * 未授权，没有权限
     */
    NO_AUTHORIZATION(4003),
    /**
     * 服务器异常
     */
    SERVER_ERROR(4005);

    @Getter
    private int code;

    SzxyResponseStatusCode(int code) {
        this.code = code;
    }
}
