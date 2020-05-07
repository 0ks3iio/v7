package net.zdsoft.keel.dao;

/**
 * @author shenke
 * @since 2017.08.03
 */
public class DataExistsException extends RuntimeException{

    private static final long serialVersionUID = 6019586042924994573L;

    public DataExistsException() {
        super();
    }

    public DataExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataExistsException(String message) {
        super(message);
    }

    public DataExistsException(Throwable cause) {
        super(cause);
    }

}
