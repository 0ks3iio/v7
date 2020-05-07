package net.zdsoft.newstusys.entity;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.newstusys.constants.BaseStudentConstants;

import javax.persistence.*;
import java.util.Date;

/**
 * 
 * @author weixh
 * 2019年2月25日	
 */
@SuppressWarnings("serial")
@Entity
@Table(name="student_abnormalflow")
public class StudentAbnormalFlow extends BaseEntity<String> {
	@Id
    @Column(name="abflowid")
	private String id;
	private String applyId;
	private int businessType;
	private String schid;
	private String stuid;
	private String flowtype;// 异动类型
	private String flowreason;
	private Date flowdate;
	private Date passdate;
	private String passfilecode;
	private String flowsource;
	private String flowto;
	private String flowexplain;
	private String operator;// userId
	private String operateunit;// unitName
	private String flowtypeoption;
	private String currentclassid;// 转出时/转入后的学生班级id
	private String sectionid;
	private String toschoolid;
	private String schopinion;
	private String oldunitivecode;
	private String remark;
	private String acadyear;
	private String semester;
	private long updatestamp;
	
	@Transient
	private String toSchName;
	@Transient
	private String oldSchName;
	@Transient
	private String stuStuCode;
	@Transient
	private String stuInnerCode;
	@Transient
	private String studentName;
	@Transient
	private String idCardNo;
	@Transient
	private String schName;
	@Transient
	private String gradeName;
	@Transient
	private String className;
	@Transient
	private String flowtypeName;
	
	public String fetchCacheEntitName() {
		return "StudentAbnormalFlow";
	}
	
	/**
	 * 转出
	 * @param stu
	 */
	public void leaveToFlow(Student stu) {
		if(stu == null) {
			return;
		}
		this.setApplyId(null);
		this.setBusinessType(BaseStudentConstants.BUSINESSTYPE_ABNORMAL4);
		this.setCurrentclassid(stu.getClassId());
		Date now = new Date();
		this.setFlowsource(stu.getClassId());
		this.setOldunitivecode(stu.getUnitiveCode());
		this.setPassdate(now);
		this.setUpdatestamp(System.currentTimeMillis());
	}
	
	/**
	 * 转入
	 * @param stu 修改前的学生信息
	 */
	public void inToFlow(BaseStudent stu) {
		if(stu == null) {
			return;
		}
		this.setApplyId(null);
		this.setBusinessType(BaseStudentConstants.BUSINESSTYPE_ABNORMAL4);
		Date now = new Date();
		this.setFlowsource(stu.getClassId());
		this.setOldunitivecode(stu.getUnitiveCode());
		this.setPassdate(now);
		this.setUpdatestamp(System.currentTimeMillis());
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public int getBusinessType() {
		return businessType;
	}

	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}

	public String getSchid() {
		return schid;
	}

	public void setSchid(String schid) {
		this.schid = schid;
	}

	public String getStuid() {
		return stuid;
	}

	public void setStuid(String stuid) {
		this.stuid = stuid;
	}

	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public String getFlowreason() {
		return flowreason;
	}

	public void setFlowreason(String flowreason) {
		this.flowreason = flowreason;
	}

	public Date getFlowdate() {
		return flowdate;
	}

	public void setFlowdate(Date flowdate) {
		this.flowdate = flowdate;
	}

	public Date getPassdate() {
		return passdate;
	}

	public void setPassdate(Date passdate) {
		this.passdate = passdate;
	}

	public String getPassfilecode() {
		return passfilecode;
	}

	public void setPassfilecode(String passfilecode) {
		this.passfilecode = passfilecode;
	}

	public String getFlowsource() {
		return flowsource;
	}

	public void setFlowsource(String flowsource) {
		this.flowsource = flowsource;
	}

	public String getFlowto() {
		return flowto;
	}

	public void setFlowto(String flowto) {
		this.flowto = flowto;
	}

	public String getFlowexplain() {
		return flowexplain;
	}

	public void setFlowexplain(String flowexplain) {
		this.flowexplain = flowexplain;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperateunit() {
		return operateunit;
	}

	public void setOperateunit(String operateunit) {
		this.operateunit = operateunit;
	}

	public String getFlowtypeoption() {
		return flowtypeoption;
	}

	public void setFlowtypeoption(String flowtypeoption) {
		this.flowtypeoption = flowtypeoption;
	}

	public String getCurrentclassid() {
		return currentclassid;
	}

	public void setCurrentclassid(String currentclassid) {
		this.currentclassid = currentclassid;
	}

	public String getSectionid() {
		return sectionid;
	}

	public void setSectionid(String sectionid) {
		this.sectionid = sectionid;
	}

	public String getToschoolid() {
		return toschoolid;
	}

	public void setToschoolid(String toschoolid) {
		this.toschoolid = toschoolid;
	}

	public String getSchopinion() {
		return schopinion;
	}

	public void setSchopinion(String schopinion) {
		this.schopinion = schopinion;
	}

	public String getOldunitivecode() {
		return oldunitivecode;
	}

	public void setOldunitivecode(String oldunitivecode) {
		this.oldunitivecode = oldunitivecode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getUpdatestamp() {
		return updatestamp;
	}

	public void setUpdatestamp(long updatestamp) {
		this.updatestamp = updatestamp;
	}

	public String getToSchName() {
		return toSchName;
	}

	public void setToSchName(String toSchName) {
		this.toSchName = toSchName;
	}

	public String getOldSchName() {
		return oldSchName;
	}

	public void setOldSchName(String oldSchName) {
		this.oldSchName = oldSchName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getSchName() {
		return schName;
	}

	public void setSchName(String schName) {
		this.schName = schName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFlowtypeName() {
		return flowtypeName;
	}

	public void setFlowtypeName(String flowtypeName) {
		this.flowtypeName = flowtypeName;
	}

	public String getStuStuCode() {
		return stuStuCode;
	}

	public void setStuStuCode(String stuStuCode) {
		this.stuStuCode = stuStuCode;
	}

	public String getStuInnerCode() {
		return stuInnerCode;
	}

	public void setStuInnerCode(String stuInnerCode) {
		this.stuInnerCode = stuInnerCode;
	}

}
