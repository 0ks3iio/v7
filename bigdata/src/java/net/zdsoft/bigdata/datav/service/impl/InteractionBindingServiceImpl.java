package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.InteractionBindingRepository;
import net.zdsoft.bigdata.datav.entity.InteractionBinding;
import net.zdsoft.bigdata.datav.service.InteractionBindingService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:38
 */
@Service("interactionBindingService")
public class InteractionBindingServiceImpl extends BaseServiceImpl<InteractionBinding, String> implements InteractionBindingService {

    @Resource
    private InteractionBindingRepository interactionBindingRepository;

    @Override
    protected BaseJpaRepositoryDao<InteractionBinding, String> getJpaDao() {
        return interactionBindingRepository;
    }

    @Override
    protected Class<InteractionBinding> getEntityClass() {
        return InteractionBinding.class;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<InteractionBinding> getBindingsByElementId(String elementId) {
        return interactionBindingRepository.streamAllByElementId(elementId);
    }

    @Override
    public List<InteractionBinding> getBindingsByScreenId(String screenId) {
        return interactionBindingRepository.getBindingsByScreenId(screenId);
    }

    @Override
    public void deleteByElementId(String elementId) {
        interactionBindingRepository.deleteByElementId(elementId);
    }

    @Override
    public void deleteByElementIds(String[] elementIds) {
        interactionBindingRepository.deleteByElementIdIn(elementIds);
    }
}
