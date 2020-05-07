package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramParameterArray;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 10:02
 */
public interface DiagramParameterArrayService extends BaseService<DiagramParameterArray, String> {

    List<DiagramParameterArray> getDiagramParameterArraysByGroupKeyAndDiagramType(String groupKey, Integer diagramType);
}
