package net.zdsoft.desktop.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 参见{@code FunctionArea.getDynamicParam} <br/>
 * dataUrl 动态参数定义</br>
 * Created by shenke on 2017/4/1.
 */
public class FunctionAreaDynamicParam {

    public static final String[] INT_TYPE_ARRAY = new String[]{"int.class","Integer.class","long.class","Long.class"};

    private String name;
    private String typeClass; //int.class String.class Integer.class
    private String max;
    private String min;
    private String description;

    @JSONField(serialize = false,deserialize = false)
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String toJSONString(){
        return Json.toJSONString(this);
    }

    @JSONField(serialize = false)
    public boolean isNumber(){
        return ArrayUtils.contains(INT_TYPE_ARRAY,getTypeClass());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JSONField(deserialize = false)
    public Object getValue() {
        return value;
    }

    @JSONField(serialize = false)
    public void setValue(Object value) {
        this.value = value;
    }

    public static List<FunctionAreaDynamicParam> parse(String dynamic){
        if (StringUtils.isBlank(dynamic)){
            return Lists.newArrayList();
        }
        return SUtils.dt(dynamic,FunctionAreaDynamicParam.class);
    }
    public static void main(String[] args){
        FunctionAreaDynamicParam dynamicParam = new FunctionAreaDynamicParam();
        dynamicParam.setName("num");
        dynamicParam.setTypeClass("int.class");
        dynamicParam.setMax("10");
        dynamicParam.setMin("5");
        dynamicParam.setDescription("显示数量");
        FunctionAreaDynamicParam imageParam = new FunctionAreaDynamicParam();
        imageParam.setName("showImage");
        imageParam.setTypeClass("boolean.class");
        imageParam.setDescription("是否显示头像");
        FunctionAreaDynamicParam isPage = new FunctionAreaDynamicParam();
        isPage.setName("paging");
        isPage.setTypeClass("boolean.class");
        isPage.setDescription("是否分页显示");
        List<FunctionAreaDynamicParam> list = Lists.newArrayList(dynamicParam,imageParam,isPage);
        System.out.println(SUtils.serialize(list));
    }
}
