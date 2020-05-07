/**
 * FileName: Classification.java
 * Author:   shenke
 * Date:     2018/5/28 下午1:22
 * Descriptor:
 */
package net.zdsoft.bigdata.data.code;

/**
 * @author shenke
 * @since 2018/5/28 下午1:22
 */
public abstract class Classification{

    protected String name;
    protected Integer order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
