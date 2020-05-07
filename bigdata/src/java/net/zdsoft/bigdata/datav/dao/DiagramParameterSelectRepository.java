package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramParameterSelect;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 17:49
 */
@Repository
public interface DiagramParameterSelectRepository extends BaseJpaRepositoryDao<DiagramParameterSelect, String> {

    List<DiagramParameterSelect> getDiagramParameterSelectsByValueType(Integer valueType);
}
