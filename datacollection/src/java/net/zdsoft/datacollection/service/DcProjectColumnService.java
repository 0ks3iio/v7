package net.zdsoft.datacollection.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datacollection.entity.DcProjectColumn;

public interface DcProjectColumnService extends BaseService<DcProjectColumn, String> {

	void delete(String projectId, String unitId);

}
