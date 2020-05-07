package net.zdsoft.bigdata.datav.enu;

/**
 * 用于判定参数是否是集合
 * @author shenke
 * @since 2018/9/27 9:44
 */
public enum ArrayEnum {

    /**
     * 是数组形式
     */
    TRUE(1);

    private Integer array;

    ArrayEnum(Integer array) {
        this.array = array;
    }

    public Integer getArray() {
        return array;
    }
}
