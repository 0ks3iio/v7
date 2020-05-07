package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.StatAppUsage;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface StatAppUsageDao extends BaseJpaRepositoryDao<StatAppUsage, String> {

    List<StatAppUsage> findAllByOwnerIdOrderByUsageDateAsc(String ownerId);
}
