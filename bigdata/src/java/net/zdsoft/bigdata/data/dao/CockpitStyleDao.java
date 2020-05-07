package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.CockpitStyle;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.Optional;

/**
 * @author shenke
 * @since 2018/8/7 上午11:12
 */
public interface CockpitStyleDao extends BaseJpaRepositoryDao<CockpitStyle, String> {

    Optional<CockpitStyle> getByCockpitId(String cockpitId);
}
