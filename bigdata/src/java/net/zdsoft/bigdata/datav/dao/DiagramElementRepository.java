package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramElement;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 12:48
 */
@Repository
public interface DiagramElementRepository extends BaseJpaRepositoryDao<DiagramElement, String> {

    List<DiagramElement> getDiagramElementsByDiagramId(String diagramId);

    DiagramElement getDiagramElementByDiagramIdAndDiagramType(String diagramId, Integer diagramType);

    void deleteAllByDiagramId(String diagramId);

}
