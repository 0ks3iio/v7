package net.zdsoft.credit.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.entity.CreditSet;

public interface CreditSetService extends BaseService<CreditSet, String> {

    /**
     * 根据学年学期查找设置，如果取不到则取上个学期的设置，并保存
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @return
     */
    CreditSet findAndInit(String unitId, String acadyear, String semester);

    CreditSet findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester);

}
