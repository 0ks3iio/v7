package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityCode;

import java.util.List;

public interface StutotalityCodeService extends BaseService<StutotalityCode,String> {

    List<StutotalityCode> findListByUnitIdAndTypeAndNameIn(String unitId, Integer type, String[] names);
}
