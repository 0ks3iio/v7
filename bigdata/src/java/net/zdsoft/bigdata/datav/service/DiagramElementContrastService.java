package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramElementContrast;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/17 11:29
 */
public interface DiagramElementContrastService extends BaseService<DiagramElementContrast, String> {

    List<DiagramElementContrast> getElementContrastsByRootDiagramType(Integer rootDiagramType);

    DiagramElementContrast getElementByElementDiagramType(Integer elementDiagramType);
}
