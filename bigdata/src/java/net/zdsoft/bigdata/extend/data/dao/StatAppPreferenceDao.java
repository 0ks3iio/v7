package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.StatAppPreference;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface StatAppPreferenceDao extends BaseJpaRepositoryDao<StatAppPreference, String> {

    List<StatAppPreference> findAllByOwnerIdOrderByAppUsageDesc(String ownerId);
}
