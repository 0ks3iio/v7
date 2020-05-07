package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramContrast;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/28 14:20
 */
public interface DiagramContrastService extends BaseService<DiagramContrast, String> {

    DiagramContrast getDiagramContrastByDiagramType(Integer diagramType);

    List<DiagramContrast> getAll();
}
