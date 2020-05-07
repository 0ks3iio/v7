package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.DiagramElementContrastRepository;
import net.zdsoft.bigdata.datav.entity.DiagramElementContrast;
import net.zdsoft.bigdata.datav.service.DiagramElementContrastService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/10/17 11:35
 */
@Service
public class DiagramElementContrastServiceImpl extends BaseServiceImpl<DiagramElementContrast, String>
        implements DiagramElementContrastService {

    @Resource
    private DiagramElementContrastRepository diagramElementContrastRepository;

    @Override
    protected BaseJpaRepositoryDao<DiagramElementContrast, String> getJpaDao() {
        return diagramElementContrastRepository;
    }

    @Override
    protected Class<DiagramElementContrast> getEntityClass() {
        return DiagramElementContrast.class;
    }

    @Override
    public List<DiagramElementContrast> getElementContrastsByRootDiagramType(Integer rootDiagramType) {
        return diagramElementContrastRepository.getDiagramElementContrastByRootDiagramType(rootDiagramType);
    }

    @Override
    public DiagramElementContrast getElementByElementDiagramType(Integer elementDiagramType) {
        return diagramElementContrastRepository.getDiagramElementContrastByElementDiagramType(elementDiagramType);
    }
}
