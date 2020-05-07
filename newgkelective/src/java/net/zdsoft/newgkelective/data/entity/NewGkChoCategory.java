package net.zdsoft.newgkelective.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 选课课程类别
 * @author niuchao
 * @since  2019年1月9日
 */
@Entity
@Table(name = "newgkelective_choice_category")
public class NewGkChoCategory extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String unitId;
	private String choiceId;
	private String categoryType; // 级别
	private String categoryName; // 类别名称
	private Integer maxNum; // 最多选
	private Integer minNum; // 最少选
	private Integer isDeleted;
	private Integer orderId;
	private Date creationTime;
	private Date modifyTime;
	
	@Transient //类别下单个科目
	private List<String> courseList;
	@Transient //类别下组合科目
	private List<List<String>> courseLists;
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Integer getMinNum() {
		return minNum;
	}

	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public List<String> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<String> courseList) {
		this.courseList = courseList;
	}

	public List<List<String>> getCourseLists() {
		return courseLists;
	}

	public void setCourseLists(List<List<String>> courseLists) {
		this.courseLists = courseLists;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkChoCategory";
	}

}
