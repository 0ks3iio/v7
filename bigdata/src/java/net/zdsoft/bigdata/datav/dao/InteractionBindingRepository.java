package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.InteractionBinding;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:35
 */
@Repository
public interface InteractionBindingRepository extends BaseJpaRepositoryDao<InteractionBinding, String> {

    List<InteractionBinding> streamAllByElementId(String elementId);

    /**
     *
     * @param screenId
     * @return
     */
    @Query(
            value = "select * from bg_interaction_binding where element_id in (" +
                    " select id from bg_diagram where screen_id=?1" +
                    " union " +
                    " select id from bg_diagram_element where diagram_id in (" +
                    "   select id from bg_diagram where screen_id=?1" +
                    "   )" +
                    ")",
            nativeQuery = true
    )
    List<InteractionBinding> getBindingsByScreenId(String screenId);

    void deleteByElementId(String elementId);

    void deleteByElementIdIn(String[] elementIds);
}
