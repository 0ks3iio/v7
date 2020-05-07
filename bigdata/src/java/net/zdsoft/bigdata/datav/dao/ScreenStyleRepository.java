package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.ScreenStyle;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

/**
 * @author shenke
 * @since 2018/10/16 18:41
 */
@Repository
public interface ScreenStyleRepository extends BaseJpaRepositoryDao<ScreenStyle, String> {

    ScreenStyle getScreenStyleByScreenId(String screenId);

    void deleteAllByScreenIdIn(String[] screenIds);
}
