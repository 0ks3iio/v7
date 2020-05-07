package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalitySchoolNotice;

import java.util.List;

public interface StutotalitySchoolNoticeDao extends BaseJpaRepositoryDao<StutotalitySchoolNotice, String> {

	public StutotalitySchoolNotice findByUnitIdAndAcadyearAndSemesterAndGradeId(String unitId, String acadyear,String semester, String gradeId );

	public List<StutotalitySchoolNotice> findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester);

	public void deleteByUnitId(String unitId);

}
