package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.StatGrowthEvent;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

/**
 * Created by wangdongdong on 2018/8/3 17:04.
 */
public interface StatGrowthEventDao extends BaseJpaRepositoryDao<StatGrowthEvent, String> {

    List<StatGrowthEvent> findAllByOwnerIdOrderByEventDateAsc(String ownerId);
}
