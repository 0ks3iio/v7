package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableTeacherDao;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableTeacherJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;

@Service("newGkTimetableTeacherService")
public class NewGkTimetableTeacherServiceImpl extends BaseServiceImpl<NewGkTimetableTeacher, String>
	implements NewGkTimetableTeacherService{
	
	@Autowired
	private NewGkTimetableTeacherDao newGkTimetableTeacherDao;
	@Autowired
	private NewGkTimetableTeacherJdbcDao newGkTimetableTeacherJdbcDao;
	@Autowired
	private NewGkArrayService newGkArrayService;

	@Override
	protected BaseJpaRepositoryDao<NewGkTimetableTeacher, String> getJpaDao() {
		return newGkTimetableTeacherDao;
	}

	@Override
	protected Class<NewGkTimetableTeacher> getEntityClass() {
		return NewGkTimetableTeacher.class;
	}
	
	@Override
	public void deleteByTimetableIdIn(String[] timeTableIds) {
		if(timeTableIds==null || timeTableIds.length<=0){
			return;
		}
		newGkTimetableTeacherJdbcDao.deleteByIdInOrTimetableIdIn(null,timeTableIds);
	}

	@Override
	public void saveAllEntity(List<NewGkTimetableTeacher> insertTeacherList) {
		if(CollectionUtils.isEmpty(insertTeacherList)){
			return;
		}
		newGkTimetableTeacherJdbcDao.insertBatch(checkSave(insertTeacherList.toArray(new NewGkTimetableTeacher[]{})));
		
	}

	@Override
	public List<NewGkTimetableTeacher> findByTimetableIds(String[] array) {
		if(array != null && array.length > 0) {
			int start = 0 ;
			int end = 0;
			List<NewGkTimetableTeacher> timeTableTeacherList = new ArrayList<>();
			while(start < array.length) {
				end += 1000;
				if(end > array.length) {
					end = array.length;
				}
				String[] copyOfRange = Arrays.copyOfRange(array, start, end);
				List<NewGkTimetableTeacher> partList = newGkTimetableTeacherDao.findByTimetableIdIn(copyOfRange);
				timeTableTeacherList.addAll(partList);
				start = end;
			}
			
			return timeTableTeacherList;
		}
		return new ArrayList<NewGkTimetableTeacher>();
	}

	@Override
	public void saveOrDel(String arrayId,List<NewGkTimetableTeacher> insertList,
			String[] timetableIds) {
		if(timetableIds!=null && timetableIds.length>0){
			newGkTimetableTeacherJdbcDao.deleteByIdInOrTimetableIdIn(null,timetableIds);
		}
		if(CollectionUtils.isNotEmpty(insertList)){
			newGkTimetableTeacherJdbcDao.insertBatch(checkSave(insertList.toArray(new NewGkTimetableTeacher[]{})));
		}
		/**
		 * 修改
		 */
		newGkArrayService.updateStatById(NewGkElectiveConstant.IF_OTHER_4, arrayId);
	}

	@Override
	public List<NewGkTimetableTeacher> findByTeacherIdTimetableIds(
			String teacherId, String[] timeTableIds) {
		if(timeTableIds!=null && timeTableIds.length>0){
			return newGkTimetableTeacherDao.findByTeacherIdTimetableIds(teacherId,timeTableIds);
		}
		return new ArrayList<NewGkTimetableTeacher>();
	}

	@Override
	public List<NewGkTimetableTeacher> findByTeacherId(String teacherId) {
		return newGkTimetableTeacherDao.findByTeacherId(teacherId);
	}

	@Override
	public NewGkTimetableTeacher findByTimetableId(String timetibleId) {
		return newGkTimetableTeacherDao.findByTimetableId(timetibleId);
	}

	@Override
	public void deleteAndsave(List<String> timetableTeachIdList,
			List<NewGkTimetableTeacher> newGkTimetableTeacherList) {
		newGkTimetableTeacherJdbcDao.deleteByIdInOrTimetableIdIn(timetableTeachIdList.toArray(new String[timetableTeachIdList.size()]),null);
		newGkTimetableTeacherJdbcDao.insertBatch(newGkTimetableTeacherList);
	}

	@Override
	public List<NewGkTimetableTeacher> findByTeacherIds(Set<String> teacherIds, String arrayId) {
		if(CollectionUtils.isEmpty(teacherIds) || StringUtils.isBlank(arrayId)) {
			return new ArrayList<>();
		}
		return newGkTimetableTeacherDao.findByTeacherIds(teacherIds.toArray(new String[0]), arrayId);
	}

	@Override
	public List<String> findTeachersByArrayId(String arrayId) {
		if(StringUtils.isBlank(arrayId))
			return new ArrayList<>();
		
		return newGkTimetableTeacherDao.findTeachersByArrayId(arrayId);
	}

    @Override
    public void deleteByTeacherIds(String... teacherIds) {
        newGkTimetableTeacherDao.deleteByTeacherIdIn(teacherIds);
    }

}
