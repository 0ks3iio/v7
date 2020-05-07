package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.DiagramParameterGroupRepository;
import net.zdsoft.bigdata.datav.entity.DiagramParameterGroup;
import net.zdsoft.bigdata.datav.service.DiagramParameterGroupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/9/26 17:03
 */
@Service("diagramParameterGroupService")
public class DiagramParameterGroupServiceImpl extends BaseServiceImpl<DiagramParameterGroup, String> implements DiagramParameterGroupService {

    private Map<Integer, List<DiagramParameterGroup>> cache;

    @Resource
    private DiagramParameterGroupRepository diagramParameterGroupRepository;

    @Override
    protected BaseJpaRepositoryDao<DiagramParameterGroup, String> getJpaDao() {
        return diagramParameterGroupRepository;
    }

    @Override
    protected Class<DiagramParameterGroup> getEntityClass() {
        return DiagramParameterGroup.class;
    }

    @Override
    public List<DiagramParameterGroup> getGroupsByDiagramType(Integer diagramType) {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new HashMap<>(32);
                }
            }
        }
        return cache.computeIfAbsent(diagramType, k-> diagramParameterGroupRepository.getDiagramParameterGroupsByDiagramType(k));
        //return diagramParameterGroupRepository.getDiagramParameterGroupsByDiagramType(diagramType);
    }
}
