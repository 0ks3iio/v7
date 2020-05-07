package net.zdsoft.teaeaxam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;

public interface TeaexamRegisterInfoDao extends BaseJpaRepositoryDao<TeaexamRegisterInfo, String>{
	
	@Query("SELECT DISTINCT roomNo From TeaexamRegisterInfo where examId = ?1 and subjectInfoId in (?2)")
	public List<String> findRoomNoBySubInfoId(String examId, String[] subIds);
	
	@Query("From TeaexamRegisterInfo where examId = ?1 and subjectInfoId=?2 and teacherId in (?2)")
	public List<TeaexamRegisterInfo> findBySubInfoIdTeaIds(String examId, String subInfoId, String[] teaIds);

	@Query("From TeaexamRegisterInfo where teacherId = ?1 and examId in (?2)")
	public List<TeaexamRegisterInfo> findByTeacherIdAndExamIdIn(
			String teacherId, String[] examIds);
	
	@Query("From TeaexamRegisterInfo where examId in (?1)")
	public List<TeaexamRegisterInfo> findByExamIdIn(String[] examIds);
	
	@Query("From TeaexamRegisterInfo where status=?1 and examId in (?2) order by schoolId, creationTime")
	public List<TeaexamRegisterInfo> findByStatusAndExamIdIn(int status,
			String[] examIds);
	
	/**
	 * 查找已安排考场的报名数据
	 * @param status
	 * @param subInfoId
	 * @return
	 */
	@Query("From TeaexamRegisterInfo where status=?1 and subjectInfoId = ?2 and seatNo is not null")
	public List<TeaexamRegisterInfo> findByStatusAndSubInfoId(int status, String subInfoId);
	
	@Query("From TeaexamRegisterInfo where examId=?1 and status=2 and cardNo is null")
	public List<TeaexamRegisterInfo> findByExamIdWithNoCard(String examId);
	
	@Query("select count(*) From TeaexamRegisterInfo where status=2 and examId = ?1 and seatNo is not null")
	public int findCountByArrange(String examId);
	
	@Query("select count(*) From TeaexamRegisterInfo where examId = ?1 and status=?2 ")
	public int findCountByStatus(String examId, int status);
	
	@Modifying
	@Query("UPDATE TeaexamRegisterInfo SET locationId='', roomNo='', seatNo='' where examId = ?1 and subjectInfoId=?2 ")
	public void clearSetByExamIdSubInfoId(String examId, String subInfoId);
	
	@Modifying
	@Query("UPDATE TeaexamRegisterInfo SET locationId='', roomNo='', seatNo='' where examId = ?1")
	public void clearSetByExamId(String examId);
	
	@Query("SELECT max(roomNo) From TeaexamRegisterInfo where examId = ?1 and status=2 and seatNo is not null")
	public String findRoomNoByExamId(String examId);
	
	@Query("SELECT DISTINCT teacherId From TeaexamRegisterInfo where examId = ?1 and status=2")
	public List<String> findByExamIdGroupTeacherId(String examId);
	
	@Query("SELECT roomNo,locationId, count(*) From TeaexamRegisterInfo where examId=?1 and subjectInfoId = ?2 and status=2 and seatNo is not null group by roomNo,locationId")
	public List<Object[]> findRoomCountByExamIdSubInfoId(String examId, String subInfoId);
	
	@Query("SELECT count(*) From TeaexamRegisterInfo where examId=?1 and roomNo = ?2 and status=2 and seatNo is not null")
	public int findCountByExamIdRoomNo(String examId, String roomNo);
	
	@Query("From TeaexamRegisterInfo where examId=?1 and status=2 and seatNo is not null and roomNo in (?2) order by roomNo,locationId")
	public List<TeaexamRegisterInfo> findByExamIdRoomNos(String examId, String[] roomNos);
	@Query("From TeaexamRegisterInfo where teacherId = ?1 and status=2")
	public List<TeaexamRegisterInfo> findByTeacherId(String teacherId);
}
