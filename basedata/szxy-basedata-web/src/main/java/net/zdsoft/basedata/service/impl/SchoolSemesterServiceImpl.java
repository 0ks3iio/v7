package net.zdsoft.basedata.service.impl;

import java.util.List;

import net.zdsoft.basedata.dao.SchoolSemesterDao;
import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.basedata.service.SchoolSemesterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("schoolSemesterService")
public class SchoolSemesterServiceImpl extends BaseServiceImpl<SchoolSemester, String> implements SchoolSemesterService {

	@Autowired
	private SchoolSemesterDao schoolSemesterDao;
	
	@Override
	protected BaseJpaRepositoryDao<SchoolSemester, String> getJpaDao() {
		return schoolSemesterDao;
	}

	@Override
	protected Class<SchoolSemester> getEntityClass() {
		return SchoolSemester.class;
	}

	@Override
	public List<SchoolSemester> findBySchoolIdIn(String[] schoolIds) {
		return schoolSemesterDao.findBySchoolIdIn(schoolIds);
	}
}
