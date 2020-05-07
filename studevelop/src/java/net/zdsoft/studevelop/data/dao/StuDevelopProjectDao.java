package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopProject;

public interface StuDevelopProjectDao extends BaseJpaRepositoryDao<StuDevelopProject,String>{
    @Modifying
	@Query("delete From StuDevelopProject where id in (?1)")
	public void deleteByIds(String[] ids);
    @Query("From StuDevelopProject where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4 order by creationTime")
    public List<StuDevelopProject> stuDevelopProjectList(String unitId,
			String acadyear, String semester, String gradeId);
    @Query("From StuDevelopProject where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId in (?4) order by creationTime")
    public List<StuDevelopProject> stuDevelopProjectListByGradeIds(String unitId,
			String acadyear, String semester, String[] gradeIds);
}
