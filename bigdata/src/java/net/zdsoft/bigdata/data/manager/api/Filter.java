/**
 * FileName: Filter.java
 * Author:   shenke
 * Date:     2018/5/24 上午11:35
 * Descriptor:
 */
package net.zdsoft.bigdata.data.manager.api;

/**
 * @author shenke
 * @since 2018/5/24 上午11:35
 */
public interface Filter {

    //void init();

    void doFilter(Invocation invocation);

}
