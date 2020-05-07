package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:40
 */
@Repository
public interface ScreenInteractionElementRepository extends BaseJpaRepositoryDao<ScreenInteractionElement, String> {

    List<ScreenInteractionElement> streamAllByScreenId(String screenId);

    ScreenInteractionElement getByBindKeyAndScreenId(String bindKey, String screenId);

    void deleteByScreenIdAndBindKey(String screenId, String bindKey);

    void deleteByScreenId(String screenId);
}
