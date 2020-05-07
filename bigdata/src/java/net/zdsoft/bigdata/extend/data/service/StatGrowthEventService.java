package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.StatGrowthEvent;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface StatGrowthEventService extends BaseService<StatGrowthEvent, String> {

    List<StatGrowthEvent> findByOwnerId(String ownerId);
}
