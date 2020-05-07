package net.zdsoft.szxy.base.exception;

import lombok.Getter;

/**
 * @author shenke
 * @since 2019/3/20 下午2:56
 */
public class UsernameAlreadyExistsException extends RpcException {

    @Getter
    private String username;

    public UsernameAlreadyExistsException(String message, String username) {
        super(message);
        this.username = username;
    }
}
