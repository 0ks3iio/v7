package net.zdsoft.eclasscard.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccAttachFolderToDao;
import net.zdsoft.eclasscard.data.entity.EccAttachFolderTo;
import net.zdsoft.eclasscard.data.service.EccAttachFolderToService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("eccAttachFolderToService")
public class EccAttachFolderToServiceImpl extends BaseServiceImpl<EccAttachFolderTo, String> implements EccAttachFolderToService {

	@Autowired
	private EccAttachFolderToDao eccAttachFolderToDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccAttachFolderTo, String> getJpaDao() {
		return eccAttachFolderToDao;
	}

	@Override
	protected Class<EccAttachFolderTo> getEntityClass() {
		return EccAttachFolderTo.class;
	}

	@Override
	public List<EccAttachFolderTo> findByObjIdAndRange(String objectId, int range) {
		return eccAttachFolderToDao.findByObjIdAndRange(objectId,range);
	}

	@Override
	public void deleteByObjAndRange(String objectId, int range) {
		eccAttachFolderToDao.deleteByObjAndRange(objectId,range);
		
	}

	@Override
	public List<EccAttachFolderTo> findByFolderId(String folderId) {
		return eccAttachFolderToDao.findByFolderId(folderId);
	}

	@Override
	public void deleteByFolderAndRange(String folderId, int range) {
		eccAttachFolderToDao.deleteByFolderAndRange(folderId,range);
	}

	@Override
	public List<EccAttachFolderTo> findByObjIdAndRangeAndType(String objectId,
			Integer range, Integer type) {
		return eccAttachFolderToDao.findByObjIdAndRangeAndType(objectId,range,type);
	}


}
