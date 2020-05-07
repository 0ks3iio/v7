package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramParameterGroup;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 17:03
 */
public interface DiagramParameterGroupService extends BaseService<DiagramParameterGroup, String> {

    List<DiagramParameterGroup> getGroupsByDiagramType(Integer diagramType);
}
