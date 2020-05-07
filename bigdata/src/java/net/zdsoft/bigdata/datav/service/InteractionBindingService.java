package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.InteractionBinding;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:37
 */
public interface InteractionBindingService extends BaseService<InteractionBinding, String> {

    List<InteractionBinding> getBindingsByElementId(String elementId);

    /**
     * 获取某个大屏所有交互参数的关系
     * @param screenId
     * @return
     */
    List<InteractionBinding> getBindingsByScreenId(String screenId);

    void deleteByElementId(String elementId);

    void deleteByElementIds(String[] elementIds);
}
