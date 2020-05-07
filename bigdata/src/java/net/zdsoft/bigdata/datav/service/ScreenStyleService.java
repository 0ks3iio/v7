package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.ScreenStyle;

/**
 * @author shenke
 * @since 2018/10/16 18:41
 */
public interface ScreenStyleService extends BaseService<ScreenStyle, String> {

    ScreenStyle getByScreenId(String screenId);

    void bacthDelete(String[] screeenIds);
}
