package net.zdsoft.bigdata.datasource;

/**
 * @author shenke
 * @since 2018/11/27 下午1:47
 */
public class Response {

    private Throwable throwable;

    public Response(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean hasError() {
        return throwable != null;
    }
}
