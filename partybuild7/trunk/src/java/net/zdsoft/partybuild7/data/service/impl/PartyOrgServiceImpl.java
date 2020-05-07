package net.zdsoft.partybuild7.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.dao.PartyOrgDao;
import net.zdsoft.partybuild7.data.entity.PartyOrg;
import net.zdsoft.partybuild7.data.service.PartyOrgService;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("PartyOrgService")
public class PartyOrgServiceImpl extends BaseServiceImpl<PartyOrg, String> implements PartyOrgService {
    @Autowired
    private PartyOrgDao partyOrgDao;
    @Override
    protected BaseJpaRepositoryDao<PartyOrg, String> getJpaDao() {
        return partyOrgDao;
    }

    @Override
    protected Class<PartyOrg> getEntityClass() {
        return PartyOrg.class;
    }

    @Override
    public List<PartyOrg> getPartyOrgListByIds(String[] ids) {
        if(ArrayUtils.isEmpty(ids)) {
        	return new ArrayList<PartyOrg>();
        }
    	return partyOrgDao.getAllByIds(ids);
    }
}
