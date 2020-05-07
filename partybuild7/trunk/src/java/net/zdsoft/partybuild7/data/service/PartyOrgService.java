package net.zdsoft.partybuild7.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.partybuild7.data.entity.PartyOrg;

import java.util.List;

public interface PartyOrgService extends BaseService<PartyOrg , String> {

    public List<PartyOrg> getPartyOrgListByIds(String[] ids);
}
