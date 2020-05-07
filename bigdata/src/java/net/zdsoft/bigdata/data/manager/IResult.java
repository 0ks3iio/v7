package net.zdsoft.bigdata.data.manager;

import net.zdsoft.bigdata.data.manager.api.Result;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午3:08
 */
public class IResult implements Result {

    private Object value;
    private Throwable error;

    public IResult() {
    }

    public IResult(Object value) {
        this(value, null);
    }

    public IResult(Object value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Throwable getException() {
        return error;
    }

    @Override
    public boolean hasError() {
        return error != null;
    }
}
