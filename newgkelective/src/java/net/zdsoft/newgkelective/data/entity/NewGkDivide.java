package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *  分班
 */
@Entity
@Table(name = "newgkelective_divide")
public class NewGkDivide extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String unitId;
	private String gradeId;
	private String referScoreId; //参考成绩id
	private String choiceId; //选课id
	private Integer maxGalleryful; //最大增加容纳量
	private Integer galleryful;
	/**
	 *01:7选3模式原行政班不重新分班 （改成全固定模式）
	 *02:7选3模式原行政班重新分班（改成半固定模式）
	 *05:新增（选考单科分层模式）
	 *03:文理科分层教学模式 — 语数外独立分班(重组行政班)
	 *04:文理科分层教学模式 — 语数外跟随文理组合分班(重组行政班)
	 *06:全手动模式
	 *07：行政班排课模式
	 */
	private String openType;
	private Integer times; //次数
	private Integer isDeleted;
	private Date creationTime;
	private Date modifyTime;
	private String divideName;
	private String stat;
	private int isDefault;
	/**
	 * follow_type
	 * 初始化为空
	 * 09：3+1+2 单科分层重组  
	 * 	  B-0：相当于物理历史学考跟选考结果无关，独立走班
	 *    B-1:相当于物理历史学考根据选考结果分层,走班
	 * 
	 * 11：3+1+2单科分层不重组 
	 * 	 A-2:预先的分配组合结果物理历史班，选考按行政班上课  剩余4门走批次
	 * 	 A-1:相当于物理历史学考根据预先的分配组合结果物理历史班分层
	 * 
	 * 	 B-2:预先的分配组合结果物理历史班，学考按行政班上课  剩余4门走批次
	 * 	 B-1:相当于物理历史学考根据预先的分配组合结果物理历史班分层
	 * 	 B-0：相当于物理历史学考跟选考结果无关，独立走班
	 */
	private String followType;
	
	private Integer batchCountTypea; 
	private Integer batchCountTypeb;
	@Transient
	private String gradeName;
	
	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getReferScoreId() {
		return referScoreId;
	}

	public void setReferScoreId(String referScoreId) {
		this.referScoreId = referScoreId;
	}

	public String getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}

	public Integer getMaxGalleryful() {
		return maxGalleryful;
	}

	public void setMaxGalleryful(Integer maxGalleryful) {
		this.maxGalleryful = maxGalleryful;
	}

	public Integer getGalleryful() {
		return galleryful;
	}

	public void setGalleryful(Integer galleryful) {
		this.galleryful = galleryful;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getDivideName() {
		return divideName;
	}

	public void setDivideName(String divideName) {
		this.divideName = divideName;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkDivide";
	}

	public Integer getBatchCountTypea() {
		return batchCountTypea;
	}

	public void setBatchCountTypea(Integer batchCountTypea) {
		this.batchCountTypea = batchCountTypea;
	}

	public Integer getBatchCountTypeb() {
		return batchCountTypeb;
	}

	public void setBatchCountTypeb(Integer batchCountTypeb) {
		this.batchCountTypeb = batchCountTypeb;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getFollowType() {
		return followType;
	}

	public void setFollowType(String followType) {
		this.followType = followType;
	}
	
}
