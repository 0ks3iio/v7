package net.zdsoft.bigdata.base.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yangkj
 * @since 2019/5/20 15:52
 */
@Entity
@Table(name = "bg_node")
public class Node extends BaseEntity<String> {

    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点域名
     */
    private String domain;
    /**
     * 节点端口
     */
    private String port;
    private String username;
    private String password;
    private String remark;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * @see net.zdsoft.bigdata.base.enu.NodeStatusCode
     */
    private Integer status;

    @Override
    public String fetchCacheEntitName() {
        return "node";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
