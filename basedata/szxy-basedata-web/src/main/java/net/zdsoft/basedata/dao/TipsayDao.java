package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TipsayDao extends BaseJpaRepositoryDao<Tipsay, String>{
	static String sqlOreder="order by weekOfWorktime desc,dayOfWeek desc";
	
	@Modifying
	@Query("update Tipsay set isDeleted=1 where id = ?1 ")
	public void updateById(String tipsayId);

	@Query("from Tipsay where schoolId=?1 and acadyear=?2 and semester=?3 and isDeleted in (?4) " +sqlOreder)
	public List<Tipsay> findBySemester(String schoolId, String acadyear,
			Integer semester, Integer[] isDeleteds);
	@Query("from Tipsay where schoolId=?1 and acadyear=?2 and semester=?3 and weekOfWorktime=?4 and isDeleted in (?5) "+sqlOreder)
	public List<Tipsay> findByWeekOfWorkTime(String schoolId, String acadyear,
			Integer semester, Integer weekOfWorkTime, Integer[] isDeleteds);
	@Query("from Tipsay where schoolId=?1 and acadyear=?2 and semester=?3 and (teacherId =?5 or newTeacherId=?5 or  teacherExIds like ?6 ) and isDeleted in (?4) "+sqlOreder)
	public List<Tipsay> findBySemesterTeacher(String schoolId, String acadyear, Integer semester, Integer[] deletedArr,
			String teacherId,String tIdEx);
	@Query("from Tipsay where schoolId=?1 and acadyear=?2 and semester=?3 and weekOfWorktime=?4  and (teacherId =?6 or newTeacherId=?6 or  teacherExIds like ?7 )  and isDeleted in (?5) "+sqlOreder)
	public List<Tipsay> findByWeekOfWorkTimeTeacher(String schoolId, String acadyear, Integer semester,
			Integer weekOfWorkTime, Integer[] deletedArr, String teacherId,String tIdEx);

	@Query("from Tipsay where teacherId= ?1 or newTeacherId = ?1 or teacherExIds like ?2")
	public List<Tipsay> findByTeacherId(String teacherId, String teacherIds);

	@Modifying
	@Query("update Tipsay set isDeleted=1 where classId = ?1 and isDeleted=0")
	public void deleteByClassId(String classId);

	@Query("from Tipsay where subjectId = ?1 and isDeleted=0")
	public List<Tipsay> findBySubjectId(String subjectId);
}
