package net.zdsoft.bigdata.system.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.system.dao.BgHelpDao;
import net.zdsoft.bigdata.system.entity.BgHelp;
import net.zdsoft.bigdata.system.service.BgHelpService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgHelpService")
public class BgHelpServiceImpl  extends BaseServiceImpl<BgHelp, String> implements BgHelpService{

	@Autowired
	private BgHelpDao bgHelpDao;
	@Override
	protected BaseJpaRepositoryDao<BgHelp, String> getJpaDao() {
		return bgHelpDao;
	}

	@Override
	protected Class<BgHelp> getEntityClass() {
		return BgHelp.class;
	}

	@Override
	public List<BgHelp> findHelpListByCore(Integer core) {
		return bgHelpDao.findHelpListByCore(core);
	}

	@Override
	public List<BgHelp> findAllHelpList() {
		return bgHelpDao.findAllHelpList();
	}
	
	@Override
	public List<BgHelp> findHelpListByModuleId(String moduleId){
		return bgHelpDao.findHelpListByModuleId(moduleId);
	}

	@Override
	public Integer getMaxOrderId() {
		return bgHelpDao.getMaxOrderId();
	}

	
}
