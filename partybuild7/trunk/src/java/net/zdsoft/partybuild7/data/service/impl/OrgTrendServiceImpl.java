package net.zdsoft.partybuild7.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.dao.OrgTrendDao;
import net.zdsoft.partybuild7.data.entity.OrgTrend;
import net.zdsoft.partybuild7.data.entity.PartyMember;
import net.zdsoft.partybuild7.data.service.OrgTrendService;
import net.zdsoft.partybuild7.data.service.PartyMemberService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OrgTrendService")
public class OrgTrendServiceImpl extends BaseServiceImpl<OrgTrend , String> implements OrgTrendService {
    @Autowired
    private OrgTrendDao orgTrendDao;
    @Autowired
    private PartyMemberService partyMemberService;
    @Override
    public List<OrgTrend> getAllByOrgId(String orgId) {
        return orgTrendDao.getAllByOrgId(orgId);
    }

    @Override
    public OrgTrend getOrgTrendById(String id) {
        return orgTrendDao.getOrgTrendById(id);
    }

    @Override
    public OrgTrend getOrgTrendByOrgId(String orgId) {
        return orgTrendDao.getOrgTrendByOrgId(orgId);
    }

    @Override
    public OrgTrend getOrgTrendByTeacherId(String teacherId) {
        PartyMember partyMemeber = partyMemberService.getPartyMemeberById(teacherId);
        List<OrgTrend> list = orgTrendDao.getAllByOrgId(partyMemeber.getOrgId());
        OrgTrend trend = null;
        if(CollectionUtils.isNotEmpty(list)){
             trend = list.get(0);
        }
        return trend;
    }

    @Override
    protected BaseJpaRepositoryDao<OrgTrend, String> getJpaDao() {
        return orgTrendDao;
    }

    @Override
    protected Class<OrgTrend> getEntityClass() {
        return OrgTrend.class;
    }
}
