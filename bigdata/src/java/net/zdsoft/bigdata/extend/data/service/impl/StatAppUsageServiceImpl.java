package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.StatAppUsageDao;
import net.zdsoft.bigdata.extend.data.entity.StatAppUsage;
import net.zdsoft.bigdata.extend.data.service.StatAppUsageService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class StatAppUsageServiceImpl extends BaseServiceImpl<StatAppUsage, String> implements StatAppUsageService {

    @Resource
    private StatAppUsageDao statAppUsageDao;

    @Override
    protected BaseJpaRepositoryDao<StatAppUsage, String> getJpaDao() {
        return statAppUsageDao;
    }

    @Override
    protected Class<StatAppUsage> getEntityClass() {
        return StatAppUsage.class;
    }

    @Override
    public List<StatAppUsage> findByOwnerId(String userId) {
        return statAppUsageDao.findAllByOwnerIdOrderByUsageDateAsc(userId);
    }
}
