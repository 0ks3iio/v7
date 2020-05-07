package net.zdsoft.gkelective.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkStuConversion;

public interface GkStuConversionService extends BaseService<GkStuConversion, String>{

	void saveAllEntitys(GkStuConversion... array);

}
