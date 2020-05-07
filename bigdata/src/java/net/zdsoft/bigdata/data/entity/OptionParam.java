package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 框架
 * Created by wangdongdong on 2018/11/27 10:02.
 */
@Entity
@Table(name = "bg_sys_option_param")
public class OptionParam extends BaseEntity<String> {

	private static final long serialVersionUID = 5536956761371149719L;

	private String paramName;

    private String paramKey;

    private String paramValue;

    private String optionCode;

    private Integer orderId;

    private String remark;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getOptionCode() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
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
        return "FrameParam";
    }
}
