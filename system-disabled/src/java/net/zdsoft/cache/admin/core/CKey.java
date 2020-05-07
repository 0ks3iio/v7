package net.zdsoft.cache.admin.core;

import org.springframework.data.redis.connection.DataType;

/**
 * @author shenke
 * @since 2017.07.11
 */
public class CKey implements Comparable {

    private String key;
    //memcached is key-value so dataType is string
    private DataType dataType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public int compareTo(Object o) {
        if ( o == null ) {
            return 1;
        }
        if ( o instanceof CKey ) {
            return this.getKey().compareTo(CKey.class.cast(o).getKey());
        }
        return 1;
    }

    public String getTypeName(){
        return this.getDataType().name();
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return Boolean.FALSE;
        }
        if ( obj instanceof  CKey ) {
            return this.getKey().equals(CKey.class.cast(obj).getKey());
        }
        return Boolean.FALSE;
    }
}
