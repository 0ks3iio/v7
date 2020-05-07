package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.TeachAreaDao;
import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.service.TeachAreaService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("teachAreaService")
public class TeachAreaServiceImpl extends BaseServiceImpl<TeachArea, String> implements TeachAreaService {

    @Autowired
    private TeachAreaDao teachAreaDao;

    @Override
    protected BaseJpaRepositoryDao<TeachArea, String> getJpaDao() {
        return teachAreaDao;
    }

    @Override
    protected Class<TeachArea> getEntityClass() {
        return TeachArea.class;
    }

    @Override
    public List<TeachArea> findByUnitId(String unitId) {
        return teachAreaDao.findByUnitId(unitId);
    }

    @Override
    public void deleteAllIsDeleted(String... ids) {
        teachAreaDao.updateIsDelete(ids);
    }

    @Override
    public List<TeachArea> saveAllEntitys(TeachArea... teachArea) {
        return teachAreaDao.saveAll(checkSave(teachArea));
    }

	@Override
	public List<TeachArea> findByUnitIdIn(String[] uidList) {
		return teachAreaDao.findByUnitIdIn(uidList);
	}

}
