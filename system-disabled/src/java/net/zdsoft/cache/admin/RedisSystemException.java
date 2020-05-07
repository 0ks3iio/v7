package net.zdsoft.cache.admin;

/**
 * @author shenke
 * @since 2017.07.18
 */
public class RedisSystemException extends Exception {

    public RedisSystemException(String message) {
        super(message);
    }

    public RedisSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisSystemException(Throwable cause) {
        super(cause);
    }
    
}
