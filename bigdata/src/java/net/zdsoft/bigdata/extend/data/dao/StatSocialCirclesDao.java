package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.StatSocialCircles;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface StatSocialCirclesDao extends BaseJpaRepositoryDao<StatSocialCircles, String> {

    List<StatSocialCircles> findAllByOwnerId(String ownerId);

}
