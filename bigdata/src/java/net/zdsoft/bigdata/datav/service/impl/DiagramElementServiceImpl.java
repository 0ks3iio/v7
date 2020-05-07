package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.DiagramElementRepository;
import net.zdsoft.bigdata.datav.entity.DiagramElement;
import net.zdsoft.bigdata.datav.service.DiagramElementService;
import net.zdsoft.bigdata.datav.service.DiagramParameterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 12:49
 */
@Service("diagramElementService")
public class DiagramElementServiceImpl extends BaseServiceImpl<DiagramElement, String> implements DiagramElementService {

    @Resource
    private DiagramElementRepository diagramElementRepository;
    @Resource
    private DiagramParameterService diagramParameterService;

    @Override
    protected BaseJpaRepositoryDao<DiagramElement, String> getJpaDao() {
        return diagramElementRepository;
    }

    @Override
    protected Class<DiagramElement> getEntityClass() {
        return DiagramElement.class;
    }

    @Override
    public List<DiagramElement> getElementsByDiagramId(String diagramId) {
        return diagramElementRepository.getDiagramElementsByDiagramId(diagramId);
    }

    @Override
    public DiagramElement getElementByDiagramIdAndDiagramType(String diagramId, Integer diagramType) {
        return diagramElementRepository.getDiagramElementByDiagramIdAndDiagramType(diagramId, diagramType);
    }

    @Override
    public void deleteByDiagramId(String diagramId) {
        for (DiagramElement element : getElementsByDiagramId(diagramId)) {
            diagramParameterService.deleteByDiagramId(element.getId());
        }
        diagramElementRepository.deleteAllByDiagramId(diagramId);
    }

}
