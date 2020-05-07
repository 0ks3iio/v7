package net.zdsoft.remote.openapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_openapi_apply")
public class OpenApiApply extends BaseEntity<String> {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_MAX_NUM_EVERYDAY = 5000;
    public static final int DEFAULT_LIMIT_EVERY_TIME = 1000;
    @Column(name = "developer_id")
    private String developerId;

    private int status;

    private String opinion;
    @Column(name = "audit_time")
    private Date auditTime;
    @Column(name = "creation_time")
    private Date creationTime;
    @Column(name = "modify_time")
    private Date modifyTime;

    private String type;// 结果类型

    private transient String typeNameValue;
    private Integer maxNumDay;  //每天的最大调用次数
    private Integer limitEveryTime;  //获取接口最大数量

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "openapiapply";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeNameValue() {
        return typeNameValue;
    }

    public void setTypeNameValue(String typeNameValue) {
        this.typeNameValue = typeNameValue;
    }

	public Integer getMaxNumDay() {
		return maxNumDay;
	}

	public void setMaxNumDay(Integer maxNumDay) {
		this.maxNumDay = maxNumDay;
	}

	public Integer getLimitEveryTime() {
		return limitEveryTime;
	}

	public void setLimitEveryTime(Integer limitEveryTime) {
		this.limitEveryTime = limitEveryTime;
	}
}
