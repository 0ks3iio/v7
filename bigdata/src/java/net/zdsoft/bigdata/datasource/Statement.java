package net.zdsoft.bigdata.datasource;

/**
 * @author shenke
 * @since 2018/11/27 下午1:49
 */
public class Statement<A extends Adapter> {
    /**
     * 封装了数据源的全部信息
     */
    private A adapter;

    public Statement(A adapter) {
        this.adapter = adapter;
    }

    public A getAdapter() {
        return adapter;
    }
}
