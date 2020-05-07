package net.zdsoft.qulity.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.qulity.data.entity.QualitStuRecover;

import java.util.List;

public interface QualitStuRecoverService extends BaseService<QualitStuRecover,String> {

    List<QualitStuRecover> findListByUnitIDAndAcadyear(String unitId,String acadyear);
}
