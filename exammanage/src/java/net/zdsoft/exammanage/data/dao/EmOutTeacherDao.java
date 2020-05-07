package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmOutTeacher;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface EmOutTeacherDao extends BaseJpaRepositoryDao<EmOutTeacher, String> {

    public List<EmOutTeacher> findByExamIdAndSchoolId(String examId, String unitId);

    public List<EmOutTeacher> findBySchoolId(String unitId);

}
