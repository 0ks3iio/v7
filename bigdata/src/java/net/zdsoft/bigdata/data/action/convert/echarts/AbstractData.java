package net.zdsoft.bigdata.data.action.convert.echarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import net.zdsoft.bigdata.data.action.convert.echarts.op.Option;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:15
 */
@SuppressWarnings("unchecked")
public abstract class AbstractData<T extends AbstractData> implements Data<T>, Root<T> {

    private List<Object> data;

    @JSONField(serialize = false)
    private Option option;

    @Override
    public Option option() {
        return this.option;
    }

    @Override
    public T option(Option option) {
        this.option = option;
        return (T) this;
    }

    @Override
    public T data(Object... values) {
        if (values == null || values.length == 0) {
            return (T) this;
        }
        this.data().addAll(Arrays.asList(values));
        return (T) this;
    }

    public T data(Object data, Function<Object, Object> function) {
        this.data(function.apply(data));
        return (T) this;
    }

    public List<Object> data() {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        return data;
    }

    public List<Object> getData() {
        return data;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean contains(Object data) {
        return this.data != null && this.data.contains(data);
    }
}
