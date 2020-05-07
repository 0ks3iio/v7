package net.zdsoft.partybuild7.data.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.partybuild7.data.constant.PartyBuildConstant;
import net.zdsoft.partybuild7.data.dao.PartyMemberApplyDao;
import net.zdsoft.partybuild7.data.entity.PartyMember;
import net.zdsoft.partybuild7.data.entity.PartyMemberApply;
import net.zdsoft.partybuild7.data.service.PartyMemberApplyService;
import net.zdsoft.partybuild7.data.service.PartyMemberService;

@Service("PartyMemberApplyService")
public class PartyMemberApplyServiceImpl extends BaseServiceImpl<PartyMemberApply ,String> implements PartyMemberApplyService {

    @Autowired
    private PartyMemberApplyDao partyMemberApplyDao;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private PartyMemberService partyMemberService;
    @Override
    protected BaseJpaRepositoryDao<PartyMemberApply, String> getJpaDao() {
        return partyMemberApplyDao;
    }

    @Override
    protected Class<PartyMemberApply> getEntityClass() {
        return PartyMemberApply.class;
    }

    @Override
    public void savePartyMemberApply(PartyMemberApply partyMemberApply) {
        if(StringUtils.isEmpty(partyMemberApply.getId())){
            partyMemberApply.setId(UuidUtils.generateUuid());
        }
        save(partyMemberApply);
        String partyMemberId = partyMemberApply.getPartyMemberId();
        PartyMember partyMember = partyMemberService.getPartyMemeberById(partyMemberId);
        Teacher teacher = Teacher.dc(teacherRemoteService.findOneById(partyMemberId));
        if(partyMember == null){
            partyMember = new PartyMember();
        }
        partyMember.setId(partyMemberId);
        partyMember.setOrgId(partyMemberApply.getOrgId());
        partyMember.setPartyMemberType(PartyBuildConstant.PARTY_MEMBER_TYPE_TEACHER);
        partyMember.setPartyApplicationState(PartyBuildConstant.PARTY_STATE_NOT);
        partyMember.setPointsRemain(PartyBuildConstant.PARTY_INIT_POINTS);
        partyMember.setPartyMemberState(PartyBuildConstant.PARTY_MEMBER_STATE_NORMAL);
        partyMember.setIsDeny(PartyBuildConstant.MEMBER_AUDIT_STATE_ING);
        partyMember.setModifyTime(new Date());
        if(teacher != null){
            partyMember.setUnitId(teacher.getUnitId());
        }

        partyMemberService.save(partyMember);
    }

    @Override
    public PartyMemberApply getPartyMemberApplyByPartyMemberId(String partyMemberId) {

        return partyMemberApplyDao.getPartyMemberApplyByPartyMemberId(partyMemberId);
    }

	public void deleteByPartyMemberId(String partyMemberId) {
		// TODO Auto-generated method stub
        partyMemberApplyDao.deleteByPartyMemberId(partyMemberId);
        partyMemberService.delete(partyMemberId);
	}
}
