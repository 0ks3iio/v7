package net.zdsoft.scoremanage.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.HwReinstate;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:26
 */
public interface HwReinstateDao extends BaseJpaRepositoryDao<HwReinstate, String> {

    List<HwReinstate> findByUnitIdAndAcadyearAndSemesterAndStudentId(String unitId, String acadyear, String semester, String studentId);

    HwReinstate findByUnitIdAndAcadyearAndSemesterAndStudentIdAndExamId(String unitId, String acadyear, String semester, String studentId, String examId);

    List<HwReinstate> findByUnitIdAndAcadyearAndSemesterAndGradeIdAndExamId(String unitId, String acadyear, String semester, String gradeId, String examId);
}
