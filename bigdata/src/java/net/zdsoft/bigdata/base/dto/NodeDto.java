package net.zdsoft.bigdata.base.dto;

import net.zdsoft.bigdata.base.entity.NodeServer;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/6/6 13:51
 */
public class NodeDto {

    private String id;
    private String name;
    private Integer count;
    private Integer status;
    private List<NodeServer> nodeServers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<NodeServer> getNodeServers() {
        return nodeServers;
    }

    public void setNodeServers(List<NodeServer> nodeServers) {
        this.nodeServers = nodeServers;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
