package net.zdsoft.newgkelective.data.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *  分班
 */
@Entity
@Table(name = "newgkelective_divide_ex")
public class NewGkDivideEx extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	
	private String divideId;
	/**
	 * 分层类型
	 */
	private String hierarchyType;
	/**
	 * 单科成绩还是总分
	 */
	private String hierarchyScore;
	/**
	 * A:班级数：总人数;B:班级数：总人数;C:班级数：总人数;
	 */
	private String classSumNum; 
	/**
	 * 1:7选3或者6选3(物化生历地政技)  2:理科(物化生)3:文科(历地政)4:语数英
	 */
	private String groupType;
	private Date creationTime;
	private Date modifyTime;
	
	private String subjectType;//暂时A:代表物化生或者史地政 J:语数英
	
	@Transient
	private String groupName;//组合名称 例如物化生 语数英
	@Transient
	private int studentNum;//选择该组合学生人数
	//解析classSumNum
	@Transient
	Map<String,String[]> classSumNumMap=new HashMap<String,String[]>();
	
	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String getHierarchyType() {
		return hierarchyType;
	}

	public void setHierarchyType(String hierarchyType) {
		this.hierarchyType = hierarchyType;
	}

	public String getHierarchyScore() {
		return hierarchyScore;
	}

	public void setHierarchyScore(String hierarchyScore) {
		this.hierarchyScore = hierarchyScore;
	}

	public String getClassSumNum() {
		return classSumNum;
	}

	public void setClassSumNum(String classSumNum) {
		this.classSumNum = classSumNum;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
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

	@Override
	public String fetchCacheEntitName() {
		return "newGkDivideEx";
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	
	public void makeClassSumNumMap(){
		if(StringUtils.isNotBlank(this.classSumNum)){
			String[] arr = this.classSumNum.split(";");
			this.classSumNumMap=new HashMap<String,String[]>();
			for(String s:arr){
				if(StringUtils.isNotBlank(this.classSumNum)){
					String[] cc=s.split(":");
					this.classSumNumMap.put(cc[0], new String[]{cc[1],cc[2]});
				}
			}
		}else{
			this.classSumNumMap=new HashMap<String,String[]>();
		}
		
	}

	public int getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}

	public Map<String, String[]> getClassSumNumMap() {
		return classSumNumMap;
	}

	public void setClassSumNumMap(Map<String, String[]> classSumNumMap) {
		this.classSumNumMap = classSumNumMap;
	}

}
