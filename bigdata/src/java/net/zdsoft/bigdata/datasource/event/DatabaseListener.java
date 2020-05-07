package net.zdsoft.bigdata.datasource.event;

/**
 * @author shenke
 * @since 2018/11/27 上午9:42
 */
public interface DatabaseListener {

    void onEvent(DatabaseEvent event);
}
