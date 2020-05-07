package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopSchoolNotice;

/**
 * Created by Administrator on 2018/4/8.
 */
public interface StuDevelopSchoolNoticeService extends BaseService<StudevelopSchoolNotice,String> {

    public StudevelopSchoolNotice getSchoolNoticeByAcadyearSemesterUnitId(String acadyear,String semester , String schoolSection ,String unitId);
}
