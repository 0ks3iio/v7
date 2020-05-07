package net.zdsoft.newstusys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newstusys.dao.StusysColsDisplayDao;
import net.zdsoft.newstusys.entity.StusysColsDisplay;
import net.zdsoft.newstusys.service.StusysColsDisplayService;

@Service("stusysColsDisplayService")
public class StusysColsDisplayServiceImpl extends BaseServiceImpl<StusysColsDisplay, String>
		implements StusysColsDisplayService {
	@Autowired
	private StusysColsDisplayDao stusysColsDisplayDao;
	
	protected BaseJpaRepositoryDao<StusysColsDisplay, String> getJpaDao() {
		return stusysColsDisplayDao;
	}

	@Override
	protected Class<StusysColsDisplay> getEntityClass() {
		return StusysColsDisplay.class;
	}
	
	public List<StusysColsDisplay> findByUnitIdType(String unitId, String type){
		return stusysColsDisplayDao.findByUnitIdType(unitId, type);
	}

	public void deleteByUnitIdType(String unitId, String type) {
		stusysColsDisplayDao.deleteByUnitIdType(unitId, type);
	}
}
