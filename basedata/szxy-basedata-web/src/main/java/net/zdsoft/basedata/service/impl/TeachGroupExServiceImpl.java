package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.dao.TeachGroupExDao;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("teachGroupExService")
public class TeachGroupExServiceImpl extends BaseServiceImpl<TeachGroupEx, String> implements TeachGroupExService{

	@Autowired
	private TeachGroupExDao teachGroupExDao;
	
	@Override
	protected BaseJpaRepositoryDao<TeachGroupEx, String> getJpaDao() {
		return teachGroupExDao;
	}

	@Override
	protected Class<TeachGroupEx> getEntityClass() {
		return TeachGroupEx.class;
	}

	@Override
	public List<TeachGroupEx> findByTeachGroupId(String[] teachGroupIds) {
		return teachGroupExDao.findByTeachGroupIdIn(teachGroupIds);
	}

    @Override
    public List<TeachGroupEx> findByTeachGroupIdWithMaster(String[] teachGroupIds) {
        return teachGroupExDao.findByTeachGroupIdIn(teachGroupIds);
    }

	@Override
	public List<TeachGroupEx> findByTypeAndTeachGroupIdIn(Integer type, String[] teachGroupIds) {
		return teachGroupExDao.findByTypeAndTeachGroupIdIn(type,teachGroupIds);
	}


	@Override
	public void deleteByTeacherIds(String... teacherIds) {
		 teachGroupExDao.deleteByTeacherIdIn(teacherIds);
	}
}
