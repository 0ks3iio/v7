package net.zdsoft.bigdata.datasource;

/**
 * @author shenke
 * @since 2018/11/27 下午1:48
 */
public final class CheckResponse extends Response {

    private static CheckResponse OK = null;

    private CheckResponse(Throwable throwable) {
        super(throwable);
    }

    private CheckResponse() {
        super(null);
    }

    public static CheckResponse error(Throwable throwable) {
        return new CheckResponse(throwable);
    }

    public static CheckResponse ok() {
        if (OK == null) {
            synchronized (CheckResponse.class) {
                if (OK == null) {
                    OK = new CheckResponse();
                    return OK;
                }
            }
        }
        return OK;
    }
}
