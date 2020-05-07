package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramContrast;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

/**
 * @author shenke
 * @since 2018/9/28 14:19
 */
@Repository
public interface DiagramContrastRepository extends BaseJpaRepositoryDao<DiagramContrast, String> {


    DiagramContrast getDiagramContrastByType(Integer diagramType);
}
