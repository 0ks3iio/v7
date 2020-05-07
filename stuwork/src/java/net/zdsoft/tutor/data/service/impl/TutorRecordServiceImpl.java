package net.zdsoft.tutor.data.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.dao.TutorRecordDao;
import net.zdsoft.tutor.data.entity.TutorRecord;
import net.zdsoft.tutor.data.service.TutorRecordService;

/**
 * @author yangsj  2017年9月11日下午8:25:55
 */
@Service
public class TutorRecordServiceImpl extends BaseServiceImpl<TutorRecord, String> implements TutorRecordService {
    
	@Autowired
	private TutorRecordDao tutorRecordDao;
	
	@Override
	protected BaseJpaRepositoryDao<TutorRecord, String> getJpaDao() {
		// TODO Auto-generated method stub
		return tutorRecordDao;
	}

	@Override
	protected Class<TutorRecord> getEntityClass() {
		// TODO Auto-generated method stub
		return TutorRecord.class;
	}

	@Override
	public List<TutorRecord> findByTeacherIds(List<String> teacherIds) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findByTeacherIds(teacherIds);
	}

	@Override
	public List<TutorRecord> findByStudentIds(String... studentIds) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findByStudentIds(studentIds);
	}

	@Override
	public void deleteByStudentIds(List<String> studentIds) {
		// TODO Auto-generated method stub
		tutorRecordDao.deleteByStudentIds(studentIds);
	}

	@Override
	public List<TutorRecord> findByUIdAndSId(String unitId, String sId) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findByUIdAndSId(unitId,sId);
	}

	@Override
	public List<TutorRecord> findBySIdAndSemester(String sId, String acadyear, String semester) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findBySIdAndSemester(sId,acadyear,semester);
	}

	@Override
	public void updateTeacherByStuIds(String teacherId, String[] updateStuIds,Date modifyTime) {
		tutorRecordDao.updateTeacherByStuIds(teacherId,updateStuIds, modifyTime);
	}
	@Override
	public List<TutorRecord> findByFamIdAndSemester(String id, String acadyear, String semester) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findByFamIdAndSemester(id,acadyear,semester);
	}
	
	@Override
	public List<TutorRecord> findByTIdsAndSemester(String[] teacherIds,
			String acadyear, String semester) {
		return tutorRecordDao.findByTIdsAndSemester(teacherIds,acadyear,semester);
	}

	@Override
	public List<TutorRecord> findByAllSIdAndSemester(String studentId, String acadyear, String semester) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findByAllSIdAndSemester(studentId,acadyear,semester);
	}

	@Override
	public void deleteByDetailed(String tutorRecordDetailedId) {
		// TODO Auto-generated method stub
		tutorRecordDao.deleteByDetailed(tutorRecordDetailedId);
	}

	@Override
	public TutorRecord findByDetailedIdAndStuId(String tutorRecordDetailedId, String sid) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findByDetailedIdAndStuId(tutorRecordDetailedId,sid);
	}

	@Override
	public List<TutorRecord> findByUidAndSemester(String unitId, String acadyear, String semester) {
		// TODO Auto-generated method stub
		return tutorRecordDao.findByUidAndSemester(unitId,acadyear,semester);
	}


}
