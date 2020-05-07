package net.zdsoft.partybuild7.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.PartyMember;

import org.springframework.data.jpa.repository.Query;

public interface PartyMemberDao extends BaseJpaRepositoryDao<PartyMember,String> {


    @Query(nativeQuery = true,value = "SELECT * FROM pb_party_member where id = ?1 ")
    public PartyMember getPartyMemeberById(String id);
    
    @Query(nativeQuery = true,value =" SELECT * FROM pb_party_member where id = ?1 and PARTY_APPLICATION_STATE=3 and org_Id != '00000000000000000000000000000000' ")
    public PartyMember getFormalPartyMemeberById(String id);
    
    @Query("FROM PartyMember WHERE orgId=?1 AND partyMemberState = ?2 AND partyApplicationState IN (?3) ORDER BY partyApplicationState DESC")
    public List<PartyMember> getPartyMemberByOrgIdStates(String orgId, int memberState, int[] applyStates);
    
    @Query("FROM PartyMember WHERE orgId=?1 AND partyMemberState = ?2 AND isDeny=?3 AND partyApplicationState IN (?4) ORDER BY partyApplicationState DESC")
    public List<PartyMember> getPartyMemberListByOrgIdStates(String orgId, int memberState, int isDeny, int[] applyStates);
}
