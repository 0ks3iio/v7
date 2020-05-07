package net.zdsoft.bigdata.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 框架
 * Created by wangdongdong on 2018/11/27 10:02.
 */
@Entity
@Table(name = "bg_sys_option")
public class Option extends BaseEntity<String> {

	private static final long serialVersionUID = 8049947139096339744L;

	private String name;

    private String code;
    
    private String type;

    private Integer status;
    
    private Integer mobility;

    private Integer orderId;
    
    private String remark;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMobility() {
		return mobility;
	}

	public void setMobility(Integer mobility) {
		this.mobility = mobility;
	}

	public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
    public String fetchCacheEntitName() {
        return "Frame";
    }
}
