package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.Colors;
import net.zdsoft.bigdata.datav.dao.DiagramParameterRepository;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.entity.DiagramParameterArray;
import net.zdsoft.bigdata.datav.entity.DiagramParameterGroup;
import net.zdsoft.bigdata.datav.enu.ArrayEnum;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.Root;
import net.zdsoft.bigdata.datav.model.QDiagram;
import net.zdsoft.bigdata.datav.model.QDiagramParameter;
import net.zdsoft.bigdata.datav.service.DiagramParameterArrayService;
import net.zdsoft.bigdata.datav.service.DiagramParameterGroupService;
import net.zdsoft.bigdata.datav.service.DiagramParameterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import java.util.*;

/**
 * @author shenke
 * @since 2018/9/26 17:00
 */
@Service
public class DiagramParameterServiceImpl extends BaseServiceImpl<DiagramParameter, String> implements DiagramParameterService {

    @Resource
    private DiagramParameterRepository diagramParameterRepository;
    @Resource
    private DiagramParameterGroupService diagramParameterGroupService;
    @Resource
    private DiagramParameterArrayService diagramParameterArrayService;

    @Override
    protected BaseJpaRepositoryDao<DiagramParameter, String> getJpaDao() {
        return diagramParameterRepository;
    }

    @Override
    protected Class<DiagramParameter> getEntityClass() {
        return DiagramParameter.class;
    }

    @Override
    public List<DiagramParameter> getDiagramParametersByDiagramId(String diagramId) {
        Assert.notNull(diagramId, "diagramId can't null");
        return diagramParameterRepository.getDiagramParametersByDiagramId(diagramId);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public List<DiagramParameter> getOrDefault(String diagramId, Integer diagramType) {
        List<DiagramParameter> diagramParameters = getDiagramParametersByDiagramId(diagramId);

        Map<String, DiagramParameter> diagramParameterMap = new HashMap<>();
        for (DiagramParameter diagramParameter : diagramParameters) {
            diagramParameterMap.put(diagramParameter.getGroupKey() + diagramParameter.getArrayName() + diagramParameter.getKey(),
                    diagramParameter);
        }

        List<DiagramParameterGroup> diagramParameterGroups = diagramParameterGroupService.getGroupsByDiagramType(diagramType);
        for (DiagramParameterGroup group : diagramParameterGroups) {
            if (Root.ROOT.getRoot().equals(group.getRoot())) {
                if (ArrayEnum.TRUE.getArray().equals(group.getArray())) {
                    //先判定用户有没有设置系列
                    if (diagramParameterMap.keySet().stream().filter(Objects::nonNull)
                            .anyMatch(k->k.startsWith(group.getGroupKey() + group.getArrayNamePrefix()))) {
                        //do nothing
                    } else {
                        List<DiagramParameterArray> diagramParameterArrays = diagramParameterArrayService.getDiagramParameterArraysByGroupKeyAndDiagramType(group.getGroupKey(), diagramType);
                        if (!diagramParameterArrays.isEmpty()) {
                            Set<String> used = new HashSet<>(group.getDefaultArraySize());
                            DiagramParameter colorTemp = null;
                            for (int i = 0, length = group.getDefaultArraySize(); i < length; i++) {
                                for (DiagramParameterArray diagramParameterArray : diagramParameterArrays) {
                                    if ((colorTemp = diagramParameterMap.get(group.getGroupKey() + group.getArrayNamePrefix() + (i + 1) + diagramParameterArray.getKey())) == null) {
                                        DiagramParameter diagramParameter = new DiagramParameter();
                                        if ("color".equals(diagramParameterArray.getKey())) {
                                            if (i == 0) {
                                                diagramParameter.setValue(diagramParameterArray.getDefaultValue());
                                                used.add(diagramParameterArray.getDefaultValue());
                                            } else {
                                                String color = Colors.colors.stream().filter(c -> !used.contains(c)).findAny().orElse("#b9396d");
                                                used.add(color);
                                                diagramParameter.setValue(color);
                                            }
                                        } else if ("seriesName".equals(diagramParameterArray.getKey())) {
                                            diagramParameter.setValue(String.valueOf(i + 1));
                                        } else if ("seriesShowName".equals(diagramParameterArray.getKey())) {
                                            diagramParameter.setValue(group.getArrayNamePrefix() + (i + 1));
                                        } else if ("seriesType".equals(diagramParameterArray.getKey())
                                                && DiagramEnum.COMPOSITE_BAR_LINE.getType().equals(diagramType)) {
                                            if (i==1) {
                                                diagramParameter.setValue("line");
                                            } else {
                                                diagramParameter.setValue(diagramParameterArray.getDefaultValue());
                                            }
                                        }
                                        else {
                                            diagramParameter.setValue(diagramParameterArray.getDefaultValue());
                                        }
                                        diagramParameter.setGroupKey(group.getGroupKey());
                                        diagramParameter.setArrayName(group.getArrayNamePrefix() + (i + 1));
                                        diagramParameter.setKey(diagramParameterArray.getKey());
                                        diagramParameters.add(diagramParameter);
                                    } else {
                                        if ("color".equalsIgnoreCase(diagramParameterArray.getKey())) {
                                            used.add(colorTemp.getValue());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (diagramParameterMap.get(group.getGroupKey() + null + group.getKey()) == null)
                {
                    DiagramParameter diagramParameter = new DiagramParameter();
                    diagramParameter.setGroupKey(group.getGroupKey());
                    diagramParameter.setKey(group.getKey());
                    diagramParameter.setValue(group.getDefaultValue());
                    diagramParameters.add(diagramParameter);
                }
            }
        }

        saveAll(diagramParameters.stream().filter(p -> Objects.isNull(p.getDiagramId())).map(p->{
            p.setId(UuidUtils.generateUuid());
            p.setDiagramId(diagramId);
            return p;
        }).toArray(DiagramParameter[]::new));

        return diagramParameters;
    }

    @Override
    public List<DiagramParameter> getDiagramBasicById(String diagramId) {

        return diagramParameterRepository.getBaiscParameters(diagramId);
    }


    @Override
    public void updateDiagramParameter(String diagramId, String key, String value) {
        diagramParameterRepository.updateDiagramParameterValueByIdAndKey(diagramId, value, key);
    }

    @Override
    public void updateDiagramParameter(String diagramId, String key, String arrayName, String value) {
        diagramParameterRepository.updateDiagramParameterValueByIdAndKeyAndArrayName(diagramId, value, key, arrayName);
    }

    @Override
    public void deleteByDiagramId(String diagramId) {
        diagramParameterRepository.deleteByDiagramId(diagramId);
    }

    @Override
    public void deleteByDiagramIdAndArrayName(String diagramId, String arrayName) {
        diagramParameterRepository.deleteByDiagramIdAndArrayName(diagramId, arrayName);
    }

    @Override
    public Integer getMaxLevel(String[] diagramId) {
        return diagramParameterRepository.getMaxLevel(diagramId);
    }

    @Override
    public List<DiagramParameter> getLevelParameters(String screenId) {
        return diagramParameterRepository.findAll((Specification<DiagramParameter>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<DiagramParameter, Diagram> join = root.join("diagram");
            return criteriaQuery.where(
                    criteriaBuilder.equal(join.get(QDiagram.screenId), screenId),
                    criteriaBuilder.equal(root.get(QDiagramParameter.key), "level")
            ).getRestriction();
        });
    }

    @Override
    public DiagramParameter findByDiagramIdAndKeyAndArrayName(String diagramId, String key,String arrayName) {
        return diagramParameterRepository.findByDiagramIdAndKeyAndArrayName(diagramId,key,arrayName);
    }

    @Override
    public DiagramParameter findByDiagramIdAndKey(String diagramId, String key) {
        return diagramParameterRepository.findByDiagramIdAndKey(diagramId,key);
    }
}
