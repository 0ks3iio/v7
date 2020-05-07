package net.zdsoft.studevelop.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuCheckAttendanceDao;
import net.zdsoft.studevelop.data.entity.StuCheckAttendance;
import net.zdsoft.studevelop.data.service.StuCheckAttendanceService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("StuCheckAttendanceService")
public class StuCheckAttendanceServiceImpl extends BaseServiceImpl<StuCheckAttendance,String> implements StuCheckAttendanceService{

	@Autowired
	private StuCheckAttendanceDao stuCheckAttendanceDao;
	
	@Override
	public StuCheckAttendance findBystudentId(String acadyear, String semester,String stuId) {
		List<StuCheckAttendance> css = findListByCls(acadyear, semester, new String[] {stuId});
		if(CollectionUtils.isNotEmpty(css)) {
			return css.get(0);
		}
		return null;
	}
	
	@Override
	public List<StuCheckAttendance> findListByCls(String acadyear,
			String semester, String[] array) {
		return stuCheckAttendanceDao.findListByCls(acadyear,semester,array);
	}
	
	@Override
	public Integer deleteByStuIds(String acadyear, String semester,String[] studentIds) {
		return stuCheckAttendanceDao.deleteByStuIds(acadyear,semester,studentIds);
	}
	
	@Override
	protected BaseJpaRepositoryDao<StuCheckAttendance, String> getJpaDao() {
		return stuCheckAttendanceDao;
	}

	@Override
	protected Class<StuCheckAttendance> getEntityClass() {
		return StuCheckAttendance.class;
	}

}
