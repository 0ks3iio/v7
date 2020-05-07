package net.zdsoft.bigdata.base.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.entity.NodeServer;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/6/4 11:44
 */
public interface BgNodeServerService extends BaseService<NodeServer,String> {

    /**
     * 按服务类型获取所有可用的服务的正常节点list
     */
    List<Node> findByTypeAndStatus(String type);
}
