package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.StatGrowthEventDao;
import net.zdsoft.bigdata.extend.data.entity.StatGrowthEvent;
import net.zdsoft.bigdata.extend.data.service.StatGrowthEventService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class StatGrowthEventServiceImpl extends BaseServiceImpl<StatGrowthEvent, String> implements StatGrowthEventService {

    @Resource
    private StatGrowthEventDao statGrowthEventDao;

    @Override
    protected BaseJpaRepositoryDao<StatGrowthEvent, String> getJpaDao() {
        return statGrowthEventDao;
    }

    @Override
    protected Class<StatGrowthEvent> getEntityClass() {
        return StatGrowthEvent.class;
    }

    @Override
    public List<StatGrowthEvent> findByOwnerId(String ownerId) {
        return statGrowthEventDao.findAllByOwnerIdOrderByEventDateAsc(ownerId);
    }
}
