package net.zdsoft.studevelop.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuDevelopCateGoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stuDevelopCateGoryService")
public class StuDevelopCateGoryServiceImpl extends BaseServiceImpl<StuDevelopCateGory,String> implements StuDevelopCateGoryService{
    @Autowired
	private StuDevelopCateGoryDao stuDevelopCateGoryDao;
	@Override
	protected BaseJpaRepositoryDao<StuDevelopCateGory, String> getJpaDao() {
		return stuDevelopCateGoryDao;
	}

	@Override
	protected Class<StuDevelopCateGory> getEntityClass() {
		return StuDevelopCateGory.class;
	}

	@Override
	public List<StuDevelopCateGory> findListBySubjectId(String subjectId) {
		return stuDevelopCateGoryDao.findListBySubjectId(subjectId);
	}

	@Override
	public List<StuDevelopCateGory> findListBySubjectIdIn(String[] subjectIds) {
		return stuDevelopCateGoryDao.findListBySubjectIdIn(subjectIds);
	}

}
