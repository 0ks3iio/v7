package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.DiagramContrastRepository;
import net.zdsoft.bigdata.datav.entity.DiagramContrast;
import net.zdsoft.bigdata.datav.service.DiagramContrastService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/9/28 14:21
 */
@Service
public class DiagramContrastServiceImpl extends BaseServiceImpl<DiagramContrast, String> implements DiagramContrastService {

    @Resource
    private DiagramContrastRepository diagramContrastRepository;

    @Override
    protected BaseJpaRepositoryDao<DiagramContrast, String> getJpaDao() {
        return diagramContrastRepository;
    }

    @Override
    protected Class<DiagramContrast> getEntityClass() {
        return DiagramContrast.class;
    }

    @Override
    public DiagramContrast getDiagramContrastByDiagramType(Integer diagramType) {
        return diagramContrastRepository.getDiagramContrastByType(diagramType);
    }

    @Override
    public List<DiagramContrast> getAll() {
        return diagramContrastRepository.findAll();
    }
}
