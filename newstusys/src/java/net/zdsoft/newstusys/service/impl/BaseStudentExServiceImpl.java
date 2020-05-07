package net.zdsoft.newstusys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newstusys.dao.BaseStudentExDao;
import net.zdsoft.newstusys.entity.BaseStudentEx;
import net.zdsoft.newstusys.service.BaseStudentExService;

/**
 * 
 * @author weixh
 * @since 2018年3月2日 下午5:42:47
 */
@Service("baseStudentExService")
public class BaseStudentExServiceImpl extends BaseServiceImpl<BaseStudentEx, String> implements BaseStudentExService {
	@Autowired
	private BaseStudentExDao baseStudentExDao;
	
	@Override
	protected BaseJpaRepositoryDao<BaseStudentEx, String> getJpaDao() {
		return baseStudentExDao;
	}

	@Override
	protected Class<BaseStudentEx> getEntityClass() {
		return BaseStudentEx.class;
	}

	public void deleteByStuIds(String... ids) {
		baseStudentExDao.deleteByStuIds(ids);
	}
}
