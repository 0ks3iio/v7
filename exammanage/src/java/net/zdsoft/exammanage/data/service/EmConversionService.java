package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmConversion;

import java.util.List;

public interface EmConversionService extends BaseService<EmConversion, String> {

    List<EmConversion> findByUnitId(String unitId);

    void saveAllEntity(String unitId, List<EmConversion> insertlist);

}
