package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.CourseScheduleRevertDao;
import net.zdsoft.basedata.entity.CourseScheduleRevert;
import net.zdsoft.basedata.service.CourseScheduleRevertService;
import org.apache.commons.lang.StringUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("CourseScheduleRevertService")
public class CourseScheduleRevertServiceImpl extends BaseServiceImpl<CourseScheduleRevert, String> 
	implements CourseScheduleRevertService {
	@Autowired
	private CourseScheduleRevertDao courseScheduleRevertDao;
	
	@Override
	protected BaseJpaRepositoryDao<CourseScheduleRevert, String> getJpaDao() {
		return courseScheduleRevertDao;
	}

	@Override
	protected Class<CourseScheduleRevert> getEntityClass() {
		return CourseScheduleRevert.class;
	}

	@Override
	public void saveBackup(String unitId, String acadyear, Integer semester, String gradeId,
			List<CourseScheduleRevert> savedList) {
		// 先删除 旧数据
		if(StringUtils.isNotBlank(gradeId))
			courseScheduleRevertDao.deleteByGradeId(unitId,acadyear,semester,gradeId);
		else if(StringUtils.isNotBlank(unitId)){
			courseScheduleRevertDao.deleteByUnitId(unitId,acadyear,semester);
		}
		if(CollectionUtils.isNotEmpty(savedList))
			this.saveAll(savedList.toArray(new CourseScheduleRevert[0]));
	}

	@Override
	public List<CourseScheduleRevert> findByClassIds(String schoolId,String acadyear, Integer semester, String[] classIds) {
		if(StringUtils.isBlank(schoolId)||semester==null||classIds==null||classIds.length==0) {
			return new ArrayList<>();
		}
		
		return courseScheduleRevertDao.findbyClassIds(schoolId,acadyear,semester,classIds);
	}

	@Override
	public boolean checkBackupExists(String unitId, String acadyear, Integer semester, String[] gradeIds) {
		for (String gradeId : gradeIds) {
			Integer inte = courseScheduleRevertDao.checkBackupExists(unitId,acadyear,semester, gradeId);
			if(Objects.equals(1, inte)){
				return true;
			}
		}
		return false;
	}
	
}
