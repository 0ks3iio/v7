package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmSubGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmSubGroupDao extends BaseJpaRepositoryDao<EmSubGroup, String> {

    public List<EmSubGroup> findByExamId(String examId);

    public List<EmSubGroup> findByExamIdAndSubjectId(String examId, String subId);

    public void deleteByExamIdAndUnitId(String examId, String unitId);

//	public EmSubGroup findByExamIdAndSubIdAndSubType(String examId, String subId, String subType);

    @Query("From EmSubGroup where examId = ?1 and subType = ?2 and subjectId like '%'||?3||'%'")
    public List<EmSubGroup> findByExamIdAndSubIdAndSubType(String examId, String subType, String subId);
}
