package net.zdsoft.studevelop.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StuDevelopCateGoryDao extends BaseJpaRepositoryDao<StuDevelopCateGory,String>{
	@Query("From StuDevelopCateGory where subjectId = ?1 order by creationTime")
	public List<StuDevelopCateGory> findListBySubjectId(String subjectId);
	@Query("From StuDevelopCateGory where subjectId in (?1) order by creationTime")
	public List<StuDevelopCateGory> findListBySubjectIdIn(String[] subjectIds);
	@Modifying
	@Query("delete from StuDevelopCateGory where subjectId in (?1)")
	public void deleteBySubjectIds(String[] subjectIds);
}
