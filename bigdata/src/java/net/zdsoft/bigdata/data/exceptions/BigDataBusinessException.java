package net.zdsoft.bigdata.data.exceptions;

/**
 * 业务异常
 * Created by wangdongdong on 2018/5/15 10:11.
 */
public class BigDataBusinessException extends Exception {

    public BigDataBusinessException(String message) {
        super(message);
    }

    public BigDataBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BigDataBusinessException(Throwable cause) {
        super(cause);
    }
}
