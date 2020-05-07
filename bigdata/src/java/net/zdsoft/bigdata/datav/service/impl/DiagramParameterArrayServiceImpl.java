package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.DiagramParameterArrayRepository;
import net.zdsoft.bigdata.datav.entity.DiagramParameterArray;
import net.zdsoft.bigdata.datav.service.DiagramParameterArrayService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 10:02
 */
@Service
public class DiagramParameterArrayServiceImpl extends BaseServiceImpl<DiagramParameterArray, String> implements DiagramParameterArrayService {

    @Resource
    private DiagramParameterArrayRepository diagramParameterArrayRepository;

    @Override
    protected BaseJpaRepositoryDao<DiagramParameterArray, String> getJpaDao() {
        return diagramParameterArrayRepository;
    }

    @Override
    protected Class<DiagramParameterArray> getEntityClass() {
        return DiagramParameterArray.class;
    }

    @Override
    public List<DiagramParameterArray> getDiagramParameterArraysByGroupKeyAndDiagramType(String groupKey, Integer diagramType) {
        return diagramParameterArrayRepository.getDiagramParameterArraysByGroupKeyAndDiagramType(groupKey, diagramType);
    }
}
