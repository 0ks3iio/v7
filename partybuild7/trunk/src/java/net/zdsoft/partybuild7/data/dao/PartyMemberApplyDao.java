package net.zdsoft.partybuild7.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.PartyMemberApply;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PartyMemberApplyDao extends BaseJpaRepositoryDao<PartyMemberApply,String> {


    @Query(nativeQuery = true,value = "select * from pb_member_apply where party_member_id = ?1")
    public PartyMemberApply getPartyMemberApplyByPartyMemberId(String partyMemberId);

    @Modifying
    @Query(nativeQuery = true ,value = "delete from pb_member_apply where party_member_id = ?1 ")
    public void deleteByPartyMemberId(String partyMemberId);

}
