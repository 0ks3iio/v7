package net.zdsoft.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnInfo {

    String VTYPE_EMAIL = "email";
    String VTYPE_URL = "url";
    String VTYPE_INT = "int";
    String VTYPE_STRING = "string";
    String VTYPE_DATE = "date";
    String VTYPE_SELECT = "select";
    String VTYPE_RADIO = "radio";
    String VTYPE_CHECKBOX = "checkbox";
    String VTYPE_RANGEDATE = "rangedate";
    String UNIT_TYPE_1 = "1";
    String UNIT_TYPE_2 = "2";
    String UNIT_TYPE_3 = "3";
    String UNIT_TYPE_4 = "4";
    String UNIT_TYPE_5 = "5";

    int displayOrder() default 999;

    String vsql() default "";

    //适用的学校类别，base_school.school_type
    String fitForSchoolType() default "";

    //适合的单位类型，1=教育局，2=学校
    String fitForUnitClass() default "";

    //单位类型
    String unitType() default "";

    //字段是否隐藏
    boolean hide() default false;

    //是否是否不可用（不传送到页面上）
    boolean disabled() default false;

    //格式类型，主要用于时间
    String format() default "";

    //微代码
    String mcodeId() default "";

    //显示的名字
    String displayName() default "";

    //是否允许为空
    boolean nullable() default true;

    //字段最大字节长度
    int maxLength() default 1000;

    //字段最小字节长度
    int minLength() default 0;

    //指定长度，效果与将maxLength和minLength设置成一样相同
    int length() default 0;

    //内容个最大值，适合数字
    String max() default "";

    //内容最小值，适合数字
    String min() default "";

    //校验正则表达式
    String regex() default "";

    //正则表达式提示信息（验证通不过时）
    String regexTip() default "";

    //内容类型，详见VTYPE_*属性默认空string类型type=text 如果传入password 就是密码类型
    String vtype() default "";

    String[] vselect() default {};

    //是否允许编辑
    boolean readonly() default false;
}
