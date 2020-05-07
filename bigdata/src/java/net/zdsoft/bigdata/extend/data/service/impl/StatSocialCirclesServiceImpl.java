package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.StatSocialCirclesDao;
import net.zdsoft.bigdata.extend.data.entity.StatSocialCircles;
import net.zdsoft.bigdata.extend.data.service.StatSocialCirclesService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class StatSocialCirclesServiceImpl extends BaseServiceImpl<StatSocialCircles, String> implements StatSocialCirclesService {

    @Resource
    private StatSocialCirclesDao statSocialCirclesDao;

    @Override
    protected BaseJpaRepositoryDao<StatSocialCircles, String> getJpaDao() {
        return statSocialCirclesDao;
    }

    @Override
    protected Class<StatSocialCircles> getEntityClass() {
        return StatSocialCircles.class;
    }

    @Override
    public List<StatSocialCircles> findByOwnerId(String ownerId) {
        return statSocialCirclesDao.findAllByOwnerId(ownerId);
    }
}
