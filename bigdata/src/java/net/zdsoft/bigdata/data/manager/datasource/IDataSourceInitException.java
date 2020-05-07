package net.zdsoft.bigdata.data.manager.datasource;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午4:14
 */
public class IDataSourceInitException extends Exception {

    private String uri;

    public IDataSourceInitException(String message, String uri) {
        super(message);
        this.uri = uri;
    }

    public IDataSourceInitException(String message, Throwable cause, String uri) {
        super(message, cause);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
