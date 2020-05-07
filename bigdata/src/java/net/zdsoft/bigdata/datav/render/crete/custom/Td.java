package net.zdsoft.bigdata.datav.render.crete.custom;

/**
 * @author shenke
 * @since 2019/4/23 下午5:14
 */
public class Td<T> implements Comparable<Td> {

    private String name;
    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int compareTo(Td o) {
        if (o == null) {
            return -1;
        }
        if (this.name == null) {
            return -1;
        }
        return this.getName().compareTo(o.getName());
    }
}
