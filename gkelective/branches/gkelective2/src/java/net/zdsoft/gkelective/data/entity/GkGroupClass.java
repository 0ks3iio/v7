package net.zdsoft.gkelective.data.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * @author zhouyz 预排组合班级 
 */
@Entity
@Table(name = "gkelective_group_class")
public class GkGroupClass extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String GROUP_TYPE_1="1";//3+0 组合班
	public static final String GROUP_TYPE_2="2";//2+X 组合班
	public static final String GROUP_TYPE_3="3";//0+3 混合班

	private String roundsId;
	private String subjectIds;//组合科目id 以，号分开
	private String groupName;
	private String groupType;// 
	private String classId;//关联行政班id
	private String batch;//主要针对2+x中 2的批次
	
	@Transient
	private String className;//关联行政班名称
	@Transient
	private List<String> stuIdList;
	@Transient
	private int notexists=0;//是否不存在学生选择情况与班级不符  0:存在 1:不存在
	@Transient
	private String teachClassId;
	@Transient
	private int number;//总人数
	@Transient
	private String stuIdStr;//页面保存作用
	@Transient
	private int manNumber;//男人数
	@Transient
	private int womanNumber;//女人数
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getManNumber() {
		return manNumber;
	}

	public void setManNumber(int manNumber) {
		this.manNumber = manNumber;
	}

	public int getWomanNumber() {
		return womanNumber;
	}

	public void setWomanNumber(int womanNumber) {
		this.womanNumber = womanNumber;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gkGroupClass";
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRoundsId() {
		return roundsId;
	}

	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
	}

	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public List<String> getStuIdList() {
		return stuIdList;
	}

	public void setStuIdList(List<String> stuIdList) {
		this.stuIdList = stuIdList;
	}

	public int getNotexists() {
		return notexists;
	}

	public void setNotexists(int notexists) {
		this.notexists = notexists;
	}

	public String getTeachClassId() {
		return teachClassId;
	}

	public void setTeachClassId(String teachClsId) {
		this.teachClassId = teachClsId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStuIdStr() {
		return stuIdStr;
	}

	public void setStuIdStr(String stuIdStr) {
		this.stuIdStr = stuIdStr;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

}
