package net.zdsoft.szxy.operation.inner.exception;

import lombok.Getter;

/**
 * @author shenke
 * @since 2019/4/9 下午5:07
 */
public class IllegalStateException extends Exception {

    @Getter
    private Integer state;

    public IllegalStateException(String message, Integer state) {
        super(message);
        this.state = state;
    }
}
