package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.DiagramParameterSelectRepository;
import net.zdsoft.bigdata.datav.entity.DiagramParameterSelect;
import net.zdsoft.bigdata.datav.service.DiagramParameterSelectService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/9/26 17:49
 */
@Service
public class DiagramParameterSelectServiceImpl extends BaseServiceImpl<DiagramParameterSelect, String> implements DiagramParameterSelectService {

    @Resource
    private DiagramParameterSelectRepository diagramParameterSelectRepository;

    private Map<Integer, List<DiagramParameterSelect>> cache;

    @Override
    protected BaseJpaRepositoryDao<DiagramParameterSelect, String> getJpaDao() {
        return diagramParameterSelectRepository;
    }

    @Override
    protected Class<DiagramParameterSelect> getEntityClass() {
        return DiagramParameterSelect.class;
    }

    @Override
    public List<DiagramParameterSelect> getSelectsByValueType(Integer valueType) {
        Assert.notNull(valueType, "valueType can't null");
        initCache();
        //return diagramParameterSelectRepository.getDiagramParameterSelectsByValueType(valueType);
        return cache.computeIfAbsent(valueType, type-> diagramParameterSelectRepository.getDiagramParameterSelectsByValueType(valueType));
    }

    private void initCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new HashMap<>(32);
                }
            }
        }
    }
}
