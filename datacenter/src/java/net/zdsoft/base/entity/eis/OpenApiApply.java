package net.zdsoft.base.entity.eis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.base.entity.base.DataCenterBaseEntity;

@Entity
@Table(name = "eis_openapi_apply")
public class OpenApiApply extends DataCenterBaseEntity {
    private static final long serialVersionUID = 1L;
    @Override
    public String fetchCacheEntitName() {
        return "openapiapply";
    }
    public static final int DEFAULT_MAX_NUM_EVERYDAY = 5000;
    public static final int DEFAULT_LIMIT_EVERY_TIME = 1000;
    public static final int IS_LIMIT_TRUE = 0;
    public static final int IS_LIMIT_FALSE = -1;
    private int status;

    @Column(name = "audit_time")
    private Date auditTime;
    private String type;// 结果类型
    @Column(length = 32)
    private String interfaceId;
    private Integer fastFail;//是否快速失败
    private transient String typeNameValue;
    private Integer maxNumDay;  //每天的最大调用次数
    private Integer limitEveryTime;  //获取接口最大数量
    private Integer isLimit;  //是否走每天限制调用

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
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

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public Integer getFastFail() {
		return fastFail;
	}

	public void setFastFail(Integer fastFail) {
		this.fastFail = fastFail;
	}
	

	public Integer getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(Integer isLimit) {
		this.isLimit = isLimit;
	}
}
