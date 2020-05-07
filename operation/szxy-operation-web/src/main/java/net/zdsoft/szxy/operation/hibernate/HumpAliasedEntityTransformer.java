package net.zdsoft.szxy.operation.hibernate;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.validation.DataBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 要求要目标对象必须有默认构造方法
 * 同时要求查询的字段和实体的影射关系比较固定
 * ex： 实体 userName  => 查询字段名称 user_name
 * 不支持复杂对象的影射
 *
 * @author shenke
 * @since 2019/3/4 下午6:42
 */
public class HumpAliasedEntityTransformer<T> implements ResultTransformer {

    private Class<T> targetClass;
    private Map<String, String> cache = new HashMap<>();

    public HumpAliasedEntityTransformer(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        T target = BeanUtils.instantiateClass(targetClass);
        List<PropertyValue> values = new ArrayList<>(tuple.length);
        for (int i = 0, length = tuple.length; i < length; i++) {
            values.add(new PropertyValue(toHump(aliases[i]), tuple[i]));
        }
        DataBinder dataBinder = new DataBinder(target);
        dataBinder.bind(new HumpAliasedPropertyValues(values));
        return target;
    }

    @Override
    public List<T> transformList(List collection) {
        return collection;
    }

    public String toHump(String src) {
        return cache.computeIfAbsent(src, e -> {
            StringBuilder builder = new StringBuilder();
            String[] childs = e.split("_");
            for (int i = 0, length = childs.length; i < length; i++) {
                if (i == 0) {
                    builder.append(childs[i].toLowerCase());
                } else {
                    builder.append(Character.toUpperCase(childs[i].charAt(0)))
                            .append(childs[i].substring(1).toLowerCase());
                }
            }
            return builder.toString();
        });
    }
}
