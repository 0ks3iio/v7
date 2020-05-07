package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.InteractionActive;
import net.zdsoft.bigdata.datav.entity.Active;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author shenke
 * @since 2018/10/26 上午10:03
 */
@Repository
public interface InteractionActiveRepository extends BaseJpaRepositoryDao<InteractionActive, String> {

    @Query(
            value = "select active from InteractionActive where elementId=?1"
    )
    Active isActive(String elementId);

    void  deleteByElementId(String elementId);

    void deleteByElementIdIn(String[] elementIds);
}
