package net.zdsoft.partybuild7.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.partybuild7.data.constant.PartyBuildConstant;

@SuppressWarnings("serial")
@Entity
@Table(name="pb_party_member")
public class PartyMember extends BaseEntity<String> {

    private Integer pointsRemain; // ʣ�����
    private Integer partyApplicationState;// ��Ա����״̬
    private Integer partyMemberType; // ��Ա��Ա����
    private String orgId;
    private String unitId;
    private Integer partyMemberState;// ��Ա״̬ 1 ������2 �쳣
    private String dues;// ���ѱ�׼
    private String partyMemberCode;
    private String isNeedy;// �Ƿ�������Ա
    private String isFlow;// �Ƿ����ѵ�Ա
    private String duty;// ����ְ��
    private String activistRemark;
    private String proRemark;
    private String formalRemark;
    private Integer isDeny;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    
    @Transient
    private String teacherName;
    @Transient
    private Integer sex;
    @Transient
    private String phone;
    @Transient
    private String orgName;
    @Transient
    private String unitName;

    // ---------------------常量---------------------------------------
    public static final int PARTY_MEMBER_TYPE_TEACHER = 1; // 教师类型
    public static final int PARTY_MEMBER_TYPE_STUDENT = 2; // 学生类型

    public static final int PARTY_INIT_POINTS = 0; // 新增党员默认积分
    /**
     * 撤销申请
     */
//    public static final int PARTY_STATE_CANCEL = -1;


    public static final int PARTY_STATE_NOT = 0; // 非党员（已经申请入党积极分子，但是未批准）
    public static final int PARTY_STATE_ACTIVIST = 1; // 入党积极分子
    /**
     * 预备党员
     */
    public static final int PARTY_STATE_PROBATIONARY = 2;
    public static final int PARTY_STATE_FORMAL = 3; // 党员

    /**
     * 党员状态正常
     */
    public static final int PARTY_MEMBER_STATE_NORMAL = 1;

    /**
     * 党员状态异常
     */
    public static final int PARTY_MEMBER_STATE_ABNORMAL = 2;

    /**
     * 拒绝
     */
    public static final int PARTY_IS_DENY = 1;
    /**
     * 没有拒绝
     */
    public static final int PARTY_IS_NOT_DENY = 0;

    /**
     * 获取显示的审核意见
     * @return
     */
    public String getShowRemark(){
	    switch(partyApplicationState){
		    case PartyBuildConstant.PARTY_STATE_NOT :{
				if (isDeny == 1) {
					return activistRemark;// 积极分子考察 不通过
				}
	    	}	
		    case PartyBuildConstant.PARTY_STATE_ACTIVIST :{
				if (isDeny == 0) {
					return activistRemark;// 积极分子考察通过
				} else {
					return proRemark;// 入党审批不通过
				}
	    	}
	    	case PartyBuildConstant.PARTY_STATE_PROBATIONARY :{
				if (isDeny == 0) {
					return proRemark;// 入党审批通过
				} else {// 转正 不通过
					return formalRemark;
				}
	    	}
	    	case PartyBuildConstant.PARTY_STATE_FORMAL :{
				return formalRemark;// 转正通过
	    	}
		}
	    return activistRemark;
    }
    
    public Integer getPointsRemain() {
        return pointsRemain;
    }

    public void setPointsRemain(Integer pointsRemain) {
        this.pointsRemain = pointsRemain;
    }

    public Integer getPartyApplicationState() {
        return partyApplicationState;
    }

    public void setPartyApplicationState(Integer partyApplicationState) {
        this.partyApplicationState = partyApplicationState;
    }

    public Integer getPartyMemberType() {
        return partyMemberType;
    }

    public void setPartyMemberType(Integer partyMemberType) {
        this.partyMemberType = partyMemberType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Integer getPartyMemberState() {
        return partyMemberState;
    }

    public void setPartyMemberState(Integer partyMemberState) {
        this.partyMemberState = partyMemberState;
    }

    public String getDues() {
        return dues;
    }

    public void setDues(String dues) {
        this.dues = dues;
    }

    public String getPartyMemberCode() {
        return partyMemberCode;
    }

    public void setPartyMemberCode(String partyMemberCode) {
        this.partyMemberCode = partyMemberCode;
    }

    public String getIsNeedy() {
        return isNeedy;
    }

    public void setIsNeedy(String isNeedy) {
        this.isNeedy = isNeedy;
    }

    public String getIsFlow() {
        return isFlow;
    }

    public void setIsFlow(String isFlow) {
        this.isFlow = isFlow;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @Override
    public String fetchCacheEntitName() {
        return "PartyMember";
    }

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getActivistRemark() {
		return activistRemark;
	}

	public void setActivistRemark(String activistRemark) {
		this.activistRemark = activistRemark;
	}

	public String getProRemark() {
		return proRemark;
	}

	public void setProRemark(String proRemark) {
		this.proRemark = proRemark;
	}

	public String getFormalRemark() {
		return formalRemark;
	}

	public void setFormalRemark(String formalRemark) {
		this.formalRemark = formalRemark;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getIsDeny() {
		return isDeny;
	}

	public void setIsDeny(Integer isDeny) {
		this.isDeny = isDeny;
	}
    
}
