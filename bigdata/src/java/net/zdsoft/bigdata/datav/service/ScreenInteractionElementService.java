package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:41
 */
public interface ScreenInteractionElementService extends BaseService<ScreenInteractionElement, String> {

    List<ScreenInteractionElement> getScreenDefaultInteractionItems(String screenId);

    ScreenInteractionElement getByBindKeyAndScreenId(String bindKey, String screenId);

    void deleteByScreenIdAndBindKey(String screenId, String bindKey);

    void deleteByScreenId(String screenId);
}
