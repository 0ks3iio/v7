package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.CockpitStyle;

import java.util.Optional;

/**
 * @author shenke
 * @since 2018/8/7 上午11:14
 */
public interface CockpitStyleService extends BaseService<CockpitStyle, String> {

    Optional<CockpitStyle> getByCockpitId(String cockpitId);
}
