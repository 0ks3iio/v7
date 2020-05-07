package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.utils.FastJSONArrayParseEx;

/**
 * @author ke_shen@126.com
 * @since 2018/4/12 上午10:22
 */
public class IResultAtapter implements Result {

    private Result result;
    private Object array;

    public static Result convert(Result result) {
        if (result instanceof IResultAtapter) {
            return result;
        }
        return new IResultAtapter(result);
    }

    private IResultAtapter(Result result) {
        this.result = result;
    }

    @Override
    public Object getValue() {
        if (this.result.getValue() instanceof String) {
            this.array = FastJSONArrayParseEx.parseArray(result.getValue().toString());
        }
        else {
            array = this.result.getValue();
        }
        return array;
    }

    @Override
    public Throwable getException() {
        return this.result.getException();
    }

    @Override
    public boolean hasError() {
        return this.result.hasError();
    }
}
