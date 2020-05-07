package net.zdsoft.careerplan.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 支付订单
 */
@Entity
@Table(name="base_payment_details")
public class PaymentDetails extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//id 为订单号
	
	private String userId;
	private String orderType;
	private String orderName;
	private double commodityAmount;//业务购买金额
	//最终支付宝具体支付金额--生成默认跟commodifyAmount一致
	//最后需要从最终支付信息中获取是否跟实际需要支付的commodifyAmount一致
	private Double orderAmount;//未付款的时候都显示为空
	private String qrCode;//二维码串码
	private String tradeStatus;
	private String msg;//支付中心返回的描述信息
	@Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    private Date invalidTime;//二维码失效时间
    @Transient
    private String userName;


	@Override
	public String fetchCacheEntitName() {
		return "paymentDetails";
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public double getCommodityAmount() {
		return commodityAmount;
	}

	public void setCommodityAmount(double commodityAmount) {
		this.commodityAmount = commodityAmount;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}
	
	
}


