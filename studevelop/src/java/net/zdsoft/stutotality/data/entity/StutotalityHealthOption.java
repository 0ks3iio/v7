package net.zdsoft.stutotality.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * stutotality_health_option
 * 编辑身心项目
 */

@Entity
@Table(name = "stutotality_health_option")
public class StutotalityHealthOption extends BaseEntity<String> {
	private static final long serialVersionUID = 45674987121L;

	private String unitId;
	private String gradeCode; //年级标准
	private String healthId;
	private String healthStandard;
	private Date creationTime;
	private Date modifyTime;
	private String modifyId;
	@Transient
	private String healthName;
	@Transient
	private String result;
	@Transient
	private String lastYearResult;
	@Transient
	private String resultId;
	@Transient
	private String result2;//视力的右结果 专门定制结果

	@Transient
	private String gradeName;

	/**
	 * 排序
	 */
	@Transient
	private Integer orderNumber;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getHealthStandard() {
		return healthStandard;
	}

	public void setHealthStandard(String healthStandard) {
		this.healthStandard = healthStandard;
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

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}


	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getResult2() {
		return result2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	@Override
	public String fetchCacheEntitName() {
		return "StutotalityHealthOption";
	}

	@Override
	public String toString() {
		return "StutotalityHealthOption{" +
				"unitId='" + unitId + '\'' +
				", gradeCode='" + gradeCode + '\'' +
				", healthId='" + healthId + '\'' +
				", healthStandard='" + healthStandard + '\'' +
				", creationTime='" + creationTime + '\'' +
				", modifyTime='" + modifyTime + '\'' +
				", modifyId='" + modifyId + '\'' +
				'}';
	}

	public String getHealthName() {
		return healthName;
	}

	public void setHealthName(String healthName) {
		this.healthName = healthName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public String getLastYearResult() {
		return lastYearResult;
	}

	public void setLastYearResult(String lastYearResult) {
		this.lastYearResult = lastYearResult;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
}
