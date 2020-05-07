package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author: panlf
 * @Date: 2019/3/27 18:09
 */
@Entity
@Table(name="newdiathesis_record")
public class DiathesisRecord extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private String unitId;
    private String projectId;
    /**
     * 学生id
     */
    private String stuId;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 审核意见
     */
    private String auditOpinion;
    /**
     * 审核时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditTime;
    /**
     * 审核人
     */
    private String auditor;
    /**
     * 学年
     */
    private String acadyear;
    /**
     * 学期
     */
    private Integer semester;
    
    private String gradeCode;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }
    
    public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	@Override
    public String fetchCacheEntitName() {
        return "diathesisRecord";
    }

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}
	
	
}
