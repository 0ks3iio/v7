package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.ScreenStyleRepository;
import net.zdsoft.bigdata.datav.entity.ScreenStyle;
import net.zdsoft.bigdata.datav.service.ScreenStyleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2018/10/16 18:42
 */
@Service
public class ScreenStyleServiceImpl extends BaseServiceImpl<ScreenStyle, String> implements ScreenStyleService {

    @Resource
    private ScreenStyleRepository screenStyleRepository;

    @Override
    protected BaseJpaRepositoryDao<ScreenStyle, String> getJpaDao() {
        return screenStyleRepository;
    }

    @Override
    protected Class<ScreenStyle> getEntityClass() {
        return ScreenStyle.class;
    }

    @Override
    public ScreenStyle getByScreenId(String screenId) {
        return screenStyleRepository.getScreenStyleByScreenId(screenId);
    }

    @Override
    public void bacthDelete(String[] screeenIds) {
        screenStyleRepository.deleteAllByScreenIdIn(screeenIds);
    }
}
