package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.StatPlatformUsageDao;
import net.zdsoft.bigdata.extend.data.entity.StatPlatformUsage;
import net.zdsoft.bigdata.extend.data.service.StatPlatformUsageService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class StatPlatformUsageServiceImpl extends BaseServiceImpl<StatPlatformUsage, String> implements StatPlatformUsageService {

    @Resource
    private StatPlatformUsageDao statPlatformUsageDao;

    @Override
    protected BaseJpaRepositoryDao<StatPlatformUsage, String> getJpaDao() {
        return statPlatformUsageDao;
    }

    @Override
    protected Class<StatPlatformUsage> getEntityClass() {
        return StatPlatformUsage.class;
    }

    @Override
    public List<StatPlatformUsage> findByOwnerId(String userId) {
        return statPlatformUsageDao.findAllByOwnerIdOrderByLoginDateAsc(userId);
    }
}
