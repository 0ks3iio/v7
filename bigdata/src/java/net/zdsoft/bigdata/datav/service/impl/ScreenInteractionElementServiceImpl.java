package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.ScreenInteractionElementRepository;
import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;
import net.zdsoft.bigdata.datav.service.ScreenInteractionElementService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:42
 */
@Service("screenInteractionElementService")
public class ScreenInteractionElementServiceImpl extends BaseServiceImpl<ScreenInteractionElement, String>
        implements ScreenInteractionElementService {

    @Resource
    private ScreenInteractionElementRepository screenInteractionElementRepository;

    @Override
    protected BaseJpaRepositoryDao<ScreenInteractionElement, String> getJpaDao() {
        return screenInteractionElementRepository;
    }

    @Override
    protected Class<ScreenInteractionElement> getEntityClass() {
        return ScreenInteractionElement.class;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<ScreenInteractionElement> getScreenDefaultInteractionItems(String screenId) {
        return screenInteractionElementRepository.streamAllByScreenId(screenId);
    }

    @Override
    public ScreenInteractionElement getByBindKeyAndScreenId(String bindKey, String screenId) {
        return screenInteractionElementRepository.getByBindKeyAndScreenId(bindKey, screenId);
    }

    @Override
    public void deleteByScreenIdAndBindKey(String screenId, String bindKey) {
        screenInteractionElementRepository.deleteByScreenIdAndBindKey(screenId, bindKey);
    }

    @Override
    public void deleteByScreenId(String screenId) {
        screenInteractionElementRepository.deleteByScreenId(screenId);
    }
}
