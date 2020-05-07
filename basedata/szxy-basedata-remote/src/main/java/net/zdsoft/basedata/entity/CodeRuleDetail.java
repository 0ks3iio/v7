package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
/**
 * @author yangsj  2017-1-24下午2:22:48
 */
@Entity
@Table(name="base_code_rule_detail")
public class CodeRuleDetail extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	@ColumnInfo(displayName="规则主表id")
    private String ruleId;
	@ColumnInfo(displayName="数据名称")
    private String dataType; 
	@ColumnInfo(displayName="序列位置")
    private int rulePosition; 
	@ColumnInfo(displayName="位数")
    private int ruleNumber; 
	@ColumnInfo(displayName="默认值")
    private String constant; 
	@ColumnInfo(displayName="备注")
    private String remark; 
	@ColumnInfo(displayName="是否计入流水号")
    private boolean inSerialNumber;
    
    
    @ColumnInfo(displayName="是否软删",disabled = true)
	private Integer isDeleted;
    
    @ColumnInfo(displayName="本系统内",disabled = true)
	private Integer eventSource;
    
    // ===================辅助字段=====================
    @Transient
    private String dataTypeDisplay; // 数据类型名称显示值
    @Transient
    private String length; // 类型长度
    
    @Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "codeRuleDetail";
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getRulePosition() {
		return rulePosition;
	}

	public void setRulePosition(int rulePosition) {
		this.rulePosition = rulePosition;
	}

	public int getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(int ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public String getConstant() {
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isInSerialNumber() {
		return inSerialNumber;
	}

	public void setInSerialNumber(boolean inSerialNumber) {
		this.inSerialNumber = inSerialNumber;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getEventSource() {
		return eventSource;
	}

	public void setEventSource(Integer eventSource) {
		this.eventSource = eventSource;
	}

	public String getDataTypeDisplay() {
		return dataTypeDisplay;
	}

	public void setDataTypeDisplay(String dataTypeDisplay) {
		this.dataTypeDisplay = dataTypeDisplay;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
    
    
}
