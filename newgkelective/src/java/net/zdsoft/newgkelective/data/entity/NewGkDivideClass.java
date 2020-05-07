package net.zdsoft.newgkelective.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;

/**
 * 分班结果
 */
@Entity
@Table(name="newgkelective_divide_class")
public class NewGkDivideClass extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String divideId;
	
	private String className;
	
	private String classType;
	
	private String relateId;//关联id---扩展出来多个班级，用于classType=3
	
	private String subjectIds;
	/**
	 * 0时  3+0:3 2+x:2 classType:2时  A,B
	 */
	private String subjectType;
	/**
	 * ABC分层
	 */
	private String bestType;
	
	private Date creationTime;
	
	private Date modifyTime;
	private String isHand;
	// 关联 分班之前  原行政班的id
	private String oldClassId;
	// 由于排课结束时 会复制分班信息，此用来记录原来的 班级id，只在排课结束时使用
	private String oldDivideClassId;
	/**
	 * 来源  1：原始分班结果 2：排课后最终分班结果 divide_id 存放的是array_id
	 */
	private String sourceType;
	/**
	 * 1:7选3或者6选3(物化生历地政技)  2:理科(物化生)3:文科(历地政)4:语数英
	 */
//	private String groupType;
	/**
	 * 1.7选3 批次点
	 * 2.行政班排课 虚拟课程id-classIds ,classIds 为 虚拟课程对应的行政班班级
	 */
	private String batch;
	
	private Integer orderId;
	
	private String parentId;//-----classType=3 这个字段用于关联的主班级
	/**
	 * 在行政班上课的 学考科目
	 */
//	@Transient
	private String subjectIdsB;
	
	@Transient
	private List<String> studentList=new ArrayList<String>();
	@Transient
	private List<NewGkConditionDto> newDtoList=new ArrayList<NewGkConditionDto>();
	@Transient
	private Integer classNum;
	@Transient
	private String placeIds;
	@Transient
	private String placeName;
	@Transient
	private String subNames;  // 跟随行政班上课的科目
	@Transient
	private List<String> floatingSubIds;  // 走班科目
	@Transient
	private String relateName;
	@Transient
	private int girlCount;
	@Transient
	private int boyCount;
	@Transient
	private int studentCount;
	@Transient
	private List<String> batchs;  // 走班时间点
	
	@Transient
	private int notexists=0;//是否不存在学生选择情况与班级不符  0:存在 1:不存在
	
	@Transient
	private String stuIdStr;//学生 以，隔开
	@Transient
	private List<String[]> stuCountByClassId=new ArrayList<>();//来源各个班级的人数
	@Transient
	private List<String> stuNumBySubList=new ArrayList<>();//各组合学生人数
	
	public String getOldClassId() {
		return oldClassId;
	}

	public void setOldClassId(String oldClassId) {
		this.oldClassId = oldClassId;
	}

	public int getGirlCount() {
		return girlCount;
	}

	public void setGirlCount(int girlCount) {
		this.girlCount = girlCount;
	}

	public int getBoyCount() {
		return boyCount;
	}

	public void setBoyCount(int boyCount) {
		this.boyCount = boyCount;
	}

	public int getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	public String getRelateName() {
		return relateName;
	}

	public void setRelateName(String relateName) {
		this.relateName = relateName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceIds() {
		return placeIds;
	}

	public void setPlaceIds(String placeIds) {
		this.placeIds = placeIds;
	}

	public Integer getClassNum() {
		return classNum;
	}

	public void setClassNum(Integer classNum) {
		this.classNum = classNum;
	}

	public List<NewGkConditionDto> getNewDtoList() {
		return newDtoList;
	}

	public void setNewDtoList(List<NewGkConditionDto> newDtoList) {
		this.newDtoList = newDtoList;
	}

	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
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

	public List<String> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<String> studentList) {
		this.studentList = studentList;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkDivideClass";
	}

	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getBestType() {
		return bestType;
	}

	public void setBestType(String bestType) {
		this.bestType = bestType;
	}

	public String getIsHand() {
		return isHand;
	}

	public void setIsHand(String isHand) {
		this.isHand = isHand;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public int getNotexists() {
		return notexists;
	}

	public void setNotexists(int notexists) {
		this.notexists = notexists;
	}

	public String getStuIdStr() {
		return stuIdStr;
	}

	public void setStuIdStr(String stuIdStr) {
		this.stuIdStr = stuIdStr;
	}


	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public List<String[]> getStuCountByClassId() {
		return stuCountByClassId;
	}

	public void setStuCountByClassId(List<String[]> stuCountByClassId) {
		this.stuCountByClassId = stuCountByClassId;
	}

	public String getOldDivideClassId() {
		return oldDivideClassId;
	}

	public void setOldDivideClassId(String oldDivideClassId) {
		this.oldDivideClassId = oldDivideClassId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public List<String> getStuNumBySubList() {
		return stuNumBySubList;
	}

	public void setStuNumBySubList(List<String> stuNumBySubList) {
		this.stuNumBySubList = stuNumBySubList;
	}


	public String getSubNames() {
		return subNames;
	}

	public void setSubNames(String subNames) {
		this.subNames = subNames;
	}

	public List<String> getBatchs() {
		return batchs;
	}

	public void setBatchs(List<String> batchs) {
		this.batchs = batchs;
	}

	public List<String> getFloatingSubIds() {
		return floatingSubIds;
	}

	public void setFloatingSubIds(List<String> floatingSubIds) {
		this.floatingSubIds = floatingSubIds;
	}

	public String getSubjectIdsB() {
		return subjectIdsB;
	}

	public void setSubjectIdsB(String subjectIdsB) {
		this.subjectIdsB = subjectIdsB;
	}
	
	

}
