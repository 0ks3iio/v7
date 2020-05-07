package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.AdjustedDetail;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdjustedDetailDao extends BaseJpaRepositoryDao<AdjustedDetail, String>{

    @Query("FROM AdjustedDetail where adjustedId in ?1")
    List<AdjustedDetail> findListByAdjustedIds(String[] adjustedIds);

    @Modifying
    @Query("delete from AdjustedDetail where adjustedId in (?1)")
    void deleteByAdjustedIds(String[] adjustedIds);

    @Query("FROM AdjustedDetail where teacherId= ?1 or teacherExIds like ?2")
	List<AdjustedDetail> findByTeacherId(String teacherId, String teacherIds);

	void deleteByClassIdIn(String... classIds);

	void deleteBySubjectIdIn(String... subjectIds);
	
	void deleteByTeacherIdIn(String... teacherIds);
}
