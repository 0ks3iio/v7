package net.zdsoft.newstusys.entity;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.Family;

/**
 * Created by Administrator on 2018/3/1.
 */
public class ExFamily {
    private List<Family> familyTempList;
    private int forAbnormal = 0;
    private String flowtype;
    private Date flowdate;
    private String acadyear;
    private String semester;
    private String flowreason;
    private String flowRemark;

    public StudentAbnormalFlow toFlow(StudentAbnormalFlow flow) {
    	if(flow == null) {
    		flow = new StudentAbnormalFlow();
    	}
    	flow.setFlowdate(flowdate);
    	flow.setFlowtype(flowtype);
    	flow.setAcadyear(acadyear);
    	flow.setSemester(semester);
    	flow.setFlowreason(flowreason);
    	flow.setRemark(flowRemark);
    	return flow;
    }
    
    public List<Family> getFamilyTempList() {
        return familyTempList;
    }

    public void setFamilyTempList(List<Family> familyTempList) {
        this.familyTempList = familyTempList;
    }

	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public Date getFlowdate() {
		return flowdate;
	}

	public void setFlowdate(Date flowdate) {
		this.flowdate = flowdate;
	}

	public int getForAbnormal() {
		return forAbnormal;
	}

	public void setForAbnormal(int forAbnormal) {
		this.forAbnormal = forAbnormal;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getFlowreason() {
		return flowreason;
	}

	public void setFlowreason(String flowreason) {
		this.flowreason = flowreason;
	}

	public String getFlowRemark() {
		return flowRemark;
	}

	public void setFlowRemark(String flowRemark) {
		this.flowRemark = flowRemark;
	}
    
}
