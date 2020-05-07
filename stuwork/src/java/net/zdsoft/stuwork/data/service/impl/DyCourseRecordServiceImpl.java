package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyCourseRecordDao;
import net.zdsoft.stuwork.data.dao.DyCourseStudentRecordDao;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyCourseStudentRecord;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyCourseStudentRecordService;
@Service("dyCourseRecordService")
public class DyCourseRecordServiceImpl extends BaseServiceImpl<DyCourseRecord, String> implements DyCourseRecordService{
    @Autowired
	private DyCourseRecordDao dyCourseRecordDao;
    
    @Autowired
	private DyCourseStudentRecordDao dyCourseStudentRecordDao;
    @Autowired
    private DyCourseStudentRecordService dyCourseStudentRecordService;

	@Override
	protected BaseJpaRepositoryDao<DyCourseRecord, String> getJpaDao() {
		return dyCourseRecordDao;
	}

	@Override
	protected Class<DyCourseRecord> getEntityClass() {
		return DyCourseRecord.class;
	}

	@Override
	public void save(DyCourseRecord dyCourseRecord,
			List<DyCourseStudentRecord> dyCourseStudentRecordList) {
		List<DyCourseRecord> res = dyCourseRecordDao.findByAll(dyCourseRecord.getSchoolId(), dyCourseRecord.getAcadyear(),
				dyCourseRecord.getSemester(), dyCourseRecord.getType(), dyCourseRecord.getTeacherId(),
				dyCourseRecord.getWeek(), dyCourseRecord.getDay(), dyCourseRecord.getPeriod());
		if(StringUtils.isNotEmpty(dyCourseRecord.getId())) {
			for(DyCourseRecord re : res) {
				if(re.getId().equals(dyCourseRecord.getId())) {
					res.remove(re);
					break;
				}
			}
		}
		if(CollectionUtils.isNotEmpty(res)) {
			List<String> rids = EntityUtils.getList(res, DyCourseRecord::getId);
			this.deleteAll(res.toArray(new DyCourseRecord[0]));
			dyCourseStudentRecordDao.deleteByRecordId(rids.toArray(new String[0]));
		}
		if(StringUtils.isBlank(dyCourseRecord.getId())){
			dyCourseRecord.setId(UuidUtils.generateUuid());
		}
		dyCourseRecordDao.save(dyCourseRecord);
		dyCourseStudentRecordDao.deleteByRecordId(dyCourseRecord.getId());
		if(CollectionUtils.isNotEmpty(dyCourseStudentRecordList)){
			for(DyCourseStudentRecord item : dyCourseStudentRecordList){
				item.setId(UuidUtils.generateUuid());
				item.setSchoolId(dyCourseRecord.getSchoolId());
				item.setAcadyear(dyCourseRecord.getAcadyear());
				item.setSemester(dyCourseRecord.getSemester());
				item.setRecordId(dyCourseRecord.getId());
				item.setType(dyCourseRecord.getType());
				item.setTeacherId(dyCourseRecord.getTeacherId());
				item.setWeek(dyCourseRecord.getWeek());
				item.setDay(dyCourseRecord.getDay());
				item.setPeriod(dyCourseRecord.getPeriod());
				item.setRecordDate(dyCourseRecord.getRecordDate());
			}
			dyCourseStudentRecordDao.saveAll(dyCourseStudentRecordList);
		}
	}

	@Override
	public DyCourseRecord findById(String id) {
		return dyCourseRecordDao.findById(id).orElse(null);
	}

	@Override
	public DyCourseRecord findByAll(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day,
			int period) {
		List<DyCourseRecord> res = dyCourseRecordDao.findByAll(schoolId, acadyear, semester, type, teacherId, week, day, period);
		if(CollectionUtils.isNotEmpty(res)) {
			return res.get(0);
		}
		return null;
	}

	@Override
	public DyCourseRecord findBy(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day) {
		return dyCourseRecordDao.findBy(schoolId, acadyear, semester, type, teacherId, week, day);
	}

	@Override
	public List<DyCourseRecord> findListBy(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day) {
		return dyCourseRecordDao.findListBy(schoolId, acadyear, semester, type, teacherId, week, day);
	}
	
	public List<DyCourseRecord> findListByPeriod(String schoolId, String acadyear,
			String semester, String type, int week, int day, int period){
		if(period == 0) {
			return dyCourseRecordDao.findListByDay(schoolId, acadyear, semester, week, day, type);
		}
		return dyCourseRecordDao.findListByPeriod(schoolId, acadyear, semester, week, day, type, period);
	}

	@Override
	public List<DyCourseRecord> findListByRecordDate(String schoolId, String type, Date recordDate) {
		return dyCourseRecordDao.findListByRecordDate(schoolId, type, recordDate);
	}
	
	public List<DyCourseRecord> findListByDateClsIds(String schoolId, Date recordDate, String[] clsIds){
		List<DyCourseStudentRecord> stuRes = dyCourseStudentRecordService.findListBy(DyCourseStudentRecord.class, new String[] {"schoolId","recordDate"}, new Object[] {schoolId, recordDate}, "classId", clsIds, new String[] {"recordId"});
		List<DyCourseRecord> res;
		if(CollectionUtils.isNotEmpty(stuRes)) {
			res = this.findListByIdIn(EntityUtils.getList(stuRes, DyCourseStudentRecord::getRecordId).toArray(new String[0]));
		} else {
			res = new ArrayList<>();
		}
		return res;
	}

	@Override
	public List<DyCourseRecord> findListByRecordClassId(String schoolId,
			String acadyear, String semester, int week, int day, String type, String classId) {
		if(day < 0) {
			return dyCourseRecordDao.findListByRecordClassId(schoolId, acadyear, semester, week, type, classId);
		}else {
			return dyCourseRecordDao.findListByRecordClassId(schoolId, acadyear, semester, week, day, type, classId);
		}
	}
	@Override
	public List<DyCourseRecord> findListByRecordClassIds(String schoolId, String acadyear, String semester, int week,
			String[] classIds) {
		return dyCourseRecordDao.findListByRecordClassIds(schoolId, acadyear, semester, week, classIds);
	}
	@Override
	public void deleteBy(String acadyear, String semester, int week,
			int day, String type, String[] classIds) {
		dyCourseRecordDao.deleteBy(acadyear, semester, week, day, type, classIds);
	}

	@Override
	public void saveNightCourseRecord(List<DyCourseRecord> dyCourseRecordList,
			List<DyCourseStudentRecord> dyCourseStudentRecordList,
			String acadyear, String semester, int week, int day, String type,
			String[] classIds) {
		if (StuworkConstants.COURSERECORD_WZX_TYPE.equals(type)) {
			dyCourseRecordDao.deleteBy(acadyear, semester, week, day, type, classIds);
			List<String> rcIds = EntityUtils.getList(dyCourseRecordList, DyCourseRecord::getRecordClass);
			dyCourseRecordDao.deleteByRecordClass(acadyear, semester, week, day, type, rcIds.toArray(new String[0]));
			dyCourseStudentRecordDao.deleteBy(acadyear, semester, week, day, type, classIds);
		} else {
			List<String> rcIds = EntityUtils.getList(dyCourseRecordList, DyCourseRecord::getId);
			dyCourseStudentRecordDao.deleteByRecordId(rcIds.toArray(new String[0]));
		}
		dyCourseRecordDao.saveAll(dyCourseRecordList);
		dyCourseStudentRecordDao.saveAll(dyCourseStudentRecordList);		
	}



}
