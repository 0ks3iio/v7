package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramElementContrast;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/17 11:27
 */
@Repository
public interface DiagramElementContrastRepository extends BaseJpaRepositoryDao<DiagramElementContrast, String> {

    List<DiagramElementContrast> getDiagramElementContrastByRootDiagramType(Integer rootDiagramType);

    DiagramElementContrast getDiagramElementContrastByElementDiagramType(Integer elementDiagramType);
}
