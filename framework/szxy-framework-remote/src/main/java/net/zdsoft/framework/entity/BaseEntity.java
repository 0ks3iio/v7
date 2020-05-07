package net.zdsoft.framework.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<K extends Serializable> implements Serializable {

    private static final long serialVersionUID = 4965074185832333543L;

    @Id
    private K id;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    /**
     * 缓存的标识符，如果不需要默认为这个对象设置缓存，则这个值为null
     *
     * @return
     */
    public abstract String fetchCacheEntitName();

}
