package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.InteractionActive;
import net.zdsoft.bigdata.datav.entity.Active;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午11:16
 */
public interface InteractionActiveService extends BaseService<InteractionActive, String> {

    Active isActice(String elementId);

    void activeForElement(String elementId);

    void cancelActive(String screenId, String elementId);

    void deleteByElementIds(String[] elementIds);

    List<InteractionActive> getActivitiesByScreenId(String screenId);
}
