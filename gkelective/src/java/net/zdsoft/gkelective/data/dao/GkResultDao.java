package net.zdsoft.gkelective.data.dao;

import java.util.List;
import java.util.Set;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkResult;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkResultDao extends BaseJpaRepositoryDao<GkResult, String> {

    @Query("select t1 from GkResult t1,Student t2 where t1.studentId=t2.id and t1.subjectArrangeId = ?1 order by t2.classId,t1.studentId,t1.subjectId")
    public List<GkResult> findByGkId(String gkId);

    @Modifying
    @Query("delete GkResult where subjectArrangeId = ?1 and studentId in (?2)")
    public void deleteByClassIds(String arrangeId, String[] stuIds);

    @Query("select COUNT(DISTINCT studentId) from GkResult where subjectArrangeId = ?1 and studentId in (?2)")
    public Integer countIsChosenByArrangeId(String arrangeId, String[] studentIds);

    @Query("select subjectId from GkResult where studentId=?1 and subjectArrangeId = ?2")
    public List<String> findCoursesByStuId(String stuId, String arrangeId);

    @Modifying
    @Query("delete from GkResult where subjectArrangeId = ?1 and studentId=?2")
    public void deleteByArrangeIdAndStudentId(String arrangeId, String studentId);

    @Query("From GkResult where studentId in (?1) and subjectArrangeId = ?2 order by id")
    public List<GkResult> findGkByStuId(String[] stuIds, String arrangeId);

    @Query("select distinct studentId from GkResult where subjectArrangeId =?1")
    public Set<String> findStudentIdsByArrangeId(String arrangeId);
    
    @Query("From GkResult where subjectArrangeId = ?1 and subjectId in (?2)")
	public List<GkResult> findBySubjectArrangeIdAndSubjectId(String arrangeId,
			String... subjectId);
    
    @Query("From GkResult where subjectArrangeId = ?1 and subjectId in (?2) and studentId in (?3)")
	public List<GkResult> findBySubjectArrangeIdAndSubjectIdStuIds(String arrangeId,String[] subjectId,String[] studentIds);
    @Modifying
    @Query("delete from GkResult where id in (?1)")
	public void deleteByIds(String[] ids);
    @Modifying
    @Query("delete from GkResult where subjectArrangeId in (?1)")
	public void deleteByArrangeIdIn(String[] ids);
    
    @Query("from GkResult where subjectArrangeId =?1 order by subjectId ")
    public List<GkResult> findByArrangeId(String arrangeId);
}
