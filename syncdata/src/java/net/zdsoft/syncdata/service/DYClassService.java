package net.zdsoft.syncdata.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.syncdata.entity.DYClazz;

public interface DYClassService extends BaseService<DYClazz, String> {

	List<DYClazz> saveAllEntitys(DYClazz... array);

}
