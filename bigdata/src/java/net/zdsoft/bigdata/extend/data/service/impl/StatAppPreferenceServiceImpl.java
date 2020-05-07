package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.StatAppPreferenceDao;
import net.zdsoft.bigdata.extend.data.entity.StatAppPreference;
import net.zdsoft.bigdata.extend.data.service.StatAppPreferenceService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class StatAppPreferenceServiceImpl extends BaseServiceImpl<StatAppPreference, String> implements StatAppPreferenceService {

    @Resource
    private StatAppPreferenceDao statAppPreferenceDao;

    @Override
    protected BaseJpaRepositoryDao<StatAppPreference, String> getJpaDao() {
        return statAppPreferenceDao;
    }

    @Override
    protected Class<StatAppPreference> getEntityClass() {
        return StatAppPreference.class;
    }

    @Override
    public List<StatAppPreference> findByOwnerId(String userId) {
        return statAppPreferenceDao.findAllByOwnerIdOrderByAppUsageDesc(userId);
    }
}
