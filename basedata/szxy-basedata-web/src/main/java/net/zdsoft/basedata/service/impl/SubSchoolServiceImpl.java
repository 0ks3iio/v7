package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SubSchoolDao;
import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.service.SubSchoolService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("subSchoolService")
public class SubSchoolServiceImpl extends BaseServiceImpl<SubSchool, String> implements SubSchoolService {

    @Autowired
    private SubSchoolDao subSchoolDao;

    @Override
    protected BaseJpaRepositoryDao<SubSchool, String> getJpaDao() {
        return subSchoolDao;
    }

    @Override
    protected Class<SubSchool> getEntityClass() {
        return SubSchool.class;
    }

    @Override
    public List<SubSchool> saveAllEntitys(SubSchool... subSchool) {
        return subSchoolDao.saveAll(checkSave(subSchool));
    }

	@Override
	public List<SubSchool> findbySchoolIdIn(String... unitId) {
		return subSchoolDao.findbySchoolIdIn(unitId);
	}

}
