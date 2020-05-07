package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.EventFavoriteParam;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2018/9/25 13:41.
 */
public interface EventFavoriteParamService extends BaseService<EventFavoriteParam, String> {

    List<EventFavoriteParam> findByFavoriteId(String favoriteId);

    Map<String, String> getMapByFavoriteId(String favoriteId);

    void deleteByFavoriteId(String favoriteId);
}
