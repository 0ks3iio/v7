package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramParameterSelect;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 17:48
 */
public interface DiagramParameterSelectService extends BaseService<DiagramParameterSelect, String> {

    List<DiagramParameterSelect> getSelectsByValueType(Integer valueType);
}
