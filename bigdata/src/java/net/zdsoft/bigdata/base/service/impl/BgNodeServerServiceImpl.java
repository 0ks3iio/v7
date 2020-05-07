package net.zdsoft.bigdata.base.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.base.dao.BgNodeDao;
import net.zdsoft.bigdata.base.dao.BgNodeServerDao;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.entity.NodeServer;
import net.zdsoft.bigdata.base.service.BgNodeServerService;
import net.zdsoft.bigdata.base.service.BgNodeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangkj
 * @since 2019/6/4 11:45
 */
@Service("bgNodeServerService")
public class BgNodeServerServiceImpl extends BaseServiceImpl<NodeServer,String>
        implements BgNodeServerService {

    @Autowired
    private BgNodeServerDao bgNodeServerDao;

    @Autowired
    private BgNodeService bgNodeService;

    @Override
    protected BaseJpaRepositoryDao<NodeServer, String> getJpaDao() {
        return bgNodeServerDao;
    }

    @Override
    protected Class<NodeServer> getEntityClass() {
        return NodeServer.class;
    }

    @Override
    public List<Node> findByTypeAndStatus(String type) {
        List<String> ids = bgNodeServerDao.findNodeIdByTypeAndStatus(type);
        List<Node> nodes = bgNodeService.findListByIdIn(ids.toArray(new String[0]));
        List<Node> result = nodes.stream().filter(x -> x.getStatus() == 1).collect(Collectors.toList());
        return result;
    }
}
