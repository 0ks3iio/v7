package net.zdsoft.partybuild7.data.service.impl;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.partybuild7.data.constant.PartyBuildConstant;
import net.zdsoft.partybuild7.data.dao.PartyMemberDao;
import net.zdsoft.partybuild7.data.entity.PartyMember;
import net.zdsoft.partybuild7.data.service.PartyMemberService;
import net.zdsoft.partybuild7.data.service.PartyOrgService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PartyMemberService")
public class PartyMemberServiceImpl extends BaseServiceImpl<PartyMember, String> implements PartyMemberService {
    @Autowired
	private PartyMemberDao partyMemberDao;
    @Autowired
    private PartyOrgService partyOrgService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    
    @Override
    public PartyMember getPartyMemeberById(String id) {
//		PartyMember partyMember = partyMemberDao.getPartyMemeberById(id);
    	return partyMemberDao.getPartyMemeberById(id);
    }
    
    public PartyMember getFormalPartyMemeberById(String id){
    	return partyMemberDao.getFormalPartyMemeberById(id);
    }
    
    public List<PartyMember> getMemberListByState(String orgId, int[] states){
    	List<PartyMember> pms = partyMemberDao.getPartyMemberByOrgIdStates(orgId, PartyBuildConstant.PARTY_MEMBER_STATE_NORMAL, states);
    	dealListStr(pms);
    	return pms;
    }
    
    private void dealListStr(List<PartyMember> pms){
    	List<String> ids = EntityUtils.getList(pms, "id");
    	if(CollectionUtils.isEmpty(ids)){
    		return;
    	}
    	
    	List<Teacher> teas = Teacher.dt(teacherRemoteService.findListByIds(ids.toArray(new String[0])));
    	if (CollectionUtils.isEmpty(teas)) {
			return;
		}
    	Map<String, Teacher> tm = EntityUtils.getMap(teas, "id");
    	Set<String> unitIds = EntityUtils.getSet(teas, "unitId");
    	List<Unit> uns = Unit.dt(unitRemoteService.findListByIds(unitIds
				.toArray(new String[0])));
    	Map<String, String> unm = EntityUtils.getMap(uns, "id", "unitName");
    	Iterator<PartyMember> it = pms.iterator();
    	while(it.hasNext()){
    		PartyMember pm = it.next();
    		Teacher tea = tm.get(pm.getId());
    		if(tea == null){
    			it.remove();
    			break;
    		}
    		if(!unm.containsKey(tea.getUnitId())){
    			it.remove();
    			break;
    		}
    		pm.setTeacherName(tea.getTeacherName());
    		pm.setSex(tea.getSex());
    		pm.setPhone(tea.getMobilePhone());
    		pm.setUnitName(unm.get(tea.getUnitId()));
    	}
    }
    
    public List<PartyMember> getPartyMemberListByOrgIdStates(String orgId, int isdeny, int[] states){
    	List<PartyMember> pms = partyMemberDao.getPartyMemberListByOrgIdStates(orgId, PartyBuildConstant.PARTY_MEMBER_STATE_NORMAL, isdeny, states);
    	dealListStr(pms);
    	return pms;
    }

	@Override
	public List<PartyMember> getPartyMemberListByOrgIdStates(String orgId, int memberState, int isDeny, int[] applyStates) {
//		PartyOrg org = partyOrgService.findOne(orgId);

    	List<PartyMember> partyMemberList = partyMemberDao.getPartyMemberListByOrgIdStates(orgId,memberState,isDeny,applyStates);
		dealListStr(partyMemberList);

		return partyMemberList;
	}

	public void saveMember(String[] ids, int applyState, int isDeny, String remark){
    	List<PartyMember> pms = findListByIds(ids);
    	if(CollectionUtils.isEmpty(pms)){
    		return;
    	}
    	for(PartyMember mem : pms){
    		mem.setPartyApplicationState(applyState);
    		mem.setIsDeny(isDeny);
    		mem.setModifyTime(new Date());
    		dealMemberRemark(mem, applyState, isDeny, remark);
    	}
    	this.saveAll(pms.toArray(new PartyMember[0]));
    }
    
    /**
     * ����������
     * �������ӿ�������Ķ�����ActivistRemark���뵳�����Ĵ���ProRemark��ת���Ĵ���FormalRemark
     * @param pm
     * @param applyState
     * @param isDeny
     * @param remark
     */
    private void dealMemberRemark(PartyMember pm, int applyState, int isDeny, String remark){
    	switch(applyState){
    		case PartyBuildConstant.PARTY_STATE_ACTIVIST :{
				if (isDeny == 0) {
					pm.setActivistRemark(remark);// �������ӿ���ͨ��
				} else {
					pm.setProRemark(remark);// �뵳������ͨ��
				}
				break;
	    	}
	    	case PartyBuildConstant.PARTY_STATE_PROBATIONARY :{
				if (isDeny == 0) {
					pm.setProRemark(remark);// �뵳����ͨ��
				} else {// ת�� ��ͨ��
					pm.setFormalRemark(remark);
				}
				break;
	    	}
	    	case PartyBuildConstant.PARTY_STATE_FORMAL :{
				pm.setFormalRemark(remark);// ת��ͨ��
				break;
	    	}
	    	default : {// ���ַ��ӿ��첻ͨ�����˻�
	    		pm.setActivistRemark(remark);
	    	}
    	}
    }

	@Override
	public List<PartyMember> getPartyMemberByOrgIdStates(String orgId, int memberState, int[] applyStates) {
		return partyMemberDao.getPartyMemberByOrgIdStates(orgId,memberState,applyStates);
	}

	@Override
	protected BaseJpaRepositoryDao<PartyMember, String> getJpaDao() {
		return partyMemberDao;
	}

	@Override
	protected Class<PartyMember> getEntityClass() {
		return PartyMember.class;
	}
}
