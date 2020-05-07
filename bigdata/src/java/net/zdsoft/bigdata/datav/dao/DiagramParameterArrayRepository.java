package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramParameterArray;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 10:01
 */
@Repository
public interface DiagramParameterArrayRepository extends BaseJpaRepositoryDao<DiagramParameterArray, String> {

    List<DiagramParameterArray> getDiagramParameterArraysByGroupKeyAndDiagramType(String groupName, Integer diagramType);
}
