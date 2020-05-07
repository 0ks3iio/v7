package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.InteractionItem;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:34
 */
@Repository
public interface InteractionItemRepository extends BaseJpaRepositoryDao<InteractionItem, String> {

    List<InteractionItem> getInteractionItemsByDiagramType(Integer diagramType);
}
