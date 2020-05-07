package net.zdsoft.bigdata.datasource.event;

import java.io.Serializable;

/**
 * @author shenke
 * @since 2018/11/27 下午5:53
 */
public abstract class DatabaseEvent implements Serializable {

    protected Object source;

    public DatabaseEvent(Object source) {
        this.source = source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
