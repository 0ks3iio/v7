package net.zdsoft.partybuild7.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.partybuild7.data.entity.PartyMemberApply;

public interface PartyMemberApplyService extends BaseService<PartyMemberApply , String> {

    /**
     * 保存积极分子信息
     * @param partyMemberApply
     */
    public void savePartyMemberApply(PartyMemberApply partyMemberApply );

    /**
     * 通过partyMemberId 获取到申请积极分子信息
     * @param partyMemberId
     * @return
     */
    public PartyMemberApply getPartyMemberApplyByPartyMemberId(String partyMemberId);

    /**
     * 通过partyMemberId 删除
     * @param partyMemberId
     */
    public void deleteByPartyMemberId(String partyMemberId);
}
