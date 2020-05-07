package net.zdsoft.bigdata.base.dao;

import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/5/20 15:58
 */
public interface BgNodeDao extends BaseJpaRepositoryDao<Node,String> {

    /**
     * 批量更改状态码
     * @param ids
     */
    @Modifying
    @Query(value = "update Node set status = (case when id in ?1 then 1 else 0 end)")
    void updateStatusByIds(List<String> ids);


}
