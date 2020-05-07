package net.zdsoft.cache.admin;

/**
 * @author shenke
 * @since 2017.07.18
 */
public class Pair<T,O> {

    private T t;
    private O o;

    public Pair(T t, O o) {
        this.t = t;
        this.o = o;
    }

    public T getT() {
        return t;
    }

    public O getO() {
        return o;
    }
}
