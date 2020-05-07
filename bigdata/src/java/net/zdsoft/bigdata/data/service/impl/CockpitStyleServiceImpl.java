package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.CockpitStyleDao;
import net.zdsoft.bigdata.data.entity.CockpitStyle;
import net.zdsoft.bigdata.data.service.CockpitStyleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/8/7 上午11:14
 */
@Service
public class CockpitStyleServiceImpl extends BaseServiceImpl<CockpitStyle, String> implements CockpitStyleService {

    @Resource
    private CockpitStyleDao cockpitStyleDao;

    @Override
    protected BaseJpaRepositoryDao<CockpitStyle, String> getJpaDao() {
        return cockpitStyleDao;
    }

    @Override
    protected Class<CockpitStyle> getEntityClass() {
        return CockpitStyle.class;
    }

    @Override
    public Optional<CockpitStyle> getByCockpitId(String cockpitId) {
        return cockpitStyleDao.getByCockpitId(cockpitId);
    }
}
