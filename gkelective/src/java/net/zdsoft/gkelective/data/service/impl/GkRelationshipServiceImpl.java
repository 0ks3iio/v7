package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkRelationshipDao;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.service.GkRelationshipService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkRelationshipService")
public class GkRelationshipServiceImpl extends BaseServiceImpl<GkRelationship, String> implements GkRelationshipService {

    @Autowired
    private GkRelationshipDao gkRelationshipDao;

    @Override
    protected BaseJpaRepositoryDao<GkRelationship, String> getJpaDao() {
        return gkRelationshipDao;
    }

    @Override
    protected Class<GkRelationship> getEntityClass() {
        return GkRelationship.class;
    }

    @Override
    public void deleteByPrimaryId(String primaryId,String type) {
        gkRelationshipDao.deleteByPrimaryId(primaryId,type);
    }

    @Override
    public void saveAll(List<GkRelationship> insertRShop) {
        if (CollectionUtils.isNotEmpty(insertRShop)) {
            gkRelationshipDao.saveAll(insertRShop);
        }
    }

    @Override
    public List<GkRelationship> findByTypePrimaryIdIn(String type, String... primaryIds) {

        if (primaryIds != null && primaryIds.length > 0) {
            return gkRelationshipDao.findByTypePrimaryIdIn(type, primaryIds);
        }

        return new ArrayList<GkRelationship>();
    }

	@Override
	public void deleteByPrimaryIds(String[] primaryId) {
		if(primaryId!=null && primaryId.length>0){
			gkRelationshipDao.deleteByPrimaryIds(primaryId);
		}
		
	}

}
