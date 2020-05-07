package net.zdsoft.newstusys.businessimport.exception;

/**
 * Created by IntelliJ IDEA. User: yaozw Date: 2005-4-7 Time: 17:34:46 代表配置不合法
 */
public class ConfigurationException extends Exception {
    private static final long serialVersionUID = -8221304612753963775L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
