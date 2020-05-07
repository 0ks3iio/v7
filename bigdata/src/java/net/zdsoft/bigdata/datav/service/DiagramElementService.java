package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramElement;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 12:49
 */
public interface DiagramElementService extends BaseService<DiagramElement, String> {

    List<DiagramElement> getElementsByDiagramId(String diagramId);

    /**
     * 获取特定的组件
     * @param diagramId root
     * @param diagramType  type
     * @return
     */
    DiagramElement getElementByDiagramIdAndDiagramType(String diagramId, Integer diagramType);

    void deleteByDiagramId(String diagramId);


}
