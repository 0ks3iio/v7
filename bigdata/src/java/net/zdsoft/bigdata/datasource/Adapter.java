package net.zdsoft.bigdata.datasource;

import java.io.Serializable;

/**
 * @author shenke
 * @since 2018/11/27 下午3:10
 */
public abstract class Adapter implements Serializable {

    public abstract DataType getDataType();

}
