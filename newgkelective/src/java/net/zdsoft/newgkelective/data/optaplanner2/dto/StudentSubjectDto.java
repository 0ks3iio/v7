package net.zdsoft.newgkelective.data.optaplanner2.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 
 * 学生选课情况
 */
public class StudentSubjectDto implements Cloneable{

	/**算法需要用到的值开始-----------------------**/
    private String stuId;
    private String stuCode;
    private String stuName;
	private int sex;
	private String classId; 
	private String className;
	
	 /* 所有走班科目 如果分层加上层次ABCD */
    private List<String> allSubjectIds;
    /* 选的3门科目  如果分层加上层次ABCD */
    private Set<String> chooseSubjectIds;
    
    /* 选的3门科目 */
    private Set<String> realChooseSubjectIds; 
    
    
    private Map<String,Double> scoreMap;//成绩key:subjectId   语数英
    private double avgScore;//组合内，单科排序 以选的三门成绩平均分  //预排自动分配：选择三门总分
    // batch,subjectId,teaClsId
    private Set<String> oldSubjectIds;
    
    /**算法需要用到的值结束---------------------**/
    private String groupClassId;//两科所在的班级
	
  
	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}


	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}


	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public List<String> getAllSubjectIds() {
		return allSubjectIds;
	}

	public void setAllSubjectIds(List<String> allSubjectIds) {
		this.allSubjectIds = allSubjectIds;
	}

	public Set<String> getChooseSubjectIds() {
		return chooseSubjectIds;
	}

	public void setChooseSubjectIds(Set<String> chooseSubjectIds) {
		this.chooseSubjectIds = chooseSubjectIds;
	}

//	public Set<Integer> getNoBathSet() {
//		return noBathSet;
//	}
//
//	public void setNoBathSet(Set<Integer> noBathSet) {
//		this.noBathSet = noBathSet;
//	}

	public Set<String> getOldSubjectIds() {
		return oldSubjectIds;
	}

	public void setOldSubjectIds(Set<String> oldSubjectIds) {
		this.oldSubjectIds = oldSubjectIds;
	}

	public Map<String, Double> getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map<String, Double> scoreMap) {
		this.scoreMap = scoreMap;
	}

	public double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}
	
	 public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Set<String> getRealChooseSubjectIds() {
		return realChooseSubjectIds;
	}

	public void setRealChooseSubjectIds(Set<String> realChooseSubjectIds) {
		this.realChooseSubjectIds = realChooseSubjectIds;
	}

	@Override
	public StudentSubjectDto clone() throws CloneNotSupportedException {
		 StudentSubjectDto student = (StudentSubjectDto)super.clone();
    	if (this.allSubjectIds != null) {
    		student.setAllSubjectIds((ArrayList<String>)((ArrayList<String>)allSubjectIds).clone());
    	}
		return student;
	}

	public String getGroupClassId() {
		return groupClassId;
	}

	public void setGroupClassId(String groupClassId) {
		this.groupClassId = groupClassId;
	}
	
}

