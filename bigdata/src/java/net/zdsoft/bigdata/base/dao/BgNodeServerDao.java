package net.zdsoft.bigdata.base.dao;

import net.zdsoft.bigdata.base.entity.NodeServer;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/6/4 11:47
 */
public interface BgNodeServerDao extends BaseJpaRepositoryDao<NodeServer,String> {

    /**
     * 按服务类型获取所有可用的服务的正常节点id集合
     */
    @Query("select nodeId from NodeServer where type = ?1 and status = 1 group by nodeId")
    List<String> findNodeIdByTypeAndStatus(String type);
}
