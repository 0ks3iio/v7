package net.zdsoft.desktop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.desktop.dao.FunctionAreaDao;
import net.zdsoft.desktop.entity.FunctionArea;
import net.zdsoft.desktop.service.FunctionAreaService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service
public class FunctionAreaServiceImpl extends BaseServiceImpl<FunctionArea, String> implements FunctionAreaService {

	@Autowired
	private FunctionAreaDao functionAreaDao;
	
	
	@Override
	protected BaseJpaRepositoryDao<FunctionArea, String> getJpaDao() {
		return functionAreaDao;
	}

	@Override
	protected Class<FunctionArea> getEntityClass() {
		return FunctionArea.class;
	}

	@Override
	public void save(FunctionArea fa) {
		functionAreaDao.save(fa);
	}

	@Override
	public List<FunctionArea> findByTypes(List<String> typeIds) {
		// TODO Auto-generated method stub
		return functionAreaDao.findByTypes(typeIds);
	}

	@Override
	public List<FunctionArea> findByType(String type) {
		// TODO Auto-generated method stub
		return functionAreaDao.findByType(type);
	}

	@Override
	public List<FunctionArea> findByUnitClassAndType(Integer unitClass,
			Integer ownerType) {
		// TODO Auto-generated method stub
		String type =ownerType.toString();
		
		return functionAreaDao.findByUnitClassAndType(unitClass,"%"+type+"%");
	}

    
	
}
