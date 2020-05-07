package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.stuwork.data.dao.DyCourseStudentRecordDao;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyCourseStudentRecord;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyCourseStudentRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
@Service("dyCourseStudentRecordService")
public class DyCourseStudentRecordServiceImpl extends BaseServiceImpl<DyCourseStudentRecord, String> implements DyCourseStudentRecordService{
    @Autowired
	private DyCourseStudentRecordDao dyCourseStudentRecordDao;
    @Autowired
    private DyCourseRecordService dyCourseRecordService;
	
	@Override
	protected BaseJpaRepositoryDao<DyCourseStudentRecord, String> getJpaDao() {
		return dyCourseStudentRecordDao;
	}

	@Override
	protected Class<DyCourseStudentRecord> getEntityClass() {
		return DyCourseStudentRecord.class;
	}

	@Override
	public List<DyCourseStudentRecord> findListByRecordId(String recordId) {
		return dyCourseStudentRecordDao.findListByRecordId(recordId);
	}


	@Override
	public List<DyCourseStudentRecord> findListByDate(final String schoolId, final String classId, final Date queryDate, final String period, final String teacherId) {
		Specification<DyCourseStudentRecord> specification = new Specification<DyCourseStudentRecord>(){
			@Override
			public Predicate toPredicate(Root<DyCourseStudentRecord> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("schoolId").as(String.class), schoolId));
				ps.add(cb.equal(root.get("type").as(String.class), "1"));
				ps.add(cb.equal(root.get("queryDate").as(String.class), queryDate));
				if (!Validators.isEmpty(classId)) {
                    ps.add(cb.like(root.get("classId").as(String.class), classId));
                }
				if (!Validators.isEmpty(period)) {
                    ps.add(cb.like(root.get("period").as(String.class), period));
                }
				if (!Validators.isEmpty(teacherId)) {
                    ps.add(cb.like(root.get("teacherId").as(String.class), teacherId));
                }
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		List<DyCourseStudentRecord> dyCourseStudentRecordList = findAll(specification);
		return dyCourseStudentRecordList;
	}

	@Override
	public List<DyCourseStudentRecord> findListByRecordIds(String[] recordIds) {
		return dyCourseStudentRecordDao.findListByRecordIds(recordIds);
	}

	@Override
	public void deleteByRecordId(String recordId) {
		dyCourseStudentRecordDao.deleteByRecordId(recordId);
	}

	@Override
	public void deleteBy(String acadyear, String semester, int week, int day,
			String type, String[] classIds) {
		dyCourseStudentRecordDao.deleteBy(acadyear, semester, week, day, type, classIds);		
	}

	@Override
	public List<DyCourseStudentRecord> findListByClassId(String schoolId,
			String acadyear, String semester, int week, int day, String classId) {
		return dyCourseStudentRecordDao.findListByClassId(schoolId, acadyear, semester, week, day, classId);
	}

	public List<DyCourseStudentRecord> findByWeekAndInClassId(String unitId,
			String acadyear, String semester, int week, String[] classIds){
		return dyCourseStudentRecordDao.findByWeekAndInClassId(unitId,acadyear,semester,week,classIds);
	}
	
	@Override
	public Map<String, Float> findByClassIdAndWeek(String unitId,
			String acadyear, String semester, String[] classIds, int week) {
		// TODO
		Map<String,Float> classScoreMap = new HashMap<String, Float>();
		List<DyCourseStudentRecord> stuRecords = dyCourseStudentRecordDao.findByWeekAndInClassId(unitId,acadyear,semester,week,classIds);
		Set<String> recordIds = EntityUtils.getSet(stuRecords, DyCourseStudentRecord::getRecordId);
//		Map<String,DyCourseRecord> recordMap = dyCourseRecordService.findByIdInMap(recordIds.toArray(new String[0]));
		List<DyCourseRecord> records  =dyCourseRecordService.findListByIdIn(recordIds.toArray(new String[0]));
		for(DyCourseRecord re : records){
			if(!classScoreMap.containsKey(re.getClassId()+","+re.getType()+","+re.getDay())){
				classScoreMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 0f);
			}
//			if(recordMap.containsKey(stuRe.getRecordId())){
				classScoreMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 
						classScoreMap.get(re.getClassId()+","+re.getType()+","+re.getDay()) +re.getScore());
//			}
		}
		return classScoreMap;
	}

	@Override
	public Map<String, String> findByClassIdAndWeekRemark(String unitId,
			String acadyear, String semester, String[] classIds, int week) {
		// TODO
		Map<String,String> classScoreMap = new HashMap<String, String>();
		List<DyCourseStudentRecord> stuRecords = dyCourseStudentRecordDao.findByWeekAndInClassId(unitId,acadyear,semester,week,classIds);
		Set<String> recordIds = EntityUtils.getSet(stuRecords, DyCourseStudentRecord::getRecordId);
//				Map<String,DyCourseRecord> recordMap = dyCourseRecordService.findByIdInMap(recordIds.toArray(new String[0]));
		List<DyCourseRecord> records  =dyCourseRecordService.findListByIdIn(recordIds.toArray(new String[0]));
		for(DyCourseRecord re : records){
			if(!classScoreMap.containsKey(re.getClassId()+","+re.getType()+","+re.getDay())){
				classScoreMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), "");
			}else{		
				if(null!=re.getRemark()){					
					classScoreMap.put(re.getClassId()+","+re.getType()+","+re.getDay(),classScoreMap.get(re.getClassId()+","+re.getType()+","+re.getDay()) +re.getRemark()+",");
				}
			}
		}
		return classScoreMap;
	}

}
