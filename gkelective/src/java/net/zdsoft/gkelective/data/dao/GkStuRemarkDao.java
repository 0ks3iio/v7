package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkStuRemark;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkStuRemarkDao extends BaseJpaRepositoryDao<GkStuRemark, String> {
	
	@Modifying
	@Query("delete from GkStuRemark where subjectArrangeId=?1 AND type=?2 AND studentId in (?3)")
	public void deleteByStudentIdsAndArrangeId(String subjectArrangeId, String type, String[] studentIds);
	
	@Query("From GkStuRemark where subjectArrangeId=?1 AND type=?2 AND studentId in (?3)")
	public List<GkStuRemark> findByStudentIds(String subjectArrangeId, String type, String[] studentIds);
	
	@Modifying
	@Query("delete from GkStuRemark where subjectArrangeId=?1 AND type=?2 AND subjectId in (?3)")
	public void deleteBySubjectIds(String arrangeId, String typeScore, String[] subjectIds);
	
	@Modifying
	@Query("delete from GkStuRemark where subjectArrangeId=?1")
	public void deleteByArrangeId(String subjectArrangeId);
	
	@Query("From GkStuRemark where subjectArrangeId=?1 AND type=?2 ")
	public List<GkStuRemark> findByArrangeIdType(String subjectArrangeId, String type);

	@Query("From GkStuRemark where subjectArrangeId=?1 AND type in (?2) AND studentId in (?3)")
	public List<GkStuRemark> findByStudentIds(String arrangeId, String[] types, String[] studentIds);
}
