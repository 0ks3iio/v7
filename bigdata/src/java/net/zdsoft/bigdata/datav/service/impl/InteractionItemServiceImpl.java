package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.InteractionItemRepository;
import net.zdsoft.bigdata.datav.entity.InteractionItem;
import net.zdsoft.bigdata.datav.service.InteractionItemService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:36
 */
@Service
public class InteractionItemServiceImpl extends BaseServiceImpl<InteractionItem, String> implements InteractionItemService {

    @Resource
    private InteractionItemRepository interactionItemRepository;

    @Override
    protected BaseJpaRepositoryDao<InteractionItem, String> getJpaDao() {
        return interactionItemRepository;
    }

    @Override
    protected Class<InteractionItem> getEntityClass() {
        return InteractionItem.class;
    }

    @Override
    public List<InteractionItem> getInteractionItemsByDiagramType(Integer diagramType) {
        return interactionItemRepository.getInteractionItemsByDiagramType(diagramType);
    }
}
