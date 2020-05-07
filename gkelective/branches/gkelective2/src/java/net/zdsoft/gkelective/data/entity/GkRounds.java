package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *	轮次表
 */
@Entity
@Table(name="gkelective_rounds")
public class GkRounds extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subjectArrangeId;
	private Integer orderId;//轮次
	private int isDeleted;
	private String openClass;//是否开学考班
	private String openClassType;//是否开设行政班
	private String openTwo;//是否2+x

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	private int step;
	public static final String OPENT_CLASS_1 = "1";//全开
	public static final String OPENT_CLASS_0 = "0";//只开选考科目
	public static final String OPENT_CLASS_TYPE_1 = "1";//走行政班
	public static final String OPENT_CLASS_TYPE_0 = "0";//走教学班
	
	//每班容纳量 范围
	private Integer minNum;
	private Integer maxNum;
	//批次数
	private Integer batchCountA;
	private Integer batchCountB;
	//辅助
	@Transient
	private String copyRoundIds;
	@Transient
	private boolean canDelete;
	@Transient
	private boolean isOpenIng;//是否正在开班计算中
	
	public boolean isOpenIng() {
		return isOpenIng;
	}

	public void setOpenIng(boolean isOpenIng) {
		this.isOpenIng = isOpenIng;
	}

	public String getCopyRoundIds() {
		return copyRoundIds;
	}

	public void setCopyRoundIds(String copyRoundIds) {
		this.copyRoundIds = copyRoundIds;
	}

	public String getSubjectArrangeId() {
		return subjectArrangeId;
	}

	public void setSubjectArrangeId(String subjectArrangeId) {
		this.subjectArrangeId = subjectArrangeId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOpenClass() {
		return openClass;
	}

	public void setOpenClass(String openClass) {
		this.openClass = openClass;
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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gKRounds";
	}

	public String getOpenClassType() {
		return openClassType;
	}

	public void setOpenClassType(String openClassType) {
		this.openClassType = openClassType;
	}

	public Integer getMinNum() {
		return minNum;
	}

	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	public Integer getBatchCountA() {
		return batchCountA;
	}

	public void setBatchCountA(Integer batchCountA) {
		this.batchCountA = batchCountA;
	}

	public Integer getBatchCountB() {
		return batchCountB;
	}

	public void setBatchCountB(Integer batchCountB) {
		this.batchCountB = batchCountB;
	}

	public String getOpenTwo() {
		return openTwo;
	}

	public void setOpenTwo(String openTwo) {
		this.openTwo = openTwo;
	}
	
}
