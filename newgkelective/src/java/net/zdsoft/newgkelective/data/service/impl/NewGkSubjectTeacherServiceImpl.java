package net.zdsoft.newgkelective.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkSubjectTeacherDao;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTeacher;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTeacherService;

@Service("newGkSubjectTeacherService")
public class NewGkSubjectTeacherServiceImpl extends BaseServiceImpl<NewGkSubjectTeacher, String>
	implements NewGkSubjectTeacherService{

	@Autowired
	private NewGkSubjectTeacherDao newGkSubjectTeacherDao;
	@Override
	protected BaseJpaRepositoryDao<NewGkSubjectTeacher, String> getJpaDao() {
		return newGkSubjectTeacherDao;
	}

	@Override
	protected Class<NewGkSubjectTeacher> getEntityClass() {
		return NewGkSubjectTeacher.class;
	}

	@Override
	public List<NewGkSubjectTeacher> findByArrayId(String arrayId) {
		return newGkSubjectTeacherDao.findByArrayId(arrayId);
	}

	@Override
	public void deleteAndSave(String arrayId,
			List<NewGkSubjectTeacher> newGkSubjectTeacherList) {
		newGkSubjectTeacherDao.deleteByArrayId(arrayId);
		newGkSubjectTeacherDao.saveAll(newGkSubjectTeacherList);
	}

	@Override
	public List<NewGkSubjectTeacher> findByArrayIdAndSubjectId(String arrayId,
			String subjectId) {
		return newGkSubjectTeacherDao.findByArrayIdAndSubjectId(arrayId,subjectId);
	}

    @Override
    public void deleteByTeacherIds(String... teacherids) {
        newGkSubjectTeacherDao.deleteByTeacherIdIn(teacherids);
    }

    @Override
    public void deleteBySubjectIds(String... subjectIds) {
        newGkSubjectTeacherDao.deleteBySubjectIdIn(subjectIds);
    }

}
