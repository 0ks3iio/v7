package net.zdsoft.cache.admin.core;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shenke
 * @since 2017.07.12
 */
public class CValue {

    private Object field;
    private Object value;
    private Double score; //ZSET

    public CValue(Object field, Object value) {
        this.field = field;
        this.value = value;
    }

    public CValue(Object value, Double score) {
        this.value = value;
        this.score = score;
    }

    public CValue(Object value) {
        this.value = value;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    /**
     * json 字符串 转换引号
     * @return
     */
    public Object getViewValue(){
        if ( value == null ) {
            return StringUtils.EMPTY;
        }
        if ( value instanceof String ){
            return StringUtils.replacePattern((String)value,"\"","&quot;");
        }

        if ( value instanceof JSONObject) {
            return StringUtils.replacePattern(value.toString(),"\"","&quot;");
        }
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public static List<CValue> createHash(Map<Object,Object> entries) {
        List<CValue> cValues = Lists.newArrayList();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            cValues.add(new CValue(entry.getKey(),entry.getValue()));
        }
        return cValues;
    }

    public static List<CValue> createList(List<Object> list){
        List<CValue> cValues = Lists.newArrayList();
        for (Object o : list) {
            cValues.add(new CValue(o));
        }
        return cValues;
    }

    public static List<CValue> createZSet(Set<ZSetOperations.TypedTuple<Object>> sets){
        List<CValue> cValues = Lists.newArrayList();
        for (ZSetOperations.TypedTuple<Object> o : sets) {
            cValues.add(new CValue(o.getValue(),o.getScore()));
        }
        return cValues;
    }

    public static List<CValue> createSet(Set<Object> sets) {
        List<CValue> cValues = Lists.newArrayList();
        for (Object o : sets) {
            cValues.add(new CValue(o));
        }
        return cValues;
    }

    public static List<CValue> createString(Object value) {
        List<CValue> cValues = Lists.newArrayList();
        cValues.add(new CValue(value));
        return cValues;
    }
}
