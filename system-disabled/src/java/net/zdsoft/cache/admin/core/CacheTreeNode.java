package net.zdsoft.cache.admin.core;

import java.util.Set;

/**
 * @author shenke
 * @since 2017.07.11
 */
public class CacheTreeNode implements Comparable{

    private String name;
    private boolean open = Boolean.FALSE;
    private boolean isParent;
    private String host;
    private String port;
    private String dbIndex;
    private Set<CacheTreeNode> children;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public Set<CacheTreeNode> getChildren() {
        return children;
    }

    public void setChildren(Set<CacheTreeNode> children) {
        this.children = children;
    }

    @Override
    public int compareTo(Object o) {
        if ( o == null ) {
            return 1;
        }
        if ( o instanceof CacheTreeNode ) {
            return this.getName().compareTo(CacheTreeNode.class.cast(o).getName());
        }
        return 1;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return Boolean.FALSE;
        }
        if ( obj instanceof  CacheTreeNode ) {
            return this.getName().equals(CacheTreeNode.class.cast(obj).getName());
        }
        return Boolean.FALSE;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(String dbIndex) {
        this.dbIndex = dbIndex;
    }
}
