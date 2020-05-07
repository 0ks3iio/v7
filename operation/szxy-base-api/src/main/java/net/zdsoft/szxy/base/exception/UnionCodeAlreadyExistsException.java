package net.zdsoft.szxy.base.exception;

import lombok.Getter;

/**
 * @author shenke
 * @since 2019/3/20 下午2:49
 */
public class UnionCodeAlreadyExistsException extends RpcException {

    @Getter
    private String unionCode;

    public UnionCodeAlreadyExistsException(String message, String unionCode) {
        super(message);
        this.unionCode = unionCode;
    }
}
