package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramParameterGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 17:02
 */
@Repository
public interface DiagramParameterGroupRepository extends BaseJpaRepositoryDao<DiagramParameterGroup, String> {

    @Query(
            value = "from DiagramParameterGroup where diagramType=?1 order by orderNumber"
    )
    List<DiagramParameterGroup> getDiagramParameterGroupsByDiagramType(Integer diagramType);
}
