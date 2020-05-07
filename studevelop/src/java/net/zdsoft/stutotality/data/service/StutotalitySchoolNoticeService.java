package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalitySchoolNotice;

import java.util.List;

public interface StutotalitySchoolNoticeService extends BaseService<StutotalitySchoolNotice,String> {

	public StutotalitySchoolNotice findByUnitIdAndAcadyearAndSemesterAndGradeId(String unitId, String acadyear,String semester, String gradeId );

	public List<StutotalitySchoolNotice> findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester);

	public void deleteByUnitId(String unitId);

}
