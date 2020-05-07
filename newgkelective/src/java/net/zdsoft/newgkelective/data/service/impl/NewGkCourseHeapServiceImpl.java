package net.zdsoft.newgkelective.data.service.impl;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkCourseHeapDao;
import net.zdsoft.newgkelective.data.entity.NewGkCourseHeap;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkCourseHeapService;

@Service("newGkCourseHeapService")
public class NewGkCourseHeapServiceImpl extends BaseServiceImpl<NewGkCourseHeap, String> implements NewGkCourseHeapService{

	@Autowired
	private NewGkCourseHeapDao newGkCourseHeapDao;
	@Autowired
	private NewGkArrayService newGkArrayService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkCourseHeap, String> getJpaDao() {
		return newGkCourseHeapDao;
	}

	@Override
	protected Class<NewGkCourseHeap> getEntityClass() {
		return NewGkCourseHeap.class;
	}

	@Override
	public void saveOrDel(String arrayId, List<NewGkCourseHeap> insertList) {
		/**
		 * 修改排课状态
		 */
		newGkArrayService.updateStatById(NewGkElectiveConstant.IF_OTHER_3, arrayId);
		newGkCourseHeapDao.deleteByArrayId(arrayId);
		if(CollectionUtils.isNotEmpty(insertList)){
			newGkCourseHeapDao.saveAll(checkSave(insertList.toArray(new NewGkCourseHeap[]{})));
		}
	}

	@Override
	public List<NewGkCourseHeap> findByArrayId(String arrayId) {
		return newGkCourseHeapDao.findByArrayId(arrayId);
	}

	@Override
	public List<NewGkCourseHeap> findByArrayIdAndSubjectId(String arrayId,
			String subjectId) {
		return newGkCourseHeapDao.findByArrayIdAndSubjectId(arrayId,subjectId);
	}

	@Override
	public NewGkCourseHeap findByArrayIdAndTimetableId(String arrayId,
			String timetibleId) {
		return newGkCourseHeapDao.findByArrayIdAndTimetableId(arrayId,timetibleId);
	}

	@Override
	public List<NewGkCourseHeap> findByArrayIdAndTimetableIdIn(String arrayId,
			String[] array) {
		return newGkCourseHeapDao.findByArrayIdAndTimetableIdIn(arrayId,array);
	}

    @Override
    public void deleteBySubjectIds(String... subIds) {
        newGkCourseHeapDao.deleteBySubjectIdIn(subIds);
    }
}
