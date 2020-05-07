package net.zdsoft.desktop.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.entity.FunctionAreaTemplate;

/**
 * @author shenke
 */
public interface FunctionAreaTemplateService extends BaseService<FunctionAreaTemplate, String> {

	void save(FunctionAreaTemplate template);

}
