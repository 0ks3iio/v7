package net.zdsoft.careerplan.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;
/**
 *  支付宝的业务金额设置
 */
@Entity
@Table(name="base_trade_info")
public class TradeInfo extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tradeName;
	private String tradeType;
	private Integer isDeleted;
	private double amountMoney;
	private double primeMoney; 
	private String remark;
	@Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    
    private String operator;//存姓名
	
	@Override
	public String fetchCacheEntitName() {
		return "tradeInfo";
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public double getAmountMoney() {
		return amountMoney;
	}

	public void setAmountMoney(double amountMoney) {
		this.amountMoney = amountMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public double getPrimeMoney() {
		return primeMoney;
	}

	public void setPrimeMoney(double primeMoney) {
		this.primeMoney = primeMoney;
	}
	
	
}

