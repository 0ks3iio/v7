package net.zdsoft.partybuild7.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.partybuild7.data.entity.PartyMember;

import java.util.List;


public interface PartyMemberService extends BaseService<PartyMember, String> {

    public PartyMember getPartyMemeberById(String id);
    
    /**
     * 正式党员
     * @param id
     * @return
     */
    public PartyMember getFormalPartyMemeberById(String id);

    /**
     * 根据组织id  状态 得到列表
     * @param orgId
     * @param memberState
     * @param applyStates
     * @return
     */
    public List<PartyMember> getPartyMemberByOrgIdStates(String orgId, int memberState, int[] applyStates);
    
    public List<PartyMember> getMemberListByState(String orgId, int[] states);
    
    public List<PartyMember> getPartyMemberListByOrgIdStates(String orgId, int isdeny, int[] states);

    public List<PartyMember> getPartyMemberListByOrgIdStates(String orgId, int memberState, int isDeny, int[] applyStates);
    
    /**
     * 审核
     * @param ids 
     * @param applyState 要保存的状态
     * @param isDeny 是否是不通过
     * @param remark 审核意见
     */
    public void saveMember(String[] ids, int applyState, int isDeny, String remark);
}
