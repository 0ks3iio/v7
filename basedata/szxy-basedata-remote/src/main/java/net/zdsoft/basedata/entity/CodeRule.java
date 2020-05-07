package net.zdsoft.basedata.entity;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
/**
 * @author yangsj  2017-1-24下午2:12:50
 */
@Entity
@Table(name="base_code_rule")
public class CodeRule extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "codeRule";
	}
	@ColumnInfo(displayName="学校id")
	private String unitId; 
	@ColumnInfo(displayName="是否自动生成")
    private String automatism; 
	@ColumnInfo(displayName="号码规则类型")
    private int codeType; 
	@ColumnInfo(displayName="学段")
    private int section;
	@ColumnInfo(displayName="是否系统初始化")
    private boolean isSystemInit;
	
    @ColumnInfo(displayName="是否软删",disabled = true)
	private Integer isDeleted;
    
    @ColumnInfo(displayName="本系统内",disabled = true)
	private Integer eventSource;
    
 // ===================辅助字段=====================
    @Transient
    private List<CodeRuleDetail> codeRuleDetails = new ArrayList<CodeRuleDetail>();// 规则明细
    
	public List<CodeRuleDetail> getCodeRuleDetails() {
		return codeRuleDetails;
	}

	public void setCodeRuleDetails(List<CodeRuleDetail> codeRuleDetails) {
		this.codeRuleDetails = codeRuleDetails;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAutomatism() {
		return automatism;
	}

	public void setAutomatism(String automatism) {
		this.automatism = automatism;
	}

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public boolean isSystemInit() {
		return isSystemInit;
	}

	public void setSystemInit(boolean isSystemInit) {
		this.isSystemInit = isSystemInit;
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
    
    
	
}
