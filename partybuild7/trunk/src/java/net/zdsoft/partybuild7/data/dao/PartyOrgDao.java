package net.zdsoft.partybuild7.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.PartyOrg;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartyOrgDao extends BaseJpaRepositoryDao<PartyOrg , String> {

    @Query("From PartyOrg where id in ?1 ")
    public List<PartyOrg> getAllByIds(String[] ids);
}
