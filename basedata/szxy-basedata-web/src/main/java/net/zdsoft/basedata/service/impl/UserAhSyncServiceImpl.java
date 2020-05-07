package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.UserAhSyncDao;
import net.zdsoft.basedata.entity.UserAhSync;
import net.zdsoft.basedata.service.UserAhSyncService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("userAhSyncService")
public class UserAhSyncServiceImpl extends BaseServiceImpl<UserAhSync, String> implements UserAhSyncService {

    @Autowired 
    private UserAhSyncDao userAhSyncDao;
    
    @Override
    public List<UserAhSync> findByAhUnitId(String ahSyncUnitId) {
    	return userAhSyncDao.findByAhUnitId(ahSyncUnitId);
    }
    @Override
    public List<UserAhSync> findByObjectType(String objcetType) {
    	return userAhSyncDao.findByObjectType(objcetType);
    }
    
    @Override
    public List<UserAhSync> findByObjectTypeAndAhUnitId(String objcetType,String ahUnitId) {
    	return userAhSyncDao.findByObjectTypeAndAhUnitId(objcetType,ahUnitId);
    }
    
    @Override
    public List<UserAhSync> findByAhObjectId(String ahUserId) {
    	return userAhSyncDao.findByAhObjectId(ahUserId);
    }
    
    @Override
    protected BaseJpaRepositoryDao<UserAhSync, String> getJpaDao() {
        return this.userAhSyncDao;
    }
    
    @Override
    public List<UserAhSync> findByObjectId(String objectId) {
    	// TODO Auto-generated method stub
    	return  userAhSyncDao.findByObjectId(objectId);
    }

    @Override
    protected Class<UserAhSync> getEntityClass() {
        return getTClass();
    }

}
