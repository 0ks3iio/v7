package net.zdsoft.studevelop.data.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dao.StuDevelopHonorRecordDao;
import net.zdsoft.studevelop.data.entity.StuDevelopHonorRecord;
import net.zdsoft.studevelop.data.service.StuDevelopHonorRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("StuDevelopHonorRecordService")
public class StuDevelopHonorRecordServiceImpl extends BaseServiceImpl<StuDevelopHonorRecord,String> implements StuDevelopHonorRecordService{

	@Autowired
	private StuDevelopHonorRecordDao stuDevelopHonorRecordDao;
	
	@Override
	public List<StuDevelopHonorRecord> getHonorList(String unitId,String acadyear,
			String semester, String stuId) {
		return stuDevelopHonorRecordDao.getHonorList(unitId,acadyear,semester,stuId);
	}

	
	@Override
	public StuDevelopHonorRecord findById(String id) {
		return stuDevelopHonorRecordDao.findById(id).orElse(null);
	}

	
	@Override
	public List<StuDevelopHonorRecord> findListByCls(String unitId,String acadyear,
			String semester, String[] array) {
		return stuDevelopHonorRecordDao.findListByCls(unitId,acadyear,semester,array);
	}
	
	@Override
	public void save(StuDevelopHonorRecord honorRecordXJRW,StuDevelopHonorRecord honorRecordQCYGK) {
		String unitId = "";
		String acadyear = "";
		String semester = "";
		String studentId = "";
		if(honorRecordXJRW.getHonorLevelArray() != null 
				&& honorRecordXJRW.getHonorLevelArray().length > 0) {
			for(String honorLevel : honorRecordXJRW.getHonorLevelArray()) {
				StuDevelopHonorRecord entity = new StuDevelopHonorRecord();
				unitId = honorRecordXJRW.getUnitId();
				acadyear = honorRecordXJRW.getAcadyear();
				semester = honorRecordXJRW.getSemester();
				studentId = honorRecordXJRW.getStudentId();
				entity.setId(UuidUtils.generateUuid());
				entity.setUnitId(honorRecordXJRW.getUnitId());
				entity.setAcadyear(honorRecordXJRW.getAcadyear());
				entity.setSemester(honorRecordXJRW.getSemester());
				entity.setStudentId(honorRecordXJRW.getStudentId());
				entity.setHonorType(honorRecordXJRW.getHonorType());
				entity.setHonorLevel(honorLevel);
				entity.setGiveDate(honorRecordXJRW.getGiveDate());
				entity.setRemark(honorRecordXJRW.getRemark());
				stuDevelopHonorRecordDao.save(entity);
			}
		}
		if(honorRecordQCYGK.getHonorLevelArray() != null 
				&& honorRecordQCYGK.getHonorLevelArray().length > 0) {
			for(String honorLevel : honorRecordQCYGK.getHonorLevelArray()) {
				StuDevelopHonorRecord entity = new StuDevelopHonorRecord();
				unitId = honorRecordQCYGK.getUnitId();
				acadyear = honorRecordQCYGK.getAcadyear();
				semester = honorRecordQCYGK.getSemester();
				studentId = honorRecordQCYGK.getStudentId();
				entity.setId(UuidUtils.generateUuid());
				entity.setUnitId(honorRecordQCYGK.getUnitId());
				entity.setAcadyear(honorRecordQCYGK.getAcadyear());
				entity.setSemester(honorRecordQCYGK.getSemester());
				entity.setStudentId(honorRecordQCYGK.getStudentId());
				entity.setHonorType(honorRecordQCYGK.getHonorType());
				entity.setHonorLevel(honorLevel);
				entity.setGiveDate(honorRecordQCYGK.getGiveDate());
				entity.setRemark(honorRecordQCYGK.getRemark());
				stuDevelopHonorRecordDao.save(entity);
			}
		}
		//当前学期内获得四个或者四个以上的七彩阳光卡的荣誉称号是自动获取一个'全面发展'的荣誉称号
		String unitId1 = unitId;
		String acadyear1 = acadyear;
		String semester1 = semester;
		String studentId1 = studentId;
		String honorType = StuDevelopConstant.HONOR_TYPE_QCYGK;
		String honorLevel = StuDevelopConstant.HONOR_LEVEL_QMFZ;
		List<StuDevelopHonorRecord> list = stuDevelopHonorRecordDao.getStudocHonorRecordList(unitId1,acadyear1,semester1,studentId1,honorType,honorLevel);
		if(list == null || list.size() == 0) {
			int distinctCount = stuDevelopHonorRecordDao.getDistinctCount(acadyear, semester, studentId,honorType);
			if(distinctCount >= 4) {
				StuDevelopHonorRecord entity = new StuDevelopHonorRecord();
				entity.setId(UuidUtils.generateUuid());
				entity.setUnitId(unitId);
				entity.setAcadyear(acadyear);
				entity.setSemester(semester);
				entity.setStudentId(studentId);
				entity.setHonorType(StuDevelopConstant.HONOR_TYPE_QCYGK);
				entity.setHonorLevel(StuDevelopConstant.HONOR_LEVEL_QMFZ);
				Date date = new Date();
				Date nowDate = null;
				try {
					nowDate = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entity.setGiveDate(nowDate);
				entity.setRemark("系统自增");
				stuDevelopHonorRecordDao.save(entity);
			}
		}
		
	}
	
	@Override
	public List<StuDevelopHonorRecord> findAllhonor(String acadyear,
			String semester, String unitId) {
		return stuDevelopHonorRecordDao.findAllhonor(acadyear,semester,unitId);
	}
	
	@Override
	public List<StuDevelopHonorRecord> findByHonortype(String honortype,
			String acadyear, String semester, String unitId) {
		return stuDevelopHonorRecordDao.findByHonortype(honortype,acadyear,semester,unitId);
	}
	

	@Override
	public List<StuDevelopHonorRecord> findByfindBytypeAndclass(String unitId,
			String honortype, String acadyear, String semester, String[] array) {
		return stuDevelopHonorRecordDao.findByfindBytypeAndclass(unitId,honortype,acadyear,semester,array);
	}
	
	@Override
	public void deleteById(String id) {
		StuDevelopHonorRecord developHonorRecord = stuDevelopHonorRecordDao.findById(id).orElse(null);
		stuDevelopHonorRecordDao.deleteById(id);
		String unitId = developHonorRecord.getUnitId();
		String acadyear = developHonorRecord.getAcadyear();
		String semester = developHonorRecord.getSemester();
		String studentId = developHonorRecord.getStudentId();
		String honorType = StuDevelopConstant.HONOR_TYPE_QCYGK;
		String honorLevel = StuDevelopConstant.HONOR_LEVEL_QMFZ;
		List<StuDevelopHonorRecord> list = stuDevelopHonorRecordDao.getStudocHonorRecordList(unitId,acadyear,semester,studentId,honorType,honorLevel);
		if(!list.isEmpty()){
			int distinctCount = stuDevelopHonorRecordDao.getDistinctCount(acadyear,semester,studentId,StuDevelopConstant.HONOR_TYPE_QCYGK);
			if(distinctCount < 5){
				stuDevelopHonorRecordDao.deleteById(list.get(0).getId());
			}
		}
	}
	
	@Override
	protected BaseJpaRepositoryDao<StuDevelopHonorRecord, String> getJpaDao() {
		return stuDevelopHonorRecordDao;
	}

	@Override
	protected Class<StuDevelopHonorRecord> getEntityClass() {
		return StuDevelopHonorRecord.class;
	}

}
