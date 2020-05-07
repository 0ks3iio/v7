package net.zdsoft.bigdata.base.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.base.entity.Node;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/5/20 16:06
 */
public interface BgNodeService extends BaseService<Node,String> {

    /**
     * 检测节点服务器状态
     */
    public boolean nodeConnection(Node node);

    /**
     * 批量更改节点状态
     * @param ids
     */
    public void updateStatusByIds(List<String> ids);
}
